# Verificar si se ejecuta como administrador
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltinRole]::Administrator)) {
    Write-Host "Por favor ejecuta este script como Administrador." -ForegroundColor Red
    exit 1
}

# Función para instalar Chocolatey si no está
function Install-Chocolatey {
    if (-not (Get-Command choco -ErrorAction SilentlyContinue)) {
        Write-Host "Instalando Chocolatey..."
        Set-ExecutionPolicy Bypass -Scope Process -Force
        [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
        Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    } else {
        Write-Host "Chocolatey ya instalado."
    }
}

# Instalar Chocolatey si falta
Install-Chocolatey

# Función para verificar e instalar OpenJDK 21
function Install-OpenJDK21 {
    $java = & java -version 2>&1
    if ($java -match "21") {
        Write-Host "OpenJDK 21 ya instalado."
    } else {
        Write-Host "Instalando OpenJDK 21..."
        choco install openjdk --version=21 -y
    }
}

# Función para verificar e instalar Maven
function Install-Maven {
    if (Get-Command mvn -ErrorAction SilentlyContinue) {
        Write-Host "Maven ya instalado."
    } else {
        Write-Host "Instalando Maven..."
        choco install maven -y
    }
}

# Ejecutar instalaciones
Install-OpenJDK21
Install-Maven

# Mostrar versiones finales
Write-Host "`nVersiones instaladas:"
java -version
mvn -v
