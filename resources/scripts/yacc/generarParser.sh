#!/bin/bash

# Directorio donde se llamó al script
CALL_DIR="$PWD"

# Directorio donde está el script
SCRIPT_DIR="$(dirname "$0")"

# Directorio donde está la gramática
OUT_DIR="$CALL_DIR/code/parser"

# Nos movemos al directorio de la gramática
cd "$OUT_DIR" || { echo "No se pudo cambiar a $OUT_DIR"; exit 1; }

# Ejecutamos yacc desde ahí
"$SCRIPT_DIR"/yacc -J -Jnoconstruct -Jnorun -Jfinal -v gramatica.y

# Agregamos "package parser;" al inicio del archivo ParserVal.java.
sed -i '1ipackage parser;\n' ParserVal.java

# Comentamos typedef union
sed -i '/^typedef union/{s/^/\/\*/}' Parser.java
sed -i '/^} YYSTYPE;/{s/$/ \*\//}' Parser.java

# Volvemos al directorio original
cd "$CALL_DIR" || exit 1
