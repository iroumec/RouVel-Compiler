import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {

    public static String[] getReservedWords() {

        List<String> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(
                "resources/reservedWords.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(linea.trim()); // trim por si hay espacios o saltos
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pasar la lista a arreglo
        String[] palabras = lista.toArray(new String[0]);

        return palabras;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(DataManager.getReservedWords()));
    }

}
