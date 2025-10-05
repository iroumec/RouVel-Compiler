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

    /* Importaciones.*/
    import lexer.Lexer;
    import common.Token;
    import utilities.Printer;
//#line 19 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
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
   35,   35,   37,   36,   36,   38,   38,   38,   38,   38,
   38,   38,   33,   33,   33,   33,   39,   39,   34,   40,
   40,   40,   40,   41,   32,   42,   42,   42,    5,    5,
    5,    5,   43,   43,   11,   11,    6,    6,    6,   44,
   44,    7,    7,    7,    4,    3,    3,   20,   20,   46,
   46,   31,   45,   45,   47,   47,   47,   49,   49,   48,
   48,   48,   50,   50,   50,    8,    9,    9,   10,   10,
};
final static short yylen[] = {                            2,
    2,    2,    1,    1,    3,    2,    0,    3,    1,    2,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    3,    3,    4,    0,    1,
    1,    3,    2,    3,    1,    3,    2,    7,    2,    0,
    1,    1,    3,    2,    1,    2,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    1,    1,    3,
    2,    2,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    5,    4,    5,    2,    0,    2,    2,    2,
    1,    2,    3,    2,    4,    1,    1,    0,    3,    1,
    1,    2,    1,    1,    2,    2,    3,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    3,    8,    7,    1,
    0,    4,    1,    0,    1,    3,    1,    2,    2,    3,
    2,    2,    0,    1,    1,    4,    1,    3,    3,    1,
};
final static short yydefred[] = {                         0,
    4,    0,    0,    0,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   49,    1,    0,    9,   22,   23,
   24,    0,   51,   55,    0,   50,   52,   53,   54,   58,
   59,    0,    6,    0,    0,    0,    0,    0,    0,  105,
    0,  102,  103,    0,    0,   98,  104,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   41,   42,    0,   79,
   81,    0,    0,    0,   33,    0,    0,    0,    0,    0,
   10,   30,   25,   48,    8,    5,    0,    0,  127,  107,
   86,    0,    0,    0,   62,    0,   68,   70,   69,   71,
   72,   66,   67,   93,   94,    0,    0,  100,  101,    0,
    0,    0,    0,   61,   27,    0,  125,  124,    0,    0,
    0,  115,  117,    0,    0,   26,   84,    0,   44,   45,
    0,    0,   82,   80,    0,   39,    0,   32,    0,   35,
    0,    0,   15,   18,   16,   17,   19,   21,   20,   13,
   12,   11,   14,    0,    0,  126,   85,    0,    0,   63,
   60,    0,    0,   99,   97,    0,    0,  119,    0,    0,
    0,    0,  122,   28,   47,   43,   46,   83,  112,    0,
    0,   37,  129,  128,   78,   75,   73,    0,    0,  116,
  120,    0,   36,    0,    0,    0,    0,    0,  109,   38,
  108,
};
final static short yydgoto[] = {                          4,
   13,  129,   42,   43,   44,   45,   46,   47,   78,   79,
   48,    5,  185,   18,  142,  143,   19,   20,   21,   22,
   73,   23,   24,   64,   58,   59,  121,   25,   26,   27,
   28,   29,   30,   31,  117,   50,  151,   96,  149,   60,
   61,   83,   97,  101,  110,  186,  111,  112,  113,  114,
};
final static short yysindex[] = {                      -114,
    0,   12,  -40,    0,    0,   13,  -11,   31,   20,  -12,
   14, -205,  -39, -229,    0,    0,   24,    0,    0,    0,
    0,   -2,    0,    0,   11,    0,    0,    0,    0,    0,
    0,  -43,    0,  -28, -152, -173, -213,   40,   13,    0,
   47,    0,    0,  -13,    8,    0,    0, -152,   40,   45,
   53,   59,  -31,    7,  -36,   36,    0,    0,  -38,    0,
    0, -152, -164,   61,    0, -140, -138, -152, -152,  197,
    0,    0,    0,    0,    0,    0,  -24,   37,    0,    0,
    0,   -9,   77, -147,    0,   81,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -152, -152,    0,    0,   16,
 -142,   16, -147,    0,    0,  -31,    0,    0, -240,   82,
   80,    0,    0, -233,   66,    0,    0,   19,    0,    0,
   52,   43,    0,    0,   34,    0,   18,    0,  -27,    0,
   -9,   -9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -125, -152,    0,    0,   40, -135,    0,
    0,   -9,   16,    0,    0, -132,  117,    0,   55, -240,
  113,  -97,    0,    0,    0,    0,    0,    0,    0,  136,
 -138,    0,    0,    0,    0,    0,    0,   56,   63,    0,
    0, -152,    0,   63,   24,   73,  144,   75,    0,    0,
    0,
};
final static short yyrindex[] = {                       201,
    0,  201,    0,    0,    0,   -5,    0,    0,    0,    0,
    0,  165,    0,    0,    0,    0,  207,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  167,  150,   86,    0,
    0,    0,    0,  169,  121,    0,    0,  130,    0,    0,
    0,   60,  -33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   46,    0,    0,    0,
    0,  174,    0,  -16,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  143,    0,    0,   95,
    0,  108,  -26,    0,    0,  -33,    0,    0, -232,    0,
  192,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  211,    0,
  213,  215,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  242,  152,    0,    0,   11,    0,    0,    0,  -34,
   50,   54,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  160,    0,
    0,    0,    0,  160,  170,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  302,    0,  399,  -61,    5,  -25,  -86,  419,    0,  168,
    0,  313,  106,    9,    0,    0,   51,    0,    0,    0,
    0,    0,    0,    0,  194,  -35,    0,    0,    0,    0,
    0,    0,    0,    0,    3,  282,    0,    0,  222,    0,
  268,    0,    0,    0,  225,  148,    0,  -91,    0,    0,
};
final static int YYTABLESIZE=604;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   29,   41,   84,   41,   66,  130,  118,  114,    3,  118,
   49,   12,  109,  103,  155,  107,  171,  158,   94,  100,
   95,   67,  102,  161,  123,   71,   68,   12,   37,   94,
  108,   95,   77,   94,   69,   95,  162,  123,   31,   77,
   29,   82,   71,   39,   40,   81,   93,   91,   92,   99,
   66,   12,   35,   62,   98,   31,   72,   99,   36,   53,
   57,  123,   98,   12,   63,  116,  125,  172,  180,   74,
   41,  153,  131,  132,  169,   12,   94,  146,   95,   12,
  145,   75,   41,   80,   33,  104,  130,   85,   57,  130,
  106,   12,  126,  106,  121,  187,   76,  121,  106,   57,
  152,  127,   12,   31,   39,   40,  120,   17,   34,  183,
   56,  105,  175,  154,   39,   40,  128,  147,   31,   40,
  148,  150,  159,  160,  164,   29,  106,  106,  106,  106,
  106,  173,  106,  176,    3,   95,  177,   95,   95,   95,
   56,    1,    2,  165,  106,  106,  106,  106,   96,   77,
   96,   96,   96,   95,   95,   95,   95,  178,   36,  161,
  119,   90,   56,   90,   90,   90,   96,   96,   96,   96,
   91,  167,   91,   91,   91,  182,  166,  179,  184,   90,
   90,   90,   90,   92,  190,   92,   92,   92,   91,   91,
   91,   91,   89,   71,   89,   89,   89,  189,   57,  191,
    7,   92,   92,   92,   92,   40,    2,   88,   76,   65,
   89,   89,   89,   89,   87,   32,    6,   65,   39,   40,
   39,   40,  123,  123,  107,    7,    8,   70,    6,    9,
   40,   10,  113,   11,  122,  123,  123,    7,    8,  108,
  144,    9,   77,   10,    6,   11,   87,   88,   89,   90,
  106,   31,   77,    7,    8,  141,   29,   29,  106,   10,
   55,   11,  115,   65,   39,   40,   29,   29,    6,   34,
   29,   57,   29,   56,   29,   51,   52,    7,    8,   70,
    6,    9,   64,   10,  111,   11,   38,   39,   40,    7,
    8,  118,    6,    9,  110,   10,    6,   11,  168,   39,
   40,    7,    8,   39,   40,    7,    8,   10,    6,   11,
   54,   10,  174,   11,   16,   31,   31,    7,    8,    6,
  170,  140,   86,   10,  156,   11,  124,    0,    7,    8,
  157,  188,    9,    0,   10,    0,   11,    0,    0,    0,
    0,    0,  106,  106,    0,  106,  106,  106,  106,    0,
  106,   95,   95,    0,   95,   95,   95,   95,    0,   95,
    0,    0,    0,    0,   96,   96,    0,   96,   96,   96,
   96,    0,   96,    0,    0,    0,    0,    0,    0,    0,
   90,   90,   90,   90,    0,   90,    0,    0,    0,   91,
   91,   91,   91,    0,   91,    0,    0,    0,    0,    0,
   14,   14,   92,   92,   92,   92,    0,   92,   14,    0,
    0,   89,   89,   89,   89,   14,   89,    0,    0,    0,
   15,   15,    0,    0,    0,    0,    0,    0,   15,    0,
    0,    0,   14,    0,    0,   15,   14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   14,    0,    0,
    0,    0,   15,  133,   14,    0,   15,    0,    0,    0,
    0,    0,  134,  135,    0,    0,  136,   15,  137,  138,
  139,    0,    0,    0,   15,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  163,    0,    0,    0,    0,    0,    0,   14,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   15,
    0,    0,    0,    0,    0,    0,   14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  181,    0,    0,    0,    0,    0,   15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   14,    0,    0,
    0,    0,   14,   14,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   15,    0,    0,
    0,    0,   15,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   38,   40,   44,   67,   41,   41,  123,   44,
    8,   40,   44,   49,  101,  256,   44,  109,   43,   45,
   45,   61,   48,  257,  257,   17,  256,   40,   40,   43,
  271,   45,   59,   43,  264,   45,  270,  270,   44,   35,
   40,   37,   34,  257,  258,  259,   60,   61,   62,   42,
   44,   40,   40,   40,   47,   61,   59,   42,   46,   40,
   10,   59,   47,   40,  270,   59,   62,  129,  160,   59,
   40,   97,   68,   69,   41,   40,   43,   41,   45,   40,
   44,  125,   40,  257,  125,   41,   41,   41,   38,   44,
   41,   40,  257,   44,   41,  182,  125,   44,   40,   49,
   96,   41,   40,   44,  257,  258,   56,    2,    3,  171,
  123,   59,  148,  256,  257,  258,  257,   41,   59,  258,
  268,   41,   41,   44,   59,  125,   41,   42,   43,   44,
   45,  257,   47,  269,  123,   41,  269,   43,   44,   45,
  123,  256,  257,  125,   59,   60,   61,   62,   41,  145,
   43,   44,   45,   59,   60,   61,   62,   41,   46,  257,
  125,   41,  123,   43,   44,   45,   59,   60,   61,   62,
   41,  121,   43,   44,   45,   40,  125,  123,  123,   59,
   60,   61,   62,   41,   41,   43,   44,   45,   59,   60,
   61,   62,   41,  185,   43,   44,   45,  125,  148,  125,
    0,   59,   60,   61,   62,   41,    0,   41,   59,   41,
   59,   60,   61,   62,   41,  256,  257,  257,  257,  258,
  257,  258,  257,  257,  256,  266,  267,  256,  257,  270,
  258,  272,   41,  274,  273,  270,  270,  266,  267,  271,
  265,  270,  269,  272,  257,  274,  260,  261,  262,  263,
  256,  257,  269,  266,  267,   59,  256,  257,  264,  272,
  273,  274,  256,  257,  257,  258,  266,  267,  257,   59,
  270,   59,  272,   59,  274,  256,  257,  266,  267,  256,
  257,  270,   41,  272,  125,  274,  256,  257,  258,  266,
  267,  256,  257,  270,  125,  272,  257,  274,  256,  257,
  258,  266,  267,  257,  258,  266,  267,  272,  257,  274,
    9,  272,  145,  274,    2,  256,  257,  266,  267,  257,
  127,  125,   41,  272,  103,  274,   59,   -1,  266,  267,
  106,  184,  270,   -1,  272,   -1,  274,   -1,   -1,   -1,
   -1,   -1,  257,  258,   -1,  260,  261,  262,  263,   -1,
  265,  257,  258,   -1,  260,  261,  262,  263,   -1,  265,
   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,
  263,   -1,  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,  260,
  261,  262,  263,   -1,  265,   -1,   -1,   -1,   -1,   -1,
    2,    3,  260,  261,  262,  263,   -1,  265,   10,   -1,
   -1,  260,  261,  262,  263,   17,  265,   -1,   -1,   -1,
    2,    3,   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,
   -1,   -1,   34,   -1,   -1,   17,   38,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   49,   -1,   -1,
   -1,   -1,   34,  257,   56,   -1,   38,   -1,   -1,   -1,
   -1,   -1,  266,  267,   -1,   -1,  270,   49,  272,  273,
  274,   -1,   -1,   -1,   56,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  114,   -1,   -1,   -1,   -1,   -1,   -1,  121,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  121,
   -1,   -1,   -1,   -1,   -1,   -1,  148,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  162,   -1,   -1,   -1,   -1,   -1,  148,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  179,   -1,   -1,
   -1,   -1,  184,  185,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  179,   -1,   -1,
   -1,   -1,  184,  185,
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
"conjunto_sentencias_ejecutables : error '}'",
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
"if : IF error cuerpo_ejecutable rama_else ENDIF",
"if : IF error",
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

