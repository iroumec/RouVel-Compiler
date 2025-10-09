package common;

/*
 * Esta clase se expandirá en
 * etapas posteriores del trabajo.
 * 
 * POR AHORA SOLO FUNCIONA COMO UNA PLANTILLA.
 */
public class Symbol {

    String name; // Lexema.
    String type; // Uint, float, etc.
    String category; // Variable, función, etc.
    int scope; // Nivel de anidamiento.
    int references;
    Object extra; // Extra.

    public Symbol(String lexema) {
        this.name = lexema;
        this.references = 0;
    }

    @Override
    public String toString() {
        int extraSpacesName = 20 - name.length();
        int extraSpacesType = 6;// - Objects.toString(type, "").length();
        int extraSpacesCategory = 11;// - Objects.toString(category, "").length();
        int extraSpacesScope = 9;// - Objects.toString(scope, "").length();
        int extraSpacesExtra = 7;// - Objects.toString(extra, "").length();
        int extraSpacesReferences = 2;

        return "| " +
                name
                + " ".repeat(Math.max(0, extraSpacesName)) + " | " +
                // type +
                " ".repeat(Math.max(0, extraSpacesType)) + " | " +
                // category +
                " ".repeat(Math.max(0, extraSpacesCategory)) + " | " +
                // scope +
                " ".repeat(Math.max(0, extraSpacesScope)) + " | " +
                // extra +
                " ".repeat(Math.max(0, extraSpacesExtra)) + " | " +
                references +
                " ".repeat(Math.max(0, extraSpacesReferences)) + " | ";
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