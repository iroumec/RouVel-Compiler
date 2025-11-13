package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Assignment implements AssemblerOperator {

    private Assignment() {
    }

    private static class Holder {
        private static final Assignment INSTANCE = new Assignment();
    }

    public static Assignment getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        // El primer operando siempre es una variable (por cómo es nuestro lenguaje).
        // Y las variables siempre son de tipo UINT.
        Symbol secondOperand = SymbolTable.getInstance().getSymbol(operands.pop());
        Symbol firstOperand = SymbolTable.getInstance().getSymbol(operands.pop());

        StringBuilder code = new StringBuilder();

        // No se requiere conversión.
        if (secondOperand.isType(SymbolType.UINT)) {

            if (secondOperand.isCategory(SymbolCategory.VARIABLE)) {

                code.append(String.format(indentation + "local.get $%s %n", secondOperand.getLexemaWithoutScope()));

            } else { // Si no es una variable, es una constante.
                code.append(String.format(indentation + "i32.const %s %n", secondOperand.getValue()));
            }

        } else {

            int convertedValue = Float.valueOf(secondOperand.getValue()).intValue();

            // Carga de la constante flotante.
            code.append(
                    String.format(indentation + "f32.const %s %n", Float.valueOf(secondOperand.getValue()).toString()));

            // Conversión a entero.
            // En una asignación, del lado izquierdo siempre voy a tener variables de tipo
            // entero, por cómo es el lenguaje.
            code.append(String.format(indentation + "i32.trunc_f32_u %n")); // Conversión de flotante a entero sin
                                                                            // signo.
        }

        code.append(String.format(indentation + "local.set $%s %n", firstOperand.getLexemaWithoutScope()));

        return code.toString();
    }
}
