package parser;
import lexer.Lexer;
import common.Token;

//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";










public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short STR=259;
public final static short EQ=260;
public final static short GEQ=261;
public final static short LEQ=262;
public final static short NEQ=263;
public final static short DASIG=264;
public final static short FLECHA=265;
public final static short PRINT=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short ENDIF=269;
public final static short UINT=270;
public final static short CVR=271;
public final static short DO=272;
public final static short WHILE=273;
public final static short RETURN=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    4,    4,    7,    7,    5,
    5,    8,    9,    9,   11,   14,   14,   12,   12,   15,
   15,    3,    3,   16,   16,   16,   16,   16,   16,   16,
   18,   19,   19,   26,   26,   27,   27,   27,   27,   27,
   27,   24,   24,   28,   28,   25,   21,   29,   29,   23,
   23,   23,   30,   30,   31,   31,   31,   32,   32,   13,
   13,   10,   10,   22,   22,    6,   34,   34,   20,   33,
   33,   35,   35,   35,   37,   37,   36,   38,   38,   38,
   17,   39,   39,   40,
};
final static short yylen[] = {                            2,
    4,    1,    2,    1,    1,    3,    2,    0,    1,    1,
    3,    3,    1,    3,    8,    1,    1,    3,    2,    1,
    2,    2,    2,    1,    1,    1,    1,    1,    1,    1,
    3,    1,    1,    3,    0,    1,    1,    1,    1,    1,
    1,    7,    7,    0,    2,    6,    4,    1,    1,    3,
    1,    3,    1,    1,    3,    1,    3,    1,    1,    1,
    1,    1,    2,    1,    3,    8,    1,    0,    2,    1,
    0,    1,    3,    1,    2,    2,    3,    0,    1,    1,
    4,    1,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    4,    5,    0,    0,   26,   30,    0,   24,
   25,   27,   28,   29,    0,   32,   33,    0,    0,    0,
    0,    0,    0,    0,   17,   16,    0,    0,   62,    0,
   61,   56,   60,    0,    0,    0,    1,    3,    0,    0,
    9,    7,   23,   22,    0,    0,    0,   82,   65,   48,
    0,    0,    0,    0,    0,    6,   19,   20,    0,    0,
   63,   53,   54,    0,   58,   59,    0,    0,   11,    0,
   13,    0,    0,    0,   81,   47,   38,   40,   39,   41,
   36,   37,    0,    0,   80,   79,    0,    0,    0,   72,
   74,    0,   18,   21,    0,   52,    0,   57,   55,    0,
    0,   84,   83,    0,    0,   76,    0,    0,    0,    0,
    0,   14,    0,    0,    0,   73,   77,   46,    0,   45,
   43,   42,    0,    0,    0,   66,   15,
};
final static short yydgoto[] = {                          2,
   11,   12,   13,   14,   15,   16,   52,   17,   80,   41,
   18,   36,   42,   37,   69,   19,   20,   21,   22,   23,
   24,   43,   56,   26,   27,   64,   93,  124,   62,   74,
   45,   77,   98,  134,   99,  100,  101,  102,   57,   58,
};
final static short yysindex[] = {                      -226,
  -82,    0,  -18,    8,    5,    9, -204,   -7,  -33, -207,
  -40,    0,    0,    0,  -23,   23,    0,    0,  -53,    0,
    0,    0,    0,    0, -200,    0,    0,  -33, -188,   53,
  -33,   43,  -19,   -4,    0,    0, -189,   55,    0, -159,
    0,    0,    0,  -16,   19, -155,    0,    0, -154,  -22,
    0,    0,    0,    0,  -33,  -42,   11,    0,    0,    0,
  -16,   65,   45,   67,  -36,    0,    0,    0,    7,   69,
    0,    0,    0,   28,    0,    0,   46,   70,    0,   66,
    0,  -16, -228,  -33,    0,    0,    0,    0,    0,    0,
    0,    0,  -33,   -7,    0,    0, -228,   71,   74,    0,
    0, -157,    0,    0,  -33,    0,   19,    0,    0,   -8,
  -22,    0,    0,  -16, -149,    0,   -3, -228, -143,   81,
   83,    0,   -7, -237,  -18,    0,    0,    0,  -33,    0,
    0,    0,  -18,   -1,   85,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -35,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -29,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,   -9,    0,    0,    0,    0,    0,   15,    0,    0,
    0,    0,    0,  -52,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   87,    0,    0,    0,  -37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -49,
    0,  -46, -141,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -141,    0,   89,    0,
    0,    0,    0,    0,   86,    0,   35,    0,    0,    0,
    0,    0,    0,   90, -232,    0,    0,  -39,    0,    0,
    0,    0,    0,    0,   12,    0,    0,    0,    0,    0,
    0,    0,   13,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   14,    3,   10,    0,  127,    0,    0,    0,    0,  -30,
    0,   25,  -62,  -77,    0,    0,    0,    0,    0,    0,
    0,   31,   62,    0,    0,   36,    0,    0,    0,    0,
   68,    0,    0,    0,    0,  -67,    0,    0,    0,   56,
};
final static int YYTABLESIZE=312;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         10,
   72,   75,   73,   71,   75,   54,   69,   97,   10,   12,
    8,   40,   31,   48,  109,  112,  115,   35,  131,   81,
   49,   10,   40,   44,   49,   10,   72,   95,   73,  116,
    1,  132,   10,   25,   10,   10,   44,   50,   25,   66,
    3,   25,   96,   68,   30,  130,   10,   28,   31,   10,
  126,   85,   32,   29,   84,   64,   64,   64,   64,   64,
   76,   64,   46,   55,   25,   75,  135,   51,   59,   51,
   44,   51,   40,   64,   64,   50,   64,   50,  104,   50,
  122,   51,   65,   70,   47,   51,   51,   72,   51,   73,
   40,   61,   63,   50,   50,    8,   50,   40,   71,   25,
   29,   78,   79,   35,   92,   86,   91,   94,  105,  111,
  110,  117,  119,   38,   34,   34,   82,  118,  123,  125,
   67,  128,  129,  136,   25,  137,   35,   49,   78,   70,
   34,  103,   35,   33,  121,   48,   68,   67,  133,  113,
  120,  107,    0,    0,    0,    0,    0,    0,    0,  127,
    0,    0,    0,   25,  114,   25,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,   63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   53,   69,    0,    0,   12,    0,    0,   31,
    0,    0,    0,    0,    0,    0,    4,    0,    0,   95,
    0,    0,   83,   38,   39,    5,    6,    8,   64,    7,
   78,    8,   78,    9,   96,   39,    8,    8,    4,    0,
    8,    0,    8,    0,    8,    0,    0,    5,    6,    4,
    0,    7,    4,    8,    0,    9,    0,    0,    5,    6,
    0,    5,    6,    4,    8,    0,    9,    8,    0,    9,
   64,    0,    5,    6,   64,   64,   64,   64,    8,   64,
    9,    0,   51,  106,   38,   39,   51,   51,   51,   51,
   50,   51,    0,    0,   50,   50,   50,   50,    0,   50,
    0,  108,   38,   39,   87,   88,   89,   90,    0,   38,
   39,   60,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   43,   41,   45,   41,   44,   59,   59,   44,   44,   59,
   40,   45,   59,   11,   77,   83,   94,    8,  256,   50,
   44,   40,   45,  256,   44,   61,   43,  256,   45,   97,
  257,  269,   40,    3,   44,   40,  269,   61,    8,   59,
  123,   11,  271,   34,   40,  123,   40,   40,   40,   59,
  118,   41,  257,   46,   44,   41,   42,   43,   44,   45,
   42,   47,  270,  264,   34,   47,  129,   41,  257,   43,
    9,   45,   45,   59,   60,   41,   62,   43,   69,   45,
  111,   59,   40,  273,  125,   59,   60,   43,   62,   45,
   45,   30,   31,   59,   60,  125,   62,   45,  258,   69,
   46,  257,  257,   94,   60,   41,   62,   41,   40,   44,
   41,   41,  270,  257,  123,  123,   55,   44,  268,  123,
  125,   41,   40,  125,   94,   41,   41,   41,  270,   41,
   41,  125,  123,    7,  110,  133,  125,  125,  125,   84,
  105,   74,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  119,
   -1,   -1,   -1,  123,   93,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  133,   -1,   -1,  105,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  256,   -1,   -1,  256,   -1,   -1,  256,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,  256,
   -1,   -1,  265,  257,  258,  266,  267,  257,  264,  270,
  270,  272,  270,  274,  271,  258,  266,  267,  257,   -1,
  270,   -1,  272,   -1,  274,   -1,   -1,  266,  267,  257,
   -1,  270,  257,  272,   -1,  274,   -1,   -1,  266,  267,
   -1,  266,  267,  257,  272,   -1,  274,  272,   -1,  274,
  256,   -1,  266,  267,  260,  261,  262,  263,  272,  265,
  274,   -1,  256,  256,  257,  258,  260,  261,  262,  263,
  256,  265,   -1,   -1,  260,  261,  262,  263,   -1,  265,
   -1,  256,  257,  258,  260,  261,  262,  263,   -1,  257,
  258,  259,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=274;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","STR","EQ","GEQ","LEQ","NEQ",
