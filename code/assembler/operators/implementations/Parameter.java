package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class Parameter implements AssemblerOperator {

    private Parameter() {
    }

    private static class Holder {
        private static final Parameter INSTANCE = new Parameter();
    }

    public static Parameter getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        // Se debe leer el tope de la pila y almacenarse en el parámetro.
        Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        String code = String.format(indentation + ";; Copia del valor del argumento en el parámetro %s. %n",
                symbol.getLexemaWithoutScope());
        code += String.format(indentation + "local.set $%s %n", symbol.getLexemaWithoutScope());

        return code;
    }

}
