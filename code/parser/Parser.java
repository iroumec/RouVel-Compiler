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
    import semantic.ScopeType;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 40 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 36 "Parser.java"




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
    3,    2,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    1,    3,    2,    3,    1,    3,    1,
    1,    2,    1,    1,    1,    1,    1,    1,    2,    1,
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
    0,    0,    0,   60,    0,    0,   62,   43,   36,    0,
   34,    0,    0,    0,    0,   63,    0,   22,   18,    0,
   29,   27,  117,    0,   30,    0,   31,  107,    0,    0,
  120,    0,    0,    0,  123,    6,   91,    0,   89,  159,
    0,   86,   71,   72,    0,   78,    0,    0,   87,    0,
   76,   82,  157,  156,   93,    0,  102,  104,  103,  105,
  106,  100,  101,    0,    0,    0,   94,    0,   47,   52,
   46,    0,    0,    0,  148,   16,    0,    0,   53,    0,
  144,  143,    0,  133,    0,    0,  134,  136,    0,   57,
    0,    0,   61,   56,   33,    0,    0,    0,    0,    0,
    0,   35,    0,    0,  152,    0,   58,    0,   65,    0,
    0,    0,    0,  115,  125,  124,  122,  119,  118,  158,
    0,   68,    0,   75,   73,   95,   92,    0,    0,    0,
   51,  147,    0,   19,   20,  172,  138,  141,    0,    0,
  132,  127,  130,    0,   55,   54,   32,    0,  170,    0,
  165,  164,    0,  167,    0,    0,  150,   59,   64,   13,
    0,  112,  113,   79,   77,   48,  146,  145,  139,  135,
  126,  131,  166,  169,  168,  154,  153,  111,  110,
};
final static short yydgoto[] = {                          4,
   64,   54,   55,  125,  126,  127,   56,   18,   57,   58,
   59,  184,  185,  146,  165,   21,  178,    5,   70,   71,
   22,   23,   97,   24,   25,   26,    6,    8,  158,   27,
  100,   28,   29,   30,   31,   32,  102,  106,  107,   93,
   33,   34,   35,   36,   37,   38,   39,   66,   67,   40,
  108,  109,  110,   41,  114,  115,   81,  224,  166,  167,
  168,   60,   61,
};
final static short yysindex[] = {                         3,
   67,    0,    0,    0,  -28,  -96,    0, -207,   -1,   19,
   -5,  493, -196,    0,  503,  -40, -193,   41,  467,  -46,
   92,   73,   -2,    0,    0,   95,  -19,    0,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,    0,  -17,
   13,    0,  -96,    0, -135,    0,    0,   65, -129,    0,
    0,  522,  273,    7,    0,  346, -125,    0,    0,  -43,
    0,    0,  529,  338,    7,    0,   86,    0,    0, -130,
  120,    0,  536,  147,   -7,    0,    0,   24, -124,  -23,
   15,  542, -118,    0,  229,  130,    0,    0,    0,  106,
    0,  103,  118,   54,   96,    0,   71,    0,    0,   36,
    0,    0,    0,  106,    0, -123,    0,    0, -122,  122,
    0,   26,  -38,  -42,    0,    0,    0,    7,    0,    0,
  109,    0,    0,    0,    7,    0,  471,    0,    0,  250,
    0,    0,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    7,  471,   54,    0,   96,    0,    0,
    0, -124,   97,   99,    0,    0,   29,   30,    0,  124,
    0,    0, -242,    0, -213,   34,    0,    0,   47,    0,
   43,  -16,    0,    0,    0,  136,  -35,  -20,  103,    6,
  -97,    0,  166,   38,    0,   75,    0,   96,    0,   58,
  140,  -44,  102,    0,    0,    0,    0,    0,    0,    0,
  295,    0,    7,    0,    0,    0,    0,    7,  273,  107,
    0,    0,   64,    0,    0,    0,    0,    0,  -90, -242,
    0,    0,    0,   69,    0,    0,    0,  -88,    0,  128,
    0,    0,  -86,    0,  -84,   54,    0,    0,    0,    0,
   78,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        25,
  -82,    0,    0,    0,  177,  177,    0,    0,    0,  375,
   94,    0,  138,    0,    0,    0,    0,    0,  141,    0,
    0,    0,    0,    0,    0,  182,   81,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,  -83,
    0,    0,  177,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   21,  301,    0,    0,  141,    0,    0,    0,
    0,    0,    0,    0,  429,    0,    0,    0,  238,  395,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -211,
    0,    0,    0,    0,    0,  141,    0,    0,    0,    0,
    0,  112,  -71,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   -3,    0,    0,    0,  101,
    0,    0,    0,    0,    0,    0,    0,  397,    0,    0,
    0,    0,    0,    0,  407,    0,    0,  385,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  438,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  187,  188,    0,    0,
    0,    0, -211,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -71,    0,    0,  -71,    0,
    0,    0,   50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  419,    0,    0,    0,    0,  460,   49,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   84,  -33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   11,  757,  411,  129,    0,  131,  711,    0,  662,  420,
  737,    0,  -39,    0,    0,    0,  -62,  190,  -60,    0,
    0,   17,  104,    0,   27,    8,    0,    0,    0,    0,
    0,   -6,  692,    0,    0,    0,    0,   14,  175,  -56,
    0,    0,    0,    0,    0,    0,    0,   39,  137,    0,
    0,   98,    0,    0,    0,   88,    0,    0,    0, -131,
    0,    0,  150,
};
final static int YYTABLESIZE=961;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,   63,    2,   51,   48,  229,   49,  137,   50,   49,
  137,   17,   89,  161,  242,  134,  199,  164,  160,   98,
  163,   53,   17,   78,    3,   74,   16,   83,  162,   85,
  181,  217,   42,  176,   52,   87,   51,   48,  232,   49,
   28,   50,   49,  218,   84,  142,  229,  191,   51,    3,
   49,   44,   17,   50,  113,  114,  219,   44,  142,   68,
   69,  163,   53,   17,   45,   63,    7,   51,   48,  116,
   49,   98,   50,  207,  221,   17,   79,  220,  237,  163,
   80,  236,   76,  154,   77,  123,   17,  124,  250,   96,
  155,  211,  171,  155,   16,   51,   48,   17,   49,  173,
   50,  226,   87,   99,  183,  104,   51,  190,   17,   49,
  101,   50,   94,  228,  188,   49,  233,  156,  188,   49,
   12,  117,  248,  194,  140,   28,  147,  140,  119,  187,
  132,   17,  159,  148,   17,  104,  259,  169,   10,  213,
   49,  123,  177,  124,  103,   17,  193,    3,  157,  200,
  195,  196,  161,  214,  215,  212,  209,  180,  234,  116,
  243,   17,  223,  152,  216,  246,  249,  253,  254,  255,
  171,  222,  256,   83,    5,  180,   15,  129,  151,   17,
  151,    2,  240,   98,  171,  114,   10,   11,   87,  123,
   84,  124,  144,  251,  145,   92,  257,   43,  186,  136,
  197,  121,    0,  192,    0,  155,    0,    0,  123,   88,
  124,    0,  133,  198,   90,   75,   10,  252,   10,   46,
    0,   10,   46,  142,  241,   11,   12,    9,   10,   13,
  175,   14,  161,   15,  112,  231,  142,   11,   12,   10,
   10,   13,  179,   14,  104,   15,  183,  162,   11,   12,
  103,   10,   46,   47,   14,   46,   15,   28,    1,    3,
  227,   95,   10,   46,  227,  114,   28,   28,  111,   10,
   28,  123,   28,  124,   28,   79,  163,  128,   11,   12,
   10,   53,   10,   46,   14,  112,   15,  174,  206,   11,
   12,    9,   10,   13,   49,   14,   53,   15,  225,   10,
  122,   11,   12,   10,   96,   13,    0,   14,    0,   15,
   10,   46,   11,   12,   10,  123,   13,  124,   14,  247,
   15,   10,   46,   11,   12,   10,    0,   13,   46,   14,
  238,   15,   46,  258,   11,   12,   12,   12,   13,   49,
   14,   66,   15,   66,   66,   66,   12,   12,   10,  161,
   12,   10,   12,   46,   12,   10,  122,   11,   12,   66,
   11,   12,   10,   14,   13,   15,   14,  171,   15,  116,
    0,   11,   12,    0,   10,  149,  150,   14,   10,   15,
  123,    0,  124,   11,   12,  132,   10,   11,   12,   14,
   49,   15,   10,   14,    0,   15,   10,  143,  141,  142,
    0,   11,   12,   10,  122,   11,   12,   14,    0,   15,
    0,   14,    0,   15,   90,   90,   90,   90,   90,   90,
    0,   90,   10,  122,  151,   85,   85,   85,   85,   85,
  235,   85,    0,   90,   90,   90,   90,   70,   50,   70,
   70,   70,   96,   85,   85,   85,   85,   69,    0,   69,
   69,   69,    0,   50,    0,   70,   70,   70,   70,   67,
    0,   67,   67,   67,    0,   69,  131,    0,    0,   99,
    0,   66,    0,   66,    0,    0,    0,   67,   97,    0,
   69,    0,   69,    0,    0,   10,  122,    0,   66,   66,
   66,    0,    0,   53,   53,    0,    0,   69,   69,   69,
   98,   53,   67,    0,   67,  204,   10,   46,   51,   48,
   83,   49,   51,   50,   96,   49,  189,   50,    0,   67,
   67,   67,    0,    0,    0,    0,    0,   84,    0,   10,
  122,    0,   63,    0,   51,   48,    0,   49,    0,   50,
  205,    0,   73,    0,   51,   48,    0,   49,    0,   50,
  244,   10,   46,    0,    0,    0,   66,   66,   66,    0,
    0,    0,  120,   51,   48,   66,   49,  210,   50,  135,
   51,   48,    0,   49,    0,   50,  153,   51,   48,    0,
   49,    0,   50,   51,   48,    0,   49,  230,   50,    0,
  230,    0,    0,    0,   10,  122,    0,  137,  138,  139,
  140,    0,   10,   46,    0,  189,    0,  239,    0,    0,
    0,  245,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,   90,   90,    0,   90,   90,   90,   90,   90,   90,
   85,   85,   85,    0,   85,   85,   85,   85,    0,   85,
   50,   50,   70,   70,   70,    0,   70,   70,   70,   70,
    0,   70,   69,   69,   69,    0,   19,    0,    0,    0,
    0,   69,    0,    0,   67,   67,   67,   19,    0,    0,
   86,    0,   19,   67,   99,   66,   66,   19,   66,   66,
   66,   66,    0,   97,   69,   69,    0,   69,   69,   69,
   69,   19,   19,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   91,    0,  128,   98,   67,   67,    0,   67,
   67,   67,   67,   10,   46,  128,  202,   10,   46,    0,
   82,  105,  105,    0,    0,  128,    0,    0,    0,   19,
    0,   20,    0,    0,  172,    0,  128,  172,   62,   10,
   46,   19,   20,    0,   19,    0,    0,   20,   72,   10,
   46,   19,   20,    0,  130,   19,    0,    0,   65,    0,
    0,   19,    0,    0,    0,  130,   20,   20,   10,   46,
   47,   91,    0,    0,  182,   10,   46,    0,    0,  129,
    0,    0,   10,   46,    0,   91,    0,  170,   10,   46,
  129,  105,    0,    0,  118,    0,    0,    0,    0,    0,
  129,    0,    0,    0,   20,  128,    0,    0,    0,   65,
    0,  129,    0,    0,    0,    0,   20,    0,  130,   20,
   19,    0,  128,  172,    0,  201,   20,   19,    0,    0,
   20,    0,    0,    0,  128,    0,   20,    0,    0,    0,
    0,   19,   19,    0,  201,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  182,   65,   65,
  128,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  182,  203,    0,   19,    0,    0,    0,    0,
  129,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  208,    0,    0,    0,   20,    0,  129,    0,    0,
    0,    0,   20,  130,    0,    0,    0,    0,  130,  129,
    0,    0,    0,    0,    0,    0,   20,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  129,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   20,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,    0,   42,   43,   41,   45,   41,   47,   45,
   44,   40,   59,  256,   59,   59,   59,   41,   79,   26,
   44,   11,   40,   16,    0,   15,  123,   44,  271,   19,
   93,  163,    6,   90,   40,   19,   42,   43,   59,   45,
   40,   47,   45,  257,   61,  257,   41,  104,   42,  257,
   45,   59,   40,   47,   41,   59,  270,   59,  270,  256,
  257,   41,   52,   40,   46,   40,    0,   42,   43,   43,
   45,   78,   47,   41,   41,   40,  270,   44,   41,   59,
   40,   44,  123,   73,  125,   43,   40,   45,  220,   41,
   41,  152,   82,   44,  123,   42,   43,   40,   45,   83,
   47,   59,   86,  123,   94,  123,   42,  100,   40,   45,
   59,   47,   40,  176,   44,   45,  179,  125,   44,   45,
   40,  257,   59,  110,   41,  125,   41,   44,  258,   59,
  256,   40,  257,  264,   40,  123,   59,  123,  257,   41,
   45,   43,   40,   45,  268,   40,  269,  123,  125,   41,
  112,  113,   59,  125,  125,   59,  146,   40,  256,   59,
   59,   40,  169,   44,   41,   59,  257,  256,   41,  256,
   59,  125,  257,   44,  257,   40,    0,   40,   59,   40,
   40,    0,  125,  190,  256,  269,    0,    0,  172,   43,
   61,   45,   64,  125,   64,   21,  236,    8,   95,   63,
  113,   52,   -1,  106,   -1,   59,   -1,   -1,   43,  256,
   45,   -1,  256,  256,  123,  256,  257,  224,  257,  258,
   -1,  257,  258,  257,  269,  266,  267,  256,  257,  270,
  125,  272,  256,  274,  273,  256,  270,  266,  267,  257,
  257,  270,  125,  272,  123,  274,  236,  271,  266,  267,
  268,  257,  258,  259,  272,  258,  274,  257,  256,  257,
  125,  264,  257,  258,  125,  269,  266,  267,  256,  257,
  270,   43,  272,   45,  274,  270,  256,   40,  266,  267,
  257,   44,  257,  258,  272,  273,  274,   59,  256,  266,
  267,  256,  257,  270,   45,  272,   59,  274,  256,  257,
  258,  266,  267,  257,  256,  270,   -1,  272,   -1,  274,
  257,  258,  266,  267,  257,   43,  270,   45,  272,  256,
  274,  257,  258,  266,  267,  257,   -1,  270,  258,  272,
  256,  274,  258,  256,  266,  267,  256,  257,  270,   45,
  272,   41,  274,   43,   44,   45,  266,  267,  257,  256,
  270,  257,  272,  258,  274,  257,  258,  266,  267,   59,
  266,  267,  257,  272,  270,  274,  272,  256,  274,  269,
   -1,  266,  267,   -1,  257,  256,  257,  272,  257,  274,
   43,   -1,   45,  266,  267,  256,  257,  266,  267,  272,
   45,  274,  257,  272,   -1,  274,  257,   60,   61,   62,
   -1,  266,  267,  257,  258,  266,  267,  272,   -1,  274,
   -1,  272,   -1,  274,   40,   41,   42,   43,   44,   45,
   -1,   47,  257,  258,   40,   41,   42,   43,   44,   45,
  265,   47,   -1,   59,   60,   61,   62,   41,   44,   43,
   44,   45,   23,   59,   60,   61,   62,   41,   -1,   43,
   44,   45,   -1,   59,   -1,   59,   60,   61,   62,   41,
   -1,   43,   44,   45,   -1,   59,   56,   -1,   -1,   41,
   -1,   43,   -1,   45,   -1,   -1,   -1,   59,   41,   -1,
   43,   -1,   45,   -1,   -1,  257,  258,   -1,   60,   61,
   62,   -1,   -1,  256,  257,   -1,   -1,   60,   61,   62,
   41,  264,   43,   -1,   45,  256,  257,  258,   42,   43,
   44,   45,   42,   47,   95,   45,   97,   47,   -1,   60,
   61,   62,   -1,   -1,   -1,   -1,   -1,   61,   -1,  257,
  258,   -1,   40,   -1,   42,   43,   -1,   45,   -1,   47,
  130,   -1,   40,   -1,   42,   43,   -1,   45,   -1,   47,
  256,  257,  258,   -1,   -1,   -1,  256,  257,  258,   -1,
   -1,   -1,   41,   42,   43,  265,   45,  148,   47,   41,
   42,   43,   -1,   45,   -1,   47,   41,   42,   43,   -1,
   45,   -1,   47,   42,   43,   -1,   45,  177,   47,   -1,
  180,   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,
  263,   -1,  257,  258,   -1,  186,   -1,  188,   -1,   -1,
   -1,  201,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,  264,  265,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
  256,  257,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,  265,  256,  257,  258,   -1,    5,   -1,   -1,   -1,
   -1,  265,   -1,   -1,  256,  257,  258,   16,   -1,   -1,
   19,   -1,   21,  265,  256,  257,  258,   26,  260,  261,
  262,  263,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,   40,   41,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   21,   -1,   53,  256,  257,  258,   -1,  260,
  261,  262,  263,  257,  258,   64,  256,  257,  258,   -1,
  264,   40,   41,   -1,   -1,   74,   -1,   -1,   -1,   78,
   -1,    5,   -1,   -1,   83,   -1,   85,   86,  256,  257,
  258,   90,   16,   -1,   93,   -1,   -1,   21,  256,  257,
  258,  100,   26,   -1,   54,  104,   -1,   -1,   12,   -1,
   -1,  110,   -1,   -1,   -1,   65,   40,   41,  257,  258,
  259,   90,   -1,   -1,   93,  257,  258,   -1,   -1,   53,
   -1,   -1,  257,  258,   -1,  104,   -1,  256,  257,  258,
   64,  110,   -1,   -1,   48,   -1,   -1,   -1,   -1,   -1,
   74,   -1,   -1,   -1,   78,  154,   -1,   -1,   -1,   63,
   -1,   85,   -1,   -1,   -1,   -1,   90,   -1,  118,   93,
  169,   -1,  171,  172,   -1,  125,  100,  176,   -1,   -1,
  104,   -1,   -1,   -1,  183,   -1,  110,   -1,   -1,   -1,
   -1,  190,  191,   -1,  144,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  176,  112,  113,
  209,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  191,  127,   -1,  224,   -1,   -1,   -1,   -1,
  154,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  145,   -1,   -1,   -1,  169,   -1,  171,   -1,   -1,
   -1,   -1,  176,  203,   -1,   -1,   -1,   -1,  208,  183,
   -1,   -1,   -1,   -1,   -1,   -1,  190,  191,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  209,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  224,
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
"list_of_variables : variable list_of_variables",
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

