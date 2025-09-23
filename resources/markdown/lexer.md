# Anaizador Léxico

## Autómata Finito

![Finite Automatom](../diagrams/finiteAutomatom.png)

Quisimos adecuarnos a la convención utilizado para la cátedra y, si bien está buena para ejemplos concretos, en un grafo grande puede ser algo tedioso. Por eso, definimos nuestra convención como sigue:

Se define:

- `L`: conjunto de todos los carácteres correspondientes a letras mayúsculas.
- `l`: conjunto de todos los carácteres correspondientes a letras minúsculas.
- `d`: conjunto de carácteres correspondientes a dígitos (0 a 9).
- Los carácteres específicos serán indicados entre comillas simples (" y "), exceptuando por el carácter correspondiente a estas ("), que será indicado en solitario, ya que indicarlo entre comillas afecta a la claridad del autómata (""").
- `s`: indica un espacio en blanco.
- `t`: indica una tabulación.
- `n`: indica un salto de línea.
- `P`: conjunto de símbolos monocarácter. `P = {'+', '\*', '/', '(', ')', '{', '}', ';', ','}`.
- `Otro`: representa a un carácter que no está comprendido en los demás arcos.

Además, se definen las siguientes acciones semánticas (AS), en orden alfabérico:

- `FixedTokenFinalizer` (FTF): detecta el tipo de token de palabras reservadas y operadores (_tokens_ a los que les corresponde un único lexema) y setea el tipo de _token_ correspondiente de acuerdo a la detección. En caso de no ser válida la palabra reservada, levantará un error léxico: `UnknownToken`.
- `FloatChecker` (FC): valida que el flotante esté dentro del rango de representación válido y mejora su legibilidad (ver Decisiones/Flotantes/Legibilidad).
- `IdentifierLengthChecker` (ILC): valida que el identificador se halle en el rango de los 20 caracteres. En otro caso, lo trunca, presentando una _warning_.
- `LexemaAppender` (LA): agrega el último carácter leído al string del lexema.
- `LexemaInitializer` (LI): inicializa el string del lexema, agregando el carácter leido.
- `NewLineDetected` (NLD): incrementa el contador de números de línea del programa.
- `ReturnCharacterToEntry` (RCE): decrementa la referencia al siguiente caracter a leer (devolviendo así el último caracter leído a la entrada).
- `UintChecker` (UIC): se eliminan los trailing zeros, se verifica que el número está dentro del rango de uint. Si no, entonces se le da el valor máximo del rango.
- `VariableTokenFinalizer` (VTF): detecta el tipo de token de _tokens_ a los que les corresponde más de un lexema (identificadores, constantes y cadenas) y setea el tipo de _token_ correspondiente de acuerdo a la detección.

## Matriz de Transición de Estados

Decisiones de diseño e implementación.

- Diagrama de transición de estados.
- Errores léxicos considerados, describiendo la forma de tratar cada uno.
  Para implementaciones que contemplen el uso de herramienta tipo YACC
- Matriz de transición de estados,
- Descripción del mecanismo empleado para implementar la matriz de transición de estados y la matriz de acciones
  semánticas
- Lista de acciones semánticas asociadas a las transiciones del autómata del Analizador Léxico, con una breve
  descripción de cada una.

## Decisiones

### Autíomata Autoexplicativo

Decidimos minimizar la cantidad de acciones semánticas a aplicar a costa de aumentar el número de estados en el autómata finito. De esta forma:

- El autómata se autoexplica. No es necesario recurrir al código constantemente para entenderlo.
- Se facilita el debugging, ya que se minimiza el código.

Debido a ello, es posible notar en el autómata existen arcos, como `1 -> 2` y `2 -> F`, en los que el carácter para realizar la transición es explícito. Una alternativa sería una única transición a un estado. Sin embargo, como se mencionó anteriormente, esto implicaría la aplicación y creación de un mayor número de acciones semánticas al mismo tiempo que sería necesario recurrir al código con más frecuencia para entender el comportamiento del analizador.

### Reglas de Desambiguación

En el reconocimiento de varios operadores juntos, se decidió optar por una regla de desambiguación de lectura a partir de la izquierda.

Por ejemplo, de detectarse en el código `<==`, los _tokens_ detectados podrían ser `<=` y `=` o, `<` y `==`. De acuerdo a la regla de desambiguación propuesta, la primera opción es la considerada por el analizador léxico.

Esta regla no solo aplica a operadores mezclados, sino también a posibles confusiones entre constantes. Por ejemplo, de recibir algo como `31.2UI`, no es del todo claro si el usuario buscaba escribir un entero o un flotante. Sin embargo, aplicando la desambiguación, este sería interpretado como una constante flotante `31.2` y un identificador `UI`.

### Flotantes

Los flotantes son, sin duda.

Se considero que:

----DUDAS----
-79.F ¿Número 79. y luego identificador F? O ¿intento fallido de flotante?

0.0UI ¿Número flotante 0. y numero UINT 0UI? Lo toma como flotante 0.0

010201101.F-00000.0 Detecta dos tokens, dos flotantes, siendo el segundo ".0". ¿Debería dar error porque se intenta poner un flotante como exponente de otro flotante o está bien que identifique dos flotantes distintos?

#### Mejoras de Legibilidad

Con motivos de legibilidad, las constantes flotantes cuya parte entera o flotante se encuentre faltante son completadas. Es decir, en caso de identificar una constante flotante escrita por el usuario como `0.`, esta se almacenará en la tabla de símbolos como `0.0`.

Adicionalmente, de detectar un flotante que puede ser escrito de forma más compacta en notación científica, por ejemplo, `0.000000000001`, se hará la transformación a dicha notación.

Estando en el estado 1 es distinto el error si se quiere escribir un UINT a si se quiere escribir un float. Pero no hay forma de darse cuenta.

### Comentarios

De venir un único `#`, este se descarta. Como consecuencia de esto, de corresponder a un comentario mal escrito, se

Fueron consideradas otras alternativas, como: contar los `#` que siguen y determinar si fue un intento de comentario o un error. Sin embargo, el analizador léxico tendrá que leer todo el archivo por adelantado y, además, podría haber `#` dentro de los propios comentarios o haber varios comentarios mal escritos. Por eso, ante la dificultad de poder determinar la intención del usuario, se decidió descartar el carácter y

En el peor caso, si fue un intento de comentario, se considerará desde el fin de este hasta el inicio válido de otro comentariodesde el fin del comentario. En el mejor de los casos (el carácter se escribió como un error), al ser este descartado, se analizará el código como si nunca hubiese sido escrito.

En InvalidAssignmentOperator podría asumirse que, si se puso :, se quiso realizar una asignación.

---> No vamos a hacernos casos de este error.

## Dudas

- ¿Muchas AS en un mismo arco?
- ¿Escalable o fijo y adecuado al problema?

TokenType identifica todos los tipos de tokens.

El sintáctico, además de poder acceder al token, debe poder acceder a la línea. El semántico ya no va a necesitar el número de línea.
