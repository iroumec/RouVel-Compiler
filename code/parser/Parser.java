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

import lexer.Lexer;
import common.Token;
import common.SymbolTable;
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
  public final static short EOF = 0;
  public final static short YYERRCODE = 256;
  final static short yylhs[] = { -1,
      0, 0, 0, 19, 0, 0, 0, 18, 18, 15,
      16, 22, 16, 16, 16, 16, 21, 21, 20, 20,
      17, 17, 17, 23, 23, 25, 25, 28, 28, 29,
      29, 30, 30, 31, 31, 24, 24, 24, 24, 24,
      24, 24, 24, 34, 34, 26, 26, 26, 26, 26,
      26, 8, 8, 8, 8, 32, 32, 32, 32, 40,
      40, 40, 33, 33, 33, 33, 41, 42, 42, 9,
      9, 9, 1, 1, 1, 1, 6, 6, 2, 2,
      2, 4, 4, 4, 7, 7, 3, 3, 3, 5,
      5, 5, 11, 11, 10, 10, 43, 43, 43, 43,
      43, 44, 44, 45, 45, 45, 45, 45, 45, 45,
      38, 38, 38, 38, 38, 46, 46, 39, 39, 39,
      47, 48, 48, 48, 49, 27, 27, 52, 52, 51,
      50, 50, 53, 53, 53, 55, 55, 54, 54, 54,
      56, 56, 56, 35, 35, 35, 35, 35, 12, 13,
      13, 14, 14, 36, 36, 58, 58, 58, 58, 57,
      59, 59, 37, 37, 37, 37, 37, 62, 63, 62,
      62, 61, 60,
  };
  final static short yylen[] = { 2,
      2, 2, 2, 0, 2, 2, 2, 1, 1, 3,
      3, 0, 4, 2, 0, 3, 2, 2, 2, 2,
      1, 2, 2, 1, 1, 1, 2, 0, 1, 1,
      1, 3, 2, 1, 2, 2, 1, 1, 1, 1,
      1, 1, 2, 1, 1, 3, 3, 3, 3, 2,
      5, 3, 3, 2, 2, 4, 3, 4, 3, 2,
      4, 4, 2, 4, 2, 4, 3, 5, 1, 1,
      3, 2, 1, 3, 3, 2, 1, 1, 3, 1,
      3, 3, 1, 3, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 2, 1, 3, 3, 2, 2, 2,
      3, 3, 1, 1, 1, 1, 1, 1, 1, 1,
      6, 6, 5, 5, 2, 0, 2, 3, 3, 2,
      2, 1, 1, 2, 2, 6, 5, 1, 2, 3,
      1, 0, 1, 3, 1, 2, 2, 3, 2, 2,
      0, 1, 1, 5, 5, 4, 3, 2, 4, 1,
      3, 3, 1, 3, 3, 1, 2, 1, 0, 3,
      1, 1, 4, 4, 5, 4, 5, 1, 0, 3,
      0, 3, 4,
  };
  final static short yydefred[] = { 0,
      0, 0, 0, 0, 6, 7, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 1, 2, 0,
      0, 21, 24, 25, 26, 0, 37, 38, 39, 40,
      41, 42, 44, 45, 0, 0, 8, 9, 5, 23,
      0, 0, 93, 161, 0, 0, 0, 0, 80, 87,
      88, 89, 0, 0, 0, 115, 0, 0, 0, 0,
      50, 0, 0, 0, 0, 120, 0, 0, 30, 0,
      31, 0, 0, 0, 148, 0, 0, 0, 17, 14,
      0, 0, 0, 0, 0, 69, 0, 0, 37, 36,
      22, 18, 0, 29, 27, 65, 63, 0, 0, 34,
      0, 0, 96, 0, 0, 150, 94, 0, 0, 91,
      77, 78, 0, 83, 0, 90, 92, 85, 86, 0,
      154, 155, 99, 0, 106, 108, 107, 109, 110, 104,
      105, 0, 0, 100, 98, 48, 55, 47, 0, 0,
      143, 142, 0, 0, 0, 133, 135, 0, 49, 54,
      46, 0, 0, 125, 33, 0, 0, 121, 118, 119,
      0, 0, 147, 0, 0, 0, 0, 0, 0, 0,
      0, 57, 0, 0, 59, 67, 0, 0, 70, 0,
      0, 0, 0, 0, 0, 35, 0, 0, 0, 0,
      149, 160, 0, 75, 0, 81, 79, 101, 97, 0,
      0, 0, 52, 0, 137, 0, 0, 0, 140, 53,
      0, 32, 146, 0, 19, 20, 173, 58, 56, 0,
      0, 60, 0, 13, 66, 64, 0, 72, 0, 0,
      0, 163, 164, 0, 166, 152, 151, 84, 82, 117,
      114, 0, 113, 0, 0, 128, 127, 134, 138, 51,
      145, 144, 0, 0, 0, 71, 165, 0, 172, 167,
      112, 111, 0, 126, 129, 0, 61, 62, 68, 130,
  };
  final static short yydgoto[] = { 3,
      58, 48, 49, 113, 114, 115, 120, 64, 178, 50,
      51, 52, 105, 106, 18, 19, 266, 39, 4, 166,
      21, 93, 22, 23, 24, 25, 26, 95, 70, 71,
      102, 27, 28, 29, 30, 31, 32, 33, 34, 172,
      35, 88, 59, 60, 132, 202, 72, 73, 74, 144,
      246, 247, 145, 146, 147, 148, 53, 54, 55, 36,
      187, 183, 258,
  };
  final static short yysindex[] = { -175,
      2, -24, 0, -80, 0, 0, -65, 19, 80, 107,
      40, -12, 261, -36, -204, 248, -49, 0, 0, 111,
      -55, 0, 0, 0, 0, 27, 0, 0, 0, 0,
      0, 0, 0, 0, -39, 129, 0, 0, 0, 0,
      -182, 287, 0, 0, -158, 94, 208, -7, 0, 0,
      0, 0, 46, -147, 0, 0, 289, 357, 142, -9,
      0, 282, 10, 369, -150, 0, 6, 155, 0, 63,
      0, 62, -133, 0, 0, 299, 147, 21, 0, 0,
      45, -121, 287, 287, -113, 0, 316, -1, 0, 0,
      0, 0, 57, 0, 0, 0, 0, -1, 155, 0,
      105, 158, 0, 132, 50, 0, 0, 0, 109, 0,
      0, 0, -7, 0, 304, 0, 0, 0, 0, 326,
      0, 0, 0, 16, 0, 0, 0, 0, 0, 0,
      0, 287, -120, 0, 0, 0, 0, 0, -104, 10,
      0, 0, -208, 114, 112, 0, 0, -236, 0, 0,
      0, -100, -1, 0, 0, 169, 0, 0, 0, 0,
      102, 196, 0, 0, 37, 38, 123, 339, 348, -7,
      -91, 0, 120, -8, 0, 0, 76, 335, 0, 183,
      328, 115, -85, 105, -30, 0, 0, -78, -81, 287,
      0, 0, -115, 0, -7, 0, 0, 0, 0, 208,
      142, 259, 0, 143, 0, 66, -208, -113, 0, 0,
      124, 0, 0, -42, 0, 0, 0, 0, 0, 304,
      326, 0, -1, 0, 0, 0, -1, 0, -71, 0,
      145, 0, 0, -60, 0, 0, 0, 0, 0, 0,
      0, 31, 0, 74, 33, 0, 0, 0, 0, 0,
      0, 0, 20, -57, 156, 0, 0, 0, 0, 0,
      0, 0, 57, 0, 0, 88, 0, 0, 0, 0,
  };
  final static short yyrindex[] = { 8,
      0, 203, 0, 203, 0, 0, 0, 417, -32, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 210,
      100, 0, 0, 0, 0, 1, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 54, 427, 0, 0,
      0, 0, 29, 0, 39, 0, 0, 60, 0, 0,
      0, 34, -41, 0, 0, 0, 0, 0, 0, 0,
      0, -147, 0, 48, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      79, 79, 0, 78, 0, 0, 0, 131, 0, 0,
      0, 0, 439, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 266, 0, 0, 0, 0, 0, 0, -41,
      0, 0, -228, 0, 174, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 136, 0, 0, 0,
      0, 0, 0, 12, 376, 406, 0, 0, 0, 388,
      478, 0, -11, 0, 0, 0, 0, 0, 0, 79,
      0, 154, 0, 79, 0, 0, 154, 0, 0, 0,
      0, 0, 0, 0, 449, 0, 0, 0, 0, 91,
      0, 0, 0, 0, 0, 0, -35, 89, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 164,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 471, 484, 0, 0, 0, 167, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      586, -66, -13, 0, 195, 242, -94, 0, 0, 11,
      354, 25, 0, 244, 433, 434, 24, 0, 0, 0,
      0, 0, -17, 542, 0, 0, 0, 0, -52, 403,
      -44, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 270, 7, 389, 0, 0, 0, 0, 375, 308,
      207, 0, 0, -129, 0, 0, 0, 0, 410, 0,
      362, -46, 0,
  };
  final static int YYTABLESIZE = 776;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 132,
        28, 6, 91, 15, 98, 136, 133, 4, 136, 90,
        230, 16, 16, 205, 45, 15, 252, 170, 193, 97,
        173, 65, 16, 156, 16, 20, 17, 15, 141, 95,
        16, 135, 95, 208, 119, 85, 17, 81, 17, 118,
        28, 141, 14, 45, 17, 57, 16, 141, 195, 95,
        45, 23, 86, 143, 180, 188, 199, 116, 42, 40,
        17, 119, 142, 91, 41, 82, 118, 92, 116, 16,
        171, 117, 15, 154, 103, 221, 157, 248, 16, 63,
        1, 2, 117, 17, 15, 94, 79, 116, 80, 262,
        191, 16, 17, 190, 162, 174, 15, 116, 14, 107,
        103, 117, 57, 16, 121, 17, 197, 45, 122, 16,
        68, 117, 16, 153, 116, 15, 177, 17, 153, 46,
        159, 153, 160, 17, 45, 28, 17, 15, 117, 139,
        4, 102, 139, 229, 108, 167, 23, 234, 45, 12,
        238, 8, 110, 173, 181, 164, 57, 201, 240, 192,
        15, 45, 203, 253, 206, 207, 210, 265, 209, 91,
        213, 215, 216, 217, 222, 41, 16, 231, 15, 165,
        233, 231, 116, 232, 111, 236, 112, 235, 116, 116,
        17, 15, 250, 244, 257, 259, 117, 16, 245, 111,
        16, 112, 117, 117, 15, 260, 263, 185, 268, 269,
        224, 17, 15, 116, 17, 163, 89, 254, 15, 3,
        116, 16, 270, 251, 131, 141, 96, 117, 249, 78,
        8, 141, 185, 159, 117, 17, 8, 43, 141, 9,
        10, 7, 8, 11, 141, 12, 214, 13, 111, 82,
        112, 9, 10, 66, 8, 11, 134, 12, 91, 13,
        111, 99, 112, 9, 10, 16, 43, 28, 5, 12,
        67, 13, 8, 43, 68, 141, 28, 28, 23, 17,
        28, 198, 28, 16, 28, 267, 16, 23, 23, 155,
        142, 23, 184, 23, 156, 23, 261, 17, 7, 8,
        17, 85, 45, 212, 158, 61, 62, 95, 9, 10,
        76, 8, 11, 123, 12, 45, 13, 212, 86, 162,
        9, 10, 7, 8, 11, 103, 12, 243, 13, 8,
        43, 140, 9, 10, 116, 139, 11, 41, 12, 123,
        13, 45, 8, 45, 171, 67, 8, 43, 44, 161,
        138, 9, 10, 45, 8, 11, 102, 12, 45, 13,
        8, 43, 44, 9, 10, 12, 12, 11, 111, 12,
        112, 13, 56, 8, 43, 12, 12, 8, 230, 12,
        45, 12, 45, 12, 175, 10, 9, 10, 227, 45,
        11, 111, 12, 112, 13, 8, 157, 239, 8, 110,
        111, 124, 112, 226, 9, 10, 189, 218, 8, 111,
        12, 112, 13, 8, 110, 11, 219, 9, 10, 168,
        220, 8, 152, 12, 8, 13, 131, 129, 130, 169,
        9, 10, 170, 9, 10, 8, 12, 151, 13, 12,
        73, 13, 73, 237, 9, 10, 37, 38, 101, 8,
        12, 176, 13, 223, 158, 124, 73, 204, 9, 10,
        264, 179, 8, 110, 12, 109, 13, 95, 95, 95,
        95, 95, 182, 95, 8, 110, 0, 73, 0, 73,
        73, 73, 0, 0, 0, 95, 95, 95, 95, 76,
        0, 76, 76, 76, 0, 73, 73, 73, 73, 74,
        0, 74, 74, 74, 0, 0, 0, 76, 76, 76,
        76, 0, 0, 83, 8, 43, 211, 74, 74, 74,
        74, 84, 0, 74, 241, 74, 75, 8, 43, 80,
        80, 116, 80, 0, 80, 79, 79, 242, 79, 74,
        79, 228, 0, 0, 116, 0, 80, 136, 137, 0,
        0, 0, 79, 8, 43, 8, 43, 0, 0, 0,
        0, 0, 0, 69, 0, 8, 43, 0, 0, 194,
        8, 43, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 8, 110, 0, 0, 255, 100, 0, 0,
        256, 196, 8, 43, 8, 43, 0, 0, 0, 0,
        225, 0, 43, 0, 47, 8, 110, 0, 77, 0,
        69, 87, 0, 0, 8, 110, 0, 0, 0, 100,
        0, 0, 0, 8, 110, 0, 125, 126, 127, 128,
        0, 0, 0, 0, 149, 150, 0, 104, 0, 0,
        0, 47, 0, 0, 0, 0, 0, 0, 0, 0,
        100, 0, 0, 186, 73, 73, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 162, 0, 0, 0, 0, 0, 0, 168, 169,
        0, 0, 95, 95, 95, 0, 95, 95, 95, 95,
        95, 95, 73, 73, 73, 0, 73, 73, 73, 73,
        0, 73, 0, 0, 76, 76, 76, 186, 76, 76,
        76, 76, 0, 76, 74, 74, 74, 0, 74, 74,
        74, 74, 0, 74, 0, 0, 0, 200, 0, 0,
        0, 186, 0, 0, 0, 0, 0, 74, 74, 0,
        0, 0, 0, 0, 80, 80, 0, 0, 0, 0,
        79, 79, 69, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 104,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        0, 0, 20, 40, 44, 41, 59, 0, 44, 59,
        41, 0, 2, 143, 45, 40, 59, 84, 113, 59,
        257, 11, 12, 68, 14, 2, 2, 40, 257, 41,
        20, 41, 44, 270, 42, 44, 12, 14, 14, 47,
        40, 270, 123, 45, 20, 40, 36, 256, 115, 61,
        45, 40, 61, 44, 99, 102, 41, 47, 40, 125,
        36, 42, 271, 81, 46, 270, 47, 123, 58, 59,
        84, 47, 40, 67, 257, 170, 70, 207, 68, 40,
        256, 257, 58, 59, 40, 59, 123, 77, 125, 59,
        41, 81, 68, 44, 41, 85, 40, 87, 123, 258,
        41, 77, 40, 93, 59, 81, 120, 45, 256, 99,
        123, 87, 102, 264, 104, 40, 93, 93, 41, 40,
        59, 44, 256, 99, 45, 125, 102, 40, 104, 41,
        123, 41, 44, 180, 41, 257, 125, 184, 45, 40,
        256, 257, 258, 257, 40, 125, 40, 268, 201, 41,
        40, 45, 257, 220, 41, 44, 257, 125, 148, 177,
        59, 125, 125, 41, 256, 46, 156, 181, 40, 125,
        256, 185, 162, 59, 43, 257, 45, 256, 168, 169,
        156, 40, 59, 41, 256, 41, 162, 177, 123, 43,
        180, 45, 168, 169, 40, 256, 123, 40, 256, 44,
        125, 177, 0, 193, 180, 59, 256, 221, 40, 0,
        200, 201, 125, 256, 41, 257, 256, 193, 208, 256,
        257, 257, 40, 256, 200, 201, 257, 258, 270, 266,
        267, 256, 257, 270, 270, 272, 41, 274, 43, 270,
        45, 266, 267, 256, 257, 270, 256, 272, 266, 274,
        43, 123, 45, 266, 267, 245, 258, 257, 257, 272,
        273, 274, 257, 258, 123, 256, 266, 267, 257, 245,
        270, 256, 272, 263, 274, 256, 266, 266, 267, 125,
        271, 270, 125, 272, 256, 274, 256, 263, 256, 257,
        266, 44, 45, 125, 256, 256, 257, 264, 266, 267,
        40, 257, 270, 256, 272, 45, 274, 125, 61, 256,
        266, 267, 256, 257, 270, 256, 272, 59, 274, 257,
        258, 40, 266, 267, 59, 44, 270, 46, 272, 41,
        274, 45, 257, 45, 256, 273, 257, 258, 259, 41,
        59, 266, 267, 45, 257, 270, 256, 272, 45, 274,
        257, 258, 259, 266, 267, 256, 257, 270, 43, 272,
        45, 274, 256, 257, 258, 266, 267, 257, 41, 270,
        45, 272, 45, 274, 59, 0, 266, 267, 44, 45,
        270, 43, 272, 45, 274, 257, 256, 193, 257, 258,
        43, 256, 45, 59, 266, 267, 265, 59, 257, 43,
        272, 45, 274, 257, 258, 0, 59, 266, 267, 256,
        169, 257, 44, 272, 257, 274, 60, 61, 62, 256,
        266, 267, 256, 266, 267, 257, 272, 59, 274, 272,
        43, 274, 45, 190, 266, 267, 4, 4, 36, 257,
        272, 88, 274, 174, 70, 57, 59, 140, 266, 267,
        244, 98, 257, 258, 272, 46, 274, 41, 42, 43,
        44, 45, 101, 47, 257, 258, -1, 41, -1, 43,
        44, 45, -1, -1, -1, 59, 60, 61, 62, 41,
        -1, 43, 44, 45, -1, 59, 60, 61, 62, 41,
        -1, 43, 44, 45, -1, -1, -1, 59, 60, 61,
        62, -1, -1, 256, 257, 258, 153, 59, 60, 61,
        62, 264, -1, 43, 256, 45, 256, 257, 258, 42,
        43, 256, 45, -1, 47, 42, 43, 269, 45, 59,
        47, 178, -1, -1, 269, -1, 59, 256, 257, -1,
        -1, -1, 59, 257, 258, 257, 258, -1, -1, -1,
        -1, -1, -1, 12, -1, 257, 258, -1, -1, 256,
        257, 258, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 257, 258, -1, -1, 223, 36, -1, -1,
        227, 256, 257, 258, 257, 258, -1, -1, -1, -1,
        256, -1, 258, -1, 9, 257, 258, -1, 13, -1,
        59, 16, -1, -1, 257, 258, -1, -1, -1, 68,
        -1, -1, -1, 257, 258, -1, 260, 261, 262, 263,
        -1, -1, -1, -1, 256, 257, -1, 42, -1, -1,
        -1, 46, -1, -1, -1, -1, -1, -1, -1, -1,
        99, -1, -1, 102, 257, 258, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, 76, -1, -1, -1, -1, -1, -1, 83, 84,
        -1, -1, 256, 257, 258, -1, 260, 261, 262, 263,
        264, 265, 256, 257, 258, -1, 260, 261, 262, 263,
        -1, 265, -1, -1, 256, 257, 258, 156, 260, 261,
        262, 263, -1, 265, 256, 257, 258, -1, 260, 261,
        262, 263, -1, 265, -1, -1, -1, 132, -1, -1,
        -1, 180, -1, -1, -1, -1, -1, 257, 258, -1,
        -1, -1, -1, -1, 257, 258, -1, -1, -1, -1,
        257, 258, 201, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, 190,
    };
  }

  final static short YYFINAL = 3;
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
      "programa : ID cuerpo_programa",
      "programa : ID cuerpo_programa_recuperacion",
      "programa : ID conjunto_sentencias",
      "$$1 :",
      "programa : $$1 programa_sin_nombre",
      "programa : error ID",
      "programa : error EOF",
      "programa_sin_nombre : cuerpo_programa",
      "programa_sin_nombre : cuerpo_programa_recuperacion",
      "cuerpo_programa : '{' conjunto_sentencias '}'",
      "cuerpo_programa_recuperacion : '{' conjunto_sentencias lista_llaves_cierre",
      "$$2 :",
      "cuerpo_programa_recuperacion : lista_llaves_apertura $$2 conjunto_sentencias '}'",
      "cuerpo_programa_recuperacion : '{' '}'",
      "cuerpo_programa_recuperacion :",
      "cuerpo_programa_recuperacion : '{' error '}'",
      "lista_llaves_apertura : '{' '{'",
      "lista_llaves_apertura : lista_llaves_apertura '{'",
      "lista_llaves_cierre : '}' '}'",
      "lista_llaves_cierre : lista_llaves_cierre '}'",
      "conjunto_sentencias : sentencia",
      "conjunto_sentencias : conjunto_sentencias sentencia",
      "conjunto_sentencias : error '}'",
      "sentencia : sentencia_ejecutable",
      "sentencia : sentencia_declarativa",
      "sentencia_declarativa : declaracion_variables",
      "sentencia_declarativa : declaracion_funcion punto_y_coma_opcional",
      "punto_y_coma_opcional :",
      "punto_y_coma_opcional : ';'",
      "cuerpo_ejecutable : sentencia_ejecutable",
      "cuerpo_ejecutable : bloque_ejecutable",
      "bloque_ejecutable : '{' conjunto_sentencias_ejecutables '}'",
      "bloque_ejecutable : '{' '}'",
      "conjunto_sentencias_ejecutables : sentencia_ejecutable",
      "conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables sentencia_ejecutable",
      "sentencia_ejecutable : invocacion_funcion ';'",
      "sentencia_ejecutable : asignacion_simple",
      "sentencia_ejecutable : asignacion_multiple",
      "sentencia_ejecutable : sentencia_control",
      "sentencia_ejecutable : sentencia_retorno",
      "sentencia_ejecutable : impresion",
      "sentencia_ejecutable : lambda",
      "sentencia_ejecutable : invocacion_funcion error",
      "sentencia_control : if",
      "sentencia_control : do_while",
      "declaracion_variables : UINT lista_variables ';'",
      "declaracion_variables : UINT ID ';'",
      "declaracion_variables : UINT ID error",
      "declaracion_variables : UINT lista_variables error",
      "declaracion_variables : UINT error",
      "declaracion_variables : UINT variable DASIG constante ';'",
      "lista_variables : ID ',' ID",
      "lista_variables : lista_variables ',' ID",
      "lista_variables : lista_variables ID",
      "lista_variables : ID ID",
      "asignacion_simple : variable DASIG expresion ';'",
      "asignacion_simple : variable DASIG asignable",
      "asignacion_simple : variable error expresion ';'",
      "asignacion_simple : variable expresion ';'",
      "asignable : factor error",
      "asignable : expresion operador_suma termino error",
      "asignable : termino operador_multiplicacion factor error",
      "asignacion_multiple : inicio_par_variable_constante ';'",
      "asignacion_multiple : inicio_par_variable_constante ',' lista_constantes ';'",
      "asignacion_multiple : inicio_par_variable_constante error",
      "asignacion_multiple : inicio_par_variable_constante ',' lista_constantes error",
      "inicio_par_variable_constante : variable par_variable_constante constante",
      "par_variable_constante : ',' variable par_variable_constante constante ','",
      "par_variable_constante : '='",
      "lista_constantes : constante",
      "lista_constantes : lista_constantes ',' constante",
      "lista_constantes : lista_constantes constante",
      "expresion : termino",
      "expresion : expresion operador_suma termino",
      "expresion : expresion operador_suma error",
      "expresion : expresion termino_simple",
      "operador_suma : '+'",
      "operador_suma : '-'",
      "termino : termino operador_multiplicacion factor",
      "termino : factor",
      "termino : termino operador_multiplicacion error",
      "termino_simple : termino_simple operador_multiplicacion factor_simple",
      "termino_simple : factor_simple",
      "termino_simple : termino_simple operador_multiplicacion error",
      "operador_multiplicacion : '/'",
      "operador_multiplicacion : '*'",
      "factor : variable",
      "factor : constante",
      "factor : invocacion_funcion",
      "factor_simple : variable",
      "factor_simple : CTE",
      "factor_simple : invocacion_funcion",
      "constante : CTE",
      "constante : '-' CTE",
      "variable : ID",
      "variable : ID '.' ID",
      "condicion : '(' cuerpo_condicion ')'",
      "condicion : cuerpo_condicion ')'",
      "condicion : '(' ')'",
      "condicion : cuerpo_condicion error",
      "condicion : '(' cuerpo_condicion error",
      "cuerpo_condicion : expresion comparador expresion",
      "cuerpo_condicion : expresion",
      "comparador : '>'",
      "comparador : '<'",
      "comparador : EQ",
      "comparador : LEQ",
      "comparador : GEQ",
      "comparador : NEQ",
      "comparador : '='",
      "if : IF condicion cuerpo_ejecutable rama_else ENDIF ';'",
      "if : IF condicion cuerpo_ejecutable rama_else ENDIF error",
      "if : IF condicion cuerpo_ejecutable rama_else ';'",
      "if : IF condicion cuerpo_ejecutable rama_else error",
      "if : IF error",
      "rama_else :",
      "rama_else : ELSE cuerpo_ejecutable",
      "do_while : DO cuerpo_do ';'",
      "do_while : DO cuerpo_do_recuperacion error",
      "do_while : DO error",
      "cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
      "cuerpo_do_recuperacion : cuerpo_do",
      "cuerpo_do_recuperacion : fin_cuerpo_do",
      "cuerpo_do_recuperacion : cuerpo_ejecutable condicion",
      "fin_cuerpo_do : WHILE condicion",
      "declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible",
      "declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
      "cuerpo_funcion : cuerpo_funcion_admisible",
      "cuerpo_funcion : '{' '}'",
      "cuerpo_funcion_admisible : '{' conjunto_sentencias '}'",
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
      "sentencia_retorno : RETURN '(' expresion ')' ';'",
      "sentencia_retorno : RETURN '(' expresion ')' error",
      "sentencia_retorno : RETURN '(' ')' ';'",
      "sentencia_retorno : RETURN expresion ';'",
      "sentencia_retorno : RETURN error",
      "invocacion_funcion : ID '(' lista_argumentos ')'",
      "lista_argumentos : argumento",
      "lista_argumentos : lista_argumentos ',' argumento",
      "argumento : expresion FLECHA ID",
      "argumento : expresion",
      "impresion : PRINT imprimible_admisible ';'",
      "impresion : PRINT imprimible error",
      "imprimible : imprimible_admisible",
      "imprimible : '(' ')'",
      "imprimible : elemento_imprimible",
      "imprimible :",
      "imprimible_admisible : '(' elemento_imprimible ')'",
      "elemento_imprimible : STR",
      "elemento_imprimible : expresion",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda_admisible ';'",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
      "lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
      "argumento_lambda : argumento_lambda_admisible",
      "$$3 :",
      "argumento_lambda : '(' ')' $$3",
      "argumento_lambda :",
      "argumento_lambda_admisible : '(' factor ')'",
      "parametro_lambda : '(' UINT ID ')'",
  };

  // #line 836 "/home/iroumec/Documents/University/Compiladores e
  // Intérpretes/TPE-Compiler/code/parser/gramatica.y"

  // ====================================================================================================================
  // INICIO DE CÓDIGO (opcional)
  // ====================================================================================================================

  // Lexer.
  private final Lexer lexer;

  // Contadores de la cantidad de errores detectados.
  private int errorsDetected;
  private int warningsDetected;

  public Parser(Lexer lexer) {

    this.lexer = lexer;
    this.errorsDetected = this.warningsDetected = 0;

    // Descomentar la siguiente línea para activar el debugging.
    // yydebug = true;
  }

  // --------------------------------------------------------------------------------------------------------------------

  // Método público para llamar a yyparse(), ya que, por defecto,
  // su modificador de visibilidad es package.
  public void execute() {
    yyparse();
  }

  // --------------------------------------------------------------------------------------------------------------------

  // Método yylex() invocado durante yyparse().
  int yylex() {

    if (lexer == null) {
      throw new IllegalStateException("No hay un analizador léxico asignado.");
    }

    Token token = lexer.getNextToken();

    this.yylval = new ParserVal(token.getLexema());

    return token.getIdentificationCode();
  }

  // --------------------------------------------------------------------------------------------------------------------

  /**
   * Este método es invocado por el parser generado por Byacc/J cada vez que
   * se encuentra un error de sintaxis que no puede ser manejado por una
   * regla gramatical específica con el token 'error'.
   *
   * @param s El mensaje de error por defecto (generalmente "syntax error").
   */
  // Se ejecuta cada vez que encuentra un token error.
  public void yyerror(String s) {

    // Silenciado.
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyDetection(String message) {
    Printer.printBetweenSeparations(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message));
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyWarning(String warningMessage) {
    Printer.printBetweenSeparations(String.format(
        "WARNING SINTÁCTICA: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), warningMessage));
    this.warningsDetected++;
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyError(String errorMessage) {
    Printer.printBetweenSeparations(String.format(
        "ERROR SINTÁCTICO: Línea %d, caracter %d: %s",
        lexer.getNroLinea(), lexer.getNroCaracter(), errorMessage));
    this.errorsDetected++;
  }

  // --------------------------------------------------------------------------------------------------------------------

  public int getWarningsDetected() {
    return this.warningsDetected;
  }

  // --------------------------------------------------------------------------------------------------------------------

  public int getErrorsDetected() {
    return this.errorsDetected;
  }

  // --------------------------------------------------------------------------------------------------------------------

  public boolean isUint(String number) {
    return !number.contains(".");
  }

  // --------------------------------------------------------------------------------------------------------------------

  public void altaSymbolTable(String lexema) {
    SymbolTable.getInstance().agregarEntrada(lexema);
  }

  // ====================================================================================================================
  // FIN DE CÓDIGO
  // ====================================================================================================================
  // #line 747 "Parser.java"
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
        // #line 75 "gramatica.y"
        {
          notifyDetection("Programa.");
        }
          break;
        case 3:
        // #line 82 "gramatica.y"
        {
          notifyError("Las sentencias del programa deben estar delimitadas por llaves.");
        }
          break;
        case 4:
        // #line 85 "gramatica.y"
        {
          notifyError("El programa requiere de un nombre.");
        }
          break;
        case 6:
        // #line 88 "gramatica.y"
        {
          notifyError("Inicio de programa inválido. Este debe seguir la estructura: <NOMBRE%PROGRAMA> { ... }.");
        }
          break;
        case 7:
        // #line 91 "gramatica.y"
        {
          notifyError("Se llegó al fin del programa sin encontrar un programa válido.");
        }
          break;
        case 11:
        // #line 112 "gramatica.y"
        {
          notifyError("Se encontraron múltiples llaves al final del programa.");
        }
          break;
        case 12:
        // #line 115 "gramatica.y"
        {
          notifyError("Se encontraron múltiples llaves al comienzo del programa.");
        }
          break;
        case 14:
        // #line 118 "gramatica.y"
        {
          notifyError("El programa no posee ninguna sentencia.");
        }
          break;
        case 15:
        // #line 120 "gramatica.y"
        {
          notifyError("El programa no posee ningún cuerpo.");
        }
          break;
        case 16:
        // #line 122 "gramatica.y"
        {
          notifyError("Las sentencias del programa no tuvieron un cierre adecuado. ¿Algún ';' o '}' faltantes?");
        }
          break;
        case 23:
        // #line 148 "gramatica.y"
        {
          notifyError("Error capturado a nivel de sentencia.");
        }
          break;
        case 27:
        // #line 165 "gramatica.y"
        {
          notifyDetection("Declaración de función.");
        }
          break;
        case 33:
        // #line 192 "gramatica.y"
        {
          notifyError("El cuerpo de la sentencia no puede estar vacío.");
        }
          break;
        case 36:
        // #line 206 "gramatica.y"
        {
          notifyDetection("Invocación de función.");
        }
          break;
        case 43:
        // #line 217 "gramatica.y"
        {
          notifyError("La invocación a función debe terminar con ';'.");
        }
          break;
        case 46:
        // #line 233 "gramatica.y"
        {
          notifyDetection("Declaración de variables.");
        }
          break;
        case 47:
        // #line 236 "gramatica.y"
        {
          notifyDetection("Declaración de variable.");
        }
          break;
        case 48:
        // #line 241 "gramatica.y"
        {
          notifyError("La declaración de variable debe terminar con ';'.");
        }
          break;
        case 49:
        // #line 245 "gramatica.y"
        {
          notifyError("La declaración de variables debe terminar con ';'.");
        }
          break;
        case 50:
        // #line 249 "gramatica.y"
        {
          notifyError("Declaración de variables inválida.");
        }
          break;
        case 51:
        // #line 253 "gramatica.y"
        {
          notifyError(
              "La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
          break;
        case 53:
        // #line 263 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 54:
        // #line 268 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
          {
            yyval.sval = val_peek(0).sval;
          }
        }
          break;
        case 55:
        // #line 275 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
          {
            yyval.sval = val_peek(0).sval;
          }
        }
          break;
        case 56:
        // #line 289 "gramatica.y"
        {
          notifyDetection("Asignación simple.");
        }
          break;
        case 57:
        // #line 294 "gramatica.y"
        {
          notifyError("Las asignaciones simples deben terminar con ';'.");
        }
          break;
        case 58:
        // #line 297 "gramatica.y"
        {
          notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión.");
        }
          break;
        case 59:
        // #line 300 "gramatica.y"
        {
          notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión.");
        }
          break;
        case 63:
        // #line 325 "gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 64:
        // #line 327 "gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 65:
        // #line 332 "gramatica.y"
        {
          notifyDetection("La asignación múltiple debe terminar con ';'.");
        }
          break;
        case 66:
        // #line 334 "gramatica.y"
        {
          notifyDetection("La asignación múltiple debe terminar con ';'.");
        }
          break;
        case 71:
        // #line 355 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 72:
        // #line 360 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 74:
        // #line 374 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 75:
        // #line 379 "gramatica.y"
        {
          notifyError(String.format(
              "Falta de operando en expresión luego de %s %s.",
              val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 76:
        // #line 386 "gramatica.y"
        {
          notifyError(String.format(
              "Falta de operador entre operandos %s y %s.",
              val_peek(1).sval, val_peek(0).sval));
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 77:
        // #line 399 "gramatica.y"
        {
          yyval.sval = "+";
        }
          break;
        case 78:
        // #line 401 "gramatica.y"
        {
          yyval.sval = "-";
        }
          break;
        case 79:
        // #line 408 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval;
        }
          break;
        case 81:
        // #line 414 "gramatica.y"
        {
          notifyError(String.format(
              "Falta de operando en expresión luego de %s %s.",
              val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 82:
        // #line 426 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval;
        }
          break;
        case 84:
        // #line 432 "gramatica.y"
        {
          notifyError(String.format(
              "Falta de operando en expresión luego de %s %s.",
              val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 85:
        // #line 444 "gramatica.y"
        {
          yyval.sval = "/";
        }
          break;
        case 86:
        // #line 446 "gramatica.y"
        {
          yyval.sval = "*";
        }
          break;
        case 93:
        // #line 471 "gramatica.y"
        {
          altaSymbolTable(val_peek(0).sval);
        }
          break;
        case 94:
        // #line 473 "gramatica.y"
        {
          yyval.sval = "-" + val_peek(0).sval;
          if (isUint(yyval.sval)) {
            notifyWarning("El número está fuera del rango de uint, se asignará el mínimo del rango.");
            yyval.sval = "0";
          }
          altaSymbolTable(yyval.sval);
        }
          break;
        case 96:
        // #line 488 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval + "." + val_peek(0).sval;
        }
          break;
        case 97:
        // #line 497 "gramatica.y"
        {
          notifyDetection("Condición.");
        }
          break;
        case 98:
        // #line 502 "gramatica.y"
        {
          notifyError("Falta apertura de paréntesis en condición.");
        }
          break;
        case 99:
        // #line 505 "gramatica.y"
        {
          notifyError("La condición no puede estar vacía.");
        }
          break;
        case 100:
        // #line 508 "gramatica.y"
        {
          notifyError("La condición debe ir entre paréntesis.");
        }
          break;
        case 101:
        // #line 511 "gramatica.y"
        {
          notifyError("Falta cierre de paréntesis en condición.");
        }
          break;
        case 103:
        // #line 523 "gramatica.y"
        {
          notifyError("Falta de comparador en comparación.");
        }
          break;
        case 110:
        // #line 539 "gramatica.y"
        {
          notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?");
        }
          break;
        case 111:
        // #line 548 "gramatica.y"
        {
          notifyDetection("Sentencia IF.");
        }
          break;
        case 112:
        // #line 553 "gramatica.y"
        {
          notifyError("La sentencia IF debe terminar con ';'.");
        }
          break;
        case 113:
        // #line 555 "gramatica.y"
        {
          notifyError("La sentencia IF debe finalizar con 'endif'.");
        }
          break;
        case 114:
        // #line 557 "gramatica.y"
        {
          notifyError("La sentencia IF debe finalizar con 'endif' y ';'.");
        }
          break;
        case 115:
        // #line 559 "gramatica.y"
        {
          notifyError("Sentencia IF inválida.");
        }
          break;
        case 118:
        // #line 575 "gramatica.y"
        {
          notifyDetection("Sentencia 'do-while'.");
        }
          break;
        case 119:
        // #line 580 "gramatica.y"
        {
          notifyError("La sentencia 'do-while' debe terminar con ';'.");
        }
          break;
        case 120:
        // #line 582 "gramatica.y"
        {
          notifyError("Sentencia 'do-while' inválida.");
        }
          break;
        case 123:
        // #line 599 "gramatica.y"
        {
          notifyError("Debe especificarse un cuerpo para la sentencia do-while.");
        }
          break;
        case 124:
        // #line 601 "gramatica.y"
        {
          notifyError("Falta 'while'.");
        }
          break;
        case 127:
        // #line 620 "gramatica.y"
        {
          notifyError("La función requiere de un nombre.");
        }
          break;
        case 129:
        // #line 632 "gramatica.y"
        {
          notifyError("El cuerpo de la función no puede estar vacío.");
        }
          break;
        case 132:
        // #line 649 "gramatica.y"
        {
          notifyError("Toda función debe recibir al menos un parámetro.");
        }
          break;
        case 135:
        // #line 661 "gramatica.y"
        {
          notifyError("Se halló un parámetro formal vacío.");
        }
          break;
        case 139:
        // #line 679 "gramatica.y"
        {
          notifyError("Falta de nombre de parámetro formal en declaración de función.");
        }
          break;
        case 140:
        // #line 671 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("Falta de tipo de parámetro formal en declaración de función.");
        }
          break;
        case 143:
        // #line 693 "gramatica.y"
        {
          notifyError("Semántica de pasaje de parámetro inválida.");
        }
          break;
        case 144:
        // #line 702 "gramatica.y"
        {
          notifyDetection("Sentencia 'return'.");
        }
          break;
        case 145:
        // #line 707 "gramatica.y"
        {
          notifyError("La sentencia 'return' debe terminar con ';'.");
        }
          break;
        case 146:
        // #line 709 "gramatica.y"
        {
          notifyError("El retorno no puede estar vacío.");
        }
          break;
        case 147:
        // #line 711 "gramatica.y"
        {
          notifyError("El resultado a retornar debe ir entre paréntesis.");
        }
          break;
        case 148:
        // #line 713 "gramatica.y"
        {
          notifyError("Sentencia 'return' inválida.");
        }
          break;
        case 149:
        // #line 722 "gramatica.y"
        {
          yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
          break;
        case 151:
        // #line 732 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 152:
        // #line 739 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
        }
          break;
        case 153:
        // #line 744 "gramatica.y"
        {
          notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
        }
          break;
        case 154:
        // #line 753 "gramatica.y"
        {
          notifyDetection("Sentencia 'print'.");
        }
          break;
        case 155:
        // #line 758 "gramatica.y"
        {
          notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
          break;
        case 157:
        // #line 769 "gramatica.y"
        {
          notifyError("La sentencia 'print' requiere de al menos un argumento.");
        }
          break;
        case 158:
        // #line 772 "gramatica.y"
        {
          notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
          break;
        case 159:
        // #line 774 "gramatica.y"
        {
          notifyError("La sentencia 'print' requiere de un argumento entre paréntesis.");
        }
          break;
        case 163:
        // #line 797 "gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 164:
        // #line 793 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 165:
        // #line 795 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 166:
        // #line 797 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 167:
        // #line 799 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyDetection("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 169:
        // #line 810 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El argumento de la expresión 'lambda' no puede estar vacío.");
        }
          break;
        case 170:
        // #line 813 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("El argumento de la expresión 'lambda' debe ir entre paréntesis");
        }
          break;
        case 171:
        // #line 816 "/home/iroumec/Documents/University/Compiladores e
        // Intérpretes/TPE-Compiler/code/parser/gramatica.y"
        {
          notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis.");
        }
          break;
        // #line 1317 "Parser.java"
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
