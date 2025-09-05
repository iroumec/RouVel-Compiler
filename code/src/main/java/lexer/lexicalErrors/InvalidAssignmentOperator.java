package lexer.lexicalErrors;

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
                + "El operador correcto es ':='. Se descartará el símbolo ':'.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -7. InvalidAssignmentOperator.";
    }

}
