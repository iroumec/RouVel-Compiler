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
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 36 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 32 "Parser.java"




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
    0,    0,    0,   23,    0,   24,    0,    0,    0,   18,
   22,   22,   19,   20,   27,   20,   20,   20,   20,   26,
   26,   25,   25,   21,   21,   21,   28,   28,   30,   30,
   33,   33,   34,   34,   35,   35,   36,   36,   29,   29,
   29,   29,   29,   29,   29,   29,   39,   39,   31,   31,
   31,   31,   31,   31,    9,    9,    9,    9,   37,   37,
   37,   37,   38,   38,   38,   38,   38,   45,   45,   46,
   46,   47,   47,   10,   10,   10,    1,    1,    1,    1,
    1,    6,    6,    2,    2,    2,    2,    4,    4,    4,
    7,    7,    3,    3,    3,    5,    5,    5,   12,   12,
   11,   11,   48,   48,   48,   48,   49,   49,   49,   49,
   16,   16,   16,   16,   16,   16,   16,   43,   43,   43,
   50,   51,   51,   51,   52,   53,   53,   53,   55,   44,
   54,   54,   54,   54,   54,   56,   58,   57,   57,   59,
   32,   32,   32,   32,    8,   62,   61,   60,   60,   63,
   63,   63,   65,   65,   64,   64,   64,   17,   17,   17,
   40,   40,   40,   40,   40,   13,   14,   14,   15,   15,
   41,   41,   41,   41,   66,   67,   67,   67,   68,   68,
   42,   42,   42,   42,   42,   42,   42,   42,   42,   42,
   70,   71,   71,   69,
};
final static short yylen[] = {                            2,
    2,    2,    2,    0,    2,    0,    4,    2,    1,    1,
    1,    1,    3,    3,    0,    4,    2,    0,    3,    2,
    2,    2,    2,    1,    2,    2,    1,    1,    1,    2,
    0,    1,    1,    1,    3,    2,    1,    2,    2,    1,
    1,    1,    1,    1,    1,    2,    1,    1,    3,    3,
    3,    3,    5,    2,    3,    3,    2,    2,    4,    4,
    3,    3,    4,    6,    4,    6,    5,    3,    1,    2,
    1,    2,    1,    1,    3,    2,    1,    3,    3,    2,
    2,    1,    1,    3,    1,    3,    2,    3,    1,    3,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    2,
    1,    3,    3,    2,    2,    3,    3,    2,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    3,    2,
    4,    4,    3,    3,    1,    0,    2,    1,    0,    2,
    3,    3,    3,    3,    2,    2,    1,    1,    2,    2,
    3,    4,    3,    4,    2,    1,    3,    3,    2,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    5,    5,    4,    3,    2,    4,    1,    3,    3,    1,
    3,    3,    3,    3,    3,    2,    1,    0,    1,    1,
    4,    4,    4,    4,    5,    4,    5,    5,    4,    5,
    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    9,   10,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    2,    0,    0,   24,   27,   28,   29,    0,   40,   41,
   42,   43,   44,   45,   47,   48,    0,    0,    0,   11,
   12,    5,    0,   26,    0,    0,   99,  179,    0,    0,
   91,   92,    0,    0,    0,   85,    0,    0,   94,   95,
    0,    0,  177,  120,    0,    0,    0,    0,    0,   54,
    0,    0,    0,  165,    0,    0,    0,   20,   17,    0,
    0,    0,    0,    0,    0,   69,    0,    0,    0,    0,
   46,   39,   25,   21,    0,   32,   30,    0,  130,    0,
    0,   37,    0,    0,    0,    7,  102,    0,    0,  167,
    0,  100,  176,    0,   97,   82,   83,    0,   89,    0,
   96,   98,    0,   87,   93,  172,  171,  174,  173,  104,
    0,  113,  115,  114,  116,  117,  111,  112,    0,    0,
    0,    0,    0,   33,  125,   34,  118,  119,    0,    0,
  105,   51,   58,   50,    0,   52,   57,   49,    0,    0,
    0,    0,  164,    0,    0,    0,    0,  160,  159,    0,
  149,    0,    0,  150,  152,    0,  141,   62,    0,    0,
   70,   61,    0,   71,    0,    0,  135,    0,    0,    0,
    0,    0,  138,    0,  143,   36,    0,    0,    0,    0,
    0,    0,   38,    0,    0,    0,    0,    0,  166,  175,
    0,   79,    0,   86,   84,  106,  103,    0,    0,  127,
    0,    0,    0,   55,   56,    0,  163,    0,   22,   23,
  194,  154,  157,    0,    0,  148,  142,    0,   60,   59,
   65,   63,    0,    0,   74,    0,   68,   16,  140,  139,
  133,  131,  134,  132,  136,  144,   35,    0,    0,  192,
    0,  182,  181,  184,  183,    0,    0,  186,  189,   13,
  169,  168,   90,   88,    0,  123,  124,   53,  162,  161,
  155,  151,  147,    0,   67,    0,   76,   72,  185,  188,
  191,  187,  190,  122,  121,   66,   64,   75,
};
final static short yydgoto[] = {                          4,
   66,   55,   56,  118,  119,  120,   57,   17,   72,  244,
   18,   59,   60,  109,  110,  141,  172,    5,   20,   21,
  238,   42,    6,    8,  166,   23,   95,   24,   25,   26,
   27,   28,   97,  145,  146,  104,   29,   30,   31,   32,
   33,   34,   35,   36,   89,   90,  247,   68,   69,  147,
  148,  149,  150,   99,   37,  190,  191,  192,  193,   83,
  177,   38,  173,  174,  175,   61,   62,   63,   39,  199,
  200,
};
final static short yysindex[] = {                         3,
   28,    0,    0,    0,  -35,  -76,    0, -204,  -66,  -15,
  478,  498, -163,  508,  -40, -209,   47,  399,  -46,    0,
    0,   92,  -60,    0,    0,    0,    0,   53,    0,    0,
    0,    0,    0,    0,    0,    0, -158,   47,  106,    0,
    0,    0,    7,    0, -135,  533,    0,    0,   62, -131,
    0,    0,  517,  384,   31,    0,  355, -123,    0,    0,
  -44,  -41,    0,    0,  121,  414,   31,  -24,   94,    0,
  446,  337, -128,    0,  526,  529,   22,    0,    0,   30,
 -119,  -33,   29,  160, -112,    0,  540, -123,    5,   52,
    0,    0,    0,    0,   70,    0,    0,  -20,    0,   35,
   -2,    0,  110,  103,   70,    0,    0,   96,   13,    0,
   31,    0,    0,  112,    0,    0,    0,   31,    0,  359,
    0,    0,  243,    0,    0,    0,    0,    0,    0,    0,
   10,    0,    0,    0,    0,    0,    0,    0,   31,  359,
  533,  117,   -2,    0,    0,    0,    0,    0, -117, -113,
    0,    0,    0,    0,  -98,    0,    0,    0,  -97,    5,
  108,   -6,    0,    0,   40,   44,  129,    0,    0, -211,
    0, -208,   33,    0,    0,   24,    0,    0,  504,  134,
    0,    0,  -38,    0,    5,   65,    0,  132,  132,  -37,
   21,  -90,    0,   36,    0,    0,  131,   99,   43,   49,
  110,  137,    0,  -70,  -69,   76,  -73,  533,    0,    0,
  347,    0,   31,    0,    0,    0,    0,   31,  384,    0,
  136,  -45,  130,    0,    0,  145,    0,   56,    0,    0,
    0,    0,    0,  -62, -211,    0,    0,   81,    0,    0,
    0,    0,    5,   -1,    0,  144,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -65,  -48,    0,
  168,    0,    0,    0,    0,  -43,   45,    0,    0,    0,
    0,    0,    0,    0,   60,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    0,    5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        19,
   -9,    0,    0,    0,    2,  211,    0,    0,    0,  390,
   72,    0,  294,    0,   69,    0,    0,    0,    0,    0,
    0,    4,   51,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   69,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -11,  155,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  436, -188,    0,    0,
  -23,    0,    0,    0,    0,    0,    0,    0,    0,   69,
    0, -201,    0,    0,    0,    0,    0,   67,    0,    0,
    0,    0,    0,    0,   69,    0,    0,   69,    0,    0,
   69,    0,   89, -230,   69,    0,    0,   48,    0,    0,
  426,    0,    0,    0,    0,    0,    0,  270,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  448,    0,
    0,   95,   69,    0,    0,    0,    0,    0,   20,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,  352,  368,    0,    0,    0, -201,
    0,    0,    0,    0,    0,   69,    0,    0,    0,   73,
    0,    0,    0,    0,    0,   69,    0,    0,   98,    0,
    0,    0,    0,   69,    0,    0, -230,    0,    0,    0,
  120,    0,    0,    0,    0,   69,    0,    0,    0,    0,
    0,    0,  380,    0,    0,    0,    0,  470,   14,    0,
   69,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   54,   -8,    0,    0,   69,    0,    0,
    0,    0,    0,    0,    0,   37,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  727,  669,  -17,  319,    0,  320,  -21,    0,    0,  165,
  739,  435,  360,    0,  179,    0,    0,  401,   23,  409,
  501,    0,    0,    0,    0,    0,    0,  -13,  709,    0,
    0,    0,    0,  -74,  383,  -78,    0,    0,    0,    0,
    0,    0,    0,    0,  329,    0,    0,  -88,  365,    0,
    0,    0,  289,    0,    0,    0,    0,    0,  253,  410,
  353,    0,    0, -160,    0,    0,    0,  403,    0,  -77,
  -72,
};
final static int YYTABLESIZE=958;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   31,   18,    2,    3,   16,  243,   50,  171,   93,  232,
  170,   19,   92,  276,  127,   16,  145,  129,    4,   16,
  242,  252,  197,  189,   46,  193,  204,    7,   40,  180,
   45,  205,  153,  123,  228,  153,  116,   16,  117,  124,
   31,  129,  286,   50,  168,  123,   15,  180,  233,   50,
  217,   26,    3,  209,  107,  158,  208,  285,   44,  169,
   81,  234,   94,   16,  221,  106,   93,  220,  158,   16,
  286,   50,   52,  236,  282,   16,  235,   51,  126,  254,
  126,   73,   78,  129,   79,  297,   82,   15,  170,  123,
   15,  170,   70,   71,  156,   85,  211,  156,  143,  249,
  250,  263,  143,   52,   16,  215,   50,  265,   51,   16,
   71,   96,   86,   98,  280,   16,  101,  211,  295,  258,
   16,  107,  196,  266,  259,   31,  112,   71,  267,  105,
  178,   16,  125,  101,  151,  160,   26,  167,  116,  260,
  117,    4,  202,   50,  180,   16,  164,  193,  237,  198,
  142,  176,  210,  128,  165,  223,   16,  194,  224,  225,
  256,  130,   52,   49,  229,   50,  227,   51,  230,  231,
  202,   65,   93,   52,   49,   16,   50,  260,   51,   45,
  261,   50,  188,  271,  261,  268,  269,  288,  277,  248,
  289,  123,   93,  274,  281,   77,  123,   77,   77,   77,
  270,   52,   49,  278,   50,  283,   51,  290,  291,   91,
   18,  126,  292,   77,  128,   77,   10,  241,  251,   47,
    9,   10,  168,  275,   93,   11,   12,  201,  101,   13,
   11,   12,   10,   14,   13,  187,   10,  169,   14,  143,
  101,   11,   12,  142,  180,   11,   12,    6,  158,   14,
   10,  115,  188,   14,   10,  257,   47,   31,    1,    3,
  257,  158,   47,   11,   12,  216,   31,   31,   26,  107,
   31,   14,   31,  129,   31,  129,  253,   26,   26,    9,
   10,   26,  296,   26,   47,   26,   10,   50,  126,   11,
   12,    9,   10,   13,   73,   11,   12,   14,  262,   13,
  293,   11,   12,   14,  264,   13,   15,   15,  180,   14,
   80,  279,   80,   80,   80,  294,   15,   15,   10,   47,
   15,   10,   15,   71,   15,    9,   10,  178,   80,  101,
   11,   12,   10,  146,   13,   11,   12,   10,   14,   13,
  129,   11,   12,   14,  193,   13,   11,   12,   10,   14,
   13,   13,   10,  115,   14,   10,   47,   11,   12,   10,
  207,   13,   10,  128,   19,   14,  129,   14,   11,   12,
  137,   11,   12,   10,   19,  193,   14,   10,   47,   14,
  159,   19,   11,   12,  139,  140,  272,   10,   10,   47,
   14,   50,   10,   10,   47,  158,   11,   12,   19,   50,
   52,   11,   12,   50,   14,   51,   81,  284,   43,   14,
   77,   77,   77,  122,   41,  178,   10,   47,  185,   77,
   78,  103,   78,   78,   78,  122,  116,   19,  117,  131,
  101,  101,  101,  101,  101,  122,  101,  222,   78,   19,
   52,   49,   85,   50,  255,   51,  122,  100,  101,  101,
  101,  101,  195,    0,   19,  114,  116,   19,  117,   86,
   19,    0,    0,   19,   19,    0,   81,  122,   81,   81,
   81,    0,    0,  138,  136,  137,  110,    0,   77,    0,
   77,    0,    0,    0,   81,   81,   81,   81,  108,  155,
   80,   45,   80,    0,    0,   77,   77,   77,  214,   10,
   47,   19,   19,    0,  154,   22,    0,   80,   80,   80,
  109,    0,   78,    0,   78,   80,    0,   53,    0,   52,
   49,  122,   50,  183,   51,   80,   80,   80,    0,   78,
   78,   78,    0,    0,   80,   19,    0,   65,  122,   52,
   49,    0,   50,    0,   51,   19,  116,   75,  117,   52,
   49,    0,   50,   19,   51,    0,   19,  113,   52,   49,
    0,   50,  240,   51,    0,   19,  161,   52,   49,    0,
   50,  116,   51,  117,   52,   49,    0,   50,  122,   51,
   19,    0,  116,    0,  117,    0,    0,  163,    0,    0,
    0,    0,  156,  157,  226,  186,    0,   19,  182,    0,
    0,    0,  273,   10,   47,  206,    0,    0,    0,    0,
    0,   10,   47,    0,  212,   10,   47,  245,    0,  246,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   78,   78,   78,    0,    0,
   10,  115,    0,    0,   78,  101,  101,  101,    0,  101,
  101,  101,  101,  101,  101,   10,   47,    0,    0,    0,
    0,    0,   84,    0,    0,    0,    0,    0,    0,    0,
   10,  115,    0,  132,  133,  134,  135,  245,  287,    0,
   67,   81,   81,   81,    0,   81,   81,   81,   81,    0,
   81,  110,   77,   77,    0,   77,   77,   77,   77,    0,
    0,  152,  153,  108,   80,   80,    0,   80,   80,   80,
   80,    0,    0,    0,    0,    0,    0,  111,  287,    0,
  298,    0,    0,    0,    0,  109,   78,   78,    0,   78,
   78,   78,   78,   67,   10,   47,   48,   54,    0,    0,
   76,    0,    0,    0,   87,    0,    0,  102,    0,   58,
   58,   73,   58,   64,   10,   47,   88,    0,    0,  239,
   10,  115,    0,   74,   10,   47,    0,    0,    0,    0,
    0,    0,  108,   10,   47,   48,  144,    0,    0,   54,
    0,    0,   10,   47,   58,   10,  115,   58,  213,   10,
   47,   58,  121,    0,    0,   58,   10,  115,    0,    0,
    0,  162,    0,   58,  121,    0,  144,    0,  218,  102,
  179,    0,  203,   58,  121,    0,    0,    0,    0,    0,
    0,    0,   58,  181,    0,  121,    0,    0,  184,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  121,    0,    0,    0,
  144,  102,    0,    0,    0,    0,   67,   67,   58,    0,
    0,   58,    0,    0,    0,    0,    0,  219,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  121,    0,    0,    0,    0,  203,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  121,    0,    0,
    0,    0,    0,    0,    0,    0,   58,   58,    0,  203,
    0,    0,    0,    0,  108,    0,   58,    0,    0,    0,
   58,    0,    0,    0,    0,    0,   58,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,  121,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,    0,    0,   40,   44,   45,   41,   22,  170,
   44,    0,   59,   59,   59,   40,   40,   59,    0,   40,
   59,   59,  101,   98,   40,  256,  104,    0,    6,   41,
   46,  104,   41,   55,   41,   44,   43,   40,   45,   57,
   40,  272,   44,   45,  256,   67,  123,   59,  257,   45,
   41,   40,  257,   41,   41,  257,   44,   59,  125,  271,
  270,  270,  123,   40,  143,   43,   80,  142,  270,   40,
   44,   45,   42,   41,  235,   40,   44,   47,   59,   59,
  269,   45,  123,  272,  125,   59,   40,  123,   41,  111,
   40,   44,  256,  257,   41,   44,  118,   44,  123,  188,
  189,   59,  123,   42,   40,  123,   45,   59,   47,   40,
   44,   59,   61,  272,   59,   40,   44,  139,   59,  197,
   40,  257,  125,  201,  197,  125,  258,   61,  201,  123,
   59,   40,  256,   61,   41,  264,  125,  257,   43,   41,
   45,  123,   40,   45,  257,   40,  125,   59,  125,   40,
  268,  123,   41,   59,  125,  269,   40,  123,  257,  257,
  125,   41,   42,   43,  125,   45,   59,   47,  125,   41,
   40,   40,  186,   42,   43,   40,   45,   41,   47,   46,
  198,   45,  273,  257,  202,  256,  256,   44,   59,  125,
  256,  213,  206,  211,  257,   41,  218,   43,   44,   45,
  125,   42,   43,   59,   45,  125,   47,  256,   41,  256,
    0,  256,  256,   59,  256,  256,  257,  256,  256,  258,
  256,  257,  256,  269,  238,  266,  267,  125,  123,  270,
  266,  267,  257,  274,  270,  256,  257,  271,  274,  123,
  264,  266,  267,  268,  256,  266,  267,  257,  257,  274,
  257,  258,  273,  274,  257,  125,  258,  257,  256,  257,
  125,  270,  258,  266,  267,  256,  266,  267,  257,  256,
  270,  274,  272,  272,  274,  272,  256,  266,  267,  256,
  257,  270,  256,  272,  258,  274,  257,   45,  269,  266,
  267,  256,  257,  270,  258,  266,  267,  274,  256,  270,
  256,  266,  267,  274,  256,  270,  256,  257,  257,  274,
   41,  256,   43,   44,   45,  256,  266,  267,  257,  258,
  270,  257,  272,  257,  274,  256,  257,  256,   59,  257,
  266,  267,  257,   40,  270,  266,  267,  257,  274,  270,
  272,  266,  267,  274,  256,  270,  266,  267,  257,  274,
  270,    0,  257,  258,  274,  257,  258,  266,  267,  257,
  265,  270,  257,  269,    5,  274,  272,    0,  266,  267,
  273,  266,  267,  257,   15,  256,  274,  257,  258,  274,
   44,   22,  266,  267,   66,   66,  208,  257,  257,  258,
  274,   45,  257,  257,  258,   59,  266,  267,   39,   45,
   42,  266,  267,   45,  274,   47,  270,  243,    8,  274,
  256,  257,  258,   54,    6,  256,  257,  258,   90,  265,
   41,   39,   43,   44,   45,   66,   43,   68,   45,   65,
   41,   42,   43,   44,   45,   76,   47,  149,   59,   80,
   42,   43,   44,   45,  192,   47,   87,   38,   59,   60,
   61,   62,  100,   -1,   95,   53,   43,   98,   45,   61,
  101,   -1,   -1,  104,  105,   -1,   41,  108,   43,   44,
   45,   -1,   -1,   60,   61,   62,   41,   -1,   43,   -1,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   41,   44,
   43,   46,   45,   -1,   -1,   60,   61,   62,  256,  257,
  258,  142,  143,   -1,   59,    5,   -1,   60,   61,   62,
   41,   -1,   43,   -1,   45,   15,   -1,   40,   -1,   42,
   43,  162,   45,   89,   47,  256,  257,  258,   -1,   60,
   61,   62,   -1,   -1,  265,  176,   -1,   40,  179,   42,
   43,   -1,   45,   -1,   47,  186,   43,   40,   45,   42,
   43,   -1,   45,  194,   47,   -1,  197,   41,   42,   43,
   -1,   45,   59,   47,   -1,  206,   41,   42,   43,   -1,
   45,   43,   47,   45,   42,   43,   -1,   45,  219,   47,
  221,   -1,   43,   -1,   45,   -1,   -1,   59,   -1,   -1,
   -1,   -1,  256,  257,  160,   95,   -1,  238,   59,   -1,
   -1,   -1,  256,  257,  258,  105,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,  256,  257,  258,  183,   -1,  185,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,
  257,  258,   -1,   -1,  265,  256,  257,  258,   -1,  260,
  261,  262,  263,  264,  265,  257,  258,   -1,   -1,   -1,
   -1,   -1,  264,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,  260,  261,  262,  263,  243,  244,   -1,
   12,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
   -1,  256,  257,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,   -1,   -1,   -1,   -1,   -1,   49,  284,   -1,
  286,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,   65,  257,  258,  259,   11,   -1,   -1,
   14,   -1,   -1,   -1,   18,   -1,   -1,   39,   -1,   11,
   12,   13,   14,  256,  257,  258,   18,   -1,   -1,  256,
  257,  258,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   46,  257,  258,  259,   68,   -1,   -1,   53,
   -1,   -1,  257,  258,   46,  257,  258,   49,  120,  257,
  258,   53,   54,   -1,   -1,   57,  257,  258,   -1,   -1,
   -1,   75,   -1,   65,   66,   -1,   98,   -1,  140,  101,
   84,   -1,  104,   75,   76,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   84,   85,   -1,   87,   -1,   -1,   90,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  108,   -1,   -1,   -1,
  142,  143,   -1,   -1,   -1,   -1,  188,  189,  120,   -1,
   -1,  123,   -1,   -1,   -1,   -1,   -1,  141,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  140,  141,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  162,   -1,   -1,   -1,   -1,  197,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  179,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  188,  189,   -1,  221,
   -1,   -1,   -1,   -1,  208,   -1,  198,   -1,   -1,   -1,
  202,   -1,   -1,   -1,   -1,   -1,  208,   -1,   -1,  211,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  219,
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
"programa : nombre_programa cuerpo_programa",
"programa : nombre_programa cuerpo_programa_recuperacion",
"programa : nombre_programa conjunto_sentencias",
"$$1 :",
"programa : $$1 programa_sin_nombre",
"$$2 :",
"programa : error $$2 nombre_programa cuerpo_programa",
"programa : error EOF",
"programa : EOF",
"nombre_programa : ID",
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
"sentencia_control : iteracion",
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
"asignacion_multiple : variable asignacion_par constante lista_constantes ';'",
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
"condicion : '(' ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' cuerpo_condicion error",
"cuerpo_condicion : expresion comparador expresion",
"cuerpo_condicion : expresion termino_simple",
"cuerpo_condicion : expresion operador_suma termino",
"cuerpo_condicion : termino",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : IF condicion cuerpo_if",
"if : IF condicion cuerpo_if_error",
"if : IF error",
"cuerpo_if : cuerpo_then rama_else ENDIF ';'",
"cuerpo_if_error : cuerpo_then rama_else ENDIF error",
"cuerpo_if_error : cuerpo_then rama_else ';'",
"cuerpo_if_error : rama_else ENDIF ';'",
"cuerpo_then : cuerpo_ejecutable",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"rama_else : ELSE",
"$$4 :",
"iteracion : $$4 do_while",
"do_while : DO cuerpo_iteracion ';'",
"do_while : DO cuerpo_iteracion_recuperacion ';'",
"do_while : DO cuerpo_iteracion error",
"do_while : DO cuerpo_iteracion_recuperacion error",
"do_while : DO error",
"cuerpo_iteracion : cuerpo_do fin_cuerpo_iteracion",
"cuerpo_do : cuerpo_ejecutable",
"cuerpo_iteracion_recuperacion : fin_cuerpo_iteracion",
"cuerpo_iteracion_recuperacion : cuerpo_ejecutable condicion",
"fin_cuerpo_iteracion : WHILE condicion",
"declaracion_funcion : inicio_funcion conjunto_parametros cuerpo_funcion",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' '}'",
"declaracion_funcion : inicio_funcion_sin_nombre conjunto_parametros cuerpo_funcion",
"declaracion_funcion : inicio_funcion_sin_nombre conjunto_parametros '{' '}'",
"inicio_funcion : UINT ID",
"inicio_funcion_sin_nombre : UINT",
"cuerpo_funcion : '{' conjunto_sentencias '}'",
"conjunto_parametros : '(' lista_parametros ')'",
"conjunto_parametros : '(' ')'",
"lista_parametros : parametro_formal",
"lista_parametros : lista_parametros ',' parametro_formal",
"lista_parametros : parametro_vacio",
"parametro_vacio : lista_parametros ','",
"parametro_vacio : ',' parametro_formal",
"parametro_formal : semantica_pasaje UINT ID",
"parametro_formal : semantica_pasaje UINT",
"parametro_formal : semantica_pasaje ID",
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

