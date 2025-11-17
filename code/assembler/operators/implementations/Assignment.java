package assembler.operators.implementations;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Assignment implements AssemblerOperator {

    private Assignment() {
    }

    // --------------------------------------------------------------------------------------------

    private static class Holder {
        private static final Assignment INSTANCE = new Assignment();
    }

    // --------------------------------------------------------------------------------------------

    public static Assignment getInstance() {
        return Holder.INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        Symbol firstOperand, secondOperand;
        StringBuilder code = new StringBuilder();

        // El primer operando siempre es una variable (por cómo es nuestro lenguaje).
        // Y las variables siempre son de tipo UINT.
        secondOperand = SymbolTable.getInstance().getSymbol(repository.popOperand());
        firstOperand = SymbolTable.getInstance().getSymbol(repository.popOperand());

        // No se requiere conversión.
        if (secondOperand.isType(SymbolType.UINT)) {
            code.append(this.getNonConversionAssignment(secondOperand));
        } else {
            code.append(this.getConversionAssignment(secondOperand));
        }

        code.append(String.format("local.set $%s %n", firstOperand.getLexemaWithoutScope()));

        repository.addCode(code);
    }

    // --------------------------------------------------------------------------------------------

    private String getNonConversionAssignment(Symbol operand) {

        String code;

        if (operand.isCategory(SymbolCategory.VARIABLE)) {

            code = String.format("local.get $%s %n", operand.getLexemaWithoutScope());

        } else { // Si no es una variable, es una constante.
            code = String.format("i32.const %s %n", operand.getValue());
        }

        return code;
    }

    // --------------------------------------------------------------------------------------------

    private String getConversionAssignment(Symbol operand) {

        String code;

        // Carga de la constante flotante.
        code = String.format("f32.const %s %n", Float.valueOf(operand.getValue()).toString());

        // Conversión a entero.
        // En una asignación, del lado izquierdo siempre voy a tener variables de tipo
        // entero, por cómo es el lenguaje.
        code += String.format("i32.trunc_f32_u %n"); // Conversión de flotante a entero sin signo.

        return code;
    }
}
