package assembler.operators.implementations;

import java.math.BigDecimal;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
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

        // Si es negativo, se agrega la conversión a absoluto.
        if (operandValue.signum() < 0) {
            code += String.format("f32.abs %n");
            code += String.format("f32.const %s%n", MAX_UINT); // TODO: hay que tirar warning
            code += String.format("f32.min %n");
        }

        // Conversión a entero.
        // En una asignación, del lado izquierdo siempre voy a tener variables de tipo
        // entero, por cómo es el lenguaje.
        code += String.format("i32.trunc_f32_u %n"); // Conversión de flotante a entero sin signo.

        return code;
    }
}
