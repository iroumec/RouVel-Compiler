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
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolDirector;
    import semantic.ReversePolish;
    import semantic.ReturnsController;
    import common.ParameterSemanticModel;
//#line 33 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 34 "Parser.java"




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
    0,    0,   28,    0,   29,    0,    0,    0,    1,   26,
   26,   32,   26,   26,   26,   26,   31,   31,   30,   30,
   27,   27,   27,   33,   33,   35,   35,   38,   38,   39,
   39,   40,   40,   41,   41,   34,   34,   34,   34,   34,
   34,   34,   44,   44,   36,   36,   36,   36,    7,    7,
    7,    6,   42,   42,   42,   42,    2,   25,   25,   23,
   23,   23,   24,   24,   24,   18,   18,   18,   18,   18,
   15,   15,   19,   19,   19,   19,   21,   21,   21,   16,
   16,   20,   20,   20,   22,   22,   22,    5,    5,    4,
    4,   50,   50,   50,   50,   51,   51,   51,   51,   17,
   17,   17,   17,   17,   17,   17,   48,   48,   54,   52,
   53,   53,   53,   53,   53,   55,   55,   55,   56,   49,
   49,   49,   57,   58,   58,   58,   59,   37,   37,    8,
    8,   61,   61,   60,   60,   62,   62,   62,   64,   64,
   63,   63,   63,   14,   14,   14,   45,   45,   45,   45,
   45,   43,   43,    9,    3,   12,   12,   13,   13,   46,
   46,   65,   65,   65,   65,   66,   66,   47,   47,   47,
   47,   47,   67,   11,   11,   11,   10,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    5,    2,    1,    3,
    2,    1,    4,    4,    3,    3,    1,    3,    3,    2,
    3,    3,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    1,    3,    2,    3,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    2,    1,
    3,    3,    2,    2,    3,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    2,    2,    0,    3,
    4,    4,    3,    3,    3,    0,    2,    1,    1,    3,
    3,    2,    1,    2,    1,    2,    2,    5,    4,    2,
    1,    1,    2,    3,    2,    1,    3,    1,    2,    2,
    3,    2,    2,    0,    1,    1,    5,    5,    4,    3,
    2,    2,    2,    4,    1,    1,    3,    3,    1,    3,
    3,    3,    2,    1,    0,    1,    1,    4,    4,    5,
    4,    5,    1,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  123,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   38,    1,    0,    0,   21,   24,
   25,   26,    0,   36,   37,   39,   40,   41,   42,   43,
   44,    0,    0,    4,    0,   23,    0,   88,  166,    0,
    0,   80,   81,    0,    0,   83,   84,    0,    0,    0,
   74,    0,  164,  108,    0,   48,    0,    0,    0,  151,
    0,    0,    0,   17,   14,    0,    0,    0,    0,    0,
    0,    0,   60,    0,    0,  153,  152,    0,   34,  173,
    0,    0,   63,    0,   22,   18,    0,   29,   27,  119,
    0,   30,    0,   31,  107,    0,    0,  122,    0,    0,
    0,  125,    6,   91,    0,   89,  163,    0,   76,   86,
   71,   72,    0,   87,    0,    0,   78,    0,  161,  160,
    0,    0,    0,  110,    0,    0,   46,   51,   45,    0,
    0,    0,  150,   16,    0,    0,    0,   56,    0,   55,
    0,  156,    0,    0,   62,   61,  146,  145,    0,  135,
    0,    0,  136,  138,    0,   33,    0,    0,    0,    0,
   35,    0,    0,   59,   58,    0,   65,    0,    0,    0,
    0,  117,  127,  126,  124,  121,  120,  162,   68,    0,
    0,   75,   73,   93,    0,  102,  104,  103,  105,  106,
  100,  101,    0,    0,    0,   94,    0,   52,   50,  149,
    0,   19,   20,  177,   54,   53,    0,  154,    0,  140,
  143,    0,    0,  134,  129,  132,    0,   32,    0,    0,
  175,    0,  171,  169,  168,   64,   13,  113,    0,  114,
  115,   79,   77,   95,   92,    0,    0,   47,  148,  147,
  157,  158,  141,  137,  128,  133,  170,  172,  174,  112,
  111,
};
final static short yydgoto[] = {                          4,
    5,   18,   19,   55,   56,   68,   69,   21,   57,   23,
  170,  151,  152,  161,  125,   58,  204,  132,   60,   61,
  126,  127,   24,   94,   25,   26,   27,    6,    8,  146,
   28,   97,   29,   30,   31,   32,   33,   99,  103,  104,
   91,   34,   35,   36,   37,   38,   39,   40,   41,  134,
  135,   42,  105,   65,  106,  107,   43,  111,  112,   85,
  227,  162,  163,  164,   62,   63,   92,
};
final static short yysindex[] = {                         3,
   17,    0,    0,    0,  -38,  -80,    0, -210,   -8,    7,
  500, -207,  -99,    0,  512,  -26, -211,  164,   38,   14,
   67,  -53,  -10,  -32,    0,    0,  130,  -58,    0,    0,
    0,    0,   23,    0,    0,    0,    0,    0,    0,    0,
    0,   33,   22,    0,  -80,    0, -142,    0,    0,  565,
 -136,    0,    0,  519,    0,    0,    0,   85,  457,   69,
    0,  -52,    0,    0,  525,    0,    0, -135,  -40,    0,
  538,  411,  -44,    0,    0,   51, -126,  551,  464,  557,
 -122, -122,    0,  -36,   18,    0,    0,  141,    0,    0,
  144,  103,    0,  -13,    0,    0,   63,    0,    0,    0,
  141,    0, -119,    0,    0, -116,  157,    0,  525,  -19,
  -23,    0,    0,    0,   69,    0,    0,  114,    0,    0,
    0,    0,    0,    0,  561,   69,    0,   25,    0,    0,
  545,  390,   69,    0,  131,  -32,    0,    0,    0,  -82,
  127,  120,    0,    0,   64,   71,  151,    0,  126,    0,
   54,    0,  281,   14,    0,    0,    0,    0, -246,    0,
 -215,   79,    0,    0,   74,    0,  160,  103,   93,  -51,
    0,   -6,   -3,    0,    0,  -32,    0,   88,  181,   35,
  149,    0,    0,    0,    0,    0,    0,    0,    0,   69,
  358,    0,    0,    0,  -29,    0,    0,    0,    0,    0,
    0,    0,  561,  557,   69,    0,  156,    0,    0,    0,
   30,    0,    0,    0,    0,    0,  557,    0,  -20,    0,
    0,    8, -246,    0,    0,    0,  100,    0,   31,   37,
    0,  209,    0,    0,    0,    0,    0,    0,   42,    0,
    0,    0,    0,    0,    0,   69,  457,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         9,
   57,    0,    0,    0,  332,  332,    0,    0,    0,  399,
   60,  531,  294,    0,    0,    0,    0,    0,    0,  148,
    0,    0,    0,    0,    0,    0,  336,  119,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,   78,    0,    0,  332,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  422,    0,    0,    0,   -7,  123,
    0,    0,    0,    0,    0,    0,   20,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -213,    0,    0,    0,    0,    0,    0,
   96,   66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   41,    0,    0,    0,   59,    0,    0,    0,
    0,    0,    0,    0,  444,    0,    0,    0,    0,    0,
    0,    0,  432,    0,    0,  268,    0,    0,    0,    0,
    0,    0,  467,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  349,  353,    0,    0,    0,    0,
    0,    0,  106,    0,    0,    0,    0,    0, -213,    0,
    0,    0,    0,    0,    0,    0,   96,   96,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  454,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  475,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  110,    4,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  489,   -1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  348,    0,    0,   -5,  490,  219,    0,    0,   45,    0,
  -59,    0,  147,    0,  233,  639,    0,  756,  736,   11,
  236,    0,   97,    0,    0,   21,   13,    0,    0,    0,
    0,    0,   36,  750,    0,    0,    0,    0,  -27,  338,
    5,    0,    0,    0,    0,    0,    0,    0,    0,  229,
  238,    0,    0,    0,  270,    0,    0,    0,  261,    0,
    0,    0, -139,    0,    0,  340,    0,
};
final static int YYTABLESIZE=973;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
   28,   17,    2,  140,  160,   87,  130,  159,    3,  157,
   20,  245,   51,   17,   46,  110,    7,   20,  139,  220,
  131,   20,   53,   50,  158,   51,   44,   52,   76,   17,
  176,   51,  173,  167,  231,  187,   20,   20,   51,   96,
   28,  221,   16,  144,  139,  175,    3,  139,   64,   22,
   46,  167,   47,  123,  222,  235,  144,   82,   77,  130,
   22,   17,   95,   52,   96,  113,  123,   22,  119,   51,
   20,   22,   17,  123,   83,  154,  154,   80,   52,  182,
  144,   98,   20,  254,   16,   20,   22,   22,  250,   49,
   17,   20,  167,  240,  218,   20,   74,  217,   75,  116,
  261,   20,   17,  124,   49,  179,   84,  229,  230,  178,
   53,   95,   88,   17,  114,   52,  124,  118,  165,  224,
   22,  116,  223,  124,  176,   28,  123,   17,  136,   51,
  147,    3,   22,  231,   10,   22,  123,   51,  193,   17,
  165,   22,  172,  123,  101,   22,  159,  123,  100,  159,
  142,   22,  181,  142,  188,  101,   66,   67,   12,   20,
  211,   20,  121,   66,  122,   66,   66,   66,  121,   17,
  122,  206,   20,   20,  208,  145,  124,  155,  156,  232,
   17,   66,  232,  169,  216,  210,  124,  155,  212,   57,
   57,  214,   57,  124,   57,  213,   17,  124,  225,  169,
  226,  243,   86,  129,  233,   53,   50,  241,   51,   22,
   52,   22,  237,   95,  248,  137,  138,    9,   10,  157,
   17,   20,   22,   22,  255,   48,  244,   11,   12,   73,
   10,   13,  186,   14,  158,   15,  252,   10,   48,   11,
   12,  123,  174,   13,   48,   14,   10,   15,  167,  259,
   10,   48,  234,  109,   96,   11,   12,   28,    1,    3,
  144,   14,  256,   15,  253,  166,   28,   28,  168,   81,
   28,   22,   28,  144,   28,   52,   52,  108,   10,  101,
  192,   10,   48,   52,  228,  249,  257,   11,   12,   10,
  238,  124,  258,   14,  109,   15,  116,  260,   11,   12,
  100,   49,   49,  239,   14,  228,   15,   10,   69,  116,
   69,   69,   69,    5,  118,  165,   11,   12,    9,   10,
   13,  176,   14,  121,   15,  122,   69,  118,   11,   12,
   10,   15,   13,  131,   14,    2,   15,  183,  184,   11,
   12,   10,   48,   13,   10,   14,  116,   15,   10,   10,
   48,  176,   11,   11,   12,   45,   10,   13,  209,   14,
   90,   15,   77,  251,  203,   11,   12,  205,  195,   13,
  185,   14,  180,   15,   12,   12,   10,  120,   66,   66,
   66,  215,   10,  120,   12,   12,   10,   66,   12,    0,
   12,    0,   12,  118,    0,   11,   12,   10,    0,   13,
   10,   14,   51,   15,   57,   57,   11,   12,    0,   11,
   12,   57,   14,   10,   15,   14,   10,   15,    0,    0,
   10,   48,   11,   12,    0,   11,   12,   78,   14,    0,
   15,   14,  121,   15,  122,    0,    0,   10,   90,   90,
   90,   90,   90,   90,    0,   90,   11,   12,    0,  202,
  200,  201,   14,  121,   15,  122,    0,   90,   90,   90,
   90,  155,   82,   82,   82,   82,   82,    0,   82,  143,
    0,  155,   85,   85,   85,   85,   85,    0,   85,    0,
   82,   82,   82,   82,   70,    0,   70,   70,   70,    0,
   85,   85,   85,   85,   67,    0,   67,   67,   67,  121,
    0,  122,   70,   70,   70,   70,  121,   99,  122,   66,
    0,   66,   67,   93,    0,   97,    0,   69,    0,   69,
    0,    0,  150,   69,   69,   69,   66,   66,   66,   98,
    0,   67,   69,   67,   69,   69,   69,   10,  120,   54,
    0,   53,   50,    0,   51,  219,   52,    0,   67,   67,
   67,   71,    0,   53,   50,    0,   51,    0,   52,  117,
   53,   50,    0,   51,  131,   52,   53,   50,    0,   51,
  109,   52,  109,  109,    0,  109,    0,  109,  141,   53,
   50,    0,   51,  177,   52,  194,   53,   50,    0,   51,
    0,   52,   53,   50,    0,   51,    0,   52,   53,   50,
    0,   51,   53,   52,    0,   51,   53,   52,    0,   51,
    0,   52,    0,  242,   10,   48,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  207,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   10,  120,    0,  196,
  197,  198,  199,    0,   90,   90,   90,    0,   90,   90,
   90,   90,   90,   90,    0,  236,    0,   10,  120,    0,
    0,    0,    0,    0,    0,    0,    0,   82,   82,   82,
    0,   82,   82,   82,   82,    0,   82,   85,   85,   85,
    0,   85,   85,   85,   85,    0,   85,    0,  128,   70,
   70,   70,    0,   70,   70,   70,   70,    0,   70,   67,
   67,   67,    0,   10,  120,    0,    0,    0,   67,    0,
   10,  120,   99,   66,   66,    0,   66,   66,   66,   66,
   97,   69,   69,    0,   69,   69,   69,   69,    0,    0,
    0,    0,    0,    0,   98,   67,   67,    0,   67,   67,
   67,   67,    0,  128,    0,    0,   10,   48,   49,    0,
    0,    0,    0,    0,  191,    0,   59,   70,   10,   48,
   72,  128,   89,   79,    0,   10,   48,   49,    0,    0,
    0,   10,   48,    0,    0,  115,    0,  109,  109,    0,
    0,  102,  102,    0,   10,   48,    0,    0,    0,    0,
  133,   10,   48,    0,    0,    0,  148,   10,   48,   59,
    0,    0,    0,   10,   48,    0,  189,   10,   48,    0,
    0,   10,   48,    0,    0,    0,  142,    0,  128,    0,
    0,    0,    0,  149,    0,  153,    0,   89,    0,    0,
  171,    0,    0,  191,  133,  133,    0,    0,    0,    0,
   89,    0,    0,    0,    0,    0,  102,    0,    0,    0,
  190,    0,    0,    0,    0,    0,  133,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  128,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  171,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  171,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  246,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  247,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  153,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,   40,    0,   44,   41,   59,   59,   44,    0,  256,
   16,   41,   45,   40,   59,   43,    0,   23,   59,  159,
   40,   27,   42,   43,  271,   45,    6,   47,   16,   40,
   44,   45,   92,   41,   41,   59,   42,   43,   45,   41,
   40,  257,  123,  257,   41,   59,  257,   44,  256,    5,
   59,   59,   46,   59,  270,   59,  270,   44,  270,   40,
   16,   40,   27,   44,  123,   45,   72,   23,   58,   45,
   76,   27,   40,   79,   61,   81,   82,   40,   59,  107,
  125,   59,   88,  223,  123,   91,   42,   43,   59,   44,
   40,   97,   88,   59,   41,  101,  123,   44,  125,   59,
   59,  107,   40,   59,   59,  101,   40,  167,  168,   97,
   42,   76,  123,   40,  257,   47,   72,   59,   59,   41,
   76,  258,   44,   79,   59,  125,  132,   40,  264,   45,
  257,  123,   88,   41,  257,   91,  142,   45,  128,   40,
  123,   97,   40,  149,  123,  101,   41,  153,  268,   44,
   41,  107,  269,   44,   41,  123,  256,  257,   40,  165,
   41,  167,   43,   41,   45,   43,   44,   45,   43,   40,
   45,   41,  178,  179,  257,  125,  132,   81,   82,  169,
   40,   59,  172,   40,   59,   59,  142,   40,  125,   42,
   43,   41,   45,  149,   47,  125,   40,  153,  125,   40,
  165,  191,  256,  256,  256,   42,   43,   59,   45,  165,
   47,  167,  125,  178,   59,  256,  257,  256,  257,  256,
   40,  227,  178,  179,  125,  258,  256,  266,  267,  256,
  257,  270,  256,  272,  271,  274,  257,  257,  258,  266,
  267,  247,  256,  270,  258,  272,  257,  274,  256,   41,
  257,  258,  256,  273,  256,  266,  267,  257,  256,  257,
  257,  272,  227,  274,  257,  125,  266,  267,  125,  256,
  270,  227,  272,  270,  274,  256,  257,  256,  257,  123,
  256,  257,  258,  264,  125,  256,  256,  266,  267,  257,
  256,  247,  256,  272,  273,  274,  256,  256,  266,  267,
  268,  256,  257,  269,  272,  125,  274,  257,   41,  269,
   43,   44,   45,  257,  256,  256,  266,  267,  256,  257,
  270,  256,  272,   43,  274,   45,   59,  269,  266,  267,
  257,    0,  270,   40,  272,    0,  274,  109,  110,  266,
  267,  257,  258,  270,  257,  272,  269,  274,    0,  257,
  258,  256,    0,  266,  267,    8,  257,  270,  140,  272,
   23,  274,  270,  217,  132,  266,  267,  132,  131,  270,
  110,  272,  103,  274,  256,  257,  257,  258,  256,  257,
  258,  256,  257,  258,  266,  267,  257,  265,  270,   -1,
  272,   -1,  274,   54,   -1,  266,  267,  257,   -1,  270,
  257,  272,   45,  274,  257,  258,  266,  267,   -1,  266,
  267,  264,  272,  257,  274,  272,  257,  274,   -1,   -1,
  257,  258,  266,  267,   -1,  266,  267,  264,  272,   -1,
  274,  272,   43,  274,   45,   -1,   -1,  257,   40,   41,
   42,   43,   44,   45,   -1,   47,  266,  267,   -1,   60,
   61,   62,  272,   43,  274,   45,   -1,   59,   60,   61,
   62,   40,   41,   42,   43,   44,   45,   -1,   47,   59,
   -1,   40,   41,   42,   43,   44,   45,   -1,   47,   -1,
   59,   60,   61,   62,   41,   -1,   43,   44,   45,   -1,
   59,   60,   61,   62,   41,   -1,   43,   44,   45,   43,
   -1,   45,   59,   60,   61,   62,   43,   41,   45,   43,
   -1,   45,   59,   24,   -1,   41,   -1,   43,   -1,   45,
   -1,   -1,   59,  256,  257,  258,   60,   61,   62,   41,
   -1,   43,  265,   45,   60,   61,   62,  257,  258,   40,
   -1,   42,   43,   -1,   45,  265,   47,   -1,   60,   61,
   62,   40,   -1,   42,   43,   -1,   45,   -1,   47,   41,
   42,   43,   -1,   45,   40,   47,   42,   43,   -1,   45,
   40,   47,   42,   43,   -1,   45,   -1,   47,   41,   42,
   43,   -1,   45,   94,   47,   41,   42,   43,   -1,   45,
   -1,   47,   42,   43,   -1,   45,   -1,   47,   42,   43,
   -1,   45,   42,   47,   -1,   45,   42,   47,   -1,   45,
   -1,   47,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  136,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,
  261,  262,  263,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,  264,  265,   -1,  176,   -1,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,   60,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,  256,
  257,  258,   -1,  257,  258,   -1,   -1,   -1,  265,   -1,
  257,  258,  256,  257,  258,   -1,  260,  261,  262,  263,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  115,   -1,   -1,  257,  258,  259,   -1,
   -1,   -1,   -1,   -1,  126,   -1,   11,  256,  257,  258,
   15,  133,   23,   18,   -1,  257,  258,  259,   -1,   -1,
   -1,  257,  258,   -1,   -1,   50,   -1,  257,  258,   -1,
   -1,   42,   43,   -1,  257,  258,   -1,   -1,   -1,   -1,
   65,  257,  258,   -1,   -1,   -1,  256,  257,  258,   54,
   -1,   -1,   -1,  257,  258,   -1,  256,  257,  258,   -1,
   -1,  257,  258,   -1,   -1,   -1,   71,   -1,  190,   -1,
   -1,   -1,   -1,   78,   -1,   80,   -1,   88,   -1,   -1,
   91,   -1,   -1,  205,  109,  110,   -1,   -1,   -1,   -1,
  101,   -1,   -1,   -1,   -1,   -1,  107,   -1,   -1,   -1,
  125,   -1,   -1,   -1,   -1,   -1,  131,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  246,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  167,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  179,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  203,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  204,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  217,
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
"asignacion_simple : left_hand_side DASIG expression ';'",
"asignacion_simple : left_hand_side DASIG expression error",
"asignacion_simple : left_hand_side expression ';'",
"asignacion_simple : left_hand_side DASIG error",
"left_hand_side : variable",
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
"factor : function_invocation",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : function_invocation",
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
"inline_function_invocation : function_invocation ';'",
"inline_function_invocation : function_invocation error",
"function_invocation : function_invocation_start '(' lista_argumentos ')'",
"function_invocation_start : variable",
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
"lambda : parametro_lambda lambda_body argumento_lambda ';'",
"lambda : parametro_lambda lambda_body argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"lambda_body : bloque_ejecutable",
"argumento_lambda : '(' factor ')'",
"argumento_lambda : '(' ')'",
"argumento_lambda :",
"parametro_lambda : '(' UINT ID ')'",
};

