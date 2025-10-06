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






//#line 10 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
    package parser;

    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 31 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 27 "Parser.java"




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
    0,    0,    0,    0,   13,   13,   13,   13,   14,   14,
   14,   16,   16,   16,   16,   17,   17,   17,   17,   17,
   17,   17,   15,   15,   19,   19,   22,   22,   20,   20,
   20,   20,   20,    6,    6,    6,    6,   23,   23,   23,
   23,   24,   25,   25,    7,    7,    7,   26,   26,   27,
   27,   29,   29,   28,   28,   30,   30,   30,   18,   18,
   18,   18,   18,   18,   18,   18,   31,   31,   31,   32,
   32,   37,   37,   37,   37,   37,   38,   38,   39,   39,
   39,   39,   39,   39,   39,   35,   35,   35,   35,   35,
   40,   40,   36,   36,   36,   41,   41,   41,   42,   34,
   34,   34,   34,   43,   43,    1,    1,    1,    1,   44,
   44,    2,    2,    2,    4,    4,    4,   45,   45,    3,
    3,    3,    5,    5,    5,    9,    9,    8,    8,   21,
   21,   47,   47,   33,   33,   33,   46,   46,   48,   48,
   48,   50,   50,   49,   49,   49,   51,   51,   51,   10,
   11,   11,   12,   12,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    2,    0,    3,    1,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    0,    1,    3,    3,
    3,    2,    5,    3,    3,    2,    2,    2,    4,    2,
    4,    3,    5,    1,    1,    3,    2,    8,    8,    2,
    0,    1,    1,    3,    2,    1,    2,    2,    2,    2,
    1,    1,    1,    1,    1,    1,    4,    4,    4,    1,
    1,    3,    2,    2,    2,    3,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    6,    6,    5,    5,    2,
    0,    2,    3,    3,    2,    2,    1,    2,    2,    5,
    5,    4,    4,    1,    1,    3,    1,    3,    2,    1,
    1,    3,    1,    3,    3,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    2,    1,    3,    6,
    5,    3,    2,    5,    5,    4,    1,    0,    1,    3,
    1,    2,    2,    3,    2,    2,    0,    1,    1,    4,
    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    0,    9,   23,   24,
   25,    0,   62,    0,   66,   61,   63,   64,   65,   70,
   71,    0,    6,    0,   16,   19,   17,   18,   20,   22,
   21,   15,   13,   12,   11,   14,    0,    0,    0,   90,
  126,    0,    0,    0,    0,  113,  120,  121,  122,    0,
    0,   32,    0,    0,    0,    0,   95,    0,    0,   52,
   53,    0,    0,   97,    0,    0,    0,    0,    0,    0,
   44,    0,   60,   59,   10,   28,   26,   40,   38,    0,
    0,    5,    0,    0,  151,  129,  104,    0,    0,    0,
   74,    0,  127,  124,   81,   83,   82,   84,   85,   79,
   80,  110,  111,    0,  116,  123,  125,    0,    0,  118,
  119,    0,    0,   75,   73,   30,   37,    0,    0,  149,
  148,    0,    0,    0,  139,  141,    0,   31,   36,   29,
    0,    0,   99,    0,   55,   56,    0,   98,   96,   94,
   93,    0,    0,   50,    0,    0,    0,    0,    0,   42,
    0,   45,    0,    0,  150,  103,  102,    0,   76,   72,
    0,    0,  108,    0,  114,  112,    0,    0,   34,    0,
  143,    0,    0,    0,  146,   35,    0,   58,   54,   57,
  136,    0,    0,   69,   68,   67,    0,   41,   39,    0,
   47,  153,  152,  101,  100,  117,  115,   92,   89,    0,
   88,    0,    0,  131,  140,  144,   33,  135,  134,    0,
    0,   46,   87,   86,  130,  133,    0,    0,   43,  132,
    0,   49,   48,
};
final static short yydgoto[] = {                          4,
   54,   55,   56,  114,  115,   65,  161,   57,   58,   59,
   94,   95,    5,   17,   18,   45,   46,   19,   20,   21,
   22,   87,   23,   24,   82,   25,   77,   71,   72,  147,
   26,   27,   28,   29,   30,   31,   60,   61,  118,  178,
   73,   74,  100,  119,  122,  133,  214,  134,  135,  136,
  137,
};
final static short yysindex[] = {                      -103,
    0,  -40,  -21,    0,    0,    1,   32,  -30,   77,  -32,
   46,  -14, -242,  -12,  -46,    0,   92,    0,    0,    0,
    0,    9,    0,   43,    0,    0,    0,    0,    0,    0,
    0,   12,    0,  -10,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   88, -219,   64,    0,
    0,  -38, -213,  369,   37,    0,    0,    0,    0,  -24,
  -27,    0,   83,  -33,   31, -217,    0,  -18,   70,    0,
    0,  -36,  -44,    0,  110, -205,   13,   88,   88, -188,
    0,   25,    0,    0,    0,    0,    0,    0,    0,   25,
    0,    0,   52,   44,    0,    0,    0,  -42,   19,   35,
    0,   -1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   37,    0,    0,    0,   88,  137,    0,
    0,  153, -191,    0,    0,    0,    0, -164,  -33,    0,
    0, -223,   55,   63,    0,    0, -220,    0,    0,    0,
 -146,   25,    0,  106,    0,    0,  103,    0,    0,    0,
    0,   53,   99,    0,   -9,    8,  130,   67,  -20,    0,
   14,    0, -141,   88,    0,    0,    0,  -41,    0,    0,
  162,   19,    0,   37,    0,    0,  -24,   42,    0,   84,
    0,   -3, -223, -188,    0,    0,   59,    0,    0,    0,
    0,    7,   90,    0,    0,    0,   25,    0,    0,   25,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   49,
    0,   -3,   58,    0,    0,    0,    0,    0,    0,   88,
   80,    0,    0,    0,    0,    0,   81,   87,    0,    0,
   75,    0,    0,
};
final static short yyrindex[] = {                       131,
    0,  131,    0,    0,    0,    0,  143,    0,    0,    0,
    0,    0,   94,    0,    0,    0,  138,    0,    0,    0,
    0,   23,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    3,  383,    0,    0,    0,    0,    0,
    0,    0, -119,  -16,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   34,    0,   50,    0,    0,    0,    0,    0,  108,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  393,    0,    0,    0,    0,    0,    0,
    0,    0,   60,    0,    0,    0,    0,    0,  -16,    0,
    0, -215,    0,  109,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -5,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    5,    0,  422,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  411,   28,  -79,    0,    0,    0,    0,  379,  -61,  381,
    0,   -8,  155,   79,  144,    0,    0,   20,    0,    0,
    0,    0,    0,    0,    4,    0,    0,   10,  -25,    0,
    0,    0,    0,    0,    0,    0,   -7,  114,    0,    0,
    0,   86,    0,    0,   48,   39,  -52,    0, -126,    0,
    0,
};
final static int YYTABLESIZE=687;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   42,  142,  101,   52,  142,  181,   53,   64,   53,   49,
  132,   42,   84,  125,  151,   13,  167,  205,   13,    3,
  160,   52,   27,   80,  138,   75,   53,   76,  162,   13,
   70,   80,  130,    8,  123,  128,  158,   96,  128,  170,
   81,  147,  176,   78,  103,   77,  142,  131,   81,  184,
  112,  154,  113,  155,  147,  128,  215,  200,   53,   44,
  143,  112,   27,  113,  148,  219,  194,   86,  158,   53,
   44,   47,  199,   13,  141,  168,  177,   48,  121,   70,
  187,   34,    3,  120,  165,   13,   90,  164,  146,  140,
  154,  207,  179,  154,  112,  182,  113,   13,   69,  201,
  211,   89,  145,   33,   98,  145,  183,  224,   53,   13,
  186,  191,   48,   69,   92,  202,   52,  217,   91,  213,
   13,   53,  129,  229,  212,   43,  128,  231,   48,  220,
    7,   13,   53,  233,   51,  221,   91,    2,  222,  192,
  228,  112,   13,  113,  128,   13,  174,   27,  105,  137,
  152,  208,    1,    2,   53,  203,   16,  149,   13,  225,
   85,  171,  197,  188,  193,  102,  190,  180,   69,    0,
    0,    0,  112,    0,  113,    0,    0,   85,    0,    0,
    0,   53,  226,  128,  128,  128,  128,  128,  196,  128,
    0,    0,    0,    0,  145,    0,   70,   53,    0,    0,
    0,  128,  128,  128,  128,  230,   53,    0,    0,   83,
    0,  150,    0,  166,  204,    6,    7,  147,    7,   51,
    7,   51,  130,   62,   63,    8,    9,  189,  124,   10,
  147,   11,    7,   12,   32,    7,   68,  131,    7,   51,
  147,    8,    9,   78,    8,    9,    7,   11,   10,   12,
   11,   79,   12,  147,  169,    8,    9,   35,   78,   10,
   77,   11,  218,   12,    7,  104,   36,   37,   35,  198,
   38,   51,   39,   40,   41,    7,  104,   36,   37,   27,
    0,   38,   51,   39,   40,   41,  138,  139,   27,   27,
   13,  227,   27,    0,   27,    0,   27,  209,   88,   13,
   13,   67,    7,   13,  223,   13,    0,   13,    7,  104,
  210,    8,    9,    6,    7,   91,  163,   11,   68,   12,
    7,   51,   97,    8,    9,  144,    7,   10,   91,   11,
  232,   12,   50,    7,   51,    8,    9,    7,  126,  127,
    0,   11,    0,   12,    7,   51,    8,    9,    7,    0,
   10,    0,   11,    0,   12,    7,  104,    8,    9,    7,
    0,   10,    7,   11,    0,   12,    7,   51,    8,    9,
   85,    8,    9,    0,   11,    0,   12,   11,    0,   12,
   14,   14,   15,   15,    0,  195,    7,  104,   66,   14,
    0,   15,  173,    7,   51,   14,    0,   15,  128,  128,
  128,    0,  128,  128,  128,  128,  128,  128,  175,    7,
   51,  112,   14,  113,   15,    0,    0,  206,    7,   51,
    0,    0,    0,  107,    0,  107,  107,  107,  111,  109,
  110,    0,  116,  109,  117,  109,  109,  109,   14,    0,
   15,  107,  107,  107,  107,    0,    0,   14,    0,   15,
    0,  109,  109,  109,  109,    0,    0,   93,  159,   99,
    0,    0,  106,    0,  106,  106,  106,    0,    0,    0,
    0,  116,    0,  117,    0,    0,    0,  116,    0,  117,
  106,  106,  106,  106,    0,  153,    0,    0,  156,  157,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  185,    0,    0,    0,    0,
    0,    0,   14,    0,   15,   14,    0,   15,  172,    0,
    0,  116,    0,  117,  116,  116,  117,  117,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  116,    0,  117,    0,    0,   14,    0,   15,    0,    0,
    0,    0,  216,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   93,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   14,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   14,    0,   15,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    7,  104,    0,  105,  106,
  107,  108,    0,    0,    0,    0,    0,    0,  107,  107,
  107,    0,  107,  107,  107,  107,    0,  107,  109,  109,
  109,    0,  109,  109,  109,  109,    0,  109,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  106,  106,  106,
    0,  106,  106,  106,  106,    0,  106,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   41,   40,   44,  132,   45,   40,   45,   40,
   44,    0,   59,   41,   59,   40,   59,   59,   40,  123,
   82,   40,    0,   44,   41,   40,   45,  270,   90,   40,
   11,   44,  256,    0,   60,   41,  257,  257,   44,   41,
   61,  257,  122,   41,  258,   41,  264,  271,   61,  270,
   43,  257,   45,   41,  270,   61,  183,   44,   45,   59,
   68,   43,   40,   45,   72,   59,   59,   59,  257,   45,
   59,   40,   59,   40,   44,   41,  268,   46,   42,   60,
  142,    3,  123,   47,   41,   40,   44,   44,   69,   59,
   41,  171,  257,   44,   43,   41,   45,   40,  123,  161,
   59,   59,   41,  125,   41,   44,   44,   59,   45,   40,
  257,   59,   46,  123,  125,  257,   40,   59,   59,  123,
   40,   45,   40,   44,   41,  125,   44,   41,   46,   40,
    0,   40,   45,   59,   41,  197,  125,    0,  200,   41,
  220,   43,   40,   45,  264,   40,  119,  125,   41,   41,
   41,  177,  256,  257,   45,  164,    2,   72,  125,  212,
   17,  114,  159,  144,  155,   52,  147,  129,  123,   -1,
   -1,   -1,   43,   -1,   45,   -1,   -1,   34,   -1,   -1,
   -1,   45,  125,   41,   42,   43,   44,   45,   59,   47,
   -1,   -1,   -1,   -1,  125,   -1,  177,   45,   -1,   -1,
   -1,   59,   60,   61,   62,  125,   45,   -1,   -1,  256,
   -1,  256,   -1,  256,  256,  256,  257,  257,  257,  258,
  257,  258,  256,  256,  257,  266,  267,  125,  256,  270,
  270,  272,  257,  274,  256,  257,  273,  271,  257,  258,
  257,  266,  267,  256,  266,  267,  257,  272,  270,  274,
  272,  264,  274,  270,  256,  266,  267,  257,  256,  270,
  256,  272,  256,  274,  257,  258,  266,  267,  257,  256,
  270,  258,  272,  273,  274,  257,  258,  266,  267,  257,
   -1,  270,  258,  272,  273,  274,  256,  257,  266,  267,
  257,  213,  270,   -1,  272,   -1,  274,  256,  256,  266,
  267,  256,  257,  270,  256,  272,   -1,  274,  257,  258,
  269,  266,  267,  256,  257,  256,  265,  272,  273,  274,
  257,  258,  259,  266,  267,  256,  257,  270,  269,  272,
  256,  274,  256,  257,  258,  266,  267,  257,  256,  257,
   -1,  272,   -1,  274,  257,  258,  266,  267,  257,   -1,
  270,   -1,  272,   -1,  274,  257,  258,  266,  267,  257,
   -1,  270,  257,  272,   -1,  274,  257,  258,  266,  267,
  227,  266,  267,   -1,  272,   -1,  274,  272,   -1,  274,
    2,    3,    2,    3,   -1,  256,  257,  258,   10,   11,
   -1,   11,  256,  257,  258,   17,   -1,   17,  256,  257,
  258,   -1,  260,  261,  262,  263,  264,  265,  256,  257,
  258,   43,   34,   45,   34,   -1,   -1,  256,  257,  258,
   -1,   -1,   -1,   41,   -1,   43,   44,   45,   60,   61,
   62,   -1,   54,   41,   54,   43,   44,   45,   60,   -1,
   60,   59,   60,   61,   62,   -1,   -1,   69,   -1,   69,
   -1,   59,   60,   61,   62,   -1,   -1,   47,   80,   49,
   -1,   -1,   41,   -1,   43,   44,   45,   -1,   -1,   -1,
   -1,   93,   -1,   93,   -1,   -1,   -1,   99,   -1,   99,
   59,   60,   61,   62,   -1,   75,   -1,   -1,   78,   79,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  137,   -1,   -1,   -1,   -1,
   -1,   -1,  144,   -1,  144,  147,   -1,  147,  118,   -1,
   -1,  153,   -1,  153,  156,  157,  156,  157,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  172,   -1,  172,   -1,   -1,  177,   -1,  177,   -1,   -1,
   -1,   -1,  184,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  164,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  213,   -1,  213,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  227,   -1,  227,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,  256,  257,
  258,   -1,  260,  261,  262,  263,   -1,  265,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,
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
"programa : ID cuerpo_programa",
"programa : ID conjunto_sentencias",
"programa : cuerpo_programa",
"programa : error",
"cuerpo_programa : '{' conjunto_sentencias '}'",
"cuerpo_programa : '{' '}'",
"cuerpo_programa :",
"cuerpo_programa : '{' error '}'",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : error punto_sincronizacion_sentencia",
"punto_sincronizacion_sentencia : ';'",
"punto_sincronizacion_sentencia : '}'",
"punto_sincronizacion_sentencia : token_inicio_sentencia",
"punto_sincronizacion_sentencia : EOF",
"token_inicio_sentencia : ID",
"token_inicio_sentencia : IF",
"token_inicio_sentencia : UINT",
"token_inicio_sentencia : PRINT",
"token_inicio_sentencia : DO",
"token_inicio_sentencia : RETURN",
"token_inicio_sentencia : WHILE",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT ID error",
"declaracion_variable : UINT lista_variables error",
"declaracion_variable : UINT error",
"declaracion_variable : UINT variable DASIG constante ';'",
"lista_variables : ID ',' ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"lista_variables : ID ID",
"asignacion_multiple : inicio_par_variable_constante ';'",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes ';'",
"asignacion_multiple : inicio_par_variable_constante error",
"asignacion_multiple : inicio_par_variable_constante ',' lista_constantes error",
"inicio_par_variable_constante : variable par_variable_constante constante",
"par_variable_constante : ',' variable par_variable_constante constante ','",
"par_variable_constante : '='",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')' ';'",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')' error",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"conjunto_sentencias_ejecutables : error sentencia_ejecutable",
"sentencia_ejecutable : invocacion_funcion ';'",
"sentencia_ejecutable : invocacion_funcion error",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion ';'",
"asignacion_simple : variable DASIG expresion error",
"asignacion_simple : variable error expresion ';'",
"sentencia_control : if",
"sentencia_control : do_while",
"condicion : '(' cuerpo_condicion ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : cuerpo_condicion error",
"condicion : '(' cuerpo_condicion error",
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
"if : IF condicion cuerpo_ejecutable rama_else error",
"if : IF error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"do_while : DO cuerpo_do ';'",
"do_while : DO cuerpo_do error",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do : fin_cuerpo_do",
"cuerpo_do : cuerpo_ejecutable condicion",
"fin_cuerpo_do : WHILE condicion",
"impresion : PRINT '(' imprimible ')' ';'",
"impresion : PRINT '(' imprimible ')' error",
"impresion : PRINT '(' ')' ';'",
"impresion : PRINT '(' ')' error",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : expresion operador_suma error",
"expresion : expresion termino_simple",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"termino_simple : termino_simple operador_multiplicacion factor",
"termino_simple : factor_simple",
"termino_simple : termino_simple operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : invocacion_funcion",
"factor_simple : variable",
"factor_simple : CTE",
"factor_simple : invocacion_funcion",
"constante : CTE",
"constante : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion",
"declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
"cuerpo_funcion : '{' conjunto_sentencias '}'",
"cuerpo_funcion : '{' '}'",
"sentencia_retorno : RETURN '(' expresion ')' ';'",
"sentencia_retorno : RETURN '(' expresion ')' error",
"sentencia_retorno : RETURN '(' ')' ';'",
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
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : argumento",
"lista_argumentos : lista_argumentos ',' argumento",
"argumento : expresion FLECHA ID",
"argumento : expresion",
};

