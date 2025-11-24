package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import common.Symbol;
import utilities.Printer;
import common.SymbolTable;
import common.SymbolType;
import common.SymbolDirector;
import assembler.CodeRepository;

public class Multiplication extends ArithmeticOperator {

    private Multiplication() {
    }

    // ============================================================================================

    private static class Holder {
        private static final Multiplication INSTANCE = new Multiplication();
    }

    // ============================================================================================

    public static Multiplication getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValue().multiply(secondOperand.getValue());
        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {

                if (result.intValue() > MAX_UINT) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la multiplicación supera el valor admitido para enteros: '%s * %s'.",
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
                                    "ERROR DE COMPILACIÓN: El resultado de la multiplicación supera el rango: '%s * %s'.",
                                    firstOperand.getValue(), secondOperand.getValue()));
                    System.exit(1);
                }

                // Verificar si es menor que el mínimo (pero no cero)
                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la multiplicación es menor que el mínimo representable: '%s * %s'.",
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
        return "mul";
    }

    // ============================================================================================

    @Override
    protected void applyPreviosOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {
        // Empty intentionally...
    }

    // ============================================================================================

    @Override
    protected void applyPostOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository) {

        /*
         * String message = "RUNTIME ERROR: Overflow en multiplicación de enteros.";
         * 
         * Symbol messageSymbol = SymbolDirector.createNewString(message);
         * 
         * // Se agrega el string con el mensaje a la tabla de símbolos.
         * SymbolTable.getInstance().addEntry(messageSymbol);
         * 
         * repository.
         * addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))"
         * );
         */

        RuntimeControlsManager.addIntegerOverflowChecker(repository);
    }

}
