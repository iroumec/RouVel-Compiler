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
      0, 1, 1, 2, 2, 4, 4, 5, 5, 5,
      5, 7, 7, 8, 8, 9, 10, 10, 12, 13,
      13, 16, 16, 14, 14, 14, 17, 17, 3, 3,
      18, 18, 18, 18, 18, 18, 18, 20, 21, 21,
      28, 28, 29, 29, 29, 29, 29, 29, 29, 26,
      31, 31, 30, 30, 27, 23, 32, 32, 25, 25,
      33, 33, 34, 34, 35, 35, 15, 15, 15, 11,
      11, 24, 24, 6, 37, 37, 22, 36, 36, 38,
      38, 38, 40, 40, 39, 41, 41, 41, 19, 42,
      42, 43,
  };
  final static short yylen[] = { 2,
      4, 1, 2, 1, 1, 1, 2, 3, 4, 5,
      4, 0, 1, 1, 3, 3, 1, 3, 7, 2,
      0, 1, 1, 3, 2, 0, 1, 2, 2, 2,
      1, 1, 1, 1, 1, 1, 1, 3, 1, 1,
      3, 0, 1, 1, 1, 1, 1, 1, 1, 7,
      1, 1, 0, 2, 6, 4, 1, 1, 3, 1,
      1, 1, 3, 1, 1, 1, 1, 1, 1, 1,
      2, 1, 3, 8, 1, 0, 2, 1, 0, 1,
      3, 1, 2, 2, 3, 0, 1, 1, 4, 1,
      3, 3,
  };
  final static short yydefred[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 2, 4, 5, 6, 0, 0, 33, 37,
      0, 31, 32, 34, 35, 36, 0, 39, 40, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 23, 22,
      0, 69, 0, 70, 0, 68, 64, 67, 0, 0,
      0, 0, 1, 3, 13, 7, 0, 0, 30, 29,
      0, 0, 14, 0, 0, 0, 90, 73, 57, 0,
      0, 0, 0, 0, 0, 8, 25, 27, 0, 0,
      71, 61, 62, 0, 65, 66, 0, 20, 0, 15,
      0, 17, 0, 0, 11, 0, 0, 89, 56, 49,
      45, 47, 46, 48, 43, 44, 0, 0, 9, 88,
      87, 0, 0, 0, 80, 82, 0, 24, 28, 0,
      0, 63, 0, 0, 10, 92, 91, 0, 0, 84,
      0, 0, 0, 0, 0, 18, 0, 0, 0, 81,
      85, 55, 0, 54, 52, 51, 50, 0, 0, 19,
      74,
  };
  final static short yydgoto[] = { 2,
      12, 13, 14, 15, 16, 17, 56, 18, 19, 91,
      46, 20, 52, 40, 47, 41, 79, 21, 22, 23,
      24, 25, 26, 48, 65, 28, 29, 73, 107, 138,
      147, 71, 84, 50, 87, 113, 149, 114, 115, 116,
      117, 66, 67,
  };
  final static short yysindex[] = { -234,
      -60, 0, -16, -202, 32, 34, 46, -224, -5, -9,
      -181, -40, 0, 0, 0, 0, 37, -24, 0, 0,
      -53, 0, 0, 0, 0, 0, -164, 0, 0, -129,
      -9, -152, 57, -9, -147, 75, -18, -2, 0, 0,
      -156, 0, 73, 0, -136, 0, 0, 0, 71, 10,
      -132, 88, 0, 0, 0, 0, -127, -34, 0, 0,
      -9, -147, 0, -15, -42, 12, 0, 0, 0, 71,
      90, 49, 91, -14, -36, 0, 0, 0, 9, 93,
      0, 0, 0, -9, 0, 0, -9, 0, 14, 0,
      92, 0, 71, -13, 0, -122, -9, 0, 0, 0,
      0, 0, 0, 0, 0, 0, -9, -5, 0, 0,
      0, -206, 97, 95, 0, 0, -130, 0, 0, -9,
      10, 0, 102, -34, 0, 0, 0, 71, -125, 0,
      21, -206, -112, 105, -9, 0, -5, -163, -16, 0,
      0, 0, 106, 0, 0, 0, 0, -16, 23, 0,
      0,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, -27, 0, 0, 0, -121, 0,
      110, 0, 0, 0, 0, 0, -28, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 113, 0, 11, 0, 0, 0, 0,
      0, 0, 17, 0, 0, 0, 0, 0, -52, 28,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 115,
      0, 0, 0, 0, -37, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 117, 0,
      -50, 0, -49, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, -165, 0, 0,
      0, -111, 0, 119, 0, 0, 0, 0, 0, 113,
      39, 0, 0, 0, 0, 0, 0, 120, -149, 0,
      0, -39, 0, 0, 0, 0, -148, 0, 33, 0,
      0, 0, 0, 0, 0, 0, 0, 38, 0, 0,
      0,
  };
  final static short yygindex[] = { 0,
      26, 2, 4, 0, 0, 0, 0, 13, 0, 0,
      -43, 0, 0, 77, -69, -86, 0, 0, 0, 0,
      0, 0, 0, 16, 6, 0, 0, 42, 0, 0,
      0, 0, 0, 83, 0, 0, 0, 0, -85, 0,
      0, 0, 72,
  };
  final static int YYTABLESIZE = 316;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 11,
        82, 83, 83, 79, 83, 60, 77, 112, 16, 38,
        45, 12, 39, 54, 92, 49, 14, 122, 27, 57,
        37, 129, 1, 11, 27, 57, 130, 27, 57, 57,
        57, 35, 36, 14, 11, 45, 58, 11, 70, 72,
        76, 78, 64, 95, 109, 125, 140, 74, 11, 110,
        144, 86, 98, 27, 14, 97, 85, 72, 72, 72,
        72, 72, 3, 72, 111, 143, 93, 30, 60, 14,
        60, 31, 60, 33, 94, 72, 72, 32, 72, 59,
        136, 59, 119, 59, 53, 34, 60, 60, 51, 60,
        26, 82, 145, 83, 27, 55, 12, 59, 59, 61,
        59, 45, 26, 26, 68, 146, 53, 26, 106, 63,
        105, 39, 128, 82, 75, 83, 80, 38, 32, 53,
        26, 81, 77, 27, 88, 72, 62, 63, 89, 90,
        99, 108, 120, 118, 126, 124, 38, 131, 132, 133,
        39, 135, 137, 139, 43, 142, 150, 151, 141, 54,
        21, 26, 27, 42, 27, 58, 26, 76, 86, 78,
        41, 134, 75, 27, 148, 123, 121, 0, 127, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 59, 77, 0, 16, 38, 0, 0, 0,
        0, 0, 0, 0, 0, 4, 5, 0, 0, 110,
        0, 0, 96, 44, 0, 6, 7, 12, 12, 8,
        86, 9, 86, 10, 111, 0, 72, 12, 12, 4,
        5, 12, 0, 12, 0, 12, 42, 43, 44, 6,
        7, 5, 0, 8, 5, 9, 0, 10, 0, 0,
        6, 7, 0, 6, 7, 5, 9, 0, 10, 9,
        0, 10, 72, 0, 6, 7, 72, 72, 72, 72,
        9, 72, 10, 60, 0, 0, 0, 60, 60, 60,
        60, 0, 60, 0, 59, 0, 0, 0, 59, 59,
        59, 59, 0, 59, 100, 0, 0, 0, 101, 102,
        103, 104, 42, 43, 44, 69,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 40,
        43, 41, 45, 41, 44, 59, 59, 44, 59, 59,
        45, 40, 9, 12, 58, 10, 44, 87, 3, 44,
        8, 108, 257, 40, 9, 44, 112, 12, 44, 44,
        44, 256, 257, 61, 40, 45, 61, 40, 33, 34,
        59, 38, 30, 59, 59, 59, 132, 35, 40, 256,
        137, 42, 41, 38, 44, 44, 47, 41, 42, 43,
        44, 45, 123, 47, 271, 135, 61, 270, 41, 59,
        43, 40, 45, 40, 62, 59, 60, 46, 62, 41,
        124, 43, 79, 45, 125, 40, 59, 60, 270, 62,
        256, 43, 256, 45, 79, 59, 125, 59, 60, 264,
        62, 45, 268, 269, 257, 269, 256, 256, 60, 257,
        62, 108, 107, 43, 40, 45, 273, 123, 46, 269,
        269, 258, 125, 108, 257, 120, 256, 257, 41, 257,
        41, 41, 40, 125, 257, 44, 123, 41, 44, 270,
        137, 40, 268, 123, 257, 41, 41, 125, 133, 148,
        41, 273, 137, 41, 139, 41, 40, 125, 270, 41,
        41, 120, 125, 148, 139, 89, 84, -1, 97, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 256, 256, -1, 256, 256, -1, -1, -1,
        -1, -1, -1, -1, -1, 256, 257, -1, -1, 256,
        -1, -1, 265, 258, -1, 266, 267, 256, 257, 270,
        270, 272, 270, 274, 271, -1, 264, 266, 267, 256,
        257, 270, -1, 272, -1, 274, 256, 257, 258, 266,
        267, 257, -1, 270, 257, 272, -1, 274, -1, -1,
        266, 267, -1, 266, 267, 257, 272, -1, 274, 272,
        -1, 274, 256, -1, 266, 267, 260, 261, 262, 263,
        272, 265, 274, 256, -1, -1, -1, 260, 261, 262,
        263, -1, 265, -1, 256, -1, -1, -1, 260, 261,
        262, 263, -1, 265, 256, -1, -1, -1, 260, 261,
        262, 263, 256, 257, 258, 259,
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
      "conjunto_sentencias : sentencia",
      "conjunto_sentencias : conjunto_sentencias sentencia",
      "sentencia : sentencia_ejecutable",
      "sentencia : sentencia_declarativa",
      "sentencia_declarativa : declaracion_variable",
      "sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
      "declaracion_variable : UINT lista_variables ';'",
      "declaracion_variable : UINT error lista_variables ';'",
      "declaracion_variable : error UINT error lista_variables ';'",
      "declaracion_variable : error UINT lista_variables ';'",
      "punto_y_coma_opcional :",
      "punto_y_coma_opcional : ';'",
      "lista_variables : ID",
      "lista_variables : lista_variables ',' ID",
      "asignacion_multiple : lista_variables '=' lista_constantes",
      "lista_constantes : constante",
      "lista_constantes : lista_constantes ',' constante",
      "lambda : '(' parametro_lambda ')' bloque_ejecutable '(' factor ')'",
      "parametro_lambda : UINT ID",
      "parametro_lambda :",
      "cuerpo_ejecutable : bloque_ejecutable",
      "cuerpo_ejecutable : sentencia_ejecutable",
      "bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
      "bloque_ejecutable : '{' '}'",
      "bloque_ejecutable :",
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
      "sentencia_control : if",
      "sentencia_control : while",
      "condicion : expresion comparador expresion",
      "condicion :",
      "comparador : '>'",
      "comparador : '<'",
      "comparador : EQ",
      "comparador : LEQ",
      "comparador : GEQ",
      "comparador : NEQ",
      "comparador : error",
      "if : IF '(' condicion ')' cuerpo_ejecutable rama_else fin_if",
      "fin_if : ENDIF",
      "fin_if : error",
      "rama_else :",
      "rama_else : ELSE cuerpo_ejecutable",
      "while : DO cuerpo_ejecutable WHILE '(' condicion ')'",
      "impresion : PRINT '(' imprimible ')'",
      "imprimible : STR",
      "imprimible : expresion",
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
      "factor : error",
      "constante : CTE",
      "constante : '-' CTE",
      "variable : ID",
      "variable : ID '.' ID",
      "declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' cuerpo_funcion '}'",
      "cuerpo_funcion : conjunto_sentencias",
      "cuerpo_funcion :",
      "sentencia_retorno : RETURN expresion",
      "conjunto_parametros : lista_parametros",
      "conjunto_parametros :",
      "lista_parametros : parametro_formal",
      "lista_parametros : lista_parametros ',' parametro_formal",
      "lista_parametros : parametro_vacio",
      "parametro_vacio : lista_parametros ','",
      "parametro_vacio : ',' parametro_formal",
      "parametro_formal : semantica_pasaje UINT variable",
      "semantica_pasaje :",
      "semantica_pasaje : CVR",
      "semantica_pasaje : error",
      "invocacion_funcion : ID '(' lista_argumentos ')'",
      "lista_argumentos : argumento",
      "lista_argumentos : lista_argumentos ',' argumento",
      "argumento : expresion FLECHA ID",
  };

  // #line 327 "/home/iroumec/Documents/University/Compiladores e
  // Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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

  // Método yylex() invocado durante yyparse().
  int yylex() {

    if (lexer == null) {
      throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    // Se muestra el token.
    System.out.println(token);

    return token.getIdentificationCode();
  }

  void yyerror(String message) {
    // Silenciar esto permite que mensajes como "syntax error", al utilizar
    // un token de error, no se muestren.
    // En su lugar, se presentan los mensajes que nosotros brindamos,
    // diferenciando entre error y warning.
  }

  // El token error no consume automáticamente el token incorrecto.
  // Este debe descartarse explícitamente.
  void descartarTokenError() {
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro
    // token.
    yychar = -1;

    // Se limpia el estado de error.
    // yyerrflag = 0;
  }
  // TODO: descartar hasta un punto de sincronizacion. "}" o ";".

  void notifyDetection(String message) {
    System.err.println("------------------------------------");
    System.err.printf("DETECCIÓN SEMÁNTICA: %s\n", message);
    System.err.println("------------------------------------");
  }

  void notifyWarning(String warningMessage) {
    System.err.println("------------------------------------");
    System.err.printf("WARNING SINTÁCTICA: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(),
        warningMessage);
    System.err.println("------------------------------------");
    this.warningsDetected++;
  }

  void notifyError(String errorMessage) {
    System.err.println("------------------------------------");
    System.err.printf("ERROR SINTÁCTICO: Línea %d, caracter %d: %s\n", lexer.getNroLinea(), lexer.getNroCaracter(),
        errorMessage);
    System.err.println("------------------------------------");
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

  /**
   * Este método es llamado por el parser en cuanto detecta un error de sintaxis.
   * Aquí es donde obtienes el token que fue descartado.
   */
  @Override
  public void syntax_error(java_cup.runtime.Symbol cur_token) {
    // ¡'cur_token' ES el token que intentabas obtener con $1!
    // Por ejemplo, si la entrada era "miVar UINT ...", cur_token contiene "miVar".

    String valorErroneo = (cur_token.value != null) ? cur_token.value.toString() : "fin de archivo";
    int linea = cur_token.left;
    int columna = cur_token.right;

    // Ahora puedes usar tu función notifyError o simplemente imprimir el mensaje.
    String mensaje = "Error de sintaxis en la línea " + linea + ", columna " + columna + ". ";
    mensaje += "Se encontró un token inesperado: '" + valorErroneo + "'.";

    // notifyError(mensaje);
    System.err.println(mensaje);
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
  // #line 515 "Parser.java"
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
        // #line 23 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Programa.");
        }
          break;
        case 6:
        // #line 39 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Declaración de variable.");
        }
          break;
        case 9:
        // #line 48 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(applySynchronizationFormat(val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 10:
        // #line 50 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(val_peek(4).sval + " no es tipo válido y " + val_peek(2).sval + " no es una variable válida.");
        }
          break;
        case 11:
        // #line 52 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(applySynchronizationFormat(val_peek(3).sval, val_peek(2).sval));
        }
          break;
        case 16:
        // #line 78 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 19:
        // #line 87 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 21:
        // #line 96 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La expresión lambda requiere de un parámetro.");
        }
          break;
        case 25:
        // #line 112 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El cuerpo de la sentencia no puede estar vacío.");
        }
          break;
        case 26:
        // #line 114 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Debe especificarse un cuerpo para la sentencia.");
        }
          break;
        case 30:
        // #line 126 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Toda sentencia ejecutable debe terminar con punto y coma.");
        }
          break;
        case 38:
        // #line 139 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Asignación simple.");
        }
          break;
        case 40:
        // #line 145 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Setencia WHILE.");
        }
          break;
        case 42:
        // #line 153 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La condición no puede estar vacía.");
        }
          break;
        case 49:
        // #line 166 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El token leído no corresponde a un operador de comparación válido. Este se descartará.");
          descartarTokenError();
        }
          break;
        case 52:
        // #line 180 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La sentencia IF debe finalizarse con 'endif'.");
        }
          break;
        case 53:
        // #line 184 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Setencia IF.");
        }
          break;
        case 54:
        // #line 186 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Setencia IF-ELSE.");
        }
          break;
        case 57:
        // #line 197 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Impresión de cadena.");
        }
          break;
        case 58:
        // #line 199 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Impresión de expresión.");
        }
          break;
        case 69:
        // #line 243 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Operando no válido.");
        }
          break;
        case 74:
        // #line 261 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Declaración de función.");
        }
          break;
        case 76:
        // #line 269 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El cuerpo de la función no puede estar vacío.");
        }
          break;
        case 79:
        // #line 279 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Toda función debe recibir al menos un parámetro.");
        }
          break;
        case 82:
        // #line 288 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Se halló un parámetro formal vacío.");
        }
          break;
        case 88:
        // #line 304 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
          descartarTokenError();
        }
          break;
        case 89:
        // #line 311 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Invocación de función.");
        }
          break;
        // #line 778 "Parser.java"
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
