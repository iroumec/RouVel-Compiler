package assembler.operators.implementations.arithmetic;

import common.Symbol;
import common.SymbolDirector;
import common.SymbolTable;
import common.SymbolType;
import utilities.Printer;

import java.math.BigDecimal;

import assembler.CodeRepository;

public class Division extends ArithmeticOperator {

    private Division() {
    }

    // ============================================================================================

    private static class Holder {
        private static final Division INSTANCE = new Division();
    }

    // ============================================================================================

    public static Division getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        if (secondOperand.getValue().compareTo(BigDecimal.ZERO) == 0) {
            Printer.printWrapped(
                    String.format(
                            "ERROR DE COMPILACIÓN: El dividendo no puede ser cero: '%s / %s'.",
                            firstOperand.getLexema(), secondOperand.getLexema()));
            System.exit(1);
        }

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValue().add(secondOperand.getValue());

        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {

                // Esto solo puede ocurrir en el caso del par UINT_FLOAT.
                if (result.intValue() > MAX_UINT) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la división supera el valor admitido para enteros: '%s / %s'.",
                                    firstOperand.getLexema(), secondOperand.getLexema()));
                    System.exit(1);
                }

                Symbol symbol = SymbolDirector.createNewUint(result.intValue());
                symbolTable.addEntry(symbol);
                repository.pushOperand(symbol.getLexema());
            }
            case FLOAT_FLOAT -> {

                if (result.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la división supera el rango: '%s + %s'.",
                                    firstOperand.getValue(), secondOperand.getValue()));
                    System.exit(1);
                }

                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la división es menor que el mínimo representable: '%s + %s'.",
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
        return "div_u";
    }

    // ============================================================================================

    @Override
    protected void applyPreviosOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {

        RuntimeControlsManager.addZeroDividendChecker(repository);

        // Debido a que el último operando se envió como parámetro
        // a la función, debe volver a apilarse.
        repository.addCode(getCode(secondOperand, conversionType));
    }

    // ============================================================================================

    @Override
    protected void applyPostOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {

        // Se tira el tope de la pila.
        // Necesario ya que se usa "tee" siempre, y al no tener la división controles
        // post-operación, la pila siempre tiene algo al final y esta debe estar vacía
        // para terminar el programa.
        repository.addCode("drop");
    }
}