//#line 704 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

// ====================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ====================================================================================================================

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

public Parser(Lexer lexer) {
    
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    this.readAgain = false;
    
    // Descomentar la siguiente línea para activar el debugging.
    yydebug = true;
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

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// --------------------------------------------------------------------------------------------------------------------

Token getAppropiateToken() {

    Token token;
    // Se lee nuevamente el último token.
    // Útil para recuperar el token de sincronización en reglas de error.
    if (this.readAgain) {
        token = lexer.getCurrentToken();
        this.readAgain = false;
    } else {
        token = lexer.getNextToken();
    }

    return token;
}

// --------------------------------------------------------------------------------------------------------------------

public void descartarTokensHasta(int tokenEsperado) {

    int t = yylex();
    
    // Se pide un token mientras no se halle el token esperado
    // o el final de archivo.
    while (t != tokenEsperado && t != EOF) {
        t = yylex();
    }

    if (t == EOF) {
        Printer.printBetweenSeparations("SE LLEGÓ AL FINAL DEL ARCHIVO SIN ENCONTRAR UN TOKEN DE SINCRONIZACION.");
    }

    // Se actualizá que se halló el token deseado o se llegó al final del archivo.
    yychar = t;
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
    /*
    // 'yylval' contiene el valor del token que el parser no pudo procesar.
    String errorMessage;
    if (yylval != null) {
        errorMessage = String.format(
            "Error de sintaxis: token inesperado '%s'.",
            yylval.sval
        );
    } else {
        errorMessage = "Error de sintaxis: se encontró un token inesperado.";
    }

    Printer.printBetweenSeparations(errorMessage);*/
}

// --------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// --------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// --------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// --------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// --------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

// --------------------------------------------------------------------------------------------------------------------

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// --------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// --------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ====================================================================================================================
// FIN DE CÓDIGO
// ====================================================================================================================
//#line 777 "Parser.java"
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
//#line 68 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 73 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 75 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 77 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
            descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
        }
