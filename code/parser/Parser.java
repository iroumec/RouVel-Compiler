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

public class Parser {

  boolean yydebug; // do I want debug output?
  int yynerrs; // number of errors so far
  int yyerrflag; // was there an error?
  int yychar; // the current working character

  // ########## MESSAGES ##########
  // ###############################################################
  // method: debug
  // ###############################################################
  void debug(String msg) {
    if (yydebug)
      System.out.println(msg);
  }

  // ########## STATE STACK ##########
  final static int YYSTACKSIZE = 500; // maximum stack size
  int statestk[] = new int[YYSTACKSIZE]; // state stack
  int stateptr;
  int stateptrmax; // highest index of stackptr
  int statemax; // state when highest index reached
  // ###############################################################
  // methods: state stack push,pop,drop,peek
  // ###############################################################

  final void state_push(int state) {
    try {
      stateptr++;
      statestk[stateptr] = state;
    } catch (ArrayIndexOutOfBoundsException e) {
      int oldsize = statestk.length;
      int newsize = oldsize * 2;
      int[] newstack = new int[newsize];
      System.arraycopy(statestk, 0, newstack, 0, oldsize);
      statestk = newstack;
      statestk[stateptr] = state;
    }
  }

  final int state_pop() {
    return statestk[stateptr--];
  }

  final void state_drop(int cnt) {
    stateptr -= cnt;
  }

  final int state_peek(int relative) {
    return statestk[stateptr - relative];
  }

  // ###############################################################
  // method: init_stacks : allocate and prepare stacks
  // ###############################################################
  final boolean init_stacks() {
    stateptr = -1;
    val_init();
    return true;
  }

  // ###############################################################
  // method: dump_stacks : show n levels of the stacks
  // ###############################################################
  void dump_stacks(int count) {
    int i;
    System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
    for (i = 0; i < count; i++)
      System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
    System.out.println("======================");
  }

  // ########## SEMANTIC VALUES ##########
  // public class ParserVal is defined in ParserVal.java

  String yytext;// user variable to return contextual strings
  ParserVal yyval; // used to return semantic vals from action routines
  ParserVal yylval;// the 'lval' (result) I got from yylex()
  ParserVal valstk[];
  int valptr;

  // ###############################################################
  // methods: value stack push,pop,drop,peek.
  // ###############################################################
  void val_init() {
    valstk = new ParserVal[YYSTACKSIZE];
    yyval = new ParserVal();
    yylval = new ParserVal();
    valptr = -1;
  }

  void val_push(ParserVal val) {
    if (valptr >= YYSTACKSIZE)
      return;
    valstk[++valptr] = val;
  }

  ParserVal val_pop() {
    if (valptr < 0)
      return new ParserVal();
    return valstk[valptr--];
  }

  void val_drop(int cnt) {
    int ptr;
    ptr = valptr - cnt;
    if (ptr < 0)
      return;
    valptr = ptr;
  }

  ParserVal val_peek(int relative) {
    int ptr;
    ptr = valptr - relative;
    if (ptr < 0)
      return new ParserVal();
    return valstk[ptr];
  }

  final ParserVal dup_yyval(ParserVal val) {
    ParserVal dup = new ParserVal();
    dup.ival = val.ival;
    dup.dval = val.dval;
    dup.sval = val.sval;
    dup.obj = val.obj;
    return dup;
  }

