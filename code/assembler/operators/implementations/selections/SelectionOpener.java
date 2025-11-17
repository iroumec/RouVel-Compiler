package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class SelectionOpener implements AssemblerOperator {
    private SelectionOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final SelectionOpener INSTANCE = new SelectionOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static SelectionOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        repository.addCode("(if (then\n");
        repository.increaseIndentation();
    }
}
