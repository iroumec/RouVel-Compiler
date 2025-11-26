# RouVel Compiler

Trabajo práctico de cursada para la materia Compiladores e Intérpretes.

A partir de un lenguaje simple proporcionado por la cátedra, al que se denominó UKI, se implementó su compilador.

![Ícono](resources/images/logo-recortado-verticalmente.png)

## Ejecución

### Compilación del Programa

A continuación, se detallará la compilación del programa utilizando el archivo _jar_. Para ello, debe tener instaladas las últimas versiones de Java y JDK. Estas pueden ser descargadas e instaladas accediendo a la página oficial y siguiendo los pasos de instalación correspondientes a su sistema operativo: [Oracle Java and JDK](https://www.oracle.com/java/technologies/downloads/#java25). No obstante, también, si lo desea, existe la posibilidad de ejecutarlo usando Docker: [¿Cómo ejecutó el programa usando Docker?](resources/markdown/docker.md)

El programa funciona tanto con rutas absolutas como relativas. En `resources/testFiles` se proporcionan códigos de ejemplo para probarlo.

> [!IMPORTANT]
> Si se encuentra en Windows, antes de compilar un programa, debe asegurarse de que la codificación del archivo sea UTF-8 y el _end line separator_ sea LF, en lugar de CRLF, que es el utilizado por defecto en este sistema operativo.

Para ejecutar el programa, debe posicionarse en la carpeta del proyecto y ejecutar:

```sh
java -jar rouvel-compiler.jar <archivo>
```

Por ejemplo:

```sh
java -jar rouvel-compiler.jar resources/testFiles/ejemplo.uki
```

### Salida de la Compilación

El comando anterior compila el archivo `.uki` especificado, mostrando los resultados en pantalla y guardándolos también en la carpeta `outputs/results/`, generando un `.txt` con el nombre del archivo compilado. De no presentarse ningún error durante la compilación, se genera el archivo `.wat` correspondiente en `outputs/wat`. Adicionalmente, de tener la herramienta `wat2wasm` instalada, compila el `.wat` y almacena el archivo `.wasm` resultante en `outputs/wasm/`.

De compilar el archivo `.wat` manualmente, deberá especificar la opción `--enable-exceptions` en la herramienta `was2wasm`, debido a que el compilador utiliza una función experimental, provista por WASM, para el levantamiento de excepciones.

### Ejecución del Programa Compilado

A continuación, se detallan los pasos que deben seguirse para ejecutar el archivo `.wasm` en el navegador. Es importante que se encuentre en la carpeta del proyecto durante todos los pasos.

#### 1. Generar el archivo HTML

Se debe reemplazar `{{WASM_FILE}}` en la plantilla provista en `resources/index.template.html` con el nombre del archivo `.wasm` a ejecutar:

**Linux/macOS:**

```bash
sed "s|{{WASM_FILE}}|ejemplo.wasm|g" index.template.html > index.html
```

**Windows (PowerShell):**

```powershell
(Get-Content index.template.html) -replace '{{WASM_FILE}}', 'ejemplo.wasm' | Set-Content index.html
```

WebAssembly requiere servirse a través de HTTP/HTTPS. **No funciona** abriendo el archivo `index.html` directamente desde el explorador de archivos. A continuación, se explica cómo levantar un servidor HTTP local.

#### 2. Levantar un servidor HTTP local

Elija una de estas opciones:

**Python:**

```bash
# Python 3
python3 -m http.server 8000

# Python 2
python -m SimpleHTTPServer 8000
```

**Node.js:**

```bash
# Usando http-server (instalar con: npm install -g http-server).
http-server -p 8000

# Usando serve (instalar con: npm install -g serve).
serve -p 8000
```

**PHP:**

```bash
php -S localhost:8000
```

**Ruby:**

```bash
ruby -run -e httpd . -p 8000
```

**Otras opciones:**

```bash
# Live Server (VS Code extension) - clic derecho > "Open with Live Server"

# Browsersync
browser-sync start --server --port 8000

# Caddy
caddy file-server --listen :8000
```

#### 3. Abrir en el navegador

Navegue a: **<http://localhost:8000>**

#### 4. Detener el servidor

Presione `Ctrl+C` en la terminal.

#### Ejemplo (Linux/macOS con Python)

```bash
# Generar HTML.
sed "s|{{WASM_FILE}}|output.wasm|g" index.template.html > index.html

# Levantar servidor.
python3 -m http.server 8000

# Abrir http://localhost:8000 en el navegador.
```

### Nota importante

## Code Highlighter

Si se encuentra en Visual Studio Code y desea utilizar un resaltado de sintaxis para los archivos .uki, puede instalar la extensión proporcionada con dicho fin:

### Opción 1

Desde una terminal en la carpeta del proyecto, ejecutar:

```sh
code --install-extension resources/extensions/uki-syntax-0.0.1.vsix
```

### Opción 2

- Abrir VS Code.
- Ir a **Extensiones** (`Ctrl+Shif+X` o `Cmd+Shift+X` en macOS).
- Hacer clic en los tres puntos (...) en la esquina superior derecha.
- Hacer clic en **Install from VSIX...**.
- Navegar hasta la carpeta del proyecto y abrir el directorio `resources/extensions`.
- Seleccionar `uki-syntax-0.0.1.vsix`.
