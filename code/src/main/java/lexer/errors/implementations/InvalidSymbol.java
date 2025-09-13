package lexer.errors.implementations;

import lexer.Lexer;
import lexer.errors.LexicalError;

/**
 * Estado de error -1.
 * InvalidSymbol.
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
    public void handleError(Lexer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Símbolo inválido '"
                + lexicalAnalyzer.getLastCharRead() + "'. "
                + "El símbolo se omitirá.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -1. InvalidSymbol.";
    }

}
