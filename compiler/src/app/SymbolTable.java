package app;

import java.util.HashMap;

public final class SymbolTable {

    private static int nroEntradas = 0;
    private final static HashMap<Integer, String> tabla = new HashMap<>();

    private SymbolTable() {
    }

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     * 
     * @param simbolo
     * @return El índice en el que se agregó la entrada.
     */
    public static int agregarEntrada(TokenType tokenType, String lexema) {

        if (!tokenType.requiereLexema()) {
            System.err.println("Este tipo de token no es válido para agregar a la tabla.");
            System.exit(1);
        }

        tabla.put(nroEntradas, lexema);

        return nroEntradas++;
    }

    public static String getLexema(int indice) {
        return tabla.get(indice);
    }

}