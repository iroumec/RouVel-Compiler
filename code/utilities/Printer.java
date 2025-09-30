package utilities;

public final class Printer {

    private final static String SEPARATOR = "------------------------------------";
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

    private static boolean wasThereNoSeparatorBefore() {
        return lastMessagePrinted == null || lastMessagePrinted != SEPARATOR;
    }
}
