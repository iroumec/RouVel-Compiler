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
    import java.util.Arrays;
    import semantic.Promise;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 39 "gramatica.y"
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
    0,    0,   27,    0,   28,    0,    0,    0,   18,   25,
   25,   31,   25,   25,   25,   25,   30,   30,   29,   29,
   26,   26,   26,   32,   32,   34,   34,   37,   37,   38,
   38,   39,   39,   40,   40,   33,   33,   33,   33,   33,
   33,   33,   33,   42,   42,   35,   35,   35,   35,   20,
   20,   20,   19,   41,   41,   41,   41,   24,   24,   22,
   22,   22,   23,   23,   23,    1,    1,    1,    1,    1,
    6,    6,    2,    2,    2,    2,    4,    4,    4,    7,
    7,    3,    3,    3,    5,    5,    5,   10,   10,    9,
    9,   48,   48,   48,   48,   49,   49,   49,   49,   14,
   14,   14,   14,   14,   14,   14,   46,   46,   50,   51,
   51,   51,   51,   52,   52,   52,   53,   47,   47,   47,
   54,   55,   55,   55,   56,   36,   36,    8,    8,   58,
   58,   57,   57,   59,   59,   59,   61,   61,   60,   60,
   60,   15,   15,   15,   43,   43,   43,   43,   43,   11,
   21,   12,   12,   13,   13,   44,   44,   62,   62,   62,
   62,   63,   63,   45,   45,   45,   45,   45,   17,   17,
   17,   16,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    5,    2,    1,
    3,    2,    1,    4,    4,    3,    3,    3,    4,    2,
    3,    3,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    1,    3,    2,    3,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    2,    1,
    3,    3,    2,    2,    3,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    2,    2,    2,    4,
    4,    3,    3,    0,    2,    1,    1,    3,    3,    2,
    1,    2,    1,    2,    2,    5,    4,    2,    1,    1,
    2,    3,    2,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    5,    5,    4,    3,    2,    4,
    1,    1,    3,    3,    1,    3,    3,    3,    2,    1,
    0,    1,    1,    4,    4,    5,    4,    5,    3,    2,
    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  121,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   38,    1,    0,    0,   21,   24,   25,
   26,    0,   37,   39,   40,   41,   42,   44,   45,    0,
    0,    4,    0,   23,    0,   88,  162,    0,    0,   80,
   81,    0,    0,    0,   74,    0,    0,   83,   84,    0,
  160,  108,    0,    0,    0,  109,    0,   49,    0,    0,
    0,  149,    0,    0,    0,   17,   14,    0,    0,    0,
    0,    0,    0,    0,   60,    0,   43,   36,    0,   34,
    0,    0,    0,    0,   63,    0,   22,   18,    0,   29,
   27,  117,    0,   30,    0,   31,  107,    0,    0,  120,
    0,    0,    0,  123,    6,   91,    0,   89,  159,    0,
   86,   71,   72,    0,   78,    0,    0,   87,    0,   76,
  157,  156,   93,    0,  102,  104,  103,  105,  106,  100,
  101,    0,    0,    0,   94,    0,   47,   52,   46,    0,
    0,    0,  148,   16,    0,    0,   53,    0,  144,  143,
    0,  133,    0,    0,  134,  136,    0,    0,   62,   57,
    0,   61,   56,   33,    0,    0,    0,    0,    0,    0,
   35,    0,    0,  152,    0,   58,    0,   65,    0,    0,
    0,    0,  115,  125,  124,  122,  119,  118,  158,    0,
   68,    0,   75,   73,   95,   92,    0,    0,    0,   51,
  147,    0,   19,   20,  172,  138,  141,    0,    0,  132,
  127,  130,    0,   55,   54,   32,    0,  170,    0,  165,
  164,    0,  167,    0,    0,  150,   59,   64,   13,    0,
  112,  113,   79,   77,   48,  146,  145,  139,  135,  126,
  131,  166,  169,  168,  154,  153,  111,  110,
};
final static short yydgoto[] = {                          4,
   64,   54,   55,  124,  125,  126,   56,   18,   57,   58,
   59,  183,  184,  144,  163,   21,  177,    5,   70,   71,
   22,   23,   96,   24,   25,   26,    6,    8,  156,   27,
   99,   28,   29,   30,   31,   32,  101,  105,  106,   92,
   33,   34,   35,   36,   37,   38,   39,   66,   67,   40,
  107,  108,  109,   41,  113,  114,   81,  223,  164,  165,
  166,   60,   61,
};
final static short yysindex[] = {                         3,
   31,    0,    0,    0,  -28,  -99,    0, -219,  -19,   28,
  481,  491, -127,    0,  500,  -40, -210,   62,  466,  -44,
   92,   67,  -11,    0,    0,   95,  -34,    0,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,    0,  -17,
   13,    0,  -99,    0, -142,    0,    0,   26, -141,    0,
    0,  507,   99,   12,    0,  325,    0,    0,    0,  -43,
    0,    0,  520,  338,   12,    0,   84,    0,    0, -133,
  120,    0,  528,  147,  -22,    0,    0,   24, -118,  -23,
   27, -117,  534, -117,    0,  559,    0,    0,  106,    0,
  101,  118,  542,   30,    0,    4,    0,    0,   36,    0,
    0,    0,  106,    0, -125,    0,    0, -122,  122,    0,
   54,  -38,  -42,    0,    0,    0,   12,    0,    0,  110,
    0,    0,    0,   12,    0,  470,    0,    0,  350,    0,
    0,    0,    0,   -2,    0,    0,    0,    0,    0,    0,
    0,   12,  470,  542,    0,   30,    0,    0,    0, -118,
   93,   43,    0,    0,   29,   32,    0,  112,    0,    0,
 -229,    0, -212,   37,    0,    0,   47,  -31,    0,    0,
  496,    0,    0,    0,  136,  -35,   18,  101,    6, -101,
    0,  166,   38,    0,   60,    0,   30,    0,   58,  140,
  -13,   97,    0,    0,    0,    0,    0,    0,    0,  356,
    0,   12,    0,    0,    0,    0,   12,   99,  100,    0,
    0,   33,    0,    0,    0,    0,    0,  -97, -229,    0,
    0,    0,   69,    0,    0,    0,  -93,    0,  124,    0,
    0,  -89,    0,  -88,  542,    0,    0,    0,    0,   49,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        14,
  -87,    0,    0,    0,  168,  168,    0,    0,    0,  375,
   53,    0,  131,    0,    0,    0,    0,    0,  133,    0,
    0,    0,    0,    0,    0,  177,   81,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,  -85,
    0,    0,  168,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -15,  301,    0,    0,  385,    0,    0,    0,
    0,    0,    0,    0,  436,    0,    0,    0,  238,  130,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -205,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,  -75,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -3,    0,    0,    0,    8,    0,
    0,    0,    0,    0,    0,    0,  397,    0,    0,    0,
    0,    0,    0,  419,    0,    0,  408,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  445,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  182,  185,    0,    0,    0,    0,
 -205,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -75,    0,    0,  -75,    0,    0,
    0,   78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  430,    0,    0,    0,    0,  458,   16,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   83,  -33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   17,  732,   -1,  123,    0,  127,   -4,    0,  720,   20,
  729,    0,  -49,    0,    0,    0,  -30,  180,  -59,    0,
    0,  -12,  102,    0,   23,   19,    0,    0,    0,    0,
    0,  653,  722,    0,    0,    0,    0,  -16,  172,  -70,
    0,    0,    0,    0,    0,    0,    0,   22,  132,    0,
    0,   96,    0,    0,    0,   85,    0,    0,    0, -139,
    0,    0,  148,
};
final static int YYTABLESIZE=952;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,   63,    2,   51,   48,  228,   49,  137,   50,   49,
  137,   17,   84,    3,   88,  132,  198,  162,  175,  158,
  161,  216,   17,   16,  112,  163,  159,   53,   42,   85,
    7,   74,  190,   49,   78,   86,   44,    3,  206,   44,
   28,  160,   95,  163,  217,  241,  228,  187,   49,  129,
   49,  142,   17,   51,  130,  114,   96,  218,   50,   79,
  129,  180,  186,   17,  142,  115,  116,   51,   53,  169,
   49,  172,   50,   45,   49,   17,  231,  220,  236,  249,
  219,  235,   76,  212,   77,  122,   17,  123,   98,  152,
  210,  247,  193,   63,   16,   51,   48,   17,   49,  171,
   50,   80,  154,  187,   49,  103,   93,  258,   17,  182,
  100,  161,  129,   95,  116,  188,  118,  189,  155,  200,
   12,  155,  171,  140,  145,   28,  140,  204,   68,   69,
  146,   17,  194,  195,   17,  103,    3,  200,  157,   10,
  176,  122,  102,  123,  227,   17,  192,  232,  155,  167,
  199,  211,  215,  213,  233,  242,  214,  179,  245,  248,
  208,   17,  252,  150,  253,  209,  254,   15,  255,    5,
  129,  221,  151,   50,  229,  179,    2,  229,  149,   17,
  171,   10,  239,  114,   11,  256,  142,   43,   50,  122,
  143,  123,   91,  250,  134,  185,  196,  129,  244,  120,
  191,    0,  129,    0,  188,  153,  238,    0,  122,    0,
  123,   87,  131,  197,   89,   75,   10,    0,   10,   46,
    0,   10,   46,  142,   82,   11,   12,    9,   10,   13,
  174,   14,  159,   15,  111,    0,  142,   11,   12,   10,
  163,   13,  178,   14,  103,   15,   46,  160,   11,   12,
  102,  182,   94,  205,   14,  240,   15,   28,    1,    3,
  226,   46,   10,   46,  226,  114,   28,   28,  110,   10,
   28,   96,   28,  230,   28,   79,  116,  128,   11,   12,
   10,   53,   10,   46,   14,  111,   15,   46,  246,   11,
   12,    9,   10,   13,    0,   14,   53,   15,    0,   10,
  121,   11,   12,   10,  257,   13,    0,   14,  161,   15,
   10,   46,   11,   12,   10,  237,   13,   46,   14,  171,
   15,    0,    0,   11,   12,   10,    0,   13,    0,   14,
    0,   15,    0,    0,   11,   12,   12,   12,   13,    0,
   14,   66,   15,   66,   66,   66,   12,   12,   10,    0,
   12,   10,   12,    0,   12,   10,  121,   11,   12,   66,
   11,   12,   10,   14,   13,   15,   14,    0,   15,   49,
    0,   11,   12,    0,   10,  147,  148,   14,   10,   15,
  122,    0,  123,   11,   12,   50,   50,   11,   12,   14,
    0,   15,   10,   14,   49,   15,   10,  141,  139,  140,
   49,   11,   12,   10,  121,   11,   12,   14,    0,   15,
    0,   14,    0,   15,   90,   90,   90,   90,   90,   90,
    0,   90,   10,  121,  151,   82,   82,   82,   82,   82,
  234,   82,    0,   90,   90,   90,   90,   70,    0,   70,
   70,   70,    0,   82,   82,   82,   82,  151,   85,   85,
   85,   85,   85,    0,   85,   70,   70,   70,   70,   69,
    0,   69,   69,   69,    0,    0,   85,   85,   85,   85,
   67,    0,   67,   67,   67,    0,   99,   69,   66,    0,
   66,    0,    0,    0,    0,   97,    0,   69,   67,   69,
    0,    0,    0,   53,   53,   66,   66,   66,   98,    0,
   67,   53,   67,    0,   69,   69,   69,   51,   48,   84,
   49,   51,   50,    0,   49,    0,   50,   67,   67,   67,
   52,    0,   51,   48,    0,   49,   85,   50,    0,    0,
   63,    0,   51,   48,    0,   49,    0,   50,  122,   73,
  123,   51,   48,    0,   49,    0,   50,  119,   51,   48,
    0,   49,    0,   50,  225,    0,   66,   66,   66,    0,
  133,   51,   48,    0,   49,   66,   50,    0,  151,   51,
   48,    0,   49,    0,   50,   51,   48,    0,   49,    0,
   50,   10,   46,   51,   48,    0,   49,    0,   50,    0,
    0,    0,    0,    0,   10,  121,    0,  135,  136,  137,
  138,  122,    0,  123,    0,  203,   10,   46,    0,    0,
    0,  243,   10,   46,    0,    0,    0,  173,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,   90,   90,    0,   90,   90,   90,   90,   90,   90,
   82,   82,   82,    0,   82,   82,   82,   82,    0,   82,
    0,    0,   70,   70,   70,    0,   70,   70,   70,   70,
    0,   70,    0,   85,   85,   85,    0,   85,   85,   85,
   85,    0,   85,    0,   69,   69,   69,    0,   97,    0,
    0,    0,    0,   69,    0,   67,   67,   67,    0,    0,
    0,   99,   66,   66,   67,   66,   66,   66,   66,    0,
   97,   69,   69,    0,   69,   69,   69,   69,    0,    0,
    0,    0,    0,   98,   67,   67,    0,   67,   67,   67,
   67,   82,   10,   46,   19,  201,   10,   46,    0,   83,
   97,    0,    0,   20,    0,   19,    0,   10,   46,   47,
   19,    0,   90,   65,   20,   19,   62,   10,   46,   20,
    0,  224,   10,  121,   20,   72,   10,   46,    0,   19,
   19,  104,  104,   10,   46,   47,    0,    0,   20,   20,
    0,    0,  127,    0,    0,    0,   10,   46,    0,  117,
    0,  128,    0,  127,   10,   46,    0,    0,    0,  170,
   10,   46,  128,  127,   65,    0,    0,   19,   10,   46,
    0,  168,  128,  168,    0,  127,   20,    0,   19,    0,
   90,   19,    0,  181,  128,   10,  121,   20,   19,  222,
   20,    0,   19,    0,   90,    0,    0,   20,   19,    0,
  104,   20,    0,    0,    0,    0,    0,   20,    0,    0,
    0,   97,   65,   65,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  202,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  127,    0,    0,  207,  251,    0,    0,    0,    0,
  128,    0,    0,    0,    0,    0,   19,    0,    0,    0,
  127,    0,    0,    0,   19,   20,  181,    0,    0,  128,
    0,  127,    0,   20,    0,    0,    0,    0,   19,   19,
  128,  181,    0,    0,    0,    0,    0,   20,   20,    0,
    0,    0,    0,    0,    0,    0,    0,  127,    0,    0,
    0,    0,    0,    0,    0,    0,  128,    0,    0,    0,
    0,    0,   19,    0,    0,    0,    0,    0,    0,    0,
    0,   20,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,    0,   42,   43,   41,   45,   41,   47,   45,
   44,   40,   44,    0,   59,   59,   59,   41,   89,   79,
   44,  161,   40,  123,   41,   41,  256,   11,    6,   61,
    0,   15,  103,   45,   16,   19,   59,  257,   41,   59,
   40,  271,   23,   59,  257,   59,   41,   44,   45,   54,
   45,  257,   40,   42,   56,   59,   41,  270,   47,  270,
   65,   92,   59,   40,  270,   43,   59,   42,   52,   82,
   45,   84,   47,   46,   45,   40,   59,   41,   41,  219,
   44,   44,  123,   41,  125,   43,   40,   45,  123,   73,
  150,   59,  109,   40,  123,   42,   43,   40,   45,   83,
   47,   40,  125,   44,   45,  123,   40,   59,   40,   93,
   59,   59,  117,   94,  257,   96,  258,   99,   41,  124,
   40,   44,   59,   41,   41,  125,   44,  129,  256,  257,
  264,   40,  111,  112,   40,  123,  123,  142,  257,  257,
   40,   43,  268,   45,  175,   40,  269,  178,  125,  123,
   41,   59,   41,  125,  256,   59,  125,   40,   59,  257,
  144,   40,  256,   44,   41,  146,  256,    0,  257,  257,
   40,  125,   40,   44,  176,   40,    0,  179,   59,   40,
  256,    0,  125,  269,    0,  235,   64,    8,   59,   43,
   64,   45,   21,  125,   63,   94,  112,  202,  200,   52,
  105,   -1,  207,   -1,  185,   59,  187,   -1,   43,   -1,
   45,  256,  256,  256,  123,  256,  257,   -1,  257,  258,
   -1,  257,  258,  257,  256,  266,  267,  256,  257,  270,
  125,  272,  256,  274,  273,   -1,  270,  266,  267,  257,
  256,  270,  125,  272,  123,  274,  258,  271,  266,  267,
  268,  235,  264,  256,  272,  269,  274,  257,  256,  257,
  125,  258,  257,  258,  125,  269,  266,  267,  256,  257,
  270,  256,  272,  256,  274,  270,  269,   40,  266,  267,
  257,   44,  257,  258,  272,  273,  274,  258,  256,  266,
  267,  256,  257,  270,   -1,  272,   59,  274,   -1,  257,
  258,  266,  267,  257,  256,  270,   -1,  272,  256,  274,
  257,  258,  266,  267,  257,  256,  270,  258,  272,  256,
  274,   -1,   -1,  266,  267,  257,   -1,  270,   -1,  272,
   -1,  274,   -1,   -1,  266,  267,  256,  257,  270,   -1,
  272,   41,  274,   43,   44,   45,  266,  267,  257,   -1,
  270,  257,  272,   -1,  274,  257,  258,  266,  267,   59,
  266,  267,  257,  272,  270,  274,  272,   -1,  274,   45,
   -1,  266,  267,   -1,  257,  256,  257,  272,  257,  274,
   43,   -1,   45,  266,  267,  256,  257,  266,  267,  272,
   -1,  274,  257,  272,   45,  274,  257,   60,   61,   62,
   45,  266,  267,  257,  258,  266,  267,  272,   -1,  274,
   -1,  272,   -1,  274,   40,   41,   42,   43,   44,   45,
   -1,   47,  257,  258,   40,   41,   42,   43,   44,   45,
  265,   47,   -1,   59,   60,   61,   62,   41,   -1,   43,
   44,   45,   -1,   59,   60,   61,   62,   40,   41,   42,
   43,   44,   45,   -1,   47,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,   -1,   -1,   59,   60,   61,   62,
   41,   -1,   43,   44,   45,   -1,   41,   59,   43,   -1,
   45,   -1,   -1,   -1,   -1,   41,   -1,   43,   59,   45,
   -1,   -1,   -1,  256,  257,   60,   61,   62,   41,   -1,
   43,  264,   45,   -1,   60,   61,   62,   42,   43,   44,
   45,   42,   47,   -1,   45,   -1,   47,   60,   61,   62,
   40,   -1,   42,   43,   -1,   45,   61,   47,   -1,   -1,
   40,   -1,   42,   43,   -1,   45,   -1,   47,   43,   40,
   45,   42,   43,   -1,   45,   -1,   47,   41,   42,   43,
   -1,   45,   -1,   47,   59,   -1,  256,  257,  258,   -1,
   41,   42,   43,   -1,   45,  265,   47,   -1,   41,   42,
   43,   -1,   45,   -1,   47,   42,   43,   -1,   45,   -1,
   47,  257,  258,   42,   43,   -1,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,
  263,   43,   -1,   45,   -1,  256,  257,  258,   -1,   -1,
   -1,  256,  257,  258,   -1,   -1,   -1,   59,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,  264,  265,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,  265,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,  256,  257,  258,   -1,   26,   -1,
   -1,   -1,   -1,  265,   -1,  256,  257,  258,   -1,   -1,
   -1,  256,  257,  258,  265,  260,  261,  262,  263,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,   -1,
   -1,   -1,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,  256,  257,  258,    5,  256,  257,  258,   -1,  264,
   78,   -1,   -1,    5,   -1,   16,   -1,  257,  258,  259,
   21,   -1,   21,   12,   16,   26,  256,  257,  258,   21,
   -1,  256,  257,  258,   26,  256,  257,  258,   -1,   40,
   41,   40,   41,  257,  258,  259,   -1,   -1,   40,   41,
   -1,   -1,   53,   -1,   -1,   -1,  257,  258,   -1,   48,
   -1,   53,   -1,   64,  257,  258,   -1,   -1,   -1,  256,
  257,  258,   64,   74,   63,   -1,   -1,   78,  257,  258,
   -1,   82,   74,   84,   -1,   86,   78,   -1,   89,   -1,
   89,   92,   -1,   92,   86,  257,  258,   89,   99,  167,
   92,   -1,  103,   -1,  103,   -1,   -1,   99,  109,   -1,
  109,  103,   -1,   -1,   -1,   -1,   -1,  109,   -1,   -1,
   -1,  189,  111,  112,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  126,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  152,   -1,   -1,  143,  223,   -1,   -1,   -1,   -1,
  152,   -1,   -1,   -1,   -1,   -1,  167,   -1,   -1,   -1,
  171,   -1,   -1,   -1,  175,  167,  175,   -1,   -1,  171,
   -1,  182,   -1,  175,   -1,   -1,   -1,   -1,  189,  190,
  182,  190,   -1,   -1,   -1,   -1,   -1,  189,  190,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  208,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  208,   -1,   -1,   -1,
   -1,   -1,  223,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  223,
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
"multiple_assignment : list_of_variables DASIG list_of_constants error",
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

