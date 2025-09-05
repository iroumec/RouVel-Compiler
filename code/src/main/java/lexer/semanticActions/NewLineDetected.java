package lexer.semanticActions;

import lexer.LexicalAnalyzer;
import lexer.SemanticAction;

/**
 * ASN:
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
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.incrementarNroLinea();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ASN";
    }
}