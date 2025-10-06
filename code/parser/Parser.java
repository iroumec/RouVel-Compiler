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

    /* Importaciones.*/
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 32 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
public final static short constante=257;
public final static short ID=258;
public final static short CTE=259;
public final static short STR=260;
public final static short EQ=261;
public final static short GEQ=262;
public final static short LEQ=263;
public final static short NEQ=264;
public final static short DASIG=265;
public final static short FLECHA=266;
public final static short PRINT=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short ENDIF=270;
public final static short UINT=271;
public final static short CVR=272;
public final static short DO=273;
public final static short WHILE=274;
public final static short RETURN=275;
public final static short EOF=0;
public final static short UMINUS=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,   10,   10,   10,   10,   11,   11,
   11,   13,   13,   13,   14,   14,   14,   14,   14,   14,
   14,   12,   12,   16,   16,   19,   19,   17,   17,   17,
    4,    4,    4,   20,    5,    5,    5,   21,   22,   22,
   24,   24,   23,   23,   25,   25,   15,   26,   26,   26,
   26,   26,   26,   26,   27,   27,   28,   28,   33,   33,
   33,   34,   34,   35,   35,   35,   35,   35,   35,   35,
   31,   31,   31,   31,   36,   36,   32,   32,   37,   37,
   37,   37,   37,   38,   30,   30,   39,   39,    1,    1,
    1,    1,   40,   40,    2,    2,   41,   41,    3,    3,
    3,    3,    6,    6,   18,   18,   43,   43,   29,   42,
   42,   44,   44,   44,   46,   46,   45,   45,   45,   47,
   47,   47,    7,    8,    8,    9,    9,
};
final static short yylen[] = {                            2,
    3,    3,    1,    1,    3,    2,    0,    3,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    0,    1,    3,    3,    4,
    1,    3,    2,    3,    1,    3,    2,    7,    2,    0,
    1,    1,    3,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    1,    1,    3,    2,
    2,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    5,    5,    4,    2,    0,    2,    2,    2,    2,    1,
    2,    3,    2,    2,    4,    3,    1,    1,    3,    1,
    3,    2,    1,    1,    3,    1,    1,    1,    1,    1,
    2,    1,    1,    3,    8,    7,    1,    0,    4,    1,
    0,    1,    3,    1,    2,    2,    3,    2,    2,    0,
    1,    1,    4,    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,    0,    0,    9,   22,   23,
   24,    0,   50,   54,    0,   49,   51,   52,   53,   57,
   58,    0,    6,    0,    0,    0,    0,    0,    0,  100,
    0,    0,    0,    0,   96,   99,  102,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   41,   42,    0,   77,
   80,    0,    0,    0,   33,    0,    0,    0,    0,    1,
    0,    2,   10,   27,   25,   47,    8,    5,    0,    0,
  124,  104,   87,   86,    0,    0,    0,  101,   61,    0,
   66,   68,   67,   69,   93,    0,   70,   64,   65,    0,
    0,    0,   98,   97,    0,    0,   60,   29,    0,  122,
  121,    0,    0,    0,  112,  114,    0,    0,   28,   83,
   84,   44,   45,    0,    0,   81,   79,    0,   39,    0,
   32,   35,    0,    0,    0,   15,   18,   16,   17,   19,
   21,   20,   13,   12,   11,   14,    0,    0,  123,   85,
    0,    0,   59,    0,   91,    0,   95,    0,    0,  116,
    0,    0,    0,    0,  119,   30,   43,   46,   82,  109,
    0,   37,    0,  126,  125,   76,   72,   71,    0,    0,
  113,  117,    0,   36,    0,    0,    0,    0,    0,  106,
   38,  105,
};
final static short yydgoto[] = {                          4,
   43,   44,   45,   13,  133,   46,   47,   80,   81,    5,
  186,   18,  145,  146,   19,   20,   21,   22,   75,   23,
   24,   64,   58,   59,  124,   25,   26,   27,   28,   29,
   30,   31,  121,   49,  101,  152,   60,   61,   86,  102,
  105,  113,  187,  114,  115,  116,  117,
};
final static short yysindex[] = {                       -83,
    0,   26,  -40,    0,    0,   10,  -16,   19,    5,  -26,
   -9, -235,   23, -239,    0,   46,   15,    0,    0,    0,
    0,  -12,    0,    0,   -8,    0,    0,    0,    0,    0,
    0,  -71,    0,  -13,  134, -198,  129,  -29,   10,    0,
 -196,    8,  116,  -14,    0,    0,    0,  -29,   29,   16,
   41,  -32,  127, -200,  -36,   12,    0,    0,  -38,    0,
    0,  134, -175,   45,    0, -163, -161,  134,  134,    0,
  140,    0,    0,    0,    0,    0,    0,    0,   62,   17,
    0,    0,    0,    0,   44,   57, -170,    0,    0,   59,
    0,    0,    0,    0,    0, -196,    0,    0,    0,  -14,
  134,   58,    0,    0,  134, -170,    0,    0,  -32,    0,
    0, -240,   60,   64,    0,    0, -233,   43,    0,    0,
    0,    0,    0,   37,   33,    0,    0,   -6,    0,  -19,
    0,    0,  -23,   44,   44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -152,  134,    0,    0,
  -29, -160,    0,   44,    0,  -14,    0, -159,   68,    0,
   -5, -240,   74, -136,    0,    0,    0,    0,    0,    0,
   83,    0, -133,    0,    0,    0,    0,    0,    7,   51,
    0,    0,  134,    0,   51,   40,   22,   94,   28,    0,
    0,    0,
};
final static short yyrindex[] = {                       141,
    0,  141,    0,    0,    0,  -39,    0,    0,    0,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -12,   72,    0,
    0,    0,  119,   84,    0,    0,    0,    0,    0,    0,
  128,   -7,    0,  104,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   21,    0,
    0,    0,    0,    0,  123,    0, -105,    0,    0,    0,
    0,    0,    0,    0,    0,  138,    0,    0,    0,   95,
    0,    0,    0,    0,    0,  -56,    0,    0,   -7,    0,
    0, -229,    0,  139,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  122,  125,  130,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  147,    0,  107,    0,  131,    0,    0,
    0,  -21,   27,   35,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   66,
    0,    0,    0,    0,   66,   67,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  360,  297,  -95,  184,    0,  399,  402,    0,   47,  192,
   90,   -4,    0,    0,   34,    0,    0,    0,    0,    0,
    0,    0,   70,  -30,    0,    0,    0,    0,    0,    0,
    0,    0,   -2,  154,    0,   91,    0,  -11,    0,    0,
    0,   89,   18,    0,  -93,    0,    0,
};
final static int YYTABLESIZE=588;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   26,   42,   75,   42,   31,   48,   41,   87,   41,  157,
   12,  112,   73,   12,   72,  110,   68,  106,  160,  115,
  173,   31,  115,   37,  163,   69,   12,  103,  120,   73,
   62,  111,  104,  111,  170,   63,   95,  164,   96,    3,
   26,  120,  120,   57,   52,   70,   74,  127,   89,   35,
   76,   12,   41,   77,   12,   36,  126,  149,   42,   82,
  148,  127,   88,   41,  127,   12,   66,  103,  181,  107,
  103,   57,   42,   55,  108,  118,   12,   41,  118,   12,
  109,   57,  129,   67,   33,  130,   95,  188,   96,  123,
   12,   17,   34,   56,  131,  132,   56,  150,  151,  153,
  161,  166,   41,   56,   95,  174,   96,  162,  179,  177,
  178,   78,  103,  103,  103,  103,  103,  180,  103,   36,
  176,  163,  183,  184,   90,   26,   90,   90,   90,  185,
  103,  103,  103,  103,  191,   92,  122,   92,   92,   92,
    7,   40,   90,   90,   90,   90,  190,   89,    3,   89,
   89,   89,  192,   92,   92,   92,   92,  168,   95,   63,
   96,  167,   78,   88,   75,   89,   89,   89,   89,   84,
   66,   31,    1,   41,    2,   99,   97,   98,   41,  110,
   34,   73,   94,   56,   57,  119,   31,   62,   55,   73,
  108,  107,   53,   16,  175,   90,  158,  159,  144,  171,
    0,    0,  189,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   75,    0,   32,  103,    6,   31,   39,
   40,   39,   40,  110,    0,  103,    7,    8,    6,   54,
    9,    6,   10,  172,   11,  125,  120,    7,    8,  111,
    7,    8,   71,   10,    6,   11,   10,   55,   11,  120,
  120,   39,   40,    7,    8,    0,   26,    9,   26,   10,
   50,   11,   51,  120,  143,   39,   40,   26,   26,    6,
   71,   26,    6,   26,   38,   26,   39,   40,    7,    8,
   65,    7,    8,    6,   10,    9,   11,   10,  169,   11,
   39,   40,    7,    8,    6,   71,    9,    6,   10,    0,
   11,   39,   40,    7,    8,    0,    7,    8,    6,   10,
    9,   11,   10,  155,   11,   39,   40,    7,    8,   39,
   40,    9,    0,   10,    0,   11,    0,  147,    0,  103,
  103,    0,  103,  103,  103,  103,    0,  103,    0,  100,
    0,   90,   90,    0,   90,   90,   90,   90,    0,   90,
    0,    0,   92,   92,    0,   92,   92,   92,   92,    0,
   92,    0,    0,    0,   89,   89,    0,   89,   89,   89,
   89,    0,   89,   39,   40,  100,   91,   92,   93,   94,
    0,  100,  118,   31,   65,   31,   39,   40,   83,    0,
    0,   39,   40,   94,   79,   94,   85,  136,  156,    0,
   14,   14,    0,   15,   15,    0,  137,  138,   14,    0,
  139,   15,  140,  141,  142,   14,    0,    0,   15,    0,
    0,  128,    0,    0,  100,    0,    0,  134,  135,    0,
  100,  100,   14,    0,    0,   15,   14,    0,    0,   15,
    0,    0,    0,    0,    0,    0,   14,    0,    0,   15,
  100,    0,    0,    0,   14,    0,    0,   15,    0,    0,
  154,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   79,    0,    0,
    0,    0,    0,    0,    0,  165,    0,    0,    0,    0,
    0,    0,   14,    0,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   14,
    0,    0,   15,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  182,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
    0,   15,    0,   14,   14,    0,   15,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   40,   44,    8,   45,   38,   45,  105,
   40,   44,   17,   40,    0,  256,  256,   48,  112,   41,
   44,   61,   44,   40,  258,  265,   40,   42,  258,   34,
   40,  272,   47,   41,   41,  271,   43,  271,   45,  123,
   40,  271,   54,   10,   40,    0,   59,   59,   41,   40,
   59,   40,   45,  125,   40,   46,   59,   41,   40,  258,
   44,   41,  259,   45,   44,   40,   44,   41,  162,   41,
   44,   38,   40,  274,   59,   41,   40,   45,   44,   40,
   40,   48,  258,   61,  125,   41,   43,  183,   45,   56,
   40,    2,    3,  123,  258,  257,  123,   41,  269,   41,
   41,   59,   45,  123,   43,  258,   45,   44,   41,  270,
  270,  125,   41,   42,   43,   44,   45,  123,   47,   46,
  151,  258,   40,  257,   41,  125,   43,   44,   45,  123,
   59,   60,   61,   62,   41,   41,  125,   43,   44,   45,
    0,   41,   59,   60,   61,   62,  125,   41,  123,   43,
   44,   45,  125,   59,   60,   61,   62,  124,   43,   41,
   45,  125,   59,   41,  270,   59,   60,   61,   62,   41,
   44,   44,  256,   45,  258,   60,   61,   62,   45,   41,
   59,  186,   45,   59,  151,   59,   59,   41,   59,   59,
  125,  125,    9,    2,  148,   42,  106,  109,   59,  130,
   -1,   -1,  185,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  270,   -1,  256,  256,  258,  258,  258,
  259,  258,  259,  256,   -1,  265,  267,  268,  258,  256,
  271,  258,  273,  257,  275,  274,  258,  267,  268,  272,
  267,  268,  256,  273,  258,  275,  273,  274,  275,  271,
  258,  258,  259,  267,  268,   -1,  256,  271,  258,  273,
  256,  275,  258,  271,  125,  258,  259,  267,  268,  258,
  256,  271,  258,  273,  256,  275,  258,  259,  267,  268,
  258,  267,  268,  258,  273,  271,  275,  273,  256,  275,
  258,  259,  267,  268,  258,  256,  271,  258,  273,   -1,
  275,  258,  259,  267,  268,   -1,  267,  268,  258,  273,
  271,  275,  273,  256,  275,  258,  259,  267,  268,  258,
  259,  271,   -1,  273,   -1,  275,   -1,  266,   -1,  258,
  259,   -1,  261,  262,  263,  264,   -1,  266,   -1,   43,
   -1,  258,  259,   -1,  261,  262,  263,  264,   -1,  266,
   -1,   -1,  258,  259,   -1,  261,  262,  263,  264,   -1,
  266,   -1,   -1,   -1,  258,  259,   -1,  261,  262,  263,
  264,   -1,  266,  258,  259,   79,  261,  262,  263,  264,
   -1,   85,  256,  256,  258,  258,  258,  259,  260,   -1,
   -1,  258,  259,  256,   35,  258,   37,  258,  102,   -1,
    2,    3,   -1,    2,    3,   -1,  267,  268,   10,   -1,
  271,   10,  273,  274,  275,   17,   -1,   -1,   17,   -1,
   -1,   62,   -1,   -1,  128,   -1,   -1,   68,   69,   -1,
  134,  135,   34,   -1,   -1,   34,   38,   -1,   -1,   38,
   -1,   -1,   -1,   -1,   -1,   -1,   48,   -1,   -1,   48,
  154,   -1,   -1,   -1,   56,   -1,   -1,   56,   -1,   -1,
  101,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  148,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  117,   -1,   -1,   -1,   -1,
   -1,   -1,  124,   -1,   -1,  124,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  151,
   -1,   -1,  151,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  164,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  180,   -1,
   -1,  180,   -1,  185,  186,   -1,  185,  186,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=276;
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
null,null,null,null,null,null,null,"constante","ID","CTE","STR","EQ","GEQ",
"LEQ","NEQ","DASIG","FLECHA","PRINT","IF","ELSE","ENDIF","UINT","CVR","DO",
"WHILE","RETURN","UMINUS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID cuerpo_programa EOF",
"programa : ID conjunto_sentencias EOF",
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
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT error ';'",
"declaracion_variable : UINT lista_variables error ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID",
"asignacion_multiple : lista_variables '=' lista_constantes",
"lista_constantes : constante",
"lista_constantes : lista_constantes ',' constante",
"lista_constantes : lista_constantes constante",
"lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'",
"parametro_lambda : UINT ID",
"parametro_lambda :",
"cuerpo_ejecutable : sentencia_ejecutable",
"cuerpo_ejecutable : bloque_ejecutable",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"sentencia_ejecutable : operacion_ejecutable ';'",
"operacion_ejecutable : invocacion_funcion",
"operacion_ejecutable : asignacion_simple",
"operacion_ejecutable : asignacion_multiple",
"operacion_ejecutable : sentencia_control",
"operacion_ejecutable : sentencia_retorno",
"operacion_ejecutable : impresion",
"operacion_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion",
"asignacion_simple : variable error expresion",
"sentencia_control : if",
"sentencia_control : do_while",
"condicion : '(' cuerpo_condicion ')'",
"condicion : cuerpo_condicion ')'",
"condicion : '(' ')'",
"cuerpo_condicion : expresion comparador expresion",
"cuerpo_condicion : expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"if : IF condicion cuerpo_ejecutable rama_else ENDIF",
"if : IF error cuerpo_ejecutable rama_else ENDIF",
"if : IF condicion cuerpo_ejecutable rama_else",
"if : IF error",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"do_while : DO cuerpo_do",
"do_while : DO error",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do : fin_cuerpo_do",
"cuerpo_do : cuerpo_ejecutable condicion",
"cuerpo_do : cuerpo_ejecutable WHILE error",
"cuerpo_do : error fin_cuerpo_do",
"fin_cuerpo_do : WHILE condicion",
"impresion : PRINT '(' imprimible ')'",
"impresion : PRINT '(' ')'",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : expresion operador_suma error",
"expresion : expresion termino",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : CTE",
"factor : '-' CTE",
"factor : invocacion_funcion",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
"declaracion_funcion : UINT '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
"cuerpo_funcion : conjunto_sentencias",
"cuerpo_funcion :",
"sentencia_retorno : RETURN '(' expresion ')'",
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

