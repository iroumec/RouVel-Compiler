package assembler.operators.implementations.functions.lambdas;

import common.Symbol;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LambdaOpener implements AssemblerOperator {

    private LambdaOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final LambdaOpener INSTANCE = new LambdaOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static LambdaOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(repository.popOperand());

        repository.startBlock(symbol.getScope() + ":" + symbol.getLexemaWithoutScope());

        repository.addCode(String.format("(func $%s", symbol.getLexemaWithoutScope()));

        repository.increaseIndentation();

        dumpParameters(repository);

        // Se agrega una etiqueta que, luego de haber determinado todas las variables
        // temporales que serán necesarias, se remplaza por su declaració
        repository.addCode("<local_variables>");
    }

    // --------------------------------------------------------------------------------------------

    private void dumpParameters(CodeRepository repository) {

        // Las funciones lambda solo tienen parámetro por CV.
        Symbol parameter = SymbolTable.getInstance().get(repository.getCurrentScope(), SymbolCategory.CV_PARAMETER)
                .getFirst();

        repository.addCode(String.format("(param $%s i32) %n", parameter.getLexemaWithoutScope()));
    }
}
