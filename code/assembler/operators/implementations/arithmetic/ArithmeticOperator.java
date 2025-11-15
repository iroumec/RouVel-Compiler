package assembler.operators.implementations.arithmetic;

import java.util.Deque;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public abstract class ArithmeticOperator implements AssemblerOperator {

    // --------------------------------------------------------------------------------------------

    @Override
    public String getAssembler(Deque<String> operands) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Obtención del símbolo del segundo operando.
        Symbol secondOperand = symbolTable.getSymbol(operands.pop());

        // Obtención del símbolo del primer operando.
        Symbol firstOperand = symbolTable.getSymbol(operands.pop());

        return resolveOperation(firstOperand, secondOperand, operands);
    }

    // --------------------------------------------------------------------------------------------

    private String resolveOperation(Symbol firstOperand, Symbol secondOperand, Deque<String> operands) {

        String code = "";
        String newOperandName;
        PairType pairType = PairType.getType(firstOperand, secondOperand);

        SymbolTable symbolTable = SymbolTable.getInstance();

        // De ser ambos operandos constantes, se aplica la optimización por reducción
        // simple, añadiendo en la tabla simplemente una constante en la que se halla
        // calculada la suma.
        if (symbolsBelongToCategory(SymbolCategory.CONSTANT, firstOperand, secondOperand)) {

            String sumValue = applySimpleReduction(pairType, firstOperand, secondOperand);
            symbolTable.addEntry(sumValue, new Symbol(sumValue, sumValue, SymbolType.UINT));
            newOperandName = sumValue;

            // Sin assembler generado.
        } else {
            // Se añade una variable auxiliar.
            // Podría obtener el scope del segundo operando indistinguidamente.
            newOperandName = symbolTable.addAuxiliarVariable(firstOperand.getScope());

            code = this.getCode(pairType, symbolTable, firstOperand, secondOperand, newOperandName);
        }

        // Se remueve una referencia de cada operando.
        symbolTable.removeEntry(firstOperand.getLexema());
        symbolTable.removeEntry(secondOperand.getLexema());

        // Se añade el nuevo operando.
        operands.push(newOperandName);

        return code;
    }

    // --------------------------------------------------------------------------------------------

    private boolean symbolsBelongToCategory(SymbolCategory category, Symbol... symbols) {

        for (Symbol symbol : symbols) {
            if (!symbol.isCategory(category)) {
                return false;
            }
        }

        return true;
    }

    // --------------------------------------------------------------------------------------------

    private enum PairType {
        UINT_UINT,
        UINT_FLOAT,
        FLOAT_FLOAT;

        private static PairType getType(Symbol firstOperand, Symbol secondOperand) {

            if (areBothOperandsOfType(firstOperand, secondOperand, SymbolType.UINT)) {
                return UINT_UINT;
            } else if (areBothOperandsOfType(firstOperand, secondOperand, SymbolType.FLOAT)) {
                return FLOAT_FLOAT;
            } else {
                return UINT_FLOAT;
            }

        }

        private static boolean areBothOperandsOfType(Symbol firstOperand, Symbol secondOperand, SymbolType type) {

            return firstOperand.isType(type) && secondOperand.isType(type);
        }
    }

    // --------------------------------------------------------------------------------------------

    private String applySimpleReduction(PairType pairType, Symbol firstOperand, Symbol secondOperand) {

        return switch (pairType) {
            case UINT_UINT ->
                String.valueOf(this.applyOperation(
                        Integer.valueOf(firstOperand.getValue()), Integer.valueOf(secondOperand.getValue())));
            case FLOAT_FLOAT -> String
                    .valueOf(this.applyOperation(Float.valueOf(firstOperand.getValue()),
                            Float.valueOf(secondOperand.getValue())));
            case UINT_FLOAT ->
                Integer.toUnsignedString(this.applyOperation(Float.valueOf(firstOperand.getValue()).intValue(),
                        Float.valueOf(secondOperand.getValue()).intValue()));
            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    private String getCode(PairType pairType, SymbolTable symbolTable, Symbol firstOperand, Symbol secondOperand,
            String newOperandName) {

        return switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> """
                    %s\
                    %s\
                    i32.%s
                    local.set %s
                    """.formatted(
                    getCode(firstOperand, SymbolType.UINT),
                    getCode(secondOperand, SymbolType.UINT),
                    this.getAssemblerOperator(),
                    symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

            case FLOAT_FLOAT -> """
                    %s\
                    %s\
                    f32.%s
                    local.set %s
                    """.formatted(
                    getCode(firstOperand, null),
                    getCode(secondOperand, null),
                    this.getAssemblerOperator(),
                    symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    protected abstract int applyOperation(int firstOperand, int secondOperand);

    // --------------------------------------------------------------------------------------------

    protected abstract float applyOperation(float firstOperand, float secondOperand);

    // --------------------------------------------------------------------------------------------

    protected abstract String getAssemblerOperator();
}
