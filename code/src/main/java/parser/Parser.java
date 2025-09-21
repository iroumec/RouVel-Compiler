package parser;
import lexer.Lexer;
import common.Token;

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
    0,    1,    1,    2,    2,    4,    4,    5,    5,    7,
    8,    8,   10,   11,   14,   12,   12,    3,    3,    3,
    3,    3,    3,   16,   17,   17,   23,   24,   24,   24,
   24,   24,   24,   21,   21,   21,   22,   22,   18,   25,
   25,   20,   20,   20,   20,   20,   26,   26,   26,   26,
   26,   13,   13,    9,    9,   19,   19,    6,    6,   28,
   27,   27,   29,   30,   30,   15,   31,   31,
};
final static short yylen[] = {                            2,
    4,    2,    3,    1,    1,    2,    1,    1,    3,    3,
    1,    3,    5,    2,    3,    2,    3,    1,    1,    1,
    1,    1,    1,    3,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    6,    8,    6,    6,    6,    4,    1,
    1,    3,    3,    1,    3,    3,    3,    3,    1,    3,
    3,    1,    1,    1,    2,    1,    3,    8,    7,    4,
    1,    3,    3,    0,    1,    4,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    5,    0,    7,   20,   23,    0,   18,   19,   21,
   22,    0,   25,   26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,   54,    0,   53,   49,   52,    0,    0,    0,
   57,   40,    0,    0,    0,    0,    0,   14,    0,    0,
    0,    3,    9,   10,    0,    0,    0,    0,   55,    0,
    0,    0,    0,    0,    0,   66,   39,   30,   32,   31,
   33,   28,   29,    0,    0,   65,    0,    0,   61,    0,
   15,    0,    0,    0,   17,    0,   67,   68,   45,    0,
   46,    0,   50,   47,   51,   48,    0,    0,    0,    0,
    0,    0,    0,    0,   12,   13,   36,    0,   34,    0,
    0,   62,    0,   63,   37,   38,    0,    0,   59,    0,
   35,    0,   58,   60,
};
final static short yydgoto[] = {                          2,
  120,   10,   11,   12,   13,   14,   15,   64,   45,   16,
   17,   40,   46,   34,   18,   19,   20,   21,   47,   55,
   23,   24,   56,   84,   54,   49,   88,  121,   89,   90,
   50,
};
final static short yysindex[] = {                      -215,
  -79,    0, -174,   45,   37,   71, -207, -115,  -69,   30,
    0,    0,   -1,    0,    0,    0, -167,    0,    0,    0,
    0, -147,    0,    0,   29, -137,    4,   29,   81,   79,
 -133, -167, -148, -146,    0, -174, -131,  -25,   69,   89,
   29,   84,    0, -127,    0,    0,    0,  -31,   55,   91,
    0,    0,   67,   92,   16,   93,  -36,    0,   11,   95,
   97,    0,    0,    0,   94, -167,   29,   67,    0, -132,
   29,  -13,   10,   24,   27,    0,    0,    0,    0,    0,
    0,    0,    0,   29,   17,    0,   18,   60,    0, -126,
    0,   29,   29,  -25,    0,  102,    0,    0,    0,   55,
    0,   55,    0,    0,    0,    0,   67, -162, -174, -132,
   22, -111,  106,  107,    0,    0,    0,   17,    0, -125,
   28,    0, -174,    0,    0,    0, -119,   29,    0,   31,
    0,   25,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    3,   99,
    0,    0,    0,    0,    0, -110,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,    0,  -34,    0,
    0,    0,  113,    0,    0,    0, -109,    0,    0,    0,
    0,    0,    0,    0,  -43,  -17,    0,  -37,    0, -109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -14,
    0,   -8,    0,    0,    0,    0,  119,    0,    0, -109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   35,    0,   49,    0,  156,    0,    0,   73,    1,    0,
    0,    9,   13,  -45,    0,    0,    0,    0,   50,   38,
    0,    0,   21,    0,    0,   46,    0,   42,  -46,    0,
   98,
};
final static int YYTABLESIZE=287;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   56,   56,   56,   56,   87,   56,   44,   32,   44,   44,
   44,   72,   71,   73,    2,   11,    8,   56,   56,   44,
   56,   24,   16,   97,   44,   44,   42,   44,   42,   42,
   42,   44,   43,    8,   43,   43,   43,    9,   65,  108,
   59,    1,   37,    3,   42,   42,    8,   42,   44,   29,
   43,   43,   22,   43,   44,   35,   33,   22,   72,   38,
   73,    8,   48,  122,   53,   39,   22,   72,   44,   73,
   62,   44,  127,   44,   95,   83,   27,   82,   68,   96,
   39,   22,    4,  134,   25,   22,  104,  106,   36,    4,
   26,    5,    6,  117,   65,    7,   75,    8,    5,    6,
  111,   74,   31,  110,    8,  118,  119,   16,   48,   72,
   28,   73,  113,  114,   39,   22,   41,  100,  102,   51,
   57,  107,   37,   58,   60,   63,   61,   66,   67,   26,
   69,   76,   77,   85,   92,   91,   93,   94,   86,   32,
  109,    4,  116,  112,  123,   42,  125,  126,  128,  131,
    5,    6,  129,   41,   31,  133,    8,    6,   22,   27,
   64,  124,   30,    2,  130,  132,  115,    0,   98,    0,
    0,    0,   22,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   56,   56,
   56,   56,    0,   56,    0,   44,   44,   44,   44,   11,
   44,   56,   43,   70,   86,   24,   56,    0,   44,    0,
    0,    0,   99,   42,   43,   42,   42,   42,   42,    0,
   42,   43,   43,   43,   43,    0,   43,    0,   42,   14,
   42,   43,   52,    0,   43,  101,   42,   43,   14,   14,
    0,    0,   14,    0,   14,   78,   79,   80,   81,  103,
   42,   43,  105,   42,   43,   42,   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   41,  123,   43,   44,
   45,   43,   44,   45,  125,   59,   44,   59,   60,   45,
   62,   59,   40,   70,   59,   60,   41,   62,   43,   44,
   45,   45,   41,   61,   43,   44,   45,    3,   38,   85,
   32,  257,   44,  123,   59,   60,   44,   62,   45,  257,
   59,   60,    3,   62,   45,  125,    8,    8,   43,   61,
   45,   59,   25,  110,   27,   17,   17,   43,   45,   45,
   36,   45,  118,   45,   66,   60,   40,   62,   41,   67,
   32,   32,  257,   59,   40,   36,   74,   75,   59,  257,
   46,  266,  267,  256,   94,  270,   42,  272,  266,  267,
   41,   47,  270,   44,  272,  268,  269,  125,   71,   43,
   40,   45,   92,   93,   66,   66,  264,   72,   73,  257,
   40,   84,   44,  257,  273,  257,  273,   59,   40,   46,
  258,   41,   41,   41,   40,  125,   40,   44,  271,  123,
  123,  257,   41,  270,  123,  257,   41,   41,  274,  269,
  266,  267,  125,   41,  270,  125,  272,   59,  109,   41,
  270,  112,    7,  274,  123,  128,   94,   -1,   71,   -1,
   -1,   -1,  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  260,  261,
  262,  263,   -1,  265,   -1,  260,  261,  262,  263,  273,
  265,  273,  258,  265,  271,  273,  264,   -1,  273,   -1,
   -1,   -1,  256,  257,  258,  260,  261,  262,  263,   -1,
  265,  260,  261,  262,  263,   -1,  265,   -1,  273,  257,
  257,  258,  259,   -1,  273,  256,  257,  258,  266,  267,
   -1,   -1,  270,   -1,  272,  260,  261,  262,  263,  256,
  257,  258,  256,  257,  258,  257,  258,
};
}
final static short YYFINAL=2;
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
"programa : ID '{' conjunto_sentencias '}'",
"conjunto_sentencias : sentencia ';'",
"conjunto_sentencias : sentencia ';' conjunto_sentencias",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : UINT lista_variables",
"sentencia_declarativa : declaracion_funcion",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"asignacion_multiple : lista_variables '=' lista_constantes",
"lista_constantes : constante",
"lista_constantes : constante ',' lista_constantes",
"lambda : parametro conjunto_sentencias_ejecutables '(' factor ')'",
"parametro : UINT ID",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable ';'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable ';' conjunto_sentencias_ejecutables",
"sentencia_ejecutable : invocacion_funcion",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : impresion",
"sentencia_ejecutable : lambda",
"asignacion_simple : variable DASIG expresion",
"sentencia_control : if",
"sentencia_control : while",
"condicion : expresion comparador expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"if : IF '(' condicion ')' bloque_ejecutable ENDIF",
"if : IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable ENDIF",
"if : IF '(' condicion ')' bloque_ejecutable error",
"while : DO sentencia_ejecutable WHILE '(' condicion ')'",
"while : DO bloque_ejecutable WHILE '(' condicion ')'",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : expresion '+' error",
"expresion : expresion '-' error",
"termino : termino '/' factor",
"termino : termino '*' factor",
"termino : factor",
"termino : termino '/' error",
"termino : termino '*' error",
"factor : variable",
"factor : constante",
"constante : CTE",
"constante : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' lista_parametros ')' '{' cuerpo_funcion '}'",
"declaracion_funcion : UINT ID '(' ')' '{' cuerpo_funcion '}'",
"cuerpo_funcion : conjunto_sentencias RETURN expresion ';'",
"lista_parametros : parametro_formal",
"lista_parametros : lista_parametros ',' parametro_formal",
"parametro_formal : semantica_pasaje UINT variable",
"semantica_pasaje :",
"semantica_pasaje : CVR",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : expresion FLECHA parametro_formal",
"lista_argumentos : expresion ',' lista_argumentos",
};

