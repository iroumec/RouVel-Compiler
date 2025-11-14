package assembler;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.Argument;
import assembler.operators.implementations.Assignment;
import assembler.operators.implementations.Call;
import assembler.operators.implementations.EndLabel;
import assembler.operators.implementations.EndLoopLabel;
import assembler.operators.implementations.FunctionLabel;
import assembler.operators.implementations.Label;
import assembler.operators.implementations.LoopLabel;
import assembler.operators.implementations.Parameter;
import assembler.operators.implementations.Print;
import assembler.operators.implementations.ProgramLabel;
import assembler.operators.implementations.ReadResult;
import assembler.operators.implementations.Return;
import assembler.operators.implementations.WriteResult;
import assembler.operators.implementations.arithmetic.Division;
import assembler.operators.implementations.arithmetic.Multiplication;
import assembler.operators.implementations.arithmetic.Subtraction;
import assembler.operators.implementations.arithmetic.Sum;
import assembler.operators.implementations.bifurcations.TrueBifurcation;
import assembler.operators.implementations.comparison.Equal;
import assembler.operators.implementations.comparison.Greater;
import assembler.operators.implementations.comparison.GreaterOrEqual;
import assembler.operators.implementations.comparison.LessOrEqual;
import assembler.operators.implementations.comparison.LessThan;
import assembler.operators.implementations.comparison.NotEqual;

class OperatorTranslator {

    static AssemblerOperator getOperator(String operator) {

        return switch (operator) {
            case "+" -> Sum.getInstance();
            case "-" -> Subtraction.getInstance();
            case "*" -> Multiplication.getInstance();
            case "/" -> Division.getInstance();
            case "function-label" -> FunctionLabel.getInstance();
            case "program-label" -> ProgramLabel.getInstance();
            case "loop-label" -> LoopLabel.getInstance();
            case "end-loop-label" -> EndLoopLabel.getInstance();
            case "label" -> Label.getInstance();
            case "end-label" -> EndLabel.getInstance();
            case "call" -> Call.getInstance();
            case "->" -> Argument.getInstance();
            case "return" -> Return.getInstance();
            case "print" -> Print.getInstance();
            case "result" -> WriteResult.getInstance();
            case "<-" -> ReadResult.getInstance();
            case ":=" -> Assignment.getInstance();
            case "parameter" -> Parameter.getInstance();
            case ">" -> Greater.getInstance();
            case "<=" -> LessOrEqual.getInstance();
            case ">=" -> GreaterOrEqual.getInstance();
            case "==" -> Equal.getInstance();
            case "<" -> LessThan.getInstance();
            case "=!" -> NotEqual.getInstance();
            case "TB" -> TrueBifurcation.getInstance();
            case "FB" -> null;
            case "UB" -> null;
            default -> null;
        };

    }
}
