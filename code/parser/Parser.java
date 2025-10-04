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






//#line 7 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
    package parser;

    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 18 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    5,    7,    5,    5,    6,    6,
    8,    8,    8,    8,    8,   10,   10,   11,   11,   11,
   13,   13,    1,    1,    1,   14,    2,    2,    2,   15,
   16,   16,   19,   19,   17,   21,   17,   20,   20,    9,
    9,   22,   22,   22,   22,   22,   22,   22,   24,   24,
   25,   25,   31,   31,   31,   31,   31,   32,   33,   33,
   33,   33,   33,   33,   33,   33,   29,   29,   34,   34,
   30,   35,   35,   27,   36,   36,   36,   28,   28,   37,
   37,   38,   38,   39,   39,   18,   18,   18,    4,    4,
    3,    3,   12,   12,   41,   41,   26,   40,   40,   42,
   42,   42,   44,   44,   43,   43,   43,   45,   45,   45,
   23,   46,   46,   47,   47,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    0,    3,    0,    1,    2,
    1,    1,    2,    2,    2,    1,    2,    3,    3,    4,
    0,    1,    1,    3,    2,    3,    1,    3,    2,    7,
    2,    0,    1,    1,    3,    0,    3,    1,    2,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    1,    3,    2,    3,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    5,    4,    0,    2,
    2,    3,    2,    4,    1,    1,    0,    3,    1,    1,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    2,
    1,    3,    8,    7,    1,    0,    4,    1,    0,    1,
    3,    1,    2,    2,    3,    2,    2,    0,    1,    1,
    4,    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    0,    9,   11,   12,
   16,    0,   44,   48,    0,   42,   43,   45,   46,   47,
   51,   52,    6,    0,   14,   13,   15,    0,    0,    0,
    0,    0,   89,    0,    0,   86,   87,   83,   88,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   33,   34,
    0,   71,    0,    0,    0,   25,    0,    0,    0,    0,
   10,   22,   17,   41,   40,    7,    5,    0,    0,  112,
   92,   75,    0,    0,    0,   54,    0,   90,   66,   61,
   63,   62,   64,   65,   59,   60,   80,   81,    0,    0,
    0,   57,   84,   85,    0,   19,    0,  110,  109,    0,
    0,    0,  100,  102,    0,    0,   18,   36,   38,    0,
    0,   73,    0,   31,    0,   24,    0,   27,    0,    0,
    0,    0,  111,   74,   55,   56,   53,    0,    0,    0,
    0,   82,    0,  104,    0,    0,    0,    0,  107,   20,
   37,   35,   39,   72,   97,    0,    0,   29,  114,  113,
   70,   67,    0,    0,  101,  105,    0,   28,    0,    0,
    0,    0,    0,   94,   30,   93,
};
final static short yydgoto[] = {                          4,
   14,  127,   46,   47,    5,  170,   76,   18,   19,   20,
   21,   22,   73,   23,   24,   65,   60,   48,   61,  120,
  151,   25,   49,   27,   28,   29,   30,   50,   31,   32,
   51,   52,   99,  141,   62,   84,  100,   53,  105,  111,
  171,  112,  113,  114,  115,   79,   80,
};
final static short yysindex[] = {                       -96,
    0,  -28,   13,    0,    0,  -40,    9,    8,  -35,    7,
  -12,   18, -208,   -4, -204,    0,   37,    0,    0,    0,
    0,   22,    0,    0,  -55,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,   31, -191,   19,
   31,    9,    0,  -10, -176,    0,    0,    0,    0,  104,
  -12,   61,   21,   27,   52,  -31,   45,   48,    0,    0,
  -38,    0,   31, -152,   65,    0, -150,   -2,   31,   31,
    0,    0,    0,    0,    0,    0,    0,  -22,  -15,    0,
    0,    0,   54,   67, -147,    0,  -25,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   31,   31,
 -158,    0,    0,    0,   31,    0,  -31,    0,    0, -226,
   76,   75,    0,    0, -219,   63,    0,    0,    0,   51,
  -35,    0,   55,    0,   -3,    0,   -8,    0,   54,   54,
 -133,   31,    0,    0,    0,    0,    0,   54,   21,  -12,
 -135,    0,   94,    0,   29, -226,  102, -115,    0,    0,
    0,    0,    0,    0,    0,  111,   -2,    0,    0,    0,
    0,    0,   30,   37,    0,    0,   31,    0,   37,   37,
   42,  113,   44,    0,    0,    0,
};
final static short yyrindex[] = {                       162,
    0,  162,    0,    0,    0,    0,   -5,    0,    0,    0,
    0,    0,  127,    0,    0,    0,  170,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  130,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   84,    0,  115,  -21,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   28,    0,    0,
    0,    0,  131,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -56,    0,    0,    0,    0,    0,  -21,    0,    0, -211,
    0,  134,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -53,    0,  -50,  -45,
    0,    0,    0,    0,    0,    0,    0,  -23,   96,    0,
  -44,    0,    0,    0,    0,  -33,   34,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,    0,    0,    0,    0,   53,   56,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  167,    0,  367,  -34,  177,   68,    0,   16,   43,    0,
    0,    0,    0,    0,    0,    0,   57,  -88,  -19,    0,
    0,    0,  377,    0,    0,    0,    0,    4,    0,    0,
  -37,   46,    0,    0,    0,    0,    0,   80,    0,   77,
   20,    0,  -85,    0,    0,    0,   58,
};
final static int YYTABLESIZE=547;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   21,   44,   69,   75,   44,   26,   45,  103,   50,   45,
  103,   13,  110,   49,   68,  137,  142,   58,   36,   99,
   97,   37,   98,  122,  144,  133,    3,   13,  132,  108,
   86,  101,   71,  128,   45,  157,   45,  147,   23,   67,
   21,   78,   45,   83,  109,  108,   56,   40,   38,   71,
  148,   69,   13,   59,   39,   23,   68,   63,  108,   70,
  165,   64,  104,   45,   13,   81,  123,  103,  115,   17,
   34,  115,  129,  130,   91,   45,   13,   91,  172,  106,
   72,   88,  106,  154,   35,  106,   85,   13,   67,   87,
   13,  107,  158,   59,    3,  155,   97,   97,   98,   98,
  119,  102,  138,  117,  124,  125,  126,  134,  135,  140,
   58,   91,   91,   91,   91,   91,  145,   91,  146,   58,
  161,  150,  168,  159,   79,   21,   79,   79,   79,   91,
   91,   91,   91,  162,  163,   78,   78,   33,   78,   78,
   78,  147,   79,   79,   79,   79,   97,   39,   98,   77,
  167,  164,  169,  175,   78,   78,   78,   78,   23,    1,
    2,    8,  153,   96,   94,   95,  174,   32,  176,    2,
   77,   76,  118,   23,   98,  152,   57,   96,   16,  139,
   95,  156,   59,  143,    0,   71,    0,    0,  173,  160,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   69,
   74,    0,   26,    0,    0,   50,    0,    0,    0,    0,
   49,   68,   69,    0,    0,    6,    7,   41,   42,   43,
   41,   42,   43,  108,  108,    8,    9,    6,    7,   10,
  136,   11,   58,   12,  121,  108,  108,    8,    9,  109,
    0,   10,  131,   11,    7,   12,   42,   43,  108,   43,
   91,   23,   66,    8,    9,   43,   21,   21,   91,   11,
    0,   12,   54,   55,    0,    0,   21,   21,    6,    7,
   21,    0,   21,    0,   21,   42,   43,   82,    8,    9,
    6,    7,   10,    0,   11,    0,   12,   42,   43,    0,
    8,    9,    6,    7,   10,    0,   11,    0,   12,    0,
  116,   66,    8,    9,    7,    0,   10,    7,   11,    0,
   12,    0,    0,    8,    9,    0,    8,    9,    0,   11,
    0,   12,   11,    0,   12,    0,   91,    0,    0,    0,
   91,   91,   91,   91,    0,   91,    0,    0,    0,   79,
    0,    0,    0,   79,   79,   79,   79,    0,   79,    0,
    0,   78,    0,    0,    0,   78,   78,   78,   78,   89,
   78,    0,    0,   90,   91,   92,   93,    0,   15,   15,
   23,   23,   15,    0,    0,    0,    0,   15,   26,   26,
    0,    0,   26,   15,    0,    0,    0,   26,    0,    0,
    0,    0,    0,   26,    0,    0,    0,    0,    0,    0,
   15,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   26,    0,    0,    0,    0,    0,    0,   15,    0,    0,
    0,    0,    0,    0,   15,    0,    0,   26,    0,    0,
    0,    0,    0,    0,   26,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  149,    0,    0,    0,    0,   15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   15,    0,    0,    0,
    0,    0,    0,    0,  166,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   15,    0,    0,    0,    0,   15,   15,    0,    0,    0,
   26,    0,    0,    0,    0,   26,   26,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   59,   40,   59,   45,   41,   59,   45,
   44,   40,   44,   59,   59,   41,  105,   41,   59,   41,
   43,    6,   45,   61,  110,   41,  123,   40,   44,  256,
   41,   51,   17,   68,   45,   44,   45,  257,   44,   44,
   40,   38,   45,   40,  271,  257,   40,   40,   40,   34,
  270,  256,   40,   11,   46,   61,   61,   40,  270,  264,
  146,  270,   42,   45,   40,  257,   63,   47,   41,    2,
    3,   44,   69,   70,   41,   45,   40,   44,  167,   41,
   59,  258,   44,  121,  125,   59,   41,   40,   44,   44,
   40,   40,  127,   51,  123,   41,   43,   43,   45,   45,
   58,   41,   99,   59,  257,   41,  257,   41,  256,  268,
  123,   41,   42,   43,   44,   45,   41,   47,   44,  123,
  140,   59,  157,  257,   41,  125,   43,   44,   45,   59,
   60,   61,   62,  269,   41,  132,   41,  125,   43,   44,
   45,  257,   59,   60,   61,   62,   43,   46,   45,  125,
   40,  123,  123,   41,   59,   60,   61,   62,   44,  256,
  257,    0,  120,   60,   61,   62,  125,   41,  125,    0,
   41,   41,  125,   59,   41,  125,   10,  125,    2,  100,
  125,  125,  140,  107,   -1,  170,   -1,   -1,  169,  132,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  256,   -1,  256,   -1,   -1,  256,   -1,   -1,   -1,   -1,
  256,  256,  269,   -1,   -1,  256,  257,  256,  257,  258,
  256,  257,  258,  257,  256,  266,  267,  256,  257,  270,
  256,  272,  256,  274,  273,  257,  270,  266,  267,  271,
   -1,  270,  265,  272,  257,  274,  257,  258,  270,  258,
  256,  257,  257,  266,  267,  258,  256,  257,  264,  272,
   -1,  274,  256,  257,   -1,   -1,  266,  267,  256,  257,
  270,   -1,  272,   -1,  274,  257,  258,  259,  266,  267,
  256,  257,  270,   -1,  272,   -1,  274,  257,  258,   -1,
  266,  267,  256,  257,  270,   -1,  272,   -1,  274,   -1,
  256,  257,  266,  267,  257,   -1,  270,  257,  272,   -1,
  274,   -1,   -1,  266,  267,   -1,  266,  267,   -1,  272,
   -1,  274,  272,   -1,  274,   -1,  256,   -1,   -1,   -1,
  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,  256,
   -1,   -1,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,  256,   -1,   -1,   -1,  260,  261,  262,  263,  256,
  265,   -1,   -1,  260,  261,  262,  263,   -1,    2,    3,
  256,  257,    6,   -1,   -1,   -1,   -1,   11,    2,    3,
   -1,   -1,    6,   17,   -1,   -1,   -1,   11,   -1,   -1,
   -1,   -1,   -1,   17,   -1,   -1,   -1,   -1,   -1,   -1,
   34,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   34,   -1,   -1,   -1,   -1,   -1,   -1,   51,   -1,   -1,
   -1,   -1,   -1,   -1,   58,   -1,   -1,   51,   -1,   -1,
   -1,   -1,   -1,   -1,   58,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  115,   -1,   -1,   -1,   -1,  120,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  120,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  140,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  148,   -1,  140,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  164,   -1,   -1,   -1,   -1,  169,  170,   -1,   -1,   -1,
  164,   -1,   -1,   -1,   -1,  169,  170,
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
"$$1 :",
"cuerpo_programa : '{' '}' $$1",
"cuerpo_programa :",
"conjunto_sentencias : sentencia",
"conjunto_sentencias : conjunto_sentencias sentencia",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia : error ';'",
"sentencia : error '}'",
"sentencia : error sentencia",
"sentencia_declarativa : declaracion_variable",
"sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
"declaracion_variable : UINT lista_variables ';'",
"declaracion_variable : UINT error ';'",
"declaracion_variable : UINT lista_variables error ';'",
"punto_y_coma_opcional :",
"punto_y_coma_opcional : ';'",
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
"$$2 :",
"bloque_ejecutable : '{' '}' $$2",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"sentencia_ejecutable : operacion_ejecutable ';'",
"sentencia_ejecutable : operacion_ejecutable error",
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
"sentencia_control : while",
"condicion : '(' cuerpo_condicion ')'",
"condicion : '(' ')'",
"condicion : error cuerpo_condicion error",
"condicion : '(' cuerpo_condicion error",
"condicion : cuerpo_condicion ')'",
"cuerpo_condicion : expresion comparador expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"comparador : '='",
"comparador : error",
"if : IF condicion cuerpo_ejecutable rama_else ENDIF",
"if : IF condicion cuerpo_ejecutable rama_else",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"while : DO cuerpo_do",
"cuerpo_do : cuerpo_ejecutable WHILE condicion",
"cuerpo_do : cuerpo_ejecutable condicion",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"imprimible :",
"expresion : expresion operador_suma termino",
"expresion : termino",
"operador_suma : '+'",
"operador_suma : '-'",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : invocacion_funcion",
"constante : CTE",
"constante : '-' CTE",
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

