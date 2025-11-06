# Return

Un problema al que se enfrentó es la restricción de que todas las funciones deben tener al menos una sentencia de retorno.

El problema parece fácil de solucionar a simple vista: agregar sentencia de retorno como no-terminal en las reglas correspondientes al cuerpo de la función.

Sin embargo, una regla de ese estilo no permitiría cosas como la siguiente:

```uki
uint FUNC1(uint A) {
    if (A > X) {
        return(0UI);
    } else {
        return(1UI);
    }
}
```

Por lo tanto, se tuvo que pensar en otra estrategia. Además, de haber sentencias luego de una sentencia de retorno, se debería mostrar un mensaje informando "sentencias inalcanzables".

La solución: un booleano que indique si la función en la que ahora mismo se está ubicado tiene un retorno o no. ¿Cuándo este booleano se activaría?

- Se halla un retorno en el cuerpo principal de la función.
- Se halla un retorno en las dos ramas de un if-else.

En estos dos casos, se cumple siempre que la función retornará algo.

¿En qué casos una función es válida con respecto al retorno?

```uki
uint FUNC1(uint A) {
    ## ... ##
    return(3UI);
}
```

```uki
uint FUNC1(uint A) {
    ## ... ##
    if (A > X) {
        ## ... ##
        return(0UI);
    } else {
        ## ... ##
        return(1UI);
    }
    ## ... (Sentencias descartadas) ##
}
```

```uki
uint FUNC1(uint A) {
    ## ... ##
    if (A > X) {
        return(0UI);
    }
    ## ... ##
    return(1UI);
}
```

```uki
uint FUNC1(uint A) {
    ## ... ##
    if (A > X) {
        ## ... ##
        return(0UI);
    } else {
        ## ... ##
        if (X > A) {
            ## ... ##
            return(1UI);
        } else {
            ## ... ##
            return(2UI);
        }
        ## ... (Sentencias descartadas) ##
    }
}
```

Y similares.

¿Qué sucede si hay algo más luego del retorno? ¿Simplemente se descarta o se da un mensaje de error o _warning_?

Además, se debe pensar: ¿qué pasa si hay declaraciones de función anidadas? ¿La solución contempla esos casos?

Si se halla un `return`, se setea en `true` una bandera. Si, luego, se sale del ámbito de un if, esa bandera se setea en `false`. Si, por otro lado, se sale del ámbito de un `else`, y ese `else` tiene un `return`, la bandera no se modifica (si era `true`, hay un `return` en el `if` y, como también lo hay en el `else`, la función tiene un retorno válido).

Sin embargo, eso requiere saber dentró de qué tipo de ámbito se está metido, para lo cual debe crearse otra variable adicional a la bandera que se menciona antes. A considerar, esto solo sirve porque el if-else tienen dentro suyo sentencias ejecutables. En otro caso, no serviría.

Otra solución: llevar un contador de return que se necesitan. Al detectarse un if-else, en las que ambas ramas tienen un return, se decrementa la cantidad de return necesarios. Se requiere llevar registro de si el anterior if tuvo un return o no.

Problema: if-else anidados. La variable debe pisarse. Por lo tanto, debe encolarse la variable que indique si el if-else necesita un retorno.

Otra solución: incrementar en cada entrada de un if el número de return necesarios.
