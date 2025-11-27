package common;

import java.math.BigDecimal;

public class SymbolDirector {

    // ============================================================================================

    private static int stringCounter = 0;
    private static int auxiliarVariableNumber = 0;

    // ============================================================================================

    public static Symbol createProgram(String programName) {

        return new SymbolBuilder(programName).category(SymbolCategory.PROGRAM).build();
    }

    // ============================================================================================

    public static Symbol createNewString(String lexema) {

        String finalLexema = lexema;

        if (!lexema.startsWith("\"")) {
            finalLexema = "\"" + finalLexema;
        }

        if (!lexema.endsWith("\"")) {
            finalLexema = finalLexema + "\"";
        }

        Symbol symbol = new SymbolBuilder(finalLexema).value(new BigDecimal(stringCounter))
                .category(SymbolCategory.CONSTANT)
                .type(SymbolType.STRING).build();

        // Se incrementa el string counter de acuerdo al número de caracteres
        // en el string que se creo. Esto es útil para el assembler, para saber dónde
        // comenzar a gaurdar cada String y que estos no se pisen.
        stringCounter += finalLexema.length() - 2;

        return symbol;
    }

    // ============================================================================================

    public static Symbol createNewUint(int value) {

        return createNewUint(String.valueOf(value));
    }

    // ============================================================================================

    public static Symbol createNewUint(String value) {

        return new SymbolBuilder(value + "UI").value(new BigDecimal(value)).category(SymbolCategory.CONSTANT)
                .type(SymbolType.UINT).build();
    }

    // ============================================================================================

    public static Symbol createNewFunction(String functionName) {

        return new SymbolBuilder(functionName).category(SymbolCategory.FUNCTION)
                .type(SymbolType.UINT).build();
    }

    // ============================================================================================

    public static Symbol createNewFloat(float value) {

        return createNewFloat(String.valueOf(value));
    }

    // ============================================================================================

    public static Symbol createNewFloat(String value) {

        return new SymbolBuilder(value).value(new BigDecimal(value.replace("F", "e"))).category(SymbolCategory.CONSTANT)
                .type(SymbolType.FLOAT).build();
    }

    // ============================================================================================

    public static Symbol createNewVariable(String lexema) {

        return new SymbolBuilder(lexema).category(SymbolCategory.VARIABLE)
                .type(SymbolType.UINT).build();
    }

    // ============================================================================================

    public static Symbol createNewParameter(String lexema) {

        return createNewParameter(lexema, ParameterSemanticModel.CV);
    }

    // ============================================================================================

    public static Symbol createNewParameter(String lexema, ParameterSemanticModel parameterSemanticModel) {

        return new SymbolBuilder(lexema).category(parameterSemanticModel.getSymbolCategory())
                .type(SymbolType.UINT).build();
    }

    // ============================================================================================

    public static Symbol createNewAuxiliarVariable(String scope) {

        String lexema = "aux" + (auxiliarVariableNumber++) + ":" + scope;

        return new SymbolBuilder(lexema).category(SymbolCategory.AUXILIAR_VARIABLE)
                .type(SymbolType.UINT).build();
    }

    // ============================================================================================

    public static Symbol getNegativeVersion(Symbol symbol) {

        return symbol.getNegative();
    }

    // ============================================================================================

    public static Symbol createNewAppropiateSymbol(String lexema, String value, SymbolType type) {

        if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
            return SymbolDirector.createNewString(lexema);
        }

        BigDecimal finalValue = null;
        SymbolCategory category = null;

        // Solo las constantes tienen su tipo distinto de null.
        if (type != null) {
            category = SymbolCategory.CONSTANT;
        }

        if (!value.isBlank()) {
            finalValue = new BigDecimal(value);

        }

        return new SymbolBuilder(lexema).value(finalValue).category(category).type(type).build();
    }
}
