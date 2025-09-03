#!/bin/bash
BASE_DIR="$(dirname "$0")/errors"
cd "$BASE_DIR" || exit 1
archivo_salida="$(dirname "$0")/FAErrors.dot"

# Verificar si existen archivos .dot
if ! ls *.dot >/dev/null 2>&1; then
    echo "Error: No se encontraron archivos .dot en $BASE_DIR"
    exit 1
fi

# Encabezado fijo
cat > "$archivo_salida" <<'EOF'
digraph DFA {
    // Layout.
    rankdir = LR;
    splines = true;
    overlap = false;
    nodesep = 0.05;
    ranksep = 0.1;
    // Estados normales.
    node [shape = circle;];
    // Estado de aceptación.
    F [shape = doublecircle;];
    // Estado de error.
    e [shape = doublecircle;style = dashed;color = red;];
    
EOF

# Extraemos todas las líneas de transición con patrón más flexible
# Busca líneas que contengan '->' (transiciones)
# -h hace que no se muestre el nombre del archivo.
# Ordenar numéricamente por el número del estado (primer número después de espacios)
grep -h -E '\->' *.dot 2>/dev/null | sort -n -k1,1 | uniq >> "$archivo_salida"

# Cerramos el grafo
echo "}" >> "$archivo_salida"
echo "Archivo unificado generado: $archivo_salida"