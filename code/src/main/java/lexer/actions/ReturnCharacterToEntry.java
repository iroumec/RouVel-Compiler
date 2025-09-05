package lexer.actions;

import lexer.LexicalAnalyzer;
import lexer.SemanticAction;

/**
 * ASR:
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
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ASR";
    }

}
