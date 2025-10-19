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
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 36 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 32 "Parser.java"




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
    0,    0,   23,    0,   24,    0,    0,    0,   20,   21,
   21,   27,   21,   21,   21,   21,   26,   26,   25,   25,
   22,   22,   22,   28,   28,   30,   30,   33,   33,   34,
   34,   35,   35,   36,   36,   29,   29,   29,   29,   29,
   29,   29,   29,   39,   39,   31,   31,   31,   31,   31,
   31,    9,    9,    9,    9,   37,   37,   37,   37,   38,
   38,   38,   38,   38,   45,   45,   46,   46,   47,   47,
   10,   10,   10,    1,    1,    1,    1,    1,    6,    6,
    2,    2,    2,    2,    4,    4,    4,    7,    7,    3,
    3,    3,    5,    5,    5,   12,   12,   11,   11,   48,
   48,   48,   48,   49,   49,   49,   49,   16,   16,   16,
   16,   16,   16,   16,   43,   43,   50,   50,   50,   50,
   51,   52,   52,   52,   54,   44,   53,   53,   53,   53,
   53,   55,   57,   56,   56,   58,   32,   32,    8,    8,
   59,   59,   60,   60,   60,   62,   62,   61,   61,   61,
   17,   17,   17,   40,   40,   40,   40,   40,   13,   14,
   14,   15,   15,   41,   41,   63,   63,   63,   63,   64,
   64,   42,   42,   42,   42,   42,   19,   19,   19,   18,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    5,
    2,    3,    3,    2,    2,    4,    4,    3,    3,    4,
    6,    4,    6,    5,    3,    1,    2,    1,    2,    1,
    1,    3,    2,    1,    3,    3,    2,    2,    1,    1,
    3,    1,    3,    2,    3,    1,    3,    1,    1,    2,
    1,    1,    1,    1,    1,    1,    2,    1,    3,    3,
    2,    2,    3,    3,    2,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    2,    4,    4,    3,    3,
    1,    0,    2,    1,    0,    2,    3,    3,    3,    3,
    2,    2,    1,    1,    2,    2,    5,    4,    2,    1,
    3,    2,    1,    3,    1,    2,    2,    3,    2,    2,
    0,    1,    1,    5,    5,    4,    3,    2,    4,    1,
    3,    3,    1,    3,    3,    3,    2,    1,    0,    1,
    1,    4,    4,    5,    4,    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,   21,   24,   25,   26,    0,   37,   38,
   39,   40,   41,   42,   44,   45,    0,    4,    0,   23,
    0,    0,   96,  170,    0,    0,   88,   89,    0,    0,
    0,   82,    0,    0,   91,   92,    0,  168,  116,    0,
    0,    0,    0,    0,   51,    0,    0,    0,  158,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,    0,
   66,    0,    0,    0,    0,   43,   36,    0,   34,    0,
    0,   22,   18,    0,   29,   27,    0,  126,    6,   99,
    0,    0,  160,    0,   97,  167,    0,   94,   79,   80,
    0,   86,    0,   93,   95,    0,   84,   90,  165,  164,
  101,    0,  110,  112,  111,  113,  114,  108,  109,    0,
    0,    0,    0,    0,   30,  121,   31,  115,    0,    0,
  102,   48,   55,   47,    0,   49,   54,   46,    0,    0,
    0,    0,  157,    0,    0,    0,    0,  153,  152,    0,
  142,    0,    0,  143,  145,    0,   59,    0,    0,   67,
   58,    0,   68,    0,   33,    0,    0,    0,    0,    0,
    0,   35,    0,  131,    0,    0,    0,    0,    0,  134,
    0,    0,  159,  166,    0,   76,    0,   83,   81,  103,
  100,    0,    0,  123,    0,    0,    0,   52,   53,    0,
  156,    0,   19,   20,  180,  147,  150,    0,    0,  141,
  138,    0,   57,   56,   62,   60,    0,    0,   71,    0,
   65,   32,    0,  178,    0,  173,  172,    0,  175,   13,
  136,  135,  129,  127,  130,  128,  132,  162,  161,   87,
   85,    0,  119,  120,   50,  155,  154,  148,  144,  137,
    0,   64,    0,   73,   69,  174,  177,  176,  118,  117,
   63,   61,   72,
};
final static short yydgoto[] = {                          4,
   61,   51,   52,  111,  112,  113,   53,   17,   67,  228,
   54,   55,   56,  102,  103,  132,  162,   20,  178,    5,
   21,   22,    6,    8,  156,   23,   94,   24,   25,   26,
   27,   28,   96,  136,  137,   91,   29,   30,   31,   32,
   33,   34,   35,   36,   84,   85,  231,   63,   64,  138,
  139,  140,   98,   37,  187,  188,  189,  190,   78,  163,
  164,  165,   57,   58,
};
final static short yysindex[] = {                         3,
   51,    0,    0,    0,  -35,  -99,    0, -226,  -80,    9,
  416,  425, -145,  431,  -40, -214,   47,  408,  -45,   -2,
    0,   66,  -50,    0,    0,    0,    0,   32,    0,    0,
    0,    0,    0,    0,    0,    0, -183,    0,  -99,    0,
 -164,  465,    0,    0,  475, -160,    0,    0,  438,    5,
   16,    0,  338, -151,    0,    0,  -44,    0,    0,  451,
  310,   16,  -24,   68,    0,  485,  286, -146,    0,  458,
  366,    7,    0,    0,   30, -138,  -33,   11,  321, -134,
    0,  401, -151,    8,  -38,    0,    0,   77,    0,   85,
   80,    0,    0,   55,    0,    0,  -20,    0,    0,    0,
   84,   25,    0,   16,    0,    0,   87,    0,    0,    0,
   16,    0,  491,    0,    0,  293,    0,    0,    0,    0,
    0,  -31,    0,    0,    0,    0,    0,    0,    0,   16,
  491,  465,   91,   77,    0,    0,    0,    0, -144, -139,
    0,    0,    0,    0, -121,    0,    0,    0, -118,    8,
   81,   -6,    0,    0,   19,   22,  102,    0,    0, -228,
    0, -224,   33,    0,    0,   24,    0,   57,   99,    0,
    0,  469,    0,    8,    0,   93,   -1,   21,   85,   -9,
 -110,    0,   50,    0,  444,  444,   45,   49, -125,    0,
 -107,  465,    0,    0,  331,    0,   16,    0,    0,    0,
    0,   16,    5,    0,   95,  -29,   92,    0,    0,   94,
    0,   63,    0,    0,    0,    0,    0, -105, -228,    0,
    0,   61,    0,    0,    0,    0,    8,   27,    0,  110,
    0,    0, -100,    0,  116,    0,    0,  -93,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   83,    0,    0,    0,    0,    0,    0,    0,    0,
  480,    0,    8,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                        18,
  -92,    0,    0,    0,    2,  166,    0,    0,    0,  117,
  145,    0,  127,    0, -102,    0,    0,    0,    0, -102,
    0,    4,   36,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  166,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -32,
  139,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  357, -204,    0,    0,  -23,    0,    0,    0,    0,
    0,    0,    0,    0, -102,    0, -223,    0,    0,    0,
    0,    0,   31,    0,    0,    0,    0, -102,    0,  147,
 -230,    0,    0, -102,    0,    0, -102,    0,    0,    0,
   40,    0,    0,  128,    0,    0,    0,    0,    0,    0,
  150,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  379,
    0,    0,  154, -102,    0,    0,    0,    0,   20,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,  174,  181,    0,    0,    0, -223,
    0,    0,    0,    0,    0, -102,    0,    0,   52,    0,
    0,    0,    0,    0,    0, -230,    0,    0,  -64,    0,
    0,    0, -102,    0,    0,  -88,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  156,    0,    0,    0,
    0,  387,  -28,    0, -102,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   53,   13,    0,
    0, -102,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  743,  417,  578,  135,    0,  142,  496,    0,    0,  -19,
  649,  347,  706,    0,  107,    0,    0,    0,  -69,  221,
   23,   44,    0,    0,    0,    0,    0,  -15,  732,    0,
    0,    0,    0,  -72,  225,  -67,    0,    0,    0,    0,
    0,    0,    0,    0,  241,    0,    0,  -71,  296,    0,
    0,  248,    0,    0,    0,    0,    0,  203,    0,    0,
 -141,    0,    0,  345,
};
final static int YYTABLESIZE=937;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   28,   15,    2,    2,   16,   80,   92,  161,  171,  201,
  160,   16,  104,   87,  120,   16,  139,    3,  216,   16,
  176,  181,   81,   15,  186,  179,  171,  158,   38,  253,
    3,  234,  217,  151,  212,   46,  109,   16,  110,  234,
   28,  125,  159,   46,   40,  218,  151,  109,   42,  110,
    7,   23,   46,  146,   41,   76,  146,   48,   75,   92,
  204,   99,   47,   16,  122,  193,  205,  125,  192,   16,
  263,   46,   93,  220,   68,   12,  219,  259,  122,  237,
  163,   70,   73,  163,   74,  262,   77,   15,   97,   16,
   95,   68,  100,  149,   16,   98,  149,  105,  134,  109,
   16,  110,  134,  244,  118,   16,  233,  246,  141,  238,
   65,   66,   98,  241,  242,  224,   16,  150,  157,  180,
   88,  257,  169,  133,  177,   28,  109,  194,  110,  207,
   16,  154,  180,  166,   16,  208,   23,  183,  209,  211,
    3,  270,  215,  213,   41,  239,  214,  185,  221,  248,
  254,  258,  255,  265,  155,  266,  267,   98,   98,   98,
   98,   98,  268,   98,    5,   15,  140,   92,   78,  125,
   78,   78,   78,   10,  240,   98,   98,   98,   98,   74,
   11,   74,   74,   74,  133,  260,   78,   78,   78,   78,
   77,  179,   77,   77,   77,  130,   75,   74,   75,   75,
   75,  175,  131,  169,  179,  179,   92,  261,   77,  222,
   86,  119,  124,  134,   75,   72,   10,  232,  169,  232,
    9,   10,  158,  171,  200,   11,   12,  104,   39,   13,
   11,   12,   10,   14,   13,  184,   10,  159,   14,  252,
   98,   11,   12,  133,   90,   11,   12,   10,   43,   14,
   10,  108,  185,   14,   10,   10,   43,   28,    1,    3,
   76,   10,  108,   11,   12,   43,   28,   28,   23,  151,
   28,   14,   28,  125,   28,  125,  236,   23,   23,    9,
   10,   23,  151,   23,   43,   23,   10,   68,  122,   11,
   12,   12,   12,   13,   70,   11,   12,   14,  249,   13,
  243,   12,   12,   14,  245,   12,   10,   12,   98,   12,
    9,   10,  223,   10,  108,   11,   12,   10,  256,   13,
   11,   12,   10,   14,   13,  174,   11,   12,   14,  149,
   13,   11,   12,   10,   14,   13,   10,   46,  269,   14,
   10,  108,   11,   12,  148,   11,   12,   10,  191,   10,
   14,   10,  109,   14,  110,  122,   11,   12,   11,   12,
   11,   12,   48,   45,   14,   46,   14,   47,   14,  129,
  127,  128,   98,   98,   98,   46,   98,   98,   98,   98,
   98,   98,   46,   78,   78,   78,  206,   78,   78,   78,
   78,  247,   78,  107,   74,   74,   74,  107,    0,   74,
  169,   74,  179,   74,    0,   77,   77,   77,  109,    0,
  110,   75,   75,   75,   77,    0,   74,   74,   74,  105,
   75,   77,  124,   77,  153,  125,    0,  106,   62,   75,
  172,   75,    0,    0,    0,    0,    0,    0,   77,   77,
   77,    0,    0,  109,    0,  110,   75,   75,   75,   48,
   45,   80,   46,    0,   47,   49,    0,   48,   45,  171,
   46,  104,   47,    0,   60,    0,   48,   45,   81,   46,
   70,   47,   48,   45,    0,   46,   62,   47,  106,   48,
   45,    0,   46,   60,   47,   48,   45,    0,   46,    0,
   47,  121,   48,   45,    0,   46,  210,   47,  151,   48,
   45,    0,   46,    0,   47,    0,   48,   45,    0,   46,
    0,   47,  227,   46,    0,    0,   48,    0,  229,   46,
  230,   47,    0,  263,   46,    0,    0,  226,  145,  197,
   41,    0,   48,    0,    0,   46,    0,   47,  272,    0,
    0,  146,  147,  144,    0,    0,  116,  202,  198,   10,
   43,    0,    0,    0,    0,    0,    0,  116,    0,    0,
    0,    0,    0,    0,    0,    0,   10,  108,    0,  123,
  124,  125,  126,  229,  264,    0,  167,   10,   43,    0,
    0,    0,    0,    0,    0,    0,  250,   10,   43,    0,
    0,    0,    0,    0,   10,   43,    0,    0,    0,  116,
    0,   62,   62,    0,    0,    0,  195,  264,    0,  273,
    0,    0,  107,   74,   74,    0,   74,   74,   74,   74,
    0,    0,   10,  108,    0,  195,    0,    0,    0,    0,
  117,    0,    0,    0,  105,   77,   77,    0,   77,   77,
   77,   77,  106,   75,   75,    0,   75,   75,   75,   75,
    0,    0,    0,   18,    0,    0,    0,   10,  108,    0,
    0,   68,    0,   18,   10,   43,   83,    0,   18,    0,
   18,   79,   10,   43,   44,    0,    0,    0,    0,    0,
   59,   10,   43,    0,    0,    0,   69,   10,   43,    0,
    0,    0,  116,  199,   10,   43,   44,  116,  114,    0,
   10,   43,    0,    0,    0,    0,    0,   10,   43,  114,
   19,   18,    0,    0,   10,   43,    0,    0,    0,  114,
   19,   10,   43,   18,  225,   19,   43,   19,  170,    0,
  114,   10,   43,  173,    0,  271,   18,   43,    0,   18,
  142,  143,   18,    0,    0,   18,  196,   10,   43,  114,
    0,   89,    0,   50,  235,  115,   71,  235,    0,    0,
   82,    0,    0,    0,    0,    0,  115,    0,   19,    0,
    0,    0,  251,    0,    0,    0,  115,    0,    0,    0,
   19,   18,   18,    0,  101,    0,    0,  115,    0,    0,
    0,   50,    0,   19,  135,    0,   19,    0,    0,   19,
  114,    0,   19,    0,    0,    0,  115,    0,    0,    0,
    0,    0,  152,    0,   18,    0,  114,    0,    0,   89,
    0,  168,  182,    0,   18,    0,    0,    0,  135,    0,
    0,   18,    0,    0,    0,    0,    0,    0,   19,   19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  114,    0,   18,    0,    0,    0,  115,    0,    0,
    0,    0,    0,    0,  135,   89,    0,    0,    0,    0,
   18,   19,    0,  115,  203,    0,    0,    0,    0,    0,
    0,   19,    0,    0,    0,    0,    0,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  182,  115,    0,
   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   19,    0,    0,
    0,    0,    0,    0,  101,    0,  182,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,    0,    0,   40,   44,   22,   41,   41,   41,
   44,    0,   41,   59,   59,   40,   40,    0,  160,   40,
   88,   91,   61,  123,   97,  256,   59,  256,    6,   59,
  257,   41,  257,  257,   41,   45,   43,   40,   45,   41,
   40,  272,  271,   45,  125,  270,  270,   43,   40,   45,
    0,   40,   45,   41,   46,  270,   44,   42,   15,   75,
  133,   39,   47,   40,  269,   41,  134,  272,   44,   40,
   44,   45,  123,   41,   44,   40,   44,  219,   59,   59,
   41,   45,  123,   44,  125,   59,   40,  123,  272,   40,
   59,   61,  257,   41,   40,   44,   44,  258,  123,   43,
   40,   45,  123,   59,  256,   40,  176,   59,   41,  179,
  256,  257,   61,  185,  186,   59,   40,  264,  257,   40,
  123,   59,  257,  268,   40,  125,   43,   41,   45,  269,
   40,  125,   40,  123,   40,  257,  125,   94,  257,   59,
  123,   59,   41,  125,   46,  256,  125,  273,  125,  257,
   59,  257,   59,   44,  125,  256,   41,   41,   42,   43,
   44,   45,  256,   47,  257,    0,   40,  183,   41,  272,
   43,   44,   45,    0,  125,   59,   60,   61,   62,   41,
    0,   43,   44,   45,  273,  125,   59,   60,   61,   62,
   41,  256,   43,   44,   45,   61,   41,   59,   43,   44,
   45,  125,   61,   59,  125,   59,  222,  227,   59,  166,
  256,  256,   59,  123,   59,  256,  257,  125,  257,  125,
  256,  257,  256,  256,  256,  266,  267,  256,    8,  270,
  266,  267,  257,  274,  270,  256,  257,  271,  274,  269,
  264,  266,  267,  268,   20,  266,  267,  257,  258,  274,
  257,  258,  273,  274,  257,  257,  258,  257,  256,  257,
  270,  257,  258,  266,  267,  258,  266,  267,  257,  257,
  270,  274,  272,  272,  274,  272,  256,  266,  267,  256,
  257,  270,  270,  272,  258,  274,  257,  257,  269,  266,
  267,  256,  257,  270,  258,  266,  267,  274,  192,  270,
  256,  266,  267,  274,  256,  270,  257,  272,  257,  274,
  256,  257,  256,  257,  258,  266,  267,  257,  256,  270,
  266,  267,  257,  274,  270,   85,  266,  267,  274,   44,
  270,  266,  267,  257,  274,  270,  257,   45,  256,  274,
  257,  258,  266,  267,   59,  266,  267,  257,  265,  257,
  274,  257,   43,  274,   45,   60,  266,  267,  266,  267,
  266,  267,   42,   43,  274,   45,  274,   47,  274,   60,
   61,   62,  256,  257,  258,   45,  260,  261,  262,  263,
  264,  265,   45,  256,  257,  258,  139,  260,  261,  262,
  263,  189,  265,   49,  256,  257,  258,   41,   -1,   43,
  256,   45,  256,  265,   -1,  256,  257,  258,   43,   -1,
   45,  256,  257,  258,  265,   -1,   60,   61,   62,   41,
  265,   43,  269,   45,   59,  272,   -1,   41,   12,   43,
   84,   45,   -1,   -1,   -1,   -1,   -1,   -1,   60,   61,
   62,   -1,   -1,   43,   -1,   45,   60,   61,   62,   42,
   43,   44,   45,   -1,   47,   40,   -1,   42,   43,   59,
   45,   45,   47,   -1,   40,   -1,   42,   43,   61,   45,
   40,   47,   42,   43,   -1,   45,   60,   47,   41,   42,
   43,   -1,   45,   40,   47,   42,   43,   -1,   45,   -1,
   47,   41,   42,   43,   -1,   45,  150,   47,   41,   42,
   43,   -1,   45,   -1,   47,   -1,   42,   43,   -1,   45,
   -1,   47,   44,   45,   -1,   -1,   42,   -1,  172,   45,
  174,   47,   -1,   44,   45,   -1,   -1,   59,   44,  113,
   46,   -1,   42,   -1,   -1,   45,   -1,   47,   59,   -1,
   -1,  256,  257,   59,   -1,   -1,   51,  131,  256,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   62,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,
  261,  262,  263,  227,  228,   -1,  256,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,  104,
   -1,  185,  186,   -1,   -1,   -1,  111,  261,   -1,  263,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,   -1,  257,  258,   -1,  130,   -1,   -1,   -1,   -1,
   53,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,   -1,   -1,    5,   -1,   -1,   -1,  257,  258,   -1,
   -1,   13,   -1,   15,  257,  258,   18,   -1,   20,   -1,
   22,  264,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,  256,  257,  258,   -1,
   -1,   -1,  197,  116,  257,  258,  259,  202,   50,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,  257,  258,   61,
    5,   63,   -1,   -1,  257,  258,   -1,   -1,   -1,   71,
   15,  257,  258,   75,  256,   20,  258,   22,   80,   -1,
   82,  257,  258,   85,   -1,  256,   88,  258,   -1,   91,
  256,  257,   94,   -1,   -1,   97,  256,  257,  258,  101,
   -1,   20,   -1,   11,  177,   50,   14,  180,   -1,   -1,
   18,   -1,   -1,   -1,   -1,   -1,   61,   -1,   63,   -1,
   -1,   -1,  195,   -1,   -1,   -1,   71,   -1,   -1,   -1,
   75,  133,  134,   -1,   42,   -1,   -1,   82,   -1,   -1,
   -1,   49,   -1,   88,   63,   -1,   91,   -1,   -1,   94,
  152,   -1,   97,   -1,   -1,   -1,  101,   -1,   -1,   -1,
   -1,   -1,   70,   -1,  166,   -1,  168,   -1,   -1,   88,
   -1,   79,   91,   -1,  176,   -1,   -1,   -1,   97,   -1,
   -1,  183,   -1,   -1,   -1,   -1,   -1,   -1,  133,  134,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  203,   -1,  205,   -1,   -1,   -1,  152,   -1,   -1,
   -1,   -1,   -1,   -1,  133,  134,   -1,   -1,   -1,   -1,
  222,  166,   -1,  168,  132,   -1,   -1,   -1,   -1,   -1,
   -1,  176,   -1,   -1,   -1,   -1,   -1,   -1,  183,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  176,  203,   -1,
  205,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  222,   -1,   -1,
   -1,   -1,   -1,   -1,  192,   -1,  205,
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
"programa : nombre_programa cuerpo_programa",
"programa : nombre_programa conjunto_sentencias",
"$$1 :",
"programa : $$1 cuerpo_programa",
"$$2 :",
"programa : error $$2 nombre_programa cuerpo_programa",
"programa : error EOF",
"programa : EOF",
"nombre_programa : ID",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa : '{' conjunto_sentencias lista_llaves_cierre",
"$$3 :",
"cuerpo_programa : lista_llaves_apertura $$3 conjunto_sentencias '}'",
"cuerpo_programa : '{' '}'",
"cuerpo_programa :",
"cuerpo_programa : '{' error '}'",
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
"sentencia_control : iteracion",
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
"asignacion_multiple : variable asignacion_par constante lista_constantes ';'",
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
"condicion : '(' cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' cuerpo_condicion error",
"cuerpo_condicion : expresion comparador expresion",
"cuerpo_condicion : expresion termino_simple",
"cuerpo_condicion : expresion operador_suma termino",
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
"declaracion_funcion : inicio_funcion conjunto_parametros '{' conjunto_sentencias '}'",
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
"imprimible : '(' elemento_imprimible ')'",
"imprimible : '(' ')'",
"imprimible : elemento_imprimible",
"imprimible :",
"elemento_imprimible : STR",
"elemento_imprimible : expresion",
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