//#line 1204 "gramatica.y"

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
private boolean isThereReturn;
private ScopeType lastScopeEntered;

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
//#line 818 "Parser.java"
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
//#line 82 "gramatica.y"
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
//#line 94 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 97 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 99 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 102 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 105 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 112 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
            this.reversePolish.recordSafeState();
        }
break;
case 11:
//#line 128 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 131 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 134 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 136 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 138 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 21:
//#line 159 "gramatica.y"
{ this.treatErrorState(); }
break;
case 22:
//#line 161 "gramatica.y"
{ this.treatErrorState(); }
break;
case 23:
//#line 166 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 209 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 225 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 236 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 252 "gramatica.y"
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
//#line 265 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 48:
//#line 269 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 49:
//#line 273 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 283 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 52:
//#line 288 "gramatica.y"
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
//#line 311 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 54:
//#line 325 "gramatica.y"
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
//#line 351 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 56:
//#line 354 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 57:
//#line 357 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 58:
//#line 367 "gramatica.y"
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
//#line 412 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 61:
//#line 421 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 62:
//#line 426 "gramatica.y"
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
//#line 449 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 65:
//#line 454 "gramatica.y"
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
//#line 477 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 68:
//#line 485 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 69:
//#line 489 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 70:
//#line 496 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 71:
//#line 506 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 72:
//#line 508 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 73:
//#line 515 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 75:
//#line 524 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 76:
//#line 531 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 77:
//#line 538 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 79:
//#line 547 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 80:
//#line 554 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 81:
//#line 556 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 82:
//#line 565 "gramatica.y"
{
            /* TODO: esto es un parche. Debe verse mejor después.*/
            this.errorCollector.removeLast(); /* Debido a que se usa el token error.*/
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 83:
//#line 571 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 85:
//#line 582 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 86:
//#line 586 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 89:
//#line 597 "gramatica.y"
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
//#line 613 "gramatica.y"
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
//#line 626 "gramatica.y"
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
//#line 650 "gramatica.y"
{ 
            if (!errorState) {
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 93:
//#line 661 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 665 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 667 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 674 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            this.reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 97:
//#line 682 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 684 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 686 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 100:
//#line 693 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 101:
//#line 697 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 106:
//#line 708 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 107:
//#line 717 "gramatica.y"
{ 
            if (!errorState) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                this.reversePolish.addSeparation("Leaving 'if-else' body...");
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 108:
//#line 731 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 109:
//#line 738 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'if' body...");
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("FB");
            this.lastScopeEntered = ScopeType.IF;
        }
break;
case 111:
//#line 754 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 112:
//#line 756 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 113:
//#line 758 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 116:
//#line 770 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 778 "gramatica.y"
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

            this.lastScopeEntered = ScopeType.ELSE;
        }
break;
case 118:
//#line 802 "gramatica.y"
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
//#line 814 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 120:
//#line 816 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 121:
//#line 824 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.stackBifurcationPoint();
        }
break;
case 123:
//#line 838 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 124:
//#line 840 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 126:
//#line 855 "gramatica.y"
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
        }
