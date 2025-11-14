package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Print implements AssemblerOperator {

    private Print() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Print INSTANCE = new Print();
    }

    // --------------------------------------------------------------------------------------------

    public static Print getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        Symbol operand = SymbolTable.getInstance().getSymbol(operands.pop());

        String code;

        if (operand.isType(SymbolType.STRING)) {
            code = String.format("i32.const %s %n", operand.getValue());
            code += String.format("i32.const %d %n", operand.getLexema().length() - 2); // -2 ya que se restan las
                                                                                        // comillas.
            code += String.format("call $printString %n");
        } else if (operand.isCategory(SymbolCategory.CONSTANT)) {

            if (operand.isType(SymbolType.UINT)) {
                code = String.format("i32.const %s %n", operand.getValue());
                code += String.format("call $printInt %n");

            } else { // Flotante.

                code = String.format("f32.const %s %n", operand.getValue());
                code += String.format("call $printFloat %n");
            }
        } else { // Es variable.
            code = String.format("local.get %s %n", operand.getLexemaWithoutScope());
            code += String.format("call $printInt %n");
        }

        return indent(code, indentation);
    }

}
