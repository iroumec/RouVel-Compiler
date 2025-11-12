package assembler.operators;

import java.util.Deque;

public interface Operator {

    String getAssembler(Deque<String> operands);

}
