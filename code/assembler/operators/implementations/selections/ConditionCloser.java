package assembler.operators.implementations.selections;

import assembler.CodeRepository;
import assembler.SelectionManager;
import assembler.operators.AssemblerOperator;

public class ConditionCloser implements AssemblerOperator {
    private ConditionCloser() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final ConditionCloser INSTANCE = new ConditionCloser();
    }

    // --------------------------------------------------------------------------------------------

    public static ConditionCloser getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        repository.popOperand(); // Necesario para consumir la operando que corresponde al salto de la bifuración que nunca se utiliza.

        SelectionManager selectionManager = SelectionManager.getInstance();

        repository.addCode(String.format("br_if $then%s %nbr $else%s%n",selectionManager.getThenValue(),selectionManager.getElseValue()));
        repository.decreaseIndentation();
        repository.addCode(")");
        selectionManager.decreaseClosers();
    }
}