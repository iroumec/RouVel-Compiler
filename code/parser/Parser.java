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
    import utilities.Printer;
//#line 31 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 27 "Parser.java"




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
    0,    0,    0,    0,    0,   17,   19,   19,   19,   19,
   19,   20,   20,   18,   18,   18,   22,   22,   22,   22,
   23,   23,   23,   23,   23,   23,   23,   21,   21,   25,
   25,   28,   28,   29,   29,   30,   30,   31,   31,   31,
   24,   24,   24,   24,   24,   24,   24,   24,   34,   34,
   26,   26,   26,   26,   26,   10,   10,   10,   10,   32,
   32,   32,   32,   40,   33,   33,   33,   33,   41,   42,
   42,   11,   11,   11,    1,    1,    6,    6,    6,    6,
    8,    8,    2,    2,    7,    7,    7,    4,    4,    4,
    9,    9,    3,    3,    3,    5,    5,    5,   13,   13,
   12,   12,   43,   43,   43,   43,   43,   44,   45,   45,
   46,   46,   46,   46,   46,   46,   46,   38,   38,   38,
   38,   38,   47,   47,   39,   39,   39,   49,   49,   49,
   48,   50,   27,   27,   53,   53,   52,   35,   35,   35,
   35,   51,   51,   54,   54,   54,   56,   56,   55,   55,
   55,   57,   57,   57,   14,   15,   15,   16,   16,   36,
   36,   59,   59,   59,   59,   58,   60,   60,   37,   37,
   63,   63,   63,   63,   62,   61,
};
final static short yylen[] = {                            2,
    2,    2,    1,    2,    2,    3,    1,    2,    2,    0,
    3,    2,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    0,    1,    1,    1,    3,    2,    1,    2,    2,
    2,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    3,    3,    2,    5,    3,    3,    2,    2,    4,
    4,    4,    3,    1,    2,    4,    2,    4,    3,    5,
    1,    1,    3,    2,    1,    3,    1,    3,    2,    2,
    1,    1,    3,    1,    2,    1,    3,    3,    1,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,
    1,    3,    1,    2,    2,    2,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    6,    6,    5,
    5,    2,    0,    2,    3,    3,    2,    1,    1,    2,
    2,    2,    6,    5,    1,    2,    3,    5,    5,    4,
    3,    1,    0,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    4,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    0,    3,    1,    1,    4,    4,
    1,    2,    1,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    7,    3,    4,    5,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,   14,   28,   29,   30,    0,   43,   44,   45,   46,
   47,   48,   49,   50,    0,    0,    0,    0,    0,    0,
   21,   24,   22,   23,   25,   27,   26,   20,   18,   17,
   16,   19,    0,    0,   99,  167,    0,    0,    0,    0,
   84,   93,   94,   95,    0,    0,    0,  122,    0,    0,
    0,  103,    0,   54,    0,    0,    0,    0,  127,    0,
    0,   34,    0,   35,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   71,   86,    0,    0,    0,   42,   41,
   15,   33,   31,   67,   65,    0,    0,    0,   12,    6,
   13,  102,    0,    0,  156,  100,    0,    0,   81,   82,
    0,   91,   92,    0,  160,  161,  105,    0,  113,  115,
  114,  116,  117,  111,  112,    0,    0,  106,  104,   52,
   59,    0,    0,  154,  153,    0,    0,    0,  144,  146,
    0,   53,   58,   51,    0,    0,  132,    0,   37,   38,
    0,    0,  131,  125,  126,    0,    0,  141,    0,    0,
    0,    0,    0,    0,    0,    0,   97,   63,    0,   89,
    0,   96,   98,    0,   69,    0,   72,    0,    0,    0,
    0,    0,    0,  155,  166,    0,   83,  107,  108,    0,
    0,    0,   56,    0,  148,    0,    0,    0,  151,   57,
    0,   40,   36,   39,  140,    0,  176,   62,   60,   61,
    0,    0,    0,   87,   68,   66,    0,   74,    0,    0,
  169,  170,  158,  157,  124,  121,    0,  120,    0,    0,
  135,  134,  145,  149,   55,  139,  138,    0,   90,   88,
   73,  175,  119,  118,    0,  133,  136,    0,   70,  137,
};
final static short yydgoto[] = {                          4,
   70,   60,   61,  179,  180,   96,   97,  121,  124,   77,
  186,   62,   63,   64,  114,  115,    5,   39,    6,   40,
   22,   51,   52,   23,   24,   25,   26,  103,   83,   84,
  161,   27,   28,   29,   30,   31,   32,   33,   34,  174,
   35,   98,   71,   72,   73,  136,  202,   85,   86,   87,
  147,  241,  242,  148,  149,  150,  151,   65,   66,   67,
   36,  190,  191,
};
final static short yysindex[] = {                      -103,
    2,  -35,  -23,    0,    0,    0,    0,    0,    1,   38,
   59,   88,  -37,   57,   70,   81, -205,  254,  -48,    0,
  127,    0,    0,    0,    0,   37,    0,    0,    0,    0,
    0,    0,    0,    0,   63,  -54,   12,  -43,   92,   11,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -167,  251,    0,    0, -135,   75,   68,   -1,
    0,    0,    0,    0,   71, -118,    0,    0,  152,  260,
  139,    0,  -10,    0,  -31,  -14,  248, -125,    0,  117,
  104,    0,    8,    0,   82, -114,    0,  157,    7, -112,
  251,  251, -111,    0,    0,  324,   -1,    6,    0,    0,
    0,    0,    0,    0,    0,    6,  145,    0,    0,    0,
    0,    0,  -29,   17,    0,    0,    0,  102,    0,    0,
  251,    0,    0,  251,    0,    0,    0,   -4,    0,    0,
    0,    0,    0,    0,    0,  251, -119,    0,    0,    0,
    0, -106,  -14,    0,    0, -229,  111,  114,    0,    0,
 -221,    0,    0,    0,  -97,    6,    0,  155,    0,    0,
  151,    0,    0,    0,    0,  105,   42,    0,  120,  345,
   49,    0,  355,  -91,  124,   -6,    0,    0,   -1,    0,
  251,    0,    0,  -84,    0,  437,    0,  162,    0,  118,
  -82,  -76,  251,    0,    0,   -1,    0,    0,    0,   68,
  139,  291,    0,  142,    0,   61, -229, -111,    0,    0,
  128,    0,    0,    0,    0,  -26,    0,    0,    0,    0,
    6,  227,   -1,    0,    0,    0,    6,    0,    0,  148,
    0,    0,    0,    0,    0,    0,   55,    0,   73,   69,
    0,    0,    0,    0,    0,    0,    0,  156,    0,    0,
    0,    0,    0,    0,   81,    0,    0,  115,    0,    0,
};
final static short yyrindex[] = {                       186,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  389,
 -114,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  192,    0,    0,    0,    0,   23,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  199,    0,  201,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   21,  399,
    0,    0,    0,    0,  -52,    0,  -51,    0,    0,   31,
    0,    0,    0,    0,  -55,   13,    0,    0,    0,    0,
    0,    0,    0,    0,  -42,    0,  -38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  297,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -33,   34,    0,    0,
    0,    0,   83,    0,    0,    0,  -28,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  314,    0,    0,    0,
    0,    0,   13,    0,    0, -193,    0,  175,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  421,  121,    0,  -16,    0,    0,    0,  309,    0,
   46,    0,    0,  432,    0,    0,    0,    0,  130,  135,
    0,    0,    0,    0,    0,  411,    0,    0,    0,   32,
    0,    0,    0,    0,    0,    0,   -9,   90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  320,    0,    0,    0,    0,    0,  136,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  487,  129,  -12,    0,    0,   27,  214,  -67,  -50,    0,
    0,    5,  -39,   54,    0,  187,  252,   -2,    0,    0,
  -17,    0,    0,   10,    0,    0,    0,    0,  -61,  220,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  134,  -40,    0,  338,    0,    0,    0,    0,  333,
  281,  189,    0,    0, -107,    0,    0,    0,    0,  368,
    0,    0,    0,
};
final static int YYTABLESIZE=695;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         21,
   48,    8,   76,  101,   17,   95,   18,   18,  143,  137,
  100,   48,  142,  119,   53,  120,   17,   78,   18,    3,
   18,  101,   32,   82,  101,   18,  144,  101,  181,  146,
  139,  147,  247,   11,  147,  175,  199,   93,  205,  157,
  123,  145,  162,   18,  101,  122,  184,   69,  208,  119,
   57,  120,   57,  143,   94,   19,   19,  194,  185,   50,
  193,  168,   32,  152,   90,  168,  187,   19,   81,   19,
   50,  110,  109,   18,   19,   18,  152,   54,   95,  172,
   82,  109,  216,   53,  119,   18,  120,   16,   79,  112,
  160,  119,   19,  120,  189,  102,   17,  176,   58,  243,
  182,   38,  181,   57,   79,  181,  106,  219,   17,   88,
  119,  197,  120,  254,   57,  117,  211,  170,  173,   57,
   17,  105,  116,  159,   19,   49,  159,   69,  222,  125,
  150,   17,   57,  150,   19,  111,  108,  126,  156,  235,
  164,  165,  195,   17,  169,  175,  228,   32,  201,  183,
  203,  206,    1,    2,   17,  209,   69,  207,   18,  210,
  217,   57,   18,  215,  220,   18,   17,  212,   95,   53,
  214,  224,  184,  232,  182,  230,  231,  182,   17,   81,
  233,  248,  239,  240,  188,   10,  245,  251,  252,   57,
   17,    2,  127,  257,   17,  255,   57,  166,    9,  259,
    8,   57,  229,  162,  164,   18,   57,   99,  101,  250,
   82,   19,  244,  128,   19,  142,  110,  129,   74,   75,
    9,   10,  174,  183,  140,  141,  183,  163,  159,  246,
   11,   12,   37,   10,   13,  192,   14,  258,   15,  260,
  101,  144,   11,   12,   18,  138,   13,  152,   14,  196,
   15,  198,  258,   20,   19,  107,  145,   41,    7,   18,
  152,   81,   18,   55,   10,   55,   42,   43,   41,  152,
   44,   57,   45,   46,   47,  213,  168,   42,   43,   32,
   80,   44,  152,   45,   46,   47,  110,  109,   32,   32,
   18,  155,   32,   19,   32,   57,   32,   93,   57,   18,
   18,   79,  119,   18,  120,   18,  154,   18,   19,  221,
  253,   19,   79,   10,   94,   10,   55,   56,  104,  135,
  133,  134,   11,   12,    9,   10,   10,   55,   14,   80,
   15,   10,   55,   56,   11,   12,    9,   10,   13,   77,
   14,   77,   15,   68,   10,   55,   11,   12,   10,  238,
   13,   80,   14,   80,   15,   77,  130,   11,   12,  158,
   10,   13,   78,   14,   78,   15,  119,   80,  120,   11,
   12,   10,  123,   10,   55,   14,   64,   15,   78,  234,
   11,   12,  178,   10,   13,  173,   14,  119,   15,  120,
  171,  172,   11,   12,  223,   10,   13,  119,   14,  120,
   15,   10,   55,  218,   11,   12,  128,   10,   10,   55,
   14,   10,   15,   10,   55,  163,   11,   12,   10,   55,
   11,   12,   14,  204,   15,  118,   14,  256,   15,  101,
  101,  101,  101,  101,    0,  101,    0,    0,    0,   75,
    0,   75,   75,   75,    0,    0,    0,  101,  101,  101,
  101,   76,    0,   76,   76,   76,    0,   75,   75,   75,
   75,    0,   84,   84,    0,   84,    0,   84,    0,   76,
   76,   76,   76,   85,   85,    0,   85,    0,   85,   84,
  227,   57,  249,   10,   55,    0,    0,    0,    0,    0,
   85,    0,    0,    0,    0,  226,    0,   59,    0,    0,
    0,   89,    0,  152,  153,    0,    0,   10,   55,   91,
   10,   55,    0,    0,    0,    0,    0,   92,    0,  129,
  130,  131,  132,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  113,    0,    0,    0,   59,    0,  236,    0,    0,    0,
    0,    0,   77,   77,   77,    0,    0,    0,    0,  237,
    0,    0,    0,    0,   80,   80,   80,    0,    0,  123,
    0,    0,    0,    0,  167,   78,   78,   78,  171,    0,
   10,  177,  123,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   10,  177,    0,    0,    0,    0,    0,    0,    0,
    0,   10,  177,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  200,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  101,  101,  101,    0,  101,  101,
  101,  101,  101,  101,   75,    0,    0,    0,   75,   75,
   75,   75,    0,   75,    0,    0,   76,    0,    0,    0,
   76,   76,   76,   76,    0,   76,   86,   86,   86,  113,
    0,    0,    0,    0,    0,    0,    0,    0,   85,   85,
    0,    0,  225,    0,   55,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    0,   40,   21,   40,   18,    2,    3,   40,   71,
   59,    0,   44,   43,   46,   45,   40,   13,   14,  123,
   16,   39,    0,   14,   41,   21,  256,   44,   96,   44,
   41,   41,   59,    0,   44,  257,   41,   44,  146,   80,
   42,  271,   83,   39,   61,   47,   97,   40,  270,   43,
   45,   45,   45,   41,   61,    2,    3,   41,   98,   59,
   44,   41,   40,  257,  270,   59,  106,   14,  123,   16,
   59,   41,   41,   40,   21,   71,  270,   40,   91,   92,
   71,  125,   41,   46,   43,   81,   45,  123,   43,  257,
   81,   43,   39,   45,  107,   59,   40,   93,   40,  207,
   96,  125,  170,   45,   59,  173,   44,   59,   40,   40,
   43,  124,   45,   59,   45,   41,  156,   91,   92,   45,
   40,   59,  258,   41,   71,  125,   44,   40,  179,   59,
   41,   40,   45,   44,   81,  125,  125,  256,  264,  201,
   59,  256,   41,   40,  257,  257,  186,  125,  268,   96,
  257,   41,  256,  257,   40,  151,   40,   44,  125,  257,
   41,   45,  158,   59,  256,  161,   40,  158,  181,   46,
  161,  256,  223,  256,  170,  188,   59,  173,   40,  123,
  257,  221,   41,  123,   40,    0,   59,  227,   41,   45,
   40,    0,   41,  125,   40,  123,   45,   41,    0,   44,
    0,   45,   41,  256,  256,  201,   45,  256,  264,  222,
  201,  158,  208,  256,  161,   41,  125,  256,  256,  257,
  256,  257,  256,  170,  256,  257,  173,  256,  125,  256,
  266,  267,  256,  257,  270,  265,  272,  240,  274,  125,
  258,  256,  266,  267,  240,  256,  270,  257,  272,  121,
  274,  256,  255,    2,  201,   36,  271,  257,  257,  255,
  270,  123,  258,  258,  257,  258,  266,  267,  257,  257,
  270,   45,  272,  273,  274,  125,  256,  266,  267,  257,
  273,  270,  270,  272,  273,  274,  256,  256,  266,  267,
  257,   44,  270,  240,  272,   45,  274,   44,   45,  266,
  267,  256,   43,  270,   45,  272,   59,  274,  255,  176,
  256,  258,  256,  257,   61,  257,  258,  259,  256,   60,
   61,   62,  266,  267,  256,  257,  257,  258,  272,  273,
  274,  257,  258,  259,  266,  267,  256,  257,  270,   43,
  272,   45,  274,  256,  257,  258,  266,  267,  257,   59,
  270,   43,  272,   45,  274,   59,  256,  266,  267,  256,
  257,  270,   43,  272,   45,  274,   43,   59,   45,  266,
  267,  257,   59,  257,  258,  272,  256,  274,   59,  193,
  266,  267,   59,  257,  270,  256,  272,   43,  274,   45,
  256,  256,  266,  267,  181,  257,  270,   43,  272,   45,
  274,  257,  258,   59,  266,  267,   69,  257,  257,  258,
  272,  257,  274,  257,  258,   83,  266,  267,  257,  258,
  266,  267,  272,  143,  274,   58,  272,  239,  274,   41,
   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,   59,   60,   61,
   62,   41,   -1,   43,   44,   45,   -1,   59,   60,   61,
   62,   -1,   42,   43,   -1,   45,   -1,   47,   -1,   59,
   60,   61,   62,   42,   43,   -1,   45,   -1,   47,   59,
   44,   45,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,
   59,   -1,   -1,   -1,   -1,   59,   -1,   11,   -1,   -1,
   -1,   15,   -1,  256,  257,   -1,   -1,  257,  258,  256,
  257,  258,   -1,   -1,   -1,   -1,   -1,  264,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   54,   -1,   -1,   -1,   58,   -1,  256,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,  256,
   -1,   -1,   -1,   -1,   88,  256,  257,  258,   92,   -1,
  257,  258,  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  136,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,  264,  265,  256,   -1,   -1,   -1,  260,  261,
  262,  263,   -1,  265,   -1,   -1,  256,   -1,   -1,   -1,
  260,  261,  262,  263,   -1,  265,  256,  257,  258,  193,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,   -1,  256,   -1,  258,
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
"programa : ID conjunto_sentencias",
"programa : cuerpo_programa_flexible",
"programa : error ID",
"programa : error EOF",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa_flexible : cuerpo_programa",
"cuerpo_programa_flexible : '{' lista_llaves",
"cuerpo_programa_flexible : '{' '}'",
"cuerpo_programa_flexible :",
"cuerpo_programa_flexible : '{' error '}'",
"lista_llaves : '}' '}'",
"lista_llaves : lista_llaves '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : error punto_sincronizacion_sentencia",
"punto_sincronizacion_sentencia : ';'",
"punto_sincronizacion_sentencia : '}'",
"punto_sincronizacion_sentencia : token_inicio_sentencia",
"punto_sincronizacion_sentencia : EOF",
"token_inicio_sentencia : ID",
"token_inicio_sentencia : IF",
"token_inicio_sentencia : UINT",
"token_inicio_sentencia : PRINT",
"token_inicio_sentencia : DO",
"token_inicio_sentencia : RETURN",
"token_inicio_sentencia : WHILE",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"conjunto_sentencias_ejecutables : error sentencia_ejecutable",
"sentencia_ejecutable : invocacion_funcion ';'",
"sentencia_ejecutable : invocacion_funcion error",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"sentencia_control : if",
"sentencia_control : do_while",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT ID error",
"declaracion_variable : UINT lista_variables error",
"declaracion_variable : UINT error",
"declaracion_variable : UINT variable DASIG constante ';'",
"lista_variables : ID ',' ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"lista_variables : ID ID",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG expresion_o_termino error",
"asignacion_simple : variable error expresion_flexible ';'",
"asignacion_simple : variable expresion_flexible ';'",
"expresion_o_termino : expresion_flexible",
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
"expresion_flexible : termino_flexible",
"expresion_flexible : expresion_flexible operador_suma termino_flexible",
"expresion_flexible : expresion_flexible operador_suma",
"expresion_flexible : expresion_flexible termino_simple",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino_flexible : termino_flexible operador_multiplicacion",
"termino_flexible : factor",
"termino_flexible : termino_flexible operador_multiplicacion error",
"termino_simple : termino_simple operador_multiplicacion factor",
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
"condicion : condicion_admisible",
"condicion : cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion error",
"condicion : '(' cuerpo_condicion error",
"condicion_admisible : '(' cuerpo_condicion ')'",
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
"do_while : DO cuerpo_do_admisible ';'",
"do_while : DO cuerpo_do error",
"do_while : DO error",
"cuerpo_do : cuerpo_do_admisible",
"cuerpo_do : fin_cuerpo_do",
"cuerpo_do : cuerpo_ejecutable condicion",
"cuerpo_do_admisible : cuerpo_ejecutable fin_cuerpo_do",
"fin_cuerpo_do : WHILE condicion",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible",
"declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
"cuerpo_funcion : cuerpo_funcion_admisible",
"cuerpo_funcion : '{' '}'",
"cuerpo_funcion_admisible : '{' conjunto_sentencias '}'",
"sentencia_retorno : RETURN '(' expresion ')' ';'",
"sentencia_retorno : RETURN '(' expresion ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
"sentencia_retorno : RETURN expresion ';'",
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

