package common;

public class Symbol {

    private int references;
    private SymbolType type;
    private StringBuilder value; // Valor real
    private StringBuilder lexema; // Lexema.
    private SymbolCategory category;

    private Symbol(String lexema, String value, int references) {
        this.lexema = new StringBuilder(lexema);
        this.value = new StringBuilder(value);
        this.references = references;
    }

    public Symbol(String lexema, String value, SymbolType type) {
        this.lexema = new StringBuilder(lexema);
        this.value = new StringBuilder(value);
        this.type = type;

        if (type != null) {
            this.category = SymbolCategory.CONSTANT;
        }

        this.references = 0;
    }

    // --------------------------------------------------------------------------------------------

    int getReferences() {
        return this.references;
    }

    // --------------------------------------------------------------------------------------------

    void incrementarReferencias() {
        this.references++;
    }

    // --------------------------------------------------------------------------------------------

    void decrementarReferencias() {
        this.references--;
    }

    // --------------------------------------------------------------------------------------------

    boolean hasNoReferences() {
        return references == 0;
    }

    // --------------------------------------------------------------------------------------------

    String getLexema() {
        return this.lexema.toString();
    }

    // --------------------------------------------------------------------------------------------

    String getValue() {
        return this.value.toString();
    }

    // --------------------------------------------------------------------------------------------

    SymbolType getType() {
        return this.type == null ? null : this.type;
    }

    // --------------------------------------------------------------------------------------------

    void setType(SymbolType newType) {
        this.type = newType;
    }

    // --------------------------------------------------------------------------------------------

    void setCategory(SymbolCategory newCategory) {
        this.category = newCategory;
    }

    // --------------------------------------------------------------------------------------------

    SymbolCategory getCategory() {
        return this.category;
    }

    boolean isEmpty() {
        return lexema.length() == 0 && value.length() == 0 && type.length() == 0;
    }

    void setLexema(String lexema) {
        this.lexema.setLength(0);
        this.lexema.append(lexema);
    }

    void setValue(String value) {
        this.value.setLength(0);
        this.value.append(value);
    }

    Symbol getNegative() {
        return new Symbol("-" + lexema, "-" + value, references);
    }

    @Override
    public String toString() {
        return this.lexema.toString();
    }
}