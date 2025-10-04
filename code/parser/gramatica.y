// ============================================================================================================================================================
// INICIO DE DECLARACIONES
// ============================================================================================================================================================

// Lo siguiente se coloca previo a la signatura de la clase.
%{
    package parser;

    // Importaciones.
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
%}

// Declaración de los tipos de valores.
// Se le informa a Yacc que tendrá que trabajar con valores de tipo String.
// Esto es para que no dé error el highlighter de la gramática, ya que está pensado para byacc para C.
// Al generar el Parser, debe comentarse para no romper el código .java, ya que genera un typedef (C).
%union {
    String sval;
}

// No terminales que guardan un String.
%type <sval> lista_variables, lista_constantes, variable, constante, expresion, termino,
            factor, invocacion_funcion, lista_argumentos, argumento, secuencia_sin_operador

// Asignación de tipo a token y no-terminales.
// Esto es necesario para ejecutar acciones semánticas como: "$$ = $3".
%token <sval> ID, CTE, STR  // Los tokens ID, CTE y STR tendrán un valor de tipo String (accedido vía .sval)

// Tokens sin valor semántico asociado (no necesitan tipo).
%token <sval> EQ, GEQ, LEQ, NEQ, DASIG, FLECHA
%token PRINT, IF, ELSE, ENDIF, UINT, CVR, DO, WHILE, RETURN

// ============================================================================================================================================================
// FIN DE DECLARACIONES
// ============================================================================================================================================================

%%

/*
EXPLICACIÓN DEL TOKEN DE ERROR

Definición: token especial que representa cualquier cosa que en ese punto no cumpla ninguna de las alternativas válidas.

Si se define el error con un token de sincronización, por ejemplo, "error '}'", el parser consumirá todos los tokens habidos, incluido '}'
antes de reducir por la regla. Lo mismo sucede si se usa un no-terminal, por ejemplo, "error sentencia". No hay forma de recuperar el token o
no terminal consumido por el error, por lo que no puede usarse para detectar luego otra regla. Esto es muy importante ya que complica el manejo
de errores.

En caso de que el token a recuperarse quiera volver a leerse, se proporciona un método que puede ser invocado con dicho propósito: "readLastTokenAgain()".
ESTE SOLO ES VÁLIDO PARA LA LECTURA REPETIDA DE TOKENS. NO SIRVE PARA NO-TERMINALES.

De producirse un error, el error sube hasta que una regla superior lo intercepta. En tal caso de que una regla levante un error que deba ser interceptada
por una regla de orden superior, se usa la notación "@LevantaError" para hacer esto explícito y claro. Estas reglas se van a caracterizar por ser un
posible punto de fallo y no permitir, debido a la aparición de conflictos, la posibilidad de vacío (lambda). Si la regla de un orden superior no intercepta
el error, este pasara a la regla de dos órdenes superiores. Y así sucesivamente hasta que una regla intercepte el error o el parser se detenga.

De forma similar, se usa la notación "@InterceptaError" para especificar que una regla usa un token de error con la finalidad de interceptar un error
de un no-terminal que utiliza; y "@TrasladaError" si, en lugar de interceptarlo, se lo traslada a la regla de un orden superior.

Es importante tener en cuenta que NO CUALQUIER REGLA PUEDE INTERCEPTAR UN ERROR. La regla, además de usar un no-terminal que levante o traslade un error,
este no terminal, en la regla, al remplazarse por el token de error, debe estar procedido de otro no-terminal, de forma que haya un punto de sincronización.
En caso contrario, en caso de una regla de la forma "<no-terminal1> <no-terminal2> ... error", no se intercepta el error, sino que se traslada a una regla de
orden superior. Es decir, SÍ O SÍ, LUEGO DE UN TOKEN DE ERROR, DEBE HABER UN TOKEN O NO-TERMINAL QUE FUNCIONE COMO PUNTO DE SINCRONIZACIÓN (no olvidar lo
mencionado en el segundo párrago).

ATENCIÓN: lo último no es siempre cierto. Existen veces donde el error de igual forma sí se intercepta. Aún no sé por qué a veces sí y a veces no.
NUEVA HIPÓTESIS: O quizás sí. Pero hay una peculiaridad: al irse trasladando el error, las acciones semánticas no se ejecutan hasta que el error no es
interceptado. Si ocurren otros errores (con otras acciones semánticas asociadas en el medio), únicamente la primera acción semántica se ejecuta.
Por lo mismo, es altamente recomendable interceptar los errores lo antes posible, ya que no se acumulan.

Básicamente, es como si una regla, al usar un no-terminal que levanta o traslada un error, estuviese firmando un contrato que dice: "Me comprometo a
interceptar o trasladar el error".

Si en una regla está la posibilidad de vacío, "lambda", jamás va a producirse un error en dicha regla, porque, de venir algo que no cumpla
con ninguna de las alternativas válidas, va a reducir por la regla vacía.

Las reglas vacías son altamente propensas a conflictos shift/reduce.

*/

