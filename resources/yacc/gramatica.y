declaraciones

%token ID, CTE, STR, PRNT, IF, ELSE, EIF

%%

programa                : nombre_programa '{' conjunto_sentencias '}'
                        ;

nombre_programa         : ID 
igual no seria identificador, pero como el ejemplo el nombre de programa esta en minusculas, no se que deberíamos hacer ahí
                    
conjunto_sentencias     : sentencia ';' 
                        | sentencia ';' conjunto_sentencias
                        ;

sentencia               : sentencia_control 
                        | sentencia_declarativa
                        ;

bloque_ejecutable       : '{' conjunto_sentencias_ejecutables '}'

sentencia_control       : if
                        | while
                        ;

condicion               :

sentencia_declarativa   :

asignacion              : identificador_multiple '=' expresion_multiple
                        ;

identificador_multiple  : identificador
                        | identificador_multiple ',' identificador_multiple
                        ;

expresion_multiple      : CTE
                        | expresion_multiple ',' expresion_multiple
                        ;

lambda                  : parametro cuerpo '(' factor ')'
                        ;

parametro               : tipo ID
                        ;

tipo                    : UINT
                        ;

argumento               : factor // revisar luego
                        ;

if                      : IF '(' condicion ')' bloque_sentencias_ejecutable EIF
                        | IF '(' condicion ')' bloque_sentencias_ejecutable ELSE bloque_sentencias_ejecutable EIF
                        ;

while                   : DO bloque_sentencias_ejecutable WHILE '(' condicion ')'

impresion               : PRNT '(' STR ')'
                        | PRNT '(' expresion ')'

expresion               : expresion '+' termino
                        | expresion '-' termino
                        | termino
                        ;

termino                 : termino '/' factor 
                        | termino '*' fector
                        | factor
                        ;

factor                  : identificador
                        | CTE
                        ;

identificador           : ID
                        | ID '.' ID
                        ;

%%

código (opcional)