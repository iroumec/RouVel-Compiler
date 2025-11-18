# ==============================================================================
# Makefile para automatización
# ==============================================================================

# Cada línea de Make "@" se ejecuta en una subshell independiente.

# --- Variables de Configuración ---
FILE ?= program.uki
TEST_FILE_DIR ?= resources/testFiles/
WASM := outputs/wasm/$(basename $(FILE)).wasm
SCRIPT ?= "resources/scripts/yacc-compile-run.sh"
DOCKER_SCRIPT ?= "resources/scripts/docker/generate.sh"

# =================================================================================================

# Target por defecto que se ejecuta al correr `make`.
all: help

# =================================================================================================

clean: ## Limpia todos los archivos no solicitados para la entrega.
	@rm Dockerfile
	@rm rouvel-linux
	@rm rouvel-macos
	@rm rouvel-windows.exe

# =================================================================================================

# TODO: hacer que funcione correctamente.
generate-docker: ## Genera ejecutables que permiten correr el compilador utilizando Docker.
	@chmod +x "$(SCRIPT)"
	@"$(SCRIPT)"

# =================================================================================================

uki-compile compile: ## Ejecuta yacc, compila el programa y lo ejecuta. Ejemplo de uso: `make run FILE="main.uki"`.
	@chmod +x "$(SCRIPT)"
	@"$(SCRIPT)" "$(TEST_FILE_DIR)$(FILE)"; \
	EXIT_CODE=$$?; \
	echo; \
	if [ $$EXIT_CODE -ne 0 ]; then \
		touch .compile_failed; \
	else \
		rm -f .compile_failed; \
	fi

# =================================================================================================

uki-run run: uki-compile
	@if [ -f .compile_failed ]; then \
		rm -f .compile_failed; \
		exit 0; \
	fi; \
	$(MAKE) generate-html; \
	echo "Levantando servidor..."; \
	python3 -m http.server 8000 >/dev/null 2>&1 & \
	echo $$! > .server.pid; \
	sleep 1; \
	xdg-open "http://localhost:8000/" 2>/dev/null || true; \
	echo "\nServidor levantado en 'http://localhost:8000'"; \
	echo "Presione ENTER para cerrar el servidor..."; \
	read _; \
	if [ -f .server.pid ]; then \
		kill `cat .server.pid` 2>/dev/null || true; \
		rm -f .server.pid; \
	fi

# =================================================================================================

generate-html:
	@echo "Generando index.html usando $(WASM)..."
	@sed "s|{{WASM_FILE}}|$(WASM)|g" resources/index.template.html > index.html

# =================================================================================================

help: ## Muestra los comandos disponibles.
	@echo "Comandos disponibles:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

# =================================================================================================