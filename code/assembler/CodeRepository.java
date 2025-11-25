package assembler;

import java.util.Deque;

import common.SymbolTable;

import java.util.ArrayDeque;

public final class CodeRepository {

    private final static String INDENTATION = "    ";

    // ============================================================================================

    private StringBuilder imports;
    private Deque<String> operands;
    private StringBuilder executableCode;

    // ============================================================================================

    private Deque<Block> blockStack;

    // ============================================================================================

    public CodeRepository() {
        this.imports = new StringBuilder();
        this.operands = new ArrayDeque<>();
        this.blockStack = new ArrayDeque<>();
        this.executableCode = new StringBuilder();
    }

    // ============================================================================================
    // Manejo de Operandos
    // ============================================================================================

    public String popOperand() {
        return operands.pop();
    }

    // ============================================================================================

    public void pushOperand(String operand) {
        operands.push(operand);
    }

    // ============================================================================================
    // Manejo del Programa
    // ============================================================================================

    public void startProgram() {
        this.startBlock(Dumper.getProgramName());
        this.increaseIndentation();
    }

    // ============================================================================================

    public void endProgram() {

        StringBuilder program = this.blockStack.peek().code();
        program.insert(0, Indenter.indent(Dumper.dumpEntryPoint(), INDENTATION));
        this.decreaseIndentation();
        this.addCode(")");

        this.endBlock();
    }

    // ============================================================================================
    // Manejo de los Bloques
    // ============================================================================================

    public void startBlock(String scope) {
        this.blockStack.push(new Block(scope, new StringBuilder(), new StringBuilder(INDENTATION)));
    }

    // ============================================================================================

    public void endBlock() {

        String blockVariables = Indenter
                .indent(Indenter.indent(Dumper.dumpBlockVariables(this.getCurrentScope()), INDENTATION), INDENTATION);

        String searchString = INDENTATION + INDENTATION + "<local_variables>";
        int index = this.blockStack.peek().code().indexOf(searchString);

        if (index != -1) {
            this.blockStack.peek().code().replace(index, index + searchString.length(),
                    String.format("%s", blockVariables));
        }

        this.executableCode.append(this.blockStack.pop().code());
        this.executableCode.append("\n");
    }

    // ============================================================================================
    // Manejo del Código
    // ============================================================================================

    public void addCode(String code) {
        this.blockStack.peek().code()// .append("\n")
                .append(Indenter.indent(code, this.blockStack.peek().indentation().toString()));
    }

    // ============================================================================================

    public void addCode(StringBuilder code) {
        this.addCode(code.toString());
    }

    // ============================================================================================
    // Manejo de Importaciones
    // ============================================================================================

    public void addImport(String importCode) {

        if (!this.imports.toString().contains(importCode)) {
            this.imports.append(importCode).append("\n");
        }
    }

    // ============================================================================================
    // Manejo de la Indentación
    // ============================================================================================

    public void increaseIndentation() {
        this.blockStack.peek().indentation().append(INDENTATION);
    }

    // ============================================================================================

    public void decreaseIndentation() {
        this.blockStack.peek().indentation()
                .setLength(this.blockStack.peek().indentation().length() - INDENTATION.length());
    }

    // ============================================================================================
    // Métodos Auxiliares
    // ============================================================================================

    public String getCurrentScope() {
        SymbolTable symbolTable = SymbolTable.getInstance();
        return this.blockStack.peek().scope().
            replaceFirst("main",symbolTable.getProgramName());
    }

    // ============================================================================================

    // Este método solo tiene fines estéticos.
    public void removeLastLine() {

        StringBuilder code = this.blockStack.peek().code();

        int lastNewline = code.lastIndexOf("\n");
        if (lastNewline == -1) {
            code.setLength(0); // No hay saltos de línea: borrar todo.
        } else {
            code.setLength(lastNewline); // Cortar desde el salto.
        }
    }

    // ============================================================================================
    // Generación del Programa Final
    // ============================================================================================

    public String getProgram() {

        StringBuilder code = new StringBuilder();

        // El volcado de las variables globales se hace a lo último, ya que, durante la
        // generación del código assembler, se generan variables auxiliares que también
        // deben ser agregadas.

        // Si hay al menos una impresión, se debe importar el módulo de impresiones.
        code.append("(module \n");

        if (!imports.isEmpty()) {
            code.append("\n").append(INDENTATION + ";; Importación de recursos.").append("\n");
            code.append(Indenter.indent(imports, INDENTATION));
            code.append("\n").append(INDENTATION + "(import \"js\" \"mem\" (memory 1))").append("\n");
        }

        String stringsSection = Dumper.dumpStrings();
        /**
         * Puede ser que haya un string en la tabla de símbolos por el análisis léxico,
         * pero que no aparezca en la polaca por aparecer en un caso de error.
         * 
         * En tal caso, no habrá instrucciones que usen el string y, por lo tanto,
         * no habrá importaciones. En ese caso, los string no deben dumpearse.
         */
        if (!imports.isEmpty() && !stringsSection.isBlank()) {
            code.append("\n").append(stringsSection);
        }

        if (!executableCode.isEmpty()) {
            code.append("\n").append(executableCode);
        }

        return code.append(")").toString();

    }

    // ============================================================================================
    // Estructuras Internas
    // ============================================================================================

    private record Block(String scope, StringBuilder code, StringBuilder indentation) {

    }
}
