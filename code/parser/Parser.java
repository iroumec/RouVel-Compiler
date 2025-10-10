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
   45,   45,   45,   45,   45,   46,   46,   47,   47,   47,
   47,   47,   47,   47,   39,   39,   39,   39,   39,   48,
   48,   40,   40,   40,   49,   50,   50,   50,   51,   28,
   28,   54,   54,   53,   52,   52,   55,   55,   55,   57,
   57,   56,   56,   56,   58,   58,   58,   36,   36,   36,
   36,   36,   12,   13,   13,   14,   14,   37,   37,   60,
   60,   60,   60,   59,   61,   61,   38,   38,   38,   38,
   38,   64,   64,   64,   63,   62,
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
    3,    2,    2,    2,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    6,    6,    5,    5,    2,    0,
    2,    3,    3,    2,    2,    1,    1,    2,    2,    6,
    5,    1,    2,    3,    1,    0,    1,    3,    1,    2,
    2,    3,    2,    2,    0,    1,    1,    5,    5,    4,
    3,    2,    4,    1,    3,    3,    1,    3,    3,    1,
    2,    1,    0,    3,    1,    1,    4,    4,    5,    4,
    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   22,   25,   26,   27,    0,   38,   39,   40,   41,
   42,   43,   45,   46,    0,    0,    9,   10,    5,    0,
   24,    0,    0,   97,  165,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,    0,
  119,    0,    0,    0,    0,   51,    0,    0,    0,    0,
  124,    0,    0,   31,    0,   32,    0,    0,    0,  152,
    0,    0,    0,   18,   15,    0,    0,   60,    0,    0,
   67,    0,    0,    0,    0,   44,   37,   23,   19,    0,
   30,   28,   63,   61,    0,    0,   35,    0,    0,    0,
    7,  100,    0,    0,  154,    0,   98,    0,    0,   95,
   80,   81,    0,   87,    0,   94,   96,    0,   85,   91,
  158,  159,  103,    0,  110,  112,  111,  113,  114,  108,
  109,    0,    0,  104,  102,   49,   56,   48,    0,    0,
  147,  146,    0,    0,    0,  137,  139,    0,   50,   55,
   47,    0,    0,  129,   34,    0,    0,  125,  122,  123,
    0,    0,  151,    0,    0,    0,    0,    0,    0,   68,
   59,   65,   69,    0,    0,    0,   72,    0,    0,    0,
    0,    0,    0,   36,    0,    0,    0,    0,    0,  153,
  164,    0,   77,    0,   84,   82,  105,  101,    0,    0,
    0,   53,    0,  141,    0,    0,    0,  144,   54,    0,
   33,  150,    0,   20,   21,  176,   58,   57,    0,   66,
   14,   64,   62,    0,   74,    0,    0,    0,  167,  168,
    0,  170,   11,  156,  155,   88,   86,  121,  118,    0,
  117,    0,    0,  132,  131,  138,  142,   52,  149,  148,
   70,   73,  169,  175,  171,  116,  115,    0,  130,  133,
    0,  134,
};
final static short yydgoto[] = {                          3,
   63,   52,   53,  123,  124,  125,   54,   69,  186,   16,
   56,   17,  114,  115,   18,   19,  271,   39,    4,    6,
  176,   21,  100,   22,   23,   24,   25,   26,  102,   75,
   76,  109,   27,   28,   29,   30,   31,   32,   33,   34,
   35,   94,   95,  230,   64,   65,  142,  211,   77,   78,
   79,  154,  254,  255,  155,  156,  157,  158,   58,   59,
   60,   36,  195,  191,
};
final static short yysindex[] = {                      -162,
   15,  -26,    0,  -98,    0, -221,  -79,   27,  463,  475,
  -32,   24,  483,  -38, -193,  450,  -43,    0,    0,  104,
  -44,    0,    0,    0,    0,   31,    0,    0,    0,    0,
    0,    0,    0,    0,  -39,  115,    0,    0,    0,  -22,
    0, -174,  147,    0,    0,  163, -156,    0,    0,  368,
  169,   -8,    0,  -42, -153,    0,    0,   45, -151,    0,
    0,   66,  296,  126,  -23,    0,  112,  -21,  284, -150,
    0,   -5,  129,    0,  -36,    0,   68, -131,    0,    0,
   77,  287,    5,    0,    0,  -10, -129,    0,  147, -126,
    0,  332, -153,   25,   28,    0,    0,    0,    0,   48,
    0,    0,    0,    0,   25,  129,    0,   94,  140,   48,
    0,    0,  256,    9,    0,   -8,    0,    0,  100,    0,
    0,    0,   -8,    0,  343,    0,    0,  175,    0,    0,
    0,    0,    0,  -13,    0,    0,    0,    0,    0,    0,
    0,  147, -133,    0,    0,    0,    0,    0, -114,  -21,
    0,    0, -209,  105,  101,    0,    0, -212,    0,    0,
    0, -109,   25,    0,    0,  151,    0,    0,    0,    0,
   90,   55,    0,    0,   26,   34,  109,  218,  107,    0,
    0,    0,    0,   25,   59,   16,    0,  162,  122,   95,
  -99,   94,   37,    0,    0,  -96,   70,  -95,  147,    0,
    0,  276,    0,   -8,    0,    0,    0,    0,  169,  126,
  293,    0,  123,    0,   49, -209, -126,    0,    0,  114,
    0,    0,   32,    0,    0,    0,    0,    0,  124,    0,
    0,    0,    0,   25,    0,  -81,    0,  135,    0,    0,
  -78,    0,    0,    0,    0,    0,    0,    0,    0,   33,
    0,   56,   36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   48,    0,    0,
   81,    0,
};
final static short yyrindex[] = {                        19,
  -75,  181,    0,  181,    0,    0,    0,  396,  -73,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  185,
   93,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    3,  406,    0,    0,    0,    0,    0,  -70,    0,  -69,
    0,    0,   10,    0,    0,    0,  -71,  -15,    0,    0,
    0,    0,    0,    0,    0,    0,  -60,    0,  -59,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   62,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -58,  -58,    0,
    0,    0,   30,    0,    0,  418,    0,  -57,    0,    0,
    0,    0,  428,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  295,    0,    0,    0,    0,    0,    0,  -15,
    0,    0, -201,    0,  159,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -55,    0,    0,    0,
    0,    0,    0,   12,  203,  204,    0,    0,  -12,    0,
    0,    0,    0,    0,    0,    0,    0,  -58,    0,  -49,
    0,  -58,    0,    0,  -49,    0,    0,    0,    0,    0,
    0,    0,    0,  440,    0,    0,    0,    0,   14,    0,
    0,    0,    0,    0,    0,  -31,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   87,    0,
    0,    0,    0,    0,    0,    0,  -47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  743,   11,  376,    0,    0,    0,  -30,    0,    0,  700,
  -46,  748,    0,  110,   23,  207,   29,    0,    0,    0,
    0,    0,    0,  -20,  507,    0,    0,    0,    0,  -40,
  187,  -52,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  132,    0,    0,   -7,  238,    0,    0,    0,    0,
  236,  167,  121,    0,    0, -136,    0,    0,    0,    0,
  312,    0,  258,  -76,
};
final static int YYTABLESIZE=957;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         98,
   29,   15,   47,   62,  105,   49,   46,   68,   47,  140,
   48,   17,  140,   15,    5,   97,  214,  145,    4,  104,
  166,  128,  153,  143,   14,  136,   37,  208,   99,   15,
   20,   99,  196,   49,   62,   40,   49,   46,   48,   47,
   29,   48,   86,  166,  179,   41,  151,  182,   99,  200,
  107,   24,  199,  188,  106,  145,  116,  217,  187,  234,
   47,  152,  111,   15,  164,   98,   43,  167,  145,   47,
  157,   90,   42,  157,  233,   15,   87,  237,   99,  256,
  143,   47,  112,  143,   84,  128,   85,   15,   91,  101,
  260,  267,  202,    1,    2,  223,   14,  121,   15,  122,
  110,  117,  130,  131,  132,   69,  133,   49,   46,   15,
   47,  236,   48,  163,  175,  241,  220,  171,   49,   46,
   15,   47,   69,   48,  170,   29,  169,  177,  185,  174,
  179,   71,   13,  189,  210,  204,   24,  229,  197,  235,
  201,    4,  212,   15,  216,  215,   73,  219,  222,  226,
  224,  150,   42,  239,   15,  149,  240,   42,  225,  242,
  270,  244,  237,  252,   98,   15,   47,  261,   15,  248,
  148,  253,  258,  128,  263,  264,   98,  265,  268,  193,
   16,    6,  163,  231,    3,  160,  162,  262,   49,   46,
   15,   47,   99,   48,  243,  126,  127,  174,  161,  135,
  128,  193,   11,   12,   49,  272,  172,   47,  173,   48,
   38,  121,   96,  122,    8,   44,  103,   83,    8,   47,
    8,   44,  108,   66,   67,  145,  184,    9,   10,    7,
    8,   11,  144,   12,  151,   13,   72,  106,  145,    9,
   10,  145,  207,   11,   99,   12,    8,   13,   73,  152,
   98,    8,   44,  165,  145,    9,   10,   29,  166,   11,
  121,   12,  122,   13,  192,  107,   29,   29,   24,  106,
   29,  232,   29,   44,   29,  221,  228,   24,   24,   71,
    8,   24,   44,   24,  179,   24,  221,  259,  266,    9,
   10,    7,    8,    8,   44,   12,   72,   13,  121,  134,
  122,    9,   10,    7,    8,   11,   87,   12,  245,   13,
  168,    8,  120,    9,   10,    8,  213,   11,   69,   12,
   47,   13,    8,   44,    9,   10,    8,  162,   11,  121,
   12,  122,   13,    8,   44,    9,   10,    8,  121,   11,
  122,   12,  161,   13,   71,  173,    9,   10,   13,   13,
   11,  251,   12,  120,   13,  141,  139,  140,   13,   13,
    8,  119,   13,    0,   13,  190,   13,  146,  147,    9,
   10,    8,  269,   11,  121,   12,  122,   13,    8,   44,
    9,   10,    8,    0,   49,    8,   12,   47,   13,   48,
  181,    9,   10,    0,    9,   10,    8,   12,    0,   13,
   12,    0,   13,    8,   44,    9,   10,    8,  118,   49,
   46,   12,   47,   13,   48,    0,    9,   10,    8,    8,
   44,    0,   12,    0,   13,    8,  120,    9,   10,  129,
  205,    8,   44,   12,    0,   13,   99,   99,   99,   99,
   99,    0,   99,    0,    0,    0,   75,    0,   75,   75,
   75,    0,    0,    0,   99,   99,   99,   99,   79,    0,
   79,   79,   79,    0,   75,   75,   75,   75,   78,    0,
   78,   78,   78,  227,    8,  120,   79,   79,   79,   79,
   76,    0,   76,   76,   76,    0,   78,   78,   78,   78,
    0,   49,   46,   90,   47,    0,   48,    0,   76,   76,
   76,   76,   50,  206,   49,   46,    0,   47,    0,   48,
   91,    0,    8,  120,   62,    0,   49,   46,   74,   47,
  198,   48,   81,    0,   49,   46,    0,   47,    0,   48,
    0,  246,    8,   44,    0,    0,    0,    0,    0,  159,
  160,    0,  107,    8,  120,    0,    0,    0,  249,    0,
  120,    0,    8,  120,    0,  135,  136,  137,  138,    0,
    0,  250,    0,  120,  238,    0,    0,    0,  238,    0,
   74,    0,    0,    0,    0,    0,    0,  247,    0,  107,
    0,    0,    0,    0,    0,    0,    0,    0,    8,  120,
    0,    0,    0,    0,    0,    0,    0,    0,  203,    8,
   44,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  107,    0,    0,  194,    0,    0,    0,    0,
    0,    0,    0,    0,    8,   44,   45,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   99,   99,   99,    0,   99,   99,   99,   99,   99,
   99,   75,   75,   75,    0,   75,   75,   75,   75,    0,
   75,    0,  194,   79,   79,   79,    0,   79,   79,   79,
   79,    0,   79,   78,   78,   78,    0,   78,   78,   78,
   78,    0,   78,    0,  194,   76,   76,   76,    0,   76,
   76,   76,   76,    0,   76,   88,    8,   44,   55,   55,
   70,    0,   55,   89,    0,   93,   74,    0,    0,    8,
   44,   45,    0,    0,    0,    0,    0,    0,    0,    0,
   61,    8,   44,    0,    0,    0,    0,    0,   80,    8,
   44,    0,   55,    0,    0,   55,    0,    0,    0,   55,
  126,   51,    0,   55,    0,   82,   57,   57,   92,    0,
   57,   55,  126,   57,    0,    0,    0,    0,    0,    0,
    0,   55,    0,    0,   55,    0,    0,    0,    0,    0,
   55,  126,    0,    0,    0,  113,    0,    0,   55,  180,
   57,  126,   51,   57,  183,    0,    0,   57,  127,    0,
    0,   57,    0,    0,    0,    0,    0,    0,    0,   57,
  127,    0,  126,    0,    0,    0,    0,    0,    0,   57,
    0,    0,   57,  172,   55,    0,    0,   55,   57,  127,
    0,  178,    0,    0,    0,    0,   57,    0,    0,  127,
    0,   55,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  218,    0,    0,
  127,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  126,   57,    0,    0,   57,    0,  126,    0,    0,
    0,    0,    0,    0,  209,    0,    0,    0,   55,   57,
    0,    0,   55,    0,    0,    0,    0,    0,   55,    0,
    0,   55,    0,    0,    0,    0,    0,    0,  126,    0,
    0,    0,    0,    0,    0,    0,  257,    0,    0,  127,
    0,    0,    0,    0,    0,  127,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   57,    0,    0,    0,
   57,  113,    0,    0,    0,    0,   57,    0,    0,   57,
    0,    0,    0,    0,    0,    0,  127,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   40,   45,   40,   44,   42,   43,   40,   45,   41,
   47,    0,   44,   40,    0,   59,  153,   41,    0,   59,
   73,   52,   44,   64,  123,   41,    4,   41,   41,   40,
    2,   44,  109,   42,   40,  257,   42,   43,   47,   45,
   40,   47,   14,   41,  257,  125,  256,   94,   61,   41,
   41,   40,   44,  106,   41,  257,   46,  270,  105,   44,
   45,  271,   40,   40,   72,   86,   40,   75,  270,   45,
   41,   44,   46,   44,   59,   40,  270,   41,  123,  216,
   41,   45,  257,   44,  123,  116,  125,   40,   61,   59,
   59,   59,  123,  256,  257,   41,  123,   43,   40,   45,
  123,  258,  256,   59,  256,   44,   41,   42,   43,   40,
   45,  188,   47,  264,  125,  192,  163,   41,   42,   43,
   40,   45,   61,   47,  256,  125,   59,  257,  100,  125,
  257,   45,   40,   40,  268,  125,  125,  184,  110,  186,
   41,  123,  257,   40,   44,   41,  123,  257,   59,   41,
  125,   40,   46,   59,   40,   44,  256,   46,  125,  256,
  125,  257,   41,   41,  185,   40,   45,   44,   40,  210,
   59,  123,   59,  204,  256,   41,  197,  256,  123,   40,
    0,  257,  256,  125,    0,  256,  256,  234,   42,   43,
   40,   45,  264,   47,  125,  256,  256,  256,  256,   41,
  256,   40,    0,    0,   42,  125,  256,   45,  256,   47,
    4,   43,  256,   45,  257,  258,  256,  256,  257,   45,
  257,  258,   36,  256,  257,  257,   95,  266,  267,  256,
  257,  270,  256,  272,  256,  274,  273,  123,  270,  266,
  267,  257,  256,  270,  257,  272,  257,  274,  123,  271,
  271,  257,  258,  125,  270,  266,  267,  257,  256,  270,
   43,  272,   45,  274,  125,  256,  266,  267,  257,  256,
  270,  256,  272,  258,  274,  125,   59,  266,  267,  256,
  257,  270,  258,  272,  257,  274,  125,  256,  256,  266,
  267,  256,  257,  257,  258,  272,  273,  274,   43,   62,
   45,  266,  267,  256,  257,  270,  270,  272,  199,  274,
   75,  257,  258,  266,  267,  257,  150,  270,  257,  272,
   45,  274,  257,  258,  266,  267,  257,   44,  270,   43,
  272,   45,  274,  257,  258,  266,  267,  257,   43,  270,
   45,  272,   59,  274,  258,   59,  266,  267,  256,  257,
  270,   59,  272,   59,  274,   60,   61,   62,  266,  267,
  257,   50,  270,   -1,  272,  108,  274,  256,  257,  266,
  267,  257,  252,  270,   43,  272,   45,  274,  257,  258,
  266,  267,  257,   -1,   42,  257,  272,   45,  274,   47,
   59,  266,  267,   -1,  266,  267,  257,  272,   -1,  274,
  272,   -1,  274,  257,  258,  266,  267,  257,   41,   42,
   43,  272,   45,  274,   47,   -1,  266,  267,  257,  257,
  258,   -1,  272,   -1,  274,  257,  258,  266,  267,   54,
  256,  257,  258,  272,   -1,  274,   41,   42,   43,   44,
   45,   -1,   47,   -1,   -1,   -1,   41,   -1,   43,   44,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   41,   -1,
   43,   44,   45,   -1,   59,   60,   61,   62,   41,   -1,
   43,   44,   45,  256,  257,  258,   59,   60,   61,   62,
   41,   -1,   43,   44,   45,   -1,   59,   60,   61,   62,
   -1,   42,   43,   44,   45,   -1,   47,   -1,   59,   60,
   61,   62,   40,  128,   42,   43,   -1,   45,   -1,   47,
   61,   -1,  257,  258,   40,   -1,   42,   43,   12,   45,
  265,   47,   40,   -1,   42,   43,   -1,   45,   -1,   47,
   -1,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,  256,
  257,   -1,   36,  257,  258,   -1,   -1,   -1,  256,   -1,
  256,   -1,  257,  258,   -1,  260,  261,  262,  263,   -1,
   -1,  269,   -1,  269,  189,   -1,   -1,   -1,  193,   -1,
   64,   -1,   -1,   -1,   -1,   -1,   -1,  202,   -1,   73,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  106,   -1,   -1,  109,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,   -1,  166,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,  188,  256,  257,  258,   -1,  260,
  261,  262,  263,   -1,  265,  256,  257,  258,    9,   10,
   11,   -1,   13,  264,   -1,   16,  210,   -1,   -1,  257,
  258,  259,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,   43,   -1,   -1,   46,   -1,   -1,   -1,   50,
   51,    9,   -1,   54,   -1,   13,    9,   10,   16,   -1,
   13,   62,   63,   16,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   72,   -1,   -1,   75,   -1,   -1,   -1,   -1,   -1,
   81,   82,   -1,   -1,   -1,   43,   -1,   -1,   89,   90,
   43,   92,   50,   46,   95,   -1,   -1,   50,   51,   -1,
   -1,   54,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   62,
   63,   -1,  113,   -1,   -1,   -1,   -1,   -1,   -1,   72,
   -1,   -1,   75,   81,  125,   -1,   -1,  128,   81,   82,
   -1,   89,   -1,   -1,   -1,   -1,   89,   -1,   -1,   92,
   -1,  142,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  158,   -1,   -1,
  113,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  172,  125,   -1,   -1,  128,   -1,  178,   -1,   -1,
   -1,   -1,   -1,   -1,  142,   -1,   -1,   -1,  189,  142,
   -1,   -1,  193,   -1,   -1,   -1,   -1,   -1,  199,   -1,
   -1,  202,   -1,   -1,   -1,   -1,   -1,   -1,  209,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  217,   -1,   -1,  172,
   -1,   -1,   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  189,   -1,   -1,   -1,
  193,  199,   -1,   -1,   -1,   -1,  199,   -1,   -1,  202,
   -1,   -1,   -1,   -1,   -1,   -1,  209,
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

