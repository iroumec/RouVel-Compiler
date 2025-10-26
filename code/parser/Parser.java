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
    0,    0,   28,    0,   29,    0,    0,    0,   25,   26,
   26,   32,   26,   26,   26,   26,   31,   31,   30,   30,
   27,   27,   27,   33,   33,   35,   35,   38,   38,   39,
   39,   40,   40,   41,   41,   34,   34,   34,   34,   34,
   34,   34,   34,   44,   44,   36,   36,   36,   36,   36,
   36,    9,    9,    9,    9,   42,   42,   42,   42,   43,
   43,   43,   43,   43,   50,   50,   20,   20,   21,   21,
   10,   10,   10,   24,   22,   22,   22,   23,   23,   23,
    1,    1,    1,    1,    1,    6,    6,    2,    2,    2,
    2,    4,    4,    4,    7,    7,    3,    3,    3,    5,
    5,    5,   12,   12,   11,   11,   51,   51,   51,   51,
   52,   52,   52,   52,   16,   16,   16,   16,   16,   16,
   16,   48,   48,   53,   53,   53,   53,   54,   55,   55,
   55,   57,   49,   56,   56,   56,   56,   56,   58,   60,
   59,   59,   61,   37,   37,    8,    8,   62,   62,   63,
   63,   63,   65,   65,   64,   64,   64,   17,   17,   17,
   45,   45,   45,   45,   45,   13,   14,   14,   15,   15,
   46,   46,   66,   66,   66,   66,   67,   67,   47,   47,
   47,   47,   47,   19,   19,   19,   18,
};
final static short yylen[] = {                            2,
    2,    2,    0,    2,    0,    4,    2,    1,    1,    3,
    3,    0,    4,    2,    0,    3,    2,    2,    2,    2,
    1,    2,    2,    1,    1,    1,    2,    0,    1,    1,
    1,    3,    2,    1,    2,    2,    1,    1,    1,    1,
    1,    1,    2,    1,    1,    3,    3,    3,    3,    5,
    2,    3,    3,    2,    2,    4,    4,    3,    3,    4,
    6,    4,    6,    5,    3,    1,    2,    1,    2,    1,
    1,    3,    2,    3,    1,    3,    2,    1,    3,    2,
    1,    3,    3,    2,    2,    1,    1,    3,    1,    3,
    2,    3,    1,    3,    1,    1,    2,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    3,    2,    2,    3,
    3,    2,    3,    1,    1,    1,    1,    1,    1,    1,
    1,    3,    2,    4,    4,    3,    3,    1,    0,    2,
    1,    0,    2,    3,    3,    3,    3,    2,    2,    1,
    1,    2,    2,    5,    4,    2,    1,    3,    2,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    5,    5,    4,    3,    2,    4,    1,    3,    3,    1,
    3,    3,    3,    2,    1,    0,    1,    1,    4,    4,
    5,    4,    5,    3,    2,    0,    4,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,   21,   24,   25,   26,    0,   37,   38,
   39,   40,   41,   42,   44,   45,    0,    4,    0,   23,
    0,    0,  103,  177,    0,    0,   95,   96,    0,    0,
    0,   89,    0,    0,   98,   99,    0,  175,  123,    0,
    0,    0,    0,    0,   51,    0,    0,    0,  165,    0,
    0,    0,   17,   14,    0,    0,    0,    0,    0,    0,
   66,    0,    0,    0,    0,   43,   36,    0,   34,    0,
    0,   22,   18,    0,   29,   27,    0,  133,    6,  106,
    0,    0,  167,    0,  104,  174,    0,  101,   86,   87,
    0,   93,    0,  100,  102,    0,   91,   97,  172,  171,
  108,    0,  117,  119,  118,  120,  121,  115,  116,    0,
    0,    0,    0,    0,   30,  128,   31,  122,    0,    0,
  109,   48,   55,   47,    0,   49,   54,   46,    0,    0,
    0,    0,  164,   16,    0,    0,    0,  160,  159,    0,
  149,    0,    0,  150,  152,    0,   59,    0,    0,   67,
   58,   68,    0,    0,   33,    0,    0,    0,    0,    0,
    0,   35,    0,  138,    0,    0,    0,    0,    0,  141,
    0,    0,  166,  173,    0,   83,    0,   90,   88,  110,
  107,    0,    0,  130,    0,    0,    0,   52,   53,    0,
  163,    0,   19,   20,  187,  154,  157,    0,    0,  148,
  145,    0,   57,   56,    0,   65,   62,   60,    0,    0,
   71,   32,    0,  185,    0,  180,  179,    0,  182,   13,
  143,  142,  136,  134,  137,  135,  139,  169,  168,   94,
   92,    0,  126,  127,   50,  162,  161,  155,  151,  144,
   69,    0,   64,    0,   73,  181,  184,  183,  125,  124,
   63,   61,   72,
};
final static short yydgoto[] = {                          4,
   61,   51,   52,  111,  112,  113,   53,   17,   67,  230,
   54,   55,   56,  102,  103,  132,  162,   20,  178,   84,
  226,    0,    0,    0,    5,   21,   22,    6,    8,  156,
   23,   94,   24,   25,   26,   27,   28,   96,  136,  137,
   91,   29,   30,   31,   32,   33,   34,   35,   36,   85,
   63,   64,  138,  139,  140,   98,   37,  187,  188,  189,
  190,   78,  163,  164,  165,   57,   58,
};
final static short yysindex[] = {                         3,
   47,    0,    0,    0,  -35,  -86,    0, -195,   23,   20,
  328,  351, -205,  407,  -40, -186,   52,   26,   66,   51,
    0,   62,  -22,    0,    0,    0,    0,   41,    0,    0,
    0,    0,    0,    0,    0,    0, -176,    0,  -86,    0,
 -142,  437,    0,    0,  441, -141,    0,    0,  418,  255,
   48,    0,  266, -137,    0,    0,   87,    0,    0,   69,
  396,   48,  -24,   98,    0,  374,  -37, -136,    0,   89,
  100,  -46,    0,    0,   15, -122,  -33,   45,  428, -113,
    0,  431, -137,   13,    8,    0,    0,   64,    0,  113,
   67,    0,    0,   40,    0,    0,  -20,    0,    0,    0,
  104,  -16,    0,   48,    0,    0,  115,    0,    0,    0,
   48,    0,  295,    0,    0,  241,    0,    0,    0,    0,
    0,  -31,    0,    0,    0,    0,    0,    0,    0,   48,
  295,  437,   78,   64,    0,    0,    0,    0, -110, -100,
    0,    0,    0,    0,  -87,    0,    0,    0,  -74,    8,
  119,   22,    0,    0,   60,   68,  156,    0,    0, -232,
    0, -238,   53,    0,    0,   -5,    0,  410,  154,    0,
    0,    0,    8,  422,    0,   82,  165,  131,  113,   96,
  -47,    0,   35,    0,   -9,   -9,  136,  315,  -61,    0,
  -17,  437,    0,    0,  259,    0,   48,    0,    0,    0,
    0,   48,  255,    0,   93,   30,  348,    0,    0,  353,
    0,  327,    0,    0,    0,    0,    0,  167, -232,    0,
    0,   46,    0,    0,  386,    0,    0,    0,    8,    5,
    0,    0,  172,    0,  390,    0,    0,  178,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  336,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  447,    0,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         6,
  183,    0,    0,    0,    2,  435,    0,    0,    0,  120,
  343,    0,  402,    0,  171,    0,    0,    0,    0,  171,
    0,    4,   21,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  435,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -32,
   -1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  153, -164,    0,    0,  -19,    0,    0,    0,    0,
    0,    0,    0,    0,  171,    0, -211,    0,    0,    0,
    0,    0,   37,    0,    0,    0,    0,  171,    0,  367,
 -227,    0,    0,  171,    0,    0,  171,    0,    0,    0,
   65,    0,    0,  143,    0,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  376,
    0,    0,  256,  171,    0,    0,    0,    0,   54,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  448,  451,    0,    0,    0, -211,
    0,    0,    0,    0,    0,  171,    0,    0,   94,    0,
    0,    0,    0,    0,    0, -227,    0,    0,  206,    0,
    0,    0,  171,    0,    0,  191,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  132,    0,    0,    0,
    0,  384,  -27,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   80,  -29,    0,
    0,  171,    0,    0,   97,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  490,  657,  -23,  411,    0,  416,  427,    0,    0,  239,
  599,  565,  686,    0,  293,    0,    0,    0,   32,    0,
    0,    0,    0,    0,  485,   17,   33,    0,    0,    0,
    0,    0,  357,  467,    0,    0,    0,    0,  -79,  474,
  -62,    0,    0,    0,    0,    0,    0,    0,    0,  412,
 -108,  440,    0,    0,  356,    0,    0,    0,    0,    0,
  313,    0,    0, -143,    0,    0,  454,
};
final static int YYTABLESIZE=908;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   28,   15,    2,    2,   16,    3,  149,  161,  178,  201,
  160,  153,   40,  111,  153,   16,  216,  186,  217,   16,
  146,  148,   38,  158,  193,  176,  178,  192,  186,  117,
   60,  218,   48,   45,   16,   46,   15,   47,  159,   81,
   28,   81,   81,   81,  132,  158,    7,   75,  264,   46,
   65,   66,   46,  204,   16,   99,   80,   81,  158,   42,
   12,    3,  212,  263,  109,   41,  110,   48,   45,   80,
   46,  205,   47,   81,   16,  259,  241,  242,  154,   16,
   68,   40,   73,   76,   74,   16,   81,   15,  253,   48,
   16,   77,  199,  220,   47,   97,  219,   68,  134,   95,
   93,   16,  134,   16,  129,  170,  180,  132,  170,  121,
   48,   45,  129,   46,  100,   47,  105,   16,  118,  221,
  156,  180,  181,  156,   87,   28,  183,  150,    3,  151,
   48,   45,   16,   46,  157,   47,  234,  105,  141,  155,
   46,   70,  109,  169,  110,  120,  109,   84,  110,   84,
   84,   84,  177,  235,  105,  194,  235,  133,  153,  240,
  105,  105,  105,  105,  105,   84,  105,  166,  207,  208,
  260,  251,   82,   88,   82,   82,   82,  211,  105,  105,
  105,  105,  209,   85,  213,   85,   85,   85,  175,  237,
   82,  179,  214,  114,  244,   81,  215,   81,  222,   41,
  134,   85,   85,   85,   85,  234,  232,  233,  239,   46,
  238,  185,   81,   81,   81,   72,   10,  232,  146,  147,
    9,   10,  158,  178,  200,   11,   12,  158,  111,   13,
   11,   12,   10,   14,   13,  184,   10,  159,   14,  248,
  158,   11,   12,  133,  105,   11,   12,   10,   43,   14,
    9,   10,  185,   14,   81,   81,   81,   28,    1,    3,
   11,   12,   43,   81,   13,   43,   28,   28,   14,  169,
   28,   10,   28,  132,   28,  132,   12,   12,   10,  108,
   11,   12,   10,   43,   13,   46,   12,   12,   14,   79,
   12,   10,   12,   68,   12,    9,   10,  109,  252,  110,
   11,   12,   10,   46,   13,   11,   12,   10,   14,   13,
   46,   11,   12,   14,  131,   13,   11,   12,   10,   14,
   10,   86,  129,   10,   14,   10,   43,   11,   12,   11,
   12,   13,   11,   12,   10,   14,   48,   14,   10,   46,
   14,   47,  119,   11,   12,   10,   43,   11,   12,   10,
  105,   14,   10,   43,   70,   14,   10,  108,   11,   12,
   10,  108,   84,   84,   84,   76,   14,   49,  191,   48,
   45,   84,   46,  246,   47,  105,  105,  105,   92,  105,
  105,  105,  105,  105,  105,  257,  236,   82,   82,   82,
   60,  243,   48,   45,  270,   46,   82,   47,   85,   85,
   85,  176,   85,   85,   85,   85,  254,   85,  114,   81,
   81,  255,   81,   81,   81,   81,  112,  145,   84,   41,
   84,   10,   43,  258,  113,  186,   82,  266,   82,  261,
  267,   92,  144,  268,   15,   84,   84,   84,  109,    5,
  110,  147,  132,   82,   82,   82,   70,   10,   48,   45,
   11,   46,  109,   47,  110,  129,  127,  128,  106,   48,
   45,  186,   46,  140,   47,  229,   46,  262,  224,   48,
   45,  130,   46,  109,   47,  110,  131,  116,   48,   45,
  228,   46,   48,   47,  249,   46,   89,   47,  116,  171,
  264,   46,   39,   90,  206,  173,  198,   10,   43,  122,
   50,  247,  107,   71,    0,  272,    0,   82,    0,    0,
    0,   10,  108,    0,  250,   10,   43,    0,    0,    0,
    0,    0,   10,   43,  131,    0,    0,  132,    0,  135,
  116,  101,    0,    0,    0,    0,    0,  195,   50,   92,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  196,   10,   43,    0,   89,    0,  195,  182,    0,  152,
    0,    0,    0,  135,    0,    0,    0,    0,  168,    0,
  245,    0,    0,    0,    0,    0,    0,    0,   92,    0,
    0,    0,  256,    0,   10,   43,   44,    0,    0,    0,
    0,  269,    0,    0,    0,    0,    0,    0,  176,  135,
   89,    0,    0,   18,    0,    0,   59,   10,   43,    0,
    0,   68,    0,   18,    0,    0,   83,    0,   18,    0,
   18,  203,  186,  116,    0,    0,    0,    0,  116,  142,
  143,  112,   84,   84,    0,   84,   84,   84,   84,  113,
   82,   82,  182,   82,   82,   82,   82,    0,  114,  174,
    0,    0,   10,  108,    0,  123,  124,  125,  126,  114,
    0,   18,   69,   10,   43,  223,   10,  108,   62,  114,
    0,  182,    0,   18,   10,   43,   44,  227,  170,   43,
  114,  101,  172,  167,   10,   43,   18,   10,  108,   18,
   19,    0,   18,   10,   43,   18,    0,   10,   43,  114,
   19,  104,  271,    0,   43,   19,    0,   19,    0,    0,
    0,    0,    0,    0,  210,    0,   62,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,   18,    0,    0,  115,    0,  225,  231,    0,
    0,    0,    0,    0,    0,    0,  115,    0,   19,    0,
  114,    0,    0,    0,    0,    0,  115,    0,    0,    0,
   19,    0,    0,    0,   18,    0,  114,  115,    0,  197,
    0,    0,    0,   19,   18,    0,   19,    0,    0,   19,
    0,   18,   19,    0,    0,    0,  115,  202,    0,    0,
    0,    0,    0,  231,  265,    0,    0,    0,    0,    0,
    0,  114,    0,   18,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   19,   19,
   18,    0,    0,    0,    0,    0,  265,    0,  273,    0,
    0,    0,    0,    0,    0,    0,    0,  115,    0,    0,
    0,   62,   62,    0,    0,    0,    0,    0,    0,    0,
    0,   19,    0,  115,    0,    0,    0,    0,    0,    0,
    0,   19,    0,    0,    0,    0,    0,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  115,    0,
   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   19,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,    0,    0,   40,    0,   44,   41,   41,   41,
   44,   41,   59,   41,   44,   40,  160,   97,  257,   40,
   40,   59,    6,  256,   41,   88,   59,   44,  256,   53,
   40,  270,   42,   43,   40,   45,  123,   47,  271,   41,
   40,   43,   44,   45,  272,  257,    0,   15,   44,   45,
  256,  257,   45,  133,   40,   39,   44,   59,  270,   40,
   40,  257,   41,   59,   43,   46,   45,   42,   43,   44,
   45,  134,   47,   61,   40,  219,  185,  186,  125,   40,
   44,   59,  123,  270,  125,   40,   61,  123,   59,   42,
   40,   40,  116,   41,   47,  272,   44,   61,  123,   59,
  123,   40,  123,   40,  269,   41,   40,  272,   44,   41,
   42,   43,   59,   45,  257,   47,  258,   40,  256,  125,
   41,   40,   91,   44,   59,  125,   94,  264,  123,   41,
   42,   43,   40,   45,  257,   47,   41,   44,   41,  125,
   45,   45,   43,  257,   45,   59,   43,   41,   45,   43,
   44,   45,   40,  177,   61,   41,  180,  268,   59,  125,
   41,   42,   43,   44,   45,   59,   47,  123,  269,  257,
  125,  195,   41,  123,   43,   44,   45,   59,   59,   60,
   61,   62,  257,   41,  125,   43,   44,   45,  125,   59,
   59,  125,  125,   41,   59,   43,   41,   45,  166,   46,
  123,   59,   60,   61,   62,   41,  125,  176,  256,   45,
  179,  273,   60,   61,   62,  256,  257,  125,  256,  257,
  256,  257,  256,  256,  256,  266,  267,  257,  256,  270,
  266,  267,  257,  274,  270,  256,  257,  271,  274,  257,
  270,  266,  267,  268,  264,  266,  267,  257,  258,  274,
  256,  257,  273,  274,  256,  257,  258,  257,  256,  257,
  266,  267,  258,  265,  270,  258,  266,  267,  274,  257,
  270,  257,  272,  272,  274,  272,  256,  257,  257,  258,
  266,  267,  257,  258,  270,   45,  266,  267,  274,  264,
  270,  257,  272,  257,  274,  256,  257,   43,  269,   45,
  266,  267,  257,   45,  270,  266,  267,  257,  274,  270,
   45,  266,  267,  274,   59,  270,  266,  267,  257,  274,
  257,  256,  269,  257,  274,  257,  258,  266,  267,  266,
  267,  270,  266,  267,  257,  274,   42,  274,  257,   45,
  274,   47,  256,  266,  267,  257,  258,  266,  267,  257,
  257,  274,  257,  258,  258,  274,  257,  258,  266,  267,
  257,  258,  256,  257,  258,  270,  274,   40,  265,   42,
   43,  265,   45,   59,   47,  256,  257,  258,   22,  260,
  261,  262,  263,  264,  265,   59,  256,  256,  257,  258,
   40,  256,   42,   43,   59,   45,  265,   47,  256,  257,
  258,   59,  260,  261,  262,  263,   59,  265,  256,  257,
  258,   59,  260,  261,  262,  263,   41,   44,   43,   46,
   45,  257,  258,  257,   41,   59,   43,  256,   45,   44,
   41,   75,   59,  256,    0,   60,   61,   62,   43,  257,
   45,   40,  272,   60,   61,   62,   40,    0,   42,   43,
    0,   45,   43,   47,   45,   60,   61,   62,   41,   42,
   43,  256,   45,  273,   47,   44,   45,  229,   59,   42,
   43,   61,   45,   43,   47,   45,   61,   51,   42,   43,
   59,   45,   42,   47,  192,   45,   20,   47,   62,   59,
   44,   45,    8,   20,  139,   84,  256,  257,  258,   60,
   11,  189,   49,   14,   -1,   59,   -1,   18,   -1,   -1,
   -1,  257,  258,   -1,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,  257,  258,  269,   -1,   -1,  272,   -1,   63,
  104,   42,   -1,   -1,   -1,   -1,   -1,  111,   49,  183,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,   -1,   88,   -1,  130,   91,   -1,   70,
   -1,   -1,   -1,   97,   -1,   -1,   -1,   -1,   79,   -1,
  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  222,   -1,
   -1,   -1,  256,   -1,  257,  258,  259,   -1,   -1,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,  256,  133,
  134,   -1,   -1,    5,   -1,   -1,  256,  257,  258,   -1,
   -1,   13,   -1,   15,   -1,   -1,   18,   -1,   20,   -1,
   22,  132,  256,  197,   -1,   -1,   -1,   -1,  202,  256,
  257,  256,  257,  258,   -1,  260,  261,  262,  263,  256,
  257,  258,  176,  260,  261,  262,  263,   -1,   50,   85,
   -1,   -1,  257,  258,   -1,  260,  261,  262,  263,   61,
   -1,   63,  256,  257,  258,  256,  257,  258,   12,   71,
   -1,  205,   -1,   75,  257,  258,  259,  256,   80,  258,
   82,  192,   84,  256,  257,  258,   88,  257,  258,   91,
    5,   -1,   94,  257,  258,   97,   -1,  257,  258,  101,
   15,   45,  256,   -1,  258,   20,   -1,   22,   -1,   -1,
   -1,   -1,   -1,   -1,  150,   -1,   60,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  133,  134,   -1,   -1,   50,   -1,  173,  174,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   61,   -1,   63,   -1,
  152,   -1,   -1,   -1,   -1,   -1,   71,   -1,   -1,   -1,
   75,   -1,   -1,   -1,  166,   -1,  168,   82,   -1,  113,
   -1,   -1,   -1,   88,  176,   -1,   91,   -1,   -1,   94,
   -1,  183,   97,   -1,   -1,   -1,  101,  131,   -1,   -1,
   -1,   -1,   -1,  229,  230,   -1,   -1,   -1,   -1,   -1,
   -1,  203,   -1,  205,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  133,  134,
  222,   -1,   -1,   -1,   -1,   -1,  262,   -1,  264,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  152,   -1,   -1,
   -1,  185,  186,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  166,   -1,  168,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  176,   -1,   -1,   -1,   -1,   -1,   -1,  183,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  203,   -1,
  205,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  222,
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
"programa : nombre_programa conjunto_sentencias",
"$$1 :",
"programa : $$1 cuerpo_programa",
"$$2 :",
"programa : error $$2 nombre_programa cuerpo_programa",
"programa : error EOF",
"programa : EOF",
"nombre_programa : ID",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa : '{' conjunto_sentencias lista_llaves_cierre",
"$$3 :",
"cuerpo_programa : lista_llaves_apertura $$3 conjunto_sentencias '}'",
"cuerpo_programa : '{' '}'",
"cuerpo_programa :",
"cuerpo_programa : '{' error '}'",
"lista_llaves_apertura : '{' '{'",
"lista_llaves_apertura : lista_llaves_apertura '{'",
"lista_llaves_cierre : '}' '}'",
"lista_llaves_cierre : lista_llaves_cierre '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : error ';'",
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
"multiple_assignment : list_of_variables '=' list_of_constants",
"list_of_variables : variable",
"list_of_variables : list_of_variables ',' variable",
"list_of_variables : list_of_variables variable",
"list_of_constants : constante",
"list_of_constants : list_of_constants ',' constante",
"list_of_constants : list_of_constants constante",
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
"if : IF error",
"cuerpo_if : cuerpo_then rama_else ENDIF ';'",
"cuerpo_if : cuerpo_then rama_else ENDIF error",
"cuerpo_if : cuerpo_then rama_else ';'",
"cuerpo_if : rama_else ENDIF ';'",
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
"declaracion_funcion : inicio_funcion conjunto_parametros '{' conjunto_sentencias '}'",
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
"imprimible : '(' elemento_imprimible ')'",
"imprimible : '(' ')'",
"imprimible : elemento_imprimible",
"imprimible :",
"elemento_imprimible : STR",
"elemento_imprimible : expresion",
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

