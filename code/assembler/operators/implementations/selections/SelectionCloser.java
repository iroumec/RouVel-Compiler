package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class SelectionCloser implements AssemblerOperator {
    private SelectionCloser() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final SelectionCloser INSTANCE = new SelectionCloser();
    }

    // --------------------------------------------------------------------------------------------

    public static SelectionCloser getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {
        repository.decreaseIndentation();
        repository.addCode("))\n");
    }
}
