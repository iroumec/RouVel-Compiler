/* --------------------------------------------------------------------------------------------- */
/* INICIO DE DECLARACIONES                                                                       */
/* --------------------------------------------------------------------------------------------- */

%token ID, CTE, STR
%token EQ, GEQ, LEQ, NEQ, DASIG, FLECHA
%token PRINT, IF, ELSE, ENDIF, UINT, CVR, DO, WHILE, RETURN

/* --------------------------------------------------------------------------------------------- */
/* FIN DE DECLARACIONES                                                                       */
/* --------------------------------------------------------------------------------------------- */

%%

/* error: cualquier cosa que en ese punto no cumpla ninguna de las alternativas válidas. */

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE REGLAS                                                                              */
/* --------------------------------------------------------------------------------------------- */

programa                        : ID '{' conjunto_sentencias '}'                                    { notifyDetection("Programa."); }
                                ;
                    
conjunto_sentencias             : sentencia ';' 
                                | sentencia ';' conjunto_sentencias
                                ;

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Sentencias declarativas                                                                       */
/* --------------------------------------------------------------------------------------------- */

sentencia_declarativa           : UINT lista_variables                                              { notifyDetection("Declaración de variable."); }
                                | declaracion_funcion
                                ;

lista_variables                 : ID
                                | lista_variables ',' ID
                                ;

/* Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17). */
/* De esta forma, siempre se controla que el lado derecho tenga, al menos, tanto elementos como el izquierdo. */
/* Los elementos del lado derecho sólo pueden ser constantes. */
asignacion_multiple             : lista_variables '=' lista_constantes  {  } /* Acción semántica para comprobar número. Se requiere gramática de turing*/
                                ;
                                
lista_constantes                : constante
                                | constante ',' lista_constantes
                                ;

lambda                          : parametro conjunto_sentencias_ejecutables '(' factor /*argumento*/ ')'
                                ;

parametro                       : UINT ID
                                ;
                            

/* --------------------------------------------------------------------------------------------- */
/* Sentencias ejecutables                                                                        */
/* --------------------------------------------------------------------------------------------- */

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'

conjunto_sentencias_ejecutables : sentencia_ejecutable ';'
                                | sentencia_ejecutable ';' conjunto_sentencias_ejecutables
                                /* REGLAS DE ERROR */
                                /*| /* epsilon - cadena vacía (shift/reduce si se coloca esto)
                                { notifyError("El cuerpo del IF no puede estar vacío."); } */
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

sentencia_control               : if                                                                
                                { notifyDetection("Setencia IF."); }
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
                                | IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable ENDIF
                                /* REGLAS DE ERROR */
                                | IF '(' condicion ')' bloque_ejecutable error                          { notifyError("La sentencia IF debe finalizarse con 'endif'."); }
                                ;

while                           : DO sentencia_ejecutable WHILE '(' condicion ')'
                                | DO bloque_ejecutable WHILE '(' condicion ')'
                                ;

impresion                       : PRINT '(' imprimible ')'                                          
                                ;

imprimible                      : STR                                                               { notifyDetection("Impresión de cadena."); }
                                | expresion                                                         { notifyDetection("Impresión de expresión."); }
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Expresiones                                                                                   */
/* --------------------------------------------------------------------------------------------- */

expresion                       : expresion '+' termino
                                | expresion '-' termino
                                | termino
                                /* REGLAS DE ERROR */
                                | expresion '+' error
                                | expresion '-' error
                                ;

termino                         : termino '/' factor 
                                | termino '*' factor
                                | factor
                                /* REGLAS DE ERROR */
                                | termino '/' error
                                | termino '*' error
                                ;


factor                          : variable
                                | constante
                                ;

/* Separado para contemplar la posibilidad de CTE negativa. */
constante                       : CTE
                                | '-' CTE /* Luego debe revisarse que la CTE no sea un entero */
                                ;

variable                        : ID
                                | ID '.' ID
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Funciones, llamadas y parametros                                                              */
/* --------------------------------------------------------------------------------------------- */

declaracion_funcion             : UINT ID '(' lista_parametros ')' '{' cuerpo_funcion '}'
                                /* REGLAS DE ERROR */
                                | UINT ID '(' ')' '{' cuerpo_funcion '}'                    { notifyError("Toda función debe recibir al menos un parámetro."); }
                                ;

cuerpo_funcion                  : conjunto_sentencias RETURN expresion ';'
                                ;
                            
lista_parametros                : parametro_formal 
                                | lista_parametros ',' parametro_formal 
                                ;

parametro_formal                : semantica_pasaje UINT variable /* lista_variables */
                                ; 

semantica_pasaje                : /* épsilon - cadena vacía */
                                | CVR
                                ;

invocacion_funcion              : ID '(' lista_argumentos ')' 
                                ;

lista_argumentos                : expresion FLECHA parametro_formal
                                | expresion ',' lista_argumentos
                                ;

/* --------------------------------------------------------------------------------------------- */
/* FIN DE REGLAS                                                                                 */
/* --------------------------------------------------------------------------------------------- */

%%

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                   */
/* --------------------------------------------------------------------------------------------- */

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
}

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

void notifyError(String errorMessage) {
    System.out.println("\nERROR SINTÁCTICO: " + errorMessage + '\n');
}

void notifyDetection(String message) {
    System.out.println("\nDETECCIÓN SEMÁNTICA: " + message + '\n');
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

/* --------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                 */
/* --------------------------------------------------------------------------------------------- */