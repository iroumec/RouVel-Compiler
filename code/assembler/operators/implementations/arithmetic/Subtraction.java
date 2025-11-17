package assembler.operators.implementations.arithmetic;

import common.Symbol;

public class Subtraction extends ArithmeticOperator {

    private Subtraction() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Subtraction INSTANCE = new Subtraction();
    }

    // --------------------------------------------------------------------------------------------

    public static Subtraction getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected int applyOperation(int firstOperand, int secondOperand) {
        return firstOperand - secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected float applyOperation(float firstOperand, float secondOperand) {
        return firstOperand - secondOperand;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getAssemblerOperator() {
        return "sub";
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected String getRuntimeControls(Symbol firstOperand, Symbol secondOperand) {
        return "";
    }
}
