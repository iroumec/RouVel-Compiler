package lexicalAnalysis.semanticActions;

import java.math.BigDecimal;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

public class FloatChecker implements SemanticAction {

    private static final BigDecimal MIN_POS_VAL = new BigDecimal("1.17549435E-38");
    private static final BigDecimal MAX_POS_VAL = new BigDecimal("3.40282347E38");
    private static final BigDecimal MIN_NEG_VAL = new BigDecimal("-3.40282347E38");
    private static final BigDecimal MAX_NEG_VAL = new BigDecimal("-1.17549435E-38");

    // --------------------------------------------------------------------------------------------

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        lexema = cleanFloat(lexema);

        lexicalAnalyzer.setLexema(parseToFloat(lexema));
    }

    // --------------------------------------------------------------------------------------------

    private String cleanFloat(String lexema) {

        // Maneja notación con F
        if (lexema.contains("F")) {
            String[] parts = lexema.split("F", 2);
            // Elimina ceros a la izquierda y derecha del número
            String number = parts[0].replaceFirst("^0+(?!$)", "0").replaceFirst("0+$", "");
            lexema = number + "E" + parts[1];
        } else {
            // Elimina ceros a la izquierda y derecha del número
            lexema = lexema.replaceFirst("^0+(?!$)", "0").replaceFirst("0+$", "");
            // Asegura que no quede cadena vacía
            if (lexema.isEmpty() || lexema.equals(".")) {
                lexema = "0.0";
            }
        }

        return lexema;
    }

    // --------------------------------------------------------------------------------------------

    private String parseToFloat(String lexema) {

        String number = transformToScientific(lexema);

        if (!isInRange(number)) {
            System.out.println("WARNING: El número flotante " + number
                    + " está fuera del rango de representación. Se asignará el valor 0.0.");
            return "0.0";
        }

        return number.replace("E", "F");
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

        return (value.compareTo(MIN_POS_VAL) >= 0 && value.compareTo(MAX_POS_VAL) <= 0)
                || (value.compareTo(MAX_NEG_VAL) <= 0 && value.compareTo(MIN_NEG_VAL) >= 0)
                || value.compareTo(BigDecimal.ZERO) == 0;
    }

}