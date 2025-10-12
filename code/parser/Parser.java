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






//#line 10 "gramatica.y"
    package parser;

    import lexer.Lexer;
    import common.Token;
    import common.SymbolTable;
    import utilities.Printer;
//#line 32 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 28 "Parser.java"




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
public final static short EOF=0;
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
    0,    0,    0,   19,    0,   20,    0,    0,    0,   18,
   18,   15,   16,   23,   16,   16,   16,   16,   22,   22,
   21,   21,   17,   17,   17,   24,   24,   26,   26,   29,
   29,   30,   30,   31,   31,   32,   32,   25,   25,   25,
   25,   25,   25,   25,   25,   35,   35,   27,   27,   27,
   27,   27,   27,    8,    8,    8,    8,   33,   33,   33,
   33,   34,   34,   34,   34,   41,   41,   42,   42,   43,
   43,    9,    9,    9,    1,    1,    1,    1,    1,    6,
    6,    2,    2,    2,    2,    4,    4,    4,    7,    7,
    3,    3,    3,    5,    5,    5,   11,   11,   10,   10,
   44,   44,   45,   45,   47,   47,   46,   46,   48,   48,
   48,   48,   48,   48,   48,   39,   39,   39,   39,   39,
   49,   49,   49,   40,   40,   40,   40,   40,   50,   51,
   51,   52,   28,   28,   28,   55,   55,   54,   53,   53,
   56,   56,   56,   58,   58,   57,   57,   57,   59,   59,
   59,   36,   36,   36,   36,   36,   12,   13,   13,   14,
   14,   37,   37,   37,   37,   60,   61,   61,   61,   62,
   62,   38,   38,   38,   38,   38,   65,   65,   65,   64,
   63,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    0,    4,    2,    1,    1,
    1,    3,    3,    0,    4,    2,    0,    3,    2,    2,
    2,    2,    1,    2,    2,    1,    1,    1,    2,    0,
    1,    1,    1,    3,    2,    1,    2,    2,    1,    1,
    1,    1,    1,    1,    2,    1,    1,    3,    3,    3,
    3,    5,    2,    3,    3,    2,    2,    4,    4,    3,
    3,    4,    6,    4,    6,    3,    1,    2,    1,    2,
    1,    1,    3,    2,    1,    3,    3,    2,    2,    1,
    1,    3,    1,    3,    2,    3,    1,    3,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    3,    2,    1,    0,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    2,    5,
    0,    2,    1,    3,    3,    3,    3,    2,    2,    1,
    2,    2,    6,    5,    7,    1,    2,    3,    1,    0,
    1,    3,    1,    2,    2,    3,    2,    2,    0,    1,
    1,    5,    5,    4,    3,    2,    4,    1,    3,    3,
    1,    3,    3,    3,    3,    3,    2,    1,    0,    1,
    1,    4,    4,    5,    4,    5,    1,    2,    0,    3,
    4,
};
final static short yydefred[] = {                         0,
    0,    9,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    2,
    0,    0,   23,   26,   27,   28,    0,   39,   40,   41,
   42,   43,   44,   46,   47,    0,   10,   11,    5,    0,
   25,    0,    0,   97,  170,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,  168,
  119,    0,    0,    0,   53,    0,    0,    0,    0,  128,
    0,    0,   32,    0,   33,    0,    0,  130,  156,    0,
    0,    0,   19,   16,    0,    0,    0,    0,   67,    0,
    0,    0,    0,   45,   38,   24,   20,    0,   31,   29,
    0,   36,    0,    0,    0,    7,  100,    0,    0,  158,
    0,   98,  167,    0,   95,   80,   81,    0,   87,    0,
   94,   96,    0,   85,   91,  163,  162,  165,  164,  102,
    0,    0,    0,    0,    0,   50,   57,   49,    0,    0,
  151,  150,    0,    0,    0,  141,  143,    0,   51,   56,
   48,    0,    0,  132,   35,    0,  131,  129,  126,  124,
  127,  125,    0,    0,  155,    0,    0,    0,    0,   61,
    0,    0,   68,   60,    0,   69,    0,    0,    0,    0,
    0,    0,    0,    0,   37,    0,    0,    0,    0,    0,
  157,  166,    0,   77,    0,   84,   82,  122,    0,    0,
  111,  113,  112,  114,  115,  109,  110,    0,  106,  105,
  101,   54,    0,  145,    0,    0,    0,  148,   55,    0,
   34,  154,    0,   21,   22,  181,   59,   58,   64,   62,
    0,    0,   66,   15,    0,    0,    0,  172,  173,    0,
  175,   12,  160,  159,   88,   86,    0,  118,  120,    0,
    0,    0,  136,  134,  142,  146,   52,  153,  152,    0,
   72,   70,  174,  180,  176,  117,  116,    0,  133,  137,
    0,   65,   63,    0,   74,  135,  138,   73,
};
final static short yydgoto[] = {                          4,
   51,   52,   53,  118,  119,  120,   54,   68,  260,   17,
   56,   18,  109,  110,   19,   20,  271,   39,    5,    7,
  168,   22,   98,   23,   24,   25,   26,   27,  100,   74,
   75,  104,   28,   29,   30,   31,   32,   33,   34,   35,
   92,   93,  233,   63,   64,  135,  211,  208,  133,   76,
   77,   78,  144,  253,  254,  145,  146,  147,  148,   58,
   59,   60,   36,  186,  182,
};
final static short yysindex[] = {                         3,
   24,    0,   31,    0, -100,    0, -232,  -93,  -12,  429,
  -38,  -31,  -11,  539,  -35, -224,  524,  -43,    0,    0,
  163,  -68,    0,    0,    0,    0,    4,    0,    0,    0,
    0,    0,    0,    0,    0,  177,    0,    0,    0,  -54,
    0, -184,   23,    0,    0,  330, -180,    0,    0,  405,
  350,   94,    0,  359, -143,    0,    0,  -40,   50,    0,
    0,   81,   75,   23,    0,  160,   18,  111, -130,    0,
   98,  188,    0,  -33,    0,   84,   89,    0,    0,  454,
  315,   21,    0,    0,   87, -107,   53, -105,    0,  368,
 -143,   27,   15,    0,    0,    0,    0,   99,    0,    0,
  188,    0,  100,  201,   99,    0,    0,   34,   88,    0,
   94,    0,    0,  119,    0,    0,    0,   94,    0,   38,
    0,    0,  207,    0,    0,    0,    0,    0,    0,    0,
  213, -106, -108,  339,   -2,    0,    0,    0,  -88,   18,
    0,    0, -236,  131,  129,    0,    0, -214,    0,    0,
    0,  -82,   27,    0,    0,  226,    0,    0,    0,    0,
    0,    0,  120,  271,    0,    0,   56,   57,  142,    0,
  333,  147,    0,    0,   72,    0,   27,  113,  237,  145,
  135,  -60,  100,  -28,    0,    0,  -59,  124,  -56,   23,
    0,    0,  293,    0,   94,    0,    0,    0,  -49,  146,
    0,    0,    0,    0,    0,    0,    0,   23,    0,    0,
    0,    0,  167,    0,   86, -236, -105,    0,    0,  151,
    0,    0,   90,    0,    0,    0,    0,    0,    0,    0,
   27,  171,    0,    0,   48,    0,  216,    0,    0,   59,
    0,    0,    0,    0,    0,    0,  130,    0,    0,  350,
  204,   51,    0,    0,    0,    0,    0,    0,    0,   66,
    0,    0,    0,    0,    0,    0,    0,   63,    0,    0,
  140,    0,    0,   27,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        22,
   82,    0,  290,    0,  290,    0,    0,    0,  471,  132,
  414,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  348,  152,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -8,  481,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  439,   83,    0,    0,  125,   13,    0,    0,    0,
  414,    0,    0,  414,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   74,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  107,  107,    0,    0,    0,  103,    0,    0,
  493,    0,    0,    0,    0,    0,    0,  503,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -45,   33,    0,    8,    0,    0,    0,    0,    0,   13,
    0,    0, -151,    0,  309,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   12,  364,  415,    0,    0,
    0,  -14,    0,    0,    0,    0,    0,    0,  107,    0,
  149,    0,  107,    0,    0,  149,    0,    0,    0,    0,
    0,    0,    0,    0,  515,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -23,  115,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   76,    0,    0,    0,  165,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  595,  -15,  434,    0,    0,    0,  -10,    0,    0,   -6,
  488,   43,    0,  233,   10,  420,  505,    0,    0,    0,
    0,    0,    0,  -21,  711,    0,    0,    0,    0,  -27,
  392,  -34,    0,    0,    0,    0,    0,    0,    0,    0,
  338,    0,    0,   97,    0,    0,    0,    0,  300,    0,
    0,  362,  298,  189,    0,    0, -117,    0,    0,    0,
    0,  389,    0,  363,   16,
};
final static int YYTABLESIZE=890;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         96,
   30,   62,    2,   55,   16,   69,   62,   55,   67,  248,
   91,   18,  236,  123,   37,   95,   47,  144,  127,  141,
  144,    4,   15,    6,   40,  214,   99,   43,   16,   99,
  111,   41,  171,   42,  142,  132,   55,  156,  210,   55,
   30,  123,  172,   55,  121,   86,   99,   55,  108,  106,
  171,   25,   57,  140,   97,  217,   57,   55,   88,   57,
  107,  143,   99,   96,   49,   46,  179,   47,  105,   48,
   16,   47,  107,   55,  121,   89,  116,  112,  117,   49,
   55,  173,   47,  121,   48,   57,  176,   83,   57,   84,
   16,  121,   57,  122,   49,   46,   57,   47,  255,   48,
  123,  121,   16,  198,  195,  149,   57,  193,  129,  274,
   47,   72,  125,   55,   16,  231,   55,   69,  149,  187,
   71,  130,   57,  122,  273,   30,   16,  121,  191,   57,
  230,  190,  122,  153,   69,   49,   25,   62,   16,  180,
   48,  218,  160,  161,    4,  166,  161,  162,  259,  169,
  122,  172,   16,   15,  152,  147,   96,  121,  147,  192,
  200,  131,   57,   16,  121,   57,   96,  154,  212,  151,
  157,  215,  216,   55,  219,  270,  122,   55,  222,   16,
  224,  225,  226,   55,  123,  236,   55,  276,  267,   47,
  169,   14,   42,  238,  235,  239,  241,   72,  240,  140,
  243,   55,   16,  139,  249,   42,  122,  251,  252,  257,
  256,  167,   94,  122,  262,  126,   16,   61,  138,  247,
   82,    9,   57,  123,   65,   66,   57,   16,    9,   44,
   10,   11,   57,  149,   12,   57,   13,  234,   14,   71,
  184,   86,   99,  121,   70,    9,  149,  171,  242,   96,
   57,   47,   16,  209,   10,   11,  264,   30,    1,    3,
   13,   71,   14,  108,  277,   16,   30,   30,   25,  149,
   30,  172,   30,  141,   30,  107,  184,   25,   25,    9,
   44,   25,  149,   25,   44,   25,    8,    9,  142,   17,
    9,  115,  122,  194,    9,   44,   10,   11,  189,  101,
   12,  121,   13,  263,   14,  128,    8,    9,  170,    9,
   44,  223,  155,  116,  265,  117,   10,   11,    8,    9,
   12,  272,   13,   44,   14,  183,  268,  229,   10,   11,
   69,    9,   12,   71,   13,   72,   14,   47,    6,  159,
   10,   11,  131,    9,  161,  258,   13,    3,   14,  139,
  221,  121,   10,   11,    8,    9,   12,  116,   13,  117,
   14,  221,  179,   12,   10,   11,  149,  150,   12,    9,
   13,   49,   14,  165,   47,  116,   48,  117,   10,   11,
    9,  116,   12,  117,   13,  266,   14,  169,   99,   10,
   11,  228,  116,   12,  117,   13,    9,   14,  207,  205,
  206,    9,   44,   47,  177,   10,   11,   14,   14,   12,
  116,   13,  117,   14,   13,  136,  137,   14,   14,    9,
  178,   14,  244,   14,   38,   14,  174,  103,   10,   11,
  177,  199,   12,    9,   13,  158,   14,  213,  114,  269,
    0,    0,   10,   11,    9,  113,   49,   46,   13,   47,
   14,   48,    0,   10,   11,  104,  104,    9,  104,   13,
  104,   14,  196,    9,   44,  181,   10,   11,   50,    9,
   49,   46,   13,   47,   14,   48,    0,    0,   10,   11,
  103,  103,    9,  103,   13,  103,   14,  124,    0,    0,
    0,   10,   11,    9,  163,   49,   46,   13,   47,   14,
   48,    0,   10,   11,    0,    0,    0,   21,   13,    0,
   14,   99,   99,   99,   99,   99,    0,   99,    0,   85,
    0,   75,    0,   75,   75,   75,    0,    9,  115,   99,
   99,   99,   99,   79,    0,   79,   79,   79,    0,   75,
   75,   75,   75,   78,    0,   78,   78,   78,  245,    9,
   44,   79,   79,   79,   79,   76,  197,   76,   76,   76,
    0,   78,   78,   78,   78,   49,   46,   88,   47,    0,
   48,    9,  115,   76,   76,   76,   76,    0,   80,  175,
   49,   46,    0,   47,   89,   48,    9,   44,  227,    9,
  115,    0,    0,    0,    0,    9,  115,    0,  201,  202,
  203,  204,  178,    0,    0,    0,    9,  115,   81,  188,
    0,   90,    0,  237,    0,    9,   44,  237,    0,    0,
    0,    0,    0,    0,    9,  115,  246,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  108,    0,    0,
  220,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  134,    0,
    0,    9,   44,   45,  232,    0,    0,    0,    0,    0,
  104,  104,    0,    0,  164,    0,    0,    0,    0,    0,
    0,  171,    0,    0,    0,    9,   44,   45,    0,    0,
    0,    0,    0,    0,    0,  103,  103,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    9,   44,    0,    0,    0,    0,    0,    0,  261,    0,
    0,    0,    0,   73,    0,    0,   99,   99,   99,    0,
   99,   99,   99,   99,   99,   99,   75,   75,   75,    0,
   75,   75,   75,   75,    0,   75,  102,  275,   79,   79,
   79,    0,   79,   79,   79,   79,    0,   79,   78,   78,
   78,  278,   78,   78,   78,   78,    0,   78,    0,    0,
   76,   76,   76,   73,   76,   76,   76,   76,    0,   76,
    9,   44,  102,    0,  108,    0,    0,   87,    0,    0,
    0,    0,    0,    0,   79,    9,   44,    0,    0,    0,
    0,    0,  250,    0,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,  185,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   73,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  185,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  185,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
    0,   40,    0,   10,   40,   12,   40,   14,   40,   59,
   17,    0,   41,   59,    5,   59,   45,   41,   59,  256,
   44,    0,  123,    0,  257,  143,   41,   40,   40,   44,
   46,  125,   41,   46,  271,   63,   43,   72,   41,   46,
   40,   52,  257,   50,   51,  270,   61,   54,   41,   40,
   59,   40,   10,   41,  123,  270,   14,   64,   44,   17,
   41,   44,   59,   85,   42,   43,  101,   45,  123,   47,
   40,   45,  257,   80,   81,   61,   43,  258,   45,   42,
   87,   88,   45,   90,   47,   43,   93,  123,   46,  125,
   40,   59,   50,   51,   42,   43,   54,   45,  216,   47,
  111,  108,   40,  131,  120,  257,   64,  118,   59,   44,
   45,  123,  256,  120,   40,   44,  123,   44,  270,  104,
   45,   41,   80,   81,   59,  125,   40,  134,   41,   87,
   59,   44,   90,  264,   61,   42,  125,   40,   40,   40,
   47,  148,   59,   41,  123,  125,   44,   59,   59,  257,
  108,  257,   40,  123,   44,   41,  178,  164,   44,   41,
  269,  268,  120,   40,  171,  123,  188,   71,  257,   59,
   74,   41,   44,  180,  257,  125,  134,  184,   59,   40,
  125,  125,   41,  190,  195,   41,  193,  125,   59,   45,
   59,   40,   46,   59,  179,  256,  256,  123,  183,   40,
  257,  208,   40,   44,   59,   46,  164,   41,  123,   59,
  217,  125,  256,  171,   44,  256,   40,  256,   59,  269,
  256,  257,  180,  269,  256,  257,  184,   40,  257,  258,
  266,  267,  190,  257,  270,  193,  272,  125,  274,  273,
   40,  270,  257,  250,  256,  257,  270,  256,  125,  271,
  208,   45,   40,  256,  266,  267,   41,  257,  256,  257,
  272,  273,  274,  256,  125,   40,  266,  267,  257,  257,
  270,  257,  272,  256,  274,  256,   40,  266,  267,  257,
  258,  270,  270,  272,  258,  274,  256,  257,  271,    0,
  257,  258,  250,  256,  257,  258,  266,  267,  265,  123,
  270,  269,  272,  256,  274,  256,  256,  257,  256,  257,
  258,   41,  125,   43,  256,   45,  266,  267,  256,  257,
  270,  256,  272,  258,  274,  125,  123,  256,  266,  267,
  257,  257,  270,  258,  272,  123,  274,   45,  257,  256,
  266,  267,  268,  257,  256,  256,  272,    0,  274,   41,
  125,  269,  266,  267,  256,  257,  270,   43,  272,   45,
  274,  125,  256,    0,  266,  267,  256,  257,  270,  257,
  272,   42,  274,   59,   45,   43,   47,   45,  266,  267,
  257,   43,  270,   45,  272,  256,  274,  256,  264,  266,
  267,   59,   43,  270,   45,  272,  257,  274,   60,   61,
   62,  257,  258,   45,  256,  266,  267,  256,  257,  270,
   43,  272,   45,  274,    0,  256,  257,  266,  267,  257,
  256,  270,  190,  272,    5,  274,   59,   36,  266,  267,
   93,  132,  270,  257,  272,   74,  274,  140,   50,  251,
   -1,   -1,  266,  267,  257,   41,   42,   43,  272,   45,
  274,   47,   -1,  266,  267,   42,   43,  257,   45,  272,
   47,  274,  256,  257,  258,  103,  266,  267,   40,  257,
   42,   43,  272,   45,  274,   47,   -1,   -1,  266,  267,
   42,   43,  257,   45,  272,   47,  274,   54,   -1,   -1,
   -1,  266,  267,  257,   41,   42,   43,  272,   45,  274,
   47,   -1,  266,  267,   -1,   -1,   -1,    3,  272,   -1,
  274,   41,   42,   43,   44,   45,   -1,   47,   -1,   15,
   -1,   41,   -1,   43,   44,   45,   -1,  257,  258,   59,
   60,   61,   62,   41,   -1,   43,   44,   45,   -1,   59,
   60,   61,   62,   41,   -1,   43,   44,   45,  256,  257,
  258,   59,   60,   61,   62,   41,  123,   43,   44,   45,
   -1,   59,   60,   61,   62,   42,   43,   44,   45,   -1,
   47,  257,  258,   59,   60,   61,   62,   -1,   40,   92,
   42,   43,   -1,   45,   61,   47,  257,  258,  256,  257,
  258,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,   98,   -1,   -1,   -1,  257,  258,   14,  105,
   -1,   17,   -1,  180,   -1,  257,  258,  184,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  193,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   43,   -1,   -1,
  153,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   64,   -1,
   -1,  257,  258,  259,  177,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,   80,   -1,   -1,   -1,   -1,   -1,
   -1,   87,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,  231,   -1,
   -1,   -1,   -1,   13,   -1,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,   36,  260,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,  256,  257,
  258,  274,  260,  261,  262,  263,   -1,  265,   -1,   -1,
  256,  257,  258,   63,  260,  261,  262,  263,   -1,  265,
  257,  258,   72,   -1,  190,   -1,   -1,  264,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,  208,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  101,   -1,   -1,  104,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  131,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  156,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  179,
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
"programa : ID cuerpo_programa_recuperacion",
"programa : ID conjunto_sentencias",
"$$1 :",
"programa : $$1 programa_sin_nombre",
"$$2 :",
"programa : error $$2 ID cuerpo_programa",
"programa : error EOF",
"programa : EOF",
"programa_sin_nombre : cuerpo_programa",
"programa_sin_nombre : cuerpo_programa_recuperacion",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa_recuperacion : '{' conjunto_sentencias lista_llaves_cierre",
"$$3 :",
"cuerpo_programa_recuperacion : lista_llaves_apertura $$3 conjunto_sentencias '}'",
"cuerpo_programa_recuperacion : '{' '}'",
"cuerpo_programa_recuperacion :",
"cuerpo_programa_recuperacion : '{' error '}'",
"lista_llaves_apertura : '{' '{'",
"lista_llaves_apertura : lista_llaves_apertura '{'",
"lista_llaves_cierre : '}' '}'",
"lista_llaves_cierre : lista_llaves_cierre '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : error '}'",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : declaracion_variables",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"sentencia_ejecutable : invocacion_funcion ';'",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"sentencia_ejecutable : invocacion_funcion error",
"sentencia_control : if",
"sentencia_control : do_while",
"declaracion_variables : UINT lista_variables ';'",
"declaracion_variables : UINT ID ';'",
"declaracion_variables : UINT ID error",
"declaracion_variables : UINT lista_variables error",
"declaracion_variables : UINT variable DASIG constante ';'",
"declaracion_variables : UINT error",
"lista_variables : ID ',' ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"lista_variables : ID ID",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG expresion error",
"asignacion_simple : variable expresion ';'",
"asignacion_simple : variable DASIG error",
"asignacion_multiple : variable asignacion_par constante ';'",
"asignacion_multiple : variable asignacion_par constante ',' lista_constantes ';'",
"asignacion_multiple : variable asignacion_par constante error",
"asignacion_multiple : variable asignacion_par constante ',' lista_constantes error",
"asignacion_par : variable_con_coma asignacion_par constante_con_coma",
"asignacion_par : '='",
"variable_con_coma : ',' variable",
"variable_con_coma : variable",
"constante_con_coma : constante ','",
"constante_con_coma : constante",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"expresion : termino",
"expresion : expresion operador_suma termino",
"expresion : expresion operador_suma error",
"expresion : expresion termino_simple",
"expresion : '+' termino",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"termino : operador_multiplicacion factor",
"termino_simple : termino_simple operador_multiplicacion factor",
"termino_simple : factor_simple",
"termino_simple : termino_simple operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable error",
"factor : constante",
"factor : invocacion_funcion",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : invocacion_funcion",
"constante : CTE",
"constante : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"condicion : parentesis_apertura cuerpo_condicion parentesis_cierre",
"condicion : '(' ')'",
"parentesis_apertura : '('",
"parentesis_apertura :",
"parentesis_cierre : ')'",
"parentesis_cierre : error",
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
"if : IF error",
"if : IF condicion rama_else ENDIF ';'",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"rama_else : ELSE",
"do_while : DO cuerpo_do ';'",
"do_while : DO cuerpo_do_recuperacion ';'",
"do_while : DO cuerpo_do error",
"do_while : DO cuerpo_do_recuperacion error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do_recuperacion : fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_ejecutable condicion",
"fin_cuerpo_do : WHILE condicion",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible",
"declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' '}'",
"cuerpo_funcion : cuerpo_funcion_admisible",
"cuerpo_funcion : '{' '}'",
"cuerpo_funcion_admisible : '{' conjunto_sentencias '}'",
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
"sentencia_retorno : RETURN '(' expresion ')' ';'",
"sentencia_retorno : RETURN '(' expresion ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
"sentencia_retorno : RETURN expresion ';'",
"sentencia_retorno : RETURN error",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expresion FLECHA ID",
"argumento : expresion",
"impresion : PRINT imprimible ';'",
"impresion : PRINT imprimible error",
"impresion : PRINT imprimible_recuperacion ';'",
"impresion : PRINT imprimible_recuperacion error",
"imprimible : '(' elemento_imprimible ')'",
"imprimible_recuperacion : '(' ')'",
"imprimible_recuperacion : elemento_imprimible",
"imprimible_recuperacion :",
"elemento_imprimible : STR",
"elemento_imprimible : expresion",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda_admisible ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"argumento_lambda : argumento_lambda_admisible",
"argumento_lambda : '(' ')'",
"argumento_lambda :",
"argumento_lambda_admisible : '(' factor ')'",
"parametro_lambda : '(' UINT ID ')'",
};