//#line 1035 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.

// Contadores de errores y warning detectados.

// Generacion de código
private final Lexer lexer;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private MessageCollector errorCollector, warningCollector;
private boolean errorState;

public Parser(Lexer lexer, SymbolTable symbolTable,
                MessageCollector errorCollector, MessageCollector warningCollector) {
    
    this.lexer = lexer;
    this.symbolTable = symbolTable;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    
    this.scopeStack = new ScopeStack();
    this.reversePolish = ReversePolish.getInstance();

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

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

public boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 814 "Parser.java"
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
//#line 72 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 79 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 4:
//#line 82 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 6:
//#line 84 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); }
break;
case 8:
//#line 87 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 9:
//#line 90 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 10:
//#line 97 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); }
break;
case 14:
//#line 118 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); }
break;
case 15:
//#line 121 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); }
break;
case 17:
//#line 124 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 18:
//#line 126 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 19:
//#line 128 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); }
break;
case 26:
//#line 154 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 36:
//#line 195 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 39:
//#line 213 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 46:
//#line 224 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 49:
//#line 241 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 50:
//#line 244 "gramatica.y"
{
            notifyDetection("Declaración de variable.");
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(1).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(1).sval,scopeStack.asText());
        }
break;
case 51:
//#line 254 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 52:
//#line 258 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 53:
//#line 262 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 266 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 56:
//#line 276 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 57:
//#line 281 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 58:
//#line 288 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 59:
//#line 302 "gramatica.y"
{ 
            notifyDetection("Asignación simple."); 
            this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);
            
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(2).sval);
        }
