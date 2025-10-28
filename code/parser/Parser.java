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
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 37 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 33 "Parser.java"




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
    0,    0,   26,    0,   27,    0,    0,    0,   18,   24,
   24,   30,   24,   24,   24,   24,   29,   29,   28,   28,
   25,   25,   25,   31,   31,   33,   33,   36,   36,   37,
   37,   38,   38,   39,   39,   32,   32,   32,   32,   32,
   32,   32,   32,   41,   41,   34,   34,   34,   34,   20,
   20,   20,   19,   40,   40,   40,   40,   23,   23,   21,
   21,   21,   22,   22,   22,    1,    1,    1,    1,    1,
    6,    6,    2,    2,    2,    2,    4,    4,    4,    7,
    7,    3,    3,    3,    5,    5,    5,   10,   10,    9,
    9,   47,   47,   47,   47,   48,   48,   48,   48,   14,
   14,   14,   14,   14,   14,   14,   45,   45,   49,   49,
   49,   49,   50,   51,   51,   51,   53,   46,   52,   52,
   52,   52,   52,   54,   56,   55,   55,   57,   35,   35,
    8,    8,   58,   58,   59,   59,   59,   61,   61,   60,
   60,   60,   15,   15,   15,   42,   42,   42,   42,   42,
   11,   12,   12,   13,   13,   43,   43,   62,   62,   62,
   62,   63,   63,   44,   44,   44,   44,   44,   17,   17,
   17,   16,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    5,    2,    1,
    3,    2,    1,    4,    4,    3,    3,    3,    4,    2,
    3,    2,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    1,    3,    2,    3,    1,    3,    1,
    1,    2,    1,    1,    1,    1,    1,    1,    2,    1,
    3,    3,    2,    2,    3,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    2,    4,    4,
    3,    3,    1,    0,    2,    1,    0,    2,    3,    3,
    3,    3,    2,    2,    1,    1,    2,    2,    5,    4,
    2,    1,    3,    2,    1,    3,    1,    2,    2,    3,
    2,    2,    0,    1,    1,    5,    5,    4,    3,    2,
    4,    1,    3,    3,    1,    3,    3,    3,    2,    1,
    0,    1,    1,    4,    4,    5,    4,    5,    3,    2,
    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   38,    1,    0,    0,   21,   24,   25,   26,    0,
   37,   39,   40,   41,   42,   44,   45,    0,    4,    0,
   23,    0,    0,   88,  162,    0,    0,   80,   81,    0,
    0,    0,   74,    0,    0,   83,   84,    0,  160,  108,
    0,    0,    0,    0,    0,   49,    0,    0,    0,  150,
    0,    0,    0,   17,   14,    0,    0,    0,    0,    0,
    0,   60,    0,    0,   62,   43,   36,    0,   34,    0,
    0,    0,   63,    0,   22,   18,    0,   29,   27,    0,
  118,    6,   91,    0,    0,  152,    0,   89,  159,    0,
   86,   71,   72,    0,   78,    0,   85,   87,    0,   76,
   82,  157,  156,   93,    0,  102,  104,  103,  105,  106,
  100,  101,    0,    0,    0,    0,    0,   30,  113,   31,
  107,    0,    0,   94,    0,   47,   52,   46,    0,    0,
    0,  149,   16,    0,    0,    0,  145,  144,    0,  134,
    0,    0,  135,  137,    0,   57,    0,    0,    0,   61,
   56,   33,    0,    0,    0,    0,    0,    0,   35,    0,
   58,    0,   65,    0,  123,    0,    0,    0,    0,    0,
  126,    0,    0,  151,  158,    0,   68,    0,   75,   73,
   95,   92,    0,    0,  115,    0,    0,    0,    0,   53,
   51,  148,    0,   19,   20,  172,  139,  142,    0,    0,
  133,  130,    0,   55,   54,   32,    0,  170,    0,  165,
  164,    0,  167,   59,   64,   13,  128,  127,  121,  119,
  122,  120,  124,  154,  153,   79,   77,    0,  111,  112,
   48,  147,  146,  140,  136,  129,  166,  169,  168,  110,
  109,
};
final static short yydgoto[] = {                          4,
   62,   52,   53,  114,  115,  116,   54,   17,   55,   56,
   57,  105,  106,  135,  161,   20,  175,    5,   68,   69,
   21,   94,   22,   23,   24,    6,    8,  155,   25,   97,
   26,   27,   28,   29,   30,   99,  139,  140,   91,   31,
   32,   33,   34,   35,   36,   37,   64,   65,  141,  142,
  143,  101,   38,  188,  189,  190,  191,   79,  162,  163,
  164,   58,   59,
};
final static short yysindex[] = {                         3,
   29,    0,    0,    0,  -35, -101,    0, -210,   -7,   32,
  328,  404, -133,  418,  -40, -189,   61,   26,  -46,   51,
  -39,    0,    0,   62,  -10,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,    0,    0, -157,    0, -101,
    0, -140,  436,    0,    0,  422, -139,    0,    0,  429,
  163,   47,    0,  277, -135,    0,    0,  -42,    0,    0,
   69,  393,   47,  -24,   84,    0,    0, -129,  343,    0,
   89,  100,  -41,    0,    0,   15, -119,  -33,   16,  446,
 -113,    0,  439,  250,    0,    0,    0,   64,    0,  113,
   67,  -13,    0,    5,    0,    0,   40,    0,    0,  -20,
    0,    0,    0,  104,    7,    0,   47,    0,    0,  114,
    0,    0,    0,   47,    0,  295,    0,    0,  259,    0,
    0,    0,    0,    0,  -27,    0,    0,    0,    0,    0,
    0,    0,   47,  295,  436,   78,   64,    0,    0,    0,
    0, -112, -100,    0,  -13,    0,    0,    0,  -89,   98,
   22,    0,    0,   53,   58,  131,    0,    0, -217,    0,
 -211,   49,    0,    0,   -5,    0,  414,  144,  -37,    0,
    0,    0,   82,  170,   87,  113,   96,  -57,    0,  254,
    0,  -13,    0,   35,    0,   -9,   -9,   95,   99,  -75,
    0,  -44,  436,    0,    0,  270,    0,   47,    0,    0,
    0,    0,   47,  163,    0,   93,   -3,  141,  320,    0,
    0,    0,  135,    0,    0,    0,    0,    0,  129, -217,
    0,    0,   46,    0,    0,    0,  151,    0,  382,    0,
    0,  169,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  137,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                        19,
  172,    0,    0,    0,    2,  432,    0,    0,    0,  120,
  241,    0,  405,    0,  176,    0,    0,    0,    0,  176,
    0,    0,    0,    4,   21,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  432,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -32,   -1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  351, -174,    0,    0,  153,  371,    0,    0,
    0,    0,    0,    0,    0,  176,    0, -178,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  176,    0,  264,
 -219,    0,    0,    0,    0,    0,  176,    0,    0,  176,
    0,    0,    0,   56,    0,    0,  143,    0,    0,    0,
    0,    0,    0,  107,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  373,    0,    0,  126,  176,    0,    0,    0,
    0,   17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  450,  452,    0,    0,    0, -178,    0,
    0,    0,    0,    0,  176,    0,    0,   13,    0,    0,
    0,    0, -219,    0,    0,  200,    0,    0,    0,    0,
    0,    0,    0,  176,    0,    0,  189,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  132,    0,    0,
    0,    0,  381,  -16,    0,  176,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   65,  -29,
    0,    0,  176,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  522,  678,  385,  406,    0,  415,  442,    0,  601,  398,
  644,    0,  273,    0,    0,    0,  -68,  472,  336,    0,
  356,  394,    0,   20,   30,    0,    0,    0,    0,    0,
  -14,  411,    0,    0,    0,    0,  -70,  467,  -60,    0,
    0,    0,    0,    0,    0,    0,  -58,  434,    0,    0,
  354,    0,    0,    0,    0,    0,  310,    0,    0, -138,
    0,    0,  451,
};
final static int YYTABLESIZE=867;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   28,   15,    2,    2,   16,   47,   81,  160,  163,   95,
  159,  138,   87,  202,  138,   16,  123,   41,    3,   16,
  217,   15,  178,   82,   96,   39,  163,  173,    7,  187,
   61,   47,   49,   46,   16,   47,  171,   48,  157,   66,
   28,   66,   66,   66,   76,  218,    3,  194,  182,   47,
  193,   41,  117,  158,   16,  249,   90,   66,  219,  102,
   12,   95,  213,  181,  112,  205,  113,   49,   46,   81,
   47,   43,   48,   90,   16,  114,  206,   42,  143,   16,
   77,  255,   74,  153,   75,   16,   82,   15,   49,  221,
   16,  143,  220,   48,  114,   98,  155,  117,  137,  155,
   78,   16,  137,   16,  227,  141,  177,  232,  141,  124,
   49,   46,   96,   47,  100,   48,  103,   16,  108,  222,
  121,  177,   66,   67,  144,   28,  184,  237,  238,  150,
   49,   46,   16,   47,  145,   48,  228,  156,  165,  154,
   47,    3,  112,  168,  113,  231,  112,   69,  113,   69,
   69,   69,  174,  240,  195,  136,  212,  242,  152,  236,
   90,   90,   90,   90,   90,   69,   90,  210,  208,   95,
  256,  216,   67,   88,   67,   67,   67,  214,   90,   90,
   90,   90,  215,   70,  116,   70,   70,   70,  172,   42,
   67,  176,  131,  253,  223,  261,   53,  186,  233,  250,
  137,   70,   70,   70,   70,  112,  226,  113,   95,   86,
  228,   53,  244,  122,   47,   73,   10,  226,   44,  168,
    9,   10,  157,  163,   92,   11,   12,  143,  201,   13,
   11,   12,   10,   14,   13,  185,   10,  158,   14,   96,
  143,   11,   12,  136,   44,   11,   12,   10,   44,   14,
    9,   10,  186,   14,   66,   66,   66,   28,    1,    3,
   11,   12,   44,   66,   13,  248,   28,   28,   14,   90,
   28,   10,   28,  117,   28,  117,   12,   12,   10,  111,
   11,   12,   10,   44,   13,  114,   12,   12,   14,   80,
   12,   10,   12,   81,   12,    9,   10,  182,   47,  161,
   11,   12,   10,   47,   13,   11,   12,   10,   14,   13,
   82,   11,   12,   14,   47,   13,   11,   12,   10,   14,
   10,   47,  171,   10,   14,   10,   44,   11,   12,   11,
   12,   13,   11,   12,   10,   14,   49,   14,   10,   47,
   14,   48,  230,   11,   12,   10,   44,   11,   12,   10,
  239,   14,   10,   44,  241,   14,   10,  111,   11,   12,
   10,  111,   69,   69,   69,   77,   14,   50,  192,   49,
   46,   69,   47,   85,   48,   90,   90,   90,  251,   90,
   90,   90,   90,   90,   90,  254,  149,   67,   67,   67,
  252,   99,  260,   66,  116,   66,   67,  117,   70,   70,
   70,  148,   70,   70,   70,   70,  257,   70,   53,   53,
   66,   66,   66,   97,   50,   69,   53,   69,   93,   10,
  111,   98,  258,   67,  259,   67,   10,   44,    5,   50,
   89,   15,   69,   69,   69,  112,  170,  113,  120,   85,
   67,   67,   67,   61,  132,   49,   46,  117,   47,   10,
   48,   11,  132,  130,  131,  171,  112,   71,  113,   49,
   46,  125,   47,   49,   48,  245,   47,  133,   48,  109,
   49,   46,  225,   47,  138,   48,  134,   49,   46,   40,
   47,  112,   48,  113,  211,  180,   90,   49,   46,   93,
   47,  183,   48,  119,  125,  207,  161,  171,   89,  243,
  110,  179,    0,  200,  119,  121,  168,    0,    0,  234,
  138,   44,    0,    0,  199,   10,   44,    0,    0,  171,
    0,    0,    0,    0,   85,  246,   10,   44,    0,    0,
    0,    0,   51,   10,   44,   72,    0,    0,    0,   83,
    0,    0,  209,    0,    0,    0,  138,   89,  119,    0,
  197,   10,   44,    0,    0,  196,    0,    0,  229,    0,
    0,  229,    0,    0,  104,    0,    0,    0,    0,    0,
    0,   51,    0,    0,  196,    0,    0,  183,    0,  235,
  247,    0,    0,  179,   10,   44,   45,    0,    0,    0,
    0,    0,  151,    0,    0,    0,    0,    0,  146,  147,
    0,  167,    0,    0,    0,   18,   99,   66,   66,    0,
   66,   66,   66,   66,    0,   18,  179,    0,   84,    0,
   18,    0,    0,    0,   18,    0,   50,   50,   97,   69,
   69,    0,   69,   69,   69,   69,   98,   67,   67,  119,
   67,   67,   67,   67,  119,    0,    0,    0,   19,   10,
  111,  117,  126,  127,  128,  129,  204,    0,   19,   60,
   10,   44,  117,   19,   18,    0,    0,   19,    0,  224,
   10,  111,  117,   70,   10,   44,   18,    0,   10,   44,
    0,  169,    0,  117,  169,   10,   44,   45,   18,   63,
    0,   18,   10,   44,  118,   10,  111,   18,    0,    0,
   18,  166,   10,   44,  117,  118,    0,   19,    0,    0,
    0,    0,    0,    0,  104,  118,    0,    0,    0,   19,
    0,    0,    0,  107,    0,    0,  118,    0,    0,    0,
    0,   19,    0,    0,   19,    0,   18,   18,   63,    0,
   19,    0,    0,   19,    0,    0,    0,  118,    0,    0,
    0,  117,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   18,    0,  117,    0,  169,
    0,    0,    0,   18,    0,    0,    0,    0,    0,   19,
   19,    0,    0,    0,   18,    0,    0,    0,    0,    0,
    0,    0,    0,  198,  118,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  117,    0,   18,    0,   19,    0,
  118,  203,    0,    0,    0,    0,   19,    0,    0,    0,
    0,    0,    0,   18,    0,    0,    0,   19,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  118,    0,   19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   63,   63,    0,   19,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,    0,    0,   40,   45,   44,   41,   41,   24,
   44,   41,   59,   41,   44,   40,   59,   59,    0,   40,
  159,  123,   91,   61,   41,    6,   59,   88,    0,  100,
   40,   45,   42,   43,   40,   45,  256,   47,  256,   41,
   40,   43,   44,   45,   15,  257,  257,   41,   44,   45,
   44,   59,  272,  271,   40,   59,   44,   59,  270,   40,
   40,   76,   41,   59,   43,  136,   45,   42,   43,   44,
   45,   40,   47,   61,   40,   59,  137,   46,  257,   40,
  270,  220,  123,  125,  125,   40,   61,  123,   42,   41,
   40,  270,   44,   47,  269,   59,   41,  272,  123,   44,
   40,   40,  123,   40,  173,   41,   40,  176,   44,   41,
   42,   43,  123,   45,  272,   47,  257,   40,  258,  125,
  256,   40,  256,  257,   41,  125,   97,  186,  187,   41,
   42,   43,   40,   45,  264,   47,   41,  257,  123,  125,
   45,  123,   43,  257,   45,   59,   43,   41,   45,   43,
   44,   45,   40,   59,   41,  268,   59,   59,   59,  125,
   41,   42,   43,   44,   45,   59,   47,  257,  269,  184,
  125,   41,   41,  123,   43,   44,   45,  125,   59,   60,
   61,   62,  125,   41,   59,   43,   44,   45,  125,   46,
   59,  125,   40,   59,  165,   59,   44,  273,  256,   59,
  123,   59,   60,   61,   62,   43,  125,   45,  223,  256,
   41,   59,  257,  256,   45,  256,  257,  125,  258,  257,
  256,  257,  256,  256,  264,  266,  267,  257,  256,  270,
  266,  267,  257,  274,  270,  256,  257,  271,  274,  256,
  270,  266,  267,  268,  258,  266,  267,  257,  258,  274,
  256,  257,  273,  274,  256,  257,  258,  257,  256,  257,
  266,  267,  258,  265,  270,  269,  266,  267,  274,  257,
  270,  257,  272,  272,  274,  272,  256,  257,  257,  258,
  266,  267,  257,  258,  270,  269,  266,  267,  274,  264,
  270,  257,  272,   44,  274,  256,  257,   44,   45,   59,
  266,  267,  257,   45,  270,  266,  267,  257,  274,  270,
   61,  266,  267,  274,   45,  270,  266,  267,  257,  274,
  257,   45,   59,  257,  274,  257,  258,  266,  267,  266,
  267,  270,  266,  267,  257,  274,   42,  274,  257,   45,
  274,   47,  256,  266,  267,  257,  258,  266,  267,  257,
  256,  274,  257,  258,  256,  274,  257,  258,  266,  267,
  257,  258,  256,  257,  258,  270,  274,   40,  265,   42,
   43,  265,   45,   18,   47,  256,  257,  258,   59,  260,
  261,  262,  263,  264,  265,  257,   44,  256,  257,  258,
  256,   41,  256,   43,  269,   45,  265,  272,  256,  257,
  258,   59,  260,  261,  262,  263,  256,  265,  256,  257,
   60,   61,   62,   41,   44,   43,  264,   45,   21,  257,
  258,   41,   41,   43,  256,   45,  257,  258,  257,   59,
   20,    0,   60,   61,   62,   43,   81,   45,   54,   84,
   60,   61,   62,   40,   40,   42,   43,  272,   45,    0,
   47,    0,   60,   61,   62,  256,   43,   40,   45,   42,
   43,  273,   45,   42,   47,  193,   45,   62,   47,   41,
   42,   43,   59,   45,   64,   47,   62,   42,   43,    8,
   45,   43,   47,   45,  149,   92,   20,   42,   43,   92,
   45,   94,   47,   52,   61,  142,  256,   59,   88,  190,
   50,   91,   -1,  119,   63,  256,  257,   -1,   -1,  256,
  100,  258,   -1,   -1,  256,  257,  258,   -1,   -1,  256,
   -1,   -1,   -1,   -1,  169,  256,  257,  258,   -1,   -1,
   -1,   -1,   11,  257,  258,   14,   -1,   -1,   -1,   18,
   -1,   -1,  145,   -1,   -1,   -1,  136,  137,  107,   -1,
  256,  257,  258,   -1,   -1,  114,   -1,   -1,  174,   -1,
   -1,  177,   -1,   -1,   43,   -1,   -1,   -1,   -1,   -1,
   -1,   50,   -1,   -1,  133,   -1,   -1,  180,   -1,  182,
  196,   -1,   -1,  173,  257,  258,  259,   -1,   -1,   -1,
   -1,   -1,   71,   -1,   -1,   -1,   -1,   -1,  256,  257,
   -1,   80,   -1,   -1,   -1,    5,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,   15,  206,   -1,   18,   -1,
   20,   -1,   -1,   -1,   24,   -1,  256,  257,  256,  257,
  258,   -1,  260,  261,  262,  263,  256,  257,  258,  198,
  260,  261,  262,  263,  203,   -1,   -1,   -1,    5,  257,
  258,   51,  260,  261,  262,  263,  135,   -1,   15,  256,
  257,  258,   62,   20,   64,   -1,   -1,   24,   -1,  256,
  257,  258,   72,  256,  257,  258,   76,   -1,  257,  258,
   -1,   81,   -1,   83,   84,  257,  258,  259,   88,   12,
   -1,   91,  257,  258,   51,  257,  258,   97,   -1,   -1,
  100,  256,  257,  258,  104,   62,   -1,   64,   -1,   -1,
   -1,   -1,   -1,   -1,  193,   72,   -1,   -1,   -1,   76,
   -1,   -1,   -1,   46,   -1,   -1,   83,   -1,   -1,   -1,
   -1,   88,   -1,   -1,   91,   -1,  136,  137,   61,   -1,
   97,   -1,   -1,  100,   -1,   -1,   -1,  104,   -1,   -1,
   -1,  151,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  165,   -1,  167,   -1,  169,
   -1,   -1,   -1,  173,   -1,   -1,   -1,   -1,   -1,  136,
  137,   -1,   -1,   -1,  184,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  116,  151,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  204,   -1,  206,   -1,  165,   -1,
  167,  134,   -1,   -1,   -1,   -1,  173,   -1,   -1,   -1,
   -1,   -1,   -1,  223,   -1,   -1,   -1,  184,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  204,   -1,  206,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  186,  187,   -1,  223,
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
"executable_statement : invocacion_funcion ';'",
"executable_statement : asignacion_simple",
"executable_statement : multiple_assignment",
"executable_statement : sentencia_control",
"executable_statement : sentencia_retorno",
"executable_statement : impresion",
"executable_statement : lambda",
"executable_statement : invocacion_funcion error",
"sentencia_control : if",
"sentencia_control : iteracion",
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
"multiple_assignment : list_of_variables DASIG list_of_constants error",
"list_of_variables : variable '='",
"list_of_variables : variable ',' list_of_variables",
"list_of_variables : variable list_of_variables",
"list_of_constants : constant",
"list_of_constants : list_of_constants ',' constant",
"list_of_constants : list_of_constants constant",
"expression : termino",
"expression : expression operador_suma termino",
"expression : expression operador_suma error",
"expression : expression termino_simple",
"expression : '+' termino",
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
"cuerpo_condicion : expression termino_simple",
"cuerpo_condicion : expression operador_suma termino",
"cuerpo_condicion : termino",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : IF condicion cuerpo_if",
"if : IF error",
"cuerpo_if : cuerpo_then rama_else ENDIF ';'",
"cuerpo_if : cuerpo_then rama_else ENDIF error",
"cuerpo_if : cuerpo_then rama_else ';'",
"cuerpo_if : rama_else ENDIF ';'",
"cuerpo_then : cuerpo_ejecutable",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"rama_else : ELSE",
"$$4 :",
"iteracion : $$4 do_while",
"do_while : DO cuerpo_iteracion ';'",
"do_while : DO cuerpo_iteracion_recuperacion ';'",
"do_while : DO cuerpo_iteracion error",
"do_while : DO cuerpo_iteracion_recuperacion error",
"do_while : DO error",
"cuerpo_iteracion : cuerpo_do fin_cuerpo_iteracion",
"cuerpo_do : cuerpo_ejecutable",
"cuerpo_iteracion_recuperacion : fin_cuerpo_iteracion",
"cuerpo_iteracion_recuperacion : cuerpo_ejecutable condicion",
"fin_cuerpo_iteracion : WHILE condicion",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' statement_list '}'",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' '}'",
"inicio_funcion : UINT ID",
"inicio_funcion : UINT",
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
"invocacion_funcion : ID '(' lista_argumentos ')'",
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
"parametro_lambda : '(' UINT ID ')'",
};

