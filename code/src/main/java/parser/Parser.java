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
   24,   24,   24,   21,   21,   22,   18,   25,   25,   20,
   20,   20,   20,   26,   26,   26,   13,   13,    9,    9,
   19,   19,    6,    6,   28,   27,   27,   29,   30,   30,
   15,   31,   31,
};
final static short yylen[] = {                            2,
    4,    2,    3,    1,    1,    2,    1,    1,    3,    3,
    1,    3,    5,    2,    3,    1,    3,    1,    1,    1,
    1,    1,    1,    3,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    6,    8,    6,    4,    1,    1,    3,
    3,    4,    1,    3,    3,    1,    1,    1,    1,    2,
    1,    3,    8,    7,    4,    1,    3,    3,    0,    1,
    4,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    5,    0,    7,   20,   23,    0,   18,   19,   21,
   22,    0,   25,   26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
   49,    0,   48,   46,   47,    0,    0,    0,   52,   38,
    0,    0,    0,    0,    0,   14,    0,    0,    3,    9,
   10,    0,    0,    0,   50,    0,    0,    0,    0,    0,
    0,   61,   37,   30,   32,   31,   33,   28,   29,    0,
    0,   60,    0,    0,   56,    0,   17,    0,    0,    0,
   62,   63,    0,    0,    0,   44,   45,    0,    0,    0,
    0,    0,    0,    0,    0,   12,   13,    0,    0,    0,
   34,    0,    0,   57,    0,   58,   36,   15,    0,    0,
   54,    0,   35,    0,   53,   55,
};
final static short yydgoto[] = {                          2,
  112,   10,   11,   12,   13,   14,   15,   61,   43,   16,
   17,   33,   44,  100,   18,   19,   20,   21,   45,   46,
   23,   24,   54,   80,   52,   47,   84,  113,   85,   86,
   48,
};
final static short yysindex[] = {                      -231,
  -93,    0, -142,   58,   49,   66, -189, -131,   -9,   59,
    0,    0,   27,    0,    0,    0, -131,    0,    0,    0,
    0, -144,    0,    0,   18, -136,  -11,   18,   82,   85,
 -126,   74, -139,    0, -142, -120,    4,   98,   18,   96,
    0, -115,    0,    0,    0,  -27,   65,  103,    0,    0,
    1,  104,   41,  106,  -26,    0, -131,  108,    0,    0,
    0,  105,   18,    1,    0, -121,   18,   10,   18,   18,
   18,    0,    0,    0,    0,    0,    0,    0,    0,   18,
   29,    0,   30,   28,    0, -119,    0,   18,    4,  113,
    0,    0,   18,   65,   65,    0,    0,    1, -131, -155,
 -142, -121,   32, -101,  116,    0,    0,   65,   34,   29,
    0, -116,   35,    0, -142,    0,    0,    0, -107,   18,
    0,   38,    0,   52,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   23,  107,
    0,  -29,    0,    0, -104,    0,    0,    0,    0,  -40,
    0,    0,    0,    0,    0,    0,  -31,    0,    0,    0,
  123,    0,    0,    0, -105,    0,    0,    0,    0,    0,
    0,  -34,    0,  -32,    0, -105,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -8,   -2,    0,    0,  126,    0,    0,
    0, -105,    0,    0,    0,    0,    0,   21,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   42,    0,   62,    0,  161,    0,    0,   83,  -13,    0,
    0,   33,   39,   63,    0,    0,    0,    0,   70,   20,
    0,    0,   87,    0,    0,    6,    0,   61,  -10,    0,
  110,
};
final static int YYTABLESIZE=304;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
   51,   51,   51,   51,   51,   11,   51,   24,   43,   43,
   16,   43,   43,   43,   83,   68,   67,   69,   51,   51,
    2,   51,    8,   62,   11,    1,   24,   43,   43,    3,
   43,   40,   40,   42,   40,   40,   40,   41,   41,    8,
   41,   41,   41,   68,    9,   69,   51,   53,   42,   38,
   40,   40,   93,   40,   42,   91,   41,   41,   64,   41,
   42,   42,   42,   42,   42,   42,    8,   29,  103,   32,
   36,  102,   22,   94,   95,   62,   59,   22,   32,   42,
   42,    8,   42,   68,   51,   69,   22,   37,   27,   87,
   11,  114,   24,   43,   68,   16,   69,   25,  108,   98,
   79,   90,   78,   26,   22,   28,   71,   53,   96,   97,
  126,   70,  110,  111,    4,   34,   40,   35,   32,   39,
   49,   55,   41,    5,    6,    4,   22,    7,   36,    8,
   56,  109,   57,   58,    5,    6,   60,   63,   31,  124,
    8,   26,   65,   72,   73,   42,   81,   88,   89,   82,
  104,   99,  101,  107,  115,   40,  117,  120,  118,  121,
   32,  123,  125,   39,   59,    6,   27,   30,   22,    2,
   22,  106,  119,  116,  105,  122,   92,    0,    0,    0,
    0,    0,    0,    0,   22,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   51,
   51,   51,   51,    0,   51,    0,    0,    0,   43,   43,
   43,   43,   51,   43,    0,    0,    0,   66,   11,    0,
   24,   43,   51,   16,   82,   40,   41,   50,    0,    0,
    0,   40,   40,   40,   40,    0,   40,   41,   41,   41,
   41,   41,   41,    0,   40,    0,   40,   41,    0,    0,
   41,    0,    0,    0,   40,   41,    0,    0,    0,   14,
   42,   42,   42,   42,    0,   42,    0,    0,   14,   14,
    0,    0,   14,   42,   14,    0,    0,    0,    0,    0,
   74,   75,   76,   77,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   40,   40,   41,
   40,   43,   44,   45,   41,   43,   44,   45,   59,   60,
  125,   62,   44,   37,   59,  257,   59,   59,   60,  123,
   62,   40,   41,   45,   43,   44,   45,   40,   41,   61,
   43,   44,   45,   43,    3,   45,   27,   28,   45,   17,
   59,   60,   43,   62,   45,   66,   59,   60,   39,   62,
   40,   41,   45,   43,   44,   45,   44,  257,   41,    8,
   44,   44,    3,   68,   69,   89,   35,    8,   17,   59,
   60,   59,   62,   43,  125,   45,   17,   61,   40,   57,
  125,  102,  125,  125,   43,  125,   45,   40,   93,   80,
   60,   63,   62,   46,   35,   40,   42,   88,   70,   71,
   59,   47,  268,  269,  257,  125,  125,   59,   57,  264,
  257,   40,  125,  266,  267,  257,   57,  270,   44,  272,
  257,   99,   59,  273,  266,  267,  257,   40,  270,  120,
  272,   46,  258,   41,   41,  125,   41,   40,   44,  271,
  270,  123,  123,   41,  123,  257,   41,  274,  125,  125,
   99,  269,  125,   41,  270,   59,   41,    7,   99,  274,
  101,   89,  110,  104,   88,  115,   67,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  115,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  260,
  261,  262,  263,   -1,  265,   -1,   -1,   -1,  260,  261,
  262,  263,  273,  265,   -1,   -1,   -1,  265,  273,   -1,
  273,  273,  264,  273,  271,  257,  258,  259,   -1,   -1,
   -1,  260,  261,  262,  263,   -1,  265,  260,  261,  262,
  263,  258,  265,   -1,  273,   -1,  257,  258,   -1,   -1,
  273,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,  257,
  260,  261,  262,  263,   -1,  265,   -1,   -1,  266,  267,
   -1,   -1,  270,  273,  272,   -1,   -1,   -1,   -1,   -1,
  260,  261,  262,  263,
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
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
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
"while : DO conjunto_sentencias_ejecutables WHILE '(' condicion ')'",
"impresion : PRINT '(' imprimible ')'",
"imprimible : STR",
"imprimible : expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : expresion '+' '+' termino",
"expresion : termino",
"termino : termino '/' factor",
"termino : termino '*' factor",
"termino : factor",
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

//#line 183 "gramatica.y"

/* --------------------------------------------------------------------------------------------- */
/* INICIO DE CÓDIGO (opcional)                                                                   */
/* --------------------------------------------------------------------------------------------- */

// Lexer.
private final Lexer lexer;

/**
* Constructor que recibe un Lexer.
*/
public Parser(Lexer lexer) {
    this.lexer = lexer;
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

    return token.getIdentificationCode();
}

void yyerror(String message) {
    System.out.println(message);
}

/* --------------------------------------------------------------------------------------------- */
/* FIN DE CÓDIGO                                                                                 */
/* --------------------------------------------------------------------------------------------- */
//#line 382 "Parser.java"
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
case 10:
//#line 45 "gramatica.y"
{  }
break;
case 54:
//#line 154 "gramatica.y"
{ System.out.println("Error: toda función debe recibir al menos un parámetro.");}
break;
//#line 539 "Parser.java"
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
