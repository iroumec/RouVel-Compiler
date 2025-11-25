package assembler.operators.implementations.arithmetic;

import assembler.CodeRepository;

public final class RuntimeControlsManager {

    private static final int MAX_UINT = 65535;

    // ============================================================================================

    private static boolean zeroDividendCheckerAdded = false;
    private static boolean integerOverflowCheckerAdded = false;
    private static boolean integerNegativeSubtractionCheckerAdded = false;

    // ============================================================================================

    public static void addIntegerOverflowChecker(CodeRepository repository) {

        repository.addImport("(import \"errors\" \"overflowException\" (tag $overflowException (param i32 i32)))");

        if (!integerOverflowCheckerAdded) {

            repository.startBlock("integer-overflow-checker");
            repository.addCode("""
                    ;; Chequeo de overflow de enteros.
                    (func $integer-overflow-checker
                        (param $value i32)

                        (block $evaluate
                            ;; ¿Es el resultado mayor al máximo entero?
                            local.get $value
                            i32.const %s
                            i32.gt_s

                            br_if $evaluate ;; Si es mayor al máximo entero, salta a las instrucciones de error.
                            return ;; Si es menor o igual, vuelve a la funcion llamadora.
                        )

                        local.get $value
                        i32.const %s
                        throw $overflowException
                    )
                    """.formatted(MAX_UINT, MAX_UINT));

            repository.endBlock();
            integerOverflowCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $integer-overflow-checker");
        repository.addCode("\n");
    }

    // ============================================================================================

    public static void addZeroDividendChecker(CodeRepository repository) {

        repository.addImport("(import \"errors\" \"divideByZeroException\" (tag $divideByZeroException))");

        if (!zeroDividendCheckerAdded) {

            repository.startBlock("zero-dividend-checker");
            repository.addCode("""
                    ;; Chequeo de división por cero.
                    (func $zero-dividend-checker
                        (param $value i32)

                        (block $evaluate
                            ;; ¿Es el denominador igual a 0?
                            local.get $value
                            i32.const 0
                            i32.eq

                            br_if $evaluate ;; Si es cero, salta a las instrucciones de error.
                            return ;; Si es distinto de cero, vuelve a la función llamadora.
                        )

                        throw $divideByZeroException
                    )
                    """);
            repository.endBlock();
            zeroDividendCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $zero-dividend-checker");
        repository.addCode("\n");
    }

    // ============================================================================================

    public static void addIntegerNegativeSubtractionChecker(CodeRepository repository) {

        repository
                .addImport("(import \"errors\" \"negativeSubtractionException\" (tag $negativeSubtractionException))");

        if (!integerNegativeSubtractionCheckerAdded) {

            repository.startBlock("integer-negative-subtraction-checker");
            repository.addCode("""
                    ;; Chequeo de resta negativa.
                    ;; Si compara si el primer operando es menor al segundo.
                    ;; "$value" = primer operando - segundo operando.
                    (func $integer-negative-subtraction-checker
                        (param $value i32)

                        (block $evaluate
                            ;; ¿Es el resultado menor a cero (negativo)?
                            local.get $value
                            i32.const 0
                            i32.lt_s

                            br_if $evaluate ;; Si es menor a cero, salta a las instrucciones de error.
                            return ;; Si es mayor o igual a cero, vuelve a la función llamadora.
                        )

                        throw $negativeSubtractionException
                    )
                    """);
            repository.endBlock();
            integerNegativeSubtractionCheckerAdded = true;
        }

        repository.addCode("\n");
        repository.addCode("call $integer-negative-subtraction-checker");
        repository.addCode("\n");
    }
}
