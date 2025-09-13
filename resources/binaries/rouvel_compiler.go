package main

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"runtime"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Uso: rouvel <ruta_al_archivo.uki>")
		os.Exit(1)
	}

	filePath, err := filepath.Abs(os.Args[1])
	if err != nil {
		fmt.Println("Error al obtener la ruta absoluta:", err)
		os.Exit(1)
	}

	// Comprobar que el archivo existe
	if _, err := os.Stat(filePath); os.IsNotExist(err) {
		fmt.Println("El archivo no existe:", filePath)
		os.Exit(1)
	}

	dir := filepath.Dir(filePath)
	file := filepath.Base(filePath)

	// Ajuste de ruta para Windows Docker (si es necesario)
	if runtime.GOOS == "windows" {
		// Convertir C:\path a /c/path
		drive := filepath.VolumeName(dir)
		path := dir[len(drive):]
		driveLetter := string(drive[0])
		dir = fmt.Sprintf("/%s%s", driveLetter, filepath.ToSlash(path))
	}

	// Ejecutar Docker
	cmd := exec.Command("docker", "run", "--rm", "-v", fmt.Sprintf("%s:/data", dir), "tpe-compiler", fmt.Sprintf("/data/%s", file))
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.Stdin = os.Stdin

	if err := cmd.Run(); err != nil {
		fmt.Println("Error al ejecutar Docker:", err)
		os.Exit(1)
	}
}
