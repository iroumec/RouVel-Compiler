package common;

public class Token {

    private final TokenType tokenType;
    private final String symbolTableKey;

    // --------------------------------------------------------------------------------------------

    public Token(TokenType tokenType, String lexema) {
        if (tokenType.requiereLexema() && lexema == null) {
            throw new IllegalArgumentException(
                    "El tipo de token especificado requiere de una entrada en la tabla.");
        }

        if (!tokenType.requiereLexema() && lexema != null) {
            throw new IllegalArgumentException(
                    "El tipo de token especificado no requiere de una entrada en la tabla.");
        }

        this.tokenType = tokenType;

        // Se agrega el lexema a la tabla de símbolos.
        if (lexema != null) {
            SymbolTable.getInstance().agregarEntrada(lexema);
        }

        this.symbolTableKey = lexema;
    }

    // --------------------------------------------------------------------------------------------

    public String getSymbolTableKey() {
        return this.symbolTableKey;
    }

    // --------------------------------------------------------------------------------------------

    public int getIdentificationCode() {
        return tokenType.getIdentificationCode();
    }

    // --------------------------------------------------------------------------------------------

    public boolean hasSymbolTableIndex() {
        return tokenType.requiereLexema();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String lexema = (this.symbolTableKey == null) ? "" : this.symbolTableKey;

        // Formateo de las columnas.
        String columnas = String.format(
                // Caracteres máximos.
                // 4 para el código de identificación.
                // 6 para el tipo de token.
                // 20 para el lexema.
                "%-4s %-6s %-20s",
                this.getIdentificationCode(),
                this.tokenType.toString(),
                lexema);

        // Se añaden corchetes al inicio y al final.
        return "[ " + columnas + " ]";
    }

}
