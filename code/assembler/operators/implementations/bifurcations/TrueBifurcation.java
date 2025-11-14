package assembler.operators.implementations.bifurcations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.arithmetic.Multiplication;
import common.Symbol;
import common.SymbolTable;

public class TrueBifurcation implements AssemblerOperator {

    private TrueBifurcation() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final TrueBifurcation INSTANCE = new TrueBifurcation();
    }

    // --------------------------------------------------------------------------------------------

    public static TrueBifurcation getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        String code = String.format(indentation + "br_if $L%s %n", operands.pop());
        code += String.format(indentation + "br $exit");

        return code;
    }
}
