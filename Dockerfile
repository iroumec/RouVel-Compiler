# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY code /app
RUN mvn clean package shade:shade -DskipTests

# Etapa 2: Solo ejecución con Java
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Creación de un usuario y grupo no root.
RUN addgroup -S rouvel && adduser -S rouvel -G rouvel

COPY --from=build /app/target/RouVel-Compiler-1.0.jar /app/app.jar

# Se brinda al usuario no root permisos para ejecutar la app.
RUN chown -R rouvel:rouvel /app

# Se cambia al usuario no root.
USER rouvel

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
