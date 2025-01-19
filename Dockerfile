# OpenJDK base imag
FROM openjdk:17-jdk-slim AS builder

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Working directory
WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

# Build
RUN mvn clean package -DskipTests

# New image
FROM openjdk:17-jdk-slim

# Working directory
WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/target/employee-0.0.1-SNAPSHOT.jar app.jar

# Exposing the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
