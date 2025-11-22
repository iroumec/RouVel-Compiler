package assembler.operators.implementations.functions.lambdas;

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

        repository.decreaseIndentation();
        repository.addCode(")");
        repository.endBlock();
    }

}
