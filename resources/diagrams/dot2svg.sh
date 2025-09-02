#!/bin/bash
# Convert all .dot files in units to .svg.

BASE_DIR="$(dirname "$0")/units"

cd "$BASE_DIR"

for file in "$BASE_DIR"/*.dot; do
    [ -e "$file" ] || continue 

    base="$(basename "$file" .dot)"
    dot -Tsvg "$file" -o "$BASE_DIR/images/svg/$base.svg"
done
