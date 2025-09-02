package semanticActions;
//AS4

import app.LexicalAnalyzer;
import app.TokenType;

public class LexemaIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        char lastChar = lexicalAnalyzer.getLastCharRead();
        TokenType tokenType;
        if (Character.isUpperCase(lastChar)) {
            lexicalAnalyzer.setDetectedType(TokenType.ID);
        } else if (Character.isDigit(lastChar)) {
            lexicalAnalyzer.setDetectedType(TokenType.CTE);
        } else if (lastChar == '\"') {
            lexicalAnalyzer.setDetectedType(TokenType.STR);
        }

        System.out.println("AS4 executed.");
    }

}
