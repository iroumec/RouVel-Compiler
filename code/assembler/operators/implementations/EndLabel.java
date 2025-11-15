package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class EndLabel implements AssemblerOperator {

    private EndLabel() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final EndLabel INSTANCE = new EndLabel();
    }

    // --------------------------------------------------------------------------------------------

    public static EndLabel getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {

        return ")\n";
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se decrementa en 1 la indentación al salir del cuerpo de la función.
     */
    @Override
    public int getExitIndentationChange() {
        return 1;
    }
}
