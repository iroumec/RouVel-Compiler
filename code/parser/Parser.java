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
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 37 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 33 "Parser.java"




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
    0,    0,   26,    0,   27,    0,    0,    0,   18,   24,
   24,   30,   24,   24,   24,   24,   29,   29,   28,   28,
   25,   25,   25,   31,   31,   33,   33,   36,   36,   37,
   37,   38,   38,   39,   39,   32,   32,   32,   32,   32,
   32,   32,   32,   41,   41,   34,   34,   34,   34,   20,
   20,   20,   19,   40,   40,   40,   40,   23,   23,   21,
   21,   21,   22,   22,   22,    1,    1,    1,    1,    1,
    6,    6,    2,    2,    2,    2,    4,    4,    4,    7,
    7,    3,    3,    3,    5,    5,    5,   10,   10,    9,
    9,   47,   47,   47,   47,   48,   48,   48,   48,   14,
   14,   14,   14,   14,   14,   14,   45,   45,   49,   49,
   49,   49,   50,   51,   51,   51,   52,   46,   53,   53,
   53,   54,   55,   55,   55,   56,   57,   35,   35,    8,
    8,   58,   58,   59,   59,   59,   61,   61,   60,   60,
   60,   15,   15,   15,   42,   42,   42,   42,   42,   11,
   12,   12,   13,   13,   43,   43,   62,   62,   62,   62,
   63,   63,   44,   44,   44,   44,   44,   17,   17,   17,
   16,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    5,    2,    1,
    3,    2,    1,    4,    4,    3,    3,    3,    4,    2,
    3,    2,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    1,    3,    2,    3,    1,    3,    1,
    1,    2,    1,    1,    1,    1,    1,    1,    2,    1,
    3,    3,    2,    2,    3,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    2,    4,    4,
    3,    3,    1,    0,    2,    1,    1,    1,    3,    3,
    2,    1,    2,    1,    2,    1,    2,    5,    4,    2,
    1,    3,    2,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    5,    5,    4,    3,    2,    4,
    1,    3,    3,    1,    3,    3,    3,    2,    1,    0,
    1,    1,    4,    4,    5,    4,    5,    3,    2,    0,
    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  122,    0,    0,    0,    0,    0,    0,
    0,    0,   38,    1,    0,    0,   21,   24,   25,   26,
    0,   37,   39,   40,   41,   42,   44,   45,  118,    0,
    4,    0,   23,    0,    0,   88,  161,    0,    0,   80,
   81,    0,    0,    0,   74,    0,    0,   83,   84,    0,
  159,  108,    0,    0,    0,    0,    0,   49,    0,    0,
    0,  149,    0,    0,    0,   17,   14,    0,    0,    0,
    0,    0,    0,   60,    0,    0,   62,   43,   36,    0,
   34,    0,    0,    0,   63,    0,   22,   18,    0,   29,
   27,  121,    0,    0,   30,    0,   31,    0,    0,  124,
    6,   91,    0,    0,  151,    0,   89,  158,    0,   86,
   71,   72,    0,   78,    0,   85,   87,    0,   76,   82,
  156,  155,   93,    0,  102,  104,  103,  105,  106,  100,
  101,    0,    0,    0,  117,  113,  107,    0,    0,    0,
   94,    0,   47,   52,   46,    0,    0,    0,  148,   16,
    0,    0,    0,  144,  143,    0,  133,    0,    0,  134,
  136,    0,   57,    0,    0,    0,   61,   56,   33,    0,
    0,    0,    0,    0,    0,   35,    0,   58,    0,   65,
    0,  127,    0,  125,  120,  119,  123,    0,    0,  150,
  157,    0,   68,    0,   75,   73,   95,   92,    0,    0,
    0,    0,  115,    0,   53,   51,  147,    0,   19,   20,
  171,  138,  141,    0,    0,  132,  129,    0,   55,   54,
   32,    0,  169,    0,  164,  163,    0,  166,   59,   64,
   13,  153,  152,   79,   77,    0,  111,  112,   48,  146,
  145,  139,  135,  128,  165,  168,  167,  110,  109,
};
final static short yydgoto[] = {                          4,
   64,   54,   55,  123,  124,  125,   56,   18,   57,   58,
   59,  114,  115,  144,  168,   21,  182,    5,   70,   71,
   22,   96,   23,   24,   25,    6,    8,  162,   26,   99,
   27,   28,   29,   30,   31,  101,  106,  107,   93,   32,
   33,   34,   35,   36,   37,   38,   66,   67,  147,  148,
  149,  150,   39,   40,  108,  109,  110,   81,  169,  170,
  171,   60,   61,
};
final static short yysindex[] = {                         3,
   62,    0,    0,    0,  -28,  -90,    0, -193,   11,   15,
   -5,  419, -228,    0,  439,  -40, -190,   42,  130,  -46,
   98,   49,    0,    0,  101,  -37,    0,    0,    0,    0,
   31,    0,    0,    0,    0,    0,    0,    0,    0,   13,
    0,  -90,    0, -173,  495,    0,    0,  501, -169,    0,
    0,  124,  231,   12,    0,  177, -154,    0,    0,  -45,
    0,    0,   26,  519,   12,  -17,   78,    0,    0, -144,
  373,    0,   66,  276,  -38,    0,    0,   36, -134,  -36,
    2,  477, -133,    0,  506,  256,    0,    0,    0,  112,
    0,   87,  123,  -11,    0,  -13,    0,    0,   48,    0,
    0,    0,  102,  112,    0,  102,    0,  -43, -145,    0,
    0,    0,  157,   52,    0,   12,    0,    0,   88,    0,
    0,    0,   12,    0,  483,    0,    0,  296,    0,    0,
    0,    0,    0,  -23,    0,    0,    0,    0,    0,    0,
    0,   12,  483,  495,    0,    0,    0, -138, -137,  139,
    0,  -11,    0,    0,    0, -126,   74,  119,    0,    0,
    9,   10,   96,    0,    0, -232,    0, -213,   56,    0,
    0,   25,    0,  253,   93,  -14,    0,    0,    0,  136,
  163,   22,   87,  -39, -113,    0,   72,    0,  -11,    0,
   59,    0,  152,    0,    0,    0,    0, -111,  495,    0,
    0,  312,    0,   12,    0,    0,    0,    0,   12,  231,
  -55,   89,    0,   92,    0,    0,    0,   32,    0,    0,
    0,    0,    0, -104, -232,    0,    0,   70,    0,    0,
    0, -102,    0,  114,    0,    0, -100,    0,    0,    0,
    0,    0,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        17,
  -99,    0,    0,    0,  159,  159,    0,    0,    0,  386,
   45,    0,  117,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  170,   82,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  159,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -32,    7,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  409,  -89,    0,    0,  250,  379,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -212,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   55,  -75,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -91,    0,    0,    0,    0,
    0,    0,   57,    0,    0,  396,    0,    0,    0,    0,
    0,    0,  142,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  431,    0,    0,    0,    0,    0,  -44,    0,   -3,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  188,  189,    0,    0,    0, -212,    0,    0,    0,    0,
    0,    0,    0,    0,   -1,    0,    0,    0,    0,  -75,
    0,    0,  -75,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  302,    0,    0,    0,    0,  456,  -15,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   77,  -34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  754,  697,  644,  126,    0,  129,  557,    0,  510,  445,
  658,    0,   -2,    0,    0,    0,  -68,  186,   40,    0,
   -8,  104,    0,   30,    6,    0,    0,    0,    0,    0,
  366,  535,    0,    0,    0,    0,  -47,  178,  -41,    0,
    0,    0,    0,    0,    0,    0,  -29,  140,    0,    0,
   58,    0,    0,    0,    0,    0,  100,    0,    0, -146,
    0,    0,  153,
};
final static int YYTABLESIZE=953;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,  233,    2,  247,  167,   49,  137,  166,  162,  137,
   87,   17,   89,  132,  114,  196,    3,  208,  146,  222,
   43,   78,   17,  164,  185,   96,  162,   68,   69,   83,
  189,   49,   16,   49,   52,   41,   51,   48,  165,   49,
   28,   50,   90,  223,  142,  188,   84,   66,  180,   66,
   66,   66,   17,   51,   45,  116,  224,  142,   50,   90,
   44,    7,  193,    3,   17,   66,  133,   51,   48,   43,
   49,  111,   50,  192,  177,   17,  194,   87,  253,   79,
  236,   80,   76,  112,   77,   98,  160,   17,  117,  100,
  251,  259,  200,   49,   16,  199,  226,  154,   17,  225,
  154,  130,  213,  160,  191,  104,  157,   51,   48,   17,
   49,  232,   50,  170,  237,  189,   49,  140,  151,  152,
  140,   12,  163,  175,  172,   28,  181,  103,  201,  145,
  215,  212,  217,  219,  220,  104,  221,   17,   44,    3,
   17,   63,  238,   51,   48,  242,   49,  248,   50,  227,
  249,   17,  252,  255,  256,  257,  131,    5,   15,  218,
  161,  121,  184,  122,  118,   51,   48,   87,   49,    2,
   50,   51,   48,   83,   49,  184,   50,  228,   17,  114,
  170,  126,   69,  241,   69,   69,   69,   10,   11,  142,
   84,   17,  143,   42,  254,  216,  243,  187,   92,  121,
   69,  122,  134,  233,  119,  211,    0,   49,  197,   88,
  131,    0,  195,  246,    0,   75,   10,   10,   46,  164,
   90,   49,  142,  162,  114,   11,   12,    9,   10,   13,
   79,   14,  207,   15,  165,  142,  179,   11,   12,   10,
   96,   13,  175,   14,   46,   15,   46,  183,   11,   12,
  145,   10,   46,   47,   14,   90,   15,   28,    1,    3,
  231,  104,   66,   66,   66,  116,   28,   28,  102,   10,
   28,   66,   28,  121,   28,  122,  231,  235,   11,   12,
    9,   10,   10,   46,   14,  103,   15,  250,  258,  130,
   11,   12,   10,   53,   13,  121,   14,  122,   15,   83,
  160,   11,   12,    9,   10,   13,   46,   14,   53,   15,
  170,  230,   94,   11,   12,   10,   84,   13,  121,   14,
  122,   15,   10,   46,   11,   12,   10,  239,   13,   46,
   14,    0,   15,    0,  159,   11,   12,   12,   12,   13,
   49,   14,   67,   15,   67,   67,   67,   12,   12,    0,
    0,   12,    0,   12,   10,   12,   49,   10,   10,   46,
   67,    0,    0,   11,   12,    0,   11,   12,   10,   14,
   13,   15,   14,    0,   15,   10,  120,   11,   12,   10,
   10,   46,   47,   14,    0,   15,   10,   46,   11,   12,
   97,    0,   10,   82,   14,   10,   15,   69,   69,   69,
    0,   11,   12,    0,   11,   12,   69,   14,   10,   15,
   14,    0,   15,   10,  120,    0,  156,   11,   12,   10,
   46,  198,   50,   14,    0,   15,   90,   90,   90,   90,
   90,  155,   90,   10,   46,    0,   70,   50,   70,   70,
   70,    0,    0,   97,   90,   90,   90,   90,    0,   99,
    0,   66,    0,   66,   70,   70,   70,   70,   63,    0,
   51,   48,    0,   49,    0,   50,   95,    0,   66,   66,
   66,   97,    0,   69,    0,   69,    0,    0,   73,    0,
   51,   48,    0,   49,    0,   50,    0,   10,  120,    0,
   69,   69,   69,    0,    0,    0,   98,    0,   67,    0,
   67,    0,    0,    0,    0,   53,   53,    0,  229,   10,
  120,  130,  175,   53,   19,   67,   67,   67,   51,   48,
    0,   49,    0,   50,   51,   19,    0,   49,   86,   50,
   19,    0,   10,  120,   19,    0,   51,   48,   95,   49,
  190,   50,   51,    0,    0,   49,    0,   50,  121,   19,
  122,  205,   10,   46,    0,   91,   97,   67,   67,   67,
    0,  121,  126,  122,  178,    0,   67,  244,   10,   46,
    0,    0,    0,  126,  105,   19,    0,    0,  141,  139,
  140,    0,    0,  126,    0,    0,    0,   19,    0,    0,
    0,    0,  176,   97,  126,  176,  214,    0,    0,   19,
  105,    0,   19,    0,    0,    0,    0,    0,   19,    0,
  128,    0,    0,   19,    0,    0,    0,    0,    0,    0,
    0,  128,  126,    0,   91,    0,    0,  186,  153,  154,
    0,  190,    0,  240,   50,   50,    0,    0,   91,    0,
    0,   90,   90,   90,    0,   90,   90,   90,   90,   90,
   90,   70,   70,   70,    0,   70,   70,   70,   70,   19,
   70,    0,   20,    0,   99,   66,   66,  126,   66,   66,
   66,   66,  128,   20,   62,   10,   46,    0,   20,  202,
    0,   19,   20,  126,  105,  176,   97,   69,   69,   19,
   69,   69,   69,   69,   72,   10,   46,   20,  202,  129,
   19,    0,   19,    0,    0,    0,    0,    0,   65,    0,
  127,   98,   67,   67,  186,   67,   67,   67,   67,  126,
    0,  127,    0,   20,    0,    0,    0,  186,    0,    0,
    0,  127,  173,   10,   46,   20,    0,   19,  203,   10,
   46,    0,  127,    0,  116,    0,    0,   20,    0,    0,
   20,   10,   46,    0,    0,    0,   20,   10,   46,   65,
  128,   20,   10,  120,   53,  128,    0,    0,   74,    0,
  127,  206,   85,    0,    0,   10,  120,    0,  135,  136,
  137,  138,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  113,   65,
    0,    0,   65,    0,    0,   53,    0,   20,    0,    0,
    0,    0,    0,    0,    0,  127,    0,    0,    0,    0,
    0,  204,    0,    0,  234,    0,  158,  234,    0,   20,
    0,  127,    0,    0,    0,  174,    0,   20,    0,  209,
    0,    0,    0,    0,    0,  245,    0,    0,   20,    0,
   20,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  127,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  210,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  113,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,    0,   59,   41,   45,   41,   44,   41,   44,
   19,   40,   59,   59,   59,   59,    0,   41,   66,  166,
   59,   16,   40,  256,   93,   41,   59,  256,  257,   44,
   44,   45,  123,   45,   40,    6,   42,   43,  271,   45,
   40,   47,   44,  257,  257,   59,   61,   41,   90,   43,
   44,   45,   40,   42,   40,   59,  270,  270,   47,   61,
   46,    0,  104,  257,   40,   59,   41,   42,   43,   59,
   45,   42,   47,  103,   83,   40,  106,   86,  225,  270,
   59,   40,  123,  257,  125,  123,  125,   40,  258,   59,
   59,   59,   41,   45,  123,   44,   41,   41,   40,   44,
   44,  256,  150,   59,   99,  123,   41,   42,   43,   40,
   45,  180,   47,   59,  183,   44,   45,   41,   41,  264,
   44,   40,  257,  257,  123,  125,   40,  273,   41,  268,
  257,  269,   59,  125,  125,  123,   41,   40,   46,  123,
   40,   40,  256,   42,   43,  257,   45,   59,   47,  125,
   59,   40,  257,  256,   41,  256,   40,  257,    0,   41,
  125,   43,   40,   45,   41,   42,   43,  176,   45,    0,
   47,   42,   43,   44,   45,   40,   47,  172,   40,  269,
  256,  273,   41,  125,   43,   44,   45,    0,    0,   64,
   61,   40,   64,    8,  125,  156,  199,   94,   21,   43,
   59,   45,   63,   41,   52,  148,   -1,   45,  109,  256,
  256,   -1,  256,  269,   -1,  256,  257,  257,  258,  256,
  123,   45,  257,  256,  269,  266,  267,  256,  257,  270,
  270,  272,  256,  274,  271,  270,  125,  266,  267,  257,
  256,  270,  257,  272,  258,  274,  258,  125,  266,  267,
  268,  257,  258,  259,  272,  257,  274,  257,  256,  257,
  125,  123,  256,  257,  258,  269,  266,  267,  256,  257,
  270,  265,  272,   43,  274,   45,  125,  256,  266,  267,
  256,  257,  257,  258,  272,  273,  274,  256,  256,   40,
  266,  267,  257,   44,  270,   43,  272,   45,  274,   44,
  256,  266,  267,  256,  257,  270,  258,  272,   59,  274,
  256,   59,  264,  266,  267,  257,   61,  270,   43,  272,
   45,  274,  257,  258,  266,  267,  257,  256,  270,  258,
  272,   -1,  274,   -1,   59,  266,  267,  256,  257,  270,
   45,  272,   41,  274,   43,   44,   45,  266,  267,   -1,
   -1,  270,   -1,  272,  257,  274,   45,  257,  257,  258,
   59,   -1,   -1,  266,  267,   -1,  266,  267,  257,  272,
  270,  274,  272,   -1,  274,  257,  258,  266,  267,  257,
  257,  258,  259,  272,   -1,  274,  257,  258,  266,  267,
   25,   -1,  257,  264,  272,  257,  274,  256,  257,  258,
   -1,  266,  267,   -1,  266,  267,  265,  272,  257,  274,
  272,   -1,  274,  257,  258,   -1,   44,  266,  267,  257,
  258,  265,   44,  272,   -1,  274,   41,   42,   43,   44,
   45,   59,   47,  257,  258,   -1,   41,   59,   43,   44,
   45,   -1,   -1,   78,   59,   60,   61,   62,   -1,   41,
   -1,   43,   -1,   45,   59,   60,   61,   62,   40,   -1,
   42,   43,   -1,   45,   -1,   47,   22,   -1,   60,   61,
   62,   41,   -1,   43,   -1,   45,   -1,   -1,   40,   -1,
   42,   43,   -1,   45,   -1,   47,   -1,  257,  258,   -1,
   60,   61,   62,   -1,   -1,   -1,   41,   -1,   43,   -1,
   45,   -1,   -1,   -1,   -1,  256,  257,   -1,  256,  257,
  258,  256,  257,  264,    5,   60,   61,   62,   42,   43,
   -1,   45,   -1,   47,   42,   16,   -1,   45,   19,   47,
   21,   -1,  257,  258,   25,   -1,   42,   43,   94,   45,
   96,   47,   42,   -1,   -1,   45,   -1,   47,   43,   40,
   45,  256,  257,  258,   -1,   21,  191,  256,  257,  258,
   -1,   43,   53,   45,   59,   -1,  265,  256,  257,  258,
   -1,   -1,   -1,   64,   40,   66,   -1,   -1,   60,   61,
   62,   -1,   -1,   74,   -1,   -1,   -1,   78,   -1,   -1,
   -1,   -1,   83,  228,   85,   86,  152,   -1,   -1,   90,
   66,   -1,   93,   -1,   -1,   -1,   -1,   -1,   99,   -1,
   54,   -1,   -1,  104,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   65,  113,   -1,   90,   -1,   -1,   93,  256,  257,
   -1,  187,   -1,  189,  256,  257,   -1,   -1,  104,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,  150,
  265,   -1,    5,   -1,  256,  257,  258,  158,  260,  261,
  262,  263,  116,   16,  256,  257,  258,   -1,   21,  123,
   -1,  172,   25,  174,  150,  176,  256,  257,  258,  180,
  260,  261,  262,  263,  256,  257,  258,   40,  142,   56,
  191,   -1,  193,   -1,   -1,   -1,   -1,   -1,   12,   -1,
   53,  256,  257,  258,  180,  260,  261,  262,  263,  210,
   -1,   64,   -1,   66,   -1,   -1,   -1,  193,   -1,   -1,
   -1,   74,  256,  257,  258,   78,   -1,  228,  256,  257,
  258,   -1,   85,   -1,   48,   -1,   -1,   90,   -1,   -1,
   93,  257,  258,   -1,   -1,   -1,   99,  257,  258,   63,
  204,  104,  257,  258,   11,  209,   -1,   -1,   15,   -1,
  113,  128,   19,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   45,  103,
   -1,   -1,  106,   -1,   -1,   52,   -1,  150,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  158,   -1,   -1,   -1,   -1,
   -1,  125,   -1,   -1,  181,   -1,   73,  184,   -1,  172,
   -1,  174,   -1,   -1,   -1,   82,   -1,  180,   -1,  143,
   -1,   -1,   -1,   -1,   -1,  202,   -1,   -1,  191,   -1,
  193,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  210,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  228,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  144,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  199,
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
"$accept : program",
"program : program_name program_body",
"program : program_name statement_list",
"$$1 :",
"program : $$1 program_body",
"$$2 :",
"program : error $$2 program_name program_body",
"program : error EOF",
"program : EOF",
"program_name : ID",
"program_body : '{' statement_list '}'",
"program_body : '{' statement_list close_brace_list",
"$$3 :",
"program_body : open_brace_list $$3 statement_list '}'",
"program_body : '{' '}'",
"program_body :",
"program_body : '{' error '}'",
"open_brace_list : '{' '{'",
"open_brace_list : open_brace_list '{'",
"close_brace_list : '}' '}'",
"close_brace_list : close_brace_list '}'",
"statement_list : statement",
"statement_list : statement_list statement",
"statement_list : error ';'",
"statement : executable_statement",
"statement : declarative_statement",
"declarative_statement : declaration_of_variables",
"declarative_statement : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"cuerpo_ejecutable : executable_statement",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : executable_statement",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables executable_statement",
"executable_statement : invocacion_funcion ';'",
"executable_statement : asignacion_simple",
"executable_statement : multiple_assignment",
"executable_statement : sentencia_control",
"executable_statement : sentencia_retorno",
"executable_statement : impresion",
"executable_statement : lambda",
"executable_statement : invocacion_funcion error",
"sentencia_control : if",
"sentencia_control : iteracion",
"declaration_of_variables : UINT list_of_identifiers ';'",
"declaration_of_variables : UINT list_of_identifiers error",
"declaration_of_variables : UINT identifier DASIG constant ';'",
"declaration_of_variables : UINT error",
"list_of_identifiers : identifier",
"list_of_identifiers : list_of_identifiers ',' identifier",
"list_of_identifiers : list_of_identifiers ID",
"identifier : ID",
"asignacion_simple : variable DASIG expression ';'",
"asignacion_simple : variable DASIG expression error",
"asignacion_simple : variable expression ';'",
"asignacion_simple : variable DASIG error",
"multiple_assignment : list_of_variables list_of_constants ';'",
"multiple_assignment : list_of_variables DASIG list_of_constants error",
"list_of_variables : variable '='",
"list_of_variables : variable ',' list_of_variables",
"list_of_variables : variable list_of_variables",
"list_of_constants : constant",
"list_of_constants : list_of_constants ',' constant",
"list_of_constants : list_of_constants constant",
"expression : termino",
"expression : expression operador_suma termino",
"expression : expression operador_suma error",
"expression : expression termino_simple",
"expression : '+' termino",
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
"factor : constant",
"factor : invocacion_funcion",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : invocacion_funcion",
"constant : CTE",
"constant : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"condicion : '(' cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' cuerpo_condicion error",
"cuerpo_condicion : expression comparador expression",
"cuerpo_condicion : expression termino_simple",
"cuerpo_condicion : expression operador_suma termino",
"cuerpo_condicion : termino",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : IF condicion cuerpo_if",
"if : IF error",
"cuerpo_if : cuerpo_then rama_else ENDIF ';'",
"cuerpo_if : cuerpo_then rama_else ENDIF error",
"cuerpo_if : cuerpo_then rama_else ';'",
"cuerpo_if : rama_else ENDIF ';'",
"cuerpo_then : cuerpo_ejecutable",
"rama_else :",
"rama_else : else_start cuerpo_ejecutable",
"rama_else : else_start",
"else_start : ELSE",
"iteracion : do_while",
"do_while : do_while_start cuerpo_iteracion ';'",
"do_while : do_while_start cuerpo_iteracion error",
"do_while : do_while_start error",
"do_while_start : DO",
"cuerpo_iteracion : cuerpo_do fin_cuerpo_iteracion",
"cuerpo_iteracion : fin_cuerpo_iteracion",
"cuerpo_iteracion : cuerpo_ejecutable condicion",
"cuerpo_do : cuerpo_ejecutable",
"fin_cuerpo_iteracion : WHILE condicion",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' statement_list '}'",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' '}'",
"inicio_funcion : UINT ID",
"inicio_funcion : UINT",
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
"sentencia_retorno : RETURN '(' expression ')' ';'",
"sentencia_retorno : RETURN '(' expression ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
"sentencia_retorno : RETURN expression ';'",
"sentencia_retorno : RETURN error",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expression FLECHA ID",
"argumento : expression",
"impresion : PRINT imprimible ';'",
"impresion : PRINT imprimible error",
"imprimible : '(' elemento_imprimible ')'",
"imprimible : '(' ')'",
"imprimible : elemento_imprimible",
"imprimible :",
"elemento_imprimible : STR",
"elemento_imprimible : expression",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"argumento_lambda : '(' factor ')'",
"argumento_lambda : '(' ')'",
"argumento_lambda :",
"parametro_lambda : '(' UINT ID ')'",
};

