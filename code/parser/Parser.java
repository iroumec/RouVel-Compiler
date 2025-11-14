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
    import common.Monitor;
    import java.util.Arrays;
    import semantic.Promise;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
//#line 34 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 35 "Parser.java"




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
    0,    0,   27,    0,   28,    0,    0,    0,    1,   25,
   25,   31,   25,   25,   25,   25,   30,   30,   29,   29,
   26,   26,   26,   32,   32,   34,   34,   37,   37,   38,
   38,   39,   39,   40,   40,   33,   33,   33,   33,   33,
   33,   33,   43,   43,   35,   35,   35,   35,    6,    6,
    6,    5,   41,   41,   41,   41,   24,   24,   22,   22,
   22,   23,   23,   23,   17,   17,   17,   17,   17,   14,
   14,   18,   18,   18,   18,   20,   20,   20,   15,   15,
   19,   19,   19,   21,   21,   21,    4,    4,    3,    3,
   49,   49,   49,   49,   50,   50,   50,   50,   16,   16,
   16,   16,   16,   16,   16,   47,   47,   51,   52,   52,
   52,   52,   53,   53,   53,   54,   48,   48,   48,   55,
   56,   56,   56,   57,   36,   36,    7,    7,   59,   59,
   58,   58,   60,   60,   60,   62,   62,   61,   61,   61,
   13,   13,   13,   44,   44,   44,   44,   44,   42,   42,
    8,    2,   11,   11,   12,   12,   45,   45,   63,   63,
   63,   63,   64,   64,   46,   46,   46,   46,   46,   10,
   10,   10,    9,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    5,    2,    1,    3,
    2,    1,    4,    4,    3,    3,    3,    3,    2,    3,
    3,    1,    3,    2,    1,    3,    3,    2,    2,    1,
    1,    3,    1,    3,    2,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    3,    2,    2,    3,    3,    2,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    2,    2,    4,    4,
    3,    3,    0,    2,    1,    1,    3,    3,    2,    1,
    2,    1,    2,    2,    5,    4,    2,    1,    1,    2,
    3,    2,    1,    3,    1,    2,    2,    3,    2,    2,
    0,    1,    1,    5,    5,    4,    3,    2,    2,    2,
    4,    1,    1,    3,    3,    1,    3,    3,    3,    2,
    1,    0,    1,    1,    4,    4,    5,    4,    5,    3,
    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  120,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   38,    1,    0,    0,   21,   24,   25,
   26,    0,   36,   37,   39,   40,   41,   42,   43,   44,
    0,    0,    4,    0,   23,    0,   87,  163,    0,    0,
   79,   80,    0,    0,   82,   83,    0,    0,    0,   73,
    0,  161,  107,    0,    0,    0,  108,    0,   48,    0,
    0,    0,  148,    0,    0,    0,   17,   14,    0,    0,
    0,    0,    0,    0,   59,    0,    0,    0,  150,  149,
    0,   34,    0,    0,   62,    0,   22,   18,    0,   29,
   27,  116,    0,   30,    0,   31,  106,    0,    0,  119,
    0,    0,    0,  122,    6,   90,    0,   88,  160,    0,
   75,   85,   70,   71,    0,   86,    0,    0,   77,    0,
  158,  157,   92,    0,  101,  103,  102,  104,  105,   99,
  100,    0,    0,    0,   93,    0,   46,   51,   45,    0,
    0,    0,  147,   16,    0,    0,   52,    0,    0,  153,
    0,    0,   61,   56,    0,   60,   55,  143,  142,    0,
  132,    0,    0,  133,  135,    0,   33,    0,    0,    0,
    0,    0,    0,   35,   58,   57,    0,   64,    0,    0,
    0,    0,  114,  124,  123,  121,  118,  117,  159,   67,
    0,    0,   74,   72,   94,   91,    0,    0,    0,   50,
  146,    0,   19,   20,  173,    0,  151,    0,   54,   53,
  137,  140,    0,    0,  131,  126,  129,    0,   32,    0,
  171,    0,  166,  165,    0,  168,   63,   13,    0,  111,
  112,   78,   76,   47,  145,  144,  154,  155,  138,  134,
  125,  130,  167,  170,  169,  110,  109,
};
final static short yydgoto[] = {                          4,
    5,   18,   54,   55,   71,   72,   20,   56,   22,  180,
  159,  160,  172,  127,   57,  143,   65,   59,   60,  128,
  129,   23,   96,   24,   25,   26,    6,    8,  156,   27,
   99,   28,   29,   30,   31,   32,  101,  105,  106,   94,
   33,   34,   35,   36,   37,   38,   39,   40,   67,   68,
   41,  107,  108,  109,   42,  113,  114,   88,  228,  173,
  174,  175,   61,   62,
};
final static short yysindex[] = {                         3,
   80,    0,    0,    0,  -28,  -74,    0, -203,    2,   19,
   -5,  142, -197,    0,  486,  -40, -189,   64,  473,   82,
  -44,   92,   -2,    0,    0,   95,  -34,    0,    0,    0,
    0,   68,    0,    0,    0,    0,    0,    0,    0,    0,
  -17,   13,    0,  -74,    0, -134,    0,    0,   65, -129,
    0,    0,  124,    0,    0,    0,  129,  368,  -18,    0,
  -43,    0,    0,  445,  691,  -18,    0,   97,    0,    0,
 -131,  120,    0,  498,  147,  -11,    0,    0,   24, -118,
   54, -116,  506, -116,    0,  454,  -23,   20,    0,    0,
  106,    0,  102,  118,    0,   49,    0,    0,   36,    0,
    0,    0,  106,    0, -124,    0,    0, -122,  122,    0,
   26,  -38,  -42,    0,    0,    0,  -18,    0,    0,  104,
    0,    0,    0,    0,    0,    0,  537,  -18,    0,  254,
    0,    0,    0,   -9,    0,    0,    0,    0,    0,    0,
    0,  537,   54,  -18,    0,   -2,    0,    0,    0, -118,
   89,   43,    0,    0,   27,   29,    0,  110,  -22,    0,
  166,  -31,    0,    0,  311,    0,    0,    0,    0, -225,
    0, -212,   61,    0,    0,   47,    0,  136,  -35,  -20,
  102,    6, -103,    0,    0,    0,   -2,    0,   58,  140,
   -7,   96,    0,    0,    0,    0,    0,    0,    0,    0,
  -18,  364,    0,    0,    0,    0,  -18,  368,   98,    0,
    0,   18,    0,    0,    0,   54,    0, -101,    0,    0,
    0,    0,  -98, -225,    0,    0,    0,   69,    0,  -96,
    0,  127,    0,    0,  -95,    0,    0,    0,   32,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        14,
  -94,    0,    0,    0,  170,  170,    0,    0,    0,  375,
   33,    0,  133,    0,    0,    0,    0,    0,  135,    0,
    0,    0,    0,    0,    0,  181,   81,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
  -83,    0,    0,  170,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  385,    0,    0,    0,  -15,  301,    0,
    0,    0,    0,    0,    0,  462,    0,    0,    0,  238,
  395,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -207,    0,    0,    0,
    0,    0,   60,  -68,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -3,    0,    0,    0,    8,    0,
    0,    0,    0,    0,    0,    0,  397,    0,    0,    0,
    0,    0,    0,    0,  408,    0,    0,  419,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  529,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  195,  196,    0,    0,    0,    0,
   84,    0,    0,    0,    0,    0,    0,    0,    0, -207,
    0,    0,    0,    0,    0,    0,    0,  -68,    0,    0,
  -68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  430,    0,    0,    0,    0,    0,  555,   16,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   90,  -33,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  190,    0,  520,    4,  -60,    0,    0,  675,    0,  -61,
    0,  -16,    0,  134,   -4,    0,  756,   66,  426,  137,
    0,   34,    0,    0,   28,   12,    0,    0,    0,    0,
    0,  518,  716,    0,    0,    0,    0,   -6,  179,  -21,
    0,    0,    0,    0,    0,    0,    0,    0,  -37,  141,
    0,    0,   99,    0,    0,    0,  109,    0,    0,    0,
 -145,    0,    0,  154,
};
final static int YYTABLESIZE=972;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,   64,    2,   52,   49,  231,   50,  136,   51,   50,
  136,   17,   84,    3,   90,  132,  198,  171,  217,  158,
  170,  216,   17,   52,  221,  164,   95,   79,   51,   85,
  168,  206,  183,   43,   53,  112,   52,   49,  234,   50,
   28,   51,   50,  164,  222,  169,  231,   45,   16,  141,
   50,  240,   17,    3,  130,  113,   95,  223,   69,   70,
   45,  130,  141,   17,   46,   64,  115,   52,   49,  178,
   50,  115,   51,  194,  195,   17,  246,   66,  250,    7,
   80,  190,   77,  212,   78,  123,   17,  124,   98,  210,
  257,  162,  187,   50,   16,   52,   49,   17,   50,  188,
   51,  225,  193,   81,  224,  103,   52,  186,   17,   50,
  189,   51,  130,  154,  117,  163,  230,  166,  172,  235,
   12,   87,  116,  202,  156,   28,  100,  156,  118,   66,
  139,   17,  146,  139,   17,  103,    3,  145,  157,  202,
   10,  179,  176,  102,  199,   17,  192,  211,  155,  209,
  215,  213,  236,  214,  241,  248,  244,  182,  249,  253,
  255,   17,    5,  150,  119,   52,   49,  254,   50,   15,
   51,  226,  128,   50,  152,  182,   66,   66,  149,   17,
    2,   64,  238,   52,   49,  113,   50,  172,   51,  123,
  237,  124,  201,  251,   10,   11,  130,   44,  142,  247,
   93,  144,  130,  191,  134,  153,  120,  207,  123,    0,
  124,   89,  131,  197,   91,   76,   10,    0,   10,   47,
  196,   10,   47,  141,   82,   11,   12,    9,   10,   13,
  177,   14,  168,   15,  111,  233,  141,   11,   12,   10,
  164,   13,  181,   14,  103,   15,  205,  169,   11,   12,
  102,   10,   47,   48,   14,   47,   15,   28,    1,    3,
  229,  239,   10,   47,  229,  113,   28,   28,  110,   10,
   28,   95,   28,  245,   28,   80,  115,  127,   11,   12,
   10,   52,   10,   47,   14,  111,   15,  256,  162,   11,
   12,    9,   10,   13,    0,   14,   52,   15,   50,   10,
  122,   11,   12,   10,  185,   13,   47,   14,    0,   15,
   10,   47,   11,   12,   10,  172,   13,    0,   14,    0,
   15,   10,   47,   11,   12,   10,    0,   13,    0,   14,
    0,   15,    0,    0,   11,   12,   12,   12,   13,    0,
   14,   65,   15,   65,   65,   65,   12,   12,   10,    0,
   12,   10,   12,  123,   12,  124,    0,   11,   12,   65,
   11,   12,   10,   14,   13,   15,   14,    0,   15,  220,
    0,   11,   12,    0,   10,  147,  148,   14,   10,   15,
   10,   47,   48,   11,   12,   10,   47,   11,   12,   14,
    0,   15,   10,   14,    0,   15,   10,   63,   10,   47,
    0,   11,   12,   10,  122,   11,   12,   14,   50,   15,
  123,   14,  124,   15,   89,   89,   89,   89,   89,   89,
    0,   89,   10,  122,  152,   81,   81,   81,   81,   81,
  218,   81,    0,   89,   89,   89,   89,   69,   49,   69,
   69,   69,    0,   81,   81,   81,   81,  152,   84,   84,
   84,   84,   84,   49,   84,   69,   69,   69,   69,   68,
    0,   68,   68,   68,    0,    0,   84,   84,   84,   84,
   66,    0,   66,   66,   66,    0,    0,   68,    0,    0,
    0,    0,  121,    0,    0,  133,   52,   49,   66,   50,
    0,   51,    0,   52,   52,    0,  123,    0,  124,    0,
    0,   52,   98,    0,   65,    0,   65,    0,    0,  203,
   10,   47,  167,    0,   52,   49,   84,   50,    0,   51,
    0,   65,   65,   65,   19,   74,    0,   52,   49,    0,
   50,    0,   51,   85,    0,   19,    0,    0,  151,   52,
   49,   19,   50,   97,   51,   19,    0,   52,   49,    0,
   50,    0,   51,    0,    0,  204,   65,   65,   65,    0,
   19,   19,    0,    0,    0,   65,  219,   10,  122,   96,
    0,   68,    0,   68,    0,    0,    0,  125,   52,    0,
    0,   50,    0,   51,  125,    0,    0,    0,   68,   68,
   68,    0,    0,    0,  125,   97,   97,   66,   19,   66,
    0,  162,    0,  162,  232,  125,    0,  232,    0,    0,
   19,    0,    0,   19,   66,   66,   66,    0,   19,  242,
   10,   47,   19,    0,   10,  122,    0,  243,   19,    0,
   89,   89,   89,    0,   89,   89,   89,   89,   89,   89,
   81,   81,   81,    0,   81,   81,   81,   81,    0,   81,
   49,   49,   69,   69,   69,    0,   69,   69,   69,   69,
    0,   69,    0,   84,   84,   84,    0,   84,   84,   84,
   84,  125,   84,    0,   68,   68,   68,    0,    0,   21,
  125,    0,    0,   68,  125,   66,   66,   66,    0,    0,
   21,    0,    0,  227,   66,   19,   21,   19,    0,    0,
   21,   10,   47,    0,    0,    0,   97,    0,   19,   19,
   10,  122,    0,    0,    0,   21,   21,   98,   65,   65,
    0,   65,   65,   65,   65,    0,    0,  125,   82,   10,
   47,    0,  126,  123,    0,  124,   83,   92,    0,  126,
    0,   73,   10,   47,    0,  252,    0,   19,    0,  126,
  141,  139,  140,   21,   10,   47,  104,  104,    0,    0,
  126,  164,   10,   47,    0,   21,   58,    0,   21,    0,
   75,    0,    0,   21,   86,    0,    0,   21,    0,    0,
    0,    0,    0,   21,   96,   68,   68,    0,   68,   68,
   68,   68,  200,   10,   47,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   92,    0,   58,  184,
   97,   66,   66,    0,   66,   66,   66,   66,   92,    0,
    0,    0,    0,    0,  104,    0,  126,    0,    0,  152,
    0,    0,    0,    0,    0,  126,  161,    0,  165,  126,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   21,    0,   21,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   21,   21,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  126,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  184,    0,    0,    0,    0,  208,    0,
    0,    0,   21,    0,    0,  184,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   10,  122,    0,
  135,  136,  137,  138,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  161,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,    0,   42,   43,   41,   45,   41,   47,   45,
   44,   40,   44,    0,   59,   59,   59,   41,   41,   80,
   44,   44,   40,   42,  170,   41,   23,   16,   47,   61,
  256,   41,   94,    6,   40,   42,   42,   43,   59,   45,
   40,   47,   45,   59,  257,  271,   41,   59,  123,  257,
   45,   59,   40,  257,   59,   59,   41,  270,  256,  257,
   59,   66,  270,   40,   46,   40,   59,   42,   43,   91,
   45,   44,   47,  111,  112,   40,   59,   12,  224,    0,
  270,  103,  123,   41,  125,   43,   40,   45,  123,  150,
   59,   59,   44,   45,  123,   42,   43,   40,   45,   96,
   47,   41,  109,   40,   44,  123,   42,   59,   40,   45,
   99,   47,  117,  125,   49,   82,  178,   84,   59,  181,
   40,   40,  257,  128,   41,  125,   59,   44,  258,   64,
   41,   40,  264,   44,   40,  123,  123,   41,  257,  144,
  257,   40,  123,  268,   41,   40,  269,   59,  125,  146,
   41,  125,  256,  125,   59,  257,   59,   40,  257,  256,
  256,   40,  257,   44,   41,   42,   43,   41,   45,    0,
   47,  125,   40,   45,   40,   40,  111,  112,   59,   40,
    0,   40,  125,   42,   43,  269,   45,  256,   47,   43,
  187,   45,  127,  125,    0,    0,  201,    8,   65,  216,
   22,   65,  207,  105,   64,   59,   53,  142,   43,   -1,
   45,  256,  256,  256,  123,  256,  257,   -1,  257,  258,
  112,  257,  258,  257,  256,  266,  267,  256,  257,  270,
  125,  272,  256,  274,  273,  256,  270,  266,  267,  257,
  256,  270,  125,  272,  123,  274,  256,  271,  266,  267,
  268,  257,  258,  259,  272,  258,  274,  257,  256,  257,
  125,  269,  257,  258,  125,  269,  266,  267,  256,  257,
  270,  256,  272,  256,  274,  270,  269,   40,  266,  267,
  257,   44,  257,  258,  272,  273,  274,  256,  256,  266,
  267,  256,  257,  270,   -1,  272,   59,  274,   45,  257,
  258,  266,  267,  257,  256,  270,  258,  272,   -1,  274,
  257,  258,  266,  267,  257,  256,  270,   -1,  272,   -1,
  274,  257,  258,  266,  267,  257,   -1,  270,   -1,  272,
   -1,  274,   -1,   -1,  266,  267,  256,  257,  270,   -1,
  272,   41,  274,   43,   44,   45,  266,  267,  257,   -1,
  270,  257,  272,   43,  274,   45,   -1,  266,  267,   59,
  266,  267,  257,  272,  270,  274,  272,   -1,  274,   59,
   -1,  266,  267,   -1,  257,  256,  257,  272,  257,  274,
  257,  258,  259,  266,  267,  257,  258,  266,  267,  272,
   -1,  274,  257,  272,   -1,  274,  257,  256,  257,  258,
   -1,  266,  267,  257,  258,  266,  267,  272,   45,  274,
   43,  272,   45,  274,   40,   41,   42,   43,   44,   45,
   -1,   47,  257,  258,   40,   41,   42,   43,   44,   45,
  265,   47,   -1,   59,   60,   61,   62,   41,   44,   43,
   44,   45,   -1,   59,   60,   61,   62,   40,   41,   42,
   43,   44,   45,   59,   47,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,   -1,   -1,   59,   60,   61,   62,
   41,   -1,   43,   44,   45,   -1,   -1,   59,   -1,   -1,
   -1,   -1,   57,   -1,   -1,   41,   42,   43,   59,   45,
   -1,   47,   -1,  256,  257,   -1,   43,   -1,   45,   -1,
   -1,  264,   41,   -1,   43,   -1,   45,   -1,   -1,  256,
  257,  258,   59,   -1,   42,   43,   44,   45,   -1,   47,
   -1,   60,   61,   62,    5,   40,   -1,   42,   43,   -1,
   45,   -1,   47,   61,   -1,   16,   -1,   -1,   41,   42,
   43,   22,   45,   26,   47,   26,   -1,   42,   43,   -1,
   45,   -1,   47,   -1,   -1,  130,  256,  257,  258,   -1,
   41,   42,   -1,   -1,   -1,  265,  256,  257,  258,   41,
   -1,   43,   -1,   45,   -1,   -1,   -1,   58,   42,   -1,
   -1,   45,   -1,   47,   65,   -1,   -1,   -1,   60,   61,
   62,   -1,   -1,   -1,   75,   41,   79,   43,   79,   45,
   -1,   82,   -1,   84,  179,   86,   -1,  182,   -1,   -1,
   91,   -1,   -1,   94,   60,   61,   62,   -1,   99,  256,
  257,  258,  103,   -1,  257,  258,   -1,  202,  109,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,  264,  265,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
  256,  257,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,  265,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,  152,  265,   -1,  256,  257,  258,   -1,   -1,    5,
  161,   -1,   -1,  265,  165,  256,  257,  258,   -1,   -1,
   16,   -1,   -1,  176,  265,  176,   22,  178,   -1,   -1,
   26,  257,  258,   -1,   -1,   -1,  189,   -1,  189,  190,
  257,  258,   -1,   -1,   -1,   41,   42,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,   -1,  208,  256,  257,
  258,   -1,   58,   43,   -1,   45,  264,   22,   -1,   65,
   -1,  256,  257,  258,   -1,  228,   -1,  228,   -1,   75,
   60,   61,   62,   79,  257,  258,   41,   42,   -1,   -1,
   86,  256,  257,  258,   -1,   91,   11,   -1,   94,   -1,
   15,   -1,   -1,   99,   19,   -1,   -1,  103,   -1,   -1,
   -1,   -1,   -1,  109,  256,  257,  258,   -1,  260,  261,
  262,  263,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   53,   94,
  256,  257,  258,   -1,  260,  261,  262,  263,  103,   -1,
   -1,   -1,   -1,   -1,  109,   -1,  152,   -1,   -1,   74,
   -1,   -1,   -1,   -1,   -1,  161,   81,   -1,   83,  165,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  176,   -1,  178,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  189,  190,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  208,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,  143,   -1,
   -1,   -1,  228,   -1,   -1,  190,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  216,
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
"executable_statement : asignacion_simple",
"executable_statement : inline_function_invocation",
"executable_statement : multiple_assignment",
"executable_statement : sentencia_control",
"executable_statement : sentencia_retorno",
"executable_statement : impresion",
"executable_statement : lambda",
"sentencia_control : if",
"sentencia_control : do_while",
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
"multiple_assignment : list_of_variables list_of_constants error",
"list_of_variables : variable '='",
"list_of_variables : variable ',' list_of_variables",
"list_of_variables : variable error list_of_variables",
"list_of_constants : constant",
"list_of_constants : list_of_constants ',' constant",
"list_of_constants : list_of_constants constant",
"expression : term",
"expression : expression operador_suma term",
"expression : expression operador_suma error",
"expression : expression term_simple",
"expression : '+' term",
"operador_suma : '+'",
"operador_suma : '-'",
"term : term operador_multiplicacion factor",
"term : factor",
"term : term operador_multiplicacion error",
"term : operador_multiplicacion factor",
"term_simple : term_simple operador_multiplicacion factor",
"term_simple : factor_simple",
"term_simple : term_simple operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
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
"cuerpo_condicion : expression term_simple",
"cuerpo_condicion : expression operador_suma term",
"cuerpo_condicion : term",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : if_start cuerpo_if",
"if : IF error",
"if_start : IF condicion",
"cuerpo_if : cuerpo_ejecutable rama_else ENDIF ';'",
"cuerpo_if : cuerpo_ejecutable rama_else ENDIF error",
"cuerpo_if : cuerpo_ejecutable rama_else ';'",
"cuerpo_if : rama_else ENDIF ';'",
"rama_else :",
"rama_else : else_start cuerpo_ejecutable",
"rama_else : else_start",
"else_start : ELSE",
"do_while : do_while_start cuerpo_iteracion ';'",
"do_while : do_while_start cuerpo_iteracion error",
"do_while : do_while_start error",
"do_while_start : DO",
"cuerpo_iteracion : cuerpo_ejecutable fin_cuerpo_iteracion",
"cuerpo_iteracion : fin_cuerpo_iteracion",
"cuerpo_iteracion : cuerpo_ejecutable condicion",
"fin_cuerpo_iteracion : WHILE condicion",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' function_body '}'",
"declaracion_funcion : inicio_funcion conjunto_parametros '{' '}'",
"inicio_funcion : UINT ID",
"inicio_funcion : UINT",
"function_body : statement",
"function_body : function_body statement",
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
"inline_function_invocation : invocacion_funcion ';'",
"inline_function_invocation : invocacion_funcion error",
"invocacion_funcion : function_start '(' lista_argumentos ')'",
"function_start : variable",
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
"parametro_lambda : '(' UINT identifier ')'",
};

