/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE DECLARACIONES                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

%token ID, CTE, STR
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
                    
conjunto_sentencias             : sentencia ';' 
                                | conjunto_sentencias sentencia ';'
                                ;

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias declarativas                                                                              */
/* ---------------------------------------------------------------------------------------------------- */

sentencia_declarativa           : UINT lista_variables
                                { notifyDetection("Declaración de variable."); }
                                | declaracion_funcion
                                { notifyDetection("Declaración de función."); }
                                ;

lista_variables                 : ID
                                | lista_variables ',' ID
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
lambda                          : '(' UINT ID ')' bloque_ejecutable '(' factor ')'
                                { notifyDetection("Expresión lambda."); }
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Sentencias ejecutables                                                                               */
/* ---------------------------------------------------------------------------------------------------- */

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | '{' '}'
                                { notifyError("El cuerpo de la sentencia no puede estar vacío."); }
                                ;

conjunto_sentencias_ejecutables : sentencia_ejecutable ';'
                                | conjunto_sentencias_ejecutables sentencia_ejecutable ';'
                                ;

sentencia_ejecutable            : invocacion_funcion
                                | asignacion_simple
                                | asignacion_multiple
                                | sentencia_control
                                | impresion
                                | lambda
                                ;

asignacion_simple               : variable DASIG expresion                                          
                                { notifyDetection("Asignación simple."); }
                                ;

// Separada por legibilidad.
sentencia_control               : if                                                                
                                | while         
                                { notifyDetection("Setencia WHILE."); }                                                   
                                ;

condicion                       : expresion comparador expresion
                                ;
                        
comparador                      : '>'
                                | '<'
                                | EQ
                                | LEQ
                                | GEQ
                                | NEQ
                                ;

if                              : IF '(' condicion ')' bloque_ejecutable ENDIF
                                { notifyDetection("Setencia IF."); }
                                | IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable ENDIF
                                { notifyDetection("Setencia IF-ELSE."); }
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | IF '(' condicion ')' bloque_ejecutable error
                                { notifyError("La sentencia IF debe finalizarse con 'endif'."); }
                                | IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable error
                                { notifyError("La sentencia IF-ELSE debe finalizarse con 'endif'."); }
                                ;

while                           : DO sentencia_ejecutable WHILE '(' condicion ')'
                                | DO bloque_ejecutable WHILE '(' condicion ')'
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
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | expresion operador_suma error
                                ;

// Esta separación se realiza en libros como "Compilers: Principles, Techniques, and Tools”
// para aclarar la estructura y reducir la duplicación.
operador_suma                   : '+'
                                | '-'
                                ;

termino                         : termino operador_multiplicacion factor 
                                | factor
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | termino operador_multiplicacion error
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
                                ;

// Separado para contemplar la posibilidad de identificador prefijado.
variable                        : ID
                                | ID '.' ID
                                ;

/* ---------------------------------------------------------------------------------------------------- */
/* Funciones, llamadas y parametros                                                                     */
/* ---------------------------------------------------------------------------------------------------- */

declaracion_funcion             : UINT ID '(' lista_parametros ')' '{' cuerpo_funcion '}'
                                // --------------- //
                                // REGLAS DE ERROR //
                                // --------------- //
                                | UINT ID '(' ')' '{' cuerpo_funcion '}'
                                { notifyError("Toda función debe recibir al menos un parámetro."); }
                                ;

cuerpo_funcion                  : conjunto_sentencias RETURN expresion ';'
                                ;
                            
lista_parametros                : parametro_formal 
                                | lista_parametros ',' parametro_formal 
                                ;

// Separado por legibilidad.
parametro_formal                : semantica_pasaje UINT variable
                                ; 

// Separado para evitar un reduce/reduce.
semantica_pasaje                : // épsilon - cadena vacía.
                                | CVR
                                ;

invocacion_funcion              : ID '(' lista_argumentos ')' 
                                { notifyDetection("Invocación de función."); }
                                ;

lista_argumentos                : argumento
                                | lista_argumentos ',' argumento
                                ;

// Separado por legibilidad.
argumento                       : expresion FLECHA parametro_formal
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

void notifyDetection(String message) {
    System.out.printf("%nDETECCIÓN SEMÁNTICA: %s%n%n", message);
}

void notifyWarning(String warningMessage) {
    System.err.printf("%WARNING SINTÁCTICA: Línea %d: %s%n%n", lexer.getNroLinea(), warningMessage);
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    System.err.printf("%nERROR SINTÁCTICO: Línea %d: %s%n%n", lexer.getNroLinea(), errorMessage);
    this.errorsDetected++;
}

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = (token.hasSymbolTableIndex()) ?
        new ParserVal(token.getSymbolTableIndex())
        : new ParserVal();

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
}

void yyerror(String message) {
    System.out.println(message);
}

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                        */
/* ---------------------------------------------------------------------------------------------------- */