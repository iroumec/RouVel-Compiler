package assembler.operators.implementations.functions.declaration;

import common.Symbol;
import common.SymbolType;
import common.SymbolTable;
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

        // En WebAssembly, el retorno simplemente se deja apilado en la pila.

        String out = ";; Retorno de la función.\n";
        out += getCode(operand, SymbolType.UINT);
        out += "return";

        repository.addCode(out);
    }
}
