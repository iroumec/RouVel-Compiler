package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Equal implements AssemblerOperator {

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol secondOperand = symbolTable.getSymbol(operands.pop());
        Symbol firstOperand = symbolTable.getSymbol(operands.pop());

        String code = getCode(firstOperand, SymbolType.UINT, indentation);
        code += getCode(secondOperand, SymbolType.UINT, indentation);
        code += indentation + "i32.eq";

        return code;
    }
}