//#line 1114 "gramatica.y"

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
//#line 796 "Parser.java"
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
//#line 77 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Programa.");
            } else {
                errorState = false;
            }
        }
break;
case 2:
//#line 88 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 91 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 5:
//#line 93 "gramatica.y"
{ notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa."); }
break;
case 7:
//#line 96 "gramatica.y"
{ notifyError("Se llegó al fin del programa sin encontrar un programa válido."); }
break;
case 8:
//#line 99 "gramatica.y"
{ notifyError("El archivo está vacío."); }
break;
case 9:
//#line 106 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.PROGRAM); }
break;
case 11:
//#line 117 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al final del programa."); errorState = true; }
break;
case 12:
//#line 120 "gramatica.y"
{ notifyError("Se encontraron múltiples llaves al comienzo del programa."); errorState = true; }
break;
case 14:
//#line 123 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); errorState = true; }
break;
case 15:
//#line 125 "gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); errorState = true; }
break;
case 16:
//#line 127 "gramatica.y"
{ notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes."); errorState = true; }
break;
case 23:
//#line 153 "gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 33:
//#line 194 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 36:
//#line 212 "gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 43:
//#line 223 "gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 46:
//#line 240 "gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 47:
//#line 243 "gramatica.y"
{
            notifyDetection("Declaración de variable.");
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(1).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(1).sval, scopeStack.asText());
        }
break;
case 48:
//#line 253 "gramatica.y"
{
            notifyError("La declaración de variable debe terminar con ';'.");
        }
break;
case 49:
//#line 257 "gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 50:
//#line 261 "gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 51:
//#line 265 "gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 52:
//#line 274 "gramatica.y"
{
            this.symbolTable.setType(val_peek(2).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(2).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(2).sval, scopeStack.asText());

            this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
            this.symbolTable.setCategory(val_peek(0).sval, SymbolCategory.VARIABLE);
            this.symbolTable.setScope(val_peek(0).sval, scopeStack.asText());
        }
break;
case 53:
//#line 284 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 54:
//#line 289 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 55:
//#line 296 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 56:
//#line 310 "gramatica.y"
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
case 57:
//#line 339 "gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 58:
//#line 342 "gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 59:
//#line 345 "gramatica.y"
{ notifyError("Asignación simple inválida."); }
break;
case 60:
//#line 355 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(3).sval);
            reversePolish.addPolish(val_peek(1).sval);

            reversePolish.rearrangePairs();

            this.symbolTable.setValue(this.scopeStack.appendScope(val_peek(3).sval), val_peek(1).sval);/*yo no pondría esto, cuando $3 es una expresion queda mal*/

            notifyDetection("Asignación múltiple."); 
        }
