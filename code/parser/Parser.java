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
    import common.Monitor;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import semantic.TypeChecker;
    import common.SymbolCategory;
    import common.SymbolDirector;
    import semantic.ReversePolish;
    import semantic.ReturnsController;
//#line 35 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 36 "Parser.java"




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
    0,    0,   27,    0,   28,    0,    0,    0,    1,   25,
   25,   31,   25,   25,   25,   25,   30,   30,   29,   29,
   26,   26,   26,   32,   32,   34,   34,   37,   37,   38,
   38,   39,   39,   40,   40,   33,   33,   33,   33,   33,
   33,   33,   43,   43,   35,   35,   35,   35,    6,    6,
    6,    5,   41,   41,   41,   41,   24,   24,   22,   22,
   22,   23,   23,   23,   17,   17,   17,   17,   17,   14,
   14,   18,   18,   18,   18,   20,   20,   20,   15,   15,
   19,   19,   19,   21,   21,   21,    4,    4,    3,    3,
   49,   49,   49,   49,   50,   50,   50,   50,   16,   16,
   16,   16,   16,   16,   16,   47,   47,   53,   51,   52,
   52,   52,   52,   52,   54,   54,   54,   55,   48,   48,
   48,   56,   57,   57,   57,   58,   36,   36,    7,    7,
   60,   60,   59,   59,   61,   61,   61,   63,   63,   62,
   62,   62,   13,   13,   13,   44,   44,   44,   44,   44,
   42,   42,    8,    2,   11,   11,   12,   12,   45,   45,
   64,   64,   64,   64,   65,   65,   46,   46,   46,   46,
   46,   10,   10,   10,    9,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    5,    2,    1,    3,
    2,    1,    4,    4,    3,    3,    3,    3,    2,    3,
    3,    1,    3,    2,    1,    3,    3,    2,    2,    1,
    1,    3,    1,    3,    2,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    3,    2,    2,    3,    3,    2,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    2,    0,    3,    4,
    4,    3,    3,    3,    0,    2,    1,    1,    3,    3,
    2,    1,    2,    1,    2,    2,    5,    4,    2,    1,
    1,    2,    3,    2,    1,    3,    1,    2,    2,    3,
    2,    2,    0,    1,    1,    5,    5,    4,    3,    2,
    2,    2,    4,    1,    1,    3,    3,    1,    3,    3,
    3,    2,    1,    0,    1,    1,    4,    4,    5,    4,
    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  122,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   38,    1,    0,    0,   21,   24,   25,
   26,    0,   36,   37,   39,   40,   41,   42,   43,   44,
    0,    0,    4,    0,   23,    0,   87,  165,    0,    0,
   79,   80,    0,    0,   82,   83,    0,    0,    0,   73,
    0,  163,  107,    0,   48,    0,    0,    0,  150,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,    0,
   59,    0,    0,    0,  152,  151,    0,   34,    0,    0,
   62,    0,   22,   18,    0,   29,   27,  118,    0,   30,
    0,   31,  106,    0,    0,  121,    0,    0,    0,  124,
    6,   90,    0,   88,  162,    0,   75,   85,   70,   71,
    0,   86,    0,    0,   77,    0,  160,  159,    0,    0,
    0,  109,    0,    0,   46,   51,   45,    0,    0,    0,
  149,   16,    0,    0,   52,    0,    0,  155,    0,    0,
   61,   56,    0,   60,   55,  145,  144,    0,  134,    0,
    0,  135,  137,    0,   33,    0,    0,    0,    0,    0,
    0,   35,   58,   57,    0,   64,    0,    0,    0,    0,
  116,  126,  125,  123,  120,  119,  161,   67,    0,    0,
   74,   72,   92,    0,  101,  103,  102,  104,  105,   99,
  100,    0,    0,    0,   93,    0,   50,  148,    0,   19,
   20,  175,    0,  153,    0,   54,   53,  139,  142,    0,
    0,  133,  128,  131,    0,   32,    0,  173,    0,  168,
  167,    0,  170,   63,   13,  112,    0,  113,  114,   78,
   76,   94,   91,    0,    0,   47,  147,  146,  156,  157,
  140,  136,  127,  132,  169,  172,  171,  111,  110,
};
final static short yydgoto[] = {                          4,
    5,   18,   54,   55,   67,   68,   20,   56,   22,  168,
  147,  148,  160,  123,   57,  203,  130,   59,   60,  124,
  125,   23,   92,   24,   25,   26,    6,    8,  144,   27,
   95,   28,   29,   30,   31,   32,   97,  101,  102,   90,
   33,   34,   35,   36,   37,   38,   39,   40,  132,  133,
   41,  103,   64,  104,  105,   42,  109,  110,   84,  225,
  161,  162,  163,   61,   62,
};
final static short yysindex[] = {                         3,
   62,    0,    0,    0,  -24,  -60,    0, -183,   19,   65,
  159, -149, -226,    0,  516,   20, -158,   73,  499,   79,
  -46,  112,   16,    0,    0,  123,   -8,    0,    0,    0,
    0,   63,    0,    0,    0,    0,    0,    0,    0,    0,
   -2,   32,    0,  -60,    0, -133,    0,    0,  465, -135,
    0,    0,  529,    0,    0,    0,   58,  -31,   17,    0,
   29,    0,    0,  545,    0,    0, -127,   25,    0,  558,
  568,  -32,    0,    0,  -36, -126,  389, -115,   85, -115,
    0,  571,  -34,   26,    0,    0,  137,    0,  111,  153,
    0,   95,    0,    0,   52,    0,    0,    0,  137,    0,
 -112,    0,    0, -116,  156,    0,  545,  141,   37,    0,
    0,    0,   17,    0,    0,  117,    0,    0,    0,    0,
    0,    0,  379,   17,    0,  363,    0,    0,  565,  507,
   17,    0,  125,   16,    0,    0,    0, -126,  108,  149,
    0,    0,   43,   44,    0,  130,  -19,    0,   38,   -9,
    0,    0,  521,    0,    0,    0,    0, -227,    0, -237,
    4,    0,    0,   64,    0,  172,  -39,   46,  111,  -13,
  -78,    0,    0,    0,   16,    0,   78,  176,  116,  126,
    0,    0,    0,    0,    0,    0,    0,    0,   17,  367,
    0,    0,    0,   -7,    0,    0,    0,    0,    0,    0,
    0,  379,  389,   17,    0,  128,    0,    0,   51,    0,
    0,    0,  389,    0,  -75,    0,    0,    0,    0,  -59,
 -227,    0,    0,    0,   89,    0,  -56,    0,  166,    0,
    0,  -47,    0,    0,    0,    0,   55,    0,    0,    0,
    0,    0,    0,   17,  -31,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        15,
  -44,    0,    0,    0,  197,  197,    0,    0,    0,  411,
   61,  551,  171,    0,    0,    0,    0,    0,  185,    0,
    0,    0,    0,    0,    0,  229,  101,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
  -18,    0,    0,  197,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  421,    0,    0,    0,  -33,  269,    0,
    0,    0,    0,    0,    0,   27,  120,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -215,    0,    0,    0,    0,    0,   77,   24,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0,  136,    0,    0,    0,    0,    0,
    0,    0,  433,    0,    0,    0,    0,    0,    0,    0,
  444,    0,    0,  455,    0,    0,    0,    0,    0,    0,
  321,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  254,  263,    0,    0,    0,    0,   13,    0,
    0,    0,    0,    0,    0,    0,    0, -215,    0,    0,
    0,    0,    0,    0,    0,   24,    0,    0,   24,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  472,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  478,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
   -1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  487,    5,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  315,    0,   -5,   42,  -29,    0,    0,   75,    0,  -71,
    0,  119,    0,  195,  527,    0,  706,  711,  270,  199,
    0,   28,    0,    0,   12,    7,    0,    0,    0,    0,
    0,  -17,  740,    0,    0,    0,    0,  -37,  317,  -48,
    0,    0,    0,    0,    0,    0,    0,    0,  -58,  208,
    0,    0,    0,  239,    0,    0,    0,  241,    0,    0,
    0, -134,    0,    0,  294,
};
final static int YYTABLESIZE=919;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         19,
   28,  228,    2,   17,  108,   50,  159,  166,   93,  158,
   19,  119,   86,  120,    3,   17,   19,   43,  171,  219,
   19,  214,   75,  218,  213,  166,   45,  228,  156,   65,
   66,   50,  220,  243,   80,   19,   19,   17,  166,  138,
   28,  143,  138,  157,  222,   95,  146,  221,  182,  183,
  178,   81,  121,  158,  143,  111,  158,   93,   52,   17,
   50,    7,   16,   51,   91,  121,  129,  181,  138,   19,
   52,   17,  150,    3,  150,  141,  121,   45,  141,   21,
  119,   19,  120,  137,   19,   52,  252,  128,  143,   19,
   21,   17,  142,   19,  227,  186,   21,  232,   16,   19,
   21,  177,   50,   17,  231,  151,   63,  154,  207,  248,
   46,   76,   77,  259,   94,   21,   21,   17,   83,  164,
   99,   96,  114,  112,  121,   28,   52,   49,   17,   50,
  145,   51,  122,  176,  121,  174,  134,    3,  175,   50,
   12,   10,   73,  121,   74,  122,  224,  121,  164,   21,
  167,   17,  180,  174,   99,   98,  122,  187,   19,   93,
   19,   21,   17,   49,   21,  205,  208,  210,  211,   21,
  212,   19,   19,   21,  238,  206,   17,  233,   49,   21,
  129,  250,   52,   49,  239,   50,  246,   51,  223,  209,
  115,  119,  170,  120,  117,   17,   15,  251,   53,  255,
   52,   49,  235,   50,  122,   51,  256,  254,  257,   85,
  130,  170,    5,  253,  122,   17,  234,   10,   47,   19,
   10,  156,  166,  122,  154,   10,  118,  122,    2,   11,
   12,    9,   10,   13,   87,   14,  157,   15,   21,  121,
   21,   11,   12,   10,   47,   13,   78,   14,  242,   15,
  115,   21,   21,   10,   10,  143,   76,   28,    1,    3,
   95,  165,   11,   11,   12,   98,   28,   28,  143,   14,
   28,   15,   28,   47,   28,   72,   10,  169,   99,  174,
  135,  136,   52,   52,  127,   11,   12,  106,   10,   13,
   52,   14,  185,   15,   10,  118,  226,   11,   12,   21,
  226,  230,  215,   14,  107,   15,  247,    9,   10,   65,
  258,   65,   65,   65,   10,   47,  164,   11,   12,  122,
   10,   13,   44,   14,  202,   15,  117,   65,  204,   11,
   12,  249,  174,   13,   10,   14,  194,   15,   89,  179,
  152,   10,   47,   11,   12,   10,  116,   13,  184,   14,
  173,   15,   47,    0,   11,   12,   12,   12,   13,    0,
   14,   98,   15,   65,    0,   65,   12,   12,   10,    0,
   12,  236,   12,    0,   12,   49,   49,   11,   12,   10,
   65,   65,   65,   14,  237,   15,    0,  115,   11,   12,
    0,  117,   13,   10,   14,  192,   15,   10,   47,    0,
  115,    0,   11,   12,  117,   10,  118,   50,   14,   10,
   15,   50,   10,  107,    0,   10,   47,   48,   11,   12,
   52,   11,   12,   50,   14,   51,   15,   14,   10,   15,
   52,   49,   10,   50,    0,   51,  229,   11,   12,  229,
    0,   11,   12,   14,    0,   15,    0,   14,    0,   15,
   89,   89,   89,   89,   89,   89,    0,   89,    0,  241,
  154,   81,   81,   81,   81,   81,    0,   81,    0,   89,
   89,   89,   89,   69,    0,   69,   69,   69,    0,   81,
   81,   81,   81,  154,   84,   84,   84,   84,   84,    0,
   84,   69,   69,   69,   69,   68,    0,   68,   68,   68,
    0,    0,   84,   84,   84,   84,   52,    0,    0,   50,
    0,   51,   66,   68,   66,   66,   66,    0,   96,    0,
   68,    0,   68,    0,   65,   65,   65,   97,    0,   66,
   66,   66,    0,   65,    0,    0,    0,   68,   68,   68,
   52,   49,   80,   50,    0,   51,   66,   66,   66,  119,
    0,  120,    0,    0,    0,   70,    0,   52,   49,   81,
   50,    0,   51,  119,    0,  120,  201,  199,  200,  115,
   52,   49,    0,   50,    0,   51,   98,   65,   65,  217,
   65,   65,   65,   65,  129,  126,   52,   49,    0,   50,
  108,   51,  108,  108,    0,  108,    0,  108,  139,   52,
   49,    0,   50,    0,   51,  193,   52,   49,    0,   50,
  119,   51,  120,  119,    0,  120,    0,    0,  191,   10,
   47,    0,  240,   10,   47,    0,  141,    0,    0,  155,
    0,    0,    0,    0,  188,   10,   47,    0,    0,  126,
    0,    0,    0,    0,    0,   10,   47,    0,    0,    0,
  190,    0,    0,    0,    0,    0,    0,  126,    0,    0,
    0,    0,    0,    0,    0,    0,   89,   89,   89,    0,
   89,   89,   89,   89,   89,   89,   81,   81,   81,    0,
   81,   81,   81,   81,    0,   81,    0,    0,   69,   69,
   69,    0,   69,   69,   69,   69,    0,   69,    0,   84,
   84,   84,    0,   84,   84,   84,   84,    0,   84,    0,
   68,   68,   68,    0,    0,  126,   58,    0,    0,   68,
   71,   10,   47,    0,   82,    0,    0,   66,   66,   66,
  190,    0,    0,   96,   68,   68,   66,   68,   68,   68,
   68,    0,   97,   66,   66,    0,   66,   66,   66,   66,
    0,    0,    0,    0,   78,   10,   47,    0,   58,  113,
    0,   88,   79,   10,  118,    0,  195,  196,  197,  198,
  126,   69,   10,   47,  131,  140,  216,   10,  118,    0,
  100,  100,  149,    0,  153,   10,   47,   48,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   10,   47,    0,    0,    0,    0,  108,  108,    0,
    0,    0,    0,    0,   10,   47,    0,  131,  131,    0,
    0,   10,   47,    0,   10,  118,   88,   10,  118,  172,
    0,    0,    0,  189,    0,    0,    0,    0,   88,  131,
    0,    0,    0,    0,  100,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  172,    0,    0,  245,    0,
    0,    0,  244,    0,    0,    0,    0,  172,  149,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,   41,    0,   40,   42,   45,   41,   41,   26,   44,
   16,   43,   59,   45,    0,   40,   22,    6,   90,  257,
   26,   41,   16,  158,   44,   59,   59,   41,  256,  256,
  257,   45,  270,   41,   44,   41,   42,   40,   87,   41,
   40,  257,   44,  271,   41,   41,   76,   44,  107,  108,
   99,   61,   58,   41,  270,   44,   44,   75,   42,   40,
   45,    0,  123,   47,   23,   71,   40,  105,   44,   75,
   44,   40,   78,  257,   80,   41,   82,   59,   44,    5,
   43,   87,   45,   59,   90,   59,  221,   59,  125,   95,
   16,   40,  125,   99,  166,   59,   22,  169,  123,  105,
   26,   95,   45,   40,   59,   78,  256,   80,  138,   59,
   46,  270,   40,   59,  123,   41,   42,   40,   40,   59,
  123,   59,  258,  257,  130,  125,   42,   43,   40,   45,
  257,   47,   58,   92,  140,   59,  264,  123,   44,   45,
   40,  257,  123,  149,  125,   71,  164,  153,  123,   75,
   40,   40,  269,   59,  123,  268,   82,   41,  164,  177,
  166,   87,   40,   44,   90,   41,   59,  125,  125,   95,
   41,  177,  178,   99,   59,  134,   40,  256,   59,  105,
   40,  257,   42,   43,   59,   45,   59,   47,  125,   41,
   59,   43,   40,   45,   59,   40,    0,  257,   40,  256,
   42,   43,  125,   45,  130,   47,   41,  225,  256,  256,
   40,   40,  257,  125,  140,   40,  175,  257,  258,  225,
  257,  256,  256,  149,   40,  257,  258,  153,    0,  266,
  267,  256,  257,  270,  123,  272,  271,  274,  164,  245,
  166,  266,  267,  257,  258,  270,  256,  272,  256,  274,
  269,  177,  178,    0,  257,  257,  270,  257,  256,  257,
  256,  125,    0,  266,  267,  268,  266,  267,  270,  272,
  270,  274,  272,  258,  274,  256,  257,  125,  123,  256,
  256,  257,  256,  257,  256,  266,  267,  256,  257,  270,
  264,  272,  256,  274,  257,  258,  125,  266,  267,  225,
  125,  256,  265,  272,  273,  274,  256,  256,  257,   41,
  256,   43,   44,   45,  257,  258,  256,  266,  267,  245,
  257,  270,    8,  272,  130,  274,   57,   59,  130,  266,
  267,  213,  256,  270,  257,  272,  129,  274,   22,  101,
  256,  257,  258,  266,  267,  257,   53,  270,  108,  272,
  256,  274,  258,   -1,  266,  267,  256,  257,  270,   -1,
  272,   41,  274,   43,   -1,   45,  266,  267,  257,   -1,
  270,  256,  272,   -1,  274,  256,  257,  266,  267,  257,
   60,   61,   62,  272,  269,  274,   -1,  256,  266,  267,
   -1,  256,  270,  257,  272,  126,  274,  257,  258,   -1,
  269,   -1,  266,  267,  269,  257,  258,   45,  272,  257,
  274,   45,  257,  273,   -1,  257,  258,  259,  266,  267,
   42,  266,  267,   45,  272,   47,  274,  272,  257,  274,
   42,   43,  257,   45,   -1,   47,  167,  266,  267,  170,
   -1,  266,  267,  272,   -1,  274,   -1,  272,   -1,  274,
   40,   41,   42,   43,   44,   45,   -1,   47,   -1,  190,
   40,   41,   42,   43,   44,   45,   -1,   47,   -1,   59,
   60,   61,   62,   41,   -1,   43,   44,   45,   -1,   59,
   60,   61,   62,   40,   41,   42,   43,   44,   45,   -1,
   47,   59,   60,   61,   62,   41,   -1,   43,   44,   45,
   -1,   -1,   59,   60,   61,   62,   42,   -1,   -1,   45,
   -1,   47,   41,   59,   43,   44,   45,   -1,   41,   -1,
   43,   -1,   45,   -1,  256,  257,  258,   41,   -1,   43,
   59,   45,   -1,  265,   -1,   -1,   -1,   60,   61,   62,
   42,   43,   44,   45,   -1,   47,   60,   61,   62,   43,
   -1,   45,   -1,   -1,   -1,   40,   -1,   42,   43,   61,
   45,   -1,   47,   43,   -1,   45,   60,   61,   62,   41,
   42,   43,   -1,   45,   -1,   47,  256,  257,  258,   59,
  260,  261,  262,  263,   40,   59,   42,   43,   -1,   45,
   40,   47,   42,   43,   -1,   45,   -1,   47,   41,   42,
   43,   -1,   45,   -1,   47,   41,   42,   43,   -1,   45,
   43,   47,   45,   43,   -1,   45,   -1,   -1,  256,  257,
  258,   -1,  256,  257,  258,   -1,   59,   -1,   -1,   59,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,  113,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,
  124,   -1,   -1,   -1,   -1,   -1,   -1,  131,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,   -1,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,
  256,  257,  258,   -1,   -1,  189,   11,   -1,   -1,  265,
   15,  257,  258,   -1,   19,   -1,   -1,  256,  257,  258,
  204,   -1,   -1,  256,  257,  258,  265,  260,  261,  262,
  263,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   53,   49,
   -1,   22,  264,  257,  258,   -1,  260,  261,  262,  263,
  244,  256,  257,  258,   64,   70,  256,  257,  258,   -1,
   41,   42,   77,   -1,   79,  257,  258,  259,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,  107,  108,   -1,
   -1,  257,  258,   -1,  257,  258,   87,  257,  258,   90,
   -1,   -1,   -1,  123,   -1,   -1,   -1,   -1,   99,  129,
   -1,   -1,   -1,   -1,  105,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  166,   -1,   -1,  203,   -1,
   -1,   -1,  202,   -1,   -1,   -1,   -1,  178,  213,
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
"$accept : program",
"program : program_name program_body",
"program : program_name statement_list",
"$$1 :",
"program : $$1 program_body",
"$$2 :",
"program : error $$2 program_name program_body",
"program : error EOF",
"program : EOF",
"program_name : ID",
"program_body : '{' statement_list '}'",
"program_body : '{' statement_list close_brace_list",
"$$3 :",
"program_body : open_brace_list $$3 statement_list '}'",
"program_body : '{' '}'",
"program_body :",
"program_body : '{' error '}'",
"open_brace_list : '{' '{'",
"open_brace_list : open_brace_list '{'",
"close_brace_list : '}' '}'",
"close_brace_list : close_brace_list '}'",
"statement_list : statement",
"statement_list : statement_list statement",
"statement_list : error ';'",
"statement : executable_statement",
"statement : declarative_statement",
"declarative_statement : declaration_of_variables",
"declarative_statement : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"cuerpo_ejecutable : executable_statement",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : executable_statement",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables executable_statement",
"executable_statement : asignacion_simple",
"executable_statement : inline_function_invocation",
"executable_statement : multiple_assignment",
"executable_statement : sentencia_control",
"executable_statement : sentencia_retorno",
"executable_statement : impresion",
"executable_statement : lambda",
"sentencia_control : if",
"sentencia_control : do_while",
"declaration_of_variables : UINT list_of_identifiers ';'",
"declaration_of_variables : UINT list_of_identifiers error",
"declaration_of_variables : UINT identifier DASIG constant ';'",
"declaration_of_variables : UINT error",
"list_of_identifiers : identifier",
"list_of_identifiers : list_of_identifiers ',' identifier",
"list_of_identifiers : list_of_identifiers ID",
"identifier : ID",
"asignacion_simple : variable DASIG expression ';'",
"asignacion_simple : variable DASIG expression error",
"asignacion_simple : variable expression ';'",
"asignacion_simple : variable DASIG error",
"multiple_assignment : list_of_variables list_of_constants ';'",
"multiple_assignment : list_of_variables list_of_constants error",
"list_of_variables : variable '='",
"list_of_variables : variable ',' list_of_variables",
"list_of_variables : variable error list_of_variables",
"list_of_constants : constant",
"list_of_constants : list_of_constants ',' constant",
"list_of_constants : list_of_constants constant",
"expression : term",
"expression : expression operador_suma term",
"expression : expression operador_suma error",
"expression : expression term_simple",
"expression : '+' term",
"operador_suma : '+'",
"operador_suma : '-'",
"term : term operador_multiplicacion factor",
"term : factor",
"term : term operador_multiplicacion error",
"term : operador_multiplicacion factor",
"term_simple : term_simple operador_multiplicacion factor",
"term_simple : factor_simple",
"term_simple : term_simple operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constant",
"factor : invocacion_funcion",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : invocacion_funcion",
"constant : CTE",
"constant : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"condicion : '(' cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' cuerpo_condicion error",
"cuerpo_condicion : expression comparador expression",
"cuerpo_condicion : expression term_simple",
"cuerpo_condicion : expression operador_suma term",
"cuerpo_condicion : term",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : if_start cuerpo_if",
"if : IF error",
"$$4 :",
"if_start : IF $$4 condicion",
"cuerpo_if : cuerpo_ejecutable rama_else ENDIF ';'",
"cuerpo_if : cuerpo_ejecutable rama_else ENDIF error",
"cuerpo_if : cuerpo_ejecutable rama_else error",
"cuerpo_if : cuerpo_ejecutable rama_else ';'",
"cuerpo_if : rama_else ENDIF ';'",
"rama_else :",
"rama_else : else_start cuerpo_ejecutable",
"rama_else : else_start",
"else_start : ELSE",
"do_while : do_while_start cuerpo_iteracion ';'",
"do_while : do_while_start cuerpo_iteracion error",
"do_while : do_while_start error",
"do_while_start : DO",
"cuerpo_iteracion : cuerpo_ejecutable fin_cuerpo_iteracion",
"cuerpo_iteracion : fin_cuerpo_iteracion",
"cuerpo_iteracion : cuerpo_ejecutable condicion",
"fin_cuerpo_iteracion : WHILE condicion",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' function_body '}'",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' '}'",
"inicio_funcion : UINT ID",
"inicio_funcion : UINT",
"function_body : statement",
"function_body : function_body statement",
"conjunto_parametros : '(' lista_parametros ')'",
"conjunto_parametros : '(' ')'",
"lista_parametros : parametro_formal",
"lista_parametros : lista_parametros ',' parametro_formal",
"lista_parametros : parametro_vacio",
"parametro_vacio : lista_parametros ','",
"parametro_vacio : ',' parametro_formal",
"parametro_formal : semantica_pasaje UINT ID",
"parametro_formal : semantica_pasaje UINT",
"parametro_formal : semantica_pasaje ID",
"semantica_pasaje :",
"semantica_pasaje : CVR",
"semantica_pasaje : error",
"sentencia_retorno : RETURN '(' expression ')' ';'",
"sentencia_retorno : RETURN '(' expression ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
"sentencia_retorno : RETURN expression ';'",
"sentencia_retorno : RETURN error",
"inline_function_invocation : invocacion_funcion ';'",
"inline_function_invocation : invocacion_funcion error",
"invocacion_funcion : function_start '(' lista_argumentos ')'",
"function_start : variable",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expression FLECHA ID",
"argumento : expression",
"impresion : PRINT imprimible ';'",
"impresion : PRINT imprimible error",
"imprimible : '(' elemento_imprimible ')'",
"imprimible : '(' ')'",
"imprimible : elemento_imprimible",
"imprimible :",
"elemento_imprimible : STR",
"elemento_imprimible : expression",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"argumento_lambda : '(' factor ')'",
"argumento_lambda : '(' ')'",
"argumento_lambda :",
"parametro_lambda : '(' UINT identifier ')'",
};

