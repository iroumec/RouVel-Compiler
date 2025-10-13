#!/bin/bash

SCRIPT_DIR="$(dirname "$0")"

if [ -z "$1" ]; then
    echo "Se debe proporcionar el nombre de un archivo."
    exit 1
fi

echo "Realizando compilación..."

# Se ejecuta yacc.
YACC_SCRIPT="$SCRIPT_DIR/yacc/run-yacc.sh"
chmod +x "$YACC_SCRIPT"
"$YACC_SCRIPT"

# Se compila el programa.
JAR_SCRIPT="$SCRIPT_DIR/jar/build-jar.sh"
chmod +x "$JAR_SCRIPT"
# Redirección de la salida para que no se muestre en consola.
"$JAR_SCRIPT" > /dev/null 2>&1

# Se ejecuta el programa con el archivo pasado.
java -jar rouvel-compiler.jar "$1"