#!/bin/bash

# Directorio desde el que se llamó al script
CALL_DIR="$PWD"

# Carpeta donde está el script
SCRIPT_DIR="$(dirname "$0")"

cd "$SCRIPT_DIR" || { echo "No se pudo cambiar a $SCRIPT_DIR"; exit 1; }

./yacc -J -v gramatica.y

cd "$CALL_DIR" || exit 1