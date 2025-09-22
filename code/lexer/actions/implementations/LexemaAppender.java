package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * LA:
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
    public void execute(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.appendToLexema(lexicalAnalyzer.getLastCharRead());
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "LA";
    }

}