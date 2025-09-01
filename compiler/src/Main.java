public class Main {
    public static void main(String[] args) {

        LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

        System.out.println("Tokens identificados en el programa:");

        Token token = lexicalAnalyzer.getNextToken();
        while (token != null) {
            System.out.println(token);
            token = lexicalAnalyzer.getNextToken();
        }

    }
}