//#line 1216 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (Segmento Ocional)
// ====================================================================================================================

// --------------------------------------------------------------------------------------------------------------------

private static final boolean printDetections = false;

// --------------------------------------------------------------------------------------------------------------------

private final Lexer lexer;
private boolean errorState;
private final Monitor monitor;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private final ReturnsController returnsController;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.scopeStack = new ScopeStack();
    this.monitor = Monitor.getInstance();
    this.symbolTable = SymbolTable.getInstance();
    this.returnsController = new ReturnsController();
    this.reversePolish = ReversePolish.getInstance();

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

public ReversePolish getReversePolish() {
    return this.reversePolish;
}

// --------------------------------------------------------------------------------------------------------------------

// Método yylex() invocado durante yyparse().
private int yylex() {

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra con un token error.
 *
 * En caso de que el error sea tratado en la gramática, este será remplazado
 * posteriormente por un mensaje de error más apropiado.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
private void yyerror(String s) {
    notifyError(s);
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyDetection(String message) {

    if (printDetections) {

        Printer.printWrapped(String.format(
            "DETECCIÓN SINTÁCTICA: Línea %d: %s",
            monitor.getLineNumber(), message
        ));
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    monitor.addWarning(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        monitor.getLineNumber(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    monitor.addError(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    monitor.replaceLastErrorWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean statementAppearsInValidState() {

    return !this.returnsController.isThereReturnInDeclaration()
        && !this.returnsController.isThereReturnInSection() && !errorState;
}

// --------------------------------------------------------------------------------------------------------------------

private void treatInvalidState(String statementName) {

    if (this.returnsController.isThereReturnInDeclaration() || this.returnsController.isThereReturnInSection()) {
        this.showOmittedStatementNotification(statementName);
    }

    // De haber un error, no se trata acá, sino que se levanta
    // hasta el nivel de sentencia. Esto se realiza de esta forma
    // porque, de haber un error dentro de un if, se quiere invalidar
    // toda la estructura de control completa y no, solamente, la 
    // sentencia.
}

// --------------------------------------------------------------------------------------------------------------------

private void showOmittedStatementNotification(String statementName) {
    notifyWarning(statementName + " no alcanzable. No se ejecutará.");
}

// --------------------------------------------------------------------------------------------------------------------

private void treatErrorState() {

    if (!errorState) {
        this.reversePolish.recordSafeState();
    } else {
        this.recoverFromErrorState();
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void recoverFromErrorState() {

    this.reversePolish.emptyTemporalPolishes();
    this.reversePolish.returnToLastSafeState();
    this.errorState = false;
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 846 "Parser.java"
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
//#line 79 "gramatica.y"
{
            if (!this.errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(1).sval));
            } else {
                this.errorState = false;
            }
        }
break;
case 2:
//#line 91 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 94 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 96 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 99 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 102 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 109 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.recordSafeState();
        }
break;
case 11:
//#line 125 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 128 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 131 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 133 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 135 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 21:
//#line 156 "gramatica.y"
{ this.treatErrorState(); }
break;
case 22:
//#line 158 "gramatica.y"
{ this.treatErrorState(); }
break;
case 23:
//#line 163 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 206 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 45:
//#line 241 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {

                if (val_peek(1).sval.split("\\s*,\\s*").length == 1) {
                    notifyDetection("Declaración de variable.");
                } else {
                    notifyDetection("Declaración de variables.");
                }
            } else {
                this.treatInvalidState("declaración de variables");
            }
        }
break;
case 46:
//#line 256 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 47:
//#line 260 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 48:
//#line 264 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 50:
//#line 274 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 51:
//#line 279 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes. Si esta no se agregase, de entrar*/
            /* nuevamente a esta regla, la separación de las variables no funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 52:
//#line 301 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 53:
//#line 315 "gramatica.y"
{ 

            if (this.statementAppearsInValidState()) {
                
                notifyDetection("Asignación simple.");

                /* El valor aún no debe calcularse.*/
                /* this.symbolTable.setValue($1, $3);*/

                reversePolish.addPolish(val_peek(3).sval);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish(val_peek(2).sval);
            } else {

                this.treatInvalidState("asignación simple");

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                this.symbolTable.removeEntry(val_peek(3).sval);
                this.symbolTable.removeEntry(val_peek(1).sval);
            }
        }
break;
case 54:
//#line 343 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 55:
//#line 346 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 56:
//#line 349 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 57:
//#line 359 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
                String[] variables = val_peek(2).sval.split("\\s*,\\s*");
                String[] constants = val_peek(1).sval.split("\\s*,\\s*");

                if (variables.length > constants.length) {

                    notifyError(String.format(
                            "El número de variables (%d) del lado izquierdo de la asignación "
                            + "no puede superar el número de constantes (%d) en el lado derecho.",
                            variables.length, constants.length));

                } else {
                
                    if (variables.length < constants.length) {

                        notifyWarning(String.format(
                                "El número de variables (%d) en el lado izquierdo de la asignación "
                                + "es menor al número de constantes (%d) en el lado derecho de esta. "
                                + "Las constantes sobrantes serán descartadas.",
                                variables.length, constants.length));
                    }

                    /* En este punto, la lista de variables y constantes tendrá la misma longitud.*/
                    for (int i = 0; i < variables.length; i++) {
                        
                        String variable = variables[i];
                        String constant = constants[i];   

                        /* La asignación del valor no se realiza acá. Eso se hace en el assembler.*/
                        /* this.symbolTable.setValue(variable, constant);*/

                        reversePolish.addPolish(variable);
                        reversePolish.addPolish(constant);
                        /* Se agrega un DASIG ya que son varias asignaciones simples.*/
                        reversePolish.addPolish(":=");
                    }

                    notifyDetection("Asignación múltiple.");
                }
            } else {
                this.treatInvalidState("asignación múltiple");
            }
        }
break;
case 58:
//#line 408 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 60:
//#line 417 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 61:
//#line 422 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(2).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval;
        }
