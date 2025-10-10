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

```sh
sudo usermod -aG docker $USER \
&& newgrp docker
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

java -jar rouvel-compiler.jar resources/testFiles/a.uki
