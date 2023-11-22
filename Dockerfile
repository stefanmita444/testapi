# Use the official OpenJDK image as the base image
FROM amazoncorretto:17
# Set the working directory
WORKDIR /app
# Copy the JAR file from the target folder to the working directory
COPY target/*.jar ezer.jar
# Expose the default port for the Spring Boot application
EXPOSE 8080
# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "ezer.jar"]