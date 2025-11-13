package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Assignment implements Operator {

    private Assignment() {
    }

    private static class Holder {
        private static final Assignment INSTANCE = new Assignment();
    }

    public static Assignment getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        String secondOperand = operands.pop();
        String firstOperand = operands.pop();

        // El primer operando siempre es una variable (por cómo es nuestro lenguaje).
        // Y las variables siempre son de tipo UINT.
        Symbol firstSymbol = SymbolTable.getInstance().getSymbol(firstOperand);
        Symbol secondSymbol = SymbolTable.getInstance().getSymbol(secondOperand);

        StringBuilder code = new StringBuilder();

        // No se requiere conversión.
        if (secondSymbol.isType(SymbolType.UINT)) {

            if (secondSymbol.isCategory(SymbolCategory.VARIABLE)) {

                code.append(String.format("local.get $%s %n", secondSymbol.getLexemaWithoutScope()));

            } else { // Si no es una variable, es una constante.
                code.append(String.format("i32.const %s %n", secondSymbol.getValue()));
            }

        } else {

            int convertedValue = Float.valueOf(secondSymbol.getValue()).intValue();

            // Carga de la constante flotante.
            code.append(String.format("f32.const %s %n", Float.valueOf(secondSymbol.getValue()).toString()));

            // Conversión a entero.
            // En una asignación, del lado izquierdo siempre voy a tener variables de tipo
            // entero, por cómo es el lenguaje.
            code.append(String.format("i32.trunc_f32_u %n")); // Conversión de flotante a entero sin signo.
        }

        code.append(String.format("local.set $%s", firstSymbol.getLexemaWithoutScope()));

        return code.toString();
    }
}