//#line 1004 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private MessageCollector errorCollector, warningCollector;

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
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

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private String appendScope(String lexema) {
    return lexema + ":" + this.scopeStack.asText();
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 784 "Parser.java"
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
//#line 73 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Programa.");
            } else {
                errorState = false;
            }
        }
break;
case 2:
//#line 84 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 87 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 89 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); }
break;
case 7:
//#line 92 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 95 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 102 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); }
break;
case 11:
//#line 113 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 116 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 119 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 121 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 123 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 23:
//#line 149 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 190 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 208 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 219 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 236 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 239 "gramatica.y"
{
            notifyDetection("Declaración de variable.");
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(1).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(1).sval,scopeStack.asText());
        }
break;
case 48:
//#line 249 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 253 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 257 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 51:
//#line 261 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 53:
//#line 271 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 276 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 283 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 297 "gramatica.y"
{ 
            notifyDetection("Asignación simple."); 
            this.symbolTable.setValue(this.appendScope(val_peek(3).sval), val_peek(1).sval);/*yo no pondría esto, cuando $3 es una expresion queda mal*/
            
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(2).sval);
        }
break;
case 57:
//#line 309 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 312 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 315 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 60:
//#line 325 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(1).sval);

            reversePolish.rearrangePairs();

            notifyDetection("Asignación múltiple."); 
        }
