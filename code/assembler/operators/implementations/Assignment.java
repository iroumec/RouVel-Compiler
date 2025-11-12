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

        String firstOperand = operands.pop();
        String secondOperand = operands.pop();

        // El primer operando siempre es una variable (por cómo es nuestro lenguaje).
        // Y las variables siempre son de tipo UINT.
        Symbol firstSymbol = SymbolTable.getInstance().getSymbol(firstOperand);
        Symbol secondSymbol = SymbolTable.getInstance().getSymbol(secondOperand);

        StringBuilder code = new StringBuilder();

        // No se requiere conversión.
        if (secondSymbol.isType(SymbolType.UINT)) {

            if (secondSymbol.isCategory(SymbolCategory.VARIABLE)) {

                code.append(String.format("local.get $%s%n", secondSymbol.getLexemaWithoutScope()));

            } else { // Si no es una variable, es una constante.
                code.append(String.format("i32.const %s%n", secondSymbol.getValue()));
            }

        } else {

            int convertedValue = Float.valueOf(secondSymbol.getValue()).intValue();

            // TODO: ¿sería esto correcto o la conversión debe realizarse en el WebAssembly?
            code.append(String.format("i32.const %s%n", convertedValue));
        }

        code.append(String.format("local.set $%s%n", firstSymbol.getLexemaWithoutScope()));

        return code.toString();
    }
}
