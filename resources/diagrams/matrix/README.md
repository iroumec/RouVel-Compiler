# LaTeX + TikZ

Posibilita la renderización de matrices en imágenes.

# 1. Instalar LaTeX

En Debian, lo más sencillo es instalar la distribución completa (varios GB, pero sin dolores de cabeza con paquetes faltantes):

```bash
sudo apt update
sudo apt install texlive-full
```

Si preferís algo más liviano (y después vas agregando paquetes según los necesites):

```bash
sudo apt install texlive-latex-base texlive-latex-recommended texlive-latex-extra texlive-pictures
```

El paquete `texlive-pictures` incluye TikZ.

---

# 2. Crear tu archivo `.tex`

Ejemplo: `matriz.tex`

```latex
\documentclass{standalone}
\usepackage{tikz}

\begin{document}
\begin{tikzpicture}
  \matrix[matrix of nodes,
          nodes={draw, minimum size=1cm, anchor=center},
          column sep=-\pgflinewidth, row sep=-\pgflinewidth] (m) {
      & a & b & c \\
    S1 & 0 & 1 & 0 \\
    S2 & 1 & 0 & 1 \\
    S3 & 0 & 0 & 1 \\
  };
\end{tikzpicture}
\end{document}
```

---

# 3. Compilar a PDF

En consola:

```bash
pdflatex matriz.tex
```

Esto te genera `matriz.pdf`.

---

# 4. Exportar a PNG o SVG

- A **PNG** (300 dpi):

```bash
convert -density 300 matriz.pdf -quality 90 matriz.png
```

(necesitás `imagemagick`: `sudo apt install imagemagick`)

- A **SVG** (vectorial):

```bash
pdf2svg matriz.pdf matriz.svg
```

(necesitás `pdf2svg`: `sudo apt install pdf2svg`)

---

# 5. Resultado

Ya tenés tu matriz declarada en texto, exportada como imagen, igual que haces con Graphviz o PlantUML.

---

¿Querés que te arme un **makefile** o **script bash** para que sólo con `make matriz` te genere directamente el PNG/SVG?

sudo apt update
sudo apt install imagemagick
