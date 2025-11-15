package assembler;

import java.util.List;

import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;

public final class Dumper {

    protected static String dumpEntryPoint() {

        StringBuilder code = new StringBuilder();

        String functionName = SymbolTable.getInstance().get(null, SymbolCategory.PROGRAM).getFirst().getLexema();

        code.append(";; Punto de entrada del programa. \n");
        code.append(String.format("(func (export \"main\") %n"));

        String functionVariables = dumpBlockVariables(functionName);

        if (!functionVariables.isBlank()) {
            code.append("\n").append("    ;; Variable globales.");
            code.append("\n").append(functionVariables);
        }

        return code.toString();
    }

    public static String dumpBlockVariables(String blockName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> localVariable = SymbolTable.getInstance().get(blockName, SymbolCategory.VARIABLE);

        for (Symbol symbol : localVariable) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("    (local $%s i32)%n", symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }

    static String dumpStrings() {

        StringBuilder code = new StringBuilder();

        int stringNumber = 0;

        List<Symbol> strings = SymbolTable.getInstance().get(null, SymbolType.STRING);

        for (Symbol symbol : strings) {

            // Se le asigna el valor al string para luego utilizarlo en la generación de
            // código.
            SymbolTable.getInstance().setValue(symbol.getLexema(), String.valueOf(stringNumber));

            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(
                    String.format("    (data (i32.const %d) %s)%n", stringNumber++, symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }
}
