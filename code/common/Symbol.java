package common;

public class Symbol {

    private String scope; // Nivel de anidamiento.
    private int references;
    private SymbolType type;
    private StringBuilder value; // Valor real
    private StringBuilder lexema; // Lexema.
    private SymbolCategory category;

    public Symbol() {
        this.lexema = new StringBuilder("");
        this.value = new StringBuilder("");
        this.references = 0;
    }

    private Symbol(StringBuilder lexema, StringBuilder value, int references) {
        this.lexema = lexema;
        this.value = value;
        this.references = references;
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

    public StringBuilder getLexema() {
        return this.lexema;
    }

    // --------------------------------------------------------------------------------------------

    public StringBuilder getValue() {
        return this.value;
    }

    // --------------------------------------------------------------------------------------------

    public SymbolType getType() {
        return this.type == null ? "" : this.type;
    }
    
    // --------------------------------------------------------------------------------------------

    public String getScope() {
        return this.scope;
    }

    // --------------------------------------------------------------------------------------------

    public String getCategory() {
        return this.category == null ? "" : this.category;
    }

    public boolean isEmpty() {
        return lexema.length() == 0 && value.length() == 0 && type.length() == 0;
    }

    public void vaciar() {
        this.lexema.setLength(0);
        this.value.setLength(0);
        this.references = 0;
    }

    public void setLexema(StringBuilder lexema) {
        this.lexema = lexema;
    }

    public void setValue(StringBuilder value) {
        this.value = value;
    }

    public Symbol getNegative() {
        return new Symbol("-" + lexema,"-" + value,references);
    }
}