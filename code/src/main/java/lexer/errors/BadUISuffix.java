package lexer.errors;

import lexer.LexicalAnalyzer;
import lexer.LexicalError;
import lexer.actions.LexemaFinalizer;
import lexer.actions.ReturnCharacterToEntry;
import lexer.actions.UintChecker;

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
        return "Estado de Error: -3. BadUISuffix.";
    }

}