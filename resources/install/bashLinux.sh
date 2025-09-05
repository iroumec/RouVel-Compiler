#!/bin/bash
set -e

# Detectar distro
if [ -f /etc/os-release ]; then
    . /etc/os-release
    DISTRO=$ID
    echo "Distro detectada: $DISTRO"
    echo
else
    echo "No se pudo detectar la distro"
    exit 1
fi

install_java_maven_debian() {
    sudo apt update
    # Java
    if java -version 2>&1 | grep -q '21'; then
        echo "OpenJDK 21 ya instalado"
    else
        echo "Instalando OpenJDK 21..."
        sudo apt install -y openjdk-21-jdk
    fi
    # Maven
    if ! mvn -v >/dev/null 2>&1; then
        echo "Instalando Maven..."
        sudo apt install -y maven
    else
        echo "Maven ya instalado"
    fi
}

install_java_maven_fedora() {
    sudo dnf check-update || true
    # Java
    if java -version 2>&1 | grep -q '21'; then
        echo "OpenJDK 21 ya instalado"
    else
        echo "Instalando OpenJDK 21..."
        sudo dnf install -y java-21-openjdk-devel
    fi
    # Maven
    if ! mvn -v >/dev/null 2>&1; then
        echo "Instalando Maven..."
        sudo dnf install -y maven
    else
        echo "Maven ya instalado"
    fi
}

install_java_maven_arch() {
    sudo pacman -Sy --noconfirm
    # Java
    if java -version 2>&1 | grep -q '21'; then
        echo "OpenJDK 21 ya instalado"
    else
        echo "Instalando OpenJDK 21..."
        sudo pacman -S --noconfirm jdk-openjdk
    fi
    # Maven
    if ! mvn -v >/dev/null 2>&1; then
        echo "Instalando Maven..."
        sudo pacman -S --noconfirm maven
    else
        echo "Maven ya instalado"
    fi
}

case "$DISTRO" in
    ubuntu|debian)
        install_java_maven_debian
        ;;
    fedora|centos|rhel)
        install_java_maven_fedora
        ;;
    arch|manjaro)
        install_java_maven_arch
        ;;
    *)
        echo "Distro no soportada automáticamente. Por favor instala Java 21 y Maven manualmente."
        exit 1
        ;;
esac

# Mostrar versiones
echo
java -version
echo
mvn -v
