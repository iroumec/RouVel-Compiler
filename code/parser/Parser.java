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
    0,    1,    1,    2,    2,    4,    4,    5,    7,    7,
    8,    8,    9,   10,   10,   12,   13,   13,   16,   16,
   14,   14,   14,   17,   17,    3,    3,   18,   18,   18,
   18,   18,   18,   18,   20,   21,   21,   28,   28,   29,
   29,   29,   29,   29,   29,   29,   26,   31,   31,   30,
   30,   27,   23,   32,   32,   25,   25,   33,   33,   34,
   34,   35,   35,   15,   15,   15,   11,   11,   24,   24,
    6,   37,   37,   22,   36,   36,   38,   38,   38,   40,
   40,   39,   41,   41,   41,   19,   42,   42,   43,
};
final static short yylen[] = {                            2,
    4,    1,    2,    1,    1,    1,    2,    3,    0,    1,
    1,    3,    3,    1,    3,    7,    2,    0,    1,    1,
    3,    2,    0,    1,    2,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    3,    1,    1,    3,    0,    1,
    1,    1,    1,    1,    1,    1,    7,    1,    1,    0,
    2,    6,    4,    1,    1,    3,    1,    1,    1,    3,
    1,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    8,    1,    0,    2,    1,    0,    1,    3,    1,    2,
    2,    3,    0,    1,    1,    4,    1,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    4,    5,    6,    0,    0,   30,   34,    0,
   28,   29,   31,   32,   33,    0,   36,   37,    0,    0,
    0,    0,    0,    0,    0,   20,   19,    0,   66,    0,
   67,    0,   65,   61,   64,    0,    0,    0,    0,    1,
    3,   10,    7,    0,    0,   27,   26,    0,    0,    0,
   87,   70,   54,    0,    0,    0,    0,    0,    8,   22,
   24,    0,    0,   68,   58,   59,    0,   62,   63,    0,
   17,    0,   12,    0,   14,    0,    0,    0,   86,   53,
   46,   42,   44,   43,   45,   40,   41,    0,    0,   85,
   84,    0,    0,    0,   77,   79,    0,   21,   25,    0,
    0,   60,    0,    0,   89,   88,    0,    0,   81,    0,
    0,    0,    0,    0,   15,    0,    0,    0,   78,   82,
   52,    0,   51,   49,   48,   47,    0,    0,   16,   71,
};
final static short yydgoto[] = {                          2,
   11,   12,   13,   14,   15,   16,   53,   17,   18,   84,
   43,   19,   49,   37,   44,   38,   72,   20,   21,   22,
   23,   24,   25,   45,   59,   27,   28,   67,   98,  127,
  136,   65,   77,   47,   80,  103,  138,  104,  105,  106,
  107,   60,   61,
};
final static short yysindex[] = {                      -237,
 -102,    0,  -18,   60,  -11,    6, -206,   -7,   28, -203,
  -40,    0,    0,    0,    0,   12,  -26,    0,    0,  -53,
    0,    0,    0,    0,    0, -185,    0,    0,   28, -164,
   53,   28,   59,  -19,   -4,    0,    0, -172,    0,   56,
    0, -155,    0,    0,    0,   39,  -15, -153,   68,    0,
    0,    0,    0, -147,  -33,    0,    0,   28,  -42,   25,
    0,    0,    0,   39,   70,   45,   71,  -36,    0,    0,
    0,    7,   73,    0,    0,    0,   28,    0,    0,   28,
    0,   -9,    0,   74,    0,   39, -142,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   28,   -7,    0,
    0, -226,   76,   78,    0,    0, -150,    0,    0,   28,
  -15,    0,   83,  -33,    0,    0,   39, -144,    0,    2,
 -226, -131,   86,   28,    0,   -7, -217,  -18,    0,    0,
    0,   87,    0,    0,    0,    0,  -18,    4,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -35,    0,    0,    0, -143,    0,   92,
    0,    0,    0,    0,    0,  -29,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   93,   -6,    0,    0,    0,    0,    0,    0,   15,
    0,    0,    0,    0,    0,  -52,   27,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   95,    0,    0,    0,  -37,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   97,    0,  -49,    0,  -46,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -219,    0,
    0, -132,    0,   98,    0,    0,    0,    0,    0,   93,
   35,    0,    0,    0,    0,    0,  100, -215,    0,    0,
  -39,    0,    0,    0,    0, -208,    0,   19,    0,    0,
    0,    0,    0,    0,    0,    0,   22,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   17,    3,    9,    0,    0,    0,    0,  142,    0,    0,
  -31,    0,    0,   69,  -61,  -83,    0,    0,    0,    0,
    0,    0,    0,   20,   33,    0,    0,   40,    0,    0,
    0,    0,    0,   75,    0,    0,    0,    0,  -87,    0,
    0,    0,   65,
};
final static int YYTABLESIZE=312;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         10,
   75,   80,   76,   76,   80,   57,   74,  102,   11,   13,
    9,   42,   35,   51,  119,  118,   36,   54,  112,    1,
    3,   10,   26,   85,   54,   11,   79,   26,   31,  100,
   26,   78,   10,  129,   55,   10,   23,   11,  134,   69,
   50,   46,  133,   71,  101,   32,   10,   23,   23,   23,
   33,  135,   11,   50,   26,   69,   69,   69,   69,   69,
   23,   69,  132,   64,   66,   89,   48,   57,   88,   57,
   52,   57,   42,   69,   69,   56,   69,   56,   58,   56,
  109,   75,  125,   76,   50,   57,   57,   75,   57,   76,
   86,   26,   62,   56,   56,    9,   56,   42,   68,   29,
   73,   30,   74,   81,   97,   30,   96,   36,   82,   83,
   90,   99,  110,   35,  115,   35,  120,  114,   26,  122,
   70,  121,  124,  126,  128,   40,  131,  139,  140,   23,
  117,  108,   18,   39,   36,   55,   23,   83,   75,   51,
   38,  130,   66,   73,  137,   26,   72,   26,   34,  123,
  113,  111,  116,    0,    0,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,   74,    0,    0,   13,    0,    0,   35,
    0,    0,    0,    0,    0,    0,    4,    0,    0,  100,
    0,    0,   87,    0,   41,    5,    6,    9,   69,    7,
   83,    8,   83,    9,  101,    0,    9,    9,    4,    0,
    9,    0,    9,    0,    9,    0,    0,    5,    6,    4,
    0,    7,    4,    8,    0,    9,    0,    0,    5,    6,
    0,    5,    6,    4,    8,    0,    9,    8,    0,    9,
   69,    0,    5,    6,   69,   69,   69,   69,    8,   69,
    9,    0,   57,   39,   40,   41,   57,   57,   57,   57,
   56,   57,    0,    0,   56,   56,   56,   56,    0,   56,
   91,    0,    0,    0,   92,   93,   94,   95,   39,   40,
   41,   63,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   43,   41,   45,   41,   44,   59,   59,   44,   44,   59,
   40,   45,   59,   11,  102,   99,    8,   44,   80,  257,
  123,   40,    3,   55,   44,   61,   42,    8,   40,  256,
   11,   47,   40,  121,   61,   40,  256,   44,  256,   59,
  256,    9,  126,   35,  271,   40,   40,  256,  268,  269,
  257,  269,   59,  269,   35,   41,   42,   43,   44,   45,
  269,   47,  124,   31,   32,   41,  270,   41,   44,   43,
   59,   45,   45,   59,   60,   41,   62,   43,  264,   45,
   72,   43,  114,   45,  125,   59,   60,   43,   62,   45,
   58,   72,  257,   59,   60,  125,   62,   45,   40,   40,
  273,   46,  258,  257,   60,   46,   62,   99,   41,  257,
   41,   41,   40,  123,  257,  123,   41,   44,   99,  270,
  125,   44,   40,  268,  123,  257,   41,   41,  125,  273,
   98,  125,   41,   41,  126,   41,   40,  270,   41,  137,
   41,  122,  110,  125,  128,  126,  125,  128,    7,  110,
   82,   77,   88,   -1,   -1,   -1,  137,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  256,   -1,   -1,  256,   -1,   -1,  256,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,  256,
   -1,   -1,  265,   -1,  258,  266,  267,  257,  264,  270,
  270,  272,  270,  274,  271,   -1,  266,  267,  257,   -1,
  270,   -1,  272,   -1,  274,   -1,   -1,  266,  267,  257,
   -1,  270,  257,  272,   -1,  274,   -1,   -1,  266,  267,
   -1,  266,  267,  257,  272,   -1,  274,  272,   -1,  274,
  256,   -1,  266,  267,  260,  261,  262,  263,  272,  265,
  274,   -1,  256,  256,  257,  258,  260,  261,  262,  263,
  256,  265,   -1,   -1,  260,  261,  262,  263,   -1,  265,
  256,   -1,   -1,   -1,  260,  261,  262,  263,  256,  257,
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
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"declaracion_variable : UINT lista_variables ';'",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"asignacion_multiple : lista_variables '=' lista_constantes",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : bloque_ejecutable",
"cuerpo_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"bloque_ejecutable :",
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
"if : IF '(' condicion ')' cuerpo_ejecutable rama_else fin_if",
"fin_if : ENDIF",
"fin_if : error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"while : DO cuerpo_ejecutable WHILE '(' condicion ')'",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion operador_suma termino",
"expresion : termino",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : error",
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

