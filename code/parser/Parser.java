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
   27,   27,   24,   24,   28,   28,   25,   21,   29,   29,
   23,   23,   23,   30,   30,   31,   31,   31,   32,   32,
   13,   13,   10,   10,   22,   22,    6,   34,   34,   20,
   33,   33,   35,   35,   35,   37,   37,   36,   38,   38,
   38,   17,   39,   39,   40,
};
final static short yylen[] = {                            2,
    4,    1,    2,    1,    1,    3,    2,    0,    1,    1,
    3,    3,    1,    3,    8,    1,    1,    3,    2,    1,
    2,    2,    2,    1,    1,    1,    1,    1,    1,    1,
    3,    1,    1,    3,    0,    1,    1,    1,    1,    1,
    1,    1,    7,    7,    0,    2,    6,    4,    1,    1,
    3,    1,    3,    1,    1,    3,    1,    3,    1,    1,
    1,    1,    1,    2,    1,    3,    8,    1,    0,    2,
    1,    0,    1,    3,    1,    2,    2,    3,    0,    1,
    1,    4,    1,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    4,    5,    0,    0,   26,   30,    0,   24,
   25,   27,   28,   29,    0,   32,   33,    0,    0,    0,
    0,    0,    0,    0,   17,   16,    0,    0,   63,    0,
   62,   57,   61,    0,    0,    0,    1,    3,    0,    0,
    9,    7,   23,   22,    0,    0,    0,   83,   66,   49,
    0,    0,    0,    0,    0,    6,   19,   20,    0,    0,
   64,   54,   55,    0,   59,   60,    0,    0,   11,    0,
   13,    0,    0,    0,   82,   48,   42,   38,   40,   39,
   41,   36,   37,    0,    0,   81,   80,    0,    0,    0,
   73,   75,    0,   18,   21,    0,   53,    0,   58,   56,
    0,    0,   85,   84,    0,    0,   77,    0,    0,    0,
    0,    0,   14,    0,    0,    0,   74,   78,   47,    0,
   46,   44,   43,    0,    0,    0,   67,   15,
};
final static short yydgoto[] = {                          2,
   11,   12,   13,   14,   15,   16,   52,   17,   80,   41,
   18,   36,   42,   37,   69,   19,   20,   21,   22,   23,
   24,   43,   56,   26,   27,   64,   94,  125,   62,   74,
   45,   77,   99,  135,  100,  101,  102,  103,   57,   58,
};
final static short yysindex[] = {                      -227,
  -73,    0,  -18,  -19,    5,   13, -203,   -7,  -33, -215,
  -40,    0,    0,    0,  -26,   20,    0,    0,  -53,    0,
    0,    0,    0,    0, -201,    0,    0,  -33, -191,   53,
  -33,   42,  -27,   -4,    0,    0, -206,   37,    0, -174,
    0,    0,    0,  -14,    4, -156,    0,    0, -155,  -22,
    0,    0,    0,    0,  -33,  -42,    8,    0,    0,    0,
  -14,   63,   45,   65,  -36,    0,    0,    0,    7,   68,
    0,    0,    0,   28,    0,    0,   46,   69,    0,   59,
    0,  -14, -148,  -33,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -33,   -7,    0,    0, -231,   70,   71,
    0,    0, -158,    0,    0,  -33,    0,    4,    0,    0,
  -10,  -22,    0,    0,  -14, -154,    0,   -5, -231, -138,
   79,   82,    0,   -7, -232,  -18,    0,    0,    0,  -33,
    0,    0,    0,  -18,   -2,   83,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -35,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -29,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,  -16,    0,    0,    0,    0,    0,   15,    0,    0,
    0,    0,    0,  -52,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   87,    0,    0,    0,  -37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -49,
    0,  -46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -141,    0,   89,
    0,    0,    0,    0,    0,   86,    0,   35,    0,    0,
    0,    0,    0,    0,   90, -208,    0,    0,  -39,    0,
    0,    0,    0,    0,    0,    9,    0,    0,    0,    0,
    0,    0,    0,   10,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   12,    3,   30,    0,  126,    0,    0,    0,    0,  -31,
    0,   25,  -61,  -80,    0,    0,    0,    0,    0,    0,
    0,   31,   62,    0,    0,   33,    0,    0,    0,    0,
   66,    0,    0,    0,    0,  -78,    0,    0,    0,   57,
};
final static int YYTABLESIZE=312;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         10,
   72,   76,   73,   72,   76,   54,   70,   98,   10,   12,
    8,   40,   31,   48,  116,  110,   49,   49,   81,  117,
   28,   10,   40,  132,   96,   10,   29,   10,   72,    1,
   73,   66,   10,   25,   50,   10,  133,   35,   25,   97,
  127,   25,   10,  131,   30,   76,   10,   45,   85,    3,
   75,   84,   31,   32,   46,   65,   65,   65,   65,   65,
   45,   65,   55,   68,   25,   59,   70,   52,  136,   52,
   44,   52,   40,   65,   65,   51,   65,   51,   51,   51,
  123,   65,   29,   71,   47,   52,   52,   72,   52,   73,
   40,   61,   63,   51,   51,    8,   51,   40,  105,   25,
   78,   79,  112,   86,   93,   95,   92,  106,  113,  111,
  118,  120,   34,  124,  119,   34,   82,  126,   38,  129,
   67,  130,  137,  138,   35,   25,   35,   50,   79,   71,
   34,  104,   33,   69,   68,  122,   48,  134,  121,  108,
  114,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  128,    0,    0,   35,   25,  115,   25,    0,    0,    0,
    0,    0,    0,    0,   25,    0,    0,   63,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   53,   70,    0,    0,   12,    0,    0,   31,
    0,    0,    0,    0,    0,    0,    4,    0,    0,   96,
    0,    0,   83,   38,   39,    5,    6,    8,   65,    7,
   79,    8,   79,    9,   97,   39,    8,    8,    4,    0,
    8,    0,    8,    0,    8,    0,    0,    5,    6,    4,
    0,    7,    4,    8,    0,    9,    0,    0,    5,    6,
    0,    5,    6,    4,    8,    0,    9,    8,    0,    9,
   65,    0,    5,    6,   65,   65,   65,   65,    8,   65,
    9,    0,   52,  107,   38,   39,   52,   52,   52,   52,
   51,   52,    0,    0,   51,   51,   51,   51,    0,   51,
   87,  109,   38,   39,   88,   89,   90,   91,    0,   38,
   39,   60,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   43,   41,   45,   41,   44,   59,   59,   44,   44,   59,
   40,   45,   59,   11,   95,   77,   44,   44,   50,   98,
   40,   40,   45,  256,  256,   61,   46,   44,   43,  257,
   45,   59,   40,    3,   61,   40,  269,    8,    8,  271,
  119,   11,   59,  124,   40,   42,   40,  256,   41,  123,
   47,   44,   40,  257,  270,   41,   42,   43,   44,   45,
  269,   47,  264,   34,   34,  257,  273,   41,  130,   43,
    9,   45,   45,   59,   60,   41,   62,   43,   59,   45,
  112,   40,   46,  258,  125,   59,   60,   43,   62,   45,
   45,   30,   31,   59,   60,  125,   62,   45,   69,   69,
  257,  257,   44,   41,   60,   41,   62,   40,  257,   41,
   41,  270,  123,  268,   44,  123,   55,  123,  257,   41,
  125,   40,  125,   41,   95,   95,   41,   41,  270,   41,
   41,  125,    7,  125,  125,  111,  134,  126,  106,   74,
   84,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  120,   -1,   -1,  124,  124,   94,  126,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  134,   -1,   -1,  106,   -1,   -1,
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
  256,  256,  257,  258,  260,  261,  262,  263,   -1,  257,
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
"comparador : error",
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
"argumento : expresion FLECHA ID",
};

//#line 292 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
    // Silenciar esto permite que mensajes como "syntax error", al utilizar
    // un token de error, no se muestren.
    // En su lugar, se presentan los mensajes que nosotros brindamos,
    // diferenciando entre error y warning.
}

void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;
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
//#line 466 "Parser.java"
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
//#line 26 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 6:
//#line 42 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 12:
//#line 64 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 15:
//#line 73 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 19:
//#line 89 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 23:
//#line 101 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 31:
//#line 114 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 33:
//#line 120 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia WHILE."); }
break;
case 35:
//#line 128 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 42:
//#line 141 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
                                    descartarTokenError(); 
                                }
break;
case 44:
//#line 152 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 45:
//#line 156 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF."); }
break;
case 46:
//#line 158 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF-ELSE."); }
break;
case 49:
//#line 169 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 50:
//#line 171 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 67:
//#line 226 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 69:
//#line 234 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 72:
//#line 244 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 75:
//#line 253 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 81:
//#line 269 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 82:
//#line 276 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
//#line 705 "Parser.java"
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
