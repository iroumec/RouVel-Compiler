
import java.io.File;
import java.io.IOException;

import lexer.Lexer;
import parser.Parser;
import common.Monitor;
import utilities.Printer;
import utilities.ResultsExporter;
import common.SymbolTable;
import assembler.Assembler;
import semantic.ReversePolish;
import assembler.WebAssemblyExporter;

public class Main {

    private static final String fileSuffix = ".uki";
    private static final boolean printAssemblerCode = false;

    // ============================================================================================

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {

            Printer.printBlankSpace();
            Printer.printSeparator();
            Printer.printCentered("ERROR");
            Printer.printSeparator();
            Printer.printFramed("Se debe indicar un archivo .uki como argumento.");
            Printer.printSeparator();
            System.exit(1);
            return;
        }

        File file = new File(args[0]);
        if (isFileValid(file)) {

            Printer.printBlankSpace();
            Printer.printSeparator();
            Printer.printCentered("ERROR");
            Printer.printSeparator();
            Printer.printFramed("El archivo no existe o no tiene la extensión '.uki': " + args[0]);
            Printer.printSeparator();
            System.exit(1);
        }

        startCompilation(file);
    }

    // ============================================================================================

    private static void startCompilation(File file) {

        Monitor monitor = Monitor.getInstance();

        Printer.printIntroduction(file.getName());

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        if (lexicalAnalyzer.isPrintOn() || sintacticalAnalyzer.isPrintOn()) {
            Printer.printBlankSpace();
            Printer.printSeparator();
        }

        sintacticalAnalyzer.execute();

        if (lexicalAnalyzer.isPrintOn() || sintacticalAnalyzer.isPrintOn()) {
            Printer.printSeparator();
        }

        printReport();

        if (!monitor.hasErrorMessages()) {

            ReversePolish.getInstance().print();

            Printer.printBlankSpace();

            String assemblerCode = Assembler.generate(sintacticalAnalyzer.getReversePolish());

            SymbolTable.getInstance().print();

            Printer.printBlankSpace();

            if (printAssemblerCode) {
                Printer.printSeparator();
                Printer.printCentered("Código WebAssembly");
                Printer.printSeparator();

                Printer.printFramed(assemblerCode);
                Printer.printSeparator();
                Printer.printBlankSpace();
            }

            if (monitor.hasCompilationWarnings()) {
                Printer.printSeparator();
                Printer.printCentered("> Advertencias de Compilación <");
                monitor.showCompilationWarnings();
                Printer.printSeparator();
                Printer.printBlankSpace();
            }

            Printer.printSeparator();
            WebAssemblyExporter.exportToWasm(file, assemblerCode);
            Printer.printSeparator();
            Printer.printBlankSpace();

        } else {
            Printer.printSeparator();
            Printer.printFramed("El código contiene errores, por lo que no fue posible generar un código assembler.");
            Printer.printSeparator();
            Printer.printBlankSpace();
            ResultsExporter.exportResults(file);
            System.exit(1); // Se retorna código de error.
        }

        ResultsExporter.exportResults(file);
    }

    // ============================================================================================

    private static boolean isFileValid(File file) {
        return !file.exists() || !file.getName().endsWith(fileSuffix);
    }

    // ============================================================================================

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
    }
}
