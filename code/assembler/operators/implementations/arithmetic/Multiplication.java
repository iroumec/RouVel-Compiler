package assembler.operators.implementations.arithmetic;

public class Multiplication extends ArithmeticOperator {

    private Multiplication() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Multiplication INSTANCE = new Multiplication();
    }

    // --------------------------------------------------------------------------------------------

    public static Multiplication getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected int applyOperation(int firstOperand, int secondOperand) {
        return firstOperand * secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected float applyOperation(float firstOperand, float secondOperand) {
        return firstOperand * secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "mul";
    }

}
