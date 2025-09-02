package lexicalAnalysis.semanticActions;

import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class LiteralIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = null;

        if (lexicalAnalyzer.isLiteral(lexema)) {
            tokenType = TokenType.fromSymbol(lexema);
        } else if (lexicalAnalyzer.isLiteral(lexema.substring(-1))) {
            tokenType = TokenType.fromSymbol(lexema.substring(-1));
            lexicalAnalyzer.decrementarSiguienteCaracterALeer();
        }

        lexicalAnalyzer.setDetectedType(tokenType);
    }
}