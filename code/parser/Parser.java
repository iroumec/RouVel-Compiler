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






//#line 6 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
    package parser;

    /* Importaciones.*/
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;

    /* End of File.*/
    public final static short EOF = 0;

    /* Lexer.*/
    private final Lexer lexer;

    /* Contadores de la cantidad de errores detectados.*/
    private int errorsDetected;
    private int warningsDetected;
//#line 28 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 38 "Parser.java"




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
    0,    0,    0,    0,    5,    7,    5,    5,    6,    6,
    8,    8,    8,    8,    8,   10,   10,   11,   11,   11,
   13,   13,    1,    1,    1,   14,    2,    2,    2,   15,
   16,   16,   19,   19,   17,   21,   17,   20,   20,    9,
    9,   22,   22,   22,   22,   22,   22,   22,   24,   24,
   25,   25,   31,   31,   31,   31,   31,   32,   32,   33,
   33,   33,   33,   33,   33,   33,   33,   29,   29,   34,
   34,   30,   35,   35,   27,   36,   36,   36,   28,   28,
   28,   37,   37,   39,   39,   38,   38,   40,   40,   18,
   18,   18,    4,    3,    3,   12,   12,   42,   42,   26,
   41,   41,   43,   43,   43,   45,   45,   44,   44,   44,
   46,   46,   46,   23,   47,   47,   48,   48,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    0,    3,    0,    1,    2,
    1,    1,    2,    2,    2,    1,    2,    3,    3,    4,
    0,    1,    1,    3,    2,    3,    1,    3,    2,    7,
    2,    0,    1,    1,    3,    0,    3,    1,    2,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    1,    3,    2,    3,    3,    2,    3,    0,    1,
    1,    1,    1,    1,    1,    1,    1,    5,    4,    0,
    2,    2,    3,    2,    4,    1,    1,    0,    3,    1,
    1,    1,    1,    2,    2,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    8,    7,    1,    0,    4,
    1,    0,    1,    3,    1,    2,    2,    3,    2,    2,
    0,    1,    1,    4,    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    0,    9,   11,   12,
   16,    0,   44,   48,    0,   42,   43,   45,   46,   47,
   51,   52,    6,    0,   14,   13,   15,    0,    0,    0,
    0,    0,   93,    0,   90,   91,   87,   92,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   33,   34,
    0,   72,    0,    0,    0,   25,    0,    0,    0,    0,
   10,   22,   17,   41,   40,    7,    5,    0,    0,  115,
   95,   76,    0,    0,    0,   54,    0,   67,   62,   64,
   63,   65,   66,   60,   61,   82,   83,    0,    0,    0,
   57,   88,   89,    0,    0,    0,   19,    0,  113,  112,
    0,    0,    0,  103,  105,    0,    0,   18,   36,   38,
    0,    0,   74,    0,   31,    0,   24,    0,   27,    0,
    0,    0,    0,  114,   75,   55,   56,   53,    0,    0,
    0,    0,   86,    0,  107,    0,    0,    0,    0,  110,
   20,   37,   35,   39,   73,  100,    0,    0,   29,  117,
  116,   71,   68,    0,    0,  104,  108,    0,   28,    0,
    0,    0,    0,    0,   97,   30,   96,
};
final static short yydgoto[] = {                          4,
   14,  128,   45,   46,    5,  171,   76,   18,   19,   20,
   21,   22,   73,   23,   24,   65,   60,   47,   61,  121,
  152,   25,   48,   27,   28,   29,   30,   49,   31,   32,
   50,   51,   98,  142,   62,   84,   99,   52,   53,  105,
  112,  172,  113,  114,  115,  116,   79,   80,
};
final static short yysindex[] = {                       -91,
    0,  -28,   13,    0,    0,  -40,    3,  -16,   -9,  -35,
  -12,   -6, -253,   -7, -220,    0,   37,    0,    0,    0,
    0,  -21,    0,    0,  -53,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0, -206, -215, -138,
 -206,    3,    0,    6,    0,    0,    0,    0,  129,  -12,
   22,    8, -206,    9,   30,  -31,   20,   48,    0,    0,
  -38,    0, -206, -197,   41,    0, -171, -166, -206, -206,
    0,    0,    0,    0,    0,    0,    0,  -22,   18,    0,
    0,    0,   57,   56, -158,    0,  -25,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -206, -206, -163,
    0,    0,    0,   19, -206,   19,    0,  -31,    0,    0,
 -226,   65,   66,    0,    0, -224,   50,    0,    0,    0,
   51,   -9,    0,   28,    0,   -1,    0,  -17,    0,   57,
   57, -140, -206,    0,    0,    0,    0,    0,   57,   19,
  -12, -146,    0,   83,    0,   11, -226,   89, -115,    0,
    0,    0,    0,    0,    0,    0,  107, -166,    0,    0,
    0,    0,    0,   31,   37,    0,    0, -206,    0,   37,
   37,   35,  123,   61,    0,    0,    0,
};
final static short yyrindex[] = {                       148,
    0,  148,    0,    0,    0,    0,   -5,    0,  134,    0,
    0,    0,  141,    0,    0,    0,  187,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  147,
  -64,   71,    0,  -64,    0,    0,    0,    0,    0,    0,
    0,  108,  118,    0,   45,  -33,    0,    0,    0,    0,
  134,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,  152,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   44,
    0,    0,    0,   84,    0,   96,    0,  -33,    0,    0,
 -222,    0,   22,    0,    0,    0,    0,    0,    0,    0,
    0,  134,    0,    0,    0,    0,    0,  -50,    0,  -48,
  -45,    0,    0,    0,    0,    0,    0,    0,  -23,  140,
    0,  -44,    0,    0,    0,    0,  -34,   43,   49,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   70,    0,    0,    0,    0,   70,
   73,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  194,    0,  404,  128,  195,   72,    0,   23,  305,    0,
    0,    0,    0,    0,    0,    0,   79, -101,  -47,    0,
    0,    0,  406,    0,    0,    0,    0,   38,    0,    0,
  -41,   55,    0,    0,    0,    0,    0,  -27,    0,    0,
   99,   40,    0,  -89,    0,    0,    0,   76,
};
final static int YYTABLESIZE=577;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   21,   44,  100,  143,   56,   75,  106,  102,   26,  106,
   50,   13,  111,   49,   69,  138,   64,   58,   36,  123,
   96,  145,   97,   40,  104,  106,  158,   13,   37,  109,
   44,    3,  148,   63,  111,   69,   67,   72,   23,   71,
   21,   81,   38,   70,  110,  149,   86,  111,   39,  103,
   42,   43,   13,   68,  102,   23,   71,  166,  134,  125,
  103,  133,  101,   67,   13,  102,  173,  107,  156,  108,
   96,  140,   97,   17,   34,   78,   13,   83,  118,  118,
  155,  126,  118,   94,   35,  127,   94,   13,   23,  109,
   13,   43,  109,  162,    3,   85,  135,  136,   87,   96,
  124,   97,   70,   23,  141,  146,  130,  131,  151,  147,
   58,   94,   94,   94,   94,   94,  160,   94,   42,   43,
   82,   58,  163,  164,   84,   21,   84,   84,   84,   94,
   94,   94,   94,  165,   39,  139,   85,   33,   85,   85,
   85,  148,   84,   84,   84,   84,  168,    8,   80,   77,
   80,   80,   80,  170,   85,   85,   85,   85,   81,  175,
   81,   81,   81,  176,    1,    2,   80,   80,   80,   80,
   78,   96,  119,   97,   59,  153,   81,   81,   81,   81,
   79,   32,   79,   79,   79,  177,    2,   78,   95,   93,
   94,   59,   77,   71,   99,  129,   16,   98,   79,   79,
   79,   79,   74,   57,  157,   26,  144,   50,  161,  174,
   49,   69,    0,    0,    0,    6,    7,   41,   42,   43,
   54,   55,  111,  111,  109,    8,    9,    6,    7,   10,
  137,   11,   58,   12,  122,  111,  111,    8,    9,  110,
   43,   10,  132,   11,    7,   12,   41,   42,   43,   66,
   94,   23,    0,    8,    9,  159,   21,   21,   94,   11,
    0,   12,   42,   43,   42,   43,   21,   21,    6,    7,
   21,    0,   21,    0,   21,  117,   66,    0,    8,    9,
    6,    7,   10,    0,   11,  169,   12,    0,    0,    0,
    8,    9,    6,    7,   10,    0,   11,    0,   12,   70,
   23,   23,    8,    9,    7,    0,   10,    7,   11,    0,
   12,    0,   70,    8,    9,   59,    8,    9,    0,   11,
    0,   12,   11,    0,   12,    0,   94,   94,   94,    0,
   94,   94,   94,   94,    0,   94,    0,    0,    0,   84,
   84,   84,    0,   84,   84,   84,   84,    0,   84,    0,
    0,   85,   85,   85,   59,   85,   85,   85,   85,    0,
   85,    0,  120,   80,    0,    0,    0,   80,   80,   80,
   80,    0,   80,   81,    0,    0,    0,   81,   81,   81,
   81,    0,   81,    0,   88,    0,    0,    0,   89,   90,
   91,   92,    0,    0,    0,   79,    0,    0,    0,   79,
   79,   79,   79,    0,   79,   15,   15,   26,   26,   15,
    0,   26,    0,    0,   15,    0,   26,    0,    0,    0,
   15,    0,   26,    0,    0,  154,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   15,    0,   26,
    0,    0,    0,    0,    0,   59,    0,    0,    0,    0,
    0,    0,    0,   15,    0,   26,    0,    0,    0,    0,
    0,   15,    0,   26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  150,
    0,    0,    0,    0,   15,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   15,    0,   26,    0,    0,    0,
    0,    0,  167,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   15,    0,
   26,    0,    0,   15,   15,   26,   26,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   50,  105,   40,   59,   41,   41,   59,   44,
   59,   40,   44,   59,   59,   41,  270,   41,   59,   61,
   43,  111,   45,   40,   52,   53,   44,   40,    6,  256,
   40,  123,  257,   40,  257,  256,   44,   59,   44,   17,
   40,  257,   40,  264,  271,  270,   41,  270,   46,   42,
  257,  258,   40,   61,   47,   61,   34,  147,   41,  257,
   42,   44,   41,   44,   40,   47,  168,   59,   41,   40,
   43,   99,   45,    2,    3,   38,   40,   40,   59,   41,
  122,   41,   44,   41,  125,  257,   44,   40,   44,   41,
   40,  258,   44,  141,  123,   41,   41,  256,   44,   43,
   63,   45,   59,   59,  268,   41,   69,   70,   59,   44,
  123,   41,   42,   43,   44,   45,  257,   47,  257,  258,
  259,  123,  269,   41,   41,  125,   43,   44,   45,   59,
   60,   61,   62,  123,   46,   98,   41,  125,   43,   44,
   45,  257,   59,   60,   61,   62,   40,    0,   41,  125,
   43,   44,   45,  123,   59,   60,   61,   62,   41,  125,
   43,   44,   45,   41,  256,  257,   59,   60,   61,   62,
  133,   43,  125,   45,   41,  125,   59,   60,   61,   62,
   41,   41,   43,   44,   45,  125,    0,   41,   60,   61,
   62,  256,   41,  171,  125,   68,    2,  125,   59,   60,
   61,   62,  256,   10,  126,  256,  108,  256,  133,  170,
  256,  256,   -1,   -1,   -1,  256,  257,  256,  257,  258,
  256,  257,  257,  257,  256,  266,  267,  256,  257,  270,
  256,  272,  256,  274,  273,  270,  270,  266,  267,  271,
  258,  270,  265,  272,  257,  274,  256,  257,  258,  257,
  256,  257,   -1,  266,  267,  128,  256,  257,  264,  272,
   -1,  274,  257,  258,  257,  258,  266,  267,  256,  257,
  270,   -1,  272,   -1,  274,  256,  257,   -1,  266,  267,
  256,  257,  270,   -1,  272,  158,  274,   -1,   -1,   -1,
  266,  267,  256,  257,  270,   -1,  272,   -1,  274,  256,
  256,  257,  266,  267,  257,   -1,  270,  257,  272,   -1,
  274,   -1,  269,  266,  267,   11,  266,  267,   -1,  272,
   -1,  274,  272,   -1,  274,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,  256,  257,  258,   50,  260,  261,  262,  263,   -1,
  265,   -1,   58,  256,   -1,   -1,   -1,  260,  261,  262,
  263,   -1,  265,  256,   -1,   -1,   -1,  260,  261,  262,
  263,   -1,  265,   -1,  256,   -1,   -1,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,  256,   -1,   -1,   -1,  260,
  261,  262,  263,   -1,  265,    2,    3,    2,    3,    6,
   -1,    6,   -1,   -1,   11,   -1,   11,   -1,   -1,   -1,
   17,   -1,   17,   -1,   -1,  121,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   34,   -1,   34,
   -1,   -1,   -1,   -1,   -1,  141,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   50,   -1,   50,   -1,   -1,   -1,   -1,
   -1,   58,   -1,   58,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  116,
   -1,   -1,   -1,   -1,  121,   -1,  121,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  141,   -1,  141,   -1,   -1,   -1,
   -1,   -1,  149,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  165,   -1,
  165,   -1,   -1,  170,  171,  170,  171,
};
}
final static short YYFINAL=4;
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
"programa : ID cuerpo_programa",
"programa : ID conjunto_sentencias",
"programa : cuerpo_programa",
"programa : error",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"$$1 :",
"cuerpo_programa : '{' '}' $$1",
"cuerpo_programa :",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia : error ';'",
"sentencia : error '}'",
"sentencia : error sentencia",
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT error ';'",
"declaracion_variable : UINT lista_variables error ';'",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"asignacion_multiple : lista_variables '=' lista_constantes",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"$$2 :",
"bloque_ejecutable : '{' '}' $$2",
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
"asignacion_simple : variable error expresion",
"sentencia_control : if",
"sentencia_control : while",
"condicion : '(' cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : error cuerpo_condicion error",
"condicion : '(' cuerpo_condicion error",
"condicion : cuerpo_condicion ')'",
"cuerpo_condicion : expresion comparador expresion",
"cuerpo_condicion :",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"comparador : error",
"if : IF condicion cuerpo_ejecutable rama_else ENDIF",
"if : IF condicion cuerpo_ejecutable rama_else",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"while : DO cuerpo_do",
"cuerpo_do : cuerpo_ejecutable WHILE condicion",
"cuerpo_do : cuerpo_ejecutable condicion",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"imprimible :",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : secuencia_sin_operador",
"operador_suma : '+'",
"operador_suma : '-'",
"secuencia_sin_operador : termino termino",
"secuencia_sin_operador : secuencia_sin_operador termino",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : invocacion_funcion",
"constante : CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
"declaracion_funcion : UINT '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
"cuerpo_funcion : conjunto_sentencias",
"cuerpo_funcion :",
"sentencia_retorno : RETURN '(' expresion ')'",
"conjunto_parametros : lista_parametros",
"conjunto_parametros :",
"lista_parametros : parametro_formal",
"lista_parametros : lista_parametros ',' parametro_formal",
"lista_parametros : parametro_vacio",
"parametro_vacio : lista_parametros ','",
"parametro_vacio : ',' parametro_formal",
"parametro_formal : semantica_pasaje UINT variable",
"parametro_formal : semantica_pasaje UINT",
"parametro_formal : semantica_pasaje variable",
"semantica_pasaje :",
"semantica_pasaje : CVR",
"semantica_pasaje : error",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expresion FLECHA ID",
"argumento : expresion",
};