//#line 1101 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private MessageCollector errorCollector, warningCollector;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    this.symbolTable = SymbolTable.getInstance();
    
    this.scopeStack = new ScopeStack();
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
    Printer.printWrapped(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    errorCollector.replaceLastWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 768 "Parser.java"
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
//#line 78 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addExitSeparation(val_peek(1).sval);
            } else {
                errorState = false;
            }
        }
break;
case 2:
//#line 90 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 93 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 95 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 98 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 101 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 108 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addEntrySeparation(val_peek(0).sval);
        }
break;
case 11:
//#line 123 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 126 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 129 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 131 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 133 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 23:
//#line 159 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 200 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 218 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 229 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 246 "gramatica.y"
{
            if (!errorState) {
                { notifyDetection("Declaración de variables."); }
            } else {
                errorState = false;
            }
        }
break;
case 47:
//#line 256 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 48:
//#line 260 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 49:
//#line 264 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 274 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 52:
//#line 279 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 53:
//#line 302 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 54:
//#line 316 "gramatica.y"
{ 

            if (!errorState) {
                
                notifyDetection("Asignación simple.");

                this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);/*yo no pondría esto, cuando $3 es una expresion queda mal*/

                reversePolish.addPolish(val_peek(3).sval);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish(val_peek(2).sval);
            } else {

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                this.symbolTable.removeEntry(val_peek(3).sval);
                this.symbolTable.removeEntry(val_peek(1).sval);

                this.reversePolish.emptyTemporalPolishes();

                errorState = false;
            }
        }
