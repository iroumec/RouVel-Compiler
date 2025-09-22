package common;

import java.util.Map;
import java.util.HashMap;

public final class SymbolTable {

    private static final SymbolTable INSTANCE = new SymbolTable();

    // --------------------------------------------------------------------------------------------

    private final Map<String, Symbol> tablaSimbolos = new HashMap<>();

    // --------------------------------------------------------------------------------------------

    private SymbolTable() {
    }

    // --------------------------------------------------------------------------------------------

    public static SymbolTable getInstance() {
        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Agrega un lexema a la tabla si no existe.
     */
    public void agregarEntrada(String lexema) {
        tablaSimbolos.put(lexema, new Symbol(lexema));
    }
}
