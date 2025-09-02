package lexicalAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lexicalAnalysis.semanticActions.*;

public final class DataLoader {

    private static String sourceCodePath;
    private final static String stateTransitionMatrixPath = "../resources/data/stateTransitionMatrix.csv";
    private final static String semanticActionsMatrixPath = "../resources/data/semanticActionsMatrix.csv";

    // --------------------------------------------------------------------------------------------

    private static final Map<String, SemanticAction> semanticActions = new HashMap<>();
    static {
        semanticActions.put("AS1", new LexemaInitializer());
        semanticActions.put("AS2", new LexemaAppender());
        semanticActions.put("AS3", new LexemaFinalizer());
        semanticActions.put("AS4", new LexemaIdentifier());
        semanticActions.put("AS5", new IdentifierLengthChecker());
        semanticActions.put("ASUI", new UintChecker());
        semanticActions.put("ASF", new FloatChecker());
        semanticActions.put("ASR", new ReturnCharacterToEntry());
        semanticActions.put("ASN", new NewLineDetected());
    }

    // --------------------------------------------------------------------------------------------

    public static int[][] loadStateTransitionMatrix() {

        List<int[]> lista = null;
        String separator = ",";

        try (Stream<String> stream = Files.lines(Paths.get(stateTransitionMatrixPath))) {
            lista = stream.skip(1) // Se saltea el encabezado.
                    // -1 para devolver todos los campos, incluso los vacíos.
                    .map(line -> Arrays.stream(line.split(separator, -1))
                            .skip(1)
                            .mapToInt(s -> {
                                try {
                                    return Integer.parseInt(s.trim());
                                } catch (NumberFormatException e) {
                                    return -1;
                                }
                            })
                            .toArray())
                    .collect(Collectors.toList());
        } catch (IOException e1) {
            System.err.println("Ha ocurrido un error relacionado a la lectura del archivo de las matrices.");
            System.exit(1);
        }

        return lista.toArray(new int[0][]);
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][][] loadSemanticActionMatrix() {
        try (Stream<String> lines = Files.lines(Paths.get(semanticActionsMatrixPath))) {
            List<SemanticAction[][]> listaFilas = lines
                    .skip(1) // Se saltea el encabezado (primera línea).
                    .map(line -> {
                        String[] tokens = line.split(",", -1); // Mantener vacíos
                        SemanticAction[][] fila = new SemanticAction[tokens.length - 1][]; // Ignorar col. row

                        for (int i = 1; i < tokens.length; i++) {
                            String valor = tokens[i].trim();

                            if (valor.isEmpty()) {
                                fila[i - 1] = new SemanticAction[0]; // Sin acciones
                            } else {
                                String[] acciones = valor.replace("\"", "").split("-");
                                List<SemanticAction> validActions = new ArrayList<>();

                                for (String act : acciones) {
                                    SemanticAction sa = semanticActions.get(act.trim());
                                    if (sa != null)
                                        validActions.add(sa);
                                }

                                fila[i - 1] = validActions.toArray(new SemanticAction[0]);
                            }
                        }

                        return fila;
                    })
                    .toList();

            return listaFilas.toArray(new SemanticAction[0][][]);

        } catch (IOException e) {
            e.printStackTrace();
            return new SemanticAction[0][][]; // Se devuelve vacío en caso de error.
        }
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
