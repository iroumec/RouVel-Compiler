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
    0,    0,    0,   19,    0,    0,    0,   18,   18,   15,
   16,   22,   16,   16,   16,   16,   21,   21,   20,   20,
   17,   17,   17,   23,   23,   25,   25,   28,   28,   29,
   29,   30,   30,   31,   31,   31,   24,   24,   24,   24,
   24,   24,   24,   24,   34,   34,   26,   26,   26,   26,
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
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    3,    3,    3,
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
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   39,   40,   41,   42,
   43,   44,   45,   46,    0,    0,    8,    9,    5,   23,
    0,    0,   95,  163,    0,    0,    0,    0,   82,   89,
   90,   91,    0,    0,    0,  118,    0,    0,    0,   99,
    0,   51,    0,    0,    0,    0,  123,    0,    0,   30,
    0,   31,    0,    0,    0,    0,    0,    0,   17,   14,
    0,    0,    0,    0,    0,   71,    0,    0,   38,   37,
   22,   18,    0,   29,   27,   67,   65,    0,    0,   98,
    0,    0,  152,   96,    0,    0,   93,   79,   80,    0,
   85,    0,   92,   94,   87,   88,    0,  156,  157,  101,
    0,  109,  111,  110,  112,  113,  107,  108,    0,    0,
  102,  100,   49,   56,   48,    0,    0,  150,  149,    0,
    0,    0,  140,  142,    0,   50,   55,   47,    0,    0,
  128,    0,   33,   34,    0,    0,  127,  121,  122,    0,
    0,  137,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   58,    0,    0,   60,   69,    0,    0,   72,    0,
    0,    0,    0,    0,    0,  151,  162,    0,   77,    0,
   83,   81,  103,  104,    0,    0,    0,   53,    0,  144,
    0,    0,    0,  147,   54,    0,   36,   32,   35,  136,
    0,   19,   20,  172,   59,   61,   57,    0,    0,   62,
    0,   13,   68,   66,    0,   74,    0,    0,  165,  166,
  154,  153,   86,   84,  120,  117,    0,  116,    0,    0,
  131,  130,  141,  145,   52,  135,  134,    0,    0,    0,
   73,  171,  115,  114,    0,  129,  132,    0,   63,   64,
   70,  133,
};
final static short yydgoto[] = {                          3,
   58,   48,   49,  110,  111,  112,  117,   65,  178,   50,
   51,   52,  102,  103,   18,   19,  258,   39,    4,  165,
   21,   93,   22,   23,   24,   25,   26,   95,   71,   72,
  155,   27,   28,   29,   30,   31,   32,   33,   34,  172,
   35,   88,   59,   60,   61,  129,  197,   73,   74,   75,
  141,  241,  242,  142,  143,  144,  145,   53,   54,   55,
   36,  182,  183,
};
final static short yysindex[] = {                      -153,
    2,   54,    0, -101,    0,    0,  -85,   34,  125,  -34,
   94,  -37,  -10,   31, -246,  262,  -49,    0,    0,   42,
  -79,    0,    0,    0,    0,   -1,    0,    0,    0,    0,
    0,    0,    0,    0,   10,  -63,    0,    0,    0,    0,
 -185,  154,    0,    0, -177,  137,  259,   50,    0,    0,
    0,    0,   51, -168,    0,    0,  172,  229,  147,    0,
  -28,    0,   73,  -40,  333, -148,    0,    5,   75,    0,
  -31,    0,   52, -125,    0,  255,  110,   16,    0,    0,
   87, -122,  154,  295, -117,    0,  117,   55,    0,    0,
    0,    0,   99,    0,    0,    0,    0,   55,   23,    0,
   80,   57,    0,    0,    0,  102,    0,    0,    0,   50,
    0,  301,    0,    0,    0,    0,  307,    0,    0,    0,
   -5,    0,    0,    0,    0,    0,    0,    0,  154, -123,
    0,    0,    0,    0,    0, -111,  -40,    0,    0, -229,
  106,  104,    0,    0, -223,    0,    0,    0, -107,   55,
    0,  171,    0,    0,  168,    0,    0,    0,    0,   93,
  152,    0,    0,   41,   47,  122,  396,  -82,  439,   50,
  -77,    0,  134,   -6,    0,    0,  119,  -13,    0,  298,
    0,  126,  -75,  -73,  154,    0,    0, -136,    0,   50,
    0,    0,    0,    0,  259,  147,  146,    0,  145,    0,
   92, -229, -117,    0,    0,  129,    0,    0,    0,    0,
  -38,    0,    0,    0,    0,    0,    0,  301,  307,    0,
   55,    0,    0,    0,   55,    0,    0,  155,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,  142,  -17,
    0,    0,    0,    0,    0,    0,    0,   20,  -65,  159,
    0,    0,    0,    0,   99,    0,    0,  131,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                        19,
    0,  210,    0,  210,    0,    0,    0,  405,   27,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  322,
  150,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -4,  274,    0,    0,
    0,    0,  114,    0,  116,    0,    0,   29,    0,    0,
    0,    0,   63,  -36,    0,    0,    0,    0,    0,    0,
    0,    0,  143,    0,  144,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  162,    0,
   61,    0,    0,    0, -125,    0,    0,    0,    0,  319,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  201,
    0,    0,    0,    0,    0,    0,  -36,    0,    0, -180,
    0,  317,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  167,    0,    0,    0,    0,
    0,    0,   12,  348,  387,    0,    0,    0,    0,  451,
  426,    0,   15,    0,    0,    0,    0,    0,    0,    0,
  170,  175,    0,    0,    0,    0,    0,    0,    0,  415,
    0,    0,    0,    0,   38,    0,    0,    0,    0,    0,
    0,  -16,   68,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  176,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  456,  436,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  558,  -69,  -51,    0,  202,  239,  -81,    0,    0,    6,
  423,   37,    0,  242,  429,  432,   71,    0,    0,    0,
    0,    0,  -20,  485,    0,    0,    0,    0,  -52,  408,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  277,   65,    0,  397,    0,    0,    0,    0,  382,
  324,  223,    0,    0, -124,    0,    0,    0,    0,  417,
    0,    0,    0,
};
final static int YYTABLESIZE=743;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         91,
   28,    6,   15,  140,  139,   57,  130,   16,   57,   90,
   45,   16,  132,   45,  170,  200,   66,   16,    4,   16,
  247,   14,   15,   82,  143,   16,  138,  143,  188,   76,
  225,   45,  171,  173,   45,  194,  164,   85,   17,   40,
   28,  139,  190,   92,   57,  224,  203,  181,   17,   45,
   17,   23,  113,   98,   86,   97,   17,   94,   97,   69,
   91,  116,  180,  113,   16,  192,  115,   45,   97,  106,
   15,  100,   20,   42,   16,   97,  148,  243,  105,   41,
  104,   15,  113,  114,   81,   69,   16,  119,  219,  148,
  174,  116,  113,   15,  114,   17,  115,  186,   16,   45,
  185,  155,    1,    2,  155,   17,  113,  257,  146,  118,
  158,  146,  137,  114,   15,  150,  136,   17,   41,  233,
    8,  107,  108,  114,  109,   28,   15,  254,  228,   17,
  159,  135,  151,   64,  166,  156,   23,  114,   15,  173,
  163,    4,  187,  235,  196,  198,  201,  202,  248,  205,
  204,  210,  108,   79,  109,   80,   91,   16,   15,  108,
   16,  109,  214,  177,   46,  212,  113,  249,  162,   45,
   15,  213,  113,  216,  113,  175,   14,  105,  220,   41,
  230,   45,   16,  231,  229,  239,   15,  245,   17,   12,
  260,   17,  211,  113,  108,  252,  109,  114,   45,  153,
  113,   16,  261,  114,  238,  114,   89,   15,  244,   15,
   15,  164,  120,   17,  240,  138,   45,  246,   67,    8,
  148,   56,    8,   43,  114,    8,   43,  131,    9,   10,
  139,  114,   17,  148,   12,   68,   13,   91,    7,    8,
  148,   68,  223,  222,   43,   16,    8,   43,    9,   10,
  193,  164,   11,  148,   12,  262,   13,   28,    5,  119,
   16,    8,   43,   16,  255,   96,   28,   28,   23,   69,
   28,  108,   28,  109,   28,  259,   17,   23,   23,    8,
   43,   23,  161,   23,  106,   23,   78,    8,  128,  126,
  127,   17,  208,  105,   17,  160,    9,   10,    8,   45,
   11,  108,   12,  109,   13,   85,   45,    9,   10,    7,
    8,   11,   43,   12,   75,   13,   75,   75,   75,    9,
   10,    3,   86,   11,  253,   12,   97,   13,  133,  134,
  152,    8,   75,   75,   75,   75,    8,  107,  227,   45,
    9,   10,   45,    8,  184,   45,   12,   10,   13,   62,
   63,   45,    9,   10,    7,    8,   11,  138,   12,   78,
   13,   78,   78,   78,    9,   10,    8,  107,   11,  158,
   12,  160,   13,    8,  107,    8,  149,   78,   78,   78,
   78,    8,   43,   44,    9,   10,   11,    8,   11,  234,
   12,  148,   13,    8,   43,   44,    9,   10,  124,  125,
   11,  236,   12,    8,   13,   12,   12,  218,    8,  107,
    8,   43,    9,   10,  237,   12,   12,  170,   12,   12,
   13,   12,  126,   12,    8,  169,  232,    8,    8,   43,
  167,  168,   37,    9,   10,   38,    9,   10,  108,   12,
  109,   13,   12,   99,   13,   97,   97,   97,   97,   97,
  221,   97,  157,  121,  215,   76,  119,   76,   76,   76,
  199,  256,  106,   97,   97,   97,   97,   82,   82,  119,
   82,    0,   82,   76,   76,   76,   76,   81,   81,    0,
   81,  108,   81,  109,   82,    8,  107,    0,  122,  123,
  124,  125,    0,   75,   81,   75,   70,  217,   76,    0,
   76,    0,    0,    0,    0,    0,    0,    0,    0,   75,
  176,    8,   43,    0,   76,    8,  107,   83,    8,   43,
  179,    0,    0,    0,    0,   84,    0,    0,    0,   75,
   75,   75,    0,   75,   75,   75,   75,    0,   75,    0,
    0,    0,    0,   70,    0,    0,    0,    0,    0,    0,
  168,    8,   43,  154,    8,   43,  189,    8,   43,    0,
    0,    0,  191,    8,   43,    0,   47,    0,    0,    0,
   77,    0,  206,   87,   78,   78,   78,    0,   78,   78,
   78,   78,    0,   78,    0,    0,    0,    0,  146,  147,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  101,
  226,    0,    0,   47,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  161,    0,    0,  207,    0,    0,  209,
  167,  169,    0,  250,    0,    0,    0,  251,    0,    0,
    0,    0,    8,  107,    0,    0,    0,    0,    0,    0,
   97,   97,   97,    0,   97,   97,   97,   97,   97,   97,
   76,   76,   76,    0,   76,   76,   76,   76,    0,   76,
   70,    0,   82,   82,    0,    0,  195,    0,    0,    0,
    0,    0,   81,   81,    0,    8,  107,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   75,   75,    0,
    0,    0,   76,   76,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,    0,   40,   44,   41,   40,   59,    2,   40,   59,
   45,    0,   41,   45,   84,  140,   11,   12,    0,   14,
   59,  123,   40,  270,   41,   20,  256,   44,  110,   40,
   44,   45,   84,  257,   45,   41,   41,   44,    2,  125,
   40,  271,  112,  123,   40,   59,  270,   99,   12,   45,
   14,   40,   47,   44,   61,   41,   20,   59,   44,  123,
   81,   42,   40,   58,   59,  117,   47,   45,   59,   41,
   40,  257,    2,   40,   69,   61,  257,  202,   41,   46,
  258,   40,   77,   47,   14,  123,   81,  256,  170,  270,
   85,   42,   87,   40,   58,   59,   47,   41,   93,   45,
   44,   41,  256,  257,   44,   69,  101,  125,   41,   59,
   59,   44,   40,   77,   40,  264,   44,   81,   46,  256,
  257,  258,   43,   87,   45,  125,   40,   59,  180,   93,
  256,   59,   68,   40,  257,   71,  125,  101,   40,  257,
  125,  123,   41,  196,  268,  257,   41,   44,  218,  257,
  145,   59,   43,  123,   45,  125,  177,  152,   40,   43,
  155,   45,   41,   93,   40,  125,  161,  219,   59,   45,
   40,  125,  167,  256,  169,   59,  123,   41,  256,   46,
  256,   45,  177,  257,   59,   41,   40,   59,  152,   40,
  256,  155,   41,  188,   43,   41,   45,  161,   45,  125,
  195,  196,   44,  167,   59,  169,  256,   40,  203,    0,
   40,  125,   41,  177,  123,  256,   45,  256,  256,  257,
  257,  256,  257,  258,  188,  257,  258,  256,  266,  267,
  271,  195,  196,  270,  272,  273,  274,  258,  256,  257,
  257,  273,  256,  125,  258,  240,  257,  258,  266,  267,
  256,  256,  270,  270,  272,  125,  274,  257,  257,   59,
  255,  257,  258,  258,  123,  256,  266,  267,  257,  123,
  270,   43,  272,   45,  274,  256,  240,  266,  267,  257,
  258,  270,  256,  272,  256,  274,  256,  257,   60,   61,
   62,  255,  125,  256,  258,   41,  266,  267,  257,   45,
  270,   43,  272,   45,  274,   44,   45,  266,  267,  256,
  257,  270,  258,  272,   41,  274,   43,   44,   45,  266,
  267,    0,   61,  270,  256,  272,  264,  274,  256,  257,
  256,  257,   59,   60,   61,   62,  257,  258,   41,   45,
  266,  267,   45,  257,  265,   45,  272,    0,  274,  256,
  257,   45,  266,  267,  256,  257,  270,   41,  272,   41,
  274,   43,   44,   45,  266,  267,  257,  258,  270,  256,
  272,  256,  274,  257,  258,  257,   44,   59,   60,   61,
   62,  257,  258,  259,  266,  267,    0,  257,  270,  188,
  272,   59,  274,  257,  258,  259,  266,  267,  256,  256,
  270,  256,  272,  257,  274,  256,  257,  169,  257,  258,
  257,  258,  266,  267,  269,  266,  267,  256,  272,  270,
  274,  272,  256,  274,  257,  256,  185,  257,  257,  258,
  256,  256,    4,  266,  267,    4,  266,  267,   43,  272,
   45,  274,  272,   36,  274,   41,   42,   43,   44,   45,
  174,   47,   71,   57,   59,   41,  256,   43,   44,   45,
  137,  239,   46,   59,   60,   61,   62,   42,   43,  269,
   45,   -1,   47,   59,   60,   61,   62,   42,   43,   -1,
   45,   43,   47,   45,   59,  257,  258,   -1,  260,  261,
  262,  263,   -1,   43,   59,   45,   12,   59,   43,   -1,
   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,
   88,  257,  258,   -1,   59,  257,  258,  256,  257,  258,
   98,   -1,   -1,   -1,   -1,  264,   -1,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,   -1,   -1,   59,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   69,  257,  258,  256,  257,  258,   -1,
   -1,   -1,  256,  257,  258,   -1,    9,   -1,   -1,   -1,
   13,   -1,  150,   16,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,   -1,   -1,   -1,   -1,  256,  257,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   42,
  178,   -1,   -1,   46,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   76,   -1,   -1,  152,   -1,   -1,  155,
   83,   84,   -1,  221,   -1,   -1,   -1,  225,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,  264,  265,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
  196,   -1,  257,  258,   -1,   -1,  129,   -1,   -1,   -1,
   -1,   -1,  257,  258,   -1,  257,  258,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  185,
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

