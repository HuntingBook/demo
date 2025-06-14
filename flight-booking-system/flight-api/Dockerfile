# ---- Stage 1: Build ----
# Use a Maven/JDK image for building
FROM maven:3.9-eclipse-temurin-18 AS builder

# Declare the build argument with a default value
ARG PROFILE=dev

# Set working directory
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
# The -P${PROFILE} here might not be strictly necessary for go-offline
# if your profiles don't drastically change dependencies, but it's safer to include it.
# If profiles DO change dependencies, it's crucial.
RUN mvn dependency:go-offline -B -P${PROFILE}

# Copy the rest of your source code
COPY src ./src

# Build the application using the specified profile
# -B for batch mode
# -DskipTests is generally good for image builds
# -P${PROFILE} activates the Maven profile passed via ARG
RUN echo "Building with Maven profile: ${PROFILE}" && \
    mvn package -DskipTests -B -P${PROFILE}

# ---- Stage 2: Runtime ----
# Use a lightweight JRE image for the final image
FROM eclipse-temurin:18-jre

# Set working directory
WORKDIR /app

# Copy the JAR from the builder stage
# Ensure the path matches your actual JAR file name in the target directory
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT [ "java", "-jar", "app.jar" ]