break;
case 60:
//#line 314 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 61:
//#line 317 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 62:
//#line 320 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 63:
//#line 335 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(1).sval);

            reversePolish.rearrangePairs();

            notifyDetection("Asignación múltiple."); 
        }
break;
case 64:
//#line 344 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(5).sval);
            reversePolish.addPolish(val_peek(3).sval);

            reversePolish.rearrangePairs();

            notifyWarning(String.format("Se descartarán las constantes posteriores a %s",val_peek(3).sval)); /*TP3*/
            notifyDetection("Asignación múltiple.");
        }
break;
case 65:
//#line 357 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 359 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 67:
//#line 361 "gramatica.y"
{ notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval)); }
break;
case 70:
//#line 375 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 71:
//#line 380 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 387 "gramatica.y"
{ reversePolish.addPolish(val_peek(1).sval); }
break;
case 73:
//#line 392 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 75:
//#line 400 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 405 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 78:
//#line 419 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 79:
//#line 427 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 80:
//#line 431 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 81:
//#line 438 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 82:
//#line 448 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 83:
//#line 450 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 84:
//#line 457 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 86:
//#line 466 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 473 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 88:
//#line 480 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 90:
//#line 489 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 91:
//#line 496 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 498 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 93:
//#line 507 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 94:
//#line 511 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 96:
//#line 522 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 97:
//#line 526 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 100:
//#line 537 "gramatica.y"
{/* 
            $$ = "-" + $2;

            notifyDetection(String.format("Constante negativa: %s.",$$));

            if(isUint($$)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                $$ = null;
            } 

            modificarSymbolTable($$,$2);
        */
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 101:
//#line 564 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.asText()+":"+val_peek(0).sval)) { /*Si entra por aca, la variable debe ser local*/
                notifyError(String.format("Variable %s no declarada.",val_peek(0).sval));
            }
        }
