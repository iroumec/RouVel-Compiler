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
    import java.util.Stack;
    import utilities.Printer;
    import common.SymbolTable;
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
                lista_variables, lista_constantes, variable, constante, invocacion_funcion, lista_argumentos, argumento

// ====================================================================================================================
// FIN DE DECLARACIONES
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE REGLAS
// ====================================================================================================================

programa
    : nombre_programa cuerpo_programa
        { notifyDetection("Programa."); }

    // |========================= REGLAS DE ERROR =========================| //

    | nombre_programa cuerpo_programa_recuperacion

    | nombre_programa conjunto_sentencias
        { notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }

    // El error se muestra al comienzo y no al final.
    | { notifyError("El programa requiere de un nombre."); } programa_sin_nombre

    | error { notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); } nombre_programa cuerpo_programa
        
    | error EOF
        { notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
    
    | EOF 
        { notifyError("El archivo está vacío."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

nombre_programa
    : ID
        { pushScope($1); }
    ;

// --------------------------------------------------------------------------------------------------------------------

programa_sin_nombre
    : cuerpo_programa
    | cuerpo_programa_recuperacion
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_programa
    : '{' conjunto_sentencias '}'
    ;

// --------------------------------------------------------------------------------------------------------------------

// Marca la distinción entre un programa válido y uno inválido.
cuerpo_programa_recuperacion
    : '{' conjunto_sentencias lista_llaves_cierre
        { notifyError("Se encontraron múltiples llaves al final del programa."); }

    | lista_llaves_apertura // El error se presenta al detectar la lista de llaves de paertura y no, al finalizar el programa.
        { notifyError("Se encontraron múltiples llaves al comienzo del programa."); } conjunto_sentencias '}'

    | '{' '}'
        { notifyError("El programa no posee ninguna sentencia."); }
    | // lambda //
        { notifyError("El programa no posee ningún cuerpo."); }
    | '{' error '}'
        { notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
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

    | error '}'
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
    ;

// --------------------------------------------------------------------------------------------------------------------

sentencia_control
    : if                                                                
    | do_while                                                
    ;

// ********************************************************************************************************************
// Declaración de Variables
// ********************************************************************************************************************

declaracion_variables
    : UINT lista_variables ';'
        { notifyDetection("Declaración de variables."); }
    
    | UINT ID ';'
        { notifyDetection("Declaración de variable."); }
    
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
            { $$ = $2; }
        }
    | ID ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
            { $$ = $2; }
        }
    ;

// ********************************************************************************************************************
// Asignación Simple
// ********************************************************************************************************************

asignacion_simple
    : variable DASIG expresion ';'                              
        { notifyDetection("Asignación simple."); }

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
        { notifyDetection("Asignación múltiple."); }
    | variable asignacion_par constante ',' lista_constantes ';'
        { notifyDetection("Asignación múltiple."); }

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
    | variable
        { notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", $1)); }
    ;

// --------------------------------------------------------------------------------------------------------------------

constante_con_coma
    : constante ','
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
        { $$ = $3; }

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
        { $$ = $3; }
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
        { $$ = $1; }
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
        // Si no se coloca el token error, da shift/reduce con asignación múltiple.
    | constante
    | invocacion_funcion
    ;

// --------------------------------------------------------------------------------------------------------------------

// Factor que no contempla la posibilidad de constantes negativas.
factor_simple
    : variable
    | CTE
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

            modificarSymbolTable($$,$2); 
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

variable
    : ID
    | ID '.' ID
        { $$ = $1 + "." + $3; }
    ;

// ********************************************************************************************************************
// Condición
// ********************************************************************************************************************

condicion
    : '(' cuerpo_condicion ')'
        { notifyDetection("Condición."); }

    // |========================= REGLAS DE ERROR =========================| //

    | '(' ')'
        { notifyError("La condición no puede estar vacía."); }
    //| cuerpo_condicion
        //{ notifyDetection("La condición debe ir entre paréntesis."); }
    | cuerpo_condicion ')'
        { notifyError("Falta apertura de paréntesis en condición."); }
    | '(' cuerpo_condicion error
        { notifyError("Falta cierre de paréntesis en condición."); }
    ;
    
// --------------------------------------------------------------------------------------------------------------------

cuerpo_condicion
    : expresion comparador expresion

    // |========================= REGLAS DE ERROR =========================| //

    | expresion termino_simple
        { notifyError("Falta de comparador en comparación."); }
    | expresion operador_suma termino
        { notifyError("Falta de comparador en comparación."); }
    | termino
        { notifyError("Falta de comparador en comparación."); }
    ;

// --------------------------------------------------------------------------------------------------------------------
                                
comparador                      
    : '>'
    | '<'
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
    : IF condicion cuerpo_ejecutable rama_else ENDIF ';'
        { notifyDetection("Sentencia IF."); }

    // |========================= REGLAS DE ERROR =========================| //

    | IF condicion cuerpo_ejecutable rama_else ENDIF error
        { notifyError("La sentencia IF debe terminar con ';'."); }
    | IF condicion cuerpo_ejecutable rama_else ';'
        { notifyError("La sentencia IF debe finalizar con 'endif'."); }
    | IF condicion rama_else ENDIF ';'
        { notifyError("Falta el bloque de sentencias del IF."); }
    | IF error
        { notifyError("Sentencia IF inválida."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

rama_else
    : // lambda //
    | ELSE cuerpo_ejecutable

    // |========================= REGLAS DE ERROR =========================| //
    
    | ELSE 
        { notifyError("Falta el bloque de sentencias del ELSE."); }
    ;

// ********************************************************************************************************************
// Sentencia WHILE
// ********************************************************************************************************************

do_while                        
    : DO cuerpo_do ';'
        { notifyDetection("Sentencia 'do-while'."); }  
    
    // |========================= REGLAS DE ERROR =========================| //

    | DO cuerpo_do_recuperacion ';'
    | DO cuerpo_do error
        { notifyError("La sentencia 'do-while' debe terminar con ';'."); }
    | DO cuerpo_do_recuperacion error
        { notifyError("La sentencia 'do-while' debe terminar con ';'."); }
    | DO error
        { notifyError("Sentencia 'do-while' inválida."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_do
    : cuerpo_ejecutable fin_cuerpo_do
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_do_recuperacion
    : fin_cuerpo_do
        { notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
    | cuerpo_ejecutable condicion
        { notifyError("Falta 'while'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

fin_cuerpo_do
    : WHILE condicion
    ;

// ********************************************************************************************************************
// Declaración de Función
// ********************************************************************************************************************

declaracion_funcion
    : inicio_funcion conjunto_parametros cuerpo_funcion
        {
            notifyDetection("Declaración de función.");
            popScope();    
        }

    // |========================= REGLAS DE ERROR =========================| //

    | inicio_funcion conjunto_parametros '{' '}'
        { popScope(); }

    | inicio_funcion_sin_nombre conjunto_parametros cuerpo_funcion
        { popScope(); }

    | inicio_funcion_sin_nombre conjunto_parametros '{' '}'
        {
            notifyError("El cuerpo de la función no puede estar vacío.");
            popScope();
        }
    ;

// --------------------------------------------------------------------------------------------------------------------

// Separadas con el objetivo de incrementar el ámbito cuando es detectada
// la declaración de una función.
inicio_funcion
    : UINT ID
        { pushScope($2); }
    ;

// --------------------------------------------------------------------------------------------------------------------

inicio_funcion_sin_nombre
    : UINT
        {
            pushScope(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
        } 
    ;

// --------------------------------------------------------------------------------------------------------------------

cuerpo_funcion
    : '{' conjunto_sentencias '}'
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
    : semantica_pasaje UINT variable

    // |========================= REGLAS DE ERROR =========================| //

    | semantica_pasaje UINT 
        { notifyError("Falta de nombre de parámetro formal en declaración de función."); }
    | semantica_pasaje variable
        { notifyError("Falta de tipo de parámetro formal en declaración de función."); }
    ; 

// --------------------------------------------------------------------------------------------------------------------

semantica_pasaje
    : // lambda //
    | CVR

    // |========================= REGLAS DE ERROR =========================| //

    | error
        { notifyError("Semántica de pasaje de parámetro inválida."); }
    ;

// ********************************************************************************************************************
// Retorno
// ********************************************************************************************************************

sentencia_retorno
    : RETURN '(' expresion ')' ';'
        { notifyDetection("Sentencia RETURN."); }
    
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
        { notifyDetection("Sentencia 'print'."); }

    // |========================= REGLAS DE ERROR =========================| //

    | PRINT imprimible error
        { notifyError("La sentencia 'print' debe finalizar con ';'."); }
    | PRINT imprimible_recuperacion ';'
    | PRINT imprimible_recuperacion error
        { notifyError("La sentencia 'print' debe finalizar con ';'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

imprimible
    : '(' elemento_imprimible ')'
    ;

// --------------------------------------------------------------------------------------------------------------------

imprimible_recuperacion
    : '(' ')'
        { notifyError("La sentencia 'print' requiere de al menos un argumento."); }

    | elemento_imprimible
        { notifyError("El imprimible debe encerrarse entre paréntesis."); }
    | // lambda //
        { notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
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
        { notifyDetection("Expresión lambda."); }

    // |========================= REGLAS DE ERROR =========================| //

    | parametro_lambda bloque_ejecutable argumento_lambda error
        { notifyError("La expresión 'lambda' debe terminar con ';'."); }
    | parametro_lambda bloque_ejecutable argumento_lambda_recuperacion ';'
    | parametro_lambda bloque_ejecutable argumento_lambda_recuperacion error
        { notifyError("La expresión 'lambda' debe terminar con ';'."); }

    | parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error
        { notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
    | parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error
        { notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
    | parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error
        { notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
    | parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda_recuperacion error
        { notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
    | parametro_lambda conjunto_sentencias_ejecutables argumento_lambda_recuperacion error
        { notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
    | parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda_recuperacion error
        { notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento_lambda
    : '(' factor ')'
    ;

// --------------------------------------------------------------------------------------------------------------------

argumento_lambda_recuperacion
    :  '(' ')'
        { notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }

    | // lambda //
        { notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
    ;

// --------------------------------------------------------------------------------------------------------------------

parametro_lambda
    : '(' UINT ID ')'
    ;

// ====================================================================================================================
// FIN DE REGLAS  
// ====================================================================================================================

%%

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de errores y warning detectados.
private MessageCollector errorCollector, warningCollector;

// Generacion de código
private final ReversePolish reversePolish;
private final Stack<String> scopes;

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    this.lexer = lexer;

    this.scopes = new Stack<>();
    this.reversePolish = new ReversePolish();

    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    
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

void pushScope(String scope) {
    this.scopes.push(scope);
}

// --------------------------------------------------------------------------------------------------------------------

void popScope() {
    this.scopes.pop();
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isUint(String number) {
    return !number.contains(".");
}

// --------------------------------------------------------------------------------------------------------------------

/*public void modificarSymbolTable(String lexemaNuevo, String lexemaAnterior) {
    SymbolTable.getInstance().decrementarReferencia(lexemaAnterior);
    if (lexemaNuevo != null) {
        Symbol symbol = SymbolTable.getInstance().getSymbol(lexemaAnterior).getNegative();
        SymbolTable.getInstance().addEntry(lexemaNuevo,symbol);
    }
}*/

public void modificarTabla(String lexema) {
    SymbolTable.getInstance().replaceEntry(lexema);
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================