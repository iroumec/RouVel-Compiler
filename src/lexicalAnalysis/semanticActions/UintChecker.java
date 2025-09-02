package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

public class UintChecker implements SemanticAction {
    private static final int MAX_UINT = 65535;

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        // Se eliminan ceros adicionales a la izquierda.
        lexema = cleanUint(lexema);

        System.out.println(lexema);

        if (!isInRange(lexema)) {
            System.out.println(
                    "WARNING: El número " + lexema
                            + " está fuera del rango de uint. Se ajustará al máximo en el rango permitido [0, "
                            + MAX_UINT + "].");
            lexicalAnalyzer.setLexema(MAX_UINT + "UI");
        }
    }

    private String cleanUint(String lexema) {

        return lexema.replaceFirst("^0+(?!$)", "");
    }

    private boolean isInRange(String lexema) {

        // -2 ya que contiene UI.
        if (lexema.length() - 2 > String.valueOf(MAX_UINT).length()) {
            return false;
        }

        int number = Integer.parseInt(lexema.substring(0, lexema.length() - 2));
        return number >= 0 && number <= MAX_UINT;

    }

}