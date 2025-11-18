package assembler.operators.implementations.arithmetic;

import common.Monitor;
import common.Symbol;
import common.SymbolTable;

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
    protected int applyOperation(int firstOperand, int secondOperand) {

        if (secondOperand == 0) {
            Monitor.getInstance().addError("División por cero");
        }

        return firstOperand / secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected float applyOperation(float firstOperand, float secondOperand) {
        return firstOperand / secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "div_u";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getRuntimeControls(Symbol firstOperand, Symbol secondOperand) {

        // Si se colocan los dos puntos, por alguna razón, no lo agrega al assembler.
        String message = "RUNTIME ERROR: División por cero.";

        Symbol messageSymbol = Symbol.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(message, messageSymbol);

        return """
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
                message.length() - 2);
    }
}
