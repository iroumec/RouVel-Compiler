package assembler.operators.implementations.functions.declaration;

import common.Symbol;
import common.SymbolCategory;
import common.SymbolType;
import common.SymbolTable;

import java.util.List;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class Return implements AssemblerOperator {

    private Return() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Return INSTANCE = new Return();
    }

    // --------------------------------------------------------------------------------------------

    public static Return getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol operand = SymbolTable.getInstance().getSymbol(repository.popOperand());

        // Previo a cada retorno, deben retornarse los valores correspondientes
        // a los parámetros por CVR.
        repository.addCode(dumpResultsParameters(repository.getCurrentScope()));

        // En WebAssembly, el retorno simplemente se deja apilado en la pila.
        repository.addCode(";; Retorno de la función.");
        repository.addCode(getCode(operand, SymbolType.UINT));
        repository.addCode("return");
    }

    // --------------------------------------------------------------------------------------------

    private String dumpResultsParameters(String functionName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER);

        for (Symbol symbol : parameters) {

            code.append(String.format("%n;; Apilamiento del resultado del parámetro formal por CVR %s. %n",
                    symbol.getLexemaWithoutScope()));
            code.append(String.format("%s %n", getCode(symbol, SymbolType.UINT), symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }
}
