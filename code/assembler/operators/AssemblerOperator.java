package assembler.operators;

import java.util.Deque;

public interface AssemblerOperator {

    String getAssembler(Deque<String> operands, String indentation);

    default int getEntryIndentationChange() {
        return 0;
    }

    default int getExitIndentationChange() {
        return 0;
    }

    default boolean producesEntryChangeInIndentation() {
        return this.getEntryIndentationChange() != 0;
    }

    default boolean producesExitChangeInIndentation() {
        return this.getExitIndentationChange() != 0;
    }

}