//#line 1234 "gramatica.y"

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

private void notifySemanticError(String errorMessage) {

    monitor.addError(String.format(
        "ERROR SEMÁNTICO: Línea %d: %s",
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

    this.reversePolish.returnToLastSafeState();
    this.errorState = false;
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isPrintOn() {
    return printDetections;
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 874 "Parser.java"
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
            this.symbolTable.removeEntry(val_peek(0).sval);
            this.symbolTable.addEntry(SymbolDirector.createProgram(val_peek(0).sval));
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.recordSafeState();
        }
break;
case 11:
//#line 126 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 129 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 132 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 134 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 136 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 21:
//#line 157 "gramatica.y"
{ this.treatErrorState(); }
break;
case 22:
//#line 159 "gramatica.y"
{ this.treatErrorState(); }
break;
case 23:
//#line 164 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 207 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 45:
//#line 242 "gramatica.y"
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
//#line 257 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 47:
//#line 261 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 48:
//#line 265 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 50:
//#line 275 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 51:
//#line 280 "gramatica.y"
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
//#line 302 "gramatica.y"
{ 
            if (!symbolTable.entryExists(val_peek(0).sval + ":" + scopeStack.asText())) {
                symbolTable.removeEntry(val_peek(0).sval);
                symbolTable.addEntry(SymbolDirector.createNewVariable(this.scopeStack.appendScope(val_peek(0).sval)));
            } else {
                errorState = true;
                notifySemanticError(String.format("El identificador '%s' ya fue declarado en el ámbito '%s'.", val_peek(0).sval, scopeStack.asText()));
            }
        }
break;
case 53:
//#line 319 "gramatica.y"
{ 

            if (this.statementAppearsInValidState()) {
                
                notifyDetection("Asignación simple.");

                /* Se añade el operador.*/
                reversePolish.addPolish(val_peek(2).sval);

            } else {

                this.treatInvalidState("asignación simple");

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                /*this.symbolTable.removeEntry($1);*/
                /*this.symbolTable.removeEntry($3);*/
            }
        }
break;
case 54:
//#line 342 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 55:
//#line 345 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 56:
//#line 348 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 57:
//#line 355 "gramatica.y"
{ this.reversePolish.addPolish(val_peek(0).sval); }
break;
case 58:
//#line 365 "gramatica.y"
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
case 59:
//#line 414 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 61:
//#line 423 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 62:
//#line 428 "gramatica.y"
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
case 64:
//#line 451 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 65:
//#line 456 "gramatica.y"
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
case 67:
//#line 479 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 68:
//#line 487 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 69:
//#line 491 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 498 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 71:
//#line 508 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 72:
//#line 510 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 73:
//#line 517 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 75:
//#line 526 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 533 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 77:
//#line 540 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;

            /*TypeChecker.checkDivisionByZero($2, $3);*/
        }
break;
case 79:
//#line 551 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 80:
//#line 558 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 81:
//#line 560 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 82:
//#line 568 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 83:
//#line 570 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 85:
//#line 579 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 86:
//#line 581 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 89:
//#line 590 "gramatica.y"
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
case 90:
//#line 609 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope(val_peek(0).sval))) {
                /* De entrar acá, la variable debe ser local.*/
                errorState = true;
                notifyError(String.format("Variable %s no declarada.", val_peek(0).sval));
            } else {
                /* A la entrada sin el scope, se le agrega el scope.*/
                /* Se combina con otra entrada en caso de coincidir el scope.*/
                yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
                this.symbolTable.replaceEntry(val_peek(0).sval, yyval.sval);
            }
        }
