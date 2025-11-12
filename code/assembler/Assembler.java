package assembler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import assembler.operators.Operator;
import common.Symbol;
import common.SymbolCategory;
import common.SymbolTable;
import semantic.ReversePolish;

public class Assembler {

    public static String generate(ReversePolish reversePolish) {

        StringBuilder imports = new StringBuilder();
        StringBuilder dataSection = new StringBuilder();

        StringBuilder declarations = new StringBuilder();

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();

        assemblerCode.append(dumpGlobalVariables());

        for (String polish : reversePolish) {

            Operator operator = OperatorTranslator.getOperator(polish);

            if (operator != null) {
                assemblerCode.append(operator.getAssembler(operands)).append("\n");
            } else {
                operands.add(polish);
            }
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

        return code.append("\n").toString();
    }
}
