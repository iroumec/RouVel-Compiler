package semantic;

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

    // --------------------------------------------------------------------------------------------

    public String appendScope(String lexema) {
        return lexema + ":" + this.asText();
    }

    // --------------------------------------------------------------------------------------------

    public boolean isReacheable(String scope) {
        if (asText().indexOf(scope) == 0) // Si el ámbito es el nombre de programa
            return true;
        return asText().contains(":" + scope);
    }

    // --------------------------------------------------------------------------------------------

    public String getScopeRoad(String scope) {
        if (asText().indexOf(scope) == 0)
            return scope + ":";
        return asText().split(":" + scope + ":", 2)[0] + ":" + scope + ":";
    }
}
