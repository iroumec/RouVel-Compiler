package assembler.operators.implementations.arithmetic;

public class Sum extends ArithmeticOperator {

    private Sum() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Sum INSTANCE = new Sum();
    }

    // --------------------------------------------------------------------------------------------

    public static Sum getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected int applyOperation(int firstOperand, int secondOperand) {
        return firstOperand + secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected float applyOperation(float firstOperand, float secondOperand) {
        return firstOperand + secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "add";
    }
}
