# Paso 1: Usar una imagen ligera de Java 17 o 21 (según tu proyecto)
FROM eclipse-temurin:21-jre-jammy

# Paso 2: Crear el directorio de la app
WORKDIR /app

# Paso 3: Copiar el .jar generado por Maven/Gradle desde el build de GitHub Actions
COPY target/*.jar app.jar

# Paso 4: Exponer el puerto de tu API (ej: 8080)
EXPOSE 8080

# Paso 5: Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]