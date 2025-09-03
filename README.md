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

AS1: LexemaInitializer:
-Inicializa el string del lexema y agrega caracter leido.

AS2: LexemaAppender:
-Agrega caracter leído al string del lexema.

AS3: LexemaFinalizer:
-Se obtiene el lexema y el TokenType (será nulo para todo aquello que no sea palabra reservada o literal).
-Si el TokenType era nulo y el lexema no es vacío:
--Se obtiene el TokenType detectado (es el caso de identificadores, constantes y cadenas de caracteres).
--Si el TokenType es vacío:
---Error (Se toma como palabra reservada inexistente)
-Se da de alta en la tabla de símbolos.

AS4: LexemaIdentifier:
-Se obtiene el último caracter leído (que es el primero del lexema).
-Si es letra mayúscula, define el TokenType como ID (identificador).
-Si es dígito o punto, define el TokenType como CTE (constante).
-Si es comillas, define el TokenType como STR (cadena de caracteres).

ASN: NewLineDetected:
-Incrementa el número de linea.

ASR: ReturnCharacterToEntry:
-Decrementa la referencia al siguiente caracter a leer (devolviendo así el último caracter leído a la entrada).

ASUI: UintChecker:
-Se eliminan los trailing zeros.
-Se verifica que el número está dentro del rango de uint.
--Si no, entonces se le da el valor máximo del rango.

ASF: FloatChecker:
-?

LOS ARCHIVOS .UKI DEBEN ESTAR CODIFICADOS CON UTF-8. Si se codifican con UTF-8 with BOM, da error.

----DUDAS----
-79.F ¿Número 79. y luego identificador F? O ¿intento fallido de flotante?

0.0UI ¿Número flotante 0. y numero UINT 0UI? Lo toma como flotante 0.0

010201101.F-00000.0 Detecta dos tokens, dos flotantes, siendo el segundo ".0". ¿Debería dar error porque se intenta poner un flotante como exponente de otro flotante o está bien que identifique dos flotantes distintos?

Cuando nuestro programa guarda las constantes como 0., las guarda como 0.0, para mayor legibilidad. ¿Está bien que haga eso? Si encuentra un número como 0.000000000001, ¿está bien que lo guarde en notación científica? ¿Tenemos esas libertades?

Estando en el estado 1 es distinto el error si se quiere escribir un UINT a si se quiere escribir un float. Pero no hay forma de darse cuenta.
