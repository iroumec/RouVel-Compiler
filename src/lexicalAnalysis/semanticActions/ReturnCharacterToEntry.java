package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * ASR
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
