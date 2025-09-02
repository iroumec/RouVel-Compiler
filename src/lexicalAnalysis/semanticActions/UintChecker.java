package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

public class UintChecker implements SemanticAction {
    private static final int MAX_UINT = 65535;

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();
        // System.out.println("UintChecker: lexema = " + lexema);

        int number = Integer.parseInt(lexema.substring(0, lexema.length() - 2));
        if (number < 0 || number > MAX_UINT) {
            System.out.println(
                    "WARNING: El número " + number
                            + " está fuera del rango de uint. Se ajustará al máximo en el rango permitido [0, "
                            + MAX_UINT + "].");
            lexicalAnalyzer.setLexema(MAX_UINT + "UI");
        }
    }

}