package lexicalAnalysis.semanticActions;
//AS6

import general.SymbolTable;
import general.Token;
import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class STRegister implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        TokenType tokenType = lexicalAnalyzer.getDetectedType();
        String lex = lexicalAnalyzer.getLexema();

        Integer symbol = SymbolTable.agregarEntrada(tokenType, lex);

        if (tokenType != null) {
            lexicalAnalyzer.setToken(new Token(tokenType, symbol));
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AS6";
    }
}