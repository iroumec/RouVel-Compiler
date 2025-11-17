(module 

    ;; Importación de funciones de impresión.
    (import "console" "log_i32" (func $console_log_i32 (param i32)))
    (import "console" "log_f32" (func $console_log_f32 (param f32)))
    (import "console" "log_string" (func $console_log_string (param i32 i32)))

    (import "js" "mem" (memory 1))

    (data (i32.const 0) "Hello")
    (data (i32.const 1) "Maybe")
    (data (i32.const 2) "Forget it")
    (data (i32.const 3) "TRUE")
    (data (i32.const 4) "FALSE-START")
    (data (i32.const 5) "FALSE-TRUE")
    (data (i32.const 6) "FALSE-FALSE")
    (data (i32.const 7) "FALSE-END")
    (data (i32.const 8) "Exiting...")

    ;; Punto de entrada del programa. 
    (func (export "main") 
    
        ;; Variable globales.
        (local $X i32)

        i32.const 0 
        i32.const 5 
        call $console_log_string 
    
        local.get $X 
        i32.const 3 
        i32.gt_u
    
        i32.const 0 
        i32.const 5 
        call $console_log_string 
    
        i32.const 0 
        i32.const 9 
        call $console_log_string 
    
        i32.const 67 
        local.get $X 
        i32.gt_u
    
        i32.const 0 
        i32.const 4 
        call $console_log_string 
    
        i32.const 0 
        i32.const 11 
        call $console_log_string 
    
        local.get $X 
        i32.const 8 
        i32.gt_u
    
        i32.const 0 
        i32.const 10 
        call $console_log_string 
    
        i32.const 0 
        i32.const 11 
        call $console_log_string 
    
        i32.const 0 
        i32.const 9 
        call $console_log_string 
    
        i32.const 0 
        i32.const 10 
        call $console_log_string 

    )
)