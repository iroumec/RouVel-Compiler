package lexicalAnalysis.semanticActions;
//AS3

import lexicalAnalysis.LexicalAnalyzer;

/**
 * AS5
 */
public class IdentifierLengthChecker implements SemanticAction {
    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {

        String lexema = lexicalAnalyzer.getLexema();

        if (lexema.length() > lexicalAnalyzer.getMaxCaracteres()) {
            System.out.println("WARNING en la línea " + lexicalAnalyzer.getNroLinea() + ": El identificador '" + lexema
                    + "' excede la longitud máxima de " + lexicalAnalyzer.getMaxCaracteres()
                    + " caracteres. Se truncará a '"
                    + lexema.substring(0, lexicalAnalyzer.getMaxCaracteres()) + "'.");
            lexema = lexema.substring(0, lexicalAnalyzer.getMaxCaracteres());

            lexicalAnalyzer.setLexema(lexema);
        }
    }
}