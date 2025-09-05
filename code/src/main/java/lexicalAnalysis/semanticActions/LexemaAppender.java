package lexicalAnalysis.semanticActions;
//AS2

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS2:
 * Agrega el caracter leído al string del lexema.
 */
public class LexemaAppender implements SemanticAction {

    private static LexemaAppender INSTANCE;

    // --------------------------------------------------------------------------------------------

    private LexemaAppender() {
    }

    // --------------------------------------------------------------------------------------------

    public static LexemaAppender getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexemaAppender();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

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