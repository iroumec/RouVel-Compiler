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
    0,    0,    0,   19,    0,    0,    0,    0,   18,   18,
   15,   16,   22,   16,   16,   16,   16,   21,   21,   20,
   20,   17,   17,   17,   23,   23,   25,   25,   28,   28,
   29,   29,   30,   30,   31,   31,   31,   24,   24,   24,
   24,   24,   24,   24,   24,   34,   34,   26,   26,   26,
   26,   26,    8,    8,    8,    8,   32,   32,   32,   32,
   32,   40,   40,   40,   33,   33,   33,   33,   41,   42,
   42,    9,    9,    9,    1,    1,    1,    1,    6,    6,
    2,    2,    2,    4,    4,    4,    7,    7,    3,    3,
    3,    5,    5,    5,   11,   11,   10,   10,   43,   43,
   43,   43,   43,   44,   45,   45,   46,   46,   46,   46,
   46,   46,   46,   38,   38,   38,   38,   38,   47,   47,
   39,   39,   39,   49,   49,   49,   48,   50,   27,   27,
   53,   53,   52,   35,   35,   35,   35,   51,   51,   54,
   54,   54,   56,   56,   55,   55,   55,   57,   57,   57,
   12,   13,   13,   14,   14,   36,   36,   59,   59,   59,
   59,   58,   60,   60,   37,   37,   63,   63,   63,   63,
   62,   61,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    1,    2,    2,    1,    1,
    3,    3,    0,    4,    2,    0,    3,    2,    2,    2,
    2,    1,    2,    2,    1,    1,    1,    2,    0,    1,
    1,    1,    3,    2,    1,    2,    2,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    3,    3,
    2,    5,    3,    3,    2,    2,    4,    3,    4,    3,
    4,    2,    4,    4,    2,    4,    2,    4,    3,    5,
    1,    1,    3,    2,    1,    3,    3,    2,    1,    1,
    3,    1,    3,    3,    1,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    1,    3,    1,    2,
    2,    2,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    6,    6,    5,    5,    2,    0,    2,
    3,    3,    2,    1,    1,    2,    2,    2,    6,    5,
    1,    2,    3,    5,    5,    4,    3,    1,    0,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    4,    1,    3,    3,    1,    3,    3,    1,    2,    1,
    0,    3,    1,    1,    4,    4,    1,    2,    1,    0,
    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   22,   25,   26,   27,    0,   40,   41,
   42,   43,   44,   45,   46,   47,    0,    0,    7,    8,
   24,    0,    0,    0,    0,    0,    1,    2,    0,    0,
   95,  163,    0,    0,    0,    0,   82,   89,   90,   91,
    0,    0,    0,  118,    0,    0,    0,   99,    0,   51,
    0,    0,    0,    0,  123,    0,    0,   31,    0,   32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   71,
    0,    0,   39,   38,   23,    9,   10,    5,   30,   28,
   67,   65,    0,    0,    0,    0,   18,   15,    0,   98,
    0,    0,  152,   19,    0,   96,    0,    0,   93,   79,
   80,    0,   85,    0,   92,   94,   87,   88,    0,  156,
  157,  101,    0,  109,  111,  110,  112,  113,  107,  108,
    0,    0,  102,  100,   49,   56,    0,    0,  150,  149,
    0,    0,    0,  140,  142,    0,   50,   55,   48,    0,
    0,  128,    0,   34,   35,    0,    0,  127,  121,  122,
    0,    0,  137,    0,    0,    0,    0,    0,    0,   58,
    0,    0,   60,   69,    0,   72,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  151,    0,  162,    0,   77,
    0,   83,   81,  103,  104,    0,    0,    0,   53,    0,
  144,    0,    0,    0,  147,   54,    0,   37,   33,   36,
  136,    0,  172,   59,   61,   57,    0,    0,   62,    0,
   68,   66,    0,   74,    0,    0,  165,  166,   20,   21,
  154,  153,   14,   86,   84,  120,  117,    0,  116,    0,
    0,  131,  130,  141,  145,   52,  135,  134,    0,    0,
    0,   73,  171,  115,  114,    0,  129,  132,    0,   63,
   64,   70,  133,
};
final static short yydgoto[] = {                          9,
   56,   46,   47,  112,  113,  114,  119,   63,  175,   48,
   49,   50,  102,  103,   37,   38,  259,   88,   13,  183,
   40,  105,   14,   15,   16,   17,   18,   90,   69,   70,
  156,   19,   20,   21,   22,   23,   24,   25,   26,  170,
   27,   82,   57,   58,   59,  131,  198,   71,   72,   73,
  142,  242,  243,  143,  144,  145,  146,   51,   52,   53,
   28,  179,  180,
};
final static short yysindex[] = {                        91,
    4,  -33,   -6,  153,   -7,   53,   58, -242,    0,   66,
  -39,  175,  -67,    0,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,    0,  -14,  -47,    0,    0,
    0,  -48,   18,   32, -166,   23,    0,    0,  175,  -10,
    0,    0, -163,  255,  328,   38,    0,    0,    0,    0,
   50, -142,    0,    0,  381,  321,  189,    0,  -31,    0,
  361,   -8,  356, -146,    0,  122,   -2,    0,   34,    0,
   60, -135,    0,  383,  258, -132,  311,  229, -122,    0,
  395,   36,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   36,  139,  311,   -3,    0,    0,  102,    0,
   56,   13,    0,    0,  121,    0,    0,   95,    0,    0,
    0,   38,    0,  273,    0,    0,    0,    0,  308,    0,
    0,    0,  -30,    0,    0,    0,    0,    0,    0,    0,
  311, -130,    0,    0,    0,    0, -118,   -8,    0,    0,
 -206,   99,   97,    0,    0, -238,    0,    0,    0, -114,
   36,    0,  118,    0,    0,   88,    0,    0,    0,    0,
   85,  371,    0,  104,  459, -110,  462,   38, -105,    0,
  106,  -18,    0,    0,  399,    0,  386,    0,   94,  -96,
    0,   39,   41,  -94,  311,    0,  132,    0, -124,    0,
   38,    0,    0,    0,    0,  328,  189,  335,    0,  130,
    0,   54, -206, -122,    0,    0,  114,    0,    0,    0,
    0,  -38,    0,    0,    0,    0,  273,  308,    0,   36,
    0,    0,   36,    0,    0,  140,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -37,    0,   57,
   72,    0,    0,    0,    0,    0,    0,    0,  -24,  -74,
  141,    0,    0,    0,    0,  121,    0,    0,  151,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                        24,
    0,    2,  -73,    0,    0,    0,    0,    0,    0,    0,
    0,  186,  187,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  290,    0,    0,    0,    0,    0,  188,  163,
    0,    0,    0,    0,  -25,  407,    0,    0,    0,    0,
  -62,    0, -135,    0,    0,  -16,    0,    0,    0,    0,
  -75,  -35,    0,    0,    0,    0,    0,    0,    0,    0,
  -61,    0,  -60,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -55,    0,    0,    0,    0,    0,    0,
   48,    0,    0,    0,    0,    0,  -52,    0,    0,    0,
    0,  429,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  354,    0,    0,    0,    0,    0,  -35,    0,    0,
 -226,    0,  165,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  465,  417,    0,
   25,    0,    0,    0,    0,    0,    0,  -44,  -36,    0,
   12,  208,  209,    0,    0,    0,    0,    0,    0,    0,
  439,    0,    0,    0,    0,   21,    0,    0,    0,    0,
    0,    0,  -27,   76,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  474,  450,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  553,  -43,  496,    0,   67,   61,  -72,    0,    0,    3,
  505,   49,    0,  100,  274,  282,   73,    0,    0,    0,
    0,    0,  -12,  469,    0,    0,    0,    0,  -28,  193,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  125,   31,    0,  256,    0,    0,    0,    0,  267,
  202,  101,    0,    0,  -88,    0,    0,    0,    0,  299,
    0,    0,    0,
};
final static int YYTABLESIZE=738;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         85,
   29,   16,   10,   30,   10,  139,   36,   64,   10,  134,
  195,   17,   35,  143,   10,  164,  143,  118,  171,   84,
  248,  255,  117,    4,  106,   79,   85,   76,  132,   93,
  148,  204,   62,   44,  168,  141,   10,    8,   43,  189,
   29,   10,   80,  148,   92,   97,   97,  115,   11,  139,
   11,   24,  201,  186,   11,   34,  185,   95,  115,   10,
   11,  105,   97,   35,  140,   97,   89,   43,   97,   10,
  191,    8,   12,   55,   39,   67,   31,  115,   43,  118,
   43,  172,   11,  115,  117,   97,   85,   11,  155,   34,
  100,  155,    8,  116,  106,  218,  152,   74,  110,  157,
  111,   10,   43,  115,  116,   11,   99,   10,  120,   79,
   43,    8,  104,  121,  244,   11,  146,  151,  159,  146,
  160,  181,  154,  116,  164,   29,   80,    8,   31,  116,
    8,  234,   33,  109,  171,  188,   24,  197,  199,  202,
  203,    8,  206,  211,  213,  215,    4,   11,  205,  116,
  219,   35,  227,   11,   97,   10,   98,    8,   10,  228,
    8,   55,  231,  229,  115,  230,   43,  115,  236,  115,
  240,    8,  246,  249,   85,   67,  241,  187,  177,  256,
  253,  261,  161,   43,  262,    6,   16,    3,   97,   10,
    8,  115,   55,  158,  124,  125,  258,   43,  115,   10,
  170,   11,   13,  159,   11,  138,  245,   11,   12,  126,
  116,  169,  209,  116,    8,  116,   83,  247,  254,  167,
   94,  148,   32,   33,  133,  194,  182,  217,    8,  148,
  164,  260,    3,    4,  148,   11,    5,  116,    6,  106,
    7,   91,  148,   10,  116,   11,   85,  139,   60,   61,
   33,   41,   42,  153,   33,  235,  233,   29,   10,   97,
   29,   10,  140,    3,    4,   97,   29,   29,   24,    6,
   29,    7,   29,   43,   29,  263,  105,   24,   24,   33,
   41,   24,  168,   24,  232,   24,   86,   96,   33,   11,
   33,   41,   76,   41,   87,  107,  220,    3,    4,   43,
  110,    5,  111,    6,   11,    7,   66,   11,   65,   33,
  123,   67,   33,  109,   33,   41,  163,   43,    3,    4,
  184,   77,   33,   41,    6,   66,    7,   32,   33,   78,
   97,   97,   97,   97,   97,  158,   97,    3,    4,  200,
  257,    5,  108,    6,   33,    7,    1,    2,   97,   97,
   97,   97,   43,    3,    4,   43,    3,    4,   33,    6,
    5,    7,    6,  110,    7,  111,    0,    3,    4,    0,
  110,    5,  111,    6,   33,    7,   32,   33,   33,   41,
  130,  128,  129,    3,    4,    0,    3,    4,   33,    6,
    5,    7,    6,  239,    7,   33,   41,    3,    4,  150,
  138,    5,    0,    6,  137,    7,   35,   33,   54,   33,
   41,  212,  119,  110,  149,  111,    3,    4,   13,   13,
    5,  122,    6,  161,    7,   43,  225,   43,   13,   13,
   43,   33,   13,    0,   13,    0,   13,  110,    0,  111,
    3,    4,  223,   43,    5,   33,    6,   75,    7,   75,
   75,   75,    0,  173,    3,    4,    0,  222,   82,   82,
    6,   82,    7,   82,    0,   75,   75,   75,   75,   78,
    0,   78,   78,   78,   68,   82,    0,    0,    0,   76,
    0,   76,   76,   76,  166,   33,   41,   78,   78,   78,
   78,   81,   81,    0,   81,    0,   81,   76,   76,   76,
   76,  110,    0,  111,  110,    0,  111,   75,   81,   75,
    0,   33,   41,   42,   33,  109,   76,  214,   76,    0,
  216,    0,    0,   75,    0,   68,    0,    0,  190,   33,
   41,    0,   76,    0,    0,  155,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   97,   97,   97,    0,   97,
   97,   97,   97,   97,   97,   45,    0,    0,    0,   75,
    0,    0,   81,  192,   33,   41,    0,   33,   41,    0,
    0,    0,    0,  169,    0,    0,    0,   33,  109,    0,
  124,  125,  126,  127,   33,  109,  174,    0,  101,  178,
  237,    0,    0,    0,    0,    0,   45,  176,    0,    0,
    0,    0,    0,  238,    0,    0,    0,    0,    0,  119,
    0,  147,  148,    0,  193,    0,  135,  136,    0,    0,
    0,  208,  119,    0,  210,    0,  162,   33,  109,  165,
  167,    0,    0,    0,    0,    0,    0,   33,   41,   33,
   41,    0,   33,   41,    0,    0,    0,  101,    0,    0,
    0,   33,  109,    0,  221,  207,   41,    0,    0,    0,
    0,    0,   75,   75,   75,   68,   75,   75,   75,   75,
    0,   75,  226,   82,   82,    0,    0,    0,    0,  224,
    0,    0,    0,  196,   78,   78,   78,    0,   78,   78,
   78,   78,    0,   78,   76,   76,   76,    0,   76,   76,
   76,   76,    0,   76,    0,    0,   81,   81,    0,    0,
    0,    0,    0,  250,    0,   33,  109,    0,   33,  109,
    0,   75,   75,    0,  251,    0,    0,  252,    0,    0,
   76,   76,    0,    0,    0,    0,    0,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
    0,    0,    0,    0,    2,   41,   40,    5,    6,   41,
   41,    0,   46,   41,   12,   41,   44,   42,  257,   59,
   59,   59,   47,    0,   41,   44,   39,  270,   57,   44,
  257,  270,   40,   40,   78,   44,   34,   40,   45,  112,
   40,   39,   61,  270,   59,   44,   45,   45,    0,  256,
    2,   40,  141,   41,    6,  123,   44,   40,   56,   57,
   12,   41,   61,   46,  271,   41,   59,   45,   44,   67,
  114,   40,    0,   40,    2,  123,  125,   75,   45,   42,
   45,   79,   34,   81,   47,   61,   99,   39,   41,  123,
  257,   44,   40,   45,  258,  168,   66,   40,   43,   69,
   45,   99,   45,  101,   56,   57,   34,  105,   59,   44,
   45,   40,  123,  256,  203,   67,   41,  264,   59,   44,
  256,  125,  125,   75,  257,  125,   61,   40,  125,   81,
   40,  256,  257,  258,  257,   41,  125,  268,  257,   41,
   44,   40,  257,   59,   41,  256,  123,   99,  146,  101,
  256,   46,   59,  105,  123,  153,  125,   40,  156,  256,
   40,   40,  257,  125,  162,  125,   45,  165,  197,  167,
   41,   40,   59,  217,  187,  123,  123,  105,   40,  123,
   41,  256,  256,   45,   44,    0,    0,    0,  264,  187,
   40,  189,   40,  256,  256,  256,  125,   45,  196,  197,
  256,  153,   40,  256,  156,   41,  204,    0,    0,  256,
  162,  256,  125,  165,   40,  167,  256,  256,  256,  256,
   28,  257,  256,  257,  256,  256,  125,  167,   40,  257,
  256,  256,  266,  267,  270,  187,  270,  189,  272,  256,
  274,  256,  270,  241,  196,  197,  259,  256,  256,  257,
  257,  258,  259,  256,  257,  189,  125,  257,  256,  258,
  257,  259,  271,  266,  267,  264,  266,  267,  257,  272,
  270,  274,  272,   45,  274,  125,  256,  266,  267,  257,
  258,  270,  256,  272,  185,  274,   13,  256,  257,  241,
  257,  258,  270,  258,   13,   41,  172,  266,  267,   45,
   43,  270,   45,  272,  256,  274,  273,  259,  256,  257,
   55,  123,  257,  258,  257,  258,   59,   45,  266,  267,
  265,  256,  257,  258,  272,  273,  274,  256,  257,  264,
   41,   42,   43,   44,   45,   69,   47,  266,  267,  138,
  240,  270,   44,  272,  257,  274,  256,  257,   59,   60,
   61,   62,   45,  266,  267,   45,  266,  267,  257,  272,
  270,  274,  272,   43,  274,   45,   -1,  266,  267,   -1,
   43,  270,   45,  272,  257,  274,  256,  257,  257,  258,
   60,   61,   62,  266,  267,   -1,  266,  267,  257,  272,
  270,  274,  272,   59,  274,  257,  258,  266,  267,   44,
   40,  270,   -1,  272,   44,  274,   46,  257,  256,  257,
  258,   41,   59,   43,   59,   45,  266,  267,  256,  257,
  270,   41,  272,   41,  274,   45,   41,   45,  266,  267,
   45,  257,  270,   -1,  272,   -1,  274,   43,   -1,   45,
  266,  267,   44,   45,  270,  257,  272,   41,  274,   43,
   44,   45,   -1,   59,  266,  267,   -1,   59,   42,   43,
  272,   45,  274,   47,   -1,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,    6,   59,   -1,   -1,   -1,   41,
   -1,   43,   44,   45,  256,  257,  258,   59,   60,   61,
   62,   42,   43,   -1,   45,   -1,   47,   59,   60,   61,
   62,   43,   -1,   45,   43,   -1,   45,   43,   59,   45,
   -1,  257,  258,  259,  257,  258,   43,   59,   45,   -1,
   59,   -1,   -1,   59,   -1,   57,   -1,   -1,  256,  257,
  258,   -1,   59,   -1,   -1,   67,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,  264,  265,    3,   -1,   -1,   -1,    7,
   -1,   -1,   10,  256,  257,  258,   -1,  257,  258,   -1,
   -1,   -1,   -1,   78,   -1,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,  257,  258,   82,   -1,   36,   94,
  256,   -1,   -1,   -1,   -1,   -1,   44,   93,   -1,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,   -1,  256,
   -1,  256,  257,   -1,  119,   -1,  256,  257,   -1,   -1,
   -1,  153,  269,   -1,  156,   -1,   74,  257,  258,   77,
   78,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  257,
  258,   -1,  257,  258,   -1,   -1,   -1,   95,   -1,   -1,
   -1,  257,  258,   -1,  256,  151,  258,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,  197,  260,  261,  262,  263,
   -1,  265,  177,  257,  258,   -1,   -1,   -1,   -1,  175,
   -1,   -1,   -1,  131,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,   -1,   -1,  257,  258,   -1,   -1,
   -1,   -1,   -1,  218,   -1,  257,  258,   -1,  257,  258,
   -1,  257,  258,   -1,  220,   -1,   -1,  223,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,  185,
};
}
final static short YYFINAL=9;
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
"programa : conjunto_sentencias",
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
"asignacion_simple : variable DASIG expresion_o_termino",
"asignacion_simple : variable error expresion ';'",
"asignacion_simple : variable expresion ';'",
"asignacion_simple : variable DASIG error error",
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

