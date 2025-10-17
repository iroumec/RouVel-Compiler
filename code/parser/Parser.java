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
    0,    0,    0,   21,    0,   22,    0,    0,    0,   16,
   20,   20,   17,   18,   25,   18,   18,   18,   18,   24,
   24,   23,   23,   19,   19,   19,   26,   26,   28,   28,
   31,   31,   32,   32,   33,   33,   34,   34,   27,   27,
   27,   27,   27,   27,   27,   27,   37,   37,   29,   29,
   29,   29,   29,   29,    9,    9,    9,    9,   35,   35,
   35,   35,   36,   36,   36,   36,   36,   43,   43,   44,
   44,   45,   45,   10,   10,   10,    1,    1,    1,    1,
    1,    6,    6,    2,    2,    2,    2,    4,    4,    4,
    7,    7,    3,    3,    3,    5,    5,    5,   12,   12,
   11,   11,   46,   46,   46,   46,   47,   47,   47,   47,
   48,   48,   48,   48,   48,   48,   48,   41,   41,   41,
   41,   41,   49,   49,   49,   42,   42,   42,   42,   42,
   50,   51,   51,   52,   30,   30,   30,   30,    8,   55,
   54,   53,   53,   56,   56,   56,   58,   58,   57,   57,
   57,   59,   59,   59,   38,   38,   38,   38,   38,   13,
   14,   14,   15,   15,   39,   39,   39,   39,   60,   61,
   61,   61,   62,   62,   40,   40,   40,   40,   40,   40,
   40,   40,   40,   40,   64,   65,   65,   63,
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
    1,    1,    1,    1,    1,    1,    1,    6,    6,    5,
    5,    2,    0,    2,    1,    3,    3,    3,    3,    2,
    2,    1,    2,    2,    3,    4,    3,    4,    2,    1,
    3,    3,    2,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    5,    5,    4,    3,    2,    4,
    1,    3,    3,    1,    3,    3,    3,    3,    3,    2,
    1,    0,    1,    1,    4,    4,    4,    4,    5,    4,
    5,    5,    4,    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    9,   10,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    2,    0,    0,   24,   27,   28,   29,    0,   40,
   41,   42,   43,   44,   45,   47,   48,    0,    0,   11,
   12,    5,    0,   26,    0,    0,   99,  173,    0,    0,
   91,   92,    0,    0,    0,   85,    0,    0,   94,   95,
    0,    0,  171,  122,    0,    0,    0,    0,    0,   54,
    0,    0,    0,  130,    0,    0,   33,    0,   34,    0,
    0,  132,  159,    0,    0,    0,   20,   17,    0,    0,
    0,    0,    0,    0,   69,    0,    0,    0,    0,   46,
   39,   25,   21,    0,   32,   30,    0,    0,   37,    0,
    0,    0,    7,  102,    0,    0,  161,    0,  100,  170,
    0,   97,   82,   83,    0,   89,    0,   96,   98,    0,
   87,   93,  166,  165,  168,  167,  104,    0,  113,  115,
  114,  116,  117,  111,  112,    0,    0,    0,    0,    0,
    0,  105,   51,   58,   50,    0,   52,   57,   49,    0,
    0,  134,   36,    0,  133,  131,  128,  126,  129,  127,
    0,    0,  158,    0,    0,    0,    0,  154,  153,    0,
  143,    0,  144,  146,    0,    0,  135,   62,    0,    0,
   70,   61,    0,   71,    0,    0,    0,  137,    0,    0,
    0,    0,    0,    0,   38,    0,    0,    0,    0,    0,
  160,  169,    0,   79,    0,   86,   84,  106,  103,    0,
    0,  124,    0,    0,   55,   56,    0,   35,  157,    0,
   22,   23,  188,  148,    0,  142,    0,  151,  136,    0,
   60,   59,   65,   63,    0,    0,   74,    0,   68,   16,
  138,    0,    0,  186,    0,  176,  175,  178,  177,    0,
    0,  180,  183,   13,  163,  162,   90,   88,    0,  120,
  121,   53,  156,  155,  145,  149,  141,    0,   67,    0,
   76,   72,  179,  182,  185,  181,  184,  119,  118,   66,
   64,   75,
};
final static short yydgoto[] = {                          4,
   66,   55,   56,  125,  126,  127,   57,   18,   72,  246,
   19,   59,   60,  116,  117,    5,   21,   22,  240,   42,
    6,    8,  176,   24,  104,   25,   26,   27,   28,   29,
  106,   78,   79,  111,   30,   31,   32,   33,   34,   35,
   36,   37,   98,   99,  249,   68,   69,  148,  151,   80,
   81,   82,   92,  187,   38,  182,  183,  184,  185,   61,
   62,   63,   39,  201,  202,
};
final static short yysindex[] = {                         3,
   28,    0,    0,    0,  -26, -103,    0, -212,  -78,   13,
  438,  167, -188,   24,  564,  -38, -214,   58,  518,    7,
    0,    0,  136,  -27,    0,    0,    0,    0,   59,    0,
    0,    0,    0,    0,    0,    0,    0,   58,  160,    0,
    0,    0,    8,    0, -140,   66,    0,    0,  448, -139,
    0,    0,  399,  106,   23,    0,  143, -124,    0,    0,
   18,   20,    0,    0,  596,  572,   23,   -2,   92,    0,
  528,  406, -126,    0,  132,  164,    0,  115,    0,   43,
   45,    0,    0,  603,  402,   14,    0,    0,   60, -117,
  -19,   22,  362, -116,    0,  498, -124,  -13,  -40,    0,
    0,    0,    0,   72,    0,    0,   25,  164,    0,  110,
  182,   72,    0,    0,   30,   37,    0,   23,    0,    0,
  111,    0,    0,    0,   23,    0,  274,    0,    0,  298,
    0,    0,    0,    0,    0,    0,    0,  -33,    0,    0,
    0,    0,    0,    0,    0,   23,  274,   66,  186, -112,
 -101,    0,    0,    0,    0,  -91,    0,    0,    0,  -88,
  -13,    0,    0,  210,    0,    0,    0,    0,    0,    0,
  112,   79,    0,    0,   61,   62,  129,    0,    0, -223,
    0,   49,    0,    0, -215,   36,    0,    0,  456,  134,
    0,    0,   -9,    0,  -13,   83,   48,    0,  222,  161,
   55,   56,  110,  101,    0,  -73,  -63,   95,  -62,   66,
    0,    0,  410,    0,   23,    0,    0,    0,    0,   23,
  106,    0,  -54,  137,    0,    0,  138,    0,    0,   57,
    0,    0,    0,    0, -223,    0, -116,    0,    0,  113,
    0,    0,    0,    0,  -13,   -1,    0,  154,    0,    0,
    0,  -57,  -51,    0,  170,    0,    0,    0,    0,  -43,
  -32,    0,    0,    0,    0,    0,    0,    0,   69,    0,
    0,    0,    0,    0,    0,    0,    0,  119,    0,  -13,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                        31,
   -3,    0,    0,    0,  251,  251,    0,    0,    0,  478,
   75,    0,  237,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  294,  125,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -35,  370,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  530,   76,    0,    0,
  -25,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -203,    0,    0,    0,    0,    0,   -4,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   77,
   44,    0,    0,    0,   51,    0,    0,  508,    0,    0,
    0,    0,    0,    0,  428,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  541,    0,    0,  -42,   -8,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,  341,  348,    0,    0,    0, -203,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -15,
    0,    0,    0,    0,    0,    0,    0,    0,   44,    0,
    0,    0,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  468,    0,    0,    0,    0,  552,
  -31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -14,    0,   86,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   27,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  743,  604,  376,  285,    0,  288,  632,    0,    0,  121,
  451,  650,   -5,    0,  146,  352,   17,  368,   78,    0,
    0,    0,    0,    0,    0,   -7,  802,    0,    0,    0,
    0,   -6,  329,  -69,    0,    0,    0,    0,    0,    0,
    0,    0,  277,    0,    0,   11,  313,    0,  234,    0,
    0,  308,  356,  289,    0,    0, -161,    0,    0,    0,
    0,  345,    0,  -98,  -74,
};
final static int YYTABLESIZE=1001;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
   31,   17,    2,   94,  270,  174,  164,  219,   20,  107,
   20,   19,  206,   17,  139,  102,  125,   20,  234,   16,
   95,  181,   40,  174,  180,  101,  147,    7,  101,  147,
    4,   50,  178,   20,  245,   50,  207,   17,  199,   71,
   31,  190,  280,   50,    3,  101,   44,  179,  129,  244,
  123,   26,   46,  152,  237,   90,   71,  279,   45,  113,
  129,  150,   20,   17,   52,  101,  152,   70,   71,   51,
   20,   73,  123,  275,  124,   17,  134,  211,  136,  129,
  210,  102,   23,   20,   87,  162,   88,   17,  165,  236,
  129,  164,  235,   89,  164,  103,   16,   91,   20,   17,
  252,  168,   20,  170,  260,   20,   20,   52,   49,  129,
   50,   17,   51,  257,  259,  274,  114,  105,  119,  230,
   76,  123,   17,  124,  253,   31,  150,  289,  261,  150,
  112,  132,  152,  172,   17,  187,   26,  161,  174,  177,
  190,  254,  222,   20,  186,   50,   76,  197,  123,  200,
  124,  212,   17,    4,   65,  149,   52,   49,   20,   50,
  239,   51,  280,   50,   15,  225,  129,  224,  226,  233,
  229,   65,  251,   52,   49,   17,   50,  291,   51,   45,
   20,  196,  262,  129,  175,  231,  232,   50,  102,  208,
   20,   20,  263,   20,  265,  271,  272,  282,  283,   17,
  102,  254,   20,   17,  284,   50,   65,  250,   52,   49,
  285,   50,  286,   51,  269,  129,  190,   86,   10,  264,
  174,  204,  218,  287,  107,   17,  125,   11,   12,    9,
   10,   13,  102,   14,   20,   15,  178,  277,  101,   11,
   12,  101,  152,   13,   47,   14,  243,   15,   47,   17,
   18,  179,   71,    6,   10,  152,   47,   31,    1,    3,
  123,  204,  100,   11,   12,  149,   31,   31,   26,   14,
   31,   15,   31,  133,   31,  135,  140,   26,   26,   74,
   10,   26,  108,   26,   73,   26,   10,  122,  163,   11,
   12,    9,   10,    3,  209,   14,   75,   15,  167,  187,
  169,   11,   12,    9,   10,   13,  203,   14,   76,   15,
  256,  258,  273,   11,   12,   52,   10,   13,   50,   14,
   51,   15,   10,   47,  288,   11,   12,    9,   10,   13,
  172,   14,  187,   15,  228,   10,  122,   11,   12,   10,
   13,   13,   50,   14,  123,   15,  228,   14,   11,   12,
  146,   10,   13,  147,   14,  266,   15,   10,   47,   43,
   11,   12,   10,  122,   13,  278,   14,  110,   15,   10,
   90,   10,   47,   41,  290,  195,   47,  138,   11,   12,
   15,   15,   13,  223,   14,  166,   15,   75,   10,   47,
   15,   15,   10,  107,   15,  198,   15,  121,   15,   10,
   47,   11,   12,   52,   49,   13,   50,   14,   51,   15,
   77,    0,   77,   77,   77,    0,   10,   10,   47,    0,
   10,    0,   64,   10,   47,   11,   12,    0,   77,   11,
   12,   14,  131,   15,    0,   14,    0,   15,   10,  120,
   52,   49,   10,   50,  123,   51,  124,   11,   12,  160,
    0,   11,   12,   14,   50,   15,    0,   14,    0,   15,
  173,   58,   58,   73,  159,   58,   10,    0,   80,   97,
   80,   80,   80,    0,    0,   11,   12,   53,   10,   52,
   49,   14,   50,   15,   51,    0,   80,   11,   12,   52,
    0,    0,   50,   14,   51,   15,   58,    0,  123,   58,
  124,    0,    0,   58,  128,  217,    0,   58,   78,    0,
   78,   78,   78,    0,  242,   58,  128,    0,  101,  101,
  101,  101,  101,    0,  101,   58,   78,    0,   58,  214,
   10,   47,    0,    0,   58,  128,  101,  101,  101,  101,
  123,    0,  124,   58,  191,    0,  128,    0,   81,  194,
   81,   81,   81,  216,   10,   47,  192,    0,    0,   52,
   49,   94,   50,    0,   51,  128,   81,   81,   81,   81,
  110,  156,   77,   45,   77,  255,    0,   58,   95,  255,
   58,  108,    0,   80,    0,   80,  155,    0,  268,   77,
   77,   77,  109,    0,   78,    0,   78,   58,   58,    0,
   80,   80,   80,   84,    0,   52,   49,    0,   50,    0,
   51,   78,   78,   78,  123,   67,  124,  188,   10,   47,
    0,    0,  128,    0,    0,   77,   77,   77,    0,    0,
    0,  145,  143,  144,   77,  238,  137,   52,   49,  128,
   50,    0,   51,  171,   52,   49,    0,   50,    0,   51,
   58,    0,  118,    0,   58,   10,   47,   48,   10,  122,
   58,  157,  158,   58,    0,  267,   10,   47,   67,    0,
    0,  128,    0,    0,    0,    0,    0,    0,   67,    0,
    0,   67,    0,   80,   80,   80,  130,  276,    0,    0,
    0,    0,   80,    0,   10,   47,   48,    0,  130,    0,
    0,    0,    0,    0,   10,   47,    0,    0,    0,    0,
    0,  241,   10,  122,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   78,   78,   78,    0,    0,    0,    0,
  215,    0,   78,  101,  101,  101,    0,  101,  101,  101,
  101,  101,  101,    0,    0,    0,    0,  193,    0,  130,
  220,    0,    0,   54,   10,  122,  213,   85,    0,    0,
    0,   96,    0,   81,   81,   81,    0,   81,   81,   81,
   81,    0,   81,    0,   10,   47,    0,  213,    0,    0,
    0,   93,    0,  153,  154,  110,   77,   77,  115,   77,
   77,   77,   77,    0,    0,   54,  108,   80,   80,    0,
   80,   80,   80,   80,    0,    0,    0,  109,   78,   78,
  227,   78,   78,   78,   78,   77,    0,    0,    0,   83,
   10,   47,    0,    0,    0,    0,  172,    0,   10,  122,
    0,  139,  140,  141,  142,  189,    0,    0,    0,    0,
  109,    0,  247,    0,  248,    0,  130,    0,    0,    0,
    0,  130,   10,   47,    0,    0,    0,    0,    0,   10,
   47,    0,    0,    0,    0,    0,    0,    0,    0,   77,
    0,    0,    0,    0,    0,    0,    0,  109,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  221,    0,    0,    0,  247,  281,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  109,
    0,    0,  205,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  281,    0,  292,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   77,    0,  115,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  205,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  205,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,   40,    0,   44,   59,   41,   76,   41,   14,   41,
   16,    0,  111,   40,   40,   23,   59,   23,  180,  123,
   61,   41,    6,   59,   44,   41,   41,    0,   44,   44,
    0,   45,  256,   39,   44,   45,  111,   40,  108,   44,
   40,  257,   44,   45,  257,   61,  125,  271,   54,   59,
   59,   40,   40,  257,  270,  270,   61,   59,   46,   43,
   66,   68,   68,   40,   42,   59,  270,  256,  257,   47,
   76,   45,   43,  235,   45,   40,   59,   41,   59,   85,
   44,   89,    5,   89,  123,   75,  125,   40,   78,   41,
   96,   41,   44,   16,   44,  123,  123,   40,  104,   40,
  199,   59,  108,   59,  203,  111,  112,   42,   43,  115,
   45,   40,   47,   59,   59,   59,  257,   59,  258,   41,
  123,   43,   40,   45,  199,  125,   41,   59,  203,   44,
  123,  256,   41,   59,   40,   59,  125,  264,  125,  257,
  257,   41,  149,  149,  123,   45,  123,  123,   43,   40,
   45,   41,   40,  123,   40,  268,   42,   43,  164,   45,
  125,   47,   44,   45,   40,  257,  172,  269,  257,   41,
   59,   40,  125,   42,   43,   40,   45,   59,   47,   46,
  186,  104,  256,  189,  125,  125,  125,   45,  196,  112,
  196,  197,  256,  199,  257,   59,   59,   44,  256,   40,
  208,   41,  208,   40,  256,   45,   40,  125,   42,   43,
   41,   45,  256,   47,  269,  221,  257,  256,  257,  125,
  256,   40,  256,  256,  256,   40,  269,  266,  267,  256,
  257,  270,  240,  272,  240,  274,  256,  125,  264,  266,
  267,  257,  257,  270,  258,  272,  256,  274,  258,   40,
    0,  271,  257,  257,  257,  270,  258,  257,  256,  257,
  269,   40,  256,  266,  267,  268,  266,  267,  257,  272,
  270,  274,  272,  256,  274,  256,   40,  266,  267,  256,
  257,  270,  123,  272,  258,  274,  257,  258,  125,  266,
  267,  256,  257,    0,  265,  272,  273,  274,  256,  256,
  256,  266,  267,  256,  257,  270,  125,  272,  123,  274,
  256,  256,  256,  266,  267,   42,  257,  270,   45,  272,
   47,  274,  257,  258,  256,  266,  267,  256,  257,  270,
  256,  272,  256,  274,  125,  257,  258,  266,  267,  257,
    0,  270,   45,  272,  269,  274,  125,    0,  266,  267,
   66,  257,  270,   66,  272,  210,  274,  257,  258,    8,
  266,  267,  257,  258,  270,  245,  272,   39,  274,  257,
  270,  257,  258,    6,  256,   99,  258,   65,  266,  267,
  256,  257,  270,  150,  272,   78,  274,  273,  257,  258,
  266,  267,  257,   38,  270,  107,  272,   53,  274,  257,
  258,  266,  267,   42,   43,  270,   45,  272,   47,  274,
   41,   -1,   43,   44,   45,   -1,  257,  257,  258,   -1,
  257,   -1,  256,  257,  258,  266,  267,   -1,   59,  266,
  267,  272,   57,  274,   -1,  272,   -1,  274,  257,   41,
   42,   43,  257,   45,   43,   47,   45,  266,  267,   44,
   -1,  266,  267,  272,   45,  274,   -1,  272,   -1,  274,
   59,   11,   12,   13,   59,   15,  257,   -1,   41,   19,
   43,   44,   45,   -1,   -1,  266,  267,   40,  257,   42,
   43,  272,   45,  274,   47,   -1,   59,  266,  267,   42,
   -1,   -1,   45,  272,   47,  274,   46,   -1,   43,   49,
   45,   -1,   -1,   53,   54,  130,   -1,   57,   41,   -1,
   43,   44,   45,   -1,   59,   65,   66,   -1,   41,   42,
   43,   44,   45,   -1,   47,   75,   59,   -1,   78,  256,
  257,  258,   -1,   -1,   84,   85,   59,   60,   61,   62,
   43,   -1,   45,   93,   94,   -1,   96,   -1,   41,   99,
   43,   44,   45,  256,  257,  258,   59,   -1,   -1,   42,
   43,   44,   45,   -1,   47,  115,   59,   60,   61,   62,
   41,   44,   43,   46,   45,  200,   -1,  127,   61,  204,
  130,   41,   -1,   43,   -1,   45,   59,   -1,  213,   60,
   61,   62,   41,   -1,   43,   -1,   45,  147,  148,   -1,
   60,   61,   62,   40,   -1,   42,   43,   -1,   45,   -1,
   47,   60,   61,   62,   43,   12,   45,  256,  257,  258,
   -1,   -1,  172,   -1,   -1,  256,  257,  258,   -1,   -1,
   -1,   60,   61,   62,  265,  185,   41,   42,   43,  189,
   45,   -1,   47,   41,   42,   43,   -1,   45,   -1,   47,
  200,   -1,   49,   -1,  204,  257,  258,  259,  257,  258,
  210,  256,  257,  213,   -1,  256,  257,  258,   65,   -1,
   -1,  221,   -1,   -1,   -1,   -1,   -1,   -1,   75,   -1,
   -1,   78,   -1,  256,  257,  258,   55,  237,   -1,   -1,
   -1,   -1,  265,   -1,  257,  258,  259,   -1,   67,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
  127,   -1,  265,  256,  257,  258,   -1,  260,  261,  262,
  263,  264,  265,   -1,   -1,   -1,   -1,   98,   -1,  118,
  147,   -1,   -1,   11,  257,  258,  125,   15,   -1,   -1,
   -1,   19,   -1,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,  257,  258,   -1,  146,   -1,   -1,
   -1,  264,   -1,  256,  257,  256,  257,  258,   46,  260,
  261,  262,  263,   -1,   -1,   53,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,   -1,   -1,  256,  257,  258,
  161,  260,  261,  262,  263,   14,   -1,   -1,   -1,  256,
  257,  258,   -1,   -1,   -1,   -1,   84,   -1,  257,  258,
   -1,  260,  261,  262,  263,   93,   -1,   -1,   -1,   -1,
   39,   -1,  193,   -1,  195,   -1,  215,   -1,   -1,   -1,
   -1,  220,  257,  258,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   68,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   76,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  148,   -1,   -1,   -1,  245,  246,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  108,
   -1,   -1,  111,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,   -1,  280,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  149,   -1,  210,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  164,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  199,
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

