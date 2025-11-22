package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import common.Symbol;
import utilities.Printer;
import common.SymbolTable;
import common.SymbolType;
import common.SymbolDirector;
import assembler.CodeRepository;

public class Subtraction extends ArithmeticOperator {

    private Subtraction() {
    }

    // ============================================================================================

    private static class Holder {
        private static final Subtraction INSTANCE = new Subtraction();
    }

    // ============================================================================================

    public static Subtraction getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValue().subtract(secondOperand.getValue());
        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {

                if (result.intValue() < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de una resta en la que participa un entero no puede ser negativo: '%s - %s'.",
                                    firstOperand.getLexema(), secondOperand.getLexema()));
                    System.exit(1);
                }

                Symbol symbol = SymbolDirector.createNewUint(result.intValue());
                symbolTable.addEntry(symbol);
                repository.pushOperand(symbol.getLexema());
            }
            case FLOAT_FLOAT -> {

                // Verificar si supera el máximo
                if (result.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la resta supera el rango: '%s - %s'.",
                                    firstOperand.getValue(), secondOperand.getValue()));
                    System.exit(1);
                }

                // Verificar si es menor que el mínimo (pero no cero)
                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la resta es menor que el mínimo representable: '%s - %s'.",
                                    firstOperand.getValue(), secondOperand.getValue()));
                    System.exit(1);
                }

                Symbol symbol = SymbolDirector.createNewFloat(result.floatValue());
                symbolTable.addEntry(symbol);
                repository.pushOperand(symbol.getLexema());
            }
        }

    }

    // ============================================================================================

    @Override
    protected String getAssemblerOperator() {
        return "sub";
    }

    // ============================================================================================

    @Override
    protected void applyPreviosOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {
        // Empty intentionally...
    }

    // ============================================================================================

    /**
     * Este chequeo ocasionalmente podría hacerse en el semántico, pero sería en
     * casos muy particulares, como A := 3UI - 8UI. Si se tiene algo como
     * A := B + 3UI - 8UI, ya no sería posible. Por consiguiente, se tomó la
     * decisión de realizarlo completamente en la pragmática.
     */
    @Override
    protected void applyPostOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {

        RuntimeControlsManager.addIntegerNegativeSubtractionChecker(repository);
    }
}
