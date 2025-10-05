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






//#line 7 "gramatica.y"
    package parser;

    /* Importaciones.*/
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 19 "gramatica.y"
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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,   12,   12,   12,   12,   13,   13,
   13,   15,   15,   15,   16,   16,   16,   16,   16,   16,
   16,   14,   14,   18,   18,   19,   19,   19,   21,   21,
    1,    1,    1,   22,    2,    2,    2,   23,   24,   24,
   26,   26,   25,   25,   27,   27,   27,   17,   28,   28,
   28,   28,   28,   28,   28,   29,   29,   30,   30,   35,
   35,   35,   37,   37,   36,   36,   38,   38,   38,   38,
   38,   38,   38,   33,   33,   39,   39,   34,   40,   40,
   40,   40,   41,   32,   42,   42,   42,    5,    5,    5,
    5,   43,   43,   11,   11,    6,    6,    6,   44,   44,
    7,    7,    7,    4,    3,    3,   20,   20,   46,   46,
   31,   45,   45,   47,   47,   47,   49,   49,   48,   48,
   48,   50,   50,   50,    8,    9,    9,   10,   10,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    2,    0,    3,    1,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    3,    3,    4,    0,    1,
    1,    3,    2,    3,    1,    3,    2,    7,    2,    0,
    1,    1,    3,    2,    1,    2,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    1,    1,    3,
    2,    2,    1,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    5,    4,    0,    2,    2,    2,    1,
    2,    3,    2,    4,    1,    1,    0,    3,    1,    1,
    2,    1,    1,    2,    2,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    8,    7,    1,    0,
    4,    1,    0,    1,    3,    1,    2,    2,    3,    2,
    2,    0,    1,    1,    4,    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   49,    1,    0,    9,   22,
   23,   24,    0,   51,   55,    0,   50,   52,   53,   54,
   58,   59,    0,    6,    0,   15,   18,   16,   17,   19,
   21,   20,   13,   12,   11,   14,    0,    0,    0,    0,
  104,    0,  101,  102,    0,    0,   97,  103,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,   42,    0,
   78,   80,    0,    0,    0,   33,    0,    0,    0,    0,
   10,   30,   25,   48,    0,    5,    0,    0,  126,  106,
   85,    0,    0,   62,    0,   69,   71,   70,   72,   73,
   67,   68,   92,   93,    0,    0,   99,  100,    0,    0,
    0,    0,   61,   27,    0,  124,  123,    0,    0,    0,
  114,  116,    0,    0,   26,   83,    0,   44,   45,    0,
    0,   81,   79,    0,   39,    0,   32,    0,   35,    0,
    0,    0,    0,  125,   84,   64,   63,   60,    0,    0,
   98,   96,    0,    0,    0,  118,    0,    0,    0,    0,
  121,   28,   47,   43,   46,   82,  111,    0,    0,   37,
  128,  127,   77,   74,    0,    0,  115,  119,    0,   36,
    0,    0,    0,    0,    0,  108,   38,  107,
};
final static short yydgoto[] = {                          4,
   14,  138,   53,   54,   55,   56,   57,   58,   88,   89,
   59,    5,  182,   19,   45,   46,   20,   21,   22,   23,
   83,   24,   25,   75,   69,   70,  130,   26,   27,   28,
   29,   30,   31,   32,  126,   61,  148,  105,  154,   71,
   72,   93,  106,  110,  119,  183,  120,  121,  122,  123,
};
final static short yysindex[] = {                       -64,
    0,  -40,  -21,    0,    0,  193,   -2,  -14,   -3,  -33,
  -10,   13, -234,   30, -231,    0,    0,   58,    0,    0,
    0,    0,  -12,    0,    0,   19,    0,    0,    0,    0,
    0,    0,  215,    0,   23,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -229, -215, -163,   -2,
    0,    2,    0,    0,  238,    8,    0,    0, -229,  -24,
   15,   33,   46,  -27,   49,   -3,   35,    0,    0,  -36,
    0,    0, -229, -173,   48,    0, -160, -153, -229, -229,
    0,    0,    0,    0,    0,    0,  -37,    4,    0,    0,
    0,   17,   65,    0,    5,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -229, -229,    0,    0,    7, -156,
    7, -161,    0,    0,  -27,    0,    0, -232,   73,   71,
    0,    0, -235,   57,    0,    0,   72,    0,    0,   69,
  -17,    0,    0,   36,    0,   -6,    0,   27,    0,   17,
   17, -139, -229,    0,    0,    0,    0,    0,   17,    7,
    0,    0,  -24, -150,   79,    0,   -1, -232,   75, -134,
    0,    0,    0,    0,    0,    0,    0,   84, -153,    0,
    0,    0,    0,    0,   18,   47,    0,    0, -229,    0,
   47,   58,   14,   86,   24,    0,    0,    0,
};
final static short yyrindex[] = {                       129,
    0,  129,    0,    0,    0,    0,  159,    0,    0,    0,
    0,    0,   89,    0,    0,    0,    0,  131,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   99,   91,
    0,    0,    0,    0,   16,  124,    0,    0,  136,    0,
    0,    0,   66,  -32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   12,    0,   25,    0,    0,    0,
    0,  101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  146,    0,    0,  102,    0,
  113,  -56,    0,    0,  -32,    0,    0, -230,    0,  103,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   96,    0,  100,
  107,    0,    0,    0,    0,    0,    0,    0,   20,  369,
    0,    0,    0,  111,    0,    0,    0,  -39,   26,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   51,    0,    0,    0,    0,
   51,   63,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  161,    0,  416,  -58,  -15,  -38,  -97,  435,    0,   39,
    0,  197,   70,   -4,    0,    0,  366,    0,    0,    0,
    0,    0,    0,    0,   64,  -50,    0,    0,    0,    0,
    0,    0,    0,    0,    6,  149,    0,    0,    0,    0,
  132,    0,    0,    0,   94,   29,    0, -107,    0,    0,
};
final static int YYTABLESIZE=634;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   29,  117,   76,   52,  117,  103,   64,  104,  113,  112,
  156,    8,  152,   81,   60,   13,  118,  109,   13,  139,
  111,  159,   52,  116,   79,   49,  122,   50,   51,   13,
   81,   87,   80,   92,  160,   74,   52,   47,  117,  122,
   29,   90,   94,   48,  144,  147,   82,  143,  108,  108,
  177,   13,   73,  107,  107,  113,   66,  134,    3,  103,
   65,  104,   13,  140,  141,  129,  105,  150,  129,  105,
  169,   18,   35,   77,   13,  132,  167,   84,  103,  170,
  104,  184,    3,  135,  120,  115,   13,  120,  136,  149,
   78,  114,   77,   50,   51,   91,  137,   13,   67,  151,
   50,   51,  173,   34,   51,  145,  153,  125,   13,   31,
  180,   13,   67,  157,  158,  162,   67,  171,  174,  175,
   48,  176,  159,  179,   31,   29,  187,   87,    7,   40,
    2,  105,  105,  105,  105,  105,   13,  105,  186,   87,
  181,   86,   94,  112,   94,   94,   94,   86,  188,  105,
  105,  105,  105,   95,   34,   95,   95,   95,   57,  128,
   94,   94,   94,   94,   89,   56,   89,   89,   89,   75,
   65,   95,   95,   95,   95,  110,   90,   81,   90,   90,
   90,  172,   89,   89,   89,   89,   91,  109,   91,   91,
   91,    1,    2,  164,   90,   90,   90,   90,   17,  168,
   95,  133,   31,    0,   91,   91,   91,   91,  155,  185,
    0,    0,   76,    0,    0,    6,    7,  122,    0,   31,
   50,   51,   62,   63,  122,    8,    9,  142,  116,   10,
  122,   11,    7,   12,   33,    7,  131,  122,  166,   50,
   51,    8,    9,  117,    8,    9,    7,   11,   10,   12,
   11,   44,   12,   50,   51,    8,    9,   29,   50,   51,
  146,   11,   66,   12,   50,   51,   29,   29,   13,    0,
   29,   66,   29,   44,   29,   65,    0,   13,   13,    7,
  103,   13,  104,   13,   51,   13,   76,    0,    8,    9,
  127,    7,   10,    0,   11,    0,   12,  102,  100,  101,
    8,    9,    6,    7,  124,   76,   11,    0,   12,    0,
    0,    0,    8,    9,    7,    0,   10,   43,   11,    0,
   12,   31,   31,    8,    9,    7,    0,   10,    7,   11,
    0,   12,    0,    0,    8,    9,    0,    8,    9,   85,
   11,    0,   12,   11,    0,   12,  105,  105,  105,    0,
  105,  105,  105,  105,    0,  105,    0,   94,   94,   94,
    0,   94,   94,   94,   94,    0,   94,    0,   95,   95,
   95,    0,   95,   95,   95,   95,   68,   95,    0,   89,
    0,    0,    0,   89,   89,   89,   89,    0,   89,    0,
    0,   90,    0,    0,    0,   90,   90,   90,   90,    0,
   90,   91,    0,    0,    0,   91,   91,   91,   91,   88,
   91,   88,   88,   88,  105,   31,    0,   15,   15,    0,
    0,    0,  105,    0,    0,   68,   15,   88,   88,   88,
   88,    0,  129,   15,    0,    0,   16,   16,    0,    0,
    0,    0,    0,    0,    0,   16,    0,    0,    0,   36,
   15,    0,   16,    0,    0,    0,    0,    0,   37,   38,
    0,    0,   39,    0,   40,   41,   42,    0,    0,   16,
    0,   36,    0,    0,    0,   15,    0,    0,    0,    0,
   37,   38,   15,    0,   39,    0,   40,   41,   42,    0,
    0,    0,  163,    0,   16,  165,    0,   96,   97,   98,
   99,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  161,    0,
    0,    0,   15,    0,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   16,    0,    0,   16,    0,    0,    0,   15,    0,
    0,    0,    0,    0,    0,  178,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   16,    0,    0,
    0,   15,    0,    0,    0,    0,   15,   15,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   16,    0,    0,    0,    0,   16,   16,    0,    0,    0,
    0,    0,    0,    0,   88,    0,    0,    0,   88,   88,
   88,   88,    0,   88,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   59,   40,   44,   43,   40,   45,   41,   60,
  118,    0,  110,   18,    9,   40,   44,   56,   40,   78,
   59,  257,   40,  256,  256,   40,  257,  257,  258,   40,
   35,   47,  264,   49,  270,  270,   40,   40,  271,  270,
   40,  257,   41,   46,   41,   41,   59,   44,   42,   42,
  158,   40,   40,   47,   47,   41,   41,   73,  123,   43,
   41,   45,   40,   79,   80,   41,   41,  106,   44,   44,
   44,    2,    3,   44,   40,   70,   41,   59,   43,  138,
   45,  179,  123,  257,   41,   40,   40,   44,   41,  105,
   61,   59,   44,  257,  258,  259,  257,   40,  123,  256,
  257,  258,  153,  125,  258,   41,  268,   59,   40,   44,
  169,   40,  123,   41,   44,   59,  123,  257,  269,   41,
   46,  123,  257,   40,   59,  125,   41,  143,    0,   41,
    0,   41,   42,   43,   44,   45,  125,   47,  125,   41,
  123,   41,   41,   41,   43,   44,   45,  125,  125,   59,
   60,   61,   62,   41,   59,   43,   44,   45,   59,  125,
   59,   60,   61,   62,   41,   59,   43,   44,   45,   59,
   10,   59,   60,   61,   62,  125,   41,  182,   43,   44,
   45,  143,   59,   60,   61,   62,   41,  125,   43,   44,
   45,  256,  257,  125,   59,   60,   61,   62,    2,  136,
   52,   70,   44,   -1,   59,   60,   61,   62,  115,  181,
   -1,   -1,  269,   -1,   -1,  256,  257,  257,   -1,   61,
  257,  258,  256,  257,  257,  266,  267,  265,  256,  270,
  270,  272,  257,  274,  256,  257,  273,  270,  256,  257,
  258,  266,  267,  271,  266,  267,  257,  272,  270,  274,
  272,   59,  274,  257,  258,  266,  267,  257,  257,  258,
  256,  272,  273,  274,  257,  258,  266,  267,  257,   -1,
  270,  256,  272,   59,  274,  256,   -1,  266,  267,  257,
   43,  270,   45,  272,  258,  274,  257,   -1,  266,  267,
  256,  257,  270,   -1,  272,   -1,  274,   60,   61,   62,
  266,  267,  256,  257,  256,  257,  272,   -1,  274,   -1,
   -1,   -1,  266,  267,  257,   -1,  270,  125,  272,   -1,
  274,  256,  257,  266,  267,  257,   -1,  270,  257,  272,
   -1,  274,   -1,   -1,  266,  267,   -1,  266,  267,  125,
  272,   -1,  274,  272,   -1,  274,  256,  257,  258,   -1,
  260,  261,  262,  263,   -1,  265,   -1,  256,  257,  258,
   -1,  260,  261,  262,  263,   -1,  265,   -1,  256,  257,
  258,   -1,  260,  261,  262,  263,   11,  265,   -1,  256,
   -1,   -1,   -1,  260,  261,  262,  263,   -1,  265,   -1,
   -1,  256,   -1,   -1,   -1,  260,  261,  262,  263,   -1,
  265,  256,   -1,   -1,   -1,  260,  261,  262,  263,   41,
  265,   43,   44,   45,  256,  257,   -1,    2,    3,   -1,
   -1,   -1,  264,   -1,   -1,   60,   11,   59,   60,   61,
   62,   -1,   67,   18,   -1,   -1,    2,    3,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   11,   -1,   -1,   -1,  257,
   35,   -1,   18,   -1,   -1,   -1,   -1,   -1,  266,  267,
   -1,   -1,  270,   -1,  272,  273,  274,   -1,   -1,   35,
   -1,  257,   -1,   -1,   -1,   60,   -1,   -1,   -1,   -1,
  266,  267,   67,   -1,  270,   -1,  272,  273,  274,   -1,
   -1,   -1,  127,   -1,   60,  130,   -1,  260,  261,  262,
  263,   67,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  153,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,   -1,
   -1,   -1,  127,   -1,   -1,  130,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  127,   -1,   -1,  130,   -1,   -1,   -1,  153,   -1,
   -1,   -1,   -1,   -1,   -1,  160,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  153,   -1,   -1,
   -1,  176,   -1,   -1,   -1,   -1,  181,  182,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  176,   -1,   -1,   -1,   -1,  181,  182,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,  260,  261,
  262,  263,   -1,  265,
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
"bloque_ejecutable : '{' '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
"conjunto_sentencias_ejecutables : error sentencia_ejecutable",
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
"condicion : '(' cuerpo_condicion fin_condicion",
"condicion : cuerpo_condicion ')'",
"condicion : '(' ')'",
"fin_condicion : ')'",
"fin_condicion : error",
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
"if : IF condicion cuerpo_ejecutable rama_else",
"rama_else :",
"rama_else : ELSE cuerpo_ejecutable",
"do_while : DO cuerpo_do",
"cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
"cuerpo_do : fin_cuerpo_do",
"cuerpo_do : cuerpo_ejecutable condicion",
"cuerpo_do : cuerpo_ejecutable WHILE error",
"fin_cuerpo_do : WHILE condicion",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"imprimible :",
"expresion : expresion operador_suma termino",
"expresion : termino",
"expresion : secuencia_sin_operador",
"expresion : expresion operador_suma",
"operador_suma : '+'",
"operador_suma : '-'",
"secuencia_sin_operador : termino termino",
"secuencia_sin_operador : secuencia_sin_operador termino",
"termino : termino operador_multiplicacion factor",
"termino : factor",
"termino : termino operador_multiplicacion error",
"operador_multiplicacion : '/'",
"operador_multiplicacion : '*'",
"factor : variable",
"factor : constante",
"factor : invocacion_funcion",
"constante : CTE",
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

