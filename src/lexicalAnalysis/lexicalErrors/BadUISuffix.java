package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.LexicalError;

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
                + ": Falta el sufijo 'U' o 'I' en el número entero '"
                + lexicalAnalyzer.getLexema() + "'. "
                + "Se asumirá el sufijo 'I' por defecto.");
        lexicalAnalyzer.incrementErrorsDetected();
        lexicalAnalyzer.setDetectedType(general.TokenType.ENTERO);
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "ERUI";
    }

}