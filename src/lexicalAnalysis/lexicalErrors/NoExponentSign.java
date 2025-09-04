package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;
import lexicalAnalysis.semanticActions.FloatChecker;
import lexicalAnalysis.semanticActions.LexemaAppender;
import lexicalAnalysis.semanticActions.LexemaFinalizer;

/**
 * EREXP.
 * -3
 */
public class NoExponentSign implements LexicalError {

    private static NoExponentSign INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NoExponentSign() {
    }

    // --------------------------------------------------------------------------------------------

    public static NoExponentSign getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoExponentSign();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {

        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Se debe especificar un número decimal como exponente."
                + lexicalAnalyzer.getLastCharRead() + "'. "
                + "Se asumirá que el exponente de interés es: F"
                + lexicalAnalyzer.getLexema().split("F", 2)[1] // Obtención del signo.
                + "0.");
        lexicalAnalyzer.incrementErrorsDetected();

        // Se invocan a las acciones semánticas correspondientes.
        lexicalAnalyzer.appendToLexema('0');
        LexemaAppender.getInstance().execute(lexicalAnalyzer);
        FloatChecker.getInstance().execute(lexicalAnalyzer);
        LexemaFinalizer.getInstance().execute(lexicalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "EREXP";
    }
}
