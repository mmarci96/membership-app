# Use the official OpenJDK image as a base image
FROM openjdk:17-jdk-slim AS backend

# Set the working directory in the container
WORKDIR /app

# Copy only the Maven wrapper and pom.xml first (to leverage caching)
COPY ./mvnw ./
COPY ./.mvn .mvn/
COPY pom.xml ./

# Download dependencies (cache this layer)
RUN ./mvnw dependency:go-offline

# Now copy the entire source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Specify the command to run your application
CMD ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar"]