//#line 901 "gramatica.y"

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

public Parser(Lexer lexer, SymbolTable symbolTable,
                MessageCollector errorCollector, MessageCollector warningCollector) {
    
    this.lexer = lexer;
    this.symbolTable = symbolTable;
    this.errorCollector = errorCollector;
    this.warningCollector = warningCollector;
    
    this.scopeStack = new ScopeStack();
    this.reversePolish = new ReversePolish();

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
//#line 813 "Parser.java"
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
{ notifyDetection("Asignación simple."); this.symbolTable.setValue(val_peek(3).sval, val_peek(1).sval); }
break;
case 60:
//#line 306 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 61:
//#line 309 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 62:
//#line 312 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 63:
//#line 322 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 64:
//#line 324 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 65:
//#line 329 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 66:
//#line 331 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 67:
//#line 333 "gramatica.y"
{ notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval)); }
break;
case 71:
//#line 348 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 73:
//#line 356 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 75:
//#line 364 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 76:
//#line 369 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 78:
//#line 383 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            notifyDetection(val_peek(2).sval);
            notifyDetection(val_peek(0).sval);
            notifyDetection(val_peek(1).sval);
        }
break;
case 79:
//#line 393 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 80:
//#line 397 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 81:
//#line 404 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 82:
//#line 414 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 83:
//#line 416 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 84:
//#line 423 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 86:
//#line 429 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 87:
//#line 436 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 88:
//#line 443 "gramatica.y"
{ yyval.sval = val_peek(2).sval; }
break;
case 90:
//#line 449 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 91:
//#line 456 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 92:
//#line 458 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 100:
//#line 485 "gramatica.y"
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
//#line 513 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 103:
//#line 522 "gramatica.y"
{ notifyDetection("Condición."); }
break;
case 104:
//#line 527 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 105:
//#line 531 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 106:
//#line 533 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 108:
//#line 544 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 109:
//#line 546 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 110:
//#line 548 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 117:
//#line 564 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 118:
//#line 573 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 119:
//#line 578 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 120:
//#line 580 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 121:
//#line 582 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); }
break;
case 122:
//#line 584 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 125:
//#line 596 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); }
break;
case 126:
//#line 605 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 128:
//#line 611 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 129:
//#line 613 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 130:
//#line 615 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 132:
//#line 628 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 133:
//#line 630 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 135:
//#line 645 "gramatica.y"
{
            notifyDetection("Declaración de función.");
            this.symbolTable.setType(val_peek(2).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(2).sval, SymbolCategory.FUNCTION);
            this.scopeStack.pop();    
        }