break;
case 91:
//#line 622 "gramatica.y"
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

            /* Se decrementa una referencia en la entrada del símbolo de ámbito.*/
            this.symbolTable.removeEntry(val_peek(2).sval);
        }
break;
case 92:
//#line 648 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {
                notifyDetection("Condición."); 
            } else {
                this.treatInvalidState("Condición");
            }
        }
break;
case 93:
//#line 659 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 663 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 665 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 672 "gramatica.y"
{
            if(!this.returnsController.isThereReturnInSection()) {
                this.reversePolish.addPolish(val_peek(1).sval);
            }
        }
break;
case 97:
//#line 681 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 683 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 685 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 100:
//#line 692 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 101:
//#line 696 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 106:
//#line 707 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 107:
//#line 716 "gramatica.y"
{ 

            this.returnsController.notifySectionEnd();

            if (this.statementAppearsInValidState()) {
                this.reversePolish.closeSelection();
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia 'if'."); 
            } else {

                this.treatInvalidState("Sentencia 'if'");
                this.reversePolish.discardSelection(); 
            }

            this.returnsController.notifySelectionEnd();
        }
break;
case 108:
//#line 737 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 109:
//#line 743 "gramatica.y"
{ if (!this.returnsController.isThereReturnInSection()) reversePolish.addPolish("open-selection"); }
break;
case 110:
//#line 744 "gramatica.y"
{
            if (!this.returnsController.isThereReturnInSection()) {  
                this.reversePolish.addSeparation("Entering 'if' body...");
                this.reversePolish.openSelection();
                this.returnsController.notifySelectionStart();
            }
        }
