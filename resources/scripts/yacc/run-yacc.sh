#!/bin/bash

# Directorio desde el que se llamó al script.
CALL_DIR="$PWD"

# Directorio donde está el script.
SCRIPT_DIR="$(dirname "$0")"

# Directorio donde está la gramática.
OUT_DIR="$CALL_DIR/code/parser"

# Se traslada al directorio de la gramática.
cd "$OUT_DIR" || { echo "No se pudo cambiar a $OUT_DIR"; exit 1; }

# Se ejecuta yacc.
"$CALL_DIR/$SCRIPT_DIR/yacc" -J -Jnoconstruct -Jnorun -Jfinal -v gramatica.y

# Se agrega "package parser;" al inicio del archivo ParserVal.java.
sed -i '1ipackage parser;\n' ParserVal.java

# Se comenta el typedef union.
sed -i '/^typedef union/{s/^/\/\*/}' Parser.java
sed -i '/^} YYSTYPE;/{s/$/ \*\//}' Parser.java

# Se vuelve al directorio original.
cd "$CALL_DIR" || exit 1
