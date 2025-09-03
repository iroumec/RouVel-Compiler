package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * ASR
 */
public class ReturnCharacterToEntry implements SemanticAction {

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
