package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

public class NewLineDetected implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.incrementarNroLinea();
    }

}