//#line 509 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

// ============================================================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ============================================================================================================================================================

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    // Se activa el debug.
    yydebug = true;
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

    return token.getIdentificationCode();
}

public void descartarTokensHasta(int tokenEsperado) {

    int t = yylex();
    
    // Se pide un token mientras no se halle el token esperado
    // o el final de archivo.
    while (t != tokenEsperado && t != EOF) {
        t = yylex();
    }

    if (t == EOF) {
        Printer.printBetweenSeparations("SE LLEGÓ AL FINAL DEL ARCHIVO SIN ENCONTRAR UN TOKEN DE SINCRONIZACION.");
    }

    // Se actualizá que se halló el token deseado o se llegó al final del archivo.
    yychar = t;
}

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
public void yyerror(String s) {
    // El mensaje 's' que nos pasa Byacc/J es genérico y poco útil.
    // Lo ignoramos y usamos el estado del lexer para dar un mensaje preciso.
    
    // yychar es una variable interna del parser que contiene el token actual (lookahead).
    if (yychar == EOF) {
        notifyError("Error de sintaxis: Se alcanzó el final del archivo inesperadamente. (¿Falta un '}' o un ';'? )");
        return;
    }
    
    // Usamos nuestro método notificador con la información de línea/columna del lexer.
    /*notifyError(
        String.format(
            "Error de sintaxis cerca del token '%s'.", 
            lexer.getCurrentToken().getLexema() // Asumiendo que tu lexer tiene un método para obtener el lexema actual.
        )
    );
    */
}

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

