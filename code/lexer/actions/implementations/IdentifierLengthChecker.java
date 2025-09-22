package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

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
    public void execute(Lexer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        if (lexema.length() > MAX_IDENTIFIER_LENGTH) {

            String truncatedLexema = lexema.substring(0, MAX_IDENTIFIER_LENGTH);

            lexicalAnalyzer.notifyWarning("""
                    El identificador %s excede la longitud máxima de %d \
                    caracteres. Se truncará a %s. \
                    """.formatted(lexema, MAX_IDENTIFIER_LENGTH, truncatedLexema));

            lexicalAnalyzer.setLexema(truncatedLexema);
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ILC";
    }
}