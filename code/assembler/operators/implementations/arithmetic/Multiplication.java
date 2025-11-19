package assembler.operators.implementations.arithmetic;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolDirector;
import common.SymbolTable;
import utilities.Printer;

public class Multiplication extends ArithmeticOperator {

    private Multiplication() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Multiplication INSTANCE = new Multiplication();
    }

    // --------------------------------------------------------------------------------------------

    public static Multiplication getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "mul";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        String message = "RUNTIME ERROR: Overflow en multiplicación de enteros.";

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

        /*
         * El "param i32" es necesario para explicitarle al bloque que debe
         * tomar lo que había antes de la pila. En otro caso, el bloque
         * tiene su propia pila, la cual arranca vacía.
         */
        repository.addCode("""
                ;; Chequeo de overflow de enteros.
                (block $continue (param i32)
                    ;; ¿Es el resultado mayor al máximo entero?
                    i32.const %s
                    i32.le_u
                    br_if $continue ;; Si es menor al máximo entero, se sale del bloque.

                    ;; -------- OVERFLOW --------
                    i32.const %s     ;; ptr
                    i32.const %d    ;; len
                    call $console_log_string
                    return
                ) ;; $continue
                """.formatted(MAX_UINT, messageSymbol.getValue(),
                messageLength));
    }

}
