package lexicalAnalysis.semanticActions;

import general.SymbolTable;
import general.Token;
import general.TokenType;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * AS3:
 * Detecta palabras reservadas y literales, da de alta en la tabla de símbolos
 * los lexemas de constantes, identificadores y cadenas de caracteres y devuelve
 * el Token correspondiente.
 */
public class LexemaFinalizer implements SemanticAction {

    private static LexemaFinalizer INSTANCE;

    // --------------------------------------------------------------------------------------------

    private LexemaFinalizer() {
    }

    // --------------------------------------------------------------------------------------------

    public static LexemaFinalizer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexemaFinalizer();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

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
                System.out.println("ERROR: Línea " + lexicalAnalyzer.getNroLinea() +
                        ": El lexema '" + lexema
                        + "' no corresponde a una palabra reservada del lenguaje. Este se omitirá.");
                lexicalAnalyzer.incrementErrorsDetected();
                return;
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
