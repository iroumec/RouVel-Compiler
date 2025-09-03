package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS1:
 * Inicializa el string del lexema y agrega caracter leido.
 */
public class LexemaInitializer implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.initializeLexema(lexicalAnalyzer.getLastCharRead());
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AS1";
    }

}
