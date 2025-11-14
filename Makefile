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

HTTPD ?= busybox

everything:
	@if [ ! -f "$(FILE)" ]; then echo "No existe $(FILE)"; exit 1; fi

	@echo "Generando index.html temporal..."
	@sed "s|__WASM_FILE__|$(FILE)|" index.template.html > index.html

	@echo "Iniciando servidor..."
	@$(HTTPD) httpd -f -p 8080 &
	@SERVER_PID=$$!; \
	echo "Servidor PID: $$SERVER_PID"; \
	\
	echo "Abriendo navegador..."; \
	( \
		if command -v xdg-open >/dev/null; then xdg-open http://localhost:8080/index.html; \
		elif command -v open >/dev/null; then open http://localhost:8080/index.html; \
		else echo "Abra manualmente: http://localhost:8080/index.html"; \
		fi \
	); \
	\
	echo "Presione Ctrl+C para detener"; \
	trap "kill $$SERVER_PID; rm -f index.html; exit 0" INT; \
	while true; do sleep 1; done

# =================================================================================================

help: ## Muestra los comandos disponibles.
	@echo "Comandos disponibles:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

# =================================================================================================



