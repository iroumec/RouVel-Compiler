#!/bin/bash

SCRIPT_DIR="$(dirname "$0")"

if [ -z "$1" ]; then
    echo "Se debe proporcionar el nombre de un archivo que esté ubicado en la carpeta \"testFiles\"."
    exit 1
fi

# Se ejecuta yacc.
YACC_SCRIPT="$SCRIPT_DIR/yacc/run-yacc.sh"
chmod +x "$YACC_SCRIPT"
"$YACC_SCRIPT"

# Se compila el programa.
JAR_SCRIPT="$SCRIPT_DIR/jar/build-jar.sh"
chmod +x "$JAR_SCRIPT"
"$JAR_SCRIPT"
clear

# Se ejecuta el programa con el archivo pasado.
java -jar rouvel-compiler.jar "$PWD/resources/testFiles/$1"