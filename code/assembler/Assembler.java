package assembler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import assembler.operators.AssemblerOperator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import semantic.ReversePolish;

public class Assembler {

    private static final boolean debug = false;

    public static String generate(ReversePolish reversePolish) {

        StringBuilder imports = new StringBuilder();
        StringBuilder dataSection = new StringBuilder();

        StringBuilder declarations = new StringBuilder();

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();

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

                assemblerCode.append(operator.getAssembler(operands, indentation.toString())).append("\n");

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
        declarations.append("\n").append(dumpGlobalVariables());

        declarations.append(assemblerCode);

        assemblerCode = declarations;

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
}
