package assembler.operators.implementations.comparison;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;
import common.SymbolType;

public interface ComparisonOperator extends AssemblerOperator {

    // ============================================================================================

    @Override
    default void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol secondOperand = symbolTable.getSymbol(repository.popOperand());
        Symbol firstOperand = symbolTable.getSymbol(repository.popOperand());

        repository.addCode(getCode(firstOperand, SymbolType.UINT, repository));
        repository.addCode(getCode(secondOperand, SymbolType.UINT, repository));
        repository.addCode(this.getAssemblerComparator());
        repository.addCode("\n");
    }

    // ============================================================================================

    String getAssemblerComparator();

}
