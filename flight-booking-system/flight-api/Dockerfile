# ---- Stage 1: Build ----
  FROM maven:3.9-eclipse-temurin-18-slim AS builder

  ARG PROFILE=dev
  
  WORKDIR /app
  
  # Copy pom.xml and download dependencies
  COPY pom.xml .
  RUN mvn dependency:go-offline -B -P${PROFILE}
  
  # Copy source and build
  COPY src ./src
  
  RUN echo "Building with Maven profile: ${PROFILE}" && \
      mvn package -DskipTests -Dmaven.test.skip=true -B -P${PROFILE}
  
  # ---- Stage 2: Runtime ----
  FROM eclipse-temurin:18-jre
  
  WORKDIR /app
  
  COPY --from=builder /app/target/*.jar app.jar
  
  EXPOSE 8080
  
  ENTRYPOINT ["java", "-jar", "app.jar"]
  