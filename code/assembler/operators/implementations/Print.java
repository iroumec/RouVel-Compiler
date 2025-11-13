package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class Print implements AssemblerOperator {

    private Print() {
    }

    private static class Holder {
        private static final Print INSTANCE = new Print();
    }

    public static Print getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        return "invoke StdOut, addr " + operands.pop(); // TODO: esto luego debe cambiarse.
    }

}
