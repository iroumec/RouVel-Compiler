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
        return ")\n(else";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 2 la indentación al salir del cuerpo de la función.
     */
    @Override
    public int getEntryIndentationChange() {
        return 0;
    }
}
