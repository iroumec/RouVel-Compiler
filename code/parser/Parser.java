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
   14,   14,   14,   14,   14,   14,   45,   45,   49,   50,
   50,   50,   50,   51,   51,   51,   52,   46,   46,   46,
   53,   54,   54,   54,   55,   35,   35,    8,    8,   56,
   56,   57,   57,   57,   59,   59,   58,   58,   58,   15,
   15,   15,   42,   42,   42,   42,   42,   11,   12,   12,
   13,   13,   43,   43,   60,   60,   60,   60,   61,   61,
   44,   44,   44,   44,   44,   17,   17,   17,   16,
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
    4,    4,    5,    4,    5,    3,    2,    0,    4,
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
    0,  142,  141,    0,  131,    0,    0,  132,  134,    0,
   57,    0,    0,    0,   61,   56,   33,    0,    0,    0,
    0,    0,    0,   35,    0,   58,    0,   65,    0,    0,
    0,    0,  115,  125,  124,  122,  119,  118,    0,    0,
  148,  155,    0,   68,    0,   75,   73,   95,   92,    0,
    0,    0,   53,   51,  145,    0,   19,   20,  169,  136,
  139,    0,    0,  130,  127,    0,   55,   54,   32,    0,
  167,    0,  162,  161,    0,  164,   59,   64,   13,    0,
  112,  113,  151,  150,   79,   77,   48,  144,  143,  137,
  133,  126,  163,  166,  165,  111,  110,
};
final static short yydgoto[] = {                          4,
   64,   54,   55,  127,  128,  129,   56,   18,   57,   58,
   59,  118,  119,  148,  166,   21,  180,    5,   70,   71,
   22,   96,   23,   24,   25,    6,    8,  160,   26,   99,
   27,   28,   29,   30,   31,  101,  105,  106,   93,   32,
   33,   34,   35,   36,   37,   38,   66,   67,   39,  107,
  108,  109,   40,  113,  114,   81,  167,  168,  169,   60,
   61,
};
final static short yysindex[] = {                         3,
   52,    0,    0,    0,  -28, -104,    0, -203,   18,   16,
  124,  164, -167,    0,  486,  -40, -226,   40,   26,  -46,
   98,   14,    0,    0,  101,  -63,    0,    0,    0,    0,
   27,    0,    0,    0,    0,    0,    0,    0,  -17,   13,
    0, -104,    0, -194,  130,    0,    0,  448, -186,    0,
    0,  472,  308,   32,    0,  253, -174,    0,    0,  -42,
    0,    0,   55,  460,   32,    0,   43,    0,    0, -163,
  318,    0,   66,  453,  -10,    0,    0,   36, -152,  -15,
   -9,  492, -138,    0,  503,  313,    0,    0,    0,  112,
    0,   81,  123,   30,    0,  -13,    0,    0,   48,    0,
    0,    0,  112,    0, -145,    0,    0, -132,  139,    0,
  102,  -38,   -2,    0,    0,    0,  251,    4,    0,   32,
    0,    0,   90,    0,    0,    0,   32,    0,  500,    0,
    0,  219,    0,    0,    0,    0,    0,  -23,    0,    0,
    0,    0,    0,    0,    0,   32,  500,  130,    0,   30,
    0,    0,    0, -118,   87,  -35,    0,    0,   15,   28,
  107,    0,    0, -213,    0, -232,   84,    0,    0,   25,
    0,  482,  105,  -33,    0,    0,    0,  136,  266,   33,
   81,   -5, -102,    0,   72,    0,   30,    0,   59,  152,
  -44,   96,    0,    0,    0,    0,    0,    0, -101,  130,
    0,    0,  296,    0,   32,    0,    0,    0,    0,   32,
  308,   99,    0,    0,    0,   44,    0,    0,    0,    0,
    0, -100, -213,    0,    0,   70,    0,    0,    0,  -97,
    0,  119,    0,    0,  -94,    0,    0,    0,    0,   45,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                        20,
  -92,    0,    0,    0,  168,  168,    0,    0,    0,  386,
   53,    0,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  170,   82,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,  -89,    0,
    0,  168,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -25,  142,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  418,    0,    0,    0,  159,  332,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -215,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   61,  -75,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -26,    0,    0,    0,  -22,    0,
    0,    0,    0,    0,    0,    0,   88,    0,    0,  396,
    0,    0,    0,    0,    0,    0,  302,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  427,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  188,  190,
    0,    0,    0, -215,    0,    0,    0,    0,    0,    0,
    0,    0,   17,    0,    0,    0,    0,  -75,    0,    0,
  -75,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  408,    0,    0,    0,    0,  440,
  -20,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   89,    6,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  589,  764,  398,  127,    0,  129,  680,    0,  688,  442,
  730,    0,   -6,    0,    0,    0,  -54,  189,   42,    0,
    8,  104,    0,   24,   19,    0,    0,    0,    0,    0,
  -11,  756,    0,    0,    0,    0,  -16,  179,  -39,    0,
    0,    0,    0,    0,    0,    0,   23,  145,    0,    0,
   97,    0,    0,    0,   93,    0,    0, -142,    0,    0,
  160,
};
final static int YYTABLESIZE=956;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         17,
   28,   63,    2,   51,   48,  216,   49,  125,   50,  126,
   83,   17,   89,   97,  241,  160,  136,  209,   16,    3,
   96,  220,   17,  112,  221,  165,   87,   84,  164,   41,
  187,   49,  114,  160,   78,  231,  116,  222,  183,   49,
   28,  140,  162,   79,  201,  186,  135,  200,   43,  135,
  178,    7,   17,    3,  140,   45,  198,  163,   49,   98,
   90,   44,  116,  190,   17,  115,   97,   51,   48,   83,
   49,  121,   50,   51,   49,   17,   43,   90,   50,   80,
  251,  134,   76,  149,   77,  100,   84,   17,   68,   69,
  175,  234,  193,   87,   16,  137,   51,   48,   17,   49,
  150,   50,  249,  257,  161,  103,  155,   51,   48,   17,
   49,  158,   50,  170,  158,  187,   49,  189,  173,  168,
  179,   12,  102,  230,  224,   28,  235,  223,  152,  138,
  202,  152,  138,  194,  195,  103,  192,   17,  213,  217,
   17,   63,    3,   51,   48,  215,   49,  219,   50,  225,
   44,   17,  218,  236,  242,  243,  250,  247,  253,  254,
  159,  255,  182,   52,    5,   51,   48,   15,   49,    2,
   50,   51,   48,  129,   49,  182,   50,   97,   17,  114,
  168,   87,   66,  239,   66,   66,   66,   10,  226,   11,
  146,   17,  147,  244,  252,  214,   42,  185,  128,   92,
   66,  191,   53,   63,  196,   51,   48,  138,   49,   88,
   50,  123,    0,  135,   97,   75,   10,   53,   10,   46,
   90,   10,  124,  173,  240,   11,   12,    9,   10,   13,
  160,   14,  208,   15,  111,   96,  177,   11,   12,   10,
  162,   13,  114,   14,   46,   15,  116,  181,   11,   12,
  102,   10,   46,  197,   14,  163,   15,   28,    1,    3,
  229,  103,  140,   49,   79,    0,   28,   28,  110,   10,
   28,   46,   28,   90,   28,  140,  229,   94,   11,   12,
    9,   10,   10,   46,   14,  111,   15,   46,  233,   82,
   11,   12,   10,  125,   13,  126,   14,   49,   15,  248,
  256,   11,   12,    9,   10,   13,  231,   14,  158,   15,
   49,   10,   46,   11,   12,   10,  168,   13,    0,   14,
    0,   15,   10,   46,   11,   12,   10,  237,   13,   46,
   14,    0,   15,    0,    0,   11,   12,   12,   12,   13,
   49,   14,   69,   15,   69,   69,   69,   12,   12,    0,
  125,   12,  126,   12,   10,   12,   83,   10,   10,   46,
   69,  154,    0,   11,   12,    0,   11,   12,   10,   14,
   13,   15,   14,   84,   15,   50,  153,   11,   12,   10,
   10,   46,   47,   14,    0,   15,   10,   46,   11,   12,
   50,    0,   10,    0,   14,   10,   15,   66,   66,   66,
    0,   11,   12,    0,   11,   12,   66,   14,   10,   15,
   14,    0,   15,    0,   53,   53,    0,   11,   12,   62,
   10,   46,   53,   14,    0,   15,   90,   90,   90,   90,
   90,    0,   90,    0,    0,    0,   70,    0,   70,   70,
   70,    0,    0,    0,   90,   90,   90,   90,   67,    0,
   67,   67,   67,  133,   70,   70,   70,   70,   99,    0,
   66,    0,   66,   95,    0,    0,   67,   97,    0,   69,
    0,   69,    0,    0,  206,   10,   46,   66,   66,   66,
   98,    0,   67,    0,   67,    0,   69,   69,   69,   51,
    0,    0,   49,    0,   50,  125,    0,  126,    0,   67,
   67,   67,  125,    0,  126,    0,    0,   10,  124,   10,
   46,  157,  122,   51,   48,  199,   49,    0,   50,  145,
  143,  144,   10,   46,  125,   73,  126,   51,   48,  207,
   49,    0,   50,   51,   48,   95,   49,  188,   50,    0,
  228,   51,    0,    0,   49,  125,   50,  126,    0,    0,
    0,  245,   10,   46,    0,    0,    0,   69,   69,   69,
    0,  176,    0,    0,   10,  124,   69,    0,  134,  173,
    0,    0,    0,  151,  152,    0,  232,    0,    0,  232,
    0,    0,    0,    0,    0,    0,    0,   50,   50,    0,
    0,  212,    0,    0,    0,    0,    0,    0,    0,   53,
  246,    0,    0,   74,    0,    0,    0,   85,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  188,    0,  238,    0,
    0,    0,    0,  117,    0,    0,    0,    0,    0,    0,
   53,   90,   90,   90,    0,   90,   90,   90,   90,   90,
   90,   70,   70,   70,    0,   70,   70,   70,   70,    0,
   70,  156,    0,   67,   67,   67,    0,    0,    0,    0,
  172,    0,   67,   99,   66,   66,    0,   66,   66,   66,
   66,    0,   97,   69,   69,    0,   69,   69,   69,   69,
    0,    0,   19,    0,    0,   98,   67,   67,    0,   67,
   67,   67,   67,   19,   10,   46,   86,    0,   19,   10,
  124,    0,   19,    0,    0,    0,   10,  124,    0,  139,
  140,  141,  142,    0,    0,    0,   19,   19,   10,   46,
   47,    0,    0,  132,   20,    0,  211,  227,   10,  124,
  130,   72,   10,   46,  132,   20,    0,  171,   10,   46,
   20,  130,    0,    0,   20,  204,   10,   46,    0,   10,
  124,  130,    0,    0,    0,   19,    0,    0,   20,   20,
  174,    0,  130,  174,    0,   65,   91,   19,    0,    0,
   19,    0,  131,    0,    0,    0,   19,    0,  117,    0,
   19,    0,    0,  131,  104,  104,   19,    0,    0,  132,
    0,    0,    0,  131,  130,    0,  203,   20,    0,    0,
    0,  120,    0,    0,  131,    0,    0,    0,    0,   20,
    0,    0,   20,    0,    0,  203,   65,    0,   20,    0,
    0,    0,   20,    0,    0,    0,    0,    0,   20,    0,
    0,    0,    0,  130,    0,   91,  131,    0,  184,    0,
    0,    0,    0,    0,    0,    0,    0,   19,   91,  130,
    0,  174,    0,    0,  104,   19,    0,    0,    0,    0,
    0,    0,    0,    0,   65,   65,   19,   19,    0,    0,
    0,    0,    0,    0,  132,  131,    0,    0,    0,  132,
    0,    0,  205,    0,    0,    0,    0,    0,  130,   20,
    0,  131,    0,    0,    0,    0,    0,   20,    0,    0,
  210,    0,    0,   19,    0,    0,    0,    0,   20,   20,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  184,    0,    0,    0,    0,    0,    0,
  131,    0,    0,    0,    0,  184,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   20,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,    0,   42,   43,   41,   45,   43,   47,   45,
   44,   40,   59,   25,   59,   41,   59,   41,  123,    0,
   41,  164,   40,   40,  257,   41,   19,   61,   44,    6,
   44,   45,   59,   59,   16,   41,   59,  270,   93,   45,
   40,  257,  256,  270,   41,   59,   41,   44,   59,   44,
   90,    0,   40,  257,  270,   40,   59,  271,   45,  123,
   44,   46,  257,  103,   40,   42,   78,   42,   43,   44,
   45,  258,   47,   42,   45,   40,   59,   61,   47,   40,
  223,  256,  123,   41,  125,   59,   61,   40,  256,  257,
   83,   59,  109,   86,  123,   41,   42,   43,   40,   45,
  264,   47,   59,   59,  257,  123,   41,   42,   43,   40,
   45,   59,   47,  123,  125,   44,   45,   99,  257,   59,
   40,   40,  268,  178,   41,  125,  181,   44,   41,   41,
   41,   44,   44,  111,  112,  123,  269,   40,  257,  125,
   40,   40,  123,   42,   43,   59,   45,   41,   47,  125,
   46,   40,  125,  256,   59,  257,  257,   59,  256,   41,
  125,  256,   40,   40,  257,   42,   43,    0,   45,    0,
   47,   42,   43,   40,   45,   40,   47,  189,   40,  269,
  256,  174,   41,  125,   43,   44,   45,    0,  170,    0,
   64,   40,   64,  200,  125,  154,    8,   94,   40,   21,
   59,  105,   44,   40,  112,   42,   43,   63,   45,  256,
   47,   52,   -1,  256,  226,  256,  257,   59,  257,  258,
  123,  257,  258,  257,  269,  266,  267,  256,  257,  270,
  256,  272,  256,  274,  273,  256,  125,  266,  267,  257,
  256,  270,  269,  272,  258,  274,  269,  125,  266,  267,
  268,  257,  258,  256,  272,  271,  274,  257,  256,  257,
  125,  123,  257,   45,  270,   -1,  266,  267,  256,  257,
  270,  258,  272,  257,  274,  270,  125,  264,  266,  267,
  256,  257,  257,  258,  272,  273,  274,  258,  256,  264,
  266,  267,  257,   43,  270,   45,  272,   45,  274,  256,
  256,  266,  267,  256,  257,  270,   41,  272,  256,  274,
   45,  257,  258,  266,  267,  257,  256,  270,   -1,  272,
   -1,  274,  257,  258,  266,  267,  257,  256,  270,  258,
  272,   -1,  274,   -1,   -1,  266,  267,  256,  257,  270,
   45,  272,   41,  274,   43,   44,   45,  266,  267,   -1,
   43,  270,   45,  272,  257,  274,   44,  257,  257,  258,
   59,   44,   -1,  266,  267,   -1,  266,  267,  257,  272,
  270,  274,  272,   61,  274,   44,   59,  266,  267,  257,
  257,  258,  259,  272,   -1,  274,  257,  258,  266,  267,
   59,   -1,  257,   -1,  272,  257,  274,  256,  257,  258,
   -1,  266,  267,   -1,  266,  267,  265,  272,  257,  274,
  272,   -1,  274,   -1,  256,  257,   -1,  266,  267,  256,
  257,  258,  264,  272,   -1,  274,   41,   42,   43,   44,
   45,   -1,   47,   -1,   -1,   -1,   41,   -1,   43,   44,
   45,   -1,   -1,   -1,   59,   60,   61,   62,   41,   -1,
   43,   44,   45,   56,   59,   60,   61,   62,   41,   -1,
   43,   -1,   45,   22,   -1,   -1,   59,   41,   -1,   43,
   -1,   45,   -1,   -1,  256,  257,  258,   60,   61,   62,
   41,   -1,   43,   -1,   45,   -1,   60,   61,   62,   42,
   -1,   -1,   45,   -1,   47,   43,   -1,   45,   -1,   60,
   61,   62,   43,   -1,   45,   -1,   -1,  257,  258,  257,
  258,   59,   41,   42,   43,  265,   45,   -1,   47,   60,
   61,   62,  257,  258,   43,   40,   45,   42,   43,  132,
   45,   -1,   47,   42,   43,   94,   45,   96,   47,   -1,
   59,   42,   -1,   -1,   45,   43,   47,   45,   -1,   -1,
   -1,  256,  257,  258,   -1,   -1,   -1,  256,  257,  258,
   -1,   59,   -1,   -1,  257,  258,  265,   -1,  256,  257,
   -1,   -1,   -1,  256,  257,   -1,  179,   -1,   -1,  182,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,
   -1,  150,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   11,
  203,   -1,   -1,   15,   -1,   -1,   -1,   19,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  185,   -1,  187,   -1,
   -1,   -1,   -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,
   52,  256,  257,  258,   -1,  260,  261,  262,  263,  264,
  265,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,   73,   -1,  256,  257,  258,   -1,   -1,   -1,   -1,
   82,   -1,  265,  256,  257,  258,   -1,  260,  261,  262,
  263,   -1,  256,  257,  258,   -1,  260,  261,  262,  263,
   -1,   -1,    5,   -1,   -1,  256,  257,  258,   -1,  260,
  261,  262,  263,   16,  257,  258,   19,   -1,   21,  257,
  258,   -1,   25,   -1,   -1,   -1,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   39,   40,  257,  258,
  259,   -1,   -1,   54,    5,   -1,  148,  256,  257,  258,
   53,  256,  257,  258,   65,   16,   -1,  256,  257,  258,
   21,   64,   -1,   -1,   25,  256,  257,  258,   -1,  257,
  258,   74,   -1,   -1,   -1,   78,   -1,   -1,   39,   40,
   83,   -1,   85,   86,   -1,   12,   21,   90,   -1,   -1,
   93,   -1,   53,   -1,   -1,   -1,   99,   -1,  200,   -1,
  103,   -1,   -1,   64,   39,   40,  109,   -1,   -1,  120,
   -1,   -1,   -1,   74,  117,   -1,  127,   78,   -1,   -1,
   -1,   48,   -1,   -1,   85,   -1,   -1,   -1,   -1,   90,
   -1,   -1,   93,   -1,   -1,  146,   63,   -1,   99,   -1,
   -1,   -1,  103,   -1,   -1,   -1,   -1,   -1,  109,   -1,
   -1,   -1,   -1,  156,   -1,   90,  117,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  170,  103,  172,
   -1,  174,   -1,   -1,  109,  178,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  111,  112,  189,  190,   -1,   -1,
   -1,   -1,   -1,   -1,  205,  156,   -1,   -1,   -1,  210,
   -1,   -1,  129,   -1,   -1,   -1,   -1,   -1,  211,  170,
   -1,  172,   -1,   -1,   -1,   -1,   -1,  178,   -1,   -1,
  147,   -1,   -1,  226,   -1,   -1,   -1,   -1,  189,  190,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,   -1,   -1,
  211,   -1,   -1,   -1,   -1,  190,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  226,
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

