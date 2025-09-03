import general.Token;
import lexicalAnalysis.DataLoader;
import lexicalAnalysis.LexicalAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        File folder = new File("../resources/testFiles");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".uki"));

        if (files == null || files.length == 0) {
            System.out.println("No se encontraron archivos .uki en " + folder.getPath());
            return;
        }

        // Se ordenan los archivos por nombre.
        Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

        for (File file : files) {
            System.out.println("=== Analizando archivo: " + file.getName() + " ===");

            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(file.getPath());

            System.out.println("Tokens identificados en el programa:");

            Token token = lexicalAnalyzer.getNextToken();
            while (token != null) {
                System.out.println(token);
                token = lexicalAnalyzer.getNextToken();
            }

            System.out.println("El programa tiene " + lexicalAnalyzer.getNroLinea() + " líneas.");
            System.out.println();
        }
    }
}
