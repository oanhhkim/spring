# Tài liệu nghiên cứu chi tiết về Keycloak và Spring Boot Integration

## Mục lục
1. [Tổng quan về dự án](#1-tổng-quan-về-dự-án)
2. [Kiến trúc và công nghệ](#2-kiến-trúc-và-công-nghệ)
3. [Keycloak - Identity and Access Management](#3-keycloak---identity-and-access-management)
4. [Cấu hình Spring Security với OAuth2](#4-cấu-hình-spring-security-với-oauth2)
5. [Luồng xác thực và phân quyền](#5-luồng-xác-thực-và-phân-quyền)
6. [Cấu trúc mã nguồn chi tiết](#6-cấu-trúc-mã-nguồn-chi-tiết)
7. [API Endpoints và bảo mật](#7-api-endpoints-và-bảo-mật)
8. [Docker và môi trường phát triển](#8-docker-và-môi-trường-phát-triển)
9. [Testing và best practices](#9-testing-và-best-practices)

---

## 1. Tổng quan về dự án

### 1.1 Mục đích
Dự án demo này minh họa cách tích hợp Keycloak với Spring Boot để thực hiện:
- Authentication (Xác thực người dùng)
- Authorization (Phân quyền dựa trên vai trò)
- OAuth2/OpenID Connect integration
- JWT token validation
- Role-based access control (RBAC)

### 1.2 Công nghệ sử dụng
- **Java 17**: Ngôn ngữ lập trình chính
- **Spring Boot 3.5.0**: Framework phát triển ứng dụng
- **Spring Security**: Bảo mật ứng dụng
- **Keycloak 24.0**: Identity Provider (IdP)
- **PostgreSQL**: Database cho Keycloak
- **Docker & Docker Compose**: Container hóa services
- **Maven**: Build tool

---

## 2. Kiến trúc và công nghệ

### 2.1 Kiến trúc tổng thể
```
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│   Client    │────►│  Spring Boot    │────►│  Keycloak   │
│ (Browser/   │     │  Application    │     │   Server    │
│   Postman)  │◄────│  (Port 8080)    │◄────│ (Port 8180) │
└─────────────┘     └─────────────────┘     └─────────────┘
                             │                       │
                             └───────┬───────────────┘
                                     │
                              ┌──────▼──────┐
                              │ PostgreSQL  │
                              │  Database   │
                              └─────────────┘
```

### 2.2 Các thành phần chính

#### 2.2.1 **Keycloak Server (Identity Provider)**
- **Port**: 8180
- **Vai trò**: Quản lý identity và access management
- **Chức năng chính**:
  - Authentication (xác thực người dùng)
  - Authorization (cấp quyền)
  - User management
  - Token issuance (cấp phát JWT tokens)
  - Single Sign-On (SSO)
  - Social login integration

#### 2.2.2 **Spring Boot Application (Resource Server)**
- **Port**: 8080
- **Vai trò**: API server cung cấp business logic
- **Chức năng chính**:
  - Validate JWT tokens từ Keycloak
  - Enforce authorization rules
  - Serve protected resources
  - Handle business logic
  - RESTful API endpoints

#### 2.2.3 **PostgreSQL Database**
- **Port**: 5432
- **Vai trò**: Lưu trữ persistent data cho Keycloak
- **Data stored**:
  - Users và profiles
  - Roles và permissions
  - Clients và configurations
  - Sessions và tokens
  - Audit logs

#### 2.2.4 **Client Applications**
- Browser-based apps (SPA, MPA)
- Mobile applications
- API clients (Postman, cURL)
- Microservices
- Third-party applications

### 2.3 Luồng hoạt động chi tiết

#### 2.3.1 **Authentication Flow (Luồng xác thực)**

```
1. Initial Request
   Client ──────► Spring Boot
                     │
                     ▼
              [No valid token?]
                     │
   Client ◄────── Redirect to Keycloak login
     │
     ▼
   Keycloak Login Page
     │
     ▼
   Enter credentials
     │
     ▼
   Keycloak validates ──────► PostgreSQL
     │                         (Check user/password)
     ▼
   Generate JWT token
     │
     ▼
   Client ◄────── Return with token
     │
     ▼
   Store token (localStorage/cookie)
```

#### 2.3.2 **Authorization Flow (Luồng phân quyền)**

```
1. Client sends request với JWT token
   GET /api/user/profile
   Authorization: Bearer eyJhbGciOiJSUzI1NiIs...
   
2. Spring Security Filter Chain
   ├── BearerTokenAuthenticationFilter
   │   └── Extract JWT from Authorization header
   │
   ├── JwtAuthenticationProvider
   │   ├── Validate JWT signature (using Keycloak public key)
   │   ├── Check token expiry
   │   └── Verify issuer
   │
   └── JwtAuthenticationConverter
       ├── Extract claims from JWT
       ├── Extract roles (realm_access + resource_access)
       └── Create Authentication object

3. Security Context Setup
   ├── Create JwtAuthenticationToken
   ├── Set user principal (from JWT subject)
   └── Set granted authorities (ROLE_USER, ROLE_ADMIN)

4. Authorization Decision
   ├── Match request path với security rules
   ├── Check required roles
   ├── Evaluate @PreAuthorize expressions
   └── Allow/Deny access
```

### 2.4 Security Architecture

#### 2.4.1 **JWT Token Structure**
```json
{
  "header": {
    "alg": "RS256",
    "typ": "JWT",
    "kid": "key-id"
  },
  "payload": {
    "exp": 1706707200,
    "iat": 1706703600,
    "jti": "unique-token-id",
    "iss": "http://localhost:8180/realms/demo-realm",
    "aud": "account",
    "sub": "user-uuid-12345",
    "typ": "Bearer",
    "azp": "spring-boot-app",
    "session_state": "session-id",
    "preferred_username": "testuser",
    "email": "test@example.com",
    "email_verified": true,
    "given_name": "Test",
    "family_name": "User",
    "realm_access": {
      "roles": ["USER", "offline_access"]
    },
    "resource_access": {
      "spring-boot-app": {
        "roles": ["custom-role"]
      }
    },
    "scope": "openid email profile"
  },
  "signature": "RS256-signed-data..."
}
```


### 2.9 Dependencies quan trọng
```xml
<!-- OAuth2 Resource Server để validate JWT tokens -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- OAuth2 Client để handle login flow -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

---

## 3. Keycloak - Identity and Access Management

### 3.1 Khái niệm cơ bản

#### Realm
- **Definition**: Một không gian độc lập quản lý users, roles, clients
- **Demo realm**: `demo-realm`
- **Purpose**: Cô lập các môi trường khác nhau (dev, staging, production)

#### Client
- **Definition**: Ứng dụng kết nối với Keycloak
- **Demo client**: `spring-boot-app`
- **Type**: Confidential client (có client secret)
- **Grant types**: Authorization Code, Direct Access Grants

#### Roles
Hệ thống định nghĩa 3 roles chính:
1. **USER**: Quyền cơ bản cho người dùng thông thường
2. **ADMIN**: Quyền quản trị
3. **SUPER_ADMIN**: Quyền cao nhất (composite role = ADMIN + USER)

#### Users
Ba tài khoản demo được tạo sẵn:
- `testuser/password123`: Role USER
- `testadmin/password123`: Roles ADMIN + USER  
- `superadmin/password123`: Role SUPER_ADMIN

### 3.2 Cấu hình Keycloak Server

#### Docker Compose Configuration
```yaml
keycloak:
  image: quay.io/keycloak/keycloak:24.0
  environment:
    KEYCLOAK_ADMIN: admin
    KEYCLOAK_ADMIN_PASSWORD: admin
    KC_DB: postgres
    KC_HTTP_PORT: 8180
  ports:
    - "8180:8180"
```

#### Realm Export Configuration
File `realm-export.json` chứa cấu hình realm:
- Client configurations
- Role definitions
- User accounts
- Security settings

---

## 4. Cấu hình Spring Security với OAuth2

### 4.1 Application Properties
```properties
# JWT Issuer URI - endpoint để verify JWT
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8180/realms/demo-realm

# JWK Set URI - endpoint lấy public keys để verify JWT signature
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/demo-realm/protocol/openid-connect/certs

# OAuth2 Client configuration
spring.security.oauth2.client.registration.keycloak.client-id=spring-boot-app
spring.security.oauth2.client.registration.keycloak.client-secret=your-client-secret
```

### 4.2 Security Configuration Class

#### Cấu trúc SecurityConfig.java
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Cho phép sử dụng @PreAuthorize
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // 1. Disable CSRF (vì là stateless API)
        // 2. Configure stateless session
        // 3. Define authorization rules
        // 4. Configure OAuth2 Resource Server
    }
}
```

#### JWT Authentication Converter
Converter này chịu trách nhiệm:
1. Extract roles từ JWT token
2. Convert Keycloak roles thành Spring Security authorities
3. Xử lý cả realm roles và resource roles

```java
private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
    // Extract từ "realm_access.roles"
    // Extract từ "resource_access.spring-boot-app.roles"
    // Convert sang format ROLE_XXX
}
```

---

## 5. Luồng xác thực và phân quyền

### 5.1 Authorization Code Flow
```
1. User truy cập protected resource
2. App redirect đến Keycloak login page
3. User nhập credentials
4. Keycloak validate và redirect về app với authorization code
5. App exchange code lấy access token
6. App sử dụng token để access resources
```

### 5.2 JWT Token Structure
```json
{
  "exp": 1706707200,
  "iat": 1706703600,
  "sub": "user-id-123",
  "preferred_username": "testuser",
  "email": "testuser@example.com",
  "realm_access": {
    "roles": ["USER"]
  },
  "resource_access": {
    "spring-boot-app": {
      "roles": ["special-role"]
    }
  }
}
```

### 5.3 Request Flow với JWT

#### 5.3.1 Tổng quan luồng xử lý JWT Token

Sau khi Keycloak generate access token, luồng xử lý diễn ra qua các bước sau:

```
┌──────────────┐                    ┌──────────────────┐                    ┌─────────────┐
│   Client     │                    │  Spring Boot App │                    │  Keycloak   │
│  (Browser/   │                    │  (Port 8080)     │                    │   Server    │
│   Postman)   │                    │                  │                    │ (Port 8180) │
└──────┬───────┘                    └────────┬─────────┘                    └──────┬──────┘
       │                                     │                                      │
       │ 1. Login with credentials           │                                      │
       ├─────────────────────────────────────┴──────────────────────────────────────►
       │                                                                            │
       │                                     2. Validate credentials                 │
       │                                     3. Generate JWT Access Token            │
       │                                                                            │
       │◄───────────────────────────────────────────────────────────────────────────┤
       │ 4. Return: { access_token: "eyJhbGc...", expires_in: 300, ... }         │
       │                                                                            │
       │ 5. Store token in client                                                  │
       │                                                                            │
       │ 6. Request: GET /api/user/profile                                         │
       │    Headers: Authorization: Bearer eyJhbGc...                              │
       ├─────────────────────────────────────►                                     │
       │                                     │                                      │
       │                                     │ 7. Extract token from header        │
       │                                     │ 8. Validate token with Keycloak     │
       │                                     ├──────────────────────────────────────►
       │                                     │                                      │
       │                                     │◄─────────────────────────────────────┤
       │                                     │ 9. Return: Token valid + Public Key │
       │                                     │                                      │
       │                                     │ 10. Create Security Context         │
       │                                     │ 11. Execute Controller method       │
       │                                     │                                      │
       │◄────────────────────────────────────┤                                     │
       │ 12. Return: User Profile Data      │                                      │
       │                                     │                                      │
```

#### 5.3.2 Chi tiết từng bước xử lý trong Spring Boot

##### **Bước 1-4: Client nhận JWT token từ Keycloak**

```bash
# Request login
POST http://localhost:8180/realms/demo-realm/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&
client_id=spring-boot-app&
client_secret=your-secret&
username=testuser&
password=password123

# Response
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJON...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "5e7a8b2c-4f3a-4e9a-b1c2-3d4e5f6a7b8c",
  "scope": "openid email profile"
}
```

##### **Bước 5: Client lưu trữ token**

```javascript
// Browser storage
localStorage.setItem('access_token', response.access_token);
localStorage.setItem('refresh_token', response.refresh_token);

// Hoặc in-memory storage cho security tốt hơn
const tokenStore = {
  accessToken: response.access_token,
  refreshToken: response.refresh_token,
  expiresAt: Date.now() + (response.expires_in * 1000)
};
```

##### **Bước 6: Client gửi request với JWT token**

```bash
GET http://localhost:8080/api/user/profile
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOM...
```

##### **Bước 7-11: Spring Security xử lý JWT token**

#### 5.3.3 Spring Security Filter Chain xử lý JWT

```
┌─────────────────────────────────────────────────────────────────────────┐
│                     Spring Security Filter Chain                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  1. BearerTokenAuthenticationFilter                                    │
│     └─► Extract token từ Authorization header                          │
│         └─► Tạo BearerTokenAuthenticationToken                        │
│                                                                         │
│  2. AuthenticationManager                                              │
│     └─► Delegate to JwtAuthenticationProvider                         │
│                                                                         │
│  3. JwtAuthenticationProvider                                          │
│     └─► JwtDecoder.decode(token)                                      │
│         ├─► Validate signature với public key từ Keycloak             │
│         ├─► Check token expiration                                    │
│         ├─► Verify issuer URI                                         │
│         └─► Return Jwt object nếu valid                               │
│                                                                         │
│  4. JwtAuthenticationConverter                                         │
│     └─► Convert Jwt to Authentication                                 │
│         ├─► Extract username từ preferred_username claim              │
│         ├─► Extract roles từ realm_access.roles                       │
│         ├─► Extract resource roles từ resource_access                 │
│         └─► Create JwtAuthenticationToken với authorities             │
│                                                                         │
│  5. SecurityContextHolder                                              │
│     └─► Set Authentication vào SecurityContext                        │
│                                                                         │
│  6. Controller Method Execution                                        │
│     └─► @AuthenticationPrincipal Jwt jwt được inject                  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```


---

## 6. Cấu trúc mã nguồn chi tiết

### 6.1 Controllers Layer

#### PublicController
- **Purpose**: Endpoints không cần authentication
- **Endpoints**:
  - `GET /api/public/health`: Health check
  - `GET /api/public/info`: Application info

#### UserController
- **Purpose**: Endpoints cho authenticated users
- **Security**: Requires role USER hoặc ADMIN
- **Features**:
  - Sử dụng `@AuthenticationPrincipal Jwt` để inject JWT
  - Extract user info từ JWT claims
- **Endpoints**:
  - `GET /api/user/profile`: User profile từ JWT
  - `GET /api/user/data`: Mock user data
  - `POST /api/user/preferences`: Update preferences

#### AdminController
- **Purpose**: Admin-only endpoints
- **Security**: 
  - Class-level: `@PreAuthorize("hasRole('ADMIN')")`
  - Method-level: `@PreAuthorize("hasRole('SUPER_ADMIN')")` cho sensitive operations
- **Endpoints**:
  - `GET /api/admin/dashboard`: Admin dashboard
  - `GET /api/admin/users`: List users (paginated)
  - `GET /api/admin/logs`: System logs (SUPER_ADMIN only)

### 6.2 Service Layer

#### Service Implementation Pattern
```java
@Slf4j              // Lombok logging
@Service            // Spring service bean
@RequiredArgsConstructor  // Constructor injection
public class UserServiceImpl implements UserService {
    
    // Extract info từ JWT
    // Business logic processing
    // Return DTOs
}
```

#### Key Features:
- JWT claim extraction
- Role-based data filtering
- Audit logging với user context

### 6.3 DTOs (Data Transfer Objects)

#### Design Principles:
- Immutable objects với Lombok `@Builder`
- Clear separation từ entities
- Validation annotations khi cần

#### Examples:
- `UserProfileDto`: JWT-based user info
- `AdminDashboardDto`: Aggregated admin data
- `PreferencesDto`: User preferences update response

---

## 7. API Endpoints và bảo mật

### 7.1 Endpoint Security Matrix

| Endpoint | Method | Required Role | Description |
|----------|--------|---------------|-------------|
| `/api/public/**` | ALL | None | Public access |
| `/api/user/**` | ALL | USER or ADMIN | Authenticated users |
| `/api/admin/**` | ALL | ADMIN | Admin functions |
| `/api/admin/logs` | GET | SUPER_ADMIN | Sensitive logs |

### 7.2 Security Annotations

#### Method Security
```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<AdminDashboardDto> getAdminDashboard() {}

@PreAuthorize("hasRole('SUPER_ADMIN')") 
public ResponseEntity<List<Map<String, Object>>> getSystemLogs() {}
```

#### Parameter Injection
```java
@AuthenticationPrincipal Jwt jwt  // Inject JWT token
```

---

## 8. Docker và môi trường phát triển

### 8.1 Docker Services

#### Keycloak Service
- **Image**: quay.io/keycloak/keycloak:24.0
- **Mode**: Development mode (`start-dev`)
- **Database**: PostgreSQL
- **Admin Console**: http://localhost:8180/admin (admin/admin)

#### PostgreSQL Service
- **Image**: postgres:16-alpine
- **Purpose**: Keycloak data persistence
- **Volume**: postgres_data for data persistence

### 8.2 Network Configuration
- Custom bridge network: `keycloak-network`
- Internal communication between services
- Port mappings cho external access

---

## 9. Testing và best practices

### 9.1 Testing với Postman

#### Collection Structure
- Environment variables cho dynamic values
- Pre-request scripts cho token refresh
- Test scripts cho response validation

#### Test Scenarios:
1. **Public Access**: Test endpoints không cần auth
2. **User Access**: Login as user, test user endpoints
3. **Admin Access**: Login as admin, test admin endpoints
4. **Unauthorized Access**: Test access denied scenarios

### 9.2 Security Best Practices

#### Token Management
- Short-lived access tokens (5 minutes)
- Refresh token rotation
- Secure token storage in client

#### Role Design
- Principle of least privilege
- Composite roles cho inheritance
- Resource-specific roles khi cần

#### Configuration Security
- Externalize sensitive configs
- Use environment variables
- Secure client secrets

### 9.3 Development Tips

#### Local Development
1. Start Keycloak first: `docker-compose up -d`
2. Wait for Keycloak ready (check health endpoint)
3. Import realm if needed
4. Start Spring Boot app

#### Debugging
- Enable security debug logs
- Use JWT decoder tools
- Monitor Keycloak admin console

#### Common Issues
- **Token expired**: Check token lifetime settings
- **Role not found**: Verify role extraction logic
- **CORS errors**: Configure CORS in Spring Security

---

## Kết luận

Dự án này demonstrate một implementation hoàn chỉnh của:
- OAuth2/OIDC authentication với Keycloak
- Role-based authorization trong Spring Boot
- JWT token validation và parsing
- Secure REST API design
- Docker-based development environment

Key takeaways:
1. Keycloak cung cấp enterprise-grade IAM solution
2. Spring Security OAuth2 integration rất mạnh mẽ
3. JWT tokens enable stateless authentication
4. Role-based security flexible và scalable
5. Docker simplifies development environment setup