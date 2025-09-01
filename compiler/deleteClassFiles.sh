#!/bin/bash

DIR="$(dirname "$0")/src"
cd "$DIR" || exit 1

# Se buscan y eliminan todos los .class dentro del subdirectorio.
find "$DIR" -type f -name "*.class" -delete
