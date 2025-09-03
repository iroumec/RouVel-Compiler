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

public final class LexicalAnalyzer {

    private Token token;
    private int nroLinea;
    private char lastCharRead;
    private String codigoFuente;
    private StringBuilder lexema;
    private TokenType detectedType;
    private int siguienteCaracterALeer;

    private final static int estadoInicio = 0;
    private final static int estadoError = -1;
    private final static int maxCaracteres = 20;
    private final static int estadoAceptacion = 19;

    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][][] matrizAccionesSemanticas;

    // --------------------------------------------------------------------------------------------

    public LexicalAnalyzer() {
        this.nroLinea = 1;
        this.siguienteCaracterALeer = 0;
        // Se agrega una marca para indicar el final del archivo.
        // Quizás deberpia usarse '\0'.
        this.codigoFuente = DataLoader.loadSourceCode() + '\s';
        this.matrizTransicionEstados = DataLoader.loadStateTransitionMatrix();
        // System.out.println(Arrays.deepToString(matrizTransicionEstados));
        this.matrizAccionesSemanticas = DataLoader.loadSemanticActionsMatrix();
        // System.out.println(Arrays.deepToString(matrizAccionesSemanticas));
    }

    // --------------------------------------------------------------------------------------------

    public Token getNextToken() {

        // Se limpia los datos arrastrados de búsquedas anteriores.
        // De no haber búsqueda anterior, se inicializan.
        cleanSearch();

        // Se comienza la búsqueda de un token.
        searchToken();

        // Se devuelve un token (si se halló).
        return this.token;
    }

    // --------------------------------------------------------------------------------------------

    private void searchToken() {

        int index;
        int estadoActual = estadoInicio;

        while (estadoActual != estadoAceptacion && siguienteCaracterALeer < codigoFuente.length()) {

            this.lastCharRead = codigoFuente.charAt(siguienteCaracterALeer);

            index = charToIndex(lastCharRead);

            SemanticAction[] semanticActionsToExecute = matrizAccionesSemanticas[estadoActual][index];
            estadoActual = matrizTransicionEstados[estadoActual][index];

            if (estadoActual == estadoError) {
                System.err.println("Error: Línea " + this.nroLinea + ": Se detectó el carácter \"" + this.lastCharRead
                        + "\", el cual no corresponde a un símbolo válido en el lenguaje.");
                System.exit(1);
            }

            for (SemanticAction action : semanticActionsToExecute) {
                action.execute(this);
            }

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
                    yield 28;
            }
        };
    }

    // --------------------------------------------------------------------------------------------

    public char getLastCharRead() {
        return this.lastCharRead;
    }

    // --------------------------------------------------------------------------------------------

    public int getMaxCaracteres() {
        return maxCaracteres;
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
        this.lexema = new StringBuilder(lexema);
    }

    // --------------------------------------------------------------------------------------------

    public void initializeLexema(char startingChar) {
        this.lexema = new StringBuilder().append(startingChar);
    }

    // --------------------------------------------------------------------------------------------

    public void appendToLexema(char charToAppend) {
        this.lexema.append(charToAppend);
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
}
