package assembler.operators.implementations.functions.declaration;

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

        // Si se cerró el bloque correspondiente a una función, se agrega
        // un unreachable. Esto es necesario para que wasm no dé errores
        // en el caso de tener un if-else, con return en ambas ramas, y que
        // no haya un retorno al final de la función.
        repository.addCode("unreachable");
        repository.decreaseIndentation();
        repository.addCode(")");
        repository.endBlock();
    }

}
