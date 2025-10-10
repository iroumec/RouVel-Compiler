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
   45,   45,   46,   46,   48,   48,   47,   47,   49,   49,
   49,   49,   49,   49,   49,   39,   39,   39,   39,   50,
   50,   40,   40,   40,   51,   52,   52,   52,   53,   28,
   28,   56,   56,   55,   54,   54,   57,   57,   57,   59,
   59,   58,   58,   58,   60,   60,   60,   36,   36,   36,
   36,   36,   12,   13,   13,   14,   14,   37,   37,   62,
   62,   62,   62,   61,   63,   63,   38,   38,   38,   38,
   38,   66,   66,   66,   65,   64,
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
    3,    2,    1,    0,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    2,    0,
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
  119,    0,    0,    0,   51,    0,    0,    0,    0,  124,
    0,    0,   31,    0,   32,    0,    0,    0,  152,    0,
    0,    0,   18,   15,    0,    0,   60,    0,    0,   67,
    0,    0,    0,    0,   44,   37,   23,   19,    0,   30,
   28,   63,   61,    0,    0,   35,    0,    0,    0,    7,
  100,    0,    0,  154,    0,   98,    0,    0,   95,   80,
   81,    0,   87,    0,   94,   96,    0,   85,   91,  158,
  159,  102,    0,    0,    0,   49,   56,   48,    0,    0,
  147,  146,    0,    0,    0,  137,  139,    0,   50,   55,
   47,    0,    0,  129,   34,    0,    0,  125,  122,  123,
    0,    0,  151,    0,    0,    0,    0,    0,    0,   68,
   59,   65,   69,    0,    0,    0,   72,    0,    0,    0,
    0,    0,    0,   36,    0,    0,    0,    0,    0,  153,
  164,    0,   77,    0,   84,   82,    0,    0,  111,  113,
  112,  114,  115,  109,  110,    0,  106,  105,  101,   53,
    0,  141,    0,    0,    0,  144,   54,    0,   33,  150,
    0,   20,   21,  176,   58,   57,    0,   66,   14,   64,
   62,    0,   74,    0,    0,    0,  167,  168,    0,  170,
   11,  156,  155,   88,   86,  121,    0,  118,    0,    0,
    0,  132,  131,  138,  142,   52,  149,  148,   70,   73,
  169,  175,  171,  117,  116,    0,  130,  133,    0,  134,
};
final static short yydgoto[] = {                          3,
   51,   52,   53,  122,  123,  124,   54,   68,  176,   16,
   56,   17,  113,  114,   18,   19,  269,   39,    4,    6,
  166,   21,   99,   22,   23,   24,   25,   26,  101,   74,
   75,  108,   27,   28,   29,   30,   31,   32,   33,   34,
   35,   93,   94,  228,   63,   64,  135,  209,  206,  198,
   76,   77,   78,  144,  252,  253,  145,  146,  147,  148,
   58,   59,   60,   36,  185,  181,
};
final static short yysindex[] = {                      -179,
   43,  -15,    0,  -73,    0, -196,  -54,   26,  170,   -2,
   20,   36,  415,  -36, -197,  491,  -45,    0,    0,  151,
  -39,    0,    0,    0,    0,   21,    0,    0,    0,    0,
    0,    0,    0,    0,  -42,   59,    0,    0,    0,  -37,
    0, -178,  404,    0,    0,   56, -165,    0,    0,  393,
   47,   15,    0,   65, -159,    0,    0,   60, -141,    0,
    0,   92,  165,  404,    0,  -10,  -31,  319, -129,    0,
   83,  176,    0,  -29,    0,   80, -116,    0,    0,   89,
  356,   16,    0,    0,   62, -114,    0,  404, -109,    0,
  431, -159,  -30,   50,    0,    0,    0,    0,   82,    0,
    0,    0,    0,  -30,  176,    0,  112,  187,   82,    0,
    0,  101,   68,    0,   15,    0,    0,  116,    0,    0,
    0,   15,    0,  315,    0,    0,  250,    0,    0,    0,
    0,    0, -108,  343,   14,    0,    0,    0,  -99,  -31,
    0,    0, -232,  120,  119,    0,    0, -228,    0,    0,
    0,  -93,  -30,    0,    0,  199,    0,    0,    0,    0,
  106,   84,    0,    0,   44,   45,  130,  292,  126,    0,
    0,    0,    0,  -30,   98,  500,    0,  220,    6,  115,
  -81,  112,  -25,    0,    0,  -78,  110,  -72,  404,    0,
    0,  298,    0,   15,    0,    0,  165,  -13,    0,    0,
    0,    0,    0,    0,    0,  404,    0,    0,    0,    0,
  149,    0,   70, -232, -109,    0,    0,  135,    0,    0,
   41,    0,    0,    0,    0,    0,  152,    0,    0,    0,
    0,  -30,    0,  -77,    0,  154,    0,    0,  -59,    0,
    0,    0,    0,    0,    0,    0,   55,    0,   47,   75,
   24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   82,    0,    0,  128,    0,
};
final static short yyrindex[] = {                        22,
  -58,  201,    0,  201,    0,    0,    0,  437,  -53,  425,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  204,
  140,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,  330,    0,    0,    0,    0,    0, -116,    0,  -50,
    0,  446,    0,    0,    0,  -57,  -33,    0,    0,    0,
  425,    0,    0,  425,    0,  -48,    0,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   63,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   33,   33,    0,    0,
    0,   72,    0,    0,  459,    0,   94,    0,    0,    0,
    0,  469,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   46,   27,    0,    0,    0,    0,    0,  -33,
    0,    0, -203,    0,  258,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   97,    0,    0,    0,
    0,    0,    0,   12,  318,  340,    0,    0,  -35,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    0,  105,
    0,   33,    0,    0,  105,    0,    0,    0,    0,    0,
    0,    0,    0,  481,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -9,   76,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,  113,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   29,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  551,  -28,  441,    0,    0,    0,  417,    0,    0,   -6,
  454,  138,    0,  190,   23,  377,   67,    0,    0,    0,
    0,    0,    0,  -20,  572,    0,    0,    0,    0,  -44,
  347,  -49,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  299,    0,    0,  -43,    0,    0,    0,    0,    0,
    0,    0,  335,  271,  163,    0,    0, -110,    0,    0,
    0,    0,  366,    0,  312,  -87,
};
final static int YYTABLESIZE=769;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   29,  104,   55,   15,   69,   99,   55,  136,   99,   92,
   62,   17,  143,   96,   47,  235,  103,  115,  133,   47,
  186,    4,  156,  141,   15,   99,   37,  154,  169,  140,
  157,  140,  212,  139,  140,   42,   55,   62,  142,   55,
   29,  215,    5,   55,  125,  248,  235,   55,  138,   14,
   47,   24,   71,  145,  208,  178,   49,   55,  166,   67,
   40,   48,  110,   15,   97,   43,  145,  108,   20,  107,
   41,   42,   86,   55,  125,   15,    1,    2,  111,  100,
   85,   55,  170,   98,  125,  109,   83,  173,   84,  120,
  234,  121,  116,   89,  239,  194,  129,   49,   15,  258,
   47,   15,   48,  254,  120,  125,   69,   14,  190,   47,
   90,  189,  157,  265,  131,  157,  143,   55,  130,  143,
   55,   15,   62,   69,  221,   29,  120,  125,  121,  161,
   49,   46,  132,   47,  153,   48,   24,   15,  159,  160,
  164,  216,  167,  120,    4,  121,   57,  169,  268,   15,
   57,  179,  246,   57,   97,  125,  191,  210,   72,  197,
  213,  125,  214,  217,  220,  175,   97,   15,  222,  223,
  224,   42,   55,  237,  238,  187,   55,  240,  261,   13,
   57,  105,   55,   57,  242,   55,  165,   57,  126,  250,
   15,   57,  251,  256,  262,  259,  263,  266,    6,   55,
   16,   57,  163,    3,   15,  162,   99,  126,  255,   50,
   95,   49,   46,  102,   47,   15,   48,   57,  126,   82,
    8,   99,  229,  145,  141,   57,  183,   44,  126,    9,
   10,    8,   44,   11,  241,   12,  145,   13,   15,  142,
    7,    8,  125,   71,   86,  136,  137,  145,   97,  126,
    9,   10,  270,   61,   11,  247,   12,   29,   13,  183,
  145,   57,    8,   44,   57,   71,   29,   29,   24,  207,
   29,  126,   29,  166,   29,   65,   66,   24,   24,    7,
    8,   24,  108,   24,  107,   24,  127,   72,  174,    9,
   10,   70,    8,   11,   47,   12,  257,   13,  135,  126,
  155,    9,   10,    8,  119,  126,  169,   12,   71,   13,
  264,  182,    8,   44,  120,    8,   57,   11,    8,   69,
   57,    8,   44,  219,    9,   10,   57,    9,   10,   57,
   12,   11,   13,   12,  120,   13,  121,    7,    8,   12,
    8,  119,   47,   57,  219,    8,   44,    9,   10,  161,
  226,   11,  128,   12,    8,   13,   49,    8,  119,   47,
  172,   48,  152,    9,   10,  188,    8,   11,  173,   12,
   75,   13,   75,   75,   75,    9,   10,  151,  243,   11,
   38,   12,  107,   13,    8,  120,  126,  121,   75,   75,
   75,   75,  174,    9,   10,   13,   13,   11,  120,   12,
  121,   13,  205,  203,  204,   13,   13,    8,  158,   13,
  211,   13,  267,   13,  163,  118,    9,   10,  180,    0,
   11,    8,   12,    0,   13,    0,    8,   44,   45,    0,
    9,   10,    8,  117,   49,   46,   12,   47,   13,   48,
    0,    9,   10,    8,    0,   49,   46,   12,   47,   13,
   48,    0,    9,   10,   80,    8,   49,   46,   12,   47,
   13,   48,    0,    0,    9,   10,  104,  104,  127,  104,
   12,  104,   13,  120,    0,  121,    8,   99,   99,   99,
   99,   99,    0,   99,    0,    9,   10,  103,  103,  171,
  103,   12,  103,   13,  128,   99,   99,   99,   99,   79,
    0,   79,   79,   79,    0,  195,    8,   44,    0,   78,
    0,   78,   78,   78,    0,    0,    0,   79,   79,   79,
   79,   76,    0,   76,   76,   76,    0,   78,   78,   78,
   78,  127,   49,   46,   89,   47,    0,   48,  192,   76,
   76,   76,   76,  232,   47,    0,  172,  225,    8,  119,
    0,   90,    0,  244,    8,   44,    0,  177,  231,    0,
    0,    0,    0,   81,    0,    0,   91,  196,    0,    0,
  193,    8,   44,    0,  149,  150,    0,    0,    0,    0,
    0,    0,    0,   73,    0,   75,   75,   75,    0,   75,
   75,   75,   75,  112,   75,    0,    0,    0,    0,    8,
  119,    0,  199,  200,  201,  202,  218,  106,    0,    0,
  127,    0,    8,  119,  134,    0,    0,    0,    0,  236,
    0,    0,    0,  236,    0,    0,    0,  227,    0,  233,
  162,    0,  245,    0,   73,    0,    0,    0,  168,    0,
    0,    0,    0,  106,    0,    0,    0,    0,    0,    8,
   44,   45,    0,    0,    0,    0,    0,    0,    0,    0,
    8,   44,    0,    0,    0,    0,    0,    0,    0,    0,
   79,    8,   44,    0,    0,    0,  106,    0,    0,  184,
    0,  104,  104,    0,    0,  260,    0,    8,  119,    0,
    0,    0,   99,   99,   99,    0,   99,   99,   99,   99,
   99,   99,  103,  103,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   79,   79,   79,    0,   79,   79,
   79,   79,    0,   79,   78,   78,   78,  184,   78,   78,
   78,   78,    0,   78,    0,    0,   76,   76,   76,  112,
   76,   76,   76,   76,    0,   76,   87,    8,   44,  184,
    0,    0,    0,    0,   88,  230,  249,   44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   73,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   44,    9,   40,   11,   41,   13,   41,   44,   16,
   40,    0,   44,   59,   45,   41,   59,   46,   63,   45,
  108,    0,   72,  256,   40,   61,    4,   71,  257,   40,
   74,   41,  143,   44,   44,   46,   43,   40,  271,   46,
   40,  270,    0,   50,   51,   59,   41,   54,   59,  123,
   45,   40,   45,  257,   41,  105,   42,   64,   41,   40,
  257,   47,   40,   40,   85,   40,  270,   41,    2,   41,
  125,   46,  270,   80,   81,   40,  256,  257,  257,   59,
   14,   88,   89,  123,   91,  123,  123,   94,  125,   43,
  178,   45,  258,   44,  182,  124,  256,   42,   40,   59,
   45,   40,   47,  214,   59,  112,   44,  123,   41,   45,
   61,   44,   41,   59,  256,   44,   41,  124,   59,   44,
  127,   40,   40,   61,   41,  125,   43,  134,   45,   41,
   42,   43,   41,   45,  264,   47,  125,   40,   59,  256,
  125,  148,  257,   43,  123,   45,    9,  257,  125,   40,
   13,   40,  197,   16,  175,  162,   41,  257,  123,  268,
   41,  168,   44,  257,   59,   99,  187,   40,  125,  125,
   41,   46,  179,   59,  256,  109,  183,  256,  256,   40,
   43,  123,  189,   46,  257,  192,  125,   50,   51,   41,
   40,   54,  123,   59,   41,   44,  256,  123,  257,  206,
    0,   64,  256,    0,   40,  256,  264,  256,  215,   40,
  256,   42,   43,  256,   45,   40,   47,   80,   81,  256,
  257,  257,  125,  257,  256,   88,   40,  258,   91,  266,
  267,  257,  258,  270,  125,  272,  270,  274,   40,  271,
  256,  257,  249,  273,  270,  256,  257,  257,  269,  112,
  266,  267,  125,  256,  270,  269,  272,  257,  274,   40,
  270,  124,  257,  258,  127,  258,  266,  267,  257,  256,
  270,  134,  272,  256,  274,  256,  257,  266,  267,  256,
  257,  270,  256,  272,  256,  274,  256,  123,  256,  266,
  267,  256,  257,  270,   45,  272,  256,  274,   41,  162,
  125,  266,  267,  257,  258,  168,  257,  272,  273,  274,
  256,  125,  257,  258,  269,  257,  179,    0,  257,  257,
  183,  257,  258,  125,  266,  267,  189,  266,  267,  192,
  272,  270,  274,  272,   43,  274,   45,  256,  257,    0,
  257,  258,   45,  206,  125,  257,  258,  266,  267,  256,
   59,  270,  256,  272,  257,  274,   42,  257,  258,   45,
  256,   47,   44,  266,  267,  265,  257,  270,  256,  272,
   41,  274,   43,   44,   45,  266,  267,   59,  189,  270,
    4,  272,   36,  274,  257,   43,  249,   45,   59,   60,
   61,   62,   94,  266,  267,  256,  257,  270,   43,  272,
   45,  274,   60,   61,   62,  266,  267,  257,   74,  270,
  140,  272,  250,  274,   59,   50,  266,  267,  107,   -1,
  270,  257,  272,   -1,  274,   -1,  257,  258,  259,   -1,
  266,  267,  257,   41,   42,   43,  272,   45,  274,   47,
   -1,  266,  267,  257,   -1,   42,   43,  272,   45,  274,
   47,   -1,  266,  267,   40,  257,   42,   43,  272,   45,
  274,   47,   -1,   -1,  266,  267,   42,   43,   52,   45,
  272,   47,  274,   43,   -1,   45,  257,   41,   42,   43,
   44,   45,   -1,   47,   -1,  266,  267,   42,   43,   59,
   45,  272,   47,  274,   54,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,   -1,  256,  257,  258,   -1,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,   59,   60,   61,
   62,   41,   -1,   43,   44,   45,   -1,   59,   60,   61,
   62,  115,   42,   43,   44,   45,   -1,   47,  122,   59,
   60,   61,   62,   44,   45,   -1,   93,  256,  257,  258,
   -1,   61,   -1,  256,  257,  258,   -1,  104,   59,   -1,
   -1,   -1,   -1,   13,   -1,   -1,   16,  127,   -1,   -1,
  256,  257,  258,   -1,  256,  257,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   12,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,   43,  265,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,  153,   36,   -1,   -1,
  194,   -1,  257,  258,   64,   -1,   -1,   -1,   -1,  179,
   -1,   -1,   -1,  183,   -1,   -1,   -1,  174,   -1,  176,
   80,   -1,  192,   -1,   63,   -1,   -1,   -1,   88,   -1,
   -1,   -1,   -1,   72,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,  105,   -1,   -1,  108,
   -1,  257,  258,   -1,   -1,  232,   -1,  257,  258,   -1,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,  265,  256,  257,  258,  156,  260,  261,
  262,  263,   -1,  265,   -1,   -1,  256,  257,  258,  189,
  260,  261,  262,  263,   -1,  265,  256,  257,  258,  178,
   -1,   -1,   -1,   -1,  264,  256,  206,  258,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  197,
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
"condicion : parentesis_apertura cuerpo_condicion parentesis_cierre",
"condicion : '(' ')'",
"parentesis_apertura : '('",
"parentesis_apertura :",
"parentesis_cierre : ')'",
"parentesis_cierre : error",
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

