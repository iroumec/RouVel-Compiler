package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;

public class FloatChecker implements SemanticAction {

    private static final float MIN_POS_VAL = 1.17549435E-38f;
    private static final float MAX_POS_VAL = 3.40282347E+38f;
    private static final float MIN_NEG_VAL = -3.40282347E+38f;
    private static final float MAX_NEG_VAL = -1.17549435E-38f;

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        String lexema = lexicalAnalyzer.getLexema();
        if (lexema.contains("F")) {
            lexema = lexema.replace("F", "E");
            lexema += "f";
        }
        float value = Float.parseFloat(lexema);
        if (!((value >= MIN_POS_VAL && value <= MAX_POS_VAL) || (value >= MIN_NEG_VAL && value <= MAX_NEG_VAL))) {
            System.out.println("WARNING: El número flotante " + lexema
                    + " está fuera del rango de representación. Se asignará el valor 0.");
            lexicalAnalyzer.setLexema("0.0");
        }
    }

}