package common;

public enum SymbolCategory {
    VARIABLE("Variable"),
    PARAMETER("Parameter"), // Formal parameter
    ARGUMENT("Argument"), // Real parameter
    FUNCTION("Function");

    private String text;

    private SymbolCategory(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}