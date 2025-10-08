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
   40,   40,   33,   33,   33,   33,   41,   42,   42,    9,
    9,    9,    1,    1,    1,    1,    6,    6,    2,    2,
    2,    4,    4,    4,    7,    7,    3,    3,    3,    5,
    5,    5,   11,   11,   10,   10,   43,   43,   43,   43,
   43,   44,   44,   45,   45,   45,   45,   45,   45,   45,
   38,   38,   38,   38,   38,   46,   46,   39,   39,   39,
   47,   48,   48,   48,   49,   27,   27,   52,   52,   51,
   50,   50,   53,   53,   53,   55,   55,   54,   54,   54,
   56,   56,   56,   35,   35,   35,   35,   35,   12,   13,
   13,   14,   14,   36,   36,   58,   58,   58,   58,   57,
   59,   59,   37,   37,   62,   62,   62,   62,   61,   60,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    3,    4,    3,    2,
    4,    4,    2,    4,    2,    4,    3,    5,    1,    1,
    3,    2,    1,    3,    3,    2,    1,    1,    3,    1,
    3,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    3,    2,    2,    2,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    6,    6,    5,    5,    2,    0,    2,    3,    3,    2,
    2,    1,    1,    2,    2,    6,    5,    1,    2,    3,
    1,    0,    1,    3,    1,    2,    2,    3,    2,    2,
    0,    1,    1,    5,    5,    4,    3,    2,    4,    1,
    3,    3,    1,    3,    3,    1,    2,    1,    0,    3,
    1,    1,    4,    4,    1,    2,    1,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   37,   38,   39,   40,
   41,   42,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   93,  161,    0,    0,    0,    0,   80,   87,
   88,   89,    0,    0,    0,  115,    0,    0,    0,    0,
   50,    0,    0,    0,    0,  120,    0,    0,   30,    0,
   31,    0,    0,    0,  148,    0,    0,    0,   17,   14,
    0,    0,    0,    0,    0,   69,    0,    0,   43,   36,
   22,   18,    0,   29,   27,   65,   63,    0,    0,   96,
    0,    0,  150,   94,    0,    0,   91,   77,   78,    0,
   83,    0,   90,   92,   85,   86,    0,  154,  155,   99,
    0,  106,  108,  107,  109,  110,  104,  105,    0,    0,
  100,   98,   48,   55,   47,    0,    0,  143,  142,    0,
    0,    0,  133,  135,    0,   49,   54,   46,    0,    0,
  125,   33,   34,    0,    0,  121,  118,  119,    0,    0,
  147,    0,    0,    0,    0,    0,    0,    0,    0,   57,
    0,    0,   59,   67,    0,    0,   70,    0,    0,    0,
    0,    0,    0,  149,  160,    0,   75,    0,   81,   79,
  101,   97,    0,    0,    0,   52,    0,  137,    0,    0,
    0,  140,   53,    0,   32,   35,  146,    0,   19,   20,
  170,   58,   56,    0,    0,   60,    0,   13,   66,   64,
    0,   72,    0,    0,  163,  164,  152,  151,   84,   82,
  117,  114,    0,  113,    0,    0,  128,  127,  134,  138,
   51,  145,  144,    0,    0,    0,   71,  169,  112,  111,
    0,  126,  129,    0,   61,   62,   68,  130,
};
final static short yydgoto[] = {                          3,
   58,   48,   49,  110,  111,  112,  117,   64,  176,   50,
   51,   52,  102,  103,   18,   19,  254,   39,    4,  164,
   21,   93,   22,   23,   24,   25,   26,   95,   70,   71,
  154,   27,   28,   29,   30,   31,   32,   33,   34,  170,
   35,   88,   59,   60,  129,  195,   72,   73,   74,  141,
  237,  238,  142,  143,  144,  145,   53,   54,   55,   36,
  180,  181,
};
final static short yysindex[] = {                      -184,
    2,  -22,    0, -101,    0,    0,  -94,   28,  134,  140,
   43,   24,  220,  -34, -237,  154,  -38,    0,    0,  105,
  -85,    0,    0,    0,    0,    4,    0,    0,    0,    0,
    0,    0,    0,    0,   79,  -56,    0,    0,    0,    0,
 -179,  244,    0,    0, -173,  231,  275,   23,    0,    0,
    0,    0,   20, -162,    0,    0,  322,  262,  -11,  -37,
    0,  288,   -9,  311, -169,    0,  -15,  116,    0,  101,
    0,   41, -149,    0,    0,  324,  298,  -16,    0,    0,
   47, -139,  244,  244, -133,    0,  420,   53,    0,    0,
    0,    0,   59,    0,    0,    0,    0,   53,   37,    0,
   88,   62,    0,    0,    0,   84,    0,    0,    0,   23,
    0,  238,    0,    0,    0,    0,  256,    0,    0,    0,
  -25,    0,    0,    0,    0,    0,    0,    0,  244, -136,
    0,    0,    0,    0,    0, -121,   -9,    0,    0, -212,
   98,   96,    0,    0, -225,    0,    0,    0, -113,   53,
    0,    0,    0,  128,    0,    0,    0,    0,   92,  -32,
    0,    0,   18,   29,  112,  423,  426,   23,  -98,    0,
  109,   -1,    0,    0,   70,  131,    0,  335,    0,  100,
  -96,  -95,  244,    0,    0, -128,    0,   23,    0,    0,
    0,    0,  275,  -11,  -39,    0,  123,    0,   42, -212,
 -133,    0,    0,  107,    0,    0,    0,  -35,    0,    0,
    0,    0,    0,  238,  256,    0,   53,    0,    0,    0,
   53,    0,    0,  126,    0,    0,    0,    0,    0,    0,
    0,    0,  122,    0,   48,   36,    0,    0,    0,    0,
    0,    0,    0,   -5,  -83,  133,    0,    0,    0,    0,
   59,    0,    0,   82,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        27,
    0,  182,    0,  182,    0,    0,    0,  362,  -73,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  184,
   94,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -7,  372,    0,    0,
    0,    0,  -70,    0, -149,    0,    0,    8,    0,    0,
    0,  -77,  -33,    0,    0,    0,    0,    0,    0,    0,
    0,  -63,    0,  -54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -53,    0,
   64,    0,    0,    0,  -51,    0,    0,    0,    0,  384,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  271,
    0,    0,    0,    0,    0,    0,  -33,    0,    0, -201,
    0,  150,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -50,    0,    0,    0,    0,    0,
    0,   12,  210,  211,    0,    0,    0,  438,  405,    0,
   14,    0,    0,    0,    0,    0,    0,    0,  -44,  -43,
    0,    0,    0,    0,    0,    0,    0,  394,    0,    0,
    0,    0,   10,    0,    0,    0,    0,    0,    0,  -41,
   72,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -42,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  441,  415,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  651,  -65,  302,    0,   91,  142,  -71,    0,    0,    3,
  342,   34,    0,  129,  270,  334,   26,    0,    0,    0,
    0,    0,   33,   -2,    0,    0,    0,    0,  -52,  345,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  212,   50,  351,    0,    0,    0,    0,  329,  277,
  185,    0,    0, -114,    0,    0,    0,    0,  380,    0,
    0,    0,
};
final static int YYTABLESIZE=834;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        136,
   28,    6,  136,  132,   16,   15,  130,  132,  208,   69,
  108,   16,  109,   65,   16,  192,   16,   15,  168,  234,
   90,   14,   16,  243,   57,  198,    4,   20,   15,   45,
   40,  171,   82,  162,  140,   17,  116,   92,  186,   81,
   28,  115,   85,  138,  201,   17,  188,   17,  103,  113,
  102,   23,   91,   17,   95,  141,   69,   95,  139,   86,
  113,   16,   94,   15,  116,  153,   68,   42,  141,  115,
   16,    1,    2,   41,   95,   15,  178,  100,  118,  113,
  114,   45,   63,   16,  104,  239,   15,  172,   79,  113,
   80,  114,   17,  119,  150,   16,  215,   45,   15,  157,
   14,   17,  184,  113,  153,  183,  158,  153,  162,   15,
  114,   68,  139,   91,   17,  139,  151,  165,  175,  155,
  114,   15,   98,  171,  185,   28,   17,  229,    8,  107,
  108,  194,  109,   12,  114,  196,   23,   97,  199,  200,
   57,  231,  209,  203,   15,   45,   68,  202,  244,    4,
  207,  206,  211,  210,   41,   15,   16,  216,  225,  226,
  253,  227,  113,  235,  236,  241,  248,   15,  113,  113,
  251,  163,  256,   46,  221,   45,  257,   16,   45,   57,
  250,   15,  159,    3,   45,  156,   95,   17,  113,  220,
  131,   69,  122,  114,  218,  113,   16,   85,   45,  114,
  114,  123,  168,  240,  157,  124,  258,   91,   17,   10,
   11,  167,  165,  166,   86,  141,  232,   89,  131,  114,
  242,   78,    8,  141,    8,  107,  114,   17,  141,  233,
  191,    9,   10,    7,    8,   11,  141,   12,   16,   13,
  152,    8,   43,    9,   10,    8,  138,   11,  162,   12,
  255,   13,  205,   16,    9,   10,   16,   28,    5,   76,
   12,  139,   13,  103,   45,  102,   28,   28,   23,   17,
   28,  105,   28,   37,   28,   45,  230,   23,   23,   66,
    8,   23,   45,   23,   17,   23,   91,   17,   45,    9,
   10,    7,    8,    8,   43,   12,   67,   13,   61,   62,
   45,    9,   10,    8,  108,   11,  109,   12,  214,   13,
   43,  228,    9,   10,    7,    8,   11,  108,   12,  109,
   13,  128,  126,  127,    9,   10,    8,  137,   11,  116,
   12,  136,   13,   41,   96,    9,   10,   38,    8,   11,
  108,   12,  109,   13,    8,  107,  135,    9,   10,   12,
   12,   11,  182,   12,  149,   13,  161,    8,   43,   12,
   12,    8,  120,   12,  159,   12,   45,   12,   45,  148,
    9,   10,    8,   67,   11,  223,   12,  249,   13,   45,
   99,    9,   10,  217,    8,  169,  219,   12,   43,   13,
    8,   43,   44,    9,   10,   56,    8,   43,  156,   12,
  179,   13,   95,   95,   95,   95,   95,  121,   95,   83,
    8,   43,   73,  197,   73,   73,   73,   84,  190,  252,
   95,   95,   95,   95,   76,  106,   76,   76,   76,  174,
   73,   73,   73,   73,   74,    0,   74,   74,   74,  177,
    0,    0,   76,   76,   76,   76,   80,   80,    0,   80,
    0,   80,   74,   74,   74,   74,   79,   79,    0,   79,
    0,   79,  108,   80,  109,  108,    0,  109,  108,    0,
  109,    0,    0,   79,    0,   75,    8,   43,  173,  224,
   73,  212,   73,   74,  213,   74,    0,    8,   43,   44,
    0,  204,    0,  187,    8,   43,   73,    0,    0,   74,
    8,   43,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  189,    8,   43,    0,    0,  245,  222,    8,  107,
    0,  122,  123,  124,  125,    0,  116,    0,    0,    0,
    0,    8,  107,    0,    0,    0,    0,    0,    0,  116,
    0,    0,    0,  133,  134,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    8,  107,    0,    0,  246,    0,
    0,    0,  247,    0,    0,    0,  146,  147,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    8,   43,
    8,   43,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    8,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   95,   95,   95,
    0,   95,   95,   95,   95,   95,   95,   73,   73,   73,
    0,   73,   73,   73,   73,    0,   73,    0,    0,   76,
   76,   76,    0,   76,   76,   76,   76,    0,   76,   74,
   74,   74,    0,   74,   74,   74,   74,    0,   74,   47,
    0,   80,   80,   77,    0,    0,   87,    0,    0,    0,
    0,   79,   79,    0,    0,    0,    8,  107,    0,    8,
  107,    0,    8,  107,    0,    0,    0,    0,    0,    0,
    0,    0,  101,    0,   73,   73,   47,   74,   74,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  160,    0,    0,    0,
    0,    0,    0,  166,  167,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  193,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,   44,   41,    2,   40,   59,   41,   41,   12,
   43,    0,   45,   11,   12,   41,   14,   40,   84,   59,
   59,  123,   20,   59,   40,  140,    0,    2,   40,   45,
  125,  257,  270,   41,   44,    2,   42,  123,  110,   14,
   40,   47,   44,  256,  270,   12,  112,   14,   41,   47,
   41,   40,   20,   20,   41,  257,   59,   44,  271,   61,
   58,   59,   59,   40,   42,   68,  123,   40,  270,   47,
   68,  256,  257,   46,   61,   40,   40,  257,   59,   77,
   47,   45,   40,   81,  258,  200,   40,   85,  123,   87,
  125,   58,   59,  256,  264,   93,  168,   45,   40,   59,
  123,   68,   41,  101,   41,   44,  256,   44,  125,   40,
   77,  123,   41,   81,   81,   44,   67,  257,   93,   70,
   87,   40,   44,  257,   41,  125,   93,  256,  257,  258,
   43,  268,   45,   40,  101,  257,  125,   59,   41,   44,
   40,  194,  125,  257,   40,   45,  123,  145,  214,  123,
   59,  154,   41,  125,   46,   40,  154,  256,   59,  256,
  125,  257,  160,   41,  123,   59,   41,   40,  166,  167,
  123,  125,  256,   40,   44,   45,   44,  175,   45,   40,
   59,    0,  256,    0,   45,  256,  264,  154,  186,   59,
   41,  194,  256,  160,  125,  193,  194,   44,   45,  166,
  167,  256,  256,  201,  256,  256,  125,  175,  175,    0,
    0,  256,  256,  256,   61,  257,  256,  256,  256,  186,
  256,  256,  257,  257,  257,  258,  193,  194,  270,  269,
  256,  266,  267,  256,  257,  270,  270,  272,  236,  274,
  125,  257,  258,  266,  267,  257,  256,  270,  256,  272,
  256,  274,  125,  251,  266,  267,  254,  257,  257,   40,
  272,  271,  274,  256,   45,  256,  266,  267,  257,  236,
  270,   41,  272,    4,  274,   45,  186,  266,  267,  256,
  257,  270,   45,  272,  251,  274,  254,  254,   45,  266,
  267,  256,  257,  257,  258,  272,  273,  274,  256,  257,
   45,  266,  267,  257,   43,  270,   45,  272,  167,  274,
  258,  183,  266,  267,  256,  257,  270,   43,  272,   45,
  274,   60,   61,   62,  266,  267,  257,   40,  270,   59,
  272,   44,  274,   46,  256,  266,  267,    4,  257,  270,
   43,  272,   45,  274,  257,  258,   59,  266,  267,  256,
  257,  270,  265,  272,   44,  274,   59,  257,  258,  266,
  267,  257,   41,  270,   41,  272,   45,  274,   45,   59,
  266,  267,  257,  273,  270,   41,  272,  256,  274,   45,
   36,  266,  267,  172,  257,   84,  256,  272,  258,  274,
  257,  258,  259,  266,  267,  256,  257,  258,   70,  272,
   99,  274,   41,   42,   43,   44,   45,   57,   47,  256,
  257,  258,   41,  137,   43,   44,   45,  264,  117,  235,
   59,   60,   61,   62,   41,   46,   43,   44,   45,   88,
   59,   60,   61,   62,   41,   -1,   43,   44,   45,   98,
   -1,   -1,   59,   60,   61,   62,   42,   43,   -1,   45,
   -1,   47,   59,   60,   61,   62,   42,   43,   -1,   45,
   -1,   47,   43,   59,   45,   43,   -1,   45,   43,   -1,
   45,   -1,   -1,   59,   -1,  256,  257,  258,   59,  178,
   43,   59,   45,   43,   59,   45,   -1,  257,  258,  259,
   -1,  150,   -1,  256,  257,  258,   59,   -1,   -1,   59,
  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,   -1,   -1,  215,  176,  257,  258,
   -1,  260,  261,  262,  263,   -1,  256,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,  217,   -1,
   -1,   -1,  221,   -1,   -1,   -1,  256,  257,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,  264,  265,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,    9,
   -1,  257,  258,   13,   -1,   -1,   16,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,  257,  258,   -1,  257,
  258,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   42,   -1,  257,  258,   46,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   76,   -1,   -1,   -1,
   -1,   -1,   -1,   83,   84,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  129,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  183,
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
"asignacion_simple : variable DASIG expresion_o_termino",
"asignacion_simple : variable error expresion ';'",
"asignacion_simple : variable expresion ';'",
"expresion_o_termino : factor error",
"expresion_o_termino : expresion operador_suma termino error",
"expresion_o_termino : termino operador_multiplicacion factor error",
"asignacion_multiple : inicio_par_variable_constante ';'",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes ';'",
"asignacion_multiple : inicio_par_variable_constante error",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes error",
"inicio_par_variable_constante : variable par_variable_constante constante",
"par_variable_constante : ',' variable par_variable_constante constante ','",
"par_variable_constante : '='",
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
"argumento_lambda : argumento_lambda_admisible",
"argumento_lambda : '(' ')'",
"argumento_lambda : factor",
"argumento_lambda :",
"argumento_lambda_admisible : '(' factor ')'",
"parametro_lambda : '(' UINT ID ')'",
};

