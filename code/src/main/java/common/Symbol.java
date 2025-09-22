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
    Object extra; // Extra.

    public Symbol(String lexema) {
        this.name = lexema;
    }
}