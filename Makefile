# ==============================================================================
# Makefile para automatización
# ==============================================================================

# Cada línea de Make "@" se ejecuta en una subshell independiente.

# --- Variables de Configuración ---
SCRIPT ?= "resources/scripts/yacc-compile-run.sh"
TEST_FILE_DIR ?= resources/testFiles/

# =================================================================================================

# Target por defecto que se ejecuta al correr `make`.
all: help

# =================================================================================================

run: ## Ejecuta yacc, compila el programa y lo ejecuta. Ejemplo de uso: `make run FILE="main.uki"`.
	@chmod +x "$(SCRIPT)"
	@"$(SCRIPT)" "$(TEST_FILE_DIR)$(FILE)"

# =================================================================================================

clean: ## Limpia todos los archivos no solicitados para la entrega.
	@rm Dockerfile
	@rm rouvel-linux
	@rm rouvel-macos
	@rm rouvel-windows.exe

# =================================================================================================

DOCKER_SCRIPT ?= "resources/scripts/docker/generate.sh"

# TODO: hacer que funcione correctamente.
generate-docker: ## Genera ejecutables que permiten correr el compilador utilizando Docker.
	@chmod +x "$(SCRIPT)"
	@"$(SCRIPT)"

# =================================================================================================

help: ## Muestra los comandos disponibles.
	@echo "Comandos disponibles:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

# =================================================================================================



