package assembler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.Symbol;
import common.SymbolTable;
import common.SymbolCategory;

public final class Dumper {

    protected static String dumpEntryPoint() {

        StringBuilder code = new StringBuilder();

        code.append(";; Punto de entrada del programa. \n");
        code.append(String.format("(func (export \"main\") %n"));

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    public static String dumpGlobalVariables() {

        StringBuilder code = new StringBuilder();

        String functionName = SymbolTable.getInstance().get("", SymbolCategory.PROGRAM).getFirst().getLexema();

        String functionVariables = dumpBlockVariables(functionName);

        if (!functionVariables.isBlank()) {
            code.append("    ;; Variables globales.\n");
            code.append(functionVariables);
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    public static String dumpBlockVariables(String blockName) {

        StringBuilder code = new StringBuilder();

        List<Symbol> localVariables = SymbolTable.getInstance().get(blockName, SymbolCategory.VARIABLE);
        localVariables.addAll(SymbolTable.getInstance().get(blockName, SymbolCategory.AUXILIAR_VARIABLE));

        for (Symbol symbol : localVariables) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("    (local $%s i32)%n", symbol.getLexemaWithoutScope()));
        }

        String cleanCode = Arrays.stream(code.toString().split("\n"))
                .filter(l -> !l.trim().isEmpty())
                .collect(Collectors.joining("\n"));

        return cleanCode;
    }

    // --------------------------------------------------------------------------------------------

    static String dumpStrings() {

        StringBuilder code = new StringBuilder();

        List<Symbol> strings = SymbolTable.getInstance().getStrings();

        for (Symbol symbol : strings) {

            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(
                    String.format("    (data (i32.const %s) %s)%n", symbol.getValue(),
                            symbol.getLexema()));
        }

        return code.toString();
    }

    // --------------------------------------------------------------------------------------------

    static String getProgramName() {
        return SymbolTable.getInstance().getProgramName();
    }
}
