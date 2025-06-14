# Stage 1: Build stage
FROM maven:3.8.6-jdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline # Download dependencies
COPY src ./src

ARG PROFILE=dev
RUN mvn package -DskipTests -Dmaven.test.skip=true -P${PROFILE}

# Stage 2: Runtime stage
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]