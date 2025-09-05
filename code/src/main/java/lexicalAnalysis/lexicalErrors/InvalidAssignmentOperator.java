package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;

/**
 * Estado de error -7.
 * InvalidAssignmentOperator.
 */
public class InvalidAssignmentOperator implements lexicalAnalysis.LexicalError {

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
    public void handleError(lexicalAnalysis.LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Operador de asignación inválido '"
                + lexicalAnalyzer.getLexema() + "'. "
                + "El operador correcto es ':='. Se descartará el símbolo ':'.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "Estado de Error: -7. InvalidAssignmentOperator.";
    }

}
