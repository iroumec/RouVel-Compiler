package assembler.operators.implementations.comparison;

public class LessOrEqual implements ComparisonOperator {

    private LessOrEqual() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LessOrEqual INSTANCE = new LessOrEqual();
    }

    // --------------------------------------------------------------------------------------------

    public static LessOrEqual getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.le_u";
    }
}
