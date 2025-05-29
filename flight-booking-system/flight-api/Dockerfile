# flight-api/Dockerfile
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
# This ARG is for the JAR file name, can be overridden at build time
ARG JAR_FILE=target/flight-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} flight-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/flight-api.jar"]