//#line 189 "gramatica.y"

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                   */
/* --------------------------------------------------------------------------------------------- */

// End of File.
public final static short EOF = 0;

// Lexer.
private final Lexer lexer;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
}

// Método público para llamar a yyparse(), ya que, por defecto,
// su modificador de visibilidad es package.
public void execute() {
    yyparse();
}

void notifyError(String errorMessage) {
    System.out.println("\nERROR SINTÁCTICO: " + errorMessage + '\n');
}

void notifyDetection(String message) {
    System.out.println("\nDETECCIÓN SEMÁNTICA: " + message + '\n');
}

// Método yylex() invocado durante yyparse().
int yylex() {

    if (lexer == null) {
        throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = (token.hasSymbolTableIndex()) ?
        new ParserVal(token.getSymbolTableIndex())
        : new ParserVal();

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
}

void yyerror(String message) {
    System.out.println(message);
}

/* --------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                 */
/* --------------------------------------------------------------------------------------------- */
//#line 406 "Parser.java"
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
//#line 21 "gramatica.y"
{ notifyDetection("Programa."); }
break;
case 6:
//#line 36 "gramatica.y"
{ notifyDetection("Declaración de variable."); }
break;
case 10:
//#line 47 "gramatica.y"
{  }
break;
case 24:
//#line 83 "gramatica.y"
{ notifyDetection("Asignación simple."); }
break;
case 25:
//#line 87 "gramatica.y"
{ notifyDetection("Setencia IF."); }
break;
case 26:
//#line 89 "gramatica.y"
{ notifyDetection("Setencia WHILE."); }
break;
case 36:
//#line 106 "gramatica.y"
{ notifyError("La sentencia IF debe finalizarse con 'endif'."); }
break;
case 40:
//#line 116 "gramatica.y"
{ notifyDetection("Impresión de cadena."); }
break;
case 41:
//#line 117 "gramatica.y"
{ notifyDetection("Impresión de expresión."); }
break;
case 59:
//#line 160 "gramatica.y"
{ notifyError("Toda función debe recibir al menos un parámetro."); }
break;
//#line 595 "Parser.java"
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
