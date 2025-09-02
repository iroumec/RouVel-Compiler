package lexicalAnalysis.semanticActions;
//AS1

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class LexemaInitializer implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.initializeLexema(lexicalAnalyzer.getLastCharRead());
    }

}
