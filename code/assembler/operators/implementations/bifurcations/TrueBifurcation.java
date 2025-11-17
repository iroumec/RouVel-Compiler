package assembler.operators.implementations.bifurcations;

import assembler.CodeRepository;
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
    public void generateAssembler(CodeRepository repository) {

        String code = String.format("br_if $L%s %n", repository.popOperand());
        code += "br $exit";

        repository.addCode(code);
    }
}