//#line 1261 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final Monitor monitor;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;

// --------------------------------------------------------------------------------------------------------------------

private int functionLevel;
// Si esto está activa, todas las instrucciones que se encuentran no serán pasadas a código intermedio.
private boolean isThereReturn;

// --------------------------------------------------------------------------------------------------------------------

private int returnsFound;
private int returnsNeeded;
private int selectionDepth;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.monitor = Monitor.getInstance();
    this.symbolTable = SymbolTable.getInstance();

    this.functionLevel = 0;
    this.isThereReturn = false;
    
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

public ReversePolish getReversePolish() {
    return this.reversePolish;
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
        "DETECCIÓN SINTÁCTICA: Línea %d: %s",
        monitor.getLineNumber(), message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyWarning(String warningMessage) {

    monitor.addWarning(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        monitor.getLineNumber(), warningMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void notifyError(String errorMessage) {

    monitor.addError(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private void replaceLastErrorWith(String errorMessage) {

    monitor.replaceLastErrorWith(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        monitor.getLineNumber(), errorMessage
    ));
}

// --------------------------------------------------------------------------------------------------------------------

private boolean statementAppearsInValidState() {

    return !isThereReturn && !errorState;
}

// --------------------------------------------------------------------------------------------------------------------

private void treatInvalidState(String statementName) {

    if (isThereReturn) {
        this.showOmittedStatementNotification(statementName);
    }

    if (errorState) {
        this.recoverFromErrorState();
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void showOmittedStatementNotification(String statementName) {
    notifyWarning(statementName + " no alcanzable. No se ejecutará.");
}

// --------------------------------------------------------------------------------------------------------------------

private void treatErrorState() {

    if (!errorState) {
        this.reversePolish.recordSafeState();
    } else {
        this.recoverFromErrorState();
    }
}

// --------------------------------------------------------------------------------------------------------------------

private void recoverFromErrorState() {

    this.reversePolish.emptyTemporalPolishes();
    this.reversePolish.returnToLastSafeState();
    this.errorState = false;
}

// --------------------------------------------------------------------------------------------------------------------

private boolean isUint(String number) {
    return !number.contains(".");
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 856 "Parser.java"
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
            if (!this.errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(1).sval));
            } else {
                this.errorState = false;
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
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.recordSafeState();
        }
break;
case 11:
//#line 124 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 127 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 130 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 132 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 134 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 21:
//#line 155 "gramatica.y"
{ this.treatErrorState(); }
break;
case 22:
//#line 157 "gramatica.y"
{ this.treatErrorState(); }
break;
case 23:
//#line 162 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 205 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 45:
//#line 240 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {

                if (val_peek(1).sval.split("\\s*,\\s*").length == 1) {
                    notifyDetection("Declaración de variable.");
                } else {
                    notifyDetection("Declaración de variables.");
                }
            } else {
                this.treatInvalidState("declaración de variables");
            }
        }
break;
case 46:
//#line 255 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 47:
//#line 259 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 48:
//#line 263 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 50:
//#line 273 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 51:
//#line 278 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(1).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes. Si esta no se agregase, de entrar*/
            /* nuevamente a esta regla, la separación de las variables no funcionaría adecuadamente.*/
            yyval.sval = val_peek(1).sval + ',' + val_peek(0).sval;
        }
break;
case 52:
//#line 300 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 53:
//#line 314 "gramatica.y"
{ 

            if (this.statementAppearsInValidState()) {
                
                notifyDetection("Asignación simple.");

                /* El valor aún no debe calcularse.*/
                /* this.symbolTable.setValue($1, $3);*/

                reversePolish.addPolish(val_peek(3).sval);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish(val_peek(2).sval);
            } else {

                this.treatInvalidState("asignación simple");

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                this.symbolTable.removeEntry(val_peek(3).sval);
                this.symbolTable.removeEntry(val_peek(1).sval);
            }
        }
break;
case 54:
//#line 342 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 55:
//#line 345 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 56:
//#line 348 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 57:
//#line 358 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
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
                this.treatInvalidState("asignación múltiple");
            }
        }
