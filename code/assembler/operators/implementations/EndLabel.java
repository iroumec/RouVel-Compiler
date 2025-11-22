package assembler.operators.implementations;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class EndLabel implements AssemblerOperator {

    private EndLabel() {
    }

    // ============================================================================================

    private static class Holder {
        private static final EndLabel INSTANCE = new EndLabel();
    }

    // ============================================================================================

    public static EndLabel getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        repository.decreaseIndentation();
        repository.addCode(")\n");
    }
}
