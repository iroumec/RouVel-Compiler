package assembler.operators.implementations.functions.declaration;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import assembler.Dumper;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

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
    public String getAssembler(Deque<String> operands) {

        Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        // TODO: revisar qué pasa si hay dos funciones con un mismo nombre pero en
        // distintos ámbitos.

        StringBuilder code = new StringBuilder();

        String functionName = symbol.getLexemaWithoutScope();

        code.append(String.format("(func $%s %n", functionName));

        code.append(dumpParameters(functionName));

        String functionVariables = Dumper.dumpBlockVariables(functionName);

        if (!functionVariables.isBlank()) {
            code.append("\n").append(functionVariables);
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    private String dumpParameters(String functionName) {

        StringBuilder code = new StringBuilder();

        // TODO: mejorar esto para no hacer dos recorridos en la tabla de símbolos.
        // TODO: aclarar en el informe el no guardado de variable auxiliar flotante.
        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CV_PARAMETER);
        parameters.addAll(SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER));

        StringBuilder copiesOfValues = new StringBuilder();

        for (Symbol symbol : parameters) {
            // El lenguaje solo tiene como parámetros válidos enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("    (param $%s i32) %n", symbol.getLexemaWithoutScope()));

            copiesOfValues.append(String.format("%n    ;; Copia del valor del argumento en el parámetro %s. %n",
                    symbol.getLexemaWithoutScope()));
            copiesOfValues.append(String.format("    local.set $%s %n", symbol.getLexemaWithoutScope()));
        }

        return code.append("    (result i32) \n").append(copiesOfValues).toString();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se incrementa en 1 la indentación al entrar en el cuerpo de la función.
     */
    @Override
    public int getEntryIndentationChange() {
        return 1;
    }
}
