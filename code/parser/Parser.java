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

//#line 10 "gramatica.y"
package parser;

/* Importaciones.*/
import lexer.Lexer;
import common.Token;
import utilities.Printer;
//#line 32 "gramatica.y"
/*typedef union {
    String sval;
} YYSTYPE; */
//#line 28 "Parser.java"

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
  public final static short EOF = 0;
  public final static short UMINUS = 275;
  public final static short DUMMY = 275;
  public final static short UMINUS = 276;
  public final static short YYERRCODE = 256;
  final static short yylhs[] = { -1,
      0, 0, 0, 0, 11, 11, 11, 11, 12, 12,
      12, 14, 14, 14, 15, 15, 15, 15, 15, 15,
      15, 13, 13, 17, 17, 20, 20, 18, 18, 18,
      4, 4, 4, 21, 5, 5, 5, 7, 7, 22,
      23, 23, 25, 25, 24, 24, 26, 26, 16, 27,
      27, 27, 27, 27, 27, 27, 28, 28, 29, 29,
      34, 34, 34, 35, 35, 36, 36, 36, 36, 36,
      36, 36, 32, 32, 32, 32, 37, 37, 33, 33,
      38, 38, 38, 38, 38, 39, 31, 31, 40, 40,
      1, 1, 1, 1, 1, 41, 41, 2, 2, 42,
      42, 3, 3, 3, 6, 6, 19, 19, 44, 44,
      30, 43, 43, 45, 45, 45, 47, 47, 46, 46,
      46, 48, 48, 48, 8, 9, 9, 10, 10,
      0, 0, 0, 0, 13, 13, 13, 13, 14, 14,
      14, 16, 16, 16, 17, 17, 17, 17, 17, 17,
      17, 15, 15, 19, 19, 22, 22, 20, 20, 20,
      6, 6, 6, 23, 7, 7, 7, 24, 25, 25,
      27, 27, 26, 26, 28, 28, 18, 29, 29, 29,
      29, 29, 29, 29, 30, 30, 31, 31, 36, 36,
      36, 37, 37, 38, 38, 38, 38, 38, 38, 38,
      34, 34, 34, 34, 39, 39, 35, 35, 40, 40,
      40, 40, 40, 41, 33, 33, 42, 42, 1, 1,
      1, 1, 43, 43, 2, 2, 4, 4, 44, 44,
      3, 3, 3, 5, 5, 5, 9, 9, 8, 8,
      21, 21, 46, 46, 32, 45, 45, 47, 47, 47,
      49, 49, 48, 48, 48, 50, 50, 50, 10, 11,
      11, 12, 12,
  };
  final static short yylen[] = { 2,
      3, 3, 1, 1, 3, 2, 0, 3, 1, 2,
      3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 2, 2, 0, 1, 3, 3, 4,
      1, 3, 2, 3, 1, 3, 2, 1, 2, 7,
      2, 0, 1, 1, 3, 2, 1, 2, 2, 1,
      1, 1, 1, 1, 1, 1, 3, 3, 1, 1,
      3, 2, 2, 3, 1, 1, 1, 1, 1, 1,
      1, 1, 5, 5, 4, 2, 0, 2, 2, 2,
      2, 1, 2, 3, 2, 2, 4, 3, 1, 1,
      3, 1, 2, 3, 2, 1, 1, 3, 1, 1,
      1, 1, 1, 1, 1, 3, 8, 7, 1, 0,
      4, 1, 0, 1, 3, 1, 2, 2, 3, 2,
      2, 0, 1, 1, 4, 1, 3, 3, 1,
      1, 1, 1, 1, 2, 0, 1, 3, 3, 4,
      1, 3, 2, 3, 1, 3, 2, 7, 2, 0,
      1, 1, 3, 2, 1, 2, 2, 1, 1, 1,
      1, 1, 1, 1, 3, 3, 1, 1, 3, 2,
      2, 3, 1, 1, 1, 1, 1, 1, 1, 1,
      5, 5, 4, 2, 0, 2, 2, 2, 2, 1,
      2, 3, 2, 2, 4, 3, 1, 1, 3, 1,
      3, 2, 1, 1, 3, 1, 3, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1, 2, 1, 3,
      8, 7, 1, 0, 4, 1, 0, 1, 3, 1,
      2, 2, 3, 2, 2, 0, 1, 1, 4, 1,
      3, 3, 1,
  };
  final static short yydefred[] = { 0,
      4, 0, 0, 0, 3, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 50, 0, 0, 9, 22, 23,
      0, 0, 52, 56, 0, 51, 53, 54, 55, 59,
      60, 0, 6, 0, 0, 0, 0, 0, 0, 38,
      0, 0, 0, 0, 99, 102, 103, 104, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 43, 44, 0,
      79, 82, 0, 0, 0, 33, 0, 0, 0, 0,
      1, 0, 2, 10, 24, 27, 25, 49, 8, 5,
      0, 0, 126, 106, 89, 88, 0, 0, 0, 39,
      63, 0, 93, 68, 70, 69, 71, 96, 0, 72,
      66, 67, 0, 0, 0, 101, 100, 0, 0, 62,
      29, 0, 124, 123, 0, 0, 0, 114, 116, 0,
      0, 28, 85, 86, 46, 47, 0, 0, 83, 81,
      0, 41, 0, 32, 0, 35, 0, 0, 15, 18,
      16, 17, 19, 21, 20, 13, 12, 11, 14, 0,
      0, 125, 87, 0, 0, 61, 0, 94, 0, 98,
      0, 0, 118, 0, 0, 0, 0, 121, 30, 45,
      48, 84, 111, 0, 0, 37, 128, 127, 78, 74,
      73, 0, 0, 115, 119, 0, 36, 0, 0, 0,
      0, 0, 108, 40, 107,
      0, 0, 0, 0, 48, 0, 0, 9, 22, 23,
      24, 0, 50, 54, 0, 49, 51, 52, 53, 57,
      58, 0, 6, 0, 0, 0, 0, 0, 0, 107,
      0, 0, 0, 0, 96, 101, 102, 103, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 41, 42, 0,
      77, 80, 0, 0, 0, 33, 0, 0, 0, 0,
      1, 0, 2, 10, 27, 25, 47, 8, 5, 0,
      0, 130, 110, 87, 86, 0, 0, 0, 108, 61,
      0, 105, 66, 68, 67, 69, 93, 94, 70, 64,
      65, 0, 98, 104, 106, 0, 0, 100, 99, 0,
      0, 60, 29, 0, 128, 127, 0, 0, 0, 118,
      120, 0, 0, 28, 83, 84, 44, 45, 0, 0,
      81, 79, 0, 39, 0, 32, 0, 35, 0, 0,
      15, 18, 16, 17, 19, 21, 20, 13, 12, 11,
      14, 0, 0, 129, 85, 0, 0, 59, 0, 0,
      91, 0, 95, 0, 0, 122, 0, 0, 0, 0,
      125, 30, 43, 46, 82, 115, 0, 0, 37, 132,
      131, 76, 72, 97, 71, 0, 0, 119, 123, 0,
      36, 0, 0, 0, 0, 0, 112, 38, 111,
  };
  final static short yydgoto[] = { 4,
      43, 44, 45, 13, 135, 46, 47, 48, 82, 83,
      5, 189, 18, 148, 149, 19, 20, 21, 22, 77,
      23, 24, 65, 59, 60, 127, 25, 26, 27, 28,
      29, 30, 31, 124, 50, 104, 155, 61, 62, 88,
      105, 108, 116, 190, 117, 118, 119, 120,
      43, 44, 45, 102, 103, 13, 137, 46, 47, 48,
      81, 82, 5, 193, 18, 150, 151, 19, 20, 21,
      22, 76, 23, 24, 65, 59, 60, 129, 25, 26,
      27, 28, 29, 30, 31, 126, 50, 106, 157, 61,
      62, 87, 107, 110, 118, 194, 119, 120, 121, 122,
  };