//#line 467 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

/* ---------------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                          */
/* ---------------------------------------------------------------------------------------------------- */

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

private int errorsDetected;
private int warningsDetected;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;
    // Se activa el debug.
    yydebug = true;
}

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
}

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

/**
 * Este método es invocado por el parser generado por Byacc/J cada vez que
 * se encuentra un error de sintaxis que no puede ser manejado por una
 * regla gramatical específica con el token 'error'.
 *
 * @param s El mensaje de error por defecto (generalmente "syntax error").
 */
public void yyerror(String s) {
    // El mensaje 's' que nos pasa Byacc/J es genérico y poco útil.
    // Lo ignoramos y usamos el estado del lexer para dar un mensaje preciso.
    
    // yychar es una variable interna del parser que contiene el token actual (lookahead).
    if (yychar == EOF) {
        notifyError("Error de sintaxis: Se alcanzó el final del archivo inesperadamente. (¿Falta un '}' o un ';'? )");
        return;
    }
    
    // Usamos nuestro método notificador con la información de línea/columna del lexer.
    /*notifyError(
        String.format(
            "Error de sintaxis cerca del token '%s'.", 
            lexer.getCurrentToken().getLexema() // Asumiendo que tu lexer tiene un método para obtener el lexema actual.
        )
    );
    */
}

