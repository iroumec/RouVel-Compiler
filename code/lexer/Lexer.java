package lexer;

import common.Token;
import common.TokenType;
import lexer.actions.SemanticAction;
import lexer.errors.LexicalError;
import lexer.errors.implementations.BadCommentInitialization;
import lexer.errors.implementations.BadUISuffix;
import lexer.errors.implementations.InvalidAssignmentOperator;
import lexer.errors.implementations.InvalidSymbol;
import lexer.errors.implementations.NewLineInString;
import lexer.errors.implementations.NoExponent;
import lexer.errors.implementations.NoExponentSign;
import lexer.errors.implementations.UndeterminedNumber;
import utilities.Printer;

public final class Lexer {

    private Token currentToken;
    private char lastCharRead;
    private StringBuilder lexema;
    private final String codigoFuente;
    private int nroLinea, siguienteCaracterALeer, nroCaracter;
    private int warningsDetected, errorsDetected;
    private final int estadoInicio, estadoAceptacion;

    private final int[][] matrizTransicionEstados;
    private final SemanticAction[][][] matrizAccionesSemanticas;

    // --------------------------------------------------------------------------------------------

    public Lexer(String sourceCodePath) {
        this.nroLinea = 1;
        this.errorsDetected = 0;
        this.warningsDetected = 0;
        this.siguienteCaracterALeer = 0;
        this.nroCaracter = 0;
        this.estadoInicio = DataManager.getEstadoInicio();
        this.estadoAceptacion = DataManager.getEstadoAceptacion();
        this.codigoFuente = DataManager.loadSourceCode(sourceCodePath);
        this.matrizTransicionEstados = DataManager.getStateTransitionMatrix();
        this.matrizAccionesSemanticas = DataManager.getSemanticActionsMatrix();
    }

    // --------------------------------------------------------------------------------------------

    public Token getNextToken() {

        // Mientras no se haya hallado un token y queden caracteres por leer...
        do {
            // Se limpian los datos arrastrados de búsquedas anteriores.
            // De no haber búsqueda anterior, se inicializan.
            cleanSearch();

            // Se comienza la búsqueda de un token.
            // Al terminar de ejecutarse, se habrá hallado un token
            // o este será nulo (en caso de ya haberse leído todo el archivo).
            searchToken();

        } while (this.currentToken == null && siguienteCaracterALeer <= codigoFuente.length());

        // Si el token es null, es porque no hay más tokens reconocibles.
        // Esto es, se llegó al final del archivo.
        if (this.currentToken == null) {
            this.currentToken = new Token(TokenType.EOF, null);
        }

        // Se imprime el token en la salida.
        Printer.print(this.currentToken);

        // Se devuelve el token.
        return this.currentToken;
    }

    // --------------------------------------------------------------------------------------------