break;
case 61:
//#line 334 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(5).sval);
            reversePolish.addPolish(val_peek(3).sval);

            reversePolish.rearrangePairs();

            notifyWarning(String.format("Se descartarán las constantes posteriores a %s",val_peek(3).sval)); /*TP3*/
            notifyDetection("Asignación múltiple.");
        }
break;
case 62:
//#line 347 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 63:
//#line 349 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 64:
//#line 351 "gramatica.y"
{ notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval)); }
break;
case 67:
//#line 365 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 68:
//#line 370 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 69:
//#line 377 "gramatica.y"
{ reversePolish.addPolish(val_peek(1).sval); }
break;
case 70:
//#line 382 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 390 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 73:
//#line 395 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 75:
//#line 409 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 76:
//#line 417 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 77:
//#line 421 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 78:
//#line 428 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 438 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 80:
//#line 440 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 81:
//#line 447 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 83:
//#line 456 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 463 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 85:
//#line 470 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 87:
//#line 479 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 88:
//#line 486 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 89:
//#line 488 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 90:
//#line 497 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 91:
//#line 501 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 93:
//#line 512 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 94:
//#line 516 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 97:
//#line 527 "gramatica.y"
{/* 
            $$ = "-" + $2;

            notifyDetection(String.format("Constante negativa: %s.",$$));

            if(isUint($$)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                $$ = null;
            } 

            modificarSymbolTable($$,$2);
        */
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 98:
//#line 554 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.asText()+":"+val_peek(0).sval)) { /*Si entra por aca, la variable debe ser local*/
                notifyError(String.format("Variable %s no declarada.",val_peek(0).sval));
            }
        }
