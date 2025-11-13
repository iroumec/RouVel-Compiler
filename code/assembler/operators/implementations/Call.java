package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class Call implements AssemblerOperator {

    private Call() {
    }

    private static class Holder {
        private static final Call INSTANCE = new Call();
    }

    public static Call getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        Symbol function = SymbolTable.getInstance().getSymbol(operands.pop());

        return indentation + "call $" + function.getLexemaWithoutScope();
    }
}
