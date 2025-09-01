import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class LexicalAnalyzer {

    private static LexicalAnalyzer INSTANCE;

    private final static int estadoInicio = 0;
    private final static int estadoError = -1;
    private final static int maxCaracteres = 20;
    private final static int estadoAceptacion = 21;

    private final Set<String> reservedWords;
    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][] matrizAccionesSemanticas;

    private int nroLinea = 1;
    private String codigoFuente;
    private int siguienteCaracterALeer;

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
    }

    // --------------------------------------------------------------------------------------------

    public static LexicalAnalyzer getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new LexicalAnalyzer();
        }

        return INSTANCE;
    }

    // --------------------------------------------------------------------------------------------

    private LexicalAnalyzer() {
        this.matrizTransicionEstados = DataLoader.loadStateTransitionMatrix();
        this.matrizAccionesSemanticas = DataLoader.loadSemanticActionMatrix();
        this.reservedWords = DataLoader.loadReservedWords();
        this.codigoFuente = DataLoader.loadSourceCode();
        this.siguienteCaracterALeer = 0;
    }

    // --------------------------------------------------------------------------------------------

    public Token getNextToken() {

        String lexema = "";
        char caracter = ' ';
        int normalizedChar;
        TokenType tokenType = null;
        SemanticAction accionSemantica;
        int estadoActual = estadoInicio;

        while (estadoActual != estadoAceptacion) {

            // Si estoy en el estado cero, se vacía el lexema.
            // En caso contrario, pasé a un estado no final y lo agrego.
            lexema = (estadoActual == estadoInicio) ? "" : lexema + caracter;

            caracter = codigoFuente.charAt(siguienteCaracterALeer);
            System.out.println("Caracter leído: \"" + caracter + "\"");
            System.out.println("Lexema actual: \"" + lexema + "\"");
            normalizedChar = normalizeChar(caracter);
            System.out.println("Caracter normalizado: " + normalizedChar);

            accionSemantica = matrizAccionesSemanticas[estadoActual][normalizedChar];
            estadoActual = matrizTransicionEstados[estadoActual][normalizedChar];

            System.out.println("Estado siguiente: " + estadoActual);

            if (estadoActual == estadoError) {
                System.err.println("Se llegó a un estado de error.");
                System.exit(1);
            }

            if (accionSemantica != null)
                accionSemantica.execute(this, lexema);

            if (estadoActual == estadoAceptacion) {
                if (isReservedWord(lexema + caracter)) {
                    System.out.println("Palabra reservada hallada.");
                    lexema += caracter;
                    tokenType = TokenType.fromSymbol(lexema);
                } else {
                    if (isReservedWord(lexema)) {
                        System.out.println("Palabra reservada hallada.");
                        tokenType = TokenType.fromSymbol(lexema);
                    }
                }
            } else {
                siguienteCaracterALeer++;
            }
        }

        return new Token(tokenType);
    }

    public boolean isReservedWord(String lexema) {
        return this.reservedWords.contains(lexema);
    }

    public int getMaxCaracteres() {
        return maxCaracteres;
    }

    public int getNroLinea() {
        return nroLinea;
    }

    public void incrementarNroLinea() {
        this.nroLinea++;
    }

    public void decrementarSiguienteCaracterALeer() {
        this.siguienteCaracterALeer--;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     */
    private int normalizeChar(char c) {

        // Chequeo de símbolos particulares.
        if (excepciones.containsKey(c)) {
            return excepciones.get(c);
        }

        // Chequeo de reglas generales.
        return Character.isUpperCase(c) ? 0 : Character.isLowerCase(c) ? 1 : Character.isDigit(c) ? 2 : 21;
    }
}
