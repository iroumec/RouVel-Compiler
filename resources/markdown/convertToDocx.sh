#!/bin/bash

mkdir -p temp

for f in *.md; do
    cat "$f" >> temp/combinado.md
    echo -e "\n\n" >> temp/combinado.md
done

pandoc temp/combinado.md -o combinado.docx
