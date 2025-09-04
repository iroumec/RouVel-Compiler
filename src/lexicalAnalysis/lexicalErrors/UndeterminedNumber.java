package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;

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
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "ERNUM";
    }

}
