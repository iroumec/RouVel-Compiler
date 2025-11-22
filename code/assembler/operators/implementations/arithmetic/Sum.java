package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import assembler.CodeRepository;
import assembler.WebAssemblyExporter;
import common.Symbol;
import common.SymbolDirector;
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
        BigDecimal result = firstOperand.getValue().add(secondOperand.getValue());

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

                Symbol symbol = SymbolDirector.createNewUint(result.intValue());
                symbolTable.addEntry(symbol);
                repository.pushOperand(symbol.getLexema());
            }
            case FLOAT_FLOAT -> {

                // Verificar si supera el máximo
                if (result.compareTo(ABSOLUTE_MAXIMUM) > 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la suma supera el rango admitido: %s + %s > %s.",
                                    firstOperand.getValue(), secondOperand.getValue(),
                                    ABSOLUTE_MAXIMUM));
                    System.exit(1);
                }

                // Verificar si es menor que el mínimo (pero no cero)
                if (result.compareTo(BigDecimal.ZERO) != 0 &&
                        result.compareTo(ABSOLUTE_MINIMUN.negate()) < 0) {
                    Printer.printWrapped(
                            String.format(
                                    "ERROR DE COMPILACIÓN: El resultado de la suma es menor que el mínimo representable: %s + %s < -%s.",
                                    firstOperand.getValue(), secondOperand.getValue(),
                                    ABSOLUTE_MINIMUN));
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
        return "add";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        String message = "RUNTIME ERROR: Overflow en suma de enteros.";

        Symbol messageSymbol = SymbolDirector.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(messageSymbol);

        repository.addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))");

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
                    unreachable
                ) ;; $continue
                """.formatted(MAX_UINT, messageSymbol.getValue(),
                WebAssemblyExporter.getAppropiateMessageLength(message)));
    }
}