# Lenguaje

## Características Generales

### Identificadores

Cuyos nombres pueden tener hasta 20 caracteres de longitud. El primer caracter sólo puede ser una letra, y el resto pueden ser letras, dígitos y “%”. Los identificadores con longitud mayor serán truncados y esto se informará como Warning. Las letras utilizadas en los nombres de identificador sólo pueden ser mayúsculas.

### Operadores

Operadores aritméticos: `+`, `-`, `*` y `/`.

Operadores de asignación: `=` y `:=`.

Operadores de comparación: `>=`, `<=`, `>`, `<`, `==` y `=!`.

Otros símbolos: `(`, `)`, `{`, `}`, `_`, `,`, `;` y `->`.

### Palabras Reservadas

Estas deben escribirse con minúscula. Algunas de ellas son: `if`, `else`, `endif`, `print` y `return`. Las demás se introducirán con cada tema particular.

### Descarte

El [analizador léxico](lexer.md) elimina de la entrada (reconoce, pero no informa como _tokens_ al Analizador Sintáctico), los siguientes elementos:

- Comentarios correspondientes al tema particular de cada grupo.

- Caracteres en blanco, tabulaciones y saltos de línea, que pueden aparecer en cualquier lugar de una sentencia.

## Temas Asignados

Las características del lenguaje se corresponderse con los temas pertenecientes al conjunto {2, 5, 7, 14, 17, 22, 26, 27, 30, 33}, los cuales se detallan a continuación:

### Tema 2

_Enteros sin signo de 16 bits_.

Se deben incorporar al lenguaje constantes con valores entre 0 y 2 ＾ 16 – 1, que se escriben como una secuencia de dígitos seguidos del sufijo `UI`.

Se debe incorporar a la lista de palabras reservadas la palabra `uint`.

### Tema 5

_Punto flotante de 32 bits_.

Se deben incorporar al lenguaje números reales con signo y parte exponencial. La parte exponencial puede estar ausente. Si está presente, el exponente comienza con la letra “F” (mayúscula) seguido del signo del exponente
que es obligatorio, y luego el valor del exponente.

El ‘.’ es obligatorio. Solo una de las partes **puede** estar ausente en una misma constante. Es decir, si falta la parte entera, debe estar presente la decimal, y viceversa.

El rango a considerar es:

- 0.0
- U 1.17549435F-38 < x < 3.40282347F+38 U
- U 3.40282347F+38 < x < -1.17549435F-38

**No pueden declararse variables de este tipo. Únicamente constantes.**

### Tema 7

Cadenas de 1 línea: Cadenas de caracteres delimitadas por comillas dobles. Estas cadenas no pueden ocupar
más de una línea).
Ejemplo: “¡Hola mundo !”

### Tema 14 - **_do while_**

`do <bloque_de_sentencias_ejecutables> while ( <condicion> );`

Donde:

- `<condicion>` posee la misma definición que la condición de las sentencias IF.
- `<bloque_de_sentencias_ejecutables>` podrá contener una sentencia, o un grupo de sentencias ejecutables delimitados por **{}**.

Como consecuencia de esta estructura, se incorporaron a la lista de palabras reservadas las palabras `do` y `while`.

### Tema 17

_Asignaciones que pueden tener menor número de elementos del lado izquierdo_.

Se deben reconocer asignaciones múltiples, que permitan una lista de variables, sepradas por coma (",") del lado izquierdo de la asignación, y una lista de constantes separadas por coma (",") del lado derecho. El operador utilizado para la asignación será "=" y l número de constantes del lado izquierdo puede ser menor al de variables en el lado derecho.

### Tema 22

_Prefijado obligatorio_.

Se debe incorporar la posibilidad de utilizar, en cualqueir lugar donde pueda participar una variable, un prefijado que indicará la unidad a la que pertenece. Este debe escribirse como un identificador seguido de un "." precediendo al nombre de la variable: `ID.ID`.

Este prefijado se utilizará para indicar la unidad a la que pertenece la variable, en caso de que no esté declarada localmente. Su obligatoriedad y uso correcto se chequeará en la etapa 3 del TP.

### Tema 26

_Copia-Valor-Resultado_.

Se debe agregar a la lista de palabras reservadas la palabra `cvr`.

En la declaración de la función, antes de cada parámetro formal, se **podrá** indicar, mediante una palabra reservada, la semántica del pasaje de ese parámetro.

Los casos posibles son:

- `<parámetro_formal> <tipo> ID`, donde se usa la semántica por defecto.
- `<parámetro_formal> cvr <tipo> ID`, donde se usa la semántica de copia-valor-resultado.

### Tema 27

_Expresiones lambda en línea_.

Se debe incoporar al lenguaje la definición y uso de expresiones lambda que pueden ser usadas en línea. La sintáxis de esta es: `<parámetro><cuerpo><argumento>`.

Estas expresiones permiten un solo parámetro, que se escribirá entre paréntesis con la estructura `<tipo> ID`.

El cuerpo será un bloque de sentencias ejecutables delimitado por llaves.

El argumento podrá ser un identificador o una constante. Este debe ir entre paréntesis.

Ejemplo:

`(int A) { if (A > 1UI) print ("¡Hola!); } (3I)`

### Tema 30

_Conversiones implícitas_.

Se explicará y resolverá en trabajos prácticos 3 y 4.

### Tema 33

_Comentarios multilínea_.

Se debe incorporar al lenguaje la posibilidad de escribir comentarios que puedan ocupar más de una lista. Estos deben comenzar con `##` y, terminar con `##`.
