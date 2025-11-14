package assembler.operators;

import java.util.Deque;

import common.Symbol;
import common.SymbolCategory;
import common.SymbolType;

public interface AssemblerOperator {

    String getAssembler(Deque<String> operands, String indentation);

    default int getEntryIndentationChange() {
        return 0;
    }

    default int getExitIndentationChange() {
        return 0;
    }

    default boolean producesEntryChangeInIndentation() {
        return this.getEntryIndentationChange() != 0;
    }

    default boolean producesExitChangeInIndentation() {
        return this.getExitIndentationChange() != 0;
    }

    default String getCode(Symbol operand, SymbolType conversionType, String indentation) {

        String out;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format(indentation + "i32.const %s %n", operand.getValue());
            } else {

                out = String.format(indentation + "f32.const %s %n", operand.getValue());

                if (conversionType == SymbolType.UINT) {
                    out += String.format(indentation + "i32.trunc_f32_u %n");
                }
            }
        } else {

            out = String.format(indentation + "local.get $%s %n", operand.getLexemaWithoutScope());
        }

        return out;
    }
}
