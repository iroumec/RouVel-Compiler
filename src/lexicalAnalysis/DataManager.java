package lexicalAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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

    private static final int[][] STATE_TRANSITION_MATRIX = new int[NUM_ESTADOS][NUM_SIMBOLOS];
    private static final SemanticAction[][][] SEMANTIC_ACTIONS_MATRIX = new SemanticAction[NUM_ESTADOS][NUM_SIMBOLOS][];

    // --------------------------------------------------------------------------------------------

    public static int[][] getStateTransitionMatrix() {
        return STATE_TRANSITION_MATRIX;
    }

    // --------------------------------------------------------------------------------------------

    public static SemanticAction[][][] getSemanticActionsMatrix() {
        return SEMANTIC_ACTIONS_MATRIX;
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
            throw new RuntimeException("No se pudo leer el archivo: " + sourceCodePath, e);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Carga de Matriz de Transición de Estados
    // --------------------------------------------------------------------------------------------

    static {

        // Estado 0
        Arrays.fill(STATE_TRANSITION_MATRIX[0], -1);
        STATE_TRANSITION_MATRIX[0][charToIndex('n')] = 0;
        STATE_TRANSITION_MATRIX[0][charToIndex('s')] = 0;
        STATE_TRANSITION_MATRIX[0][charToIndex('t')] = 0;
        STATE_TRANSITION_MATRIX[0][charToIndex('d')] = 1;
        STATE_TRANSITION_MATRIX[0][charToIndex('.')] = 3;
        STATE_TRANSITION_MATRIX[0][charToIndex('"')] = 8;
        STATE_TRANSITION_MATRIX[0][charToIndex('l')] = 9;
        STATE_TRANSITION_MATRIX[0][charToIndex(':')] = 10;
        STATE_TRANSITION_MATRIX[0][charToIndex('=')] = 11;
        STATE_TRANSITION_MATRIX[0][charToIndex('-')] = 12;
        STATE_TRANSITION_MATRIX[0][charToIndex('>')] = 13;
        STATE_TRANSITION_MATRIX[0][charToIndex('<')] = 14;
        STATE_TRANSITION_MATRIX[0][charToIndex('#')] = 15;
        STATE_TRANSITION_MATRIX[0][charToIndex('L')] = 18;
        STATE_TRANSITION_MATRIX[0][charToIndex('U')] = 18;
        STATE_TRANSITION_MATRIX[0][charToIndex('I')] = 18;
        STATE_TRANSITION_MATRIX[0][charToIndex('F')] = 18;
        STATE_TRANSITION_MATRIX[0][charToIndex('+')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('*')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('/')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('(')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex(')')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('{')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('}')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex('_')] = 19;
        STATE_TRANSITION_MATRIX[0][charToIndex(';')] = 19;

        // Estado 1
        Arrays.fill(STATE_TRANSITION_MATRIX[1], -2);
        STATE_TRANSITION_MATRIX[1][charToIndex('d')] = 1;
        STATE_TRANSITION_MATRIX[1][charToIndex('U')] = 2;
        STATE_TRANSITION_MATRIX[1][charToIndex('.')] = 4;

        // Estado 2
        Arrays.fill(STATE_TRANSITION_MATRIX[2], -3);
        STATE_TRANSITION_MATRIX[2][charToIndex('I')] = 19;

        // Estado 3
        Arrays.fill(STATE_TRANSITION_MATRIX[3], -4);
        STATE_TRANSITION_MATRIX[3][charToIndex('d')] = 4;

        // Estado 4
        Arrays.fill(STATE_TRANSITION_MATRIX[4], 19);
        STATE_TRANSITION_MATRIX[4][charToIndex('d')] = 4;
        STATE_TRANSITION_MATRIX[4][charToIndex('F')] = 5;

        // Estado 5
        Arrays.fill(STATE_TRANSITION_MATRIX[5], -5);
        STATE_TRANSITION_MATRIX[5][charToIndex('-')] = 6;
        STATE_TRANSITION_MATRIX[5][charToIndex('+')] = 6;

        // Estado 6
        Arrays.fill(STATE_TRANSITION_MATRIX[6], -6);
        STATE_TRANSITION_MATRIX[6][charToIndex('d')] = 7;

        // Estado 7
        Arrays.fill(STATE_TRANSITION_MATRIX[7], 19);
        STATE_TRANSITION_MATRIX[7][charToIndex('d')] = 7;

        // Estado 8
        Arrays.fill(STATE_TRANSITION_MATRIX[8], 8);
        STATE_TRANSITION_MATRIX[8][charToIndex('"')] = 19;
        STATE_TRANSITION_MATRIX[8][charToIndex('n')] = -8;

        // Estado 9
        Arrays.fill(STATE_TRANSITION_MATRIX[9], 19);
        STATE_TRANSITION_MATRIX[9][charToIndex('l')] = 9;

        // Estado 10
        Arrays.fill(STATE_TRANSITION_MATRIX[10], -7);
        STATE_TRANSITION_MATRIX[10][charToIndex('=')] = 19;

        // Estado 11
        Arrays.fill(STATE_TRANSITION_MATRIX[11], 19);

        // Estado 12
        Arrays.fill(STATE_TRANSITION_MATRIX[12], 19);

        // Estado 13
        Arrays.fill(STATE_TRANSITION_MATRIX[13], 19);

        // Estado 14
        Arrays.fill(STATE_TRANSITION_MATRIX[14], 19);

        // Estado 15
        Arrays.fill(STATE_TRANSITION_MATRIX[15], -9);
        STATE_TRANSITION_MATRIX[15][charToIndex('#')] = 16;

        // Estado 16
        Arrays.fill(STATE_TRANSITION_MATRIX[16], 16);
        STATE_TRANSITION_MATRIX[16][charToIndex('#')] = 17;

        // Estado 17
        Arrays.fill(STATE_TRANSITION_MATRIX[17], 16);
        STATE_TRANSITION_MATRIX[17][charToIndex('#')] = 0;

        // Estado 18
        Arrays.fill(STATE_TRANSITION_MATRIX[18], 19);
        STATE_TRANSITION_MATRIX[18][charToIndex('L')] = 18;
        STATE_TRANSITION_MATRIX[18][charToIndex('d')] = 18;
        STATE_TRANSITION_MATRIX[18][charToIndex('U')] = 18;
        STATE_TRANSITION_MATRIX[18][charToIndex('I')] = 18;
        STATE_TRANSITION_MATRIX[18][charToIndex('F')] = 18;
        STATE_TRANSITION_MATRIX[18][charToIndex('%')] = 18;
    }

    // --------------------------------------------------------------------------------------------
    // Carga de Matriz de Acciones Semánticas
    // --------------------------------------------------------------------------------------------

    static {

        // Se definen objetos comunes para no tener múltiples instancias iguales.
        SemanticAction[] EMPTY = new SemanticAction[0];
        SemanticAction[] AS1_AS4 = new SemanticAction[] { getSemanticAction("AS1"), getSemanticAction("AS4") };
        SemanticAction[] AS1 = new SemanticAction[] { getSemanticAction("AS1") };
        SemanticAction[] AS1_AS3 = new SemanticAction[] { getSemanticAction("AS1"), getSemanticAction("AS3") };
        SemanticAction[] AS2 = new SemanticAction[] { getSemanticAction("AS2") };
        SemanticAction[] AS2_AS3 = new SemanticAction[] { getSemanticAction("AS2"), getSemanticAction("AS3") };
        SemanticAction[] AS2_ASUI_AS3 = new SemanticAction[] { getSemanticAction("AS2"), getSemanticAction("AS3"),
                getSemanticAction("ASUI") };
        SemanticAction[] ASN = new SemanticAction[] { getSemanticAction("ASN") };
        SemanticAction[] ASF_AS3_ASR = new SemanticAction[] { getSemanticAction("ASF"),
                getSemanticAction("AS3"), getSemanticAction("ASR") };
        SemanticAction[] AS3_ASR = new SemanticAction[] { getSemanticAction("AS3"), getSemanticAction("ASR") };
        SemanticAction[] AS5_AS3_ASR = new SemanticAction[] { getSemanticAction("AS5"),
                getSemanticAction("AS3"), getSemanticAction("ASR") };

        // Estado 0
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[0], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('L')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('d')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('U')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('I')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('.')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('F')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('"')] = AS1_AS4;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex(':')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('=')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('>')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('<')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('l')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('-')] = AS1;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('n')] = ASN;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('+')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('*')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('/')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('(')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex(')')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('{')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('}')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('_')] = AS1_AS3;
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex(';')] = AS1_AS3;

        // Estado 1
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[1], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[1][charToIndex('L')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[1][charToIndex('l')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[1][charToIndex('d')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[1][charToIndex('U')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[1][charToIndex('.')] = AS2;

        // Estado 2
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[2], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[2][charToIndex('I')] = AS2_ASUI_AS3;

        // Estado 3
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[3], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[3][charToIndex('d')] = AS2;

        // Estado 4
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[4], ASF_AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[4][charToIndex('d')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[4][charToIndex('F')] = AS2;

        // Estado 5
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[5], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[5][charToIndex('-')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[5][charToIndex('+')] = AS2;

        // Estado 6
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[6], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[6][charToIndex('d')] = AS2;

        // Estado 7
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[7], ASF_AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[7][charToIndex('d')] = AS2;

        // Estado 8
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[8], AS2);
        SEMANTIC_ACTIONS_MATRIX[8][charToIndex('"')] = AS2_AS3;
        SEMANTIC_ACTIONS_MATRIX[8][charToIndex('n')] = ASN;

        // Estado 9
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[9], AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[9][charToIndex('l')] = AS2;

        // Estado 10
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[10], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[10][charToIndex('=')] = AS2_AS3;

        // Estado 11
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[11], AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[11][charToIndex('=')] = AS2_AS3;
        SEMANTIC_ACTIONS_MATRIX[11][charToIndex('!')] = AS2_AS3;

        // Estado 12
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[12], AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[12][charToIndex('>')] = AS2_AS3;

        // Estado 13
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[13], AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[13][charToIndex('=')] = AS2_AS3;

        // Estado 14
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[14], AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[14][charToIndex('=')] = AS2_AS3;

        // Estado 15.
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[15], EMPTY);

        // Estado 16
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[16], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[16][charToIndex('n')] = ASN;

        // Estado 17
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[17], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[17][charToIndex('n')] = ASN;

        // Estado 18
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[18], AS5_AS3_ASR);
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('L')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('d')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('U')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('I')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('F')] = AS2;
        SEMANTIC_ACTIONS_MATRIX[18][charToIndex('%')] = AS2;
    }

    // --------------------------------------------------------------------------------------------
    // Mapeos
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
}
