import csv

column_headers = ["L", "l", "d", "U", "I", ".", "F", "-", "P", '"', ":", "=", "!", ">", "<", r"\%", "#", "n", "s", "t", "Otro"]

# -----------------------------
# Leer CSV como listas
# -----------------------------
def leer_csv(file_path, is_actions=False):
    matrix = []
    with open(file_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile, quotechar='"')
        for row in reader:
            if is_actions:
                # Convertir cada celda a lista de acciones
                matrix.append([cell.split(",") if cell else [] for cell in row])
            else:
                # Convertir a enteros
                matrix.append([int(cell) for cell in row])
    return matrix

states = leer_csv("resources/diagrams/matrix/csv/matrizEstados.csv")
actions = leer_csv("resources/diagrams/matrix/csv/matrizAcciones.csv", is_actions=True)

def escape_latex(cell):
    if isinstance(cell, str):
        cell = cell.strip()
        cell = cell.replace('|', r'\|')
        return r'\verb|' + cell + '|'
    return str(cell)


def generar_latex(matrix, filename, is_actions=False):
    with open(filename, "w", encoding="utf-8") as f:
        f.write(r"\documentclass{standalone}" + "\n")
        f.write(r"\usepackage{tikz}" + "\n")
        f.write(r"\usetikzlibrary{matrix}" + "\n")
        f.write(r"\usepackage{graphicx}" + "\n")  # Para resizebox
        f.write(r"\begin{document}" + "\n")

        # Escalar la matriz automáticamente al ancho de la página
        f.write(r"\resizebox{\textwidth}{!}{" + "\n")
        f.write(r"\begin{tikzpicture}" + "\n")
        f.write(r"\matrix[matrix of nodes, nodes={draw, minimum size=0.6cm, anchor=center}, column sep=-\pgflinewidth, row sep=-\pgflinewidth] (m) {" + "\n")

        # Encabezado
        f.write(" & " + " & ".join([escape_latex(h) for h in column_headers]) + r" \\" + "\n")

        # Filas
        for idx, row in enumerate(matrix):
            fila = [str(idx)]
            if is_actions:
                fila += [escape_latex(", ".join(cell)) for cell in row]
            else:
                fila += [escape_latex(str(cell)) for cell in row]

            # Completar columnas si faltan
            while len(fila) < len(column_headers) + 1:
                fila.append('{}')

            f.write(" & ".join(fila) + r" \\" + "\n")

        f.write(r"};" + "\n")
        f.write(r"\end{tikzpicture}" + "\n")
        f.write(r"}" + "\n")  # Cierra resizebox
        f.write(r"\end{document}" + "\n")


# -----------------------------
# Generar archivos LaTeX
# -----------------------------
generar_latex(states, "resources/diagrams/matrix/latex/matrizEstados.tex", is_actions=False)
generar_latex(actions, "resources/diagrams/matrix/latex/matrizAcciones.tex", is_actions=True)
