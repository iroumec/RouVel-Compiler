package assembler.operators.implementations.selections;

import assembler.CodeRepository;
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
    public void generateAssembler(CodeRepository repository) {
        repository.decreaseIndentation();
        repository.addCode(")(else");
        repository.increaseIndentation();
    }
}
