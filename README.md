# Event Ticket Platform - Backend

This repository contains the backend services for the Event Ticket Platform project. It is a Spring Boot application written in Java, exposing a RESTful API for managing events, tickets, ticket types, and validation.

## Key Features

- Create, update, publish, and delete events
- Define and manage ticket types for events
- Purchase, validate, and manage tickets
- Secure endpoints using JWT-based authentication
- QR code generation for ticket validation
- Global exception handling and custom error responses

## Project Structure

```
src/main/java/com/example/tickets/          # Main application code
  config/          # Spring configuration classes
  controllers/     # REST controllers
  domain/          # Request/response models and DTOs
  exceptions/      # Custom application exceptions
  filters/         # HTTP filters (e.g., user provisioning)
  mappers/         # MapStruct mappers
  repositories/    # Spring Data JPA repositories
  services/        # Business logic services and implementations
  util/            # Utility classes

src/main/resources/                      # Configuration files (application.properties)

src/test/java/...                        # Unit and integration tests
```

## Requirements

- Java 17 (or compatible)
- Maven 3.6+
- PostgreSQL (or another supported relational database)

## Configuration

The application reads its configuration from `src/main/resources/application.properties`. Key properties include database connection settings and JWT secrets. You can override these with environment variables or by providing a different spring profile.

## Building and Running

1. **Clone the repository**
   ```bash
   git clone <repo-url> tickets
   cd tickets
   ```

2. **Configure the database**
   - Ensure a PostgreSQL database is available.
   - Update `application.properties` with the correct JDBC URL, username, and password.

3. **Build with Maven**
   ```bash
   ./mvnw clean package
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   Alternatively, run the generated JAR:
   ```bash
   java -jar target/tickets-0.0.1-SNAPSHOT.jar
   ```

5. **API access**
   - The application will start on `http://localhost:8080` by default.
   - Use an API client (Postman, curl) to interact with endpoints documented in the controllers.

## Docker

A `docker-compose.yml` is provided to spin up the backend alongside a database. Use:
```bash
docker-compose up --build
```

## Testing

Run unit and integration tests using Maven:
```bash
./mvnw test
```

## Development Tips

- Updating MapStruct mappers may require running `mvnw clean compile` to generate sources.
- Custom exceptions are handled globally by `GlobalExceptionHandler`.
- JWT auth configuration is in `SecurityConfig`.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/foo`)
3. Commit your changes and push to your fork
4. Open a pull request with a clear description of your changes