break;
case 99:
//#line 560 "gramatica.y"
{ 
            if (!this.scopeStack.isReacheable(val_peek(2).sval)) 
                notifyError(String.format("Variable %s no declarada (no visible).",val_peek(0).sval));
            else {
                if (!this.symbolTable.entryExists(this.scopeStack.getScopeRoad(val_peek(2).sval)+val_peek(0).sval))
                    notifyError(String.format("Variable %s no declarada en el ámbito %s.",val_peek(0).sval,val_peek(2).sval));
            }

            yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; 
        }
break;
case 100:
//#line 578 "gramatica.y"
{ 
            if (!errorState) {
                reversePolish.addFalseBifurcation();
                notifyDetection("Condición."); 
            } else {
                errorState = false;
            }
        }
break;
case 101:
//#line 590 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 102:
//#line 594 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 103:
//#line 596 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 104:
//#line 603 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 105:
//#line 610 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 106:
//#line 612 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 107:
//#line 614 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 108:
//#line 621 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 109:
//#line 625 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 114:
//#line 636 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 115:
//#line 645 "gramatica.y"
{ 
            if (!errorState) {
                reversePolish.completeSelection();
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 116:
//#line 658 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 118:
//#line 667 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 119:
//#line 669 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 120:
//#line 671 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 121:
//#line 676 "gramatica.y"
{ reversePolish.addInconditionalBifurcation(); }
break;
case 124:
//#line 688 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 125:
//#line 695 "gramatica.y"
{ reversePolish.registerDoBody(); }
break;
case 127:
//#line 701 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 129:
//#line 707 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 130:
//#line 709 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 131:
//#line 711 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 133:
//#line 722 "gramatica.y"
{}
break;
case 134:
//#line 729 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 135:
//#line 731 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 136:
//#line 738 "gramatica.y"
{ reversePolish.addTrueBifurcation(); }
break;
case 137:
//#line 747 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                this.scopeStack.pop();    
            } else {
                errorState = false;
            }
        }
