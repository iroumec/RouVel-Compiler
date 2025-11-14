// ====================================================================================================================
// INICIO DE DECLARACIONES
// ====================================================================================================================

// ********************************************************************************************************************
// Package e Importaciones
// ********************************************************************************************************************

%{
    package parser;

    import lexer.Lexer;
    import common.Monitor;
    import java.util.Arrays;
    import semantic.Promise;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
%}

// ********************************************************************************************************************
// Declaraciones de Tipos de Valores
// ********************************************************************************************************************

/*
    Declaración de los tipos de valores. Se le informa a Yacc que tendrá que trabajar con valores de tipo String. Esto
    se realiza para que no dé error el highlighter de la gramática, ya que está pensado para byacc para C. Al generar
    la clase Parser.java, debe comentarse para no romper el código, ya que inserta un typedef en este.
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
%type <sval> program_name
%type <sval> function_start
%type <sval> variable, constant
%type <sval> identifier, list_of_identifiers
%type <sval> inicio_funcion, invocacion_funcion
%type <sval> parametro_lambda, argumento_lambda
%type <sval> lista_argumentos, argumento, semantica_pasaje
%type <sval> operador_suma, operador_multiplicacion, comparador
%type <sval> expression, term, factor, term_simple, factor_simple
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
            if (!this.errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addPolish("end-label");
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", $1));
            } else {
                this.errorState = false;
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
            this.reversePolish.addPolish($1);
            this.reversePolish.addPolish("program-label");
            this.reversePolish.recordSafeState();
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
        { this.treatErrorState(); }
    | statement_list statement
        { this.treatErrorState(); }

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

// ********************************************************************************************************************
// Sentencias ejecutables
// ********************************************************************************************************************

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

// --------------------------------------------------------------------------------------------------------------------

executable_statement
    : asignacion_simple
    | inline_function_invocation
    | multiple_assignment
    | sentencia_control
    | sentencia_retorno
    | impresion
    | lambda
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
            if (this.statementAppearsInValidState()) {

                if ($2.split("\\s*,\\s*").length == 1) {
                    notifyDetection("Declaración de variable.");
                } else {
                    notifyDetection("Declaración de variables.");
                }
            } else {
                this.treatInvalidState("declaración de variables");
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

            // Se agrega una coma para respetar el formato en reglas siguientes. Si esta no se agregase, de entrar
            // nuevamente a esta regla, la separación de las variables no funcionaría adecuadamente.
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

            if (this.statementAppearsInValidState()) {
                
                notifyDetection("Asignación simple.");

                // El valor aún no debe calcularse.
                // this.symbolTable.setValue($1, $3);

                reversePolish.addPolish($1);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish($2);
            } else {

                this.treatInvalidState("asignación simple");

                // Se decrementan las referencias, puesto a que se está frente a una referencia no válida.
                this.symbolTable.removeEntry($1);
                this.symbolTable.removeEntry($3);
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
            if (this.statementAppearsInValidState()) {
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

                        // La asignación del valor no se realiza acá. Eso se hace en el assembler.
                        // this.symbolTable.setValue(variable, constant);

                        reversePolish.addPolish(variable);
                        reversePolish.addPolish(constant);
                        // Se agrega un DASIG ya que son varias asignaciones simples.
                        reversePolish.addPolish(":=");
                    }

                    notifyDetection("Asignación múltiple.");
                }
            } else {
                this.treatInvalidState("asignación múltiple");
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | list_of_variables list_of_constants error
        { notifyError("La asignación múltiple debe terminar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Esta regla es recursiva a derecha para evitar shift/reduce.
list_of_variables
    : variable '='
    | variable ',' list_of_variables
        { $$ = $1 + ',' + $3; }

    // |========================= REGLAS DE ERROR =========================| //

    | variable error list_of_variables
        {

            // Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.
            String[] variables = $1.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, $3));
            errorState = true;

            // Se agrega una coma para respetar el formato en reglas siguientes.
            // Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no
            // funcionaría adecuadamente.
            $$ = $1 + ',' + $3;
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
    : variable
        {
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
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope($1))) {
                // De entrar acá, la variable debe ser local.
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
            if (this.statementAppearsInValidState()) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia 'if'."); 
            } else {
                this.treatInvalidState("Sentencia 'if'");
            }

            // Se está saliendo del if más externo.
            if (this.selectionDepth == 1) {
                if (this.returnsNeeded == this.returnsFound) {
                    this.isThereReturn = true;
                }
            }

            this.selectionDepth--;
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
            this.returnsNeeded++;
            this.selectionDepth++;
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
        {
            // Se decrementa la cantidad de retornos que se requieren si el if está solo.
            this.returnsNeeded--;

            // Se decrementa la cantidad de returns hallados.
            // REVISAR QUÉ PASA SI DENTRO DEL IF HAY VARIOS RETURNS.
            this.returnsFound--;
        }
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
            if (this.statementAppearsInValidState()) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
                this.reversePolish.addPolish("end-loop-label");
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            } else {
                this.treatInvalidState("Sentencia 'do-while'");
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

            this.reversePolish.addLabel("loop-label");
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
    : inicio_funcion conjunto_parametros '{' function_body '}'
        {
            if (!this.errorState) {

                if (this.isThereReturn) {

                    this.isThereReturn = false;
                    notifyDetection("Declaración de función.");

                    this.reversePolish.closeFunctionDeclaration(this.scopeStack.appendScope($1));

                    this.reversePolish.addPolish("end-label");
                    this.symbolTable.setType($1, SymbolType.UINT);
                    this.symbolTable.setCategory($1, SymbolCategory.FUNCTION);
                    this.scopeStack.pop();
                    this.symbolTable.setScope($1, this.scopeStack.asText());
                    this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", $1));
                } else {
                    notifyError("La función necesita, en todos los casos, retornar un valor.");
                    this.errorState = true;
                }
            } else {
                this.treatInvalidState("Declaración de función");
            }

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }

    // |========================= REGLAS DE ERROR =========================| //

    | inicio_funcion conjunto_parametros '{' '}'
        {
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Separadas con el objetivo de incrementar el ámbito cuando es detectada
// la declaración de una función.
inicio_funcion
    : UINT ID
        {

            this.reversePolish.startFunctionDeclaration($2 + ":" + this.scopeStack.asText());

            $$ = $2;
            this.functionLevel++;
            this.scopeStack.push($2);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", $2));

            // Se crea un operador para la función, mediante el operador 'label'.
            this.reversePolish.addPolish($2);
            this.reversePolish.addPolish("function-label");

            this.returnsNeeded = 1;
        }
    
    // |========================= REGLAS DE ERROR =========================| //

    | UINT
        {
            errorState = true;
            this.functionLevel++;
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");

            this.returnsNeeded = 1;
        } 
    ;

// --------------------------------------------------------------------------------------------------------------------

// Why isn't used `statement_list`? Because of its semantic actions.
// We don't want in the scope of a function.
function_body
    : statement
    | function_body statement
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
            if (this.statementAppearsInValidState()) {
                this.symbolTable.setType($3, SymbolType.UINT);
                this.symbolTable.setCategory($3, ($1 == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope($3,scopeStack.asText());

                this.reversePolish.addParameter($3, "uint", $1);
            } else {
                this.treatInvalidState("Parámetro formal");
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

            if (statementAppearsInValidState()) {

                if (this.functionLevel > 0) {

                    this.reversePolish.makeTemporalPolishesDefinitive();
                    reversePolish.addPolish("return");
                    notifyDetection("Sentencia 'return'.");

                    this.returnsFound++;

                    if (this.selectionDepth == 0) {
                        this.isThereReturn = true;
                    }

                } else {
                    notifyError("La sentencia 'return' no está permitida fuera de la declaración de una función.");
                }
            } else {

                this.reversePolish.emptyTemporalPolishes();

                this.treatInvalidState("return");
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

inline_function_invocation
    : invocacion_funcion ';'
        { notifyDetection("Invocación de función."); }

    // |========================= REGLAS DE ERROR =========================| //

    | invocacion_funcion error
        { notifyError("La invocación a función debe terminar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

invocacion_funcion
    : function_start '(' lista_argumentos ')' 
        {
            if (this.statementAppearsInValidState()) {

                $$ = $1 + '(' + $3 + ')';

                this.reversePolish.closeFunctionCall();
            } else {
                this.treatInvalidState("Invocación de función");

                this.reversePolish.discardFunctionCall();
            }
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

function_start
    : variable
        {
            String[] parts = $1.split("\\s*:\\s*");
            String functionInvocationIdentifier;

            // Se pasa el nombre de la función al final.
            // Si se tiene A:B:C:D, se obtiene B:C:D.

            if (parts.length > 1) {
                String result = String.join(":", 
                    Arrays.copyOfRange(parts, 1, parts.length)) + ":" + parts[0];
                functionInvocationIdentifier = result;
            } else {
                functionInvocationIdentifier = $1; // Solo hay un elemento.
            }

            this.reversePolish.startFunctionCall($1);
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
        {

            this.reversePolish.addArgument($3);
        }

    // |========================= REGLAS DE ERROR =========================| //

    | expression
        { notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
    ;

// ********************************************************************************************************************
// Impresión
// ********************************************************************************************************************

impresion
    : PRINT imprimible ';'
        {
            if (this.statementAppearsInValidState()) {
                // Se añaden las polacas correspondiente al imprimible.
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                this.reversePolish.emptyTemporalPolishes();
                this.treatInvalidState("Sentencia 'print'");
            }
        }

    // |========================= REGLAS DE ERROR =========================| //

    | PRINT imprimible error
        {
            errorState = true;
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
        { reversePolish.addTemporalPolish($1); }
    | expression
    ;

// ********************************************************************************************************************
// Expresiones Lambda
// ********************************************************************************************************************

lambda
    : parametro_lambda bloque_ejecutable argumento_lambda ';'
        { 
            if (this.statementAppearsInValidState()) {

                // Se llena el punto de agregación reservado con la asignación
                // del argumento al parámetro.
                this.reversePolish.fillLastAggregatePoint($1, $3, ":=");

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");

            } else {
                this.treatInvalidState("Expresión 'lambda'");
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
private final Monitor monitor;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;

// --------------------------------------------------------------------------------------------------------------------

private int functionLevel;
// Si esto está activa, todas las instrucciones que se encuentran no serán pasadas a código intermedio.
private boolean isThereReturn;

// --------------------------------------------------------------------------------------------------------------------

private int returnsFound;
private int returnsNeeded;
private int selectionDepth;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.monitor = Monitor.getInstance();
    this.symbolTable = SymbolTable.getInstance();

    this.functionLevel = 0;
    this.isThereReturn = false;
    
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

public ReversePolish getReversePolish() {
    return this.reversePolish;
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
        monitor.getLineNumber(), message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    monitor.addWarning(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        monitor.getLineNumber(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    monitor.addError(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    monitor.replaceLastErrorWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean statementAppearsInValidState() {

    return !isThereReturn && !errorState;
}

// --------------------------------------------------------------------------------------------------------------------

private void treatInvalidState(String statementName) {

    if (isThereReturn) {
        this.showOmittedStatementNotification(statementName);
    }

    if (errorState) {
        this.recoverFromErrorState();
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void showOmittedStatementNotification(String statementName) {
    notifyWarning(statementName + " no alcanzable. No se ejecutará.");
}

// --------------------------------------------------------------------------------------------------------------------

private void treatErrorState() {

    if (!errorState) {
        this.reversePolish.recordSafeState();
    } else {
        this.recoverFromErrorState();
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void recoverFromErrorState() {

    this.reversePolish.emptyTemporalPolishes();
    this.reversePolish.returnToLastSafeState();
    this.errorState = false;
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================