package assembler.operators.implementations;

import java.math.BigDecimal;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Monitor;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Assignment implements AssemblerOperator {

    private static final int MAX_UINT = 65535;

    // ============================================================================================

    private Assignment() {
    }

    // ============================================================================================

    private static class Holder {
        private static final Assignment INSTANCE = new Assignment();
    }

    // ============================================================================================

    public static Assignment getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol firstOperand, secondOperand;

        // El primer operando siempre es una variable (por cómo es nuestro lenguaje).
        // Y las variables siempre son de tipo UINT.
        secondOperand = SymbolTable.getInstance().getSymbol(repository.popOperand());
        firstOperand = SymbolTable.getInstance().getSymbol(repository.popOperand());

        // No se requiere conversión.
        if (secondOperand.isType(SymbolType.UINT)) {
            repository.addCode(this.getNonConversionAssignment(secondOperand));
        } else {
            repository.addCode(this.getConversionAssignment(secondOperand));
        }

        repository.addCode(String.format("local.set $%s", firstOperand.getLexemaWithoutScope()));
        repository.addCode("\n");
    }

    // ============================================================================================

    private String getNonConversionAssignment(Symbol operand) {

        String code;

        if (operand.isCategory(SymbolCategory.VARIABLE) ||
                operand.isCategory(SymbolCategory.CV_PARAMETER) ||
                operand.isCategory(SymbolCategory.CVR_PARAMETER) ||
                operand.isCategory(SymbolCategory.AUXILIAR_VARIABLE)) {

            code = String.format("local.get $%s %n", operand.getLexemaWithoutScope());

        } else { // Si no es una variable, es una constante.
            code = String.format("i32.const %s %n", operand.getValue());
        }

        return code;
    }

    // ============================================================================================

    private String getConversionAssignment(Symbol operand) {

        String code;

        BigDecimal operandValue = operand.getValue();

        // Carga de la constante flotante.
        code = String.format("f32.const %s %n", operandValue);

        // Si el flotante es negativo, en la conversión a entero, se
        // convierte en absoluto.
        if (operandValue.signum() < 0) {
            code += String.format("f32.abs %n");
            Monitor.getInstance().addCompilationWarning(String.format(
                    "WARNING: El valor flotante %s es negativo. Se tomará su absoluto para realizar la asignación.",
                    operandValue));
        }

        BigDecimal absoluteOperand = operandValue.abs();

        // Si el valor absoluto del flotante es mayor al máximo uint,
        // se toma el mínimo entre ellos.
        if (absoluteOperand.compareTo(BigDecimal.valueOf(MAX_UINT)) > 0) {
            code += String.format("f32.const %s%n", MAX_UINT);
            code += String.format("f32.min %n");
            Monitor.getInstance().addCompilationWarning(String.format(
                    "WARNING: El valor absoluto del flotante %s excede el rango de valores válidos del tipo de dato entero. Se asignará el máximo entero %s a la variable.",
                    operandValue, MAX_UINT));
        }

        // Conversión a entero.
        // En una asignación, del lado izquierdo siempre voy a tener variables de tipo
        // entero, por cómo es el lenguaje.
        code += String.format("i32.trunc_f32_u %n"); // Conversión de flotante a entero sin signo.

        return code;
    }
}
