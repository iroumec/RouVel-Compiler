package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class WriteResult implements AssemblerOperator {

    private WriteResult() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final WriteResult INSTANCE = new WriteResult();
    }

    // --------------------------------------------------------------------------------------------

    public static WriteResult getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        return indent(getCode(symbol, SymbolType.UINT), indentation);
    }
}
