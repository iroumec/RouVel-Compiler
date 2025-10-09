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
   24,   24,   24,   24,   34,   34,   26,   26,   26,   26,
   26,   26,    8,    8,    8,    8,   32,   40,   40,   40,
   33,   33,   33,   33,   41,   42,   42,   43,   43,   44,
   44,    9,    9,    9,    1,    1,    1,    1,    1,    6,
    6,    2,    2,    2,    2,    4,    4,    4,    7,    7,
    3,    3,    3,    5,    5,    5,   11,   11,   10,   10,
   45,   45,   45,   45,   45,   46,   46,   47,   47,   47,
   47,   47,   47,   47,   38,   38,   38,   38,   38,   48,
   48,   39,   39,   39,   49,   50,   50,   50,   51,   27,
   27,   54,   54,   53,   52,   52,   55,   55,   55,   57,
   57,   56,   56,   56,   58,   58,   58,   35,   35,   35,
   35,   35,   12,   13,   13,   14,   14,   36,   36,   60,
   60,   60,   60,   59,   61,   61,   37,   37,   37,   37,
   37,   64,   64,   64,   63,   62,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    2,    1,    1,    1,
    1,    1,    2,    2,    1,    1,    3,    3,    3,    3,
    2,    5,    3,    3,    2,    2,    3,    4,    2,    3,
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
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,    0,   38,   39,   40,
   41,   42,   45,   46,    0,    0,    8,    9,    5,   23,
    0,    0,   97,  165,    0,    0,   89,   90,    0,    0,
    0,   83,    0,    0,   92,   93,    0,    0,    0,  119,
    0,    0,    0,    0,   51,    0,    0,    0,    0,  124,
    0,    0,   30,    0,   31,    0,    0,    0,  152,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,   67,
   69,    0,    0,   43,   36,   22,   18,    0,   29,   27,
   44,   37,   63,   61,    0,    0,   34,    0,    0,  100,
    0,    0,  154,    0,   98,    0,    0,   95,   80,   81,
    0,   87,    0,   94,   96,    0,   85,   91,  158,  159,
  103,    0,  110,  112,  111,  113,  114,  108,  109,    0,
    0,  104,  102,   49,   56,   48,    0,    0,  147,  146,
    0,    0,    0,  137,  139,    0,   50,   55,   47,    0,
    0,  129,   33,    0,    0,  125,  122,  123,    0,    0,
  151,    0,    0,    0,    0,    0,   68,   65,    0,    0,
    0,   72,    0,    0,    0,    0,    0,    0,   35,    0,
    0,    0,    0,  153,  164,    0,   77,    0,   84,   82,
  105,  101,    0,    0,    0,   53,    0,  141,    0,    0,
    0,  144,   54,    0,   32,  150,    0,   19,   20,  176,
    0,   66,   13,   64,   62,    0,   74,    0,    0,    0,
  167,  168,    0,  170,  156,  155,   88,   86,  121,  118,
    0,  117,    0,    0,  132,  131,  138,  142,   52,  149,
  148,   70,   73,  169,  175,  171,  116,  115,    0,  130,
  133,    0,  134,
};
final static short yydgoto[] = {                          3,
   62,   51,   52,  121,  122,  123,   53,   68,  181,   54,
   55,   56,  112,  113,   18,   19,  262,   39,    4,  174,
   21,   98,   22,   23,   24,   25,   26,  100,   74,   75,
  109,   27,   28,   29,   30,   31,   32,   33,   34,    0,
   35,   92,   93,  222,   63,   64,  140,  205,   76,   77,
   78,  152,  245,  246,  153,  154,  155,  156,   57,   58,
   59,   36,  190,  186,
};
final static short yysindex[] = {                      -175,
    2,  -18,    0,  -79,    0,    0,  -71,   22,  479,  491,
   20,   51,  497,   24, -224,   28,  -48,    0,    0,  136,
  -60,    0,    0,    0,    0,   40,  -46,    0,    0,    0,
    0,    0,    0,    0,   58,   38,    0,    0,    0,    0,
 -151,  526,    0,    0,  106, -148,    0,    0,  375,    6,
   61,    0,  140, -145,    0,    0,   63, -133,    0,    0,
  513,  290,  147,  -31,    0,  -12,  -28,  144, -140,    0,
  154,  158,    0,  -16,    0,   73, -121,    0,    0,  520,
  327,   11,    0,    0,   75, -112,  100,  526, -105,    0,
    0,   31,   36,    0,    0,    0,    0,   87,    0,    0,
    0,    0,    0,    0,   31,  158,    0,  114,  172,    0,
  170,   26,    0,   61,    0,    0,  116,    0,    0,    0,
   61,    0,   71,    0,    0,  323,    0,    0,    0,    0,
    0,   -6,    0,    0,    0,    0,    0,    0,    0,  526,
 -110,    0,    0,    0,    0,    0,  -97,  -28,    0,    0,
 -213,  123,  121,    0,    0, -215,    0,    0,    0,  -87,
   31,    0,    0,  186,    0,    0,    0,    0,  113,   42,
    0,    0,   50,   52,  137,    6,    0,    0,   31,   99,
  346,    0,  190,   45,  125,  -76,  114,  -41,    0,    0,
  -75,  -67,  526,    0,    0,  330,    0,   61,    0,    0,
    0,    0,    6,  147,  -51,    0,  141,    0,   68, -213,
 -105,    0,    0,  133,    0,    0,   66,    0,    0,    0,
  149,    0,    0,    0,    0,   31,    0,  -61,    0,  161,
    0,    0,  -52,    0,    0,    0,    0,    0,    0,    0,
   81,    0,   86,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   87,    0,
    0,  110,    0,
};
final static short yyrindex[] = {                        33,
    0,  211,    0,  211,    0,    0,    0,  424,  -36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  222,
  122,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -1,
  434,    0,    0,    0,    0,    0,  -25,    0,  -10,    0,
    0,   16,    0,    0,    0,   10,  -34,    0,    0,    0,
    0,    0,    0,    0,    0,   53,    0,   83,    0,    0,
    0,    0,    0,    0,    0,    0,  -23,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   92,   92,    0,
   89,    0,    0,  446,    0,  102,    0,    0,    0,    0,
  456,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -3,    0,    0,    0,    0,    0,    0,  -34,    0,    0,
 -209,    0,  246,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -145,    0,    0,    0,    0,    0,
    0,   12,  306,  326,    0,   84,    0,    0,    0,    0,
    0,    0,   92,    0,  104,    0,   92,    0,    0,  104,
    0,    0,    0,    0,    0,    0,    0,  468,    0,    0,
    0,    0,   32,    0,    0,    0,    0,    0,    0,  -38,
   90,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   55,    0,    0,    0,    0,    0,    0,    0,  118,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  461,    5,  419,    0,    0,    0,  334,    0,    0,    3,
  467,   57,    0,  153,  351,  358,   23,    0,    0,    0,
    0,    0,  -11,  558,    0,    0,    0,    0,  -33,  345,
  -27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  294,    0,    0,   70,  322,    0,    0,    0,    0,
  321,  251,  164,    0,    0, -115,    0,    0,    0,    0,
  360,    0,  315,  -89,
};
final static int YYTABLESIZE=784;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        229,
   28,    6,  140,   46,   16,  140,  136,  242,   96,  143,
   95,   16,  102,   69,   16,  151,   16,   99,   91,  191,
   99,   15,   16,   61,   20,   48,   45,  148,   46,  141,
   47,  147,    4,   41,  202,  208,   85,   99,   16,  166,
   28,   87,  149,   14,  164,   86,  146,  145,  119,  114,
  120,   23,  124,   40,  211,  120,  107,  150,   17,   67,
  145,   42,   97,   15,  124,   16,  194,   41,   17,  193,
   17,   89,  106,   96,   16,   46,   17,   15,  183,   89,
    1,    2,  217,  124,  119,  229,  120,   16,   90,   46,
   15,  177,   17,  228,  247,   91,   90,  233,   99,   71,
   16,  105,   48,   15,   14,  110,  125,   47,   16,  115,
  128,   16,   48,  124,   15,   46,  104,   47,  125,   17,
  180,  129,  130,  161,  251,   28,   15,  198,   17,  157,
  143,  167,  157,  143,  168,  172,   23,  125,   15,  258,
  162,   17,   57,  165,  175,   41,   83,   48,   84,   15,
   46,   87,   47,  184,   17,    4,  195,  204,  212,  206,
  106,   12,   17,  209,  210,   17,   16,  125,   96,  213,
  239,  216,  124,   72,  218,   15,  219,  220,  124,  232,
  234,  243,   16,  231,   46,   16,   15,  160,  261,  235,
  244,  249,  252,   61,  254,   48,   45,   15,   46,  173,
   47,  255,  159,  256,  240,  124,   16,   94,  259,  101,
   15,  188,  119,  248,  120,    8,   43,  241,  145,  163,
   17,    3,  145,  223,  142,   15,  125,  149,   86,  188,
  160,  145,  125,   99,  263,  145,   17,    7,    8,   17,
    8,   43,  150,  144,  145,  162,   16,    9,   10,  201,
   96,   11,  120,   12,  166,   13,   71,   28,    5,  125,
   17,   16,    8,  118,   16,  120,   28,   28,   23,   72,
   28,  107,   28,   99,   28,   65,   66,   23,   23,   82,
    8,   23,  163,   23,   87,   23,  135,  106,   43,    9,
   10,   88,   87,   11,    8,   12,  187,   13,    8,  118,
   17,    8,   43,    9,   10,   10,   70,    8,  126,   12,
  215,   13,   71,  103,  215,   17,    9,   10,   17,    7,
    8,  250,   12,   71,   13,   11,  197,    8,   43,    9,
   10,    8,  119,   11,  120,   12,  257,   13,  127,   57,
    9,   10,    7,    8,   11,  236,   12,  174,   13,  139,
  137,  138,    9,   10,   37,    8,   11,  161,   12,  172,
   13,   38,    8,   43,    9,   10,    8,   46,   11,  119,
   12,  120,   13,  173,   46,    9,   10,   12,   12,   11,
  108,   12,  132,   13,  126,  171,  179,   12,   12,  226,
   46,   12,    8,   12,  166,   12,    8,   43,  207,  157,
  158,    9,   10,    8,  225,   11,  260,   12,  117,   13,
    8,   43,    9,   10,    8,  116,   48,   45,   12,   46,
   13,   47,  185,    9,   10,    0,    8,  118,    8,   12,
    0,   13,    0,    0,  192,    0,    0,    9,   10,    0,
    0,    0,    8,   12,    0,   13,    8,  126,    0,    0,
    0,    9,   10,    0,  196,    9,   10,   12,    0,   13,
    0,   12,    0,   13,   99,   99,   99,   99,   99,   50,
   99,  127,    0,   81,   75,    0,   75,   75,   75,    0,
    0,    0,   99,   99,   99,   99,   79,    0,   79,   79,
   79,    0,   75,   75,   75,   75,   78,    0,   78,   78,
   78,    0,  111,    0,   79,   79,   79,   79,   76,   50,
   76,   76,   76,    0,   78,   78,   78,   78,   49,    0,
   48,   45,    0,   46,    0,   47,   76,   76,   76,   76,
   61,  126,   48,   45,    0,   46,   80,   47,   48,   45,
  170,   46,    0,   47,  200,    0,    8,  118,  176,  133,
  134,  135,  136,  131,   48,   45,    0,   46,  178,   47,
  169,   48,   45,    0,   46,    0,   47,   48,   45,   73,
   46,  182,   47,    0,    0,    0,    0,    0,  199,    8,
   43,    0,    0,    8,  118,  237,    8,   43,    0,    0,
    0,    0,    0,  107,    0,    0,    0,    0,    0,    0,
  203,  224,  230,   43,    0,    0,  230,    0,    0,    0,
    0,    0,    0,    0,  238,    0,    0,    0,    0,    0,
   73,    0,    0,    0,    0,    0,    0,  214,    0,  107,
    0,    8,   43,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  221,    0,  227,    0,    0,
    0,    0,    0,  111,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  107,    0,    0,  189,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   99,
   99,   99,    0,   99,   99,   99,   99,   99,   99,   75,
   75,   75,  253,   75,   75,   75,   75,    0,   75,    0,
    0,   79,   79,   79,    0,   79,   79,   79,   79,    0,
   79,   78,   78,   78,    0,   78,   78,   78,   78,    0,
   78,  189,    0,   76,   76,   76,    0,   76,   76,   76,
   76,    0,   76,    0,    0,    8,   43,   44,    0,    0,
  189,    0,    0,    0,    0,    0,   60,    8,   43,    0,
    0,    0,   79,    8,   43,    0,    0,    0,    0,    0,
    0,   73,    0,    0,    0,    0,    0,    0,    0,    8,
   43,    0,    0,    0,    0,    0,    8,   43,    0,    0,
    0,    0,    8,   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,    0,   41,   45,    2,   44,   41,   59,   20,   41,
   59,    0,   59,   11,   12,   44,   14,   41,   16,  109,
   44,   40,   20,   40,    2,   42,   43,   40,   45,   63,
   47,   44,    0,   46,   41,  151,   14,   61,   36,   41,
   40,  257,  256,  123,   72,  270,   59,  257,   43,   45,
   45,   40,   50,  125,  270,   59,   41,  271,    2,   40,
  270,   40,  123,   40,   62,   63,   41,   46,   12,   44,
   14,   44,   41,   85,   72,   45,   20,   40,  106,   44,
  256,  257,   41,   81,   43,   41,   45,   85,   61,   45,
   40,   89,   36,  183,  210,   93,   61,  187,   59,   45,
   98,   44,   42,   40,  123,  257,   50,   47,  106,  258,
  256,  109,   42,  111,   40,   45,   59,   47,   62,   63,
   98,   59,  256,  264,   59,  125,   40,  123,   72,   41,
   41,   59,   44,   44,  256,  125,  125,   81,   40,   59,
   71,   85,   59,   74,  257,   46,  123,   42,  125,   40,
   45,  257,   47,   40,   98,  123,   41,  268,  156,  257,
  123,   40,  106,   41,   44,  109,  164,  111,  180,  257,
  204,   59,  170,  123,  125,   40,  125,   41,  176,  256,
  256,   41,  180,   59,   45,  183,   40,   44,  125,  257,
  123,   59,   44,   40,  256,   42,   43,   40,   45,  125,
   47,   41,   59,  256,  256,  203,  204,  256,  123,  256,
    0,   40,   43,  211,   45,  257,  258,  269,  257,  256,
  164,    0,  257,  125,  256,   40,  170,  256,  270,   40,
  256,  270,  176,  257,  125,  270,  180,  256,  257,  183,
  257,  258,  271,  256,  257,  256,  244,  266,  267,  256,
  262,  270,  256,  272,  256,  274,  273,  257,  257,  203,
  204,  259,  257,  258,  262,  269,  266,  267,  257,  123,
  270,  256,  272,  264,  274,  256,  257,  266,  267,  256,
  257,  270,  125,  272,  257,  274,   41,  256,  258,  266,
  267,  264,  257,  270,  257,  272,  125,  274,  257,  258,
  244,  257,  258,  266,  267,    0,  256,  257,  256,  272,
  125,  274,  258,  256,  125,  259,  266,  267,  262,  256,
  257,  256,  272,  273,  274,    0,  256,  257,  258,  266,
  267,  257,   43,  270,   45,  272,  256,  274,  256,  256,
  266,  267,  256,  257,  270,  193,  272,  256,  274,   60,
   61,   62,  266,  267,    4,  257,  270,  256,  272,  256,
  274,    4,  257,  258,  266,  267,  257,   45,  270,   43,
  272,   45,  274,  256,   45,  266,  267,  256,  257,  270,
   36,  272,   61,  274,   51,   59,   93,  266,  267,   44,
   45,  270,  257,  272,   74,  274,  257,  258,  148,  256,
  257,  266,  267,  257,   59,  270,  243,  272,   49,  274,
  257,  258,  266,  267,  257,   41,   42,   43,  272,   45,
  274,   47,  108,  266,  267,   -1,  257,  258,  257,  272,
   -1,  274,   -1,   -1,  265,   -1,   -1,  266,  267,   -1,
   -1,   -1,  257,  272,   -1,  274,  257,  114,   -1,   -1,
   -1,  266,  267,   -1,  121,  266,  267,  272,   -1,  274,
   -1,  272,   -1,  274,   41,   42,   43,   44,   45,    9,
   47,   53,   -1,   13,   41,   -1,   43,   44,   45,   -1,
   -1,   -1,   59,   60,   61,   62,   41,   -1,   43,   44,
   45,   -1,   59,   60,   61,   62,   41,   -1,   43,   44,
   45,   -1,   42,   -1,   59,   60,   61,   62,   41,   49,
   43,   44,   45,   -1,   59,   60,   61,   62,   40,   -1,
   42,   43,   -1,   45,   -1,   47,   59,   60,   61,   62,
   40,  198,   42,   43,   -1,   45,   40,   47,   42,   43,
   80,   45,   -1,   47,  126,   -1,  257,  258,   88,  260,
  261,  262,  263,   41,   42,   43,   -1,   45,   92,   47,
   41,   42,   43,   -1,   45,   -1,   47,   42,   43,   12,
   45,  105,   47,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,   -1,  257,  258,  256,  257,  258,   -1,   -1,
   -1,   -1,   -1,   36,   -1,   -1,   -1,   -1,   -1,   -1,
  140,  256,  184,  258,   -1,   -1,  188,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  196,   -1,   -1,   -1,   -1,   -1,
   63,   -1,   -1,   -1,   -1,   -1,   -1,  161,   -1,   72,
   -1,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  179,   -1,  181,   -1,   -1,
   -1,   -1,   -1,  193,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  106,   -1,   -1,  109,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,  264,  265,  256,
  257,  258,  226,  260,  261,  262,  263,   -1,  265,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  164,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,   -1,  257,  258,  259,   -1,   -1,
  183,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
   -1,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,  204,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,
   -1,   -1,  257,  258,
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
"sentencia_ejecutable : asignacion_simple ';'",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"sentencia_ejecutable : invocacion_funcion error",
"sentencia_ejecutable : asignacion_simple error",
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
"asignacion_simple : variable DASIG expresion",
"asignacion_simple_recuperacion : variable DASIG expresion error",
"asignacion_simple_recuperacion : variable expresion",
"asignacion_simple_recuperacion : variable error expresion",
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

