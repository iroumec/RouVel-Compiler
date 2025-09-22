
import java.io.File;
import java.io.IOException;

import lexer.Lexer;
import parser.Parser;

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

        System.out.println("\n=== Resultados de la Compilación del Archivo: " + file.getName() + " ===\n");

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        sintacticalAnalyzer.execute();

        System.out.println("\nEl programa tiene " + lexicalAnalyzer.getNroLinea() + " líneas.");
        System.out.println("Se detectaron " + lexicalAnalyzer.getErrorsDetected() + " errores y "
                + lexicalAnalyzer.getWarningsDetected() + " warnings.");
        System.out.println();
    }
}
