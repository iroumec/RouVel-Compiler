package assembler.operators.implementations;

import java.util.Deque;
import java.util.List;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

public class FunctionLabel implements AssemblerOperator {

    private FunctionLabel() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionLabel INSTANCE = new FunctionLabel();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionLabel getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        // Symbol symbol = SymbolTable.getInstance().getSymbol(operands.pop());

        // TODO: revisar qué pasa si hay dos funciones con un mismo nombre pero en
        // distintos ámbitos.

        StringBuilder code = new StringBuilder();

        String functionName = operands.pop();

        code.append(String.format(indentation + "(func $%s %n", functionName));

        code.append(dumpParameters(functionName, indentation));

        code.append(String.format(indentation + " (result i32) %n"));

        String functionVariables = dumpFunctionVariables(functionName, indentation);

        if (!functionVariables.isBlank()) {
            code.append("\n").append(dumpFunctionVariables(functionName, indentation));
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    private static String dumpParameters(String functionName, String indentation) {

        StringBuilder code = new StringBuilder();

        // TODO: mejorar esto para no hacer dos recorridos en la tabla de símbolos.
        // TODO: aclarar en el informe el no guardado de variable auxiliar flotante.
        List<Symbol> parameters = SymbolTable.getInstance().get(functionName, SymbolCategory.CV_PARAMETER);
        parameters.addAll(SymbolTable.getInstance().get(functionName, SymbolCategory.CVR_PARAMETER));

        for (Symbol symbol : parameters) {
            // El lenguaje solo tiene como parámetros válidos enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format(indentation + " (param $%s i32) %n", symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    private static String dumpFunctionVariables(String functionName, String indentation) {

        StringBuilder code = new StringBuilder();

        List<Symbol> localVariable = SymbolTable.getInstance().get(functionName, SymbolCategory.VARIABLE);

        for (Symbol symbol : localVariable) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format(indentation + " (local $%s i32)%n", symbol.getLexemaWithoutScope()));
        }

        return code.toString();
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
