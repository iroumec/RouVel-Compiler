package lexer.errors;

import lexer.actions.FixedTokenFinalizer;
import lexer.actions.ReturnCharacterToEntry;

/**
 * Estado de error -7.
 * InvalidAssignmentOperator.
 */
public class InvalidAssignmentOperator implements lexer.LexicalError {

    private static InvalidAssignmentOperator INSTANCE;

    // --------------------------------------------------------------------------------------------

    private InvalidAssignmentOperator() {
    }

    // --------------------------------------------------------------------------------------------

    public static InvalidAssignmentOperator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InvalidAssignmentOperator();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(lexer.LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Operador de asignación inválido '"
                + lexicalAnalyzer.getLexema() + "'. "
                + "El operador correcto es ':='. Se insertará el caracter '='.");

        lexicalAnalyzer.appendToLexema("=");
        FixedTokenFinalizer.getInstance().execute(lexicalAnalyzer);
        ReturnCharacterToEntry.getInstance().execute(lexicalAnalyzer);

        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresFinalization() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -7. InvalidAssignmentOperator.";
    }

}
