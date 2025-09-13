package lexer.errors.implementations;

import lexer.Lexer;
import lexer.errors.LexicalError;
import lexer.actions.implementations.ReturnCharacterToEntry;

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
    public void handleError(Lexer lexicalAnalyzer) {
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Los comentarios deben comenzar con '##' y terminar con '##'. Se encontró un único carácter '#'. "
                + "El símbolo será descartado. Esto puede traer errores en la interpretación del código si el carácter tenía la intención de comenzar un comentario.");
        lexicalAnalyzer.incrementErrorsDetected();
        ReturnCharacterToEntry.getInstance().execute(lexicalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public boolean requiresReturnToStart() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Estado de Error: -9. BadCommentInitialization.";
    }

}
