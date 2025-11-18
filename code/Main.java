
import java.io.File;
import java.io.IOException;

import lexer.Lexer;
import parser.Parser;
import common.Monitor;
import utilities.Printer;
import common.SymbolTable;
import assembler.Assembler;
import semantic.ReversePolish;
import assembler.WebAssemblyExporter;

public class Main {

    private static final String fileSuffix = ".uki";
    private static final boolean printAssemblerCode = true;

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

        Monitor monitor = Monitor.getInstance();

        Printer.printIntroduction(file.getName());

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        Printer.printSeparator();
        sintacticalAnalyzer.execute();
        Printer.printSeparator();

        printReport();

        if (printAssemblerCode) {
            Printer.printSeparator();
            Printer.printCentered("Código WebAssembly");
            Printer.printSeparator();
        }

        if (!monitor.hasErrorMessages()) {

            String assemblerCode = Assembler.generate(sintacticalAnalyzer.getReversePolish());

            if (printAssemblerCode) {
                Printer.printFramed(assemblerCode);
                Printer.printSeparator();
                Printer.printBlankSpace();
            }

            Printer.printSeparator();
            WebAssemblyExporter.exportToWasm(file, assemblerCode);
            Printer.printSeparator();
            Printer.printBlankSpace();
        } else {
            Printer.printFramed("El código contiene errores, por lo que no fue posible generar un código assembler.");
            Printer.printSeparator();
            Printer.printBlankSpace();
            System.exit(1); // Se retorna código de error.
        }
    }

    // --------------------------------------------------------------------------------------------

    private static boolean isFileValid(File file) {
        return !file.exists() || !file.getName().endsWith(fileSuffix);
    }

    // --------------------------------------------------------------------------------------------

    private static void printReport() {

        Monitor monitor = Monitor.getInstance();

        String report = """
                El programa tiene %d líneas. \
                Se detectaron %d warnings y %d errores. \
                """.formatted(
                monitor.getLineNumber(),
                monitor.getNumberOfWarnings(),
                monitor.getNumberOfErrors());

        Printer.printBlankSpace();
        Printer.printSeparator();
        Printer.printCentered("> Compilación Finalizada <");
        Printer.printCentered(report);
        Printer.printSeparator();
        Printer.printBlankSpace();

        if (monitor.hasWarningMessages()) {
            Printer.printSeparator();
            Printer.printCentered("> Warnings <");
            monitor.showWarnings();
            Printer.printSeparator();
            Printer.printBlankSpace();
        }

        if (monitor.hasErrorMessages()) {
            Printer.printSeparator();
            Printer.printCentered("> Errores <");
            monitor.showErrors();
            Printer.printSeparator();
            Printer.printBlankSpace();
        }

        SymbolTable.getInstance().print();

        Printer.printBlankSpace();

        ReversePolish.getInstance().print();

        Printer.printBlankSpace();
    }
}
