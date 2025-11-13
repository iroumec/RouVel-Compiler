package assembler.operators.implementations.comparison;

import java.util.Deque;

import assembler.operators.AssemblerOperator;

public class GreaterOrEqualThan implements AssemblerOperator {

    @Override
    public String getAssembler(Deque<String> operands, String indentation) {

        return indentation + "i32.gt_u";
    }

}
