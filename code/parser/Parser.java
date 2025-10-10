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
   49,   49,   49,   49,   49,   39,   39,   39,   39,   39,
   50,   50,   50,   40,   40,   40,   51,   52,   52,   52,
   53,   28,   28,   55,   55,   56,   54,   54,   57,   57,
   57,   59,   59,   58,   58,   58,   60,   60,   60,   36,
   36,   36,   36,   36,   12,   13,   13,   14,   14,   37,
   37,   62,   62,   62,   62,   61,   63,   63,   38,   38,
   38,   38,   38,   66,   66,   66,   65,   64,
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
    1,    1,    1,    1,    1,    6,    6,    5,    2,    5,
    0,    2,    1,    3,    3,    2,    2,    1,    1,    2,
    2,    6,    5,    1,    2,    3,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    5,
    5,    4,    3,    2,    4,    1,    3,    3,    1,    3,
    3,    1,    2,    1,    0,    3,    1,    1,    4,    4,
    5,    4,    5,    1,    2,    0,    3,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    0,
    0,   22,   25,   26,   27,    0,   38,   39,   40,   41,
   42,   43,   45,   46,    0,    0,    9,   10,    5,    0,
   24,    0,    0,   97,  167,    0,    0,   89,   90,    0,
    0,    0,   83,    0,    0,   92,   93,    0,    0,    0,
  119,    0,    0,    0,   51,    0,    0,    0,    0,  126,
    0,    0,   31,    0,   32,    0,    0,    0,  154,    0,
    0,    0,   18,   15,    0,    0,   60,    0,    0,   67,
    0,    0,    0,    0,   44,   37,   23,   19,    0,   30,
   28,   63,   61,    0,    0,   35,    0,    0,    0,    7,
  100,    0,    0,  156,    0,   98,    0,    0,   95,   80,
   81,    0,   87,    0,   94,   96,    0,   85,   91,  160,
  161,  102,    0,    0,    0,    0,    0,   49,   56,   48,
    0,    0,  149,  148,    0,    0,    0,  139,  141,    0,
   50,   55,   47,    0,    0,  131,   34,    0,    0,  127,
  124,  125,    0,    0,  153,    0,    0,    0,    0,    0,
    0,   68,   59,   65,   69,    0,    0,    0,   72,    0,
    0,    0,    0,    0,    0,   36,    0,    0,    0,    0,
    0,  155,  166,    0,   77,    0,   84,   82,  122,    0,
    0,  111,  113,  112,  114,  115,  109,  110,    0,  106,
  105,  101,   53,    0,  143,    0,    0,    0,  146,   54,
    0,   33,  152,    0,   20,   21,  178,   58,   57,    0,
   66,   14,   64,   62,    0,   74,    0,    0,    0,  169,
  170,    0,  172,   11,  158,  157,   88,   86,    0,  118,
  120,    0,    0,    0,  133,  134,  140,  144,   52,  151,
  150,   70,   73,  171,  177,  173,  117,  116,  132,  135,
    0,  136,
};
final static short yydgoto[] = {                          3,
   51,   52,   53,  122,  123,  124,   54,   68,  178,   16,
   56,   17,  113,  114,   18,   19,   20,   39,    4,    6,
  168,   21,   99,   22,   23,   24,   25,   26,  101,   74,
   75,  108,   27,   28,   29,   30,   31,   32,   33,   34,
   35,   93,   94,  231,   63,   64,  137,  212,  209,  135,
   76,   77,   78,  146,  255,  256,  147,  148,  149,  150,
   58,   59,   60,   36,  187,  183,
};
final static short yysindex[] = {                      -184,
   93,   60,    0,  -96,    0, -228,  -92,  -14,   96,   -2,
   31,   72,  515,   36, -189,  419,   38,    0,    0,  170,
    4,    0,    0,    0,    0,   51,    0,    0,    0,    0,
    0,    0,    0,    0,  -35,   23,    0,    0,    0,   39,
    0, -140,  428,    0,    0,   19, -129,    0,    0,  443,
   41,  -12,    0,   78,  -90,    0,    0,  113,  -89,    0,
    0,  132,  -27,  428,    0,    3,  -28,  346,  -87,    0,
  134,  182,    0,  -25,    0,  122,  -78,    0,    0,  -24,
  330,   59,    0,    0,  102,  -75,    0,  428,  -71,    0,
  353,  -90,    6,   84,    0,    0,    0,    0,  114,    0,
    0,    0,    0,    6,  182,    0,  149,  186,  114,    0,
    0,   66,  -13,    0,  -12,    0,    0,  150,    0,    0,
    0,  -12,    0,  449,    0,    0,  166,    0,    0,    0,
    0,    0,  202,  -76,  -79,  317,   14,    0,    0,    0,
  -64,  -28,    0,    0, -202,  153,  152,    0,    0, -137,
    0,    0,    0,  -60,    6,    0,    0,  215,    0,    0,
    0,    0,  139,   62,    0,    0,   75,   76,  161,  342,
  158,    0,    0,    0,    0,    6,  125,   57,    0,  226,
    5,  146,  -50,  149,  -39,    0,    0,  -49,  136,  -48,
  428,    0,    0,  260,    0,  -12,    0,    0,    0,  -37,
  154,    0,    0,    0,    0,    0,    0,    0,  428,    0,
    0,    0,    0,  174,    0,   94, -202,  -71,    0,    0,
  164,    0,    0,   56,    0,    0,    0,    0,    0,  176,
    0,    0,    0,    0,    6,    0,    9,    0,  216,    0,
    0,   53,    0,    0,    0,    0,    0,    0,   81,    0,
    0,   41,   94,   91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  147,    0,
};
final static short yyrindex[] = {                        25,
   -1,  281,    0,  281,    0,    0,    0,  460,   65,  525,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  300,
  159,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,  470,    0,    0,    0,    0,    0,  -78,    0,   77,
    0,  539,   35,    0,    0,   54,  -21,    0,    0,    0,
  525,    0,    0,  525,    0,   86,    0,  -90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  109,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   87,   87,    0,    0,
    0,  108,    0,    0,  482,    0,  100,    0,    0,    0,
    0,  492,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   32,   45,    0,   27,    0,    0,    0,    0,
    0,  -21,    0,    0, -123,    0,  308,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  111,    0,
    0,    0,    0,    0,    0,   12,  322,  350,    0,    0,
   -5,    0,    0,    0,    0,    0,    0,    0,    0,   87,
    0,  127,    0,   87,    0,    0,  127,    0,    0,    0,
    0,    0,    0,    0,    0,  504,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -33,  119,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   33,
    0,    0,    0,    0,    0,    0,    0,  138,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  577,  -32,  411,    0,    0,    0,  454,    0,    0,   -6,
  535,   44,    0,  173,   73,  348,  -10,    0,    0,    0,
    0,    0,    0,  -20,  561,    0,    0,    0,    0,   17,
  364,  -38,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  313,    0,    0,   48,    0,    0,    0,    0,  275,
    0,    0,  337,  276,  167,    0,    0, -103,    0,    0,
    0,    0,  378,    0,  323,  -29,
};
final static int YYTABLESIZE=797;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
   29,  238,   55,   85,   69,   47,   55,  142,  104,   92,
  142,   17,   15,  115,   62,  145,  163,   49,   46,  138,
   47,  250,   48,  103,    4,   43,   14,  192,   40,   49,
  191,   42,   41,  158,   48,   99,   55,   62,   99,   55,
   29,  215,  142,   55,  125,  238,  141,   55,   42,   47,
   47,   24,   57,  143,  211,   99,   57,   55,  168,   57,
   49,  140,   15,   47,   97,   48,  180,  108,  144,  107,
   67,    1,    2,   55,  125,   15,   37,   71,  188,  134,
   86,   55,  172,  120,  125,  121,   57,  175,  177,   57,
  123,  196,    5,   57,  126,   72,   96,   57,  189,   15,
  235,   47,  224,  121,  120,  125,  121,   57,  120,  100,
  121,   15,  110,  257,  261,  234,  111,   55,  156,  171,
   55,  159,   47,   57,  126,   29,   98,   89,  116,  125,
   15,   57,  218,  147,  126,   50,   24,   49,   46,  268,
   47,   15,   48,  219,   90,  105,  147,    4,  159,  199,
  237,  159,   69,   15,  242,  126,   97,  125,   83,  145,
   84,  109,  145,  125,   15,  129,  131,   57,   97,   69,
   57,  130,  132,   62,   55,   15,  155,  162,   55,  126,
  161,  169,   14,  166,   55,  171,   15,   55,  181,  201,
  193,  133,  213,  216,   72,  217,  220,  223,   13,  225,
  226,  227,   55,   42,  240,  241,  243,  126,  245,   15,
   47,  258,  251,  126,  253,  270,  254,    8,   44,  262,
  102,   15,  259,  147,   57,  185,  167,  143,   57,    8,
   86,  249,    8,   44,   57,  147,  147,   57,    9,   10,
  133,   15,  144,  271,   12,  125,   13,   71,  147,  232,
   97,   99,   57,   61,   15,    6,  265,   29,  138,  139,
  244,    8,   44,   44,  264,  185,   29,   29,   24,  210,
   29,  272,   29,  168,   29,    8,   44,   24,   24,    8,
   16,   24,  108,   24,  107,   24,   65,   66,    9,   10,
   71,   82,    8,   95,   12,  126,   13,    8,  119,    3,
  123,    9,   10,  121,   47,   11,  157,   12,  266,   13,
  184,  260,  233,  121,   44,    7,    8,   99,    8,  119,
  165,   11,    8,  119,   72,    9,   10,   70,    8,   11,
  190,   12,  164,   13,    8,   44,  267,    9,   10,  222,
  171,  128,  176,   12,   71,   13,    7,    8,  137,   12,
  222,   38,    8,   44,   45,  163,    9,   10,    8,  120,
   11,  121,   12,  246,   13,   69,  130,    9,   10,    7,
    8,   11,  120,   12,  121,   13,  208,  206,  207,    9,
   10,    8,  174,   11,  120,   12,  121,   13,  165,  154,
    9,   10,    8,  175,   11,  120,   12,  121,   13,  107,
  229,    9,   10,    8,  153,   11,  176,   12,  200,   13,
  160,  173,    9,   10,   13,   13,   11,  214,   12,  269,
   13,  197,    8,   44,   13,   13,    8,  118,   13,  182,
   13,    0,   13,    0,    0,    9,   10,    0,    8,   11,
    0,   12,    8,   13,    0,    0,    0,    9,   10,    0,
    0,    9,   10,   12,    0,   13,    0,   12,    8,   13,
   49,   46,   89,   47,  128,   48,    0,    9,   10,   49,
   46,    8,   47,   12,   48,   13,    0,    0,    0,   90,
    9,   10,    8,  117,   49,   46,   12,   47,   13,   48,
   49,    9,   10,   47,    0,   48,    0,   12,    0,   13,
   99,   99,   99,   99,   99,  127,   99,    0,    0,    0,
   75,    0,   75,   75,   75,  247,    8,   44,   99,   99,
   99,   99,   79,    0,   79,   79,   79,    0,   75,   75,
   75,   75,   78,    0,   78,   78,   78,  198,    0,    0,
   79,   79,   79,   79,   76,    0,   76,   76,   76,    0,
   78,   78,   78,   78,   80,    0,   49,   46,    0,   47,
    0,   48,   76,   76,   76,   76,  104,  104,  127,  104,
    0,  104,   73,    8,  119,  194,  202,  203,  204,  205,
  103,  103,    0,  103,    0,  103,    8,  119,    0,   81,
    0,  239,   91,    0,    0,  239,  106,  228,    8,  119,
    0,  151,  152,    0,  248,    0,    0,    0,    0,    8,
  119,    0,    0,    0,    0,    0,    0,    0,    0,  112,
    0,    0,    0,   73,    0,    0,    0,  174,    0,    0,
    0,    0,  106,    0,    0,    0,    0,    0,  179,    0,
  136,    0,    0,    0,    0,    0,    0,    0,    0,  127,
    0,    0,    0,    0,    0,    0,  164,    0,    0,    0,
    0,    0,    0,    0,  170,  106,    0,    0,  186,    0,
    0,    0,    0,    0,   87,    8,   44,    0,    0,    0,
    0,    0,   88,    0,    8,   44,    0,    0,    0,  221,
    0,    0,    0,   73,    0,    0,    0,    0,    0,    8,
   44,   45,    0,    0,  195,    8,   44,    0,    0,    0,
  230,    0,  236,    0,    0,   99,   99,   99,  186,   99,
   99,   99,   99,   99,   99,   75,   75,   75,    0,   75,
   75,   75,   75,    0,   75,    0,    0,   79,   79,   79,
  186,   79,   79,   79,   79,    0,   79,   78,   78,   78,
    0,   78,   78,   78,   78,    0,   78,    0,    0,   76,
   76,   76,    0,   76,   76,   76,   76,  112,   76,  263,
   79,    8,   44,    0,    0,    0,    0,    0,    0,    0,
    0,  104,  104,    0,    0,  252,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  103,  103,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,   41,    9,   14,   11,   45,   13,   41,   44,   16,
   44,    0,   40,   46,   40,   44,   41,   42,   43,   41,
   45,   59,   47,   59,    0,   40,  123,   41,  257,   42,
   44,   46,  125,   72,   47,   41,   43,   40,   44,   46,
   40,  145,   40,   50,   51,   41,   44,   54,   46,   45,
   45,   40,    9,  256,   41,   61,   13,   64,   41,   16,
   42,   59,   40,   45,   85,   47,  105,   41,  271,   41,
   40,  256,  257,   80,   81,   40,    4,   45,  108,   63,
  270,   88,   89,   43,   91,   45,   43,   94,   99,   46,
   59,  124,    0,   50,   51,  123,   59,   54,  109,   40,
   44,   45,   41,   59,   43,  112,   45,   64,   43,   59,
   45,   40,   40,  217,   59,   59,  257,  124,   71,  257,
  127,   74,   45,   80,   81,  125,  123,   44,  258,  136,
   40,   88,  270,  257,   91,   40,  125,   42,   43,   59,
   45,   40,   47,  150,   61,  123,  270,  123,   41,  133,
  180,   44,   44,   40,  184,  112,  177,  164,  123,   41,
  125,  123,   44,  170,   40,  256,  256,  124,  189,   61,
  127,   59,   41,   40,  181,   40,  264,  256,  185,  136,
   59,  257,  123,  125,  191,  257,   40,  194,   40,  269,
   41,  268,  257,   41,  123,   44,  257,   59,   40,  125,
  125,   41,  209,   46,   59,  256,  256,  164,  257,   40,
   45,  218,   59,  170,   41,  125,  123,  257,  258,   44,
  256,   40,   59,  257,  181,   40,  125,  256,  185,  257,
  270,  269,  257,  258,  191,  257,  270,  194,  266,  267,
  268,   40,  271,  254,  272,  252,  274,  273,  270,  125,
  271,  257,  209,  256,   40,  257,   41,  257,  256,  257,
  125,  257,  258,  258,  256,   40,  266,  267,  257,  256,
  270,  125,  272,  256,  274,  257,  258,  266,  267,  257,
    0,  270,  256,  272,  256,  274,  256,  257,  266,  267,
  258,  256,  257,  256,  272,  252,  274,  257,  258,    0,
  269,  266,  267,  269,   45,  270,  125,  272,  256,  274,
  125,  256,  256,  269,  258,  256,  257,  264,  257,  258,
  256,    0,  257,  258,  123,  266,  267,  256,  257,  270,
  265,  272,  256,  274,  257,  258,  256,  266,  267,  125,
  257,  256,  256,  272,  273,  274,  256,  257,   41,    0,
  125,    4,  257,  258,  259,  256,  266,  267,  257,   43,
  270,   45,  272,  191,  274,  257,  256,  266,  267,  256,
  257,  270,   43,  272,   45,  274,   60,   61,   62,  266,
  267,  257,  256,  270,   43,  272,   45,  274,   59,   44,
  266,  267,  257,  256,  270,   43,  272,   45,  274,   36,
   59,  266,  267,  257,   59,  270,   94,  272,  134,  274,
   74,   59,  266,  267,  256,  257,  270,  142,  272,  253,
  274,  256,  257,  258,  266,  267,  257,   50,  270,  107,
  272,   -1,  274,   -1,   -1,  266,  267,   -1,  257,  270,
   -1,  272,  257,  274,   -1,   -1,   -1,  266,  267,   -1,
   -1,  266,  267,  272,   -1,  274,   -1,  272,  257,  274,
   42,   43,   44,   45,   54,   47,   -1,  266,  267,   42,
   43,  257,   45,  272,   47,  274,   -1,   -1,   -1,   61,
  266,  267,  257,   41,   42,   43,  272,   45,  274,   47,
   42,  266,  267,   45,   -1,   47,   -1,  272,   -1,  274,
   41,   42,   43,   44,   45,   52,   47,   -1,   -1,   -1,
   41,   -1,   43,   44,   45,  256,  257,  258,   59,   60,
   61,   62,   41,   -1,   43,   44,   45,   -1,   59,   60,
   61,   62,   41,   -1,   43,   44,   45,  127,   -1,   -1,
   59,   60,   61,   62,   41,   -1,   43,   44,   45,   -1,
   59,   60,   61,   62,   40,   -1,   42,   43,   -1,   45,
   -1,   47,   59,   60,   61,   62,   42,   43,  115,   45,
   -1,   47,   12,  257,  258,  122,  260,  261,  262,  263,
   42,   43,   -1,   45,   -1,   47,  257,  258,   -1,   13,
   -1,  181,   16,   -1,   -1,  185,   36,  256,  257,  258,
   -1,  256,  257,   -1,  194,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   43,
   -1,   -1,   -1,   63,   -1,   -1,   -1,   93,   -1,   -1,
   -1,   -1,   72,   -1,   -1,   -1,   -1,   -1,  104,   -1,
   64,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  196,
   -1,   -1,   -1,   -1,   -1,   -1,   80,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   88,  105,   -1,   -1,  108,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,  264,   -1,  257,  258,   -1,   -1,   -1,  155,
   -1,   -1,   -1,  133,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
  176,   -1,  178,   -1,   -1,  256,  257,  258,  158,  260,
  261,  262,  263,  264,  265,  256,  257,  258,   -1,  260,
  261,  262,  263,   -1,  265,   -1,   -1,  256,  257,  258,
  180,  260,  261,  262,  263,   -1,  265,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,  256,
  257,  258,   -1,  260,  261,  262,  263,  191,  265,  235,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,   -1,  209,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,
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
"if : IF condicion rama_else ENDIF ';'",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"rama_else : ELSE",
"do_while : DO cuerpo_do ';'",
"do_while : DO cuerpo_do_recuperacion error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_do",
"cuerpo_do_recuperacion : fin_cuerpo_do",
"cuerpo_do_recuperacion : cuerpo_ejecutable condicion",
"fin_cuerpo_do : WHILE condicion",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion",
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

//#line 848 "gramatica.y"

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
    if (lexemaNuevo != null) {
        SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
    }
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 762 "Parser.java"
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
                notifyWarning("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
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
case 120:
//#line 560 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 123:
//#line 572 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 124:
//#line 581 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 125:
//#line 586 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 126:
//#line 588 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 129:
//#line 605 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 130:
//#line 607 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 133:
//#line 626 "gramatica.y"
{ notifyError("La función requiere de un nombre."); }
break;
case 135:
//#line 638 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 138:
//#line 655 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 667 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 685 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 687 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 149:
//#line 699 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 150:
//#line 708 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 151:
//#line 713 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 152:
//#line 715 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 153:
//#line 717 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 154:
//#line 719 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 155:
//#line 728 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 157:
//#line 738 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 158:
//#line 745 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 159:
//#line 750 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 160:
//#line 759 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 161:
//#line 764 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 163:
//#line 775 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 164:
//#line 778 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 165:
//#line 780 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 169:
//#line 803 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 170:
//#line 808 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 171:
//#line 810 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 172:
//#line 812 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 173:
//#line 814 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 175:
//#line 825 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 176:
//#line 828 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1343 "Parser.java"
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
