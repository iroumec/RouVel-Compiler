package assembler.operators.implementations.arithmetic;

import java.math.BigDecimal;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolDirector;
import common.SymbolTable;
import common.SymbolType;

public abstract class ArithmeticOperator implements AssemblerOperator {

    // --------------------------------------------------------------------------------------------

    protected static final int MAX_UINT = 65535;
    protected static final BigDecimal ABSOLUTE_MAXIMUM = new BigDecimal("3.40282347E38");
    protected static final BigDecimal ABSOLUTE_MINIMUN = new BigDecimal("1.17549435E-38");

    // --------------------------------------------------------------------------------------------

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Obtención del símbolo del segundo operando.
        Symbol secondOperand = symbolTable.getSymbol(repository.popOperand());

        // Obtención del símbolo del primer operando.
        Symbol firstOperand = symbolTable.getSymbol(repository.popOperand());

        String code = resolveOperation(firstOperand, secondOperand, repository);

        if (!code.isBlank()) {
            repository.addCode(code);
        }
    }

    // --------------------------------------------------------------------------------------------

    private String resolveOperation(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

        String code = "";
        PairType pairType = PairType.getType(firstOperand, secondOperand);

        SymbolTable symbolTable = SymbolTable.getInstance();

        // De ser ambos operandos constantes, se aplica la optimización por reducción
        // simple, añadiendo en la tabla simplemente una constante en la que se halla
        // calculada la suma.
        if (symbolsBelongToCategory(SymbolCategory.CONSTANT, firstOperand, secondOperand)) {

            // Simple Reduction.
            this.applyDirectOperation(firstOperand, secondOperand, pairType, repository);

        } else {
            // Se añade una variable auxiliar.
            // Podría obtener el scope del segundo operando indistinguidamente.
            String scope = firstOperand.getScope();

            // De ser el primer operando una constante,
            // se obtiene el scope del segundo operando.
            // Alguno de los operandos debe tener un scope asociado.
            if (scope == null) {
                scope = secondOperand.getScope();
            }

            Symbol auxiliarVariable = SymbolDirector.createNewAuxiliarVariable(repository.getCurrentScope());
            symbolTable.addEntry(auxiliarVariable);

            // Se añade la variable auxiliar como nuevo operando.
            repository.pushOperand(auxiliarVariable.getLexema());

            applyRuntimeControls(firstOperand, secondOperand, repository);

            code += this.getCode(pairType, symbolTable, firstOperand, secondOperand, auxiliarVariable.getLexema());
        }

        // Se remueve una referencia de cada operando.
        symbolTable.removeEntry(firstOperand.getLexema());
        symbolTable.removeEntry(secondOperand.getLexema());

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

    protected enum PairType {
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

    private String getCode(PairType pairType, SymbolTable symbolTable, Symbol firstOperand, Symbol secondOperand,
            String newOperandName) {

        return switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> """
                    %s\
                    %s\
                    i32.%s
                    local.set $%s
                    """.formatted(
                    getCode(firstOperand, SymbolType.UINT),
                    getCode(secondOperand, SymbolType.UINT),
                    this.getAssemblerOperator(),
                    symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

            case FLOAT_FLOAT -> """
                    %s\
                    %s\
                    f32.%s
                    local.set $%s
                    """.formatted(
                    getCode(firstOperand, null),
                    getCode(secondOperand, null),
                    this.getAssemblerOperator(),
                    symbolTable.getSymbol(newOperandName).getLexemaWithoutScope());

            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    protected abstract void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository);

    // --------------------------------------------------------------------------------------------

    protected abstract String getAssemblerOperator();

    // --------------------------------------------------------------------------------------------

    protected abstract void applyRuntimeControls(Symbol firstOperand, Symbol secondOperand, CodeRepository repository);
}