//#line 673 "gramatica.y"

// ============================================================================================================================================================
// INICIO DE CÓDIGO (opcional)
// ============================================================================================================================================================

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

// Contadores de la cantidad de errores detectados.
private int errorsDetected;
private int warningsDetected;

private boolean readAgain;

/**
* Constructor que recibe un Lexer.
*/
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
//#line 728 "Parser.java"
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
//#line 97 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 102 "gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 104 "gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 106 "gramatica.y"
{
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
                                    descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
                                }
break;
case 6:
//#line 119 "gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 8:
//#line 122 "gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 11:
//#line 133 "gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 13:
//#line 140 "gramatica.y"
{ notifyDetection("Just testing"); readLastTokenAgain(); }
break;
case 14:
//#line 142 "gramatica.y"
{ notifyDetection("Just testing"); readLastTokenAgain(); }
break;
case 24:
//#line 168 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 27:
//#line 178 "gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 28:
//#line 180 "gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 32:
//#line 195 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 33:
//#line 206 "gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 34:
//#line 224 "gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 36:
//#line 231 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 37:
//#line 236 "gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 38:
//#line 249 "gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 40:
//#line 258 "gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 44:
//#line 276 "gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 47:
//#line 290 "gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 56:
//#line 313 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 318 "gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 59:
//#line 325 "gramatica.y"
{ notifyDetection("Sentencia WHILE."); }
break;
case 61:
//#line 335 "gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 62:
//#line 337 "gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 64:
//#line 359 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 66:
//#line 371 "gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 73:
//#line 386 "gramatica.y"
{
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
break;
case 75:
//#line 400 "gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 76:
//#line 415 "gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 77:
//#line 417 "gramatica.y"
{ notifyDetection("Sentencia IF-ELSE."); }
break;
case 80:
//#line 442 "gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 81:
//#line 444 "gramatica.y"
{ notifyError("Falta 'while' 2."); }
break;
case 82:
//#line 449 "gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 85:
//#line 465 "gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 86:
//#line 467 "gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 87:
//#line 472 "gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 90:
//#line 485 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 91:
//#line 487 "gramatica.y"
{
                                    notifyError("Falta de operando en expresión.");
                                    yyval.sval = val_peek(1).sval;    
                                }
break;
case 94:
//#line 506 "gramatica.y"
{
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        val_peek(1).sval, val_peek(0).sval)
                                    );
                                    yyval.sval = val_peek(0).sval;
                                }
break;
case 95:
//#line 514 "gramatica.y"
{
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        val_peek(1).sval, val_peek(0).sval)
                                    );
                                    yyval.sval = val_peek(0).sval;
                                }
break;
case 97:
//#line 527 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 98:
//#line 529 "gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 101:
//#line 541 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 102:
//#line 543 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 103:
//#line 545 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 551 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 106:
//#line 561 "gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 107:
//#line 569 "gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 108:
//#line 571 "gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 110:
//#line 581 "gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 113:
//#line 596 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 116:
//#line 607 "gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 120:
//#line 621 "gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 121:
//#line 623 "gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 124:
//#line 631 "gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 125:
//#line 642 "gramatica.y"
{
                                    notifyDetection("Invocación de función."); 
                                    yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
                                }
break;
case 127:
//#line 652 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 128:
//#line 658 "gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 129:
//#line 663 "gramatica.y"
{
                                    notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
                                }
break;
//#line 1159 "Parser.java"
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
