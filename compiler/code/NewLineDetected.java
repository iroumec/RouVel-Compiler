public class NewLineDetected implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicAnalyzer, String lexema) {
        lexicAnalyzer.incrementarNroLinea();
    }

}