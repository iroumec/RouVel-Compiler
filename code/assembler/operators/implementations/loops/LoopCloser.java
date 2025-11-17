package assembler.operators.implementations.loops;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LoopCloser implements AssemblerOperator {

    private LoopCloser() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopCloser INSTANCE = new LoopCloser();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopCloser getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {
        repository.decreaseIndentation();
        repository.addCode("))\n");
    }
}
