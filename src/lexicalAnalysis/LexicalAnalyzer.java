package lexicalAnalysis;

import java.util.Set;
import java.util.Map;
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
        this.matrizAccionesSemanticas = DataLoader.loadSemanticActionMatrix();
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

        int normalizedChar;
        int estadoActual = estadoInicio;

        while (estadoActual != estadoAceptacion && siguienteCaracterALeer < codigoFuente.length()) {

            this.lastCharRead = codigoFuente.charAt(siguienteCaracterALeer);

            normalizedChar = normalizeChar(lastCharRead);

            SemanticAction[] semanticActionsToExecute = matrizAccionesSemanticas[estadoActual][normalizedChar];
            estadoActual = matrizTransicionEstados[estadoActual][normalizedChar];

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
    private int normalizeChar(char c) {

        int exc = excepcion(c);
        if (exc != -1)
            return exc;

        /*
         * if (detectedType == null || detectedType == TokenType.CTE || detectedType ==
         * TokenType.STR
         * || (detectedType == TokenType.ID && c == '%')) {
         * int exc = excepcion(c);
         * if (exc != -1)
         * return exc;
         * }
         */
        if (Character.isUpperCase(c))
            return 0;
        if (Character.isLowerCase(c))
            return 1;
        if (Character.isDigit(c))
            return 2;
        return 28;
    }

    // --------------------------------------------------------------------------------------------

    private int excepcion(char c) {
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
            default -> -1;
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
