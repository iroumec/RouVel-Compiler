package lexer.errors.implementations;

import lexer.Lexer;
import lexer.actions.implementations.FloatChecker;
import lexer.actions.implementations.ReturnCharacterToEntry;
import lexer.actions.implementations.VariableTokenFinalizer;
import lexer.errors.LexicalError;

/**
 * Estado de error -6.
 * NoExponent.
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
    public void handleError(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.notifyError("""
                Se debe especificar un número entero como exponente. \
                Se asumirá que el exponente de interés es: F%s0. \
                """.formatted(lexicalAnalyzer.getLexema().split("F",
                2)[1])); // Obtención del signo.

        // Se invocan a las acciones semánticas correspondientes.
        lexicalAnalyzer.appendToLexema('0');
        FloatChecker.getInstance().execute(lexicalAnalyzer);
        VariableTokenFinalizer.getInstance().execute(lexicalAnalyzer);
        ReturnCharacterToEntry.getInstance().execute(lexicalAnalyzer);
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

    @Override
    public String toString() {
        return "Estado de error: -6. NoExponent.";
    }
}