break;
case 6:
//#line 91 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 7:
//#line 93 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 8:
//#line 95 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 11:
//#line 107 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado a nivel de sentencia."); }
break;
case 29:
//#line 160 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variables."); }
break;
case 30:
//#line 165 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 31:
//#line 169 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables debe terminar con ';'.");
        }
break;
case 32:
//#line 173 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Declaración de variables inválida.");
        }
break;
case 33:
//#line 177 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
break;
case 35:
//#line 187 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 36:
//#line 192 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 37:
//#line 198 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 38:
//#line 210 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 39:
//#line 212 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 40:
//#line 215 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 41:
//#line 217 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La asignación múltiple debe terminar con ';'."); }
break;
case 46:
//#line 238 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 47:
//#line 243 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 48:
//#line 256 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 49:
//#line 258 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("La expresión 'lambda' debe terminar con ';'."); }
break;
case 51:
//#line 269 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 55:
//#line 289 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 58:
//#line 299 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error capturado en sentencia ejecutable"); }
break;
case 60:
//#line 307 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La invocación a función debe terminar con ';'."); }
break;
case 67:
//#line 320 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 68:
//#line 325 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las asignaciones simples deben terminar con ';'."); }
break;
case 69:
//#line 327 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
break;
case 73:
//#line 348 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 74:
//#line 351 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 75:
//#line 354 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 76:
//#line 357 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 78:
//#line 369 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 85:
//#line 385 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 86:
//#line 395 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 87:
//#line 405 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar con ';'."); }
break;
case 88:
//#line 407 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif'."); }
break;
case 89:
//#line 409 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizar con 'endif' y ';'."); }
break;
case 90:
//#line 411 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida."); }
break;
case 93:
//#line 427 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia 'do-while'."); }
break;
case 94:
//#line 432 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'do-while' debe terminar con ';'."); }
break;
case 95:
//#line 434 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia 'do-while' inválida."); }
break;
case 97:
//#line 446 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 98:
//#line 448 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 100:
//#line 469 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia PRINT."); }
break;
case 101:
//#line 474 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 102:
//#line 476 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 103:
//#line 478 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento y debe terminar con ';'."); }
break;
case 108:
//#line 499 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ 
            notifyError("Falta de operando en expresión."); 
        }
break;
case 109:
//#line 503 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
        }
break;
case 113:
//#line 523 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 114:
//#line 525 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 116:
//#line 533 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 117:
//#line 535 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 127:
//#line 568 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 129:
//#line 576 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 130:
//#line 585 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 131:
//#line 590 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 133:
//#line 601 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 135:
//#line 610 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 136:
//#line 612 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 138:
//#line 623 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 141:
//#line 635 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 145:
//#line 650 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 146:
//#line 652 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 149:
//#line 661 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
            descartarTokenError();
        }
break;
case 150:
//#line 673 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 152:
//#line 684 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 153:
//#line 691 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 154:
//#line 696 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1254 "Parser.java"
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