break;
case 58:
//#line 405 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 60:
//#line 414 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 61:
//#line 419 "gramatica.y"
{

            /* Conversión de la lista de variables a arreglo de strings, eliminando espacios alrededor de cada elemento.*/
            String[] variables = val_peek(2).sval.split("\\s*,\\s*");
            String lastVariable = variables[variables.length - 1];

            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                lastVariable, val_peek(0).sval));
            errorState = true;

            /* Se agrega una coma para respetar el formato en reglas siguientes.*/
            /* Si no se agregara la coma, de entrar nuevamente a esta regla, la separación de las variables no*/
            /* funcionaría adecuadamente.*/
            yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval;
        }
break;
case 63:
//#line 442 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 64:
//#line 447 "gramatica.y"
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
case 66:
//#line 470 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 67:
//#line 478 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 68:
//#line 482 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 69:
//#line 489 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 499 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 71:
//#line 501 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 72:
//#line 508 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 74:
//#line 517 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 75:
//#line 524 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 76:
//#line 531 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 78:
//#line 540 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 79:
//#line 547 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 80:
//#line 549 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 81:
//#line 557 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 82:
//#line 561 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 84:
//#line 572 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 576 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 88:
//#line 587 "gramatica.y"
{
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 89:
//#line 603 "gramatica.y"
{
            if (!this.symbolTable.entryExists(this.scopeStack.appendScope(val_peek(0).sval))) {
                /* De entrar acá, la variable debe ser local.*/
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
case 90:
//#line 616 "gramatica.y"
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
case 91:
//#line 640 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 92:
//#line 651 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 93:
//#line 655 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 94:
//#line 657 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 664 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            this.reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 96:
//#line 672 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 97:
//#line 674 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 676 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 683 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 100:
//#line 687 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 105:
//#line 698 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 106:
//#line 707 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia 'if'."); 
            } else {
                this.treatInvalidState("Sentencia 'if'");
            }

            /* Se está saliendo del if más externo.*/
            if (this.selectionDepth == 1) {
                if (this.returnsNeeded == this.returnsFound) {
                    this.isThereReturn = true;
                }
            }

            this.selectionDepth--;
        }
break;
case 107:
//#line 730 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 108:
//#line 737 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'if' body...");
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("FB");
            this.returnsNeeded++;
            this.selectionDepth++;
        }
