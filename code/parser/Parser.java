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
   34,   34,   34,   34,   41,   41,   42,   42,   43,   43,
    9,    9,    9,    1,    1,    1,    1,    1,    6,    6,
    2,    2,    2,    2,    4,    4,    4,    7,    7,    3,
    3,    3,    5,    5,    5,   11,   11,   10,   10,   44,
   44,   45,   45,   47,   47,   46,   46,   48,   48,   48,
   48,   48,   48,   48,   39,   39,   39,   39,   39,   49,
   49,   49,   40,   40,   40,   50,   51,   51,   51,   52,
   28,   28,   28,   55,   55,   54,   53,   53,   56,   56,
   56,   58,   58,   57,   57,   57,   59,   59,   59,   36,
   36,   36,   36,   36,   12,   13,   13,   14,   14,   37,
   37,   61,   61,   61,   61,   60,   62,   62,   38,   38,
   38,   38,   38,   65,   65,   65,   64,   63,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    0,    4,    2,    1,    1,
    3,    3,    0,    4,    2,    0,    3,    2,    2,    2,
    2,    1,    2,    2,    1,    1,    1,    2,    0,    1,
    1,    1,    3,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    2,    1,    1,    3,    3,    3,    3,
    5,    2,    3,    3,    2,    2,    4,    4,    3,    3,
    4,    6,    4,    6,    3,    1,    2,    1,    2,    1,
    1,    3,    2,    1,    3,    3,    2,    2,    1,    1,
    3,    1,    3,    2,    3,    1,    3,    1,    1,    2,
    1,    1,    1,    1,    1,    1,    2,    1,    3,    3,
    2,    1,    0,    1,    1,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    6,    6,    5,    2,    5,    0,
    2,    1,    3,    3,    2,    2,    1,    1,    2,    2,
    6,    5,    7,    1,    2,    3,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    5,
    5,    4,    3,    2,    4,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    0,    3,    1,    1,    4,    4,
    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   22,   25,   26,   27,    0,   38,   39,   40,   41,
   42,   43,   45,   46,    0,    9,   10,    5,    0,   24,
    0,    0,   96,  167,    0,    0,   88,   89,    0,    0,
    0,   82,    0,    0,   91,   92,    0,    0,    0,  118,
    0,    0,    0,   52,    0,    0,    0,    0,  125,    0,
    0,   31,    0,   32,    0,    0,    0,  154,    0,    0,
    0,   18,   15,    0,    0,    0,    0,   66,    0,    0,
    0,    0,   44,   37,   23,   19,    0,   30,   28,    0,
   35,    0,    0,    0,    7,   99,    0,    0,  156,    0,
   97,    0,    0,   94,   79,   80,    0,   86,    0,   93,
   95,    0,   84,   90,  160,  161,  101,    0,    0,    0,
    0,    0,   49,   56,   48,    0,    0,  149,  148,    0,
    0,    0,  139,  141,    0,   50,   55,   47,    0,    0,
  130,   34,    0,    0,  126,  123,  124,    0,    0,  153,
    0,    0,    0,    0,   60,    0,    0,   67,   59,    0,
   68,    0,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,    0,    0,    0,  155,  166,    0,   76,    0,
   83,   81,  121,    0,    0,  110,  112,  111,  113,  114,
  108,  109,    0,  105,  104,  100,   53,    0,  143,    0,
    0,    0,  146,   54,    0,   33,  152,    0,   20,   21,
  178,   58,   57,   63,   61,    0,    0,   65,   14,    0,
    0,    0,  169,  170,    0,  172,   11,  158,  157,   87,
   85,    0,  117,  119,    0,    0,    0,  134,  132,  140,
  144,   51,  151,  150,    0,   71,   69,  171,  177,  173,
  116,  115,    0,  131,  135,    0,   64,   62,    0,   73,
  133,  136,   72,
};
final static short yydgoto[] = {                          3,
   50,   51,   52,  117,  118,  119,   53,   67,  255,   16,
   55,   17,  108,  109,   18,   19,  266,   38,    4,    6,
  163,   21,   97,   22,   23,   24,   25,   26,   99,   73,
   74,  103,   27,   28,   29,   30,   31,   32,   33,   34,
   91,   92,  228,   62,   63,  132,  206,  203,  130,   75,
   76,   77,  141,  248,  249,  142,  143,  144,  145,   57,
   58,   59,   35,  181,  177,
};
final static short yysindex[] = {                       -81,
   24,   34,    0,  -91,    0, -221,  -85,  104,  542,   25,
   20,   46,  548,  -17, -204,  379,  -45,    0,    0,  189,
  -52,    0,    0,    0,    0,   44,    0,    0,    0,    0,
    0,    0,    0,    0,  142,    0,    0,    0,    2,    0,
 -140,  466,    0,    0,  431, -131,    0,    0,  555,   38,
  -14,    0,  122, -121,    0,    0,   87, -109,    0,    0,
  113,   89,  466,    0,   96,  -28,  279, -102,    0,  126,
  208,    0,  -37,    0,  129,  -66,    0,    0,  -16,   59,
   66,    0,    0,  101,  -65,  317,  -60,    0,   64, -121,
   71,   17,    0,    0,    0,    0,  120,    0,    0,  208,
    0,  158,  220,  120,    0,    0,   -6,  107,    0,  -14,
    0,    0,  159,    0,    0,    0,  -14,    0,  247,    0,
    0,  125,    0,    0,    0,    0,    0,  224,  -69,  -67,
  440,  -31,    0,    0,    0,  -53,  -28,    0,    0, -194,
  165,  166,    0,    0, -181,    0,    0,    0,  -50,   71,
    0,    0,  240,    0,    0,    0,    0,  150,    4,    0,
    0,   88,   91,  181,    0,  -38,  187,    0,    0,   55,
    0,   71,  131,  253,   52,  226,   27,  158,  -26,    0,
    0,   41,  145,   77,  466,    0,    0,  260,    0,  -14,
    0,    0,    0,  -46,  276,    0,    0,    0,    0,    0,
    0,    0,  466,    0,    0,    0,    0,  258,    0,  216,
 -194,  -60,    0,    0,  282,    0,    0,  -42,    0,    0,
    0,    0,    0,    0,    0,   71,  304,    0,    0,   93,
    0,  309,    0,    0,   95,    0,    0,    0,    0,    0,
    0,   75,    0,    0,   38,  231,   58,    0,    0,    0,
    0,    0,    0,    0,  135,    0,    0,    0,    0,    0,
    0,    0,   70,    0,    0,  163,    0,    0,   71,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                        22,
  109,  369,    0,  369,    0,    0,    0,  487,  114,  479,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  372,
  175,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -21,
  407,    0,    0,    0,    0,    0,  118,    0,  128,    0,
  561,  127,    0,    0,  121,  -23,    0,    0,    0,  479,
    0,    0,  479,    0,  139,    0,  144,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   50,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  148,  148,    0,    0,    0,  133,    0,    0,  497,
    0,  151,    0,    0,    0,    0,  509,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -15,   29,    0,
    7,    0,    0,    0,    0,    0,  -23,    0,    0, -138,
    0,  348,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  154,    0,    0,    0,    0,    0,    0,
   12,  406,  413,    0,    0,    0,    9,    0,    0,    0,
    0,    0,    0,  148,    0,  162,    0,  148,    0,    0,
  162,    0,    0,    0,    0,    0,    0,    0,    0,  519,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -33,  143,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   85,    0,    0,    0,
  169,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   16,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  608,   14,  498,    0,    0,    0,    3,    0,    0,   -7,
  373,   42,    0,  242,   30,  424,   61,    0,    0,    0,
    0,    0,    0,  -20,  582,    0,    0,    0,    0,   28,
  399,   -4,    0,    0,    0,    0,    0,    0,    0,    0,
  344,    0,    0,  116,    0,    0,    0,    0,  310,    0,
    0,  365,  306,  198,    0,    0, -110,    0,    0,    0,
    0,  404,    0,  352,  -35,
};
final static int YYTABLESIZE=819;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         95,
   29,   54,   61,   68,  115,   54,  116,  142,   90,  205,
  142,   17,  243,   94,  231,  140,  254,  138,   46,  168,
  223,    4,   15,    5,  158,   48,   45,   48,   46,  209,
   47,   14,   47,   36,   54,   39,  115,   54,  116,   40,
   29,   54,  120,  122,  218,   54,  115,  107,  116,   98,
   56,   24,   98,  122,   56,   54,  106,   56,  110,   66,
   87,  138,   20,   95,   61,   85,  153,  182,  105,   98,
   96,   54,  120,   15,   84,  167,  139,   88,   54,  168,
  115,  120,  116,   56,  171,   15,   56,  120,  212,  129,
   56,  121,  231,   68,   56,  174,   46,   15,  226,  120,
  250,  115,   98,  116,   56,   82,  115,   83,  116,   15,
   68,   54,  122,  225,   54,   46,  106,  160,  147,  188,
   56,  121,  169,  120,  104,   29,  111,   56,   15,   70,
  121,  147,  190,  262,  124,  137,   24,  213,  230,  136,
   15,   41,  235,   42,    4,  125,  126,  186,  121,   41,
  185,  120,   95,  127,  135,  193,   14,  173,  120,   15,
   56,  150,   95,   56,  183,   61,   46,   54,   71,   46,
   15,   54,  121,  159,    1,    2,  159,   54,  269,   46,
   54,   15,  265,  145,   15,  151,  145,  156,  154,  157,
  161,  164,  122,  268,  271,   54,  167,  175,  128,  187,
  121,  195,   15,  207,  251,  210,  214,  121,  217,  211,
   93,   71,  219,  253,   13,  220,   56,  222,    8,  114,
   56,  221,  242,  147,  204,  162,   56,  138,   15,   56,
    8,   43,   41,  147,  168,   70,  147,  120,   81,    8,
    8,   43,  139,   85,   56,   95,  147,   15,    9,   10,
    8,  114,   11,  122,   12,  229,   13,   29,  184,  179,
    8,  114,  107,   15,  100,   98,   29,   29,   24,  237,
   29,  106,   29,  167,   29,   64,   65,   24,   24,   15,
   60,   24,  234,   24,  233,   24,  121,  272,   48,    7,
    8,   46,  179,   47,    8,  114,  236,  120,  246,    9,
   10,   69,    8,   11,   46,   12,   68,   13,    8,   43,
  224,    9,   10,    7,    8,    8,  114,   12,   70,   13,
    8,  114,  149,    9,   10,    7,    8,   11,   43,   12,
  261,   13,  152,  238,  244,    9,   10,  148,  247,   11,
  252,   12,   70,   13,  178,    8,   71,  257,  258,  259,
  260,  133,  134,  263,    9,   10,  128,    8,   48,   45,
   12,   46,   13,   47,  216,    6,    9,   10,   16,  165,
   11,    3,   12,  162,   13,    7,    8,  216,    8,   43,
  191,    8,   43,  164,   98,    9,   10,    8,  137,   11,
  267,   12,   43,   13,  127,  120,    9,   10,    8,  128,
   11,    8,   12,  176,   13,   11,  163,    9,   10,  129,
    9,   10,   12,   12,   11,   13,   12,  174,   13,    8,
   48,   45,   87,   46,  175,   47,  239,   37,    9,   10,
   13,   13,   11,  102,   12,  172,   13,  155,  194,   88,
   13,   13,  208,  264,   13,    8,   13,   74,   13,   74,
   74,   74,  113,  176,    9,   10,    0,    0,   11,    0,
   12,    0,   13,  170,    8,   74,   74,   74,   74,    0,
    0,    0,   48,    9,   10,   46,    8,   47,    0,   12,
    8,   13,  115,    0,  116,    9,   10,    0,    0,    9,
   10,   12,    0,   13,    0,   12,    8,   13,    0,  202,
  200,  201,  189,    8,   43,    9,   10,   48,   45,    8,
   46,   12,   47,   13,    0,  240,    8,   43,    9,   10,
  103,  103,  215,  103,   12,  103,   13,   98,   98,   98,
   98,   98,    0,   98,  146,  147,    0,   78,    0,   78,
   78,   78,    0,    0,  227,   98,   98,   98,   98,   77,
  123,   77,   77,   77,    0,   78,   78,   78,   78,   75,
    0,   75,   75,   75,    0,    0,    0,   77,   77,   77,
   77,    0,  165,    8,   43,    0,    0,   75,   75,   75,
   75,   49,    0,   48,   45,    0,   46,   79,   47,   48,
   45,    0,   46,   72,   47,  112,   48,   45,  256,   46,
    0,   47,  102,  102,    0,  102,    0,  102,    0,    0,
    0,    0,    0,    0,    0,    0,  101,    0,    0,  192,
   80,    0,    0,   89,    0,    0,    0,  270,    0,    0,
    0,    0,    0,    0,    0,    8,   43,    0,    0,    0,
    0,  273,   86,   72,    0,    0,    0,    0,    0,  107,
    0,    0,  101,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   74,   74,   74,    0,   74,   74,   74,   74,
  131,   74,  232,    0,    0,    0,  232,    0,    0,    0,
    0,  101,    0,    0,  180,  241,  159,    8,   43,    0,
    0,    0,    0,  166,    0,    0,    8,  114,    0,  196,
  197,  198,  199,    0,    0,    0,    0,    0,    0,   72,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    8,   43,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  180,  103,  103,    0,    0,    0,
    0,    0,   98,   98,   98,    0,   98,   98,   98,   98,
   98,   98,   78,   78,   78,  180,   78,   78,   78,   78,
    0,   78,    0,    0,   77,   77,   77,    0,   77,   77,
   77,   77,    0,   77,   75,   75,   75,    0,   75,   75,
   75,   75,    0,   75,    0,    0,    0,    0,    0,    0,
    0,    0,  107,    0,    0,    0,    0,    0,    8,   43,
   44,    0,    0,   78,    8,   43,    0,    0,    0,    0,
  245,    8,   43,   44,    0,    0,    0,  102,  102,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,    9,   40,   11,   43,   13,   45,   41,   16,   41,
   44,    0,   59,   59,   41,   44,   59,   41,   45,   41,
   59,    0,   40,    0,   41,   42,   43,   42,   45,  140,
   47,  123,   47,    4,   42,  257,   43,   45,   45,  125,
   40,   49,   50,   59,   41,   53,   43,   41,   45,   41,
    9,   40,   44,   51,   13,   63,   41,   16,   45,   40,
   44,  256,    2,   84,   40,  270,   71,  103,   39,   61,
  123,   79,   80,   40,   14,  257,  271,   61,   86,   87,
   43,   89,   45,   42,   92,   40,   45,   59,  270,   62,
   49,   50,   41,   44,   53,  100,   45,   40,   44,  107,
  211,   43,   59,   45,   63,  123,   43,  125,   45,   40,
   61,  119,  110,   59,  122,   45,  257,   59,  257,  117,
   79,   80,   59,  131,  123,  125,  258,   86,   40,   45,
   89,  270,  119,   59,  256,   40,  125,  145,  174,   44,
   40,   46,  178,   40,  123,   59,  256,   41,  107,   46,
   44,  159,  173,   41,   59,  128,  123,   97,  166,   40,
  119,  264,  183,  122,  104,   40,   45,  175,  123,   45,
   40,  179,  131,   41,  256,  257,   44,  185,   44,   45,
  188,   40,  125,   41,   40,   70,   44,   59,   73,  256,
  125,  257,  190,   59,  125,  203,  257,   40,  268,   41,
  159,  269,   40,  257,  212,   41,  257,  166,   59,   44,
  256,  123,  125,  256,   40,  125,  175,  256,  257,  258,
  179,   41,  269,  257,  256,  125,  185,  256,   40,  188,
  257,  258,   46,  257,  256,  273,  270,  245,  256,  257,
  257,  258,  271,  270,  203,  266,  270,   40,  266,  267,
  257,  258,  270,  269,  272,  125,  274,  257,  265,   40,
  257,  258,  256,   40,  123,  257,  266,  267,  257,  125,
  270,  256,  272,  257,  274,  256,  257,  266,  267,   40,
  256,  270,  256,  272,   59,  274,  245,  125,   42,  256,
  257,   45,   40,   47,  257,  258,  256,  269,   41,  266,
  267,  256,  257,  270,   45,  272,  257,  274,  257,  258,
  256,  266,  267,  256,  257,  257,  258,  272,  273,  274,
  257,  258,   44,  266,  267,  256,  257,  270,  258,  272,
  256,  274,  125,  257,   59,  266,  267,   59,  123,  270,
   59,  272,  258,  274,  125,  257,  123,   44,  256,   41,
  256,  256,  257,  123,  266,  267,  268,  257,   42,   43,
  272,   45,  274,   47,  125,  257,  266,  267,    0,  256,
  270,    0,  272,  256,  274,  256,  257,  125,  257,  258,
  256,  257,  258,  256,  264,  266,  267,  257,   41,  270,
  256,  272,  258,  274,  256,  269,  266,  267,  257,  256,
  270,  257,  272,  256,  274,    0,  256,  266,  267,  256,
  266,  267,    0,  272,  270,  274,  272,  256,  274,  257,
   42,   43,   44,   45,  256,   47,  185,    4,  266,  267,
  256,  257,  270,   35,  272,   92,  274,   73,  129,   61,
  266,  267,  137,  246,  270,  257,  272,   41,  274,   43,
   44,   45,   49,  102,  266,  267,   -1,   -1,  270,   -1,
  272,   -1,  274,   91,  257,   59,   60,   61,   62,   -1,
   -1,   -1,   42,  266,  267,   45,  257,   47,   -1,  272,
  257,  274,   43,   -1,   45,  266,  267,   -1,   -1,  266,
  267,  272,   -1,  274,   -1,  272,  257,  274,   -1,   60,
   61,   62,  256,  257,  258,  266,  267,   42,   43,  257,
   45,  272,   47,  274,   -1,  256,  257,  258,  266,  267,
   42,   43,  150,   45,  272,   47,  274,   41,   42,   43,
   44,   45,   -1,   47,  256,  257,   -1,   41,   -1,   43,
   44,   45,   -1,   -1,  172,   59,   60,   61,   62,   41,
   53,   43,   44,   45,   -1,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,   59,   60,   61,
   62,   -1,  256,  257,  258,   -1,   -1,   59,   60,   61,
   62,   40,   -1,   42,   43,   -1,   45,   40,   47,   42,
   43,   -1,   45,   12,   47,   41,   42,   43,  226,   45,
   -1,   47,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   35,   -1,   -1,  122,
   13,   -1,   -1,   16,   -1,   -1,   -1,  255,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,
   -1,  269,  264,   62,   -1,   -1,   -1,   -1,   -1,   42,
   -1,   -1,   71,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   63,  265,  175,   -1,   -1,   -1,  179,   -1,   -1,   -1,
   -1,  100,   -1,   -1,  103,  188,   79,  257,  258,   -1,
   -1,   -1,   -1,   86,   -1,   -1,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,  128,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  153,  257,  258,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  256,  257,  258,  174,  260,  261,  262,  263,
   -1,  265,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  185,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
  203,  257,  258,  259,   -1,   -1,   -1,  257,  258,
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

