import csv
import matplotlib.pyplot as plt

column_headers = ["L", "l", "d", "U", "I", ".", "F", "-", "P", '"', ":", "=", "!", ">", "<", "%", "#", "n", "s", "t", "Otro"]

# -----------------------------
# Lectura del CSV
# -----------------------------
def leer_csv(file_path, is_actions=False):
    matrix = []
    with open(file_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile, quotechar='"')
        for row in reader:
            if is_actions:
                processed_row = [", ".join(cell.split(",")) if cell else "" for cell in row]
                matrix.append(processed_row)
            else:
                matrix.append([str(cell) if cell else "" for cell in row])
    return matrix

# ---------------------------------------------
# Generación de la imagen
# ---------------------------------------------
def generar_imagen_matplotlib(matrix, headers, filename, layout='auto'):
    """
    Genera una imagen de la tabla.
    - layout='auto': Ancho de columna se ajusta al contenido (ideal para texto largo).
    - layout='fixed': Ancho de columna es uniforme (ideal para números).
    """
    
    data_with_rows = [[str(i)] + row for i, row in enumerate(matrix)]
    full_headers = [""] + headers

    fig, ax = plt.subplots(figsize=(25, 10))
    ax.axis('off')

    tabla = ax.table(
        cellText=data_with_rows,
        colLabels=full_headers,
        cellLoc='center',
        loc='center'
    )
    
    tabla.auto_set_font_size(False)
    tabla.set_fontsize(10)

    # --- LÓGICA DE DISEÑO CONDICIONAL ---
    if layout == 'auto':
        # Para la tabla de ACCIONES.
        print(f"Usando layout automático para '{filename}'...")
        tabla.auto_set_column_width(col=list(range(len(full_headers))))
    else: # layout == 'fixed'
        # Para la tabla de ESTADOS.
        print(f"Usando layout fijo para '{filename}'...")
        # Este valor puede ajustarse para hacer las tablas más anchas o estrechas.
        ancho_fijo = 0.04 
        for key, cell in tabla.get_celld().items():
            cell.set_width(ancho_fijo)

    # Estilo común para ambas tablas.
    for key, cell in tabla.get_celld().items():
        cell.set_text_props(va='center')
        cell.set_height(0.05)
        
        if key[0] == 0 or key[1] == 0:
            cell.set_facecolor("#f0f0f0")
            cell.set_text_props(weight='bold', va='center')

    # Se guarda la imagen final...
    plt.savefig(filename, bbox_inches='tight', dpi=300, pad_inches=0.4)
    plt.close(fig)

# -----------------------------
# Ejecución Principal
# -----------------------------
states = leer_csv("resources/diagrams/matrix/csv/matrizEstados.csv")
actions = leer_csv("resources/diagrams/matrix/csv/matrizAcciones.csv", is_actions=True)

# Se usa layout='fixed' para la tabla de estados.
generar_imagen_matplotlib(states, column_headers, "resources/images/matrizEstados.png", layout='fixed')

# Se usa layout='auto' para la tabla de acciones
generar_imagen_matplotlib(actions, column_headers, "resources/images/matrizAcciones.png", layout='auto')