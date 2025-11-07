package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;

public class Assignment implements Operator {

    private Assignment() {
    }

    private static class Holder {
        private static final Assignment INSTANCE = new Assignment();
    }

    public static Assignment getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        String firstOperand = operands.pop();
        String secondOperand = operands.pop();

        // Se mueve el valor del segundo operando al primero.
        return String.format("MOV _%s, %s", firstOperand, secondOperand);
    }
}