//#line 826 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
//#line 822 "Parser.java"
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
case 6:
//#line 82 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }."); }
break;
case 7:
//#line 85 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 11:
//#line 106 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa"); }
break;
case 12:
//#line 109 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa"); }
break;
case 14:
//#line 112 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 15:
//#line 114 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 16:
//#line 116 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?"); }
break;
case 23:
//#line 142 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 185 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 194 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 38:
//#line 202 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 47:
//#line 224 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 48:
//#line 227 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 49:
//#line 232 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 236 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 51:
//#line 240 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 244 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 254 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 259 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 266 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 280 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 285 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 288 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 291 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 65:
//#line 317 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 66:
//#line 319 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 67:
//#line 324 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 326 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 73:
//#line 347 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 352 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 366 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 371 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 78:
//#line 378 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 391 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "+"; }
break;
case 80:
//#line 393 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-"; }
break;
case 81:
//#line 400 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 83:
//#line 406 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 84:
//#line 418 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 86:
//#line 424 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 436 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "/"; }
break;
case 88:
//#line 438 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "*"; }
break;
case 96:
//#line 464 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 98:
//#line 472 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 100:
//#line 485 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 101:
//#line 488 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 102:
//#line 491 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 103:
//#line 494 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 106:
//#line 512 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 113:
//#line 528 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 114:
//#line 537 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 115:
//#line 542 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 116:
//#line 544 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 117:
//#line 546 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 118:
//#line 548 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 121:
//#line 564 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 122:
//#line 569 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 123:
//#line 571 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 125:
//#line 582 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 126:
//#line 584 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 129:
//#line 605 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 130:
//#line 610 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 132:
//#line 622 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 134:
//#line 635 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'return'."); }
break;
case 135:
//#line 640 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 136:
//#line 642 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 137:
//#line 644 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 139:
//#line 655 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 142:
//#line 667 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 146:
//#line 685 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 147:
//#line 687 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 150:
//#line 699 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto."); }
break;
case 151:
//#line 708 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 153:
//#line 719 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 154:
//#line 726 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 155:
//#line 731 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 156:
//#line 740 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 157:
//#line 745 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 159:
//#line 756 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 160:
//#line 759 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 161:
//#line 761 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 165:
//#line 784 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 166:
//#line 789 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 168:
//#line 800 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 169:
//#line 803 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis"); }
break;
case 170:
//#line 806 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1369 "Parser.java"
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
