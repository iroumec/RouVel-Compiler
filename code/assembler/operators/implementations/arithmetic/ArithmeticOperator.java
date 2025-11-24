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

    // ============================================================================================

    protected static final int MAX_UINT = 65535;
    protected static final BigDecimal ABSOLUTE_MAXIMUM = new BigDecimal("3.40282347E38");
    protected static final BigDecimal ABSOLUTE_MINIMUN = new BigDecimal("1.17549435E-38");

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Obtención del símbolo del segundo operando.
        Symbol secondOperand = symbolTable.getSymbol(repository.popOperand());

        // Obtención del símbolo del primer operando.
        Symbol firstOperand = symbolTable.getSymbol(repository.popOperand());

        resolveOperation(firstOperand, secondOperand, repository);
    }

    // ============================================================================================

    private void resolveOperation(Symbol firstOperand, Symbol secondOperand, CodeRepository repository) {

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

            this.generateCode(pairType, symbolTable, firstOperand, secondOperand, auxiliarVariable, repository);
        }
    }

    // ============================================================================================

    private boolean symbolsBelongToCategory(SymbolCategory category, Symbol... symbols) {

        for (Symbol symbol : symbols) {
            if (!symbol.isCategory(category)) {
                return false;
            }
        }

        return true;
    }

    // ============================================================================================

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

    // ============================================================================================

    private void generateCode(PairType pairType, SymbolTable symbolTable, Symbol firstOperand, Symbol secondOperand,
            Symbol auxiliarVariable, CodeRepository repository) {

        // local.tee es un local.set y un local.get al mismo tiempo.

        switch (pairType) {
            case UINT_UINT, UINT_FLOAT -> {
                repository.addCode(getCode(firstOperand, SymbolType.UINT)); 
                repository.addCode(getCode(secondOperand, SymbolType.UINT));
                this.applyPreviosOperationRuntimeControls(firstOperand, secondOperand, SymbolType.UINT, repository);
                repository.addCode(String.format("i32.%s", this.getAssemblerOperator()));
                repository.addCode(String.format("local.tee $%s", auxiliarVariable.getLexemaWithoutScope()));
                this.applyPostOperationRuntimeControls(firstOperand, secondOperand, SymbolType.UINT, repository);
            }
            case FLOAT_FLOAT -> {
                repository.addCode(getCode(firstOperand, null));
                repository.addCode(getCode(secondOperand, null));
                this.applyPreviosOperationRuntimeControls(firstOperand, secondOperand, null, repository);
                repository.addCode(String.format("i32.%s", this.getAssemblerOperator()));
                repository.addCode(String.format("local.tee $%s", auxiliarVariable.getLexemaWithoutScope()));
                this.applyPostOperationRuntimeControls(firstOperand, secondOperand, null, repository);
            }
        }
        ;
    }

    // ============================================================================================

    protected abstract void applyDirectOperation(Symbol firstOperand, Symbol secondOperand, PairType pairType,
            CodeRepository repository);

    // ============================================================================================

    protected abstract String getAssemblerOperator();

    // ============================================================================================

    protected abstract void applyPreviosOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository);

    // ============================================================================================

    protected abstract void applyPostOperationRuntimeControls(Symbol firstOperand, Symbol secondOperand,
            SymbolType conversionType, CodeRepository repository);

    // ============================================================================================
}
