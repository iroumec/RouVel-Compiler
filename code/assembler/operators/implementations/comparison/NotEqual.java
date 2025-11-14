package assembler.operators.implementations.comparison;

public class NotEqual implements ComparisonOperator {

    private NotEqual() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final NotEqual INSTANCE = new NotEqual();
    }

    // --------------------------------------------------------------------------------------------

    public static NotEqual getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.ne";
    }
}
