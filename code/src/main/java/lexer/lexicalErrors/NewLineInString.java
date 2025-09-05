package lexer.lexicalErrors;

import lexer.LexicalError;

/**
 * Estado de error -8.
 * NewLineInString.
 */
public class NewLineInString implements LexicalError {

    private static NewLineInString INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NewLineInString() {
    }

    // --------------------------------------------------------------------------------------------

    public static NewLineInString getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewLineInString();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(lexer.LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Las cadenas no pueden contener saltos de línea. "
                + "Este se omitirá.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -8. NewLineInString.";
    }

}
