package assembler.operators.implementations.functions.call;

import common.Symbol;
import common.SymbolType;
import common.SymbolTable;
import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;

public class Argument implements AssemblerOperator {

    private Argument() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Argument INSTANCE = new Argument();
    }

    // --------------------------------------------------------------------------------------------

    public static Argument getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Se descarta el parámetro formal al que corresponde, debido a que eso ya está
        // resuelto. Únicamente se agrega como comentario para más claridad.
        String code = String.format(";; Pasaje de parámetro %s %n",
                symbolTable.getSymbol(repository.popOperand()).getLexemaWithoutScope());

        Symbol argument = SymbolTable.getInstance().getSymbol(repository.popOperand());

        code += getCode(argument, SymbolType.UINT);

        repository.addCode(code);
    }
}
