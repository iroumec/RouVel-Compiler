package lexer.errors;

import lexer.Lexer;

public interface LexicalError {

    public void handleError(Lexer lexicalAnalyzer);

    // --------------------------------------------------------------------------------------------

    public default boolean requiresFinalization() {
        return false;
    }

    // --------------------------------------------------------------------------------------------

    public default boolean requiresReturnToStart() {
        return false;
    }
}
