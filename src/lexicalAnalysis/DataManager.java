package lexicalAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lexicalAnalysis.semanticActions.*;

public final class DataManager {

    private final static int NUM_ESTADOS = 19;
    private final static int NUM_SIMBOLOS = 29;

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
            throw new RuntimeException("No se pudo leer el archivo: " + sourceCodePath, e);
        }
    }

    // --------------------------------------------------------------------------------------------

    public static int[][] getStateTransitionMatrix() {

        return StateTransitionMatrix.getStateTransitionMatrix();
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][][] getSemanticActionsMatrix() {

        return SemanticActionMatrix.getSemanticActionMatrix();
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
    // INNER CLASS: STATE TRANSITION MATRIX
    // --------------------------------------------------------------------------------------------

    private class StateTransitionMatrix {

        private static int[][] MATRIX = null;

        // --------------------------------------------------------------------------------------------

        public static int[][] getStateTransitionMatrix() {
            if (MATRIX == null) {
                MATRIX = new int[NUM_ESTADOS][NUM_SIMBOLOS];
                loadStateTransitionMatrix();
            }
            return MATRIX;
        }

        // --------------------------------------------------------------------------------------------

        private static void load(int state, int def, String... ex) {
            Arrays.fill(MATRIX[state], def);
            for (String e : ex) {
                String[] p = e.split("\\|");
                String syms = p[0];
                int val = Integer.parseInt(p[1]);
                for (String s : syms.split(",")) {
                    Integer idx = charToIndex(s.trim().charAt(0));
                    if (idx != null)
                        MATRIX[state][idx] = val;
                }
            }
        }

        // --------------------------------------------------------------------------------------------

        private static void loadStateTransitionMatrix() {
            load(0, -1, "+,*,/,(),{},_,;|19", "L,U,I,F|18", "l|9", "d|1", ".|3", "-|12", "\"|8", ":|10", "=|11", ">|13",
                    "<|14", "#|15", "n,s,t|0");
            load(1, -2, "d|1", "U|2", ".|4");
            load(2, -3, "I|19");
            load(3, -4, "d|4");
            load(4, 19, "d|4", "F|5");
            load(5, -5, "-,+|6");
            load(6, -6, "d|7");
            load(7, 19, "d|7");
            load(8, 8, "\"|19", "n|-8");
            load(9, 19, "l|9");
            load(10, -7, "=|19");
            load(11, 19);
            load(12, 19);
            load(13, 19);
            load(14, 19);
            load(15, -9, "#|16");
            load(16, 16, "#|17");
            load(17, 16, "#|0");
            load(18, 19, "L,d,U,I,F,%|18");
        }
    }

    // --------------------------------------------------------------------------------------------
    // INNER CLASS: SEMANTIC ACTION MATRIX
    // --------------------------------------------------------------------------------------------

    private class SemanticActionMatrix {

        private static SemanticAction[][][] MATRIX = null;

        // --------------------------------------------------------------------------------------------

        public static SemanticAction[][][] getSemanticActionMatrix() {
            if (MATRIX == null) {
                MATRIX = new SemanticAction[NUM_ESTADOS][NUM_SIMBOLOS][];
                loadSemanticActionsMatrix();
            }
            return MATRIX;
        }

        // --------------------------------------------------------------------------------------------

        private static void load(int state, String def, String... ex) {
            SemanticAction[] defArr = parseActions(def);
            for (int i = 0; i < NUM_SIMBOLOS; i++) {
                MATRIX[state][i] = defArr;
            }
            for (String e : ex) {
                String[] p = e.split("\\|");
                String syms = p[0];
                SemanticAction[] acts = parseActions(p[1]);
                for (String s : syms.split(",")) {
                    Integer idx = charToIndex(s.trim().charAt(0));
                    if (idx != null)
                        MATRIX[state][idx] = acts;
                }
            }
        }

        // --------------------------------------------------------------------------------------------

        private static SemanticAction[] parseActions(String s) {
            if (s == null || s.isBlank())
                return new SemanticAction[0];
            return Arrays.stream(s.split("-"))
                    .map(String::trim)
                    .filter(x -> !x.isEmpty())
                    .map(SemanticActionMatrix::getSemanticAction)
                    .filter(Objects::nonNull)
                    .toArray(SemanticAction[]::new);
        }

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

        private static void loadSemanticActionsMatrix() {
            load(0, "", "L,d,U,I,.,F,\"|AS1-AS4", ":,=,>,<,l,-|AS1", "n|ASN", "+,*,/,(,),{,},_,;|AS1-AS3");
            load(1, "", "L,l,d,U,.|AS2");
            load(2, "", "I|AS2-ASUI-AS3");
            load(3, "", "d|AS2");
            load(4, "ASF-AS3-ASR", "d|AS2", "F|AS2");
            load(5, "", "-,+|AS2");
            load(6, "", "d|AS2");
            load(7, "ASF-AS3-ASR", "d|AS2");
            load(8, "AS2", "\"|AS2-AS3", "n|ASN");
            load(9, "AS3-ASR", "l|AS2");
            load(10, "", "=|AS2-AS3");
            load(11, "AS3-ASR", "=,!|AS2-AS3");
            load(12, "AS3-ASR", ">|AS2-AS3");
            load(13, "AS3-ASR", "=|AS2-AS3");
            load(14, "AS3-ASR", "=|AS2-AS3");
            load(15, "");
            load(16, "", "n|ASN");
            load(17, "", "n|ASN");
            load(18, "AS5-AS3-ASR", "L,d,U,I,F,%|AS2");
        }
    }
}
