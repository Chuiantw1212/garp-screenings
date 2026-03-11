# garp-screenings

## How to Build and Run

### Prerequisites

* Java 17
* Maven
* Docker

### 1. Package the application using Maven

```sh
mvn clean package
```

This will create a JAR file in the `target` directory.

### 2. Build the Docker image

```sh
docker build -t screening-service .
```

### 3. Run the application using Docker

```sh
docker run -p 8080:8080 screening-service
```

The application will be available at `http://localhost:8080`.
