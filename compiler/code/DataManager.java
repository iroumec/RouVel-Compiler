import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class DataManager {

    private static String sourceCodePath;

    public static Set<String> getReservedWords() {

        Set<String> palabras = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(
                "resources/reservedWords.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                palabras.add(linea.trim()); // trim por si hay espacios o saltos
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return palabras;
    }

    public static String getSourceCode() {
        try {
            return Files.readString(Paths.get(sourceCodePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setsourceCodePath(String path) {
        sourceCodePath = path;
    }

    public static void main(String[] args) {
        System.out.println(DataManager.getReservedWords());
    }

}
