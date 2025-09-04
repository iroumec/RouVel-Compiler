package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalError;

/**
 * ERSTR.
 * -3
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
    public void handleError(lexicalAnalysis.LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Las cadenas no pueden contener saltos de línea. "
                + "Este se omitirá.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "ERSTR";
    }

}
