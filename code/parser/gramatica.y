/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE DECLARACIONES                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

// Importaciones necesarias.
%{
    package parser;

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

// Asignación de tipo a token y no-terminales.
// Esto es necesario para ejecutar acciones semánticas como: "$$ = $3".
%token <sval> ID, CTE, STR  // Los tokens ID, CTE y STR tendrán un valor de tipo String (accedido vía .sval)
%type <sval> lista_variables, lista_constantes, variable, constante // El no-terminal lista_variables también guarda un String.

// Tokens sin valor semántico asociado (no necesitan tipo).
%token EQ, GEQ, LEQ, NEQ, DASIG, FLECHA
%token PRINT, IF, ELSE, ENDIF, UINT, CVR, DO, WHILE, RETURN

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE DECLARACIONES                                                                                 */
/* ---------------------------------------------------------------------------------------------------- */

%%

// error: token especial que representa cualquier cosa que en ese punto no cumpla ninguna
// de las alternativas válidas.

/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE REGLAS                                                                                     */
/* ---------------------------------------------------------------------------------------------------- */

programa                        : ID '{' conjunto_sentencias '}'
                                { notifyDetection("Programa."); }
                                // -----------------
                                // REGLAS DE ERROR
                                // -----------------
                                | ID
                                { notifyError("El programa no posee un cuerpo."); }
                                | ID '{' '}'
                                { notifyError("El programa no tiene ninguna sentencia."); }
                                | error
                                {
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }. Se sincronizará hasta ID.");
                                    descartarTokensHasta(ID); // Se descartan todos los tokens hasta ID.
                                }
                                ;
                
conjunto_sentencias             : sentencia
                                | conjunto_sentencias sentencia
                                ;

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                // -----------------
                                // REGLAS DE ERROR
                                // -----------------
                                | error ';'
                                {
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un ';'.");
                                }
                                | error '}'
                                {
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un '}'.");
                                }
                                | error sentencia
                                {
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta otra sentencia.");
                                }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias declarativas                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

sentencia_declarativa           : declaracion_variable
                                { notifyDetection("Declaración de variable."); }
                                | declaracion_funcion punto_y_coma_opcional
                                /*
                                | error
                                { notifyError("Sentencia declarativa no válida."); }
                                */
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Variables                                                                                            */
/* ---------------------------------------------------------------------------------------------------- */

declaracion_variable            : UINT lista_variables ';'
                                | UINT error ';'
                                { notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
                                | UINT lista_variables error ';'
                                { notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
                                ;

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

/*
Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17).

No es posible chequear utilizando únicamente las reglas que el lado derecho pueda tener más elementos
que el izquierdo. Se requería un autómata de Turing. Por eso, se utilizan acciones semánticas.

Los elementos del lado derecho sólo pueden ser constantes.
*/
asignacion_multiple             : lista_variables '=' lista_constantes
                                { notifyDetection("Asignación múltiple."); }
                                ;
                                
lista_constantes                : constante
                                | lista_constantes ',' constante
                                { $$ = $3; }
                                // ------------------------------
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ------------------------------
                                | lista_constantes constante
                                {
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        $1, $2));
                                }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Expresiones Lambda                                                                                   */
