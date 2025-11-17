package assembler.operators.implementations.functions.call;

import common.Symbol;
import common.SymbolTable;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class ReadResult implements AssemblerOperator {

    private ReadResult() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final ReadResult INSTANCE = new ReadResult();
    }

    // --------------------------------------------------------------------------------------------

    public static ReadResult getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        // Se debe leer el tope de la pila y almacenarse en el parámetro.
        Symbol argument = SymbolTable.getInstance().getSymbol(repository.popOperand());
        Symbol parameter = SymbolTable.getInstance().getSymbol(repository.popOperand());

        String code = String.format(";; Copia del valor del parámetro %s en el argumento %s. %n",
                parameter.getLexemaWithoutScope(), argument.getLexemaWithoutScope());
        code += String.format("local.set $%s %n", argument.getLexemaWithoutScope());

        repository.addCode(code);
    }
}
