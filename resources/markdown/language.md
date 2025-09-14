# Lenguaje

## Características Generales

### Identificadores

Cuyos nombres pueden tener hasta 20 caracteres de longitud. El primer caracter sólo puede ser una letra, y el resto pueden ser letras, dígitos y “%”. Los identificadores con longitud mayor serán truncados y esto se informará como Warning. Las letras utilizadas en los nombres de identificador sólo pueden ser mayúsculas.

### Operadores

Operadores aritméticos: “+”, “-” , “\*”, “/”.

Operadores de asignación: “=” , “:=”

Comparadores: “>=”, “<=”, “>”, “<”, “==”, “=!”

Otros símbolos: “(”, “)”, “{”, “}”, ”\_”, “;” y “->”

### Palabras Reservadas

Estas deben escribirse con minúscula.

If, else, endif, print, return

### Descarte

El [analizador léxico](lexer.md) elimina de la entrada (reconoce, pero no informa como _tokens_ al Analizador Sintáctico), los siguientes elementos:

- Comentarios correspondientes al tema particular de cada grupo.

- Caracteres en blanco, tabulaciones y saltos de línea, que pueden aparecer en cualquier lugar de una sentencia.

## Temas Asignados

Las características del lenguaje se corresponderse con los temas pertenecientes al conjunto {2, 5, 7, 14, 17, 22, 26, 27, 30, 33}, los cuales se detallan a continuación:

### Tema 2

Enteros sin signo (16 bits): Constantes con valores entre 0 y 216 – 1 que se escriben como una secuencia de
dígitos seguidos del sufijo UI.

Se debe incorporar a la lista de palabras reservadas la palabra int.

### Tema 5

Punto Flotante de 32 bits: Números reales con signo y parte exponencial. La parte exponencial puede estar
ausente. Si está presente, el exponente comienza con la letra “F” (mayúscula) seguido del signo del exponente
que es obligatorio, y luego el valor del exponente.
El ‘.’ es obligatorio. Las partes entera o decimal pueden estar ausentes. Si está ausente la parte entera, debe
estar presente la parte decimal. Si está ausente la parte decimal, debe estar presente la parte entera.
Ejemplos válidos: 1. .6 -1.2 3. F–5 2.0F+34 2.5F-1 15.0 0.0 .2F+10
Considerar el rango 1.17549435F-38 < x < 3.40282347F+38 U
-3.40282347F+38 < x < -1.17549435F-38 U 0.0
Se debe incorporar a la lista de palabras reservadas la palabra float.

### Tema 7

Cadenas de 1 línea: Cadenas de caracteres delimitadas por comillas dobles. Estas cadenas no pueden ocupar
más de una línea).
Ejemplo: “¡Hola mundo !”

### Tema 14

Incorporar a la lista de palabras reservadas, las palabras do y while.

### Tema 17

### Tema 22

### Tema 26

### Tema 27

### Tema 30

### Tema 33

Comentarios multilínea: Comentarios que comiencen con “##” y terminen con “##” (estos comentarios pueden
ocupar más de una línea).
