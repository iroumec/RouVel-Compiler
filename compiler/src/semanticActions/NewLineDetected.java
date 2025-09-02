package semanticActions;

import app.LexicalAnalyzer;

public class NewLineDetected implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.incrementarNroLinea();
    }

}