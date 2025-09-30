#!/bin/bash

# Directorio desde el que se llamó al script.
CALL_DIR="$PWD"

# Carpeta donde está el script
SCRIPT_DIR="$(dirname "$0")"

OUT_DIR="$CALL_DIR/code/parser"

cd "$SCRIPT_DIR" || { echo "No se pudo cambiar a $SCRIPT_DIR"; exit 1; }

# "./yacc -V" para ver las opciones.
./yacc -J -Jnoconstruct -Jnorun -Jnodebug -Jfinal -v "$OUT_DIR/gramatica.y"

# Se agrega "package parser;" al inicio de ambos archivos.
# Esto para que sea coherente con nuestra estructura de código.
sed -i '1ipackage parser;\n' Parser.java
sed -i '1ipackage parser;\n' ParserVal.java

# Se agregan las importaciones necesarias a Parser.java.
sed -i '2iimport lexer.Lexer;\nimport common.Token;' Parser.java

# FIX: comentar typedef union que no sirve en Java.
# Abrir comentario antes de "typedef union".
sed -i '/^typedef union/{s/^/\/\*/}' Parser.java

# Cerrar comentario después de "} YYSTYPE;".
sed -i '/^} YYSTYPE;/{s/$/ \*\//}' Parser.java

# Se mueven los arhcivos .java a la sección de código.
mv Parser.java "$OUT_DIR"
mv ParserVal.java "$OUT_DIR"
mv y.output "$OUT_DIR"

cd "$CALL_DIR" || exit 1