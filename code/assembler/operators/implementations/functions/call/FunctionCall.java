package assembler.operators.implementations.functions.call;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class FunctionCall implements AssemblerOperator {

    private FunctionCall() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionCall INSTANCE = new FunctionCall();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionCall getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {

        Symbol function = SymbolTable.getInstance().getSymbol(operands.pop());

        return String.format("call $%s %n", function.getLexemaWithoutScope());
    }
}
