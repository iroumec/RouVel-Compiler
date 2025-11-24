package assembler.operators;

import java.math.BigDecimal;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolType;

public interface AssemblerOperator {

    void generateAssembler(CodeRepository repository);

    // ============================================================================================

    default String getCode(Symbol operand, SymbolType conversionType) {

        String out;
        int maxUINT = 65535;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format("i32.const %s%n", operand.getValue());
            } else {

                BigDecimal operandValue = operand.getValue();

                out = String.format("f32.const %s%n", operandValue);

                if (conversionType == SymbolType.UINT) {

                    // Si el flotante es negativo, en la conversión a entero, se
                    // convierte en absoluto.
                    if (operandValue.signum() < 0) {
                        out += String.format("f32.abs %n");
                    }

                    BigDecimal absoluteOperand = operandValue.abs();

                    // Si el valor absoluto del flotante es mayor al máximo uint,
                    // se toma el mínimo entre ellos.
                    if (absoluteOperand.compareTo(BigDecimal.valueOf(maxUINT)) > 0) {
                        out += String.format("f32.const %s%n", maxUINT); // TODO: hay que tirar warning
                        out += String.format("f32.min %n");
                    }

                    out += String.format("i32.trunc_f32_u%n");
                }

            }
        } else {

            out = String.format("local.get $%s%n", operand.getLexemaWithoutScope());
        }

        return out;
    }
}
