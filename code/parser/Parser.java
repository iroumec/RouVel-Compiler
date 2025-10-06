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
   14,   16,   16,   16,   17,   17,   17,   17,   17,   17,
   17,   15,   15,   19,   19,   22,   22,   23,   23,   20,
   20,   20,    6,    6,    6,   24,    7,    7,    7,   25,
   26,   26,   28,   28,   27,   27,   29,   29,   18,   18,
   18,   18,   18,   18,   18,   30,   30,   31,   31,   36,
   36,   36,   36,   36,   37,   37,   38,   38,   38,   38,
   38,   38,   38,   34,   34,   34,   34,   39,   39,   35,
   35,   35,   40,   40,   40,   41,   33,   33,   33,   33,
   42,   42,    1,    1,    1,    1,   43,   43,    2,    2,
    2,    4,    4,    4,   44,   44,    3,    3,    3,    5,
    5,    5,    9,    9,    8,    8,   21,   21,   46,   46,
   32,   32,   32,   45,   45,   47,   47,   47,   49,   49,
   48,   48,   48,   50,   50,   50,   10,   11,   11,   12,
   12,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    2,    0,    3,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    0,    1,    1,    1,    3,
    3,    4,    1,    3,    2,    4,    1,    3,    2,    8,
    2,    0,    1,    1,    3,    2,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    4,    4,    1,    1,    3,
    2,    2,    2,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    6,    6,    5,    2,    0,    2,    3,
    3,    2,    2,    1,    2,    2,    5,    5,    4,    4,
    1,    1,    3,    1,    3,    2,    1,    1,    3,    1,
    3,    3,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    6,    5,    3,    2,
    5,    5,    4,    1,    0,    1,    3,    1,    2,    2,
    3,    2,    2,    0,    1,    1,    4,    1,    3,    3,
    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   49,    1,    0,    9,   22,   23,
   24,    0,   51,   55,   50,   52,   53,   54,   58,   59,
    0,    6,    0,    0,    0,    0,   77,    0,  113,    0,
    0,    0,    0,  100,  107,  108,  109,    0,    0,    0,
    0,    0,    0,   82,    0,    0,   43,   44,    0,    0,
   84,    0,    0,    0,   35,    0,    0,    0,    0,    0,
   10,   27,   25,    8,    5,    0,    0,  138,  116,   91,
    0,    0,    0,   62,    0,  114,  111,   69,   71,   70,
   72,   73,   67,   68,   97,   98,    0,  103,  110,  112,
    0,    0,  105,  106,    0,    0,   63,   61,   31,    0,
  136,  135,    0,    0,    0,  126,  128,    0,    0,   30,
   86,   46,   47,    0,   85,   83,   81,   80,    0,    0,
   41,    0,   34,    0,   37,    0,    0,   15,   18,   16,
   17,   19,   21,   20,   13,   12,   11,   14,    0,    0,
  137,   90,   89,    0,   64,   60,    0,    0,   95,    0,
  101,   99,    0,    0,    0,  130,    0,    0,    0,    0,
  133,   32,   45,   48,  123,    0,    0,   36,    0,   39,
   57,   56,  140,  139,   88,   87,  104,  102,   79,    0,
   76,    0,    0,  118,  127,  131,  122,  121,    0,   38,
   75,   74,  117,  120,    0,    0,  119,    0,   28,   29,
   40,
};
final static short yydgoto[] = {                          4,
   42,   43,   44,   97,   98,   13,  134,   45,   46,   47,
   77,   78,    5,   17,   18,  147,  148,   19,   20,   21,
   22,   73,  211,   23,   24,   64,   58,   59,  124,   25,
   26,   27,   28,   29,   30,   48,   49,  101,  164,   60,
   61,   83,  102,  105,  114,  194,  115,  116,  117,  118,
};
final static short yysindex[] = {                      -108,
    0,   36,  -40,    0,    0,  -18,  -14,  -35,   44,  -28,
   -7, -230,  -26, -206,    0,    0,   48,    0,    0,    0,
    0,   -4,    0,    0,    0,    0,    0,    0,    0,    0,
  -80,    0,   13,  138, -205,    6,    0,  -18,    0,   32,
 -202,  112,    7,    0,    0,    0,    0,   70,   -9,    2,
   27,  -31,  121,    0,    3,  -24,    0,    0,  -38,  -56,
    0,   55, -191,   29,    0, -189,   26,  138,  138,  182,
    0,    0,    0,    0,    0,   -6,  -20,    0,    0,    0,
  -53,   77,   40,    0,   -3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    7,    0,    0,    0,
  138,  136,    0,    0,  144, -182,    0,    0,    0,  -31,
    0,    0, -237,   51,   49,    0,    0, -234,   43,    0,
    0,    0,    0,   73,    0,    0,    0,    0,   45,   19,
    0,  -25,    0,   30,    0,   66,  123,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -154,  138,
    0,    0,    0,   10,    0,    0,  161,   77,    0,    7,
    0,    0,   70,  -55,   64,    0,  -17, -237,   61, -149,
    0,    0,    0,    0,    0,   65,   74,    0,   26,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   72,
    0,  -17,   59,    0,    0,    0,    0,    0,  138,    0,
    0,    0,    0,    0,   25,   71,    0,   82,    0,    0,
    0,
};
final static short yyrindex[] = {                       115,
    0,  115,    0,    0,    0,   -2,    0,    0,    0,    0,
    0,   75,    0,    0,    0,    0,  117,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   92,    0,    0,
    0,   16,  102,    0,    0,    0,    0,    0,    0,    0,
  250,  -21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   39,    0,    0,    0,    0,
    0,   78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  126,    0,    0,    0,
    0,    0,    0,    0,    0,  -13,    0,    0,    0,  -21,
    0,    0, -226,    0,   86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   22,    0,  151,
    0,    0,    0,    0,    0,    0,    0,  -33,   46,   50,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  363,   21,  -78,    0,    0,  109,    0,  418,  -37,  420,
    0,  -22,  127,   11,   -8,    0,    0,  388,    0,    0,
    0,    0,    0,    0,    0,    0,    8,  -19,    0,    0,
    0,    0,    0,    0,    0,   23,   90,    0,    0,    0,
   97,    0,    0,   35,   67,  -34,    0,  -96,    0,    0,
};
final static int YYTABLESIZE=625;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   26,   40,  128,  191,   40,  153,   41,  129,   71,   41,
  129,   12,  113,   33,    3,   12,  166,   66,  111,  125,
  151,   34,  169,  150,   71,   36,  162,   35,  106,  135,
  134,  108,   62,  112,   67,  170,   95,  156,   96,   63,
   26,   33,   40,  134,   74,   78,   81,   41,  104,   68,
   41,   79,   12,  103,   72,   86,   66,   69,   33,  176,
  109,   95,   65,   96,   12,  131,  110,  133,  186,  132,
   41,  195,   84,  179,   41,   12,   41,  121,  188,  141,
  154,  125,  141,   52,   32,  163,  115,   12,  178,  115,
  132,  167,  168,  132,   56,  129,  180,   56,   12,   41,
  122,  172,  183,  175,  192,  193,   35,  169,   95,   12,
   96,  208,   12,  199,    7,   42,    2,   53,   92,   95,
  206,   96,  160,  198,  181,   26,  124,  184,   16,   85,
  202,  157,  115,  115,  115,  115,  115,   75,  115,  177,
  210,  200,   94,  189,   94,   94,   94,    1,    2,  207,
  115,  115,  115,  115,   95,  126,   96,  203,    3,    0,
   94,   94,   94,   94,   66,   95,   96,   96,   96,   96,
   96,   94,   92,   93,    0,    0,  165,    0,    0,  120,
   41,  182,   41,  204,   96,   96,   96,   96,   41,    0,
    0,   93,   56,   93,   93,   93,   71,  173,    0,  127,
    0,    0,  152,  205,    0,   41,    0,    0,    0,   93,
   93,   93,   93,  190,    0,   31,    6,    0,   38,   39,
   37,   38,   39,  134,  111,    7,    8,   54,    6,    9,
   65,   10,    6,   11,   55,  134,  134,    7,    8,  112,
  146,    7,    8,   10,   55,   11,  107,   10,  134,   11,
   38,   87,  155,  115,   33,   78,   26,   26,  149,   38,
   39,  115,   38,   39,   80,  185,   26,   26,   70,    6,
   26,   66,   26,    0,   26,   38,   87,   65,    7,    8,
   70,    6,    9,   39,   10,    0,   11,   39,   38,   39,
    7,    8,    6,   33,    9,    0,   10,    0,   11,   50,
   51,    7,    8,   70,    6,    9,  145,   10,   33,   11,
    0,   38,   39,    7,    8,    6,    0,    9,    0,   10,
  197,   11,   38,   87,    7,    8,    6,  201,    9,    6,
   10,    0,   11,   38,   87,    7,    8,  209,    7,    8,
    0,   10,    0,   11,   10,    0,   11,  115,  115,  115,
    0,  115,  115,  115,  115,    0,  115,   94,   94,   94,
    0,   94,   94,   94,   94,    0,   94,    0,   38,   87,
    0,   88,   89,   90,   91,    0,  119,   65,    0,   38,
   87,   96,   96,   96,    0,   96,   96,   96,   96,    0,
   96,  159,   38,   39,   38,   39,   76,   57,   82,  161,
   38,   39,    0,    0,    0,    0,   93,   93,   93,    0,
   93,   93,   93,   93,    0,   93,  187,   38,   39,   14,
   14,   15,   15,    0,  130,    0,    0,   14,    0,   15,
  136,  137,    0,    0,   14,   57,   15,    0,  138,    0,
    0,    0,    0,  123,    0,    0,    0,  139,  140,    0,
   14,  141,   15,  142,  143,  144,    0,    0,    0,   99,
    0,  100,    0,  158,    0,   14,    0,   15,    0,    0,
    0,    0,    0,   14,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   99,    0,  100,    0,    0,    0,   99,
    0,  100,    0,    0,    0,   33,   33,    0,    0,    0,
    0,  174,   76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  171,    0,    0,    0,    0,
    0,   14,    0,   15,    0,    0,    0,   99,    0,  100,
   57,    0,    0,   99,   99,  100,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   99,    0,  100,    0,    0,
   14,    0,   15,    0,    0,    0,    0,  196,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   14,    0,   15,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   14,    0,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   59,   40,   59,   45,   41,   17,   45,
   44,   40,   44,    3,  123,   40,  113,   44,  256,   41,
   41,   40,  257,   44,   33,   40,  105,   46,   48,   67,
  257,   41,   40,  271,   61,  270,   43,   41,   45,  270,
   40,   44,   40,  270,  125,   59,   41,   45,   42,  256,
   45,  257,   40,   47,   59,  258,   41,  264,   61,   41,
   59,   43,   41,   45,   40,  257,   40,  257,   59,   41,
   45,  168,   41,   44,   45,   40,   45,   55,  157,   41,
   41,   59,   44,   40,  125,  268,   41,   40,   59,   44,
   41,   41,   44,   44,  123,   41,  134,  123,   40,   45,
  125,   59,  257,   59,   41,  123,   46,  257,   43,   40,
   45,   41,   40,   40,    0,   41,    0,    9,   41,   43,
  199,   45,  102,   59,   59,  125,   41,  150,    2,   40,
   59,   97,   41,   42,   43,   44,   45,  125,   47,  132,
   59,  179,   41,  163,   43,   44,   45,  256,  257,  125,
   59,   60,   61,   62,   43,   59,   45,  192,  123,   -1,
   59,   60,   61,   62,   44,   43,   41,   45,   43,   44,
   45,   60,   61,   62,   -1,   -1,  110,   -1,   -1,   59,
   45,   59,   45,  125,   59,   60,   61,   62,   45,   -1,
   -1,   41,  123,   43,   44,   45,  205,  125,   -1,  256,
   -1,   -1,  256,  193,   -1,   45,   -1,   -1,   -1,   59,
   60,   61,   62,  269,   -1,  256,  257,   -1,  257,  258,
  256,  257,  258,  257,  256,  266,  267,  256,  257,  270,
  257,  272,  257,  274,  273,  257,  270,  266,  267,  271,
   59,  266,  267,  272,  273,  274,  256,  272,  270,  274,
  257,  258,  256,  256,  257,  269,  256,  257,  265,  257,
  258,  264,  257,  258,  259,  256,  266,  267,  256,  257,
  270,  256,  272,   -1,  274,  257,  258,  256,  266,  267,
  256,  257,  270,  258,  272,   -1,  274,  258,  257,  258,
  266,  267,  257,   44,  270,   -1,  272,   -1,  274,  256,
  257,  266,  267,  256,  257,  270,  125,  272,   59,  274,
   -1,  257,  258,  266,  267,  257,   -1,  270,   -1,  272,
  256,  274,  257,  258,  266,  267,  257,  256,  270,  257,
  272,   -1,  274,  257,  258,  266,  267,  256,  266,  267,
   -1,  272,   -1,  274,  272,   -1,  274,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,  257,  258,
   -1,  260,  261,  262,  263,   -1,  256,  257,   -1,  257,
  258,  256,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  256,  257,  258,  257,  258,   34,   10,   36,  256,
  257,  258,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,  256,  257,  258,    2,
    3,    2,    3,   -1,   62,   -1,   -1,   10,   -1,   10,
   68,   69,   -1,   -1,   17,   48,   17,   -1,  257,   -1,
   -1,   -1,   -1,   56,   -1,   -1,   -1,  266,  267,   -1,
   33,  270,   33,  272,  273,  274,   -1,   -1,   -1,   42,
   -1,   42,   -1,  101,   -1,   48,   -1,   48,   -1,   -1,
   -1,   -1,   -1,   56,   -1,   56,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   76,   -1,   76,   -1,   -1,   -1,   82,
   -1,   82,   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,
   -1,  124,  150,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  118,   -1,   -1,   -1,   -1,
   -1,  124,   -1,  124,   -1,   -1,   -1,  130,   -1,  130,
  163,   -1,   -1,  136,  137,  136,  137,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  158,   -1,  158,   -1,   -1,
  163,   -1,  163,   -1,   -1,   -1,   -1,  170,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  193,   -1,  193,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  205,   -1,  205,
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
"conjunto_sentencias : conjunto_sentencias error punto_sincronizacion_sentencia",
"punto_sincronizacion_sentencia : ';'",
"punto_sincronizacion_sentencia : '}'",
"punto_sincronizacion_sentencia : token_inicio_sentencia",
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
"punto_y_coma_obligatorio : error",
"punto_y_coma_obligatorio : ';'",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT error ';'",
"declaracion_variable : UINT lista_variables error ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"asignacion_multiple : lista_variables '=' lista_constantes ';'",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')' punto_y_coma_obligatorio",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"sentencia_ejecutable : invocacion_funcion",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_retorno",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion ';'",
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