// El token error no consume automáticamente el token incorrecto.
// Este debe descartarse explícitamente.
void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro token.
    yychar = -1;

    // Se limpia el estado de error.
    yyerrflag = 0;

    Printer.print("Token de error descartado.");
}
// TODO: descartar hasta un punto de sincronizacion. "}" o ";".

void apagarEstadoDeError() {
    yyerrflag = 0;
}

void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message
    ));
}

void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage
    ));
    this.warningsDetected++;
}

void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage
    ));
    this.errorsDetected++;
}

String applySynchronizationFormat(String invalidWord, String synchronizationWord) {
    return """
        Se detectó una palabra inválida: '%s'. \
        Se descartaron todas las palabras inválidas subsiguientes \
        hasta el punto de sincronización: '%s'. \
        """.formatted(invalidWord, synchronizationWord);
}

public int getWarningsDetected() {
    return this.warningsDetected;
}

public int getErrorsDetected() {
    return this.errorsDetected;
}

/* ---------------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                        */
/* ---------------------------------------------------------------------------------------------------- */
//#line 630 "Parser.java"
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
//#line 45 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 3:
//#line 52 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 54 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }. Se sincronizará hasta ID.");
                                    descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
                                }
break;
case 6:
//#line 65 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 7:
//#line 67 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 8:
//#line 69 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee un cuerpo."); }
break;
case 13:
//#line 82 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un ';'.");
                                }
