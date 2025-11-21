package assembler.operators.implementations.functions.declaration;

import java.util.List;

import common.Symbol;
import common.SymbolType;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class FunctionCloser implements AssemblerOperator {

    private FunctionCloser() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionCloser INSTANCE = new FunctionCloser();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionCloser getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(repository.popOperand());

        StringBuilder code = new StringBuilder();
        String functionName = symbol.getLexemaWithoutScope();

        code.append(dumpResultsParameters(functionName));

        repository.addCode(code);
        repository.removeLastLine(); // Para que la llave no quede separada.
        repository.removeLastLine();
        // Si se cerró el bloque correspondiente a una función, se agrega
        // un unreachable. Esto es necesario para que wasm no dé errores
        // en el caso de tener un if-else, con return en ambas ramas, y que
        // no haya un retorno al final de la función.
        repository.addCode("unreachable");
        repository.decreaseIndentation();
        repository.addCode(")");
        repository.endBlock();
    }

    // --------------------------------------------------------------------------------------------

    private String dumpResultsParameters(String functionName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER);

        for (Symbol symbol : parameters) {

            code.append(String.format(";; Apilamiento del resultado del parámetro formal por CVR %s. %n",
                    symbol.getLexemaWithoutScope()));
            code.append(String.format("%s %n", getCode(symbol, SymbolType.UINT), symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }

}
