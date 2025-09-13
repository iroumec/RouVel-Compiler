package lexer.errors.implementations;

import lexer.Lexer;
import lexer.errors.LexicalError;

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
    public void handleError(Lexer lexicalAnalyzer) {
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