break;
case 14:
//#line 86 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta un '}'.");
                                }
break;
case 15:
//#line 90 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia inválida en el lenguaje. Se sincronizará hasta otra sentencia.");
                                }
break;
case 16:
//#line 100 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 19:
//#line 114 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 20:
//#line 116 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 24:
//#line 135 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 25:
//#line 146 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 26:
//#line 162 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 28:
//#line 167 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 29:
//#line 172 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 30:
//#line 185 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 32:
//#line 194 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 36:
//#line 210 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 37:
//#line 212 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia."); }
break;
case 41:
//#line 224 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 49:
//#line 237 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 50:
//#line 242 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 52:
//#line 248 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia WHILE."); }
break;
case 53:
//#line 253 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Condicion."); }
break;
case 54:
//#line 258 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 55:
//#line 260 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura y cierre de paréntesis en condicion de selección/iteración."); }
break;
case 56:
//#line 262 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condicion de selección/iteración."); }
break;
case 57:
//#line 264 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condicion de selección/iteración."); }
break;
case 65:
//#line 281 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
break;
case 66:
//#line 290 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
                                    descartarTokenError(); 
                                }
break;
case 68:
//#line 298 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 69:
//#line 302 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 70:
//#line 304 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF-ELSE."); }
break;
case 73:
//#line 320 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 75:
//#line 327 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 76:
//#line 329 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 77:
//#line 334 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 90:
//#line 371 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = '-' + val_peek(0).sval; }
break;
case 92:
//#line 377 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 93:
//#line 385 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 94:
//#line 387 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 96:
//#line 395 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 99:
//#line 406 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 102:
//#line 415 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 106:
//#line 426 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 107:
//#line 428 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 110:
//#line 435 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 111:
//#line 446 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Invocación de función."); }
break;
case 115:
//#line 459 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real."); }
break;
//#line 1010 "Parser.java"
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
