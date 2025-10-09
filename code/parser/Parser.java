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






//#line 10 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
    package parser;

    import lexer.Lexer;
    import common.Token;
    import common.SymbolTable;
    import utilities.Printer;
//#line 32 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
   26,    8,    8,    8,    8,   32,   32,   32,   32,   33,
   33,   33,   33,   40,   41,   41,   42,   42,   43,   43,
    9,    9,    9,    1,    1,    1,    1,    1,    6,    6,
    2,    2,    2,    2,    4,    4,    4,    7,    7,    3,
    3,    3,    5,    5,    5,   11,   11,   10,   10,   44,
   44,   44,   44,   44,   45,   45,   46,   46,   46,   46,
   46,   46,   46,   38,   38,   38,   38,   38,   47,   47,
   39,   39,   39,   48,   49,   49,   49,   50,   27,   27,
   53,   53,   52,   51,   51,   54,   54,   54,   56,   56,
   55,   55,   55,   57,   57,   57,   35,   35,   35,   35,
   35,   12,   13,   13,   14,   14,   36,   36,   59,   59,
   59,   59,   58,   60,   60,   37,   37,   37,   37,   37,
   63,   63,   63,   62,   61,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    4,    4,    3,    2,
    4,    2,    4,    3,    3,    1,    2,    1,    2,    1,
    1,    3,    2,    1,    3,    3,    2,    2,    1,    1,
    3,    1,    3,    2,    3,    1,    3,    1,    1,    2,
    1,    1,    1,    1,    1,    1,    2,    1,    3,    3,
    2,    2,    2,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    6,    6,    5,    5,    2,    0,    2,
    3,    3,    2,    2,    1,    1,    2,    2,    6,    5,
    1,    2,    3,    1,    0,    1,    3,    1,    2,    2,
    3,    2,    2,    0,    1,    1,    5,    5,    4,    3,
    2,    4,    1,    3,    3,    1,    3,    3,    1,    2,
    1,    0,    3,    1,    1,    4,    4,    5,    4,    5,
    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   37,   38,   39,   40,
   41,   42,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   96,  164,    0,    0,   88,   89,    0,    0,
    0,   82,    0,    0,   91,   92,    0,    0,    0,  118,
    0,    0,    0,    0,   50,    0,    0,    0,    0,  123,
    0,    0,   30,    0,   31,    0,    0,    0,  151,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,   66,
    0,    0,    0,    0,   43,   36,   22,   18,    0,   29,
   27,   62,   60,    0,    0,   34,    0,    0,   99,    0,
    0,  153,    0,   97,    0,    0,   94,   79,   80,    0,
   86,    0,   93,   95,    0,   84,   90,  157,  158,  102,
    0,  109,  111,  110,  112,  113,  107,  108,    0,    0,
  103,  101,   48,   55,   47,    0,    0,  146,  145,    0,
    0,    0,  136,  138,    0,   49,   54,   46,    0,    0,
  128,   33,    0,    0,  124,  121,  122,    0,    0,  150,
    0,    0,    0,    0,    0,    0,    0,   67,   59,   64,
   68,    0,    0,    0,   71,    0,    0,    0,    0,    0,
    0,   35,    0,    0,    0,    0,  152,  163,    0,   76,
    0,   83,   81,  104,  100,    0,    0,    0,   52,    0,
  140,    0,    0,    0,  143,   53,    0,   32,  149,    0,
   19,   20,  175,   58,   57,   56,    0,   65,   13,   63,
   61,    0,   73,    0,    0,    0,  166,  167,    0,  169,
  155,  154,   87,   85,  120,  117,    0,  116,    0,    0,
  131,  130,  137,  141,   51,  148,  147,   69,   72,  168,
  174,  170,  115,  114,    0,  129,  132,    0,  133,
};
final static short yydgoto[] = {                          3,
   62,   51,   52,  120,  121,  122,   53,   68,  184,   54,
   55,   56,  111,  112,   18,   19,  268,   39,    4,  173,
   21,   99,   22,   23,   24,   25,   26,  101,   74,   75,
  108,   27,   28,   29,   30,   31,   32,   33,   34,   35,
   93,   94,  228,   63,   64,  139,  208,   76,   77,   78,
  151,  251,  252,  152,  153,  154,  155,   57,   58,   59,
   36,  193,  189,
};
final static short yysindex[] = {                       -86,
    3,   24,    0, -106,    0,    0,  -79,   83,  491,  515,
   69,   46,  521,  -36, -251,  402,  -54,    0,    0,  139,
  -67,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,    0,    0,   16,  153,    0,    0,    0,    0,
 -190,   31,    0,    0,   99, -175,    0,    0,  108,  165,
   15,    0,   52, -154,    0,    0,   47, -141,    0,    0,
  534,  291,  164,  -28,    0,  121,  -42,   48, -151,    0,
  527,  167,    0,  -12,    0,   77, -136,    0,    0,  541,
  383,   13,    0,    0,   76, -132,   31,   31, -114,    0,
  392, -154,   -1,    5,    0,    0,    0,    0,   88,    0,
    0,    0,    0,   -1,  167,    0,  105,  186,    0,   50,
   36,    0,   15,    0,    0,  117,    0,    0,    0,   15,
    0,  158,    0,    0,  334,    0,    0,    0,    0,    0,
  -23,    0,    0,    0,    0,    0,    0,    0,   31, -108,
    0,    0,    0,    0,    0,  -95,  -42,    0,    0, -213,
  125,  129,    0,    0, -220,    0,    0,    0,  -83,   -1,
    0,    0,  199,    0,    0,    0,    0,  118,  146,    0,
    0,   51,   56,  134,  405,  348,  136,    0,    0,    0,
    0,   -1,  102,  373,    0,  202,   43,  124,  -70,  105,
   -7,    0,    0,  -62,  -69,   31,    0,    0,  342,    0,
   15,    0,    0,    0,    0,  165,  164,   37,    0,  155,
    0,   74, -213, -114,    0,    0,  140,    0,    0,  -45,
    0,    0,    0,    0,    0,    0,  154,    0,    0,    0,
    0,   -1,    0,  -50,    0,  168,    0,    0,  -39,    0,
    0,    0,    0,    0,    0,    0,  -43,    0,   96,   65,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   88,    0,    0,  116,    0,
};
final static short yyrindex[] = {                        10,
    0,  243,    0,  243,    0,    0,    0,  436,   14,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  244,
  128,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -19,
  446,    0,    0,    0,    0,    0,   27,    0,   29,    0,
    0,   -9,    0,    0,    0,  -15,   -5,    0,    0,    0,
    0,    0,    0,    0,    0,   39,    0,   41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,   73,    0,   38,
    0,    0,  458,    0,   82,    0,    0,    0,    0,  468,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   72,
    0,    0,    0,    0,    0,    0,   -5,    0,    0, -202,
    0,  223,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -154,    0,    0,    0,    0,    0,    0,
   12,  316,  330,    0,    0,    0,  -35,    0,    0,    0,
    0,    0,    0,    0,    0,   73,    0,   84,    0,   73,
    0,    0,   84,    0,    0,    0,    0,    0,    0,    0,
  480,    0,    0,    0,    0,   -2,    0,    0,    0,    0,
    0,    0,  -17,   60,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,   91,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  738,    2,  431,    0,    0,    0,  424,    0,    0,    9,
  442,   49,    0,  174,  345,  357,   40,    0,    0,    0,
    0,    0,  -20,  581,    0,    0,    0,    0,  -55,  327,
  -57,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  270,    0,    0,   61,  310,    0,    0,    0,    0,  301,
  233,  132,    0,    0, -143,    0,    0,    0,    0,  340,
    0,  285,  -68,
};
final static int YYTABLESIZE=934;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   28,  150,    6,   15,   96,   98,  211,  140,   98,    4,
   16,   16,  142,  257,  163,  264,   14,  205,   86,   69,
   16,  165,   16,  139,   92,   98,  139,   61,   16,   48,
   45,  106,   46,  235,   47,  135,  177,   46,  105,  194,
   28,   20,  148,   46,   16,   40,  113,  186,   89,  214,
   17,   23,   70,   85,  144,   98,   48,  149,  123,  104,
   17,   47,   17,   15,   97,   90,  109,  144,   17,  253,
  123,   16,   48,   45,  103,   46,  197,   47,  156,  196,
   16,  156,  114,  235,   17,   15,   83,   46,   84,  123,
  100,  159,  118,   16,  119,  248,   46,  178,  124,  123,
  142,  127,  181,  142,   15,  128,  158,   16,   67,   68,
  124,   17,  160,   16,  129,   15,   16,  234,  123,  167,
   17,  239,   42,  201,  174,   28,   68,   15,   41,  124,
  119,  161,    4,   17,  164,  166,   23,  171,  183,  124,
   48,   15,  177,   46,  187,   47,   14,   17,  115,   48,
   45,  245,   46,   17,   47,   15,   17,  198,  124,  207,
  147,  209,   97,  215,  146,  212,   41,   12,   72,    1,
    2,   16,  213,  216,  223,  221,  219,  123,   15,  145,
  222,   41,  237,  123,  123,  238,  220,  241,  118,  267,
  119,   16,   15,  240,   16,  249,  250,  258,  255,   48,
  172,   95,   46,   15,   47,  260,   15,  118,  261,  119,
  256,   17,  263,  148,  123,   16,  262,  124,  265,   82,
    8,   98,  254,  124,  124,  191,  229,  141,  149,    9,
   10,   17,  204,   11,   17,   12,  165,   13,   15,  144,
  269,  191,   15,    3,    8,   43,  106,   97,   98,    8,
   43,  144,  144,  105,  124,   17,   43,   28,   16,    5,
   71,  177,   86,  134,  144,   70,   28,   28,   23,  162,
   28,  102,   28,   16,   28,  105,   16,   23,   23,    7,
    8,   23,  159,   23,  161,   23,   72,    8,   43,    9,
   10,  162,  246,   11,  125,   12,  126,   13,   17,    8,
   43,   70,    8,  156,  157,  247,    8,  117,    8,   43,
  190,    9,   10,   17,  195,   10,   17,   12,   71,   13,
    7,    8,   68,  218,   65,   66,  218,  119,  173,   11,
    9,   10,    8,  118,   11,  119,   12,  160,   13,  171,
  119,    9,   10,    7,    8,   11,  172,   12,   37,   13,
  138,  136,  137,    9,   10,    8,   43,   11,    8,   12,
   38,   13,  107,  182,    8,   43,   44,    9,   10,  242,
  131,   11,    8,   12,  165,   13,  143,  144,   46,  210,
  266,    9,   10,   12,   12,   11,   46,   12,  116,   13,
  118,  188,  119,   12,   12,    8,    0,   12,    0,   12,
    0,   12,    8,  117,    9,   10,  226,    0,   11,    8,
   12,    0,   13,  200,    8,   43,  232,   46,    9,   10,
    8,    8,  117,    8,   12,  118,   13,  119,    0,    9,
   10,  231,    9,   10,  118,   12,  119,   13,   12,    0,
   13,  170,    8,   48,   45,   89,   46,  118,   47,  119,
  179,    9,   10,    0,    0,    8,    0,   12,    8,   13,
    0,    0,   90,  224,    9,   10,    0,    9,   10,    0,
   12,    0,   13,   12,  125,   13,   98,   98,   98,   98,
   98,    0,   98,  126,    0,    0,   74,    0,   74,   74,
   74,    0,    0,    0,   98,   98,   98,   98,   78,    0,
   78,   78,   78,    0,   74,   74,   74,   74,   77,    0,
   77,   77,   77,    0,    0,    0,   78,   78,   78,   78,
   75,    0,   75,   75,   75,    0,   77,   77,   77,   77,
   49,    0,   48,   45,  180,   46,  125,   47,   75,   75,
   75,   75,    0,  199,    0,  185,    0,    8,  117,    0,
  132,  133,  134,  135,   61,  203,   48,   45,    0,   46,
   80,   47,   48,   45,    0,   46,   61,   47,   48,   45,
    0,   46,    0,   47,  130,   48,   45,    0,   46,    0,
   47,  168,   48,   45,    0,   46,    0,   47,    0,  202,
    8,   43,   73,    0,    0,    0,    0,  243,    8,   43,
    0,  217,    0,  225,    8,  117,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  106,  236,    0,    0,
    0,  236,    0,  227,  125,  233,    0,    0,  230,  244,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    8,
  117,    0,    0,   73,    0,    0,    0,    0,    8,  117,
    0,    0,  106,    0,    0,    0,    0,   87,    8,   43,
    0,    8,  117,    0,    0,   88,    0,    0,    0,    0,
    0,    0,    0,  259,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  106,    0,    0,  192,    0,
    0,   98,   98,   98,    0,   98,   98,   98,   98,   98,
   98,   74,   74,   74,    0,   74,   74,   74,   74,    0,
   74,    0,    0,   78,   78,   78,    0,   78,   78,   78,
   78,    0,   78,   77,   77,   77,    0,   77,   77,   77,
   77,    0,   77,    0,    0,   75,   75,   75,    0,   75,
   75,   75,   75,  192,   75,    0,   50,    8,   43,   44,
   81,    0,    0,   91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  192,    0,    0,    0,
   60,    8,   43,    0,    0,    0,   79,    8,   43,  110,
    0,    0,    0,    8,   43,    0,   50,   73,    0,    0,
    8,   43,    0,    0,    0,    0,    0,    8,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  169,    0,    0,
    0,    0,    0,    0,  175,  176,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  206,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   44,    0,   40,   59,   41,  150,   63,   44,    0,
    2,    0,   41,   59,   72,   59,  123,   41,  270,   11,
   12,   41,   14,   41,   16,   61,   44,   40,   20,   42,
   43,   41,   45,   41,   47,   41,  257,   45,   41,  108,
   40,    2,  256,   45,   36,  125,   45,  105,   44,  270,
    2,   40,   45,   14,  257,  123,   42,  271,   50,   44,
   12,   47,   14,   40,   85,   61,  257,  270,   20,  213,
   62,   63,   42,   43,   59,   45,   41,   47,   41,   44,
   72,   44,  258,   41,   36,   40,  123,   45,  125,   81,
   59,   44,   43,   85,   45,   59,   45,   89,   50,   91,
   41,  256,   94,   44,   40,   59,   59,   99,   40,   44,
   62,   63,  264,  105,  256,   40,  108,  186,  110,  256,
   72,  190,   40,  122,  257,  125,   61,   40,   46,   81,
   59,   71,  123,   85,   74,   59,  125,  125,   99,   91,
   42,   40,  257,   45,   40,   47,  123,   99,   41,   42,
   43,  207,   45,  105,   47,   40,  108,   41,  110,  268,
   40,  257,  183,  155,   44,   41,   46,   40,  123,  256,
  257,  163,   44,  257,   41,  125,   59,  169,   40,   59,
  125,   46,   59,  175,  176,  256,   41,  257,   43,  125,
   45,  183,   40,  256,  186,   41,  123,   44,   59,   42,
  125,  256,   45,   40,   47,  256,   40,   43,   41,   45,
  256,  163,  256,  256,  206,  207,  256,  169,  123,  256,
  257,  257,  214,  175,  176,   40,  125,  256,  271,  266,
  267,  183,  256,  270,  186,  272,  256,  274,   40,  257,
  125,   40,    0,    0,  257,  258,  256,  268,  264,  257,
  258,  257,  270,  256,  206,  207,  258,  257,  250,  257,
  273,  257,  270,   41,  270,  258,  266,  267,  257,  256,
  270,  256,  272,  265,  274,  123,  268,  266,  267,  256,
  257,  270,  256,  272,  256,  274,  123,  257,  258,  266,
  267,  125,  256,  270,  256,  272,  256,  274,  250,  257,
  258,  256,  257,  256,  257,  269,  257,  258,  257,  258,
  125,  266,  267,  265,  265,    0,  268,  272,  273,  274,
  256,  257,  257,  125,  256,  257,  125,  256,  256,    0,
  266,  267,  257,   43,  270,   45,  272,  256,  274,  256,
  269,  266,  267,  256,  257,  270,  256,  272,    4,  274,
   60,   61,   62,  266,  267,  257,  258,  270,  257,  272,
    4,  274,   36,   94,  257,  258,  259,  266,  267,  196,
   61,  270,  257,  272,   74,  274,  256,  257,   45,  147,
  249,  266,  267,  256,  257,  270,   45,  272,   49,  274,
   43,  107,   45,  266,  267,  257,   -1,  270,   -1,  272,
   -1,  274,  257,  258,  266,  267,   59,   -1,  270,  257,
  272,   -1,  274,  256,  257,  258,   44,   45,  266,  267,
  257,  257,  258,  257,  272,   43,  274,   45,   -1,  266,
  267,   59,  266,  267,   43,  272,   45,  274,  272,   -1,
  274,   59,  257,   42,   43,   44,   45,   43,   47,   45,
   59,  266,  267,   -1,   -1,  257,   -1,  272,  257,  274,
   -1,   -1,   61,   59,  266,  267,   -1,  266,  267,   -1,
  272,   -1,  274,  272,   51,  274,   41,   42,   43,   44,
   45,   -1,   47,   53,   -1,   -1,   41,   -1,   43,   44,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   41,   -1,
   43,   44,   45,   -1,   59,   60,   61,   62,   41,   -1,
   43,   44,   45,   -1,   -1,   -1,   59,   60,   61,   62,
   41,   -1,   43,   44,   45,   -1,   59,   60,   61,   62,
   40,   -1,   42,   43,   93,   45,  113,   47,   59,   60,
   61,   62,   -1,  120,   -1,  104,   -1,  257,  258,   -1,
  260,  261,  262,  263,   40,  125,   42,   43,   -1,   45,
   40,   47,   42,   43,   -1,   45,   40,   47,   42,   43,
   -1,   45,   -1,   47,   41,   42,   43,   -1,   45,   -1,
   47,   41,   42,   43,   -1,   45,   -1,   47,   -1,  256,
  257,  258,   12,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  160,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   36,  187,   -1,   -1,
   -1,  191,   -1,  182,  201,  184,   -1,   -1,  256,  199,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,   63,   -1,   -1,   -1,   -1,  257,  258,
   -1,   -1,   72,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  257,  258,   -1,   -1,  264,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  232,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  105,   -1,   -1,  108,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,  163,  265,   -1,    9,  257,  258,  259,
   13,   -1,   -1,   16,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  186,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,  256,  257,  258,   42,
   -1,   -1,   -1,  257,  258,   -1,   49,  207,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   80,   -1,   -1,
   -1,   -1,   -1,   -1,   87,   88,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  139,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  196,
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
"asignacion_simple : variable DASIG expresion error",
"asignacion_simple : variable error expresion ';'",
"asignacion_simple : variable expresion ';'",
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

