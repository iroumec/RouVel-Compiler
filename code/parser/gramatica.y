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
    import common.SymbolType;
    import common.SymbolTable;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
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

// Token de fin de archivo.
%token EOF 0

// Estos tokens tendrán un valor de tipo String asociado.
// Esto es necesario para ejecutar acciones semánticas como: "$$ = $3".
// Al declarar que es de tipo sval, ya no es necesario especificar ".sval" junto a los $n.
%token <sval> ID, CTE, STR
%token <sval> EQ, GEQ, LEQ, NEQ, DASIG, FLECHA

// Tokens sin valor semántico asociado (no necesitan tipo).
%token PRINT, IF, ELSE, ENDIF, UINT, CVR, DO, WHILE, RETURN

// No terminales cuyo valor semántico asociado es un String.
%type <sval> expresion, termino, factor, termino_simple, factor_simple, operador_suma, operador_multiplicacion,
                inicio_funcion, lista_variables, lista_constantes, variable, constante, invocacion_funcion,
                lista_argumentos, argumento, comparador, semantica_pasaje, parametro_lambda, argumento_lambda

// ====================================================================================================================
// FIN DE DECLARACIONES
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE REGLAS
// ====================================================================================================================

programa
    : nombre_programa cuerpo_programa
        {
            if (!errorState) {
                notifyDetection("Programa.");
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | nombre_programa conjunto_sentencias
        { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }

    // El error se muestra al comienzo y no al final.
    | { notifyError("El programa requiere de un nombre."); } cuerpo_programa

    | error { notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); } nombre_programa cuerpo_programa
        
    | error EOF
        { notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
    
    | EOF 
        { notifyError("El archivo está vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

nombre_programa
    : ID
        { this.scopeStack.push($1); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_programa
    : '{' conjunto_sentencias '}'

    // |========================= REGLAS DE ERROR =========================| //

    | '{' conjunto_sentencias lista_llaves_cierre
        { notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }

    | lista_llaves_apertura // El error se presenta al detectar la lista de llaves de paertura y no, al finalizar el programa.
        { notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; } conjunto_sentencias '}'

    | '{' '}'
        { notifyError("El programa no posee ninguna sentencia."); errorState = true; }
    | // lambda //
        { notifyError("El programa no posee ningún cuerpo."); errorState = true; }
    | '{' error '}'
        { notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_llaves_apertura
    : '{' '{'
    | lista_llaves_apertura '{'
    ;

// --------------------------------------------------------------------------------------------------------------------

lista_llaves_cierre
    : '}' '}'
    | lista_llaves_cierre '}'
    ;

// --------------------------------------------------------------------------------------------------------------------
                
conjunto_sentencias
    : sentencia 
    | conjunto_sentencias sentencia 

    // |========================= REGLAS DE ERROR =========================| //

    | error ';'
        { notifyError("Error capturado a nivel de sentencia."); }
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
    : declaracion_variables
    | declaracion_funcion punto_y_coma_opcional
    ;

// --------------------------------------------------------------------------------------------------------------------

punto_y_coma_opcional
    : // lambda //
    | ';'
    ;

// --------------------------------------------------------------------------------------------------------------------

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
    ;

// ********************************************************************************************************************
// Sentencias ejecutables
// ********************************************************************************************************************

sentencia_ejecutable
    : invocacion_funcion ';'
        // Contexto en el que es invocada en línea.
        // No puede colocarse en las reglas propias porque se usa en factor.
        { notifyDetection("Invocación de función."); }
    | asignacion_simple
    | asignacion_multiple
    | sentencia_control
    | sentencia_retorno
    | impresion
    | lambda

    // |========================= REGLAS DE ERROR =========================| //
    
    | invocacion_funcion error
        { notifyError("La invocación a función debe terminar con ';'."); }
    //| asignacion_multiple_erronea
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia_control
    : if                                                                
    | iteracion                                                
    ;

// ********************************************************************************************************************
// Declaración de Variables
// ********************************************************************************************************************

declaracion_variables
    : UINT lista_variables ';'
        { notifyDetection("Declaración de variables."); }
    
    | UINT ID ';'
        {
            notifyDetection("Declaración de variable.");
            this.symbolTable.setType($2, SymbolType.UINT);
            this.symbolTable.setCategory($2, SymbolCategory.VARIABLE);
            this.symbolTable.setScope($2,scopeStack.asText());
        }
    
    // |========================= REGLAS DE ERROR =========================| //

    | UINT ID error
        {
            notifyError("La declaración de variable debe terminar con ';'.");
        }
    | UINT lista_variables error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT variable DASIG constante ';'
        {
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
    | UINT error
        {
            notifyError("Declaración de variables inválida.");
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
            $$ = $2;
        }
    | ID ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
            $$ = $2;
        }
    ;

// ********************************************************************************************************************
// Asignación Simple
// ********************************************************************************************************************

asignacion_simple
    : variable DASIG expresion ';'                              
        { 
            notifyDetection("Asignación simple."); 
            this.symbolTable.setValue(this.appendScope($1), $3);//yo no pondría esto, cuando $3 es una expresion queda mal
            
            reversePolish.addPolish($1);
            reversePolish.addPolish($2);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | variable DASIG expresion error
        // Nunca reduce por esta regla por alguna razón que se desconoce.
        { notifyError("Las asignaciones simples deben terminar con ';'."); }

    | variable expresion ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }

    | variable DASIG error
        { notifyError("Asignación simple inválida."); }
    ;

// ********************************************************************************************************************
// Asignación Múltiple
// ********************************************************************************************************************

//Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17).
asignacion_multiple 
    : variable asignacion_par constante ';'
        { 
            reversePolish.addPolish($1);
            reversePolish.addPolish($3);

            reversePolish.rearrangePairs();

            notifyDetection("Asignación múltiple."); 
        }
    | variable asignacion_par constante ',' lista_constantes ';'
        { 
            reversePolish.addPolish($1);
            reversePolish.addPolish($3);

            reversePolish.rearrangePairs();

            notifyWarning(String.format("Se descartarán las constantes posteriores a %s",$3)); //TP3
            notifyDetection("Asignación múltiple.");
        }

    // |========================= REGLAS DE ERROR =========================| //

    | variable asignacion_par constante error
        { notifyError("La asignación múltiple debe terminar con ';'."); }
    | variable asignacion_par constante ',' lista_constantes error
        { notifyError("La asignación múltiple debe terminar con ';'."); }
    | variable asignacion_par constante lista_constantes ';'
        { notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", $3)); }
    ;

// --------------------------------------------------------------------------------------------------------------------

asignacion_par
    : variable_con_coma asignacion_par constante_con_coma
    | '='
    ;

// --------------------------------------------------------------------------------------------------------------------

variable_con_coma
    : ',' variable
        { reversePolish.addPolish($2); }

    // |========================= REGLAS DE ERROR =========================| //

    | variable
        { notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", $1)); }
    ;

// --------------------------------------------------------------------------------------------------------------------

constante_con_coma
    : constante ','
        { reversePolish.addPolish($1); }

    // |========================= REGLAS DE ERROR =========================| //

    | constante
        { notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", $1)); }
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
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
        }
    ;

// ********************************************************************************************************************
// Expresiones
// ********************************************************************************************************************

expresion
    : termino
    | expresion operador_suma termino
        { 
            $$ = $3;
            reversePolish.addPolish($2);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | expresion operador_suma error
        {  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", $1, $2));
        }
    | expresion termino_simple
        {
            notifyError(String.format("Falta de operador entre operandos %s y %s.", $1, $2));
            $$ = $2;
        }
    // Se especifica únicamente el operador suma ya que, de contemplarse también el operador
    // de resta, no sería posible distinguir entre una constante negativa o un operando faltante.
    | '+' termino
        {
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",$2));
            $$ = $2;
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
        {   
            reversePolish.addPolish($2);
            $$ = $3; 
        }
    | factor

    // |========================= REGLAS DE ERROR =========================| //

    | termino operador_multiplicacion error
        {
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                $1, $2)
            );
        }
    | operador_multiplicacion factor 
        { notifyError(String.format("Falta operando previo a '%s %s'",$1,$2)); }
    ;

// -------------------------------------------------------------------------------------------------------------------

termino_simple
    : termino_simple operador_multiplicacion factor
        {   
            reversePolish.addPolish($2);
            $$ = $1;
        }
    | factor_simple

    // |========================= REGLAS DE ERROR =========================| //

    | termino_simple operador_multiplicacion error
        { notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",$1, $2)); }
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
    : variable error
        // Si no se coloca el token error, da reduce/reduce con asignación múltiple.
        {
            reversePolish.addPolish($1);
        }
    | constante
        {
            reversePolish.addPolish($1);
        }
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

// Factor que no contempla la posibilidad de constantes negativas.
factor_simple
    : variable
        {
            reversePolish.addPolish($1);
        }
    | CTE
        {
            reversePolish.addPolish($1);
        }
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

constante
    : CTE
    | '-' CTE
        {/* 
            $$ = "-" + $2;

            notifyDetection(String.format("Constante negativa: %s.",$$));

            if(isUint($$)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                $$ = null;
            } 

            modificarSymbolTable($$,$2);
        */
            notifyDetection(String.format("Constante negativa: -%s.",$2));

            if(isUint($2)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                $$ = null;
            } 

            this.symbolTable.replaceEntry($$,$2); 
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

variable
    : ID
        {
            if (!this.symbolTable.entryExists(this.scopeStack.asText()+":"+$1)) { //Si entra por aca, la variable debe ser local
                notifyError(String.format("Variable %s no declarada.",$1));
            }
        }
    | ID '.' ID
        { 
            if (!this.scopeStack.isReacheable($1)) 
                notifyError(String.format("Variable %s no declarada (no visible).",$3));
            else {
                if (!this.symbolTable.entryExists(this.scopeStack.getScopeRoad($1)+$3))
                    notifyError(String.format("Variable %s no declarada en el ámbito %s.",$3,$1));
            }

            $$ = $1 + "." + $3; 
        }
    ;

// ********************************************************************************************************************
// Condición
// ********************************************************************************************************************

condicion
    : '(' cuerpo_condicion ')'
        { 
            if (!errorState) {
                reversePolish.addFalseBifurcation();
                notifyDetection("Condición."); 
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("La condición no puede estar vacía."); errorState = true; }
    //| cuerpo_condicion
        //{ notifyDetection("La condición debe ir entre paréntesis."); }
    | cuerpo_condicion ')'
        { notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
    | '(' cuerpo_condicion error
        { notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
    ;
    
// --------------------------------------------------------------------------------------------------------------------

cuerpo_condicion
    : expresion comparador expresion
        {
            reversePolish.addPolish($2);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | expresion termino_simple
        { notifyError("Falta de comparador en comparación."); errorState = true; }
    | expresion operador_suma termino
        { notifyError("Falta de comparador en comparación."); errorState = true; }
    | termino
        { notifyError("Falta de comparador en comparación."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------
                                
comparador                      
    : '>'
        {
            $$ = ">";
        }
    | '<'
        {
            $$ = "<";
        }
    | EQ
    | LEQ
    | GEQ
    | NEQ

    // |========================= REGLAS DE ERROR =========================| //

    | '='
        { notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
    ;

// ********************************************************************************************************************
// Sentencia IF
// ********************************************************************************************************************

if 
    : IF condicion cuerpo_if
        { 
            if (!errorState) {
                reversePolish.completeSelection();
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
    ; 

    // |========================= REGLAS DE ERROR =========================| //

    | IF error
        { notifyError("Sentencia IF inválida."); }
    ;

cuerpo_if 
    : cuerpo_then rama_else ENDIF ';'

    // |========================= REGLAS DE ERROR =========================| //

    | cuerpo_then rama_else ENDIF error 
        { notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
    | cuerpo_then rama_else ';'
        { notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
    | rama_else ENDIF ';'
        { notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
    ;

cuerpo_then 
    : cuerpo_ejecutable
        { reversePolish.addInconditionalBifurcation(); }
    ;

// --------------------------------------------------------------------------------------------------------------------

rama_else
    : // lambda //
    | ELSE cuerpo_ejecutable

    // |========================= REGLAS DE ERROR =========================| //
    
    | ELSE 
        { notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
    ;

// ********************************************************************************************************************
// Sentencia WHILE
// ********************************************************************************************************************
iteracion 
    : { reversePolish.registerDoBody(); } do_while 
    ;


do_while                        
    : DO cuerpo_iteracion ';'
        { notifyDetection("Sentencia 'do-while'."); }  
    
    // |========================= REGLAS DE ERROR =========================| //

    | DO cuerpo_iteracion_recuperacion ';'
    | DO cuerpo_iteracion error
        { notifyError("La sentencia 'do-while' debe terminar con ';'."); }
    | DO cuerpo_iteracion_recuperacion error
        { notifyError("La sentencia 'do-while' debe terminar con ';'."); }
    | DO error
        { notifyError("Sentencia 'do-while' inválida."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_iteracion
    : cuerpo_do fin_cuerpo_iteracion
    ;

cuerpo_do 
    : cuerpo_ejecutable
        {}
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_iteracion_recuperacion
    : fin_cuerpo_iteracion
        { notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
    | cuerpo_ejecutable condicion
        { notifyError("Falta 'while'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

fin_cuerpo_iteracion
    : WHILE condicion
        { reversePolish.addTrueBifurcation(); }
    ;

// ********************************************************************************************************************
// Declaración de Función
// ********************************************************************************************************************

declaracion_funcion
    : inicio_funcion conjunto_parametros '{' conjunto_sentencias '}'
        {
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType($1, SymbolType.UINT);
                this.symbolTable.setCategory($1, SymbolCategory.FUNCTION);
                this.scopeStack.pop();    
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | inicio_funcion conjunto_parametros '{' '}'
        {
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Separadas con el objetivo de incrementar el ámbito cuando es detectada
// la declaración de una función.
inicio_funcion
    : UINT ID
        { this.scopeStack.push($2); $$ = $2; }
    
    // |========================= REGLAS DE ERROR =========================| //

    | UINT
        {
            this.scopeStack.push(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
            errorState = true;
        } 
    ;

// --------------------------------------------------------------------------------------------------------------------

conjunto_parametros
    : '(' lista_parametros ')'

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
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
    : semantica_pasaje UINT ID
        {
            if (!errorState) {
                this.symbolTable.setType($3, SymbolType.UINT);
                this.symbolTable.setCategory($3, ($1 == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope($3,scopeStack.asText());
            }

        }

    // |========================= REGLAS DE ERROR =========================| //

    | semantica_pasaje UINT 
        { notifyError("Falta de nombre de parámetro formal en declaración de función."); }
    | semantica_pasaje ID
        { notifyError("Falta de tipo de parámetro formal en declaración de función."); }
    ; 

// --------------------------------------------------------------------------------------------------------------------

semantica_pasaje
    : // lambda //
        { $$ = "CV"; }
    | CVR
        { $$ = "CVR"; }

    // |========================= REGLAS DE ERROR =========================| //

    | error
        { notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
    ;

// ********************************************************************************************************************
// Retorno
// ********************************************************************************************************************

sentencia_retorno
    : RETURN '(' expresion ')' ';'
        { 
            reversePolish.addPolish("return");
            notifyDetection("Sentencia RETURN."); 
        }
    
    // |========================= REGLAS DE ERROR =========================| //

    | RETURN '(' expresion ')' error
        { notifyError("La sentencia RETURN debe terminar con ';'."); }
    | RETURN '(' ')' ';'
        { notifyError("El retorno no puede estar vacío."); }
    | RETURN expresion ';'
        { notifyError("El resultado a retornar debe ir entre paréntesis."); }
    | RETURN error
        { notifyError("Sentencia RETURN inválida."); }
    ;

// ********************************************************************************************************************
// Invocación de Función
// ********************************************************************************************************************

invocacion_funcion
    : ID '(' lista_argumentos ')' 
        {
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
    : PRINT imprimible ';'
        {
            if (!errorState) {
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | PRINT imprimible error
        { notifyError("La sentencia 'print' debe finalizar con ';'."); errorState = false; }
    ;

// --------------------------------------------------------------------------------------------------------------------

imprimible
    : '(' elemento_imprimible ')'

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
    | elemento_imprimible
        { notifyError("El imprimible debe encerrarse entre paréntesis."); errorState = true; }
    | // lambda //
        { notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
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
    : parametro_lambda bloque_ejecutable argumento_lambda ';'
        { 
            if (!errorState) {
                notifyDetection("Expresión lambda.");
                this.symbolTable.setValue($1, $3);
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | parametro_lambda bloque_ejecutable argumento_lambda error
        { notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }

    | parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error
        { notifyError("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
    | parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error
        { notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
    | parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error
        { notifyError("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento_lambda
    : '(' factor ')'
        { $$ = $2; }

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }

    | // lambda //
        { notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

parametro_lambda
    : '(' UINT ID ')'
        {
            $$ = $3;
            this.symbolTable.setType($3, SymbolType.UINT);
        }
    ;

// ====================================================================================================================
// FIN DE REGLAS  
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private MessageCollector errorCollector, warningCollector;

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    this.lexer = lexer;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    this.symbolTable = SymbolTable.getInstance();
    
    this.scopeStack = new ScopeStack();
    this.reversePolish = ReversePolish.getInstance();

    // Descomentar la siguiente línea para activar el debugging.
    // yydebug = true;
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

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra con un token error.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
public void yyerror(String s) {

    // Silenciado, ya que los mensajes son manejados mediante otros métodos.
}

// --------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printWrapped(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private String appendScope(String lexema) {
    return lexema + ":" + this.scopeStack.asText();
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================