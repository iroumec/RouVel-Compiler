package common;

import java.util.HashMap;
import java.util.Map;

import parser.Parser;

public enum TokenType {

    // --------------------------------------------------------------------------------------------
    // Tokens que pueden corresponder a más de un lexema. En este sentido, "null" no
    // indica la ausencia de lexema, sino la posibilidad de varios.
    // --------------------------------------------------------------------------------------------

    ID(null, Parser.ID),
    CTE(null, Parser.CTE),
    STR(null, Parser.STR),

    // --------------------------------------------------------------------------------------------
    // Operadores multicarácteres.
    // --------------------------------------------------------------------------------------------

    GT(">"),
    LT("<"),
    PyC(";"),
    ASIG("="),
    SUM("+"),
    RES("-"),
    MUL("*"),
    DIV("/"),
    PAR("("),
    RAP(")"),
    KEY("{"),
    YEK("}"),
    SLSH("_"),
    COMMA(","),

    // --------------------------------------------------------------------------------------------
    // Operadores monocarácter.
    // --------------------------------------------------------------------------------------------

    EQ("==", Parser.EQ),
    GEQ(">=", Parser.GEQ),
    LEQ("<=", Parser.LEQ),
    NEQ("!=", Parser.NEQ),
    DASIG(":=", Parser.DASIG),
    FLECHA("->", Parser.FLECHA),

    // --------------------------------------------------------------------------------------------
    // Palabras reservadas.
    // --------------------------------------------------------------------------------------------

    IF("if", Parser.IF),
    DO("do", Parser.DO),
    CVR("cvr", Parser.CVR),
    ELSE("else", Parser.ELSE),
    UINT("uint", Parser.UINT),
    PRINT("print", Parser.PRINT),
    WHILE("while", Parser.WHILE),
    ENDIF("endif", Parser.ENDIF),
    RETURN("return", Parser.RETURN);

    // --------------------------------------------------------------------------------------------

    private final String symbol;
    private final short identificationCode;

    // --------------------------------------------------------------------------------------------

    private TokenType(String symbol) {
        this(symbol, null);
    }

    // --------------------------------------------------------------------------------------------

    private TokenType(String symbol, Short identificationCode) {
        this.symbol = symbol;

        if (symbol.length() == 1) {

            if (identificationCode != null) {
                throw new IllegalArgumentException(
                        "El código de identificación para tokens de un único carácter se establece de forma automática mediante su código ASCII.");
            }

            identificationCode = (short) symbol.charAt(0); // Se obtiene el código ASCII del carácter.
        }

        this.identificationCode = identificationCode;
    }

    // --------------------------------------------------------------------------------------------

    public String getSymbol() {
        return this.symbol;
    }

    // --------------------------------------------------------------------------------------------

    public Short getIdentificationCode() {
        return this.identificationCode;
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
