package assembler.operators.implementations;

import java.util.Deque;
import java.util.List;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

public class LoopLabel implements AssemblerOperator {

    private LoopLabel() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LoopLabel INSTANCE = new LoopLabel();
    }

    // --------------------------------------------------------------------------------------------

    public static LoopLabel getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        StringBuilder code = new StringBuilder();

        code.append(String.format(indentation + "(block $exit %n"));

        code.append(String.format(indentation + " (loop $L%s %n", operands.pop()));

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se incrementa en 1 la indentación al entrar en el cuerpo de la función.
     */
    @Override
    public int getEntryIndentationChange() {
        return 2;
    }
}
