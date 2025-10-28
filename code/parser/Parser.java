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
    import semantic.Promise;
    import lexer.token.Token;
    import utilities.Printer;
    import common.SymbolType;
    import common.SymbolTable;
    import semantic.ScopeStack;
    import common.SymbolCategory;
    import semantic.ReversePolish;
    import utilities.MessageCollector;
//#line 38 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 34 "Parser.java"




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
   14,   14,   14,   14,   14,   14,   45,   45,   49,   50,
   50,   50,   50,   51,   51,   51,   52,   46,   46,   46,
   53,   54,   54,   54,   55,   35,   35,    8,    8,   56,
   56,   57,   57,   57,   59,   59,   58,   58,   58,   15,
   15,   15,   42,   42,   42,   42,   42,   11,   12,   12,
   13,   13,   43,   43,   60,   60,   60,   60,   61,   61,
   62,   44,   44,   44,   44,   44,   17,   17,   17,   16,
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
    1,    2,    1,    2,    2,    5,    4,    2,    1,    3,
    2,    1,    3,    1,    2,    2,    3,    2,    2,    0,
    1,    1,    5,    5,    4,    3,    2,    4,    1,    3,
    3,    1,    3,    3,    3,    2,    1,    0,    1,    1,
    0,    5,    4,    5,    4,    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,  121,    0,    0,    0,    0,    0,    0,
    0,    0,   38,    1,    0,    0,   21,   24,   25,   26,
    0,   37,   39,   40,   41,   42,   44,   45,    0,    0,
    4,    0,   23,    0,    0,   88,  159,    0,    0,   80,
   81,    0,    0,    0,   74,    0,    0,   83,   84,    0,
  157,  108,    0,    0,    0,  109,    0,   49,    0,    0,
    0,  147,    0,    0,    0,   17,   14,    0,    0,    0,
    0,    0,    0,   60,    0,    0,   62,   43,   36,    0,
   34,    0,    0,    0,   63,    0,   22,   18,    0,   29,
   27,  117,    0,   30,    0,   31,  107,    0,    0,  120,
    0,    0,    0,  123,    6,   91,    0,    0,  149,    0,
   89,  156,    0,   86,   71,   72,    0,   78,    0,   85,
   87,    0,   76,   82,  154,  153,   93,    0,  102,  104,
  103,  105,  106,  100,  101,    0,    0,    0,   94,    0,
   47,   52,   46,    0,    0,    0,  146,   16,    0,    0,
   53,    0,  142,  141,    0,  131,    0,    0,  132,  134,
    0,   57,    0,    0,    0,   61,   56,   33,    0,    0,
    0,    0,    0,    0,    0,   35,    0,   58,    0,   65,
    0,    0,    0,    0,  115,  125,  124,  122,  119,  118,
    0,    0,  148,  155,    0,   68,    0,   75,   73,   95,
   92,    0,    0,    0,   51,  145,    0,   19,   20,  170,
  136,  139,    0,    0,  130,  127,    0,   55,   54,   32,
    0,  168,    0,  163,    0,    0,  165,   59,   64,   13,
    0,  112,  113,  151,  150,   79,   77,   48,  144,  143,
  137,  133,  126,  164,  167,  162,  166,  111,  110,
};
final static short yydgoto[] = {                          4,
   64,   54,   55,  127,  128,  129,   56,   18,   57,   58,
   59,  118,  119,  148,  167,   21,  181,    5,   70,   71,
   22,   96,   23,   24,   25,    6,    8,  160,   26,   99,
   27,   28,   29,   30,   31,  101,  105,  106,   93,   32,
   33,   34,   35,   36,   37,   38,   66,   67,   39,  107,
  108,  109,   40,  113,  114,   81,  168,  169,  170,   60,
   61,  182,
};
final static short yysindex[] = {                         3,
   62,    0,    0,    0,  -28, -104,    0, -224,   -4,    8,
  124,  164, -176,    0,  448,  -40, -198,   34,   26,  -46,
   98,   14,    0,    0,  101,  -41,    0,    0,    0,    0,
   35,    0,    0,    0,    0,    0,    0,    0,  -17,   13,
    0, -104,    0, -180,  130,    0,    0,  495, -166,    0,
    0,  480,  287,    2,    0,  253, -155,    0,    0,  -42,
    0,    0,   55,  439,    2,    0,   64,    0,    0, -149,
  373,    0,   66,  435,  -39,    0,    0,   36, -134,  -15,
    4,  460, -127,    0,  489,  273,    0,    0,    0,  112,
    0,   91,  123,   30,    0,  -13,    0,    0,   48,    0,
    0,    0,  112,    0, -136,    0,    0, -135,  139,    0,
  102,  -38,   -2,    0,    0,    0,  251,   22,    0,    2,
    0,    0,   92,    0,    0,    0,    2,    0,  486,    0,
    0,  219,    0,    0,    0,    0,    0,  -23,    0,    0,
    0,    0,    0,    0,    0,    2,  486,  130,    0,   30,
    0,    0,    0, -134,   76,  -35,    0,    0,   12,   21,
    0,  107,    0,    0, -229,    0, -227,   23,    0,    0,
   25,    0,  453,   97,  -33,    0,    0,    0,  136,  202,
 -105,   91,   91,   -5, -103,    0,   95,    0,   30,    0,
   59,  152,  -44,   96,    0,    0,    0,    0,    0,    0,
 -101,  130,    0,    0,  312,    0,    2,    0,    0,    0,
    0,    2,  287,   99,    0,    0,   44,    0,    0,    0,
    0,    0, -100, -229,    0,    0,   70,    0,    0,    0,
 -102,    0,  118,    0,  103,  -91,    0,    0,    0,    0,
   45,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        37,
  -89,    0,    0,    0,  170,  170,    0,    0,    0,  386,
   53,    0,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  178,   82,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,  -88,    0,
    0,  170,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -25,  142,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  425,    0,    0,    0,  159,  406,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -219,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   65,  -76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -3,    0,    0,    0,   20,    0,
    0,    0,    0,    0,    0,    0,   49,    0,    0,  396,
    0,    0,    0,    0,    0,    0,  302,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  533,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  182,  188,
    0,    0,    0,    0, -219,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,  -76,    0,
    0,  132,  -76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  408,    0,    0,    0,
    0,  545,  -20,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   73,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  740,  751,  433,  125,    0,  129,  696,    0,  499,  447,
  655,    0,   -8,    0,    0,    0,  -54,  189,  -65,    0,
   33,  104,    0,   18,   19,    0,    0,    0,    0,    0,
  360,  728,    0,    0,    0,    0,  -18,  175,  -45,    0,
    0,    0,    0,    0,    0,    0,    9,  137,    0,    0,
  100,    0,    0,    0,   90,    0,    0, -140,    0,    0,
  160,    0,
};
final static int YYTABLESIZE=942;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,   63,    2,   51,   48,  217,   49,  125,   50,  126,
   83,   17,   89,  162,  242,  160,  136,  211,   16,   43,
   96,  112,   17,   41,  221,  166,  163,   84,  165,  222,
  189,   49,    3,  160,   78,  232,    3,  140,  185,   49,
   28,  164,  223,   51,  179,  188,  135,   45,   50,  135,
  140,   87,   17,   44,   43,  114,  200,  192,   49,  115,
   90,    7,  203,  225,   17,  202,  224,   51,   48,   83,
   49,   79,   50,   80,   49,   17,  116,   90,  116,   68,
   69,   98,   76,  252,   77,  158,   84,   17,  215,  152,
  195,  121,  152,  100,   16,  137,   51,   48,   17,   49,
  134,   50,  250,  259,  149,  103,  155,   51,   48,   17,
   49,  158,   50,  138,  150,  176,  138,  191,   87,  196,
  197,   12,  161,  161,  231,   28,  171,  235,  236,  174,
  180,  102,  204,  194,  216,  103,  218,   17,  189,   49,
   17,   63,   44,   51,   48,  219,   49,  220,   50,  226,
  234,   17,  237,  254,  243,  244,  251,  248,  255,    3,
  159,  256,  184,   52,  257,   51,   48,    5,   49,   15,
   50,   51,   48,  129,   49,  184,   50,    2,   17,  169,
  114,   10,   66,  240,   66,   66,   66,   11,  146,  227,
  169,   17,  147,  245,  253,   92,   42,  187,  128,  138,
   66,  198,   53,   63,  193,   51,   48,   87,   49,   88,
   50,  123,    0,  135,    0,   75,   10,   53,   10,   46,
   90,   10,  124,  174,  241,   11,   12,    9,   10,   13,
  160,   14,  210,   15,  111,   96,  178,   11,   12,   10,
  163,   13,  232,   14,   46,   15,   49,  183,   11,   12,
  102,   10,   46,  199,   14,  164,   15,   28,    1,    3,
  230,  103,  140,   49,   79,  114,   28,   28,  110,   10,
   28,   46,   28,   90,   28,  140,  230,   94,   11,   12,
    9,   10,   10,   46,   14,  111,   15,   46,  116,   82,
   11,   12,   10,  125,   13,  126,   14,   49,   15,  249,
  258,   11,   12,    9,   10,   13,    0,   14,  158,   15,
    0,   10,   46,   11,   12,   10,   83,   13,    0,   14,
  169,   15,   10,   46,   11,   12,   10,    0,   13,  125,
   14,  126,   15,   84,    0,   11,   12,   12,   12,   13,
    0,   14,   69,   15,   69,   69,   69,   12,   12,    0,
  238,   12,   46,   12,   10,   12,   49,   10,   10,   46,
   69,    0,    0,   11,   12,    0,   11,   12,   10,   14,
   13,   15,   14,    0,   15,    0,    0,   11,   12,   10,
   10,   46,   47,   14,   97,   15,   10,   46,   11,   12,
    0,    0,   10,    0,   14,   10,   15,   66,   66,   66,
    0,   11,   12,    0,   11,   12,   66,   14,   10,   15,
   14,    0,   15,    0,   53,   53,  154,   11,   12,   62,
   10,   46,   53,   14,    0,   15,   90,   90,   90,   90,
   90,  153,   90,    0,    0,    0,   70,   97,   70,   70,
   70,    0,    0,    0,   90,   90,   90,   90,   67,   50,
   67,   67,   67,    0,   70,   70,   70,   70,   10,   46,
    0,    0,    0,    0,   50,   99,   67,   66,   95,   66,
    0,    0,    0,    0,  208,   10,   46,  125,    0,  126,
    0,  125,    0,  126,   66,   66,   66,   73,  133,   51,
   48,    0,   49,  157,   50,  125,    0,  126,  145,  143,
  144,   51,   48,   19,   49,    0,   50,   10,  124,   10,
   46,  229,    0,    0,   19,  201,    0,   86,    0,   19,
  122,   51,   48,   19,   49,    0,   50,   51,  134,  174,
   49,  125,   50,  126,    0,    0,   51,   19,   19,   49,
   95,   50,  190,   10,  124,    0,    0,  177,    0,    0,
   97,  130,    0,    0,    0,    0,    0,   69,   69,   69,
    0,    0,  130,    0,  209,    0,   69,  246,   10,   46,
    0,    0,  130,   97,    0,   69,   19,   69,    0,    0,
    0,  175,    0,  130,  175,   98,   97,   67,   19,   67,
    0,   19,   69,   69,   69,    0,  214,   19,    0,    0,
    0,   19,    0,    0,   67,   67,   67,   19,    0,    0,
    0,    0,  233,    0,    0,  130,  233,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  151,  152,
    0,    0,    0,  190,    0,  239,    0,  247,    0,    0,
    0,   90,   90,   90,    0,   90,   90,   90,   90,   90,
   90,   70,   70,   70,  130,   70,   70,   70,   70,   20,
   70,   50,   50,   67,   67,   67,    0,    0,    0,   19,
   20,  130,   67,  175,    0,   20,    0,   19,    0,   20,
   99,   66,   66,    0,   66,   66,   66,   66,    0,   19,
   19,   10,  124,   20,   20,   10,  124,    0,  139,  140,
  141,  142,    0,   72,   10,   46,    0,  131,  228,   10,
  124,  130,    0,    0,    0,  172,   10,   46,  131,    0,
    0,    0,    0,    0,    0,   19,    0,    0,  131,    0,
    0,    0,   20,    0,    0,    0,   10,   46,   47,  131,
    0,  206,   10,   46,   20,   10,  124,   20,   91,  132,
   53,   10,   46,   20,   74,    0,    0,   20,   85,    0,
  132,    0,   65,   20,    0,    0,  104,  104,    0,    0,
    0,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  117,    0,    0,    0,   97,   69,
   69,   53,   69,   69,   69,   69,    0,    0,  120,    0,
   98,   67,   67,    0,   67,   67,   67,   67,    0,    0,
  131,    0,  156,   65,    0,  132,    0,   91,    0,    0,
  186,  173,  205,    0,    0,   20,    0,  131,    0,    0,
   91,    0,    0,   20,    0,    0,  104,    0,    0,    0,
    0,  205,    0,    0,    0,   20,   20,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   65,   65,    0,    0,    0,    0,  131,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  207,
    0,   20,    0,    0,    0,    0,    0,  213,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  212,    0,    0,
    0,    0,  132,    0,    0,    0,  186,  132,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  186,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  117,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,    0,   42,   43,   41,   45,   43,   47,   45,
   44,   40,   59,   79,   59,   41,   59,   41,  123,   59,
   41,   40,   40,    6,  165,   41,  256,   61,   44,  257,
   44,   45,  257,   59,   16,   41,    0,  257,   93,   45,
   40,  271,  270,   42,   90,   59,   41,   40,   47,   44,
  270,   19,   40,   46,   59,   59,   59,  103,   45,   42,
   44,    0,   41,   41,   40,   44,   44,   42,   43,   44,
   45,  270,   47,   40,   45,   40,  257,   61,   59,  256,
  257,  123,  123,  224,  125,  125,   61,   40,  154,   41,
  109,  258,   44,   59,  123,   41,   42,   43,   40,   45,
  256,   47,   59,   59,   41,  123,   41,   42,   43,   40,
   45,   59,   47,   41,  264,   83,   44,   99,   86,  111,
  112,   40,  257,   59,  179,  125,  123,  182,  183,  257,
   40,  268,   41,  269,   59,  123,  125,   40,   44,   45,
   40,   40,   46,   42,   43,  125,   45,   41,   47,  125,
  256,   40,  256,  256,   59,  257,  257,   59,   41,  123,
  125,   59,   40,   40,  256,   42,   43,  257,   45,    0,
   47,   42,   43,   40,   45,   40,   47,    0,   40,  256,
  269,    0,   41,  125,   43,   44,   45,    0,   64,  171,
   59,   40,   64,  202,  125,   21,    8,   94,   40,   63,
   59,  112,   44,   40,  105,   42,   43,  175,   45,  256,
   47,   52,   -1,  256,   -1,  256,  257,   59,  257,  258,
  123,  257,  258,  257,  269,  266,  267,  256,  257,  270,
  256,  272,  256,  274,  273,  256,  125,  266,  267,  257,
  256,  270,   41,  272,  258,  274,   45,  125,  266,  267,
  268,  257,  258,  256,  272,  271,  274,  257,  256,  257,
  125,  123,  257,   45,  270,  269,  266,  267,  256,  257,
  270,  258,  272,  257,  274,  270,  125,  264,  266,  267,
  256,  257,  257,  258,  272,  273,  274,  258,  269,  264,
  266,  267,  257,   43,  270,   45,  272,   45,  274,  256,
  256,  266,  267,  256,  257,  270,   -1,  272,  256,  274,
   -1,  257,  258,  266,  267,  257,   44,  270,   -1,  272,
  256,  274,  257,  258,  266,  267,  257,   -1,  270,   43,
  272,   45,  274,   61,   -1,  266,  267,  256,  257,  270,
   -1,  272,   41,  274,   43,   44,   45,  266,  267,   -1,
  256,  270,  258,  272,  257,  274,   45,  257,  257,  258,
   59,   -1,   -1,  266,  267,   -1,  266,  267,  257,  272,
  270,  274,  272,   -1,  274,   -1,   -1,  266,  267,  257,
  257,  258,  259,  272,   25,  274,  257,  258,  266,  267,
   -1,   -1,  257,   -1,  272,  257,  274,  256,  257,  258,
   -1,  266,  267,   -1,  266,  267,  265,  272,  257,  274,
  272,   -1,  274,   -1,  256,  257,   44,  266,  267,  256,
  257,  258,  264,  272,   -1,  274,   41,   42,   43,   44,
   45,   59,   47,   -1,   -1,   -1,   41,   78,   43,   44,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   41,   44,
   43,   44,   45,   -1,   59,   60,   61,   62,  257,  258,
   -1,   -1,   -1,   -1,   59,   41,   59,   43,   22,   45,
   -1,   -1,   -1,   -1,  256,  257,  258,   43,   -1,   45,
   -1,   43,   -1,   45,   60,   61,   62,   40,   56,   42,
   43,   -1,   45,   59,   47,   43,   -1,   45,   60,   61,
   62,   42,   43,    5,   45,   -1,   47,  257,  258,  257,
  258,   59,   -1,   -1,   16,  265,   -1,   19,   -1,   21,
   41,   42,   43,   25,   45,   -1,   47,   42,  256,  257,
   45,   43,   47,   45,   -1,   -1,   42,   39,   40,   45,
   94,   47,   96,  257,  258,   -1,   -1,   59,   -1,   -1,
  191,   53,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,   -1,   64,   -1,  132,   -1,  265,  256,  257,  258,
   -1,   -1,   74,   41,   -1,   43,   78,   45,   -1,   -1,
   -1,   83,   -1,   85,   86,   41,  227,   43,   90,   45,
   -1,   93,   60,   61,   62,   -1,  150,   99,   -1,   -1,
   -1,  103,   -1,   -1,   60,   61,   62,  109,   -1,   -1,
   -1,   -1,  180,   -1,   -1,  117,  184,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
   -1,   -1,   -1,  187,   -1,  189,   -1,  205,   -1,   -1,
   -1,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,  256,  257,  258,  156,  260,  261,  262,  263,    5,
  265,  256,  257,  256,  257,  258,   -1,   -1,   -1,  171,
   16,  173,  265,  175,   -1,   21,   -1,  179,   -1,   25,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,  191,
  192,  257,  258,   39,   40,  257,  258,   -1,  260,  261,
  262,  263,   -1,  256,  257,  258,   -1,   53,  256,  257,
  258,  213,   -1,   -1,   -1,  256,  257,  258,   64,   -1,
   -1,   -1,   -1,   -1,   -1,  227,   -1,   -1,   74,   -1,
   -1,   -1,   78,   -1,   -1,   -1,  257,  258,  259,   85,
   -1,  256,  257,  258,   90,  257,  258,   93,   21,   54,
   11,  257,  258,   99,   15,   -1,   -1,  103,   19,   -1,
   65,   -1,   12,  109,   -1,   -1,   39,   40,   -1,   -1,
   -1,  117,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   45,   -1,   -1,   -1,  256,  257,
  258,   52,  260,  261,  262,  263,   -1,   -1,   48,   -1,
  256,  257,  258,   -1,  260,  261,  262,  263,   -1,   -1,
  156,   -1,   73,   63,   -1,  120,   -1,   90,   -1,   -1,
   93,   82,  127,   -1,   -1,  171,   -1,  173,   -1,   -1,
  103,   -1,   -1,  179,   -1,   -1,  109,   -1,   -1,   -1,
   -1,  146,   -1,   -1,   -1,  191,  192,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  111,  112,   -1,   -1,   -1,   -1,  213,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  129,
   -1,  227,   -1,   -1,   -1,   -1,   -1,  148,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  147,   -1,   -1,
   -1,   -1,  207,   -1,   -1,   -1,  179,  212,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  192,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  202,
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
"$$4 :",
"lambda : parametro_lambda bloque_ejecutable $$4 argumento_lambda ';'",
"lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
"lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
"lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
"argumento_lambda : '(' factor ')'",
"argumento_lambda : '(' ')'",
"argumento_lambda :",
"parametro_lambda : '(' UINT identifier ')'",
};

