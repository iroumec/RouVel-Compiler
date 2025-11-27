package assembler.operators.implementations.functions.lambdas;

import java.util.List;

import common.Symbol;
import common.SymbolType;
import common.SymbolTable;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LambdaCloser implements AssemblerOperator {

    private LambdaCloser() {
    }

    // ============================================================================================

    private static class Holder {
        private static final LambdaCloser INSTANCE = new LambdaCloser();
    }

    // ============================================================================================

    public static LambdaCloser getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        dumpOutOfScopeParameters(repository);

        repository.decreaseIndentation();
        repository.addCode(")");
        repository.endBlock();
    }

    // ============================================================================================

    private void dumpOutOfScopeParameters(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        List<Symbol> outOfScopeVariables = symbolTable
                .getOutOfScopeVariables(symbolTable.getFunctionSymbol(
                        repository.getCurrentScope().replaceFirst("main", symbolTable.getProgramName())));

        for (Symbol symbol : outOfScopeVariables) {

            repository.addCode(String.format(";; Apilamiento del valor de las variables pertenecientes a otro ámbito.",
                    symbol.getLexemaWithoutScope()));
            repository.addCode(String.format("%s", getCode(symbol, SymbolType.UINT, repository)));

            repository.addCode("\n");
        }
    }

}
