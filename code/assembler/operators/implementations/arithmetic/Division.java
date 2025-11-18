package assembler.operators.implementations.arithmetic;

import common.Symbol;
import common.SymbolTable;
import utilities.Printer;

import java.math.BigDecimal;

import assembler.CodeRepository;

public class Division extends ArithmeticOperator {

    private Division() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Division INSTANCE = new Division();
    }

    // --------------------------------------------------------------------------------------------

    public static Division getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository) {

        if (secondOperand.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) == 0) {
            Printer.printWrapped(
                    String.format(
                            "ERROR DE COMPILACIÓN: El dividendo no puede ser cero: '%s / %s'.",
                            firstOperand.getLexema(), secondOperand.getLexema()));
            System.exit(1);
        }

        SymbolTable symbolTable = SymbolTable.getInstance();
        BigDecimal result = firstOperand.getValueAsBigDecimal().add(secondOperand.getValueAsBigDecimal());

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

                repository.pushOperand(symbolTable.addUint(result.intValue()).getLexema());
            }
            case FLOAT_FLOAT -> {

                BigDecimal absoluteResult = result.abs();

                if (absoluteResult.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la división supera el rango: '%s + %s'.",
                                    firstOperand, secondOperand));
                    System.exit(1);
                }

                if (absoluteResult.compareTo(BigDecimal.ZERO) != 0 &&
                        absoluteResult.compareTo(ABSOLUTE_MINIMUN) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la división es menor que el mínimo representable: '%s + %s'.",
                                    firstOperand, secondOperand));
                    System.exit(1);
                }

                repository.pushOperand(symbolTable.addFloat(result.floatValue()).getLexema());
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "div_u";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        // Si se colocan los dos puntos, por alguna razón, no lo agrega al assembler.
        String message = "RUNTIME ERROR: División por cero.";

        Symbol messageSymbol = Symbol.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(message, messageSymbol);

        repository.addCode("""
                ;; Chequeo de división por cero.
                (block $continue
                    ;; ¿Es el denominador igual a 0?
                    local.get $%s
                    br_if $continue ;; Si no es cero -> continuar.

                    ;; -------- ERROR --------
                    i32.const %s     ;; ptr
                    i32.const %d    ;; len
                    call $console_log_string
                    return
                ) ;; $continue
                """.formatted(secondOperand.getLexemaWithoutScope(), messageSymbol.getValue(),
                message.length() - 2));
    }
}
