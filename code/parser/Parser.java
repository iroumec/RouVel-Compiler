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
    0,    0,    0,   22,    0,   23,    0,    0,    0,   17,
   21,   21,   18,   19,   26,   19,   19,   19,   19,   25,
   25,   24,   24,   20,   20,   20,   27,   27,   29,   29,
   32,   32,   33,   33,   34,   34,   35,   35,   28,   28,
   28,   28,   28,   28,   28,   28,   38,   38,   30,   30,
   30,   30,   30,   30,    9,    9,    9,    9,   36,   36,
   36,   36,   37,   37,   37,   37,   37,   44,   44,   45,
   45,   46,   46,   10,   10,   10,    1,    1,    1,    1,
    1,    6,    6,    2,    2,    2,    2,    4,    4,    4,
    7,    7,    3,    3,    3,    5,    5,    5,   12,   12,
   11,   11,   47,   47,   47,   47,   48,   48,   48,   48,
   16,   16,   16,   16,   16,   16,   16,   42,   42,   42,
   49,   50,   50,   50,   51,   52,   52,   52,   54,   43,
   53,   53,   53,   53,   53,   55,   57,   56,   56,   58,
   31,   31,   31,   31,    8,   61,   60,   59,   59,   62,
   62,   62,   64,   64,   63,   63,   63,   65,   65,   65,
   39,   39,   39,   39,   39,   13,   14,   14,   15,   15,
   40,   40,   40,   40,   66,   67,   67,   67,   68,   68,
   41,   41,   41,   41,   41,   41,   41,   41,   41,   41,
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
  149,    0,  150,  152,    0,    0,  141,   62,    0,    0,
   70,   61,    0,   71,    0,    0,  135,    0,    0,    0,
    0,    0,  138,    0,  143,   36,    0,    0,    0,    0,
    0,    0,   38,    0,    0,    0,    0,    0,  166,  175,
    0,   79,    0,   86,   84,  106,  103,    0,    0,  127,
    0,    0,    0,   55,   56,    0,  163,    0,   22,   23,
  194,  154,    0,  148,    0,  157,  142,    0,   60,   59,
   65,   63,    0,    0,   74,    0,   68,   16,  140,  139,
  133,  131,  134,  132,  136,  144,   35,    0,    0,  192,
    0,  182,  181,  184,  183,    0,    0,  186,  189,   13,
  169,  168,   90,   88,    0,  123,  124,   53,  162,  161,
  151,  155,  147,    0,   67,    0,   76,   72,  185,  188,
  191,  187,  190,  122,  121,   66,   64,   75,
};
final static short yydgoto[] = {                          4,
   66,   55,   56,  118,  119,  120,   57,   17,   72,  244,
   18,   59,   60,  109,  110,  141,    5,   20,   21,  238,
   42,    6,    8,  166,   23,   95,   24,   25,   26,   27,
   28,   97,  145,  146,  104,   29,   30,   31,   32,   33,
   34,   35,   36,   89,   90,  247,   68,   69,  147,  148,
  149,  150,   99,   37,  190,  191,  192,  193,   83,  177,
   38,  172,  173,  174,  175,   61,   62,   63,   39,  199,
  200,
};
final static short yysindex[] = {                         3,
   61,    0,    0,    0,  -35,  -96,    0, -179,  -36,   13,
  409,  502, -163,  570,  -40, -183,   66,  269,  -46,    0,
    0,   92,   -4,    0,    0,    0,    0,   63,    0,    0,
    0,    0,    0,    0,    0,    0, -161,   66,  106,    0,
    0,    0,    6,    0, -132,  605,    0,    0,   62, -125,
    0,    0,  578,  143,   15,    0,  167, -122,    0,    0,
  -41,  -37,    0,    0,  121,  427,   15,  -24,   94,    0,
  404,  308, -128,    0,  599,  447,   23,    0,    0,   30,
 -119,  -33,   22,  160, -107,    0,  492, -122,   37,   31,
    0,    0,    0,    0,   70,    0,    0,  -20,    0,   29,
   -2,    0,  113,  103,   70,    0,    0,   96,  -16,    0,
   15,    0,    0,  118,    0,    0,    0,   15,    0,  415,
    0,    0,  319,    0,    0,    0,    0,    0,    0,    0,
   10,    0,    0,    0,    0,    0,    0,    0,   15,  415,
  605,  117,   -2,    0,    0,    0,    0,    0, -114, -111,
    0,    0,    0,    0,  -92,    0,    0,    0,  -90,   37,
  111,   -6,    0,    0,   55,   56,  142,    0,    0, -211,
    0,   33,    0,    0, -207,   24,    0,    0,  435,  138,
    0,    0,  -38,    0,   37,   65,    0,  132,  132,   21,
   49,  -86,    0,   36,    0,    0,  131,   99,   68,   72,
  113,  137,    0,  -71,  -65,   76,  -63,  605,    0,    0,
  336,    0,   15,    0,    0,    0,    0,   15,  143,    0,
  136,  -45,  145,    0,    0,  149,    0,  130,    0,    0,
    0,    0, -211,    0, -107,    0,    0,   81,    0,    0,
    0,    0,   37,   -1,    0,  151,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -43,   43,    0,
  207,    0,    0,    0,    0,   45,   78,    0,    0,    0,
    0,    0,    0,    0,  150,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    0,   37,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        19,
  114,    0,    0,    0,    2,  329,    0,    0,    0,  470,
  152,    0,  328,    0,  104,    0,    0,    0,    0,    0,
    0,    4,   51,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  104,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    7,  155,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  507, -174,    0,    0,
  -23,    0,    0,    0,    0,    0,    0,    0,    0,  104,
    0, -201,    0,    0,    0,    0,    0,   52,    0,    0,
    0,    0,    0,    0,  104,    0,    0,  104,    0,    0,
  104,    0,  282, -230,  104,    0,    0,   71,    0,    0,
  438,    0,    0,    0,    0,    0,    0,  378,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  529,    0,
    0,  286,  104,    0,    0,    0,    0,    0,   20,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,  385,  387,    0,    0,    0, -201,
    0,    0,    0,    0,    0,  104,    0,    0,    0,  -12,
    0,    0,    0,    0,    0,  104,    0,    0,  119,    0,
    0,    0,    0,  104,    0,    0, -230,    0,    0,    0,
  140,    0,    0,    0,    0,  104,    0,    0,    0,    0,
    0,    0,  400,    0,    0,    0,    0,  542,   14,    0,
  104,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   -8,    0,   73,    0,    0,  104,    0,    0,
    0,    0,    0,    0,    0,   57,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  668,  690,  -42,  338,    0,  343,  -21,    0,    0,  172,
  420,  537,  360,    0,  219,    0,  421,   41,  424,   25,
    0,    0,    0,    0,    0,    0,  -13,  673,    0,    0,
    0,    0,  -74,  396,  -78,    0,    0,    0,    0,    0,
    0,    0,    0,  349,    0,    0,  -88,  377,    0,    0,
    0,  297,    0,    0,    0,    0,    0,  261,  429,  371,
    0,    0, -160,    0,    0,    0,    0,  422,    0,  -73,
  -50,
};
final static int YYTABLESIZE=894;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   31,   18,    2,    3,   16,  243,   50,  171,   93,  232,
  170,   19,   92,  276,  124,   16,  145,  127,    4,   16,
  242,  129,  197,  189,  209,  193,   15,  208,  101,   22,
  204,  101,  153,  123,  228,  153,  116,   16,  117,   80,
   31,  129,  286,   50,  168,  123,   40,  180,  101,  180,
  217,   26,   46,  205,  107,  158,   52,  285,   45,  169,
    7,   51,  235,   16,  221,  180,   93,  220,  158,   16,
  286,   50,  281,  234,   85,   16,  233,    3,  126,  252,
  215,   50,   78,  106,   79,  297,   81,   15,   44,  123,
   15,   86,   70,   71,  126,   71,  211,  129,  143,  249,
  250,   73,  143,   52,   16,   82,   50,  254,   51,   16,
   98,  170,   71,  156,  170,   16,  156,  211,   94,  186,
   16,   96,  196,  258,  107,   31,  263,  266,  105,  206,
  265,   16,  112,  125,  151,  160,   26,  167,  116,  260,
  117,    4,  202,   50,  176,   16,  259,  164,  237,  180,
  267,  194,  198,  142,  165,  261,   16,  223,  210,  261,
  256,  130,   52,   49,  224,   50,  225,   51,  274,  227,
  202,   65,   93,   52,   49,   16,   50,  260,   51,  229,
  230,   50,  231,   45,  268,  116,  188,  117,  280,  248,
  269,  123,   93,  271,  288,   77,  123,   77,   77,   77,
  270,   52,   49,  277,   50,  283,   51,  278,  295,   91,
  178,   50,  289,   77,  126,   77,   10,  241,  128,   47,
    9,   10,  168,  275,   93,   11,   12,  201,  101,   13,
   11,   12,   10,   14,   13,  187,   10,  169,   14,  143,
  101,   11,   12,  142,  101,   11,   12,  291,  158,   14,
   10,  115,  188,   14,   10,  257,   47,   31,    1,    3,
  257,  158,  180,   11,   12,  216,   31,   31,   26,  107,
   31,   14,   31,  129,   31,  129,  251,   26,   26,    9,
   10,   26,  296,   26,   47,   26,   10,  180,  126,   11,
   12,    9,   10,   13,   47,   11,   12,   14,  290,   13,
  292,   11,   12,   14,  253,   13,   15,   15,   71,   14,
   52,   49,   85,   50,   73,   51,   15,   15,   10,   47,
   15,   10,   15,  262,   15,    9,   10,  264,   18,   86,
   11,   12,   10,  293,   13,   11,   12,   10,   14,   13,
  193,   11,   12,   14,  128,   13,   11,   12,   10,   14,
   13,  159,   10,  115,   14,   10,   47,   11,   12,   10,
  207,   13,   10,   50,   19,   14,  158,  146,   11,   12,
    6,   11,   12,   10,   19,  129,   14,   10,   47,   14,
   50,   19,   11,   12,   13,  279,   14,   10,   10,   47,
   14,  137,   10,   10,   47,  193,   11,   12,   19,   10,
  115,   11,   12,  139,   14,  294,   81,  178,  140,   14,
   77,   77,   77,  122,  284,  178,   10,   47,   80,   77,
   80,   80,   80,   10,   47,  122,  272,   19,   43,   41,
   58,   58,   73,   58,  103,  122,   80,   88,  185,   19,
   78,  131,   78,   78,   78,  222,  122,  155,   53,   45,
   52,   49,  255,   50,   19,   51,   52,   19,   78,   50,
   19,   51,  154,   19,   19,   58,  100,  122,   58,  116,
  195,  117,   58,  121,  114,    0,   58,  116,   81,  117,
   81,   81,   81,    0,   58,  121,  138,  136,  137,  116,
    0,  117,    0,  240,   58,  121,   81,   81,   81,   81,
    0,   19,   19,   58,  181,  163,  121,    0,    0,  184,
  101,  101,  101,  101,  101,    0,  101,    0,    0,    0,
    0,  122,    0,    0,    0,   10,   47,  121,  101,  101,
  101,  101,   84,    0,  116,   19,  117,  193,  122,   58,
    0,   65,   58,   52,   49,   19,   50,  110,   51,   77,
  182,   77,    0,   19,  128,    0,   19,  129,    0,   58,
   58,    0,    0,  156,  157,   19,   77,   77,   77,  108,
    0,   80,    0,   80,  214,   10,   47,    0,  122,    0,
   19,  121,  109,    0,   78,    0,   78,    0,   80,   80,
   80,  273,   10,   47,  236,    0,    0,   19,  121,    0,
    0,   78,   78,   78,    0,    0,    0,   58,   58,   75,
    0,   52,   49,    0,   50,    0,   51,   58,  113,   52,
   49,   58,   50,    0,   51,  183,    0,   58,    0,    0,
   58,    0,    0,   80,   80,   80,    0,    0,  121,  161,
   52,   49,   80,   50,    0,   51,   52,   49,    0,   50,
    0,   51,    0,    0,  282,   78,   78,   78,    0,  152,
  153,    0,    0,    0,   78,   10,   47,   48,    0,    0,
  212,   10,   47,    0,    0,    0,    0,    0,   54,    0,
    0,   76,    0,   10,  115,   87,  132,  133,  134,  135,
  239,   10,  115,   81,   81,   81,  226,   81,   81,   81,
   81,   67,   81,   10,  115,    0,    0,    0,    0,    0,
    0,  102,    0,  108,    0,    0,    0,    0,    0,  245,
   54,  246,    0,    0,    0,  101,  101,  101,    0,  101,
  101,  101,  101,  101,  101,    0,    0,    0,  111,    0,
  144,    0,  162,    0,    0,    0,    0,    0,   10,  115,
    0,  179,    0,    0,   67,    0,    0,   64,   10,   47,
    0,    0,  110,   77,   77,    0,   77,   77,   77,   77,
  144,    0,    0,  102,    0,    0,  203,    0,    0,  245,
  287,    0,    0,    0,  108,   80,   80,    0,   80,   80,
   80,   80,    0,    0,    0,    0,    0,  109,   78,   78,
    0,   78,   78,   78,   78,    0,    0,    0,  219,  213,
    0,    0,    0,    0,  144,  102,    0,    0,    0,    0,
  287,    0,  298,    0,    0,   74,   10,   47,    0,  218,
    0,    0,    0,    0,   10,   47,   48,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   10,   47,    0,    0,    0,
    0,   10,   47,    0,    0,    0,    0,    0,    0,  203,
    0,    0,    0,    0,    0,  108,    0,   67,   67,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  203,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,    0,    0,   40,   44,   45,   41,   22,  170,
   44,    0,   59,   59,   57,   40,   40,   59,    0,   40,
   59,   59,  101,   98,   41,  256,  123,   44,   41,    5,
  104,   44,   41,   55,   41,   44,   43,   40,   45,   15,
   40,  272,   44,   45,  256,   67,    6,   41,   61,  257,
   41,   40,   40,  104,   41,  257,   42,   59,   46,  271,
    0,   47,  270,   40,  143,   59,   80,  142,  270,   40,
   44,   45,  233,   41,   44,   40,   44,  257,   59,   59,
  123,   45,  123,   43,  125,   59,  270,  123,  125,  111,
   40,   61,  256,  257,  269,   44,  118,  272,  123,  188,
  189,   45,  123,   42,   40,   40,   45,   59,   47,   40,
  272,   41,   61,   41,   44,   40,   44,  139,  123,   95,
   40,   59,  125,  197,  257,  125,   59,  201,  123,  105,
   59,   40,  258,  256,   41,  264,  125,  257,   43,   41,
   45,  123,   40,   45,  123,   40,  197,  125,  125,  257,
  201,  123,   40,  268,  125,  198,   40,  269,   41,  202,
  125,   41,   42,   43,  257,   45,  257,   47,  211,   59,
   40,   40,  186,   42,   43,   40,   45,   41,   47,  125,
  125,   45,   41,   46,  256,   43,  273,   45,   59,  125,
  256,  213,  206,  257,   44,   41,  218,   43,   44,   45,
  125,   42,   43,   59,   45,  125,   47,   59,   59,  256,
   59,   45,  256,   59,  256,  256,  257,  256,  256,  258,
  256,  257,  256,  269,  238,  266,  267,  125,  123,  270,
  266,  267,  257,  274,  270,  256,  257,  271,  274,  123,
  264,  266,  267,  268,  257,  266,  267,   41,  257,  274,
  257,  258,  273,  274,  257,  125,  258,  257,  256,  257,
  125,  270,  256,  266,  267,  256,  266,  267,  257,  256,
  270,  274,  272,  272,  274,  272,  256,  266,  267,  256,
  257,  270,  256,  272,  258,  274,  257,  257,  269,  266,
  267,  256,  257,  270,  258,  266,  267,  274,  256,  270,
  256,  266,  267,  274,  256,  270,  256,  257,  257,  274,
   42,   43,   44,   45,  258,   47,  266,  267,  257,  258,
  270,  257,  272,  256,  274,  256,  257,  256,    0,   61,
  266,  267,  257,  256,  270,  266,  267,  257,  274,  270,
   59,  266,  267,  274,   59,  270,  266,  267,  257,  274,
  270,   44,  257,  258,  274,  257,  258,  266,  267,  257,
  265,  270,  257,   45,    5,  274,   59,   40,  266,  267,
  257,  266,  267,  257,   15,  272,  274,  257,  258,  274,
   45,   22,  266,  267,    0,  256,    0,  257,  257,  258,
  274,  273,  257,  257,  258,  256,  266,  267,   39,  257,
  258,  266,  267,   66,  274,  256,  270,  256,   66,  274,
  256,  257,  258,   54,  243,  256,  257,  258,   41,  265,
   43,   44,   45,  257,  258,   66,  208,   68,    8,    6,
   11,   12,   13,   14,   39,   76,   59,   18,   90,   80,
   41,   65,   43,   44,   45,  149,   87,   44,   40,   46,
   42,   43,  192,   45,   95,   47,   42,   98,   59,   45,
  101,   47,   59,  104,  105,   46,   38,  108,   49,   43,
  100,   45,   53,   54,   53,   -1,   57,   43,   41,   45,
   43,   44,   45,   -1,   65,   66,   60,   61,   62,   43,
   -1,   45,   -1,   59,   75,   76,   59,   60,   61,   62,
   -1,  142,  143,   84,   85,   59,   87,   -1,   -1,   90,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   -1,  162,   -1,   -1,   -1,  257,  258,  108,   59,   60,
   61,   62,  264,   -1,   43,  176,   45,  256,  179,  120,
   -1,   40,  123,   42,   43,  186,   45,   41,   47,   43,
   59,   45,   -1,  194,  269,   -1,  197,  272,   -1,  140,
  141,   -1,   -1,  256,  257,  206,   60,   61,   62,   41,
   -1,   43,   -1,   45,  256,  257,  258,   -1,  219,   -1,
  221,  162,   41,   -1,   43,   -1,   45,   -1,   60,   61,
   62,  256,  257,  258,  175,   -1,   -1,  238,  179,   -1,
   -1,   60,   61,   62,   -1,   -1,   -1,  188,  189,   40,
   -1,   42,   43,   -1,   45,   -1,   47,  198,   41,   42,
   43,  202,   45,   -1,   47,   89,   -1,  208,   -1,   -1,
  211,   -1,   -1,  256,  257,  258,   -1,   -1,  219,   41,
   42,   43,  265,   45,   -1,   47,   42,   43,   -1,   45,
   -1,   47,   -1,   -1,  235,  256,  257,  258,   -1,  256,
  257,   -1,   -1,   -1,  265,  257,  258,  259,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   11,   -1,
   -1,   14,   -1,  257,  258,   18,  260,  261,  262,  263,
  256,  257,  258,  256,  257,  258,  160,  260,  261,  262,
  263,   12,  265,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   39,   -1,   46,   -1,   -1,   -1,   -1,   -1,  183,
   53,  185,   -1,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,  264,  265,   -1,   -1,   -1,   49,   -1,
   68,   -1,   75,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,   84,   -1,   -1,   65,   -1,   -1,  256,  257,  258,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   98,   -1,   -1,  101,   -1,   -1,  104,   -1,   -1,  243,
  244,   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,   -1,   -1,  141,  120,
   -1,   -1,   -1,   -1,  142,  143,   -1,   -1,   -1,   -1,
  284,   -1,  286,   -1,   -1,  256,  257,  258,   -1,  140,
   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,  197,
   -1,   -1,   -1,   -1,   -1,  208,   -1,  188,  189,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  221,
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

