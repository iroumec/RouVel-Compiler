package assembler.operators.implementations.functions.call;

import java.util.List;

import assembler.CodeRepository;
import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolDirector;
import common.SymbolTable;

public class FunctionCall implements AssemblerOperator {

    private FunctionCall() {
    }

    // ============================================================================================

    private static class Holder {
        private static final FunctionCall INSTANCE = new FunctionCall();
    }

    // ============================================================================================

    public static FunctionCall getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================

    @Override
    public void generateAssembler(CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        Symbol functionCalled = symbolTable.getSymbol(repository.popOperand());
        loadOutOfScopeVariables(functionCalled, repository);
        loadLocalVariables(functionCalled, repository);

        //repository.addCode(String.format("call $%s %n", functionCalled.getLexemaWithoutScope())); 
        repository.addCode(String.format("call $%s %n", functionCalled.getLexema().replaceFirst(symbolTable.getProgramName(),"main")));
        repository.addCode("\n");
        readReturn(repository);
        readOutOfScopeVariables(functionCalled, repository);
        readLocalVariables(functionCalled, repository);
    }

    // ============================================================================================

    public void readReturn(CodeRepository repository) {

        String code;

        // Variale auxiliar en la que se guardará el retorno de la función.
        Symbol auxiliarVariable = SymbolDirector.createNewAuxiliarVariable(repository.getCurrentScope());
        SymbolTable.getInstance().addEntry(auxiliarVariable);

        code = String.format(";; Lectura del retorno de la invocación de la función%n");
        code += String.format(";; y guardado en una variable auxiliar%n");
        code += String.format("local.set $%s", auxiliarVariable.getLexemaWithoutScope());

        // Se agrega el operando a la pila, para que el retorno
        // pueda ser usado dentro de operaciones.
        repository.pushOperand(auxiliarVariable.getLexema());
        repository.addCode(code);
        repository.addCode("\n");
    }

    // ============================================================================================

    public void loadOutOfScopeVariables(Symbol function, CodeRepository repository) {
        
        SymbolTable symbolTable = SymbolTable.getInstance();

        // Variables fuera de ámbito.
        List<Symbol> outOfScopeVariables = symbolTable.getOutOfScopeVariables(function);

        for (Symbol symbol : outOfScopeVariables) {

            if (!symbol.getScope()
                    .endsWith(symbolTable.getFunctionSymbol(repository.getCurrentScope()).getLexemaWithoutScope())) { // Variables no globales

                repository.addCode(";; Pasaje de las variables de ámbitos superiores.");
                repository
                        .addCode(String.format("local.get $%s",
                                symbol.getLexema().replaceFirst(symbolTable.getProgramName(), "main")));

                repository.addCode("\n");

            } else if (symbol.getScope().equals(symbolTable.getProgramName())) { // Variables globales

                repository.addCode(";; Pasaje de las variables de ámbitos superiores.");
                repository.addCode(String.format("local.get $%s",
                    symbol.getLexemaWithoutScope()));
                
                repository.addCode("\n");
            }
        }
    }

    // ============================================================================================

    public void readOutOfScopeVariables(Symbol function, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Variables fuera de ámbito.
        List<Symbol> outOfScopeVariables = symbolTable.getOutOfScopeVariables(function);

        for (Symbol symbol : outOfScopeVariables) {

            if (!symbol.getScope()
                    .endsWith(symbolTable.getFunctionSymbol(repository.getCurrentScope()).getLexemaWithoutScope())) {

                repository.addCode(";; Lectura de las variables de otros ámbitos.");
                repository
                        .addCode(String.format("local.set $%s",
                                symbol.getLexema().replaceFirst(symbolTable.getProgramName(), "main")));

                repository.addCode("\n");

            } else if (symbol.getScope().equals(symbolTable.getProgramName())) {

                repository.addCode(";; Lectura de las variables de otros ámbitos.");
                repository
                        .addCode(String.format("local.set $%s",
                                symbol.getLexemaWithoutScope()));

                repository.addCode("\n");

            }
        }
    }

    // ============================================================================================

    public void loadLocalVariables(Symbol functionCalled, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Variables locales, que se pasaron como parámetro a la otra función
        // para que pueda utilizarlas.
        List<Symbol> localVariables = symbolTable
                .getLocalVariablesOfUntil(symbolTable.getFunctionSymbol(repository.getCurrentScope()), functionCalled);

        for (Symbol symbol : localVariables) {

            repository.addCode(";; Enviado de las variables locales de la función definidas");
            repository.addCode(";; hasta el momento de la declaración de la función invocada.");
            repository
                    .addCode(String.format("local.get $%s",
                            symbol.getLexemaWithoutScope()));

            repository.addCode("\n");
        }
    }

    // ============================================================================================

    public void readLocalVariables(Symbol functionCalled, CodeRepository repository) {

        SymbolTable symbolTable = SymbolTable.getInstance();

        // Variables locales, que se pasaron como parámetro a la otra función
        // para que pueda utilizarlas.
        List<Symbol> localVariables = symbolTable
                .getLocalVariablesOfUntil(symbolTable.getFunctionSymbol(repository.getCurrentScope()), functionCalled);

        for (Symbol symbol : localVariables) {

            repository.addCode(";; Lectura de las variables locales.");
            repository
                    .addCode(String.format("local.set $%s",
                            symbol.getLexemaWithoutScope()));

            repository.addCode("\n");
        }
    }
}
