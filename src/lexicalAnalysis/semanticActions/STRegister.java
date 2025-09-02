package lexicalAnalysis.semanticActions;
//AS6

import general.SymbolTable;
import general.Token;
import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;

public class STRegister implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();

        TokenType tokenType = lexicalAnalyzer.getDetectedType();
        String lex = lexicalAnalyzer.getLexema().substring(-1);

        Integer symbol = SymbolTable.agregarEntrada(tokenType, lex);

        if (tokenType != null) {
            lexicalAnalyzer.setToken(new Token(tokenType, symbol));
        }
    }
}