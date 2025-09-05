package general;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class SymbolTable {

    private static final SymbolTable INSTANCE = new SymbolTable();

    // --------------------------------------------------------------------------------------------

    private final BiMap<Integer, String> tabla = HashBiMap.create();

    // --------------------------------------------------------------------------------------------

    private int nroEntradas = 0;

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
     * Devuelve el índice existente o el nuevo.
     */
    public synchronized int agregarEntrada(TokenType tokenType, String lexema) {
        if (!tokenType.requiereLexema()) {
            throw new IllegalArgumentException(
                    "Este tipo de token no es válido para agregar a la tabla.");
        }

        // Si ya existe una entrada para el lexema, se devuelve su índice.
        if (tabla.containsValue(lexema)) {
            return tabla.inverse().get(lexema);
        }

        int indice = nroEntradas++;
        tabla.put(indice, lexema);
        return indice;
    }

    // --------------------------------------------------------------------------------------------

    public synchronized String getLexema(int indice) {
        return tabla.get(indice);
    }
}
