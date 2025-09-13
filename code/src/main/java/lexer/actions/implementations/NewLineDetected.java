package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * NLD:
 * Incrementa el número de línea.
 */
public class NewLineDetected implements SemanticAction {

    private static NewLineDetected INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NewLineDetected() {
    }

    // --------------------------------------------------------------------------------------------

    public static NewLineDetected getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewLineDetected();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.incrementarNroLinea();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "NLD";
    }
}