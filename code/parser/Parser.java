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
   40,   40,   33,   33,   33,   33,   41,   42,   42,   44,
   44,   43,   43,    9,    9,    9,    1,    1,    1,    1,
    1,    6,    6,    2,    2,    2,    2,    4,    4,    4,
    7,    7,    3,    3,    3,    5,    5,    5,   11,   11,
   10,   10,   45,   45,   45,   45,   45,   46,   46,   47,
   47,   47,   47,   47,   47,   47,   38,   38,   38,   38,
   38,   48,   48,   39,   39,   39,   49,   50,   50,   50,
   51,   27,   27,   54,   54,   53,   52,   52,   55,   55,
   55,   57,   57,   56,   56,   56,   58,   58,   58,   35,
   35,   35,   35,   35,   12,   13,   13,   14,   14,   36,
   36,   60,   60,   60,   60,   59,   61,   61,   37,   37,
   37,   37,   37,   64,   64,   64,   63,   62,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    3,    4,    3,    2,
    4,    4,    2,    4,    2,    4,    3,    3,    1,    2,
    1,    2,    1,    1,    3,    2,    1,    3,    3,    2,
    2,    1,    1,    3,    1,    3,    2,    3,    1,    3,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    2,
    1,    3,    3,    2,    2,    2,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    6,    6,    5,    5,
    2,    0,    2,    3,    3,    2,    2,    1,    1,    2,
    2,    6,    5,    1,    2,    3,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    5,
    5,    4,    3,    2,    4,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    0,    3,    1,    1,    4,    4,
    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   37,   38,   39,   40,
   41,   42,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   99,  167,    0,    0,   91,   92,    0,    0,
    0,   85,    0,    0,   94,   95,    0,    0,    0,  121,
    0,    0,    0,    0,   50,    0,    0,    0,    0,  126,
    0,    0,   30,    0,   31,    0,    0,    0,  154,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,   69,
    0,    0,    0,    0,   43,   36,   22,   18,    0,   29,
   27,   65,   63,    0,    0,   34,    0,    0,  102,    0,
    0,  156,    0,  100,    0,    0,   97,   82,   83,    0,
   89,    0,   96,   98,    0,   87,   93,  160,  161,  105,
    0,  112,  114,  113,  115,  116,  110,  111,    0,    0,
  106,  104,   48,   55,   47,    0,    0,  149,  148,    0,
    0,    0,  139,  141,    0,   49,   54,   46,    0,    0,
  131,   33,    0,    0,  127,  124,  125,    0,    0,  153,
    0,    0,    0,    0,    0,    0,    0,    0,   57,    0,
   72,   59,   67,   73,    0,    0,    0,   74,    0,    0,
    0,    0,    0,    0,   35,    0,    0,    0,    0,  155,
  166,    0,   79,    0,   86,   84,  107,  103,    0,    0,
    0,   52,    0,  143,    0,    0,    0,  146,   53,    0,
   32,  152,    0,   19,   20,  178,   58,   56,    0,    0,
   60,    0,   68,   13,   66,   64,    0,   76,    0,    0,
    0,  169,  170,    0,  172,  158,  157,   90,   88,  123,
  120,    0,  119,    0,    0,  134,  133,  140,  144,   51,
  151,  150,    0,    0,   70,   75,  171,  177,  173,  118,
  117,    0,  132,  135,    0,   61,   62,  136,
};
final static short yydgoto[] = {                          3,
   62,   51,   52,  120,  121,  122,   53,   68,  187,   54,
   55,   56,  111,  112,   18,   19,  275,   39,    4,  173,
   21,   99,   22,   23,   24,   25,   26,  101,   74,   75,
  108,   27,   28,   29,   30,   31,   32,   33,   34,  179,
   35,   93,   94,  233,   63,   64,  139,  211,   76,   77,
   78,  151,  256,  257,  152,  153,  154,  155,   57,   58,
   59,   36,  196,  192,
};
final static short yysindex[] = {                      -189,
    2,   39,    0, -100,    0,    0,  -80,    7,  486,  498,
   88,   69,  504,  -35, -221,  401,  -55,    0,    0,  153,
  -39,    0,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,  -11,  164,    0,    0,    0,    0,
 -163,  524,    0,    0,  370, -162,    0,    0,  392,  171,
   44,    0,  150, -148,    0,    0,   53, -142,    0,    0,
  511,  320,  175,  -27,    0,   -4,  -28,  117, -145,    0,
  -34,   55,    0,   -8,    0,   65, -131,    0,    0,  518,
  101,    4,    0,    0,   82, -126,  524,  524, -125,    0,
  144, -148,  -38,   17,    0,    0,    0,    0,   94,    0,
    0,    0,    0,  -38,   55,    0,   98,  194,    0,   42,
   31,    0,   44,    0,    0,  106,    0,    0,    0,   44,
    0,  352,    0,    0,  339,    0,    0,    0,    0,    0,
   10,    0,    0,    0,    0,    0,    0,    0,  524, -129,
    0,    0,    0,    0,    0, -101,  -28,    0,    0, -225,
  124,  114,    0,    0, -207,    0,    0,    0,  -87,  -38,
    0,    0,  208,    0,    0,    0,    0,  113,  112,    0,
    0,   48,   49,  134,  381,  527,   44,  -75,    0,  136,
    0,    0,    0,    0,  -38,  105,   76,    0,  221,   36,
  126,  -70,   98,  -16,    0,    0,  -68,  -61,  524,    0,
    0,  -89,    0,   44,    0,    0,    0,    0,  171,  175,
   84,    0,  157,    0,   79, -225, -125,    0,    0,  140,
    0,    0,  -44,    0,    0,    0,    0,    0,  352,  339,
    0,  162,    0,    0,    0,    0,  -38,    0,  -48,    0,
  170,    0,    0,  -30,    0,    0,    0,    0,    0,    0,
    0,   45,    0,  167,  -10,    0,    0,    0,    0,    0,
    0,    0,   64,   -5,    0,    0,    0,    0,    0,    0,
    0,   94,    0,    0,  119,    0,    0,    0,
};
final static short yyrindex[] = {                        18,
    0,  292,    0,  292,    0,    0,    0,  411,   46,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  308,
  139,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   21,
  440,    0,    0,    0,    0,    0,   54,    0,   62,    0,
    0,   25,    0,    0,    0,   60,   19,    0,    0,    0,
    0,    0,    0,    0,    0,   72,    0, -142,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   75,   75,    0,   92,
    0,    0,  453,    0,   91,    0,    0,    0,    0,  463,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  355,
    0,    0,    0,    0,    0,    0,   19,    0,    0, -188,
    0,  314,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  111,    0,    0,    0,    0,    0,    0,
   12,  357,  398,    0,    0,    0,  530,   58,    0,   13,
    0,    0,    0,    0,    0,    0,    0,    0,   75,    0,
  160,    0,   75,    0,    0,  160,    0,    0,    0,    0,
    0,    0,    0,  475,    0,    0,    0,    0,   32,    0,
    0,    0,    0,    0,    0,   15,  108,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -18,    0,    0,    0,    0,    0,    0,    0,  166,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  542,  345,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  754,  -24,  423,    0,  216,  274,  479,    0,    0,    8,
  521,  128,    0,  258,  460,  465,   24,    0,    0,    0,
    0,    0,  -20,  717,    0,    0,    0,    0,  -60,  431,
  -29,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  383,    0,    0,   80,  418,    0,    0,    0,    0,
  412,  342,  236,    0,    0, -133,    0,    0,    0,    0,
  442,    0,  385,  270,
};
final static int YYTABLESIZE=953;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   28,    6,  140,   96,   15,   61,   46,   48,   45,   16,
   46,   16,   47,  142,  262,  150,  214,    4,   69,   16,
  113,   16,   14,   92,  240,   20,   71,   16,   46,   15,
  148,   61,  104,   48,   45,  147,   46,   85,   47,  146,
   28,   41,  163,   16,   40,  149,   42,  103,   86,  180,
  208,   23,   41,  101,  145,  142,  101,  123,  142,  138,
   89,  168,  217,  177,   97,  109,    1,    2,  147,  123,
   16,  200,  108,  101,  199,  189,  240,   90,   15,   16,
   46,  147,  258,   98,  118,   48,  119,   83,  123,   84,
   47,  100,   16,  109,   15,  114,  181,  204,  123,   85,
   85,  184,   85,  271,   85,   48,   16,  127,   15,   73,
   47,  128,   16,  129,  274,   16,   85,  123,  160,  237,
   46,   15,  186,  166,  167,   28,   73,   67,  171,   17,
  174,  180,  159,   15,  236,  159,   23,  190,  210,   17,
    4,   17,  253,  118,   15,  119,  201,   17,  145,  250,
  161,  145,  223,  164,  118,  212,  119,  216,   15,  170,
  159,   14,  218,   17,  215,   97,  248,    8,  117,  219,
   16,  222,  224,  225,  226,  158,  123,  124,   12,  162,
  231,   41,  123,  123,  242,  243,  118,  245,  119,  124,
   17,   72,   15,   16,   46,  246,   16,  254,  260,   17,
   95,  255,  182,   15,  263,  265,  172,  267,  124,  123,
  268,  261,   17,  118,   15,  119,  123,   16,  124,   43,
   82,    8,    8,   43,  259,  269,   17,  148,  141,  234,
    9,   10,   17,  194,   11,   17,   12,  124,   13,   71,
    8,   43,  149,  278,  102,    7,    8,   15,    8,   43,
  277,  143,  144,   86,   97,    9,   10,   28,    5,   11,
  194,   12,   16,   13,   71,  207,   28,   28,   23,  101,
   28,  147,   28,  180,   28,  147,  168,   23,   23,   16,
  109,   23,   16,   23,  147,   23,  105,  108,  147,  272,
   17,   15,    8,   43,    7,    8,  124,   72,    8,  117,
  270,  165,  124,  124,    9,   10,  198,    3,   11,  162,
   12,    8,   13,   17,   85,   85,   17,  164,  193,  276,
    9,   10,   73,  101,   70,    8,   12,  128,   13,  124,
  176,  235,  221,   43,    9,   10,  124,   17,    8,  251,
   12,   71,   13,   65,   66,  221,  163,    9,   10,    7,
    8,   11,  252,   12,  137,   13,   10,    8,  117,    9,
   10,    8,  118,   11,  119,   12,  130,   13,    8,  117,
    9,   10,  156,  157,   11,    8,   12,  197,   13,  138,
  136,  137,   17,   46,    9,   10,   84,   84,   11,   84,
   12,   84,   13,   48,   12,   12,   46,   11,   47,   17,
    8,  117,   17,   84,   12,   12,    8,   43,   12,    8,
   12,   48,   12,  122,   46,  174,   47,  249,    9,   10,
    8,  175,   11,  118,   12,  119,   13,    8,  117,    9,
   10,    8,  115,   48,   45,   12,   46,   13,   47,  227,
    9,   10,   48,   45,   89,   46,   12,   47,   13,  229,
    8,  101,  101,  101,  101,  101,  247,  101,  239,    9,
   10,   90,  244,   37,    8,   12,  107,   13,   38,  101,
  101,  101,  101,    9,   10,  126,  185,    8,  131,   12,
   77,   13,   77,   77,   77,  165,    9,   10,  213,  273,
  116,  191,   12,   80,   13,   80,   80,   80,   77,   77,
   77,   77,    0,   81,    0,   81,   81,   81,    0,    0,
  178,   80,   80,   80,   80,   78,    0,   78,   78,   78,
    0,   81,   81,   81,   81,   49,    0,   48,   45,  125,
   46,    0,   47,   78,   78,   78,   78,   61,    0,   48,
   45,    0,   46,   80,   47,   48,   45,  206,   46,    0,
   47,  130,   48,   45,    0,   46,    0,   47,  168,   48,
   45,    0,   46,    0,   47,   48,   45,    0,   46,  118,
   47,  119,   77,    0,   77,    0,    8,  117,    0,  132,
  133,  134,  135,    0,   78,  228,   78,    0,   77,    0,
    0,  125,    0,    0,  205,    8,   43,    0,  202,    0,
   78,   84,   84,    0,    0,    0,    0,  203,    8,   43,
  122,    0,  241,  183,    0,    0,  241,    0,    0,    0,
    0,    0,    0,  122,  188,    0,    8,   43,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,  117,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    8,   43,
   44,    0,  264,    0,    0,  230,   87,    8,   43,    0,
    0,    0,    0,    0,   88,    0,  101,  101,  101,    0,
  101,  101,  101,  101,  101,  101,    0,    0,    0,    0,
  220,    0,  125,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   77,   77,   77,    0,   77,
   77,   77,   77,    0,   77,  232,    0,  238,   80,   80,
   80,    0,   80,   80,   80,   80,    0,   80,   81,   81,
   81,    0,   81,   81,   81,   81,    0,   81,   73,    0,
   78,   78,   78,    0,   78,   78,   78,   78,    0,   78,
    0,  125,    8,   43,   44,    0,    0,    0,    0,    0,
    0,    0,  106,   60,    8,   43,    0,  266,    0,   79,
    8,   43,   50,    0,    0,    0,   81,    8,   43,   91,
    0,    0,    0,    0,    8,   43,    0,    0,    0,   73,
    8,   43,    0,    8,  117,    0,   77,   77,  106,    0,
    0,    0,    0,    0,    0,  110,    0,    0,   78,   78,
    0,    0,   50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  106,    0,    0,  195,    0,    0,    0,    0,    0,
    0,    0,    0,  169,    0,    0,    0,    0,    0,    0,
  175,  176,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  195,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  209,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  195,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,    0,   63,   59,   40,   40,   45,   42,   43,    2,
   45,    0,   47,   41,   59,   44,  150,    0,   11,   12,
   45,   14,  123,   16,   41,    2,   45,   20,   45,   40,
  256,   40,   44,   42,   43,   40,   45,   14,   47,   44,
   40,   46,   72,   36,  125,  271,   40,   59,  270,  257,
   41,   40,   46,   41,   59,   41,   44,   50,   44,   41,
   44,   41,  270,   88,   85,   41,  256,  257,  257,   62,
   63,   41,   41,   61,   44,  105,   41,   61,   40,   72,
   45,  270,  216,  123,   43,   42,   45,  123,   81,  125,
   47,   59,   85,  257,   40,  258,   89,  122,   91,   42,
   43,   94,   45,   59,   47,   42,   99,  256,   40,   44,
   47,   59,  105,  256,  125,  108,   59,  110,  264,   44,
   45,   40,   99,   59,  256,  125,   61,   40,  125,    2,
  257,  257,   41,   40,   59,   44,  125,   40,  268,   12,
  123,   14,   59,   43,   40,   45,   41,   20,   41,  210,
   71,   44,   41,   74,   43,  257,   45,   44,   40,   59,
   44,  123,  155,   36,   41,  186,  256,  257,  258,  257,
  163,   59,  125,  125,   41,   59,  169,   50,   40,  125,
  256,   46,  175,  176,   59,  256,   43,  256,   45,   62,
   63,  123,   40,  186,   45,  257,  189,   41,   59,   72,
  256,  123,   59,   40,  229,   44,  125,  256,   81,  202,
   41,  256,   85,   43,   40,   45,  209,  210,   91,  258,
  256,  257,  257,  258,  217,  256,   99,  256,  256,  125,
  266,  267,  105,   40,  270,  108,  272,  110,  274,  258,
  257,  258,  271,  125,  256,  256,  257,   40,  257,  258,
  256,  256,  257,  270,  275,  266,  267,  257,  257,  270,
   40,  272,  255,  274,  273,  256,  266,  267,  257,  257,
  270,  257,  272,  257,  274,  257,  256,  266,  267,  272,
  256,  270,  275,  272,  270,  274,  123,  256,  270,  123,
  163,    0,  257,  258,  256,  257,  169,  123,  257,  258,
  256,  256,  175,  176,  266,  267,  265,    0,  270,  256,
  272,  257,  274,  186,  257,  258,  189,  256,  125,  256,
  266,  267,  257,  264,  256,  257,  272,  256,  274,  202,
  256,  256,  125,  258,  266,  267,  209,  210,  257,  256,
  272,  273,  274,  256,  257,  125,  256,  266,  267,  256,
  257,  270,  269,  272,   41,  274,    0,  257,  258,  266,
  267,  257,   43,  270,   45,  272,  256,  274,  257,  258,
  266,  267,  256,  257,  270,  257,  272,  108,  274,   60,
   61,   62,  255,   45,  266,  267,   42,   43,  270,   45,
  272,   47,  274,   42,  256,  257,   45,    0,   47,  272,
  257,  258,  275,   59,  266,  267,  257,  258,  270,  257,
  272,   42,  274,   59,   45,  256,   47,  202,  266,  267,
  257,  256,  270,   43,  272,   45,  274,  257,  258,  266,
  267,  257,   41,   42,   43,  272,   45,  274,   47,   59,
  266,  267,   42,   43,   44,   45,  272,   47,  274,  176,
  257,   41,   42,   43,   44,   45,  199,   47,  189,  266,
  267,   61,  193,    4,  257,  272,   36,  274,    4,   59,
   60,   61,   62,  266,  267,   53,   94,  257,   61,  272,
   41,  274,   43,   44,   45,   74,  266,  267,  147,  254,
   49,  107,  272,   41,  274,   43,   44,   45,   59,   60,
   61,   62,   -1,   41,   -1,   43,   44,   45,   -1,   -1,
   88,   59,   60,   61,   62,   41,   -1,   43,   44,   45,
   -1,   59,   60,   61,   62,   40,   -1,   42,   43,   51,
   45,   -1,   47,   59,   60,   61,   62,   40,   -1,   42,
   43,   -1,   45,   40,   47,   42,   43,  125,   45,   -1,
   47,   41,   42,   43,   -1,   45,   -1,   47,   41,   42,
   43,   -1,   45,   -1,   47,   42,   43,   -1,   45,   43,
   47,   45,   43,   -1,   45,   -1,  257,  258,   -1,  260,
  261,  262,  263,   -1,   43,   59,   45,   -1,   59,   -1,
   -1,  113,   -1,   -1,  256,  257,  258,   -1,  120,   -1,
   59,  257,  258,   -1,   -1,   -1,   -1,  256,  257,  258,
  256,   -1,  190,   93,   -1,   -1,  194,   -1,   -1,   -1,
   -1,   -1,   -1,  269,  104,   -1,  257,  258,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,   -1,  230,   -1,   -1,  177,  256,  257,  258,   -1,
   -1,   -1,   -1,   -1,  264,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,   -1,   -1,   -1,   -1,
  160,   -1,  204,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,   -1,  265,  185,   -1,  187,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   12,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
   -1,  263,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   36,  256,  257,  258,   -1,  237,   -1,  256,
  257,  258,    9,   -1,   -1,   -1,   13,  257,  258,   16,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   63,
  257,  258,   -1,  257,  258,   -1,  257,  258,   72,   -1,
   -1,   -1,   -1,   -1,   -1,   42,   -1,   -1,  257,  258,
   -1,   -1,   49,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  105,   -1,   -1,  108,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   80,   -1,   -1,   -1,   -1,   -1,   -1,
   87,   88,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  163,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  139,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  189,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  210,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  199,
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
"par_variable_constante : variable_comada par_variable_constante constante_comada",
"par_variable_constante : '='",
"constante_comada : constante ','",
"constante_comada : constante",
"variable_comada : ',' variable",
"variable_comada : variable",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"expresion : termino",
"expresion : expresion operador_suma termino",
"expresion : expresion operador_suma error",
"expresion : '+' termino",
"expresion : expresion termino_simple",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"termino : operador_multiplicacion factor",
"termino_simple : termino_simple operador_multiplicacion factor_simple",
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

