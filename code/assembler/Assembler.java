package assembler;

import java.util.ArrayDeque;
import java.util.Deque;

import assembler.operators.Operator;
import semantic.ReversePolish;

public class Assembler {

    public static String generate(ReversePolish reversePolish) {

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();

        for (String polish : reversePolish.getPolishes()) {

            Operator operator = OperatorTranslator.getOperator(polish);

            if (operator != null) {
                assemblerCode.append(operator.getAssembler(operands)).append("\n");
            } else {
                operands.add(polish);
            }
        }

        return assemblerCode.toString();
    }
}
