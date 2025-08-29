If at the moment of rendering the dot files you receive the following error:

Error: file.dot: syntax error in line 1 near 'digraph'.

It is due to the BOM (https://en.wikipedia.org/wiki/Byte_order_mark). It's necessary to change the encoding from UTF-8 with BOM to UTF-8.

De la misma forma, el siguiente error se debe al mismo motivo:

./dot2png.sh: line 1: #!/usr/bin/env: No such file or directory

Se debe cambiar la codificación del archivo bash a UTF-8.

Al ejecutar archivos java también...

Si aparece que no se reconoce el comando javac.

java -version

Te fijás la versión de JDK, suponete que figura 21. Luego hacés: sudo apt install openjdk-21-jdk 