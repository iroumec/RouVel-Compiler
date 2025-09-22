package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * RCE:
 * Devuelve el último caracter leído a la entrada.
 */
public class ReturnCharacterToEntry implements SemanticAction {

    private static ReturnCharacterToEntry INSTANCE;

    // --------------------------------------------------------------------------------------------

    private ReturnCharacterToEntry() {
    }

    // --------------------------------------------------------------------------------------------

    public static ReturnCharacterToEntry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReturnCharacterToEntry();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "RCE";
    }

}