//#line 821 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    this.readAgain = false;
    
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

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

Token getAppropiateToken() {

    Token token;
    // Se lee nuevamente el último token.
    // Útil para recuperar el token de sincronización en reglas de error.
    if (this.readAgain) {
        token = lexer.getCurrentToken();
        this.readAgain = false;
    } else {
        token = lexer.getNextToken();
    }

    return token;
}

// --------------------------------------------------------------------------------------------------------------------

public void descartarTokensHasta(int tokenEsperado) {

    int t = yylex();
    
    // Se pide un token mientras no se halle el token esperado
    // o el final de archivo.
    while (t != tokenEsperado && t != EOF) {
        t = yylex();
    }

    if (t == EOF) {
        Printer.printBetweenSeparations("SE LLEGÓ AL FINAL DEL ARCHIVO SIN ENCONTRAR UN TOKEN DE SINCRONIZACION.");
    }

    // Se actualizá que se halló el token deseado o se llegó al final del archivo.
    yychar = t;
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
    /*
    // 'yylval' contiene el valor del token que el parser no pudo procesar.
    String errorMessage;
    if (yylval != null) {
        errorMessage = String.format(
            "Error de sintaxis: token inesperado '%s'.",
            yylval.sval
        );
    } else {
        errorMessage = "Error de sintaxis: se encontró un token inesperado.";
    }

    Printer.printBetweenSeparations(errorMessage);*/
}

