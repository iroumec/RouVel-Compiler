package assembler.operators.implementations.functions.call;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class FunctionCall implements AssemblerOperator {

    private FunctionCall() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionCall INSTANCE = new FunctionCall();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionCall getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol function = SymbolTable.getInstance().getSymbol(repository.popOperand());

        repository.addCode(String.format("call $%s %n", function.getLexemaWithoutScope()));
        repository.addCode("\n");
    }
}