break;
case 127:
//#line 879 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.errorState = true;
        }
break;
case 128:
//#line 892 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            this.functionLevel++;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
        }
break;
case 129:
//#line 902 "gramatica.y"
{
            this.scopeStack.push("error");
            this.functionLevel++;
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 133:
//#line 927 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 136:
//#line 939 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 139:
//#line 953 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 140:
//#line 964 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 141:
//#line 966 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 142:
//#line 973 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 143:
//#line 975 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 144:
//#line 980 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 145:
//#line 989 "gramatica.y"
{
            if (!this.errorState && this.functionLevel > 0) {
                this.reversePolish.makeTemporalPolishesDefinitive();
                reversePolish.addPolish("return");
                notifyDetection("Sentencia 'return'.");

                if (this.lastScopeEntered == ScopeType.IF) {
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
//#line 1012 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 147:
//#line 1014 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 148:
//#line 1016 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 149:
//#line 1018 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 150:
//#line 1027 "gramatica.y"
{
            if (!errorState) {

                yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';

                String[] arguments = val_peek(3).sval.split("\\s*,\\s*");

                this.reversePolish.addPolish(String.format("%s[%d]", val_peek(3).sval, arguments.length));
            
            }

            this.functionInvocationIdentifier = null; /* TODO: cambiar a StringBuilder.*/
        }
break;
case 151:
//#line 1046 "gramatica.y"
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
//#line 1066 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 154:
//#line 1073 "gramatica.y"
{
            yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
            
            this.reversePolish.addPolish(val_peek(0).sval + ":" + this.functionInvocationIdentifier);

            /* Se agrega la expresión.*/
            this.reversePolish.makeTemporalPolishesDefinitive();

            this.reversePolish.addPolish("->");
        }
break;
case 155:
//#line 1087 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); errorState = true; }
break;
case 156:
//#line 1096 "gramatica.y"
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
//#line 1108 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 159:
//#line 1123 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 160:
//#line 1125 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 161:
//#line 1131 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 162:
//#line 1138 "gramatica.y"
{ reversePolish.addTemporalPolish(val_peek(0).sval); }
break;
case 164:
//#line 1148 "gramatica.y"
{ 
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
//#line 1163 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 166:
//#line 1166 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 167:
//#line 1168 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 168:
//#line 1170 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 169:
//#line 1177 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 170:
//#line 1182 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 171:
//#line 1185 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 172:
//#line 1192 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
        }
break;
//#line 1808 "Parser.java"
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
