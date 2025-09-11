package lexer.actions;

import lexer.LexicalAnalyzer;
import lexer.SemanticAction;

/**
 * ILC
 */
public class IdentifierLengthChecker implements SemanticAction {

    private static IdentifierLengthChecker INSTANCE;
    private static final int MAX_IDENTIFIER_LENGTH = 20;

    // --------------------------------------------------------------------------------------------

    private IdentifierLengthChecker() {
    }

    // --------------------------------------------------------------------------------------------

    public static IdentifierLengthChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IdentifierLengthChecker();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        if (lexema.length() > MAX_IDENTIFIER_LENGTH) {
            System.out.println("WARNING: Línea " + lexicalAnalyzer.getNroLinea() + ": El identificador '" + lexema
                    + "' excede la longitud máxima de " + MAX_IDENTIFIER_LENGTH
                    + " caracteres. Se truncará a '"
                    + lexema.substring(0, MAX_IDENTIFIER_LENGTH) + "'.");
            lexicalAnalyzer.incrementWarningsDetected();
            lexema = lexema.substring(0, MAX_IDENTIFIER_LENGTH);

            lexicalAnalyzer.setLexema(lexema);
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ILC";
    }
}