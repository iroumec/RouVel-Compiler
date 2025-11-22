package assembler;

import assembler.operators.AssemblerOperator;
import assembler.operators.implementations.Print;
import assembler.operators.implementations.EndLabel;
import assembler.operators.implementations.Assignment;
import assembler.operators.implementations.arithmetic.Sum;
import assembler.operators.implementations.comparison.Equal;
import assembler.operators.implementations.loops.LoopOpener;
import assembler.operators.implementations.selections.SelectionOpener;
import assembler.operators.implementations.selections.ConditionCloser;
import assembler.operators.implementations.selections.SelectionCloser;
import assembler.operators.implementations.selections.ThenBlockCloser;
import assembler.operators.implementations.loops.LoopCloser;
import assembler.operators.implementations.loops.LoopJumper;
import assembler.operators.implementations.comparison.Greater;
import assembler.operators.implementations.comparison.NotEqual;
import assembler.operators.implementations.comparison.LessThan;
import assembler.operators.implementations.arithmetic.Division;
import assembler.operators.implementations.comparison.LessOrEqual;
import assembler.operators.implementations.arithmetic.Subtraction;
import assembler.operators.implementations.functions.call.Argument;
import assembler.operators.implementations.comparison.GreaterOrEqual;
import assembler.operators.implementations.functions.call.ReadResult;
import assembler.operators.implementations.functions.call.ReadReturn;
import assembler.operators.implementations.arithmetic.Multiplication;
import assembler.operators.implementations.functions.call.FunctionCall;
import assembler.operators.implementations.functions.declaration.Return;
import assembler.operators.implementations.functions.lambdas.LambdaCloser;
import assembler.operators.implementations.functions.lambdas.LambdaOpener;
import assembler.operators.implementations.functions.declaration.FunctionCloser;
import assembler.operators.implementations.functions.declaration.FunctionOpener;

class OperatorTranslator {

    static AssemblerOperator getOperator(String operator) {

        return switch (operator) {
            default -> null;
            case "+" -> Sum.getInstance();
            case "==" -> Equal.getInstance();
            case ">" -> Greater.getInstance();
            case "<" -> LessThan.getInstance();
            case "/" -> Division.getInstance();
            case "=!" -> NotEqual.getInstance();
            case "->" -> Argument.getInstance();
            case "print" -> Print.getInstance();
            case "-" -> Subtraction.getInstance();
            case "return" -> Return.getInstance();
            case "<-" -> ReadResult.getInstance();
            case ":=" -> Assignment.getInstance();
            case "<=" -> LessOrEqual.getInstance();
            case "*" -> Multiplication.getInstance();
            case "call" -> FunctionCall.getInstance();
            case ">=" -> GreaterOrEqual.getInstance();
            case "TB" -> LoopJumper.getInstance();
            case "end-label" -> EndLabel.getInstance();
            case "FB" -> ConditionCloser.getInstance();
            case "open-loop" -> LoopOpener.getInstance();
            case "UB" -> ThenBlockCloser.getInstance();
            case "close-loop" -> LoopCloser.getInstance();
            case "read-return" -> ReadReturn.getInstance();
            case "open-function" -> FunctionOpener.getInstance();
            case "close-function" -> FunctionCloser.getInstance();
            case "open-lambda" -> LambdaOpener.getInstance();
            case "close-lambda" -> LambdaCloser.getInstance();
            case "open-selection" -> SelectionOpener.getInstance();
            case "close-selection" -> SelectionCloser.getInstance();
        };

    }
}
