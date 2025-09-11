package lexer.actions;

import lexer.LexicalAnalyzer;
import lexer.SemanticAction;

/**
 * LI:
 * Inicializa el string del lexema y agrega caracter leido.
 */
public class LexemaInitializer implements SemanticAction {

    private static LexemaInitializer INSTANCE;

    // --------------------------------------------------------------------------------------------

    private LexemaInitializer() {
    }

    // --------------------------------------------------------------------------------------------

    public static LexemaInitializer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexemaInitializer();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.initializeLexema(lexicalAnalyzer.getLastCharRead());
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "LI";
    }

}
