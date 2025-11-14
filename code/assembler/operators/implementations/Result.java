package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Result implements AssemblerOperator {

    private Result() {
    }

    private static class Holder {
        private static final Result INSTANCE = new Result();
    }

    public static Result getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        return getCode(symbol, SymbolType.UINT, indentation);
    }
}
