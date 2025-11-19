package assembler.operators.implementations.arithmetic;

import common.Symbol;
import common.SymbolDirector;
import common.SymbolTable;
import utilities.Printer;

import java.io.UnsupportedEncodingException;
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

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "div_u";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        String message = "RUNTIME ERROR: División por cero.";

        Symbol messageSymbol = SymbolDirector.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(messageSymbol);

        repository.addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))");

        int messageLength = 0;
        try {
            // No puede usarse message.length(), porque no tiene en cuenta que en
            // WebAssembly la tilde ocupa dos caracteres.
            // TODO: llevar esto a otra clase.
            messageLength = message.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
                    unreachable
                ) ;; $continue
                """.formatted(secondOperand.getLexemaWithoutScope(), messageSymbol.getValue(),
                messageLength));
    }
}
