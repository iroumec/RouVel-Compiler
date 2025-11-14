package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

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
    public String getAssembler(Deque<String> operands, String indentation) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Se descarta el parámetro formal al que corresponde, debido a que eso ya está
        // resuelto. Únicamente se agrega como comentario para más claridad.
        String code = String.format(";; Pasaje a parámetro %s %n",
                symbolTable.getSymbol(operands.pop()).getLexemaWithoutScope());

        Symbol argument = SymbolTable.getInstance().getSymbol(operands.pop());

        code += getCode(argument, SymbolType.UINT, indentation);

        return code;
    }
}