//#line 657 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
    yydebug = true;
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
public void yyerror(String s) {

    Printer.print("Ocurre un error para el cual la gramática no estaba preparada.");
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
//#line 702 "Parser.java"
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
//#line 83 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 88 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 90 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 92 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
            descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
        }
break;
case 6:
//#line 106 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 7:
//#line 108 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ningún cuerpo."); }
break;
case 8:
//#line 110 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 11:
//#line 122 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 24:
//#line 159 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 29:
//#line 180 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 30:
//#line 182 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 32:
//#line 190 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 33:
//#line 201 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 34:
//#line 220 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 36:
//#line 228 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 37:
//#line 233 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                val_peek(1).sval, val_peek(0).sval));
        }
break;
case 38:
//#line 247 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 40:
//#line 259 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 44:
//#line 279 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 55:
//#line 314 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 56:
//#line 319 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 60:
//#line 346 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 61:
//#line 348 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 63:
//#line 359 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 70:
//#line 375 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError( "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?" );}
break;
case 71:
//#line 385 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 72:
//#line 390 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida en el lenguaje. Falta cierre de paréntesis en condición."); }
break;
case 73:
//#line 395 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 74:
//#line 397 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida en el lenguaje."); }
break;
case 78:
//#line 417 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia DO-WHILE inválida."); }
break;
case 79:
//#line 425 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia DO-WHILE."); }
break;
case 80:
//#line 430 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 81:
//#line 432 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 82:
//#line 437 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 83:
//#line 439 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Ha habido un error en el cierre del cuerpo ejecutable."); }
break;
case 86:
//#line 453 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 87:
//#line 460 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 88:
//#line 462 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 91:
//#line 479 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ 
            notifyError("Falta de operando en expresión."); 
            /* $$ no se asigna o se le da un valor por defecto*/
        }
break;
case 92:
//#line 486 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError(String.format(
                "Falta de operador entre operandos %s y %s.",
                val_peek(1).sval, val_peek(0).sval)
            );
           /*  $$ = $2; // Se podría optar por continuar con el último término*/
        }
break;
case 96:
//#line 507 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 99:
//#line 523 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 100:
//#line 525 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 101:
//#line 527 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = '-' + val_peek(0).sval; }
break;
case 102:
//#line 529 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 537 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 105:
//#line 546 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 106:
//#line 548 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 108:
//#line 559 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 111:
//#line 576 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 114:
//#line 588 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 118:
//#line 603 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 119:
//#line 605 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 122:
//#line 614 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
            descartarTokenError();
        }
break;
case 123:
//#line 626 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
            notifyDetection("Invocación de función."); 
            yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
break;
case 125:
//#line 637 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 126:
//#line 644 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 127:
//#line 649 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1109 "Parser.java"
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
