package assembler.operators.implementations.selections;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

/**
 * Else.
 */
public class AlternativeOpener implements AssemblerOperator {
    private AlternativeOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final AlternativeOpener INSTANCE = new AlternativeOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static AlternativeOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {
        return ")(else";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 1 la indentación al salir del cuerpo del then.
     */
    @Override
    public int getExitIndentationChange() {
        return 1;
    }

    /**
     * Se incrementa en 1 la indentación al entrar al cuerpo del else.
     */
    @Override
    public int getEntryIndentationChange() {
        return 1;
    }
}
