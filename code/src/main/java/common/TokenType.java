package common;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {

    // --------------------------------------------------------------------------------------------
    // Tokens que pueden corresponder a más de un lexema.

    ID(null),
    CTE(null),
    STR(null),

    // --------------------------------------------------------------------------------------------
    // Palabras reservadas.

    IF("if"),
    DO("do"),
    ELSE("else"),
    EIF("endif"),
    UINT("uint"),
    PRNT("print"),
    RET("return"),
    FLOAT("float"),
    WHILE("while"),

    // --------------------------------------------------------------------------------------------
    // Literales.

    GT(">"),
    LT("<"),
    GE(">="),
    LE("<="),
    EQ("=="),
    PC(";"),
    ASIG("="),
    NEQ("!="),
    SUM("+"),
    RES("-"),
    MUL("*"),
    DIV("/"),
    PAR("("),
    RAP(")"),
    KEY("{"),
    YEK("}"),
    SLSH("_"),
    DASIG(":="),
    FLECHA("->");

    // --------------------------------------------------------------------------------------------

    private final String symbol;

    // --------------------------------------------------------------------------------------------

    private TokenType(String symbol) {
        this.symbol = symbol;
    }

    // --------------------------------------------------------------------------------------------

    public String getSymbol() {
        return this.symbol;
    }

    // --------------------------------------------------------------------------------------------

    public boolean requiereLexema() {
        return this == ID || this == CTE || this == STR;
    }

    // --------------------------------------------------------------------------------------------

    private static final Map<String, TokenType> symbolLookup = new HashMap<>();

    /**
     * Se realiza un mapeo entre el lexema y su tipo de token correspondiente.
     */
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

        // Se verifica primero que no sea un identificador, constante o cadena.
        char firstChar = symbol.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return TokenType.ID;
        } else if (Character.isDigit(firstChar) || firstChar == '.') {
            return TokenType.CTE;
        } else if (firstChar == '\"') {
            return TokenType.STR;
        }

        return symbolLookup.get(symbol);
    }
}