break;
case 63:
//#line 445 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 64:
//#line 450 "gramatica.y"
{
            String[] constants = val_peek(1).sval.split("\\s*,\\s*");
            String lastConstant = constants[constants.length - 1];

            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                lastConstant, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las constantes no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 66:
//#line 473 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 67:
//#line 481 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 68:
//#line 485 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 69:
//#line 492 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 502 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 71:
//#line 504 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 72:
//#line 511 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 

            TypeChecker.checkDivisionByZero(val_peek(1).sval, val_peek(0).sval);
        }
break;
case 74:
//#line 522 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 75:
//#line 529 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 76:
//#line 536 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;

            TypeChecker.checkDivisionByZero(val_peek(1).sval, val_peek(0).sval);
        }
break;
case 78:
//#line 547 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 79:
//#line 554 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 80:
//#line 556 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 81:
//#line 564 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 82:
//#line 568 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 84:
//#line 579 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 583 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 88:
//#line 594 "gramatica.y"
{
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            }

            yyval.sval = '-' + val_peek(0).sval;

            this.symbolTable.addEntry(SymbolDirector.getNegativeVersion(this.symbolTable.getSymbol(val_peek(0).sval)));
            this.symbolTable.removeEntry(val_peek(0).sval);
        }
break;
case 89:
//#line 613 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope(val_peek(0).sval))) {
                /* De entrar acá, la variable debe ser local.*/
                errorState = true;
                notifyError(String.format("Variable %s no declarada.", val_peek(0).sval));
            } else {
                /* A la entrada sin el scope, se le agrega el scope.*/
                /* Se combina con otra entrada en caso de coincidir el scope.*/
                this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
                yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
            }
        }
