package utilities;

public final class Printer {

    private final static int LINE_WIDTH = 130;
    private final static String SEPARATOR;
    private static String lastMessagePrinted = null;

    /**
     * Construcción del separador de acuerdo al ancho de línea.
     */
    static {
        SEPARATOR = "|" + "=".repeat(LINE_WIDTH - 2) + "|";
    }

    private Printer() {
    }

    public static void print(String message) {
        System.out.println(message);
        lastMessagePrinted = message;
    }

    public static void print(Object object) {
        print(object.toString());
    }

    public static void printBetweenSeparations(String message) {

        printSeparator();
        print(message);
        printSeparator();
    }

    public static void printSeparator() {

        // Se imprime un separador solo si no imprimió uno antes.
        if (wasThereNoSeparatorBefore()) {
            print(SEPARATOR);
        }
    }

    public static void printFramed(Object message) {
        printFramed(message.toString());
    }

    public static void printFramed(String message) {
        String[] lines = message.split("\n");
        for (String line : lines) {
            printFrameLine(line);
        }
    }

    private static void printFrameLine(String content) {
        String padded = " " + content; // Padding izquierdo
        StringBuilder sb = new StringBuilder("|");
        sb.append(padded);

        // Relleno con espacios hasta alcanzar el ancho
        while (sb.length() < LINE_WIDTH - 1) {
            sb.append(" ");
        }

        sb.append("|");
        print(sb.toString());
    }

    public static void printCentered(String message) {
        if (message.length() >= LINE_WIDTH - 2) {
            // Si el mensaje es muy largo, se imprime tal cual.
            print(message);
            return;
        }

        // Se calcula el padding (relleno).
        // Espacio total disponible dentro de las '|'.
        int availableSpace = LINE_WIDTH - 2;
        int paddingSize = (availableSpace - message.length()) / 2;

        // Se construye la línea formateada.a
        StringBuilder formattedLine = new StringBuilder("|");
        // Relleno izquierdo.
        formattedLine.append(" ".repeat(paddingSize));
        // Mensaje.
        formattedLine.append(message);
        // Relleno derecho (ajustado en caso de impar).
        while (formattedLine.length() < LINE_WIDTH - 1) {
            formattedLine.append(" ");
        }
        formattedLine.append("|");

        print(formattedLine.toString());
    }

    /**
     * Toma un mensaje largo, lo divide en varias líneas y lo imprime enmarcado.
     * 
     * @param message
     */
    public static void printWrapped(String message) {

        printSeparator();

        // El espacio útil para el texto es el ancho total menos
        // los bordes y un pequeño margen.
        int effectiveWidth = LINE_WIDTH - 4;
        String[] words = message.split("\\s+");

        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            // Si la palabra actual no cabe en la línea actual...
            if (currentLine.length() + word.length() + 1 > effectiveWidth) {
                // Se imprime la línea actual y empezamos una nueva.
                printFramed(currentLine.toString());
                currentLine = new StringBuilder();
            }
            // Se agrega la palabra a la línea actual.
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }
        // Se imprime la última línea que quedó.
        if (currentLine.length() > 0) {
            printFramed(currentLine.toString());
        }

        printSeparator();
    }

    public static void printIntroduction(String fileName) {

        printBlankSpace();
        printSeparator();
        printCentered("Resultados de la Compilación");
        printCentered("Archivo: " + fileName);
        printSeparator();
        printBlankSpace();
    }

    public static void printBlankSpace() {
        print('\n');
    }

    private static boolean wasThereNoSeparatorBefore() {
        return lastMessagePrinted == null || lastMessagePrinted != SEPARATOR;
    }

    public static int getLineWidth() {
        return LINE_WIDTH;
    }
}
