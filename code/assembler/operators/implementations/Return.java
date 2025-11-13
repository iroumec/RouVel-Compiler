package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

public class Return implements AssemblerOperator {

    private Return() {
    }

    private static class Holder {
        private static final Return INSTANCE = new Return();
    }

    public static Return getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        Symbol operand = SymbolTable.getInstance().getSymbol(operands.pop());

        // En WebAssembly, el retorno de una función es el valor en el tope de
        // la pila.

        String out;

        // TODO: SOLO SE RETORNAN ENTEROS. ¿A ESO LO CHEQUEAMOS EN ALGÚN MOMENTO?
        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            out = indentation + String.format(indentation + "i32.const %s %n", operand.getValue());
        } else {
            out = indentation + String.format(indentation + "local.get $%s %n", operand.getLexemaWithoutScope());
        }

        // No es necesario hacer explícito el 'return', pero se incluyó
        // ya que se creé que aporta más legibilidad.
        out += indentation + "return";

        return out;
    }
}
