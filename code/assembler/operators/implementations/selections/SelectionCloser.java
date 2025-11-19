package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.SelectionManager;
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

        SelectionManager selectionManager = SelectionManager.getInstance();

        selectionManager.decreaseSelectionLevel();

        repository.decreaseIndentation();
        repository.addCode(")");
        selectionManager.decreaseClosers();

        if (selectionManager.getSelectionLevel() == 0) {

            if (selectionManager.getClosers() > 0) {
                repository.addCode(String.format("br $out%s%n", selectionManager.getOutValue()));
                repository.decreaseIndentation();
                repository.addCode(")");
                selectionManager.decreaseClosers();
            }

            /*
             * while (selectionManager.getClosers() > 0) {
             * repository.decreaseIndentation();
             * repository.addCode(")");
             * selectionManager.decreaseClosers();
             * }
             */
        }
    }
}