break;
case 110:
//#line 754 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 111:
//#line 756 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 112:
//#line 758 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 113:
//#line 765 "gramatica.y"
{
            /* Se decrementa la cantidad de retornos que se requieren si el if está solo.*/
            this.returnsNeeded--;

            /* Se decrementa la cantidad de returns hallados.*/
            /* REVISAR QUÉ PASA SI DENTRO DEL IF HAY VARIOS RETURNS.*/
            this.returnsFound--;
        }
break;
case 115:
//#line 778 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 116:
//#line 786 "gramatica.y"
{
            /* Se obtiene la promesa del cuerpo then.*/
            Promise promise = this.reversePolish.getLastPromise();

            /* Se promete un nuevo punto de bifurcación.*/
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("IB");

            /* Se cumple la promesa obtenida al comienzo.*/
            /* Es necesario que se realice así para respetar los índices de la polaca.*/
            this.reversePolish.fulfillPromise(promise);

            this.reversePolish.addSeparation("Entering 'else' body...");
        }
break;
case 117:
//#line 808 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            } else {
                this.treatInvalidState("Sentencia 'do-while'");
            }
        }
break;
case 118:
//#line 822 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 119:
//#line 824 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 120:
//#line 832 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.stackBifurcationPoint();
        }
break;
case 122:
//#line 846 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 123:
//#line 848 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 125:
//#line 863 "gramatica.y"
{
            if (!this.errorState) {

                if (this.isThereReturn) {

                    this.isThereReturn = false;
                    notifyDetection("Declaración de función.");

                    this.reversePolish.closeFunctionDeclaration(this.scopeStack.appendScope(val_peek(4).sval));

                    this.reversePolish.addPolish("end-label");
                    this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                    this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                    this.scopeStack.pop();
                    this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                    this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(4).sval));
                } else {
                    notifyError("La función necesita, en todos los casos, retornar un valor.");
                    this.errorState = true;
                }
            } else {
                this.treatInvalidState("Declaración de función");
            }

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }
break;
case 126:
//#line 895 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }
break;
case 127:
//#line 912 "gramatica.y"
{

            this.reversePolish.startFunctionDeclaration(val_peek(0).sval + ":" + this.scopeStack.asText());

            yyval.sval = val_peek(0).sval;
            this.functionLevel++;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));

            /* Se crea un operador para la función, mediante el operador 'label'.*/
            this.reversePolish.addPolish(val_peek(0).sval);
            this.reversePolish.addPolish("label");

            this.returnsNeeded = 1;
        }
