#!/bin/bash
BASE_DIR="$(dirname "$0")/errors"
cd "$BASE_DIR" || exit 1
SALIDA="$(dirname "$0")/finiteAutomatom.dot"

# Verificar si existen archivos .dot
if ! ls *.dot >/dev/null 2>&1; then
    echo "Error: No se encontraron archivos .dot en $BASE_DIR"
    exit 1
fi

# Encabezado fijo
cat > "$SALIDA" <<'EOF'
digraph DFA {
    // Layout general
    rankdir = LR;
    splines = true;
    overlap = false;
    nodesep = 0.4;
    ranksep = 0.6;
    
    // Estados normales
    node [shape = circle; style = filled;];
    
    // Estado de aceptación
    F [shape = doublecircle;style = dashed;color = "#8a048a";];
    
    // Estado de error
    e [shape = doublecircle;style = dashed;color = red;];
    
    // -----------------------------
    // Agrupación inicial
    subgraph cluster_inicio {
        label = "Bloque inicial";
        style = dashed;
        fillcolor = lightgrey;
        0;
    }
    
    // Agrupación numérica
    subgraph cluster_num {
        label = "Números";
        style = dashed;
        fillcolor = "#f2f2f2";
        node [fillcolor = lightblue;];
        1;
        2;
        3;
        4;
        5;
        6;
        7;
    }
    
    // Agrupación literales
    subgraph cluster_lit {
        label = "Letras y cadenas";
        style = dashed;
        node [fillcolor = lightyellow;];
        8;
        9;
        18;
    }
    
    // Agrupación operadores
    subgraph cluster_ops {
        label = "Operadores";
        style = dashed;
        node [fillcolor = lightpink;];
        10;
        11;
        12;
        13;
        14;
    }
    
    // Agrupación comentarios
    subgraph cluster_coment {
        label = "Comentarios";
        style = dashed;
        node [fillcolor = "#b4e7b4";];
        15;
        16;
        17;
    }
    
EOF

# Extraemos todas las líneas de transición con patrón más flexible
# Busca líneas que contengan '->' (transiciones)
# -h hace que no se muestre el nombre del archivo.
# Ordenar numéricamente por el número del estado (primer número después de espacios)
grep -h -E '\->' *.dot 2>/dev/null | sort -n -k1,1 | uniq >> "$SALIDA"

# Cerramos el grafo
echo "}" >> "$SALIDA"