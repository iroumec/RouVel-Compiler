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
        String symbolTableEntry = "";

        // Formateo de las columnas.
        String columnas = String.format(
                // Caracteres máximos.
                // 6 caracteres para el tipo de token.
                // 4 para el código de identificación.
                // 20 para el lexema.
                // 6 para la entrada en la tabla de símbolos.
                "%-6s %-4s %-20s",
                this.tokenType.toString(),
                this.getIdentificationCode(),
                lexema);

        // Se añaden corchetes al inicio y al final.
        return "[ " + columnas + " ]";
    }

}
