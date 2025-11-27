#!/bin/bash

# Directorio desde el que se llamó al script.
CALL_DIR="$PWD"

# Carpeta donde está el script.
SCRIPT_DIR="$(dirname "$0")"

# Se copia el archivo Dockerfile al directorio de invocación.
cp "$SCRIPT_DIR/Dockerfile" "$CALL_DIR"

cd "$SCRIPT_DIR" || { echo "No se pudo cambiar a $SCRIPT_DIR"; exit 1; }

GOOS=linux GOARCH=amd64 go build -o "$CALL_DIR"/rouvel-linux rouvel_compiler.go
GOOS=darwin GOARCH=amd64 go build -o "$CALL_DIR"/rouvel-macos rouvel_compiler.go
GOOS=windows GOARCH=amd64 go build -o "$CALL_DIR"/rouvel-windows.exe rouvel_compiler.go

cd "$CALL_DIR" || exit 1