import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class LexicAnalyzer {

    private final static LexicAnalyzer INSTANCE;

    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][] matrizAccionesSemanticas;
    private final Set<String> reservedWords;

    private String codigoFuente;
    private int siguienteCaracterALeer;

    public LexicAnalyzer getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new LexicAnalyzer(codigoFuente);
        }
    }

    private LexicAnalyzer(String sourceCodePath) {
        this.matrizTransicionEstados = getMatrizTransicionEstados();
        this.matrizAccionesSemanticas = getMatrizAccionesSemanticas();
        this.reservedWords = DataManager.getReservedWords();

        try {
            this.codigoFuente = Files.readString(Paths.get(sourceCodePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
     * @param c
     * @return
     */
    private int normalizeChar(char c) {

        return 0;
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
