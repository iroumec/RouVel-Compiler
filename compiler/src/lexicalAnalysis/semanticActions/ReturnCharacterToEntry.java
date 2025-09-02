package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

public class ReturnCharacterToEntry implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();

        System.out.println("ReturnCharacterToEntry executed.");
    }

}
