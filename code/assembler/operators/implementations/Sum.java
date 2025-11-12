package assembler.operators.implementations;

import java.util.Deque;

import assembler.operators.Operator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public class Sum implements Operator {

    private Sum() {
    }

    private static class Holder {
        private static final Sum INSTANCE = new Sum();
    }

    public static Sum getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getAssembler(Deque<String> operands) {

        String firstOperand = operands.pop();
        String secondOperand = operands.pop();

        // int firstOperandValue = SymbolTable.getInstance().get

        // Se remueve una referencia de cada operando y
        // se agrega una referencia a la variable auxiliar.
        SymbolTable.getInstance().removeEntry(firstOperand);
        SymbolTable.getInstance().removeEntry(secondOperand);
        // SymbolTable.getInstance().addEntry(, null);

        // TODO: no hay variables auxiliares en webAssembly.

        // De ser un entero y otro (o variable, ya que solo pueden ser enteras), no hay
        // conversión.
        Symbol firstSymbol = SymbolTable.getInstance().getSymbol(firstOperand);
        Symbol secondSymbol = SymbolTable.getInstance().getSymbol(secondOperand);

        // De estar ante dos constantes, se aplica la optimización por reducción simple.
        if (firstSymbol.isCategory(SymbolCategory.CONSTANT) && secondSymbol.isCategory(SymbolCategory.CONSTANT)) {

            this.applySimpleReductionOptimization(firstSymbol, secondSymbol, operands);
        } else {

        }

        SymbolType firstType = firstSymbol.getType();
        SymbolType secondType = secondSymbol.getType();

        StringBuilder code = new StringBuilder();

        if (!firstType.equals(secondType)) {

            code.append(String.format("local.get $%s", firstOperand));

            if (firstType.equals(SymbolType.FLOAT)) {
                code.append("i32.trunc_f64_s");
            }
        }

        // De ser un entero y un flotante, el flotante debe convertirse a entero.

        // Faltan conversiones y demás.
        return """
                local.get $%s
                local.get $%s
                """.formatted(firstOperand, secondOperand);
    }

    /**
     * Aplica la optimización por reducción simple.
     * 
     * @param operands
     */
    private static void applySimpleReductionOptimization(Symbol firstOperand, Symbol secondOperand,
            Deque<String> operands) {

        // Si no es UINT, es FLOAT. Un STRING también puede ser constante, pero no
        // debería llegar hasta acá ese caso.
        boolean isFirstOperandUINT = firstOperand.isType(SymbolType.UINT);
        boolean isSecondOperandUINT = secondOperand.isType(SymbolType.UINT);

        String value;
        SymbolType resultType;

        // TODO: optimizar esto.
        if (isFirstOperandUINT && isSecondOperandUINT) {

            value = String
                    .valueOf(Integer.valueOf(firstOperand.getValue()) * Integer.valueOf(secondOperand.getValue()));

            resultType = SymbolType.UINT;
        } else {

            if (!isFirstOperandUINT && !isSecondOperandUINT) {

                value = String
                        .valueOf(Float.valueOf(firstOperand.getValue()) * Float.valueOf(secondOperand.getValue()));
            } else if (!isFirstOperandUINT) {

                value = String
                        .valueOf(Float.valueOf(firstOperand.getValue()) * Float.valueOf(secondOperand.getValue()));
            } else {

                value = String
                        .valueOf(Float.valueOf(firstOperand.getValue()) * Float.valueOf(secondOperand.getValue()));
            }

            resultType = SymbolType.FLOAT;
        }

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Se remueve una referencia de cada constante.
        symbolTable.removeEntry(firstOperand.getLexema());
        symbolTable.removeEntry(secondOperand.getLexema());

        // Se agrega la nueva constante optimizada.
        symbolTable.addEntry(value, new Symbol(value, value, resultType));

    }
}
