package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolTable;
import utilities.Printer;

public class Sum extends ArithmeticOperator {

    private Sum() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Sum INSTANCE = new Sum();
    }

    // --------------------------------------------------------------------------------------------

    public static Sum getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValueAsBigDecimal().add(secondOperand.getValueAsBigDecimal());

        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {

                if (result.intValue() > MAX_UINT) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la suma supera el valor admitido para enteros: '%s + %s'.",
                                    firstOperand.getLexema(), secondOperand.getLexema()));
                    System.exit(1);
                }

                if (pairType == PairType.UINT_FLOAT) {

                    if (result.intValue() < 0) {
                        Printer.printWrapped(
                                String.format(
                                        "ERROR DE COMPILACIÓN: El resultado de la suma no puede ser negativo: '%s + %s'.",
                                        firstOperand.getLexema(), secondOperand.getLexema()));
                        System.exit(1);
                    }
                }

                repository.pushOperand(symbolTable.addUint(result.intValue()).getLexema());
            }
            case FLOAT_FLOAT -> {

                // Verificar si supera el máximo
                if (result.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la suma supera el rango admitido: %s + %s > %s.",
                                    firstOperand.getValueAsBigDecimal(), secondOperand.getValueAsBigDecimal(),
                                    ABSOLUTE_MAXIMUM));
                    System.exit(1);
                }

                // Verificar si es menor que el mínimo (pero no cero)
                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la suma es menor que el mínimo representable: %s + %s < -%s.",
                                    firstOperand.getValueAsBigDecimal(), secondOperand.getValueAsBigDecimal(),
                                    ABSOLUTE_MINIMUN));
                    System.exit(1);
                }

                repository.pushOperand(symbolTable.addFloatEntry(String.valueOf(result.floatValue())).getLexema());
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "add";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {
        // Empty.
    }
}