//#line 858 "gramatica.y"

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
//#line 753 "Parser.java"
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
//#line 185 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 203 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 37:
//#line 205 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 43:
//#line 212 "gramatica.y"
{ 
            notifyError("La invocación a función debe terminar con ';'."); 
            notifyDetection("Invocación de función.");
        }
break;
case 46:
//#line 231 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 234 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 239 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 243 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 247 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 251 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 261 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 266 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 273 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 287 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 292 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 295 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 298 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 323 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 325 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 330 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 332 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 69:
//#line 368 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 71:
//#line 353 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 361 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 75:
//#line 369 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 374 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 78:
//#line 388 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 79:
//#line 393 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de %s %s.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 80:
//#line 397 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 81:
//#line 404 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 82:
//#line 414 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 83:
//#line 416 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 84:
//#line 423 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 86:
//#line 429 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 436 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 88:
//#line 443 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 90:
//#line 449 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 91:
//#line 456 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 458 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 100:
//#line 485 "gramatica.y"
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
case 102:
//#line 504 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 103:
//#line 513 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 104:
//#line 518 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 105:
//#line 521 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 106:
//#line 524 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 107:
//#line 527 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 109:
//#line 539 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 116:
//#line 555 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 117:
//#line 564 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 118:
//#line 569 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 119:
//#line 571 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 120:
//#line 573 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 121:
//#line 575 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 124:
//#line 591 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 125:
//#line 596 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 598 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 615 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 617 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 636 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 648 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 665 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 677 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 695 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 697 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 709 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 718 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 723 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 725 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 727 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 729 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 738 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 748 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 755 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 760 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 769 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 774 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 785 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 788 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 790 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 813 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 818 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 171:
//#line 820 "gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 172:
//#line 822 "gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 173:
//#line 824 "gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 175:
//#line 835 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 176:
//#line 838 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1350 "Parser.java"
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