//#line 1144 "gramatica.y"

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
//#line 778 "Parser.java"
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
//#line 79 "gramatica.y"
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
//#line 91 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 94 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 96 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previas al nombre del programa."); }
break;
case 7:
//#line 99 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 102 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 109 "gramatica.y"
{
            this.scopeStack.push(val_peek(0).sval);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
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
case 23:
//#line 160 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 201 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 219 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 230 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 247 "gramatica.y"
{
            if (!errorState) {
                { notifyDetection("Declaración de variables."); }
            } else {
                errorState = false;
            }
        }
break;
case 47:
//#line 257 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 48:
//#line 261 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 49:
//#line 265 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 51:
//#line 275 "gramatica.y"
{ yyval.sval = val_peek(2).sval + ',' + val_peek(0).sval; }
break;
case 52:
//#line 280 "gramatica.y"
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
//#line 303 "gramatica.y"
{
            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
            yyval.sval = this.scopeStack.appendScope(val_peek(0).sval);
        }
break;
case 54:
//#line 317 "gramatica.y"
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

                this.reversePolish.emptyTemporalPolishes();

                errorState = false;
            }
        }
break;
case 55:
//#line 347 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 56:
//#line 350 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 57:
//#line 353 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 58:
//#line 363 "gramatica.y"
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
        }
