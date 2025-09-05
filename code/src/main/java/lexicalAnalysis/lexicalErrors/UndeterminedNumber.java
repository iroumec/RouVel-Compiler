package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;
import lexicalAnalysis.semanticActions.LexemaFinalizer;
import lexicalAnalysis.semanticActions.UintChecker;

/**
 * Estado de Error: -2.
 * UndeterminedNumber.
 */
public class UndeterminedNumber implements LexicalError {

    private static UndeterminedNumber INSTANCE;

    // --------------------------------------------------------------------------------------------

    private UndeterminedNumber() {
    }

    // --------------------------------------------------------------------------------------------

    public static UndeterminedNumber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UndeterminedNumber();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Número mal formado: '"
                + lexicalAnalyzer.getLexema() + "''."
                + "\n\t - De ser un entero, debe terminar con el sufijo 'UI'."
                + "\n\t - De ser un flotante y no tener parte decimal, se debe especificar un punto '.' al final."
                + "\nSe asumirá que es un entero y se agregará el sufijo 'UI'.");
        lexicalAnalyzer.incrementErrorsDetected();

        // Se realiza el agregado del sufijo y las acciones semánticas
        // necesarias para entregar el token.
        lexicalAnalyzer.appendToLexema("UI");
        UintChecker.getInstance().execute(lexicalAnalyzer);
        LexemaFinalizer.getInstance().execute(lexicalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresFinalization() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "Estado de Error: -2. UndeterminedNumber.";
    }

}
