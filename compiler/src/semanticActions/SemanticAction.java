package semanticActions;

import app.LexicalAnalyzer;

public interface SemanticAction {
    public void execute(LexicalAnalyzer lexicalAnalyzer);
}
