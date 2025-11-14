package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.EndLoopLabel;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public class LessThan implements ComparisonOperator {

    private LessThan() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LessThan INSTANCE = new LessThan();
    }

    // --------------------------------------------------------------------------------------------

    public static LessThan getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.lt_u";
    }
}