//#line 1116 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private MessageCollector errorCollector, warningCollector;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    this.symbolTable = SymbolTable.getInstance();
    
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
private int yylex() {

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra con un token error.
 *
 * En caso de que el error sea tratado en la gramática, este será remplazado
 * posteriormente por un mensaje de error más apropiado.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
private void yyerror(String s) {
    notifyError(s);
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyDetection(String message) {
    Printer.printWrapped(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    warningCollector.add(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    errorCollector.add(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    errorCollector.replaceLastWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 782 "Parser.java"
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
//#line 78 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addExitSeparation(val_peek(1).sval);
            } else {
                errorState = false;
            }
        }
break;
case 2:
//#line 90 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 93 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 95 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 98 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 101 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 108 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addEntrySeparation(val_peek(0).sval);
        }
break;
case 11:
//#line 123 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 126 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 129 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 131 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 133 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 23:
//#line 159 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 200 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 218 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 229 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 246 "gramatica.y"
{
            if (!errorState) {
                { notifyDetection("Declaración de variables."); }
            } else {
                errorState = false;
            }
        }
break;
case 47:
//#line 256 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 48:
//#line 260 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 49:
//#line 264 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 274 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 52:
//#line 279 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 53:
//#line 302 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 54:
//#line 316 "gramatica.y"
{ 

            if (!errorState) {
                
                notifyDetection("Asignación simple.");

                this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);/*yo no pondría esto, cuando $3 es una expresion queda mal*/

                reversePolish.addPolish(val_peek(3).sval);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish(val_peek(2).sval);
            } else {

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                this.symbolTable.removeEntry(val_peek(3).sval);
                this.symbolTable.removeEntry(val_peek(1).sval);

                this.reversePolish.emptyTemporalPolishes();

                errorState = false;
            }
        }
break;
case 55:
//#line 345 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 56:
//#line 348 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 57:
//#line 351 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 58:
//#line 361 "gramatica.y"
{
            if (!errorState) {
                /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
                String[] variables = val_peek(2).sval.split("\\s*,\\s*");
                String[] constants = val_peek(1).sval.split("\\s*,\\s*");

                if (variables.length > constants.length) {

                    notifyError(String.format(
                            "El número de variables (%d) del lado izquierdo de la asignación "
                            + "no puede superar el número de constantes (%d) en el lado derecho.",
                            variables.length, constants.length));

                } else {
                
                    if (variables.length < constants.length) {

                        notifyWarning(String.format(
                                "El número de variables (%d) en el lado izquierdo de la asignación "
                                + "es menor al número de constantes (%d) en el lado derecho de esta. "
                                + "Las constantes sobrantes serán descartadas.",
                                variables.length, constants.length));
                    }

                    /* En este punto, la lista de variables y constantes tendrá la misma longitud.*/
                    for (int i = 0; i < variables.length; i++) {
                        
                        String variable = variables[i];
                        String constant = constants[i];   

                        this.symbolTable.setValue(variable, constant);
                        reversePolish.addPolish(variable);
                        reversePolish.addPolish(constant);
                        /* Se agrega un DASIG ya que son varias asignaciones simples.*/
                        reversePolish.addPolish(":=");
                    }

                    notifyDetection("Asignación múltiple.");
                }
            } else {

                notifyError("Hola");
                errorState = false;
            }
        }
break;
case 59:
//#line 410 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 61:
//#line 419 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 62:
//#line 424 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 64:
//#line 447 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 65:
//#line 452 "gramatica.y"
{
            String[] constants = val_peek(1).sval.split("\\s*,\\s*");
            String lastConstant = constants[constants.length - 1];

            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                lastConstant, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las constantes no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 67:
//#line 475 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 68:
//#line 483 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 69:
//#line 487 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 494 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 71:
//#line 504 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 72:
//#line 506 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 73:
//#line 513 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 75:
//#line 522 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 529 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 77:
//#line 536 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 79:
//#line 545 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 80:
//#line 552 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 81:
//#line 554 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 82:
//#line 563 "gramatica.y"
{
            /* TODO: esto es un parche. Debe verse mejor después.*/
            this.errorCollector.removeLast(); /* Debido a que se usa el token error.*/
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 83:
//#line 569 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 580 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 86:
//#line 584 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 89:
//#line 595 "gramatica.y"
{
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 90:
//#line 611 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope(val_peek(0).sval))) { /*Si entra por aca, la variable debe ser local*/
                errorState = true;
                notifyError(String.format("Variable %s no declarada.", val_peek(0).sval));
            } else {
                
                /* A la entrada sin el scope, se le agrega el scope.*/
                /* Se combina con otra entrada en caso de coincidir el scope.*/
                this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
                yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
            }
        }
break;
case 91:
//#line 624 "gramatica.y"
{ 
            String scopedVariable = val_peek(0).sval + this.scopeStack.getScopeRoad(val_peek(2).sval);

            if (!this.scopeStack.isReacheable(val_peek(2).sval)) {
                errorState = true;
                notifyError(String.format("Variable %s no declarada (no visible).",val_peek(0).sval));
            } else if (!this.symbolTable.entryExists(scopedVariable)) {
                errorState = true;
                notifyError(String.format("Variable '%s' no declarada en el ámbito '%s'.",val_peek(0).sval,val_peek(2).sval));
            }

            yyval.sval = scopedVariable;

            /* Se remplaza el identificador sin ámbito por su versión con ámbito.*/
            this.symbolTable.replaceEntry(val_peek(0).sval, yyval.sval); 
        }
break;
case 92:
//#line 648 "gramatica.y"
{ 
            if (!errorState) {
                /*reversePolish.addFalseBifurcation();*/
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 93:
//#line 660 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 664 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 666 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 673 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 97:
//#line 681 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 683 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 685 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 100:
//#line 692 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 101:
//#line 696 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 106:
//#line 707 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 107:
//#line 716 "gramatica.y"
{ 
            if (!errorState) {
                this.reversePolish.completeBifurcation();
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 108:
//#line 729 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 110:
//#line 738 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 111:
//#line 740 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 112:
//#line 742 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 116:
//#line 759 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 767 "gramatica.y"
{ this.reversePolish.addInconditionalBifurcation(); }
break;
case 119:
//#line 780 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
            } else {
                errorState = false;
            }
        }
break;
case 120:
//#line 793 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 121:
//#line 795 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 122:
//#line 802 "gramatica.y"
{ this.reversePolish.stackBifurcationPoint(); }
break;
case 124:
//#line 811 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 125:
//#line 813 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 128:
//#line 832 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                this.scopeStack.pop();
                this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                this.reversePolish.addExitSeparation(val_peek(4).sval);
            } else {
                errorState = false;
            }
        }
break;
case 129:
//#line 848 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 130:
//#line 860 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addEntrySeparation(val_peek(0).sval);
        }
break;
case 131:
//#line 869 "gramatica.y"
{
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 133:
//#line 884 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 136:
//#line 896 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 139:
//#line 910 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 140:
//#line 921 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 141:
//#line 923 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 142:
//#line 930 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 143:
//#line 932 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 144:
//#line 937 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 145:
//#line 946 "gramatica.y"
{
            if (!errorState) {
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("return");
                notifyDetection("Sentencia 'return'.");
            } else {
                errorState = false;
            }
        }
break;
case 146:
//#line 959 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 147:
//#line 961 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 148:
//#line 963 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 149:
//#line 965 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 150:
//#line 974 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 152:
//#line 984 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 153:
//#line 991 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 154:
//#line 996 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 155:
//#line 1005 "gramatica.y"
{
            if (!errorState) {
                /* Se añaden las polacas correspondiente al imprimible.*/
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                errorState = false;
            }
        }
break;
case 156:
//#line 1019 "gramatica.y"
{
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 158:
//#line 1034 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 159:
//#line 1036 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 160:
//#line 1042 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 161:
//#line 1049 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 163:
//#line 1061 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Expresión lambda.");
                this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval);

                /* El argumento no se agrega como polaca.*/
                this.reversePolish.emptyTemporalPolishes();
            } else {
                errorState = false;
            }
        }
break;
case 164:
//#line 1076 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 165:
//#line 1079 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 166:
//#line 1081 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 167:
//#line 1083 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 168:
//#line 1090 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 169:
//#line 1095 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 170:
//#line 1098 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 171:
//#line 1105 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
        }
break;
//#line 1687 "Parser.java"
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
