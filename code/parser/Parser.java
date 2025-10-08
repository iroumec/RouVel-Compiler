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
public final static short EOF=0;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,   19,    0,    0,    0,   18,   18,   15,
   16,   22,   16,   16,   16,   16,   21,   21,   20,   20,
   17,   17,   17,   23,   23,   25,   25,   28,   28,   29,
   29,   30,   30,   31,   31,   24,   24,   24,   24,   24,
   24,   24,   24,   34,   34,   26,   26,   26,   26,   26,
   26,    8,    8,    8,    8,   32,   32,   32,   32,   40,
   40,   40,   33,   33,   33,   33,   41,   42,   42,   42,
   42,   43,   43,    9,    9,    9,    1,    1,    1,    1,
    6,    6,    2,    2,    2,    4,    4,    4,    7,    7,
    3,    3,    3,    5,    5,    5,   11,   11,   10,   10,
   44,   44,   44,   44,   44,   45,   45,   46,   46,   46,
   46,   46,   46,   46,   38,   38,   38,   38,   38,   47,
   47,   39,   39,   39,   48,   49,   49,   49,   50,   27,
   27,   53,   53,   52,   51,   51,   54,   54,   54,   56,
   56,   55,   55,   55,   57,   57,   57,   35,   35,   35,
   35,   35,   12,   13,   13,   14,   14,   36,   36,   59,
   59,   59,   59,   58,   60,   60,   37,   37,   37,   37,
   37,   63,   63,   63,   62,   61,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    3,    4,    3,    2,
    4,    4,    2,    4,    2,    4,    3,    5,    1,    4,
    5,    4,    4,    1,    3,    2,    1,    3,    3,    2,
    1,    1,    3,    1,    3,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    3,    2,    2,    2,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    6,    6,    5,    5,    2,    0,
    2,    3,    3,    2,    2,    1,    1,    2,    2,    6,
    5,    1,    2,    3,    1,    0,    1,    3,    1,    2,
    2,    3,    2,    2,    0,    1,    1,    5,    5,    4,
    3,    2,    4,    1,    3,    3,    1,    3,    3,    1,
    2,    1,    0,    3,    1,    1,    4,    4,    5,    4,
    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   37,   38,   39,   40,
   41,   42,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   97,  165,    0,    0,    0,    0,   84,   91,
   92,   93,    0,    0,    0,  119,    0,    0,    0,    0,
   50,    0,    0,    0,    0,  124,    0,    0,   30,    0,
   31,    0,    0,    0,  152,    0,    0,    0,   17,   14,
    0,    0,    0,    0,    0,   69,    0,    0,   43,   36,
   22,   18,    0,   29,   27,   65,   63,    0,    0,   34,
    0,    0,  100,    0,    0,  154,   98,    0,    0,   95,
   81,   82,    0,   87,    0,   94,   96,   89,   90,    0,
  158,  159,  103,    0,  110,  112,  111,  113,  114,  108,
  109,    0,    0,  104,  102,   48,   55,   47,    0,    0,
  147,  146,    0,    0,    0,  137,  139,    0,   49,   54,
   46,    0,    0,  129,   33,    0,    0,  125,  122,  123,
    0,    0,  151,    0,    0,    0,    0,    0,    0,    0,
    0,   57,    0,    0,   59,   67,    0,    0,   74,    0,
    0,    0,    0,    0,    0,   35,    0,    0,    0,    0,
  153,  164,    0,   79,    0,   85,   83,  105,  101,    0,
    0,    0,   52,    0,  141,    0,    0,    0,  144,   53,
    0,   32,  150,    0,   19,   20,  176,   58,   56,    0,
    0,   60,    0,    0,    0,   13,   66,   64,    0,   76,
    0,    0,    0,  167,  168,    0,  170,  156,  155,   88,
   86,  121,  118,    0,  117,    0,    0,  132,  131,  138,
  142,   51,  149,  148,    0,    0,    0,    0,    0,    0,
   75,  169,  175,  171,  116,  115,    0,  130,  133,    0,
   61,   62,    0,    0,   68,   71,  134,   73,   72,
};
final static short yydgoto[] = {                          3,
   58,   48,   49,  113,  114,  115,  120,   64,  178,   50,
   51,   52,  105,  106,   18,   19,  270,   39,    4,  166,
   21,   93,   22,   23,   24,   25,   26,   95,   70,   71,
  102,   27,   28,   29,   30,   31,   32,   33,   34,  172,
   35,   88,  225,   59,   60,  132,  202,   72,   73,   74,
  144,  248,  249,  145,  146,  147,  148,   53,   54,   55,
   36,  187,  183,
};
final static short yysindex[] = {                      -134,
    2,   45,    0,  -92,    0,    0,  -80,  -12,   51,   65,
   31,  -30,   90,  -10, -237,  153,  -46,    0,    0,   23,
  -74,    0,    0,    0,    0,    9,    0,    0,    0,    0,
    0,    0,    0,    0,   14,  -33,    0,    0,    0,    0,
 -198,  275,    0,    0, -188,  129,  322,   -5,    0,    0,
    0,    0,   13, -180,    0,    0,  273,  283,  162,   -1,
    0,  133,  -40,  372, -181,    0,   42,  166,    0,    8,
    0,   30, -159,    0,    0,  314,  326,  -23,    0,    0,
   84, -138,  275,  275, -130,    0,  382,  114,    0,    0,
    0,    0,   96,    0,    0,    0,    0,  114,  166,    0,
   89,  178,    0,  -37,   37,    0,    0,    0,   62,    0,
    0,    0,   -5,    0,  294,    0,    0,    0,    0,  337,
    0,    0,    0,   16,    0,    0,    0,    0,    0,    0,
    0,  275, -129,    0,    0,    0,    0,    0, -117,  -40,
    0,    0, -227,  100,   98,    0,    0, -231,    0,    0,
    0, -114,  114,    0,    0,  182,    0,    0,    0,    0,
   85,   75,    0,    0,   21,   24,  110,  414,  431,   -5,
 -104,    0,  108,  347,    0,    0,  107,  402,    0,  205,
  316,   99,  -96,   89,   -9,    0,    0,  -94,  -93,  275,
    0,    0, -125,    0,   -5,    0,    0,    0,    0,  322,
  162,  301,    0,  124,    0,   43, -227, -130,    0,    0,
  112,    0,    0,  -45,    0,    0,    0,    0,    0,  294,
  337,    0,  347,  114,  114,    0,    0,    0,  114,    0,
  -78,    0,  139,    0,    0,  -71,    0,    0,    0,    0,
    0,    0,    0,  -44,    0,   67,   68,    0,    0,    0,
    0,    0,    0,    0,  -31,  -70,  114,  114,  147,  152,
    0,    0,    0,    0,    0,    0,   96,    0,    0,  127,
    0,    0,  155,  157,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         5,
    0,  203,    0,  203,    0,    0,    0,  422,  -51,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  219,
  148,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,  444,    0,    0,
    0,    0, -159,    0,   -3,    0,    0,   71,    0,    0,
    0,  143,   26,    0,    0,    0,    0,    0,    0,    0,
    0,   50,    0,  168,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  170,  170,    0,   57,    0,    0,    0,  174,    0,    0,
    0,    0,  454,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  362,    0,    0,    0,    0,    0,    0,   26,
    0,    0, -206,    0,  290,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  181,    0,    0,    0,
    0,    0,    0,   12,  413,  442,    0,    0,    0,  435,
  333,    0,   -6,    0,    0,    0,    0,    0,    0,  170,
    0,  187,    0,  170,    0,    0,  187,    0,    0,    0,
    0,    0,    0,    0,  466,    0,    0,    0,    0,   93,
    0,    0,    0,    0,    0,    0,    6,   63,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  195,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  475,  353,    0,    0,  144,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  721,  -38,  -60,    0,  260,  286,  -90,    0,    0,    7,
  567,  136,    0,  268,  456,  464,   60,    0,    0,    0,
    0,    0,  -20,  517,    0,    0,    0,    0,  -56,  434,
  -43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -154,  252,   47,  429,    0,    0,    0,    0,  421,
  352,  247,    0,    0, -121,    0,    0,    0,    0,  450,
    0,  399,  -85,
};
final static int YYTABLESIZE=911;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         91,
   28,    6,  133,  143,    4,  111,   15,  112,   16,   15,
  119,   16,   90,  254,  266,  118,  188,   65,   16,  224,
   16,  205,  193,  171,  156,  173,   16,   42,  141,   15,
   14,  232,   82,   41,   99,   45,  119,   99,  208,  135,
   28,  118,   16,  142,   40,  170,  140,   57,   92,  140,
  145,   23,   45,  116,   99,  180,  199,   98,  103,  197,
   91,   20,   15,  145,  116,   16,  136,   94,  257,  107,
   63,  121,   97,   81,   16,  122,  195,  191,  166,  221,
  190,   57,  153,  116,   15,  250,   45,   16,  159,   99,
   46,  174,   68,  116,  231,   45,  160,  157,  236,   16,
  157,  164,  192,  143,   57,   16,  143,   15,   16,   45,
  116,  107,   79,  154,   80,  214,  157,  111,  167,  112,
  233,    1,    2,   15,  233,   28,  173,    4,  181,   76,
  240,    8,  110,  106,   45,   15,   23,   17,  201,  203,
  206,  207,  210,  213,  242,  215,   15,   17,  216,   17,
  217,  222,  177,   41,  209,   17,   91,  234,   45,  235,
  256,  237,   16,  238,  246,  247,   15,   14,  116,  108,
  252,   17,  140,   45,  116,  116,  139,  262,   41,  263,
  223,  255,  117,   16,  264,  272,   16,   12,   70,  267,
  275,  138,  269,  117,   17,  276,   85,   45,  278,  116,
  279,   15,   15,   17,  163,   15,  116,   16,  165,   89,
  253,  265,  117,   86,  251,  141,   17,  185,    3,    8,
  110,   15,  117,    8,  271,   66,    8,  189,   17,  223,
  142,  226,    9,   10,   17,    9,   10,   17,   12,  117,
   13,   12,   67,   13,  185,   78,    8,    8,   43,   91,
   99,  277,  162,   16,  134,    9,   10,   28,    5,   11,
   82,   12,  145,   13,    8,   43,   28,   28,   23,   96,
   28,  198,   28,   16,   28,  145,   16,   23,   23,    8,
   67,   23,  145,   23,   68,   23,   61,   62,    9,   10,
  155,   17,   11,  166,   12,  145,   13,  117,    8,   43,
    7,    8,  184,  117,  117,  126,  212,    8,   43,   44,
    9,   10,   17,  123,   11,   17,   12,   45,   13,   45,
   56,    8,   43,    7,    8,  111,  107,  112,  117,  212,
  135,    8,  110,    9,   10,  117,   17,   11,   45,   12,
    8,   13,  131,  129,  130,   75,    8,   43,  106,    9,
   10,    7,    8,   11,  161,   12,  232,   13,   45,  245,
   45,    9,   10,    8,  111,   11,  112,   12,  111,   13,
  112,   43,    9,   10,   84,   84,   11,   84,   12,   84,
   13,   45,   17,    8,  163,    8,   43,   44,  136,  137,
   85,   84,    9,   10,   83,   83,   11,   83,   12,   83,
   13,   70,   17,   12,   12,   17,   99,   86,   83,    8,
   43,   83,   10,   12,   12,  152,   84,   12,    8,   12,
  120,   12,    8,  127,  111,  174,  112,    9,   10,  161,
  151,    9,   10,   12,    8,   13,  128,   12,    8,   13,
  175,   11,  172,    9,   10,  229,   45,    9,   10,   12,
  173,   13,  241,   12,  220,   13,  111,  239,  112,   37,
  228,    8,   99,   99,   99,   99,   99,   38,   99,  101,
    9,   10,  218,  111,  258,  112,   12,   77,   13,   77,
   99,   99,   99,   99,   77,  124,   77,   77,   77,  219,
  158,  204,  268,   77,   80,  109,   80,   80,   80,  182,
    0,    0,   77,   77,   77,   77,   78,    0,   78,   78,
   78,    0,   80,   80,   80,   80,    0,   78,    0,   78,
    0,    0,    0,    0,   78,   78,   78,   78,   69,    8,
   43,    8,   43,   78,    0,    0,    0,    0,    0,    8,
  110,    0,  125,  126,  127,  128,    0,    0,    0,  194,
    8,   43,  100,    0,    0,    0,  243,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  244,
    8,   43,    8,   43,    0,   69,    0,    0,    8,  110,
    0,    0,    8,  110,  100,    0,    0,    0,    0,   84,
   84,    0,  196,    8,   43,    0,    0,    0,    0,    0,
    0,    0,    0,  173,    0,    0,    0,    0,    0,   83,
   83,    0,    0,    0,    0,  100,    0,  120,  186,    0,
    0,    0,    0,    0,    0,    0,    0,  149,  150,    0,
  120,    0,    0,    0,    0,    0,    0,    0,    8,  110,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  176,    0,    0,  227,    0,   43,
    0,    0,    0,    0,  179,    0,    0,    0,    0,    0,
    8,  110,  186,    0,    0,    0,    0,   99,   99,   99,
    0,   99,   99,   99,   99,   99,   99,    8,  110,    0,
    0,   77,   77,    0,    0,    0,  186,    0,    0,   77,
   77,   77,    0,   77,   77,   77,   77,    0,   77,   80,
   80,   80,    0,   80,   80,   80,   80,   69,   80,  211,
    0,   78,   78,   78,    0,   78,   78,   78,   78,   47,
   78,   78,   78,   77,    0,    0,   87,    0,    0,    0,
    0,    0,    0,    0,  230,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  104,    0,    0,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  259,  260,    0,    0,    0,  261,  162,    0,    0,    0,
    0,    0,    0,  168,  169,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  273,  274,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  200,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,    0,   59,   44,    0,   43,   40,   45,    2,   40,
   42,    0,   59,   59,   59,   47,  102,   11,   12,  174,
   14,  143,  113,   84,   68,  257,   20,   40,  256,   40,
  123,   41,  270,   46,   41,   45,   42,   44,  270,   41,
   40,   47,   36,  271,  125,   84,   41,   40,  123,   44,
  257,   40,   45,   47,   61,   99,   41,   44,  257,  120,
   81,    2,   40,  270,   58,   59,   41,   59,  223,  258,
   40,   59,   59,   14,   68,  256,  115,   41,   41,  170,
   44,   40,  264,   77,   40,  207,   45,   81,   59,  123,
   40,   85,  123,   87,  180,   45,  256,   41,  184,   93,
   44,  125,   41,   41,   40,   99,   44,   40,  102,   45,
  104,   41,  123,   67,  125,   41,   70,   43,  257,   45,
  181,  256,  257,   40,  185,  125,  257,  123,   40,   40,
  256,  257,  258,   41,   45,   40,  125,    2,  268,  257,
   41,   44,  257,   59,  201,  125,   40,   12,  125,   14,
   41,  256,   93,   46,  148,   20,  177,   59,   45,  256,
  221,  256,  156,  257,   41,  123,   40,  123,  162,   41,
   59,   36,   40,   45,  168,  169,   44,  256,   46,   41,
  174,  220,   47,  177,  256,  256,  180,   40,   45,  123,
   44,   59,  125,   58,   59,   44,   44,   45,   44,  193,
   44,   40,    0,   68,  256,   40,  200,  201,  125,  256,
  256,  256,   77,   61,  208,  256,   81,   40,    0,  257,
  258,   40,   87,  257,  256,  256,  257,  265,   93,  223,
  271,  125,  266,  267,   99,  266,  267,  102,  272,  104,
  274,  272,  273,  274,   40,  256,  257,  257,  258,  270,
  257,  125,  256,  247,  256,  266,  267,  257,  257,  270,
  270,  272,  257,  274,  257,  258,  266,  267,  257,  256,
  270,  256,  272,  267,  274,  270,  270,  266,  267,  257,
  273,  270,  257,  272,  123,  274,  256,  257,  266,  267,
  125,  156,  270,  256,  272,  270,  274,  162,  257,  258,
  256,  257,  125,  168,  169,  256,  125,  257,  258,  259,
  266,  267,  177,   41,  270,  180,  272,   45,  274,   45,
  256,  257,  258,  256,  257,   43,  256,   45,  193,  125,
   41,  257,  258,  266,  267,  200,  201,  270,   45,  272,
  257,  274,   60,   61,   62,  256,  257,  258,  256,  266,
  267,  256,  257,  270,   41,  272,   41,  274,   45,   59,
   45,  266,  267,  257,   43,  270,   45,  272,   43,  274,
   45,  258,  266,  267,   42,   43,  270,   45,  272,   47,
  274,   45,  247,  257,   59,  257,  258,  259,  256,  257,
   44,   59,  266,  267,   42,   43,  270,   45,  272,   47,
  274,  258,  267,  256,  257,  270,  264,   61,  256,  257,
  258,   59,    0,  266,  267,   44,  264,  270,  257,  272,
   59,  274,  257,  256,   43,  256,   45,  266,  267,  256,
   59,  266,  267,  272,  257,  274,  256,  272,  257,  274,
   59,    0,  256,  266,  267,   44,   45,  266,  267,  272,
  256,  274,  193,  272,  169,  274,   43,  190,   45,    4,
   59,  257,   41,   42,   43,   44,   45,    4,   47,   36,
  266,  267,   59,   43,  223,   45,  272,   43,  274,   45,
   59,   60,   61,   62,   41,   57,   43,   44,   45,   59,
   70,  140,  246,   59,   41,   46,   43,   44,   45,  101,
   -1,   -1,   59,   60,   61,   62,   41,   -1,   43,   44,
   45,   -1,   59,   60,   61,   62,   -1,   43,   -1,   45,
   -1,   -1,   -1,   -1,   59,   60,   61,   62,   12,  257,
  258,  257,  258,   59,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,   -1,   -1,   -1,  256,
  257,  258,   36,   -1,   -1,   -1,  256,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,
  257,  258,  257,  258,   -1,   59,   -1,   -1,  257,  258,
   -1,   -1,  257,  258,   68,   -1,   -1,   -1,   -1,  257,
  258,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,   -1,   -1,   99,   -1,  256,  102,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   88,   -1,   -1,  256,   -1,  258,
   -1,   -1,   -1,   -1,   98,   -1,   -1,   -1,   -1,   -1,
  257,  258,  156,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,  264,  265,  257,  258,   -1,
   -1,  257,  258,   -1,   -1,   -1,  180,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,  256,
  257,  258,   -1,  260,  261,  262,  263,  201,  265,  153,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,    9,
  265,  257,  258,   13,   -1,   -1,   16,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   42,   -1,   -1,   -1,   46,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  224,  225,   -1,   -1,   -1,  229,   76,   -1,   -1,   -1,
   -1,   -1,   -1,   83,   84,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  132,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  190,
};
}
final static short YYFINAL=3;
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
"programa : error ID",
"programa : error EOF",
"programa_sin_nombre : cuerpo_programa",
"programa_sin_nombre : cuerpo_programa_recuperacion",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa_recuperacion : '{' conjunto_sentencias lista_llaves_cierre",
"$$2 :",
"cuerpo_programa_recuperacion : lista_llaves_apertura $$2 conjunto_sentencias '}'",
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
"declaracion_variables : UINT error",
"declaracion_variables : UINT variable DASIG constante ';'",
"lista_variables : ID ',' ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"lista_variables : ID ID",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG asignable",
"asignacion_simple : variable error expresion ';'",
"asignacion_simple : variable expresion ';'",
"asignable : factor error",
"asignable : expresion operador_suma termino error",
"asignable : termino operador_multiplicacion factor error",
"asignacion_multiple : inicio_par_variable_constante ';'",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes ';'",
"asignacion_multiple : inicio_par_variable_constante error",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes error",
"inicio_par_variable_constante : variable par_variable_constante constante",
"par_variable_constante : ',' variable par_variable_constante constante ','",
"par_variable_constante : '='",
"par_variable_constante : ',' variable par_variable_constante constante",
"par_variable_constante : ',' variable secuencia_variable_sin_coma constante ','",
"secuencia_variable_sin_coma : variable secuencia_variable_sin_coma constante ','",
"secuencia_variable_sin_coma : variable par_variable_constante constante ','",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"expresion : termino",
"expresion : expresion operador_suma termino",
"expresion : expresion operador_suma error",
"expresion : expresion termino_simple",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"termino_simple : termino_simple operador_multiplicacion factor_simple",
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
"do_while : DO cuerpo_do_recuperacion error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_do",
"cuerpo_do_recuperacion : fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_ejecutable condicion",
"fin_cuerpo_do : WHILE condicion",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible",
"declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
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
"impresion : PRINT imprimible_admisible ';'",
"impresion : PRINT imprimible error",
"imprimible : imprimible_admisible",
"imprimible : '(' ')'",
"imprimible : elemento_imprimible",
"imprimible :",
"imprimible_admisible : '(' elemento_imprimible ')'",
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

