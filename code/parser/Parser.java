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
    0,    0,    0,   19,    0,   20,    0,    0,   18,   18,
   15,   16,   23,   16,   16,   16,   16,   22,   22,   21,
   21,   17,   17,   17,   24,   24,   26,   26,   29,   29,
   30,   30,   31,   31,   32,   32,   25,   25,   25,   25,
   25,   25,   25,   25,   35,   35,   27,   27,   27,   27,
   27,   27,    8,    8,    8,    8,   33,   33,   33,   33,
   34,   34,   34,   34,   41,   42,   42,   43,   43,   44,
   44,    9,    9,    9,    1,    1,    1,    1,    1,    6,
    6,    2,    2,    2,    2,    4,    4,    4,    7,    7,
    3,    3,    3,    5,    5,    5,   11,   11,   10,   10,
   45,   45,   46,   46,   48,   48,   47,   47,   49,   49,
   49,   49,   49,   49,   49,   39,   39,   39,   39,   39,
   50,   50,   50,   40,   40,   40,   51,   52,   52,   52,
   53,   28,   28,   28,   56,   56,   55,   54,   54,   57,
   57,   57,   59,   59,   58,   58,   58,   60,   60,   60,
   36,   36,   36,   36,   36,   12,   13,   13,   14,   14,
   37,   37,   62,   62,   62,   62,   61,   63,   63,   38,
   38,   38,   38,   38,   66,   66,   66,   65,   64,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    0,    4,    2,    1,    1,
    3,    3,    0,    4,    2,    0,    3,    2,    2,    2,
    2,    1,    2,    2,    1,    1,    1,    2,    0,    1,
    1,    1,    3,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    2,    1,    1,    3,    3,    3,    3,
    2,    5,    3,    3,    2,    2,    4,    4,    3,    2,
    2,    4,    2,    4,    3,    3,    1,    2,    1,    2,
    1,    1,    3,    2,    1,    3,    3,    2,    2,    1,
    1,    3,    1,    3,    2,    3,    1,    3,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    3,    2,    1,    0,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    2,    5,
    0,    2,    1,    3,    3,    2,    2,    1,    1,    2,
    2,    6,    5,    7,    1,    2,    3,    1,    0,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    5,    5,    4,    3,    2,    4,    1,    3,    3,    1,
    3,    3,    1,    2,    1,    0,    3,    1,    1,    4,
    4,    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   22,   25,   26,   27,    0,   38,   39,   40,   41,
   42,   43,   45,   46,    0,    0,    9,   10,    5,    0,
   24,    0,    0,   97,  168,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,    0,
  119,    0,    0,    0,   51,    0,    0,    0,    0,  126,
    0,    0,   31,    0,   32,    0,    0,    0,  155,    0,
    0,    0,   18,   15,    0,    0,   60,    0,    0,   67,
    0,    0,    0,    0,   44,   37,   23,   19,    0,   30,
   28,   63,   61,    0,    0,   35,    0,    0,    0,    7,
  100,    0,    0,  157,    0,   98,    0,    0,   95,   80,
   81,    0,   87,    0,   94,   96,    0,   85,   91,  161,
  162,  102,    0,    0,    0,    0,    0,   49,   56,   48,
    0,    0,  150,  149,    0,    0,    0,  140,  142,    0,
   50,   55,   47,    0,    0,  131,   34,    0,    0,  127,
  124,  125,    0,    0,  154,    0,    0,    0,    0,    0,
    0,   68,   59,   65,   69,    0,    0,    0,   72,    0,
    0,    0,    0,    0,    0,   36,    0,    0,    0,    0,
    0,  156,  167,    0,   77,    0,   84,   82,  122,    0,
    0,  111,  113,  112,  114,  115,  109,  110,    0,  106,
  105,  101,   53,    0,  144,    0,    0,    0,  147,   54,
    0,   33,  153,    0,   20,   21,  179,   58,   57,    0,
   66,   14,   64,   62,    0,   74,    0,    0,    0,  170,
  171,    0,  173,   11,  159,  158,   88,   86,    0,  118,
  120,    0,    0,    0,  135,  133,  141,  145,   52,  152,
  151,   70,   73,  172,  178,  174,  117,  116,    0,  132,
  136,    0,  134,  137,
};
final static short yydgoto[] = {                          3,
   51,   52,   53,  122,  123,  124,   54,   68,  178,   16,
   56,   17,  113,  114,   18,   19,  272,   39,    4,    6,
  168,   21,   99,   22,   23,   24,   25,   26,  101,   74,
   75,  108,   27,   28,   29,   30,   31,   32,   33,   34,
   35,   93,   94,  231,   63,   64,  137,  212,  209,  135,
   76,   77,   78,  146,  255,  256,  147,  148,  149,  150,
   58,   59,   60,   36,  187,  183,
};
final static short yysindex[] = {                       -84,
   43,   31,    0,  -92,    0, -224,  -70,   -8,  447,    9,
   24,   51,  491,  -23, -207,  427,  -45,    0,    0,  164,
  -53,    0,    0,    0,    0,   25,    0,    0,    0,    0,
    0,    0,    0,    0,   55,  176,    0,    0,    0,  -37,
    0, -108,  -34,    0,    0,   94, -103,    0,    0,  502,
  183,  -21,    0,  318,  -95,    0,    0,  106,  -80,    0,
    0,  143,  -12,  -34,    0,   33,  -14,   83,  -74,    0,
  151,   28,    0,  -36,    0,  133,  -60,    0,    0,  -18,
  107,   74,    0,    0,   88,  -57,    0,  -34,  -55,    0,
  316,  -95,   89,  116,    0,    0,    0,    0,  100,    0,
    0,    0,    0,   89,   28,    0,  165,  187,  100,    0,
    0,   70,  102,    0,  -21,    0,    0,  166,    0,    0,
    0,  -21,    0,  390,    0,    0,  308,    0,    0,    0,
    0,    0,  190,  -62,  -59,  356,    5,    0,    0,    0,
  -48,  -14,    0,    0, -205,  179,  173,    0,    0, -147,
    0,    0,    0,  -35,   89,    0,    0,  201,    0,    0,
    0,    0,  162,   35,    0,    0,  125,  147,  281,   58,
  283,    0,    0,    0,    0,   89,  111,  138,    0,  219,
   52,  278,   85,  165,  -39,    0,    0,   93,  123,  114,
  -34,    0,    0,  324,    0,  -21,    0,    0,    0,   22,
  317,    0,    0,    0,    0,    0,    0,    0,  -34,    0,
    0,    0,    0,  338,    0,  259, -205,  -55,    0,    0,
  333,    0,    0,  -41,    0,    0,    0,    0,    0,  362,
    0,    0,    0,    0,   89,    0,  156,    0,  373,    0,
    0,  168,    0,    0,    0,    0,    0,    0,    8,    0,
    0,  183,  299,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   76,    0,
    0,  141,    0,    0,
};
final static short yyrindex[] = {                        15,
  169,  428,    0,  428,    0,    0,    0,  436,  189,  508,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  429,
  153,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,  343,    0,    0,    0,    0,    0,  193,    0,  195,
    0,  514,  170,    0,    0,  182,   13,    0,    0,    0,
  508,    0,    0,  508,    0,  199,    0,  -95,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  134,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  204,  204,    0,    0,
    0,  104,    0,    0,  458,    0,  209,    0,    0,    0,
    0,  468,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,   50,    0,   21,    0,    0,    0,    0,
    0,   13,    0,    0, -137,    0,  425,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  226,    0,
    0,    0,    0,    0,    0,   12,  484,  500,    0,    0,
  -25,    0,    0,    0,    0,    0,    0,    0,    0,  204,
    0,  249,    0,  204,    0,    0,  249,    0,    0,    0,
    0,    0,    0,    0,    0,  480,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    6,  118,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  142,
    0,    0,    0,    0,    0,    0,    0,  250,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   48,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  572,   23,  450,    0,    0,    0,  -10,    0,    0,   -6,
  359,   44,    0,  319,   16,  503,   20,    0,    0,    0,
    0,    0,    0,  -20,  645,    0,    0,    0,    0,   -2,
  472,  -33,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  421,    0,    0,   96,    0,    0,    0,    0,  382,
    0,    0,  448,  384,  279,    0,    0, -110,    0,    0,
    0,    0,  496,    0,  441,   14,
};
final static int YYTABLESIZE=825;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   29,  238,   55,   62,   69,   47,   55,   49,   46,   92,
   47,   17,   48,   96,    4,   99,   15,  261,   99,   37,
   49,   20,  163,   49,   46,   48,   47,   15,   48,  145,
   14,   43,   40,   85,  215,   99,   55,   42,  158,   55,
   29,  127,    5,   55,  125,  211,  143,   55,   62,  143,
  143,   24,   57,  139,   41,  110,   57,   55,  169,   57,
  134,  108,   86,   67,   97,  144,  268,   15,  115,   98,
   15,  180,  142,   55,  125,  224,  141,  120,   42,  121,
  250,   55,  172,  100,  125,  109,   57,  175,  107,   57,
   15,  140,  238,   57,  126,  123,   47,   57,  104,   83,
  120,   84,  121,   15,  127,  125,  257,   57,  121,  171,
   72,  194,  120,  103,  121,   15,  229,   55,  177,  148,
   55,  188,  218,   57,  126,   29,  154,   15,  189,  125,
  199,   57,  148,   47,  126,   49,   24,    4,   47,   15,
   48,  153,  192,  219,  160,  191,  196,  160,  111,  120,
   15,  121,  157,   14,  116,  126,   97,  125,  146,   89,
  129,  146,   15,  125,  130,  165,  156,   57,   97,  159,
   57,    1,    2,   72,   55,  131,   90,   69,   55,  126,
   15,  235,   47,  132,   55,  127,   71,   55,  271,  155,
   62,  161,   13,  237,   69,  162,  234,  242,  166,  169,
  273,  171,   55,   15,  181,  133,  193,  126,  213,  201,
   95,  258,  167,  126,  260,   15,  217,    8,   44,  216,
  223,  220,    8,   44,   57,  120,  185,  121,   57,   15,
   86,   99,   82,    8,   57,  232,   71,   57,    8,   44,
   15,  143,    9,   10,    8,  125,   11,  244,   12,  225,
   13,   97,   57,    9,   10,  133,  144,   29,  185,   12,
  210,   13,  148,  267,   61,  274,   29,   29,   24,  148,
   29,  226,   29,  169,   29,  148,  108,   24,   24,   65,
   66,   24,  148,   24,    8,   24,    7,    8,  138,  139,
  249,    8,  119,    9,   10,  126,    9,   10,  105,   12,
   11,   13,   12,  107,   13,  123,   70,    8,    8,   44,
  102,  184,   72,  228,    8,  119,    9,   10,  121,    7,
    8,  227,   12,   71,   13,  222,    8,  119,   42,    9,
   10,    7,    8,   11,  190,   12,  240,   13,  151,  152,
  241,    9,   10,  222,    8,   11,   44,   12,  243,   13,
    8,   44,   47,    9,   10,    7,    8,   11,  120,   12,
  121,   13,   47,    8,  119,    9,   10,    8,   47,   11,
  245,   12,  171,   13,  173,  251,    9,   10,  253,    8,
   11,  254,   12,   75,   13,   75,   75,   75,    9,   10,
   69,  259,   11,  233,   12,   44,   13,    8,  120,   71,
  121,   75,   75,   75,   75,  262,    9,   10,   13,   13,
   11,  264,   12,  265,   13,  208,  206,  207,   13,   13,
    8,  269,   13,  266,   13,    6,   13,   16,    3,    9,
   10,   49,    8,   11,   47,   12,   48,   13,  121,    8,
  119,    9,   10,    8,  166,   99,    8,   12,  163,   13,
  165,  174,    9,   10,  128,    9,   10,    8,   12,  177,
   13,   12,  179,   13,  164,  138,    9,   10,   49,   46,
   89,   47,   12,   48,   13,    8,   99,   99,   99,   99,
   99,  130,   99,   11,    9,   10,   50,   90,   49,   46,
   12,   47,   13,   48,   99,   99,   99,   99,   79,   12,
   79,   79,   79,  128,  175,  176,   38,  107,   78,  246,
   78,   78,   78,  221,  176,  200,   79,   79,   79,   79,
   76,  160,   76,   76,   76,  214,   78,   78,   78,   78,
   80,  270,   49,   46,  230,   47,  236,   48,   76,   76,
   76,   76,  117,   49,   46,  118,   47,  182,   48,  104,
  104,    0,  104,    0,  104,  103,  103,    0,  103,    0,
  103,    0,    0,  197,    8,   44,    0,    0,    0,    0,
    0,    0,    8,  119,    8,   44,  198,    0,    0,  247,
    8,   44,    0,    0,   81,    0,    0,   91,    0,    0,
    0,    0,    0,  263,    0,    0,    0,    0,   75,   75,
   75,    0,   75,   75,   75,   75,    0,   75,    0,    0,
    0,    0,    8,  119,  112,  202,  203,  204,  205,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  239,    0,    0,    0,  239,  136,    0,    0,    0,    0,
    0,    0,    0,  248,    0,  195,    8,   44,    0,    0,
    0,  164,    0,    0,    0,    0,   73,    0,    0,  170,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  106,    0,   87,    8,   44,    0,    0,    0,    0,    0,
   88,   99,   99,   99,    0,   99,   99,   99,   99,   99,
   99,    0,    0,    8,   44,   45,    0,   73,    0,    0,
    0,    0,    0,   79,   79,   79,  106,   79,   79,   79,
   79,    0,   79,   78,   78,   78,    0,   78,   78,   78,
   78,    0,   78,    0,    0,   76,   76,   76,    0,   76,
   76,   76,   76,    0,   76,    0,   79,    8,   44,  106,
    0,    0,  186,    0,    0,    0,    0,    0,    8,   44,
   45,    0,  112,    0,  104,  104,    0,    0,    0,    0,
  103,  103,    0,    0,    0,    0,    0,   73,    0,    0,
  252,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  186,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  186,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   41,    9,   40,   11,   45,   13,   42,   43,   16,
   45,    0,   47,   59,    0,   41,   40,   59,   44,    4,
   42,    2,   41,   42,   43,   47,   45,   40,   47,   44,
  123,   40,  257,   14,  145,   61,   43,   46,   72,   46,
   40,   52,    0,   50,   51,   41,   41,   54,   40,   44,
  256,   40,    9,   41,  125,   40,   13,   64,   41,   16,
   63,   41,  270,   40,   85,  271,   59,   40,   46,  123,
   40,  105,   40,   80,   81,   41,   44,   43,   46,   45,
   59,   88,   89,   59,   91,  123,   43,   94,   41,   46,
   40,   59,   41,   50,   51,   59,   45,   54,   44,  123,
   43,  125,   45,   40,  115,  112,  217,   64,   59,  257,
  123,  122,   43,   59,   45,   40,   59,  124,   99,  257,
  127,  108,  270,   80,   81,  125,   44,   40,  109,  136,
  133,   88,  270,   45,   91,   42,  125,  123,   45,   40,
   47,   59,   41,  150,   41,   44,  124,   44,  257,   43,
   40,   45,  125,  123,  258,  112,  177,  164,   41,   44,
  256,   44,   40,  170,   59,   59,   71,  124,  189,   74,
  127,  256,  257,  123,  181,  256,   61,   44,  185,  136,
   40,   44,   45,   41,  191,  196,   45,  194,  125,  264,
   40,   59,   40,  180,   61,  256,   59,  184,  125,  257,
  125,  257,  209,   40,   40,  268,   41,  164,  257,  269,
  256,  218,  125,  170,  256,   40,   44,  257,  258,   41,
   59,  257,  257,  258,  181,   43,   40,   45,  185,   40,
  270,  257,  256,  257,  191,  125,  273,  194,  257,  258,
   40,  256,  266,  267,  257,  252,  270,  125,  272,  125,
  274,  272,  209,  266,  267,  268,  271,  257,   40,  272,
  256,  274,  257,  256,  256,  125,  266,  267,  257,  257,
  270,  125,  272,  256,  274,  270,  256,  266,  267,  256,
  257,  270,  270,  272,  257,  274,  256,  257,  256,  257,
  269,  257,  258,  266,  267,  252,  266,  267,  123,  272,
  270,  274,  272,  256,  274,  269,  256,  257,  257,  258,
  256,  125,  123,  256,  257,  258,  266,  267,  269,  256,
  257,   41,  272,  273,  274,  125,  257,  258,   46,  266,
  267,  256,  257,  270,  265,  272,   59,  274,  256,  257,
  256,  266,  267,  125,  257,  270,  258,  272,  256,  274,
  257,  258,   45,  266,  267,  256,  257,  270,   43,  272,
   45,  274,   45,  257,  258,  266,  267,  257,   45,  270,
  257,  272,  257,  274,   59,   59,  266,  267,   41,  257,
  270,  123,  272,   41,  274,   43,   44,   45,  266,  267,
  257,   59,  270,  256,  272,  258,  274,  257,   43,  258,
   45,   59,   60,   61,   62,   44,  266,  267,  256,  257,
  270,  256,  272,   41,  274,   60,   61,   62,  266,  267,
  257,  123,  270,  256,  272,  257,  274,    0,    0,  266,
  267,   42,  257,  270,   45,  272,   47,  274,  269,  257,
  258,  266,  267,  257,  256,  264,  257,  272,  256,  274,
  256,   93,  266,  267,  256,  266,  267,  257,  272,  256,
  274,  272,  104,  274,  256,   41,  266,  267,   42,   43,
   44,   45,  272,   47,  274,  257,   41,   42,   43,   44,
   45,  256,   47,    0,  266,  267,   40,   61,   42,   43,
  272,   45,  274,   47,   59,   60,   61,   62,   41,    0,
   43,   44,   45,   54,  256,  256,    4,   36,   41,  191,
   43,   44,   45,  155,   94,  134,   59,   60,   61,   62,
   41,   74,   43,   44,   45,  142,   59,   60,   61,   62,
   40,  253,   42,   43,  176,   45,  178,   47,   59,   60,
   61,   62,   41,   42,   43,   50,   45,  107,   47,   42,
   43,   -1,   45,   -1,   47,   42,   43,   -1,   45,   -1,
   47,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  257,  258,  127,   -1,   -1,  256,
  257,  258,   -1,   -1,   13,   -1,   -1,   16,   -1,   -1,
   -1,   -1,   -1,  235,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,
   -1,   -1,  257,  258,   43,  260,  261,  262,  263,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  181,   -1,   -1,   -1,  185,   64,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  194,   -1,  256,  257,  258,   -1,   -1,
   -1,   80,   -1,   -1,   -1,   -1,   12,   -1,   -1,   88,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   36,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,
  264,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,   -1,   -1,  257,  258,  259,   -1,   63,   -1,   -1,
   -1,   -1,   -1,  256,  257,  258,   72,  260,  261,  262,
  263,   -1,  265,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,   -1,  265,   -1,  256,  257,  258,  105,
   -1,   -1,  108,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,   -1,  191,   -1,  257,  258,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,  133,   -1,   -1,
  209,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  158,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  180,
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
"$$2 :",
"programa : error $$2 ID cuerpo_programa",
"programa : error EOF",
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
"declaracion_variables : UINT error",
"declaracion_variables : UINT variable DASIG constante ';'",
"lista_variables : ID ',' ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"lista_variables : ID ID",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG expresion error",
"asignacion_simple : variable expresion ';'",
"asignacion_simple : variable error",
"asignacion_multiple : inicio_asignacion_par ';'",
"asignacion_multiple : inicio_asignacion_par ',' lista_constantes ';'",
"asignacion_multiple : inicio_asignacion_par error",
"asignacion_multiple : inicio_asignacion_par ',' lista_constantes error",
"inicio_asignacion_par : variable asignacion_par constante",
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
"do_while : DO cuerpo_do_recuperacion error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_do",
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

