# Use an OpenJDK 21 image
FROM eclipse-temurin:21-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src/ src/

# Give execute permission to Gradle wrapper
RUN chmod +x gradlew

# Build the jar
RUN ./gradlew clean bootJar --no-daemon

# ---- Runtime image ----
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the Spring Boot port
EXPOSE 8081

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
