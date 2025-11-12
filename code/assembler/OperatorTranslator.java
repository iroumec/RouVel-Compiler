package assembler;

import assembler.operators.Operator;
import assembler.operators.implementations.Assignment;
import assembler.operators.implementations.Call;
import assembler.operators.implementations.Label;
import assembler.operators.implementations.Print;
import assembler.operators.implementations.Return;
import assembler.operators.implementations.Sum;
import assembler.operators.implementations.arithmetic.Subtraction;

class OperatorTranslator {

    static Operator getOperator(String operator) {

        return switch (operator) {
            case "+" -> Sum.getInstance();
            case "-" -> Subtraction.getInstance();
            case "label" -> Label.getInstance();
            case "call" -> Call.getInstance();
            case "return" -> Return.getInstance();
            case "print" -> Print.getInstance();
            case ":=" -> Assignment.getInstance();
            case ">" -> null;
            case "<=" -> null;
            case ">=" -> null;
            case "==" -> null;
            case "<" -> null;
            case "=!" -> null;
            default -> null;
        };

    }
}