break;
case 90:
//#line 626 "gramatica.y"
{ 
            String scopedVariable = val_peek(0).sval + this.scopeStack.getScopeRoad(val_peek(2).sval);

            if (!this.scopeStack.isReacheable(val_peek(2).sval)) {
                errorState = true;
                notifyError(String.format("Variable %s no declarada (no visible).",val_peek(0).sval));
            } else if (!this.symbolTable.entryExists(scopedVariable)) {
                errorState = true;
                notifyError(String.format("Variable '%s' no declarada en el ámbito '%s'.",val_peek(0).sval,val_peek(2).sval));
            }

            yyval.sval = scopedVariable;

            /* Se remplaza el identificador sin ámbito por su versión con ámbito.*/
            this.symbolTable.replaceEntry(val_peek(0).sval, yyval.sval); 
        }
break;
case 91:
//#line 650 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 92:
//#line 661 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 93:
//#line 665 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 94:
//#line 667 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 674 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            this.reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 96:
//#line 682 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 97:
//#line 684 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 686 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 693 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 100:
//#line 697 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 105:
//#line 708 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 106:
//#line 717 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {
                this.reversePolish.closeSelection();
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia 'if'."); 
            } else {
                System.out.println("Acá me rompo");
                System.out.println(this.errorState);
                this.treatInvalidState("Sentencia 'if'");
                this.reversePolish.discardSelection(); 
            }

            this.returnsController.notifySelectionEnd();
        }
