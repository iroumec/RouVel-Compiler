package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;
import lexicalAnalysis.semanticActions.FloatChecker;
import lexicalAnalysis.semanticActions.LexemaAppender;
import lexicalAnalysis.semanticActions.LexemaFinalizer;

/**
 * EREXP.
 * -4
 */
public class NoExponent implements LexicalError {

    private static NoExponent INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NoExponent() {
    }

    // --------------------------------------------------------------------------------------------

    public static NoExponent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoExponent();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {

        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Se debe especificar un número entero como exponente. "
                + "Se asumirá que el exponente de interés es: F"
                + lexicalAnalyzer.getLexema().split("F", 2)[1] // Obtención del signo.
                + "0.");
        lexicalAnalyzer.incrementErrorsDetected();

        // Se invocan a las acciones semánticas correspondientes.
        lexicalAnalyzer.appendToLexema('0');
        FloatChecker.getInstance().execute(lexicalAnalyzer);
        LexemaFinalizer.getInstance().execute(lexicalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    /**
     * El handler realiza las acciones semánticas necesarias,
     * por lo que el token queda listo para ser entregado.
     */
    @Override
    public boolean requiresFinalization() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "EREXP";
    }
}