break;
case 61:
//#line 366 "gramatica.y"
{ 
            reversePolish.addPolish(val_peek(5).sval);
            reversePolish.addPolish(val_peek(3).sval);

            reversePolish.rearrangePairs();

            this.symbolTable.setValue(this.scopeStack.appendScope(val_peek(5).sval), val_peek(3).sval);/*yo no pondría esto, cuando $3 es una expresion queda mal*/

            notifyWarning(String.format("Se descartarán las constantes posteriores a %s",val_peek(3).sval)); /*TP3*/
            notifyDetection("Asignación múltiple.");
        }
break;
case 62:
//#line 381 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 63:
//#line 383 "gramatica.y"
{ notifyError("La asignación múltiple debe terminar con ';'."); }
break;
case 64:
//#line 385 "gramatica.y"
{ notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval)); }
break;
case 65:
//#line 392 "gramatica.y"
{ this.symbolTable.setValue(this.scopeStack.appendScope(val_peek(2).sval), val_peek(0).sval); }
break;
case 67:
//#line 400 "gramatica.y"
{
            yyval.sval = val_peek(0).sval;
            reversePolish.addPolish(val_peek(0).sval);
            /* Se remueve la entrada sin el scope.*/
            this.symbolTable.removeEntry(val_peek(0).sval);
        }
