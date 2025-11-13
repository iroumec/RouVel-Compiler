package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class Label implements AssemblerOperator {

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

        // Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        StringBuilder code = new StringBuilder();

        // TODO: ver como poner los parametros
        code.append(String.format("(func $%s (param $%s i32) (result i32)", operands.pop(), ""));

        return code.append("\n").toString();
    }

}
