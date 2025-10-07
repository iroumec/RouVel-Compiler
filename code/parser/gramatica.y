// ====================================================================================================================
// INICIO DE DECLARACIONES
// ====================================================================================================================

// ********************************************************************************************************************
// Package e Importaciones
// ********************************************************************************************************************

%{
    package parser;

    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
%}

// ********************************************************************************************************************
// Declaraciones de Tipos de Valores
// ********************************************************************************************************************

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

// ********************************************************************************************************************
// Declaración de Tokens y de sus Tipos
// ********************************************************************************************************************

// No terminales que guardan un String.
%type <sval> expresion, termino, factor, termino_simple, factor_simple, expresion_flexible, termino_flexible,
            operador_suma, operador_multiplicacion,
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

// ====================================================================================================================
// FIN DE DECLARACIONES
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE REGLAS
// ====================================================================================================================

programa
    : ID cuerpo_programa
        { notifyDetection("Programa."); }

    // |========================= REGLAS DE ERROR =========================| //

    | ID conjunto_sentencias
        { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
    | cuerpo_programa_flexible
        { notifyError("El programa requiere de un nombre."); }
    | error ID
        { notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
    | error EOF
        { notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_programa
    : '{' conjunto_sentencias '}'
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_programa_flexible
    : cuerpo_programa

    // |========================= REGLAS DE ERROR =========================| //

    | '{' lista_llaves
        { notifyError("Se encontraron múltiples llaves al final del programa"); }

    | '{' '}'
        { notifyError("El programa no posee ninguna sentencia."); }
    | // lambda //
        { notifyError("El programa no posee ningún cuerpo."); }
    | '{' error '}'
        { notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_llaves
    : '}' '}'
    | lista_llaves '}'
    ;

// --------------------------------------------------------------------------------------------------------------------
                
conjunto_sentencias
    : sentencia 
    | conjunto_sentencias sentencia 

    // |========================= REGLAS DE ERROR =========================| //

    | error punto_sincronizacion_sentencia
        { notifyError("Error capturado a nivel de sentencia."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

punto_sincronizacion_sentencia
    : ';'
    | '}'
    | token_inicio_sentencia
    | EOF
    ;

// --------------------------------------------------------------------------------------------------------------------

token_inicio_sentencia
    : ID
    | IF
    | UINT
    | PRINT
    | DO
    | RETURN
    | WHILE
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia
    : sentencia_ejecutable 
    | sentencia_declarativa
    ;

// ********************************************************************************************************************
// Sentencias declarativas
// ********************************************************************************************************************

sentencia_declarativa
    : declaracion_variable
    | declaracion_funcion punto_y_coma_opcional
    ;

// --------------------------------------------------------------------------------------------------------------------

punto_y_coma_opcional
    : // épsilon
    | ';'
    ;

// ********************************************************************************************************************
// Sentencias ejecutables
// ********************************************************************************************************************

cuerpo_ejecutable
    : sentencia_ejecutable
    | bloque_ejecutable
    ;

// --------------------------------------------------------------------------------------------------------------------

bloque_ejecutable
    : '{' conjunto_sentencias_ejecutables '}'

    // |========================= REGLAS DE ERROR =========================| //

    | '{' '}'
        { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

conjunto_sentencias_ejecutables
    : sentencia_ejecutable
    | conjunto_sentencias_ejecutables sentencia_ejecutable
    | error sentencia_ejecutable
        { notifyError("Error capturado en sentencia ejecutable"); }
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia_ejecutable
    : invocacion_funcion ';' // Contexto en el que es invocada en línea.
    | invocacion_funcion error
        { notifyError("La invocación a función debe terminar con ';'."); }
    | asignacion_simple
    | asignacion_multiple
    | sentencia_control
    | sentencia_retorno
    | impresion
    | lambda
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia_control
    : if                                                                
    | do_while                                                
    ;

// ********************************************************************************************************************
// Declaración de Variables
// ********************************************************************************************************************

declaracion_variable
    : UINT lista_variables ';'
    { notifyDetection("Declaración de variables."); }
    
    // |========================= REGLAS DE ERROR =========================| //

    | UINT ID error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT lista_variables error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT error
        {
            notifyError("Declaración de variables inválida.");
        }
    | UINT variable DASIG constante ';'
        {
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_variables 
    : ID ',' ID
    | lista_variables ',' ID
        { $$ = $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | lista_variables ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
            { $$ = $2; }
        }
    | ID ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
            { $$ = $2; }
        }
    ;

// ********************************************************************************************************************
// Asignación Simple
// ********************************************************************************************************************

asignacion_simple
    : variable DASIG expresion ';'                              
        { notifyDetection("Asignación simple."); }

    // |========================= REGLAS DE ERROR =========================| //

    | variable DASIG expresion_o_termino error
        { notifyError("Las asignaciones simples deben terminar con ';'."); }
        
    | variable error expresion_flexible ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }

    | variable expresion_flexible ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
    ;

expresion_o_termino
    : expresion_flexible
    ;

// ********************************************************************************************************************
// Asignación Múltiple
// ********************************************************************************************************************

//Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17).
asignacion_multiple 
    : inicio_par_variable_constante ';'
        { notifyDetection("Asignación múltiple."); }
    | inicio_par_variable_constante ',' lista_constantes ';'
        { notifyDetection("Asignación múltiple."); }

    // |========================= REGLAS DE ERROR =========================| //

    | inicio_par_variable_constante error
        { notifyDetection("La asignación múltiple debe terminar con ';'."); }
    | inicio_par_variable_constante ',' lista_constantes error
        { notifyDetection("La asignación múltiple debe terminar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

inicio_par_variable_constante
    : variable par_variable_constante constante 
    ;

// --------------------------------------------------------------------------------------------------------------------

par_variable_constante
    : ',' variable par_variable_constante constante ','
    | '='
    ;

// --------------------------------------------------------------------------------------------------------------------
                                
lista_constantes
    : constante
    | lista_constantes ',' constante
        { $$ = $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | lista_constantes constante
        {
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
        }
    ;

// ********************************************************************************************************************
// Expresiones
// ********************************************************************************************************************

expresion
    : termino
    | expresion operador_suma termino
    ;

// --------------------------------------------------------------------------------------------------------------------

expresion_flexible
    : termino_flexible
    | expresion_flexible operador_suma termino_flexible

    // |========================= REGLAS DE ERROR =========================| //

    | expresion_flexible operador_suma error
        {
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                $1, $2)
            );
        }
    | expresion_flexible termino_simple
        {
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                $1, $2)
            );
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

operador_suma                   
    : '+'
        { $$ = "+"; }
    | '-'
        { $$ = "-"; }
    ;

// --------------------------------------------------------------------------------------------------------------------

termino                         
    : termino operador_multiplicacion factor
    | factor
    ;

// --------------------------------------------------------------------------------------------------------------------

termino_flexible
    : termino_flexible operador_multiplicacion error
    | factor

    // |========================= REGLAS DE ERROR =========================| //

    | termino_flexible operador_multiplicacion error
        {
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                $1, $2)
            );
        }
    ;


// --------------------------------------------------------------------------------------------------------------------

termino_simple
    : termino_simple operador_multiplicacion factor
    | factor_simple
    | termino_simple operador_multiplicacion error
        {
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                $1, $2)
            );
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

operador_multiplicacion
    : '/'
        { $$ = "/"; }
    | '*'
        { $$ = "*"; }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Factor que contempla la posibilidad de constantes negativas.
factor
    : variable
    | constante
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

// Factor que no contempla la posibilidad de constantes negativas.
factor_simple
    : variable
    | CTE
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

constante
    : CTE
    | '-' CTE
        { $$ = "-" + $2; }
    ;

// --------------------------------------------------------------------------------------------------------------------

variable
    : ID
    | ID '.' ID
        { $$ = $1 + "." + $3; }
    ;

// ********************************************************************************************************************
// Condición
// ********************************************************************************************************************

condicion
    : condicion_admisible

    // |========================= REGLAS DE ERROR =========================| //
    
    | cuerpo_condicion ')'
        { notifyError("Falta apertura de paréntesis en condición."); }

    | '(' ')'
        { notifyError("La condición no puede estar vacía."); }

    | cuerpo_condicion error
        { notifyError("La condición debe ir entre paréntesis."); }

    | '(' cuerpo_condicion error
        { notifyError("Falta cierre de paréntesis en condición"); }

    ; 

// --------------------------------------------------------------------------------------------------------------------

condicion_admisible
    : '(' cuerpo_condicion ')'
    ;
    
// --------------------------------------------------------------------------------------------------------------------

cuerpo_condicion                
    : expresion comparador expresion

    // |========================= REGLAS DE ERROR =========================| //

    | expresion
        { notifyError("Falta de comparador en comparación."); }
    ;

// --------------------------------------------------------------------------------------------------------------------
                                
comparador                      
    : '>'
    | '<'
    | EQ
    | LEQ
    | GEQ
    | NEQ

    // |========================= REGLAS DE ERROR =========================| //

    | '='
        { notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
    ;

// ********************************************************************************************************************
// Sentencia IF
// ********************************************************************************************************************

if
    : IF condicion cuerpo_ejecutable rama_else ENDIF ';'
        { notifyDetection("Sentencia IF."); }

    // |========================= REGLAS DE ERROR =========================| //

    | IF condicion cuerpo_ejecutable rama_else ENDIF error
        { notifyError("La sentencia IF debe terminar con ';'."); }
    | IF condicion cuerpo_ejecutable rama_else ';'
        { notifyError("La sentencia IF debe finalizar con 'endif'."); }
    | IF condicion cuerpo_ejecutable rama_else error
        { notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
    | IF error
        { notifyError("Sentencia IF inválida."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

rama_else
    : // lambda //
    | ELSE cuerpo_ejecutable
    ;

// ********************************************************************************************************************
// Sentencia WHILE
// ********************************************************************************************************************

do_while                        
    : DO cuerpo_do_admisible ';'
        { notifyDetection("Sentencia 'do-while'."); }  
    
    // |========================= REGLAS DE ERROR =========================| //

    | DO cuerpo_do error
        { notifyError("La sentencia 'do-while' debe terminar con ';'."); }
    | DO error
        { notifyError("Sentencia 'do-while' inválida."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_do                       
    : cuerpo_do_admisible

    // |========================= REGLAS DE ERROR =========================| //

    | fin_cuerpo_do
        { notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
    | cuerpo_ejecutable condicion
        { notifyError("Falta 'while'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_do_admisible
    : cuerpo_ejecutable fin_cuerpo_do
    ;

// --------------------------------------------------------------------------------------------------------------------

fin_cuerpo_do
    : WHILE condicion
    ;

// ********************************************************************************************************************
// Declaración de Función
// ********************************************************************************************************************

declaracion_funcion
    : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible
        { notifyDetection("Declaración de función."); }

    // |========================= REGLAS DE ERROR =========================| //

    | UINT '(' conjunto_parametros ')' cuerpo_funcion
        { notifyError("La función requiere de un nombre."); }

    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_funcion
    : cuerpo_funcion_admisible

    // |========================= REGLAS DE ERROR =========================| //

    | '{' '}'
        { notifyError("El cuerpo de la función no puede estar vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_funcion_admisible
    : '{' conjunto_sentencias '}'
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia_retorno
    : RETURN '(' expresion ')' ';'
        { notifyDetection("Sentencia 'return'."); }
    
    // |========================= REGLAS DE ERROR =========================| //

    | RETURN '(' expresion ')' error
        { notifyError("La sentencia 'return' debe terminar con ';'."); }
    | RETURN '(' ')' ';'
        { notifyError("El retorno no puede estar vacío."); }
    | RETURN expresion ';'
        { notifyError("El resultado a retornar debe ir entre paréntesis."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

conjunto_parametros
    : lista_parametros

    // |========================= REGLAS DE ERROR =========================| //

    | // lambda //
        { notifyError("Toda función debe recibir al menos un parámetro."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_parametros
    : parametro_formal 
    | lista_parametros ',' parametro_formal 

    // |========================= REGLAS DE ERROR =========================| //

    | parametro_vacio
        { notifyError("Se halló un parámetro formal vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

parametro_vacio
    : lista_parametros ','
    | ',' parametro_formal
    ;

// --------------------------------------------------------------------------------------------------------------------

parametro_formal
    : semantica_pasaje UINT variable

    // |========================= REGLAS DE ERROR =========================| //

    | semantica_pasaje UINT 
        { notifyError("Falta de nombre de parámetro formal en declaración de función."); }
    | semantica_pasaje variable
        { notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
    ; 

// --------------------------------------------------------------------------------------------------------------------

semantica_pasaje
    : // lambda //
    | CVR

    // |========================= REGLAS DE ERROR =========================| //

    | error
        { notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto."); }
    ;

// ********************************************************************************************************************
// Invocación de Función
// ********************************************************************************************************************

invocacion_funcion
    : ID '(' lista_argumentos ')' 
        {
            notifyDetection("Invocación de función."); 
            $$ = $1 + '(' + $3 + ')';
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_argumentos
    : argumento
    | lista_argumentos ',' argumento
        { $$ = $3; }
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento
    : expresion FLECHA ID
        { $$ = $1 + $2 + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | expresion
        { notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
    ;

// ********************************************************************************************************************
// Impresión
// ********************************************************************************************************************

impresion
    : PRINT imprimible_admisible ';'
        { notifyDetection("Sentencia 'print'."); }

    // |========================= REGLAS DE ERROR =========================| //

    | PRINT imprimible error
        { notifyError("La sentencia 'print' debe finalizar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

imprimible
    : imprimible_admisible

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("La sentencia 'print' requiere de al menos un argumento."); }

    | elemento_imprimible
        { notifyError("El imprimible debe encerrarse entre paréntesis."); }
    | // épsilon
        { notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Casos de imprimibles que identifican correctamente a la sentencia.
imprimible_admisible
    : '(' elemento_imprimible ')'
    ;

// --------------------------------------------------------------------------------------------------------------------

elemento_imprimible
    : STR
    | expresion
    ;

// ********************************************************************************************************************
// Expresiones Lambda
// ********************************************************************************************************************

lambda
    : parametro_lambda bloque_ejecutable argumento_lambda_admisible ';'
        { notifyDetection("Expresión lambda."); }

    // |========================= REGLAS DE ERROR =========================| //

    | parametro_lambda bloque_ejecutable argumento_lambda error
        { notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento_lambda
    : argumento_lambda_admisible

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }

    | factor
        { notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }

    | // épsilon
        { notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento_lambda_admisible
    : '(' factor ')'
    ;

// --------------------------------------------------------------------------------------------------------------------

parametro_lambda
    : '(' UINT ID ')'
    ;

// ====================================================================================================================
// FIN DE REGLAS  
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

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

// --------------------------------------------------------------------------------------------------------------------

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

// --------------------------------------------------------------------------------------------------------------------

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

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

// --------------------------------------------------------------------------------------------------------------------

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

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
 // Se ejecuta cada vez que encuentra un token error.
public void yyerror(String s) {
    /*
    // 'yylval' contiene el valor del token que el parser no pudo procesar.
    String errorMessage;
    if (yylval != null) {
        errorMessage = String.format(
            "Error de sintaxis: token inesperado '%s'.",
            yylval.sval
        );
    } else {
        errorMessage = "Error de sintaxis: se encontró un token inesperado.";
    }

    Printer.printBetweenSeparations(errorMessage);*/
}

// --------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// --------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// --------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// --------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// --------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================