//#line 1241 "gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

private final Lexer lexer;
private boolean errorState;
private final ScopeStack scopeStack;
private final SymbolTable symbolTable;
private final ReversePolish reversePolish;
private String functionInvocationIdentifier;
private MessageCollector errorCollector, warningCollector;

// --------------------------------------------------------------------------------------------------------------------

private int functionLevel;
// Si esto está activa, todas las instrucciones que se encuentran no serán pasadas a código intermedio.
private boolean isThereReturn;

// --------------------------------------------------------------------------------------------------------------------

private int returnsFound;
private int returnsNeeded;
private int selectionDepth;

// --------------------------------------------------------------------------------------------------------------------

public Parser(Lexer lexer, MessageCollector errorCollector, MessageCollector warningCollector) {
    
    if (lexer == null) {
        throw new IllegalStateException("El analizador sintáctico requiere de la designación de un analizador léxico..");
    }

    this.lexer = lexer;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
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
        lexer.getNroLinea(), message
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
//#line 827 "Parser.java"
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
//#line 81 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Programa.");
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(1).sval));
            } else {
                errorState = false;
            }
        }
break;
case 2:
//#line 93 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 96 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 98 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 101 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 104 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 111 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.recordSafeState();
        }
break;
case 11:
//#line 127 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 130 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 133 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 135 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 137 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 21:
//#line 158 "gramatica.y"
{ this.treatErrorState(); }
break;
case 22:
//#line 160 "gramatica.y"
{ this.treatErrorState(); }
break;
case 23:
//#line 165 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 208 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 224 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 235 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 251 "gramatica.y"
{
            if (!errorState) {

                if (val_peek(1).sval.split("\\s*,\\s*").length == 1) {
                    notifyDetection("Declaración de variable.");
                } else {
                    notifyDetection("Declaración de variables.");
                }
            }
        }
