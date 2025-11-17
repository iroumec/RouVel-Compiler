package assembler.operators.implementations.selections;

import java.util.Deque;
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
    public String getAssembler(Deque<String> operands) {

        return "(if \n(then \n";

        //return String.format("(block $exit (loop $L%s %n", operands.pop());
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se incrementa en 1 la indentación al entrar en el cuerpo de la función.
     */
    @Override
    public int getEntryIndentationChange() {
        return 0;
    }
}
