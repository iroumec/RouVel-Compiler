package main

import (
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"runtime"
)

const (
	imageName = "rouvel-compiler"
)

// checkOrBuildImage verifica si la imagen existe y si no, la construye.
func checkOrBuildImage() error {
	// Se verifica si la imagen ya existe.
	checkCmd := exec.Command("docker", "image", "inspect", imageName)
	if err := checkCmd.Run(); err == nil {
		return nil // Imagen ya existe.
	}

	fmt.Printf("La imagen '%s' no existe. Construyendo con Docker...\n", imageName)

	// Directorio actual donde se ejecuta el binario.
	exePath, err := os.Executable()
	if err != nil {
		return fmt.Errorf("no se pudo obtener la ruta del ejecutable: %w", err)
	}
	exeDir := filepath.Dir(exePath)

	// Se asume que el Dockerfile está en el mismo directorio que el ejecutable.
	buildCmd := exec.Command("docker", "build", "-t", imageName, exeDir)
	buildCmd.Stdout = io.Discard
	buildCmd.Stderr = io.Discard
	buildCmd.Stdin = os.Stdin

	// Ejecución del comando de construcción.
	if err := buildCmd.Run(); err != nil {
		return fmt.Errorf("falló la construcción de la imagen Docker: %w", err)
	}

	return nil
}

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

	// Se comprueba que el archivo exista.
	if _, err := os.Stat(filePath); os.IsNotExist(err) {
		fmt.Println("El archivo no existe:", filePath)
		os.Exit(1)
	}

	// Se construye la imagen si no existe.
	if err := checkOrBuildImage(); err != nil {
		fmt.Println("Error preparando Docker:", err)
		os.Exit(1)
	}

	dir := filepath.Dir(filePath)
	file := filepath.Base(filePath)

	// Ajuste de la ruta para Windows Docker.
	if runtime.GOOS == "windows" {
		drive := filepath.VolumeName(dir)
		path := dir[len(drive):]
		driveLetter := string(drive[0])
		dir = fmt.Sprintf("/%s%s", driveLetter, filepath.ToSlash(path))
	}

	// Ejecución del contenedor Docker con el archivo.
	cmd := exec.Command("docker", "run", "--rm", "-v", fmt.Sprintf("%s:/data", dir), imageName, fmt.Sprintf("/data/%s", file))
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.Stdin = os.Stdin

	if err := cmd.Run(); err != nil {
		fmt.Println("Error al ejecutar Docker:", err)
		os.Exit(1)
	}
}
