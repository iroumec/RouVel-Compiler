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
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lexicalAnalysis.semanticActions.*;

public final class DataLoader {

    private final static String stateTransitionMatrixPath = "../resources/data/stateTransitionMatrix.csv";
    private final static String semanticActionsMatrixPath = "../resources/data/semanticActionsMatrix.csv";

    private final static int NUM_ESTADOS = 19;
    private final static int NUM_SIMBOLOS = 29;

    // --------------------------------------------------------------------------------------------

    /**
     * Se mapean los nombres de las acciones semánticas a sus instancias.
     * 
     * @param semanticAction Nombre de la acción semántica.
     * @return Instancia de la acción semántica.
     */
    private static SemanticAction getSemanticAction(String semanticAction) {

        return switch (semanticAction) {
            case "AS1" -> new LexemaInitializer();
            case "AS2" -> new LexemaAppender();
            case "AS3" -> new LexemaFinalizer();
            case "AS4" -> new LexemaIdentifier();
            case "AS5" -> new IdentifierLengthChecker();
            case "ASUI" -> new UintChecker();
            case "ASF" -> new FloatChecker();
            case "ASR" -> new ReturnCharacterToEntry();
            case "ASN" -> new NewLineDetected();
            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se mapean los caracteres a índices de columna en las matrices.
     * 
     * @param character Carácter a mapear.
     * @return Índice de la columna correspondiente.
     */
    public static int charToIndex(char character) {
        return switch (character) {
            case 'L' -> 0;
            case 'l' -> 1;
            case 'd' -> 2;
            case 'U' -> 3;
            case 'I' -> 4;
            case '.' -> 5;
            case 'F' -> 6;
            case '-' -> 7;
            case '+' -> 8;
            case '"' -> 9;
            case '*' -> 10;
            case '/' -> 11;
            case '(' -> 12;
            case ')' -> 13;
            case '{' -> 14;
            case '}' -> 15;
            case '_' -> 16;
            case ';' -> 17;
            case ':' -> 18;
            case '=' -> 19;
            case '!' -> 20;
            case '>' -> 21;
            case '<' -> 22;
            case '%' -> 23;
            case '#' -> 24;
            case 'n' -> 25;
            case 's' -> 26;
            case 't' -> 27;
            default -> 28;
        };
    }

    // --------------------------------------------------------------------------------------------

    public static int[][] loadStateTransitionMatrix() {

        int[][] matrix = new int[NUM_ESTADOS][NUM_SIMBOLOS];
        String separator = ",";

        try (Stream<String> stream = Files.lines(Paths.get(stateTransitionMatrixPath))) {
            stream.skip(1) // Se saltea el encabezado (primera línea).
                    .forEach(line -> {
                        // Se sapara la línea en partes: estado, valor por defecto, excepciones
                        String[] parts = line.split(separator, 3);
                        if (parts.length < 2)
                            return; // Línea vacía o inválida.

                        int state = Integer.parseInt(parts[0].trim());
                        int defaultValue = Integer.parseInt(parts[1].trim());

                        // Se inicializa toda la fila con el valor por defecto.
                        Arrays.fill(matrix[state], defaultValue);

                        if (parts.length == 3) {
                            String exceptions = parts[2].trim();
                            if (exceptions.startsWith("[") && exceptions.endsWith("]")) {
                                exceptions = exceptions.substring(1, exceptions.length() - 1); // Se quitan las llaves.
                            }

                            // Si hay excepciones...
                            if (!exceptions.isBlank()) {
                                // Se divide por '|' para separar cada grupo.
                                for (String group : exceptions.split("\\|")) {
                                    group = group.trim();
                                    if (group.isEmpty())
                                        continue;

                                    // Se busca la última aparición de ':' para separar símbolos y valor.
                                    // Se busca la última aparición ya que ':' es un símbolo válido y se pueden
                                    // tener cosas como "::8".
                                    int colonPos = group.lastIndexOf(':');
                                    if (colonPos == -1)
                                        continue; // Inválido.

                                    String symbolsPart = group.substring(0, colonPos).trim();
                                    String valuePart = group.substring(colonPos + 1).trim();

                                    int value;
                                    try {
                                        value = valuePart.isEmpty() ? -1 : Integer.parseInt(valuePart);
                                    } catch (NumberFormatException e) {
                                        value = -1;
                                    }

                                    // Obtención de los símbolos separados por coma.
                                    for (String sym : symbolsPart.split(",")) {
                                        sym = sym.trim(); // Eliminación de espacios..
                                        if (sym.isEmpty())
                                            continue;

                                        Integer idx = charToIndex(sym.charAt(0));
                                        if (idx != null && idx >= 0 && idx < NUM_SIMBOLOS) {
                                            matrix[state][idx] = value;
                                        }
                                    }
                                }
                            }
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de la matriz: " + e.getMessage());
            System.exit(1);
        }

        return matrix;
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][][] loadSemanticActionsMatrix() {
        SemanticAction[][][] matrix = new SemanticAction[NUM_ESTADOS][NUM_SIMBOLOS][];
        String separator = ",";

        try (Stream<String> lines = Files.lines(Paths.get(semanticActionsMatrixPath))) {
            lines.skip(1).forEach(line -> {
                // Se separa la línea en partes: estado, valor por defecto, excepciones
                String[] parts = line.split(separator, 3);
                if (parts.length < 1)
                    return; // La línea no tiene tres columnas.

                int state = Integer.parseInt(parts[0].trim());

                // Default.
                List<SemanticAction> defaultActions = new ArrayList<>();
                if (parts.length > 1 && !parts[1].isBlank()) {
                    String def = parts[1].trim().replace("\"", "");
                    for (String act : def.split("-")) {
                        SemanticAction sa = getSemanticAction(act.trim());
                        if (sa != null)
                            defaultActions.add(sa);
                    }
                }

                // Se inicializa toda la fila con el default.
                for (int i = 0; i < NUM_SIMBOLOS; i++) {
                    matrix[state][i] = defaultActions.toArray(new SemanticAction[0]);
                }

                // Manejo de la columna de las excepciones.
                if (parts.length == 3) {
                    String exceptions = parts[2].trim();
                    if (exceptions.startsWith("[") && exceptions.endsWith("]")) {
                        exceptions = exceptions.substring(1, exceptions.length() - 1);
                    }

                    if (!exceptions.isBlank()) {
                        for (String group : exceptions.split("\\|")) {
                            group = group.trim();
                            if (group.isEmpty())
                                continue;

                            int colonPos = group.lastIndexOf(':');
                            if (colonPos == -1)
                                continue;

                            String symbolsPart = group.substring(0, colonPos).trim();
                            String actionsPart = group.substring(colonPos + 1).trim().replace("\"", "");

                            List<SemanticAction> actions = new ArrayList<>();
                            for (String act : actionsPart.split("-")) {
                                SemanticAction sa = getSemanticAction(act.trim());
                                if (sa != null)
                                    actions.add(sa);
                            }

                            SemanticAction[] arr = actions.toArray(new SemanticAction[0]);

                            for (String sym : symbolsPart.split(",")) {
                                sym = sym.trim();
                                if (sym.isEmpty())
                                    continue;

                                Integer idx = charToIndex(sym.charAt(0));
                                if (idx != null && idx >= 0 && idx < NUM_SIMBOLOS) {
                                    matrix[state][idx] = arr;
                                }
                            }
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new SemanticAction[0][0][];
        }

        return matrix;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se lee el código fuente y se lo convierte a String.
     * 
     * @param sourceCodePath Ruta al archivo del código fuente.
     * @return Código fuente en formato String.
     */
    public static String loadSourceCode(String sourceCodePath) {

        try {
            return Files.readString(Paths.get(sourceCodePath));
        } catch (Exception e) {
            System.err.println("Debe especificar una ruta a un código fuente.");
            System.exit(1);
        }

        return null;
    }

}
