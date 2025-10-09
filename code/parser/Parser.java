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
   40,   40,   33,   33,   33,   33,   41,   41,   41,   42,
   42,   42,   42,   43,   43,    9,    9,    9,    1,    1,
    1,    1,    6,    6,    2,    2,    2,    4,    4,    4,
    7,    7,    3,    3,    3,    5,    5,    5,   11,   11,
   10,   10,   44,   44,   44,   44,   44,   45,   45,   46,
   46,   46,   46,   46,   46,   46,   38,   38,   38,   38,
   38,   47,   47,   39,   39,   39,   48,   49,   49,   49,
   50,   27,   27,   53,   53,   52,   51,   51,   54,   54,
   54,   56,   56,   55,   55,   55,   57,   57,   57,   35,
   35,   35,   35,   35,   12,   13,   13,   14,   14,   36,
   36,   59,   59,   59,   59,   58,   60,   60,   37,   37,
   37,   37,   37,   63,   63,   63,   62,   61,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    2,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    2,
    5,    3,    3,    2,    2,    4,    3,    4,    3,    2,
    4,    4,    2,    4,    2,    4,    3,    6,    3,    5,
    1,    4,    5,    4,    4,    1,    3,    2,    1,    3,
    3,    2,    1,    1,    3,    1,    3,    3,    1,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,
    1,    3,    3,    2,    2,    2,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    6,    6,    5,    5,
    2,    0,    2,    3,    3,    2,    2,    1,    1,    2,
    2,    6,    5,    1,    2,    3,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    5,
    5,    4,    3,    2,    4,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    0,    3,    1,    1,    4,    4,
    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   21,   24,   25,   26,    0,   37,   38,   39,   40,
   41,   42,   44,   45,    0,    0,    8,    9,    5,   23,
    0,    0,   99,  167,    0,    0,    0,    0,   86,   93,
   94,   95,    0,    0,    0,  121,    0,    0,    0,    0,
   50,    0,    0,    0,    0,  126,    0,    0,   30,    0,
   31,    0,    0,    0,  154,    0,    0,    0,   17,   14,
    0,    0,    0,    0,    0,   71,    0,    0,    0,    0,
   43,   36,   22,   18,    0,   29,   27,   65,   63,    0,
    0,   34,    0,    0,  102,    0,    0,  156,  100,    0,
    0,   97,   83,   84,    0,   89,    0,   96,   98,   91,
   92,    0,  160,  161,  105,    0,  112,  114,  113,  115,
  116,  110,  111,    0,    0,  106,  104,   48,   55,   47,
    0,    0,  149,  148,    0,    0,    0,  139,  141,    0,
   49,   54,   46,    0,    0,  131,   33,    0,    0,  127,
  124,  125,    0,    0,  153,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,   59,    0,    0,    0,
   67,   69,    0,    0,   76,    0,    0,    0,    0,    0,
    0,   35,    0,    0,    0,    0,  155,  166,    0,   81,
    0,   87,   85,  107,  103,    0,    0,    0,   52,    0,
  143,    0,    0,    0,  146,   53,    0,   32,  152,    0,
   19,   20,  178,   58,   56,    0,    0,   60,    0,    0,
    0,    0,    0,   13,   66,   64,    0,   78,    0,    0,
    0,  169,  170,    0,  172,  158,  157,   90,   88,  123,
  120,    0,  119,    0,    0,  134,  133,  140,  144,   51,
  151,  150,    0,    0,    0,    0,    0,    0,   74,   77,
  171,  177,  173,  118,  117,    0,  132,  135,    0,   61,
   62,   70,   73,   75,   68,  136,
};
final static short yydgoto[] = {                          3,
   58,   48,   49,  115,  116,  117,  122,   64,  184,   50,
   51,   52,  107,  108,   18,   19,  279,   39,    4,  168,
   21,   95,   22,   23,   24,   25,   26,   97,   70,   71,
  104,   27,   28,   29,   30,   31,   32,   33,   34,  174,
   35,   89,  180,   59,   60,  134,  208,   72,   73,   74,
  146,  256,  257,  147,  148,  149,  150,   53,   54,   55,
   36,  193,  189,
};
final static short yysindex[] = {                      -166,
    2,  -39,    0,  -83,    0,    0,  -75,   61,   27,   96,
  -13,  -27,  149,   42, -211,  258,  -30,    0,    0,  130,
  -62,    0,    0,    0,    0,   11,    0,    0,    0,    0,
    0,    0,    0,    0,   44,  142,    0,    0,    0,    0,
 -141,  126,    0,    0, -136,  272,  326,   71,    0,    0,
    0,    0,   68, -123,    0,    0,   10,  280,  146,  -37,
    0,   14,   -5,  301, -129,    0,    3,  158,    0,   -9,
    0,   90,  -98,    0,    0,  165,  305,   38,    0,    0,
   65,  -96,  126,  126,  -93,    0,  468,  116,   24,   24,
    0,    0,    0,    0,   77,    0,    0,    0,    0,   24,
  158,    0,  133,  162,    0,  252,    8,    0,    0,    0,
  131,    0,    0,    0,   71,    0,  236,    0,    0,    0,
    0,  320,    0,    0,    0,  -18,    0,    0,    0,    0,
    0,    0,    0,  126,  -90,    0,    0,    0,    0,    0,
  -77,   -5,    0,    0, -224,  147,  139,    0,    0, -189,
    0,    0,    0,  -70,   24,    0,    0,  176,    0,    0,
    0,    0,  136,   33,    0,    0,   67,   78,  155,  489,
  501,   71,  -49,    0,  167,  116,    0,  116,   24,   24,
    0,    0,   89,   80,    0,  180,  317,  152,  -48,  133,
  -34,    0,    0,   19,   50,  126,    0,    0,  -57,    0,
   71,    0,    0,    0,    0,  326,  146,  132,    0,  327,
    0,  256, -224,  -93,    0,    0,  322,    0,    0,   62,
    0,    0,    0,    0,    0,  236,  320,    0,   24,   24,
   24,  348,  351,    0,    0,    0,   24,    0,  154,    0,
  370,    0,    0,  161,    0,    0,    0,    0,    0,    0,
    0,  134,    0,  298,   54,    0,    0,    0,    0,    0,
    0,    0,   73,  170,  383,  387,  391,   24,    0,    0,
    0,    0,    0,    0,    0,   77,    0,    0,  100,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        20,
    0,  438,    0,  438,    0,    0,    0,  414,  183,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  440,
  119,    0,    0,    0,    0,    6,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -4,  424,    0,    0,
    0,    0,  -98,    0,  185,    0,    0,   48,    0,    0,
    0,  181,  -15,    0,    0,    0,    0,    0,    0,    0,
    0,  188,    0,  193,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -26,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  195,  195,    0,   93,    0,    0,    0,  204,
    0,    0,    0,    0,  436,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  197,    0,    0,    0,    0,    0,
    0,  -15,    0,    0, -162,    0,  421,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  207,    0,
    0,    0,    0,    0,    0,   22,  470,  471,    0,    0,
    0,  506,  335,    0,   36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  195,    0,  216,    0,  195,
    0,    0,  216,    0,    0,    0,    0,    0,    0,    0,
  446,    0,    0,    0,    0,   59,    0,    0,    0,    0,
    0,    0,  -16,  101,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  222,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  509,  476,  140,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  585,  -76,  380,    0,  283,  328,  -80,    0,    0,   -2,
  559,   51,    0,  292,  496,  497,   28,    0,    0,    0,
    0,    0,  -17,  554,    0,    0,    0,    0,  -54,  467,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -64,   -1,   84,  447,    0,    0,    0,    0,  442,
  378,  270,    0,    0, -107,    0,    0,    0,    0,  479,
    0,  423,  -60,
};
final static int YYTABLESIZE=827;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   15,    6,   93,  137,  135,   28,  240,  172,   65,   16,
   45,   16,   15,   88,   90,   93,   93,   16,   93,    4,
   93,   16,  205,  179,  142,  138,   63,  142,   92,   20,
   57,  143,   93,   16,  199,   45,  168,  211,  145,   14,
  201,   81,   57,  194,  118,   28,  144,   45,  197,   40,
  125,  196,   17,  142,   45,  118,   16,  141,   82,   41,
   94,   23,   17,   93,   17,   16,   46,  175,   45,   96,
   17,   45,  140,  220,  118,  113,  101,  114,   16,  101,
  214,   15,  176,   14,  118,  178,   17,  100,  109,    1,
    2,  227,   16,   15,  147,   68,  101,  119,   16,  108,
   42,   16,   99,  118,   15,  258,   41,  147,  119,   17,
  158,  229,  121,  231,  121,  105,   15,  120,   17,  120,
  262,  109,  183,  237,   45,  239,  123,  119,   15,  244,
   28,   17,  124,  159,  155,   57,  159,  119,  236,   15,
   45,  145,    4,  186,  145,   17,   23,  215,  161,  263,
  156,   17,  250,  159,   17,   16,  119,  162,   12,   85,
  169,  118,  166,  175,   79,   93,   80,  118,  118,   15,
   45,  198,  187,  178,  230,  178,   86,  207,  278,  209,
   16,   15,  213,   16,   72,   15,  216,  212,   76,  167,
  253,  221,  275,   45,  219,  223,  118,   15,  248,    8,
  112,  191,  222,  118,   16,  163,  228,  243,   17,   45,
  242,  259,   41,  234,  119,   15,    7,    8,  136,  191,
  119,  119,    8,   43,  286,   91,    9,   10,   66,    8,
   11,   93,   12,   17,   13,   82,   17,  204,    9,   10,
  147,  147,   61,   62,   12,   67,   13,    8,   43,  119,
  143,  168,   16,  147,  147,  122,  119,   17,    5,    8,
   43,   93,   28,   67,  101,  144,    8,   43,   68,  138,
  139,   28,   28,   16,  245,   28,   16,   28,   23,   28,
   45,   43,  157,    8,   43,   44,  190,   23,   23,    8,
  112,   23,  101,   23,  113,   23,  114,   78,    8,   98,
  218,   85,   45,  109,  218,   17,  246,    9,   10,    7,
    8,   11,  110,   12,  108,   13,   45,  261,   86,    9,
   10,    8,  113,   11,  114,   12,   17,   13,  280,   17,
    9,   10,    7,    8,   11,  235,   12,   43,   13,  133,
  131,  132,    9,   10,  154,    8,   11,  113,   12,  114,
   13,   56,    8,   43,    9,   10,    8,  240,   11,  153,
   12,   45,   13,  165,   45,    9,   10,  254,  113,   11,
  114,   12,  175,   13,   12,   12,   86,   86,  255,   86,
  260,   86,    8,   43,   12,   12,    8,  251,   12,  274,
   12,  268,   12,   86,  269,    9,   10,   72,    8,   11,
  252,   12,    8,   13,   75,    8,   43,    9,   10,  271,
  272,    9,   10,   12,    8,   13,  273,   12,    8,   13,
  276,    8,   43,    9,   10,  281,  282,    9,   10,   12,
  283,   13,    8,   12,  284,   13,    8,   15,  165,    3,
  164,    9,   10,  128,  101,    9,   10,   12,  129,   13,
  176,   12,  122,   13,  101,  101,  101,  101,  101,  163,
  101,  137,  130,  173,   79,  122,   79,   79,   79,   10,
   11,  174,  101,  101,  101,  101,   82,  175,   82,   82,
   82,  249,   79,   79,   79,   79,   80,  247,   80,   80,
   80,  200,    8,   43,   82,   82,   82,   82,  226,   37,
   38,  203,  103,  126,   80,   80,   80,   80,    8,  112,
  113,  160,  114,   83,    8,   43,  195,   85,   85,  210,
   85,   84,   85,  277,  111,  188,  177,    0,    8,   43,
   44,  113,    0,  114,   85,    0,    8,  112,    0,  127,
  128,  129,  130,  113,    0,  114,    0,  224,   79,    0,
   79,   80,    0,   80,    0,    0,  151,  152,    0,  225,
    0,    8,  112,    0,   79,   69,  241,   80,    0,    0,
  241,    0,    0,    8,   43,  202,    8,   43,    0,    0,
    0,    0,    8,  112,    0,    0,    0,    0,    0,  102,
    0,   86,   86,   47,    0,    0,    0,   77,    0,    0,
   87,    0,    0,    0,    0,    0,  264,    0,    0,    0,
    0,    0,   69,    0,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,    0,    0,  106,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  181,  182,    0,
    0,    0,    0,    0,  102,    0,    0,  192,  185,    0,
  164,    0,    0,    0,    0,    0,    0,  170,  171,  101,
  101,  101,    0,  101,  101,  101,  101,  101,  101,   79,
   79,   79,    0,   79,   79,   79,   79,    0,   79,    0,
    0,   82,   82,   82,    0,   82,   82,   82,   82,    0,
   82,   80,   80,   80,    0,   80,   80,   80,   80,    0,
   80,  192,    0,  217,    0,    0,    0,    0,  206,    0,
    0,    0,    0,    0,    8,  112,    0,    0,    0,    0,
    0,    0,   85,   85,    0,    0,    0,  232,  233,  192,
    0,    0,  238,    0,    0,    8,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    8,  112,    0,
   69,    0,   79,   79,    0,   80,   80,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  106,    0,    0,    0,    0,    0,    0,  265,  266,  267,
    0,    0,    0,    0,    0,  270,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  285,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
   40,    0,   20,   41,   59,    0,   41,   84,   11,   12,
   45,   14,   40,   16,   16,   42,   43,   20,   45,    0,
   47,    0,   41,   88,   41,   41,   40,   44,   59,    2,
   40,  256,   59,   36,  115,   45,   41,  145,   44,  123,
  117,   14,   40,  104,   47,   40,  271,   45,   41,  125,
   41,   44,    2,   40,   45,   58,   59,   44,  270,   46,
  123,   40,   12,   81,   14,   68,   40,  257,   45,   59,
   20,   45,   59,   41,   77,   43,   41,   45,   81,   44,
  270,   40,   85,  123,   87,   88,   36,   44,   41,  256,
  257,  172,   95,   40,  257,  123,   61,   47,  101,   41,
   40,  104,   59,  106,   40,  213,   46,  270,   58,   59,
   68,  176,   42,  178,   42,  257,   40,   47,   68,   47,
   59,  258,   95,   44,   45,  186,   59,   77,   40,  190,
  125,   81,  256,   41,  264,   40,   44,   87,   59,   40,
   45,   41,  123,  101,   44,   95,  125,  150,   59,  226,
   67,  101,  207,   70,  104,  158,  106,  256,   40,   44,
  257,  164,  125,  257,  123,  183,  125,  170,  171,   40,
   45,   41,   40,  176,  176,  178,   61,  268,  125,  257,
  183,   40,   44,  186,   45,   40,  257,   41,   40,  125,
   59,  125,   59,   45,   59,   41,  199,   40,  256,  257,
  258,   40,  125,  206,  207,   41,  256,  256,  158,   45,
   59,  214,   46,  125,  164,   40,  256,  257,  256,   40,
  170,  171,  257,  258,  125,  256,  266,  267,  256,  257,
  270,  258,  272,  183,  274,  270,  186,  256,  266,  267,
  257,  257,  256,  257,  272,  273,  274,  257,  258,  199,
  256,  256,  255,  270,  270,   59,  206,  207,  257,  257,
  258,  279,  257,  273,  123,  271,  257,  258,  123,  256,
  257,  266,  267,  276,  256,  270,  279,  272,  257,  274,
   45,  258,  125,  257,  258,  259,  125,  266,  267,  257,
  258,  270,  257,  272,   43,  274,   45,  256,  257,  256,
  125,   44,   45,  256,  125,  255,  257,  266,  267,  256,
  257,  270,   41,  272,  256,  274,   45,  256,   61,  266,
  267,  257,   43,  270,   45,  272,  276,  274,  256,  279,
  266,  267,  256,  257,  270,  256,  272,  258,  274,   60,
   61,   62,  266,  267,   44,  257,  270,   43,  272,   45,
  274,  256,  257,  258,  266,  267,  257,   41,  270,   59,
  272,   45,  274,   59,   45,  266,  267,   41,   43,  270,
   45,  272,  257,  274,  256,  257,   42,   43,  123,   45,
   59,   47,  257,  258,  266,  267,  257,  256,  270,  256,
  272,   44,  274,   59,   44,  266,  267,  258,  257,  270,
  269,  272,  257,  274,  256,  257,  258,  266,  267,  256,
   41,  266,  267,  272,  257,  274,  256,  272,  257,  274,
  123,  257,  258,  266,  267,  256,   44,  266,  267,  272,
   44,  274,  257,  272,   44,  274,  257,    0,  256,    0,
  256,  266,  267,  256,  264,  266,  267,  272,  256,  274,
  256,  272,  256,  274,   41,   42,   43,   44,   45,  256,
   47,   41,  256,   84,   41,  269,   43,   44,   45,    0,
    0,  256,   59,   60,   61,   62,   41,  256,   43,   44,
   45,  199,   59,   60,   61,   62,   41,  196,   43,   44,
   45,  256,  257,  258,   59,   60,   61,   62,  171,    4,
    4,  122,   36,   57,   59,   60,   61,   62,  257,  258,
   43,   70,   45,  256,  257,  258,  265,   42,   43,  142,
   45,  264,   47,  254,   46,  103,   59,   -1,  257,  258,
  259,   43,   -1,   45,   59,   -1,  257,  258,   -1,  260,
  261,  262,  263,   43,   -1,   45,   -1,   59,   43,   -1,
   45,   43,   -1,   45,   -1,   -1,  256,  257,   -1,   59,
   -1,  257,  258,   -1,   59,   12,  187,   59,   -1,   -1,
  191,   -1,   -1,  257,  258,  256,  257,  258,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   36,
   -1,  257,  258,    9,   -1,   -1,   -1,   13,   -1,   -1,
   16,   -1,   -1,   -1,   -1,   -1,  227,   -1,   -1,   -1,
   -1,   -1,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   68,   -1,   -1,   -1,   -1,   42,   -1,   -1,   -1,
   46,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   89,   90,   -1,
   -1,   -1,   -1,   -1,  101,   -1,   -1,  104,  100,   -1,
   76,   -1,   -1,   -1,   -1,   -1,   -1,   83,   84,  256,
  257,  258,   -1,  260,  261,  262,  263,  264,  265,  256,
  257,  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  158,   -1,  155,   -1,   -1,   -1,   -1,  134,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,  179,  180,  186,
   -1,   -1,  184,   -1,   -1,  257,  258,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
  207,   -1,  257,  258,   -1,  257,  258,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  196,   -1,   -1,   -1,   -1,   -1,   -1,  229,  230,  231,
   -1,   -1,   -1,   -1,   -1,  237,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  268,
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
"asignacion_simple : variable DASIG asignable",
"asignacion_simple : variable error expresion ';'",
"asignacion_simple : variable expresion ';'",
"asignable : factor error",
"asignable : expresion operador_suma termino error",
"asignable : termino operador_multiplicacion factor error",
"asignacion_multiple : inicio_par_variable_constante ';'",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes ';'",
"asignacion_multiple : inicio_par_variable_constante error",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes error",
"inicio_par_variable_constante : variable par_variable_constante constante",
"inicio_par_variable_constante : variable variable par_variable_constante constante ',' constante",
"inicio_par_variable_constante : variable secuencia_variable_sin_coma constante",
"par_variable_constante : ',' variable par_variable_constante constante ','",
"par_variable_constante : '='",
"par_variable_constante : ',' variable par_variable_constante constante",
"par_variable_constante : ',' variable secuencia_variable_sin_coma constante ','",
"secuencia_variable_sin_coma : variable secuencia_variable_sin_coma constante ','",
"secuencia_variable_sin_coma : variable par_variable_constante constante ','",
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