break;
case 107:
//#line 736 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 108:
//#line 742 "gramatica.y"
{ reversePolish.addPolish("open-selection"); }
break;
case 109:
//#line 743 "gramatica.y"
{
            if (!this.returnsController.isThereReturnInSection()) {
                this.reversePolish.addSeparation("Entering 'if' body...");
                this.reversePolish.openSelection();
                this.returnsController.notifySelectionStart();
            }
        }
break;
case 111:
//#line 760 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 112:
//#line 762 "gramatica.y"
{ replaceLastErrorWith("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 113:
//#line 764 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 114:
//#line 766 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 115:
//#line 773 "gramatica.y"
{ this.returnsController.notifyEmptyElse(); }
break;
case 116:
//#line 775 "gramatica.y"
{ this.returnsController.notifyAlternativeEnd(); }
break;
case 117:
//#line 780 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 118:
//#line 788 "gramatica.y"
{
            this.reversePolish.openAlternative();
            this.returnsController.notifyAlternativeStart();
            this.reversePolish.addSeparation("Entering 'else' body...");
        }
break;
case 119:
//#line 801 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.closeLoop();
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            } else {
                this.treatInvalidState("Sentencia 'do-while'");
            }
        }
break;
case 120:
//#line 814 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 121:
//#line 816 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 122:
//#line 824 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.openLoop();
        }
