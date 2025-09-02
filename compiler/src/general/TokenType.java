package general;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {

    // --------------------------------------------------------------------------------------------
    // Tokens que pueden corresponder a más de un lexema.

    ID(1, null, Category.REQUIRE_LEXEMA),
    CTE(2, null, Category.REQUIRE_LEXEMA),
    STR(3, null, Category.REQUIRE_LEXEMA),

    // --------------------------------------------------------------------------------------------
    // Palabras reservadas.

    IF(11, "if", Category.RESERVED_WORD),
    DO(18, "do", Category.RESERVED_WORD),
    ELSE(12, "else", Category.RESERVED_WORD),
    EIF(13, "endif", Category.RESERVED_WORD),
    UINT(16, "uint", Category.RESERVED_WORD),
    PRNT(14, "print", Category.RESERVED_WORD),
    RET(15, "return", Category.RESERVED_WORD),
    FLOAT(17, "float", Category.RESERVED_WORD),
    WHILE(19, "while", Category.RESERVED_WORD),

    // --------------------------------------------------------------------------------------------
    // Literales.

    GT(6, ">", Category.LITERAL),
    LT(6, "<", Category.LITERAL),
    GE(6, ">=", Category.LITERAL),
    LE(7, "<=", Category.LITERAL),
    EQ(8, "==", Category.LITERAL),
    PC(29, ";", Category.LITERAL),
    ASIG(4, "=", Category.LITERAL),
    NEQ(9, "!=", Category.LITERAL),
    SUM(20, "+", Category.LITERAL),
    RES(21, "-", Category.LITERAL),
    MUL(22, "*", Category.LITERAL),
    DIV(23, "/", Category.LITERAL),
    PAR(24, "(", Category.LITERAL),
    RAP(25, ")", Category.LITERAL),
    KEY(26, "{", Category.LITERAL),
    YEK(27, "}", Category.LITERAL),
    SLSH(28, "_", Category.LITERAL),
    DASIG(5, ":=", Category.LITERAL),
    FLECHA(10, "->", Category.LITERAL);

    // --------------------------------------------------------------------------------------------

    private int number;
    private String symbol;
    private Category category;

    // --------------------------------------------------------------------------------------------

    private enum Category {
        LITERAL,
        RESERVED_WORD,
        REQUIRE_LEXEMA;
    };

    // --------------------------------------------------------------------------------------------

    private TokenType(int number, String symbol, Category category) {
        this.number = number;
        this.symbol = symbol;
        this.category = category;
    }

    // --------------------------------------------------------------------------------------------

    public int getNumber() {
        return this.number;
    }

    // --------------------------------------------------------------------------------------------

    public String getSymbol() {
        return this.symbol;
    }

    // --------------------------------------------------------------------------------------------

    public boolean requiereLexema() {
        return this.category == Category.REQUIRE_LEXEMA;
    }

    // --------------------------------------------------------------------------------------------

    public boolean isReservedWord() {
        return this.category == Category.RESERVED_WORD;
    }

    // --------------------------------------------------------------------------------------------

    private static final Map<Integer, TokenType> integerLookup = new HashMap<>();
    private static final Map<String, TokenType> symbolLookup = new HashMap<>();

    static {
        for (TokenType t : TokenType.values()) {
            integerLookup.put(t.getNumber(), t);
            if (t.getSymbol() != null) {
                symbolLookup.put(t.getSymbol(), t);
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * 
     * @param number
     * @return NULL if it doesn't exist.
     */
    public static TokenType fromNumber(int number) {
        return integerLookup.get(number);
    }

    // --------------------------------------------------------------------------------------------

    /**
     * 
     * @param symbol
     * @return NULL if it doesn't exist.
     */
    public static TokenType fromSymbol(String symbol) {
        return symbolLookup.get(symbol);
    }
}
