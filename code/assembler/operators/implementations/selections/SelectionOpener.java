package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import assembler.SelectionManager;

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

        SelectionManager selectionManager = SelectionManager.getInstance();

        selectionManager.increaseSelectionLevel();
        int level;

        if (selectionManager.getSelectionLevel() == 1) {

            level = 2;

            repository.addCode(String.format("(block $out%s%n",selectionManager.obtainOutValue()));
            repository.increaseIndentation();
            
        } else {

            level = 1;

        }

        repository.addCode(String.format("(block $else%s%n",selectionManager.obtainElseValue()));
        repository.increaseIndentation();
        repository.addCode(String.format("(block $then%s%n",selectionManager.obtainThenValue()));
        repository.increaseIndentation();
        
        selectionManager.pushSelectionLevel(level);

    }
}
