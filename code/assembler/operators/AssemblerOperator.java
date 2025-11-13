package assembler.operators;

import java.util.Deque;

public interface AssemblerOperator {

    String getAssembler(Deque<String> operands);

}
