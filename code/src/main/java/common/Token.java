package common;

public class Token {

    private TokenType tokenType;
    private Integer symbolTableIndex;

    // --------------------------------------------------------------------------------------------

    public Token(TokenType tokenType, Integer symbolTableIndex) {

        if (tokenType.requiereLexema() && symbolTableIndex == null) {
            System.err.println("El tipo de token especificado requiere de una entrada en la tabla.");
            System.exit(1);
        }

        if (!tokenType.requiereLexema() && symbolTableIndex != null) {
            System.err.println("El tipo de token especificado no requiere de una entrada en la tabla.");
            System.exit(1);
        }

        this.tokenType = tokenType;
        this.symbolTableIndex = symbolTableIndex;
    }

    // --------------------------------------------------------------------------------------------

    public int getSymbolTableIndex() {

        if (symbolTableIndex == null) {
            throw new IllegalStateException("El token no tiene una entrada en la tabla de símbolos.");
        }

        return symbolTableIndex;
    }

    // --------------------------------------------------------------------------------------------

    public int getIdentificationCode() {
        return tokenType.getIdentificationCode();
    }

    // --------------------------------------------------------------------------------------------

    public boolean hasSymbolTableIndex() {
        return symbolTableIndex != null;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "["
                + this.tokenType.name()
                + "(" + this.getIdentificationCode() + ")"
                + (symbolTableIndex == null
                        ? ""
                        : ", " + SymbolTable.getInstance().getLexema(symbolTableIndex)
                                + ", ST(" + symbolTableIndex + ")")
                + "]";
    }

}
