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

    private static class Holder {
        private static final Argument INSTANCE = new Argument();
    }

    public static Argument getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol argument = SymbolTable.getInstance().getSymbol(operands.pop());

        return getCode(argument, SymbolType.UINT, indentation);
    }

    // TODO: este código se repite en varias clases. EXTERNALIZAR.
    private String getCode(Symbol operand, SymbolType conversionType, String indentation) {

        String out;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format(indentation + "i32.const %s", operand.getValue());
            } else {

                out = String.format(indentation + "f32.const %s", operand.getValue());

                if (conversionType == SymbolType.UINT) {
                    out += indentation + "\ni32.trunc_f32_u";
                }
            }
        } else {

            out = String.format(indentation + "local.get $%s", operand.getLexemaWithoutScope());
        }

        return out;
    }
}