//#line 824 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
//#line 820 "Parser.java"
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
case 3:
//#line 76 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 79 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 7:
//#line 84 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 8:
//#line 86 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 12:
//#line 107 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa"); }
break;
case 13:
//#line 110 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa"); }
break;
case 15:
//#line 113 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 16:
//#line 115 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 17:
//#line 117 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 24:
//#line 143 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 34:
//#line 186 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 195 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 39:
//#line 203 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 48:
//#line 225 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 49:
//#line 230 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 234 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 51:
//#line 238 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 242 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 252 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 257 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 264 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 278 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 283 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 286 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 289 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 65:
//#line 315 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 66:
//#line 317 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 67:
//#line 322 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 324 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 73:
//#line 345 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 350 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 364 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 369 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 78:
//#line 376 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 389 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "+"; }
break;
case 80:
//#line 391 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-"; }
break;
case 81:
//#line 398 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 83:
//#line 404 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 416 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 86:
//#line 422 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 434 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "/"; }
break;
case 88:
//#line 436 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "*"; }
break;
case 96:
//#line 462 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 98:
//#line 470 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 100:
//#line 483 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 101:
//#line 486 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 102:
//#line 489 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 103:
//#line 492 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 106:
//#line 510 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 113:
//#line 526 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 114:
//#line 535 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 115:
//#line 540 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 116:
//#line 542 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 117:
//#line 544 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 118:
//#line 546 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 121:
//#line 562 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 122:
//#line 567 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 123:
//#line 569 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 125:
//#line 580 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 126:
//#line 582 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 129:
//#line 603 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 130:
//#line 608 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 132:
//#line 620 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 134:
//#line 633 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'return'."); }
break;
case 135:
//#line 638 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 136:
//#line 640 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 137:
//#line 642 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 139:
//#line 653 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 142:
//#line 665 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 146:
//#line 683 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 147:
//#line 685 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 150:
//#line 697 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto."); }
break;
case 151:
//#line 706 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 153:
//#line 717 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 154:
//#line 724 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 155:
//#line 729 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 156:
//#line 738 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 157:
//#line 743 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 159:
//#line 754 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 160:
//#line 757 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 161:
//#line 759 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 165:
//#line 782 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 166:
//#line 787 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 168:
//#line 798 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 169:
//#line 801 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }
break;
case 170:
//#line 804 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
