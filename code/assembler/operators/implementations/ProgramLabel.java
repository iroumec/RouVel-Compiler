package assembler.operators.implementations;

import java.util.Deque;
import java.util.List;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;

public class ProgramLabel implements AssemblerOperator {

    private ProgramLabel() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final ProgramLabel INSTANCE = new ProgramLabel();
    }

    // --------------------------------------------------------------------------------------------

    public static ProgramLabel getInstance() {
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

        code.append(";; Punto de entrada del programa. \n");
        code.append(String.format("(func (export \"main\") %n"));

        String functionVariables = dumpFunctionVariables(functionName);

        if (!functionVariables.isBlank()) {
            code.append("\n").append("    ;; Variable globales.");
            code.append("\n").append(functionVariables);
        }

        return indent(code.toString(), indentation);
    }

    // --------------------------------------------------------------------------------------------

    private static String dumpFunctionVariables(String functionName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> localVariable = SymbolTable.getInstance().get(functionName, SymbolCategory.VARIABLE);

        for (Symbol symbol : localVariable) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("    (local $%s i32)%n", symbol.getLexemaWithoutScope()));
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