//#line 330 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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

    this.yylval = new ParserVal(token.getLexema());

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
}

void yyerror(String message) {
    // Cuando yyerror es llamado, el lexer aún no ha avanzado.
    // 'lexer.getCurrentToken()' (o como lo llames) contendrá el token que causó el error.
    Token tokenProblematico = lexer.getCurrentToken(); // Necesitarás un método en tu Lexer para esto.
    
    if (tokenProblematico == null || tokenProblematico.getIdentificationCode() == EOF) {
        notifyError("Error de sintaxis al final del archivo. ¿Falta un '}' o un ';'? ");
    } else {
        String lexema = tokenProblematico.getLexema();
        
        // Creamos un mensaje mucho más útil
        String errorMessage = "Token inesperado '" + lexema + "'.";

        this.notifyError(errorMessage);
    }
}

// ... el resto de tu código ...

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    //yyerrflag = 0;
}
// TODO: descartar hasta un punto de sincronizacion. "}" o ";".

void notifyDetection(String message) {
    System.err.println("------------------------------------");
    System.err.printf("DETECCIÓN SEMÁNTICA: %s\n", message);
    System.err.println("------------------------------------");
}

void notifyWarning(String warningMessage) {
    System.err.println("------------------------------------");
    System.err.printf("WARNING SINTÁCTICA: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage);
    System.err.println("------------------------------------");
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    System.err.println("------------------------------------");
    System.err.printf("ERROR SINTÁCTICO: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage);
    System.err.println("------------------------------------");
    this.errorsDetected++;
}

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
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
//#line 495 "Parser.java"
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
case 13:
//#line 81 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 16:
//#line 90 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 18:
//#line 99 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 22:
//#line 115 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 23:
//#line 117 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia."); }
break;
case 27:
//#line 129 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 35:
//#line 142 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 37:
//#line 148 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia WHILE."); }
break;
case 39:
//#line 156 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 46:
//#line 169 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
                                    descartarTokenError(); 
                                }
break;
case 49:
//#line 183 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 50:
//#line 187 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF."); }
break;
case 51:
//#line 189 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Setencia IF-ELSE."); }
break;
case 54:
//#line 200 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 55:
//#line 202 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 66:
//#line 246 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Operando no válido."); }
break;
case 71:
//#line 264 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 73:
//#line 272 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 76:
//#line 282 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 79:
//#line 291 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 85:
//#line 307 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 86:
//#line 314 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
//#line 746 "Parser.java"
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
