package assembler;

import java.util.Deque;
import java.util.ArrayDeque;

public final class CodeRepository {

    private final static String INDENTATION = "    ";

    // --------------------------------------------------------------------------------------------

    // StringBuilder imports;
    Deque<String> operands;
    StringBuilder executableCode;

    // --------------------------------------------------------------------------------------------

    Deque<Block> blockStack;

    // --------------------------------------------------------------------------------------------

    public CodeRepository() {
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

    // --------------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------------

    public void endProgram() {

        StringBuilder program = this.blockStack.peek().code();
        this.decreaseIndentation();
        program.insert(0, Indenter.indent(Dumper.dumpGlobalVariables(), INDENTATION));
        program.insert(0, "\n" + Indenter.indent(Dumper.dumpEntryPoint(), INDENTATION));
        this.removeLastLine(); // Fines estéticos.
        this.addCode(")");

        this.endBlock();
    }

    // ============================================================================================
    // Manejo de los Bloques
    // ============================================================================================

    public void startBlock(String scope) {
        this.blockStack.push(new Block(scope, new StringBuilder(), new StringBuilder(INDENTATION)));
    }

    // --------------------------------------------------------------------------------------------

    public void endBlock() {
        this.executableCode.append(this.blockStack.pop().code());
    }

    // ============================================================================================
    // Manejo del Código
    // ============================================================================================

    public void addCode(String code) {
        this.blockStack.peek().code().append("\n")
                .append(Indenter.indent(code, this.blockStack.peek().indentation().toString()));
    }

    // --------------------------------------------------------------------------------------------

    public void addCode(StringBuilder code) {
        this.addCode(code.toString());
    }

    // ============================================================================================
    // Manejo de la Indentación
    // ============================================================================================

    public void increaseIndentation() {
        this.blockStack.peek().indentation().append(INDENTATION);
    }

    // --------------------------------------------------------------------------------------------

    public void decreaseIndentation() {
        this.blockStack.peek().indentation()
                .setLength(this.blockStack.peek().indentation().length() - INDENTATION.length());
    }

    // ============================================================================================
    // Métodos Auxiliares
    // ============================================================================================

    public String getCurrentScope() {
        return this.blockStack.peek().scope();
    }

    // --------------------------------------------------------------------------------------------

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
        if (executableCode.toString().contains("$console_log")) {
            code.append("""

                        ;; Importación de funciones de impresión.
                        (import "console" "log_i32" (func $console_log_i32 (param i32)))
                        (import "console" "log_f32" (func $console_log_f32 (param f32)))
                        (import "console" "log_string" (func $console_log_string (param i32 i32)))

                        (import "js" "mem" (memory 1))
                    """);
        }

        String stringsSection = Dumper.dumpStrings();
        if (!stringsSection.isBlank()) {
            code.append(stringsSection).append("\n");
        }

        if (!executableCode.isEmpty()) {
            code.append(executableCode).append("\n");
        }

        return code.append(")").toString();

    }

    // ============================================================================================
    // Estructuras Internas
    // ============================================================================================

    private record Block(String scope, StringBuilder code, StringBuilder indentation) {

    }
}
