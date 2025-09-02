package semanticActions;

import app.LexicalAnalyzer;

public class FloatChecker implements SemanticAction {

    private static final float MIN_POS_VAL = 1.17549435E-38f;
    private static final float MAX_POS_VAL = 3.40282347E+38f;
    private static final float MIN_NEG_VAL = -3.40282347E+38f;
    private static final float MAX_NEG_VAL = -1.17549435E-38f;

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}