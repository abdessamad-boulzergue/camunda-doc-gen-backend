# Camunda Document Generator Backend

This is the backend for the Camunda Document Generator project. It is a Spring Boot application built with Maven.

## Prerequisites

- Java 17+
- Maven

## How to Run

To run the application, use the following command in the terminal:

```bash
mvn spring-boot:run
```

Alternatively, you can build the JAR and run it:

```bash
mvn clean package
java -jar target/camunda-doc-gen-backend-0.0.1-SNAPSHOT.jar
```

## Database

The application uses an H2 in-memory database.
- Database Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## API

The backend runs on port 8080 by default.

## Troubleshooting

### Port 8080 already in use
If you encounter an error stating `Web server failed to start. Port 8080 was already in use`, it means another process is using port 8080.

To resolve this:
1.  Find the process ID (PID):
    ```bash
    lsof -i :8080
    ```
2.  Kill the process:
    ```bash
    kill -9 <PID>
    ```
3.  Restart the application.