//#line 839 "gramatica.y"

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

public void altaSymbolTable(String lexema) {
    SymbolTable.getInstance().agregarEntrada(lexema);
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
//#line 75 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 82 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 85 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 6:
//#line 88 "gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 7:
//#line 91 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 11:
//#line 112 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 12:
//#line 115 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 14:
//#line 118 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 15:
//#line 120 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 16:
//#line 122 "gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 23:
//#line 148 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 27:
//#line 165 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 33:
//#line 192 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 206 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 217 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 233 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 236 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 241 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 245 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 249 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 253 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 263 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 268 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 275 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 289 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 294 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 297 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 300 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 325 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 327 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 332 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 334 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 71:
//#line 355 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 72:
//#line 360 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 74:
//#line 374 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 75:
//#line 379 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 386 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 77:
//#line 399 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 78:
//#line 401 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 79:
//#line 408 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 81:
//#line 414 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 82:
//#line 426 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 84:
//#line 432 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 85:
//#line 444 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 86:
//#line 446 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 93:
//#line 471 "gramatica.y"
{ altaSymbolTable(val_peek(0).sval); }
break;
case 94:
//#line 473 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;
            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0";
            }
            altaSymbolTable(yyval.sval);
        }
break;
case 96:
//#line 488 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 97:
//#line 497 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 98:
//#line 502 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 99:
//#line 505 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 100:
//#line 508 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 101:
//#line 511 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 103:
//#line 523 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 110:
//#line 539 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 111:
//#line 548 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 112:
//#line 553 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 113:
//#line 555 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 114:
//#line 557 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 115:
//#line 559 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 118:
//#line 575 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 119:
//#line 580 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 120:
//#line 582 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 123:
//#line 599 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 124:
//#line 601 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 127:
//#line 620 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 129:
//#line 632 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 132:
//#line 649 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 135:
//#line 661 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 139:
//#line 679 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 140:
//#line 681 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 143:
//#line 693 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 144:
//#line 702 "gramatica.y"
{ notifyDetection("Sentencia 'return'."); }
break;
case 145:
//#line 707 "gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 146:
//#line 709 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 147:
//#line 711 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 148:
//#line 713 "gramatica.y"
{ notifyError("Sentencia 'return' inválida."); }
break;
case 149:
//#line 722 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 151:
//#line 732 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 152:
//#line 739 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 153:
//#line 744 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 154:
//#line 753 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 155:
//#line 758 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 157:
//#line 769 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 158:
//#line 772 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 159:
//#line 774 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 163:
//#line 797 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 164:
//#line 802 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 166:
//#line 813 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 167:
//#line 816 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }
break;
case 168:
//#line 819 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1316 "Parser.java"
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
