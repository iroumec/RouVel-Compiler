package assembler.operators.implementations.comparison;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public interface ComparisonOperator extends AssemblerOperator {

    @Override
    default void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol secondOperand = symbolTable.getSymbol(repository.popOperand());
        Symbol firstOperand = symbolTable.getSymbol(repository.popOperand());

        String code = getCode(firstOperand, SymbolType.UINT);
        code += getCode(secondOperand, SymbolType.UINT);
        code += this.getAssemblerComparator();

        repository.addCode(code);
    }

    String getAssemblerComparator();

}
