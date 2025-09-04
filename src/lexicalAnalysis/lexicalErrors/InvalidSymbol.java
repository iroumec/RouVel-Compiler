package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;

/**
 * ERS.
 * -1
 */
public class InvalidSymbol implements LexicalError {

    private static InvalidSymbol INSTANCE;

    // --------------------------------------------------------------------------------------------

    private InvalidSymbol() {
    }

    // --------------------------------------------------------------------------------------------

    public static InvalidSymbol getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InvalidSymbol();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Símbolo inválido '"
                + lexicalAnalyzer.getLastCharRead() + "'. "
                + "El símbolo será descartado.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "ERS";
    }

}
