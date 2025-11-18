package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;
import utilities.Printer;

public class Subtraction extends ArithmeticOperator {

    private Subtraction() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Subtraction INSTANCE = new Subtraction();
    }

    // --------------------------------------------------------------------------------------------

    public static Subtraction getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValueAsBigDecimal().subtract(secondOperand.getValueAsBigDecimal());
        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {

                if (result.intValue() < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de una resta en la que participa un entero no puede ser negativo: '%s - %s'.",
                                    firstOperand.getLexema(), secondOperand.getLexema()));
                    System.exit(1);
                }

                repository.pushOperand(symbolTable.addUint(result.intValue()).getLexema());
            }
            case FLOAT_FLOAT -> {

                // Verificar si supera el máximo
                if (result.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la resta supera el rango: '%s - %s'.",
                                    firstOperand.getValueAsBigDecimal(), secondOperand.getValueAsBigDecimal()));
                    System.exit(1);
                }

                // Verificar si es menor que el mínimo (pero no cero)
                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la resta es menor que el mínimo representable: '%s - %s'.",
                                    firstOperand.getValueAsBigDecimal(), secondOperand.getValueAsBigDecimal()));
                    System.exit(1);
                }

                repository.pushOperand(symbolTable.addFloatEntry(String.valueOf(result.floatValue())).getLexema());
            }
        }

    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "sub";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Este chequeo ocasionalmente podría hacerse en el semántico, pero sería en
     * casos muy particulares, como A := 3UI - 8UI. Si se tiene algo como
     * A := B + 3UI - 8UI, ya no sería posible. Por consiguiente, se tomó la
     * decisión de realizarlo completamente en la pragmática.
     */
    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        String message = "RUNTIME ERROR: Resultado negativo.";

        Symbol messageSymbol = Symbol.createNewString(message);
        SymbolTable.getInstance().addEntry(message, messageSymbol);

        repository.addCode("""
                ;; Chequeo de resta negativa.
                ;; Si compara si el primer operando es menor al segundo.
                (block $continue

                    ;; Lectura del primer operando.
                    %s

                    ;; Lectura del segundo operando.
                    %s

                    ;; Se compara si el primer
                    ;; operando es menor al segundo.
                    i32.lt_s

                    ;; De ser el primer operando
                    ;; >= al segundo, se continua.
                    br_if $continue

                    ;; -------- ERROR --------
                    i32.const %s       ;; ptr
                    i32.const %d       ;; len
                    call $console_log_string
                    return
                ) ;; $continue
                """.formatted(
                getCode(firstOperand, SymbolType.UINT),
                getCode(secondOperand, SymbolType.UINT),
                messageSymbol.getValue(),
                message.length() - 2));

    }
}
