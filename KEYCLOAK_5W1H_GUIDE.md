# Hướng dẫn Keycloak với Spring Boot theo mô hình 5W1H

## 1. WHAT - Keycloak là gì?

### 1.1 Định nghĩa
Keycloak là một **Identity and Access Management (IAM)** solution mã nguồn mở do Red Hat phát triển.

### 1.2 Chức năng chính
- **Authentication** (Xác thực): Xác định "bạn là ai?"
- **Authorization** (Phân quyền): Xác định "bạn được làm gì?"
- **Single Sign-On (SSO)**: Đăng nhập một lần, truy cập nhiều ứng dụng
- **User Management**: Quản lý người dùng tập trung
- **OAuth2/OIDC Provider**: Cung cấp token cho ứng dụng

### 1.3 Các thành phần cơ bản

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│   Browser   │────►│  Spring Boot    │────►│  Keycloak   │
│   (User)    │     │  Application    │     │   Server    │
└─────────────┘     └─────────────────┘     └─────────────┘
                           │                       │
                           └──────┬────────────────┘
                                  │
                           ┌──────▼──────┐
                           │ PostgreSQL  │
                           └─────────────┘
```

### 1.4 Thuật ngữ quan trọng
- **Realm**: Không gian quản lý độc lập (như một tenant)
- **Client**: Ứng dụng kết nối với Keycloak
- **Role**: Vai trò/quyền hạn (USER, ADMIN)
- **JWT Token**: JSON Web Token chứa thông tin xác thực

---

## 2. WHY - Tại sao dùng Keycloak?

### 2.1 Lợi ích chính
✅ **Bảo mật tập trung**: Một nơi quản lý authentication/authorization
✅ **Tiết kiệm thời gian**: Không cần tự build hệ thống login
✅ **Chuẩn mực**: Tuân theo OAuth2/OIDC standards
✅ **SSO Support**: Người dùng đăng nhập một lần
✅ **Multi-factor Authentication**: Hỗ trợ 2FA
✅ **Social Login**: Tích hợp Google, Facebook, GitHub

### 2.2 So sánh với giải pháp khác

| Tiêu chí | Keycloak | Auth0 | Okta | Spring Security (only) |
|----------|----------|-------|------|------------------------|
| Mã nguồn mở | ✅ | ❌ | ❌ | ✅ |
| Chi phí | Free | Paid | Paid | Free |
| SSO | ✅ | ✅ | ✅ | ❌ |
| User Management UI | ✅ | ✅ | ✅ | ❌ |
| Self-hosted | ✅ | ❌ | ❌ | N/A |

---

## 3. WHEN - Khi nào nên dùng Keycloak?

### 3.1 Nên dùng khi:
- 🔸 Có nhiều ứng dụng cần SSO
- 🔸 Cần quản lý user/role phức tạp
- 🔸 Muốn tách biệt authentication khỏi business logic
- 🔸 Cần hỗ trợ nhiều phương thức đăng nhập
- 🔸 Dự án enterprise với yêu cầu bảo mật cao

### 3.2 Không nên dùng khi:
- ❌ Ứng dụng đơn giản với ít user
- ❌ Không có resource cho việc maintain thêm service
- ❌ Chỉ cần basic authentication

---

## 4. WHERE - Keycloak chạy ở đâu?

### 4.1 Deployment Options

```yaml
# docker-compose.yml
services:
  keycloak:
    image: quay.io/keycloak/keycloak:24.0
    ports:
      - "8180:8180"
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres:5432/keycloak
    depends_on:
      - postgres

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: keycloak
      POSTGRES_PASSWORD: password
