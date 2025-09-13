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
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Número mal formado: '"
                + lexicalAnalyzer.getLexema() + "''."
                + "\n\t - De ser un entero, debe terminar con el sufijo 'UI'."
                + "\n\t - De ser un flotante y no tener parte decimal, se debe especificar un punto '.' al final."
                + "\nSe asumirá que es un entero y se agregará el sufijo 'UI'.");
        lexicalAnalyzer.incrementErrorsDetected();

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