final static short yysindex[] = {                       -91,
    0,   36,  -40,    0,    0,  -15,   -7,  -35,   44,  -28,
    6, -222,  -10, -220,    0,   50,   13,    0,    0,    0,
    3,    9,    0,    0,   14,    0,    0,    0,    0,    0,
    0,  -68,    0,   25,  160, -177,    4,  -24,  -15,    0,
 -175,   82,  141,   16,    0,    0,    0,    0,  -24,   45,
   30,   51,  -16,   88, -183,   19,   60,    0,    0,  -38,
    0,    0,  160, -165,   52,    0, -163,   -2,  160,  160,
    0,  149,    0,    0,    0,    0,    0,    0,    0,    0,
   -5,  -20,    0,    0,    0,    0,  151,   55, -171,    0,
    0,   57,    0,    0,    0,    0,    0,    0, -175,    0,
    0,    0,   16,  160,  154,    0,    0,  160, -171,    0,
    0,  -16,    0,    0, -242,   61,   59,    0,    0, -235,
   42,    0,    0,    0,    0,    0,   64,   32,    0,    0,
   67,    0,  -18,    0,  -27,    0,  151,  151,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -151,
  160,    0,    0,  -24, -162,    0,  151,    0,   16,    0,
 -160,   72,    0,   -9, -242,   69, -140,    0,    0,    0,
    0,    0,    0,   78,   -2,    0,    0,    0,    0,    0,
    0,   -4,   76,    0,    0,  160,    0,   76,   48,   -3,
   79,   -1,    0,    0,    0,
  final static short yysindex[] = { -114,
      0, 36, -40, 0, 0, 17, -14, -35, 44, -28,
      6, -246, -10, -231, 0, 47, 13, 0, 0, 0,
      0, -3, 0, 0, 7, 0, 0, 0, 0, 0,
      0, -71, 0, 25, 123, -190, 3, -24, 17, 0,
      -184, 82, 152, 26, 0, 0, 0, 0, -24, 50,
      34, 52, -16, 114, -179, 19, 60, 0, 0, -38,
      0, 0, 123, -161, 56, 0, -156, -5, 123, 123,
      0, 159, 0, 0, 0, 0, 0, 0, 0, -6,
      37, 0, 0, 0, 0, 87, 62, -163, 0, 0,
      65, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 26, 0, 0, 0, 123, 116, 0, 0, 123,
      -163, 0, 0, -16, 0, 0, -233, 67, 68, 0,
      0, -227, 51, 0, 0, 0, 0, 0, 64, 32,
      0, 0, 66, 0, -9, 0, -27, 0, 87, 87,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, -144, 123, 0, 0, -24, -154, 0, 123, 87,
      0, 26, 0, -152, 77, 0, -4, -233, 74, -136,
      0, 0, 0, 0, 0, 0, 85, -5, 0, 0,
      0, 0, 0, 0, 0, 5, 76, 0, 0, 123,
      0, 76, 48, 4, 90, 9, 0, 0, 0,
  };
final static short yyrindex[] = {                       121,
    0,  121,    0,    0,    0,    8,    0,    0,    0,    0,
    0,   84,    0,    0,    0,    0,    0,    0,    0,    0,
  final static short yyrindex[] = { 133,
      0, 133, 0, 0, 0, 8, 0, 0, 0, 0,
      0, 99, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 9, 95, 0,
      0, 0, 87, 108, 0, 0, 0, 0, 0, 0,
      0, 129, -21, 0, 70, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 92, 94, 0,
      0, 0, 111, 103, 0, 0, 0, 0, 0, 0,
      0, 122, -21, 0, 98, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 38,
      0, 0, 0, 0, 0, 119, 0, -100, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      26, 0, 0, 0, 0, 0, 89, 0, -138, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -42, 0,
      0, 0, 119, 0, 0, 0, 0, 0, -44, 0,
      0, -21, 0, 0, -231, 0, 92, 0, 0, 0,
      0, 131, 0, 0, 0, 0, 0, 0, 0, 0,
      -44, 0, 0, -21, 0, 0, -225, 0, 129, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 75, 0, 86, 99, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 100, 0, 131, 0,
      3, 0, 0, 0, -33, 37, 38, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 18, 0, 0, 0, 0, 18, 21, 0,
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 112, 0, 118, 120,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 137,
      0, 143, 0, 124, 0, 0, 0, -33, 45, 46,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 57, 0, 0, 0,
      0, 57, 69, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      394, 362, -99, 139, 0, 422, -64, 424, 0, 10,
      175, 58, 20, 0, 0, 17, 0, 0, 0, 0,
      0, 0, 0, 49, -19, 0, 0, 0, 0, 0,
      0, 0, 0, 15, 145, 0, 74, 0, -13, 0,
      0, 0, 83, 12, 0, -109, 0, 0,
      -8, 89, -88, 0, 0, 189, 0, 417, -54, 420,
      0, 53, 197, 33, -13, 0, 0, -7, 0, 0,
      0, 0, 0, 0, 0, 72, 11, 0, 0, 0,
      0, 0, 0, 0, 0, -2, 158, 0, 97, 0,
      20, 0, 0, 107, 96, 23, 0, -98, 0, 0,
  };
  final static int YYTABLESIZE = 613;
  final static int YYTABLESIZE = 613;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 12,
        26, 42, 97, 136, 42, 163, 41, 117, 160, 41,
        117, 12, 73, 113, 77, 12, 175, 41, 89, 113,
        152, 166, 49, 151, 35, 122, 58, 115, 114, 109,
        36, 3, 37, 67, 167, 69, 74, 98, 122, 99,
        26, 123, 41, 70, 86, 63, 130, 64, 41, 71,
        68, 31, 12, 74, 58, 184, 79, 106, 42, 17,
        34, 75, 107, 41, 12, 58, 129, 76, 31, 129,
        176, 42, 78, 126, 129, 12, 41, 105, 120, 84,
        105, 120, 90, 53, 33, 110, 191, 12, 111, 56,
        112, 132, 133, 134, 57, 153, 154, 156, 57, 12,
        169, 164, 165, 12, 57, 177, 180, 173, 181, 98,
        187, 99, 182, 183, 36, 12, 166, 186, 188, 194,
        7, 193, 91, 195, 42, 26, 41, 65, 80, 90,
        77, 67, 112, 34, 179, 105, 105, 105, 105, 105,
        64, 105, 110, 171, 58, 109, 122, 54, 92, 80,
        92, 92, 92, 105, 105, 105, 105, 57, 3, 95,
        178, 95, 95, 95, 1, 2, 92, 92, 92, 92,
        58, 91, 31, 91, 91, 91, 16, 95, 95, 95,
        95, 174, 161, 98, 125, 99, 92, 31, 170, 91,
        91, 91, 91, 98, 162, 99, 0, 0, 41, 192,
        102, 100, 101, 0, 41, 0, 0, 147, 74, 0,
        0, 0, 0, 97, 97, 32, 6, 0, 39, 40,
        38, 39, 40, 122, 77, 7, 8, 55, 6, 9,
        40, 10, 6, 11, 128, 122, 122, 7, 8, 113,
        0, 7, 8, 10, 56, 11, 66, 10, 122, 11,
        93, 39, 40, 0, 114, 40, 26, 26, 0, 150,
        39, 40, 85, 105, 31, 0, 26, 26, 72, 6,
        26, 105, 26, 146, 26, 39, 40, 0, 7, 8,
        72, 6, 9, 0, 10, 0, 11, 172, 39, 40,
        7, 8, 6, 0, 9, 0, 10, 0, 11, 51,
        52, 7, 8, 72, 6, 9, 0, 10, 0, 11,
        0, 0, 0, 7, 8, 0, 6, 9, 0, 10,
        6, 11, 93, 39, 40, 7, 8, 0, 0, 7,
        8, 10, 6, 11, 0, 10, 0, 11, 39, 40,
        0, 7, 8, 121, 66, 9, 0, 10, 0, 11,
        105, 105, 105, 0, 105, 105, 105, 105, 0, 105,
        0, 0, 0, 92, 92, 92, 0, 92, 92, 92,
        92, 0, 92, 0, 95, 95, 95, 0, 95, 95,
        95, 95, 0, 95, 31, 31, 91, 91, 91, 0,
        91, 91, 91, 91, 0, 91, 93, 39, 40, 0,
        94, 95, 96, 97, 103, 139, 93, 39, 40, 158,
        39, 40, 0, 0, 140, 141, 39, 40, 142, 0,
        143, 144, 145, 14, 14, 15, 15, 0, 81, 0,
        87, 14, 0, 15, 0, 0, 0, 0, 14, 0,
        15, 0, 103, 0, 0, 0, 0, 0, 103, 0,
        0, 0, 0, 0, 0, 14, 131, 15, 0, 14,
        0, 15, 137, 138, 0, 0, 159, 0, 0, 0,
        14, 0, 15, 0, 0, 0, 0, 0, 14, 0,
        15, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 103, 0, 0, 0, 0, 157, 103, 103,
        26, 42, 58, 74, 42, 49, 41, 121, 3, 41,
        121, 12, 73, 138, 75, 12, 178, 41, 166, 117,
        74, 163, 115, 64, 69, 37, 80, 117, 86, 169,
        58, 126, 70, 67, 17, 34, 97, 116, 98, 41,
        26, 58, 170, 85, 126, 63, 71, 41, 88, 128,
        68, 31, 12, 78, 133, 75, 35, 131, 42, 111,
        139, 140, 36, 41, 12, 77, 83, 108, 31, 188,
        184, 42, 109, 89, 125, 12, 41, 154, 133, 132,
        153, 133, 179, 53, 33, 109, 124, 12, 109, 124,
        112, 114, 113, 56, 57, 134, 135, 160, 57, 12,
        136, 195, 155, 12, 156, 158, 176, 167, 97, 172,
        98, 168, 180, 57, 183, 12, 185, 186, 187, 36,
        169, 174, 90, 191, 190, 26, 41, 192, 197, 97,
        198, 98, 7, 199, 109, 109, 109, 109, 109, 40,
        109, 1, 2, 90, 80, 90, 90, 90, 58, 79,
        74, 63, 109, 109, 109, 109, 78, 67, 3, 88,
        41, 90, 90, 90, 90, 31, 182, 41, 75, 116,
        34, 92, 124, 92, 92, 92, 56, 62, 55, 74,
        31, 114, 73, 89, 127, 89, 89, 89, 173, 92,
        92, 92, 92, 113, 97, 162, 98, 54, 16, 91,
        0, 89, 89, 89, 89, 181, 177, 164, 159, 165,
        0, 101, 99, 100, 196, 32, 6, 149, 39, 40,
        38, 39, 40, 126, 75, 7, 8, 55, 6, 9,
        40, 10, 6, 11, 130, 126, 126, 7, 8, 115,
        0, 7, 8, 10, 56, 11, 66, 10, 126, 11,
        39, 92, 40, 0, 116, 0, 26, 26, 152, 39,
        40, 84, 0, 109, 31, 0, 26, 26, 72, 6,
        26, 109, 26, 0, 26, 39, 40, 0, 7, 8,
        72, 6, 9, 148, 10, 0, 11, 175, 39, 40,
        7, 8, 6, 0, 9, 0, 10, 0, 11, 51,
        52, 7, 8, 72, 6, 9, 0, 10, 0, 11,
        0, 0, 0, 7, 8, 0, 6, 9, 0, 10,
        6, 11, 39, 92, 0, 7, 8, 0, 0, 7,
        8, 10, 6, 11, 0, 10, 0, 11, 39, 40,
        0, 7, 8, 39, 92, 9, 0, 10, 0, 11,
        109, 109, 0, 109, 109, 109, 109, 0, 109, 90,
        90, 0, 90, 90, 90, 90, 0, 90, 0, 123,
        66, 161, 39, 40, 0, 0, 0, 31, 31, 39,
        40, 0, 0, 0, 0, 0, 0, 92, 92, 0,
        92, 92, 92, 92, 0, 92, 0, 0, 0, 89,
        89, 0, 89, 89, 89, 89, 0, 89, 39, 92,
        0, 93, 94, 95, 96, 141, 0, 0, 14, 14,
        0, 15, 15, 0, 142, 143, 14, 0, 144, 15,
        145, 146, 147, 14, 0, 0, 15, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 103, 0,
        14, 0, 0, 15, 14, 0, 0, 15, 0, 104,
        0, 0, 105, 0, 0, 14, 0, 0, 15, 0,
        0, 0, 0, 14, 0, 0, 15, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 104, 0, 0, 105,
        0, 0, 104, 0, 0, 105, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 168, 0, 0, 81, 0, 0, 0, 14, 0,
        15, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 14, 0, 15, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 185, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 14, 0, 15, 0, 0, 14,
        14, 15, 15,
        0, 0, 0, 0, 0, 0, 0, 0, 171, 0,
        0, 0, 0, 0, 0, 14, 0, 0, 15, 104,
        0, 0, 105, 0, 0, 104, 104, 0, 105, 105,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 14, 0, 0, 15, 104, 0, 0, 105,
        0, 0, 0, 0, 0, 0, 189, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 14, 0, 0, 15, 0, 14, 14,
        0, 15, 15,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 40,
        0, 40, 45, 68, 40, 115, 45, 41, 108, 45,
        44, 40, 0, 256, 59, 40, 44, 45, 38, 41,
        41, 257, 8, 44, 40, 257, 10, 44, 271, 49,
        46, 123, 40, 44, 270, 256, 17, 43, 270, 45,
        40, 55, 45, 264, 41, 40, 60, 270, 45, 0,
        61, 44, 40, 34, 38, 165, 125, 42, 40, 2,
        3, 59, 47, 45, 40, 49, 41, 59, 61, 44,
        135, 40, 59, 57, 60, 40, 45, 41, 41, 257,
        44, 44, 258, 40, 125, 41, 186, 40, 59, 273,
        40, 257, 41, 257, 123, 41, 268, 41, 123, 40,
        59, 41, 44, 40, 123, 257, 269, 41, 269, 43,
        175, 45, 41, 123, 46, 40, 257, 40, 123, 41,
        0, 125, 41, 125, 41, 125, 45, 41, 59, 41,
        269, 44, 41, 59, 154, 41, 42, 43, 44, 45,
        41, 47, 125, 127, 59, 125, 59, 9, 41, 125,
        43, 44, 45, 59, 60, 61, 62, 59, 123, 41,
        151, 43, 44, 45, 256, 257, 59, 60, 61, 62,
        154, 41, 44, 43, 44, 45, 2, 59, 60, 61,
        62, 133, 109, 43, 125, 45, 42, 59, 125, 59,
        60, 61, 62, 43, 112, 45, -1, -1, 45, 188,
        60, 61, 62, -1, 45, -1, -1, 59, 189, -1,
        -1, -1, -1, 256, 257, 256, 257, -1, 257, 258,
        256, 257, 258, 257, 269, 266, 267, 256, 257, 270,
        258, 272, 257, 274, 273, 257, 270, 266, 267, 256,
        -1, 266, 267, 272, 273, 274, 257, 272, 270, 274,
        256, 257, 258, -1, 271, 258, 256, 257, -1, 265,
        257, 258, 259, 256, 257, -1, 266, 267, 256, 257,
        270, 264, 272, 125, 274, 257, 258, -1, 266, 267,
        256, 257, 270, -1, 272, -1, 274, 256, 257, 258,
        266, 267, 257, -1, 270, -1, 272, -1, 274, 256,
        257, 266, 267, 256, 257, 270, -1, 272, -1, 274,
        -1, -1, -1, 266, 267, -1, 257, 270, -1, 272,
        257, 274, 256, 257, 258, 266, 267, -1, -1, 266,
        267, 272, 257, 274, -1, 272, -1, 274, 257, 258,
        -1, 266, 267, 256, 257, 270, -1, 272, -1, 274,
        256, 257, 258, -1, 260, 261, 262, 263, -1, 265,
        -1, -1, -1, 256, 257, 258, -1, 260, 261, 262,
        263, -1, 265, -1, 256, 257, 258, -1, 260, 261,
        262, 263, -1, 265, 256, 257, 256, 257, 258, -1,
        260, 261, 262, 263, -1, 265, 256, 257, 258, -1,
        260, 261, 262, 263, 43, 257, 256, 257, 258, 256,
        257, 258, -1, -1, 266, 267, 257, 258, 270, -1,
        272, 273, 274, 2, 3, 2, 3, -1, 35, -1,
        37, 10, -1, 10, -1, -1, -1, -1, 17, -1,
        17, -1, 81, -1, -1, -1, -1, -1, 87, -1,
        -1, -1, -1, -1, -1, 34, 63, 34, -1, 38,
        -1, 38, 69, 70, -1, -1, 105, -1, -1, -1,
        49, -1, 49, -1, -1, -1, -1, -1, 57, -1,
        57, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 131, -1, -1, -1, -1, 104, 137, 138,
        0, 40, 10, 17, 40, 8, 45, 41, 123, 45,
        44, 40, 0, 68, 59, 40, 44, 45, 117, 41,
        34, 110, 256, 270, 256, 40, 35, 44, 37, 257,
        38, 257, 264, 44, 2, 3, 43, 271, 45, 45,
        40, 49, 270, 41, 270, 40, 0, 45, 38, 57,
        61, 44, 40, 125, 63, 59, 40, 60, 40, 49,
        69, 70, 46, 45, 40, 59, 257, 42, 61, 168,
        159, 40, 47, 258, 55, 40, 45, 41, 41, 60,
        44, 44, 137, 40, 125, 41, 41, 40, 44, 44,
        41, 40, 59, 273, 123, 257, 41, 106, 123, 40,
        257, 190, 41, 40, 268, 41, 41, 41, 43, 59,
        45, 44, 257, 123, 269, 40, 269, 41, 123, 46,
        257, 129, 41, 178, 40, 125, 45, 123, 125, 43,
        41, 45, 0, 125, 41, 42, 43, 44, 45, 41,
        47, 256, 257, 41, 153, 43, 44, 45, 156, 125,
        59, 41, 59, 60, 61, 62, 59, 44, 123, 41,
        45, 59, 60, 61, 62, 44, 156, 45, 269, 41,
        59, 41, 59, 43, 44, 45, 59, 41, 59, 193,
        59, 125, 59, 41, 125, 43, 44, 45, 125, 59,
        60, 61, 62, 125, 43, 107, 45, 9, 2, 42,
        -1, 59, 60, 61, 62, 153, 135, 111, 102, 114,
        -1, 60, 61, 62, 192, 256, 257, 59, 257, 258,
        256, 257, 258, 257, 269, 266, 267, 256, 257, 270,
        258, 272, 257, 274, 273, 257, 270, 266, 267, 256,
        -1, 266, 267, 272, 273, 274, 257, 272, 270, 274,
        257, 258, 258, -1, 271, -1, 256, 257, 265, 257,
        258, 259, -1, 256, 257, -1, 266, 267, 256, 257,
        270, 264, 272, -1, 274, 257, 258, -1, 266, 267,
        256, 257, 270, 125, 272, -1, 274, 256, 257, 258,
        266, 267, 257, -1, 270, -1, 272, -1, 274, 256,
        257, 266, 267, 256, 257, 270, -1, 272, -1, 274,
        -1, -1, -1, 266, 267, -1, 257, 270, -1, 272,
        257, 274, 257, 258, -1, 266, 267, -1, -1, 266,
        267, 272, 257, 274, -1, 272, -1, 274, 257, 258,
        -1, 266, 267, 257, 258, 270, -1, 272, -1, 274,
        257, 258, -1, 260, 261, 262, 263, -1, 265, 257,
        258, -1, 260, 261, 262, 263, -1, 265, -1, 256,
        257, 256, 257, 258, -1, -1, -1, 256, 257, 257,
        258, -1, -1, -1, -1, -1, -1, 257, 258, -1,
        260, 261, 262, 263, -1, 265, -1, -1, -1, 257,
        258, -1, 260, 261, 262, 263, -1, 265, 257, 258,
        -1, 260, 261, 262, 263, 257, -1, -1, 2, 3,
        -1, 2, 3, -1, 266, 267, 10, -1, 270, 10,
        272, 273, 274, 17, -1, -1, 17, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, 157, -1,
        34, -1, -1, 34, 38, -1, -1, 38, -1, 43,
        -1, -1, 43, -1, -1, 49, -1, -1, 49, -1,
        -1, -1, -1, 57, -1, -1, 57, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, 80, -1, -1, 80,
        -1, -1, 86, -1, -1, 86, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, 120, -1, -1, 151, -1, -1, -1, 127, -1,
        127, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, 154, -1, 154, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, 167, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, 183, -1, 183, -1, -1, 188,
        189, 188, 189,
        -1, -1, -1, -1, -1, -1, -1, -1, 122, -1,
        -1, -1, -1, -1, -1, 129, -1, -1, 129, 133,
        -1, -1, 133, -1, -1, 139, 140, -1, 139, 140,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 156, -1, -1, 156, 160, -1, -1, 160,
        -1, -1, -1, -1, -1, -1, 170, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, 187, -1, -1, 187, -1, 192, 193,
        -1, 192, 193,
    };
  }

  final static short YYFINAL = 4;
  final static short YYMAXTOKEN = 275;
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
      "UMINUS",
      null, null, null, null, null, null, null, "ID", "CTE", "STR", "EQ", "GEQ", "LEQ", "NEQ",
      "DASIG", "FLECHA", "PRINT", "IF", "ELSE", "ENDIF", "UINT", "CVR", "DO", "WHILE", "RETURN",
      "DUMMY", "UMINUS",
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
      "sentencia_declarativa : declaracion_variable ';'",
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
      "constante : CTE",
      "constante : '-' CTE",
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
      "expresion : expresion error",
      "expresion : expresion operador_suma error",
      "expresion : expresion termino_simple",
      "operador_suma : '+'",
      "operador_suma : '-'",
      "termino : termino operador_multiplicacion factor",
      "termino : factor",
      "termino_simple : termino_simple operador_multiplicacion factor",
      "termino_simple : factor_simple",
      "operador_multiplicacion : '/'",
      "operador_multiplicacion : '*'",
      "factor : variable",
      "factor : constante",
      "factor : constante",
      "factor : invocacion_funcion",
      "factor_simple : variable",
      "factor_simple : CTE",
      "factor_simple : invocacion_funcion",
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

  // #line 663 "gramatica.y"
  // #line 674 "/home/iroumec/Documents/University/Compiladores e
  // Intérpretes/TPE-Compiler/code/parser/gramatica.y"

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
    // Se fuerza a que en la próxima iteración se llame a yylex(), leyendo otro
    // token.
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
        message));
  }

  // ------------------------------------------------------------------------------------------------------------------------------------------------------------

  void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage));
    this.warningsDetected++;
  }

  // ------------------------------------------------------------------------------------------------------------------------------------------------------------

  void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage));
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
  // #line 709 "Parser.java"
  // #line 716 "Parser.java"
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
        // #line 83 "gramatica.y"
        // #line 85 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Programa.");
        }
          break;
        case 2:
        // #line 88 "gramatica.y"
        // #line 90 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Las sentencias del programa deben estar delimitadas por llaves.");
        }
          break;
        case 3:
        // #line 90 "gramatica.y"
        // #line 92 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El programa requiere de un nombre.");
        }
          break;
        case 4:
        // #line 92 "gramatica.y"
        // #line 94 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
          descartarTokensHasta(ID); /* Se descartan todos los tokens hasta ID. */
        }
          break;
        case 6:
        // #line 106 "gramatica.y"
        // #line 108 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El programa no posee ninguna sentencia.");
        }
          break;
        case 7:
        // #line 108 "gramatica.y"
        // #line 110 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El programa no posee ningún cuerpo.");
        }
          break;
        case 8:
        // #line 110 "gramatica.y"
        // #line 112 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Sentencia inválida en el lenguaje.");
        }
          break;
        case 11:
        // #line 122 "gramatica.y"
        // #line 124 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Sentencia inválida en el lenguaje.");
        }
          break;
        case 24:
        // #line 159 "gramatica.y"
        // #line 161 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Declaración de variable.");
        }
          break;
        case 29:
        // #line 180 "gramatica.y"
        // #line 182 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Error de sintaxis en la lista de variables. La declaración se ha descartado hasta el ';'.");
        }
          break;
        case 30:
        // #line 182 "gramatica.y"
        // #line 184 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(
              "Error de sintaxis al final de la lista de variables. La declaración se ha descartado hasta el ';'.");
        }
          break;
        case 32:
        // #line 190 "gramatica.y"
        // #line 192 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 33:
        // #line 201 "gramatica.y"
        // #line 203 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos variables juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 34:
        // #line 220 "gramatica.y"
        // #line 222 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 36:
        // #line 228 "gramatica.y"
        // #line 230 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 37:
        // #line 233 "gramatica.y"
        // #line 235 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos constantes juntas sin una coma de separación. Sugerencia: Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 39:
        // #line 243 "gramatica.y"
        {
          yyval.sval = '-' + val_peek(0).sval;
        }
        case 38:
        // #line 249 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 40:
        // #line 253 "gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 42:
        // #line 265 "gramatica.y"
        // #line 261 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La expresión lambda requiere de un parámetro.");
        }
          break;
        case 46:
          // #line 285 "gramatica.y"
        case 44:
        // #line 281 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El cuerpo de la sentencia no puede estar vacío.");
        }
          break;
        case 57:
          // #line 320 "gramatica.y"
        case 55:
        // #line 316 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Asignación simple.");
        }
          break;
        case 58:
          // #line 325 "gramatica.y"
        case 56:
        // #line 321 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Error en asignación. Se esperaba un ':='.");
        }
          break;
        case 62:
          // #line 352 "gramatica.y"
        case 60:
        // #line 348 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta apertura de paréntesis en condición.");
        }
          break;
        case 63:
          // #line 354 "gramatica.y"
        case 61:
        // #line 350 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La condición no puede estar vacía.");
        }
          break;
        case 65:
          // #line 365 "gramatica.y"
        case 63:
        // #line 361 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de comparador en comparación.");
        }
          break;
        case 72:
          // #line 381 "gramatica.y"
        case 70:
        // #line 377 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?");
        }
          break;
        case 73:
          // #line 391 "gramatica.y"
        case 71:
        // #line 387 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Sentencia IF.");
        }
          break;
        case 74:
          // #line 396 "gramatica.y"
        case 72:
        // #line 392 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Sentencia IF inválida en el lenguaje. Falta cierre de paréntesis en condición.");
        }
          break;
        case 75:
          // #line 401 "gramatica.y"
        case 73:
        // #line 397 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La sentencia IF debe finalizarse con 'endif'.");
        }
          break;
        case 76:
          // #line 403 "gramatica.y"
        case 74:
        // #line 399 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Sentencia IF inválida en el lenguaje.");
        }
          break;
        case 80:
          // #line 423 "gramatica.y"
        case 78:
        // #line 419 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Sentencia DO-WHILE inválida.");
        }
          break;
        case 81:
          // #line 431 "gramatica.y"
        case 79:
        // #line 427 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Sentencia DO-WHILE.");
        }
          break;
        case 82:
          // #line 436 "gramatica.y"
        case 80:
        // #line 432 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Debe especificarse un cuerpo para la sentencia do-while.");
        }
          break;
        case 83:
          // #line 438 "gramatica.y"
        case 81:
        // #line 434 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta 'while'.");
        }
          break;
        case 84:
          // #line 443 "gramatica.y"
        case 82:
        // #line 439 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta cierre de paréntesis en condición.");
        }
          break;
        case 85:
          // #line 445 "gramatica.y"
        case 83:
        // #line 441 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Ha habido un error en el cierre del cuerpo ejecutable.");
        }
          break;
        case 88:
          // #line 459 "gramatica.y"
        case 86:
        // #line 455 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La sentencia 'print' requiere de al menos un argumento.");
        }
          break;
        case 89:
          // #line 466 "gramatica.y"
        case 87:
        // #line 462 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Impresión de cadena.");
        }
          break;
        case 90:
          // #line 468 "gramatica.y"
        case 88:
        // #line 464 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Impresión de expresión.");
        }
          break;
        case 93:
        // #line 483 "gramatica.y"
        {
          notifyError("punto y coma");
        }
          break;
        case 94:
          // #line 487 "gramatica.y"
        case 91:
        // #line 478 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de operando en expresión.");
          /* $$ no se asigna o se le da un valor por defecto */
        }
          break;
        case 95:
          // #line 494 "gramatica.y"
        case 92:
        // #line 483 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError(String.format(
              "Falta de operador entre operandos %s y %s.",
              val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 99:
          // #line 515 "gramatica.y"
        case 96:
        // #line 503 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 98:
        // #line 513 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 102:
        // #line 531 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
        case 108:
        // #line 546 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = "-" + val_peek(0).sval;
        }
          break;
        case 103:
        // #line 533 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 104:
        // #line 535 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 106:
          // #line 543 "gramatica.y"
        case 110:
        // #line 554 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(2).sval + "." + val_peek(0).sval;
        }
          break;
        case 107:
          // #line 552 "gramatica.y"
        case 111:
        // #line 563 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Declaración de función.");
        }
          break;
        case 108:
          // #line 554 "gramatica.y"
        case 112:
        // #line 565 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de nombre en la función.");
        }
          break;
        case 110:
          // #line 565 "gramatica.y"
        case 114:
        // #line 576 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El cuerpo de la función no puede estar vacío.");
        }
          break;
        case 113:
          // #line 582 "gramatica.y"
        case 117:
        // #line 593 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Toda función debe recibir al menos un parámetro.");
        }
          break;
        case 116:
          // #line 594 "gramatica.y"
        case 120:
        // #line 605 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Se halló un parámetro formal vacío.");
        }
          break;
        case 120:
          // #line 609 "gramatica.y"
        case 124:
        // #line 620 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de nombre de parámetro formal en declaración de función.");
        }
          break;
        case 121:
          // #line 611 "gramatica.y"
        case 125:
        // #line 622 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de tipo de parámetro formal en declaración de funcion.");
        }
          break;
        case 124:
          // #line 620 "gramatica.y"
        case 128:
        // #line 631 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Semántica de pasaje de parámetro inválida. Se asumirá pasaje de parámetro por defecto.");
          descartarTokenError();
        }
          break;
        case 125:
          // #line 632 "gramatica.y"
        case 129:
        // #line 643 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("Invocación de función.");
          yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
          break;
        case 127:
          // #line 643 "gramatica.y"
        case 131:
        // #line 654 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 128:
          // #line 650 "gramatica.y"
        case 132:
        // #line 661 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
        }
          break;
        case 129:
          // #line 655 "gramatica.y"
        case 133:
        // #line 666 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
        }
          break;
        // #line 1120 "Parser.java"
        // #line 1114 "Parser.java"
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
