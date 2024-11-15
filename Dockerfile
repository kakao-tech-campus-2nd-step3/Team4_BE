# Base image for running Java applications
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the boot jar from the build context to the container
COPY build/libs/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]