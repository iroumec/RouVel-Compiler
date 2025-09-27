package lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import lexer.actions.SemanticAction;
import lexer.actions.implementations.FixedTokenFinalizer;
import lexer.actions.implementations.FloatChecker;
import lexer.actions.implementations.IdentifierLengthChecker;
import lexer.actions.implementations.LexemaAppender;
import lexer.actions.implementations.LexemaInitializer;
import lexer.actions.implementations.NewLineDetected;
import lexer.actions.implementations.ReturnCharacterToEntry;
import lexer.actions.implementations.UintChecker;
import lexer.actions.implementations.VariableTokenFinalizer;

public final class DataManager {

    private final static int NUM_ESTADOS = 18;
    private final static int NUM_SIMBOLOS = 21;
    private final static int ESTADO_INICIO = 0;
    private final static int ESTADO_ACEPTACION = 18;

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
     * @return Código fuente en formato {@code String}.
     */
    public static String loadSourceCode(String sourceCodePath) {

        try {
            return Files.readString(Paths.get(sourceCodePath));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo: " + sourceCodePath, e);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Carga de Matriz de Transición de Estados
    // --------------------------------------------------------------------------------------------

    static {

        // Estado 0
        Arrays.fill(STATE_TRANSITION_MATRIX[0], -1);
        for (char c : new char[] { 'n', 's', 't' }) {
            STATE_TRANSITION_MATRIX[0][charToIndex(c)] = 0;
        }
        STATE_TRANSITION_MATRIX[0][charToIndex('d')] = 1;
        STATE_TRANSITION_MATRIX[0][charToIndex('.')] = 3;
        STATE_TRANSITION_MATRIX[0][charToIndex('"')] = 8;
        STATE_TRANSITION_MATRIX[0][charToIndex('l')] = 9;
        STATE_TRANSITION_MATRIX[0][charToIndex(':')] = 11;
        STATE_TRANSITION_MATRIX[0][charToIndex('=')] = 12;
        STATE_TRANSITION_MATRIX[0][charToIndex('-')] = 13;
        STATE_TRANSITION_MATRIX[0][charToIndex('>')] = 14;
        STATE_TRANSITION_MATRIX[0][charToIndex('<')] = 14;
        STATE_TRANSITION_MATRIX[0][charToIndex('#')] = 15;
        for (char c : new char[] { 'L', 'U', 'I', 'F' }) {
            STATE_TRANSITION_MATRIX[0][charToIndex(c)] = 10;
        }
        for (char c : new char[] { '+', '*', '/', '(', ')', '{', '}', '_', ';', ',' }) {
            STATE_TRANSITION_MATRIX[0][charToIndex(c)] = ESTADO_ACEPTACION;
        }

        // Estado 1
        Arrays.fill(STATE_TRANSITION_MATRIX[1], -2);
        STATE_TRANSITION_MATRIX[1][charToIndex('d')] = 1;
        STATE_TRANSITION_MATRIX[1][charToIndex('U')] = 2;
        STATE_TRANSITION_MATRIX[1][charToIndex('.')] = 4;

        // Estado 2
        Arrays.fill(STATE_TRANSITION_MATRIX[2], -3);
        STATE_TRANSITION_MATRIX[2][charToIndex('I')] = ESTADO_ACEPTACION;

        // Estado 3
        Arrays.fill(STATE_TRANSITION_MATRIX[3], ESTADO_ACEPTACION);
        STATE_TRANSITION_MATRIX[3][charToIndex('d')] = 4;

        // Estado 4
        Arrays.fill(STATE_TRANSITION_MATRIX[4], ESTADO_ACEPTACION);
        STATE_TRANSITION_MATRIX[4][charToIndex('d')] = 4;
        STATE_TRANSITION_MATRIX[4][charToIndex('F')] = 5;

        // Estado 5
        Arrays.fill(STATE_TRANSITION_MATRIX[5], -4);
        STATE_TRANSITION_MATRIX[5][charToIndex('-')] = 6;
        STATE_TRANSITION_MATRIX[5][charToIndex('+')] = 6;

        // Estado 6
        Arrays.fill(STATE_TRANSITION_MATRIX[6], -5);
        STATE_TRANSITION_MATRIX[6][charToIndex('d')] = 7;

        // Estado 7
        Arrays.fill(STATE_TRANSITION_MATRIX[7], ESTADO_ACEPTACION);
        STATE_TRANSITION_MATRIX[7][charToIndex('d')] = 7;

        // Estado 8
        Arrays.fill(STATE_TRANSITION_MATRIX[8], 8);
        STATE_TRANSITION_MATRIX[8][charToIndex('"')] = ESTADO_ACEPTACION;
        STATE_TRANSITION_MATRIX[8][charToIndex('n')] = -8;

        // Estado 9
        Arrays.fill(STATE_TRANSITION_MATRIX[9], ESTADO_ACEPTACION);
        STATE_TRANSITION_MATRIX[9][charToIndex('l')] = 9;

        // Estado 10
        Arrays.fill(STATE_TRANSITION_MATRIX[10], ESTADO_ACEPTACION);
        for (char c : new char[] { 'L', 'd', 'U', 'I', 'F', '%' }) {
            STATE_TRANSITION_MATRIX[10][charToIndex(c)] = 10;
        }

        // Estado 11
        Arrays.fill(STATE_TRANSITION_MATRIX[11], -6);
        STATE_TRANSITION_MATRIX[11][charToIndex('=')] = ESTADO_ACEPTACION;

        // Estado 12
        Arrays.fill(STATE_TRANSITION_MATRIX[11], ESTADO_ACEPTACION);

        // Estado 13
        Arrays.fill(STATE_TRANSITION_MATRIX[13], ESTADO_ACEPTACION);

        // Estado 14
        Arrays.fill(STATE_TRANSITION_MATRIX[14], ESTADO_ACEPTACION);

        // Estado 15
        Arrays.fill(STATE_TRANSITION_MATRIX[15], -9);
        STATE_TRANSITION_MATRIX[15][charToIndex('#')] = 16;

        // Estado 16
        Arrays.fill(STATE_TRANSITION_MATRIX[16], 16);
        STATE_TRANSITION_MATRIX[16][charToIndex('#')] = 17;

        // Estado 17
        Arrays.fill(STATE_TRANSITION_MATRIX[17], 16);
        STATE_TRANSITION_MATRIX[17][charToIndex('#')] = 0;
    }

    // --------------------------------------------------------------------------------------------
    // Carga de Matriz de Acciones Semánticas
    // --------------------------------------------------------------------------------------------

    static {

        // Se definen objetos comunes para no tener múltiples instancias iguales.
        SemanticAction[] EMPTY = new SemanticAction[0];
        SemanticAction[] LI = new SemanticAction[] { getSemanticAction("LI") };
        SemanticAction[] LI_FTF = new SemanticAction[] { getSemanticAction("LI"), getSemanticAction("FTF") };
        SemanticAction[] LA = new SemanticAction[] { getSemanticAction("LA") };
        SemanticAction[] LA_FTF = new SemanticAction[] { getSemanticAction("LA"), getSemanticAction("FTF") };
        SemanticAction[] LA_VTF = new SemanticAction[] { getSemanticAction("LA"), getSemanticAction("VTF") };
        SemanticAction[] LA_UIC_VTF = new SemanticAction[] { getSemanticAction("LA"), getSemanticAction("UIC"),
                getSemanticAction("VTF") };
        SemanticAction[] NLD = new SemanticAction[] { getSemanticAction("NLD") };
        SemanticAction[] FC_VTF_RCE = new SemanticAction[] { getSemanticAction("FC"),
                getSemanticAction("VTF"), getSemanticAction("RCE") };
        SemanticAction[] FTF_RCE = new SemanticAction[] { getSemanticAction("FTF"), getSemanticAction("RCE") };
        SemanticAction[] ILC_VTF_RCE = new SemanticAction[] { getSemanticAction("ILC"),
                getSemanticAction("VTF"), getSemanticAction("RCE") };

        // Estado 0
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[0], EMPTY);
        for (char c : new char[] { 'L', 'l', 'd', 'U', 'I', '.', 'F', '"', ':', '=', '>', '<', '-' }) {
            SEMANTIC_ACTIONS_MATRIX[0][charToIndex(c)] = LI;
        }
        SEMANTIC_ACTIONS_MATRIX[0][charToIndex('n')] = NLD;
        for (char c : new char[] { '+', '*', '/', '(', ')', '{', '}', '_', ';', ',' }) {
            SEMANTIC_ACTIONS_MATRIX[0][charToIndex(c)] = LI_FTF;
        }

        // Estado 1
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[1], EMPTY);
        for (char c : new char[] { 'L', 'l', 'd', 'U', '.' }) {
            SEMANTIC_ACTIONS_MATRIX[1][charToIndex(c)] = LA;
        }

        // Estado 2
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[2], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[2][charToIndex('I')] = LA_UIC_VTF;

        // Estado 3
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[3], FTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[3][charToIndex('d')] = LA;

        // Estado 4
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[4], FC_VTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[4][charToIndex('d')] = LA;
        SEMANTIC_ACTIONS_MATRIX[4][charToIndex('F')] = LA;

        // Estado 5
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[5], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[5][charToIndex('-')] = LA;
        SEMANTIC_ACTIONS_MATRIX[5][charToIndex('+')] = LA;

        // Estado 6
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[6], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[6][charToIndex('d')] = LA;

        // Estado 7
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[7], FC_VTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[7][charToIndex('d')] = LA;

        // Estado 8
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[8], LA);
        SEMANTIC_ACTIONS_MATRIX[8][charToIndex('"')] = LA_VTF;
        SEMANTIC_ACTIONS_MATRIX[8][charToIndex('n')] = NLD;

