package assembler.operators.implementations.selections;

import java.util.Deque;

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
    public String getAssembler(Deque<String> operands) {
        return ")\n)\n";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 1 la indentación al salir del cuerpo del then o else.
     */
    @Override
    public int getExitIndentationChange() {
        return 1;
    }
}
