package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class Call implements AssemblerOperator {

    private Call() {
    }

    private static class Holder {
        private static final Call INSTANCE = new Call();
    }

    public static Call getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        return indentation + "call " + operands.pop();
    }
}
