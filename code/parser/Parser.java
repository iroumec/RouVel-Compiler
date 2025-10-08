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
   43,   44,   45,   45,   46,   46,   46,   46,   46,   46,
   46,   38,   38,   38,   38,   38,   47,   47,   39,   39,
   39,   49,   49,   49,   48,   50,   27,   27,   53,   53,
   52,   51,   51,   54,   54,   54,   56,   56,   55,   55,
   55,   57,   57,   57,   35,   35,   35,   35,   35,   12,
   13,   13,   14,   14,   36,   36,   59,   59,   59,   59,
   58,   60,   60,   37,   37,   63,   63,   63,   63,   62,
   61,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    3,    4,    3,    2,
    4,    4,    2,    4,    2,    4,    3,    5,    1,    1,
    3,    2,    1,    3,    3,    2,    1,    1,    3,    1,
    3,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    1,    2,    2,    2,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    1,    6,    6,    5,    5,    2,    0,    2,    3,    3,
    2,    1,    1,    2,    2,    2,    6,    5,    1,    2,
    3,    1,    0,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    5,    5,    4,    3,    2,    4,
    1,    3,    3,    1,    3,    3,    1,    2,    1,    0,
    3,    1,    1,    4,    4,    1,    2,    1,    0,    3,
    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   38,   39,   40,   41,
   42,   43,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   93,  162,    0,    0,    0,    0,   80,   87,
   88,   89,    0,    0,    0,  116,    0,    0,    0,   97,
    0,   50,    0,    0,    0,    0,  121,    0,    0,   30,
    0,   31,    0,    0,    0,  149,    0,    0,    0,   17,
   14,    0,    0,    0,    0,    0,   69,    0,    0,   37,
   36,   22,   18,    0,   29,   27,   65,   63,    0,    0,
   96,    0,    0,  151,   94,    0,    0,   91,   77,   78,
    0,   83,    0,   90,   92,   85,   86,    0,  155,  156,
   99,    0,  107,  109,  108,  110,  111,  105,  106,    0,
    0,  100,   98,   48,   55,   47,    0,    0,  144,  143,
    0,    0,    0,  134,  136,    0,   49,   54,   46,    0,
    0,  126,   33,   34,    0,    0,  125,  119,  120,    0,
    0,  148,    0,    0,    0,    0,    0,    0,    0,    0,
   57,    0,    0,   59,   67,    0,    0,   70,    0,    0,
    0,    0,    0,    0,  150,  161,    0,   75,    0,   81,
   79,  101,  102,    0,    0,    0,   52,    0,  138,    0,
    0,    0,  141,   53,    0,   32,   35,  147,    0,   19,
   20,  171,   58,   56,    0,    0,   60,    0,   13,   66,
   64,    0,   72,    0,    0,  164,  165,  153,  152,   84,
   82,  118,  115,    0,  114,    0,    0,  129,  128,  135,
  139,   51,  146,  145,    0,    0,    0,   71,  170,  113,
  112,    0,  127,  130,    0,   61,   62,   68,  131,
};
final static short yydgoto[] = {                          3,
   58,   48,   49,  111,  112,  113,  118,   65,  177,   50,
   51,   52,  103,  104,   18,   19,  255,   39,    4,  165,
   21,   94,   22,   23,   24,   25,   26,   96,   71,   72,
  155,   27,   28,   29,   30,   31,   32,   33,   34,  171,
   35,   89,   59,   60,   61,  130,  196,   73,   74,   75,
  142,  238,  239,  143,  144,  145,  146,   53,   54,   55,
   36,  181,  182,
};
final static short yysindex[] = {                      -172,
    2,   24,    0,  -84,    0,    0,  -71,   -2,  122,   87,
   -3,   43,  204,  -36, -207,  126,  -56,    0,    0,  132,
  -41,    0,    0,    0,    0,   32,    0,    0,    0,    0,
    0,    0,    0,    0,   36,  -31,    0,    0,    0,    0,
 -156,  287,    0,    0, -154,  220,  143,   46,    0,    0,
    0,    0,   47, -151,    0,    0,  177,  153,  -10,    0,
  -16,    0,  316,   -1,  274, -157,    0,  -29,  151,    0,
  -32,    0,   50, -145,    0,    0,  308,  360,   -7,    0,
    0,   68, -140,  287,  287, -138,    0,  430,  -39,    0,
    0,    0,    0,   80,    0,    0,    0,    0,  -39,  128,
    0,   30,   15,    0,    0,    0,   71,    0,    0,    0,
   46,    0,  294,    0,    0,    0,    0,  319,    0,    0,
    0,  -14,    0,    0,    0,    0,    0,    0,    0,  287,
 -147,    0,    0,    0,    0,    0, -135,   -1,    0,    0,
 -221,   83,   84,    0,    0, -223,    0,    0,    0, -128,
  -39,    0,    0,    0,  164,    0,    0,    0,    0,   74,
  285,    0,    0,   11,   14,   94,  445,  448,   46, -116,
    0,   97,  -13,    0,    0,   91,  115,    0,  310,    0,
   86, -110, -106,  287,    0,    0, -158,    0,   46,    0,
    0,    0,    0,  143,  -10,  138,    0,  111,    0,   34,
 -221, -138,    0,    0,   96,    0,    0,    0,  -50,    0,
    0,    0,    0,    0,  294,  319,    0,  -39,    0,    0,
    0,  -39,    0,    0,  112,    0,    0,    0,    0,    0,
    0,    0,    0,  -44,    0,   41,   57,    0,    0,    0,
    0,    0,    0,    0,   18,  -91,  133,    0,    0,    0,
    0,   80,    0,    0,  102,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         7,
    0,  178,    0,  178,    0,    0,    0,  260,  -77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  180,
  121,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -8,  383,    0,    0,
    0,    0,  -75,    0, -145,    0,    0,   10,    0,    0,
    0,    0,  -81,  -24,    0,    0,    0,    0,    0,    0,
    0,    0,  -64,    0,  -55,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -51,
    0,   28,    0,    0,    0,  -49,    0,    0,    0,    0,
  396,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  140,    0,    0,    0,    0,    0,    0,  -24,    0,    0,
 -217,    0,  144,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -48,    0,    0,    0,    0,
    0,    0,   12,  189,  209,    0,    0,    0,  451,  427,
    0,  -12,    0,    0,    0,    0,    0,    0,    0,  -19,
   21,    0,    0,    0,    0,    0,    0,    0,  406,    0,
    0,    0,    0,   16,    0,    0,    0,    0,    0,    0,
  -18,   37,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  455,  438,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  544,  -67,  456,    0,   98,  129,  -90,    0,    0,    8,
  -28,   56,    0,  157,  302,  362,   22,    0,    0,    0,
    0,    0,  -20,  436,    0,    0,    0,    0,  -54,  276,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  194,  -26,    0,  313,    0,    0,    0,    0,  321,
  259,  176,    0,    0, -127,    0,    0,    0,    0,  374,
    0,    0,    0,
};
final static int YYTABLESIZE=728;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         92,
   28,    6,   91,   15,  131,   45,    4,   57,  244,   16,
   57,   16,   45,  199,  251,   45,  133,  169,   66,   16,
  187,   16,  137,   20,  133,  137,  193,   16,   95,   15,
   86,   95,  163,  172,  139,   82,   64,   42,   14,  142,
   28,  152,  141,   41,  156,  189,  202,   87,   95,  140,
  104,   23,  142,   40,  114,  185,  103,   17,  184,  117,
  175,   92,   83,   15,  116,  114,   16,   17,  154,   17,
  178,  154,  109,  240,  110,   17,   16,  140,  216,   99,
  140,   93,   15,    1,    2,  114,   80,  117,   81,   16,
   95,   69,  116,  173,   98,  114,   15,  230,    8,  108,
  101,   16,  115,  105,  120,  119,  151,   15,  158,  114,
  159,  186,   69,  115,   17,  176,  166,  163,  172,   15,
  195,  197,  205,  200,   17,   28,   57,  201,  204,    4,
   15,   45,  208,  115,  212,  210,   23,   17,  211,  217,
  232,   15,   41,  115,  226,  227,   14,  245,  223,   17,
  228,  236,  249,  203,  242,   92,  237,  115,  222,   45,
   12,   46,   16,  252,  257,   69,   45,  179,  114,   86,
   45,   15,   45,  221,  114,  114,  258,   15,  160,    3,
  157,  254,   95,   16,  132,  109,   87,  110,   10,  247,
   15,  122,  164,  248,  114,  109,  235,  110,  117,   90,
  123,  114,   16,   15,  169,  243,  158,  124,   11,  241,
   17,  250,  129,  127,  128,  219,  115,  121,   43,   79,
    8,   45,  115,  115,    8,   43,  259,    8,   43,    9,
   10,   17,  142,   11,   92,   12,  168,   13,  142,  132,
   68,  192,  115,   77,   16,  142,    8,  163,   45,  115,
   17,  142,   62,   63,  139,    9,   10,   28,    5,   16,
  106,   12,   16,   13,   45,  104,   28,   28,   23,  140,
   28,  103,   28,  256,   28,  153,  166,   23,   23,    7,
    8,   23,  167,   23,  231,   23,    8,  108,  206,    9,
   10,   97,   17,   11,  183,   12,  215,   13,   67,    8,
   95,   95,   95,   95,   95,   37,   95,   17,    9,   10,
   17,  100,    7,    8,   12,   68,   13,  150,   95,   95,
   95,   95,    9,   10,    8,  209,   11,  109,   12,  110,
   13,   45,  149,    9,   10,    7,    8,   11,   45,   12,
  229,   13,   56,    8,   43,    9,   10,    8,  160,   11,
  224,   12,   45,   13,   45,  138,    9,   10,    8,  137,
   11,   41,   12,   45,   13,   38,  218,    9,   10,  122,
  220,   11,   43,   12,  136,   13,   12,   12,    8,   43,
   44,   84,    8,   43,    8,   43,   12,   12,    8,   85,
   12,  157,   12,  233,   12,  117,  198,    9,   10,    8,
  108,   11,  109,   12,  110,   13,  234,    8,  117,    8,
  108,  253,  123,  124,  125,  126,    9,   10,  162,  107,
    8,    0,   12,   73,   13,   73,   73,   73,    0,    9,
   10,    0,    0,    8,   43,   12,   76,   13,   76,   76,
   76,   73,   73,   73,   73,    0,   74,   70,   74,   74,
   74,    0,    0,    0,   76,   76,   76,   76,    0,   76,
    8,   43,    0,    0,   74,   74,   74,   74,   80,   80,
    0,   80,  109,   80,  110,    0,    8,   43,   44,   79,
   79,    0,   79,    0,   79,   80,    0,  109,  174,  110,
  109,    0,  110,   73,   70,   73,   79,   74,    0,   74,
    0,    0,    0,  213,  154,    0,  214,    0,    0,   73,
    0,    0,    0,   74,    0,   95,   95,   95,    0,   95,
   95,   95,   95,   95,   95,    0,    0,    0,    0,  147,
  148,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  170,    8,  108,    8,   43,    0,    0,    0,    0,  188,
    8,   43,   47,    0,    0,  180,   78,    0,    0,   88,
    0,    0,    0,    0,    8,   43,    8,   43,    0,    0,
    0,  134,  135,  191,  190,    8,   43,    0,    0,    0,
    0,    0,    0,    0,    0,  102,    0,    0,    0,   47,
  207,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    8,  108,    0,    0,
  161,    0,    0,    0,    0,    0,    0,  167,  168,    0,
   70,    0,    0,    0,  225,    0,    0,    0,   73,   73,
   73,    0,   73,   73,   73,   73,    0,   73,    0,    0,
    0,   76,   76,   76,    0,   76,   76,   76,   76,    0,
   76,   74,   74,   74,    0,   74,   74,   74,   74,    0,
   74,  246,    0,  194,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   80,   80,    0,    8,  108,    0,    0,
    0,    0,    0,    0,   79,   79,    0,    0,    0,    0,
    0,    8,  108,    0,    8,  108,    0,   73,   73,    0,
    0,   74,   74,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  102,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,    0,   59,   40,   59,   45,    0,   40,   59,    2,
   40,    0,   45,  141,   59,   45,   41,   85,   11,   12,
  111,   14,   41,    2,   41,   44,   41,   20,   41,   40,
   44,   44,   41,  257,  256,   14,   40,   40,  123,  257,
   40,   68,   44,   46,   71,  113,  270,   61,   61,  271,
   41,   40,  270,  125,   47,   41,   41,    2,   44,   42,
   89,   82,  270,   40,   47,   58,   59,   12,   41,   14,
   99,   44,   43,  201,   45,   20,   69,   41,  169,   44,
   44,  123,   40,  256,  257,   78,  123,   42,  125,   82,
   59,  123,   47,   86,   59,   88,   40,  256,  257,  258,
  257,   94,   47,  258,  256,   59,  264,   40,   59,  102,
  256,   41,  123,   58,   59,   94,  257,  125,  257,   40,
  268,  257,  151,   41,   69,  125,   40,   44,  257,  123,
   40,   45,   59,   78,   41,  125,  125,   82,  125,  256,
  195,   40,   46,   88,   59,  256,  123,  215,  177,   94,
  257,   41,   41,  146,   59,  176,  123,  102,   44,   45,
   40,   40,  155,  123,  256,  123,   45,   40,  161,   44,
   45,   40,   45,   59,  167,  168,   44,    0,  256,    0,
  256,  125,  264,  176,   41,   43,   61,   45,    0,  218,
   40,  256,  125,  222,  187,   43,   59,   45,   59,  256,
  256,  194,  195,   40,  256,  256,  256,  256,    0,  202,
  155,  256,   60,   61,   62,  125,  161,   41,  258,  256,
  257,   45,  167,  168,  257,  258,  125,  257,  258,  266,
  267,  176,  257,  270,  255,  272,  256,  274,  257,  256,
  273,  256,  187,   40,  237,  270,  257,  256,   45,  194,
  195,  270,  256,  257,  256,  266,  267,  257,  257,  252,
   41,  272,  255,  274,   45,  256,  266,  267,  257,  271,
  270,  256,  272,  256,  274,  125,  256,  266,  267,  256,
  257,  270,  256,  272,  187,  274,  257,  258,  125,  266,
  267,  256,  237,  270,  265,  272,  168,  274,  256,  257,
   41,   42,   43,   44,   45,    4,   47,  252,  266,  267,
  255,   36,  256,  257,  272,  273,  274,   44,   59,   60,
   61,   62,  266,  267,  257,   41,  270,   43,  272,   45,
  274,   45,   59,  266,  267,  256,  257,  270,   45,  272,
  184,  274,  256,  257,  258,  266,  267,  257,   41,  270,
   41,  272,   45,  274,   45,   40,  266,  267,  257,   44,
  270,   46,  272,   45,  274,    4,  173,  266,  267,   57,
  256,  270,  258,  272,   59,  274,  256,  257,  257,  258,
  259,  256,  257,  258,  257,  258,  266,  267,  257,  264,
  270,   71,  272,  256,  274,  256,  138,  266,  267,  257,
  258,  270,   43,  272,   45,  274,  269,  257,  269,  257,
  258,  236,  260,  261,  262,  263,  266,  267,   59,   46,
  257,   -1,  272,   41,  274,   43,   44,   45,   -1,  266,
  267,   -1,   -1,  257,  258,  272,   41,  274,   43,   44,
   45,   59,   60,   61,   62,   -1,   41,   12,   43,   44,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   -1,  256,
  257,  258,   -1,   -1,   59,   60,   61,   62,   42,   43,
   -1,   45,   43,   47,   45,   -1,  257,  258,  259,   42,
   43,   -1,   45,   -1,   47,   59,   -1,   43,   59,   45,
   43,   -1,   45,   43,   59,   45,   59,   43,   -1,   45,
   -1,   -1,   -1,   59,   69,   -1,   59,   -1,   -1,   59,
   -1,   -1,   -1,   59,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,  264,  265,   -1,   -1,   -1,   -1,  256,
  257,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   85,  257,  258,  257,  258,   -1,   -1,   -1,   -1,  256,
  257,  258,    9,   -1,   -1,  100,   13,   -1,   -1,   16,
   -1,   -1,   -1,   -1,  257,  258,  257,  258,   -1,   -1,
   -1,  256,  257,  118,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   42,   -1,   -1,   -1,   46,
  155,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,
   77,   -1,   -1,   -1,   -1,   -1,   -1,   84,   85,   -1,
  195,   -1,   -1,   -1,  179,   -1,   -1,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  216,   -1,  130,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,  257,  258,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,  257,  258,   -1,  257,  258,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  184,
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
"sentencia_ejecutable : invocacion_funcion error",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
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

