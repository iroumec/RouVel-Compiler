import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DataLoader {

    private static String sourceCodePath;
    private final static String reservedWordsPath = "../resources/reservedWords.txt";
    private final static String stateTransitionMatrixPath = "../resources/stateTransitionMatrix.csv";
    private final static String semanticActionsMatrixPath = "../resources/semanticActionsMatrix.csv";

    // --------------------------------------------------------------------------------------------

    private static final Map<String, SemanticAction> semanticActions = new HashMap<>();
    static {
        semanticActions.put("ASN", new NewLineDetected());
    }

    // --------------------------------------------------------------------------------------------

    public static Set<String> loadReservedWords() {

        Set<String> palabras = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(
                reservedWordsPath))) {
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

    // --------------------------------------------------------------------------------------------

    public static int[][] loadStateTransitionMatrix() {

        String line;
        String separator = ",";
        ArrayList<int[]> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(stateTransitionMatrixPath))) {
            // Saltar encabezado
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(separator, -1); // -1 para mantener vacíos finales
                int[] fila = new int[tokens.length - 1]; // ignoramos la columna "row"

                for (int i = 1; i < tokens.length; i++) { // empezamos desde 1
                    String valor = tokens[i].trim();
                    if (valor.isEmpty()) {
                        fila[i - 1] = -1;
                    } else {
                        try {
                            fila[i - 1] = Integer.parseInt(valor);
                        } catch (NumberFormatException e) {
                            fila[i - 1] = -1; // cualquier otro carácter se vuelve -1
                        }
                    }
                }
                lista.add(fila);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Conversión a matriz y retorno.
        return lista.toArray(new int[0][]);
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][] loadSemanticActionMatrix() {
        String line;
        String separator = ",";
        ArrayList<SemanticAction[]> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(semanticActionsMatrixPath))) {
            br.readLine(); // saltar encabezado

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(separator, -1); // -1 para mantener vacíos
                SemanticAction[] fila = new SemanticAction[tokens.length - 1]; // Se ignora la columna row.

                for (int i = 1; i < tokens.length; i++) {
                    String valor = tokens[i].trim();
                    if (valor.isEmpty()) {
                        fila[i - 1] = null; // Sin acción semántica.
                    } else {
                        fila[i - 1] = semanticActions.get(valor);
                    }
                }
                lista.add(fila);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista.toArray(new SemanticAction[0][]);
    }

    // --------------------------------------------------------------------------------------------

    public static String loadSourceCode() {
        try {
            return Files.readString(Paths.get(sourceCodePath));
        } catch (Exception e) {
            System.err.println("Debe especificar una ruta a un código fuente.");
            System.exit(1);
        }

        return null;
    }

    // --------------------------------------------------------------------------------------------

    public static void setSourceCode(String path) {
        sourceCodePath = path;
    }

}
