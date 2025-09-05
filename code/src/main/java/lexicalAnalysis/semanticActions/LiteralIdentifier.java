package lexicalAnalysis.semanticActions;

import general.TokenType;
import general.Token;
import general.SymbolTable;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class LiteralIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);

        lexicalAnalyzer.setDetectedType(tokenType);

        // muy feo así?
        // lexicalAnalyzer.setDetectedType(TokenType.fromSymbol(lexicalAnalyzer.getLexema()));
    }
}