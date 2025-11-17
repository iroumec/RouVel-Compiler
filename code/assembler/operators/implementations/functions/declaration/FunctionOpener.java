package assembler.operators.implementations.functions.declaration;

import java.util.List;

import common.Symbol;
import assembler.CodeRepository;
import assembler.Dumper;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.operators.AssemblerOperator;

public class FunctionOpener implements AssemblerOperator {

    private FunctionOpener() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionOpener INSTANCE = new FunctionOpener();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionOpener getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(repository.popOperand());

        // TODO: revisar qué pasa si hay dos funciones con un mismo nombre pero en
        // distintos ámbitos.

        repository.startBlock();
        StringBuilder code = new StringBuilder();

        String functionName = symbol.getLexemaWithoutScope();

        code.append(String.format("(func $%s %n", functionName));

        code.append(dumpParameters(functionName));

        String functionVariables = Dumper.dumpBlockVariables(functionName);

        if (!functionVariables.isBlank()) {
            code.append("\n").append(functionVariables);
        }

        repository.addCode(code);
        repository.increaseIndentation();
    }

    // --------------------------------------------------------------------------------------------

    private String dumpParameters(String functionName) {

        StringBuilder code = new StringBuilder();

        // TODO: mejorar esto para no hacer dos recorridos en la tabla de símbolos.
        // TODO: aclarar en el informe el no guardado de variable auxiliar flotante.
        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CV_PARAMETER);
        parameters.addAll(SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER));

        for (Symbol symbol : parameters) {
            // El lenguaje solo tiene como parámetros válidos enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("    (param $%s i32) %n", symbol.getLexemaWithoutScope()));
        }

        /**
         * Al invocar a una función, ya se realiza automáticamente la asignación en
         * orden de los operandos en la pila de operandos a los parámetros. Por
         * consiguiente, no debe hacerse explícito.
         */

        return code.append("    (result i32) \n").toString();
    }
}
