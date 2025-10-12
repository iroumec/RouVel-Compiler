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
   44,   44,   45,   45,   47,   47,   46,   46,   48,   48,
   48,   48,   48,   48,   48,   39,   39,   39,   39,   39,
   49,   49,   49,   40,   40,   40,   40,   40,   50,   51,
   51,   52,   28,   28,   28,   55,   55,   54,   53,   53,
   56,   56,   56,   58,   58,   57,   57,   57,   59,   59,
   59,   36,   36,   36,   36,   36,   12,   13,   13,   14,
   14,   37,   37,   37,   37,   60,   61,   61,   61,   62,
   62,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,   64,   65,   65,   63,
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
    3,    2,    1,    0,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    2,    5,
    0,    2,    1,    3,    3,    3,    3,    2,    2,    1,
    2,    2,    6,    5,    7,    1,    2,    3,    1,    0,
    1,    3,    1,    2,    2,    3,    2,    2,    0,    1,
    1,    5,    5,    4,    3,    2,    4,    1,    3,    3,
    1,    3,    3,    3,    3,    3,    2,    1,    0,    1,
    1,    4,    4,    4,    4,    5,    4,    5,    5,    4,
    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    9,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    2,
    0,    0,   23,   26,   27,   28,    0,   39,   40,   41,
   42,   43,   44,   46,   47,    0,   10,   11,    5,    0,
   25,    0,    0,   97,  170,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,  168,
  119,    0,    0,    0,   53,    0,    0,    0,    0,  128,
    0,    0,   32,    0,   33,    0,    0,  130,  156,    0,
    0,    0,   19,   16,    0,    0,    0,    0,   67,    0,
    0,    0,    0,   45,   38,   24,   20,    0,   31,   29,
    0,   36,    0,    0,    0,    7,  100,    0,    0,  158,
    0,   98,  167,    0,   95,   80,   81,    0,   87,    0,
   94,   96,    0,   85,   91,  163,  162,  165,  164,  102,
    0,    0,    0,    0,    0,   50,   57,   49,    0,    0,
  151,  150,    0,    0,    0,  141,  143,    0,   51,   56,
   48,    0,    0,  132,   35,    0,  131,  129,  126,  124,
  127,  125,    0,    0,  155,    0,    0,    0,    0,   61,
    0,    0,   68,   60,    0,   69,    0,    0,    0,    0,
    0,    0,    0,    0,   37,    0,    0,    0,    0,    0,
  157,  166,    0,   77,    0,   84,   82,  122,    0,    0,
  111,  113,  112,  114,  115,  109,  110,    0,  106,  105,
  101,   54,    0,  145,    0,    0,    0,  148,   55,    0,
   34,  154,    0,   21,   22,  185,   59,   58,   64,   62,
    0,    0,   66,   15,    0,    0,  183,    0,  173,  172,
  175,  174,    0,    0,  177,  180,   12,  160,  159,   88,
   86,    0,  118,  120,    0,    0,    0,  136,  134,  142,
  146,   52,  153,  152,    0,   72,   70,  176,  179,  182,
  178,  181,  117,  116,    0,  133,  137,    0,   65,   63,
    0,   74,  135,  138,   73,
};
final static short yydgoto[] = {                          4,
   51,   52,   53,  118,  119,  120,   54,   68,  265,   17,
   56,   18,  109,  110,   19,   20,  278,   39,    5,    7,
  168,   22,   98,   23,   24,   25,   26,   27,  100,   74,
   75,  104,   28,   29,   30,   31,   32,   33,   34,   35,
   92,   93,  233,   63,   64,  135,  211,  208,  133,   76,
   77,   78,  144,  258,  259,  145,  146,  147,  148,   58,
   59,   60,   36,  181,  182,
};
final static short yysindex[] = {                         3,
   55,    0,   33,    0, -107,    0, -223,  -79,   -8,   66,
  -30,  -38,  -11,  170,  -35, -208,  103,  -40,    0,    0,
  165,  -60,    0,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,    0,  185,    0,    0,    0,  -52,
    0, -188,  391,    0,    0,  427, -181,    0,    0,  439,
  271,  -27,    0,  292, -164,    0,    0,  -36,  101,    0,
    0,   69,   75,  391,    0,  136,   24,   72, -144,    0,
   94,  188,    0,  -33,    0,  135,  147,    0,    0,  532,
   82,   -3,    0,    0,   78, -122,  335, -117,    0,  116,
 -164,   27,   34,    0,    0,    0,    0,   98,    0,    0,
  188,    0,  113,  201,   98,    0,    0,  313,   88,    0,
  -27,    0,    0,  121,    0,    0,    0,  -27,    0,  399,
    0,    0,  352,    0,    0,    0,    0,    0,    0,    0,
  204, -114, -100,  326,  -13,    0,    0,    0,  -86,   24,
    0,    0, -206,  131,  129,    0,    0, -214,    0,    0,
    0,  -76,   27,    0,    0,  226,    0,    0,    0,    0,
    0,    0,  134,  -10,    0,    0,   65,   74,  156,    0,
  356,  155,    0,    0,   77,    0,   27,  109,  237,   35,
  191,  229,  113,  -28,    0,  -48,   -2,  128,   15,  391,
    0,    0,  365,    0,  -27,    0,    0,    0,   37,  222,
    0,    0,    0,    0,    0,    0,    0,  391,    0,    0,
    0,    0,  256,    0,  187, -206, -117,    0,    0,  259,
    0,    0,  263,    0,    0,    0,    0,    0,    0,    0,
   27,  302,    0,    0,  160,  163,    0,  318,    0,    0,
    0,    0,  164,  168,    0,    0,    0,    0,    0,    0,
    0,  304,    0,    0,  271,  289,   45,    0,    0,    0,
    0,    0,    0,    0,  345,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,  139,    0,    0,
   27,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        21,
  173,    0,  440,    0,  440,    0,    0,    0,  471,  325,
  514,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  443,  151,    0,    0,    0,    0,    1,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -32,  446,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  552,  180,    0,    0,  186,   13,    0,    0,    0,
  514,    0,    0,  514,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  355,  197,    0,    0,    0,  111,    0,    0,
  481,    0,    0,    0,    0,    0,    0,  493,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   40,   84,    0,   18,    0,    0,    0,    0,    0,   13,
    0,    0, -191,    0,  415,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   12,  463,  464,    0,    0,
    0,  -19,    0,    0,    0,    0,    0,    0,  197,    0,
    0,    0,  197,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  503,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   -5,  142,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   20,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  588,  -22,   16,    0,    0,    0,  377,    0,    0,   -6,
  405,   43,    0,  275,    9,  461,  189,    0,    0,    0,
    0,    0,    0,  -21,  591,    0,    0,    0,    0,  -12,
  441,  -54,    0,    0,    0,    0,    0,    0,    0,    0,
  386,    0,    0,  114,    0,    0,    0,    0,  364,    0,
    0,  425,  361,  246,    0,    0, -113,    0,    0,    0,
    0,  460,    0,  -78,  344,
};
final static int YYTABLESIZE=810;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         96,
   30,   67,    2,   55,   16,   69,   62,   55,  171,   62,
   91,   18,  237,   37,   49,   15,   47,  156,   95,   48,
    4,   99,  127,  111,   99,  186,  171,  210,   16,  214,
  223,   43,  116,   40,  117,  144,   55,   42,  144,   55,
   30,   99,  172,   55,  121,   41,  179,   55,  106,  141,
  132,   25,   57,  140,    6,  217,   57,   55,  108,   57,
  107,   86,   97,   96,  142,  149,   99,  143,  107,  124,
  105,   47,   16,   55,  121,  237,  112,   88,  149,   47,
   55,  173,   69,  121,   16,   57,  176,   83,   57,   84,
   71,  125,   57,  122,   89,  253,   57,  195,  123,   69,
  235,  121,  260,   16,  243,   50,   57,   49,   46,  130,
   47,   72,   48,   55,   16,  152,   55,   16,  198,  153,
  231,  166,   57,  122,  116,   30,  117,  121,  191,   57,
  151,  190,  122,   62,  169,  230,   25,   16,  197,  172,
  165,  218,  121,    4,   49,   46,   88,   47,   16,   48,
  122,  161,  180,  131,  161,   15,   96,  121,  116,  129,
  117,  192,   57,   89,  121,   57,   96,   16,  200,  277,
  212,  215,  216,   55,  174,  140,  122,   55,   16,  139,
  219,   42,  147,   55,  154,  147,   55,  157,  283,  224,
   14,   21,  222,  160,  138,  238,  226,   72,  225,  238,
   42,   55,  167,   85,   16,  162,  122,  245,  251,   80,
  261,   49,   46,  122,   47,   94,   48,   65,   66,  126,
   82,    9,   57,  171,   16,   61,   57,   16,    9,   44,
   10,   11,   57,  234,   12,   57,   13,   99,   14,   71,
  184,   86,  209,   16,   70,    9,    9,  115,  121,  240,
   57,  149,  247,  246,   10,   11,   96,   30,    1,    3,
   13,   71,   14,  284,  149,   16,   30,   30,   25,  149,
   30,  248,   30,  108,   30,  107,  184,   25,   25,  141,
  254,   25,  149,   25,   44,   25,  178,  242,    8,    9,
  172,    9,   44,  188,  142,   69,  256,  122,   10,   11,
    8,    9,   12,   71,   13,  252,   14,  101,  123,  257,
   10,   11,  155,  116,   12,  117,   13,  262,   14,    8,
    9,  264,    9,   44,   45,  183,   72,  149,  150,   10,
   11,    9,  229,   12,    9,   13,   47,   14,    9,  115,
   10,   11,  131,   10,   11,  267,   13,   12,   14,   13,
  221,   14,  121,    8,    9,  116,  128,  117,  270,    9,
   44,  221,  274,   10,   11,    9,   87,   12,  116,   13,
  117,   14,    9,  115,   10,   11,   49,   46,   12,   47,
   13,   48,   14,  169,    9,  207,  205,  206,  281,   47,
  159,  136,  137,   10,   11,    9,   47,   12,  116,   13,
  117,   14,  161,  280,   10,   11,   14,   14,   12,   47,
   13,  275,   14,  184,  228,  268,   14,   14,  269,  271,
   14,    9,   14,  272,   14,   79,    9,   44,  123,    6,
   10,   11,   49,   46,   12,   47,   13,   48,   14,   17,
   49,    9,    3,   47,    9,   48,  239,  187,  121,   99,
   10,   11,  184,   10,   11,  139,   13,    9,   14,   13,
    9,   14,   12,   13,  249,   38,   10,   11,   49,   10,
   11,   47,   13,   48,   14,   13,  103,   14,  177,  113,
   49,   46,    9,   47,  241,   48,   75,  123,   75,   75,
   75,   10,   11,    9,  193,  199,  175,   13,  158,   14,
  213,  276,   10,   11,   75,   75,   75,   75,   13,  114,
   14,   99,   99,   99,   99,   99,    0,   99,  263,    0,
    0,   79,  236,   79,   79,   79,  244,    9,  115,   99,
   99,   99,   99,   78,    0,   78,   78,   78,    0,   79,
   79,   79,   79,   76,    0,   76,   76,   76,    9,   44,
    0,   78,   78,   78,   78,  104,  104,  220,  104,  273,
  104,   76,   76,   76,   76,    0,    0,    0,    0,    9,
  115,  123,  163,   49,   46,    0,   47,  189,   48,    0,
  169,  232,    9,  115,    0,  201,  202,  203,  204,    0,
  170,    9,   44,  103,  103,    0,  103,    0,  103,    0,
  279,   81,   44,   73,   90,    0,    0,  196,    9,   44,
  184,  227,    9,  115,    0,    0,    0,    0,    0,    0,
  250,    9,   44,    0,    0,    0,  102,    0,    0,    0,
  108,    0,    0,    0,    0,  266,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    9,   44,    0,
    0,  134,    0,   73,  194,    9,   44,    0,    0,    0,
    0,    0,  102,    0,    0,    0,    0,  164,    0,  282,
    0,    0,    0,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    9,   44,  285,    0,    0,    0,    0,
    0,  102,    0,    0,  185,    9,   44,   45,    0,    0,
    0,   75,   75,   75,    0,   75,   75,   75,   75,    0,
   75,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   73,    0,    0,    0,    0,   99,   99,   99,    0,
   99,   99,   99,   99,   99,   99,   79,   79,   79,    0,
   79,   79,   79,   79,    0,   79,  185,    0,   78,   78,
   78,    0,   78,   78,   78,   78,    0,   78,   76,   76,
   76,    0,   76,   76,   76,   76,    0,   76,    0,  185,
  104,  104,    0,    0,    0,    0,    0,  108,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    9,   44,
    0,    0,    0,    0,    0,  255,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  103,  103,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
    0,   40,    0,   10,   40,   12,   40,   14,   41,   40,
   17,    0,   41,    5,   42,  123,   45,   72,   59,   47,
    0,   41,   59,   46,   44,  104,   59,   41,   40,  143,
   41,   40,   43,  257,   45,   41,   43,   46,   44,   46,
   40,   61,  257,   50,   51,  125,  101,   54,   40,  256,
   63,   40,   10,   41,    0,  270,   14,   64,   41,   17,
   41,  270,  123,   85,  271,  257,   59,   44,  257,   54,
  123,   45,   40,   80,   81,   41,  258,   44,  270,   45,
   87,   88,   44,   90,   40,   43,   93,  123,   46,  125,
   45,  256,   50,   51,   61,   59,   54,  120,   59,   61,
  179,  108,  216,   40,  183,   40,   64,   42,   43,   41,
   45,  123,   47,  120,   40,   44,  123,   40,  131,  264,
   44,  125,   80,   81,   43,  125,   45,  134,   41,   87,
   59,   44,   90,   40,  257,   59,  125,   40,  123,  257,
   59,  148,   59,  123,   42,   43,   44,   45,   40,   47,
  108,   41,   40,  268,   44,  123,  178,  164,   43,   59,
   45,   41,  120,   61,  171,  123,  188,   40,  269,  125,
  257,   41,   44,  180,   59,   40,  134,  184,   40,   44,
  257,   46,   41,  190,   71,   44,  193,   74,  125,  125,
   40,    3,   59,   59,   59,  180,   41,  123,  125,  184,
   46,  208,  125,   15,   40,   59,  164,  256,  193,   40,
  217,   42,   43,  171,   45,  256,   47,  256,  257,  256,
  256,  257,  180,  256,   40,  256,  184,   40,  257,  258,
  266,  267,  190,  125,  270,  193,  272,  257,  274,  273,
   40,  270,  256,   40,  256,  257,  257,  258,  255,   59,
  208,  257,  125,  256,  266,  267,  278,  257,  256,  257,
  272,  273,  274,  125,  270,   40,  266,  267,  257,  257,
  270,  257,  272,  256,  274,  256,   40,  266,  267,  256,
   59,  270,  270,  272,  258,  274,   98,   59,  256,  257,
  257,  257,  258,  105,  271,  257,   41,  255,  266,  267,
  256,  257,  270,  258,  272,  269,  274,  123,  269,  123,
  266,  267,  125,   43,  270,   45,  272,   59,  274,  256,
  257,   59,  257,  258,  259,  125,  123,  256,  257,  266,
  267,  257,  256,  270,  257,  272,   45,  274,  257,  258,
  266,  267,  268,  266,  267,   44,  272,  270,  274,  272,
  125,  274,  269,  256,  257,   43,  256,   45,   41,  257,
  258,  125,   59,  266,  267,  257,  264,  270,   43,  272,
   45,  274,  257,  258,  266,  267,   42,   43,  270,   45,
  272,   47,  274,   59,  257,   60,   61,   62,   44,   45,
  256,  256,  257,  266,  267,  257,   45,  270,   43,  272,
   45,  274,  256,   59,  266,  267,  256,  257,  270,   45,
  272,  123,  274,   59,   59,  256,  266,  267,  256,  256,
  270,  257,  272,  256,  274,  256,  257,  258,   52,  257,
  266,  267,   42,   43,  270,   45,  272,   47,  274,    0,
   42,  257,    0,   45,  257,   47,  256,  104,  269,  264,
  266,  267,  256,  266,  267,   41,  272,  257,  274,  272,
  257,  274,    0,    0,  190,    5,  266,  267,   42,  266,
  267,   45,  272,   47,  274,  272,   36,  274,   93,   41,
   42,   43,  257,   45,  256,   47,   41,  111,   43,   44,
   45,  266,  267,  257,  118,  132,   92,  272,   74,  274,
  140,  256,  266,  267,   59,   60,   61,   62,  272,   50,
  274,   41,   42,   43,   44,   45,   -1,   47,  256,   -1,
   -1,   41,  179,   43,   44,   45,  183,  257,  258,   59,
   60,   61,   62,   41,   -1,   43,   44,   45,   -1,   59,
   60,   61,   62,   41,   -1,   43,   44,   45,  257,  258,
   -1,   59,   60,   61,   62,   42,   43,  153,   45,  256,
   47,   59,   60,   61,   62,   -1,   -1,   -1,   -1,  257,
  258,  195,   41,   42,   43,   -1,   45,  265,   47,   -1,
  256,  177,  257,  258,   -1,  260,  261,  262,  263,   -1,
  256,  257,  258,   42,   43,   -1,   45,   -1,   47,   -1,
  256,   14,  258,   13,   17,   -1,   -1,  256,  257,  258,
  256,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   36,   -1,   -1,   -1,
   43,   -1,   -1,   -1,   -1,  231,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
   -1,   64,   -1,   63,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,   72,   -1,   -1,   -1,   -1,   80,   -1,  265,
   -1,   -1,   -1,   -1,   87,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  281,   -1,   -1,   -1,   -1,
   -1,  101,   -1,   -1,  104,  257,  258,  259,   -1,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  131,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,  156,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,  179,
  257,  258,   -1,   -1,   -1,   -1,   -1,  190,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,   -1,   -1,   -1,   -1,  208,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
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
"if : IF condicion rama_else ENDIF ';'",
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