//#line 852 "gramatica.y"

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
    Printer.printWrapped(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printWrapped(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printWrapped(String.format(
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
    if (lexemaNuevo != null) {
        SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
    }
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 769 "Parser.java"
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
//#line 82 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron, previo al nombre del programa, sentencias."); }
break;
case 8:
//#line 85 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 12:
//#line 106 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 13:
//#line 109 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 15:
//#line 112 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 16:
//#line 114 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 17:
//#line 116 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
break;
case 24:
//#line 142 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 28:
//#line 159 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 34:
//#line 184 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 202 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 44:
//#line 213 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 47:
//#line 229 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 48:
//#line 232 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 49:
//#line 237 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 50:
//#line 241 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 51:
//#line 245 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 249 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 259 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 264 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 271 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 285 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 291 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 294 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 297 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 61:
//#line 307 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 62:
//#line 309 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 63:
//#line 314 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 64:
//#line 316 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 69:
//#line 337 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 71:
//#line 345 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 353 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 358 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 372 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 377 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de %s %s.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 78:
//#line 381 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 388 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 80:
//#line 398 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 81:
//#line 400 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 82:
//#line 407 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 84:
//#line 413 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 85:
//#line 420 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 86:
//#line 427 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 88:
//#line 433 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 89:
//#line 440 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 90:
//#line 442 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 98:
//#line 469 "gramatica.y"
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
//#line 488 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 101:
//#line 497 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 102:
//#line 502 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 104:
//#line 508 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 513 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 108:
//#line 524 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 115:
//#line 540 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 116:
//#line 549 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 117:
//#line 554 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 118:
//#line 556 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 119:
//#line 558 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 120:
//#line 560 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 123:
//#line 572 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 124:
//#line 581 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 125:
//#line 586 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 588 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 605 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 607 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 132:
//#line 622 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 133:
//#line 627 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 134:
//#line 630 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 136:
//#line 642 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 139:
//#line 659 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 142:
//#line 671 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 146:
//#line 689 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 147:
//#line 691 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 150:
//#line 703 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 151:
//#line 712 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 152:
//#line 717 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 153:
//#line 719 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 154:
//#line 721 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 155:
//#line 723 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 156:
//#line 732 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 158:
//#line 742 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 159:
//#line 749 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 160:
//#line 754 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 161:
//#line 763 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 162:
//#line 768 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 164:
//#line 779 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 165:
//#line 782 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 166:
//#line 784 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 170:
//#line 807 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 171:
//#line 812 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 172:
//#line 814 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 173:
//#line 816 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 174:
//#line 818 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 176:
//#line 829 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 177:
//#line 832 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1358 "Parser.java"
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
