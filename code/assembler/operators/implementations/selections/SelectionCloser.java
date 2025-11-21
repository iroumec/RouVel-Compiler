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
        int closers = selectionManager.popLevel();
        
        if (selectionManager.getSelectionLevel() == 0) {
            while (closers > 0) {
                repository.decreaseIndentation();
                repository.addCode(")");
                closers--;
            }
        } else {
            closers--;
            if (closers >= 0) {
                repository.decreaseIndentation();
                repository.addCode(")");
            }   
        }
            
    }
}