//#line 849 "gramatica.y"

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
//#line 774 "Parser.java"
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
{ notifyError(String.format("Falta operador previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
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
case 104:
//#line 502 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 507 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 108:
//#line 518 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 115:
//#line 534 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 116:
//#line 543 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 117:
//#line 548 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 118:
//#line 550 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 119:
//#line 552 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 120:
//#line 554 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 123:
//#line 566 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 124:
//#line 575 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 126:
//#line 581 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 127:
//#line 583 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 128:
//#line 585 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 130:
//#line 598 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 131:
//#line 600 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 615 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 134:
//#line 620 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 623 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 137:
//#line 635 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 140:
//#line 652 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 143:
//#line 664 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 147:
//#line 682 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 148:
//#line 684 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 151:
//#line 696 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 152:
//#line 705 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 153:
//#line 710 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 154:
//#line 712 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 155:
//#line 714 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 156:
//#line 716 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 157:
//#line 725 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 159:
//#line 735 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 160:
//#line 742 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 161:
//#line 747 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 162:
//#line 756 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 163:
//#line 761 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 165:
//#line 764 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 167:
//#line 777 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 168:
//#line 780 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 169:
//#line 782 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 172:
//#line 798 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 173:
//#line 803 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 175:
//#line 806 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 176:
//#line 809 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 177:
//#line 811 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 178:
//#line 813 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 179:
//#line 815 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 180:
//#line 817 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 181:
//#line 819 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 183:
//#line 832 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 184:
//#line 835 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1387 "Parser.java"
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
