package semanticActions;
//AS1

import app.LexicalAnalyzer;

public class LexemaInitializer implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.setLexema("" + lexicalAnalyzer.getLastCharRead());
        System.out.println("AS1 executed.");
    }

}
