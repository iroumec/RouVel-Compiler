package assembler.operators.implementations.comparison;

public class Greater implements ComparisonOperator {

    private Greater() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Greater INSTANCE = new Greater();
    }

    // --------------------------------------------------------------------------------------------

    public static Greater getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.gt_u";
    }
}
