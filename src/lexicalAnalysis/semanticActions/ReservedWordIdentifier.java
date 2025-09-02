package lexicalAnalysis.semanticActions;

import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class ReservedWordIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        String lexema = lexicalAnalyzer.getLexema().substring(-1);
        char lastChar = lexicalAnalyzer.getLastCharRead();
        String posiblePalabra = lexicalAnalyzer.getLexema();
        TokenType tokenType = null;

        if (lexicalAnalyzer.isReservedWord(posiblePalabra)) {
            tokenType = TokenType.fromSymbol(posiblePalabra);
        } else if (lexicalAnalyzer.isReservedWord(lexema)) {
            tokenType = TokenType.fromSymbol(lexema);
            lexicalAnalyzer.decrementarSiguienteCaracterALeer();
        } else {
            System.err.println("Error en la línea " + lexicalAnalyzer.getNroLinea() + ": La palabra '" + lexema
                    + "' no es una palabra reservada.");
        }

        lexicalAnalyzer.setDetectedType(tokenType);
    }
}