//#line 857 "gramatica.y"

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
//#line 792 "Parser.java"
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
case 71:
//#line 350 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 358 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 75:
//#line 366 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 371 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            /* $$ = $1 + '_' + $2;*/
        }
break;
case 78:
//#line 386 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 79:
//#line 391 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 80:
//#line 398 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval)); }
break;
case 81:
//#line 400 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 82:
//#line 413 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 83:
//#line 415 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 84:
//#line 422 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 86:
//#line 428 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 435 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 88:
//#line 442 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 90:
//#line 448 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de %s %s.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 91:
//#line 455 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 457 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 100:
//#line 484 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;

            notifyDetection(String.format("Constante negativa: %s",yyval.sval));

            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0";
            } 

            modificarSymbolTable(yyval.sval,val_peek(0).sval);
        }
break;
case 102:
//#line 503 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 103:
//#line 512 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 104:
//#line 517 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 105:
//#line 520 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 106:
//#line 523 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 107:
//#line 526 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 109:
//#line 538 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 116:
//#line 554 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 117:
//#line 563 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 118:
//#line 568 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 119:
//#line 570 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 120:
//#line 572 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 121:
//#line 574 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 124:
//#line 590 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 125:
//#line 595 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 597 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 614 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 616 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 635 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 647 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 664 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 676 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 694 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 696 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 708 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 717 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 722 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 724 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 726 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 728 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 737 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 747 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 754 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 759 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 768 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 773 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 784 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 787 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 789 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 812 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 817 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 171:
//#line 819 "gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 172:
//#line 821 "gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 173:
//#line 823 "gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 175:
//#line 834 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 176:
//#line 837 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1377 "Parser.java"
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
