# Token Error

EXPLICACIÓN DEL TOKEN DE ERROR

Definición: token especial que representa cualquier cosa que en ese punto no cumpla ninguna de las alternativas válidas.

Si se define el error con un token de sincronización, por ejemplo, "error '}'", el parser consumirá todos los tokens habidos, incluido '}'
antes de reducir por la regla. Lo mismo sucede si se usa un no-terminal, por ejemplo, "error sentencia". No hay forma de recuperar el token o
no terminal consumido por el error, por lo que no puede usarse para detectar luego otra regla. Esto es muy importante ya que complica el manejo
de errores.

En caso de que el token a recuperarse quiera volver a leerse, se proporciona un método que puede ser invocado con dicho propósito: "readLastTokenAgain()".
ESTE SOLO ES VÁLIDO PARA LA LECTURA REPETIDA DE TOKENS. NO SIRVE PARA NO-TERMINALES.

De producirse un error, el error sube hasta que una regla superior lo intercepta. En tal caso de que una regla levante un error que deba ser interceptada
por una regla de orden superior, se usa la notación "@LevantaError" para hacer esto explícito y claro. Estas reglas se van a caracterizar por ser un
posible punto de fallo y no permitir, debido a la aparición de conflictos, la posibilidad de vacío (lambda). Si la regla de un orden superior no intercepta
el error, este pasara a la regla de dos órdenes superiores. Y así sucesivamente hasta que una regla intercepte el error o el parser se detenga.

De forma similar, se usa la notación "@InterceptaError" para especificar que una regla usa un token de error con la finalidad de interceptar un error
de un no-terminal que utiliza; y "@TrasladaError" si, en lugar de interceptarlo, se lo traslada a la regla de un orden superior.

Es importante tener en cuenta que NO CUALQUIER REGLA PUEDE INTERCEPTAR UN ERROR. La regla, además de usar un no-terminal que levante o traslade un error,
este no terminal, en la regla, al remplazarse por el token de error, debe estar procedido de otro no-terminal, de forma que haya un punto de sincronización.
En caso contrario, en caso de una regla de la forma "\<no-terminal1\> \<no-terminal2\> ... error", no se intercepta el error, sino que se traslada a una regla de
orden superior. Es decir, SÍ O SÍ, LUEGO DE UN TOKEN DE ERROR, DEBE HABER UN TOKEN O NO-TERMINAL QUE FUNCIONE COMO PUNTO DE SINCRONIZACIÓN (no olvidar lo
mencionado en el segundo párrago).

ATENCIÓN: lo último no es siempre cierto. Existen veces donde el error de igual forma sí se intercepta. Aún no sé por qué a veces sí y a veces no.
NUEVA HIPÓTESIS: O quizás sí. Pero hay una peculiaridad: al irse trasladando el error, las acciones semánticas no se ejecutan hasta que el error no es
interceptado. Si ocurren otros errores (con otras acciones semánticas asociadas en el medio), únicamente la primera acción semántica se ejecuta.
Por lo mismo, es altamente recomendable interceptar los errores lo antes posible, ya que no se acumulan. ESTOY SEGURO DE QUE ESTO ES LO QUE PASA.

Básicamente, es como si una regla, al usar un no-terminal que levanta o traslada un error, estuviese firmando un contrato que dice: "Me comprometo a
interceptar o trasladar el error".

Mucho cuidado con las reglas glotonas. Si únicamente se tiene una regla de error "... error \<no-terminal\> ...", el parser no va a parar de descartar todo
hasta hallar dicho no-terminal.

Si en una regla está la posibilidad de vacío, "lambda", jamás va a producirse un error en dicha regla, porque, de venir algo que no cumpla
con ninguna de las alternativas válidas, va a reducir por la regla vacía.

Las reglas vacías son altamente propensas a conflictos shift/reduce.

Si el error está al comienzo de la regla, no se puede obtener su valor a través de $n. Si está en alguna otra parte, sí se puede.

YA SÉ POR QUÉ EL ERROR SUBE: SUBE EN BUSCA DE UN PUNTO DE SINCRONIZACIÓN. POR ESO, SI EL TOKEN ERROR ESTÁ AL FINAL, SE TRASLADA A UNA REGLA SUPERIOR.
POR QUE, SI NO, NO SABE HASTA CUÁNDO DESCARTAR.

CUIDADO CON QUE EL PARSER PUEDE INTENTAR APLICAR DOS REDUCCIONES DISTINTAS Y, POR LO TANTO, SI PASA POR UN MISMO CAMINO, MOSTRAR DOS VECES UN MISMO MENSAJE.
