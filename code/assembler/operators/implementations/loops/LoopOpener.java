package assembler.operators.implementations.loops;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class LoopOpener implements AssemblerOperator {

    private LoopOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopOpener INSTANCE = new LoopOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {

        return String.format("(block $exit (loop $L%s %n", operands.pop());
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se incrementa en 1 la indentación al entrar en el cuerpo de la función.
     */
    @Override
    public int getEntryIndentationChange() {
        return 1;
    }
}