break;
case 102:
//#line 570 "gramatica.y"
{ 
            if (!this.scopeStack.isReacheable(val_peek(2).sval)) 
                notifyError(String.format("Variable %s no declarada (no visible).",val_peek(0).sval));
            else {
                if (!this.symbolTable.entryExists(this.scopeStack.getScopeRoad(val_peek(2).sval)+val_peek(0).sval))
                    notifyError(String.format("Variable %s no declarada en el ámbito %s.",val_peek(0).sval,val_peek(2).sval));
            }

            yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; 
        }
break;
case 103:
//#line 588 "gramatica.y"
{ 
            /*if(!errorState)*/
            reversePolish.addFalseBifurcation();
            notifyDetection("Condición."); 
        }
break;
case 104:
//#line 597 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 105:
//#line 601 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 603 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 107:
//#line 610 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 108:
//#line 617 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 109:
//#line 619 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 110:
//#line 621 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 111:
//#line 628 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 112:
//#line 632 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 117:
//#line 643 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 118:
//#line 671 "gramatica.y"
{ 
            reversePolish.completeSelection();
            notifyDetection("Sentencia IF."); 
        }
break;
case 120:
//#line 679 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 122:
//#line 688 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 123:
//#line 690 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 124:
//#line 692 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 125:
//#line 697 "gramatica.y"
{ reversePolish.addInconditionalBifurcation(); }
break;
case 128:
//#line 709 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 129:
//#line 716 "gramatica.y"
{ reversePolish.registerDoBody(); }
break;
case 131:
//#line 722 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 133:
//#line 728 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 134:
//#line 730 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 135:
//#line 732 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 137:
//#line 743 "gramatica.y"
{}
break;
case 138:
//#line 750 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 139:
//#line 752 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 140:
//#line 759 "gramatica.y"
{ reversePolish.addTrueBifurcation(); }
break;
case 141:
//#line 768 "gramatica.y"
{
            notifyDetection("Declaración de función.");
            this.symbolTable.setType(val_peek(2).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(2).sval, SymbolCategory.FUNCTION);
            this.scopeStack.pop();    
        }
