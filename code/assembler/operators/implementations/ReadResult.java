package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public class ReadResult implements AssemblerOperator {

    private ReadResult() {
    }

    private static class Holder {
        private static final ReadResult INSTANCE = new ReadResult();
    }

    public static ReadResult getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        // Se debe leer el tope de la pila y almacenarse en el parámetro.
        Symbol argument = SymbolTable.getInstance().getSymbol(operands.pop());
        Symbol parameter = SymbolTable.getInstance().getSymbol(operands.pop());

        String code = String.format(indentation + ";; Copia del valor del parámetro %s en el argumento %s. %n",
                parameter.getLexemaWithoutScope(), argument.getLexemaWithoutScope());
        code += String.format(indentation + "local.set $%s %n", argument.getLexemaWithoutScope());

        return code;
    }
}
