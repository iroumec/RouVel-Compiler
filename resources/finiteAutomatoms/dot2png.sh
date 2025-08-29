#!/bin/bash
# Convert all .dot files in FADiagrams to .png.

BASE_DIR="$(dirname "$0")/FADiagrams"

cd "$BASE_DIR"

for file in "$BASE_DIR"/*.dot; do
    [ -e "$file" ] || continue 

    base="$(basename "$file" .dot)"
    dot -Tpng "$file" -o "$BASE_DIR/images/png/$base.png"
done
