//AS2
public class LexemaAppender implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        lexicalAnalyzer.setLexema(lexicalAnalyzer.getLexema() + lexicalAnalyzer.getLastCharRead());
    }

}