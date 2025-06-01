# Spring Boot Automatic JWT Validation Explained

## Spring Boot tự động validate JWT như thế nào?

### 1. Cấu hình tối thiểu

Bạn chỉ cần cấu hình 2 dòng trong `application.properties`:

```properties
# Tell Spring where to find Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8180/realms/demo

# Tell Spring where to get public keys for signature verification
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/demo/protocol/openid-connect/certs
```

### 2. Spring Boot tự động làm gì?

```
┌─────────────────────────────────────────────────────────────┐
│                  Spring Boot Auto Configuration              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Auto-create JwtDecoder bean                            │
│     └─> Uses jwk-set-uri to fetch public keys              │
│                                                             │
│  2. Auto-configure BearerTokenAuthenticationFilter          │
│     └─> Extracts token from Authorization header           │
│                                                             │
│  3. Auto-validate JWT on EVERY request:                    │
│     ├─> Verify signature with public key                   │
│     ├─> Check expiration (exp claim)                       │
│     ├─> Verify issuer (iss claim)                          │
│     └─> Check not-before time (nbf claim)                  │
│                                                             │
│  4. Auto-populate SecurityContext                          │
│     └─> Set Authentication with JWT principal              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 3. Code thực tế - Bạn KHÔNG cần viết

```java
// ❌ KHÔNG CẦN viết code này - Spring Boot làm tự động
public class ManualJwtValidation {
    
    // Extract token from header - TỰ ĐỘNG
    String token = request.getHeader("Authorization").substring(7);
    
    // Decode and validate - TỰ ĐỘNG
    Jwt jwt = jwtDecoder.decode(token);
    
    // Create Authentication - TỰ ĐỘNG
    Authentication auth = new JwtAuthenticationToken(jwt);
    
    // Set SecurityContext - TỰ ĐỘNG
    SecurityContextHolder.getContext().setAuthentication(auth);
}
```

### 4. Code bạn CẦN viết

```java
// ✅ Chỉ cần configure security rules
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt())  // ← This enables auto-validation!
        .build();
}

// ✅ Use validated JWT in controller
@GetMapping("/profile")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal Jwt jwt) {
    // JWT đã được validate tự động!
    String username = jwt.getClaimAsString("preferred_username");
    return ResponseEntity.ok(Map.of("username", username));
}
```

### 5. Validation Flow Timeline

```
Request Timeline:
─────────────────────────────────────────────────────────────────►

[Client]          [Spring Security Filters]        [Your Controller]
   │                        │                              │
   ├─── Request ──────────►│                              │
   │ Authorization: Bearer  │                              │
   │ eyJhbGc...            │                              │
   │                       │                              │
   │                       ├─ Extract token               │
   │                       ├─ Validate signature          │
   │                       ├─ Check expiration            │
   │                       ├─ Verify issuer               │
   │                       ├─ Create Authentication       │
   │                       │                              │
   │                       ├──── If valid ───────────────►│
   │                       │                              ├─ Execute
   │                       │                              │  method
   │◄──────────────────────┼──────────────────────────────┤
   │                       │          Response            │
   │                       │                              │
   │                       ├─ If invalid                 │
   │◄──────────────────────┤                              │
   │   401 Unauthorized    │                              │
```

### 6. What happens behind the scenes?

```java
// Spring Boot auto-configures these beans:

@Configuration
@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
public class OAuth2ResourceServerJwtConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder() {
        // Auto-created from your properties
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
    
    @Bean
    BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter() {
        // Auto-extracts and validates tokens
        return new BearerTokenAuthenticationFilter(authenticationManager);
    }
}
```

### 7. Debug để xem validation tự động

```properties
# Add to application.properties to see auto-validation in action
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.oauth2.jwt=TRACE
```

Output logs:
```
DEBUG: BearerTokenAuthenticationFilter : Found bearer token in Authorization header
TRACE: NimbusJwtDecoder : Decoding JWT with algorithm RS256
TRACE: NimbusJwtDecoder : Validating JWT signature with key ID: N1afd2...
DEBUG: JwtAuthenticationProvider : Authenticated token
DEBUG: SecurityContextHolder : Set Authentication: JwtAuthenticationToken
```

### 8. Common Misconceptions

❌ **Myth**: "Tôi cần gọi Keycloak để validate mỗi request"
✅ **Fact**: Spring caches public keys và validate locally

❌ **Myth**: "Tôi cần viết JWT parser"
✅ **Fact**: Spring dùng Nimbus JOSE library tự động

❌ **Myth**: "Tôi cần manual check expiration"
✅ **Fact**: Spring checks tất cả standard claims tự động

### 9. Tóm tắt

```yaml
What Spring Boot does automatically:
  ✅ Extract token from Authorization header
  ✅ Fetch public keys from Keycloak (cached)
  ✅ Verify JWT signature
  ✅ Validate expiration time
  ✅ Check issuer claim
  ✅ Create Authentication object
  ✅ Populate SecurityContext
  ✅ Inject JWT into your controller methods

What you need to do:
  ✅ Add dependencies
  ✅ Configure properties (2 lines)
  ✅ Define security rules
  ✅ Use @AuthenticationPrincipal in controllers
```

**Bottom line**: Spring Boot OAuth2 Resource Server starter handles ALL JWT validation automatically. You just configure and use!