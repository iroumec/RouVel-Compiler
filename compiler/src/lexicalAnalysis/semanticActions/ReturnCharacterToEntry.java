package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

/**
 * ASR
 */
public class ReturnCharacterToEntry implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();

        System.out.println("ASR executed.");
    }

}