break;
case 128:
//#line 931 "gramatica.y"
{
            errorState = true;
            this.functionLevel++;
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");

            this.returnsNeeded = 1;
        }
break;
case 132:
//#line 958 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 135:
//#line 970 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 138:
//#line 984 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());

                this.reversePolish.addParameter(val_peek(0).sval, "uint", val_peek(2).sval);
            } else {
                this.treatInvalidState("Parámetro formal");
            }
        }
break;
case 139:
//#line 999 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 140:
//#line 1001 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 141:
//#line 1008 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 142:
//#line 1010 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 143:
//#line 1015 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 144:
//#line 1024 "gramatica.y"
{

            if (statementAppearsInValidState()) {

                if (this.functionLevel > 0) {

                    this.reversePolish.makeTemporalPolishesDefinitive();
                    reversePolish.addPolish("return");
                    notifyDetection("Sentencia 'return'.");

                    this.returnsFound++;

                    if (this.selectionDepth == 0) {
                        this.isThereReturn = true;
                    }

                } else {
                    notifyError("La sentencia 'return' no está permitida fuera de la declaración de una función.");
                }
            } else {

                this.reversePolish.emptyTemporalPolishes();

                this.treatInvalidState("return");
            }
        }
break;
case 145:
//#line 1054 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 146:
//#line 1056 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 147:
//#line 1058 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 148:
//#line 1060 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 149:
//#line 1069 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 150:
//#line 1074 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 151:
//#line 1081 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {

                yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';

                this.reversePolish.closeFunctionCall();
            } else {
                this.treatInvalidState("Invocación de función");

                this.reversePolish.discardFunctionCall();
            }
        }