break;
case 112:
//#line 761 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 113:
//#line 763 "gramatica.y"
{ replaceLastErrorWith("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 114:
//#line 765 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 115:
//#line 767 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 116:
//#line 774 "gramatica.y"
{ this.returnsController.notifyEmptyElse(); }
break;
case 117:
//#line 776 "gramatica.y"
{ this.returnsController.notifyAlternativeEnd(); }
break;
case 118:
//#line 781 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 119:
//#line 789 "gramatica.y"
{
            this.reversePolish.openAlternative();
            this.returnsController.notifyAlternativeStart();
            this.reversePolish.addSeparation("Entering 'else' body...");
        }
break;
case 120:
//#line 802 "gramatica.y"
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
case 121:
//#line 815 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 122:
//#line 817 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 123:
//#line 825 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.openLoop();
        }
break;
case 125:
//#line 839 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 126:
//#line 841 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 128:
//#line 856 "gramatica.y"
{
            if (!this.errorState) {

                if (this.returnsController.isThereReturnInDeclaration()) {
                    this.scopeStack.pop();
                    notifyDetection("Declaración de función.");
                    this.reversePolish.closeFunctionDeclaration(this.scopeStack.appendScope(val_peek(4).sval));
                    this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(4).sval));
                } else {
                    notifySemanticError("La función necesita, en todos los casos, retornar un valor.");
                    this.errorState = true;
                }
                
            } else {
                this.treatInvalidState("Declaración de función");
            }

            this.returnsController.notifyEndOfFunctionDeclaration();
        }
