package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

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

        Symbol operand = SymbolTable.getInstance().getSymbol(operands.pop());

        String code = String.format(indentation + "i32.const %s %n", operand.getValue());
        code += String.format(indentation + "i32.const %d %n", operand.getLexema().length());
        code += String.format(indentation + "call $print %n");

        return code;
    }

}
