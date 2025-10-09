package utilities;

public final class Printer {

    private final static String SEPARATOR = "------------------------------------";
    private final static String OLD_SEPARATION = "-———————————————————————————————————————————————————————————————————————————-";
    private final static String BIG_SEPARATION = " |=========================================================================|";
    private final static String BIG_SEPARATOR = "|=========================================================================|";
    private static String lastMessagePrinted = null;

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

        printSeparation();
        print(message);
        printSeparation();
    }

    public static void printSeparation() {

        // Se imprime un separador solo si no imprimió uno antes.
        if (wasThereNoSeparatorBefore()) {
            System.out.println(SEPARATOR);
            lastMessagePrinted = SEPARATOR;
        }
    }

    public static void printBigSeparation() {

        System.out.println(BIG_SEPARATION);
    }

    public static void printBigSeparator() {

        System.out.println(BIG_SEPARATOR);
    }

    public static void printIntroduction(String fileName) {
        printBlankSpace();
        printBigSeparator();
        System.out.println("| Resultados de la Compilación del Archivo: " + fileName + "                |");
        printBigSeparator();
        printBlankSpace();
    }

    public static void printBlankSpace() {
        System.out.print('\n');
    }

    private static boolean wasThereNoSeparatorBefore() {
        return lastMessagePrinted == null || lastMessagePrinted != SEPARATOR;
    }
}
