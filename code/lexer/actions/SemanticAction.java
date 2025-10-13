package lexer.actions;

import lexer.Lexer;

public interface SemanticAction {
    public void execute(Lexer lexicalAnalyzer);
}
