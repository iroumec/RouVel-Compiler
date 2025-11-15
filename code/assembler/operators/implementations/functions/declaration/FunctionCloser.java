package assembler.operators.implementations.functions.declaration;

import java.util.Deque;
import java.util.List;

import assembler.Dumper;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

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
    public String getAssembler(Deque<String> operands) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        StringBuilder code = new StringBuilder();
        String functionName = symbol.getLexemaWithoutScope();

        code.append(dumpResultsParameters(functionName));

        String results = Dumper.dumpBlockVariables(functionName);

        if (!results.isBlank()) {
            code.append("\n").append(results);
        }

        return code.append(")\n").toString();
    }

    // --------------------------------------------------------------------------------------------

    private String dumpResultsParameters(String functionName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER);

        for (Symbol symbol : parameters) {

            code.append(String.format("    ;; Apilamiento del resultado del parámetro formal por CVR %s. %n",
                    symbol.getLexemaWithoutScope()));
            code.append(String.format("    %s %n", getCode(symbol, SymbolType.UINT), symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 1 la indentación al salir del cuerpo de la función.
     */
    @Override
    public int getExitIndentationChange() {
        return 1;
    }

}