break;
case 124:
//#line 838 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 125:
//#line 840 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 127:
//#line 855 "gramatica.y"
{
            if (!this.errorState) {

                if (this.returnsController.isThereReturnInDeclaration()) {

                    notifyDetection("Declaración de función.");
                    this.scopeStack.pop();
                    this.reversePolish.closeFunctionDeclaration(this.scopeStack.appendScope(val_peek(4).sval));
                    this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(4).sval));
                } else {
                    notifyError("La función necesita, en todos los casos, retornar un valor.");
                    this.errorState = true;
                }
            } else {
                this.treatInvalidState("Declaración de función");
            }

            this.returnsController.notifyEndOfFunctionDeclaration();
        }
break;
case 128:
//#line 878 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;

            this.returnsController.notifyEndOfFunctionDeclaration();
        }
break;
case 129:
//#line 893 "gramatica.y"
{
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.startFunctionDeclaration(val_peek(0).sval + ":" + this.scopeStack.asText());
            SymbolTable.getInstance().removeEntry(val_peek(0).sval);
            SymbolTable.getInstance().addEntry(SymbolDirector.createNewFunction(val_peek(0).sval + ":" + this.scopeStack.asText()));

            yyval.sval = val_peek(0).sval;
            this.scopeStack.push(val_peek(0).sval);

            this.returnsController.notifyStartOfFunctionDeclaration();
        }
