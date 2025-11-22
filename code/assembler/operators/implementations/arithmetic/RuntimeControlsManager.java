package assembler.operators.implementations.arithmetic;

import assembler.CodeRepository;
import assembler.WebAssemblyExporter;
import common.Symbol;
import common.SymbolDirector;
import common.SymbolTable;

public final class RuntimeControlsManager {

    private static final int MAX_UINT = 65535;

    // ============================================================================================

    private static boolean zeroDividendCheckerAdded = false;
    private static boolean integerOverflowCheckerAdded = false;
    private static boolean integerNegativeSubtractionCheckerAdded = false;

    // ============================================================================================

    public static void addIntegerOverflowChecker(CodeRepository repository) {

        String message = "RUNTIME ERROR: Integer Overflow.";

        Symbol messageSymbol = SymbolDirector.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(messageSymbol);

        repository.addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))");

        if (!integerOverflowCheckerAdded) {

            repository.startBlock("integer-overflow-checker");
            repository.addCode("""
                    ;; Chequeo de overflow de enteros.
                    (func $integer-overflow-checker
                        (param $value i32)

                        ;; ¿Es el resultado mayor al máximo entero?
                        local.get $value
                        i32.const %s
                        i32.le_u
                        return ;; Si es menor al máximo entero, se sale de la función.

                        ;; -------- OVERFLOW --------
                        i32.const %s     ;; ptr
                        i32.const %d    ;; len
                        call $console_log_string
                        unreachable
                    )
                    """.formatted(MAX_UINT, messageSymbol.getValue(),
                    WebAssemblyExporter.getAppropiateMessageLength(message)));
            repository.endBlock();
            integerOverflowCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $integer-overflow-checker");
        repository.addCode("\n");
    }

    // ============================================================================================

    public static void addZeroDividendChecker(CodeRepository repository) {

        String message = "RUNTIME ERROR: Dividend is Zero.";

        Symbol messageSymbol = SymbolDirector.createNewString(message);
        SymbolTable.getInstance().addEntry(messageSymbol);

        repository.addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))");

        if (!zeroDividendCheckerAdded) {

            repository.startBlock("zero-dividend-checker");
            repository.addCode("""
                    ;; Chequeo de división por cero.
                    (func $zero-dividend-checker
                        (param $value i32)

                        ;; ¿Es el denominador igual a 0?
                        local.get $value
                        i32.const 0
                        i32.ne

                        return ;; Si no es cero, se sale.

                        ;; -------- ERROR --------
                        i32.const %s     ;; ptr
                        i32.const %d    ;; len
                        call $console_log_string
                        unreachable
                    ) ;; $continue
                    """.formatted(messageSymbol.getValue(),
                    WebAssemblyExporter.getAppropiateMessageLength(message)));
            repository.endBlock();
            zeroDividendCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $zero-dividend-checker");
        repository.addCode("\n");
    }

    // ============================================================================================

    public static void addIntegerNegativeSubtractionChecker(CodeRepository repository) {

        String message = "RUNTIME ERROR: Integer Negative Subtraction.";

        Symbol messageSymbol = SymbolDirector.createNewString(message);

        // Se agrega el string con el mensaje a la tabla de símbolos.
        SymbolTable.getInstance().addEntry(messageSymbol);

        repository.addImport("(import \"console\" \"log_string\" (func $console_log_string (param i32 i32)))");

        if (!integerNegativeSubtractionCheckerAdded) {

            repository.startBlock("integer-negative-subtraction-checker");
            repository.addCode("""
                    ;; Chequeo de resta negativa.
                    ;; Si compara si el primer operando es menor al segundo.
                    (func $integer-negative-subtraction-checker
                        (param $value i32)

                        local.get $value
                        i32.const 0
                        i32.ge_s

                        ;; De ser el valor >= 0,
                        ;; es válido.
                        return

                        ;; -------- ERROR --------
                        i32.const %s       ;; ptr
                        i32.const %d       ;; len
                        call $console_log_string
                        unreachable
                    )
                    """.formatted(
                    messageSymbol.getValue(),
                    WebAssemblyExporter.getAppropiateMessageLength(message)));
            repository.endBlock();
            integerNegativeSubtractionCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $integer-negative-subtraction-checker");
        repository.addCode("\n");
    }
}
