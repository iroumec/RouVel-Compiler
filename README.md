# RouVel Compiler

Trabajo práctico de cursada para la materia Compiladores e Intérpretes.

A partir de un lenguaje simple proporcionado por la cátedra, se implementó su compilador.

![Ícono](resources/icon.png)

## Índice

- [Características del lenguaje](resources/markdown/language.md)
- [Analizador léxico](resources/markdown/lexer.md)
- [Analizador sintáctico](resources/markdown/parser.md)

## Ejecución

Los binarios ya incluyen todo lo necesario para ejecutar el compilador. Funciona tanto con rutas absolutas como relativas. En `resources/testFiles` hay códigos de ejemplo.

Requisito: [Docker](https://docs.docker.com/engine/install/).

### Linux

```sh
./rouvel-linux resources/testFiles/example1.uki
```

### macOS

```sh
./rouvel-macos resources/testFiles/example1.uki
```

### Windows

```sh
rouvel.exe resources/testFiles/example1.uki
```

## Eliminación de la Imagen (opcional)

```sh
docker rmi rouvel-compiler
```