// ============================================================================================================================================================
// INICIO DE REGLAS
// ============================================================================================================================================================

programa                        : ID cuerpo_programa
                                { notifyDetection("Programa."); }
                                // -----------------
                                // REGLAS DE ERROR
                                // -----------------
                                | ID conjunto_sentencias
                                //| conjunto_sentencias
                                | cuerpo_programa
                                { notifyError("El programa requiere de un nombre."); }
                                | error
                                {
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }. Se sincronizará hasta ID.");
                                    descartarTokensHasta(ID); // Se descartan todos los tokens hasta ID.
                                }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_programa                 : '{' conjunto_sentencias '}'
                                // -----------------
                                // REGLAS DE ERROR
                                // -----------------
                                | '{' '}'
                                { notifyError("El programa no posee ninguna sentencia."); }
                                //| conjunto_sentencias
                                { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
                                | // lambda //
                                { notifyError("El programa no posee un cuerpo."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                
conjunto_sentencias             : sentencia
                                | conjunto_sentencias sentencia
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                // -----------------
                                // REGLAS DE ERROR
                                // -----------------
                                | error ';'
                                {
                                    notifyError("Sentencia inválida en el lenguaje.");
                                }
                                | error '}'
                                {
                                    notifyError("Sentencia inválida en el lenguaje.");
                                    readLastTokenAgain(); // Importante para no consumir erróneamente el cierre del programa.
                                }
                                | error sentencia
                                {
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta otra sentencia.");
                                }
                                ;

// ************************************************************************************************************************************************************
// Sentencias declarativas
// ************************************************************************************************************************************************************

sentencia_declarativa           : declaracion_variable
                                { notifyDetection("Declaración de variable."); }
                                | declaracion_funcion punto_y_coma_opcional
                                /*
                                | error
                                { notifyError("Sentencia declarativa no válida."); }
                                */
                                ;

// ************************************************************************************************************************************************************
// Variables
// ************************************************************************************************************************************************************

declaracion_variable            : UINT lista_variables ';'
                                | UINT error ';'
                                { notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
                                | UINT lista_variables error ';'
                                { notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

/*
Si el error está al comienzo de la regla, no se puede obtener su valor a través de $n.
Si está en alguna otra parte, sí se puede.
ERRORES AL COMIENZO DE UNA REGLA NO.
SI LOS PONEMOS EN MUCHAS REGLAS, VAMOS A TENER PROBLEMAS.
NO COMBINAR VACÍO CON ERROR.
*/

// Permite la opcionalidad de la coma al final de las funciones.
punto_y_coma_opcional           : // épsilon
                                | ';'
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Al declarar que es de tipo sval, ya no es necesario especificar ".sval" junto a los $n.
lista_variables                 : ID
                                | lista_variables ',' ID
                                { $$ = $3; }
                                // Le dice a Yacc que, cuando reduzca usando esta regla, el valor de la nueva lista_variables
                                // que coloque en la pila no sea el de la lista vieja ($1, por defecto siempre hace $$ = $1),
                                // sino el valor del ID que acaba de leer ($3)
                                // Esto es útil para mostrar errores correctamente. Si no se pone, en una sentencia como:
                                // uint A, B C
                                // Diría que la coma falta entre A y C.
                                // ------------------------------
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ------------------------------
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
asignacion_multiple             : lista_variables '=' lista_constantes
                                { notifyDetection("Asignación múltiple."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                                
lista_constantes                : constante
                                | lista_constantes ',' constante
                                { $$ = $3; }
                                // ==============================
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ==============================
                                | lista_constantes constante
                                {
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        $1, $2));
                                }
                                ;

// ************************************************************************************************************************************************************
// Expresiones Lambda
// ************************************************************************************************************************************************************

// El factor representa al argumento.
lambda                          : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'
                                { notifyDetection("Expresión lambda."); }
                                ;

// Separado por legibilidad y para contemplar los casos de error.
parametro_lambda                : UINT ID
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | // épsilon
                                { notifyError("La expresión lambda requiere de un parámetro."); }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias ejecutables                                                                               */
/* ---------------------------------------------------------------------------------------------------- */

cuerpo_ejecutable               : sentencia_ejecutable
                                | bloque_ejecutable
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | '{' '}'
                                { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
                                //| //épsilon 2 shift/reduce
                                //{ notifyError("Debe especificarse un cuerpo para la sentencia."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

conjunto_sentencias_ejecutables : sentencia_ejecutable
                                | conjunto_sentencias_ejecutables sentencia_ejecutable
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @LevantaError: "Toda sentencia ejecutable debe terminar con punto y coma."
sentencia_ejecutable            : operacion_ejecutable ';'
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | operacion_ejecutable error
                                { notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operacion_ejecutable            : invocacion_funcion
                                | asignacion_simple
                                | asignacion_multiple
                                | sentencia_control
                                | sentencia_retorno
                                | impresion
                                | lambda
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

asignacion_simple               : variable DASIG expresion                                          
                                { notifyDetection("Asignación simple."); }
                                // ==============================
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ==============================
                                | variable error expresion
                                { notifyError("Error en asignación. Se esperaba un ':='."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

sentencia_control               : if                                                                
                                | do_while         
                                { notifyDetection("Sentencia WHILE."); }                                                   
                                ;

// ************************************************************************************************************************************************************
// Condición
// ************************************************************************************************************************************************************

// @TrasladaError: "Falta cierre de paréntesis en condición."
condicion                       : inicio_condicion cuerpo_condicion fin_condicion
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

inicio_condicion                : '('
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | // lambda //
                                { notifyError("Falta apertura de paréntesis en condición."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

/*
    En caso de no recibir un paréntesis de cierre, el fin de la condición será inválida, por lo que se levantará
    un error que deberán interceptar las sentencias que usen el no terminal.
*/
// @LevantaError: "Falta cierre de paréntesis en condición."
fin_condicion                   : ')'
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_condicion                : expresion comparador expresion
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | // lambda //
                                { notifyError("La condición no puede estar vacía."); }
                                | expresion
                                { notifyError("Falta de comparador en comparación."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
                                
comparador                      : '>'
                                | '<'
                                | EQ
                                | LEQ
                                | GEQ
                                | NEQ
                                // ==============================
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ==============================
                                | '='
                                {
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
                                ;

// ************************************************************************************************************************************************************
// Sentencia IF
// ************************************************************************************************************************************************************

// @InterceptaError: "Falta cierre de paréntesis en condición."
if                              : IF condicion cuerpo_ejecutable rama_else ENDIF
                                | IF condicion cuerpo_ejecutable rama_else
                                { notifyError("La sentencia IF debe finalizarse con 'endif'."); }
                                // ==============================
                                // INTERCEPCIÓN DE ERRORES
                                // ==============================
                                | IF error cuerpo_ejecutable rama_else ENDIF
                                { notifyError("Falta cierre de paréntesis en condición."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

rama_else                       : // lambda //
                                { notifyDetection("Sentencia IF."); }
                                | ELSE cuerpo_ejecutable
                                { notifyDetection("Sentencia IF-ELSE."); }
                                ;

// ************************************************************************************************************************************************************
// Sentencia WHILE
// ************************************************************************************************************************************************************

// @InterceptaError: "Falta 'while'."
do_while                        : DO cuerpo_do
                                // ==============================
                                // INTERCEPCIÓN DE ERRORES
                                // ==============================
                                | DO error
                                { notifyError("Falta 'while'."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// @LevantaError: "Falta 'while'."
// @InterceptaError: "Falta cierre de paréntesis en condición."
cuerpo_do                       : cuerpo_ejecutable WHILE condicion
                                // ==============================
                                // INTERCEPCIÓN DE ERRORES
                                // ==============================
                                | cuerpo_ejecutable WHILE error
                                { notifyError("Falta cierre de paréntesis en condición."); }
                                | error WHILE error
                                ;

// ************************************************************************************************************************************************************
// Sentencia PRINT
// ************************************************************************************************************************************************************

impresion                       : PRINT '(' imprimible ')'                                          
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

imprimible                      : STR
                                { notifyDetection("Impresión de cadena."); }
                                | expresion
                                { notifyDetection("Impresión de expresión."); }
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | // lambda //
                                { notifyError("La sentencia 'print' requiere de al menos un argumento."); }
                                ;

// ************************************************************************************************************************************************************
// Expresiones
// ************************************************************************************************************************************************************

expresion                       : expresion operador_suma termino
                                | termino
                                // ====================
                                // REGLAS DE ERROR
                                // ====================
                                | secuencia_sin_operador
                                { $$ = $1; }
                                | expresion operador_suma
                                {
                                    notifyError("Falta de operando en expresión.");
                                    $$ = $1;    
                                }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operador_suma                   : '+'
                                | '-'
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

/*
    Permite la aparición de varios términos sin un operador de por medio.
    No hace falta agregar una regla similar para "factor" ya que hay una regla termino -> factor.
*/
secuencia_sin_operador          : termino termino
                                {
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        $1, $2)
                                    );
                                    $$ = $2;
                                }
                                | secuencia_sin_operador termino
                                {
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        $1, $2)
                                    );
                                    $$ = $2;
                                }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

termino                         : termino operador_multiplicacion factor
                                | factor
                                { $$ = $1; }
                                | termino operador_multiplicacion error
                                { notifyError("Falta de operando en expresión."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

operador_multiplicacion         : '/'
                                | '*'
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

factor                          : variable
                                { $$ = $1; }
                                | constante
                                { $$ = $1; }
                                | invocacion_funcion
                                { $$ = $1; }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

constante                       : CTE
                                { $$ = $1; }
                                //| '-' CTE // Luego debe revisarse que la CTE no sea entera.
                                //{ $$ = '-' + $2; }
                                // Si se obliga que haya un espacio entre terminos, ' ', entonces esto no da shift/reduce.
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

variable                        : ID
                                | ID '.' ID
                                { $$ = $1 + "." + $3; }
                                ;

// ************************************************************************************************************************************************************
// Declaración de Función
// ************************************************************************************************************************************************************

declaracion_funcion             : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'
                                { notifyDetection("Declaración de función."); }
                                | UINT '(' conjunto_parametros ')' '{' cuerpo_funcion '}'
                                { notifyError("Falta de nombre en la función."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

cuerpo_funcion                  : conjunto_sentencias
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | // lambda //
                                { notifyError("El cuerpo de la función no puede estar vacío."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

sentencia_retorno               : RETURN '(' expresion ')'
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

conjunto_parametros             : lista_parametros
                                // ==============================
                                // REGLAS DE ERROR
                                // ==============================
                                | // lambda //
                                { notifyError("Toda función debe recibir al menos un parámetro."); }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

lista_parametros                : parametro_formal 
                                | lista_parametros ',' parametro_formal 
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | parametro_vacio
                                { notifyError("Se halló un parámetro formal vacío."); }
                                // No puede agregarse directamente épsilon porque daría reduce/reduce con la regla de arriba.
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

parametro_vacio                 : lista_parametros ','
                                | ',' parametro_formal
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

parametro_formal                : semantica_pasaje UINT variable
                                | semantica_pasaje UINT 
                                { notifyError("Falta de nombre de parámetro formal en declaración de función."); }
                                | semantica_pasaje variable
                                { notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
                                ; 

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

semantica_pasaje                : // lambda //
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

invocacion_funcion              : ID '(' lista_argumentos ')' 
                                {
                                    notifyDetection("Invocación de función."); 
                                    $$ = $1 + '(' + $3 + ')';
                                }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

lista_argumentos                : argumento
                                | lista_argumentos ',' argumento
                                { $$ = $3; }
                                ;

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

argumento                       : expresion FLECHA ID
                                { $$ = $1 + $2 + $3; }
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | expresion
                                {
                                    notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
                                }
                                ;

// ============================================================================================================================================================
// FIN DE REGLAS  
// ============================================================================================================================================================

%%

// ============================================================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ============================================================================================================================================================

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    this.readAgain = false;
    
    // Descomentar la siguiente línea para activar el debugging.
    // yydebug = true;
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
    // El mensaje 's' que nos pasa Byacc/J es genérico y poco útil.
    // Lo ignoramos y usamos el estado del lexer para dar un mensaje preciso.
    
    // yychar es una variable interna del parser que contiene el token actual (lookahead).
    if (yychar == EOF) {
        notifyError("Error de sintaxis: Se alcanzó el final del archivo inesperadamente. (¿Falta un '}' o un ';'? )");
        return;
    }
    
    // Usamos nuestro método notificador con la información de línea/columna del lexer.
    /*notifyError(
        String.format(
            "Error de sintaxis cerca del token '%s'.", 
            lexer.getCurrentToken().getLexema() // Asumiendo que tu lexer tiene un método para obtener el lexema actual.
        )
    );
    */
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
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