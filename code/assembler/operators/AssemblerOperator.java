package assembler.operators;

import java.math.BigDecimal;

import common.Symbol;
import common.Monitor;
import common.SymbolType;
import common.SymbolTable;
import common.SymbolCategory;
import assembler.CodeRepository;

public interface AssemblerOperator {

    void generateAssembler(CodeRepository repository);

    // ============================================================================================

    default String getCode(Symbol operand, SymbolType conversionType, CodeRepository repository) {

        if (operand.isCategory(SymbolCategory.CONSTANT)) {
            if (operand.isType(SymbolType.UINT)) {
                return getUINTCode(operand, conversionType, repository);
            } else {
                return getFloatCode(operand, conversionType, repository);
            }
        } else {
            return getVariableCode(operand, conversionType, repository);
        }
    }

    // ============================================================================================

    default String getVariableCode(Symbol operand, SymbolType conversionType, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol currentFunction = symbolTable
                .getFunctionSymbol(repository.getCurrentScope().replaceFirst("main", symbolTable.getProgramName()));

        // Si es local...
        if (operand.getScope().equals(currentFunction.getScope() + ":" + currentFunction.getLexemaWithoutScope())
                || currentFunction.getScope().isBlank() || operand.isCategory(SymbolCategory.AUXILIAR_VARIABLE)) {

            return String.format("local.get $%s", operand.getLexemaWithoutScope());
        } else {

            return String.format("local.get $%s",
                    operand.getLexema().replaceFirst(symbolTable.getProgramName(), "main"));
        }
    }

    // ============================================================================================

    default String getUINTCode(Symbol operand, SymbolType conversionType, CodeRepository repository) {

        return String.format("i32.const %s", operand.getValue());
    }

    // ============================================================================================

    default String getFloatCode(Symbol operand, SymbolType conversionType, CodeRepository repository) {

        String out;
        int maxUINT = 65535;

        BigDecimal operandValue = operand.getValue();

        out = String.format("f32.const %s", operandValue);

        if (conversionType == SymbolType.UINT) {

            // Si el flotante es negativo, en la conversión a entero, se
            // convierte en absoluto.
            if (operandValue.signum() < 0) {
                out += String.format("%nf32.abs ");
                Monitor.getInstance().addCompilationWarning(String.format(
                        "WARNING: El valor flotante %s es negativo. Se tomará su absoluto para operar con enteros.",
                        operandValue));
            }

            BigDecimal absoluteOperand = operandValue.abs();

            // Si el valor absoluto del flotante es mayor al máximo uint,
            // se toma el mínimo entre ellos.
            if (absoluteOperand.compareTo(BigDecimal.valueOf(maxUINT)) > 0) {
                out += String.format("%nf32.const %s", maxUINT);
                out += String.format("%nf32.min");
                Monitor.getInstance().addCompilationWarning(String.format(
                        "WARNING: El valor flotante %s excede el rango oeprable de los enteros. Durante la operación, redondeará al valor máximo entero %s",
                        operandValue, maxUINT));
            }

            out += String.format("%ni32.trunc_f32_u");
        }

        return out;
    }
}
