package lexicalAnalysis.lexicalErrors;

import lexicalAnalysis.LexicalError;

/**
 * Estado de error -9.
 * BadCommentInitialization.
 */
public class BadCommentInitialization implements LexicalError {

    private static BadCommentInitialization INSTANCE;

    // --------------------------------------------------------------------------------------------

    private BadCommentInitialization() {
    }

    // --------------------------------------------------------------------------------------------

    public static BadCommentInitialization getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BadCommentInitialization();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(lexicalAnalysis.LexicalAnalyzer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Los comentarios deben comenzar con '##' y terminar con '##'. Se encontró un único carácter '#'. "
                + "El símbolo será descartado. Esto puede traer errores en la interpretación del código si el carácter tenía la intención de comenzar un comentario.");
        lexicalAnalyzer.incrementErrorsDetected();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "Estado de Error: -9. BadCommentInitialization.";
    }

}
