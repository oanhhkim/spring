# Keycloak Spring Boot Demo

A comprehensive demonstration of integrating Keycloak authentication and authorization with Spring Boot 3 using OAuth2/OpenID Connect.

## Features

- 🔐 OAuth2 Authorization Code Flow
- 👥 Role-Based Access Control (USER, ADMIN, SUPER_ADMIN)
- 🚀 Spring Boot 3 with Java 17
- 🐳 Dockerized Keycloak setup
- 📝 Comprehensive documentation and testing guide
- ✅ Integration test examples

## Prerequisites

- Java 17
- Maven 3.6+
- Docker and Docker Compose
- Postman (for API testing)

## Quick Start

### 1. Start Keycloak

```bash
docker-compose up -d
```

### 2. Configure Keycloak

1. Access Keycloak Admin Console: http://localhost:8180
2. Login with `admin`/`admin`
3. Import the realm configuration or follow the manual setup in `TESTING_GUIDE.md`

### 3. Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on http://localhost:8080

## Project Structure

```
keycloak/
├── src/
│   ├── main/
│   │   ├── java/com/example/keycloak/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java      # OAuth2 Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── PublicController.java    # Public endpoints
│   │   │   │   ├── UserController.java      # User role endpoints
│   │   │   │   └── AdminController.java     # Admin role endpoints
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties       # Keycloak configuration
│   └── test/                                # Integration tests
├── docker-compose.yml                       # Keycloak + PostgreSQL setup
├── realm-export.json                        # Keycloak realm template
├── KEYCLOAK_GUIDE.md                       # Comprehensive Keycloak guide
└── TESTING_GUIDE.md                        # Detailed testing instructions
```

## API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /api/public/health` - Health check
- `GET /api/public/info` - Application info

### User Endpoints (USER or ADMIN Role Required)
- `GET /api/user/profile` - Get user profile
- `GET /api/user/data` - Get user data
- `POST /api/user/preferences` - Update preferences

### Admin Endpoints (ADMIN Role Required)
- `GET /api/admin/dashboard` - Admin dashboard
- `GET /api/admin/users` - List all users
- `GET /api/admin/users/{userId}` - Get specific user
- `POST /api/admin/users/{userId}/status` - Update user status
- `DELETE /api/admin/users/{userId}` - Delete user

### Super Admin Endpoints (SUPER_ADMIN Role Required)
- `GET /api/admin/logs` - System logs

## Test Users

| Username | Password | Roles |
|----------|----------|-------|
| testuser | password123 | USER |
| testadmin | password123 | ADMIN, USER |
| superadmin | password123 | SUPER_ADMIN |

## Testing

### Get Access Token
```bash
curl -X POST http://localhost:8180/realms/demo-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=spring-boot-app" \
  -d "client_secret=your-client-secret" \
  -d "username=testuser" \
  -d "password=password123"
```

### Call Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Documentation

- [Keycloak Integration Guide](KEYCLOAK_GUIDE.md) - Architecture, features, and best practices
- [Testing Guide](TESTING_GUIDE.md) - Step-by-step testing instructions with Postman

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SecurityIntegrationTest
```

## Troubleshooting

If you encounter issues:

1. Ensure Docker containers are running: `docker-compose ps`
2. Check Keycloak logs: `docker-compose logs keycloak`
3. Verify application properties match your Keycloak configuration
4. See `TESTING_GUIDE.md` for common issues and solutions

## License

This is a demonstration project for educational purposes.