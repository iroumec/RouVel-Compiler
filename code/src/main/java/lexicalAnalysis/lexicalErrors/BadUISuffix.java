package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;
import lexicalAnalysis.semanticActions.LexemaFinalizer;
import lexicalAnalysis.semanticActions.UintChecker;

/**
 * Estado de Error: -3.
 * BadUISuffix.
 */
public class BadUISuffix implements LexicalError {

    private static BadUISuffix INSTANCE;

    // --------------------------------------------------------------------------------------------

    private BadUISuffix() {
    }

    // --------------------------------------------------------------------------------------------

    public static BadUISuffix getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BadUISuffix();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": El número: '"
                + lexicalAnalyzer.getLexema() + "' posee un sufijo inválido. "
                + "Se añadirá el sufijo 'I'.");
        lexicalAnalyzer.incrementErrorsDetected();

        // Agregado del sufijo y finalización del lexema.
        lexicalAnalyzer.appendToLexema('I');
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
        return "Estado de Error: -3. BadUISuffix.";
    }

}