package lexer.errors;

import lexer.LexicalAnalyzer;
import lexer.LexicalError;
import lexer.actions.FloatChecker;
import lexer.actions.ReturnCharacterToEntry;
import lexer.actions.VariableTokenFinalizer;

/**
 * Estado de error -5.
 * NoExponentSign.
 */
public class NoExponentSign implements LexicalError {

    private static NoExponentSign INSTANCE;

    // --------------------------------------------------------------------------------------------

    private NoExponentSign() {
    }

    // --------------------------------------------------------------------------------------------

    public static NoExponentSign getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoExponentSign();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void handleError(LexicalAnalyzer lexicalAnalyzer) {

        // Se obtienen los siguientes caracteres para ver si hay un dígito.
        StringBuilder number = new StringBuilder();
        char currentChar = lexicalAnalyzer.getLastCharRead();
        while (Character.isDigit(currentChar)) {
            number.append(currentChar);
            currentChar = lexicalAnalyzer.readNextChar();
        }

        // Si no prosigue con un dígito, se asume exponente 0 positivo.
        if (number.length() == 0) {
            number.append('0');
        }

        // Se asume exponente positivo.
        System.err.println("ERROR: Línea "
                + lexicalAnalyzer.getNroLinea()
                + ": Se debe especificar un signo para el exponente."
                + " Se asumirá que el exponente de interés es positivo: F+"
                + number + ".");
        number.insert(0, '+');
        lexicalAnalyzer.incrementErrorsDetected();

        // Se invocan a las acciones semánticas correspondientes.
        lexicalAnalyzer.appendToLexema(number.toString());
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
        return "Estado de error: -5. NoExponentSign.";
    }
}
