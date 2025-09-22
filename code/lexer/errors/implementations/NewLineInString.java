package lexer.errors.implementations;

import lexer.Lexer;
import lexer.errors.LexicalError;

/**
 * Estado de error -8.
 * NewLineInString.
 */
public class NewLineInString implements LexicalError {

    private static NewLineInString INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NewLineInString() {
    }

    // --------------------------------------------------------------------------------------------

    public static NewLineInString getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewLineInString();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.notifyError("""
                Las cadenas no pueden contener saltos de línea. \
                Este se descarta. \
                """);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -8. NewLineInString.";
    }

}
