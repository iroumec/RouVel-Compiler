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

        String lexema = "";
        String symbolTableEntry = "";

        if (symbolTableIndex != null) {
            lexema = SymbolTable.getInstance().getLexema(symbolTableIndex);
            symbolTableEntry = "ST(" + symbolTableIndex + ")";
        }

        // Formateo de las columnas.
        String columnas = String.format(
                // Caracteres máximos.
                // 6 caracteres para el tipo de token.
                // 4 para el código de identificación.
                // 20 para el lexema.
                // 6 para la entrada en la tabla de símbolos.
                "%-6s %-4s %-20s %-6s",
                this.tokenType.toString(),
                this.getIdentificationCode(),
                lexema,
                symbolTableEntry);

        // Se añaden corchetes al inicio y al final.
        return "[ " + columnas + " ]";
    }

}
