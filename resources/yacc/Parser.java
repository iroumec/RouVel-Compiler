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
public final static short PRNT=260;
public final static short IF=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short UINT=264;
public final static short EQ=265;
public final static short LEQ=266;
public final static short GEQ=267;
public final static short NEQ=268;
public final static short CVR=269;
public final static short DO=270;
public final static short WHILE=271;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    4,    4,    5,    5,    6,
    7,    7,    8,    8,   10,   11,   14,   15,   12,   12,
    3,    3,   17,   17,   20,   22,   22,   22,   22,   22,
   22,   18,   18,   19,   23,   23,   21,   21,   21,   24,
   24,   24,   13,   13,   13,   13,    9,    9,   25,   26,
   26,   16,   28,   28,   27,   27,
};
final static short yylen[] = {                            2,
    4,    2,    3,    1,    1,    2,    1,    1,    3,    3,
    1,    3,    1,    3,    5,    2,    1,    3,    1,    3,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    1,
    1,    6,    8,    6,    4,    4,    3,    3,    1,    3,
    3,    1,    1,    3,    1,    2,    1,    3,    8,    1,
    3,    4,    1,    3,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    4,
    5,    7,    0,    0,   21,   22,   23,   24,    0,    0,
    0,    6,    0,    0,    0,    1,    0,    0,    0,    0,
   48,    0,   45,    0,   42,    0,    0,    0,    0,    0,
    0,    3,    0,   11,   13,    0,    0,    0,    0,    0,
    0,   46,    0,   28,   29,   30,   31,   26,   27,    0,
    0,    0,    0,    0,    9,   20,    0,    0,   56,    0,
    0,   52,   44,    0,    0,    0,    0,   40,   41,    0,
    0,   55,   54,    0,   32,   34,    0,   33,
};
final static short yydgoto[] = {                          2,
    8,    9,   23,   11,   22,   12,   13,   46,   24,    0,
    0,   25,   35,    0,    0,   15,   16,   17,   18,   36,
   37,   62,    0,   38,    0,    0,   49,   50,
};
final static short yysindex[] = {                      -235,
  -77,    0, -213,    4,   14, -201, -208,  -54,   16,    0,
    0,    0,   -3,   36,    0,    0,    0,    0, -180,  -42,
   34,    0,   20,   36, -191,    0, -213, -176, -175, -217,
    0,   38,    0, -173,    0,   41,  -20,   23, -201, -208,
   46,    0,   43,    0,    0,   44, -201, -174,   45,   50,
 -165,    0, -208,    0,    0,    0,    0,    0,    0,  -42,
  -42,  -42,  -42,  -42,    0,    0,  -42, -175,    0, -201,
 -217,    0,    0, -196,   23,   23,  -32,    0,    0,   52,
   44,    0,    0, -208,    0,    0, -169,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,   -6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -1,    0,    0,    0,    0,    0,    0,
  -14,    0, -199,    0,    0,    0,  -30,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,  -36,    0,    0,
    0,    0,   35,    0,    0,   39,    0,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -31,  -25,   56,    0,    0,    0,
   40,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   73,    0,   12,    0,  -11,    0,   74,   33,    5,    0,
    0,  -23,   10,    0,    0,    0,    0,    0,    0,   37,
   47,    0,    0,    8,    0,    0,    0,   32,
};
final static int YYTABLESIZE=248;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   43,   43,   34,   43,   39,   43,   39,   14,   39,   37,
   60,   37,   61,   37,   10,   38,   66,   38,   43,   38,
   43,    1,   60,   39,   61,   39,    8,   65,   37,   74,
   37,   14,   44,   47,   38,   69,   38,   47,   10,   59,
   28,   58,   11,    4,    8,    3,   47,    5,    4,   19,
    6,   48,    5,   20,   47,   21,    7,   29,   82,   11,
   87,    7,   19,   19,   64,   84,   85,   75,   76,   63,
   26,   19,   78,   79,   27,   30,   31,   39,   40,   41,
    4,   53,   45,   51,   52,   67,   28,   68,   71,   70,
   72,   73,   86,   88,    2,   12,   25,   10,   14,   42,
   81,   43,   83,   80,    0,    0,    0,    0,   77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   32,   33,    0,    0,    0,    0,
    0,    0,    0,   43,   43,   43,   43,    0,   39,   39,
   39,   39,    0,   37,   37,   37,   37,    0,    0,   38,
   38,   38,   38,    0,   54,   55,   56,   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   45,   45,   41,   47,   43,    3,   45,   41,
   43,   43,   45,   45,    3,   41,   40,   43,   60,   45,
   62,  257,   43,   60,   45,   62,   41,   39,   60,   53,
   62,   27,   28,   40,   60,   47,   62,   44,   27,   60,
   44,   62,   44,  257,   59,  123,  264,  261,  257,   46,
  264,  269,  261,   40,   61,  257,  270,   61,   70,   61,
   84,  270,  262,  263,   42,  262,  263,   60,   61,   47,
  125,  271,   63,   64,   59,   40,  257,   44,   59,  271,
  257,   41,  258,   46,  258,   40,   44,   44,   44,  264,
   41,  257,   41,  263,  125,   61,   41,   59,   59,   27,
   68,   28,   71,   67,   -1,   -1,   -1,   -1,   62,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  265,  266,  267,  268,   -1,  265,  266,
  267,  268,   -1,  265,  266,  267,  268,   -1,   -1,  265,
  266,  267,  268,   -1,  265,  266,  267,  268,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=271;
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
null,null,null,null,null,null,null,"ID","CTE","STR","PRNT","IF","ELSE","ENDIF",
"UINT","EQ","LEQ","GEQ","NEQ","CVR","DO","WHILE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' conjunto_sentencias '}'",
"conjunto_sentencias : sentencia ';'",
"conjunto_sentencias : sentencia ';' conjunto_sentencias",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia_declarativa : UINT lista_variables",
"sentencia_declarativa : asignacion",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"asignacion : identificador_multiple '=' expresion_multiple",
"identificador_multiple : identificador",
"identificador_multiple : identificador_multiple ',' identificador_multiple",
"expresion_multiple : CTE",
"expresion_multiple : expresion_multiple ',' expresion_multiple",
"lambda : parametro conjunto_sentencias_ejecutables '(' factor ')'",
"parametro : UINT ID",
"argumento : factor",
"bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
"conjunto_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_sentencias_ejecutables : sentencia_ejecutable ';' conjunto_sentencias_ejecutables",
"sentencia_ejecutable : invocacion_funcion",
"sentencia_ejecutable : sentencia_control",
"sentencia_control : if",
"sentencia_control : while",
"condicion : expresion comparador expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : EQ",
"comparador : LEQ",
"comparador : GEQ",
"comparador : NEQ",
"if : IF '(' condicion ')' conjunto_sentencias_ejecutables ENDIF",
"if : IF '(' condicion ')' conjunto_sentencias_ejecutables ELSE conjunto_sentencias_ejecutables ENDIF",
"while : DO conjunto_sentencias_ejecutables WHILE '(' condicion ')'",
"impresion : PRNT '(' STR ')'",
"impresion : PRNT '(' expresion ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '/' factor",
"termino : termino '*' factor",
"termino : factor",
"factor : ID",
"factor : ID '.' ID",
"factor : CTE",
"factor : '-' CTE",
"identificador : ID",
"identificador : ID '.' ID",
"funcion : UINT identificador '(' lista_parametros ')' '{' conjunto_sentencias '}'",
"lista_parametros : parametro_formal",
"lista_parametros : parametro_formal ',' lista_parametros",
"invocacion_funcion : identificador '(' lista_parametros_formales ')'",
"lista_parametros_formales : parametro_formal",
"lista_parametros_formales : parametro_formal ',' lista_parametros_formales",
"parametro_formal : CVR UINT lista_variables",
"parametro_formal : UINT lista_variables",
};

//#line 137 "gramatica.y"

/* código (opcional) */
//#line 308 "Parser.java"
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
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
