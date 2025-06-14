# Use JDK 18 runtime only
FROM eclipse-temurin:18-jdk-jammy

WORKDIR /app

COPY target/flight-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
