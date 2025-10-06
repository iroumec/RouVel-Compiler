// ============================================================================================================================================================
// INICIO DE DECLARACIONES
// ============================================================================================================================================================

// ************************************************************************************************************************************************************
// Package e Importaciones
// ************************************************************************************************************************************************************

%{
    package parser;

    // Importaciones.
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
%}

// ************************************************************************************************************************************************************
// Declaraciones de Tipos de Valores
// ************************************************************************************************************************************************************

/*
    Declaración de los tipos de valores. Se le informa a Yacc que tendrá que
    trabajar con valores de tipo String.
    
    Esto es para que no dé error el highlighter de la gramática,
    ya que está pensado para byacc para C.
    
    Al generar el Parser, debe comentarse para no romper el código .java,
    ya que inserta un typedef en el código.
*/
%union {
    String sval;
}

// ************************************************************************************************************************************************************
// Declaración de Tokens y de sus Tipos
// ************************************************************************************************************************************************************

// No terminales que guardan un String.
%type <sval> expresion, termino, factor
            lista_variables, lista_constantes, variable, constante,
            invocacion_funcion, lista_argumentos, argumento

// Asignación de tipo a token y no-terminales.
// Esto es necesario para ejecutar acciones semánticas como: "$$ = $3".
// Al declarar que es de tipo sval, ya no es necesario especificar ".sval" junto a los $n.
%token <sval> ID, CTE, STR  // Los tokens ID, CTE y STR tendrán un valor de tipo String (accedido vía .sval)

// Tokens sin valor semántico asociado (no necesitan tipo).
%token <sval> EQ, GEQ, LEQ, NEQ, DASIG, FLECHA
%token PRINT, IF, ELSE, ENDIF, UINT, CVR, DO, WHILE, RETURN

// Token de fin de archivo.
%token EOF 0

// ************************************************************************************************************************************************************
// Declaración de Precedencias (Menor a Mayor)
// ************************************************************************************************************************************************************

// Se define la asociatividad y el nivel de precedencia de los operadores.
// El orden es de MENOR a MAYOR precedencia.

%left '+' '-'
%left '*' '/'

// "UMINUS" es un alias para la regla del menos unario.
// Al declararse al final, tiene la precedencia MÁS ALTA.
%right UMINUS

// ============================================================================================================================================================
// FIN DE DECLARACIONES
// ============================================================================================================================================================

%%

// ============================================================================================================================================================
// INICIO DE REGLAS
// ============================================================================================================================================================