break;
case 47:
//#line 264 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 48:
//#line 268 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 49:
//#line 272 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 282 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 52:
//#line 287 "gramatica.y"
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
//#line 310 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 54:
//#line 324 "gramatica.y"
{ 

            if (!errorState) {
                
                notifyDetection("Asignación simple.");

                /* El valor aún no debe calcularse.*/
                /* this.symbolTable.setValue($1, $3);*/

                reversePolish.addPolish(val_peek(3).sval);

                this.reversePolish.makeTemporalPolishesDefinitive();

                reversePolish.addPolish(val_peek(2).sval);
            } else {

                /* Se decrementan las referencias, puesto a que se está frente a una referencia no válida.*/
                this.symbolTable.removeEntry(val_peek(3).sval);
                this.symbolTable.removeEntry(val_peek(1).sval);
            }
        }
break;
case 55:
//#line 350 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 56:
//#line 353 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 57:
//#line 356 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 58:
//#line 366 "gramatica.y"
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
            }
        }
break;
case 59:
//#line 411 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 61:
//#line 420 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 62:
//#line 425 "gramatica.y"
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
case 64:
//#line 448 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 65:
//#line 453 "gramatica.y"
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
//#line 476 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 68:
//#line 484 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 69:
//#line 488 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 495 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 71:
//#line 505 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 72:
//#line 507 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 73:
//#line 514 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 75:
//#line 523 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 530 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 77:
//#line 537 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 79:
//#line 546 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 80:
//#line 553 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 81:
//#line 555 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 82:
//#line 563 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 83:
//#line 567 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 578 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 86:
//#line 582 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 89:
//#line 593 "gramatica.y"
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
//#line 609 "gramatica.y"
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
//#line 622 "gramatica.y"
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
//#line 646 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 93:
//#line 657 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 661 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 663 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 670 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            this.reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 97:
//#line 678 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 680 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 682 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 100:
//#line 689 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 101:
//#line 693 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 106:
//#line 704 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 107:
//#line 713 "gramatica.y"
{ 
            if (!errorState) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
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
case 108:
//#line 736 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 109:
//#line 743 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'if' body...");
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("FB");
            this.returnsNeeded++;
            this.selectionDepth++;
        }