//#line 872 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
//#line 70 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 77 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 80 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 6:
//#line 83 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 7:
//#line 86 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 11:
//#line 107 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 12:
//#line 110 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 14:
//#line 113 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 15:
//#line 115 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 16:
//#line 117 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 23:
//#line 143 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 27:
//#line 160 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 33:
//#line 185 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 201 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 212 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 228 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 231 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 236 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 240 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 244 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 248 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 258 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 263 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 270 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 316 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 321 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 324 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 327 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 337 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 61:
//#line 339 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 62:
//#line 344 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 63:
//#line 346 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 367 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 70:
//#line 375 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 383 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 73:
//#line 388 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 75:
//#line 402 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 407 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de %s %s.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 77:
//#line 411 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 78:
//#line 418 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 428 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "+"; }
break;
case 80:
//#line 430 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-"; }
break;
case 81:
//#line 437 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 83:
//#line 443 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 450 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 85:
//#line 457 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 87:
//#line 463 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 88:
//#line 470 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "/"; }
break;
case 89:
//#line 472 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "*"; }
break;
case 97:
//#line 499 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;

            notifyDetection(String.format("Constante negativa: %s.",yyval.sval));

            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0";
            } 

            modificarSymbolTable(yyval.sval,val_peek(0).sval);
        }
