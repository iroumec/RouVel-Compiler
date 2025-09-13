package lexer.errors.implementations;

import lexer.Lexer;
import lexer.actions.implementations.ReturnCharacterToEntry;
import lexer.errors.LexicalError;

/**
 * Estado de error -4.
 * InvalidDecimalFormat.
 */
public class InvalidDecimalFormat implements LexicalError {

    private static InvalidDecimalFormat INSTANCE;

    // --------------------------------------------------------------------------------------------

    private InvalidDecimalFormat() {
    }

    // --------------------------------------------------------------------------------------------

    public static InvalidDecimalFormat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InvalidDecimalFormat();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(Lexer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": El símbolo '.' es inválido a menos de que le siga la parte decimal de un número. "
                + "Este será omitido.");
        lexicalAnalyzer.incrementErrorsDetected();
        ReturnCharacterToEntry.getInstance().execute(lexicalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -4. InvalidDecimalFormat.";
    }

}