//#line 837 "gramatica.y"

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
    return number.endsWith("UI");
}

// --------------------------------------------------------------------------------------------------------------------

public void altaSymbolTable(String lexema) {
    SymbolTable.getInstance().agregarEntrada(lexema);
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 737 "Parser.java"
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
//#line 83 "gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 7:
//#line 86 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 11:
//#line 107 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa"); }
break;
case 12:
//#line 110 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa"); }
break;
case 14:
//#line 113 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 15:
//#line 115 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 16:
//#line 117 "gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 23:
//#line 143 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 186 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 195 "gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 38:
//#line 203 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 47:
//#line 225 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 225 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 228 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 49:
//#line 233 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 237 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 241 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 245 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 255 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 260 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 267 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 281 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 286 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 289 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 292 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 65:
//#line 318 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 66:
//#line 320 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 67:
//#line 325 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 327 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 73:
//#line 348 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 353 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 367 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 372 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 78:
//#line 379 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
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
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 419 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 86:
//#line 425 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 437 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 88:
//#line 439 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 95:
//#line 464 "gramatica.y"
{ altaSymbolTable(val_peek(0).sval); }
break;
case 96:
//#line 466 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;
            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0UI";
            }
            altaSymbolTable(yyval.sval);
        }
break;
case 98:
//#line 481 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 100:
//#line 494 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 101:
//#line 497 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 102:
//#line 500 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 103:
//#line 503 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 106:
//#line 521 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 113:
//#line 537 "gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 114:
//#line 546 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 115:
//#line 551 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 116:
//#line 553 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 117:
//#line 555 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 118:
//#line 557 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 121:
//#line 573 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 122:
//#line 578 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 123:
//#line 580 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 125:
//#line 591 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 126:
//#line 593 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 129:
//#line 614 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 130:
//#line 619 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 132:
//#line 631 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 134:
//#line 644 "gramatica.y"
{ notifyDetection("Sentencia 'return'."); }
break;
case 135:
//#line 649 "gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 136:
//#line 651 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 137:
//#line 653 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 138:
//#line 655 "gramatica.y"
{ notifyError("Sentencia 'return' inválida."); }
break;
case 140:
//#line 666 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 143:
//#line 678 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 147:
//#line 696 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 148:
//#line 698 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 151:
//#line 710 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto."); }
break;
case 152:
//#line 719 "gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 154:
//#line 730 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 155:
//#line 737 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 156:
//#line 742 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 157:
//#line 751 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 158:
//#line 756 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 160:
//#line 767 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 161:
//#line 770 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 162:
//#line 772 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 166:
//#line 795 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 167:
//#line 800 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 169:
//#line 811 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 170:
//#line 814 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }
break;
case 171:
//#line 817 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1299 "Parser.java"
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
