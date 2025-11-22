package assembler;

import semantic.ReversePolish;
import assembler.operators.AssemblerOperator;

public class Assembler {

    private static final boolean debug = false;

    // ============================================================================================

    public static String generate(ReversePolish reversePolish) {

        CodeRepository codeRepository = new CodeRepository();

        // Se abre el programa principal.
        codeRepository.startProgram();

        for (String polish : reversePolish) {

            AssemblerOperator operator = OperatorTranslator.getOperator(polish);

            if (operator != null) {
                if (debug) {
                    System.out.println("Operator " + polish + " detected.");
                }

                operator.generateAssembler(codeRepository);

            } else {
                codeRepository.pushOperand(polish);

                if (debug) {
                    System.out.println("Polish " + polish + " added to the operands.");
                }
            }
        }

        // Se cierra el programa principal.
        codeRepository.endProgram();

        // Se retorna el código completo.
        return codeRepository.getProgram();
    }

    // ============================================================================================

}
