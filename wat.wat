(module 

    ;; Importación de funciones de impresión.
    (import "console" "log_i32" (func $console_log_i32 (param i32)))
    (import "console" "log_f32" (func $console_log_f32 (param f32)))
    (import "console" "log_string" (func $console_log_string (param i32 i32)))

    (import "js" "mem" (memory 1))

    (data (i32.const 0) "Hola")

    ;; Punto de entrada del programa. 
    (func (export "main") 

        i32.const 0 
        i32.const 4 
        call $console_log_string 

    )
)