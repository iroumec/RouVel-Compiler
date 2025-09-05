package lexicalAnalysis.semanticActions;

import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS4:
 * Detecta el TokenType para identificadores, constantes y cadenas de
 * caracteres.
 */
public class LexemaIdentifier implements SemanticAction {

    private static LexemaIdentifier INSTANCE;

    // --------------------------------------------------------------------------------------------

    private LexemaIdentifier() {
    }

    // --------------------------------------------------------------------------------------------

    public static LexemaIdentifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexemaIdentifier();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        char lastChar = lexicalAnalyzer.getLastCharRead();
        TokenType tokenType;
        if (Character.isUpperCase(lastChar)) {
            lexicalAnalyzer.setDetectedType(TokenType.ID);
        } else if (Character.isDigit(lastChar) || lastChar == '.') {
            lexicalAnalyzer.setDetectedType(TokenType.CTE);
        } else if (lastChar == '\"') {
            lexicalAnalyzer.setDetectedType(TokenType.STR);
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AS4";
    }

}
