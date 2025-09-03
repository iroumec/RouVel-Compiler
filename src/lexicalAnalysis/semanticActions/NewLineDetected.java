package lexicalAnalysis.semanticActions;

import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.SemanticAction;

/**
 * ASN
 */
public class NewLineDetected implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.incrementarNroLinea();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ASN";
    }
}