package assembler.operators.implementations.functions.call;

import common.Symbol;
import common.SymbolDirector;
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

        // Variale auxiliar en la que se guardará el retorno de la función.
        Symbol auxiliarVariable = SymbolDirector.createNewAuxiliarVariable(repository.getCurrentScope());
        SymbolTable.getInstance().addEntry(auxiliarVariable);

        code = String.format(";; Lectura del retorno de la invocación de la función%n");
        code += String.format(";; y guardado en una variable auxiliar%n");
        code += String.format("local.set $%s", auxiliarVariable.getLexemaWithoutScope());

        // Se agrega el operando a la pila, para que el retorno
        // pueda ser usado dentro de operaciones.
        repository.pushOperand(auxiliarVariable.getLexema());
        repository.addCode(code);
        repository.addCode("\n");
    }
}