break;
case 130:
//#line 908 "gramatica.y"
{
            errorState = true;
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");

            this.returnsController.notifyStartOfFunctionDeclaration();
        }
break;
case 134:
//#line 934 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 137:
//#line 946 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 140:
//#line 960 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());

                this.reversePolish.addParameter(val_peek(0).sval, "uint", val_peek(2).sval);
            } else {
                this.treatInvalidState("Parámetro formal");
            }
        }
break;
case 141:
//#line 975 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 142:
//#line 977 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 143:
//#line 984 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 144:
//#line 986 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 145:
//#line 991 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 146:
//#line 1000 "gramatica.y"
{

            if (statementAppearsInValidState()) {

                if (this.returnsController.insideFunction()) {

                    this.reversePolish.makeTemporalPolishesDefinitive();
                    reversePolish.addPolish("return");
                    notifyDetection("Sentencia 'return'.");

                    this.returnsController.notifyReturn();
                    /*if(!this.returnsController.notifyReturn())*/
                        /*notifyError("Solo se permite una sentencia return por sección.");*/
                } else {
                    notifyError("La sentencia 'return' no está permitida fuera de la declaración de una función.");
                    this.errorState = true;
                }
            } else {

                this.reversePolish.emptyTemporalPolishes();

                this.treatInvalidState("return");
            }
        }
