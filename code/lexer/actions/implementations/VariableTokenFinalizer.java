package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * VTF:
 * Detecta el tipo de token para identificadores, constantes y cadenas de
 * caracteres.
 * Da de alta en la tabla de simbolos.
 * Setea el token correspondiente.
 */
public class VariableTokenFinalizer implements SemanticAction {

    private static VariableTokenFinalizer INSTANCE;

    // --------------------------------------------------------------------------------------------

    private VariableTokenFinalizer() {
    }

    // --------------------------------------------------------------------------------------------

    public static VariableTokenFinalizer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VariableTokenFinalizer();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {

        lexicalAnalyzer.finalizeVariableToken();
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "VTF";
    }

}