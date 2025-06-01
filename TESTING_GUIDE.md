# Keycloak Spring Boot Testing Guide

## Prerequisites
- Docker and Docker Compose installed
- Java 17 installed
- Maven installed
- Postman or curl for API testing

## Table of Contents
1. [Setting Up Keycloak](#setting-up-keycloak)
2. [Configuring Keycloak](#configuring-keycloak)
3. [Running the Spring Boot Application](#running-the-spring-boot-application)
4. [Testing with Postman](#testing-with-postman)
5. [Testing Different Roles](#testing-different-roles)
6. [Common Issues and Solutions](#common-issues-and-solutions)

## Setting Up Keycloak

### 1. Start Keycloak with Docker Compose

```bash
# From the project root directory
docker-compose up -d

# Check if services are running
docker-compose ps
```

Expected output:
```
NAME                 SERVICE    STATUS    PORTS
keycloak-demo        keycloak   running   0.0.0.0:8180->8180/tcp
keycloak-postgres    postgres   running   0.0.0.0:5432->5432/tcp
```

### 2. Access Keycloak Admin Console

1. Open browser: http://localhost:8180
2. Click "Administration Console"
3. Login with:
   - Username: `admin`
   - Password: `admin`

## Configuring Keycloak

### 1. Create a New Realm

1. Hover over "Master" dropdown in top-left
2. Click "Create Realm"
3. Enter:
   - Realm name: `demo-realm`
4. Click "Create"

### 2. Create Realm Roles

1. Go to "Realm roles" (left menu)
2. Click "Create role"
3. Create the following roles:
   - `USER` - Description: "Regular user role"
   - `ADMIN` - Description: "Administrator role"
   - `SUPER_ADMIN` - Description: "Super administrator role"

### 3. Create Client Application

1. Go to "Clients" (left menu)
2. Click "Create client"
3. General Settings:
   - Client ID: `spring-boot-app`
   - Name: `Spring Boot Application`
4. Click "Next"
5. Capability config:
   - Client authentication: `ON`
   - Authorization: `OFF`
   - Standard flow: `ON`
   - Direct access grants: `ON`
6. Click "Next"
7. Login settings:
   - Valid redirect URIs: `http://localhost:8080/*`
   - Web origins: `http://localhost:8080`
8. Click "Save"

### 4. Get Client Secret

1. Go to Clients → spring-boot-app → Credentials tab
2. Copy the "Client secret"
3. Update `application.properties` with this secret:
   ```properties
   spring.security.oauth2.client.registration.keycloak.client-secret=YOUR_COPIED_SECRET
   ```

### 5. Create Test Users

Navigate to Users (left menu) and create:

#### User 1: Regular User
- Username: `testuser`
- Email: `testuser@example.com`
- First name: `Test`
- Last name: `User`
- Email verified: `Yes`
- Click "Create"
- Go to "Credentials" tab → Set password: `password123` (Temporary: OFF)
- Go to "Role mapping" tab → Assign role: `USER`

#### User 2: Admin User
- Username: `testadmin`
- Email: `testadmin@example.com`
- First name: `Test`
- Last name: `Admin`
- Email verified: `Yes`
- Click "Create"
- Go to "Credentials" tab → Set password: `password123` (Temporary: OFF)
- Go to "Role mapping" tab → Assign roles: `ADMIN` and `USER`

#### User 3: Super Admin
- Username: `superadmin`
- Email: `superadmin@example.com`
- First name: `Super`
- Last name: `Admin`
- Email verified: `Yes`
- Click "Create"
- Go to "Credentials" tab → Set password: `password123` (Temporary: OFF)
- Go to "Role mapping" tab → Assign role: `SUPER_ADMIN`

## Running the Spring Boot Application

```bash
# Build the application
mvn clean package

# Run the application
mvn spring-boot:run
```

The application will start on http://localhost:8080

## Testing with Postman

### 1. Import Postman Environment

Create a new Postman environment with these variables:
- `keycloak_url`: `http://localhost:8180`
- `client_id`: `spring-boot-app`
- `client_secret`: `YOUR_CLIENT_SECRET`
- `realm`: `demo-realm`
- `api_url`: `http://localhost:8080`
- `access_token`: (leave empty, will be set by scripts)

### 2. Get Access Token

**Request: Get Token for Regular User**
```
POST {{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&client_id={{client_id}}
&client_secret={{client_secret}}
&username=testuser
&password=password123
&scope=openid profile email roles
```

**Expected Response:**
```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
    "expires_in": 300,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
    "token_type": "Bearer",
    "scope": "openid profile email roles"
}
```

**Postman Test Script** (add to Tests tab):
```javascript
if (pm.response.code === 200) {
    const jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.access_token);
    pm.environment.set("refresh_token", jsonData.refresh_token);
}
```

### 3. Test Public Endpoints

**Request: Public Health Check**
```
GET {{api_url}}/api/public/health
```

**Expected Response:**
```json
{
    "status": "UP",
    "timestamp": "2024-01-20T10:30:00",
    "service": "Keycloak Demo API"
}
```

**Request: Public Info**
```
GET {{api_url}}/api/public/info
```

**Expected Response:**
```json
{
    "application": "Spring Boot Keycloak Demo",
    "version": "1.0.0",
    "description": "Public endpoint accessible without authentication"
}
```

### 4. Test Protected User Endpoints

**Request: Get User Profile**
```
GET {{api_url}}/api/user/profile
Authorization: Bearer {{access_token}}
```

**Expected Response:**
```json
{
    "username": "testuser",
    "email": "testuser@example.com",
    "firstName": "Test",
    "lastName": "User",
    "roles": ["USER"],
    "tokenIssuedAt": "2024-01-20T10:30:00Z",
    "tokenExpiresAt": "2024-01-20T10:35:00Z"
}
```

**Request: Get User Data**
```
GET {{api_url}}/api/user/data
Authorization: Bearer {{access_token}}
```

**Expected Response:**
```json
{
    "message": "User specific data",
    "username": "testuser",
    "data": {
        "orders": 5,
        "lastLogin": "2024-01-20",
        "preferences": {
            "theme": "dark",
            "language": "en"
        }
    }
}
```

### 5. Test Admin Endpoints (Requires Admin Token)

First, get an admin token:
```
POST {{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&client_id={{client_id}}
&client_secret={{client_secret}}
&username=testadmin
&password=password123
&scope=openid profile email roles
```

**Request: Admin Dashboard**
```
GET {{api_url}}/api/admin/dashboard
Authorization: Bearer {{access_token}}
```

**Expected Response:**
```json
{
    "adminUser": "testadmin",
    "statistics": {
        "totalUsers": 2,
        "activeUsers": 2,
        "newUsersToday": 5,
        "totalRevenue": "$12,345"
    },
    "systemStatus": {
        "api": "operational",
        "database": "operational",
        "cache": "operational"
    },
    "timestamp": "2024-01-20T10:35:00"
}
```

**Request: Get All Users**
```
GET {{api_url}}/api/admin/users?page=0&size=10
Authorization: Bearer {{access_token}}
```

### 6. Test Access Control

**Test 1: User accessing Admin endpoint (Should fail)**
```
GET {{api_url}}/api/admin/dashboard
Authorization: Bearer {{user_access_token}}
```

**Expected Response:**
```json
{
    "error": "Access Denied",
    "message": "You don't have permission to access this resource",
    "status": 403,
    "timestamp": "2024-01-20T10:36:00",
    "path": "/api/admin/dashboard"
}
```

**Test 2: Super Admin accessing special endpoint**

Get super admin token first, then:
```
GET {{api_url}}/api/admin/logs
Authorization: Bearer {{super_admin_token}}
```

## Testing Different Roles

### Role Testing Matrix

| Endpoint | USER | ADMIN | SUPER_ADMIN | No Auth |
|----------|------|-------|-------------|---------|
| /api/public/* | ✅ | ✅ | ✅ | ✅ |
| /api/user/* | ✅ | ✅ | ✅ | ❌ |
| /api/admin/* | ❌ | ✅ | ✅ | ❌ |
| /api/admin/logs | ❌ | ❌ | ✅ | ❌ |

### Testing Script for All Roles

Create a Postman collection with:

1. **Environment Setup**
   - Get tokens for all three users
   - Store in variables: `user_token`, `admin_token`, `super_admin_token`

2. **Test Suite**
   ```javascript
   // Pre-request Script for collection
   const endpoints = [
       '/api/public/health',
       '/api/user/profile',
       '/api/admin/dashboard',
       '/api/admin/logs'
   ];
   
   pm.collectionVariables.set('endpoints', JSON.stringify(endpoints));
   ```

3. **Run Collection**
   - Use Postman Collection Runner
   - Set iterations for each user type
   - Validate response codes

## Common Issues and Solutions

### Issue 1: Token Validation Fails
**Error:** `401 Unauthorized - Invalid or expired token`

**Solutions:**
1. Check token expiration (default 5 minutes)
2. Verify Keycloak is running: `docker-compose ps`
3. Ensure correct issuer URI in application.properties
4. Check clock synchronization between containers

### Issue 2: CORS Errors
**Error:** `Cross-Origin Request Blocked`

**Solutions:**
1. Add CORS configuration to SecurityConfig
2. Ensure Keycloak client has correct Web Origins
3. Check browser console for specific CORS headers

### Issue 3: Role Not Found
**Error:** `403 Forbidden - Access Denied`

**Solutions:**
1. Verify role names match exactly (case-sensitive)
2. Check role mapping in JWT token:
   ```
   # Decode token at jwt.io
   # Look for realm_access.roles claim
   ```
3. Ensure SecurityConfig extracts roles correctly

### Issue 4: Connection Refused
**Error:** `Connection refused to Keycloak`

**Solutions:**
1. Check Docker containers are running
2. Verify port mappings (8180 for Keycloak)
3. Use `localhost` not `127.0.0.1` on some systems
4. Check firewall settings

## Advanced Testing

### 1. Token Refresh Flow
```
POST {{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token
&client_id={{client_id}}
&client_secret={{client_secret}}
&refresh_token={{refresh_token}}
```

### 2. Logout Flow
```
POST {{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/logout
Content-Type: application/x-www-form-urlencoded

client_id={{client_id}}
&client_secret={{client_secret}}
&refresh_token={{refresh_token}}
```

### 3. Token Introspection
```
POST {{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/token/introspect
Content-Type: application/x-www-form-urlencoded
Authorization: Basic {{base64(client_id:client_secret)}}

token={{access_token}}
```

## Performance Testing

### JMeter Test Plan
1. Thread Group: 100 users, 10 seconds ramp-up
2. HTTP Request: Get token
3. HTTP Header Manager: Add Authorization header
4. HTTP Request: Call API endpoints
5. View Results Tree & Summary Report

### Expected Performance
- Token validation: < 50ms
- API response time: < 200ms
- Throughput: > 1000 req/sec (with proper caching)

## Security Best Practices Testing

1. **Test Invalid Tokens**
   - Expired tokens
   - Malformed tokens
   - Tokens from different realm

2. **Test SQL Injection**
   - Try SQL in request parameters
   - Verify proper input validation

3. **Test Rate Limiting**
   - Send rapid requests
   - Verify appropriate throttling

4. **Test Token Leakage**
   - Check logs don't contain tokens
   - Verify tokens not in URLs