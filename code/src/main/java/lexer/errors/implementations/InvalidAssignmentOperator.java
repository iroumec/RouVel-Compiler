package lexer.errors.implementations;

import lexer.actions.implementations.FixedTokenFinalizer;
import lexer.actions.implementations.ReturnCharacterToEntry;

/**
 * Estado de error -7.
 * InvalidAssignmentOperator.
 */
public class InvalidAssignmentOperator implements lexer.errors.LexicalError {

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
    public void handleError(lexer.Lexer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Operador de asignación inválido '"
                + lexicalAnalyzer.getLexema() + "'. "
                + "El operador correcto es ':='. Se insertará el caracter '='.");

        lexicalAnalyzer.incrementErrorsDetected();

        // Ejecución de acciones semánticas reparadores.
        lexicalAnalyzer.appendToLexema("=");
        FixedTokenFinalizer.getInstance().execute(lexicalAnalyzer);
        ReturnCharacterToEntry.getInstance().execute(lexicalAnalyzer);
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
