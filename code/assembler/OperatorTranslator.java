package assembler;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.Argument;
import assembler.operators.implementations.Assignment;
import assembler.operators.implementations.Call;
import assembler.operators.implementations.EndLabel;
import assembler.operators.implementations.Label;
import assembler.operators.implementations.Print;
import assembler.operators.implementations.Return;
import assembler.operators.implementations.arithmetic.Division;
import assembler.operators.implementations.arithmetic.Multiplication;
import assembler.operators.implementations.arithmetic.Subtraction;
import assembler.operators.implementations.arithmetic.Sum;

class OperatorTranslator {

    static AssemblerOperator getOperator(String operator) {

        return switch (operator) {
            case "+" -> Sum.getInstance();
            case "-" -> Subtraction.getInstance();
            case "*" -> Multiplication.getInstance();
            case "/" -> Division.getInstance();
            case "label" -> Label.getInstance();
            case "end-label" -> EndLabel.getInstance();
            case "call" -> Call.getInstance();
            case "->" -> Argument.getInstance();
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
