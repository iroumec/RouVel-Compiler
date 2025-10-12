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
   49,   49,   49,   40,   40,   40,   50,   51,   51,   51,
   52,   28,   28,   28,   55,   55,   54,   53,   53,   56,
   56,   56,   58,   58,   57,   57,   57,   59,   59,   59,
   36,   36,   36,   36,   36,   12,   13,   13,   14,   14,
   37,   37,   61,   61,   61,   61,   60,   62,   62,   38,
   38,   38,   38,   38,   65,   65,   65,   64,   63,
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
    0,    2,    1,    3,    3,    2,    2,    1,    1,    2,
    2,    6,    5,    7,    1,    2,    3,    1,    0,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    5,    5,    4,    3,    2,    4,    1,    3,    3,    1,
    3,    3,    1,    2,    1,    0,    3,    1,    1,    4,
    4,    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    9,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    2,
    0,    0,   23,   26,   27,   28,    0,   39,   40,   41,
   42,   43,   44,   46,   47,    0,   10,   11,    5,    0,
   25,    0,    0,   97,  168,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,    0,
  119,    0,    0,    0,   53,    0,    0,    0,    0,  126,
    0,    0,   32,    0,   33,    0,    0,    0,  155,    0,
    0,    0,   19,   16,    0,    0,    0,    0,   67,    0,
    0,    0,    0,   45,   38,   24,   20,    0,   31,   29,
    0,   36,    0,    0,    0,    7,  100,    0,    0,  157,
    0,   98,    0,    0,   95,   80,   81,    0,   87,    0,
   94,   96,    0,   85,   91,  161,  162,  102,    0,    0,
    0,    0,    0,   50,   57,   49,    0,    0,  150,  149,
    0,    0,    0,  140,  142,    0,   51,   56,   48,    0,
    0,  131,   35,    0,    0,  127,  124,  125,    0,    0,
  154,    0,    0,    0,    0,   61,    0,    0,   68,   60,
    0,   69,    0,    0,    0,    0,    0,    0,    0,    0,
   37,    0,    0,    0,    0,    0,  156,  167,    0,   77,
    0,   84,   82,  122,    0,    0,  111,  113,  112,  114,
  115,  109,  110,    0,  106,  105,  101,   54,    0,  144,
    0,    0,    0,  147,   55,    0,   34,  153,    0,   21,
   22,  179,   59,   58,   64,   62,    0,    0,   66,   15,
    0,    0,    0,  170,  171,    0,  173,   12,  159,  158,
   88,   86,    0,  118,  120,    0,    0,    0,  135,  133,
  141,  145,   52,  152,  151,    0,   72,   70,  172,  178,
  174,  117,  116,    0,  132,  136,    0,   65,   63,    0,
   74,  134,  137,   73,
};
final static short yydgoto[] = {                          4,
   51,   52,   53,  118,  119,  120,   54,   68,  256,   17,
   56,   18,  109,  110,   19,   20,  267,   39,    5,    7,
  164,   22,   98,   23,   24,   25,   26,   27,  100,   74,
   75,  104,   28,   29,   30,   31,   32,   33,   34,   35,
   92,   93,  229,   63,   64,  133,  207,  204,  131,   76,
   77,   78,  142,  249,  250,  143,  144,  145,  146,   58,
   59,   60,   36,  182,  178,
};
final static short yysindex[] = {                         3,
   27,    0,   34,    0,  -94,    0, -221,  -77,   43,  424,
  -19,  122,   46,  512,  -17, -210,  379,  -46,    0,    0,
  189,  -54,    0,    0,    0,    0,   44,    0,    0,    0,
    0,    0,    0,    0,    0,  142,    0,    0,    0,  -52,
    0, -176,   23,    0,    0,  445, -141,    0,    0,  307,
  350,   88,    0,  278, -124,    0,    0,   75, -113,    0,
    0,  110,   89,   23,    0,  345,  -28,  248, -120,    0,
  116,  208,    0,  -32,    0,  106,  -90,    0,    0,  252,
   59,   50,    0,    0,  101,  -81,  415,  -78,    0,   64,
 -124,   -6,   15,    0,    0,    0,    0,  120,    0,    0,
  208,    0,  140,  222,  120,    0,    0,  -23,  104,    0,
   88,    0,    0,  143,    0,    0,    0,   88,    0,  439,
    0,    0,  339,    0,    0,    0,    0,    0,  211,  -82,
  -72,  391,  -36,    0,    0,    0,  -70,  -28,    0,    0,
 -222,  147,  154,    0,    0, -207,    0,    0,    0,  -66,
   -6,    0,    0,  234,    0,    0,    0,    0,  148,  149,
    0,    0,   74,   79,  165,    0,  530,  167,    0,    0,
   55,    0,   -6,  131,  245,   52,  150,  -42,  140,  -34,
    0,    0,  -37,  145,  -35,   23,    0,    0,  355,    0,
   88,    0,    0,    0,  -15,  174,    0,    0,    0,    0,
    0,    0,    0,   23,    0,    0,    0,    0,  220,    0,
  160, -222,  -78,    0,    0,  218,    0,    0,  -45,    0,
    0,    0,    0,    0,    0,    0,   -6,  261,    0,    0,
   33,    0,  255,    0,    0,   83,    0,    0,    0,    0,
    0,    0,  -40,    0,    0,  350,  212,   58,    0,    0,
    0,    0,    0,    0,    0,   95,    0,    0,    0,    0,
    0,    0,    0,   70,    0,    0,  163,    0,    0,   -6,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                        24,
   84,    0,  331,    0,  331,    0,    0,    0,  479,   87,
  525,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  360,  175,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -31,  321,    0,    0,    0,    0,    0,  113,    0,  118,
    0,  545,   76,    0,    0,  108,    6,    0,    0,    0,
  525,    0,    0,  525,    0,  157,    0,  162,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   81,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  169,  169,    0,    0,    0,  114,    0,    0,
  454,    0,  171,    0,    0,    0,    0,  489,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -5,   29,
    0,  -24,    0,    0,    0,    0,    0,    6,    0,    0,
 -180,    0,  369,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  172,    0,    0,    0,    0,    0,
    0,   12,  396,  438,    0,    0,    0,   72,    0,    0,
    0,    0,    0,    0,  169,    0,  183,    0,  169,    0,
    0,  183,    0,    0,    0,    0,    0,    0,    0,    0,
  501,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -26,  126,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    0,
    0,  187,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   -9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   14,   -1,   13,    0,    0,    0,  517,    0,    0,   -8,
  511,   41,    0,  258,   21,  443,   22,    0,    0,    0,
    0,    0,    0,  -21,  693,    0,    0,    0,    0,  -33,
  414,  -39,    0,    0,    0,    0,    0,    0,    0,    0,
  361,    0,    0,  103,    0,    0,    0,    0,  340,    0,
    0,  398,  335,  229,    0,    0, -101,    0,    0,    0,
    0,  453,    0,  390,  -29,
};
final static int YYTABLESIZE=868;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         96,
   30,   55,    2,   69,  206,   55,  232,   62,   91,  169,
   47,   18,   95,  255,  143,  141,  108,  143,  263,  116,
   62,  117,   16,    4,   21,   37,    6,   81,   15,  130,
   90,  107,  154,  139,   55,   40,   85,   55,   47,  210,
   30,   55,  121,  244,  111,   55,  139,   41,  140,  168,
   57,   25,   71,  123,   57,   55,  108,   57,   88,   86,
  106,  175,  213,   96,   49,   46,  124,   47,   97,   48,
  105,   55,  121,   16,  183,   89,  148,  132,   55,  169,
  107,  121,   43,   57,  172,   16,   57,  121,   42,  148,
   57,  122,  232,  160,   57,  194,   47,   16,  227,  121,
  167,  116,   99,  117,   57,   83,  116,   84,  117,   16,
  251,   55,   99,  226,   55,   99,  112,  161,  191,  174,
   57,  122,  170,  121,   69,   30,  184,   57,   16,   49,
  122,  125,   99,  126,   48,  193,   25,  214,  270,   47,
   16,   69,  127,  151,  187,  231,    4,  186,  122,  236,
  128,  121,   96,  269,  160,   62,   15,  160,  121,   16,
   57,   67,   96,   57,  157,  158,  146,   55,   72,  146,
   16,   55,  122,  152,  162,  165,  155,   55,  168,  176,
   55,   16,  266,  188,   16,  129,  208,  211,  233,  219,
  215,  116,  233,  117,  272,   55,  196,  212,  220,  108,
  122,  242,   16,  221,  252,  222,  218,  122,  234,   94,
  254,   72,   42,  235,   14,  262,   57,  246,  237,  205,
   57,  239,    9,   44,  169,  163,   57,  139,   16,   57,
  148,  108,  245,    9,  115,   86,   61,  121,   82,    9,
   71,  185,  140,  148,   57,   96,  107,   16,   10,   11,
   16,   44,   12,  243,   13,  230,   14,   30,    1,    3,
  247,  180,  148,  123,  101,   71,   30,   30,   25,  238,
   30,  168,   30,   16,   30,  148,  253,   25,   25,    9,
   44,   25,  248,   25,  180,   25,  122,  273,  259,    8,
    9,  150,  159,   49,   46,  260,   47,  121,   48,   10,
   11,   70,    9,   12,  258,   13,  149,   14,    9,   44,
  225,   10,   11,    8,    9,    9,  115,   13,   71,   14,
    9,  115,   47,   10,   11,    8,    9,   12,   99,   13,
   17,   14,  153,   72,  264,   10,   11,   69,  261,   12,
    6,   13,  166,   14,  121,    9,  179,  113,   49,   46,
  268,   47,   44,   48,   10,   11,  129,    9,  217,    3,
   13,   75,   14,   75,   75,   75,   10,   11,  163,  217,
   12,   99,   13,  165,   14,    8,    9,   65,   66,   75,
   75,   75,   75,   47,  138,   10,   11,    9,  137,   12,
   42,   13,  116,   14,  117,   12,   10,   11,    9,   47,
   12,    9,   13,  136,   14,    9,  115,   10,   11,  138,
   10,   11,  128,   13,   12,   14,   13,  129,   14,    9,
   49,   46,   88,   47,  177,   48,  164,  130,   10,   11,
   14,   14,   12,  116,   13,  117,   14,   13,  175,   89,
   14,   14,  176,  240,   14,    9,   14,   38,   14,  103,
  203,  201,  202,  173,   10,   11,   49,   46,   12,   47,
   13,   48,   14,   50,    9,   49,   46,    9,   47,  195,
   48,  156,  209,   10,   11,  265,   10,   11,    9,   13,
   49,   14,   13,   47,   14,   48,   49,   10,   11,   47,
    9,   48,  177,   13,   79,   14,   79,   79,   79,   10,
   11,    9,  114,  147,  148,   13,    0,   14,    9,   44,
   10,   11,   79,   79,   79,   79,   13,    0,   14,   99,
   99,   99,   99,   99,    0,   99,    0,    0,    0,   78,
    0,   78,   78,   78,    9,   44,    0,   99,   99,   99,
   99,   76,    0,   76,   76,   76,    0,   78,   78,   78,
   78,   80,    0,   49,   46,    0,   47,    0,   48,   76,
   76,   76,   76,    9,   44,   45,  104,  104,  123,  104,
    0,  104,  116,    0,  117,    0,   75,   75,   75,    0,
   75,   75,   75,   75,    0,   75,  103,  103,  224,  103,
    0,  103,    0,    0,  192,    9,   44,    0,    0,    0,
  134,  135,  171,    0,    0,    0,    9,  115,    0,    0,
  241,    9,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  123,    0,    0,
    0,    0,    0,    0,  189,    9,   44,    0,    0,    0,
    0,    0,   87,    0,    0,    0,    0,    9,  115,    0,
  197,  198,  199,  200,    0,    0,    0,    0,    0,    0,
    0,  216,    0,    0,    0,    0,    0,    0,    0,    0,
  166,    9,   44,    0,    0,    0,    0,    0,    0,    0,
    9,   44,   45,  228,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  190,    9,   44,    0,    0,    0,
    0,    9,   44,    0,    0,   73,    0,  123,    0,   79,
   79,   79,    0,   79,   79,   79,   79,    0,   79,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  102,    0,
    0,    0,    0,    0,   99,   99,   99,  257,   99,   99,
   99,   99,   99,   99,   78,   78,   78,    0,   78,   78,
   78,   78,    0,   78,    0,   73,   76,   76,   76,    0,
   76,   76,   76,   76,  102,   76,  271,   79,    9,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  274,  104,  104,    0,    0,  223,    9,  115,    0,    0,
    0,    0,    0,  102,    0,    0,  181,    0,    0,    0,
    0,  103,  103,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   73,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  181,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  181,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
    0,   10,    0,   12,   41,   14,   41,   40,   17,   41,
   45,    0,   59,   59,   41,   44,   41,   44,   59,   43,
   40,   45,   40,    0,    3,    5,    0,   14,  123,   63,
   17,   41,   72,  256,   43,  257,   15,   46,   45,  141,
   40,   50,   51,   59,   46,   54,   41,  125,  271,  257,
   10,   40,   45,   59,   14,   64,   43,   17,   44,  270,
   40,  101,  270,   85,   42,   43,   54,   45,  123,   47,
  123,   80,   81,   40,  104,   61,  257,   64,   87,   88,
  257,   90,   40,   43,   93,   40,   46,   59,   46,  270,
   50,   51,   41,   80,   54,  129,   45,   40,   44,  108,
   87,   43,   59,   45,   64,  123,   43,  125,   45,   40,
  212,  120,   41,   59,  123,   44,  258,   59,  120,   98,
   80,   81,   59,  132,   44,  125,  105,   87,   40,   42,
   90,  256,   61,   59,   47,  123,  125,  146,   44,   45,
   40,   61,  256,  264,   41,  175,  123,   44,  108,  179,
   41,  160,  174,   59,   41,   40,  123,   44,  167,   40,
  120,   40,  184,  123,   59,  256,   41,  176,  123,   44,
   40,  180,  132,   71,  125,  257,   74,  186,  257,   40,
  189,   40,  125,   41,   40,  268,  257,   41,  176,   41,
  257,   43,  180,   45,  125,  204,  269,   44,  125,  186,
  160,  189,   40,  125,  213,   41,   59,  167,   59,  256,
  256,  123,   46,  256,   40,  256,  176,  204,  256,  256,
  180,  257,  257,  258,  256,  125,  186,  256,   40,  189,
  257,  256,   59,  257,  258,  270,  256,  246,  256,  257,
  273,  265,  271,  270,  204,  267,  256,   40,  266,  267,
   40,  258,  270,  269,  272,  125,  274,  257,  256,  257,
   41,   40,  257,  269,  123,  258,  266,  267,  257,  125,
  270,  257,  272,   40,  274,  270,   59,  266,  267,  257,
  258,  270,  123,  272,   40,  274,  246,  125,  256,  256,
  257,   44,   41,   42,   43,   41,   45,  269,   47,  266,
  267,  256,  257,  270,   44,  272,   59,  274,  257,  258,
  256,  266,  267,  256,  257,  257,  258,  272,  273,  274,
  257,  258,   45,  266,  267,  256,  257,  270,  257,  272,
    0,  274,  125,  123,  123,  266,  267,  257,  256,  270,
  257,  272,  256,  274,  269,  257,  125,   41,   42,   43,
  256,   45,  258,   47,  266,  267,  268,  257,  125,    0,
  272,   41,  274,   43,   44,   45,  266,  267,  256,  125,
  270,  264,  272,  256,  274,  256,  257,  256,  257,   59,
   60,   61,   62,   45,   40,  266,  267,  257,   44,  270,
   46,  272,   43,  274,   45,    0,  266,  267,  257,   45,
  270,  257,  272,   59,  274,  257,  258,  266,  267,   41,
  266,  267,  256,  272,  270,  274,  272,  256,  274,  257,
   42,   43,   44,   45,  256,   47,  256,  256,  266,  267,
  256,  257,  270,   43,  272,   45,  274,    0,  256,   61,
  266,  267,  256,  186,  270,  257,  272,    5,  274,   36,
   60,   61,   62,   93,  266,  267,   42,   43,  270,   45,
  272,   47,  274,   40,  257,   42,   43,  257,   45,  130,
   47,   74,  138,  266,  267,  247,  266,  267,  257,  272,
   42,  274,  272,   45,  274,   47,   42,  266,  267,   45,
  257,   47,  103,  272,   41,  274,   43,   44,   45,  266,
  267,  257,   50,  256,  257,  272,   -1,  274,  257,  258,
  266,  267,   59,   60,   61,   62,  272,   -1,  274,   41,
   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,   41,
   -1,   43,   44,   45,  257,  258,   -1,   59,   60,   61,
   62,   41,   -1,   43,   44,   45,   -1,   59,   60,   61,
   62,   40,   -1,   42,   43,   -1,   45,   -1,   47,   59,
   60,   61,   62,  257,  258,  259,   42,   43,   52,   45,
   -1,   47,   43,   -1,   45,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,   42,   43,   59,   45,
   -1,   47,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
  256,  257,   92,   -1,   -1,   -1,  257,  258,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  111,   -1,   -1,
   -1,   -1,   -1,   -1,  118,  257,  258,   -1,   -1,   -1,
   -1,   -1,  264,   -1,   -1,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  151,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,  259,  173,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   13,   -1,  191,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   36,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,  227,  260,  261,
  262,  263,  264,  265,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,   -1,   63,  256,  257,  258,   -1,
  260,  261,  262,  263,   72,  265,  256,  256,  257,  258,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  270,  257,  258,   -1,   -1,  256,  257,  258,   -1,   -1,
   -1,   -1,   -1,  101,   -1,   -1,  104,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  129,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  154,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  175,
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
//#line 775 "Parser.java"
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
case 125:
//#line 580 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 582 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 599 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 601 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 132:
//#line 616 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 133:
//#line 621 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 134:
//#line 624 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 136:
//#line 636 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 139:
//#line 653 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 142:
//#line 665 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 146:
//#line 683 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 147:
//#line 685 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 150:
//#line 697 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 151:
//#line 706 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 152:
//#line 711 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 153:
//#line 713 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 154:
//#line 715 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 155:
//#line 717 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 156:
//#line 726 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 158:
//#line 736 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 159:
//#line 743 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 160:
//#line 748 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 161:
//#line 757 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 162:
//#line 762 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 164:
//#line 773 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 165:
//#line 776 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 166:
//#line 778 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 170:
//#line 801 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 171:
//#line 806 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 172:
//#line 808 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 173:
//#line 810 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 174:
//#line 812 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 176:
//#line 823 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 177:
//#line 826 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1364 "Parser.java"
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
