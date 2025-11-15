package assembler;

import java.util.Deque;
import java.util.ArrayDeque;

import semantic.ReversePolish;
import assembler.operators.AssemblerOperator;

public class Assembler {

    private static final boolean debug = false;

    public static String generate(ReversePolish reversePolish) {

        Deque<String> operands = new ArrayDeque<>();
        StringBuilder assemblerCode = new StringBuilder();
        StringBuilder executableCode = new StringBuilder();

        StringBuilder indentation = new StringBuilder();
        indentation.append("    ");

        for (String polish : reversePolish) {

            AssemblerOperator operator = OperatorTranslator.getOperator(polish);

            if (operator != null) {
                if (debug) {
                    System.out.println("Operator " + polish + " detected.");
                }

                if (operator.producesExitChangeInIndentation()) {
                    indentation.setLength(indentation.length() - operator.getExitIndentationChange() * 4);
                }

                String iterationCode = operator.getAssembler(operands);
                if (!iterationCode.isBlank()) {
                    executableCode.append(Indenter.indent(iterationCode, indentation)).append("\n");
                }

                if (operator.producesEntryChangeInIndentation()) {
                    indentation.append("    ".repeat(operator.getEntryIndentationChange()));
                }

            } else {
                operands.push(polish);

                if (debug) {
                    System.out.println("Polish " + polish + " added to the operands.");
                }
            }
        }

        // El volcado de las variables globales se hace a lo último, ya que, durante la
        // generación del código assembler, se generan variables auxiliares que también
        // deben ser agregadas.

        // Si hay al menos una impresión, se debe importar el módulo de impresiones.
        assemblerCode.append("(module \n");
        if (executableCode.toString().contains("$print")) {
            assemblerCode.append("""

                        ;; Importación de funciones de impresión.
                        (import "console" "log" (func $printString (param i32 i32)))
                        (import "console" "logInt" (func $printInt (param i32)))
                        (import "console" "logFloat" (func $logFloat (param f32)))

                        (import "js" "mem" (memory 1))
                    """);
        }

        // assemblerCode.append(dumpGlobalVariables());

        String stringsSection = Dumper.dumpStrings();
        if (!stringsSection.isBlank()) {
            assemblerCode.append("\n").append(stringsSection);
        }

        assemblerCode.append("\n").append(Indenter.indent(Dumper.dumpEntryPoint(), indentation.toString()));

        if (!executableCode.isEmpty()) {
            assemblerCode.append("\n").append(Indenter.indent(executableCode, indentation));
            assemblerCode.append("\n");
        }

        return assemblerCode.append(")").toString();
    }
}
