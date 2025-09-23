package lexer.actions.implementations;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * FC:
 */
public class FloatChecker implements SemanticAction {

    private static FloatChecker INSTANCE;

    private static final double MIN_POS_VAL = 1.17549435e-38;
    private static final double MAX_POS_VAL = 3.40282347e+38;
    private static final double MIN_NEG_VAL = -3.40282347e+38;
    private static final double MAX_NEG_VAL = -1.17549435e-38;

    // El número podría no venir en notación científica. El peor caso:
    // 1 cero antes del punto.
    // 1 punto.
    // 37 ceros después del punto.
    // 9 números después del último cero después del punto.
    // TOTAL: 48 caracteres.
    private static final int MAX_CARACTERES = 48;

    // --------------------------------------------------------------------------------------------

    private FloatChecker() {
    }

    // --------------------------------------------------------------------------------------------

    public static FloatChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FloatChecker();
        }
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(Lexer lexicalAnalyzer) {

        // Se remplaza la "F" por la "e" para que pueda ser tomado el número como
        // double.
        String lexema = lexicalAnalyzer.getLexema().replace("F", "e");

        double value;
        boolean outOfRange = false;

        // Si el número es demasiado grande para parsearlo
        if (lexema.length() > MAX_CARACTERES) {
            outOfRange = true;
            value = 0.0;
        } else {
            try {
                value = Double.parseDouble(lexema);
                if (!isInRange(value)) {
                    outOfRange = true;
                    value = 0.0;
                }
            } catch (NumberFormatException e) {
                outOfRange = true;
                value = 0.0;
            }
        }

        if (outOfRange) {
            lexicalAnalyzer.notifyWarning(
                    "Número flotante '%s' fuera de rango. Se asignará 0.0.".formatted(lexema));
        }

        // Se convierte a la notación del lenguaje.
        lexicalAnalyzer.setLexema(formatFloat(value));
    }

    // --------------------------------------------------------------------------------------------

    private boolean isInRange(double value) {

        return (value >= MIN_POS_VAL && value <= MAX_POS_VAL)
                || (value <= MAX_NEG_VAL && value >= MIN_NEG_VAL)
                || value == 0.0;
    }

    // --------------------------------------------------------------------------------------------

    private String formatFloat(double value) {

        // Para números muy grandes o muy pequeños, se utiliza notación científica.
        if ((Math.abs(value) > 1e7 || (Math.abs(value) < 1e-3 && value != 0.0))) {
            return String.format("%eF", value).replace("e", "F");
        }

        return String.valueOf(value);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "FC";
    }

}