"DASIG","FLECHA","PRINT","IF","ELSE","ENDIF","UINT","CVR","DO","WHILE","RETURN",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' conjunto_sentencias '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : UINT lista_variables ';'",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"asignacion_multiple : lista_variables '=' lista_constantes",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lambda : '(' UINT ID ')' bloque_ejecutable '(' factor ')'",
"cuerpo_ejecutable : bloque_ejecutable",
"cuerpo_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"sentencia_ejecutable : operacion_ejecutable ';'",
"sentencia_ejecutable : operacion_ejecutable error",
"operacion_ejecutable : invocacion_funcion",
"operacion_ejecutable : asignacion_simple",
"operacion_ejecutable : asignacion_multiple",
"operacion_ejecutable : sentencia_control",
"operacion_ejecutable : sentencia_retorno",
"operacion_ejecutable : impresion",
"operacion_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion",
"sentencia_control : if",
"sentencia_control : while",
"condicion : expresion comparador expresion",
"condicion :",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"if : IF '(' condicion ')' cuerpo_ejecutable rama_else ENDIF",
"if : IF '(' condicion ')' cuerpo_ejecutable rama_else error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"while : DO cuerpo_ejecutable WHILE '(' condicion ')'",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : expresion operador_suma error",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"constante : CTE",
"constante : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
"cuerpo_funcion : conjunto_sentencias",
"cuerpo_funcion :",
"sentencia_retorno : RETURN expresion",
"conjunto_parametros : lista_parametros",
"conjunto_parametros :",
"lista_parametros : parametro_formal",
"lista_parametros : lista_parametros ',' parametro_formal",
"lista_parametros : parametro_vacio",
"parametro_vacio : lista_parametros ','",
"parametro_vacio : ',' parametro_formal",
"parametro_formal : semantica_pasaje UINT variable",
"semantica_pasaje :",
"semantica_pasaje : CVR",
"semantica_pasaje : error",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expresion FLECHA parametro_formal",
};

