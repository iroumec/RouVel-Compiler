package general;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {

    // --------------------------------------------------------------------------------------------
    // Tokens que pueden corresponder a más de un lexema.

    ID(null, Category.REQUIRES_LEXEMA),
    CTE(null, Category.REQUIRES_LEXEMA),
    STR(null, Category.REQUIRES_LEXEMA),

    // --------------------------------------------------------------------------------------------
    // Palabras reservadas.

    IF("if", Category.RESERVED_WORD),
    DO("do", Category.RESERVED_WORD),
    ELSE("else", Category.RESERVED_WORD),
    EIF("endif", Category.RESERVED_WORD),
    UINT("uint", Category.RESERVED_WORD),
    PRNT("print", Category.RESERVED_WORD),
    RET("return", Category.RESERVED_WORD),
    FLOAT("float", Category.RESERVED_WORD),
    WHILE("while", Category.RESERVED_WORD),

    // --------------------------------------------------------------------------------------------
    // Literales.

    GT(">", Category.LITERAL),
    LT("<", Category.LITERAL),
    GE(">=", Category.LITERAL),
    LE("<=", Category.LITERAL),
    EQ("==", Category.LITERAL),
    PC(";", Category.LITERAL),
    ASIG("=", Category.LITERAL),
    NEQ("!=", Category.LITERAL),
    SUM("+", Category.LITERAL),
    RES("-", Category.LITERAL),
    MUL("*", Category.LITERAL),
    DIV("/", Category.LITERAL),
    PAR("(", Category.LITERAL),
    RAP(")", Category.LITERAL),
    KEY("{", Category.LITERAL),
    YEK("}", Category.LITERAL),
    SLSH("_", Category.LITERAL),
    DASIG(":=", Category.LITERAL),
    FLECHA("->", Category.LITERAL);

    // --------------------------------------------------------------------------------------------

    private enum Category {
        LITERAL,
        RESERVED_WORD,
        REQUIRES_LEXEMA;
    };

    // --------------------------------------------------------------------------------------------

    private final String symbol;
    private final Category category;

    // --------------------------------------------------------------------------------------------

    private TokenType(String symbol, Category category) {
        this.symbol = symbol;
        this.category = category;
    }

    // --------------------------------------------------------------------------------------------

    public String getSymbol() {
        return this.symbol;
    }

    // --------------------------------------------------------------------------------------------

    public boolean requiereLexema() {
        return this.category == Category.REQUIRES_LEXEMA;
    }

    // --------------------------------------------------------------------------------------------

    public boolean isReservedWord() {
        return this.category == Category.RESERVED_WORD;
    }

    // --------------------------------------------------------------------------------------------

    private static final Map<String, TokenType> symbolLookup = new HashMap<>();

    static {
        for (TokenType t : TokenType.values()) {
            if (t.getSymbol() != null) {
                symbolLookup.put(t.getSymbol(), t);
            }
        }
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
