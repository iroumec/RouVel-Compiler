package common;

import java.util.Map;

import utilities.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class SymbolTable {

    private static final SymbolTable INSTANCE = new SymbolTable();

    // --------------------------------------------------------------------------------------------

    private final Map<String, Symbol> symbolTable = new HashMap<>();

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
    public void addEntry(String lexema, Symbol newSymbol) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol == null) {
            symbolTable.put(lexema, newSymbol);
            newSymbol.incrementarReferencias();
        } else {
            symbol.incrementarReferencias();
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Remplaza una entrada en la tabla de símbolos.
     */
    public void replaceEntry(String oldLexema, String newLexema) {

        if (oldLexema == null || newLexema == null) {
            return;
        } else {
            Symbol symbol = this.symbolTable.get(oldLexema);
            symbol.decrementarReferencias();

            this.addEntry(newLexema, symbol);
        }
    }

    /**
     * Reemplaza una entrada en la tabla por otra. Se utiliza únicamente en la
     * detección de constantes negativas.
     */
    public void switchEntrySign(String lexema) {

        Symbol entrada = symbolTable.get(lexema);
        if (entrada == null) {
            Printer.printWrapped(String.format(
                    "Error inesperado. Se intentó decrementar la referencia del lexema \"%s\", que no existe.",
                    lexema));
        }

        // Decrementa las referencias del simbolo anterior.
        entrada.decrementarReferencias();

        // Si el símbolo queda sin referencias, se da de baja de la tabla de símbolos.
        if (entrada.sinReferencias()) {
            symbolTable.remove(lexema);
        }

        // Alta de la tabla de símbolos.
        this.addEntry("-" + lexema, entrada.getNegative());
    }

    // --------------------------------------------------------------------------------------------

    public void setType(String lexema, SymbolType type) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setType(type);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void setValue(String lexema, String value) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setValue(value);
        }
    }

    // --------------------------------------------------------------------------------------------

    public void setCategory(String lexema, SymbolCategory category) {

        Symbol symbol = this.symbolTable.get(lexema);

        if (symbol != null) {
            symbol.setCategory(category);
        }
    }

    // ============================================================================================
    // Impresión de la Tabla (Objetivo Estéticos)
    // ============================================================================================

    /**
     * Método principal que coordina la impresión de la tabla de símbolos.
     */
    public void imprimirTabla() {
        if (symbolTable.isEmpty()) {
            Printer.printWrapped("La tabla de símbolos está vacía.");
            return;
        }

        // Se analizan y calculan los anchos de columna.
        String[] headers = { "Lexema", "Valor", "Tipo", "Categoría", "Alcance", "Referencias" };
        Map<String, Integer> columnWidths = calculateColumnWidths(headers);

        // Se crean las plantillas para las filas y separadores.
        String rowFormat = buildRowFormat(headers, columnWidths);

        // Se imprime toda la tabla.
        printHeader(rowFormat, headers);
        printBody(rowFormat, headers, columnWidths);

        // Se imprime el separador al final de la tabla.
        Printer.printSeparator();
    }

    // =====================================================================
    // MÉTODOS PRIVADOS AUXILIARES
    // =====================================================================

    /**
     * Calcula los anchos de columna, aplicando un límite máximo a la columna del
     * lexema.
     */
    private Map<String, Integer> calculateColumnWidths(String[] headers) {
        Map<String, Integer> widths = new LinkedHashMap<>();
        for (String header : headers) {
            widths.put(header, header.length()); // Ancho mínimo es el del encabezado.
        }

        // Se define un ancho máximo para el lexema como un porcentaje del total.
        // Por ejemplo, que el lexema no ocupe más del 50% del ancho de la consola.
        int maxLexemaWidth = Printer.getLineWidth() / 2;

        for (Symbol symbol : symbolTable.values()) {
            // Se usa Math.min para no superar el ancho máximo del lexema.
            int lexemaLength = symbol.getLexema().length();
            widths.put("Lexema", Math.min(maxLexemaWidth, Math.max(widths.get("Lexema"), lexemaLength)));
            widths.put("Valor", Math.max(widths.get("Valor"), symbol.getValue().length()));
            widths.put("Tipo", Math.max(widths.get("Tipo"), symbol.getTypeAsString().length()));
            widths.put("Categoría", Math.max(widths.get("Categoría"), symbol.getCategory().length()));
            widths.put("Alcance", Math.max(widths.get("Alcance"), String.valueOf(symbol.getScope()).length()));
            widths.put("Referencias",
                    Math.max(widths.get("Referencias"), String.valueOf(symbol.getReferences()).length()));
        }

        distributeExtraSpace(headers, widths);
        return widths;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Distribuye el espacio sobrante entre las columnas, EXCLUYENDO el lexema.
     */
    private void distributeExtraSpace(String[] headers, Map<String, Integer> widths) {
        int requiredWidth = widths.values().stream().mapToInt(Integer::intValue).sum() + (headers.length * 3) + 1;
        int extraSpace = Printer.getLineWidth() - requiredWidth;

        if (extraSpace > 0) {
            // El espacio extra se reparte solo entre las columnas secundarias.
            int otherColumnsCount = headers.length - 1;
            if (otherColumnsCount > 0) {
                int spacePerColumn = extraSpace / otherColumnsCount;
                for (String header : headers) {
                    if (!header.equals("Lexema")) { // No se agrega espacio extra al lexema.
                        widths.put(header, widths.get(header) + spacePerColumn);
                    }
                }

                // El resto se puede repartir entre las columnas restantes de forma simple.
                int remainingSpace = extraSpace % otherColumnsCount;
                for (String header : headers) {
                    if (remainingSpace == 0)
                        break;
                    if (!header.equals("Lexema")) {
                        widths.put(header, widths.get(header) + 1);
                        remainingSpace--;
                    }
                }
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se construye el string de formato para una fila (ej: "| %-20s | %-10s
     * |").
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
     * Se imprime el título y la cabecera de la tabla.
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
     * Se imprime el cuerpo de la tabla, manejando el ajuste de líneas.
     */
    private void printBody(String rowFormat, String[] headers, Map<String, Integer> widths) {

        for (Symbol symbol : symbolTable.values()) {
            List<String> lexemaLines = wrapText(symbol.getLexema(), widths.get("Lexema"));

            // Imprime la primera (o única) línea con todos los datos.
            String firstLexema = lexemaLines.isEmpty() ? "" : lexemaLines.get(0);
            Printer.print(String.format(rowFormat,
                    firstLexema,
                    symbol.getValue(),
                    symbol.getTypeAsString(),
                    symbol.getCategory(),
                    String.valueOf(symbol.getScope()),
                    String.valueOf(symbol.getReferences())));

            // Imprime las líneas restantes del lexema si existen.
            for (int i = 1; i < lexemaLines.size(); i++) {
                // CORRECCIÓN: Ahora tiene 5 argumentos (1 para el lexema + 4 vacíos).
                Printer.print(String.format(rowFormat, lexemaLines.get(i), "", "", "", ""));
            }

            Printer.printSeparator();
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Método auxiliar para dividir un texto si excede un ancho máximo.
     */
    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        for (int i = 0; i < text.length(); i += maxWidth) {
            lines.add(text.substring(i, Math.min(text.length(), i + maxWidth)));
        }
        return lines;
    }

}