break;
case 68:
//#line 410 "gramatica.y"
{ notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 69:
//#line 417 "gramatica.y"
{ reversePolish.addPolish(val_peek(1).sval); }
break;
case 70:
//#line 422 "gramatica.y"
{ notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval)); }
break;
case 72:
//#line 430 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 73:
//#line 435 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 74:
//#line 445 "gramatica.y"
{
            
        
        
        }
break;
case 76:
//#line 456 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 77:
//#line 461 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
            errorState = true;
        }
break;
case 79:
//#line 473 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 80:
//#line 478 "gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
            errorState = true;
        }
break;
case 82:
//#line 493 "gramatica.y"
{ 
            yyval.sval = val_peek(0).sval;
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 83:
//#line 501 "gramatica.y"
{  
            notifyError(String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
break;
case 84:
//#line 505 "gramatica.y"
{
            notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 85:
//#line 512 "gramatica.y"
{
            notifyError(String.format("Falta de operando en expresión previo a '+ %s'.",val_peek(0).sval));
            yyval.sval = val_peek(0).sval;
        }
break;
case 86:
//#line 522 "gramatica.y"
{ yyval.sval = "+"; }
break;
case 87:
//#line 524 "gramatica.y"
{ yyval.sval = "-"; }
break;
case 88:
//#line 531 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(0).sval; 
        }
break;
case 90:
//#line 540 "gramatica.y"
{
            notifyError(String.format(
                "Falta de operando en expresión luego de '%s %s'.",
                val_peek(2).sval, val_peek(1).sval)
            );
        }
break;
case 91:
//#line 547 "gramatica.y"
{ notifyError(String.format("Falta operando previo a '%s %s'",val_peek(1).sval,val_peek(0).sval)); }
break;
case 92:
//#line 554 "gramatica.y"
{   
            reversePolish.addTemporalPolish(val_peek(1).sval);
            yyval.sval = val_peek(2).sval;
        }
break;
case 94:
//#line 563 "gramatica.y"
{ notifyError(String.format("Falta de operando en expresión luego de '%s %s'.",val_peek(2).sval, val_peek(1).sval)); }
break;
case 95:
//#line 570 "gramatica.y"
{ yyval.sval = "/"; }
break;
case 96:
//#line 572 "gramatica.y"
{ yyval.sval = "*"; }
break;
case 97:
//#line 581 "gramatica.y"
{
            /* TODO: esto es un parche. Debe verse mejor después.*/
            this.errorCollector.removeLast(); /* Debido a que se usa el token error.*/
            reversePolish.addTemporalPolish(val_peek(1).sval);
        }
break;
case 98:
//#line 587 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 100:
//#line 598 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 101:
//#line 602 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 104:
//#line 613 "gramatica.y"
{
            notifyDetection(String.format("Constante negativa: -%s.",val_peek(0).sval));

            if(isUint(val_peek(0).sval)) {
                notifyError("El número está fuera del rango de uint, se descartará.");
                yyval.sval = null;
            } 

            this.symbolTable.replaceEntry(yyval.sval,val_peek(0).sval); 
        }
break;
case 105:
//#line 629 "gramatica.y"
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
case 106:
//#line 642 "gramatica.y"
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
case 107:
//#line 666 "gramatica.y"
{ 
            if (!errorState) {
                reversePolish.addFalseBifurcation();
                notifyDetection("Condición."); 
            } else {
                errorState = false;
            }
        }
break;
case 108:
//#line 678 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); errorState = true; }
break;
case 109:
//#line 682 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); errorState = true; }
break;
case 110:
//#line 684 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); errorState = true; }
break;
case 111:
//#line 691 "gramatica.y"
{
            reversePolish.addPolish(val_peek(1).sval);
        }
break;
case 112:
//#line 698 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 113:
//#line 700 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 114:
//#line 702 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); errorState = true; }
break;
case 115:
//#line 709 "gramatica.y"
{
            yyval.sval = ">";
        }
break;
case 116:
//#line 713 "gramatica.y"
{
            yyval.sval = "<";
        }
break;
case 121:
//#line 724 "gramatica.y"
{ notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"); }
break;
case 122:
//#line 733 "gramatica.y"
{ 
            if (!errorState) {
                reversePolish.completeSelection();
                notifyDetection("Sentencia IF."); 
            } else {
                errorState = false;
            }
        }
break;
case 123:
//#line 746 "gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 125:
//#line 755 "gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); errorState = true; }
break;
case 126:
//#line 757 "gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); errorState = true; }
break;
case 127:
//#line 759 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del IF."); errorState = true; }
break;
case 128:
//#line 764 "gramatica.y"
{ reversePolish.addInconditionalBifurcation(); }
break;
case 131:
//#line 776 "gramatica.y"
{ notifyError("Falta el bloque de sentencias del ELSE."); errorState = true; }
break;
case 132:
//#line 783 "gramatica.y"
{ reversePolish.registerDoBody(); }
break;
case 134:
//#line 789 "gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 136:
//#line 795 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 137:
//#line 797 "gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 138:
//#line 799 "gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 141:
//#line 816 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 142:
//#line 818 "gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 143:
//#line 825 "gramatica.y"
{ reversePolish.addTrueBifurcation(); }
break;
case 144:
//#line 834 "gramatica.y"
{
            if (!errorState) {
                notifyDetection("Declaración de función.");
                this.symbolTable.setType(val_peek(4).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(4).sval, SymbolCategory.FUNCTION);
                this.scopeStack.pop();
                this.symbolTable.setScope(val_peek(4).sval, this.scopeStack.asText());
                this.reversePolish.addPolish("> VOLVIENDO AL ÁMBITO ANTERIOR <");
            } else {
                errorState = false;
            }
        }
break;
case 145:
//#line 850 "gramatica.y"
{
            this.scopeStack.pop();
            notifyError("El cuerpo de la función no puede estar vacío.");
        }
break;
case 146:
//#line 862 "gramatica.y"
{ this.scopeStack.push(val_peek(0).sval); yyval.sval = val_peek(0).sval; this.reversePolish.addPolish("> NUEVO ÁMBITO <"); /* TODO: mejorar esto. Por ahora así para debugging */ }
break;
case 147:
//#line 867 "gramatica.y"
{
            this.scopeStack.push("error");
            notifyError("La función requiere de un nombre.");
            errorState = true;
        }
break;
case 149:
//#line 882 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 152:
//#line 894 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 155:
//#line 908 "gramatica.y"
{
            if (!errorState) {
                this.symbolTable.setType(val_peek(0).sval, SymbolType.UINT);
                this.symbolTable.setCategory(val_peek(0).sval, (val_peek(2).sval == "CVR" ? SymbolCategory.CVR_PARAMETER : SymbolCategory.CV_PARAMETER));
                this.symbolTable.setScope(val_peek(0).sval,scopeStack.asText());
            }
        }
break;
case 156:
//#line 919 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 157:
//#line 921 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de función."); }
break;
case 158:
//#line 928 "gramatica.y"
{ yyval.sval = "CV"; }
break;
case 159:
//#line 930 "gramatica.y"
{ yyval.sval = "CVR"; }
break;
case 160:
//#line 935 "gramatica.y"
{ notifyError("Semántica de pasaje de parámetro inválida."); errorState = true; }
break;
case 161:
//#line 944 "gramatica.y"
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
case 162:
//#line 957 "gramatica.y"
{ notifyError("La sentencia RETURN debe terminar con ';'."); }
break;
case 163:
//#line 959 "gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 164:
//#line 961 "gramatica.y"
{ notifyError("El resultado a retornar debe ir entre paréntesis."); }
break;
case 165:
//#line 963 "gramatica.y"
{ notifyError("Sentencia RETURN inválida."); }
break;
case 166:
//#line 972 "gramatica.y"
{
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 168:
//#line 982 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 169:
//#line 989 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 170:
//#line 994 "gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
case 171:
//#line 1003 "gramatica.y"
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
case 172:
//#line 1017 "gramatica.y"
{
            errorState = false;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
break;
case 174:
//#line 1032 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); errorState = true; }
break;
case 175:
//#line 1034 "gramatica.y"
{
            errorState = true;
            this.reversePolish.emptyTemporalPolishes();
            notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
break;
case 176:
//#line 1040 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 177:
//#line 1047 "gramatica.y"
{
            reversePolish.addTemporalPolish(val_peek(0).sval);
        }
break;
case 179:
//#line 1059 "gramatica.y"
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
case 180:
//#line 1074 "gramatica.y"
{ notifyError("La expresión 'lambda' debe terminar con ';'."); errorState = false; }
break;
case 181:
//#line 1077 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de cierre en expresión 'lambda'."); errorState = false; }
break;
case 182:
//#line 1079 "gramatica.y"
{ replaceLastErrorWith("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'."); errorState = false; }
break;
case 183:
//#line 1081 "gramatica.y"
{ replaceLastErrorWith("Falta delimitador de apertura en expresión 'lambda'."); errorState = false; }
break;
case 184:
//#line 1088 "gramatica.y"
{ yyval.sval = val_peek(1).sval; }
break;
case 185:
//#line 1093 "gramatica.y"
{ notifyError("El argumento de la expresión 'lambda' no puede estar vacío."); errorState = true; }
break;
case 186:
//#line 1096 "gramatica.y"
{ notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis."); errorState = true; }
break;
case 187:
//#line 1103 "gramatica.y"
{
            yyval.sval = val_peek(1).sval;
            this.symbolTable.setType(val_peek(1).sval, SymbolType.UINT);
        }
break;
//#line 1717 "Parser.java"
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
