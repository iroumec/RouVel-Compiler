public class ReservedWordChecker implements SemanticAction {

    @Override
    public void execute(LexicalAnalyzer lexicalAnalyzer) {
        String lexema = lexicalAnalyzer.getLexema();
        if (!lexicalAnalyzer.isReservedWord(lexema)) {
            System.err.println("Error en la línea " + lexicalAnalyzer.getNroLinea() + ": La palabra '" + lexema
                    + "' no es una palabra reservada.");
        } else {
            lexicalAnalyzer.decrementarSiguienteCaracterALeer();

        }

        // ¿Buscar la palabra reservada más parecida a la que puso el usuario?
    }

}