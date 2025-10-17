package common;

public class Symbol {

    private String scope; // Nivel de anidamiento.
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
        this.references = 0;
    }

    // --------------------------------------------------------------------------------------------

    public int getReferences() {
        return this.references;
    }

    // --------------------------------------------------------------------------------------------

    public void incrementarReferencias() {
        this.references++;
    }

    // --------------------------------------------------------------------------------------------

    public void decrementarReferencias() {
        this.references--;
    }

    // --------------------------------------------------------------------------------------------

    public boolean sinReferencias() {
        return references == 0;
    }

    // --------------------------------------------------------------------------------------------

    public String getLexema() {
        return this.lexema.toString();
    }

    // --------------------------------------------------------------------------------------------

    public String getValue() {
        return this.value.toString();
    }

    // --------------------------------------------------------------------------------------------

    public SymbolType getType() {
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

    public String getTypeAsString() {
        return this.type == null ? "" : this.type.toString();
    }

    // --------------------------------------------------------------------------------------------

    public String getScope() {
        return this.scope == null ? "Global" : this.scope;
    }

    // --------------------------------------------------------------------------------------------

    public String getCategory() {
        return this.category == null ? "" : this.category.toString();
    }

    public boolean isEmpty() {
        return lexema.length() == 0 && value.length() == 0 && type.length() == 0;
    }

    public void setLexema(String lexema) {
        this.lexema.setLength(0);
        this.lexema.append(lexema);
    }

    public void setValue(String value) {
        this.value.setLength(0);
        this.value.append(value);
    }

    public Symbol getNegative() {
        return new Symbol("-" + lexema, "-" + value, references);
    }

    @Override
    public String toString() {
        return this.lexema.toString();
    }
}