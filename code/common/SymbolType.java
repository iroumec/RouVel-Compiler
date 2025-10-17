package common;

public enum SymbolType {
    UINT("Uint"),
    FLOAT("Float"),
    STRING("String");

    private String text;

    private SymbolType(String text) {
        this.text = text;
    }

    public int length() {
        return this.text.length();
    }

    @Override
    public String toString() {
        return this.text;
    }
}