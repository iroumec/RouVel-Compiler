/* Declaraciones */

%token ID, CTE, STR, PRNT, IF, ELSE, ENDIF, UINT, EQ, LEQ, GEQ, NEQ, CVR, DO, WHILE

%%

programa                    : ID '{' conjunto_sentencias '}'
                            ;
                    
conjunto_sentencias         : sentencia ';' 
                            | sentencia ';' conjunto_sentencias
                            ;

sentencia                   : sentencia_ejecutable
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
                                | identificador_multiple ',' identificador_multiple
                                ;

expresion_multiple              : CTE
                                | expresion_multiple ',' expresion_multiple
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

if                              : IF '(' condicion ')' conjunto_sentencias_ejecutables ENDIF
                                | IF '(' condicion ')' conjunto_sentencias_ejecutables ELSE conjunto_sentencias_ejecutables ENDIF
                                ;

while                           : DO conjunto_sentencias_ejecutables WHILE '(' condicion ')'
                                ;

impresion                       : PRNT '(' STR ')'
                                | PRNT '(' expresion ')'

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

funcion                     : UINT identificador '(' lista_parametros ')' '{' conjunto_sentencias '}'
                            ;
                            
lista_parametros            : parametro_formal 
                            | parametro_formal ',' lista_parametros
                            ;

invocacion_funcion          : identificador '(' lista_parametros_formales ')' 
                            ;

lista_parametros_formales   : parametro_formal 
                            | parametro_formal ',' lista_parametros_formales
                            ;

parametro_formal            : CVR UINT lista_variables 
                            | UINT lista_variables
                            ; 

%%

/* código (opcional) */