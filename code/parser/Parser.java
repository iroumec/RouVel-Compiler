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






//#line 10 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
    package parser;

    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 31 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 27 "Parser.java"




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
public final static short lista_constantes=257;
public final static short ID=258;
public final static short CTE=259;
public final static short STR=260;
public final static short EQ=261;
public final static short GEQ=262;
public final static short LEQ=263;
public final static short NEQ=264;
public final static short DASIG=265;
public final static short FLECHA=266;
public final static short PRINT=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short ENDIF=270;
public final static short UINT=271;
public final static short CVR=272;
public final static short DO=273;
public final static short WHILE=274;
public final static short RETURN=275;
public final static short EOF=0;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,   12,   12,   12,   12,   13,   13,
   13,   15,   15,   15,   16,   16,   16,   16,   16,   16,
   16,   14,   14,   18,   18,   21,   21,   22,   22,   19,
   19,   19,    6,    6,    6,   23,   24,   24,   24,   25,
   26,   26,   28,   28,   27,   27,   29,   29,   29,   17,
   17,   17,   17,   17,   17,   17,   30,   30,   30,   31,
   31,   36,   36,   36,   36,   36,   37,   37,   38,   38,
   38,   38,   38,   38,   38,   34,   34,   34,   34,   34,
   39,   39,   35,   35,   35,   40,   40,   40,   41,   33,
   33,   33,   33,   42,   42,    1,    1,    1,    1,   43,
   43,    2,    2,    2,    4,    4,    4,   44,   44,    3,
    3,    3,    5,    5,    5,    8,    8,    7,    7,   20,
   20,   46,   46,   32,   32,   32,   45,   45,   47,   47,
   47,   49,   49,   48,   48,   48,   50,   50,   50,    9,
   10,   10,   11,   11,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    2,    0,    3,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    0,    1,    1,    1,    3,
    3,    5,    1,    3,    2,    2,    5,    3,    3,    8,
    2,    0,    1,    1,    3,    2,    1,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    4,    4,    4,    1,
    1,    3,    2,    2,    2,    3,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    5,    2,
    0,    2,    3,    3,    2,    2,    1,    2,    2,    5,
    5,    4,    4,    1,    1,    3,    1,    3,    2,    1,
    1,    3,    1,    3,    3,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    2,    1,    3,    6,
    5,    3,    2,    5,    5,    4,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    4,
    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,   50,    1,    0,    9,   22,   23,   24,
    0,   52,    0,   56,   51,   53,   54,   55,   60,   61,
    0,    6,    0,    0,    0,    0,   80,  116,    0,    0,
    0,    0,  103,  110,  111,  112,    0,    0,    0,    0,
    0,    0,    0,   85,    0,    0,   43,   44,    0,    0,
   87,    0,    0,    0,    0,    0,    0,    0,    0,   10,
   27,   25,   36,    0,    8,    5,    0,    0,  141,  119,
   94,    0,    0,    0,   64,    0,  117,  114,   71,   73,
   72,   74,   75,   69,   70,  100,  101,    0,  106,  113,
  115,    0,    0,  108,  109,    0,    0,   65,   63,   31,
    0,  139,  138,    0,    0,    0,  129,  131,    0,   35,
   30,    0,    0,   89,    0,   46,   47,    0,   88,   86,
   84,   83,    0,    0,   41,    0,    0,    0,    0,    0,
    0,   38,   15,   18,   16,   17,   19,   21,   20,   13,
   12,   11,   14,   39,    0,    0,  140,   93,   92,    0,
   66,   62,    0,    0,   98,    0,  104,  102,    0,    0,
    0,  133,    0,    0,    0,  136,   34,    0,   49,   45,
   48,  126,    0,    0,   59,   58,   57,    0,  143,  142,
   91,   90,  107,  105,   82,   79,    0,   78,    0,    0,
  121,  130,  134,   32,  125,  124,    0,   37,   77,   76,
  120,  123,    0,    0,  122,    0,   28,   29,   40,
};
final static short yydgoto[] = {                          4,
   41,   42,   43,   98,   99,   52,   44,   45,   46,   78,
   79,    5,   16,   17,  152,  153,   18,   19,   20,   21,
   72,  219,   22,   23,   24,   64,   58,   59,  128,   25,
   26,   27,   28,   29,   30,   47,   48,  102,  170,   60,
   61,   84,  103,  106,  115,  201,  116,  117,  118,  119,
};
final static short yysindex[] = {                       -69,
    0,   26,  -40,    0,    0,   10,   -4,   19,   44,  -26,
   41, -220,  -39,    0,    0,   40,    0,    0,    0,    0,
   20,    0,  -27,    0,    0,    0,    0,    0,    0,    0,
  -55,    0,  -13,  151, -175,   45,    0,    0,    8, -172,
  141,  -16,    0,    0,    0,    0,   12,  -31,   29,   25,
  -32,   23, -170,    0,  -36,   54,    0,    0,  -38,  -44,
    0,   33, -165,   59,  151,  151, -159,   30,  140,    0,
    0,    0,    0,   30,    0,    0,  130,  -20,    0,    0,
    0,  -41,   76,   61,    0,  -22,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -16,    0,    0,
    0,  151,  160,    0,    0,  164, -163,    0,    0,    0,
  -32,    0,    0, -228,   63,   64,    0,    0, -233,    0,
    0, -147,   30,    0,   51,    0,    0,  -29,    0,    0,
    0,    0,   55,   -6,    0,   -8,  133,   58,   70,  -28,
   74,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -138,  151,    0,    0,    0,   39,
    0,    0,  168,   76,    0,  -16,    0,    0,   12,  -56,
   81,    0,    2, -228, -159,    0,    0,   73,    0,    0,
    0,    0,   75,   83,    0,    0,    0,   30,    0,    0,
    0,    0,    0,    0,    0,    0,   85,    0,    2,   65,
    0,    0,    0,    0,    0,    0,  151,    0,    0,    0,
    0,    0,   15,   95,    0,   97,    0,    0,    0,
};
final static short yyrindex[] = {                       124,
    0,  124,    0,    0,    0,   86,    0,    0,    0,    0,
    0,   96,    0,    0,    0,  138,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    5,   98,    0,    0,    0,    0,    0,    0,    0,   -2,
   -7,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   28,    0,    0,    0,
    0,    0,  114,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  109,    0,    0,
    0,    0,    0,    0,    0,    0,   50,    0,    0,    0,
   -7,    0,    0, -210,    0,  122,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   69,    0,  121,    0,    0,    0,    0,
    0,    0,    0,  -21,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   11,   71, -100,    0,    0,    0,  426,  327,  428,    0,
   16,  175,  158,   -3,    0,    0,  402,    0,    0,    0,
    0,    0,    0,  118,    0,    0,   52,  -18,    0,    0,
    0,    0,    0,    0,    0,    3,  152,    0,    0,    0,
  134,    0,    0,   99,   84,   -5,    0, -106,    0,    0,
};
final static int YYTABLESIZE=641;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   26,   39,  198,   39,   67,  168,   40,  172,   40,  109,
   12,  114,   70,   12,  132,   67,   74,  159,  162,  132,
  157,   68,  132,  156,  139,  105,   12,  112,  107,   70,
  104,   73,   68,  128,  183,   36,   96,  175,   97,  118,
   26,   33,  118,  113,   77,   68,   83,  137,   85,   34,
   63,   12,   40,    3,   12,   35,   33,  124,   39,  118,
  137,  129,  194,   40,  111,   12,  122,  202,  144,   75,
   35,  144,  134,  133,   40,  137,  138,   40,   71,   12,
   62,  121,   80,   51,   32,   82,   87,  110,  135,   40,
   12,  135,  135,   12,  123,  180,   56,  192,  139,  136,
   96,  160,   97,  173,   12,  169,  214,  174,   81,   67,
  177,   76,  164,  182,   56,   35,  187,  188,   96,  189,
   97,  199,  207,    7,  200,   26,  118,  118,  118,  118,
  118,  204,  118,  206,   56,  216,   42,    2,   97,  215,
   97,   97,   97,  210,  118,  118,  118,  118,    3,   99,
  195,   99,   99,   99,   95,  218,   97,   97,   97,   97,
   33,   96,  127,   96,   96,   96,   77,   99,   99,   99,
   99,  190,   96,  166,   97,   96,   15,   97,  126,   96,
   96,   96,   96,   96,  141,   97,    1,  184,    2,  212,
   86,  185,  130,  211,  171,   40,  163,    0,  151,  196,
   95,   93,   94,    0,   40,    0,    0,    0,   40,   70,
    0,  131,   40,  197,  158,   31,   65,    6,    0,    6,
   38,    6,   38,  112,  108,   66,    7,    8,    6,   54,
    9,    6,   10,  161,   11,   55,  137,    7,    8,  113,
    7,    8,   69,   10,    6,   11,   10,   55,   11,  137,
  137,    6,   88,    7,    8,   33,   26,    9,   26,   10,
   68,   11,  118,  137,  150,    6,   38,   26,   26,    6,
   69,   26,    6,   26,   37,   26,    6,   38,    7,    8,
  120,    7,    8,    6,   10,    9,   11,   10,   38,   11,
    6,   38,    7,    8,  191,   69,    9,    6,   10,   49,
   11,   50,    6,   38,   81,   81,    7,    8,    6,  125,
    9,    6,   10,  186,   11,    6,   88,    7,    8,   81,
    7,    8,    6,   10,   67,   11,   10,    0,   11,    0,
  205,    7,    8,    6,   88,    9,    0,   10,    0,   11,
  209,  118,    0,  118,  118,    0,  118,  118,  118,  118,
  118,  118,  217,   97,    0,   97,   97,  213,   97,   97,
   97,   97,    0,   97,   99,    0,   99,   99,    0,   99,
   99,   99,   99,    0,   99,    0,   96,    0,   96,   96,
    0,   96,   96,   96,   96,    0,   96,    6,   88,    0,
    6,   88,    0,    0,  142,  155,    0,  143,    6,   88,
  154,   89,   90,   91,   92,    0,  144,  145,    6,   38,
  146,   57,  147,  148,  149,  165,    0,    6,   38,  167,
    0,    6,   38,  193,    0,    6,   38,   13,   13,   14,
   14,    0,    0,    0,   53,   13,    0,   14,    0,    0,
    0,   13,    0,   14,    0,    0,    0,    0,   57,  178,
    0,    0,    0,    0,    0,    0,    0,  127,   13,    0,
   14,    0,    0,    0,    0,    0,  100,    0,  101,    0,
    0,    0,   13,    0,   14,    0,    0,    0,    0,    0,
    0,   13,    0,   14,    0,    0,    0,    0,    0,    0,
    0,    0,  140,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  100,    0,  101,    0,    0,    0,  100,    0,
  101,    0,    0,    0,  208,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  179,    0,    0,  181,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  176,    0,    0,    0,    0,    0,
   13,    0,   14,   13,    0,   14,    0,    0,    0,  100,
    0,  101,  100,  100,  101,  101,    0,    0,    0,    0,
   57,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  100,
    0,  101,    0,    0,   13,    0,   14,    0,    0,    0,
  203,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,    0,   14,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   13,    0,
   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   40,   44,  106,   45,  114,   45,   41,
   40,   44,   16,   40,   59,   44,   44,   59,   41,   41,
   41,   61,   44,   44,  258,   42,   40,  256,   47,   33,
   47,   59,   61,   41,   41,   40,   43,  271,   45,   41,
   40,   44,   44,  272,   34,   41,   36,  258,   41,   40,
  271,   40,   45,  123,   40,   46,   59,   55,   40,   61,
  271,   59,  163,   45,   40,   40,   44,  174,   41,  125,
   46,   44,   62,   41,   45,   65,   66,   45,   59,   40,
   40,   59,  258,   40,  125,   41,  259,   59,   41,   45,
   40,   44,  258,   40,  265,  125,  123,   59,  258,   41,
   43,   41,   45,   41,   40,  269,  207,   44,   59,   41,
  258,  125,  102,   59,  123,   46,   59,   44,   43,  258,
   45,   41,   40,    0,  123,  125,   41,   42,   43,   44,
   45,   59,   47,   59,  123,   41,   41,    0,   41,  125,
   43,   44,   45,   59,   59,   60,   61,   62,  123,   41,
  169,   43,   44,   45,   41,   59,   59,   60,   61,   62,
    3,   41,   41,   43,   44,   45,  156,   59,   60,   61,
   62,  156,   43,  103,   45,   43,    2,   45,  125,   59,
   60,   61,   62,   43,   67,   45,  256,  136,  258,  125,
   39,   59,   59,  199,  111,   45,   98,   -1,   59,  256,
   60,   61,   62,   -1,   45,   -1,   -1,   -1,   45,  213,
   -1,  256,   45,  270,  256,  256,  256,  258,   -1,  258,
  259,  258,  259,  256,  256,  265,  267,  268,  258,  256,
  271,  258,  273,  256,  275,  274,  258,  267,  268,  272,
  267,  268,  256,  273,  258,  275,  273,  274,  275,  271,
  258,  258,  259,  267,  268,  258,  256,  271,  258,  273,
  256,  275,  265,  271,  125,  258,  259,  267,  268,  258,
  256,  271,  258,  273,  256,  275,  258,  259,  267,  268,
  258,  267,  268,  258,  273,  271,  275,  273,  259,  275,
  258,  259,  267,  268,  256,  256,  271,  258,  273,  256,
  275,  258,  258,  259,  260,  256,  267,  268,  258,  256,
  271,  258,  273,  256,  275,  258,  259,  267,  268,  270,
  267,  268,  258,  273,  256,  275,  273,   -1,  275,   -1,
  256,  267,  268,  258,  259,  271,   -1,  273,   -1,  275,
  256,  256,   -1,  258,  259,   -1,  261,  262,  263,  264,
  265,  266,  256,  256,   -1,  258,  259,  200,  261,  262,
  263,  264,   -1,  266,  256,   -1,  258,  259,   -1,  261,
  262,  263,  264,   -1,  266,   -1,  256,   -1,  258,  259,
   -1,  261,  262,  263,  264,   -1,  266,  258,  259,   -1,
  258,  259,   -1,   -1,   68,  266,   -1,  258,  258,  259,
   74,  261,  262,  263,  264,   -1,  267,  268,  258,  259,
  271,   10,  273,  274,  275,  256,   -1,  258,  259,  256,
   -1,  258,  259,  256,   -1,  258,  259,    2,    3,    2,
    3,   -1,   -1,   -1,    9,   10,   -1,   10,   -1,   -1,
   -1,   16,   -1,   16,   -1,   -1,   -1,   -1,   47,  123,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   56,   33,   -1,
   33,   -1,   -1,   -1,   -1,   -1,   41,   -1,   41,   -1,
   -1,   -1,   47,   -1,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   56,   -1,   56,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   67,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   77,   -1,   77,   -1,   -1,   -1,   83,   -1,
   83,   -1,   -1,   -1,  188,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,  128,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  119,   -1,   -1,   -1,   -1,   -1,
  125,   -1,  125,  128,   -1,  128,   -1,   -1,   -1,  134,
   -1,  134,  137,  138,  137,  138,   -1,   -1,   -1,   -1,
  169,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  164,
   -1,  164,   -1,   -1,  169,   -1,  169,   -1,   -1,   -1,
  175,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  200,   -1,  200,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  213,   -1,
  213,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=275;
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
null,null,null,null,null,null,null,"lista_constantes","ID","CTE","STR","EQ",
"GEQ","LEQ","NEQ","DASIG","FLECHA","PRINT","IF","ELSE","ENDIF","UINT","CVR",
"DO","WHILE","RETURN",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID cuerpo_programa",
"programa : ID conjunto_sentencias",
"programa : cuerpo_programa",
"programa : error",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa : '{' '}'",
"cuerpo_programa :",
"cuerpo_programa : '{' error '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : conjunto_sentencias error punto_sincronizacion_sentencia",
"punto_sincronizacion_sentencia : ';'",
"punto_sincronizacion_sentencia : '}'",
"punto_sincronizacion_sentencia : token_inicio_sentencia",
"token_inicio_sentencia : ID",
"token_inicio_sentencia : IF",
"token_inicio_sentencia : UINT",
"token_inicio_sentencia : PRINT",
"token_inicio_sentencia : DO",
"token_inicio_sentencia : RETURN",
"token_inicio_sentencia : WHILE",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"punto_y_coma_obligatorio : error",
"punto_y_coma_obligatorio : ';'",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT error ';'",
"declaracion_variable : UINT variable DASIG constante ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"asignacion_multiple : asignacion_balanceada ';'",
"asignacion_balanceada : variable ',' asignacion_balanceada ',' constante",
"asignacion_balanceada : variable '=' constante",
"asignacion_balanceada : asignacion_balanceada ',' constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')' punto_y_coma_obligatorio",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"conjunto_sentencias_ejecutables : error sentencia_ejecutable",
"sentencia_ejecutable : invocacion_funcion",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG expresion error",
"asignacion_simple : variable error expresion ';'",
"sentencia_control : if",
"sentencia_control : do_while",
"condicion : '(' cuerpo_condicion ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion error",
"condicion : '(' cuerpo_condicion error",
"cuerpo_condicion : expresion comparador expresion",
"cuerpo_condicion : expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : IF condicion cuerpo_ejecutable rama_else ENDIF ';'",
"if : IF condicion cuerpo_ejecutable rama_else ENDIF error",
"if : IF condicion cuerpo_ejecutable rama_else ';'",
"if : IF condicion cuerpo_ejecutable rama_else error",
"if : IF error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"do_while : DO cuerpo_do ';'",
"do_while : DO cuerpo_do error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do : fin_cuerpo_do",
"cuerpo_do : cuerpo_ejecutable condicion",
"fin_cuerpo_do : WHILE condicion",
"impresion : PRINT '(' imprimible ')' ';'",
"impresion : PRINT '(' imprimible ')' error",
"impresion : PRINT '(' ')' ';'",
"impresion : PRINT '(' ')' error",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : expresion operador_suma error",
"expresion : expresion termino_simple",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"termino_simple : termino_simple operador_multiplicacion factor",
"termino_simple : factor_simple",
"termino_simple : termino_simple operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : invocacion_funcion",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : invocacion_funcion",
"constante : CTE",
"constante : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion",
"declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
"cuerpo_funcion : '{' conjunto_sentencias '}'",
"cuerpo_funcion : '{' '}'",
"sentencia_retorno : RETURN '(' expresion ')' ';'",
"sentencia_retorno : RETURN '(' expresion ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
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

