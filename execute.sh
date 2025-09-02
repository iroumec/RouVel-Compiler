#!/bin/bash

DIR="$(dirname "$0")/src"
cd "$DIR" || exit 1

# Se compila el programa.
javac Main.java && java Main

# Terminada la ejecución, se buscan y eliminar todos los.class dentro del subdirectorio.
find "$DIR" -type f -name "*.class" -delete
