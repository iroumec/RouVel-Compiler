package assembler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import common.SymbolType;
import semantic.ReversePolish;

public class Assembler {

    private static final boolean debug = false;

    public static String generate(ReversePolish reversePolish) {

        StringBuilder imports = new StringBuilder();
        StringBuilder dataSection = new StringBuilder();

        String stringsSection = dumpStrings();

        StringBuilder declarations = new StringBuilder();

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();
        StringBuilder executableCode = new StringBuilder();

        StringBuilder indentation = new StringBuilder();

        for (String polish : reversePolish) {

            AssemblerOperator operator = OperatorTranslator.getOperator(polish);

            if (operator != null) {
                if (debug) {
                    System.out.println("Operator " + polish + " detected.");
                }

                if (operator.producesExitChangeInIndentation()) {
                    indentation.setLength(indentation.length() - operator.getExitIndentationChange());
                }

                String iterationCode = operator.getAssembler(operands, indentation.toString());
                if (!iterationCode.isBlank()) {
                    executableCode.append(iterationCode).append("\n");
                }

                if (operator.producesEntryChangeInIndentation()) {
                    indentation.append(" ".repeat(operator.getEntryIndentationChange()));
                }

            } else {
                operands.push(polish);

                if (debug) {
                    System.out.println("Polish " + polish + " added to the operands.");
                }
            }
        }

        // El volcado de las variables globales se hace a lo último, ya que, durante la
        // generación del código assembler, se generan variables auxiliares que también
        // deben ser agregadas.

        // Si hay al menos una impresión, se debe importar el módulo de impresiones.
        if (executableCode.toString().contains("$print")) {
            assemblerCode.append("""
                    (module
                        (import "console" "log"
                            (func $print (param i32 i32)))
                        (import "js" "mem" (memory 1))
                    )
                    """).append("\n");
        }

        assemblerCode.append(dumpGlobalVariables());

        if (!stringsSection.isBlank()) {
            assemblerCode.append("\n").append(stringsSection);
        }

        if (!executableCode.isEmpty()) {
            assemblerCode.append("\n").append(executableCode);
        }

        return assemblerCode.toString();
    }

    private static String dumpGlobalVariables() {

        StringBuilder code = new StringBuilder();

        List<Symbol> globales = SymbolTable.getInstance().get("MAIN", SymbolCategory.VARIABLE);

        for (Symbol symbol : globales) {
            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("(local $%s i32)%n", symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }

    private static String dumpStrings() {

        StringBuilder code = new StringBuilder();

        int stringNumber = 0;

        List<Symbol> strings = SymbolTable.getInstance().get(null, SymbolType.STRING);

        for (Symbol symbol : strings) {

            // Se le asigna el valor al string para luego utilizarlo en la generación de
            // código.
            SymbolTable.getInstance().setValue(symbol.getLexema(), String.valueOf(stringNumber));

            // Todas las variables que se tienen en el lenguaje son enteros de 32 bits.
            // Por eso está "hardcodeado" el "i32".
            code.append(String.format("(data (i32.const %d) %s)%n", stringNumber++, symbol.getLexemaWithoutScope()));
        }

        return code.toString();
    }
}
