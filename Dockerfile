# Use official OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/your-java-application.jar /app/your-java-application.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "/app/your-java-application.jar"]
