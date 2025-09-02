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
    private static final Map<Character, Integer> excepciones = new HashMap<>();
    static {
        excepciones.put('U', 3);
        excepciones.put('I', 4);
        excepciones.put('.', 5);
        excepciones.put('F', 6);
        excepciones.put('-', 7);
        excepciones.put('+', 8);
        excepciones.put('"', 9);
        excepciones.put('*', 10);
        excepciones.put('/', 11);
        excepciones.put('(', 12);
        excepciones.put(')', 13);
        excepciones.put('{', 14);
        excepciones.put('}', 15);
        excepciones.put('_', 16);
        excepciones.put(';', 17);
        excepciones.put(':', 18);
        excepciones.put('=', 19);
        excepciones.put('!', 20);
        excepciones.put('>', 21);
        excepciones.put('<', 22);
        excepciones.put('%', 23);
        excepciones.put('#', 24);
        excepciones.put('\n', 25);
        excepciones.put('\s', 26);
        excepciones.put('\t', 27);
    }

    // --------------------------------------------------------------------------------------------

    public LexicalAnalyzer() {
        this.nroLinea = 1;
        this.siguienteCaracterALeer = 0;
        // Se agrega un espacio en blanco para marcar el final del archivo.
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

        int estadoActual = estadoInicio;

        while (estadoActual != estadoAceptacion && siguienteCaracterALeer < codigoFuente.length()) {

            this.lastCharRead = codigoFuente.charAt(siguienteCaracterALeer);

            int normalizedChar = normalizeChar(lastCharRead);

            // System.out.println("Estado actual: " + estadoActual);

            SemanticAction[] semanticActionsToExecute = matrizAccionesSemanticas[estadoActual][normalizedChar];
            estadoActual = matrizTransicionEstados[estadoActual][normalizedChar];

            // System.out.println("Caracter: \"" + this.lastCharRead + "\"");
            // System.out.println("Normalización: " + normalizedChar);
            // System.out.println("Siguiente estado: " + estadoActual);

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
        this.lexema = null;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     */
    private int normalizeChar(char c) {

        if (detectedType == null || detectedType == TokenType.CTE || detectedType == TokenType.STR
                || (detectedType == TokenType.ID && c == '%')) {
            // Chequeo de símbolos particulares.
            if (excepciones.containsKey(c)) {
                return excepciones.get(c);
            }
        }

        // Chequeo de reglas generales.
        return Character.isUpperCase(c) ? 0 : Character.isLowerCase(c) ? 1 : Character.isDigit(c) ? 2 : 28;

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