break;
case 55:
//#line 345 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 56:
//#line 348 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 57:
//#line 351 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 58:
//#line 361 "gramatica.y"
{
            if (!errorState) {
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

                        this.symbolTable.setValue(variable, constant);
                        reversePolish.addPolish(variable);
                        reversePolish.addPolish(constant);
                        /* Se agrega un DASIG ya que son varias asignaciones simples.*/
                        reversePolish.addPolish(":=");
                    }

                    notifyDetection("Asignación múltiple.");
                }
            } else {

                notifyError("Hola");
                errorState = false;
            }
        }
break;
case 59:
//#line 410 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 61:
//#line 419 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 62:
//#line 424 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 64:
//#line 447 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 65:
//#line 452 "gramatica.y"
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
//#line 475 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 68:
//#line 483 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 69:
//#line 487 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 494 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 71:
//#line 504 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 72:
//#line 506 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 73:
//#line 513 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 75:
//#line 522 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 529 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 77:
//#line 536 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 79:
//#line 545 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 80:
//#line 552 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 81:
//#line 554 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 82:
//#line 563 "gramatica.y"
{
            /* TODO: esto es un parche. Debe verse mejor después.*/
            this.errorCollector.removeLast(); /* Debido a que se usa el token error.*/
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 83:
//#line 569 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 580 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 86:
//#line 584 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 89:
//#line 595 "gramatica.y"
{
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 90:
//#line 611 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope(val_peek(0).sval))) { /*Si entra por aca, la variable debe ser local*/
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
case 91:
//#line 624 "gramatica.y"
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
case 92:
//#line 648 "gramatica.y"
{ 
            if (!errorState) {
                reversePolish.addFalseBifurcation();
                notifyDetection("Condición."); 
            } else {
                errorState = false;
            }
        }
break;
case 93:
//#line 660 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 664 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 666 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 673 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            reversePolish.addPolish(val_peek(1).sval);
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
            if (!errorState) {
                reversePolish.completeSelection();
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 108:
//#line 729 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 110:
//#line 738 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 111:
//#line 740 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 112:
//#line 742 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 113:
//#line 747 "gramatica.y"
{ reversePolish.addInconditionalBifurcation(); }
break;
case 116:
//#line 759 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 766 "gramatica.y"
{ reversePolish.registerDoBody(); }
break;
case 119:
//#line 772 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 121:
//#line 778 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 122:
//#line 780 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 123:
//#line 782 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 126:
//#line 799 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 127:
//#line 801 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 128:
//#line 808 "gramatica.y"
{ reversePolish.addTrueBifurcation(); }
break;
case 129:
//#line 817 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                this.scopeStack.pop();
                this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                this.reversePolish.addExitSeparation(val_peek(4).sval);
            } else {
                errorState = false;
            }
        }
break;
case 130:
//#line 833 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 131:
//#line 845 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addEntrySeparation(val_peek(0).sval);
        }
