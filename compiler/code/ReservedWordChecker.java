public class ReservedWordChecker implements SemanticAction {

    @Override
    public void execute(LexicAnalyzer lexicAnalyzer, String lexema) {
        if (lexicAnalyzer.isReservedWord(lexema)) {
            return new Token(lexema.toUpperCase());
        }
    }

}
