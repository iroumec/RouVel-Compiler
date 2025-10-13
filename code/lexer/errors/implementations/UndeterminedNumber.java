package lexer.errors.implementations;

import lexer.actions.implementations.ReturnCharacterToEntry;
import lexer.actions.implementations.UintChecker;
import lexer.actions.implementations.VariableTokenFinalizer;

import lexer.Lexer;
import lexer.errors.LexicalError;

/**
 * Estado de Error: -2.
 * UndeterminedNumber.
 */
public class UndeterminedNumber implements LexicalError {

    private static UndeterminedNumber INSTANCE;

    // --------------------------------------------------------------------------------------------

    private UndeterminedNumber() {
    }

    // --------------------------------------------------------------------------------------------

    public static UndeterminedNumber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UndeterminedNumber();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(Lexer lexicalAnalyzer) {
        lexicalAnalyzer.notifyError("""
                Número mal formado: '%s'. \
                De ser un entero, debe terminar con el sufijo 'UI'. \
                Por otro lado, de ser un flotante y no tener parte decimal, se debe especificar un punto '.' al final. \
                Se asumirá que es un entero y se agregará el sufijo 'UI'. \
                """.formatted(lexicalAnalyzer.getLexema()));

        // Se realiza el agregado del sufijo y las acciones semánticas
        // necesarias para entregar el token.
        lexicalAnalyzer.appendToLexema("UI");
        UintChecker.getInstance().execute(lexicalAnalyzer);
        VariableTokenFinalizer.getInstance().execute(lexicalAnalyzer);
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
        return "Estado de Error: -2. UndeterminedNumber.";
    }

}
