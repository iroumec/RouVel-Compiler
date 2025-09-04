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
            case "AS1" -> LexemaInitializer.getInstance();
            case "AS2" -> LexemaAppender.getInstance();
            case "AS3" -> LexemaFinalizer.getInstance();
            case "AS4" -> LexemaIdentifier.getInstance();
            case "AS5" -> IdentifierLengthChecker.getInstance();
            case "ASUI" -> UintChecker.getInstance();
            case "ASF" -> FloatChecker.getInstance();
            case "ASR" -> ReturnCharacterToEntry.getInstance();
            case "ASN" -> NewLineDetected.getInstance();
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

        // Definición de la matriz en formato similar al CSV
        String[][] MATRIX_DATA = {
                { "0", "-1",
                        "[+,*,/,(,),{,},_,;:19 | L,U,I,F:18 | l:9 | d:1 | .:3 | -:12 | \":8 | ::10 | =:11 | >:13 | <:14 | #:15 | n,s,t:0]" },
                { "1", "-2", "[d:1 | U:2 | .:4]" },
                { "2", "-3", "[I:19]" },
                { "3", "-4", "[d:4]" },
                { "4", "19", "[d:4 | F:5]" },
                { "5", "-5", "[-,+:6]" },
                { "6", "-6", "[d:7]" },
                { "7", "19", "[d:7]" },
                { "8", "8", "[\":19 | n:-8]" },
                { "9", "19", "[l:9]" },
                { "10", "-7", "[=:19]" },
                { "11", "19", "[]" },
                { "12", "19", "[]" },
                { "13", "19", "[]" },
                { "14", "19", "[]" },
                { "15", "-9", "[#:16]" },
                { "16", "16", "[#:17]" },
                { "17", "16", "[#:0]" },
                { "18", "19", "[L,d,U,I,F,%:18]" }
        };

        for (String[] parts : MATRIX_DATA) {
            if (parts.length < 2)
                continue;

            int state = Integer.parseInt(parts[0].trim());
            int defaultValue = Integer.parseInt(parts[1].trim());

            // Inicializa toda la fila con el valor por defecto
            Arrays.fill(matrix[state], defaultValue);

            if (parts.length == 3) {
                String exceptions = parts[2].trim();
                if (exceptions.startsWith("[") && exceptions.endsWith("]")) {
                    exceptions = exceptions.substring(1, exceptions.length() - 1); // quitar corchetes
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
                        String valuePart = group.substring(colonPos + 1).trim();

                        int value;
                        try {
                            value = valuePart.isEmpty() ? -1 : Integer.parseInt(valuePart);
                        } catch (NumberFormatException e) {
                            value = -1;
                        }

                        for (String sym : symbolsPart.split(",")) {
                            sym = sym.trim();
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
        }

        return matrix;
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][][] loadSemanticActionsMatrix() {
        SemanticAction[][][] matrix = new SemanticAction[NUM_ESTADOS][NUM_SIMBOLOS][];

        // Matriz hardcodeada en formato CSV-like
        String[][] MATRIX_DATA = {
                { "0", "", "[L,d,U,I,.,F,\",:\"AS1-AS4\" | :,=,>,<,l,-:AS1 | n:ASN | +,*,/,(,),{,},_,;:\"AS1-AS3\"]" },
                { "1", "", "[L,l,d,U,.:AS2]" },
                { "2", "", "[I:\"AS2-ASUI-AS3\"]" },
                { "3", "", "[d:AS2]" },
                { "4", "\"ASF-AS3-ASR\"", "[d:AS2 | F:AS2]" },
                { "5", "", "[-,+:AS2]" },
                { "6", "", "[d:AS2]" },
                { "7", "\"ASF-AS3-ASR\"", "[d:AS2]" },
                { "8", "AS2", "[\":AS2-AS3 | n:ASN]" },
                { "9", "\"AS3-ASR\"", "[l:AS2]" },
                { "10", "", "[=:\"AS2-AS3\"]" },
                { "11", "\"AS3-ASR\"", "[=,!:\"AS2-AS3\"]" },
                { "12", "\"AS3-ASR\"", "[>:\"AS2-AS3\"]" },
                { "13", "\"AS3-ASR\"", "[=:\"AS2-AS3\"]" },
                { "14", "\"AS3-ASR\"", "[=:\"AS2-AS3\"]" },
                { "15", "", "" },
                { "16", "", "[n:ASN]" },
                { "17", "", "[n:ASN]" },
                { "18", "\"AS5-AS3-ASR\"", "[L,d,U,I,F,%:AS2]" }
        };

        for (String[] parts : MATRIX_DATA) {
            if (parts.length < 1)
                continue;

            int state = Integer.parseInt(parts[0].trim());

            // Default
            List<SemanticAction> defaultActions = new ArrayList<>();
            if (parts.length > 1 && parts[1] != null && !parts[1].isBlank()) {
                String def = parts[1].trim().replace("\"", "");
                for (String act : def.split("-")) {
                    SemanticAction sa = getSemanticAction(act.trim());
                    if (sa != null)
                        defaultActions.add(sa);
                }
            }

            for (int i = 0; i < NUM_SIMBOLOS; i++) {
                matrix[state][i] = defaultActions.toArray(new SemanticAction[0]);
            }

            // Exceptions
            if (parts.length == 3 && parts[2] != null) {
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
