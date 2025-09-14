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

    @Override
    public String toString() {
        return "["
                + this.tokenType.name()
                + (symbolTableIndex == null
                        ? ""
                        : ", " + symbolTableIndex + ", " + SymbolTable.getInstance().getLexema(symbolTableIndex))
                + "]";
    }

}
