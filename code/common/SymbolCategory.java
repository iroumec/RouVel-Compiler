package common;

public enum SymbolCategory {
    VARIABLE("Variable"),
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