programa
    : ID cuerpo_programa EOF
        { notifyDetection("Programa."); }
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | ID conjunto_sentencias EOF
        { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
    | cuerpo_programa
        { notifyError("El programa requiere de un nombre."); }
    | error
        {
            notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
            descartarTokensHasta(ID); // Se descartan todos los tokens hasta ID.
        }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_programa
    : '{' conjunto_sentencias '}'
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | '{' '}'
        { notifyError("El programa no posee ninguna sentencia."); }
    | // lambda //
        { notifyError("El programa no posee ningún cuerpo."); }
    | '{' error '}'
        { notifyError("Sentencia inválida en el lenguaje."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                
conjunto_sentencias
    : sentencia 
    | conjunto_sentencias sentencia 
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | conjunto_sentencias error punto_sincronizacion_sentencia
        { notifyError("Sentencia inválida en el lenguaje."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

punto_sincronizacion_sentencia
    : ';'
    | '}'
    | token_inicio_sentencia
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

token_inicio_sentencia
    : ID
    | IF
    | UINT
    | PRINT
    | DO
    | RETURN
    | WHILE
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @LevantaError: "Sentencia inválida en el lenguaje."
sentencia
    : sentencia_ejecutable 
    | sentencia_declarativa
    ;

// ************************************************************************************************************************************************************
// Sentencias declarativas
// ************************************************************************************************************************************************************

sentencia_declarativa
    : declaracion_variable ';'
        { notifyDetection("Declaración de variable."); }
    | declaracion_funcion punto_y_coma_opcional
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

punto_y_coma_opcional
    : // épsilon
    | ';'
    ;

// ************************************************************************************************************************************************************
// Variables
// ************************************************************************************************************************************************************

declaracion_variable
    : UINT lista_variables ';'
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | UINT error ';'
        { notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
    | UINT lista_variables error ';'
        { notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

lista_variables 
    : ID
    | lista_variables ',' ID
        { $$ = $3; }
    // Le dice a Yacc que, cuando reduzca usando esta regla, el valor de la nueva lista_variables
    // que coloque en la pila no sea el de la lista vieja ($1, por defecto siempre hace $$ = $1),
    // sino el valor del ID que acaba de leer ($3)
    // Esto es útil para mostrar errores correctamente. Si no se pone, en una sentencia como:
    // uint A, B C
    // Diría que la coma falta entre A y C.
    // ============================= //
    // PATRONES DE ERROR ESPECÍFICOS //
    // ============================= //
    | lista_variables ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
        }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

/*
Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17).

No es posible chequear utilizando únicamente las reglas que el lado derecho pueda tener más elementos
que el izquierdo. Se requería un autómata de Turing. Por eso, se utilizan acciones semánticas.

Los elementos del lado derecho sólo pueden ser constantes.
*/
asignacion_multiple
    : lista_variables '=' lista_constantes
        { notifyDetection("Asignación múltiple."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                                
lista_constantes
    : constante
    | lista_constantes ',' constante
        { $$ = $3; }
    // ============================= //
    // PATRONES DE ERROR ESPECÍFICOS //
    // ============================= //
    | lista_constantes constante
        {
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
        }
    ;

constante 
    : CTE 
    | '-' CTE %prec UMINUS
        { $$ = '-' + $2; }
    ;

// ************************************************************************************************************************************************************
// Expresiones Lambda
// ************************************************************************************************************************************************************

// El factor representa al argumento.
lambda
    : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'
        { notifyDetection("Expresión lambda."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Separado por legibilidad y para contemplar los casos de error.
parametro_lambda
    : UINT ID
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | // épsilon
        { notifyError("La expresión lambda requiere de un parámetro."); }
    ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias ejecutables                                                                               */
/* ---------------------------------------------------------------------------------------------------- */

cuerpo_ejecutable
    : sentencia_ejecutable
    | bloque_ejecutable
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

bloque_ejecutable
    : '{' conjunto_sentencias_ejecutables '}'
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | '{' '}'
        { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @InterceptaError: "Toda sentencia ejecutable debe terminar con punto y coma."
conjunto_sentencias_ejecutables
    : sentencia_ejecutable
    | conjunto_sentencias_ejecutables sentencia_ejecutable
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @LevantaError: "Toda sentencia ejecutable debe terminar con punto y coma."
sentencia_ejecutable
    : operacion_ejecutable ';'
    //| operacion_ejecutable '}' // Captura sentencias al final del cuerpo del programa.
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operacion_ejecutable
    : invocacion_funcion
    | asignacion_simple
    | asignacion_multiple
    | sentencia_control
    | sentencia_retorno
    | impresion
    | lambda
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

asignacion_simple
    : variable DASIG expresion                                          
        { notifyDetection("Asignación simple."); }
    // ============================= //
    // PATRONES DE ERROR ESPECÍFICOS //
    // ============================= //
    | variable error expresion
        { notifyError("Error en asignación. Se esperaba un ':='."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

sentencia_control
    : if                                                                
    | do_while                                                
    ;

// ************************************************************************************************************************************************************
// Condición
// ************************************************************************************************************************************************************

/*
    En caso de no recibir un paréntesis de cierre, el fin de la condición será inválida, por lo que se levantará
    un error que deberán interceptar las sentencias que usen el no terminal.

    Especificar una regla "'(' cuerpo_condicion", produce shift/reduce.
*/
// @LevantaError: "Falta cierre de paréntesis en condición."
condicion
    : '(' cuerpo_condicion ')'
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | cuerpo_condicion ')'
        { notifyError("Falta apertura de paréntesis en condición."); }
    | '(' ')'
        { notifyError("La condición no puede estar vacía."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_condicion                
    : expresion comparador expresion
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | expresion
        { notifyError("Falta de comparador en comparación."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                                
comparador                      
    : '>'
    | '<'
    | EQ
    | LEQ
    | GEQ
    | NEQ
    // ============================= //
    // PATRONES DE ERROR ESPECÍFICOS //
    // ============================= //
    | '='
    { notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
    ;

// ************************************************************************************************************************************************************
// Sentencia IF
// ************************************************************************************************************************************************************

// @InterceptaError: "Falta cierre de paréntesis en condición."
if
    : IF condicion cuerpo_ejecutable rama_else ENDIF
        { notifyDetection("Sentencia IF."); }
    // ======================= //
    // INTERCEPCIÓN DE ERRORES //
    // ======================= //
    | IF error cuerpo_ejecutable rama_else ENDIF
        { notifyError("Sentencia IF inválida en el lenguaje. Falta cierre de paréntesis en condición."); }
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | IF condicion cuerpo_ejecutable rama_else
        { notifyError("La sentencia IF debe finalizarse con 'endif'."); }
    | IF error
        { notifyError("Sentencia IF inválida en el lenguaje."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

rama_else
    : // lambda //
    | ELSE cuerpo_ejecutable
    ;

// ************************************************************************************************************************************************************
// Sentencia WHILE
// ************************************************************************************************************************************************************

do_while                        
    : DO cuerpo_do
    // ======================= //
    // INTERCEPCIÓN DE ERRORES //
    // ======================= //
    | DO error
        { notifyError("Sentencia DO-WHILE inválida."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @TrasladaError: "Falta cierre de paréntesis en condición."
cuerpo_do                       
    : cuerpo_ejecutable fin_cuerpo_do
        { notifyDetection("Sentencia DO-WHILE."); }  
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | fin_cuerpo_do
        { notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
    | cuerpo_ejecutable condicion
        { notifyError("Falta 'while'."); }
    // ======================= //
    // INTERCEPCIÓN DE ERRORES //
    // ======================= //
    | cuerpo_ejecutable WHILE error
        { notifyError("Falta cierre de paréntesis en condición."); }
    | error fin_cuerpo_do
        { notifyError("Ha habido un error en el cierre del cuerpo ejecutable."); }
    ;

fin_cuerpo_do
    : WHILE condicion
    ;

// ************************************************************************************************************************************************************
// Sentencia PRINT
// ************************************************************************************************************************************************************

impresion
    : PRINT '(' imprimible ')'
    // =============== //
    // REGLAS DE ERROR //
    // =============== //    
    | PRINT '(' ')'
        { notifyError("La sentencia 'print' requiere de al menos un argumento."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

imprimible
    : STR
        { notifyDetection("Impresión de cadena."); }
    | expresion
        { notifyDetection("Impresión de expresión."); }
    ;

// ************************************************************************************************************************************************************
// Expresiones
// ************************************************************************************************************************************************************

expresion
    : expresion operador_suma termino
    | termino

    // =============== //
    // REGLAS DE ERROR //
    // =============== //

    // Error: Falta de operando al final (ej. "5 +")
    | expresion operador_suma error
        { 
            notifyError("Falta de operando en expresión."); 
            // $$ no se asigna o se le da un valor por defecto
        }

    // Error: Falta de operador entre operandos (ej. "5 4")
    | expresion termino
        {
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                $1, $2)
            );
           //  $$ = $2; // Se podría optar por continuar con el último término
        }

    // Error: Falta de primer operando ( + 7)
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operador_suma                   
    : '+'
    | '-'
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

termino                         
    : termino operador_multiplicacion factor
    | factor
        { $$ = $1; }
    /*| termino operador_multiplicacion error
        { notifyError("Falta de operando en expresión."); }*/
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operador_multiplicacion
    : '/'
    | '*'
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

factor
    : variable
        { $$ = $1; }
    | constante
        { $$ = $1; }
    | invocacion_funcion
        { $$ = $1; }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

variable
    : ID
    | ID '.' ID
        { $$ = $1 + "." + $3; }
    ;

// ************************************************************************************************************************************************************
// Declaración de Función
// ************************************************************************************************************************************************************

declaracion_funcion
    : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'
        { notifyDetection("Declaración de función."); }
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | UINT '(' conjunto_parametros ')' '{' cuerpo_funcion '}'
        { notifyError("Falta de nombre en la función."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_funcion
    : conjunto_sentencias
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | // lambda //
        { notifyError("El cuerpo de la función no puede estar vacío."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

sentencia_retorno
    : RETURN '(' expresion ')'
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

conjunto_parametros
    : lista_parametros
    // =============== //
    // REGLAS DE ERROR //
    // =============== //
    | // lambda //
        { notifyError("Toda función debe recibir al menos un parámetro."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

lista_parametros
    : parametro_formal 
    | lista_parametros ',' parametro_formal 
    // --------------- //
    // REGLAS DE ERROR //
    // --------------- //
    | parametro_vacio
        { notifyError("Se halló un parámetro formal vacío."); }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

parametro_vacio
    : lista_parametros ','
    | ',' parametro_formal
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

parametro_formal
    : semantica_pasaje UINT variable
    | semantica_pasaje UINT 
        { notifyError("Falta de nombre de parámetro formal en declaración de función."); }
    | semantica_pasaje variable
        { notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
    ; 

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

semantica_pasaje
    : // lambda //
    | CVR
    | error
        {
            notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
            descartarTokenError();
        }
    ;

// ************************************************************************************************************************************************************
// Invocación de Función
// ************************************************************************************************************************************************************

invocacion_funcion
    : ID '(' lista_argumentos ')' 
        {
            notifyDetection("Invocación de función."); 
            $$ = $1 + '(' + $3 + ')';
        }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

lista_argumentos
    : argumento
    | lista_argumentos ',' argumento
        { $$ = $3; }
    ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

argumento
    : expresion FLECHA ID
        { $$ = $1 + $2 + $3; }
    // --------------- //
    // REGLAS DE ERROR //
    // --------------- //
    | expresion
        { notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
    ;

// ============================================================================================================================================================
// FIN DE REGLAS  
// ============================================================================================================================================================

%%

// ============================================================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ============================================================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    this.readAgain = false;
    
    // Descomentar la siguiente línea para activar el debugging.
    yydebug = true;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

Token getAppropiateToken() {

    Token token;
    // Se lee nuevamente el último token.
    // Útil para recuperar el token de sincronización en reglas de error.
    if (this.readAgain) {
        token = lexer.getCurrentToken();
        this.readAgain = false;
    } else {
        token = lexer.getNextToken();
    }

    return token;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

public void descartarTokensHasta(int tokenEsperado) {

    int t = yylex();
    
    // Se pide un token mientras no se halle el token esperado
    // o el final de archivo.
    while (t != tokenEsperado && t != EOF) {
        t = yylex();
    }

    if (t == EOF) {
        Printer.printBetweenSeparations("SE LLEGÓ AL FINAL DEL ARCHIVO SIN ENCONTRAR UN TOKEN DE SINCRONIZACION.");
    }

    // Se actualizá que se halló el token deseado o se llegó al final del archivo.
    yychar = t;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
public void yyerror(String s) {

    Printer.print("Ocurre un error para el cual la gramática no estaba preparada.");
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ============================================================================================================================================================
// FIN DE CÓDIGO
// ============================================================================================================================================================