package common;

import java.util.Map;

import utilities.Printer;

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
     * Agrega un lexema a la tabla si no existe. Incrementa su referencia.
     */
    public void agregarEntrada(String lexema) {
        Symbol entrada = tablaSimbolos.get(lexema);
        if (entrada == null) {
            entrada = new Symbol(lexema);
            tablaSimbolos.put(lexema, entrada);
        }
        entrada.incrementarReferencias();
    }

    /**
     * Decrementa la referencia de un lexema. Si llega a 0, elimina la entrada.
     */
    public void decrementarReferencia(String lexema) {
        Symbol entrada = tablaSimbolos.get(lexema);
        if (entrada == null) {
            System.out.printf(
                    "Error inesperado. Se intentó decrementar la referencia del lexema \"%s\", que no existe.", lexema);
        }
        entrada.decrementarReferencias();
        if (entrada.sinReferencias()) {
            tablaSimbolos.remove(lexema);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void imprimirTabla() {
        Printer.printBigSeparation();
        System.out.println(" |        Lexema        |  Tipo  |  Categoría  |  Alcance  |  Extra  | Ref |");
        Printer.printBigSeparation();
        for (Map.Entry<String, Symbol> entrada : tablaSimbolos.entrySet()) {
            System.out.println(entrada.getValue().toString());
        }
        Printer.printBigSeparation();
    }

}
