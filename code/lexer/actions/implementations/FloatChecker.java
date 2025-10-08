package lexer.actions.implementations;

import java.math.BigDecimal;

import lexer.Lexer;
import lexer.actions.SemanticAction;

/**
 * FC:
 */
public class FloatChecker implements SemanticAction {

    private static FloatChecker INSTANCE;

    // Debido a que no se cuenta con el signo aún, solo puede efectuarse el chequeo
    // del rango considerando los valores absolutoss.
    private static final BigDecimal ABSOLUTE_MINIMUN = new BigDecimal("1.17549435E-38");
    private static final BigDecimal ABSOLUTE_MAXIMUM = new BigDecimal("3.40282347E38");

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

        String lexema = lexicalAnalyzer.getLexema();

        lexema = cleanFloat(lexema);

        lexicalAnalyzer.setLexema(parseToFloat(lexema, lexicalAnalyzer));
    }

    // --------------------------------------------------------------------------------------------

    private String cleanFloat(String lexema) {
        if (lexema.matches(".*F.*")) {
            String[] parts = lexema.split("F", 2);
            String number = parts[0].replaceFirst("^0+(?!\\.)", ""); // Elimina ceros a la izquierda.
            if (number.isEmpty() || number.equals("."))
                number = "0.0";
            String exponent = parts.length > 1 ? parts[1].replaceFirst("^0+", "") : "0";
            lexema = number + "E" + exponent;
        } else {
            // Se eliminan ceros a la derecha.
            lexema = lexema.replaceFirst("^0+(?!\\.)", "");
            // Si termina en punto, agrega un cero.
            if (lexema.endsWith("."))
                lexema += "0";
            // Si empieza con punto, agrega un cero al inicio.
            if (lexema.startsWith("."))
                lexema = "0" + lexema;
            // Si está vacío o es solo un punto, es 0.0.
            if (lexema.isEmpty() || lexema.equals("."))
                lexema = "0.0";
        }
        // Elimina ceros a la derecha del decimal, a menos de que sea el único dígito.
        if (lexema.contains(".")) {
            lexema = lexema.replaceFirst("(\\.\\d*?)0+$", "$1");
            // Si termina en punto, agrega un cero
            if (lexema.endsWith("."))
                lexema += "0";
        }
        return lexema;
    }

    // --------------------------------------------------------------------------------------------

    private String parseToFloat(String lexema, Lexer lexicalAnalyzer) {

        String number = transformToScientific(lexema);

        if (!isInRange(number)) {
            lexicalAnalyzer.notifyWarning("""
                    El número flotante %s está fuera del rango de representación. \
                    Se asignará el valor 0.0.
                    """.formatted(number));

        }

        // Se guarda con "e" como previsión del código intermedio.
        return number.replace("E", "e");
    }

    // --------------------------------------------------------------------------------------------

    private String transformToScientific(String number) {

        if (number.equals("0") || number.equals("0.0")) {
            return "0.0";
        }

        // Ya está en notación científica.
        if (number.contains("E") || number.contains("e")) {
            return number.toUpperCase();
        }

        // La parte entera del número debe ser 0 para convertirlo en notación
        // científica.
        if (number.startsWith("0.")) {
            String decPart = number.substring(2); // Parte después del "0.".
            int zeroCount = 0;
            int firstDigitIndex = -1;

            for (int i = 0; i < decPart.length(); i++) {
                if (decPart.charAt(i) == '0') {
                    zeroCount++;
                } else {
                    firstDigitIndex = i;
                    break;
                }
            }

            // Si solo hay ceros...
            if (firstDigitIndex == -1) {
                return "0.0";
            }

            char firstDigit = decPart.charAt(firstDigitIndex);
            int exponent = zeroCount + 1; // Cantidad de ceros antes del primer dígito.
            return firstDigit + ".0E-" + exponent;
        }

        return number;
    }

    // --------------------------------------------------------------------------------------------

    private boolean isInRange(String number) {

        BigDecimal value = new BigDecimal(number);

        return (value.compareTo(ABSOLUTE_MINIMUN) >= 0 && value.compareTo(ABSOLUTE_MAXIMUM) <= 0)
                || value.compareTo(BigDecimal.ZERO) == 0;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "FC";
    }

}