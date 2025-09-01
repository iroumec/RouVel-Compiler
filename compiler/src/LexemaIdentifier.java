public class LexemaIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        char lastChar = lexicalAnalyzer.getLastCharRead();
        TokenType tokenType;
        if (Character.isUpperCase(lastChar)) {
            tokenType = TokenType.ID;
        } else if (Character.isDigit(lastChar)) {
            tokenType = TokenType.CTE;
        }
    }

}