break;
case 111:
//#line 753 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 112:
//#line 755 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 113:
//#line 757 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 116:
//#line 769 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 777 "gramatica.y"
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
//#line 799 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Sentencia 'do-while'.");
                this.reversePolish.connectToLastBifurcationPoint();
                this.reversePolish.addPolish("TB");
                this.reversePolish.addSeparation("Leaving 'do-while' body...");
            } else {
                errorState = false;
            }
        }
break;
case 119:
//#line 813 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 120:
//#line 815 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 121:
//#line 823 "gramatica.y"
{
            this.reversePolish.addSeparation("Entering 'do-while' body...");
            this.reversePolish.stackBifurcationPoint();
        }
break;
case 123:
//#line 837 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 124:
//#line 839 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 126:
//#line 854 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                this.scopeStack.pop();
                this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                this.reversePolish.addSeparation(String.format("Leaving scope '%s'...", val_peek(4).sval));
            } else {
                errorState = false;
            }
        }
break;
case 127:
//#line 870 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 128:
//#line 882 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addSeparation(String.format("Entering scope '%s'...", val_peek(0).sval));
        }
break;
case 129:
//#line 891 "gramatica.y"
{
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 131:
//#line 906 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 134:
//#line 918 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 137:
//#line 932 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 138:
//#line 943 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 139:
//#line 945 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 140:
//#line 952 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 141:
//#line 954 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 142:
//#line 959 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 143:
//#line 968 "gramatica.y"
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
case 144:
//#line 981 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 145:
//#line 983 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 146:
//#line 985 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 147:
//#line 987 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 148:
//#line 996 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 150:
//#line 1006 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 151:
//#line 1013 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 152:
//#line 1018 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 153:
//#line 1027 "gramatica.y"
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
case 154:
//#line 1041 "gramatica.y"
{
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 156:
//#line 1056 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 157:
//#line 1058 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 158:
//#line 1064 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 159:
//#line 1071 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 161:
//#line 1082 "gramatica.y"
{ this.reversePolish.makeTemporalPolishesDefinitive(); }
break;
case 162:
//#line 1083 "gramatica.y"
{ 
            if (!errorState) {

                this.reversePolish.addPolish(val_peek(4).sval);
                this.reversePolish.addPolish(val_peek(1).sval);
                this.reversePolish.addPolish(":=");

                /* Se agregan todas las polacas del bloque ejecutable.*/
                /*this.reversePolish.makeTemporalPolishesDefinitive();*/

                notifyDetection("Expresión lambda.");
                this.reversePolish.addSeparation("Leaving lambda expression body...");
            } else {
                errorState = false;
            }
        }
break;
case 163:
//#line 1103 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 164:
//#line 1106 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 165:
//#line 1108 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 166:
//#line 1110 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 167:
//#line 1117 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 168:
//#line 1122 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 169:
//#line 1125 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 170:
//#line 1132 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.reversePolish.setAggregatePoint();
            this.reversePolish.addSeparation("Entering lambda expression body...");
        }
break;
//#line 1719 "Parser.java"
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
