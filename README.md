# TPE-Compiler

Trabajo práctico de cursada para la materia Compiladores e Intérpretes.

Posible nombre: RouVel Compiler.

## Decisiones

Decidimos minimizar la cantidad de acciones semánticas a aplicar a costa de aumentar el número de estados en el autómata finito. De esta forma:

- El autómata se autoexplica. No es necesario recurrir al código constantemente para entenderlo.
- Se facilita el debugging, ya que se minimiza el código.

Se define:

- C: conjunto de todos los caracteres.
- L: conjunto de todos los carácteres correspondientes a letras mayúsculas.
- l: conjunto de todos los carácteres correspondientes a letras minúsculas.
- d: conjunto de carácteres correspondientes a dígitos (0 a 9).
- Los carácteres específicos serán indicados entre comillas simples (" y "), exceptuando por el carácter correspondiente a estas ("), que será indicado en solitario, ya que indicarlo entre comillas afecta a la claridad del autómata (""").
- s: indica un espacio en blanco.
- t: indica una tabulación.
- n: indica un salto de línea.

¿Qué hacemos si viene un <==? Es ambiguo. Tomamos como vaya llegando.

    // No es necesario contemplar el salto de línea porque, al
    // retrocederse en el carácter, se vuelve a leer.

Recvisar qué pasa si no se agrega UI a un número.

Definiciones de acciones semánticas:

AS1: Inicializador de lexema.
Inicializa string y agrega caracter leido.

AS2: LexemaAppender.
Agrega caracter leído al string del lexema.

LOS ARCHIVOS .UKI DEBEN ESTAR CODIFICADOS CON UTF-8. Si se codifican con UTF-8 with BOM, da error.