/* ---------------------------------------------------------------------------------------------------- */

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

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | '{' '}'
                                { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
                                | //épsilon
                                { notifyError("Debe especificarse un cuerpo para la sentencia."); }
                                ;

conjunto_sentencias_ejecutables : sentencia_ejecutable
                                | conjunto_sentencias_ejecutables sentencia_ejecutable
                                ;

sentencia_ejecutable            : operacion_ejecutable ';'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | operacion_ejecutable error
                                { notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
                                ;

operacion_ejecutable            : invocacion_funcion
                                | asignacion_simple
                                | asignacion_multiple
                                | sentencia_control
                                | sentencia_retorno
                                | impresion
                                | lambda
                                ;

asignacion_simple               : variable DASIG expresion                                          
                                { notifyDetection("Asignación simple."); }
                                // ------------------------------
                                // REGLAS DE ERROR CONTEXTUALES
                                // ------------------------------
                                | variable error expresion
                                { notifyError("Error en asignación. Se esperaba un ':='."); }
                                ;

// Separada por legibilidad.
sentencia_control               : if                                                                
                                | while         
                                { notifyDetection("Sentencia WHILE."); }                                                   
                                ;

condicion                       : expresion comparador expresion
                                { notifyDetection("Condicion."); }
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | // épsilon
                                { notifyError("La condición no puede estar vacía."); }
                                ;

comparador                      : '>'
                                | '<'
                                | EQ
                                | LEQ
                                | GEQ
                                | NEQ
                                // ------------------------------
                                // PATRONES DE ERROR ESPECÍFICOS
                                // ------------------------------
                                | '='
                                {
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
                                // ------------------------------
                                // REGLAS DE ERROR
                                // ------------------------------
                                | error
                                {
                                    notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
                                    descartarTokenError(); 
                                }
                                ;

if                              : IF '(' condicion ')' cuerpo_ejecutable rama_else ENDIF
                                | IF '(' condicion ')' cuerpo_ejecutable rama_else
                                { notifyError("La sentencia IF debe finalizarse con 'endif'."); }
                                ;

rama_else                       : // épsilon
                                { notifyDetection("Sentencia IF."); }
                                | ELSE cuerpo_ejecutable
                                { notifyDetection("Sentencia IF-ELSE."); }
                                ;


while                           : DO cuerpo_ejecutable WHILE '(' condicion ')'
                                ;

impresion                       : PRINT '(' imprimible ')'                                          
                                ;

imprimible                      : STR
                                { notifyDetection("Impresión de cadena."); }
                                | expresion
                                { notifyDetection("Impresión de expresión."); }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Expresiones                                                                                          */
/* ---------------------------------------------------------------------------------------------------- */

// Podría simplificarse con un "operador_expresion" creo.
expresion                       : expresion operador_suma termino
                                | termino
                                ;

// Esta separación se realiza en libros como "Compilers: Principles, Techniques, and Tools”
// para aclarar la estructura y reducir la duplicación.
operador_suma                   : '+'
                                | '-'
                                ;

termino                         : termino operador_multiplicacion factor 
                                | factor
                                ;

// Esta separación se realiza en libros como "Compilers: Principles, Techniques, and Tools”
// para aclarar la estructura y reducir la duplicación.
operador_multiplicacion         : '/'
                                | '*'
                                ;

factor                          : variable
                                | constante
                                ;

// Separados para contemplar la posibilidad de CTE negativa.
constante                       : CTE
                                | '-' CTE // Luego debe revisarse que la CTE no sea entera.
                                { $$ = '-' + $2; }
                                ;

// Separado para contemplar la posibilidad de identificador prefijado.
variable                        : ID
                                | ID '.' ID
                                { $$ = $1 + "." + $3; }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Funciones, llamadas y parametros                                                                     */
/* ---------------------------------------------------------------------------------------------------- */

declaracion_funcion             : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'
                                { notifyDetection("Declaración de función."); }
                                ;

cuerpo_funcion                  : conjunto_sentencias
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | // Épsilon
                                { notifyError("El cuerpo de la función no puede estar vacío."); }
                                ;

sentencia_retorno               : RETURN expresion
                                ;

conjunto_parametros             : lista_parametros
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | // épsilon
                                { notifyError("Toda función debe recibir al menos un parámetro."); }
                                ;
                            
lista_parametros                : parametro_formal 
                                | lista_parametros ',' parametro_formal 
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | parametro_vacio
                                { notifyError("Se halló un parámetro formal vacío."); }
                                // No puede agregarse directamente épsilon porque daría reduce/reduce con la regla de arriba.
                                ;

parametro_vacio                 : lista_parametros ','
                                | ',' parametro_formal
                                ;

// Separado por legibilidad.
parametro_formal                : semantica_pasaje UINT variable
                                ; 

// Separado para evitar un reduce/reduce.
semantica_pasaje                : // épsilon
                                | CVR
                                | error
                                {
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Invocación de Función                                                                                */
/* ---------------------------------------------------------------------------------------------------- */

invocacion_funcion              : ID '(' lista_argumentos ')' 
                                { notifyDetection("Invocación de función."); }
                                ;

lista_argumentos                : argumento
                                | lista_argumentos ',' argumento
                                ;

// Separado por legibilidad.
argumento                       : expresion FLECHA ID
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE REGLAS                                                                                        */
/* ---------------------------------------------------------------------------------------------------- */

%%

/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                          */
/* ---------------------------------------------------------------------------------------------------- */

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

private int errorsDetected;
private int warningsDetected;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    // Se activa el debug.
    yydebug = true;
}

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

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

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}
// TODO: descartar hasta un punto de sincronizacion. "}" o ";".

void apagarEstadoDeError() {
    yyerrflag = 0;
}

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

public int getWarningsDetected() {
    return this.warningsDetected;
}

public int getErrorsDetected() {
    return this.errorsDetected;
}

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                        */
/* ---------------------------------------------------------------------------------------------------- */