#!/bin/bash

DIR="$(dirname "$0")/code"
cd "$DIR" || exit 1

# Se compila y ejecuta el programa.
# Luego, se limpian todos los archivos de ejecución.
# -q es para que Maven no imprima tanta información en la consola
# y pueda verse la salida correctamente.
mvn -q compile exec:java

# Con el siguiente comando es posible limpiar los archivos generados de la compilación.
# mvn -q clean