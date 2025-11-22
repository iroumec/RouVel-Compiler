package assembler.operators;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolType;

public interface AssemblerOperator {

    void generateAssembler(CodeRepository repository);

    // --------------------------------------------------------------------------------------------

    default String getCode(Symbol operand, SymbolType conversionType) {

        String out;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format("i32.const %s%n", operand.getValue());
            } else {

                out = String.format("f32.const %s%n", operand.getValue());

                if (conversionType == SymbolType.UINT) {
                    out += String.format("%ni32.trunc_f32_u");
                }
            }
        } else {

            out = String.format("local.get $%s%n", operand.getLexemaWithoutScope());
        }

        return out;
    }
}