break;
case 132:
//#line 854 "gramatica.y"
{
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 134:
//#line 869 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 137:
//#line 881 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 140:
//#line 895 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 141:
//#line 906 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 142:
//#line 908 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 143:
//#line 915 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 144:
//#line 917 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 145:
//#line 922 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 146:
//#line 931 "gramatica.y"
{
            if (!errorState) {
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("return");
                notifyDetection("Sentencia 'return'.");
            } else {
                errorState = false;
            }
        }
break;
case 147:
//#line 944 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 148:
//#line 946 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 149:
//#line 948 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 150:
//#line 950 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 151:
//#line 959 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 153:
//#line 969 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 154:
//#line 976 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 155:
//#line 981 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 156:
//#line 990 "gramatica.y"
{
            if (!errorState) {
                /* Se añaden las polacas correspondiente al imprimible.*/
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                errorState = false;
            }
        }
break;
case 157:
//#line 1004 "gramatica.y"
{
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 159:
//#line 1019 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 160:
//#line 1021 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 161:
//#line 1027 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 162:
//#line 1034 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 164:
//#line 1046 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Expresión lambda.");
                this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);

                /* El argumento no se agrega como polaca.*/
                this.reversePolish.emptyTemporalPolishes();
            } else {
                errorState = false;
            }
        }
break;
case 165:
//#line 1061 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 166:
//#line 1064 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 167:
//#line 1066 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 168:
//#line 1068 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 169:
//#line 1075 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 170:
//#line 1080 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 171:
//#line 1083 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 172:
//#line 1090 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
        }
break;
//#line 1673 "Parser.java"
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
