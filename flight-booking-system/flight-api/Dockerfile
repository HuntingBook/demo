# ---- Stage 1: Build ----
# Use a Maven image with Eclipse Temurin JDK 18 for building
FROM maven:3.8-eclipse-temurin-18-alpine AS builder

# Declare the build argument with a default value
ARG PROFILE=dev

# Set working directory
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies using the specified profile
# This helps if profiles alter dependency sets
RUN mvn dependency:go-offline -B -P${PROFILE}

# Copy the rest of your source code
COPY src ./src

# Build the application using the specified profile
# Includes -Dmaven.test.skip=true as per your request, alongside -DskipTests
# -B is for batch mode, good for CI/CD
RUN echo "Building with Maven profile: ${PROFILE}" && \
    mvn package -DskipTests -Dmaven.test.skip=true -B -P${PROFILE}

# ---- Stage 2: Runtime ----
# Use a lightweight Eclipse Temurin JRE 18 image for the final image
FROM eclipse-temurin:18-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from the builder stage
# Ensure the path matches your actual JAR file name in the target directory
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT [ "java", "-jar", "app.jar" ]