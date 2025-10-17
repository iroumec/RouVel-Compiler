package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * FTF:
 * Detecta el tipo de token para palabras reservadas y operadores.
 * Detecta errores de palabra reservada no encontrada.
 * Setea el token correspondiente.
 */

public class FixedTokenFinalizer implements SemanticAction {

    private static FixedTokenFinalizer INSTANCE;

    // --------------------------------------------------------------------------------------------

    private FixedTokenFinalizer() {
    }

    // --------------------------------------------------------------------------------------------

    public static FixedTokenFinalizer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FixedTokenFinalizer();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {

        lexicalAnalyzer.finalizeFixedToken();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "FTF";
    }
}