// --------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// --------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// --------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// --------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
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

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// --------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 813 "Parser.java"
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
//#line 69 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 74 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 76 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 78 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 5:
//#line 80 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 97 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa"); }
break;
case 9:
//#line 100 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 10:
//#line 102 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 11:
//#line 104 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 16:
//#line 123 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 37:
//#line 187 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 40:
//#line 196 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 42:
//#line 204 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 51:
//#line 226 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 52:
//#line 231 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 53:
//#line 235 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 54:
//#line 239 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 55:
//#line 243 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 57:
//#line 253 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 58:
//#line 258 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 59:
//#line 265 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 60:
//#line 279 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 61:
//#line 284 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 62:
//#line 287 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 290 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 65:
//#line 304 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 66:
//#line 306 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 67:
//#line 311 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 313 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 73:
//#line 334 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 339 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 79:
//#line 364 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
        }
break;
case 80:
//#line 371 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
        }
break;
case 81:
//#line 383 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "+"; }
break;
case 82:
//#line 385 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-"; }
break;
case 87:
//#line 404 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 90:
//#line 419 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 91:
//#line 431 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 433 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "*"; }
break;
case 100:
//#line 459 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 102:
//#line 467 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 104:
//#line 480 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 105:
//#line 483 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 106:
//#line 486 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 107:
//#line 489 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 110:
//#line 507 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 117:
//#line 523 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 118:
//#line 532 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 119:
//#line 537 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 120:
//#line 539 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 121:
//#line 541 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 122:
//#line 543 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 125:
//#line 559 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 126:
//#line 564 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 127:
//#line 566 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 577 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 579 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 600 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 134:
//#line 605 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 136:
//#line 617 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 630 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'return'."); }
break;
case 139:
//#line 635 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 140:
//#line 637 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 141:
//#line 639 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 143:
//#line 650 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 146:
//#line 662 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 150:
//#line 680 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 151:
//#line 682 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 154:
//#line 694 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto."); }
break;
case 155:
//#line 703 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 714 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 721 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 726 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 735 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 740 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 751 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 754 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 756 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 779 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 784 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 172:
//#line 795 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 173:
//#line 798 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }
break;
case 174:
//#line 801 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1339 "Parser.java"
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
