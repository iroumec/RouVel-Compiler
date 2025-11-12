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

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Obtención del símbolo del segundo operando.
        Symbol secondOperand = symbolTable.getSymbol(operands.pop());

        // Obtención del símbolo del primer operando.
        Symbol firstOperand = symbolTable.getSymbol(operands.pop());

        String code; // debug

        if (areBothOperandsOfType(firstOperand, secondOperand, SymbolType.UINT)) {

            code = this.resolveUintSum(firstOperand, secondOperand, operands);

        } else if (areBothOperandsOfType(firstOperand, secondOperand, SymbolType.FLOAT)) {

            this.resolveFloatSum(firstOperand, secondOperand);
        } else {

            this.resolveConversionSum(firstOperand, secondOperand);
        }

        /*
         * if (!firstType.equals(secondType)) {
         * 
         * code.append(String.format("local.get $%s", firstOperand));
         * 
         * if (firstType.equals(SymbolType.FLOAT)) {
         * code.append("i32.trunc_f64_s");
         * }
         * }
         */

        // De ser un entero y un flotante, el flotante debe convertirse a entero.

        // Probando.
        if (!symbolsBelongToCategory(SymbolCategory.CONSTANT, firstOperand, secondOperand)) {
            // Faltan conversiones y demás.
            return """
                    local.get $%s
                    local.get $%s
                    """.formatted(secondOperand, firstOperand);
        }

        return "";
    }

    /**
     * Aplica la optimización por reducción simple.
     * 
     * @param operands
     */
    private void applySimpleReductionOptimization(Symbol firstOperand, Symbol secondOperand,
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

    /**
     * 
     * @param operands
     * @param firstOperand
     * @param secondOperand
     * @return The name of the new operand.
     */
    private String resolveUintSum(Symbol firstOperand, Symbol secondOperand, Deque<String> operands) {

        String code = "";
        String newOperandName;

        SymbolTable symbolTable = SymbolTable.getInstance();

        // De ser ambos operandos constantes, se aplica la optimización por reducción
        // simple, añadiendo en la tabla simplemente una constante en la que se halla
        // calculada la suma.
        if (symbolsBelongToCategory(SymbolCategory.CONSTANT, firstOperand, secondOperand)) {

            String sumValue = String
                    .valueOf(Integer.valueOf(firstOperand.getValue()) + Integer.valueOf(secondOperand.getValue()));

            symbolTable.addEntry(sumValue, new Symbol(sumValue, sumValue, SymbolType.UINT));
            newOperandName = sumValue;
        } else {
            // Se añade una variable auxiliar.
            // Podría obtener el scope del segundo operando indistinguidamente.
            newOperandName = symbolTable.addAuxiliarVariable(firstOperand.getScope());

            // TODO: solucionar esto.
            code = """

                    """;
        }

        // Se remueve una referencia de cada operando.
        symbolTable.removeEntry(firstOperand.getLexema());
        symbolTable.removeEntry(secondOperand.getLexema());

        // Se añade el nuevo operando.
        operands.push(newOperandName);

        return code;
    }

    private void resolveFloatSum(Symbol firstOperand, Symbol secondOperand) {
    }

    private void resolveConversionSum(Symbol firstOperand, Symbol secondOperand) {
    }

    private boolean areBothOperandsOfType(Symbol firstOperand, Symbol secondOperand, SymbolType type) {

        return firstOperand.isType(type) && secondOperand.isType(type);
    }

    private boolean symbolsBelongToCategory(SymbolCategory category, Symbol... symbols) {

        for (Symbol symbol : symbols) {
            if (!symbol.isCategory(category)) {
                return false;
            }
        }

        return true;
    }
}
