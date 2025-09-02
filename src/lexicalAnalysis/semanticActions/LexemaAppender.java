package lexicalAnalysis.semanticActions;
//AS2

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class LexemaAppender implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.appendToLexema(lexicalAnalyzer.getLastCharRead());
    }

}