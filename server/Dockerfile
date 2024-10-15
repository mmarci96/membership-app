# Use the official OpenJDK image as a base image
FROM openjdk:17-jdk-slim AS backend

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and necessary files
COPY ./mvnw ./
COPY ./.mvn .mvn/

# Copy the project files and source code
COPY pom.xml ./
COPY src ./src

# Run Maven to build the application
RUN ./mvnw clean package -DskipTests

# Specify the command to run your application
CMD ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar"]