//#line 848 "gramatica.y"

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
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
 // Se ejecuta cada vez que encuentra un token error.
public void yyerror(String s) {

    // Silenciado.
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
    SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 782 "Parser.java"
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
//#line 70 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 77 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 80 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 6:
//#line 83 "gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 7:
//#line 86 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 11:
//#line 107 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 12:
//#line 110 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 14:
//#line 113 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 15:
//#line 115 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 16:
//#line 117 "gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 23:
//#line 143 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 27:
//#line 160 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 33:
//#line 187 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 201 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 212 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 228 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 231 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 236 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 240 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 244 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 248 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 258 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 263 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 270 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 284 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 289 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 292 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 295 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 320 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 322 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 327 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 329 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 70:
//#line 345 "gramatica.y"
{ notifyError("Falta coma en la lista de constantes de la asignación múltiple."); }
break;
case 71:
//#line 347 "gramatica.y"
{ notifyError("Falta coma en la lista de variables de la asignación múltiple."); }
break;
case 75:
//#line 360 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 365 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 78:
//#line 379 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 79:
//#line 384 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 80:
//#line 391 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 81:
//#line 404 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 82:
//#line 406 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 83:
//#line 413 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 85:
//#line 419 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 86:
//#line 431 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 88:
//#line 437 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 89:
//#line 449 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 90:
//#line 451 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 98:
//#line 477 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;

            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0";
            } 

            modificarSymbolTable(yyval.sval,val_peek(0).sval);
        }
