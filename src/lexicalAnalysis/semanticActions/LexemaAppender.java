package lexicalAnalysis.semanticActions;
//AS2

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS2:
 * Agrega el caracter leído al string del lexema.
 */
public class LexemaAppender implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.appendToLexema(lexicalAnalyzer.getLastCharRead());
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AS2";
    }

}