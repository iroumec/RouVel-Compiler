package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;
import common.TokenType;
import common.SymbolTable;
import common.Token;

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

        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);
        Symbol simbolo = lexicalAnalyzer.getSimbolo();

        // Se agrega el lexema a la tabla de símbolos.
        SymbolTable.getInstance().agregarEntrada(lexema);

        lexicalAnalyzer.setToken(new Token(tokenType, lexema));
    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "VTF";
    }

}