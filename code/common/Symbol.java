package common;

/*
 * Esta clase se expandirá en
 * etapas posteriores del trabajo.
 * 
 * POR AHORA SOLO FUNCIONA COMO UNA PLANTILLA.
 */
public class Symbol {

    StringBuilder lexema; // Lexema.
    StringBuilder value; // Valor real
    String type; // Uint, float, etc.
    String category; // Variable, función, etc.
    String scope; // Nivel de anidamiento.
    int references;

    public Symbol() {
        this.lexema = new StringBuilder("");
        this.value = new StringBuilder("");
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

    public StringBuilder getLexema() {
        return this.lexema;
    }

    // --------------------------------------------------------------------------------------------

    public String getType() {
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
}