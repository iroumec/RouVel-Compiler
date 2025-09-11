package lexer.actions;

import lexer.LexicalAnalyzer;
import lexer.SemanticAction;
import common.SymbolTable;
import common.TokenType;
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
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();
        TokenType tokenType = TokenType.fromSymbol(lexema);
        Integer symbolTableEntry = SymbolTable.getInstance().agregarEntrada(tokenType, lexema);
        lexicalAnalyzer.setToken(new Token(tokenType, symbolTableEntry));

    }

    // --------------------------------------------------------------------------------------------

    public String toString() {
        return "VTF";
    }

}