package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResultsExporter {

    // ============================================================================================

    private static final List<String> compilationResults = new ArrayList<>();

    // ============================================================================================

    static void addResult(String result) {

        compilationResults.add(result);
    }

    // ============================================================================================

    public static void exportResults(File file) {

        String programName = extractBaseName(file);

        Path path = Paths.get("outputs/results/", programName + ".txt");

        try {

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Files.write(path, compilationResults, StandardCharsets.UTF_8);

            Printer.printSeparator();
            Printer.printCentered("Resultados de la compilación guardados en: " + path.toString());
            Printer.printSeparator();
            Printer.printBlankSpace();

        } catch (IOException e) {
            Printer.printSeparator();
            Printer.printCentered("Error al exportar los resultados: " + e.getMessage());
            Printer.printSeparator();
            Printer.printBlankSpace();
        }
    }

    // ============================================================================================

    private static String extractBaseName(File file) {

        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        return baseName;
    }

    // ============================================================================================
}