//#line 842 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
    SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 790 "Parser.java"
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
//#line 82 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron, previo al nombre del programa, sentencias."); }
break;
case 8:
//#line 85 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 12:
//#line 106 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 13:
//#line 109 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 15:
//#line 112 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 16:
//#line 114 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 17:
//#line 116 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
break;
case 24:
//#line 142 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 28:
//#line 159 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 34:
//#line 184 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 202 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 44:
//#line 213 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 47:
//#line 229 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 48:
//#line 232 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 49:
//#line 237 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 50:
//#line 241 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 51:
//#line 245 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 249 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 259 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 264 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 271 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 285 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 291 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 294 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 297 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 61:
//#line 307 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 62:
//#line 309 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 63:
//#line 314 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 64:
//#line 316 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 69:
//#line 337 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 71:
//#line 345 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 353 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 358 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 372 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 377 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de %s %s.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 78:
//#line 381 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 388 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 80:
//#line 398 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "+"; }
break;
case 81:
//#line 400 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-"; }
break;
case 82:
//#line 407 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 84:
//#line 413 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 85:
//#line 420 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 86:
//#line 427 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 88:
//#line 433 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 89:
//#line 440 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "/"; }
break;
case 90:
//#line 442 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "*"; }
break;
case 98:
//#line 469 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
case 100:
//#line 488 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 101:
//#line 497 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Condición."); }
break;
case 102:
//#line 502 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 103:
//#line 505 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 104:
//#line 508 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 105:
//#line 511 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 107:
//#line 523 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 114:
//#line 539 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 115:
//#line 548 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 116:
//#line 553 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 117:
//#line 555 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 118:
//#line 557 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 119:
//#line 559 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 122:
//#line 575 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 123:
//#line 580 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 124:
//#line 582 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 127:
//#line 599 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 128:
//#line 601 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 131:
//#line 620 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 133:
//#line 632 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 136:
//#line 649 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 139:
//#line 661 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 143:
//#line 679 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 144:
//#line 681 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 147:
//#line 693 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 148:
//#line 702 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 149:
//#line 707 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 150:
//#line 709 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 151:
//#line 711 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 152:
//#line 713 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 153:
//#line 722 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 155:
//#line 732 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 156:
//#line 739 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 157:
//#line 744 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 158:
//#line 753 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 159:
//#line 758 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 161:
//#line 769 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 162:
//#line 772 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 163:
//#line 774 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 167:
//#line 797 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 168:
//#line 802 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 169:
//#line 804 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 170:
//#line 806 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 171:
//#line 808 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 173:
//#line 819 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 174:
//#line 822 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1371 "Parser.java"
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
