package program.code;

public class LexicAnalyzer {

    private int[][] matrizTransicionEstados;
    private Function[][] matrizAccionesSemanticas;

    public LexicAnalyzer() {
        this.matrizTransicionEstados = obtenerMatrizTransicionEstados();
        this.matrizAccionesSemanticas = obtenerMatrizAccionesSemanticas();
    }

    private int[][] obtenerMatrizTransicionEstados() {

        return new int[][] {
                {}, // 0
                {}, // 1
                {}
        };
    }

}
