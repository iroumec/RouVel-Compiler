package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * UIC:
 * Se verifica que el número está dentro del rango de uint.
 */
public class UintChecker implements SemanticAction {

    private static UintChecker INSTANCE;

    private static final int MAX_UINT = 65535;

    // --------------------------------------------------------------------------------------------

    private UintChecker() {
    }

    // --------------------------------------------------------------------------------------------

    public static UintChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UintChecker();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        // Se eliminan ceros adicionales a la izquierda.
        lexema = cleanUint(lexema);

        if (!isInRange(lexema)) {

            lexicalAnalyzer.notifyWarning("""
                    El número %s está fuera del rango de uint. \
                    Se ajustará al máximo en el rango permitido \
                    [0, %d]. \
                    """.formatted(lexema, MAX_UINT));
            lexema = Integer.toString(MAX_UINT);
        }

        lexicalAnalyzer.setLexema(lexema);
    }

    // --------------------------------------------------------------------------------------------

    private String cleanUint(String lexema) {

        //Nos deshacemos del sufijo 'UI'
        String numberPart = lexema.substring(0, lexema.length() - 2);

        // (?!$) se asegura de dejar al menos un cero.
        numberPart = numberPart.replaceFirst("^0+(?!$)", "");

        return numberPart;
    }

    // --------------------------------------------------------------------------------------------

    private boolean isInRange(String lexema) {

        if (lexema.length() > String.valueOf(MAX_UINT).length()) {
            return false;
        }

        int number = Integer.parseInt(lexema);
        return number >= 0 && number <= MAX_UINT;

    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "UIC";
    }

}