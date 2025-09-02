package semanticActions;
//AS6

//10 -> F
//8 -> F
//5 -> F
//3 -> F
//11 -> F

import app.LexicalAnalyzer;
import app.SymbolTable;
import app.Token;
import app.TokenType;

public class STRegister implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.decrementarSiguienteCaracterALeer();

        TokenType tokenType = lexicalAnalyzer.getDetectedType();
        String lex = lexicalAnalyzer.getLexema();

        Integer symbol = SymbolTable.agregarEntrada(tokenType, lex);

        if (tokenType != null) {
            lexicalAnalyzer.setToken(new Token(tokenType, symbol));
        }
    }
}