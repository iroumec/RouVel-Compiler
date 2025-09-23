package utilities;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import lexer.DataManager;
import lexer.actions.SemanticAction;

/**
 * Esta clase únicamente tiene el objetivo de exportar las matrices a .csv
 * para su presentación visual en el informe. No forma parte del compilador.
 */
public class MatrixExporter {
    public static void main(String[] args) throws Exception {
        int[][] states = DataManager.getStateTransitionMatrix();
        SemanticAction[][][] actions = DataManager.getSemanticActionsMatrix();

        // Exportación de matriz de transición de estados.
        try (PrintWriter pw = new PrintWriter(new FileWriter("resources/diagrams/matrix/csv/matrizEstados.csv"))) {
            for (int[] row : states) {
                for (int i = 0; i < row.length; i++) {
                    pw.print(row[i]);
                    if (i < row.length - 1)
                        pw.print(",");
                }
                pw.println();
            }
        }

        // Exportación de matriz de acciones semánticas.
        try (PrintWriter pw = new PrintWriter(new FileWriter("resources/diagrams/matrix/csv/matrizAcciones.csv"))) {
            for (SemanticAction[][] row : actions) {
                for (int i = 0; i < row.length; i++) {
                    SemanticAction[] cell = row[i];
                    String cellStr = (cell == null || cell.length == 0) ? ""
                            : String.join(",", Arrays.stream(cell)
                                    .map(Object::toString)
                                    .toArray(String[]::new));
                    pw.print("\"" + cellStr + "\""); // Comillas para proteger la celda.
                    if (i < row.length - 1)
                        pw.print(",");
                }
                pw.println();
            }
        }
    }
}
