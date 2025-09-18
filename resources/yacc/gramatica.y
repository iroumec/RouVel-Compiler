declaraciones

%token ID, CTE, STR

%%

programa                : nombre_programa '{' conjunto_sentencias '}'
                        ;

nombre_programa         : ID 
                    
conjunto_sentencias     : sentencia ';' 
                        | sentencia ';' conjunto_sentencias
                        ;

sentencia               : sentencia_control 
                        | sentencia_declarativa
                        ;

sentencia_control       :

sentencia_declarativa   :

condicional             : IF '(' 

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