# Conocimientos Adquiridos

    | lista_variables ',' ID
        { $$ = $3; }
    | lista_variables ID
        {
            notifyError(String.format(
                "Se encontraron dos variables juntas sin una coma de separación. Inserte una ',' entre '%s' y '%s'.",
                $1, $2));
        }
    // {$$ = $3}Le dice a Yacc que, cuando reduzca usando esta regla, el valor de la nueva lista_variables
    // que coloque en la pila no sea el de la lista vieja ($1, por defecto siempre hace $$ = $1),
    // sino el valor del ID que acaba de leer ($3)
    // Esto es útil para mostrar errores correctamente. Si no se pone, en una sentencia como:
    // uint A, B C
    // Diría que la coma falta entre A y C.

También, se pide que se chequee que las asignaciones puedan tener un menor número de elementos del lado izquierdo (tema 17). A eso no lo podemos hacer con la gramática, ¿verdad? Lo haríamos en la siguiente etapa, en el análisis semántico.
