package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;
import common.SymbolTable;

public class Sum implements Operator {

    private Sum() {
    }

    private static class Holder {
        private static final Sum INSTANCE = new Sum();
    }

    public static Sum getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        String firstOperand = operands.pop();
        String secondOperand = operands.pop();

        // int firstOperandValue = SymbolTable.getInstance().get

        SymbolTable.getInstance().removeEntry(firstOperand);
        SymbolTable.getInstance().removeEntry(secondOperand);
        // SymbolTable.getInstance().addEntry(, null);

        return "";
    }
}
