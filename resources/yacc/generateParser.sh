#!/bin/bash

# Directorio desde el que se llamó al script
CALL_DIR="$PWD"

# Carpeta donde está el script
SCRIPT_DIR="$(dirname "$0")"

cd "$SCRIPT_DIR" || { echo "No se pudo cambiar a $SCRIPT_DIR"; exit 1; }

./yacc -J -Jnoconstruct -Jnorun -v gramatica.y

# Se agrega "package parser;" al inicio de ambos archivos.
# Esto para que sea coherente con nuestra estructura de código.
sed -i '1ipackage parser;\n' Parser.java
sed -i '1ipackage parser;\n' ParserVal.java

# Se agregan las importaciones necesarias a Parser.java.
sed -i '2iimport lexer.Lexer;\nimport common.Token;' Parser.java

# Se mueven los arhcivos .java a la sección de código.
mv Parser.java "$CALL_DIR"/code/parser
mv ParserVal.java "$CALL_DIR"/code/parser

cd "$CALL_DIR" || exit 1