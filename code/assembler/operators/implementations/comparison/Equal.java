package assembler.operators.implementations.comparison;

public class Equal implements ComparisonOperator {

    private Equal() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Equal INSTANCE = new Equal();
    }

    // --------------------------------------------------------------------------------------------

    public static Equal getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssemblerComparator() {
        return "i32.eq";
    }
}
