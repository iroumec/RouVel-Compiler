package assembler.operators.implementations.loops;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class LoopCloser implements AssemblerOperator {

    private LoopCloser() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopCloser INSTANCE = new LoopCloser();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopCloser getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {
        return "))\n";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 2 la indentación al salir del cuerpo de la función.
     */
    @Override
    public int getExitIndentationChange() {
        return 1;
    }
}
