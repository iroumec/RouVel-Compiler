package lexicalAnalysis;

public interface LexicalError {

    public void handleError(LexicalAnalyzer lexicalAnalyzer);

    public default boolean requiresFinalization() {
        return false;
    }

    public default boolean requiresReturnToStart() {
        return false;
    }
}
