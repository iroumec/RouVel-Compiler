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

---

## No se muestra error de falta de punto y coma

declaracion_variable
: UINT lista_variables ';'
{ notifyDetection("Declaración de variables."); }

    // |========================= REGLAS DE ERROR =========================| //
    | UINT ID error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT lista_variables error
        {
            notifyError("La declaración de variables debe terminar con ';'.");
        }
    | UINT error
        {
            notifyError("Declaración de variables inválida.");
        }
    | UINT variable DASIG constante ';'
        {
            notifyError("La declaración de variables y la asignación de un valor a estas debe realizarse en dos sentencias separadas.");
        }
    ;

lista_variables
: ID ',' ID // Remplacé "ID" por "ID ',' ID" y agregué ID arriba. No sé por qué, pero ahora detecta bien la falta de ';' en sentencias como "uint A"
| lista_variables ',' ID
{ $$ = $3; }
// ============================= //
// PATRONES DE ERROR ESPECÍFICOS //
// ============================= //
| lista_variables ID
{
notifyError(String.format(
"Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
$1, $2));
}
| ID ID
{
notifyError(String.format(
"Se encontraron dos variables juntas sin separación. Inserte una ',' entre '%s' y '%s'.",
$1, $2));
}
;

LOS PEGO ASÍ NOMÁS AHORA. DESPUÉS LOS DETALLO.

---

asignacion_simple
: variable DASIG expresion ';'  
 { notifyDetection("Asignación simple."); }

    // |========================= REGLAS DE ERROR =========================| //

    | variable DASIG expresion error
        { notifyError("Las asignaciones simples deben terminar con ';'."); }

    | variable error expresion ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }

    | variable expresion ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
    ;

El problema está en que, al estar en termino y realizar un lookahead, ve print, por lo que entrá en modo error e intenta reducir. Pero en la pila tiene variable DASIG termino. Aún no redujo a expresión. Por lo que tenemos que obligarlo a que lo haga.

La solución es:

asignacion_simple
: variable DASIG expresion ';'  
 { notifyDetection("Asignación simple."); }

    // |========================= REGLAS DE ERROR =========================| //

    | variable DASIG termino error
        { notifyError("Las asignaciones simples deben terminar con ';'."); }

    | variable error expresion ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }

    | variable expresion ';'
        { notifyError("Error en asignación simple. Se esperaba un ':=' entre la variable y la expresión."); }
    ;

Con esto, a algo como: "I := 1UI" marca la falta de punto y coma. También funciona para "I := 1UI \* 4UI". Al igual que si se pone /.

Pero no funciona para cosas como: "I := 1UI + 4 UI". Para eso tuvo que agregarse:

Algo así (descartando el cambio anterior), sirve para detectar siempre el ';' faltante:

```sh
    expresion
        : termino token_fin_expresion { readLastTokenAgain(); }
        | expresion operador_suma termino token_fin_expresion { readLastTokenAgain(); }

        // |========================= REGLAS DE ERROR =========================| //

        | expresion operador_suma error
            {
                notifyError("Falta de operando en expresión.");
            }
        | expresion termino_simple
            {
                notifyError(String.format(
                    "Falta de operador entre operandos %s y %s.",
                    $1, $2)
                );
            }
        ;

    token_fin_expresion
        : token_inicio_sentencia
        | ')'
        | ';'
        | '}'
        | comparador
        ;

```

Pero se rompen sentencias como PRINT("Hola");

---

Los admisibles tienen el objetivo de mostrar la mayor cantidad de errores posible y únicamente mostrar la detección si la sentencia es totalmente correcta.

---

Principio de la regla: normal y erróneo. En otro lugar: normal y admisible.

---

Si la posibilidad de error es vacío, como en el nombre de la función, no puede separarse en admisible y normal.

---

El vacío al final no sirve para desambiguar.

---

En caso de que falte un punto y coma al final de una sentencia, se buscará otro punto y coma. De no hallarse, el error subirá hasta ser tratado por una regla superior.

// Solucionado. El problema es el vacío al final pareciera... Tipo, poner una opción con algo y una opción sin algo. Hay que poner sí o sí error

---

// TODO: agregar error si aparece sentencia declarativa en el cuerpo de la sentencia if.

```

```

java -jar rouvel-compiler.jar resources/testFiles/a.uki