break;
case 100:
//#line 494 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 101:
//#line 503 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 102:
//#line 508 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 103:
//#line 511 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 104:
//#line 514 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 105:
//#line 517 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 107:
//#line 529 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 114:
//#line 545 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 115:
//#line 554 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 116:
//#line 559 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 117:
//#line 561 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 118:
//#line 563 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 119:
//#line 565 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 122:
//#line 581 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 123:
//#line 586 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 124:
//#line 588 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 127:
//#line 605 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 128:
//#line 607 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 131:
//#line 626 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 133:
//#line 638 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 136:
//#line 655 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 139:
//#line 667 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 143:
//#line 685 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 144:
//#line 687 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 147:
//#line 699 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 148:
//#line 708 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 149:
//#line 713 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 150:
//#line 715 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 151:
//#line 717 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 152:
//#line 719 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 153:
//#line 728 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 155:
//#line 738 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 156:
//#line 745 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 157:
//#line 750 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 158:
//#line 759 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 159:
//#line 764 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 161:
//#line 775 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 162:
//#line 778 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 163:
//#line 780 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 167:
//#line 803 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 168:
//#line 808 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 169:
//#line 810 "gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 170:
//#line 812 "gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 171:
//#line 814 "gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 173:
//#line 825 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 174:
//#line 828 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1361 "Parser.java"
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
