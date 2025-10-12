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
   44,   44,   44,   44,   46,   46,   45,   45,   45,   45,
   45,   45,   45,   39,   39,   39,   39,   39,   47,   47,
   47,   40,   40,   40,   40,   40,   48,   49,   49,   50,
   28,   28,   28,   53,   53,   52,   51,   51,   54,   54,
   54,   56,   56,   55,   55,   55,   57,   57,   57,   36,
   36,   36,   36,   36,   12,   13,   13,   14,   14,   37,
   37,   37,   37,   58,   59,   59,   59,   60,   60,   38,
   38,   38,   38,   38,   38,   38,   38,   38,   38,   62,
   63,   63,   61,
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
    5,    2,    2,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    6,    6,    5,    5,    2,    0,    2,
    1,    3,    3,    3,    3,    2,    2,    1,    2,    2,
    6,    5,    7,    1,    2,    3,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    5,
    5,    4,    3,    2,    4,    1,    3,    3,    1,    3,
    3,    3,    3,    3,    2,    1,    0,    1,    1,    4,
    4,    4,    4,    5,    4,    5,    5,    4,    5,    3,
    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    9,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    2,
    0,    0,   23,   26,   27,   28,    0,   39,   40,   41,
   42,   43,   44,   46,   47,    0,   10,   11,    5,    0,
   25,    0,    0,   97,  168,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,  166,
  118,    0,    0,    0,    0,   53,    0,    0,    0,    0,
  126,    0,    0,   32,    0,   33,    0,    0,  128,  154,
    0,    0,    0,   19,   16,    0,    0,    0,    0,   67,
    0,    0,    0,    0,   45,   38,   24,   20,    0,   31,
   29,    0,   36,    0,    0,    0,    7,  100,    0,    0,
  156,    0,   98,  165,    0,   95,   80,   81,    0,   87,
    0,   94,   96,    0,   85,   91,  161,  160,  163,  162,
  102,    0,    0,  109,  111,  110,  112,  113,  107,  108,
    0,    0,    0,    0,  103,   50,   57,   49,    0,    0,
  149,  148,    0,    0,    0,  139,  141,    0,   51,   56,
   48,    0,    0,  130,   35,    0,  129,  127,  124,  122,
  125,  123,    0,    0,  153,    0,    0,    0,    0,   61,
    0,    0,   68,   60,    0,   69,    0,    0,    0,    0,
    0,    0,    0,    0,   37,    0,    0,    0,    0,    0,
  155,  164,    0,   77,    0,   84,   82,    0,  104,    0,
  120,    0,    0,   54,    0,  143,    0,    0,    0,  146,
   55,    0,   34,  152,    0,   21,   22,  183,   59,   58,
   64,   62,    0,    0,   66,   15,    0,    0,  181,    0,
  171,  170,  173,  172,    0,    0,  175,  178,   12,  158,
  157,   88,   86,    0,    0,  116,  117,    0,    0,  134,
  132,  140,  144,   52,  151,  150,    0,   72,   70,  174,
  177,  180,  176,  179,  101,  115,  114,    0,  131,  135,
    0,   65,   63,    0,   74,  133,  136,   73,
};
final static short yydgoto[] = {                          4,
   63,   52,   53,  119,  120,  121,   54,   69,  267,   17,
   56,   57,  110,  111,   19,   20,  281,   39,    5,    7,
  178,   22,   99,   23,   24,   25,   26,   27,  101,   75,
   76,  105,   28,   29,   30,   31,   32,   33,   34,   35,
   93,   94,  235,   64,  141,   65,  144,   77,   78,   79,
  154,  260,  261,  155,  156,  157,  158,   58,   59,   60,
   36,  191,  192,
};
final static short yysindex[] = {                         1,
   47,    0,   37,    0,  -58,    0, -185,  -43,    3,  163,
  556,  107,   67,  562,  -36, -173,  108,  -45,    0,    0,
  242,  -13,    0,    0,    0,    0,   56,    0,    0,    0,
    0,    0,    0,    0,    0,  121,    0,    0,    0,   -7,
    0, -137,   48,    0,    0,  429, -136,    0,    0,  569,
  119,   20,    0,  228, -130,    0,    0,  -42,   58,    0,
    0,  146,  422,  117,   90,    0,   65,  -29,  -39, -132,
    0,   33,  256,    0,  -11,    0,   74,  113,    0,    0,
  576,  481,   12,    0,    0,  135, -121,  544, -108,    0,
  516, -130,  -19,    8,    0,    0,    0,    0,  157,    0,
    0,  256,    0,  114,  272,  157,    0,    0,   85,   22,
    0,   20,    0,    0,  125,    0,    0,    0,   20,    0,
   98,    0,    0,  370,    0,    0,    0,    0,    0,    0,
    0,  422,  -98,    0,    0,    0,    0,    0,    0,    0,
   48,  221, -103, -102,    0,    0,    0,    0,  -89,  -29,
    0,    0, -216,  132,  130,    0,    0, -213,    0,    0,
    0,  -80,  -19,    0,    0,  275,    0,    0,    0,    0,
    0,    0,  120,   80,    0,    0,   51,   57,  140,    0,
  472,  138,    0,    0,  -37,    0,  -19,  173,  291,  153,
  124,  142,  114,   -4,    0,  -64,  -60,  192,  -57,   48,
    0,    0,  373,    0,   20,    0,    0,   48,    0,  119,
    0,  -40,  156,    0,  158,    0,   89, -216, -108,    0,
    0,  164,    0,    0,  143,    0,    0,    0,    0,    0,
    0,    0,  -19,  172,    0,    0,  -31,  -23,    0,  245,
    0,    0,    0,    0,   46,   62,    0,    0,    0,    0,
    0,    0,    0,  103,  150,    0,    0,  193,   79,    0,
    0,    0,    0,    0,    0,    0,   41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  101,    0,    0,
  203,    0,    0,  -19,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        11,
   38,    0,  325,    0,  325,    0,    0,    0,  459,  169,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  352,  224,    0,    0,    0,    0,    2,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   15,  392,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  341,  139,    0,    0,  122,  -33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   10,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  289,  134,    0,    0,    0,   40,    0,
    0,  492,    0,    0,    0,    0,    0,    0,  523,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  161,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -24,   39,    0,    0,    0,    0,    0,    0,  -33,
    0,    0, -211,    0,  353,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,  426,  434,    0,    0,
    0,  -16,    0,    0,    0,    0,    0,    0,  134,    0,
    0,    0,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  533,    0,    0,    0,    0,  401,
    0,    0,    0,    0,    0,    0,    0,  -35,   60,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   19,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  188,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  646,   -8,  501,    0,    0,    0,  520,    0,    0,  238,
  477,   -3,    0,  250,   18,  450,   36,    0,    0,    0,
    0,    0,    0,  -18,  705,    0,    0,    0,    0,  -48,
  420,  -52,    0,    0,    0,    0,    0,    0,    0,    0,
  367,    0,    0,   84,  331,  406,  329,    0,    0,  404,
  339,  239,    0,    0, -126,    0,    0,    0,    0,  455,
    0,  -81,  -75,
};
final static int YYTABLESIZE=894;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         18,
    2,   30,   97,   16,  162,  142,  233,  138,  142,   18,
    4,   18,   18,   96,  153,  143,  128,   18,  256,  161,
  166,  232,   37,  196,   99,   47,  216,   99,   62,  197,
   49,   46,   18,   47,  121,   48,  239,  112,   21,  151,
   47,   30,   43,  182,   99,  147,    6,  123,   42,  189,
   86,   89,   25,   69,  152,  169,  219,  107,  147,  123,
   18,   49,  201,   71,   15,  200,   48,   97,   90,   18,
   69,   40,   62,  169,   49,   46,   16,   47,  123,   48,
  159,   41,   18,  159,  284,   47,   84,  123,   85,   49,
   46,  262,   47,  211,   48,   18,   87,  119,   18,  283,
  145,   18,   18,  145,  150,  123,   16,  237,  149,   98,
   42,  245,  205,  238,  100,  106,  130,  246,   16,  108,
  225,  113,  117,  148,  118,  126,   30,  117,  123,  118,
  145,  163,  170,    4,  188,  179,  176,   25,   18,   49,
   16,  198,   47,  275,   48,  117,   68,  118,  182,   49,
   46,   89,   47,  190,   48,  164,   16,  209,  167,   15,
   16,  117,   18,  118,  142,  202,  213,  214,   90,   97,
  123,  172,  217,  218,   16,  226,  221,  123,  224,   97,
  228,  227,  242,   42,   18,   18,  131,   49,   46,   73,
   47,  247,   48,  239,   18,  248,   16,   47,  258,  250,
  244,  266,   50,  280,   49,   46,  123,   47,  277,   48,
   95,  259,   16,  127,  257,  269,  159,  160,  231,   83,
    9,  147,  264,  147,  270,  286,  151,  167,  255,   10,
   11,   16,  271,   12,  147,   13,  147,   14,   44,   73,
   99,  152,   16,  102,  121,    9,   44,   55,   55,   70,
  123,   55,    9,   44,   92,   18,    1,    3,   30,  177,
   16,   72,   97,   14,  182,   87,   69,   30,   30,   25,
  169,   30,   47,   30,   18,   30,   71,   18,   25,   25,
   55,   16,   25,   55,   25,  272,   25,   55,  122,    9,
   44,   55,    8,    9,    6,   16,  282,  236,   44,   55,
  122,  273,   10,   11,    9,   44,   12,  119,   13,   55,
   14,  194,   55,  129,   16,  278,  249,  274,   55,  122,
  146,  147,   71,    9,   17,   55,  183,  287,  122,  169,
  194,  186,   10,   11,    8,    9,    9,  116,   13,   72,
   14,    9,  116,   73,   10,   11,  122,  182,   12,  199,
   13,    3,   14,  204,    9,   44,    8,    9,   55,    9,
  116,   55,   66,   67,    9,   44,   10,   11,  171,  122,
   12,   88,   13,    9,   14,    9,  116,    9,   55,  241,
  165,  106,   10,   11,  142,   99,   10,   11,   13,  182,
   14,    9,   13,  137,   14,  220,  193,  243,  265,  223,
   10,   11,    9,   44,   12,  276,   13,  119,   14,    9,
   44,  122,    8,    9,   47,  223,  106,   47,  122,    9,
   44,   45,   10,   11,  167,   12,   12,   55,   13,    9,
   14,   55,   75,   13,   75,   75,   75,   55,   10,   11,
   55,  105,   12,  105,   13,   55,   14,  122,    9,  251,
   75,   75,   75,   75,   38,  104,  263,   10,   11,    9,
  187,   12,  208,   13,  117,   14,  118,  133,   10,   11,
   49,  212,   12,   47,   13,   48,   14,    9,  168,   14,
   14,  140,  138,  139,    9,   44,   10,   11,  215,   14,
   14,  122,   13,   14,   14,   14,  279,   14,    9,   99,
   99,   99,   99,   99,  115,   99,    0,   10,   11,    0,
    0,   12,    9,   13,  117,   14,  118,   99,   99,   99,
   99,   10,   11,  117,    0,  118,    0,   13,    9,   14,
  230,    9,   79,    0,   79,   79,   79,   10,   11,  175,
   10,   11,    0,   13,  182,   14,   13,    9,   14,    0,
   79,   79,   79,   79,  125,    0,   10,   11,  117,    0,
  118,    0,   13,   78,   14,   78,   78,   78,    0,  185,
    0,  124,    0,   76,  184,   76,   76,   76,    0,    0,
    0,   78,   78,   78,   78,   49,   46,    0,   47,    0,
   48,   76,   76,   76,   76,   62,    0,   49,   46,    0,
   47,   81,   48,   49,   46,    0,   47,    0,   48,  114,
   49,   46,    0,   47,    0,   48,  173,   49,   46,    0,
   47,    0,   48,    0,  207,  206,    9,   44,  252,    9,
   44,  124,    0,    0,    0,    0,    0,    0,  203,  222,
    0,    0,    0,    0,    0,    0,    0,   75,   75,   75,
    0,   75,   75,   75,   75,   51,   75,    0,    0,   82,
    0,    0,   91,  234,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    9,  116,
    0,  134,  135,  136,  137,    9,   44,    0,  109,    0,
  240,    0,    0,    0,  240,   51,    0,    0,    0,    0,
    0,    0,    0,  253,    0,    0,    0,  132,    0,  268,
    0,    0,    0,    0,   99,   99,   99,   74,   99,   99,
   99,   99,   99,   99,  124,    0,  174,  229,    9,  116,
    0,    0,    0,  181,    0,    0,    0,    9,  116,    0,
  103,    0,    0,  285,    0,    0,    0,   79,   79,   79,
    0,   79,   79,   79,   79,    0,   79,    0,    0,    0,
  288,    0,    0,    0,    0,    0,    0,    0,   74,    0,
    0,    0,    9,  116,    0,    0,    0,  103,   78,   78,
   78,    0,   78,   78,   78,   78,  210,   78,   76,   76,
   76,    0,   76,   76,   76,   76,    0,   76,    0,  180,
    9,   44,    0,    0,    0,    0,  103,    0,    0,  195,
    0,   61,    9,   44,    0,    0,    0,   80,    9,   44,
    0,    0,    0,    0,    0,    9,   44,   45,    0,    0,
    0,    0,    9,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  109,   74,    0,    0,    0,
    0,    0,    0,  254,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  195,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  195,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          3,
    0,    0,   21,   40,   44,   41,   44,   41,   44,   13,
    0,   15,    0,   59,   44,   64,   59,   21,   59,   59,
   73,   59,    5,  105,   41,   45,  153,   44,   40,  105,
   42,   43,   36,   45,   59,   47,   41,   46,    3,  256,
   45,   40,   40,  257,   61,  257,    0,   51,   46,  102,
   15,   44,   40,   44,  271,   41,  270,   40,  270,   63,
   64,   42,   41,   45,  123,   44,   47,   86,   61,   73,
   61,  257,   40,   59,   42,   43,   40,   45,   82,   47,
   41,  125,   86,   44,   44,   45,  123,   91,  125,   42,
   43,  218,   45,  142,   47,   99,  270,   59,  102,   59,
   41,  105,  106,   44,   40,  109,   40,  189,   44,  123,
   46,  193,  121,  189,   59,  123,   59,  193,   40,  257,
   41,  258,   43,   59,   45,  256,  125,   43,  132,   45,
   41,  264,   59,  123,   99,  257,  125,  125,  142,   42,
   40,  106,   45,   41,   47,   43,   40,   45,  257,   42,
   43,   44,   45,   40,   47,   72,   40,  256,   75,  123,
   40,   43,  166,   45,  268,   41,  269,  257,   61,  188,
  174,   59,   41,   44,   40,  125,  257,  181,   59,  198,
   41,  125,   59,   46,  188,  189,   41,   42,   43,  123,
   45,  256,   47,   41,  198,  256,   40,   45,   41,  257,
   59,   59,   40,  125,   42,   43,  210,   45,   59,   47,
  256,  123,   40,  256,   59,   44,  256,  257,  256,  256,
  257,  257,   59,  257,  256,  125,  256,   59,  269,  266,
  267,   40,  256,  270,  270,  272,  270,  274,  258,  123,
  257,  271,   40,  123,  269,  257,  258,   10,   11,   12,
  254,   14,  257,  258,   17,  259,  256,  257,  257,  125,
   40,  273,  281,   40,  257,  270,  257,  266,  267,  257,
  256,  270,   45,  272,  278,  274,  258,  281,  266,  267,
   43,   40,  270,   46,  272,   41,  274,   50,   51,  257,
  258,   54,  256,  257,  257,   40,  256,  125,  258,   62,
   63,  256,  266,  267,  257,  258,  270,  269,  272,   72,
  274,   40,   75,  256,   40,  123,  125,  256,   81,   82,
  256,  257,  256,  257,    0,   88,   89,  125,   91,  256,
   40,   94,  266,  267,  256,  257,  257,  258,  272,  273,
  274,  257,  258,  123,  266,  267,  109,   59,  270,  265,
  272,    0,  274,  256,  257,  258,  256,  257,  121,  257,
  258,  124,  256,  257,  257,  258,  266,  267,  256,  132,
  270,  264,  272,  257,  274,  257,  258,  257,  141,  256,
  125,   41,  266,  267,  268,  264,  266,  267,  272,  256,
  274,  257,  272,   41,  274,  158,  125,  256,  256,  125,
  266,  267,  257,  258,  270,  256,  272,  269,  274,  257,
  258,  174,  256,  257,   45,  125,  256,   45,  181,  257,
  258,  259,  266,  267,  256,    0,  270,  190,  272,  257,
  274,  194,   41,    0,   43,   44,   45,  200,  266,  267,
  203,   41,  270,  256,  272,  208,  274,  210,  257,  200,
   59,   60,   61,   62,    5,   36,  219,  266,  267,  257,
   94,  270,  132,  272,   43,  274,   45,   62,  266,  267,
   42,  143,  270,   45,  272,   47,  274,  257,   75,  256,
  257,   60,   61,   62,  257,  258,  266,  267,  150,  266,
  267,  254,  272,  270,  274,  272,  258,  274,  257,   41,
   42,   43,   44,   45,   50,   47,   -1,  266,  267,   -1,
   -1,  270,  257,  272,   43,  274,   45,   59,   60,   61,
   62,  266,  267,   43,   -1,   45,   -1,  272,  257,  274,
   59,  257,   41,   -1,   43,   44,   45,  266,  267,   59,
  266,  267,   -1,  272,  256,  274,  272,  257,  274,   -1,
   59,   60,   61,   62,   54,   -1,  266,  267,   43,   -1,
   45,   -1,  272,   41,  274,   43,   44,   45,   -1,   93,
   -1,   52,   -1,   41,   59,   43,   44,   45,   -1,   -1,
   -1,   59,   60,   61,   62,   42,   43,   -1,   45,   -1,
   47,   59,   60,   61,   62,   40,   -1,   42,   43,   -1,
   45,   40,   47,   42,   43,   -1,   45,   -1,   47,   41,
   42,   43,   -1,   45,   -1,   47,   41,   42,   43,   -1,
   45,   -1,   47,   -1,  124,  256,  257,  258,  256,  257,
  258,  112,   -1,   -1,   -1,   -1,   -1,   -1,  119,  163,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   10,  265,   -1,   -1,   14,
   -1,   -1,   17,  187,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,  260,  261,  262,  263,  257,  258,   -1,   43,   -1,
  190,   -1,   -1,   -1,  194,   50,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  203,   -1,   -1,   -1,   62,   -1,  233,
   -1,   -1,   -1,   -1,  256,  257,  258,   13,  260,  261,
  262,  263,  264,  265,  205,   -1,   81,  256,  257,  258,
   -1,   -1,   -1,   88,   -1,   -1,   -1,  257,  258,   -1,
   36,   -1,   -1,  267,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,
  284,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   64,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   73,  256,  257,
  258,   -1,  260,  261,  262,  263,  141,  265,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,  256,
  257,  258,   -1,   -1,   -1,   -1,  102,   -1,   -1,  105,
   -1,  256,  257,  258,   -1,   -1,   -1,  256,  257,  258,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,
   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  200,  142,   -1,   -1,   -1,
   -1,   -1,   -1,  208,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  166,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  189,
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
"condicion : '(' expresion comparador expresion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion ')'",
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
"if : IF condicion rama_else ENDIF ';'",
"if : IF error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"rama_else : ELSE",
"do_while : DO cuerpo_do ';'",
"do_while : DO cuerpo_do_recuperacion ';'",
"do_while : DO cuerpo_do error",
"do_while : DO cuerpo_do_recuperacion error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
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
"impresion : PRINT imprimible ';'",
"impresion : PRINT imprimible error",
"impresion : PRINT imprimible_recuperacion ';'",
"impresion : PRINT imprimible_recuperacion error",
"imprimible : '(' elemento_imprimible ')'",
"imprimible_recuperacion : '(' ')'",
"imprimible_recuperacion : elemento_imprimible",
"imprimible_recuperacion :",
"elemento_imprimible : STR",
"elemento_imprimible : expresion",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda_recuperacion ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda_recuperacion error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda_recuperacion error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda_recuperacion error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda_recuperacion error",
"argumento_lambda : '(' factor ')'",
"argumento_lambda_recuperacion : '(' ')'",
"argumento_lambda_recuperacion :",
"parametro_lambda : '(' UINT ID ')'",
};

//#line 844 "gramatica.y"

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
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
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
case 103:
//#line 500 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 104:
//#line 502 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 106:
//#line 513 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 113:
//#line 529 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 114:
//#line 538 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 115:
//#line 543 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 116:
//#line 545 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 117:
//#line 547 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 118:
//#line 549 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 121:
//#line 561 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 122:
//#line 570 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 124:
//#line 576 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 125:
//#line 578 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 580 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 128:
//#line 593 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 129:
//#line 595 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 131:
//#line 610 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 132:
//#line 615 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 133:
//#line 618 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 135:
//#line 630 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 647 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 659 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 677 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 679 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 691 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 700 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 705 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 707 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 709 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 711 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 720 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 730 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 737 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 742 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 751 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 756 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 759 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 165:
//#line 772 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 166:
//#line 775 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 167:
//#line 777 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 170:
//#line 793 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 171:
//#line 798 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 173:
//#line 801 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 174:
//#line 804 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 175:
//#line 806 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 176:
//#line 808 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 177:
//#line 810 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 178:
//#line 812 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 179:
//#line 814 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 181:
//#line 827 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 182:
//#line 830 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1403 "Parser.java"
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
