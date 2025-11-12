package assembler;

import java.util.ArrayDeque;
import java.util.Deque;

import assembler.operators.Operator;
import common.SymbolTable;
import semantic.ReversePolish;

public class Assembler {

    public static String generate(ReversePolish reversePolish) {

        StringBuilder imports = new StringBuilder();
        StringBuilder dataSection = new StringBuilder();

        StringBuilder declarations = new StringBuilder();

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();

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

        SymbolTable symbolTable = SymbolTable.getInstance();

        return "";

    }
}