//#line 673 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

// ============================================================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ============================================================================================================================================================

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
    //yydebug = true;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = this.getAppropiateToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

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

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

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

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

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

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void readLastTokenAgain() {
    Printer.print("READ AGAIN");
    this.readAgain = true;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void forzarUsoDeNuevoToken() {
    yylex(); // leer un token y avanzar
    yychar = -1; // forzar que el parser use el nuevo token
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void apagarEstadoDeError() {
    yyerrflag = 0;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

public int getWarningsDetected() {
    return this.warningsDetected;
}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------

public int getErrorsDetected() {
    return this.errorsDetected;
}

// ============================================================================================================================================================
// FIN DE CÓDIGO
// ============================================================================================================================================================
//#line 742 "Parser.java"
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
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 28:
//#line 155 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia debe finalizar con punto y coma. Se reanudará después del próximo ';'."); }
break;
case 31:
//#line 169 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 32:
//#line 171 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 34:
//#line 179 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 35:
//#line 190 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 36:
//#line 209 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 38:
//#line 217 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 39:
//#line 222 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 40:
//#line 235 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 42:
//#line 249 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 46:
//#line 269 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 56:
//#line 296 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 301 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 61:
//#line 322 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 62:
//#line 324 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 63:
//#line 326 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición debe ir entre paréntesis."); }
break;
case 64:
//#line 328 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición"); }
break;
case 66:
//#line 339 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 73:
//#line 355 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 74:
//#line 365 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 75:
//#line 375 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe terminar ';'."); }
break;
case 76:
//#line 377 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 77:
//#line 379 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida en el lenguaje."); }
break;
case 80:
//#line 395 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia DO-WHILE."); }
break;
case 81:
//#line 400 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia DO-WHILE debe terminar con ';'."); }
break;
case 82:
//#line 402 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia DO-WHILE inválida."); }
break;
case 84:
//#line 414 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 85:
//#line 416 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 87:
//#line 437 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia PRINT."); }
break;
case 88:
//#line 442 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' debe finalizar con ';'."); }
break;
case 89:
//#line 444 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 90:
//#line 446 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento y debe terminar con ';'."); }
break;
case 95:
//#line 467 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ 
            notifyError("Falta de operando en expresión."); 
            /* $$ no se asigna o se le da un valor por defecto*/
        }
break;
case 96:
//#line 472 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
        }
break;
case 100:
//#line 492 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 101:
//#line 494 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 103:
//#line 502 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 504 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 114:
//#line 537 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = "-" + val_peek(0).sval; }
break;
case 116:
//#line 545 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 117:
//#line 554 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 118:
//#line 559 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 120:
//#line 570 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 122:
//#line 579 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'return' debe terminar con ';'."); }
break;
case 123:
//#line 581 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El retorno no puede estar vacío."); }
break;
case 125:
//#line 592 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 128:
//#line 604 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 132:
//#line 619 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 133:
//#line 621 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 136:
//#line 630 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
            descartarTokenError();
        }
break;
case 137:
//#line 642 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 139:
//#line 653 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 140:
//#line 660 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 141:
//#line 665 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1164 "Parser.java"
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
