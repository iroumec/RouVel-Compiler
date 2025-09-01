public class ReservedWordChecker implements SemanticAction {

    @Override
    public void execute(LexicAnalyzer lexicAnalyzer, String lexema) {
        if (!lexicAnalyzer.isReservedWord(lexema)) {
            System.err.println("Error en la línea "+ lexicAnalyzer.getNroLinea() + ": La palabra '" + lexema + "' no es una palabra reservada.");
        } else {
            lexicAnalyzer.setSiguienteCaracterALeer
        }

        // ¿Buscar la palabra reservada más parecida a la que puso el usuario?
    }

}