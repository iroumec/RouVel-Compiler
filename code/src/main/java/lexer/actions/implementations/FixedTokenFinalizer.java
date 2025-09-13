package lexer.actions.implementations;

import common.Token;
import common.TokenType;
import lexer.Lexer;
import lexer.actions.SemanticAction;
import lexer.errors.implementations.UnknownToken;

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

        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);

        if (tokenType == null) {
            UnknownToken.getInstance().handleError(lexicalAnalyzer);
            return;
        }

        lexicalAnalyzer.setToken(new Token(tokenType, null));
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "FTF";
    }
}