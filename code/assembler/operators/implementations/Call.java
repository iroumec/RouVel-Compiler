package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;

public class Call implements Operator {

    private Call() {
    }

    private static class Holder {
        private static final Call INSTANCE = new Call();
    }

    public static Call getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        return "call " + operands.pop();
    }
}
