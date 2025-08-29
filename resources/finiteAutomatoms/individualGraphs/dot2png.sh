#!/bin/bash
# Convert all .dot files in the current folder to .svg.

for file in *.dot; do
    # skip if no .dot files exist
    [ -e "$file" ] || continue  

    base="${file%.dot}"
    dot -Tpng "$file" -o "$base.png"
done