  // #### end semantic value section ####
  public final static short ID = 257;
  public final static short CTE = 258;
  public final static short STR = 259;
  public final static short EQ = 260;
  public final static short GEQ = 261;
  public final static short LEQ = 262;
  public final static short NEQ = 263;
  public final static short DASIG = 264;
  public final static short FLECHA = 265;
  public final static short PRINT = 266;
  public final static short IF = 267;
  public final static short ELSE = 268;
  public final static short ENDIF = 269;
  public final static short UINT = 270;
  public final static short CVR = 271;
  public final static short DO = 272;
  public final static short WHILE = 273;
  public final static short RETURN = 274;
  public final static short YYERRCODE = 256;
  final static short yylhs[] = { -1,
      0, 1, 1, 2, 2, 4, 4, 5, 5, 7,
      8, 8, 10, 11, 11, 13, 13, 3, 3, 3,
      3, 3, 3, 15, 16, 16, 22, 23, 23, 23,
      23, 23, 23, 20, 20, 20, 20, 21, 21, 17,
      24, 24, 19, 19, 19, 25, 25, 26, 26, 26,
      27, 27, 12, 12, 9, 9, 18, 18, 6, 6,
      29, 28, 28, 30, 31, 31, 14, 32, 32, 33,
  };
  final static short yylen[] = { 2,
      4, 2, 3, 1, 1, 2, 1, 1, 3, 3,
      1, 3, 8, 3, 2, 2, 3, 1, 1, 1,
      1, 1, 1, 3, 1, 1, 3, 1, 1, 1,
      1, 1, 1, 6, 8, 6, 8, 6, 6, 4,
      1, 1, 3, 1, 3, 1, 1, 3, 1, 3,
      1, 1, 1, 1, 1, 2, 1, 3, 8, 7,
      4, 1, 3, 3, 0, 1, 4, 1, 3, 3,
  };
  final static short yydefred[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 4, 5, 0, 7, 20, 23, 18, 19, 21,
      22, 0, 25, 26, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 1, 0, 2, 0, 0, 0,
      0, 55, 0, 54, 49, 53, 0, 0, 0, 68,
      58, 41, 0, 0, 0, 0, 0, 15, 0, 0,
      0, 0, 0, 3, 9, 0, 11, 0, 56, 0,
      46, 47, 0, 51, 52, 0, 0, 67, 40, 30,
      32, 31, 33, 28, 29, 0, 0, 66, 0, 0,
      62, 0, 16, 14, 0, 0, 0, 0, 0, 70,
      45, 0, 50, 48, 69, 0, 0, 0, 0, 0,
      0, 17, 0, 0, 0, 12, 36, 0, 34, 0,
      0, 63, 0, 64, 38, 39, 0, 0, 0, 60,
      0, 0, 37, 35, 0, 59, 13, 61,
  };
  final static short yydgoto[] = { 2,
      120, 11, 12, 13, 14, 15, 16, 66, 44, 17,
      33, 45, 60, 18, 19, 20, 21, 46, 55, 23,
      24, 56, 86, 54, 73, 48, 76, 90, 121, 91,
      92, 49, 50,
  };
  final static short yysindex[] = { -247,
      -109, 0, 11, -8, -9, -1, -199, 13, -207, -16,
      9, 0, 0, -31, 0, 0, 0, 0, 0, 0,
      0, -191, 0, 0, -5, -182, -44, -5, 49, 39,
      25, -175, -172, -162, 0, 38, 0, -155, -37, -5,
      57, 0, -154, 0, 0, 0, -41, -14, 41, 0,
      0, 0, 51, 64, 26, 67, -38, 0, 52, 27,
      70, 72, 73, 0, 0, 69, 0, 51, 0, -156,
      0, 0, 1, 0, 0, 15, -5, 0, 0, 0,
      0, 0, 0, 0, 0, -5, -4, 0, -2, 43,
      0, -152, 0, 0, 61, -5, -5, -4, -37, 0,
      0, -14, 0, 0, 0, 51, -176, 11, -156, 4,
      -135, 0, 82, 83, 85, 0, 0, -4, 0, -40,
      5, 0, 11, 0, 0, 0, -5, -178, -5, 0,
      6, 87, 0, 0, 31, 0, 0, 0,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, -35, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -23, 74,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      -25, 0, 0, 0, 0, 0, 0, -18, 0, 0,
      0, 0, 88, 0, 0, 0, -144, 0, 0, 0,
      0, 0, 0, 0, 0, -54, 0, -53, 0, -144,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 2, 0, 0, 0, 91, 0, 0, -144, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      131, -3, 21, 0, 128, 0, 0, 0, -27, 0,
      -39, -61, 0, 0, 0, 0, 0, 46, 30, 0,
      0, 3, 0, 0, 0, 65, 0, 0, 14, -59,
      0, 0, 62,
  };
  final static int YYTABLESIZE = 299;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 9,
        43, 71, 89, 72, 10, 24, 36, 43, 8, 1,
        100, 67, 38, 3, 104, 57, 57, 57, 57, 57,
        8, 57, 44, 9, 44, 8, 44, 75, 32, 39,
        27, 25, 74, 57, 57, 8, 57, 26, 28, 43,
        44, 44, 43, 44, 43, 43, 43, 107, 22, 122,
        9, 59, 9, 22, 47, 22, 53, 29, 115, 43,
        43, 43, 34, 43, 9, 132, 9, 37, 71, 68,
        72, 116, 40, 71, 51, 72, 22, 133, 128, 117,
        95, 78, 38, 110, 77, 85, 109, 84, 57, 138,
        134, 118, 119, 71, 63, 72, 64, 61, 113, 114,
        62, 65, 26, 69, 79, 22, 47, 87, 35, 96,
        93, 97, 99, 98, 88, 106, 36, 111, 31, 112,
        108, 41, 125, 126, 127, 65, 123, 137, 42, 130,
        136, 27, 6, 10, 30, 31, 131, 102, 105, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 58,
        0, 94, 0, 22, 0, 0, 124, 0, 135, 0,
        0, 0, 0, 0, 0, 22, 0, 0, 22, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 41, 42, 52, 0, 4, 0, 10, 24,
        42, 0, 0, 70, 0, 5, 6, 0, 57, 7,
        0, 8, 88, 129, 57, 57, 57, 57, 0, 57,
        4, 44, 44, 44, 44, 0, 44, 57, 0, 5,
        6, 41, 42, 7, 44, 8, 101, 41, 42, 0,
        0, 43, 43, 43, 43, 0, 43, 4, 0, 4,
        103, 41, 42, 0, 43, 0, 5, 6, 5, 6,
        7, 4, 8, 4, 8, 80, 81, 82, 83, 0,
        5, 6, 5, 6, 0, 0, 8, 0, 8,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 40,
        45, 43, 41, 45, 59, 59, 10, 45, 44, 257,
        70, 39, 44, 123, 76, 41, 42, 43, 44, 45,
        44, 47, 41, 40, 43, 61, 45, 42, 8, 61,
        40, 40, 47, 59, 60, 59, 62, 46, 40, 45,
        59, 60, 41, 62, 43, 45, 45, 87, 3, 109,
        40, 31, 40, 8, 25, 10, 27, 257, 98, 45,
        59, 60, 270, 62, 40, 127, 40, 59, 43, 40,
        45, 99, 264, 43, 257, 45, 31, 256, 118, 256,
        60, 41, 44, 41, 44, 60, 44, 62, 40, 59,
        269, 268, 269, 43, 257, 45, 59, 273, 96, 97,
        273, 257, 46, 258, 41, 60, 77, 41, 125, 40,
        59, 40, 44, 41, 271, 86, 120, 270, 123, 59,
        123, 257, 41, 41, 40, 270, 123, 41, 41, 125,
        125, 41, 59, 3, 7, 123, 123, 73, 77, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, 125,
        -1, 125, -1, 108, -1, -1, 111, -1, 129, -1,
        -1, -1, -1, -1, -1, 120, -1, -1, 123, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 257, 258, 259, -1, 257, -1, 273, 273,
        258, -1, -1, 265, -1, 266, 267, -1, 264, 270,
        -1, 272, 271, 274, 260, 261, 262, 263, -1, 265,
        257, 260, 261, 262, 263, -1, 265, 273, -1, 266,
        267, 257, 258, 270, 273, 272, 256, 257, 258, -1,
        -1, 260, 261, 262, 263, -1, 265, 257, -1, 257,
        256, 257, 258, -1, 273, -1, 266, 267, 266, 267,
        270, 257, 272, 257, 272, 260, 261, 262, 263, -1,
        266, 267, 266, 267, -1, -1, 272, -1, 272,
    };
  }

  final static short YYFINAL = 2;
  final static short YYMAXTOKEN = 274;
  final static String yyname[] = {
      "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
      "'-'", "'.'", "'/'", null, null, null, null, null, null, null, null, null, null, null, "';'",
      "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, "ID", "CTE", "STR", "EQ", "GEQ", "LEQ", "NEQ",
      "DASIG", "FLECHA", "PRINT", "IF", "ELSE", "ENDIF", "UINT", "CVR", "DO", "WHILE", "RETURN",
  };
  final static String yyrule[] = {
      "$accept : programa",
      "programa : ID '{' conjunto_sentencias '}'",
      "conjunto_sentencias : sentencia ';'",
      "conjunto_sentencias : conjunto_sentencias sentencia ';'",
      "sentencia : sentencia_ejecutable",
      "sentencia : sentencia_declarativa",
      "sentencia_declarativa : UINT lista_variables",
      "sentencia_declarativa : declaracion_funcion",
      "lista_variables : ID",
      "lista_variables : lista_variables ',' ID",
      "asignacion_multiple : lista_variables '=' lista_constantes",
      "lista_constantes : constante",
      "lista_constantes : lista_constantes ',' constante",
      "lambda : '(' UINT ID ')' bloque_ejecutable '(' factor ')'",
      "bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
      "bloque_ejecutable : '{' '}'",
      "conjunto_sentencias_ejecutables : sentencia_ejecutable ';'",
      "conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable ';'",
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
      "if : IF '(' condicion ')' bloque_ejecutable ELSE bloque_ejecutable error",
      "while : DO sentencia_ejecutable WHILE '(' condicion ')'",
      "while : DO bloque_ejecutable WHILE '(' condicion ')'",
      "impresion : PRINT '(' imprimible ')'",
      "imprimible : STR",
      "imprimible : expresion",
      "expresion : expresion operador_suma termino",
      "expresion : termino",
      "expresion : expresion operador_suma error",
      "operador_suma : '+'",
      "operador_suma : '-'",
      "termino : termino operador_multiplicacion factor",
      "termino : factor",
      "termino : termino operador_multiplicacion error",
      "operador_multiplicacion : '/'",
      "operador_multiplicacion : '*'",
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
      "lista_argumentos : argumento",
      "lista_argumentos : lista_argumentos ',' argumento",
      "argumento : expresion FLECHA parametro_formal",
  };

  // #line 232 "gramatica.y"

  /*
   * -----------------------------------------------------------------------------
   * -----------------------
   */
  /* INICIO DE CÓDIGO (opcional) */
  /*
   * -----------------------------------------------------------------------------
   * -----------------------
   */

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
  }

  // Método público para llamar a yyparse(), ya que, por defecto,
  // su modificador de visibilidad es package.
  public void execute() {
    yyparse();
  }

  void notifyDetection(String message) {
    System.out.printf("%nDETECCIÓN SEMÁNTICA: %s%n%n", message);
  }

  void notifyWarning(String warningMessage) {
    System.err.printf("%WARNING SINTÁCTICA: Línea %d: %s%n%n", lexer.getNroLinea(), warningMessage);
    this.warningsDetected++;
  }

  void notifyError(String errorMessage) {
    System.err.printf("%nERROR SINTÁCTICO: Línea %d: %s%n%n", lexer.getNroLinea(), errorMessage);
    this.errorsDetected++;
  }

  // Método yylex() invocado durante yyparse().
  int yylex() {

    if (lexer == null) {
      throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getSymbolTableKey());

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
  }

  void yyerror(String message) {
    System.out.println(message);
  }

  /*
   * -----------------------------------------------------------------------------
   * -----------------------
   */
  /* FIN DE CÓDIGO */
  /*
   * -----------------------------------------------------------------------------
   * -----------------------
   */
  // #line 418 "Parser.java"
  // ###############################################################
  // method: yylexdebug : check lexer state
  // ###############################################################
  void yylexdebug(int state, int ch) {
    String s = null;
    if (ch < 0)
      ch = 0;
    if (ch <= YYMAXTOKEN) // check index bounds
      s = yyname[ch]; // now get it
    if (s == null)
      s = "illegal-symbol";
    debug("state " + state + ", reading " + ch + " (" + s + ")");
  }

  // The following are now global, to aid in error reporting
  int yyn; // next next thing to do
  int yym; //
  int yystate; // current parsing state from state table
  String yys; // current token string

  // ###############################################################
  // method: yyparse : parse input and execute indicated items
  // ###############################################################
  int yyparse() {
    boolean doaction;
    init_stacks();
    yynerrs = 0;
    yyerrflag = 0;
    yychar = -1; // impossible char forces a read
    yystate = 0; // initial state
    state_push(yystate); // save it
    val_push(yylval); // save empty value
    while (true) // until parsing is done, either correctly, or w/error
    {
      doaction = true;
      if (yydebug)
        debug("loop");
      // #### NEXT ACTION (from reduction table)
      for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
        if (yydebug)
          debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
        if (yychar < 0) // we want a char?
        {
          yychar = yylex(); // get next token
          if (yydebug)
            debug(" next yychar:" + yychar);
          // #### ERROR CHECK ####
          if (yychar < 0) // it it didn't work/error
          {
            yychar = 0; // change it to default string (no -1!)
            if (yydebug)
              yylexdebug(yystate, yychar);
          }
        } // yychar<0
        yyn = yysindex[yystate]; // get amount to shift by (shift index)
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
          if (yydebug)
            debug("state " + yystate + ", shifting to state " + yytable[yyn]);
          // #### NEXT STATE ####
          yystate = yytable[yyn];// we are in a new state
          state_push(yystate); // save it
          val_push(yylval); // push our lval as the input for next rule
          yychar = -1; // since we have 'eaten' a token, say we need another
          if (yyerrflag > 0) // have we recovered an error?
            --yyerrflag; // give ourselves credit
          doaction = false; // but don't process yet
          break; // quit the yyn=0 loop
        }

        yyn = yyrindex[yystate]; // reduce
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) { // we reduced!
          if (yydebug)
            debug("reduce");
          yyn = yytable[yyn];
          doaction = true; // get ready to execute
          break; // drop down to actions
        } else // ERROR RECOVERY
        {
          if (yyerrflag == 0) {
            yyerror("syntax error");
            yynerrs++;
          }
          if (yyerrflag < 3) // low error count?
          {
            yyerrflag = 3;
            while (true) // do until break
            {
              if (stateptr < 0) // check for under & overflow here
              {
                yyerror("stack underflow. aborting..."); // note lower case 's'
                return 1;
              }
              yyn = yysindex[state_peek(0)];
              if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                  yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                if (yydebug)
                  debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                yystate = yytable[yyn];
                state_push(yystate);
                val_push(yylval);
                doaction = false;
                break;
              } else {
                if (yydebug)
                  debug("error recovery discarding state " + state_peek(0) + " ");
                if (stateptr < 0) // check for under & overflow here
                {
                  yyerror("Stack underflow. aborting..."); // capital 'S'
                  return 1;
                }
                state_pop();
                val_pop();
              }
            }
          } else // discard this token
          {
            if (yychar == 0)
              return 1; // yyabort
            if (yydebug) {
              yys = null;
              if (yychar <= YYMAXTOKEN)
                yys = yyname[yychar];
              if (yys == null)
                yys = "illegal-symbol";
              debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
            }
            yychar = -1; // read another
          }
        } // end error recovery
      } // yyn=0 loop
      if (!doaction) // any reason not to proceed?
        continue; // skip action
      yym = yylen[yyn]; // get count of terminals on rhs
      if (yydebug)
        debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
      if (yym > 0) // if count of rhs not 'nil'
        yyval = val_peek(yym - 1); // get current semantic value
      yyval = dup_yyval(yyval); // duplicate yyval if ParserVal is used as semantic value
      switch (yyn) {
        // ########## USER-SUPPLIED ACTIONS ##########
        case 1:
        // #line 23 "gramatica.y"
        {
          notifyDetection("Programa.");
        }
          break;
        case 6:
        // #line 39 "gramatica.y"
        {
          notifyDetection("Declaración de variable.");
        }
          break;
        case 7:
        // #line 41 "gramatica.y"
        {
          notifyDetection("Declaración de función.");
        }
          break;
        case 10:
        // #line 57 "gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 13:
        // #line 66 "gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 15:
        // #line 78 "gramatica.y"
        {
          notifyError("El cuerpo de la sentencia no puede estar vacío.");
        }
          break;
        case 24:
        // #line 94 "gramatica.y"
        {
          notifyDetection("Asignación simple.");
        }
          break;
        case 26:
        // #line 100 "gramatica.y"
        {
          notifyDetection("Setencia WHILE.");
        }
          break;
        case 34:
        // #line 115 "gramatica.y"
        {
          notifyDetection("Setencia IF.");
        }
          break;
        case 35:
        // #line 117 "gramatica.y"
        {
          notifyDetection("Setencia IF-ELSE.");
        }
          break;
        case 36:
        // #line 122 "gramatica.y"
        {
          notifyError("La sentencia IF debe finalizarse con 'endif'.");
        }
          break;
        case 37:
        // #line 124 "gramatica.y"
        {
          notifyError("La sentencia IF-ELSE debe finalizarse con 'endif'.");
        }
          break;
        case 41:
        // #line 135 "gramatica.y"
        {
          notifyDetection("Impresión de cadena.");
        }
          break;
        case 42:
        // #line 137 "gramatica.y"
        {
          notifyDetection("Impresión de expresión.");
        }
          break;
        case 60:
        // #line 196 "gramatica.y"
        {
          notifyError("Toda función debe recibir al menos un parámetro.");
        }
          break;
        case 67:
        // #line 216 "gramatica.y"
        {
          notifyDetection("Invocación de función.");
        }
          break;
        // #line 631 "Parser.java"
        // ########## END OF USER-SUPPLIED ACTIONS ##########
      }// switch
       // #### Now let's reduce... ####
      if (yydebug)
        debug("reduce");
      state_drop(yym); // we just reduced yylen states
      yystate = state_peek(0); // get new state
      val_drop(yym); // corresponding value drop
      yym = yylhs[yyn]; // select next TERMINAL(on lhs)
      if (yystate == 0 && yym == 0)// done? 'rest' state and at first TERMINAL
      {
        if (yydebug)
          debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
        yystate = YYFINAL; // explicitly say we're done
        state_push(YYFINAL); // and save it
        val_push(yyval); // also save the semantic value of parsing
        if (yychar < 0) // we want another character?
        {
          yychar = yylex(); // get next character
          if (yychar < 0)
            yychar = 0; // clean, if necessary
          if (yydebug)
            yylexdebug(yystate, yychar);
        }
        if (yychar == 0) // Good exit (if lex returns 0 ;-)
          break; // quit the loop--all DONE
      } // if yystate
      else // else not done yet
      { // get next state and push, for next yydefred[]
        yyn = yygindex[yym]; // find out where to go
        if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
          yystate = yytable[yyn]; // get new state
        else
          yystate = yydgoto[yym]; // else go to new defred
        if (yydebug)
          debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
        state_push(yystate); // going again, so push state & val...
        val_push(yyval); // for next action
      }
    } // main loop
    return 0;// yyaccept!!
  }
  // ## end of method parse() ######################################

  // ## run() --- for Thread #######################################
  // ## The -Jnorun option was used ##
  // ## end of method run() ########################################

  // ## Constructors ###############################################
  // ## The -Jnoconstruct option was used ##
  // ###############################################################

}
// ################### END OF CLASS ##############################