void apagarEstadoDeError() {
    yyerrflag = 0;
}

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
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

// ============================================================================================================================================================
// FIN DE CÓDIGO
// ============================================================================================================================================================
//#line 640 "Parser.java"
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
//#line 55 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 62 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 64 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }. Se sincronizará hasta ID.");
                                    descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
                                }
break;
case 6:
//#line 77 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 7:
//#line 79 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 8:
//#line 81 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee un cuerpo."); }
break;
case 13:
//#line 98 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un ';'.");
                                }
break;
case 14:
//#line 102 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un '}'.");
                                }
break;
case 15:
//#line 106 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta otra sentencia.");
                                }
break;
case 16:
//#line 116 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 19:
//#line 130 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 20:
//#line 132 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 24:
//#line 155 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 25:
//#line 166 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 26:
//#line 184 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 28:
//#line 191 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 29:
//#line 196 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 30:
//#line 209 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 32:
//#line 218 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 36:
//#line 236 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 238 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia."); }
break;
case 41:
//#line 254 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 49:
//#line 271 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 50:
//#line 276 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 52:
//#line 283 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia WHILE."); }
break;
case 53:
//#line 290 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Condicion."); }
break;
case 54:
//#line 295 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 55:
//#line 297 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura y cierre de paréntesis en condicion de selección/iteración."); }
break;
case 56:
//#line 299 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condicion de selección/iteración."); }
break;
case 57:
//#line 301 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condicion de selección/iteración."); }
break;
case 66:
//#line 320 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
break;
case 67:
//#line 329 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
                                    descartarTokenError(); 
                                }
break;
case 69:
//#line 337 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 70:
//#line 341 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 71:
//#line 343 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF-ELSE."); }
break;
case 74:
//#line 359 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 76:
//#line 366 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 77:
//#line 368 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 78:
//#line 373 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 95:
//#line 420 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 96:
//#line 428 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 97:
//#line 430 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 99:
//#line 438 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 102:
//#line 449 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 105:
//#line 458 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 109:
//#line 469 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 110:
//#line 471 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 113:
//#line 477 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 114:
//#line 488 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 118:
//#line 501 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1016 "Parser.java"
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
