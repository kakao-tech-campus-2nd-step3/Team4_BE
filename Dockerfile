# Base image for running Java 21 applications
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY build/libs/*.jar app.jar

# Expose the application port (8080)
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
