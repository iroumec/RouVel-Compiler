
import java.io.File;
import java.io.IOException;

import lexer.Lexer;
import parser.Parser;
import common.SymbolTable;
import utilities.Printer;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Se debe indicar un archivo .uki como argumento.");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.getName().endsWith(".uki")) {
            System.out.println("El archivo no existe o no es .uki: " + args[0]);
            return;
        }

        Printer.printIntroduction(file.getName());

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        Printer.printSeparation();
        sintacticalAnalyzer.execute();
        Printer.printSeparation();

        String reporte = """
                    El programa tiene %d líneas. \
                Se detectaron %d warnings y %d errores. \
                """.formatted(
                lexicalAnalyzer.getNroLinea(),
                lexicalAnalyzer.getWarningsDetected() + sintacticalAnalyzer.getWarningsDetected(),
                lexicalAnalyzer.getErrorsDetected() + sintacticalAnalyzer.getErrorsDetected());
        Printer.printBlankSpace();
        Printer.printBigSeparator();
        Printer.print(reporte);
        Printer.printBigSeparator();
        Printer.printBlankSpace();

        SymbolTable.getInstance().imprimirTabla();
    }
}
