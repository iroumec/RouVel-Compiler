package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * NLD:
 * Incrementa el número de línea. Resetea el contador de caracter a cero.
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
        lexicalAnalyzer.resetearNroCaracter();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "NLD";
    }
}