# Spring Boot API Key Starter

Spring Boot starter template demonstrating API Key authentication with Role-Based Access Control (RBAC), database persistence, and OpenAPI integration.

## Features

- **API Key Authentication**: Secure REST endpoints using custom header-based API keys.
- **Database Persistence**: Dynamic API key management supporting H2 (default), PostgreSQL, and Oracle.
- **Role-Based Access Control (RBAC)**: Assign granular roles (e.g., `ROLE_USER`, `ROLE_ADMIN`) to API keys.
- **OpenAPI 3 (Swagger)**: Built-in API documentation with pre-configured API Key security schemes.

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Running the Application
By default, the application uses an in-memory H2 database.

```bash
./mvnw spring-boot:run
```

To use specific database profiles:

```bash
# PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres

# Oracle
./mvnw spring-boot:run -Dspring-boot.run.profiles=oracle
```

## API Documentation
Once running, you can access the Swagger UI to explore and test the endpoints:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Endpoints
| Path | Required Role | Description |
|------|---------------|-------------|
| `/greeting` | `ROLE_USER` | Simple greeting service. |
| `/test/sysdate1` | `ROLE_USER` | Returns current system date. |
| `/admin` | `ROLE_ADMIN` | Protected administrative endpoint. |

## Testing

```bash
./mvnw test
```