break;
case 138:
//#line 761 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 139:
//#line 773 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); yyval.sval = val_peek(0).sval; }
break;
case 140:
//#line 778 "gramatica.y"
{
            this.scopeStack.push(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 142:
//#line 793 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 145:
//#line 805 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 148:
//#line 819 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PARAMETER);
            this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            /*hay que guardar la semantica en la tabla*/

        }
break;
case 149:
//#line 830 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 150:
//#line 832 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 151:
//#line 839 "gramatica.y"
{ yyval.sval = "cv"; }
break;
case 152:
//#line 841 "gramatica.y"
{ yyval.sval = "cvr"; }
break;
case 153:
//#line 846 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 154:
//#line 855 "gramatica.y"
{ 
            reversePolish.addPolish("return");
            notifyDetection("Sentencia RETURN."); 
        }
break;
case 155:
//#line 863 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 156:
//#line 865 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 157:
//#line 867 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 158:
//#line 869 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 159:
//#line 878 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 161:
//#line 888 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 162:
//#line 895 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 163:
//#line 900 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 164:
//#line 909 "gramatica.y"
{
            if (!errorState) {
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                errorState = false;
            }
        }
break;
case 165:
//#line 921 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); errorState = false; }
break;
case 167:
//#line 932 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 168:
//#line 934 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); errorState = true; }
break;
case 169:
//#line 936 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 172:
//#line 952 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Expresión lambda.");
                this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);
            } else {
                errorState = false;
            }
        }
break;
case 173:
//#line 964 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 174:
//#line 967 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 175:
//#line 969 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 176:
//#line 971 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 177:
//#line 978 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 178:
//#line 983 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 179:
//#line 986 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 180:
//#line 993 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
        }
break;
//#line 1602 "Parser.java"
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
