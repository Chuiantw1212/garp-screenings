# ---- Build Stage ----
# Use a Maven image to build the application
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
# Copy pom.xml and download dependencies first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy the rest of the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Final Stage ----
# Use a lightweight JRE image to run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the JAR file from the build stage
COPY --from=build /app/target/screening-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
# Set the entrypoint to run the application
ENTRYPOINT ["java","-jar","app.jar"]
