package semanticActions;
//AS2

import app.LexicalAnalyzer;

public class LexemaAppender implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.appendToLexema(lexicalAnalyzer.getLastCharRead());

        System.out.println("AS2 executed.");
    }

}