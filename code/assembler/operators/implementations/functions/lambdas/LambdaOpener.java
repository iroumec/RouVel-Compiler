package assembler.operators.implementations.functions.lambdas;

import assembler.operators.implementations.functions.FunctionOpener;

public final class LambdaOpener extends FunctionOpener {

    // ============================================================================================

    private LambdaOpener() {
    }

    // ============================================================================================

    private static class Holder {
        private static final LambdaOpener INSTANCE = new LambdaOpener();
    }

    // ============================================================================================

    public static LambdaOpener getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    protected int getNumberOfReturns() {
        return 0;
    }

    // ============================================================================================

}
