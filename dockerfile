FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests -Djacoco.skip=true

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]