# garp-screenings

## How to Build and Run

This project uses a multi-stage Docker build, so you don't need to have Maven or Java installed on your machine. You only need Docker.

### Prerequisites

*   Docker

### 1. Build and Package the Application into a Docker Image

Open your terminal in the project root directory and run the following command:

```sh
docker build -t screening-service .
```

This single command will:
1.  Start a Maven container to build your application and create the JAR file.
2.  Create a clean, lightweight final image containing only the Java runtime and your application's JAR file.

### 2. Run the Application

Once the image is built, you can run your application with this command:

```sh
docker run --name screening-service -p 8080:8080 screening-service
```

The application will be available at `http://localhost:8080`.
