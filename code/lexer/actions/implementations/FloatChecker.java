package lexer.actions.implementations;

import java.util.Locale;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * FC:
 */
public class FloatChecker implements SemanticAction {

    private static FloatChecker INSTANCE;

    // La f le especifica a Java que el literal es un float ya que, por defecto,
    // los literales de punto flotante son double.
    private static final float MIN_POS_VAL = 1.17549435e-38f;
    private static final float MAX_POS_VAL = 3.40282347e+38f;
    private static final float MIN_NEG_VAL = -3.40282347e+38f;
    private static final float MAX_NEG_VAL = -1.17549435e-38f;
    private static final float A = 12333333333333334354444444444444.0f;

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

        float value;
        boolean outOfRange = false;

        // Si el número es demasiado grande para parsearlo
        if (lexema.length() > MAX_CARACTERES) {
            outOfRange = true;
            value = 0.0f;
        } else {
            try {
                value = Float.parseFloat(lexema);
                if (!isInRange(value)) {
                    outOfRange = true;
                    value = 0.0f;
                }
            } catch (NumberFormatException e) {
                outOfRange = true;
                value = 0.0f;
            }
        }

        if (outOfRange) {
            lexicalAnalyzer.notifyWarning(
                    "Número flotante '%s' fuera de rango. Se asignará 0.0.".formatted(lexema));
        }

        System.out.println(A);

        // Se convierte a la notación del lenguaje.
        lexicalAnalyzer.setLexema(String.valueOf(value).replace("E", "F"));
    }

    // --------------------------------------------------------------------------------------------

    private boolean isInRange(double value) {

        return (value >= MIN_POS_VAL && value <= MAX_POS_VAL)
                || (value <= MAX_NEG_VAL && value >= MIN_NEG_VAL)
                || value == 0.0;
    }

    // --------------------------------------------------------------------------------------------

    private String formatFloat(double value) {

        if (value == 0.0) {
            return "0.0";
        }

        // Para números muy grandes o muy pequeños.
        if (Math.abs(value) > 1e7 || Math.abs(value) < 1e-3) {

            // Locale.ROOT ---> Idioma neutro. Siempre se separan los decimales con ".".
            // Se asegura de que la representación no cambie según la máquina.

            // "%.ne" muestra hasta n decimales después del punto y antes del exponente.
            return String.format(Locale.ROOT, "%.38e", value)
                    .replace("e", "F")
                    .replaceAll("0+F", "F") // Se quitan ceros antes de F.
                    .replace(".F", ".0F") // Se deja siempre .0 si no hay decimales.
                    .replaceAll("F([+-])0+(\\d)", "F$1$2"); // Se quitan ceros del valor del exponente (09 -> 9).
        }

        return String.valueOf(value).replace("e", "F");
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "FC";
    }

}