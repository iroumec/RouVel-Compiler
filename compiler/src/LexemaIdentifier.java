//AS4
public class LexemaIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        System.out.println("AS4 executed.");
        char lastChar = lexicalAnalyzer.getLastCharRead();
        TokenType tokenType;
        if (Character.isUpperCase(lastChar)) {
            tokenType = TokenType.ID;
        } else if (Character.isDigit(lastChar)) {
            tokenType = TokenType.CTE;
        } else if (lastChar == '\"') {
            tokenType = TokenType.STR;
        }

        lexicalAnalyzer.setDetectedType(tokenType);
    }

}
