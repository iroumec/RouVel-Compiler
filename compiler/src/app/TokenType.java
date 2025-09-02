package app;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    ID(1, null),
    CTE(2, null),
    STR(3, null),
    ASIG(4, "="),
    DASIG(5, ":="),
    GE(6, ">="),
    LE(7, "<="),
    EQ(8, "=="),
    NEQ(9, "!="),
    FLECHA(10, "->"),
    IF(11, "if"),
    ELSE(12, "else"),
    EIF(13, "endif"),
    PRNT(14, "print"),
    RET(15, "return"),
    UINT(16, "uint"),
    FLOAT(17, "float"),
    DO(18, "do"),
    WHILE(19, "while"),
    SUM(20, "+"),
    RES(21, "-"),
    MUL(22, "*"),
    DIV(23, "/"),
    PAR(24, "("),
    RAP(25, ")"),
    KEY(26, "{"),
    YEK(27, "}"),
    SLSH(28, "_"),
    PC(29, ";");

    private int number;
    private String symbol;

    private TokenType(int number, String symbol) {
        this.number = number;
        this.symbol = symbol;
    }

    public int getNumber() {
        return this.number;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public boolean requiereLexema() {
        return this == ID || this == CTE || this == STR;
    }

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

    /**
     * 
     * @param number
     * @return NULL if it doesn't exist.
     */
    public static TokenType fromNumber(int number) {
        return integerLookup.get(number);
    }

    /**
     * 
     * @param symbol
     * @return NULL if it doesn't exist.
     */
    public static TokenType fromSymbol(String symbol) {
        return symbolLookup.get(symbol);
    }
}
