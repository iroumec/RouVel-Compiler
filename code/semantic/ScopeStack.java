package semantic;

import java.util.Deque;
import java.util.ArrayDeque;

import common.SymbolTable;

public class ScopeStack {

    // ============================================================================================

    private final Deque<String> stack;

    // ============================================================================================

    public ScopeStack() {
        this.stack = new ArrayDeque<>();
    }

    // ============================================================================================

    public void push(String scope) {
        stack.push(scope);
    }

    // ============================================================================================

    public String pop() {
        return stack.pop();
    }

    // ============================================================================================

    public String peek() {
        return stack.peek();
    }

    // ============================================================================================

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // ============================================================================================

    public String asText() {
        Iterable<String> iterable = () -> stack.descendingIterator();
        return String.join(":", iterable);
    }

    // ============================================================================================

    public String appendScope(String lexema) {
        return lexema + ":" + this.asText();
    }

    // ============================================================================================

    public boolean isReacheable(String scope) {
        String text = asText();  

        // Está al inicio exactamente
        if (text.startsWith(scope)) {
            // Se verifica que le siga ":" o nada
            if (text.length() == scope.length() || text.charAt(scope.length()) == ':') {
                return true;
            }
        }

        // Se busca ":" + scope + ":" o ":" + scope al final
        return text.contains(":" + scope + ":") || text.endsWith(":" + scope);
    }

    // ============================================================================================

    public String getScopeRoad(String scope) {
        String text = asText();

        if (SymbolTable.getInstance().getProgramName().equals(scope)) {
            return ":" + scope;
        }

        String target = ":" + scope;
        int index = text.indexOf(target);

        if (index == -1) {
            return null;
        }

        int end = index + target.length();

        return ":" + text.substring(0, end);
    }

    // ============================================================================================
}
