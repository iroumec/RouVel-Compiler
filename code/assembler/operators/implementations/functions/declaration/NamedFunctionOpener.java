package assembler.operators.implementations.functions.declaration;

import assembler.operators.implementations.functions.FunctionOpener;

public final class NamedFunctionOpener extends FunctionOpener {

    // ============================================================================================

    private NamedFunctionOpener() {
    }

    // ============================================================================================

    private static class Holder {
        private static final NamedFunctionOpener INSTANCE = new NamedFunctionOpener();
    }

    // ============================================================================================

    public static NamedFunctionOpener getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    protected int getNumberOfReturns() {
        return 1;
    }

    // ============================================================================================
}
