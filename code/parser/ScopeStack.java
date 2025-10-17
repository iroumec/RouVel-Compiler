package parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScopeStack {

    private final Deque<String> stack = new ArrayDeque<>();

    // --------------------------------------------------------------------------------------------

    public void push(String text) {
        stack.push(text);
    }

    // --------------------------------------------------------------------------------------------

    public String pop() {
        return stack.pop();
    }

    // --------------------------------------------------------------------------------------------

    public String peek() {
        return stack.peek();
    }

    // --------------------------------------------------------------------------------------------

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // --------------------------------------------------------------------------------------------

    public String asText() {
        Iterable<String> iterable = () -> stack.descendingIterator();
        return String.join(":", iterable);
    }
}
