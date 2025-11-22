package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.SelectionManager;
import assembler.operators.AssemblerOperator;

public class ThenBlockCloser implements AssemblerOperator {
    private ThenBlockCloser() {
    }

    // ============================================================================================

    private static class Holder {
        private static final ThenBlockCloser INSTANCE = new ThenBlockCloser();
    }

    // ============================================================================================

    public static ThenBlockCloser getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        SelectionManager selectionManager = SelectionManager.getInstance();

        repository.addCode(String.format("br $out%s", selectionManager.getOutValue()));

        repository.decreaseIndentation();
        repository.addCode(")");
        selectionManager.decreaseClosers();
    }
}