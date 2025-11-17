package assembler.operators.implementations.functions.call;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolTable;

public class FunctionCall implements AssemblerOperator {

    private FunctionCall() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final FunctionCall INSTANCE = new FunctionCall();
    }

    // --------------------------------------------------------------------------------------------

    public static FunctionCall getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {

        SymbolTable symbolTable = SymbolTable.getInstance();
        Symbol function = symbolTable.getSymbol(operands.pop());

        // Invocación a la función.
        String code = String.format("call $%s %n", function.getLexemaWithoutScope());

        // Variale auxiliar en la que se guardará el retorno de la función.
        String newOperandName = symbolTable.addAuxiliarVariable(function.getScope());

        code += String.format("%n;; Lectura del retorno de la invocación de la función%n");
        code += String.format(";; y guardado en una variable auxiliar%n");
        code += String.format("local.set $%s", symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

        // Se agrega el operando a la pila, para que el retorno
        // pueda ser usado dentro de operaciones.
        operands.push(newOperandName);

        return code;
    }
}