//#line 995 "gramatica.y"

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
//#line 802 "Parser.java"
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
//#line 240 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 50:
//#line 243 "gramatica.y"
{
            notifyDetection("Declaración de variable.");
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(1).sval, SymbolCategory.VARIABLE);
        }
break;
case 51:
//#line 252 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 52:
//#line 256 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 53:
//#line 260 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 54:
//#line 264 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 56:
//#line 274 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 57:
//#line 279 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 58:
//#line 286 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            { yyval.sval = val_peek(0).sval; }
        }
break;
case 59:
//#line 300 "gramatica.y"
{ 
            notifyDetection("Asignación simple."); 
            this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);
            
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(2).sval);
        }
break;
case 60:
//#line 312 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 61:
//#line 315 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 62:
//#line 318 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 63:
//#line 328 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 330 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 335 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 337 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 67:
//#line 339 "gramatica.y"
{ notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval)); }
break;
case 70:
//#line 353 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 71:
//#line 358 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 365 "gramatica.y"
{ reversePolish.addPolish(val_peek(1).sval); }
break;
case 73:
//#line 370 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 74:
//#line 377 "gramatica.y"
{ reversePolish.addPolish(val_peek(0).sval); }
break;
case 75:
//#line 379 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 384 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 78:
//#line 398 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 79:
//#line 406 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 80:
//#line 410 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 81:
//#line 417 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 82:
//#line 427 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 83:
//#line 429 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 84:
//#line 436 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 86:
//#line 445 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 452 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 88:
//#line 459 "gramatica.y"
{   
            reversePolish.addPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 90:
//#line 468 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 91:
//#line 475 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 477 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 93:
//#line 486 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 94:
//#line 490 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 96:
//#line 501 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 97:
//#line 505 "gramatica.y"
{
            reversePolish.addPolish(val_peek(0).sval);
        }
break;
case 100:
//#line 516 "gramatica.y"
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
case 102:
//#line 544 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 103:
//#line 553 "gramatica.y"
{ 
            /*if(!errorState)*/
            reversePolish.addFalseBifurcation();
            notifyDetection("Condición."); 
        }
break;
case 104:
//#line 562 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 105:
//#line 566 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 568 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 107:
//#line 575 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 108:
//#line 582 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 109:
//#line 584 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 110:
//#line 586 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 111:
//#line 593 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 112:
//#line 597 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 117:
//#line 608 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 118:
//#line 636 "gramatica.y"
{ 
            reversePolish.completeSelection();
            notifyDetection("Sentencia IF."); 
        }
break;
case 120:
//#line 644 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 122:
//#line 653 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 123:
//#line 655 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 124:
//#line 657 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 125:
//#line 662 "gramatica.y"
{ reversePolish.addInconditionalBifurcation(); }
break;
case 128:
//#line 674 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 129:
//#line 681 "gramatica.y"
{ reversePolish.registerDoBody(); }
break;
case 131:
//#line 687 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 133:
//#line 693 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 134:
//#line 695 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 135:
//#line 697 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 137:
//#line 708 "gramatica.y"
{}
break;
case 138:
//#line 715 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 139:
//#line 717 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 140:
//#line 724 "gramatica.y"
{ reversePolish.addTrueBifurcation(); }
break;
case 141:
//#line 733 "gramatica.y"
{
            notifyDetection("Declaración de función.");
            this.symbolTable.setType(val_peek(2).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(2).sval, SymbolCategory.FUNCTION);
            this.scopeStack.pop();    
        }
break;
case 142:
//#line 743 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 143:
//#line 746 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 144:
//#line 749 "gramatica.y"
{
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.scopeStack.pop();
        }
break;
case 145:
//#line 761 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); yyval.sval = val_peek(0).sval; }
break;
case 146:
//#line 768 "gramatica.y"
{
            this.scopeStack.push(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
        }
break;
case 149:
//#line 788 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 152:
//#line 800 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 155:
//#line 814 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PARAMETER);
        }
break;
case 156:
//#line 822 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 157:
//#line 824 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 160:
//#line 836 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 161:
//#line 845 "gramatica.y"
{ 
            reversePolish.addPolish("return");
            notifyDetection("Sentencia RETURN."); 
        }
break;
case 162:
//#line 853 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 163:
//#line 855 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 164:
//#line 857 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 165:
//#line 859 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 166:
//#line 868 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 168:
//#line 878 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 169:
//#line 885 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 170:
//#line 890 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 171:
//#line 899 "gramatica.y"
{ 
            reversePolish.addPolish("print");
            notifyDetection("Sentencia 'print'."); 
        }
break;
case 172:
//#line 907 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 174:
//#line 910 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 176:
//#line 923 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 177:
//#line 926 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 178:
//#line 928 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 181:
//#line 944 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 182:
//#line 949 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 184:
//#line 952 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 185:
//#line 955 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 186:
//#line 957 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 187:
//#line 959 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 188:
//#line 961 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 189:
//#line 963 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 190:
//#line 965 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 192:
//#line 978 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 193:
//#line 981 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1568 "Parser.java"
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
