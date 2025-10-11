
import java.io.File;
import java.io.IOException;

import lexer.Lexer;
import parser.Parser;
import common.SymbolTable;
import utilities.Printer;

public class Main {

    private static final String fileSuffix = ".uki";

    // --------------------------------------------------------------------------------------------

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Se debe indicar un archivo .uki como argumento.");
            return;
        }

        File file = new File(args[0]);
        if (isFileValid(file)) {
            System.out.println("El archivo no existe o no tiene la extensión '.uki': " + args[0]);
            return;
        }

        startCompilation(file);
    }

    // --------------------------------------------------------------------------------------------

    private static void startCompilation(File file) {

        Printer.printIntroduction(file.getName());

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        Printer.printSeparator();
        sintacticalAnalyzer.execute();
        Printer.printSeparator();

        printReport(lexicalAnalyzer, sintacticalAnalyzer);
    }

    // --------------------------------------------------------------------------------------------

    private static boolean isFileValid(File file) {
        return !file.exists() || !file.getName().endsWith(fileSuffix);
    }

    // --------------------------------------------------------------------------------------------

    private static void printReport(Lexer lexer, Parser parser) {

        String report = """
                El programa tiene %d líneas. \
                Se detectaron %d warnings y %d errores. \
                """.formatted(
                lexer.getNroLinea(),
                lexer.getWarningsDetected() + parser.getWarningsDetected(),
                lexer.getErrorsDetected() + parser.getErrorsDetected());

        Printer.printBlankSpace();
        Printer.printSeparator();
        Printer.printCentered("> Compilación Finalizada <");
        Printer.printCentered(report);
        Printer.printSeparator();
        Printer.printBlankSpace();

        SymbolTable.getInstance().imprimirTabla();
        Printer.printBlankSpace();
    }
}
