public class Main {
    public static void main(String[] args) {

        DataLoader.setSourceCode(
                "/home/iroumec/Documents/University/Compiladores e Intérpretes/TPE-Compiler/resources/testFiles/firstTest.uki");

        LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

        System.out.println("Tokens identificados en el programa:");

        Token token = lexicalAnalyzer.getNextToken();
        while (token != null) {
            System.out.println(token);
            token = lexicalAnalyzer.getNextToken();
        }

        System.out.println("El programa tiene " + lexicalAnalyzer.getNroLinea() + " líneas.");
    }
}