//#line 841 "gramatica.y"

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
//#line 749 "Parser.java"
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
//#line 82 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron, previo al nombre del programa, sentencias."); }
break;
case 8:
//#line 85 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 12:
//#line 106 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 13:
//#line 109 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 15:
//#line 112 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 16:
//#line 114 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 17:
//#line 116 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
break;
case 24:
//#line 142 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 28:
//#line 159 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 34:
//#line 184 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 202 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 44:
//#line 213 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 47:
//#line 229 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 48:
//#line 232 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 49:
//#line 237 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 50:
//#line 241 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 51:
//#line 245 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 249 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 259 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 55:
//#line 264 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 56:
//#line 271 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 57:
//#line 285 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 58:
//#line 291 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 59:
//#line 294 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 60:
//#line 297 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 61:
//#line 307 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 62:
//#line 309 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 63:
//#line 314 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 64:
//#line 316 "gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 69:
//#line 337 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 71:
//#line 345 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 353 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 74:
//#line 358 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 76:
//#line 372 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 377 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de %s %s.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 78:
//#line 381 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 79:
//#line 388 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 80:
//#line 398 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 81:
//#line 400 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 82:
//#line 407 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 84:
//#line 413 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 85:
//#line 420 "gramatica.y"
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 86:
//#line 427 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 88:
//#line 433 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 89:
//#line 440 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 90:
//#line 442 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 98:
//#line 469 "gramatica.y"
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
//#line 488 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 101:
//#line 497 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 102:
//#line 502 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 104:
//#line 508 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 513 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 108:
//#line 524 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 115:
//#line 540 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 116:
//#line 549 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 117:
//#line 554 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 118:
//#line 556 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 119:
//#line 558 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 122:
//#line 574 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 123:
//#line 579 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 124:
//#line 581 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 127:
//#line 598 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 128:
//#line 600 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 131:
//#line 619 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 133:
//#line 631 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 136:
//#line 648 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 139:
//#line 660 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 143:
//#line 678 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 144:
//#line 680 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 147:
//#line 692 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 148:
//#line 701 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 149:
//#line 706 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 150:
//#line 708 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 151:
//#line 710 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 152:
//#line 712 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 153:
//#line 721 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 155:
//#line 731 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 156:
//#line 738 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 157:
//#line 743 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 158:
//#line 752 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 159:
//#line 757 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 161:
//#line 768 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 162:
//#line 771 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 163:
//#line 773 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 167:
//#line 796 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 168:
//#line 801 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 169:
//#line 803 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 170:
//#line 805 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 171:
//#line 807 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 173:
//#line 818 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 174:
//#line 821 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1322 "Parser.java"
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
