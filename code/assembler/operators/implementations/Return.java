package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;

public class Return implements Operator {

    private Return() {
    }

    private static class Holder {
        private static final Return INSTANCE = new Return();
    }

    public static Return getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        return "";// "ret " + operands.pop(); // TODO: no estoy seguro de si esto está bien.
    }
}
