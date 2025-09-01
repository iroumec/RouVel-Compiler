//AS1
public class LexemaInitializer implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.setLexema("" + lexicalAnalyzer.getLastCharRead());
    }

}