    private void searchToken() {

        int index;
        int estadoActual = estadoInicio;
        int siguienteEstado;

        // Se utiliza "<=" para leer el símbolo especial que indica el fin de archvio:
        // '\0'.
        while (estadoActual != estadoAceptacion && siguienteCaracterALeer <= codigoFuente.length()) {

            // System.out.printf("'%d%s'", this.nroCaracter, this.lastCharRead);
            this.lastCharRead = this.readNextChar();
            this.nroCaracter++;

            index = charToIndex(lastCharRead);

            SemanticAction[] semanticActionsToExecute = matrizAccionesSemanticas[estadoActual][index];
            siguienteEstado = matrizTransicionEstados[estadoActual][index];

            // Si se ha llegado a un estado de error...
            if (siguienteEstado < estadoInicio) {
                estadoActual = this.handleError(estadoActual, siguienteEstado);
            } else {
                // Se avanza al siguiente estado.
                estadoActual = siguienteEstado;
            }

            // Se ejecutan las acciones semánticas asociadas.
            // De llegar a un estado de error, estas estarán vacías.
            for (SemanticAction action : semanticActionsToExecute) {
                action.execute(this);
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    private int handleError(int currentState, int nextState) {

        LexicalError errorHandler = this.getErrorHandler(nextState);

        if (errorHandler == null) {
            throw new IllegalStateException("Se ha encontrado un error léxico no manejable.");
        }

        errorHandler.handleError(this);

        // Errores como el de un comentario mal iniciado requieren volver al estado
        // inicial para evitar la aparición de múltiples errores dadas las transiciones.
        // En los demás casos, basta con quedarse en el estado actual.
        if (errorHandler.requiresReturnToStart()) {
            return estadoInicio;
        }

        // Algunos manejadores dee errores realizan las acciones semánticas necesarias
        // para dejar el token listo para ser entregado.
        if (errorHandler.requiresFinalization()) {
            return estadoAceptacion;
        }
        return currentState;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Se limpian las variables utilizadas entre búsquedas.
     */
    private void cleanSearch() {
        // Se limpia el token actual.
        this.currentToken = null;
        if (this.lexema == null)
            this.lexema = new StringBuilder();
        else
            this.lexema.setLength(0); // Se limpia el lexema y no hay necesidad de crear uno nuevo.
    }

    // --------------------------------------------------------------------------------------------

    /**
     * El objetivo de esta función es que todas las letras mayúsculas, por ejemplo,
     * sean mapeadas a una misma columna de requerirse.
     */
    public static int charToIndex(char c) {
        return switch (c) {
            case 'U' -> 3;
            case 'I' -> 4;
            case '.' -> 5;
            case 'F' -> 6;
            case '-' -> 7;
            case '+', '*', '/', '(', ')', '{', '}', '_', ',', ';' -> 8;
            case '"' -> 9;
            case ':' -> 10;
            case '=' -> 11;
            case '!' -> 12;
            case '>' -> 13;
            case '<' -> 14;
            case '%' -> 15;
            case '#' -> 16;
            case '\n' -> 17;
            // '\0' se comporta igual a que si hubiese un espacio.
            case ' ', '\0' -> 18;
            case '\t' -> 19;
            default -> {
                if (Character.isUpperCase(c))
                    yield 0;
                else if (Character.isLowerCase(c))
                    yield 1;
                else if (Character.isDigit(c))
                    yield 2;
                else
                    yield 20; // Otro.
            }
        };
    }

    // --------------------------------------------------------------------------------------------

    private LexicalError getErrorHandler(int index) {

        return switch (index) {
            case -1 -> InvalidSymbol.getInstance();
            case -2 -> UndeterminedNumber.getInstance();
            case -3 -> BadUISuffix.getInstance();
            case -4 -> NoExponentSign.getInstance();
            case -5 -> NoExponent.getInstance();
            case -6 -> InvalidAssignmentOperator.getInstance();
            case -7 -> NewLineInString.getInstance();
            case -8 -> BadCommentInitialization.getInstance();
            default -> null;
        };
    }

    // --------------------------------------------------------------------------------------------

    public char readNextChar() {
        // Al llegar al final del archivo, se retorna un carácter especial que indique
        // EOF. Adicionalmente, para cortar las iteraciones, se incrementa el siguiente
        // caracter a leer.
        if (this.siguienteCaracterALeer == codigoFuente.length()) {
            this.siguienteCaracterALeer++;
            return '\0';
        }
        return codigoFuente.charAt(siguienteCaracterALeer++);
    }

    // --------------------------------------------------------------------------------------------

    public char getLastCharRead() {
        return this.lastCharRead;
    }

    // --------------------------------------------------------------------------------------------

    public int getNroLinea() {
        return this.nroLinea;
    }

    // --------------------------------------------------------------------------------------------

    public void incrementarNroLinea() {
        this.nroLinea++;
    }

    // --------------------------------------------------------------------------------------------

    public void decrementarSiguienteCaracterALeer() {

        this.siguienteCaracterALeer--;
        this.nroCaracter--;
    }

    // --------------------------------------------------------------------------------------------

    public void setLexema(String lexema) {
        this.lexema.setLength(0);
        this.lexema.append(lexema);
    }

    // --------------------------------------------------------------------------------------------

    public void initializeLexema(char startingChar) {
        this.lexema.setLength(0);
        this.lexema.append(startingChar);
    }

    // --------------------------------------------------------------------------------------------

    public void appendToLexema(char charToAppend) {
        this.lexema.append(charToAppend);
    }

    // --------------------------------------------------------------------------------------------

    public void appendToLexema(String stringToAppeend) {
        this.lexema.append(stringToAppeend);
    }

    // --------------------------------------------------------------------------------------------

    public String getLexema() {
        return this.lexema.toString();
    }

    // --------------------------------------------------------------------------------------------

    public void setToken(Token token) {
        this.currentToken = token;
    }

    // --------------------------------------------------------------------------------------------

    public Token getCurrentToken() {
        return this.currentToken;
    }

    // --------------------------------------------------------------------------------------------

    public void notifyWarning(String warningMessage) {
        System.err.println("------------------------------------");
        System.err.printf("WARNING LÉXICO: LÍNEA %d: %s\n", this.nroLinea, warningMessage);
        System.err.println("------------------------------------");
        this.warningsDetected++;
    }

    // --------------------------------------------------------------------------------------------

    public int getWarningsDetected() {
        return this.warningsDetected;
    }

    // --------------------------------------------------------------------------------------------

    public void notifyError(String errorMessage) {
        System.err.println("------------------------------------");
        System.err.printf("ERROR LÉXICO: Línea %d, caracter %d: %s%n", this.nroLinea, this.getNroCaracter(),
                errorMessage);
        System.err.println("------------------------------------");
        this.errorsDetected++;
    }

    // --------------------------------------------------------------------------------------------

    public int getErrorsDetected() {
        return this.errorsDetected;
    }

    // --------------------------------------------------------------------------------------------

    public int getNroCaracter() {
        return this.nroCaracter - 2;
    }

    // --------------------------------------------------------------------------------------------

    public void resetearNroCaracter() {
        this.nroCaracter = 0;
    }
}
