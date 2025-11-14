package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.EndLoopLabel;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public class LessThan implements AssemblerOperator {

    private LessThan() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LessThan INSTANCE = new LessThan();
    }

    // --------------------------------------------------------------------------------------------

    public static LessThan getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol secondOperand = symbolTable.getSymbol(operands.pop());
        Symbol firstOperand = symbolTable.getSymbol(operands.pop());

        String code = getCode(firstOperand, SymbolType.UINT);
        code += getCode(secondOperand, SymbolType.UINT);
        code += String.format("i32.lt_u %n");

        return indent(code, indentation);
    }
}