break;
case 136:
//#line 655 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 137:
//#line 658 "gramatica.y"
{ this.scopeStack.pop(); }
break;
case 138:
//#line 661 "gramatica.y"
{
            notifyError("El cuerpo de la función no puede estar vacío.");
            this.scopeStack.pop();
        }
break;
case 139:
//#line 673 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); yyval.sval = val_peek(0).sval; }
break;
case 140:
//#line 680 "gramatica.y"
{
            this.scopeStack.push(String.valueOf(lexer.getNroLinea()));
            notifyError("La función requiere de un nombre.");
        }
break;
case 143:
//#line 700 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 146:
//#line 712 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 149:
//#line 726 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PARAMETER);
        }
break;
case 150:
//#line 734 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 151:
//#line 736 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 154:
//#line 748 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); }
break;
case 155:
//#line 757 "gramatica.y"
{ notifyDetection("Sentencia RETURN."); }
break;
case 156:
//#line 762 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 157:
//#line 764 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 158:
//#line 766 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 159:
//#line 768 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 160:
//#line 777 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 162:
//#line 787 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 163:
//#line 794 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 164:
//#line 799 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 165:
//#line 808 "gramatica.y"
{ notifyDetection("Sentencia 'print'."); }
break;
case 166:
//#line 813 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 168:
//#line 816 "gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 170:
//#line 829 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 171:
//#line 832 "gramatica.y"
{ notifyError("El imprimible debe encerrarse entre paréntesis."); }
break;
case 172:
//#line 834 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); }
break;
case 175:
//#line 850 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 176:
//#line 855 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 178:
//#line 858 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); }
break;
case 179:
//#line 861 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 180:
//#line 863 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 181:
//#line 865 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 182:
//#line 867 "gramatica.y"
{ notifyError("Falta delimitador de cierre en expresión 'lambda'."); }
break;
case 183:
//#line 869 "gramatica.y"
{ notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); }
break;
case 184:
//#line 871 "gramatica.y"
{ notifyError("Falta delimitador de apertura en expresión 'lambda'."); }
break;
case 186:
//#line 884 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); }
break;
case 187:
//#line 887 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); }
break;
//#line 1486 "Parser.java"
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