//#line 872 "gramatica.y"

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
//#line 769 "Parser.java"
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
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 12:
//#line 110 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
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
case 27:
//#line 160 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 33:
//#line 187 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 201 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 212 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 228 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 231 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 48:
//#line 236 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 240 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 244 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 248 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 53:
//#line 258 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 263 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 55:
//#line 270 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 284 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 289 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 292 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 295 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 63:
//#line 320 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 322 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 327 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 329 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 68:
//#line 340 "gramatica.y"
{ notifyError("Falta coma en la lista de variables de la asignación múltiple."); }
break;
case 69:
//#line 342 "gramatica.y"
{ notifyError("Falta coma en la lista de variables de la asignación múltiple."); }
break;
case 72:
//#line 354 "gramatica.y"
{ notifyError("Falta coma en la lista de constantes de la asignación múltiple."); }
break;
case 73:
//#line 356 "gramatica.y"
{ notifyError("Falta coma en la lista de variables de la asignación múltiple."); }
break;
case 77:
//#line 383 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 78:
//#line 388 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            /* $$ = $1 + '_' + $2;*/
        }
break;
case 80:
//#line 403 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 81:
//#line 408 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 82:
//#line 415 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
            yyval.sval = val_peek(0).sval;
        }
break;
case 83:
//#line 428 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 84:
//#line 430 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 85:
//#line 437 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 87:
//#line 443 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 88:
//#line 455 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 90:
//#line 461 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de %s %s.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 91:
//#line 473 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 475 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 100:
//#line 501 "gramatica.y"
{ 
            yyval.sval = "-" + val_peek(0).sval;

            if(isUint(yyval.sval)) {
                notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
                yyval.sval = "0";
            } 

            modificarSymbolTable(yyval.sval,val_peek(0).sval);
        }
break;
case 102:
//#line 518 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 103:
//#line 527 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 104:
//#line 532 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 105:
//#line 535 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 106:
//#line 538 "gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 107:
//#line 541 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 109:
//#line 553 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 116:
//#line 569 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 117:
//#line 578 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 118:
//#line 583 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 119:
//#line 585 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 120:
//#line 587 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 121:
//#line 589 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 124:
//#line 605 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 125:
//#line 610 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 612 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 629 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 631 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 650 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 662 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 679 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 691 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 709 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 711 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 723 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 732 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 737 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 739 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 741 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 743 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 752 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 762 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 769 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 774 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 783 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 788 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 799 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 802 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 804 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 827 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 832 "gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 171:
//#line 834 "gramatica.y"
{ notifyDetection("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 172:
//#line 836 "gramatica.y"
{ notifyDetection("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 173:
//#line 838 "gramatica.y"
{ notifyDetection("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 175:
//#line 849 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 176:
//#line 852 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1357 "Parser.java"
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