break;
case 129:
//#line 879 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;

            this.returnsController.notifyEndOfFunctionDeclaration();
        }
break;
case 130:
//#line 894 "gramatica.y"
{
            if (!symbolTable.entryExists(val_peek(0).sval + ":" + this.scopeStack.asText())) {
                this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
                this.reversePolish.startFunctionDeclaration(val_peek(0).sval + ":" + this.scopeStack.asText());
                SymbolTable.getInstance().removeEntry(val_peek(0).sval);
                SymbolTable.getInstance().addEntry(SymbolDirector.createNewFunction(val_peek(0).sval + ":" + this.scopeStack.asText()));

                yyval.sval = val_peek(0).sval;
                this.scopeStack.push(val_peek(0).sval);

                this.returnsController.notifyStartOfFunctionDeclaration();
            } else {
                this.errorState = true;
                notifySemanticError(String.format("El identificador '%s' ya fue declarado en el ámbito '%s'.", val_peek(0).sval, scopeStack.asText()));
            }
        }
break;
case 131:
//#line 914 "gramatica.y"
{
            errorState = true;
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");

            this.returnsController.notifyStartOfFunctionDeclaration();
        }
break;
case 135:
//#line 939 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 138:
//#line 951 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 141:
//#line 965 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                this.symbolTable.removeEntry(val_peek(0).sval);
                this.symbolTable.addEntry(
                    SymbolDirector.createNewParameter(
                        this.scopeStack.appendScope(val_peek(2).sval),
                        (val_peek(2).sval == "CVR" ? ParameterSemanticModel.CVR : ParameterSemanticModel.CV)));

                this.reversePolish.addParameter(val_peek(0).sval, val_peek(2).sval);
            } else {
                this.treatInvalidState("Parámetro formal");
            }
        }