//#line 843 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    
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
    Printer.printWrapped(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
    this.warningsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printWrapped(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
    this.errorsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isUint(String number) {
    return !number.contains(".");
}

// --------------------------------------------------------------------------------------------------------------------

public void modificarSymbolTable(String lexemaNuevo, String lexemaAnterior) {
    SymbolTable.getInstance().decrementarReferencia(lexemaAnterior);
    if (lexemaNuevo != null) {
        SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
    }
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 783 "Parser.java"
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
//#line 68 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 75 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 78 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 6:
//#line 80 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); }
break;
case 8:
//#line 83 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 9:
//#line 86 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 13:
//#line 107 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 14:
//#line 110 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 16:
//#line 113 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 17:
//#line 115 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 18:
//#line 117 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
break;
case 25:
//#line 143 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 35:
//#line 184 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 38:
//#line 202 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 45:
//#line 213 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 48:
//#line 229 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 49:
//#line 232 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 50:
//#line 237 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 51:
//#line 241 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 52:
//#line 245 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 249 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 55:
//#line 259 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 56:
//#line 264 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 271 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 58:
//#line 285 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 59:
//#line 291 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 60:
//#line 294 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 61:
//#line 297 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 62:
//#line 307 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 63:
//#line 309 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 314 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 65:
//#line 316 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 69:
//#line 331 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 71:
//#line 339 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 347 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 352 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 366 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 371 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 78:
//#line 375 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 382 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 80:
//#line 392 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 81:
//#line 394 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 82:
//#line 401 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 84:
//#line 407 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 85:
//#line 414 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 86:
//#line 421 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 88:
//#line 427 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 89:
//#line 434 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 90:
//#line 436 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 98:
//#line 463 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;

            notifyDetection(String.format("Constante negativa: %s.",yyval.sval));

            if(isUint(yyval.sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            modificarSymbolTable(yyval.sval,val_peek(0).sval);
        }
break;
case 100:
//#line 482 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 101:
//#line 491 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 102:
//#line 496 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 104:
//#line 502 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 507 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 108:
//#line 518 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 115:
//#line 534 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 116:
//#line 543 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 117:
//#line 548 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 118:
//#line 550 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 119:
//#line 552 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 120:
//#line 554 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 123:
//#line 566 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 124:
//#line 575 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 126:
//#line 581 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 127:
//#line 583 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 128:
//#line 585 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 130:
//#line 598 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 131:
//#line 600 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 615 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 134:
//#line 620 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 623 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 137:
//#line 635 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 140:
//#line 652 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 143:
//#line 664 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 147:
//#line 682 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 148:
//#line 684 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 151:
//#line 696 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 152:
//#line 705 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 153:
//#line 710 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 154:
//#line 712 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 155:
//#line 714 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 156:
//#line 716 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 157:
//#line 725 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 159:
//#line 735 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 160:
//#line 742 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 161:
//#line 747 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 162:
//#line 756 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 163:
//#line 761 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 165:
//#line 764 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 167:
//#line 777 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 168:
//#line 780 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 169:
//#line 782 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 172:
//#line 798 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 173:
//#line 803 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 174:
//#line 805 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 175:
//#line 807 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 176:
//#line 809 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 178:
//#line 820 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 179:
//#line 823 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1380 "Parser.java"
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
