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

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE REGLAS                                                                                 */
/* --------------------------------------------------------------------------------------------- */

programa                        : ID '{' conjunto_sentencias '}'
                                ;
                    
conjunto_sentencias             : sentencia ';' 
                                | sentencia ';' conjunto_sentencias
                                ;

sentencia                       : sentencia_ejecutable
                                | sentencia_declarativa
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Sentencias declarativas                                                                      */
/* --------------------------------------------------------------------------------------------- */

sentencia_declarativa           : UINT lista_variables
                                | asignacion
                                ;

lista_variables                 : ID
                                | ID ',' lista_variables
                                ;

asignacion                      : identificador_multiple '=' expresion_multiple
                                ;

identificador_multiple          : identificador
                                | identificador ',' identificador_multiple
                                ;

expresion_multiple              : CTE
                                | CTE ',' expresion_multiple
                                ;

lambda                          : parametro conjunto_sentencias_ejecutables '(' factor ')'
                                ;

parametro                       : UINT ID
                                ;

argumento                       : factor // revisar luego
                                ;
                            

/* --------------------------------------------------------------------------------------------- */
/* Sentencias ejecutables                                                                        */
/* --------------------------------------------------------------------------------------------- */

bloque_ejecutable               : '{' conjunto_sentencias_ejecutables '}'

conjunto_sentencias_ejecutables : sentencia_ejecutable
                                | sentencia_ejecutable ';' conjunto_sentencias_ejecutables
                                ;

sentencia_ejecutable            : invocacion_funcion
                                | sentencia_control
                                | impresion
                                ;

sentencia_control               : if
                                | while
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
                                ;

while                           : DO conjunto_sentencias_ejecutables WHILE '(' condicion ')'
                                ;

impresion                       : PRINT '(' imprimible ')'
                                ;

imprimible                      : STR
                                | expresion
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Expresiones                                                                                   */
/* --------------------------------------------------------------------------------------------- */

expresion                       : expresion '+' termino
                                | expresion '-' termino
                                | termino
                                ;

termino                         : termino '/' factor 
                                | termino '*' factor
                                | factor
                                ;

factor                          : ID
                                | ID '.' ID
                                | CTE
                                | '-' CTE /* Luego debe revisarse que la CTE no sea un entero */
                                ;

identificador                   : ID
                                | ID '.' ID
                                ;

/* --------------------------------------------------------------------------------------------- */
/* Funciones, llamadas y parametros                                                              */
/* --------------------------------------------------------------------------------------------- */

funcion                         : UINT identificador '(' lista_parametros ')' '{' conjunto_sentencias '}'
                                ;
                            
lista_parametros                : parametro_formal 
                                | parametro_formal ',' lista_parametros
                                ;

invocacion_funcion              : identificador '(' lista_parametros_formales ')' 
                                ;

lista_parametros_formales       : parametro_formal 
                                | parametro_formal ',' lista_parametros_formales
                                ;

parametro_formal                : CVR UINT lista_variables 
                                | UINT lista_variables
                                ; 

/* --------------------------------------------------------------------------------------------- */
/* FIN DE REGLAS                                                                                 */
/* --------------------------------------------------------------------------------------------- */

%%

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                   */
/* --------------------------------------------------------------------------------------------- */

// Lexer.
private final Lexer lexer;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
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

    return token.getIdentificationCode();
}

void yyerror(String message) {
    System.out.println(message);
}

/* --------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                 */
/* --------------------------------------------------------------------------------------------- */