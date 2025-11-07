package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;

public class Label implements Operator {

    private Label() {
    }

    private static class Holder {
        private static final Label INSTANCE = new Label();
    }

    public static Label getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        return operands.pop() + ":";
    }

}
