package assembler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import utilities.Printer;

public class WebAssemblyExporter {

    public static void exportToWat(File file, String code) {

        Path path = Paths.get(extractBaseName(file) + ".wat");

        try {
            Files.writeString(path, code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Printer.printCentered("Archivo `.wat` generado: " + path.toString());
        } catch (IOException i) {
            Printer.printCentered("ERROR: Ocurrió un problema al exportar el código a .wat.");
        }
    }

    private static String extractBaseName(File file) {

        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        return baseName;
    }
}
