#!/bin/bash

# ============================================================================
# Compila todos los .java, genera un .jar ejecutable y limpia los .class
# ============================================================================

CALL_DIR="$PWD"                 # Donde se ejecuta el script.
OUT_DIR="$CALL_DIR/code"        # Código fuente.
OUT_JAR="$CALL_DIR/rouvel-compiler.jar"
MAIN_CLASS="Main"               # Clase principal.

# --------------------------------------------------------------------------

echo "[1/4] Iniciando compilación..."
cd "$OUT_DIR" || { echo "No se pudo cambiar a $OUT_DIR"; exit 1; }

# Se compilan todos los archivos .java recursivamente.
find . -name "*.java" > sources.txt
javac @sources.txt
if [ $? -ne 0 ]; then
    echo "ERROR: Falló la compilación."
    rm -f sources.txt
    exit 1
fi
rm -f sources.txt

# --------------------------------------------------------------------------

echo "[2/4] Creando manifiesto..."
echo "Main-Class: $MAIN_CLASS" > manifest.txt
echo "" >> manifest.txt

# --------------------------------------------------------------------------

echo "[3/4] Generando ejecutable JAR..."
jar -cvfm "$OUT_JAR" manifest.txt $(find . -name "*.class") > /dev/null

# --------------------------------------------------------------------------

echo "[4/4] Limpiando archivos temporales..."
rm -f manifest.txt
find . -name "*.class" -delete

# --------------------------------------------------------------------------

cd "$CALL_DIR" || exit 1
echo "---------------------------------------------------"
echo "Generado: $OUT_JAR"
echo "Clase principal: $MAIN_CLASS"
echo "Limpieza completada. Todos los .class eliminados."
echo "Para ejecutar:"
echo "    java -jar rouvel-compiler.jar <archivo.uki>"
echo "---------------------------------------------------"