break;
case 152:
//#line 1099 "gramatica.y"
{
            String[] parts = val_peek(0).sval.split("\\s*:\\s*");
            String functionInvocationIdentifier;

            /* Se pasa el nombre de la función al final.*/
            /* Si se tiene A:B:C:D, se obtiene B:C:D.*/

            if (parts.length > 1) {
                String result = String.join(":", 
                    Arrays.copyOfRange(parts, 1, parts.length)) + ":" + parts[0];
                functionInvocationIdentifier = result;
            } else {
                functionInvocationIdentifier = val_peek(0).sval; /* Solo hay un elemento.*/
            }

            this.reversePolish.startFunctionCall(val_peek(0).sval);
        }
break;
case 154:
//#line 1123 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 155:
//#line 1130 "gramatica.y"
{

            this.reversePolish.addArgument(val_peek(0).sval);
        }
break;
case 156:
//#line 1138 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
break;
case 157:
//#line 1147 "gramatica.y"
{
            if (this.statementAppearsInValidState()) {
                /* Se añaden las polacas correspondiente al imprimible.*/
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            } else {
                this.reversePolish.emptyTemporalPolishes();
                this.treatInvalidState("Sentencia 'print'");
            }
        }
break;
case 158:
//#line 1162 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 160:
//#line 1177 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 161:
//#line 1179 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 162:
//#line 1185 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 163:
//#line 1192 "gramatica.y"
{ reversePolish.addTemporalPolish(val_peek(0).sval); }
break;
case 165:
//#line 1202 "gramatica.y"
{ 
            if (this.statementAppearsInValidState()) {

                /* Se llena el punto de agregación reservado con la asignación*/
                /* del argumento al parámetro.*/
                this.reversePolish.fillLastAggregatePoint(val_peek(3).sval, val_peek(1).sval, ":=");

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");

            } else {
                this.treatInvalidState("Expresión 'lambda'");
            }
        }
break;
case 166:
//#line 1220 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 167:
//#line 1223 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 168:
//#line 1225 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 169:
//#line 1227 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 170:
//#line 1234 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 171:
//#line 1239 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 172:
//#line 1242 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 173:
//#line 1249 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
        }
break;
//#line 1906 "Parser.java"
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