//#line 846 "gramatica.y"

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
    yydebug = true;
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

    System.out.println(s);
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
//#line 766 "Parser.java"
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
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 52:
//#line 249 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
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
case 68:
//#line 331 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 70:
//#line 339 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 347 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 73:
//#line 352 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 75:
//#line 366 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 371 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 77:
//#line 375 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 78:
//#line 382 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 392 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 80:
//#line 394 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 81:
//#line 401 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 83:
//#line 407 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 414 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 85:
//#line 421 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 87:
//#line 427 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 88:
//#line 434 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 89:
//#line 436 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 97:
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
case 99:
//#line 482 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 100:
//#line 491 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 101:
//#line 496 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 103:
//#line 502 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 105:
//#line 507 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 107:
//#line 518 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 114:
//#line 534 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 115:
//#line 543 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 116:
//#line 548 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 117:
//#line 550 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 118:
//#line 552 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 119:
//#line 554 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 122:
//#line 566 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 123:
//#line 575 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 124:
//#line 580 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 125:
//#line 582 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 128:
//#line 599 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 129:
//#line 601 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 131:
//#line 616 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 132:
//#line 621 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 133:
//#line 624 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 135:
//#line 636 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 653 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 665 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 683 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 685 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 697 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 706 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 711 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 713 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 715 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 717 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 726 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 736 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 743 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 748 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 757 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 762 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 773 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 776 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 778 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 801 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 806 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 171:
//#line 808 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 172:
//#line 810 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 173:
//#line 812 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 175:
//#line 823 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 176:
//#line 826 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1355 "Parser.java"
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
