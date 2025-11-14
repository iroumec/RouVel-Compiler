package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class EndLoopLabel implements AssemblerOperator {

    private EndLoopLabel() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final EndLoopLabel INSTANCE = new EndLoopLabel();
    }

    // --------------------------------------------------------------------------------------------

    public static EndLoopLabel getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        String code = String.format(indentation + " ) %n");
        code += String.format(indentation + ") %n");

        return code;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 2 la indentación al salir del cuerpo de la función.
     */
    @Override
    public int getExitIndentationChange() {
        return 2;
    }
}