        // Estado 9
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[9], FTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[9][charToIndex('l')] = LA;

        // Estado 10
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[10], ILC_VTF_RCE);
        for (char c : new char[] { 'L', 'd', 'U', 'I', 'F', '%' }) {
            SEMANTIC_ACTIONS_MATRIX[10][charToIndex(c)] = LA;
        }

        // Estado 11
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[11], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[11][charToIndex('=')] = LA_FTF;

        // Estado 12
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[12], FTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[12][charToIndex('=')] = LA_FTF;
        SEMANTIC_ACTIONS_MATRIX[12][charToIndex('!')] = LA_FTF;

        // Estado 13
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[13], FTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[13][charToIndex('>')] = LA_FTF;

        // Estado 14
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[14], FTF_RCE);
        SEMANTIC_ACTIONS_MATRIX[14][charToIndex('=')] = LA_FTF;

        // Estado 15.
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[15], EMPTY);

        // Estado 16
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[16], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[16][charToIndex('n')] = NLD;

        // Estado 17
        Arrays.fill(SEMANTIC_ACTIONS_MATRIX[17], EMPTY);
        SEMANTIC_ACTIONS_MATRIX[17][charToIndex('n')] = NLD;
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
            case '+', '*', '/', '(', ')', '{', '}', '_', ',', ';' -> 8;
            case '"' -> 9;
            case ':' -> 10;
            case '=' -> 11;
            case '!' -> 12;
            case '>' -> 13;
            case '<' -> 14;
            case '%' -> 15;
            case '#' -> 16;
            case 'n' -> 17;
            case 's' -> 18;
            case 't' -> 19;
            default -> 20;
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
            case "FTF" -> FixedTokenFinalizer.getInstance();
            case "FC" -> FloatChecker.getInstance();
            case "ILC" -> IdentifierLengthChecker.getInstance();
            case "LA" -> LexemaAppender.getInstance();
            case "LI" -> LexemaInitializer.getInstance();
            case "NLD" -> NewLineDetected.getInstance();
            case "RCE" -> ReturnCharacterToEntry.getInstance();
            case "UIC" -> UintChecker.getInstance();
            case "VTF" -> VariableTokenFinalizer.getInstance();
            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    public static int getEstadoInicio() {
        return ESTADO_INICIO;
    }

    // --------------------------------------------------------------------------------------------

    public static int getEstadoAceptacion() {
        return ESTADO_ACEPTACION;
    }
}