break;
case 142:
//#line 778 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 143:
//#line 781 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 144:
//#line 784 "gramatica.y"
{
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.scopeStack.pop();
        }
break;
case 145:
//#line 796 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); yyval.sval = val_peek(0).sval; }
break;
case 146:
//#line 803 "gramatica.y"
{
            this.scopeStack.push(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
        }
break;
case 149:
//#line 823 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 152:
//#line 835 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 155:
//#line 849 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PARAMETER);
            this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            /*hay que guardar la semantica en la tabla*/

        }
break;
case 156:
//#line 860 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 157:
//#line 862 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 158:
//#line 869 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 159:
//#line 871 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 160:
//#line 876 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 161:
//#line 885 "gramatica.y"
{ 
            reversePolish.addPolish("return");
            notifyDetection("Sentencia RETURN."); 
        }
break;
case 162:
//#line 893 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 163:
//#line 895 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 164:
//#line 897 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 165:
//#line 899 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 166:
//#line 908 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 168:
//#line 918 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 169:
//#line 925 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 170:
//#line 930 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 171:
//#line 939 "gramatica.y"
{ 
            reversePolish.addPolish("print");
            notifyDetection("Sentencia 'print'."); 
        }
break;
case 172:
//#line 947 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 174:
//#line 950 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 176:
//#line 963 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 177:
//#line 966 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 178:
//#line 968 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 181:
//#line 984 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 182:
//#line 989 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 184:
//#line 992 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 185:
//#line 995 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 186:
//#line 997 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 187:
//#line 999 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 188:
//#line 1001 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 189:
//#line 1003 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 190:
//#line 1005 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 192:
//#line 1018 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 193:
//#line 1021 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1620 "Parser.java"
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
