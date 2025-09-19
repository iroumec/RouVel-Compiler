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
    9,    9,    9,   11,   12,   15,   13,   13,    3,    3,
    3,    3,    3,    3,   17,   18,   18,   22,   23,   23,
   23,   23,   23,   23,   20,   20,   21,   19,   24,   24,
   10,   10,   10,   25,   25,   25,   14,   14,   14,    8,
    8,    6,    6,   27,   26,   26,   28,   28,   16,   29,
   29,
};
final static short yylen[] = {                            2,
    4,    2,    3,    1,    1,    2,    1,    1,    3,    3,
    5,    3,    1,    5,    2,    3,    1,    3,    1,    1,
    1,    1,    1,    1,    3,    1,    1,    3,    1,    1,
    1,    1,    1,    1,    6,    8,    6,    4,    1,    1,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    1,
    3,    8,    7,    4,    1,    3,    3,    2,    4,    3,
    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    5,    7,   21,    0,   24,    0,   19,   20,   22,
   23,   26,   27,    0,    0,    0,    0,    0,    6,    0,
    0,    0,    1,    0,    0,    0,   13,    0,    0,    0,
   48,    0,   47,    0,   46,    0,    0,   51,   39,    0,
    0,    0,    0,    0,    0,   15,    0,    0,    3,    0,
    0,    0,    0,   49,    0,    0,    0,    0,    0,    0,
   59,   38,   31,   33,   32,   34,   29,   30,    0,    0,
    0,    9,    0,    0,    0,    0,    0,   18,    0,    0,
   12,    0,   60,   61,    0,    0,   44,   45,    0,    0,
    0,   58,    0,    0,    0,    0,    0,    0,   14,    0,
    0,   35,   57,    0,    0,    0,   56,   37,   11,   16,
    0,    0,   53,    0,   36,    0,   52,   54,
};
final static short yydgoto[] = {                          2,
  114,   10,   11,   12,   29,   13,   14,   43,   38,   44,
   16,   17,   32,   45,  101,   18,   19,   20,   21,   22,
   23,   53,   79,   51,   46,   86,  115,   87,   47,
};
final static short yysindex[] = {                      -233,
  -73,    0, -169,   44,   30,   33, -170, -142,  -36,   59,
    0,    0,    0,    0,  -27,    0, -142,    0,    0,    0,
    0,    0,    0,  -30, -165,   10,  -30,   55,    0, -128,
   78, -151,    0, -169,  -30, -119,    0,  -30,   99,   94,
    0, -117,    0,    1,    0,   35,  101,    0,    0,   76,
  102,   16,  103, -111,  -25,    0, -142,  107,    0,   76,
  -14,   63,  -30,    0, -139,  -30,  -30,  -30,  -30,  -30,
    0,    0,    0,    0,    0,    0,    0,    0,  -30,   28,
  106,    0, -111, -118,   31,  112,  111,    0,  -30,  -30,
    0,  115,    0,    0,   35,   35,    0,    0,   76, -142,
 -135,    0, -111, -169,   34, -139,  117,   69,    0,   38,
   28,    0,    0, -114,   39, -169,    0,    0,    0,    0,
 -104,  -30,    0,   43,    0,   24,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   15,    0,    0,
  -29,    0,    0, -107,    0,    0,    0,    0,    0,  -40,
    0,    0,    0,    0,    0,  -31,    0,    0,    0,  121,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -34,
    0,  -32,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   27,    0,    0,    0,    0,    0,  125,    0,    0,    0,
    0,    0,    0,    0,   -8,   -2,    0,    0,  128,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   46,    0,   48,    0,   17,    0,    0,   45,  109,   37,
    0,    0,    9,   41,   60,    0,    0,    0,    0,    0,
    0,   83,    0,    0,   68,   67,   58,  110,  113,
};
final static int YYTABLESIZE=287;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         50,
   50,   50,   50,   50,   50,   25,   50,   10,   43,   43,
   17,   43,   43,   43,   42,   85,   36,    2,   50,   50,
   50,   50,   50,    1,   25,   39,   10,   43,   43,   36,
   43,   41,   41,   37,   41,   41,   41,   42,   42,   50,
   42,   42,   42,   67,   66,   68,   37,   15,    9,    3,
   41,   41,   15,   41,   42,   31,   42,   42,   67,   42,
   68,   15,   50,   52,   31,   88,   67,    8,   68,   26,
   82,   60,   27,    8,   62,   78,   70,   77,   15,   59,
   61,   69,  128,   24,   50,    8,   28,    4,   33,   25,
   25,   48,   10,   43,   55,   17,    5,    6,   54,  102,
    7,   15,    8,   92,   31,   67,   91,   68,  110,   97,
   98,   67,  119,   68,    4,   99,   41,   34,   67,  113,
   68,   58,   42,    5,    6,   52,  108,   30,   56,    8,
   83,   84,  111,  112,   95,   96,   57,   40,   63,   25,
   64,   71,   72,   80,   15,   81,   89,   31,   15,   54,
  100,  103,  105,  104,  106,  109,  116,  118,  126,  122,
   15,   40,  120,  123,  125,   55,    2,  127,   28,   90,
  121,  107,  117,  124,   93,    0,    0,    0,   94,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   50,
   50,   50,   50,    0,   50,    0,   40,   41,   43,   43,
   43,   43,   50,   43,    0,    0,   35,    0,   25,    0,
   10,   43,   50,   17,   83,   84,    0,    0,    0,    0,
    0,   41,   41,   41,   41,    0,   41,   42,   42,   42,
   42,    0,   42,    0,   41,   65,   40,   41,   49,    0,
   42,   15,    0,    0,    0,   73,   74,   75,   76,    0,
   15,   15,    0,    0,   15,    0,   15,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   40,   40,   41,
   40,   43,   44,   45,   45,   41,   44,  125,   59,   60,
   61,   62,   44,  257,   59,   17,   59,   59,   60,   44,
   62,   40,   41,   61,   43,   44,   45,   40,   41,   61,
   43,   44,   45,   43,   44,   45,   61,    3,    3,  123,
   59,   60,    8,   62,   45,    8,   59,   60,   43,   62,
   45,   17,   26,   27,   17,   57,   43,   41,   45,   40,
   54,   35,   40,   59,   38,   60,   42,   62,   34,   34,
   36,   47,   59,   40,  125,   59,  257,  257,  125,   46,
  125,  257,  125,  125,   40,  125,  266,  267,   44,   83,
  270,   57,  272,   63,   57,   43,   44,   45,  100,   69,
   70,   43,   44,   45,  257,   79,  125,   59,   43,  103,
   45,  273,  125,  266,  267,   89,   90,  270,  257,  272,
  270,  271,  268,  269,   67,   68,   59,  257,   40,   46,
  258,   41,   41,   41,  100,  257,   40,  100,  104,   44,
  123,  270,   41,  123,   44,   41,  123,   41,  122,  274,
  116,   41,  125,  125,  269,   41,  274,  125,   41,   61,
  111,   89,  106,  116,   65,   -1,   -1,   -1,   66,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  260,
  261,  262,  263,   -1,  265,   -1,  257,  258,  260,  261,
  262,  263,  273,  265,   -1,   -1,  264,   -1,  273,   -1,
  273,  273,  264,  273,  270,  271,   -1,   -1,   -1,   -1,
   -1,  260,  261,  262,  263,   -1,  265,  260,  261,  262,
  263,   -1,  265,   -1,  273,  265,  257,  258,  259,   -1,
  273,  257,   -1,   -1,   -1,  260,  261,  262,  263,   -1,
  266,  267,   -1,   -1,  270,   -1,  272,
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
"lista_variables : ID ',' lista_variables",
"asignacion_multiple : variable termino_asignacion_multiple expresion",
"termino_asignacion_multiple : ',' variable termino_asignacion_multiple expresion ','",
"termino_asignacion_multiple : termino_asignacion_multiple expresion ','",
"termino_asignacion_multiple : '='",
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
"expresion : termino",
"termino : termino '/' factor",
"termino : termino '*' factor",
"termino : factor",
"factor : variable",
"factor : CTE",
"factor : '-' CTE",
"variable : ID",
"variable : ID '.' ID",
"declaracion_funcion : UINT ID '(' lista_parametros ')' '{' cuerpo_funcion '}'",
"declaracion_funcion : UINT ID '(' ')' '{' cuerpo_funcion '}'",
"cuerpo_funcion : conjunto_sentencias RETURN expresion ';'",
"lista_parametros : parametro_formal",
"lista_parametros : parametro_formal ',' lista_parametros",
"parametro_formal : CVR UINT lista_variables",
"parametro_formal : UINT lista_variables",
"invocacion_funcion : ID '(' lista_argumentos ')'",
"lista_argumentos : expresion FLECHA parametro_formal",
"lista_argumentos : expresion ',' lista_argumentos",
};

//#line 164 "gramatica.y"

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
//#line 374 "Parser.java"
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
case 53:
//#line 138 "gramatica.y"
{ System.out.println("Error: toda función debe recibir al menos un parámetro.")}
break;
//#line 527 "Parser.java"
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
