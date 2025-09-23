import csv
import matplotlib.pyplot as plt
import pandas as pd

# Tus encabezados de columna
column_headers = ["L", "l", "d", "U", "I", ".", "F", "-", "P", '"', ":", "=", "!", ">", "<", "%", "#", "n", "s", "t", "Otro"]
row_headers_states = list(range(16)) # Asumiendo 16 filas para el ejemplo
row_headers_actions = list(range(16)) # Asumiendo 16 filas para el ejemplo

# --- Función para leer el CSV con Pandas (más simple) ---
def leer_csv_a_dataframe(file_path):
    # Leemos el CSV directamente a un DataFrame de pandas
    # y nos aseguramos que todas las celdas sean tratadas como texto para evitar conversiones
    return pd.read_csv(file_path, header=None, dtype=str).fillna('')

# --- Función para generar la tabla como PNG ---
def generar_png_desde_df(df, output_path, col_labels, row_labels):
    """
    Genera una imagen PNG de una tabla a partir de un DataFrame de Pandas.
    """
    # Crear una figura y un eje. El tamaño se puede ajustar.
    # El tamaño (20, 8) es un buen punto de partida para tablas anchas.
    fig, ax = plt.subplots(figsize=(20, max(4, len(df) * 0.5)))

    # Ocultar los ejes del gráfico
    ax.axis('tight')
    ax.axis('off')

    # Crear la tabla y añadirla al eje
    tabla = ax.table(
        cellText=df.values,
        colLabels=col_labels,
        rowLabels=row_labels,
        loc='center',
        cellLoc='center'
    )

    # Ajustar el tamaño de la fuente y la escala
    tabla.auto_set_font_size(False)
    tabla.set_fontsize(10)
    tabla.scale(1.2, 1.2) # Ancho, Alto

    # Guardar la figura como PNG
    # bbox_inches='tight' y pad_inches=0.5 eliminan el espacio en blanco innecesario
    plt.savefig(output_path, dpi=300, bbox_inches='tight', pad_inches=0.5)
    print(f"✅ Tabla guardada en: {output_path}")
    plt.close(fig) # Cerrar la figura para liberar memoria

# --- Proceso principal ---
# Rutas de los archivos (ajusta según tu estructura)
path_estados = "resources/diagrams/matrix/csv/matrizEstados.csv"
path_acciones = "resources/diagrams/matrix/csv/matrizAcciones.csv"
output_dir = "resources/diagrams/matrix/results/"

# Leer los datos
df_states = leer_csv_a_dataframe(path_estados)
df_actions = leer_csv_a_dataframe(path_acciones)

# Generar las imágenes
generar_png_desde_df(df_states, f"{output_dir}matrizEstados.png", column_headers, row_headers_states)
generar_png_desde_df(df_actions, f"{output_dir}matrizAcciones.png", column_headers, row_headers_actions)