//#line 683 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
    // yydebug = true;
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

    System.out.println("> " + token.getLexema());

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
//#line 99 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Programa."); }
break;
case 2:
//#line 105 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Las sentencias del programa deben estar delimitadas por llaves."); }
break;
case 3:
//#line 107 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa requiere de un nombre."); }
break;
case 4:
//#line 109 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
                                    descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID.*/
                                }
break;
case 6:
//#line 122 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El programa no posee ninguna sentencia."); }
break;
case 8:
//#line 125 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 11:
//#line 136 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia inválida en el lenguaje."); }
break;
case 13:
//#line 143 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ Printer.print("HOlaaa"); readLastTokenAgain(); }
break;
case 14:
//#line 145 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Just testing"); readLastTokenAgain(); }
break;
case 24:
//#line 171 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 27:
//#line 181 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 28:
//#line 183 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'."); }
break;
case 32:
//#line 198 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 33:
//#line 209 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 34:
//#line 227 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación múltiple."); }
break;
case 36:
//#line 234 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 37:
//#line 239 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
                                        val_peek(1).sval, val_peek(0).sval));
                                }
break;
case 38:
//#line 252 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Expresión lambda."); }
break;
case 40:
//#line 261 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La expresión lambda requiere de un parámetro."); }
break;
case 44:
//#line 279 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la sentencia no puede estar vacío."); }
break;
case 47:
//#line 293 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda sentencia ejecutable debe terminar con punto y coma."); }
break;
case 56:
//#line 316 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 57:
//#line 321 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Error en asignación. Se esperaba un ':='."); }
break;
case 59:
//#line 328 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia WHILE."); }
break;
case 61:
//#line 338 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta apertura de paréntesis en condición."); }
break;
case 62:
//#line 340 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La condición no puede estar vacía."); }
break;
case 65:
//#line 377 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de comparador en comparación."); }
break;
case 72:
//#line 392 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(
                                        "Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?"
                                        );
                                }
