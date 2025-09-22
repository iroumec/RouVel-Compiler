package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

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
    public void execute(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.initializeLexema(lexicalAnalyzer.getLastCharRead());
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "LI";
    }

}
