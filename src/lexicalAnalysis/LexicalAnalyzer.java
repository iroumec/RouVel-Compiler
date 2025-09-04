package lexicalAnalysis;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import general.Token;
import general.TokenType;
import general.SymbolTable;
import lexicalAnalysis.lexicalErrors.*;

public final class LexicalAnalyzer {

    private Token token;
    private char lastCharRead;
    private String codigoFuente;
    private StringBuilder lexema;
    private TokenType detectedType;
    private int nroLinea, siguienteCaracterALeer;
    private int warningsDetected, errorsDetected;

    private final static int ESTADO_ERROR = -1;
    private final static int ESTADO_INICIO = 0;
    private final static int MAX_CARACTERES = 20;
    private final static int ESTADO_ACEPTACION = 19;

    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][][] matrizAccionesSemanticas;

    // --------------------------------------------------------------------------------------------

    public LexicalAnalyzer(String sourceCodePath) {
        this.nroLinea = 1;
        this.errorsDetected = 0;
        this.warningsDetected = 0;
        this.siguienteCaracterALeer = 0;
        // Se agrega una marca para indicar el final del archivo.
        // Quizás deberpia usarse '\0'.
        this.codigoFuente = DataLoader.loadSourceCode(sourceCodePath) + '\s';
        this.matrizTransicionEstados = DataLoader.loadStateTransitionMatrix();
        // System.out.println(Arrays.deepToString(matrizTransicionEstados));
        this.matrizAccionesSemanticas = DataLoader.loadSemanticActionsMatrix();
        // System.out.println(Arrays.deepToString(matrizAccionesSemanticas));
    }

    // --------------------------------------------------------------------------------------------

    public Token getNextToken() {

        // Mientras no se haya hallado un token y queden caracteres por leer...
        do {
            // Se limpian los datos arrastrados de búsquedas anteriores.
            // De no haber búsqueda anterior, se inicializan.
            cleanSearch();

            // Se comienza la búsqueda de un token.
            // Al terminar de ejecutarse, se habrá hallado un token
            // o este será nulo (en caso de ya haberse leído todo el archivo).
            searchToken();

        } while (this.token == null && siguienteCaracterALeer < codigoFuente.length());

        // Se devuelve un token (si se halló).
        // En otro caso, se devuelve null.
        return this.token;
    }

    // --------------------------------------------------------------------------------------------

    private void searchToken() {

        int index;
        int estadoActual = ESTADO_INICIO;
        int siguienteEstado;

        while (estadoActual != ESTADO_ACEPTACION && siguienteCaracterALeer < codigoFuente.length()) {

            this.lastCharRead = codigoFuente.charAt(siguienteCaracterALeer);

            index = charToIndex(lastCharRead);

            SemanticAction[] semanticActionsToExecute = matrizAccionesSemanticas[estadoActual][index];
            siguienteEstado = matrizTransicionEstados[estadoActual][index];

            // Se ha llegado a un estado de error.
            if (siguienteEstado < ESTADO_INICIO) {

                LexicalError errorHandler = this.getErrorHandler(siguienteEstado);
                if (errorHandler != null) {
                    errorHandler.handleError(this);
                } else {
                    throw new IllegalStateException("Se ha encontrado un error léxico no manejable.");
                }

                // Errores como el de un comentario mal iniciado requieren volver al estado
                // inicial para evitar la aparición de múltiples errores dadas las transiciones.
                // En los demás casos, basta con quedarse en el estado actual.
                if (errorHandler.requiresReturnToStart()) {
                    estadoActual = 0;
                }

                // Algunos manejadores dee errores realizan las acciones semánticas necesarias
                // para dejar el token listo para ser entregado.
                if (errorHandler.requiresFinalization()) {
                    estadoActual = ESTADO_ACEPTACION;
                }

            } else {

                // Se avanza al siguiente estado.
                estadoActual = siguienteEstado;

                // Se ejecutan las acciones semánticas asociadas.
                for (SemanticAction action : semanticActionsToExecute) {
                    action.execute(this);
                }

            }

            // En todo caso, se avanza al siguiente carácter.
            siguienteCaracterALeer++;
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se limpian las variables utilizadas entre búsquedas.
     */
    private void cleanSearch() {
        this.token = null;
        this.detectedType = null;
        if (this.lexema == null)
            this.lexema = new StringBuilder();
        else
            this.lexema.setLength(0); // Se limpia el lexema y no hay necesidad de crear uno nuevo.
    }

    // --------------------------------------------------------------------------------------------

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     */
    public static int charToIndex(char c) {
        return switch (c) {
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
            case '\n' -> 25;
            case ' ' -> 26;
            case '\t' -> 27;
            default -> {
                if (Character.isUpperCase(c))
                    yield 0;
                else if (Character.isLowerCase(c))
                    yield 1;
                else if (Character.isDigit(c))
                    yield 2;
                else
                    yield 28; // Otro.
            }
        };
    }

    // --------------------------------------------------------------------------------------------

    private LexicalError getErrorHandler(int index) {

        return switch (index) {
            case -1 -> InvalidSymbol.getInstance();
            case -2 -> UndeterminedNumber.getInstance();
            case -3 -> BadUISuffix.getInstance();
            case -4 -> InvalidDecimalFormat.getInstance();
            case -5 -> NoExponentSign.getInstance();
            case -6 -> NoExponent.getInstance();
            case -7 -> InvalidAssignmentOperator.getInstance();
            case -8 -> NewLineInString.getInstance();
            case -9 -> BadCommentInitialization.getInstance();
            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    public char getCharInAdvance() {
        if (siguienteCaracterALeer + 1 < codigoFuente.length()) {
            siguienteCaracterALeer++;
            return codigoFuente.charAt(siguienteCaracterALeer);
        } else {
            return '\0';
        }
    }

    // --------------------------------------------------------------------------------------------

    public char getLastCharRead() {
        return this.lastCharRead;
    }

    // --------------------------------------------------------------------------------------------

    public int getMaxCaracteres() {
        return MAX_CARACTERES;
    }

    // --------------------------------------------------------------------------------------------

    public int getNroLinea() {
        return this.nroLinea;
    }

    // --------------------------------------------------------------------------------------------

    public void incrementarNroLinea() {
        this.nroLinea++;
    }

    // --------------------------------------------------------------------------------------------

    public void decrementarSiguienteCaracterALeer() {
        this.siguienteCaracterALeer--;
    }

    // --------------------------------------------------------------------------------------------

    public void setLexema(String lexema) {
        this.lexema.setLength(0);
        this.lexema.append(lexema);
    }

    // --------------------------------------------------------------------------------------------

    public void initializeLexema(char startingChar) {
        this.lexema.setLength(0);
        this.lexema.append(startingChar);
    }

    // --------------------------------------------------------------------------------------------

    public void appendToLexema(char charToAppend) {
        this.lexema.append(charToAppend);
    }

    // --------------------------------------------------------------------------------------------

    public void appendToLexema(String stringToAppeend) {
        this.lexema.append(stringToAppeend);
    }

    // --------------------------------------------------------------------------------------------

    public String getLexema() {
        return this.lexema.toString();
    }

    // --------------------------------------------------------------------------------------------

    public void setDetectedType(TokenType type) {
        this.detectedType = type;
    }

    // --------------------------------------------------------------------------------------------

    public TokenType getDetectedType() {
        return this.detectedType;
    }

    // --------------------------------------------------------------------------------------------

    public void setToken(Token token) {
        this.token = token;
    }

    // --------------------------------------------------------------------------------------------

    public void incrementWarningsDetected() {
        this.warningsDetected++;
    }

    // --------------------------------------------------------------------------------------------

    public int getWarningsDetected() {
        return this.warningsDetected;
    }

    // --------------------------------------------------------------------------------------------

    public void incrementErrorsDetected() {
        this.errorsDetected++;
    }

    // --------------------------------------------------------------------------------------------

    public int getErrorsDetected() {
        return this.errorsDetected;
    }
}
