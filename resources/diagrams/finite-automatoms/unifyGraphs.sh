#!/bin/bash

# Directorio desde el que se llamó al script,
CALL_DIR="$PWD"

BASE_DIR="$(dirname "$0")/individual-units"
cd "$BASE_DIR" || exit 1

# Lugar en el que se guardará el grafo final.
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
    // Se definen varios para que el autómata quede más claro.
    // Todos referencian al mismo estado de aceptación.
    // ACEPTACIÓN DE TOKEN VARIABLES.
    // TOKEN VARIABLE: token al que le corresponde más de un lexema.
    fI [shape = doublecircle;style = dashed;color = "#8a048a"; label = "F";];
    // ACEPTACIÓN DE OPERADORES.
    fP [shape = doublecircle;style = dashed;color = "#8a048a"; label = "F";];
    
    // Estados de error.
    // Se definen varios para que el autómata quede más claro.
    // Todos referencias al mismo estado de error o estado sumidero.
    // ERROR GENERAL.
    eG [shape = doublecircle;style = dashed;color = red;label = "e";];
    // ERROR DE COMENTARIO.
    eC [shape = doublecircle;style = dashed;color = red;label = "e";];
    // ERROR DE OPERADOR.
    eP [shape = doublecircle;style = dashed;color = red;label = "e";];
    // ERROR DE NÚMERO.
    eN [shape = doublecircle;style = dashed;color = red;label = "e";];
    // ERROR DE CADENA.
    eS [shape = doublecircle;style = dashed;color = red;label = "e";];
    
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

# Se genera una versión .png del grafo.
dot -Tpng "$SALIDA" -o "$CALL_DIR/resources/images/finiteAutomatom.png"