break;
case 111:
//#line 760 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 112:
//#line 762 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 113:
//#line 764 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 114:
//#line 771 "gramatica.y"
{
            /* Se decrementa la cantidad de retornos que se requieren si el if está solo.*/
            this.returnsNeeded--;

            /* Se decrementa la cantidad de returns hallados.*/
            /* REVISAR QUÉ PASA SI DENTRO DEL IF HAY VARIOS RETURNS.*/
            this.returnsFound--;
        }
break;
case 116:
//#line 784 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 792 "gramatica.y"
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
case 118:
//#line 814 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            }
        }
break;
case 119:
//#line 826 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 120:
//#line 828 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 121:
//#line 836 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.stackBifurcationPoint();
        }
break;
case 123:
//#line 850 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 124:
//#line 852 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 126:
//#line 867 "gramatica.y"
{
            if (!errorState) {

                if (this.isThereReturn) {

                    this.isThereReturn = false;
                    notifyDetection("Declaración de función.");
                    this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                    this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                    this.scopeStack.pop();
                    this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                    this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(4).sval));
                } else {
                    notifyError("La función necesita, en todos los casos, retornar un valor.");
                    this.errorState = true;
                }
            }

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }
break;
case 127:
//#line 893 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;

            this.functionLevel--;
            this.returnsFound = 0;
            this.returnsNeeded = 0;
        }
