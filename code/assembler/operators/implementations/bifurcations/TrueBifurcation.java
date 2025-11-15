package assembler.operators.implementations.bifurcations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

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
    public String getAssembler(Deque<String> operands) {

        String code = String.format("br_if $L%s %n", operands.pop());
        code += "br $exit";

        return code;
    }
}
