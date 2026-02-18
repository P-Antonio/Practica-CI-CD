# ------------ Stage 1: Build the application ------------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .
#Descarga las dependencias sin compilar el código, lo que permite aprovechar la caché de Docker para acelerar las compilaciones posteriores
RUN mvn dependency:go-offline
COPY src ./src
#Compila el código y empaqueta la aplicación en un archivo JAR, omitiendo las pruebas para acelerar el proceso de construcción
RUN mvn clean package -DskipTests

# ------------ Stage 2: Run the application ------------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /build/target/Personas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]