package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class GreaterOrEqual implements ComparisonOperator {

    private GreaterOrEqual() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final GreaterOrEqual INSTANCE = new GreaterOrEqual();
    }

    // --------------------------------------------------------------------------------------------

    public static GreaterOrEqual getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.ge_u";
    }
}
