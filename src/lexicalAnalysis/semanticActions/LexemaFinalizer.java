package lexicalAnalysis.semanticActions;

import general.SymbolTable;
import general.Token;
import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS3:
 * 
 */
public class LexemaFinalizer implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        // Resolución del token al llegar al estado de aceptación.
        Integer symbolTableEntry = null;
        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);

        // Se trata de un tipo de token que requiere de un lexema...
        if (tokenType == null && !lexema.isEmpty()) {
            // El tipo ya fue identificado con anterioridad por LexemaIdentifier.
            tokenType = lexicalAnalyzer.getDetectedType();

            if (tokenType == null) {
                throw new RuntimeException("Error en línea " + lexicalAnalyzer.getNroLinea() +
                        ": El lexema \"" + lexema + "\" no corresponde a una palabra reservada.");
            }

            symbolTableEntry = SymbolTable.agregarEntrada(tokenType, lexema);
        }

        if (tokenType != null) {
            lexicalAnalyzer.setToken(new Token(tokenType, symbolTableEntry));
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "AS3";
    }

}
