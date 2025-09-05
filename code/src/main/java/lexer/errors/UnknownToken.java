package lexer.errors;

import lexer.LexicalError;

/**
 * UnknownToken.
 */
public class UnknownToken implements LexicalError {

    private static UnknownToken INSTANCE;

    // --------------------------------------------------------------------------------------------

    private UnknownToken() {
    }

    // --------------------------------------------------------------------------------------------

    public static UnknownToken getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnknownToken();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(lexer.LexicalAnalyzer lexicalAnalyzer) {
        System.out.println("ERROR: Línea " + lexicalAnalyzer.getNroLinea()
            + ": El lexema '" + lexicalAnalyzer.getLexema()
            + "' no corresponde a una palabra reservada del lenguaje. Este se omitirá.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error. UnknownToken";
    }

}
