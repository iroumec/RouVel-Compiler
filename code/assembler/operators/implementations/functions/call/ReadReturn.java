package assembler.operators.implementations.functions.call;

import common.SymbolTable;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class ReadReturn implements AssemblerOperator {

    private ReadReturn() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final ReadReturn INSTANCE = new ReadReturn();
    }

    // --------------------------------------------------------------------------------------------

    public static ReadReturn getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        String code;
        SymbolTable symbolTable = SymbolTable.getInstance();

        // Variale auxiliar en la que se guardará el retorno de la función.
        String newOperandName = symbolTable.addAuxiliarVariable(repository.getCurrentScope());

        code = String.format(";; Lectura del retorno de la invocación de la función%n");
        code += String.format(";; y guardado en una variable auxiliar%n");
        code += String.format("local.set $%s", symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

        // Se agrega el operando a la pila, para que el retorno
        // pueda ser usado dentro de operaciones.
        repository.pushOperand(newOperandName);
        repository.addCode(code);
    }
}