//#line 1124 "gramatica.y"

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
                notifyDetection("Condición."); 
            } else {
                errorState = false; /* TODO: creo que no debería reiniciarse el erro acá.*/
            }
        }
break;
case 93:
//#line 659 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 94:
//#line 663 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 95:
//#line 665 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 96:
//#line 672 "gramatica.y"
{
            this.reversePolish.makeTemporalPolishesDefinitive();
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 97:
//#line 680 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 98:
//#line 682 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 99:
//#line 684 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 100:
//#line 691 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 101:
//#line 695 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 106:
//#line 706 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 107:
//#line 715 "gramatica.y"
{ 
            if (!errorState) {
                this.reversePolish.fulfillPromise(this.reversePolish.getLastPromise());
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 108:
//#line 728 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 109:
//#line 733 "gramatica.y"
{
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("FB");
        }
break;
case 111:
//#line 745 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 112:
//#line 747 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 113:
//#line 749 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 116:
//#line 761 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 117:
//#line 769 "gramatica.y"
{
            /* Se obtiene la promesa del cuerpo then.*/
            int promise = this.reversePolish.getLastPromise();

            /* Se promete un nuevo punto de bifurcación.*/
            this.reversePolish.promiseBifurcationPoint();
            this.reversePolish.addPolish("IB");

            /* Se cumple la promesa obtenida al comienzo.*/
            /* Es necesario que se realice así para respetar los índices de la polaca.*/
            this.reversePolish.fulfillPromise(promise);
        }
break;
case 118:
//#line 789 "gramatica.y"
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
case 119:
//#line 802 "gramatica.y"
{ replaceLastErrorWith("La sentencia 'do-while' debe terminar con ';'."); errorState = true; }
break;
case 120:
//#line 804 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); errorState = true; }
break;
case 121:
//#line 812 "gramatica.y"
{ this.reversePolish.promiseBifurcationPoint(); }
break;
case 123:
//#line 823 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); errorState = true; }
break;
case 124:
//#line 825 "gramatica.y"
{ notifyError("Falta 'while'."); errorState = true; }
break;
case 126:
//#line 840 "gramatica.y"
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
case 127:
//#line 856 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 128:
//#line 868 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            this.scopeStack.push(val_peek(0).sval);
            this.reversePolish.addEntrySeparation(val_peek(0).sval);
        }
break;
case 129:
//#line 877 "gramatica.y"
{
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 131:
//#line 892 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 134:
//#line 904 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 137:
//#line 918 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 138:
//#line 929 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 139:
//#line 931 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 140:
//#line 938 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 141:
//#line 940 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 142:
//#line 945 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 143:
//#line 954 "gramatica.y"
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
//#line 967 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 145:
//#line 969 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 146:
//#line 971 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 147:
//#line 973 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 148:
//#line 982 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 150:
//#line 992 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 151:
//#line 999 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 152:
//#line 1004 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 153:
//#line 1013 "gramatica.y"
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
//#line 1027 "gramatica.y"
{
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 156:
//#line 1042 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 157:
//#line 1044 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 158:
//#line 1050 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 159:
//#line 1057 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 161:
//#line 1069 "gramatica.y"
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
case 162:
//#line 1084 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 163:
//#line 1087 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 164:
//#line 1089 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 165:
//#line 1091 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 166:
//#line 1098 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 167:
//#line 1103 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 168:
//#line 1106 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 169:
//#line 1113 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
        }
break;
//#line 1700 "Parser.java"
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