break;
case 142:
//#line 982 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 143:
//#line 984 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 144:
//#line 991 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 145:
//#line 993 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 146:
//#line 998 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 147:
//#line 1007 "gramatica.y"
{

            if (statementAppearsInValidState()) {

                if (this.returnsController.insideFunction()) {

                    reversePolish.addPolish("return");
                    notifyDetection("Sentencia 'return'.");

                    this.returnsController.notifyReturn();
                } else {
                    notifyError("La sentencia 'return' no está permitida fuera de la declaración de una función.");
                    this.errorState = true;
                }
            } else {

                this.treatInvalidState("Sentencia 'return'");
            }
        }
break;
case 148:
//#line 1030 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 149:
//#line 1032 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 150:
//#line 1034 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 151:
//#line 1036 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 152:
//#line 1045 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 153:
//#line 1050 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 154:
//#line 1057 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {

                this.reversePolish.closeFunctionCall();
            } else {
                this.treatInvalidState("Invocación de función");
                if (reversePolish.functionExists(val_peek(3).sval))
                    this.reversePolish.discardFunctionCall();
            }
        }
break;
case 155:
//#line 1073 "gramatica.y"
{ 
            if (reversePolish.functionExists(val_peek(0).sval)) {
                this.reversePolish.startFunctionCall(val_peek(0).sval);
            } else {
                notifySemanticError(String.format("La función '%s' no fue declarada.", val_peek(0).sval));
                errorState = true;
            }
        }
