
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

        // Redirección del error estándar a la salida estándar.
        // Esto solo tiene el objetivo de que la salida se vea bien.
        // TODO: implementar un sistema de logging más robusto.
        System.setErr(System.out);

        System.out.println("\n=== Resultados de la Compilación del Archivo: " + file.getName() + " ===\n");

        Lexer lexicalAnalyzer = new Lexer(file.getPath());

        Parser sintacticalAnalyzer = new Parser(lexicalAnalyzer);

        System.err.println("------------------------------------");
        sintacticalAnalyzer.execute();
        System.err.println("------------------------------------");

        System.out.println("\nEl programa tiene " + lexicalAnalyzer.getNroLinea() + " líneas.");

        int warningsDetected = lexicalAnalyzer.getWarningsDetected() + sintacticalAnalyzer.getErrorsDetected();
        int errorsDetected = lexicalAnalyzer.getErrorsDetected() + sintacticalAnalyzer.getErrorsDetected();

        System.out.printf("\nSe detectaron %d warnings y %d errores.\n", warningsDetected, errorsDetected);
    }
}