break;
case 128:
//#line 910 "gramatica.y"
{
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
case 129:
//#line 926 "gramatica.y"
{
            errorState = true;
            this.functionLevel++;
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");

            this.returnsNeeded = 1;
        }
break;
case 133:
//#line 953 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 136:
//#line 965 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 139:
//#line 979 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 140:
//#line 990 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 141:
//#line 992 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 142:
//#line 999 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 143:
//#line 1001 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 144:
//#line 1006 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 145:
//#line 1015 "gramatica.y"
{
            if (!this.errorState && this.functionLevel > 0) {
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("return");
                notifyDetection("Sentencia 'return'.");

                this.returnsFound++;

                if (this.selectionDepth == 0) {
                    this.isThereReturn = true;
                }

            } else {
                
                this.reversePolish.emptyTemporalPolishes();
                
                if (this.functionLevel == 0) {
                    notifyError("La sentencia 'return' no está permitida fuera de la declaración de una función.");
                }
            }
        }
break;
case 146:
//#line 1040 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 147:
//#line 1042 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 148:
//#line 1044 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 149:
//#line 1046 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 150:
//#line 1055 "gramatica.y"
{
            if (!errorState) {

                yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';

                this.reversePolish.addPolish(val_peek(3).sval);
                this.reversePolish.addPolish("call");
            }

            this.functionInvocationIdentifier = null; /* TODO: cambiar a StringBuilder.*/
        }
break;
case 151:
//#line 1072 "gramatica.y"
{
            String[] parts = val_peek(0).sval.split("\\s*:\\s*");

            /* Se pasa el nombre de la función al final.*/
            /* Si se tiene A:B:C:D, se obtiene B:C:D.*/

            if (parts.length > 1) {
                String result = String.join(":", 
                    Arrays.copyOfRange(parts, 1, parts.length)) + ":" + parts[0];
                this.functionInvocationIdentifier = result;
            } else {
                this.functionInvocationIdentifier = val_peek(0).sval; /* Solo hay un elemento.*/
            }
        }
break;
case 153:
//#line 1093 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 154:
//#line 1100 "gramatica.y"
{
            yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;

            String formalParameter = val_peek(0).sval + ":" + this.functionInvocationIdentifier;
            
            this.reversePolish.addPolish(formalParameter);

            /* Se agrega la expresión.*/
            this.reversePolish.makeTemporalPolishesDefinitive();

            this.reversePolish.addPolish(":=");

            this.symbolTable.replaceEntry(val_peek(0).sval, formalParameter);
        }
break;
case 155:
//#line 1118 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
break;
case 156:
//#line 1127 "gramatica.y"
{
            if (!errorState) {
                /* Se añaden las polacas correspondiente al imprimible.*/
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("print");
                notifyDetection("Sentencia 'print'.");
            }
        }
break;
case 157:
//#line 1139 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 159:
//#line 1154 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 160:
//#line 1156 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 161:
//#line 1162 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 162:
//#line 1169 "gramatica.y"
{ reversePolish.addTemporalPolish(val_peek(0).sval); }
break;
case 164:
//#line 1179 "gramatica.y"
{ 
            if (!this.isThereReturn) { /* isThereReturnInScope*/
                /* OK*/
            } else {
                notifyWarning("Está función 'lambda' no es alcanzable debido a que hay un sentencia de retorno arriba.");
            }

            if (!errorState) {

                /* Se llena el punto de agregación reservado con la asignación*/
                /* del argumento al parámetro.*/
                this.reversePolish.fillLastAggregatePoint(val_peek(3).sval, val_peek(1).sval, ":=");

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");
            }
        }
break;
case 165:
//#line 1200 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 166:
//#line 1203 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 167:
//#line 1205 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 168:
//#line 1207 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 169:
//#line 1214 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 170:
//#line 1219 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 171:
//#line 1222 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 172:
//#line 1229 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
        }
break;
//#line 1858 "Parser.java"
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
