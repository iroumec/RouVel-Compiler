(module 

    ;; Punto de entrada del programa. 
    (func (export "main") 

        (func $F1 
            (param $X i32) 
            (result i32) 
        
            ;; Copia del valor del argumento en el parámetro X. 
            local.set $X 
    
            local.get $X 
            i32.const 10 
            i32.lt_u
    
            (if 
            (then 
    
                local.get $X 
                return 
    
            )
            )
    
            i32.const 15 
            return 
    
        )
    
        ;; Pasaje a parámetro X 
        f32.const 2.0 
        i32.trunc_f32_u 
    
        call $F1 

    )
)