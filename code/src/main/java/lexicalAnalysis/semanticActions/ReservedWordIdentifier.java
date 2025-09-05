package lexicalAnalysis.semanticActions;

import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class ReservedWordIdentifier implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);

        if (tokenType == null) {
            System.err.println("Error en la línea " + lexicalAnalyzer.getNroLinea() + ": La palabra '" + lexema
                    + "' no es una palabra reservada.");
        } else {
            lexicalAnalyzer.setDetectedType(tokenType);
        }
    }
}