break;
case 73:
//#line 405 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Sentencia IF."); }
break;
case 74:
//#line 407 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 75:
//#line 414 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Sentencia IF inválida en el lenguaje. Falta cierre de paréntesis en condición."); }
break;
case 76:
//#line 419 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Sentencia IF inválida en el lenguaje."); 
                                }
break;
case 81:
//#line 452 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Debe especificarse un cuerpo para la sentencia do-while."); }
break;
case 82:
//#line 454 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta 'while'."); }
break;
case 83:
//#line 459 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta cierre de paréntesis en condición."); }
break;
case 86:
//#line 475 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 87:
//#line 477 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 88:
//#line 482 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("La sentencia 'print' requiere de al menos un argumento."); }
break;
case 91:
//#line 495 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 92:
//#line 497 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Falta de operando en expresión.");
                                    yyval.sval = val_peek(1).sval;    
                                }
break;
case 95:
//#line 516 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        val_peek(1).sval, val_peek(0).sval)
                                    );
                                    yyval.sval = val_peek(0).sval;
                                }
break;
case 96:
//#line 524 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError(String.format(
                                        "Falta de operador entre operandos %s y %s.",
                                        val_peek(1).sval, val_peek(0).sval)
                                    );
                                    yyval.sval = val_peek(0).sval;
                                }
break;
case 98:
//#line 537 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 99:
//#line 539 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de operando en expresión."); }
break;
case 102:
//#line 551 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 103:
//#line 553 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 104:
//#line 555 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 105:
//#line 561 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 107:
//#line 571 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + "." + val_peek(0).sval; }
break;
case 108:
//#line 579 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyDetection("Declaración de función."); }
break;
case 109:
//#line 581 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre en la función."); }
break;
case 111:
//#line 591 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("El cuerpo de la función no puede estar vacío."); }
break;
case 114:
//#line 606 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
case 117:
//#line 617 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Se halló un parámetro formal vacío."); }
break;
case 121:
//#line 631 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de nombre de parámetro formal en declaración de función."); }
break;
case 122:
//#line 633 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ notifyError("Falta de tipo de parámetro formal en declaración de funcion."); }
break;
case 125:
//#line 641 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
                                    descartarTokenError();
                                }
break;
case 126:
//#line 652 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyDetection("Invocación de función."); 
                                    yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
                                }
break;
case 128:
//#line 662 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 129:
//#line 668 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{ yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; }
break;
case 130:
//#line 673 "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/code/parser/gramatica.y"
{
                                    notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
                                }
break;
//#line 1161 "Parser.java"
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
