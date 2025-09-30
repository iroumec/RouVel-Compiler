/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE DECLARACIONES                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

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
%type <sval> lista_variables // El no-terminal lista_variables también gaurda un String.

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
                                ;
                
conjunto_sentencias             : sentencia
                                | conjunto_sentencias sentencia
                                ;

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias declarativas                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

sentencia_declarativa           : declaracion_variable
                                { notifyDetection("Declaración de variable."); }
                                | declaracion_funcion punto_y_coma_opcional
                                
                                | error
                                { notifyError("Sentencia declarativa no válida."); }
                                ;
/*
declaracion_variable            : UINT lista_variables ';'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                /*
                                | UINT error lista_variables ';'
                                { notifyError(applySynchronizationFormat($2.sval, $3.sval)); }
                                | error UINT error lista_variables ';'
                                { notifyError($1.sval + " no es tipo válido y " + $3.sval + " no es una variable válida."); }
                                | error UINT lista_variables ';'
                                { notifyError(applySynchronizationFormat($1.sval, $2.sval)); }
                                
                                ; */

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
                                ;

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

cuerpo_ejecutable               : bloque_ejecutable
                                | sentencia_ejecutable
                                ;

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | '{' '}'
                                { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
                                | // épsilon
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
                                ;

// Separada por legibilidad.
sentencia_control               : if                                                                
                                | while         
                                { notifyDetection("Sentencia WHILE."); }                                                   
                                ;

condicion                       : expresion comparador expresion
                                { notifyDetection("Condicion.")}
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | // éspilon
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

if                              : IF '(' condicion ')' cuerpo_ejecutable rama_else fin_if
                                ;

fin_if                          : ENDIF
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | error // No hay punto de sincronización
                                {
                                    notifyError("La sentencia IF debe finalizarse con 'endif'.");
                                    descartarTokenError();
                                }
                                ;

rama_else                       : // épsilon
                                { notifyDetection("Setencia IF."); }
                                | ELSE cuerpo_ejecutable
                                { notifyDetection("Setencia IF-ELSE."); }
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
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                //| error
                                //{ notifyError("Operador no válido."); }
                                ;

termino                         : termino operador_multiplicacion factor 
                                | factor
                                ;

// Esta separación se realiza en libros como "Compilers: Principles, Techniques, and Tools”
// para aclarar la estructura y reducir la duplicación.
operador_multiplicacion         : '/'
                                | '*'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                //| error
                                //{ notifyError("Operador no válido."); }
                                ;

factor                          : variable
                                | constante
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | error
                                { notifyError("Operando no válido."); } // ¿Por qué lllega hasta acá si el if no tiene ENDIF?
                                ;

// Separados para contemplar la posibilidad de CTE negativa.
constante                       : CTE
                                | '-' CTE // Luego debe revisarse que la CTE no sea entera.
                                ;

// Separado para contemplar la posibilidad de identificador prefijado.
variable                        : ID
                                | ID '.' ID
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

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
}

void yyerror(String message) {
    // Silenciado
}

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    //yyerrflag = 0;
}
// TODO: descartar hasta un punto de sincronizacion. "}" o ";".

void notifyDetection(String message) {
    System.err.println("------------------------------------");
    System.err.printf("DETECCIÓN SEMÁNTICA: %s\n", message);
    System.err.println("------------------------------------");
}

void notifyWarning(String warningMessage) {
    System.err.println("------------------------------------");
    System.err.printf("WARNING SINTÁCTICA: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage);
    System.err.println("------------------------------------");
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    System.err.println("------------------------------------");
    System.err.printf("ERROR SINTÁCTICO: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage);
    System.err.println("------------------------------------");
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