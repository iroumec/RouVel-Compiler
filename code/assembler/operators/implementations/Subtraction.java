package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;

public class Subtraction implements Operator {

    private Subtraction() {
    }

    private static class Holder {
        private static final Subtraction INSTANCE = new Subtraction();
    }

    public static Subtraction getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAssembler'");
    }
}
