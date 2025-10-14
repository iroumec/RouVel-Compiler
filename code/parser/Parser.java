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
  public final static short EOF = 0;
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
      0, 0, 0, 19, 0, 20, 0, 0, 0, 18,
      18, 15, 16, 23, 16, 16, 16, 16, 22, 22,
      21, 21, 17, 17, 17, 24, 24, 26, 26, 29,
      29, 30, 30, 31, 31, 32, 32, 25, 25, 25,
      25, 25, 25, 25, 25, 35, 35, 27, 27, 27,
      27, 27, 27, 8, 8, 8, 8, 33, 33, 33,
      33, 34, 34, 34, 34, 34, 41, 41, 42, 42,
      43, 43, 9, 9, 9, 1, 1, 1, 1, 1,
      6, 6, 2, 2, 2, 2, 4, 4, 4, 7,
      7, 3, 3, 3, 5, 5, 5, 11, 11, 10,
      10, 44, 44, 44, 44, 45, 45, 45, 45, 46,
      46, 46, 46, 46, 46, 46, 39, 39, 39, 39,
      39, 47, 47, 47, 40, 40, 40, 40, 40, 48,
      49, 49, 50, 28, 28, 28, 53, 53, 52, 51,
      51, 54, 54, 54, 56, 56, 55, 55, 55, 57,
      57, 57, 36, 36, 36, 36, 36, 12, 13, 13,
      14, 14, 37, 37, 37, 37, 58, 59, 59, 59,
      60, 60, 38, 38, 38, 38, 38, 38, 38, 38,
      38, 38, 62, 63, 63, 61,
  };
  final static short yylen[] = { 2,
      2, 2, 2, 0, 2, 0, 4, 2, 1, 1,
      1, 3, 3, 0, 4, 2, 0, 3, 2, 2,
      2, 2, 1, 2, 2, 1, 1, 1, 2, 0,
      1, 1, 1, 3, 2, 1, 2, 2, 1, 1,
      1, 1, 1, 1, 2, 1, 1, 3, 3, 3,
      3, 5, 2, 3, 3, 2, 2, 4, 4, 3,
      3, 4, 6, 4, 6, 5, 3, 1, 2, 1,
      2, 1, 1, 3, 2, 1, 3, 3, 2, 2,
      1, 1, 3, 1, 3, 2, 3, 1, 3, 1,
      1, 2, 1, 1, 1, 1, 1, 1, 2, 1,
      3, 3, 2, 2, 3, 3, 2, 3, 1, 1,
      1, 1, 1, 1, 1, 1, 6, 6, 5, 5,
      2, 0, 2, 1, 3, 3, 3, 3, 2, 2,
      1, 2, 2, 6, 5, 7, 1, 2, 3, 1,
      0, 1, 3, 1, 2, 2, 3, 2, 2, 0,
      1, 1, 5, 5, 4, 3, 2, 4, 1, 3,
      3, 1, 3, 3, 3, 3, 3, 2, 1, 0,
      1, 1, 4, 4, 4, 4, 5, 4, 5, 5,
      4, 5, 3, 2, 0, 4,
  };
  final static short yydefred[] = { 0,
      0, 9, 0, 0, 0, 8, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 1, 2,
      0, 0, 23, 26, 27, 28, 0, 39, 40, 41,
      42, 43, 44, 46, 47, 0, 10, 11, 5, 0,
      25, 0, 0, 98, 171, 0, 0, 90, 91, 0,
      0, 0, 84, 0, 0, 93, 94, 0, 0, 169,
      121, 0, 0, 0, 0, 0, 53, 0, 0, 0,
      0, 129, 0, 0, 32, 0, 33, 0, 0, 131,
      157, 0, 0, 0, 19, 16, 0, 0, 0, 0,
      68, 0, 0, 0, 0, 45, 38, 24, 20, 0,
      31, 29, 0, 36, 0, 0, 0, 7, 101, 0,
      0, 159, 0, 99, 168, 0, 96, 81, 82, 0,
      88, 0, 95, 97, 0, 86, 92, 164, 163, 166,
      165, 103, 0, 112, 114, 113, 115, 116, 110, 111,
      0, 0, 0, 0, 0, 0, 104, 50, 57, 49,
      0, 0, 152, 151, 0, 0, 0, 142, 144, 0,
      51, 56, 48, 0, 0, 133, 35, 0, 132, 130,
      127, 125, 128, 126, 0, 0, 156, 0, 0, 0,
      0, 61, 0, 0, 69, 60, 0, 70, 0, 0,
      0, 0, 0, 0, 0, 0, 37, 0, 0, 0,
      0, 0, 158, 167, 0, 78, 0, 85, 83, 105,
      102, 0, 0, 123, 0, 0, 54, 0, 146, 0,
      0, 0, 149, 55, 0, 34, 155, 0, 21, 22,
      186, 59, 58, 64, 62, 0, 0, 73, 0, 67,
      15, 0, 0, 184, 0, 174, 173, 176, 175, 0,
      0, 178, 181, 12, 161, 160, 89, 87, 0, 119,
      120, 0, 0, 137, 135, 143, 147, 52, 154, 153,
      0, 66, 0, 75, 71, 177, 180, 183, 179, 182,
      118, 117, 0, 134, 138, 0, 65, 63, 74, 136,
      139,
  };
  final static short yydgoto[] = { 4,
      63, 52, 53, 120, 121, 122, 54, 70, 237, 17,
      56, 57, 111, 112, 19, 20, 286, 39, 5, 7,
      180, 22, 100, 23, 24, 25, 26, 27, 102, 76,
      77, 106, 28, 29, 30, 31, 32, 33, 34, 35,
      94, 95, 240, 65, 66, 143, 146, 78, 79, 80,
      156, 264, 265, 157, 158, 159, 160, 58, 59, 60,
      36, 193, 194,
  };
  final static short yysindex[] = { 5,
      21, 0, -18, 0, -100, 0, -213, -79, 90, 415,
      528, -32, 34, 534, -38, -211, 76, -30, 0, 0,
      160, -60, 0, 0, 0, 0, 17, 0, 0, 0,
      0, 0, 0, 0, 0, 146, 0, 0, 0, -58,
      0, -187, 578, 0, 0, 19, -180, 0, 0, 541,
      266, 52, 0, -42, -165, 0, 0, -21, 42, 0,
      0, 101, 505, 52, 70, 54, 0, 37, 3, 65,
      -158, 0, 547, 171, 0, -36, 0, 73, 91, 0,
      0, 554, 457, -11, 0, 0, 82, -142, 572, -141,
      0, 519, -165, 12, 6, 0, 0, 0, 0, 94,
      0, 0, 171, 0, 109, 174, 94, 0, 0, 88,
      67, 0, 52, 0, 0, 87, 0, 0, 0, 52,
      0, 424, 0, 0, 377, 0, 0, 0, 0, 0,
      0, 0, -16, 0, 0, 0, 0, 0, 0, 0,
      52, 424, 578, 187, -129, -115, 0, 0, 0, 0,
      -102, 3, 0, 0, -229, 126, 130, 0, 0, -218,
      0, 0, 0, -88, 12, 0, 0, 206, 0, 0,
      0, 0, 0, 0, 113, 8, 0, 0, 51, 60,
      138, 0, 298, 145, 0, 0, 107, 0, 12, 105,
      210, 47, 99, 111, 109, -15, 0, -62, -61, 116,
      -59, 578, 0, 0, 380, 0, 52, 0, 0, 0,
      0, 52, 266, 0, 10, 137, 0, 162, 0, 78,
      -229, -141, 0, 0, 150, 0, 0, 122, 0, 0,
      0, 0, 0, 0, 0, 12, 68, 0, 161, 0,
      0, -52, -48, 0, 172, 0, 0, 0, 0, -44,
      -39, 0, 0, 0, 0, 0, 0, 0, 125, 0,
      0, 97, 46, 0, 0, 0, 0, 0, 0, 0,
      118, 0, 12, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 58, 0, 0, 128, 0, 0, 0, 0,
      0,
  };
  final static short yyrindex[] = { 24,
      -34, 0, 245, 0, 245, 0, 0, 0, 444, 131,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      284, 149, 0, 0, 0, 0, 1, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      -25, 406, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 476, 26, 0, 0, 33, -6, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 29, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 140, 61, 0, 0, 0, 120,
      0, 0, 348, 0, 0, 0, 0, 0, 0, 454,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      485, 0, 0, 23, 100, 0, 0, 0, 0, 0,
      0, -6, 0, 0, -212, 0, 278, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 15, 323, 370,
      0, 0, 0, -24, 0, 0, 0, 0, 0, 0,
      61, 0, 0, 0, 61, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 466, 0, 0, 0,
      0, 498, -9, 0, 0, 0, 0, 0, 0, 0,
      -13, 134, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 30, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0,
  };
  final static short yygindex[] = { 0,
      636, 772, 436, 317, 0, 321, 575, 0, 163, 796,
      584, -3, 0, 195, 14, 396, 53, 0, 0, 0,
      0, 0, 0, -8, 757, 0, 0, 0, 0, -51,
      368, -31, 0, 0, 0, 0, 0, 0, 0, 0,
      316, 0, 0, 62, 352, 0, 279, 0, 0, 353,
      281, 173, 0, 0, -119, 0, 0, 0, 0, 386,
      0, -66, 11,
  };
  final static int YYTABLESIZE = 1018;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 18,
        30, 16, 47, 62, 2, 49, 46, 69, 47, 18,
        48, 18, 98, 145, 18, 172, 100, 18, 37, 100,
        6, 16, 15, 4, 211, 244, 153, 145, 97, 47,
        145, 106, 18, 172, 141, 219, 100, 129, 184, 198,
        30, 154, 168, 40, 150, 41, 155, 124, 228, 90,
        118, 222, 119, 108, 25, 21, 47, 150, 88, 124,
        49, 18, 99, 47, 107, 48, 91, 87, 260, 109,
        18, 191, 70, 16, 72, 101, 152, 114, 98, 124,
        151, 124, 42, 18, 85, 16, 86, 244, 124, 70,
        127, 47, 214, 49, 147, 150, 18, 16, 48, 18,
        131, 266, 18, 18, 15, 165, 124, 203, 164, 16,
        202, 273, 47, 178, 181, 184, 199, 49, 46, 90,
        47, 16, 48, 163, 242, 30, 272, 204, 250, 43,
        118, 172, 119, 16, 166, 42, 91, 169, 144, 25,
        18, 132, 49, 46, 16, 47, 4, 48, 192, 174,
        236, 47, 190, 216, 217, 16, 74, 247, 122, 200,
        162, 273, 47, 162, 18, 235, 220, 16, 224, 249,
        285, 227, 124, 221, 148, 229, 288, 148, 231, 124,
        270, 98, 290, 282, 230, 16, 18, 18, 14, 170,
        42, 98, 74, 252, 253, 261, 18, 255, 185, 16,
        263, 243, 262, 276, 275, 251, 179, 277, 268, 124,
        16, 279, 278, 196, 9, 44, 280, 84, 9, 283,
        9, 44, 6, 67, 68, 96, 16, 10, 11, 241,
        172, 12, 100, 13, 128, 14, 73, 8, 9, 210,
        254, 9, 44, 150, 17, 16, 106, 10, 11, 196,
        150, 12, 291, 13, 88, 14, 150, 30, 153, 18,
        1, 3, 184, 150, 9, 117, 30, 30, 103, 44,
        30, 25, 30, 154, 30, 9, 44, 98, 259, 18,
        25, 25, 18, 3, 25, 70, 25, 72, 25, 72,
        9, 124, 148, 149, 122, 167, 100, 130, 195, 10,
        11, 8, 9, 9, 44, 13, 73, 14, 118, 74,
        119, 10, 11, 8, 9, 12, 185, 13, 140, 14,
        161, 162, 12, 10, 11, 44, 9, 12, 171, 13,
        226, 14, 9, 44, 226, 10, 11, 144, 9, 89,
        118, 13, 119, 14, 9, 117, 173, 10, 11, 8,
        9, 12, 201, 13, 246, 14, 233, 9, 44, 10,
        11, 9, 234, 12, 44, 13, 248, 14, 122, 13,
        10, 11, 9, 287, 12, 44, 13, 269, 14, 141,
        281, 10, 11, 142, 9, 12, 170, 13, 80, 14,
        80, 80, 80, 10, 11, 185, 256, 12, 271, 13,
        38, 14, 9, 105, 14, 14, 80, 80, 80, 80,
        189, 10, 11, 133, 14, 14, 9, 13, 14, 14,
        14, 47, 14, 215, 47, 10, 11, 9, 170, 12,
        9, 13, 218, 14, 284, 116, 10, 11, 0, 10,
        11, 0, 13, 9, 14, 13, 76, 14, 76, 76,
        76, 0, 10, 11, 50, 0, 49, 46, 13, 47,
        14, 48, 9, 0, 76, 49, 9, 0, 47, 0,
        48, 10, 11, 0, 0, 10, 11, 13, 0, 14,
        0, 13, 0, 14, 100, 100, 100, 100, 100, 126,
        100, 0, 0, 0, 79, 0, 79, 79, 79, 118,
        0, 119, 100, 100, 100, 100, 77, 0, 77, 77,
        77, 0, 79, 0, 0, 177, 109, 0, 76, 0,
        76, 0, 9, 117, 77, 107, 0, 79, 0, 79,
        0, 0, 0, 0, 0, 76, 76, 76, 108, 0,
        77, 0, 77, 0, 79, 79, 79, 118, 0, 119,
        0, 0, 0, 232, 9, 117, 0, 77, 77, 77,
        209, 118, 0, 119, 140, 138, 139, 62, 0, 49,
        46, 0, 47, 82, 48, 49, 46, 186, 47, 0,
        48, 115, 49, 46, 0, 47, 62, 48, 49, 46,
        0, 47, 0, 48, 175, 49, 46, 0, 47, 0,
        48, 0, 0, 80, 80, 80, 0, 80, 80, 80,
        80, 0, 80, 49, 46, 0, 47, 0, 48, 49,
        46, 0, 47, 0, 48, 0, 125, 245, 0, 0,
        0, 245, 208, 9, 44, 257, 9, 44, 125, 0,
        258, 0, 0, 0, 0, 51, 0, 0, 0, 83,
        0, 0, 92, 0, 0, 0, 0, 0, 0, 0,
        0, 76, 76, 76, 0, 0, 0, 0, 0, 0,
        76, 9, 44, 45, 0, 0, 0, 187, 110, 206,
        9, 44, 0, 0, 0, 51, 0, 125, 0, 0,
        0, 0, 0, 0, 205, 0, 0, 0, 0, 100,
        100, 100, 0, 100, 100, 100, 100, 100, 100, 79,
        79, 79, 0, 9, 117, 205, 0, 176, 79, 0,
        0, 77, 77, 77, 183, 0, 0, 0, 0, 0,
        77, 109, 76, 76, 0, 76, 76, 76, 76, 0,
        107, 79, 79, 0, 79, 79, 79, 79, 225, 0,
        0, 0, 0, 108, 77, 77, 0, 77, 77, 77,
        77, 9, 117, 0, 134, 135, 136, 137, 0, 75,
        238, 0, 239, 0, 0, 9, 117, 0, 213, 0,
        0, 125, 64, 61, 9, 44, 125, 0, 0, 81,
        9, 44, 104, 0, 0, 0, 0, 9, 44, 45,
        0, 0, 0, 9, 44, 55, 55, 71, 0, 55,
        9, 44, 93, 0, 0, 0, 0, 113, 0, 238,
        274, 75, 0, 0, 0, 0, 0, 182, 9, 44,
        104, 0, 0, 64, 9, 44, 0, 110, 55, 0,
        0, 55, 0, 0, 64, 55, 123, 64, 0, 55,
        0, 0, 0, 0, 274, 0, 289, 55, 123, 104,
        0, 0, 197, 0, 0, 0, 0, 0, 55, 0,
        0, 55, 0, 0, 0, 0, 0, 55, 123, 0,
        0, 0, 0, 0, 55, 185, 0, 123, 0, 0,
        188, 0, 0, 207, 0, 0, 0, 0, 0, 0,
        75, 0, 0, 0, 0, 123, 0, 0, 0, 0,
        0, 0, 0, 212, 0, 0, 0, 55, 0, 0,
        55, 0, 0, 0, 197, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 55, 55, 0,
        0, 0, 0, 0, 0, 0, 0, 197, 0, 0,
        0, 0, 0, 0, 0, 223, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 123, 0, 0, 0, 0, 0, 0, 123, 0,
        0, 0, 0, 0, 0, 0, 0, 55, 0, 0,
        0, 55, 0, 0, 0, 0, 0, 55, 0, 0,
        55, 0, 0, 0, 0, 0, 0, 0, 123, 0,
        0, 0, 0, 0, 0, 0, 0, 267,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 3,
        0, 40, 45, 40, 0, 42, 43, 40, 45, 13,
        47, 15, 21, 65, 0, 41, 41, 21, 5, 44,
        0, 40, 123, 0, 41, 41, 256, 41, 59, 45,
        44, 41, 36, 59, 41, 155, 61, 59, 257, 106,
        40, 271, 74, 257, 257, 125, 44, 51, 41, 44,
        43, 270, 45, 40, 40, 3, 45, 270, 270, 63,
        42, 65, 123, 45, 123, 47, 61, 15, 59, 257,
        74, 103, 44, 40, 45, 59, 40, 258, 87, 83,
        44, 59, 46, 87, 123, 40, 125, 41, 92, 61,
        256, 45, 144, 42, 41, 59, 100, 40, 47, 103,
        59, 221, 106, 107, 123, 264, 110, 41, 44, 40,
        44, 44, 45, 125, 257, 257, 106, 42, 43, 44,
        45, 40, 47, 59, 191, 125, 59, 41, 195, 40,
        43, 59, 45, 40, 73, 46, 61, 76, 268, 125,
        144, 41, 42, 43, 40, 45, 123, 47, 40, 59,
        44, 45, 100, 269, 257, 40, 123, 59, 59, 107,
        41, 44, 45, 44, 168, 59, 41, 40, 257, 59,
        125, 59, 176, 44, 41, 125, 59, 44, 41, 183,
        59, 190, 125, 59, 125, 40, 190, 191, 40, 59,
        46, 200, 123, 256, 256, 59, 200, 257, 59, 40,
        123, 191, 41, 256, 44, 195, 125, 256, 59, 213,
        40, 256, 41, 40, 257, 258, 256, 256, 257, 123,
        257, 258, 257, 256, 257, 256, 40, 266, 267, 125,
        256, 270, 257, 272, 256, 274, 273, 256, 257, 256,
        125, 257, 258, 257, 0, 40, 256, 266, 267, 40,
        257, 270, 125, 272, 270, 274, 270, 257, 256, 263,
        256, 257, 257, 270, 257, 258, 266, 267, 123, 258,
        270, 257, 272, 271, 274, 257, 258, 286, 269, 283,
        266, 267, 286, 0, 270, 257, 272, 258, 274, 256,
        257, 269, 256, 257, 269, 125, 264, 256, 125, 266,
        267, 256, 257, 257, 258, 272, 273, 274, 43, 123,
        45, 266, 267, 256, 257, 270, 256, 272, 41, 274,
        256, 257, 0, 266, 267, 258, 257, 270, 256, 272,
        125, 274, 257, 258, 125, 266, 267, 268, 257, 264,
        43, 272, 45, 274, 257, 258, 256, 266, 267, 256,
        257, 270, 265, 272, 256, 274, 59, 257, 258, 266,
        267, 257, 256, 270, 258, 272, 256, 274, 269, 0,
        266, 267, 257, 256, 270, 258, 272, 256, 274, 63,
        256, 266, 267, 63, 257, 270, 256, 272, 41, 274,
        43, 44, 45, 266, 267, 256, 202, 270, 236, 272,
        5, 274, 257, 36, 256, 257, 59, 60, 61, 62,
        95, 266, 267, 62, 266, 267, 257, 272, 270, 274,
        272, 45, 274, 145, 45, 266, 267, 257, 76, 270,
        257, 272, 152, 274, 262, 50, 266, 267, -1, 266,
        267, -1, 272, 257, 274, 272, 41, 274, 43, 44,
        45, -1, 266, 267, 40, -1, 42, 43, 272, 45,
        274, 47, 257, -1, 59, 42, 257, -1, 45, -1,
        47, 266, 267, -1, -1, 266, 267, 272, -1, 274,
        -1, 272, -1, 274, 41, 42, 43, 44, 45, 54,
        47, -1, -1, -1, 41, -1, 43, 44, 45, 43,
        -1, 45, 59, 60, 61, 62, 41, -1, 43, 44,
        45, -1, 59, -1, -1, 59, 41, -1, 43, -1,
        45, -1, 257, 258, 59, 41, -1, 43, -1, 45,
        -1, -1, -1, -1, -1, 60, 61, 62, 41, -1,
        43, -1, 45, -1, 60, 61, 62, 43, -1, 45,
        -1, -1, -1, 256, 257, 258, -1, 60, 61, 62,
        125, 43, -1, 45, 60, 61, 62, 40, -1, 42,
        43, -1, 45, 40, 47, 42, 43, 59, 45, -1,
        47, 41, 42, 43, -1, 45, 40, 47, 42, 43,
        -1, 45, -1, 47, 41, 42, 43, -1, 45, -1,
        47, -1, -1, 256, 257, 258, -1, 260, 261, 262,
        263, -1, 265, 42, 43, -1, 45, -1, 47, 42,
        43, -1, 45, -1, 47, -1, 52, 192, -1, -1,
        -1, 196, 256, 257, 258, 256, 257, 258, 64, -1,
        205, -1, -1, -1, -1, 10, -1, -1, -1, 14,
        -1, -1, 17, -1, -1, -1, -1, -1, -1, -1,
        -1, 256, 257, 258, -1, -1, -1, -1, -1, -1,
        265, 257, 258, 259, -1, -1, -1, 94, 43, 256,
        257, 258, -1, -1, -1, 50, -1, 113, -1, -1,
        -1, -1, -1, -1, 120, -1, -1, -1, -1, 256,
        257, 258, -1, 260, 261, 262, 263, 264, 265, 256,
        257, 258, -1, 257, 258, 141, -1, 82, 265, -1,
        -1, 256, 257, 258, 89, -1, -1, -1, -1, -1,
        265, 256, 257, 258, -1, 260, 261, 262, 263, -1,
        256, 257, 258, -1, 260, 261, 262, 263, 165, -1,
        -1, -1, -1, 256, 257, 258, -1, 260, 261, 262,
        263, 257, 258, -1, 260, 261, 262, 263, -1, 13,
        187, -1, 189, -1, -1, 257, 258, -1, 143, -1,
        -1, 207, 11, 256, 257, 258, 212, -1, -1, 256,
        257, 258, 36, -1, -1, -1, -1, 257, 258, 259,
        -1, -1, -1, 257, 258, 10, 11, 12, -1, 14,
        257, 258, 17, -1, -1, -1, -1, 46, -1, 236,
        237, 65, -1, -1, -1, -1, -1, 256, 257, 258,
        74, -1, -1, 62, 257, 258, -1, 202, 43, -1,
        -1, 46, -1, -1, 73, 50, 51, 76, -1, 54,
        -1, -1, -1, -1, 271, -1, 273, 62, 63, 103,
        -1, -1, 106, -1, -1, -1, -1, -1, 73, -1,
        -1, 76, -1, -1, -1, -1, -1, 82, 83, -1,
        -1, -1, -1, -1, 89, 90, -1, 92, -1, -1,
        95, -1, -1, 122, -1, -1, -1, -1, -1, -1,
        144, -1, -1, -1, -1, 110, -1, -1, -1, -1,
        -1, -1, -1, 142, -1, -1, -1, 122, -1, -1,
        125, -1, -1, -1, 168, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, 142, 143, -1,
        -1, -1, -1, -1, -1, -1, -1, 191, -1, -1,
        -1, -1, -1, -1, -1, 160, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, 176, -1, -1, -1, -1, -1, -1, 183, -1,
        -1, -1, -1, -1, -1, -1, -1, 192, -1, -1,
        -1, 196, -1, -1, -1, -1, -1, 202, -1, -1,
        205, -1, -1, -1, -1, -1, -1, -1, 213, -1,
        -1, -1, -1, -1, -1, -1, -1, 222,
    };
  }

  final static short YYFINAL = 4;
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
      "$$2 :",
      "programa : error $$2 ID cuerpo_programa",
      "programa : error EOF",
      "programa : EOF",
      "programa_sin_nombre : cuerpo_programa",
      "programa_sin_nombre : cuerpo_programa_recuperacion",
      "cuerpo_programa : '{' conjunto_sentencias '}'",
      "cuerpo_programa_recuperacion : '{' conjunto_sentencias lista_llaves_cierre",
      "$$3 :",
      "cuerpo_programa_recuperacion : lista_llaves_apertura $$3 conjunto_sentencias '}'",
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
      "declaracion_variables : UINT variable DASIG constante ';'",
      "declaracion_variables : UINT error",
      "lista_variables : ID ',' ID",
      "lista_variables : lista_variables ',' ID",
      "lista_variables : lista_variables ID",
      "lista_variables : ID ID",
      "asignacion_simple : variable DASIG expresion ';'",
      "asignacion_simple : variable DASIG expresion error",
      "asignacion_simple : variable expresion ';'",
      "asignacion_simple : variable DASIG error",
      "asignacion_multiple : variable asignacion_par constante ';'",
      "asignacion_multiple : variable asignacion_par constante ',' lista_constantes ';'",
      "asignacion_multiple : variable asignacion_par constante error",
      "asignacion_multiple : variable asignacion_par constante ',' lista_constantes error",
      "asignacion_multiple : variable asignacion_par constante lista_constantes ';'",
      "asignacion_par : variable_con_coma asignacion_par constante_con_coma",
      "asignacion_par : '='",
      "variable_con_coma : ',' variable",
      "variable_con_coma : variable",
      "constante_con_coma : constante ','",
      "constante_con_coma : constante",
      "lista_constantes : constante",
      "lista_constantes : lista_constantes ',' constante",
      "lista_constantes : lista_constantes constante",
      "expresion : termino",
      "expresion : expresion operador_suma termino",
      "expresion : expresion operador_suma error",
      "expresion : expresion termino_simple",
      "expresion : '+' termino",
      "operador_suma : '+'",
      "operador_suma : '-'",
      "termino : termino operador_multiplicacion factor",
      "termino : factor",
      "termino : termino operador_multiplicacion error",
      "termino : operador_multiplicacion factor",
      "termino_simple : termino_simple operador_multiplicacion factor",
      "termino_simple : factor_simple",
      "termino_simple : termino_simple operador_multiplicacion error",
      "operador_multiplicacion : '/'",
      "operador_multiplicacion : '*'",
      "factor : variable error",
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
      "condicion : '(' ')'",
      "condicion : cuerpo_condicion ')'",
      "condicion : '(' cuerpo_condicion error",
      "cuerpo_condicion : expresion comparador expresion",
      "cuerpo_condicion : expresion termino_simple",
      "cuerpo_condicion : expresion operador_suma termino",
      "cuerpo_condicion : termino",
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
      "if : IF condicion rama_else ENDIF ';'",
      "if : IF error",
      "rama_else :",
      "rama_else : ELSE cuerpo_ejecutable",
      "rama_else : ELSE",
      "do_while : DO cuerpo_do ';'",
      "do_while : DO cuerpo_do_recuperacion ';'",
      "do_while : DO cuerpo_do error",
      "do_while : DO cuerpo_do_recuperacion error",
      "do_while : DO error",
      "cuerpo_do : cuerpo_ejecutable fin_cuerpo_do",
      "cuerpo_do_recuperacion : fin_cuerpo_do",
      "cuerpo_do_recuperacion : cuerpo_ejecutable condicion",
      "fin_cuerpo_do : WHILE condicion",
      "declaracion_funcion : UINT ID '(' conjunto_parametros ')' cuerpo_funcion_admisible",
      "declaracion_funcion : UINT '(' conjunto_parametros ')' cuerpo_funcion",
      "declaracion_funcion : UINT ID '(' conjunto_parametros ')' '{' '}'",
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
      "impresion : PRINT imprimible ';'",
      "impresion : PRINT imprimible error",
      "impresion : PRINT imprimible_recuperacion ';'",
      "impresion : PRINT imprimible_recuperacion error",
      "imprimible : '(' elemento_imprimible ')'",
      "imprimible_recuperacion : '(' ')'",
      "imprimible_recuperacion : elemento_imprimible",
      "imprimible_recuperacion :",
      "elemento_imprimible : STR",
      "elemento_imprimible : expresion",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda ';'",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda error",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda_recuperacion ';'",
      "lambda : parametro_lambda bloque_ejecutable argumento_lambda_recuperacion error",
      "lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda error",
      "lambda : parametro_lambda '{' conjunto_sentencias_ejecutables argumento_lambda_recuperacion error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables argumento_lambda_recuperacion error",
      "lambda : parametro_lambda conjunto_sentencias_ejecutables '}' argumento_lambda_recuperacion error",
      "argumento_lambda : '(' factor ')'",
      "argumento_lambda_recuperacion : '(' ')'",
      "argumento_lambda_recuperacion :",
      "parametro_lambda : '(' UINT ID ')'",
  };

  // #line 850 "gramatica.y"

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
   * se encuentra con un token error.
   *
   * @param s El mensaje de error por defecto (generalmente "syntax error").
   */
  public void yyerror(String s) {

    // Silenciado, ya que los mensajes son manejados mediante otros métodos.
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyDetection(String message) {
    Printer.printWrapped(String.format(
        "DETECCIÓN SEMÁNTICA: %s",
        message));
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyWarning(String warningMessage) {
    Printer.printWrapped(String.format(
        "WARNING SINTÁCTICA: Línea %d: %s",
        lexer.getNroLinea(), warningMessage));
    this.warningsDetected++;
  }

  // --------------------------------------------------------------------------------------------------------------------

  void notifyError(String errorMessage) {
    Printer.printWrapped(String.format(
        "ERROR SINTÁCTICO: Línea %d: %s",
        lexer.getNroLinea(), errorMessage));
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

  public void modificarSymbolTable(String lexemaNuevo, String lexemaAnterior) {
    SymbolTable.getInstance().decrementarReferencia(lexemaAnterior);
    if (lexemaNuevo != null) {
      SymbolTable.getInstance().agregarEntrada(lexemaNuevo);
    }
  }

  // ====================================================================================================================
  // FIN DE CÓDIGO
  // ====================================================================================================================
  // #line 820 "Parser.java"
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
        // #line 68 "gramatica.y"
        {
          notifyDetection("Programa.");
        }
          break;
        case 3:
        // #line 75 "gramatica.y"
        {
          notifyError("Las sentencias del programa deben estar delimitadas por llaves.");
        }
          break;
        case 4:
        // #line 78 "gramatica.y"
        {
          notifyError("El programa requiere de un nombre.");
        }
          break;
        case 6:
        // #line 80 "gramatica.y"
        {
          notifyError("Inicio de programa inválido. Se encontraron sentencias previo al nombre del programa.");
        }
          break;
        case 8:
        // #line 83 "gramatica.y"
        {
          notifyError("Se llegó al fin del programa sin encontrar un programa válido.");
        }
          break;
        case 9:
        // #line 86 "gramatica.y"
        {
          notifyError("El archivo está vacío.");
        }
          break;
        case 13:
        // #line 107 "gramatica.y"
        {
          notifyError("Se encontraron múltiples llaves al final del programa.");
        }
          break;
        case 14:
        // #line 110 "gramatica.y"
        {
          notifyError("Se encontraron múltiples llaves al comienzo del programa.");
        }
          break;
        case 16:
        // #line 113 "gramatica.y"
        {
          notifyError("El programa no posee ninguna sentencia.");
        }
          break;
        case 17:
        // #line 115 "gramatica.y"
        {
          notifyError("El programa no posee ningún cuerpo.");
        }
          break;
        case 18:
        // #line 117 "gramatica.y"
        {
          notifyError("Cierre inesperado del programa. Verifique llaves '{...}' y puntos y coma ';' faltantes.");
        }
          break;
        case 25:
        // #line 143 "gramatica.y"
        {
          notifyError("Error capturado a nivel de sentencia.");
        }
          break;
        case 35:
        // #line 184 "gramatica.y"
        {
          notifyError("El cuerpo de la sentencia no puede estar vacío.");
        }
          break;
        case 38:
        // #line 202 "gramatica.y"
        {
          notifyDetection("Invocación de función.");
        }
          break;
        case 45:
        // #line 213 "gramatica.y"
        {
          notifyError("La invocación a función debe terminar con ';'.");
        }
          break;
        case 48:
        // #line 229 "gramatica.y"
        {
          notifyDetection("Declaración de variables.");
        }
          break;
        case 49:
        // #line 232 "gramatica.y"
        {
          notifyDetection("Declaración de variable.");
        }
          break;
        case 50:
        // #line 237 "gramatica.y"
        {
          notifyError("La declaración de variable debe terminar con ';'.");
        }
          break;
        case 51:
        // #line 241 "gramatica.y"
        {
          notifyError("La declaración de variables debe terminar con ';'.");
        }
          break;
        case 52:
        // #line 245 "gramatica.y"
        {
          notifyError(
              "La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
          break;
        case 53:
        // #line 249 "gramatica.y"
        {
          notifyError("Declaración de variables inválida.");
        }
          break;
        case 55:
        // #line 259 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 56:
        // #line 264 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
          {
            yyval.sval = val_peek(0).sval;
          }
        }
          break;
        case 57:
        // #line 271 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
          {
            yyval.sval = val_peek(0).sval;
          }
        }
          break;
        case 58:
        // #line 285 "gramatica.y"
        {
          notifyDetection("Asignación simple.");
        }
          break;
        case 59:
        // #line 291 "gramatica.y"
        {
          notifyError("Las asignaciones simples deben terminar con ';'.");
        }
          break;
        case 60:
        // #line 294 "gramatica.y"
        {
          notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión.");
        }
          break;
        case 61:
        // #line 297 "gramatica.y"
        {
          notifyError("Asignación simple inválida.");
        }
          break;
        case 62:
        // #line 307 "gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 63:
        // #line 309 "gramatica.y"
        {
          notifyDetection("Asignación múltiple.");
        }
          break;
        case 64:
        // #line 314 "gramatica.y"
        {
          notifyError("La asignación múltiple debe terminar con ';'.");
        }
          break;
        case 65:
        // #line 316 "gramatica.y"
        {
          notifyError("La asignación múltiple debe terminar con ';'.");
        }
          break;
        case 66:
        // #line 318 "gramatica.y"
        {
          notifyError(String.format("Falta coma luego de la constante '%s' en asignacion múltiple", val_peek(2).sval));
        }
          break;
        case 70:
        // #line 333 "gramatica.y"
        {
          notifyError(String.format("Falta coma antes de variable '%s' en asignación múltiple.", val_peek(0).sval));
        }
          break;
        case 72:
        // #line 341 "gramatica.y"
        {
          notifyError(String.format("Falta coma luego de constante '%s' en asignación múltiple.", val_peek(0).sval));
        }
          break;
        case 74:
        // #line 349 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 75:
        // #line 354 "gramatica.y"
        {
          notifyError(String.format(
              "Se encontraron dos constantes juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
              val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 77:
        // #line 368 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 78:
        // #line 373 "gramatica.y"
        {
          notifyError(
              String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 79:
        // #line 377 "gramatica.y"
        {
          notifyError(String.format("Falta de operador entre operandos %s y %s.", val_peek(1).sval, val_peek(0).sval));
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 80:
        // #line 384 "gramatica.y"
        {
          notifyError(String.format("Falta de operando en expresión previo a '+ %s'.", val_peek(0).sval));
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 81:
        // #line 394 "gramatica.y"
        {
          yyval.sval = "+";
        }
          break;
        case 82:
        // #line 396 "gramatica.y"
        {
          yyval.sval = "-";
        }
          break;
        case 83:
        // #line 403 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 85:
        // #line 409 "gramatica.y"
        {
          notifyError(String.format(
              "Falta de operando en expresión luego de '%s %s'.",
              val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 86:
        // #line 416 "gramatica.y"
        {
          notifyError(String.format("Falta operando previo a '%s %s'", val_peek(1).sval, val_peek(0).sval));
        }
          break;
        case 87:
        // #line 423 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval;
        }
          break;
        case 89:
        // #line 429 "gramatica.y"
        {
          notifyError(
              String.format("Falta de operando en expresión luego de '%s %s'.", val_peek(2).sval, val_peek(1).sval));
        }
          break;
        case 90:
        // #line 436 "gramatica.y"
        {
          yyval.sval = "/";
        }
          break;
        case 91:
        // #line 438 "gramatica.y"
        {
          yyval.sval = "*";
        }
          break;
        case 99:
        // #line 465 "gramatica.y"
        {
          yyval.sval = "-" + val_peek(0).sval;

          notifyDetection(String.format("Constante negativa: %s.", yyval.sval));

          if (isUint(yyval.sval)) {
            notifyError("El número está fuera del rango de uint, se descartará.");
            yyval.sval = null;
          }

          modificarSymbolTable(yyval.sval, val_peek(0).sval);
        }
          break;
        case 101:
        // #line 484 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval + "." + val_peek(0).sval;
        }
          break;
        case 102:
        // #line 493 "gramatica.y"
        {
          notifyDetection("Condición.");
        }
          break;
        case 103:
        // #line 498 "gramatica.y"
        {
          notifyError("La condición no puede estar vacía.");
        }
          break;
        case 104:
        // #line 502 "gramatica.y"
        {
          notifyError("Falta apertura de paréntesis en condición.");
        }
          break;
        case 105:
        // #line 504 "gramatica.y"
        {
          notifyError("Falta cierre de paréntesis en condición.");
        }
          break;
        case 107:
        // #line 515 "gramatica.y"
        {
          notifyError("Falta de comparador en comparación.");
        }
          break;
        case 108:
        // #line 517 "gramatica.y"
        {
          notifyError("Falta de comparador en comparación.");
        }
          break;
        case 109:
        // #line 519 "gramatica.y"
        {
          notifyError("Falta de comparador en comparación.");
        }
          break;
        case 116:
        // #line 535 "gramatica.y"
        {
          notifyError("Se esperaba un comparador y se encontró el operador de asignación '='. ¿Quiso colocar '=='?");
        }
          break;
        case 117:
        // #line 544 "gramatica.y"
        {
          notifyDetection("Sentencia IF.");
        }
          break;
        case 118:
        // #line 549 "gramatica.y"
        {
          notifyError("La sentencia IF debe terminar con ';'.");
        }
          break;
        case 119:
        // #line 551 "gramatica.y"
        {
          notifyError("La sentencia IF debe finalizar con 'endif'.");
        }
          break;
        case 120:
        // #line 553 "gramatica.y"
        {
          notifyError("Falta el bloque de sentencias del IF.");
        }
          break;
        case 121:
        // #line 555 "gramatica.y"
        {
          notifyError("Sentencia IF inválida.");
        }
          break;
        case 124:
        // #line 567 "gramatica.y"
        {
          notifyError("Falta el bloque de sentencias del ELSE.");
        }
          break;
        case 125:
        // #line 576 "gramatica.y"
        {
          notifyDetection("Sentencia 'do-while'.");
        }
          break;
        case 127:
        // #line 582 "gramatica.y"
        {
          notifyError("La sentencia 'do-while' debe terminar con ';'.");
        }
          break;
        case 128:
        // #line 584 "gramatica.y"
        {
          notifyError("La sentencia 'do-while' debe terminar con ';'.");
        }
          break;
        case 129:
        // #line 586 "gramatica.y"
        {
          notifyError("Sentencia 'do-while' inválida.");
        }
          break;
        case 131:
        // #line 599 "gramatica.y"
        {
          notifyError("Debe especificarse un cuerpo para la sentencia do-while.");
        }
          break;
        case 132:
        // #line 601 "gramatica.y"
        {
          notifyError("Falta 'while'.");
        }
          break;
        case 134:
        // #line 616 "gramatica.y"
        {
          notifyDetection("Declaración de función.");
        }
          break;
        case 135:
        // #line 621 "gramatica.y"
        {
          notifyError("La función requiere de un nombre.");
        }
          break;
        case 136:
        // #line 624 "gramatica.y"
        {
          notifyError("El cuerpo de la función no puede estar vacío.");
        }
          break;
        case 138:
        // #line 636 "gramatica.y"
        {
          notifyError("El cuerpo de la función no puede estar vacío.");
        }
          break;
        case 141:
        // #line 653 "gramatica.y"
        {
          notifyError("Toda función debe recibir al menos un parámetro.");
        }
          break;
        case 144:
        // #line 665 "gramatica.y"
        {
          notifyError("Se halló un parámetro formal vacío.");
        }
          break;
        case 148:
        // #line 683 "gramatica.y"
        {
          notifyError("Falta de nombre de parámetro formal en declaración de función.");
        }
          break;
        case 149:
        // #line 685 "gramatica.y"
        {
          notifyError("Falta de tipo de parámetro formal en declaración de función.");
        }
          break;
        case 152:
        // #line 697 "gramatica.y"
        {
          notifyError("Semántica de pasaje de parámetro inválida.");
        }
          break;
        case 153:
        // #line 706 "gramatica.y"
        {
          notifyDetection("Sentencia RETURN.");
        }
          break;
        case 154:
        // #line 711 "gramatica.y"
        {
          notifyError("La sentencia RETURN debe terminar con ';'.");
        }
          break;
        case 155:
        // #line 713 "gramatica.y"
        {
          notifyError("El retorno no puede estar vacío.");
        }
          break;
        case 156:
        // #line 715 "gramatica.y"
        {
          notifyError("El resultado a retornar debe ir entre paréntesis.");
        }
          break;
        case 157:
        // #line 717 "gramatica.y"
        {
          notifyError("Sentencia RETURN inválida.");
        }
          break;
        case 158:
        // #line 726 "gramatica.y"
        {
          yyval.sval = val_peek(3).sval + '(' + val_peek(1).sval + ')';
        }
          break;
        case 160:
        // #line 736 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 161:
        // #line 743 "gramatica.y"
        {
          yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
        }
          break;
        case 162:
        // #line 748 "gramatica.y"
        {
          notifyError("Falta de especificación del parámetro formal al que corresponde el parámetro real.");
        }
          break;
        case 163:
        // #line 757 "gramatica.y"
        {
          notifyDetection("Sentencia 'print'.");
        }
          break;
        case 164:
        // #line 762 "gramatica.y"
        {
          notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
          break;
        case 166:
        // #line 765 "gramatica.y"
        {
          notifyError("La sentencia 'print' debe finalizar con ';'.");
        }
          break;
        case 168:
        // #line 778 "gramatica.y"
        {
          notifyError("La sentencia 'print' requiere de al menos un argumento.");
        }
          break;
        case 169:
        // #line 781 "gramatica.y"
        {
          notifyError("El imprimible debe encerrarse entre paréntesis.");
        }
          break;
        case 170:
        // #line 783 "gramatica.y"
        {
          notifyError("La sentencia 'print' requiere de un argumento entre paréntesis.");
        }
          break;
        case 173:
        // #line 799 "gramatica.y"
        {
          notifyDetection("Expresión lambda.");
        }
          break;
        case 174:
        // #line 804 "gramatica.y"
        {
          notifyError("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 176:
        // #line 807 "gramatica.y"
        {
          notifyError("La expresión 'lambda' debe terminar con ';'.");
        }
          break;
        case 177:
        // #line 810 "gramatica.y"
        {
          notifyError("Falta delimitador de cierre en expresión 'lambda'.");
        }
          break;
        case 178:
        // #line 812 "gramatica.y"
        {
          notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'.");
        }
          break;
        case 179:
        // #line 814 "gramatica.y"
        {
          notifyError("Falta delimitador de apertura en expresión 'lambda'.");
        }
          break;
        case 180:
        // #line 816 "gramatica.y"
        {
          notifyError("Falta delimitador de cierre en expresión 'lambda'.");
        }
          break;
        case 181:
        // #line 818 "gramatica.y"
        {
          notifyError("Faltan delimitadores en el conjunto de sentencias de la expresión 'lambda'.");
        }
          break;
        case 182:
        // #line 820 "gramatica.y"
        {
          notifyError("Falta delimitador de apertura en expresión 'lambda'.");
        }
          break;
        case 184:
        // #line 833 "gramatica.y"
        {
          notifyError("El argumento de la expresión 'lambda' no puede estar vacío.");
        }
          break;
        case 185:
        // #line 836 "gramatica.y"
        {
          notifyError("La expresión 'lambda' requiere de un argumento entre paréntesis.");
        }
          break;
        // #line 1445 "Parser.java"
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