break;
case 157:
//#line 1088 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 1095 "gramatica.y"
{ this.reversePolish.addArgument(val_peek(0).sval); }
break;
case 159:
//#line 1100 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
break;
case 160:
//#line 1109 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                this.treatInvalidState("Sentencia 'print'");
            }
        }
break;
case 161:
//#line 1121 "gramatica.y"
{
            errorState = true;
            replaceLastErrorWith("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 163:
//#line 1135 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 164:
//#line 1137 "gramatica.y"
{
            errorState = true;
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 165:
//#line 1142 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 166:
//#line 1149 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 168:
//#line 1159 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {

                this.reversePolish.addArgument(val_peek(3).sval);
                this.reversePolish.closeLambdaCall();

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");

            } else {
                this.reversePolish.discardLambdaCall();
                this.treatInvalidState("Expresión 'lambda'");
            }
        }
break;
case 169:
//#line 1177 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 170:
//#line 1180 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 171:
//#line 1182 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 172:
//#line 1184 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 173:
//#line 1191 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                this.reversePolish.closeLambdaDeclaration();
                this.reversePolish.startLambdaCall();
                this.scopeStack.pop();
            }
        }
break;
case 174:
//#line 1203 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 175:
//#line 1208 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 176:
//#line 1211 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 177:
//#line 1218 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering lambda expression body...");
            String lambdaName = this.reversePolish.startLambdaDeclaration(this.scopeStack.asText());
            this.scopeStack.push(lambdaName);
            this.reversePolish.addParameter(val_peek(1).sval, "cv");
            this.symbolTable.removeEntry(val_peek(1).sval);
            yyval.sval = this.scopeStack.appendScope(val_peek(1).sval);
            this.symbolTable.addEntry(SymbolDirector.createNewParameter(yyval.sval));
        }
break;
//#line 1901 "Parser.java"
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