//#line 288 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getSymbolTableKey());

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
}

void yyerror(String message) {
    System.out.println(message);
}

void notifyDetection(String message) {
    System.err.println("------------------------------------");
    System.err.printf("DETECCIÓN SEMÁNTICA: %s\n", message);
    System.err.println("------------------------------------");
}

void notifyWarning(String warningMessage) {
    System.err.println("------------------------------------");
    System.err.printf("WARNING SINTÁCTICA: Línea %d: %s\n", lexer.getNroLinea(), warningMessage);
    System.err.println("------------------------------------");
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    System.err.println("------------------------------------");
    System.err.printf("ERROR SINTÁCTICO: Línea %d: %s\n", lexer.getNroLinea(), errorMessage);
    System.err.println("------------------------------------");
    this.errorsDetected++;
}

public int getWarningsDetected() {
    return this.warningsDetected;
}

public int getErrorsDetected() {
    return this.errorsDetected;
}

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                        */
/* ---------------------------------------------------------------------------------------------------- */
//#line 454 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 23 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 6:
//#line 39 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 7:
//#line 41 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 12:
//#line 61 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 15:
//#line 70 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 19:
//#line 86 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 23:
//#line 98 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 31:
//#line 111 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 33:
//#line 117 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia WHILE."); }
break;
case 35:
//#line 125 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 43:
//#line 141 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 44:
//#line 145 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF."); }
break;
case 45:
//#line 147 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF-ELSE."); }
break;
case 48:
//#line 158 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 49:
//#line 160 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 68:
//#line 222 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 71:
//#line 232 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 74:
//#line 241 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 80:
//#line 265 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    /*$ $ = Pasaje.COPIA_VALOR_RESULTADO;*/
                                }
break;
case 81:
//#line 272 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
//#line 686 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