break;
case 99:
//#line 518 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 100:
//#line 527 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Condición."); }
break;
case 101:
//#line 532 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 102:
//#line 535 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 103:
//#line 538 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 104:
//#line 541 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 106:
//#line 553 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 113:
//#line 569 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 114:
//#line 578 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 115:
//#line 583 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 116:
//#line 585 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 117:
//#line 587 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 118:
//#line 589 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 121:
//#line 605 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 122:
//#line 610 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 123:
//#line 612 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 126:
//#line 629 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 127:
//#line 631 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 130:
//#line 650 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 132:
//#line 662 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 135:
//#line 679 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 138:
//#line 691 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 142:
//#line 709 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 143:
//#line 711 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 146:
//#line 723 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 147:
//#line 732 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 148:
//#line 737 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 149:
//#line 739 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 150:
//#line 741 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 151:
//#line 743 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 152:
//#line 752 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 154:
//#line 762 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 155:
//#line 769 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 156:
//#line 774 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 157:
//#line 783 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 158:
//#line 788 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 160:
//#line 799 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 161:
//#line 802 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 162:
//#line 804 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 166:
//#line 827 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 167:
//#line 832 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 168:
//#line 834 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 169:
//#line 836 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 170:
//#line 838 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 172:
//#line 849 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 173:
//#line 852 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1363 "Parser.java"
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
