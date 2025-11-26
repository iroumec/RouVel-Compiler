package assembler.operators.implementations.functions.lambdas;

import common.Symbol;
import common.SymbolTable;

import java.util.List;

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

        dumpParameters(symbol, repository);

        // Se agrega una etiqueta que, luego de haber determinado todas las variables
        // temporales que serán necesarias, se remplaza por su declaració
        repository.addCode("<local_variables>");
    }

    // ============================================================================================

    private void dumpParameters(Symbol function, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Las lambdas solo tienen un parámetro por CV.
        List<Symbol> parameters = symbolTable.getParameters(repository.getCurrentScope());

        for (Symbol symbol : parameters) {
            // El lenguaje solo tiene como parámetros válidos enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            repository.addCode(String.format("(param $%s i32)", symbol.getLexemaWithoutScope()));
        }

        // Variables fuera de ámbito.
        List<Symbol> outOfScopeVariables = symbolTable.getOutOfScopeVariables(function);

        for (Symbol symbol : outOfScopeVariables) {

            repository
                    .addCode(String.format("(param $%s i32)",
                            symbol.getLexema().replaceFirst(symbolTable.getProgramName(), "main")));
        }

        /**
         * Al invocar a una función, ya se realiza automáticamente la asignación en
         * orden de los operandos en la pila de operandos a los parámetros. Por
         * consiguiente, no debe hacerse explícito.
         */

        /**
         * El número de retornos está compuesto por el retorno del valor de todas
         * las variables de otros ámbitos que puedan ser accedidas.
         */
        repository.addCode("(result" + " i32".repeat(1 + outOfScopeVariables.size()) + ")");
    }
}
