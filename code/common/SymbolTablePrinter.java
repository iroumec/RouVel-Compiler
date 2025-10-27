package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utilities.Printer;

/**
 * Clase responsable de la impresión formateada de la tabla de símbolos.
 * 
 * Implementa el patrón Singleton utilizando el idiom "Initialization-on-demand
 * holder",
 * el cual es thread-safe y garantiza la inicialización diferida sin necesidad
 * de sincronización explícita.
 */
public final class SymbolTablePrinter {

    // ============================================================================================
    // Singleton thread-safe y lazy-loaded
    // ============================================================================================

    private SymbolTablePrinter() {
    }

    private static class Holder {
        private static final SymbolTablePrinter INSTANCE = new SymbolTablePrinter();
    }

    public static SymbolTablePrinter getInstance() {
        return Holder.INSTANCE;
    }

    // ============================================================================================
    // Configuración
    // ============================================================================================

    private static final String[] HEADERS = { "Lexema", "Valor", "Tipo", "Categoría", "Referencias" };

    // ============================================================================================
    // Impresión de la Tabla de Símbolos
    // ============================================================================================

    /**
     * Método principal que coordina la impresión de la tabla de símbolos.
     */
    void print(Collection<Symbol> symbols) {
        if (symbols.isEmpty()) {
            Printer.printWrapped("La tabla de símbolos está vacía.");
            return;
        }

        // Se calculan los anchos de las columnas.
        Map<String, Integer> columnWidths = calculateColumnWidths(HEADERS, symbols);

        // Se crean las plantillas de formato.
        String rowFormat = buildRowFormat(HEADERS, columnWidths);

        // Se imprime el encabezado y cuerpo de la tabla.
        printHeader(rowFormat, HEADERS);
        printBody(rowFormat, columnWidths, symbols);

        Printer.printSeparator();
    }

    // ============================================================================================
    // Métodos auxiliares
    // ============================================================================================

    /**
     * Calcula los anchos de columna a partir de los encabezados y el contenido.
     * Aplica un límite máximo al ancho de la columna "Lexema".
     */
    private Map<String, Integer> calculateColumnWidths(String[] headers, Collection<Symbol> symbols) {
        Map<String, Integer> widths = new LinkedHashMap<>();
        for (String header : headers)
            widths.put(header, header.length());

        int maxLexemaWidth = Printer.getLineWidth() / 2;

        symbols.forEach(symbol -> {
            widths.compute("Lexema", (_, v) -> Math.min(maxLexemaWidth, Math.max(v, symbol.getLexema().length())));
            widths.compute("Valor", (_, v) -> Math.max(v, symbol.getValue().length()));
            widths.compute("Tipo", (_, v) -> Math.max(v, safeToString(symbol.getType()).length()));
            widths.compute("Categoría", (_, v) -> Math.max(v, safeToString(symbol.getCategory()).length()));
            widths.compute("Referencias", (_, v) -> Math.max(v, String.valueOf(symbol.getReferences()).length()));
        });

        distributeExtraSpace(headers, widths);
        return widths;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Distribuye el espacio sobrante entre las columnas, excluyendo el "Lexema".
     */
    private void distributeExtraSpace(String[] headers, Map<String, Integer> widths) {
        int requiredWidth = widths.values().stream().mapToInt(Integer::intValue).sum() + (headers.length * 3) + 1;
        int extraSpace = Printer.getLineWidth() - requiredWidth;

        if (extraSpace <= 0)
            return;

        int otherColumns = headers.length - 1;
        if (otherColumns <= 0)
            return;

        int spacePerColumn = extraSpace / otherColumns;
        int remaining = extraSpace % otherColumns;

        for (String header : headers) {
            if (!header.equals("Lexema")) {
                int newWidth = widths.get(header) + spacePerColumn;
                if (remaining > 0) {
                    newWidth++;
                    remaining--;
                }
                widths.put(header, newWidth);
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Construye el formato de impresión para cada fila (ej: "| %-20s | %-10s |").
     */
    private String buildRowFormat(String[] headers, Map<String, Integer> widths) {
        StringBuilder builder = new StringBuilder("|");
        for (String header : headers) {
            builder.append(" %-").append(widths.get(header)).append("s |");
        }
        return builder.toString();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Imprime la cabecera de la tabla.
     */
    private void printHeader(String format, String[] headers) {
        Printer.printSeparator();
        Printer.printCentered("Tabla de Símbolos");
        Printer.printSeparator();
        Printer.print(String.format(format, (Object[]) headers));
        Printer.printSeparator();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Imprime el cuerpo de la tabla de símbolos.
     * Maneja el ajuste de texto para lexemas largos.
     */
    private void printBody(String rowFormat, Map<String, Integer> widths, Collection<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            List<String> lexemaLines = wrapText(symbol.getLexema(), widths.get("Lexema"));

            // Primera línea con todos los datos.
            Printer.print(String.format(rowFormat,
                    lexemaLines.get(0),
                    symbol.getValue(),
                    safeToString(symbol.getType()),
                    safeToString(symbol.getCategory()),
                    String.valueOf(symbol.getReferences())));

            // Líneas adicionales solo para el lexema.
            for (int i = 1; i < lexemaLines.size(); i++) {
                Printer.print(String.format(rowFormat, lexemaLines.get(i), "", "", "", ""));
            }

            Printer.printSeparator();
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Convierte un objeto a texto de forma segura.
     * Retorna cadena vacía si es null.
     */
    private String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Divide un texto largo en varias líneas, según un ancho máximo.
     */
    private List<String> wrapText(String text, int maxWidth) {
        if (text == null || text.isEmpty())
            return List.of("");
        int len = text.length();
        List<String> lines = new ArrayList<>((len + maxWidth - 1) / maxWidth);
        for (int i = 0; i < len; i += maxWidth) {
            lines.add(text.substring(i, Math.min(len, i + maxWidth)));
        }
        return lines;
    }

}
