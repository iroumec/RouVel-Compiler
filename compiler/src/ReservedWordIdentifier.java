//AS5
public class ReservedWordIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        String lexema = lexicalAnalyzer.getLexema().substring(-1);
        char lastChar = lexicalAnalyzer.getLastCharRead();
        String posiblePalabra = lexicalAnalyzer.getLexema();
        TokenType tokenType;

        if (lexicalAnalyzer.isReservedWord(posiblePalabra)) {
            tokenType = TokenType.fromSymbol(posiblePalabra);
        } else if (lexicalAnalyzer.isReservedWord(lexema)) {
            tokenType = TokenType.fromSymbol(lexema);
            lexicalAnalyzer.decrementarSiguienteCaracterALeer();
        }
    }
}