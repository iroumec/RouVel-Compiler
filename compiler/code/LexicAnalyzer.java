import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LexicAnalyzer {

    private static LexicAnalyzer INSTANCE;

    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][] matrizAccionesSemanticas;
    private final Set<String> reservedWords;

    private String codigoFuente;
    private int siguienteCaracterALeer;

    private static final Map<Character, Integer> excepciones = new HashMap<>();
    static {
        excepciones.put('U', 8);
        excepciones.put('I', 9);
        excepciones.put('F', 10);
        excepciones.put('-', 11);
        excepciones.put('+', 12);
        excepciones.put('.', 13);
    }

    public static LexicAnalyzer getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new LexicAnalyzer();
        }

        return INSTANCE;
    }

    private LexicAnalyzer() {
        this.matrizTransicionEstados = getMatrizTransicionEstados();
        this.matrizAccionesSemanticas = getMatrizAccionesSemanticas();
        this.reservedWords = DataManager.getReservedWords();
        this.codigoFuente = DataManager.getSourceCode();
        this.siguienteCaracterALeer = 0;
        // codigoFuente.charAt(siguienteCaracterALeer);
    }

    public Token getNextToken() {

        int estadoActual = 0;
        // -1 es el fin.
        while (estadoActual != -1) {
            char caracter = codigoFuente.charAt(siguienteCaracterALeer);
            int nomalizedChar = normalizeChar(caracter);

            SemanticAction accionSemantica = matrizAccionesSemanticas[estadoActual][nomalizedChar];
            estadoActual = matrizTransicionEstados[estadoActual][normalizeChar(caracter)];

            accionSemantica.execute();

            siguienteCaracterALeer++;
        }

        return new Token();
    }

    public boolean isReservedWord(String lexema) {
        return this.reservedWords.contains(lexema);
    }

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     * 
     * 1 --> Mayúscula.
     * 2 --> Minúscula.
     * 3 --> Dígito.
     * 
     * @param c
     * @return
     */
    private int normalizeChar(char c) {

        // Chequeo de símbolos particulares.
        if (excepciones.containsKey(c)) {
            return excepciones.get(c);
        }

        // Chequeo de reglas generales.
        return Character.isUpperCase(c) ? 1 : Character.isLowerCase(c) ? 2 : Character.isDigit(c) ? 3 : 0;
    }

    private int[][] getMatrizTransicionEstados() {

        // ¿Podría cargarse desde un CCV?

        return new int[][] {
                {}, // 0
                {}, // 1
                {}
        };
    }

    private SemanticAction[][] getMatrizAccionesSemanticas() {
        return new SemanticAction[1][1];

    }

}
