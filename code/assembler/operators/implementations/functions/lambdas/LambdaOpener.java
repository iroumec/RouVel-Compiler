package assembler.operators.implementations.functions.lambdas;

import common.Symbol;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class LambdaOpener implements AssemblerOperator {

    private LambdaOpener() {
    }

    // ============================================================================================

    private static class Holder {
        private static final LambdaOpener INSTANCE = new LambdaOpener();
    }

    // ============================================================================================

    public static LambdaOpener getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol symbol = symbolTable.getSymbol(repository.popOperand());

        repository.startBlock(symbol.getScope() + ":"
                + symbol.getLexemaWithoutScope().replaceFirst(symbolTable.getProgramName(), "main"));

        repository.addCode(
                String.format("(func $%s", symbol.getLexema().replaceFirst(symbolTable.getProgramName(), "main")));

        repository.increaseIndentation();

        dumpParameters(repository);

        // Se agrega una etiqueta que, luego de haber determinado todas las variables
        // temporales que serán necesarias, se remplaza por su declaració
        repository.addCode("<local_variables>");
    }

    // ============================================================================================

    private void dumpParameters(CodeRepository repository) {

        // Las funciones lambda solo tienen parámetro por CV.

        System.out.println(repository.getCurrentScope());
        Symbol parameter = SymbolTable.getInstance().get(repository.getCurrentScope(), SymbolCategory.CV_PARAMETER)
                .getFirst();

        repository.addCode(String.format("(param $%s i32) %n", parameter.getLexemaWithoutScope()));
    }
}
