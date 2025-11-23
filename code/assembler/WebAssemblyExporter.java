package assembler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import utilities.Printer;

public class WebAssemblyExporter {

    private static final String watPath = "outputs/wat/";
    private static final String wasmPath = "outputs/wasm/";

    // ============================================================================================

    public static void exportToWat(File file, String code) {

        createDirectoryIfNotExists(watPath);

        Path path = Paths.get(watPath + extractBaseName(file) + ".wat");

        try {
            Files.writeString(path, code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Printer.printCentered("Archivo `.wat` generado: " + path.toString());
            Printer.printSeparator();
        } catch (IOException i) {
            Printer.printCentered("ERROR: Ocurrió un problema al exportar el código a .wat.");
        }
    }

    // ============================================================================================

    public static void exportToWasm(File file, String code) {

        exportToWat(file, code);

        createDirectoryIfNotExists(wasmPath);

        Path inputPath = Paths.get(watPath + extractBaseName(file) + ".wat");
        Path outputPath = Paths.get(wasmPath + extractBaseName(file) + ".wasm");

        // sudo apt-get wabt in Debian.
        ProcessBuilder processBuilder = new ProcessBuilder("wat2wasm", inputPath.toString(), 
                "--enable-exceptions",
                "-o", 
                outputPath.toString());
                

        try {
            Process process = processBuilder.start();

            // Leer stderr
            String error = new String(process.getErrorStream().readAllBytes());
            // String output = new String(process.getInputStream().readAllBytes());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                Printer.printBlankSpace();
                Printer.printSeparator();
                Printer.printCentered("wat2wasm falló.");
                Printer.printSeparator();
                Printer.printCentered("Salida de error:");
                Printer.printFramed(error.strip());
                Printer.printSeparator();
                System.exit(1);
            }

            Printer.printCentered("Archivo `.wasm` generado: " + outputPath.toString());
        } catch (IOException | InterruptedException _) {
            Printer.printBlankSpace();
            Printer.printSeparator();
            Printer.printCentered("ERROR: Ocurrió un problema al exportar el código a .wasm.");
            Printer.printSeparator();
            Printer.printCentered("Asegúrese de tener la herramienta wat2wasm instalada.");
            Printer.printSeparator();
            Printer.printFramed("Ubuntu/Debian: sudo apt-get install wabt");
            Printer.printSeparator();
            System.exit(1);
        }
    }

    // ============================================================================================

    private static void createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            Printer.printCentered("ERROR: No se pudo crear el directorio: " + directoryPath);
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

    public static int getAppropiateMessageLength(String message) {

        int messageLength = 0;
        try {
            messageLength = message.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return messageLength;
    }
}
