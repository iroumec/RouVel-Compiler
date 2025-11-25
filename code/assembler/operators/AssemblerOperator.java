package assembler.operators;

import java.math.BigDecimal;

import assembler.CodeRepository;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public interface AssemblerOperator {

    void generateAssembler(CodeRepository repository);

    // ============================================================================================

    default String getCode(Symbol operand, SymbolType conversionType, CodeRepository repository) {

        String out;
        int maxUINT = 65535;

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                out = String.format("i32.const %s", operand.getValue());
            } else {

                BigDecimal operandValue = operand.getValue();

                out = String.format("f32.const %s", operandValue);

                if (conversionType == SymbolType.UINT) {

                    // Si el flotante es negativo, en la conversión a entero, se
                    // convierte en absoluto.
                    if (operandValue.signum() < 0) {
                        out += String.format("%nf32.abs ");
                    }

                    BigDecimal absoluteOperand = operandValue.abs();

                    // Si el valor absoluto del flotante es mayor al máximo uint,
                    // se toma el mínimo entre ellos.
                    if (absoluteOperand.compareTo(BigDecimal.valueOf(maxUINT)) > 0) {
                        out += String.format("%nf32.const %s", maxUINT); // TODO: hay que tirar warning
                        out += String.format("%nf32.min");
                    }

                    out += String.format("i32.trunc_f32_u");
                }

            }
        } else {

            SymbolTable symbolTable = SymbolTable.getInstance();

            Symbol currentFunction = symbolTable.getFunctionSymbol(repository.getCurrentScope().replaceFirst("main",symbolTable.getProgramName()));

            // Si es local...
            if (operand.getScope().equals(currentFunction.getScope() + ":" + currentFunction.getLexemaWithoutScope()) ||
                operand.isCategory(SymbolCategory.AUXILIAR_VARIABLE)) {
                    
                out = String.format("local.get $%s", operand.getLexemaWithoutScope());
            } else {
                out = String.format("local.get $%s",
                        operand.getLexema().replaceFirst(symbolTable.getProgramName(), "main"));
            }
        }

        return out;
    }
}
