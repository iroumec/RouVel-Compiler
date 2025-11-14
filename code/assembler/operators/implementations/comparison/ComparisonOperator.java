package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public interface ComparisonOperator extends AssemblerOperator {

    @Override
    default String getAssembler(Deque<String> operands, String indentation) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol secondOperand = symbolTable.getSymbol(operands.pop());
        Symbol firstOperand = symbolTable.getSymbol(operands.pop());

        String code = getCode(firstOperand, SymbolType.UINT);
        code += getCode(secondOperand, SymbolType.UINT);
        code += this.getAssemblerComparator();

        return indent(code, indentation);
    }

    String getAssemblerComparator();

}