```

### 4.2 Các endpoints quan trọng

| Endpoint | URL | Mục đích |
|----------|-----|----------|
| Admin Console | http://localhost:8180/admin | Quản lý Keycloak |
| Account Console | http://localhost:8180/realms/{realm}/account | User tự quản lý account |
| OpenID Configuration | http://localhost:8180/realms/{realm}/.well-known/openid-configuration | Metadata |
| Token Endpoint | http://localhost:8180/realms/{realm}/protocol/openid-connect/token | Lấy token |
| JWK Set | http://localhost:8180/realms/{realm}/protocol/openid-connect/certs | Public keys |

---

## 5. WHO - Ai sử dụng Keycloak?

### 5.1 Các bên liên quan

```
┌─────────────────┐
│   End Users     │ ─── Người dùng cuối đăng nhập
└────────┬────────┘
         │
┌────────▼────────┐
│   Developers    │ ─── Tích hợp Keycloak vào app
└────────┬────────┘
         │
┌────────▼────────┐
│  System Admin   │ ─── Quản lý Keycloak server
└────────┬────────┘
         │
┌────────▼────────┐
│ Security Admin  │ ─── Cấu hình realm, roles, policies
└─────────────────┘
```

### 5.2 Roles trong hệ thống demo

| Role | Quyền hạn | API Access |
|------|-----------|------------|
| USER | Quyền cơ bản | `/api/user/**` |
| ADMIN | Quản trị | `/api/admin/**` |
| SUPER_ADMIN | Toàn quyền | `/api/admin/logs` |

---

## 6. HOW - Cách triển khai Keycloak?

### 6.1 Quick Start (5 phút)

```bash
# 1. Clone project
git clone <repository-url>
cd keycloak

# 2. Start Keycloak & Database
docker-compose up -d

# 3. Import realm configuration
./import-realm-correct.sh

# 4. Start Spring Boot app
./mvnw spring-boot:run

# 5. Test
curl http://localhost:8080/api/public/health
```

### 6.2 Cấu hình Spring Boot

#### Step 1: Dependencies (pom.xml)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

#### Step 2: Application Properties
```properties
# Keycloak configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8180/realms/demo
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/demo/protocol/openid-connect/certs
```

#### Step 3: Security Configuration
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .build();
    }
}
```

### 6.3 Authentication Flow

```
1. User Login
   POST http://localhost:8180/realms/demo/protocol/openid-connect/token
   Body: username=testuser&password=password123&grant_type=password&client_id=spring-boot-app

2. Receive Token
   {
     "access_token": "eyJhbGc...",
     "expires_in": 300,
     "token_type": "Bearer"
   }

3. Use Token
   GET http://localhost:8080/api/user/profile
   Headers: Authorization: Bearer eyJhbGc...

4. Spring Boot validates token automatically
```

### 6.4 Testing với Postman

1. Import collection: `Keycloak-Demo.postman_collection.json`
2. Import environment: `Keycloak-Demo.postman_environment.json`
3. Run requests theo thứ tự:
   - Public endpoints (không cần login)
   - Login as User
   - User endpoints
   - Login as Admin
   - Admin endpoints

### 6.5 Troubleshooting

| Vấn đề | Nguyên nhân | Giải pháp |
|--------|-------------|-----------|
| 401 Unauthorized | Token hết hạn hoặc invalid | Login lại để lấy token mới |
| 403 Forbidden | Không đủ quyền | Kiểm tra role của user |
| Connection refused | Keycloak chưa start | `docker-compose up -d` |
| Invalid issuer | Config sai issuer URI | Check application.properties |

---

## 7. Tổng kết

### Quick Reference Card

```
🔑 Keycloak Admin: http://localhost:8180/admin (admin/admin)
🚀 Spring Boot App: http://localhost:8080
📘 API Docs: http://localhost:8080/swagger-ui.html

Test Users:
- testuser/password123 (Role: USER)
- testadmin/password123 (Roles: ADMIN, USER)
- superadmin/password123 (Role: SUPER_ADMIN)

Key Commands:
- Start: docker-compose up -d
- Stop: docker-compose down
- Logs: docker-compose logs -f keycloak
- Test: ./test-apis.sh
```

### Next Steps
1. ✅ Customize realm settings
2. ✅ Add custom user attributes
3. ✅ Implement refresh token flow
4. ✅ Add social login providers
5. ✅ Configure password policies