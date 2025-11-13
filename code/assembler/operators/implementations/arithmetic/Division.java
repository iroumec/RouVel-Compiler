package assembler.operators.implementations.arithmetic;

public class Division extends ArithmeticOperator {

    private Division() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Division INSTANCE = new Division();
    }

    // --------------------------------------------------------------------------------------------

    public static Division getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected int applyOperation(int firstOperand, int secondOperand) {
        return firstOperand / secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected float applyOperation(float firstOperand, float secondOperand) {
        return firstOperand / secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "div_u"; // TODO: verificar esto. Puede variar.
    }
}
