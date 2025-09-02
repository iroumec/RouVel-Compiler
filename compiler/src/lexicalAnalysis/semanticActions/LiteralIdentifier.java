package lexicalAnalysis.semanticActions;

import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;

public class LiteralIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();
        char lastChar = lexicalAnalyzer.getLastCharRead();
        TokenType tokenType = null;

        if (lexicalAnalyzer.isLiteral(lexema)) {
            tokenType = TokenType.fromSymbol(posiblePalabra);
        } else if (lexicalAnalyzer.isLiteral(lexema.substring(-1))) {
            tokenType = TokenType.fromSymbol(lexema.substring(-1));
            lexicalAnalyzer.decrementarSiguienteCaracterALeer();
        }

        lexicalAnalyzer.setDetectedType(tokenType);
    }
}