break;
case 147:
//#line 1028 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 148:
//#line 1030 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 149:
//#line 1032 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 150:
//#line 1034 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 151:
//#line 1043 "gramatica.y"
{ notifyDetection("Invocación de función."); this.reversePolish.makeTemporalPolishesDefinitive(); }
break;
case 152:
//#line 1048 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 153:
//#line 1055 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {

                this.reversePolish.closeFunctionCall();
            } else {
                this.treatInvalidState("Invocación de función");

                this.reversePolish.discardFunctionCall();
            }
        }
break;
case 154:
//#line 1071 "gramatica.y"
{ this.reversePolish.startFunctionCall(val_peek(0).sval); }
break;
case 156:
//#line 1079 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 157:
//#line 1086 "gramatica.y"
{
            this.reversePolish.addArgument(val_peek(0).sval);
        }
break;
case 158:
//#line 1093 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
break;
case 159:
//#line 1102 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                /* Se añaden las polacas correspondiente al imprimible.*/
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                this.reversePolish.emptyTemporalPolishes();
                this.treatInvalidState("Sentencia 'print'");
            }
        }
break;
case 160:
//#line 1117 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 162:
//#line 1132 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 163:
//#line 1134 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 164:
//#line 1140 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 165:
//#line 1147 "gramatica.y"
{ reversePolish.addTemporalPolish(val_peek(0).sval); }
break;
case 167:
//#line 1157 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {

                /* Se llena el punto de agregación reservado con la asignación*/
                /* del argumento al parámetro.*/
                this.reversePolish.fillLastAggregatePoint(val_peek(3).sval, val_peek(1).sval, ":=");

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");

            } else {
                this.treatInvalidState("Expresión 'lambda'");
            }
        }
break;
case 168:
//#line 1175 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 169:
//#line 1178 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 170:
//#line 1180 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 171:
//#line 1182 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 172:
//#line 1189 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 173:
//#line 1194 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 174:
//#line 1197 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 175:
//#line 1204 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
        }
break;
//#line 1859 "Parser.java"
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
