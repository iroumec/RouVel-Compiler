// ====================================================================================================================
// INICIO DE DECLARACIONES
// ====================================================================================================================

// ********************************************************************************************************************
// Package e Importaciones
// ********************************************************************************************************************

%{
    package parser;

    import lexer.Lexer;
    import semantic.Promise;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
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
%type <sval> expression, term, factor, term_simple, factor_simple, operador_suma, operador_multiplicacion,
                inicio_funcion, variable, constant, invocacion_funcion,
                lista_argumentos, argumento, comparador, semantica_pasaje, parametro_lambda, argumento_lambda,

%type <sval> program_name
%type <sval> identifier, list_of_identifiers
%type <sval> list_of_variables, list_of_constants, multiple_assignment

// ====================================================================================================================
// FIN DE DECLARACIONES
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE REGLAS
// ====================================================================================================================

program
    : program_name program_body
        {
            if (!errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", $1));
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | program_name statement_list
        { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }

    // El error se muestra al comienzo y no al final.
    | { notifyError("El programa requiere de un nombre."); } program_body

    | error { notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); } program_name program_body
        
    | error EOF
        { notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
    
    | EOF 
        { notifyError("El archivo está vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

program_name
    : ID
        {
            this.scopeStack.push($1);
            this.symbolTable.setCategory($1, SymbolCategory.PROGRAM);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", $1));
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

program_body
    : '{' statement_list '}'

    // |========================= REGLAS DE ERROR =========================| //

    | '{' statement_list close_brace_list
        { notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }

    | open_brace_list // El error se presenta al detectar la lista de llaves de paertura y no, al finalizar el programa.
        { notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; } statement_list '}'

    | '{' '}'
        { notifyError("El programa no posee ninguna sentencia."); errorState = true; }
    | // lambda //
        { notifyError("El programa no posee ningún cuerpo."); errorState = true; }
    | '{' error '}'
        { notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

open_brace_list
    : '{' '{'
    | open_brace_list '{'
    ;

// --------------------------------------------------------------------------------------------------------------------

close_brace_list
    : '}' '}'
    | close_brace_list '}'
    ;

// --------------------------------------------------------------------------------------------------------------------
                
statement_list
    : statement
    | statement_list statement 

    // |========================= REGLAS DE ERROR =========================| //

    | error ';'
        { notifyError("Error capturado a nivel de sentencia."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

statement
    : executable_statement 
    | declarative_statement
    ;

// ********************************************************************************************************************
// Sentencias declarativas
// ********************************************************************************************************************

declarative_statement
    : declaration_of_variables
    | declaracion_funcion punto_y_coma_opcional
    ;

// --------------------------------------------------------------------------------------------------------------------

punto_y_coma_opcional
    : // lambda //
    | ';'
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_ejecutable
    : executable_statement
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
    : executable_statement
    | conjunto_sentencias_ejecutables executable_statement
    ;

// ********************************************************************************************************************
// Sentencias ejecutables
// ********************************************************************************************************************

executable_statement
    : invocacion_funcion ';'
        // Contexto en el que es invocada en línea.
        // No puede colocarse en las reglas propias porque se usa en factor.
        { notifyDetection("Invocación de función."); }
    | asignacion_simple
    | multiple_assignment
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
    | do_while                                                
    ;

// ********************************************************************************************************************
// Declaración de Variables
// ********************************************************************************************************************

declaration_of_variables
    : UINT list_of_identifiers ';'
        {
            if (!errorState) {

                if ($2.split("\\s*,\\s*").length == 1) {
                    notifyDetection("Declaración de variable.");
                } else {
                    notifyDetection("Declaración de variables.");
                }
            } else {
                errorState = false;
            }
        }
        // |========================= REGLAS DE ERROR =========================| //

    | UINT list_of_identifiers error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT identifier DASIG constant ';'
        {
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
    | UINT error
        {
            notifyError("Declaración de variables inválida.");
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

list_of_identifiers
    : identifier
    | list_of_identifiers ',' identifier
        { $$ = $1 + ',' + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | list_of_identifiers ID
        {

            // Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.
            String[] variables = $1.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, $2));
            errorState = true;

            // Se agrega una coma para respetar el formato en reglas siguientes.
            // Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no
            // funcionaría adecuadamente.
            $$ = $1 + ',' + $2;
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Separado de variable ya que las acciones semánticas a realizar son distintas.
identifier
    : ID
        {
            this.symbolTable.setType($1, SymbolType.UINT);
            this.symbolTable.setCategory($1, SymbolCategory.VARIABLE);
            this.symbolTable.setScope($1, scopeStack.asText());
            $$ = this.scopeStack.appendScope($1);
        }
    ;

// ********************************************************************************************************************
// Asignación Simple
// ********************************************************************************************************************

asignacion_simple
    : variable DASIG expression ';'                              
        { 

            if (!errorState) {
                
                notifyDetection("Asignación simple.");

                // El valor aún no debe calcularse.
                // this.symbolTable.setValue($1, $3);

                reversePolish.addPolish($1);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish($2);
            } else {

                // Se decrementan las referencias, puesto a que se está frente a una referencia no válida.
                this.symbolTable.removeEntry($1);
                this.symbolTable.removeEntry($3);

                this.reversePolish.emptyTemporalPolishes();

                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | variable DASIG expression error
        // Nunca reduce por esta regla por alguna razón que se desconoce.
        { notifyError("Las asignaciones simples deben terminar con ';'."); }

    | variable expression ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }

    | variable DASIG error
        { notifyError("Asignación simple inválida."); }
    ;

// ********************************************************************************************************************
// Asignación Múltiple
// ********************************************************************************************************************

// Estas asignaciones pueden tener un menor número de elementos del lado izquierdo (tema 17).
multiple_assignment
    : list_of_variables list_of_constants ';'
        {
            if (!errorState) {
                // Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.
                String[] variables = $1.split("\\s*,\\s*");
                String[] constants = $2.split("\\s*,\\s*");

                if (variables.length > constants.length) {

                    notifyError(String.format(
                            "El número de variables (%d) del lado izquierdo de la asignación "
                            + "no puede superar el número de constantes (%d) en el lado derecho.",
                            variables.length, constants.length));

                } else {
                
                    if (variables.length < constants.length) {

                        notifyWarning(String.format(
                                "El número de variables (%d) en el lado izquierdo de la asignación "
                                + "es menor al número de constantes (%d) en el lado derecho de esta. "
                                + "Las constantes sobrantes serán descartadas.",
                                variables.length, constants.length));
                    }

                    // En este punto, la lista de variables y constantes tendrá la misma longitud.
                    for (int i = 0; i < variables.length; i++) {
                        
                        String variable = variables[i];
                        String constant = constants[i];   

                        this.symbolTable.setValue(variable, constant);
                        reversePolish.addPolish(variable);
                        reversePolish.addPolish(constant);
                        // Se agrega un DASIG ya que son varias asignaciones simples.
                        reversePolish.addPolish(":=");
                    }

                    notifyDetection("Asignación múltiple.");
                }
            } else {

                notifyError("Hola");
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | list_of_variables DASIG list_of_constants error
        { notifyError("La asignación múltiple debe terminar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Esta regla es recursiva a derecha para evitar shift/reduce.
list_of_variables
    : variable '='
    | variable ',' list_of_variables
        { $$ = $1 + ',' + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | variable list_of_variables
        {

            // Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.
            String[] variables = $1.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, $2));
            errorState = true;

            // Se agrega una coma para respetar el formato en reglas siguientes.
            // Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no
            // funcionaría adecuadamente.
            $$ = $1 + ',' + $2;
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

list_of_constants
    : constant
    | list_of_constants ',' constant
        { $$ = $1 + ',' + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | list_of_constants constant
        {
            String[] constants = $1.split("\\s*,\\s*");
            String lastConstant = constants[constants.length - 1];

            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                lastConstant, $2));
            errorState = true;

            // Se agrega una coma para respetar el formato en reglas siguientes.
            // Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las constantes no
            // funcionaría adecuadamente.
            $$ = $1 + ',' + $2;
        }
    ;

// ********************************************************************************************************************
// Expresiones
// ********************************************************************************************************************

expression
    : term
    | expression operador_suma term
        { 
            $$ = $3;
            reversePolish.addTemporalPolish($2);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | expression operador_suma error
        {  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", $1, $2));
        }
    | expression term_simple
        {
            notifyError(String.format("Falta de operador entre operandos %s y %s.", $1, $2));
            $$ = $2;
        }
    // Se especifica únicamente el operador suma ya que, de contemplarse también el operador
    // de resta, no sería posible distinguir entre una constante negativa o un operando faltante.
    | '+' term
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

term                         
    : term operador_multiplicacion factor
        {   
            reversePolish.addTemporalPolish($2);
            $$ = $3; 
        }
    | factor

    // |========================= REGLAS DE ERROR =========================| //

    | term operador_multiplicacion error
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

term_simple
    : term_simple operador_multiplicacion factor
        {   
            reversePolish.addTemporalPolish($2);
            $$ = $1;
        }
    | factor_simple

    // |========================= REGLAS DE ERROR =========================| //

    | term_simple operador_multiplicacion error
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
            // TODO: esto es un parche. Debe verse mejor después.
            this.errorCollector.removeLast(); // Debido a que se usa el token error.
            reversePolish.addTemporalPolish($1);
        }
    | constant
        {
            reversePolish.addTemporalPolish($1);
        }
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

// Factor que no contempla la posibilidad de constantes negativas.
factor_simple
    : variable
        {
            reversePolish.addTemporalPolish($1);
        }
    | CTE
        {
            reversePolish.addTemporalPolish($1);
        }
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

constant
    : CTE
    | '-' CTE
        {
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
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope($1))) { //Si entra por aca, la variable debe ser local
                errorState = true;
                notifyError(String.format("Variable %s no declarada.", $1));
            } else {
                
                // A la entrada sin el scope, se le agrega el scope.
                // Se combina con otra entrada en caso de coincidir el scope.
                this.symbolTable.setScope($1, scopeStack.asText());
                $$ = this.scopeStack.appendScope($1);
            }
        }
    | ID '.' ID
        { 
            String scopedVariable = $3 + this.scopeStack.getScopeRoad($1);

            if (!this.scopeStack.isReacheable($1)) {
                errorState = true;
                notifyError(String.format("Variable %s no declarada (no visible).",$3));
            } else if (!this.symbolTable.entryExists(scopedVariable)) {
                errorState = true;
                notifyError(String.format("Variable '%s' no declarada en el ámbito '%s'.",$3,$1));
            }

            $$ = scopedVariable;

            // Se remplaza el identificador sin ámbito por su versión con ámbito.
            this.symbolTable.replaceEntry($3, $$); 
        }
    ;

// ********************************************************************************************************************
// Condición
// ********************************************************************************************************************

condicion
    : '(' cuerpo_condicion ')'
        { 
            if (!errorState) {
                notifyDetection("Condición."); 
            } else {
                errorState = false; // TODO: creo que no debería reiniciarse el erro acá.
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
    : expression comparador expression
        {
            this.reversePolish.makeTemporalPolishesDefinitive();
            this.reversePolish.addPolish($2);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | expression term_simple
        { notifyError("Falta de comparador en comparación."); errorState = true; }
    | expression operador_suma term
        { notifyError("Falta de comparador en comparación."); errorState = true; }
    | term
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
    : if_start cuerpo_if
        { 
            if (!errorState) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
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

// --------------------------------------------------------------------------------------------------------------------

if_start
    : IF condicion
        {
            this.reversePolish.addSeparation("Entering 'if' body...");
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("FB");
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_if 
    : cuerpo_ejecutable rama_else ENDIF ';'

    // |========================= REGLAS DE ERROR =========================| //

    | cuerpo_ejecutable rama_else ENDIF error 
        { notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
    | cuerpo_ejecutable rama_else ';'
        { notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
    | rama_else ENDIF ';'
        { notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

rama_else
    : // lambda //
    | else_start cuerpo_ejecutable

    // |========================= REGLAS DE ERROR =========================| //
    
    | else_start 
        { notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Refactorización necesaria para ejecución temprana de acciones semánticas.
else_start
    : ELSE
        {
            // Se obtiene la promesa del cuerpo then.
            Promise promise = this.reversePolish.getLastPromise();

            // Se promete un nuevo punto de bifurcación.
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("IB");

            // Se cumple la promesa obtenida al comienzo.
            // Es necesario que se realice así para respetar los índices de la polaca.
            this.reversePolish.fulfillPromise(promise);

            this.reversePolish.addSeparation("Entering 'else' body...");
        }
    ;

// ********************************************************************************************************************
// Sentencia WHILE
// ********************************************************************************************************************

do_while                        
    : do_while_start cuerpo_iteracion ';'
        {
            if (!errorState) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            } else {
                errorState = false;
            }
        }  
    
    // |========================= REGLAS DE ERROR =========================| //

    | do_while_start cuerpo_iteracion error
        { replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
    | do_while_start error
        { notifyError("Sentencia 'do-while' inválida."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Refactorización necesaria para la ejecución de acciones semánticas.
do_while_start
    : DO
        {
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.stackBifurcationPoint();
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_iteracion
    : cuerpo_ejecutable fin_cuerpo_iteracion

    // |========================= REGLAS DE ERROR =========================| //

    | fin_cuerpo_iteracion
        { notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
    | cuerpo_ejecutable condicion
        { notifyError("Falta 'while'."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

fin_cuerpo_iteracion
    : WHILE condicion
    ;

// ********************************************************************************************************************
// Declaración de Función
// ********************************************************************************************************************

declaracion_funcion
    : inicio_funcion conjunto_parametros '{' statement_list '}'
        {
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType($1, SymbolType.UINT);
                this.symbolTable.setCategory($1, SymbolCategory.FUNCTION);
                this.scopeStack.pop();
                this.symbolTable.setScope($1, this.scopeStack.asText());
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", $1));
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
        {
            $$ = $2;
            this.scopeStack.push($2);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", $2));
        }
    
    // |========================= REGLAS DE ERROR =========================| //

    | UINT
        {
            this.scopeStack.push("error");
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
    : RETURN '(' expression ')' ';'
        {
            if (!errorState) {
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("return");
                notifyDetection("Sentencia 'return'.");
            } else {
                errorState = false;
            }
        }
    
    // |========================= REGLAS DE ERROR =========================| //

    | RETURN '(' expression ')' error
        { notifyError("La sentencia RETURN debe terminar con ';'."); }
    | RETURN '(' ')' ';'
        { notifyError("El retorno no puede estar vacío."); }
    | RETURN expression ';'
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
    : expression FLECHA ID
        { $$ = $1 + $2 + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | expression
        { notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
    ;

// ********************************************************************************************************************
// Impresión
// ********************************************************************************************************************

impresion
    : PRINT imprimible ';'
        {
            if (!errorState) {
                // Se añaden las polacas correspondiente al imprimible.
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | PRINT imprimible error
        {
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

imprimible
    : '(' elemento_imprimible ')'

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
    | elemento_imprimible
        {
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
    | // lambda //
        { notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
    ;

// --------------------------------------------------------------------------------------------------------------------

elemento_imprimible
    : STR
        {
            reversePolish.addTemporalPolish($1);
        }
    | expression
    ;

// ********************************************************************************************************************
// Expresiones Lambda
// ********************************************************************************************************************

lambda
    : parametro_lambda bloque_ejecutable argumento_lambda ';'
        { 
            if (!errorState) {

                // Se llena el punto de agregación reservado con la asignación
                // del argumento al parámetro.
                this.reversePolish.fillLastAggregatePoint($1, $3, ":=");

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");
            } else {
                errorState = false;
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | parametro_lambda bloque_ejecutable argumento_lambda error
        { notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }

    | parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error
        { replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
    | parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error
        { replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
    | parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error
        { replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
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
    : '(' UINT identifier ')'
        {
            $$ = $3;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
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

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

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
private int yylex() {

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra con un token error.
 *
 * En caso de que el error sea tratado en la gramática, este será remplazado
 * posteriormente por un mensaje de error más apropiado.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
private void yyerror(String s) {
    notifyError(s);
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyDetection(String message) {
    Printer.printWrapped(String.format(
        "DETECCIÓN SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    errorCollector.replaceLastWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================