package assembler.operators.implementations.functions.declaration;

import java.util.List;

import common.Symbol;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class FunctionOpener implements AssemblerOperator {

    private FunctionOpener() {
    }

    // ============================================================================================

    private static class Holder {
        private static final FunctionOpener INSTANCE = new FunctionOpener();
    }

    // ============================================================================================

    public static FunctionOpener getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol symbol = symbolTable.getSymbol(repository.popOperand());

        repository.startBlock((symbol.getScope() + ":" + symbol.getLexemaWithoutScope()).replaceFirst(symbolTable.getProgramName(), "main"));

        repository.addCode(String.format("(func $%s", symbol.getLexema().replaceFirst(symbolTable.getProgramName(), "main")));

        repository.increaseIndentation();

        dumpParameters(symbol, repository);
        repository.addCode("\n");

        // Se agrega una etiqueta que, luego de haber determinado todas las variables
        // temporales que serán necesarias, se remplaza por su declaración.
        repository.addCode("<local_variables>");
    }

    // ============================================================================================

    private void dumpParameters(Symbol function, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Parámetros de la función.
        List<Symbol> parameters = symbolTable.getParameters(repository.getCurrentScope());
        List<Symbol> copyRestoreParameter = symbolTable.get(repository.getCurrentScope(),
                SymbolCategory.CVR_PARAMETER);

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

        // Los valores de variables fuera de ámbito se leen primero, por lo que se
        // agregan a la lista
        // al final.
        copyRestoreParameter.addAll(outOfScopeVariables);

        /**
         * Al invocar a una función, ya se realiza automáticamente la asignación en
         * orden de los operandos en la pila de operandos a los parámetros. Por
         * consiguiente, no debe hacerse explícito.
         */

        /**
         * El número de retornos está compuesto por el retorno de la función,
         * más todos los parámetros que sean por cvr. Si no se aclara en el retorno
         * la cantidad de operandos que van a quedar en la pila al salir de la función,
         * se pierden.
         */
        repository.addCode("(result" + " i32".repeat(1 + copyRestoreParameter.size()) + ")");
    }
}