//#line 699 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    this.readAgain = false;
    
    // Descomentar la siguiente línea para activar el debugging.
    //yydebug = true;
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

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

Token getAppropiateToken() {

    Token token;
    // Se lee nuevamente el último token.
    // Útil para recuperar el token de sincronización en reglas de error.
    if (this.readAgain) {
        token = lexer.getCurrentToken();
        this.readAgain = false;
    } else {
        token = lexer.getNextToken();
    }

    return token;
}

// --------------------------------------------------------------------------------------------------------------------

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

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
 // Se ejecuta cada vez que encuentra un token error.
public void yyerror(String s) {
    /*
    // 'yylval' contiene el valor del token que el parser no pudo procesar.
    String errorMessage;
    if (yylval != null) {
        errorMessage = String.format(
            "Error de sintaxis: token inesperado '%s'.",
            yylval.sval
        );
    } else {
        errorMessage = "Error de sintaxis: se encontró un token inesperado.";
    }

    Printer.printBetweenSeparations(errorMessage);*/
}

// --------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// --------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// --------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// --------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// --------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 751 "Parser.java"
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
//#line 68 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 73 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 75 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 77 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
            descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
        }
break;
case 6:
//#line 91 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 7:
//#line 93 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 8:
//#line 95 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 11:
//#line 107 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 28:
//#line 155 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia debe finalizar con ';'. Se reanudará después del próximo ';'."); }
break;
case 31:
//#line 169 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 32:
//#line 173 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 34:
//#line 183 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 35:
//#line 188 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 40:
//#line 251 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 42:
//#line 265 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 46:
//#line 285 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 49:
//#line 295 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 57:
//#line 314 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 319 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 321 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 342 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 64:
//#line 345 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 65:
//#line 348 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 66:
//#line 351 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 68:
//#line 363 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 75:
//#line 379 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 76:
//#line 389 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 77:
//#line 399 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 78:
//#line 401 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 79:
//#line 403 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 80:
//#line 405 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 83:
//#line 421 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 84:
//#line 426 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 85:
//#line 428 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 87:
//#line 440 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 88:
//#line 442 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 90:
//#line 463 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia PRINT."); }
break;
case 91:
//#line 468 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 92:
//#line 470 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 93:
//#line 472 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento y debe terminar con ';'."); }
break;
case 98:
//#line 493 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ 
            notifyError("Falta de operando en expresión."); 
            /* $$ no se asigna o se le da un valor por defecto*/
        }
break;
case 99:
//#line 498 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
        }
break;
case 103:
//#line 518 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 520 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 106:
//#line 528 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 107:
//#line 530 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 117:
//#line 563 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 119:
//#line 571 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 120:
//#line 580 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 121:
//#line 585 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 123:
//#line 596 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 125:
//#line 605 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 126:
//#line 607 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 128:
//#line 618 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 131:
//#line 630 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 135:
//#line 645 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 136:
//#line 647 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 139:
//#line 656 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
            descartarTokenError();
        }
break;
case 140:
//#line 668 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 142:
//#line 679 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 143:
//#line 686 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 144:
//#line 691 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1173 "Parser.java"
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
