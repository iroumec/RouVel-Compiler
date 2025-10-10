package common;

/*
 * Esta clase se expandirá en
 * etapas posteriores del trabajo.
 * 
 * POR AHORA SOLO FUNCIONA COMO UNA PLANTILLA.
 */
public class Symbol {

    String lexema; // Lexema.
    String type; // Uint, float, etc.
    String category; // Variable, función, etc.
    int scope; // Nivel de anidamiento.
    int references;

    public Symbol(String lexema) {
        this.lexema = lexema;
        this.scope = 0;
        this.references = 0;
    }

    public String getLexema() {
        return this.lexema;
    }

    public String getType() {
        return this.type == null ? "" : this.type;
    }

    public int getScope() {
        return this.scope;
    }

    public String getCategory() {
        return this.category == null ? "" : this.category;
    }

    public int getReferences() {
        return this.references;
    }

    public void incrementarReferencias() {
        this.references++;
    }

    public void decrementarReferencias() {
        this.references--;
    }

    public boolean sinReferencias() {
        return references == 0;
    }
}