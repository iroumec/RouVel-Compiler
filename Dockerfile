# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY code /app
RUN mvn clean package shade:shade -DskipTests

# Etapa 2: Solo ejecución con Java
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/RouVel-Compiler-1.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
