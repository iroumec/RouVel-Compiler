#!/bin/bash
# Compila todos los .tex en la subcarpeta "latex" del directorio del script a PNG
# usando pdflatex + convert (ImageMagick).
# Los resultados se guardan en ./results y se eliminan PDFs intermedios.

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LATEX_DIR="$SCRIPT_DIR/latex"
RESULTS_DIR="$SCRIPT_DIR/results"

mkdir -p "$RESULTS_DIR"

shopt -s nullglob
TEX_FILES=("$LATEX_DIR"/*.tex)

if [ ${#TEX_FILES[@]} -eq 0 ]; then
    echo "No hay archivos .tex en $LATEX_DIR"
    exit 0
fi

for FILE in "${TEX_FILES[@]}"; do
    BASENAME=$(basename "$FILE" .tex)
    echo "Procesando $BASENAME.tex ..."

    # Compilar a PDF en results/
    pdflatex -interaction=nonstopmode -output-directory "$RESULTS_DIR" "$FILE" >/dev/null 2>&1
    if [ $? -ne 0 ]; then
        echo "Error al compilar $FILE"
        continue
    fi

    # Convertir PDF a PNG
    convert -density 300 "$RESULTS_DIR/$BASENAME.pdf" -quality 90 "$RESULTS_DIR/$BASENAME.png"

    # Borrar PDF intermedio
    rm -f "$RESULTS_DIR/$BASENAME.pdf"

    # Limpiar temporales
    rm -f "$RESULTS_DIR/$BASENAME.aux" "$RESULTS_DIR/$BASENAME.log" "$RESULTS_DIR/$BASENAME.out"

    echo "Listo: $RESULTS_DIR/$BASENAME.png"
done
