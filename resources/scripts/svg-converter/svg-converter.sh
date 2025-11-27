#!/bin/bash

# sudo apt install inkscape

# Se asume que se invoca al script desde la carpeta base del proyecto.

SRC_DIR="$PWD/resources/images"
OUT_DIR="$SRC_DIR/svg-version"

# Se crea el directorio de salida si no existe.
mkdir -p "$OUT_DIR"

# Se convierte cada archivo PNG a SVG.
for f in "$SRC_DIR"/*.png; do
    base_name=$(basename "$f" .png)
    inkscape "$f" --export-type=svg --export-filename="$OUT_DIR/$base_name.svg"
done
