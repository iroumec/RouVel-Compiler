# Etapa 1: Build con JDK
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY code /app

# Compilar todo el código
RUN mkdir -p bin && \
    javac -d bin $(find . -name "*.java")

# Etapa 2: Solo ejecución con Java
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S rouvel && adduser -S rouvel -G rouvel

# Copiamos las clases compiladas
COPY --from=build /app/bin /app/bin

RUN chown -R rouvel:rouvel /app
USER rouvel

# Ejecutar directamente la clase Main
ENTRYPOINT ["java", "-cp", "/app/bin", "Main"]
