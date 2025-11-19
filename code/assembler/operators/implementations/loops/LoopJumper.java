package assembler.operators.implementations.loops;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LoopJumper implements AssemblerOperator {

    private LoopJumper() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopJumper INSTANCE = new LoopJumper();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopJumper getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        String code = String.format("br_if $L%s %n", repository.popOperand());
        code += "br $exit";

        repository.addCode(code);
    }
}
