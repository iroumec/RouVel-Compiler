package main

import (
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"runtime"
)

const imageName = "rouvel-compiler"

// checkOrBuildImage verifica si la imagen Docker existe y la construye si no.
func checkOrBuildImage() error {
	// Verificar si la imagen ya existe.
	if err := exec.Command("docker", "image", "inspect", imageName).Run(); err == nil {
		return nil // Imagen ya existe
	}

	fmt.Printf("La imagen '%s' no existe. Construyendo con Docker...\n", imageName)

	exeDir, err := getExecutableDir()
	if err != nil {
		return fmt.Errorf("no se pudo obtener la ruta del ejecutable: %w", err)
	}

	buildCmd := exec.Command("docker", "build", "-t", imageName, exeDir)
	buildCmd.Stdout = io.Discard
	buildCmd.Stderr = io.Discard
	buildCmd.Stdin = os.Stdin

	if err := buildCmd.Run(); err != nil {
		return fmt.Errorf("falló la construcción de la imagen Docker: %w", err)
	}

	return nil
}

// getExecutableDir devuelve el directorio donde se encuentra el ejecutable.
func getExecutableDir() (string, error) {
	exePath, err := os.Executable()
	if err != nil {
		return "", err
	}
	return filepath.Dir(exePath), nil
}

// adaptPathForWindows convierte rutas de Windows a formato compatible con Docker.
func adaptPathForWindows(path string) string {
	if runtime.GOOS != "windows" {
		return path
	}

	drive := filepath.VolumeName(path)
	rest := path[len(drive):]
	driveLetter := string(drive[0])
	return fmt.Sprintf("/%s%s", driveLetter, filepath.ToSlash(rest))
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

	// Comprobar que el archivo existe.
	if _, err := os.Stat(filePath); os.IsNotExist(err) {
		fmt.Println("El archivo no existe:", filePath)
		os.Exit(1)
	}

	// Construir la imagen si no existe.
	if err := checkOrBuildImage(); err != nil {
		fmt.Println("Error preparando Docker:", err)
		os.Exit(1)
	}

	dir := adaptPathForWindows(filepath.Dir(filePath))
	file := filepath.Base(filePath)

	// Ejecutar el contenedor Docker con el archivo.
	cmd := exec.Command("docker", "run", "--rm", "-v", fmt.Sprintf("%s:/data", dir), imageName, fmt.Sprintf("/data/%s", file))
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.Stdin = os.Stdin

	if err := cmd.Run(); err != nil {
		fmt.Println("Error al ejecutar Docker:", err)
		os.Exit(1)
	}
}
