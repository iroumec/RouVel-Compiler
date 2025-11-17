package assembler.operators.implementations.loops;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LoopOpener implements AssemblerOperator {

    private LoopOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopOpener INSTANCE = new LoopOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        repository.addCode(String.format("(block $exit (loop $L%s %n", repository.popOperand()));
        repository.increaseIndentation();
    }
}
