public class Token {

    private TokenType tokenType;
    private Integer symbolTableEntry;

    public Token(TokenType tokenType) {
        this(tokenType, null);
    }

    public Token(TokenType tokenType, Integer symbolTableEntry) {

        if (tokenType.requiereLexema() && symbolTableEntry == null) {
            System.err.println("El tipo de token especificado no requiere de una entrada en la tabla.");
        }

        if (!tokenType.requiereLexema() && symbolTableEntry != null) {
            System.err.println("El tipo de token especificado no requiere de una entrada en la tabla.");
        }

        this.tokenType = tokenType;
        this.symbolTableEntry = symbolTableEntry;
    }

    @Override
    public String toString() {
        return "["
                + this.tokenType.name()
                + (symbolTableEntry == null
                        ? ""
                        : ", " + SymbolTable.getLexema(symbolTableEntry))
                + "]";
    }

}
