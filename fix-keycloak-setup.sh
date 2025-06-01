#!/bin/bash

echo "=== Fixing Keycloak Setup ==="

# Get admin token
echo "1. Getting admin token..."
TOKEN_RESPONSE=$(curl -s -X POST "http://localhost:8180/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli")

TOKEN=$(echo $TOKEN_RESPONSE | sed 's/.*"access_token":"\([^"]*\)".*/\1/')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "$TOKEN_RESPONSE" ]; then
  echo "❌ Failed to get admin token"
  exit 1
fi
echo "✅ Got admin token"

# Check if client exists
echo -e "\n2. Checking client configuration..."
CLIENT_CHECK=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/clients" \
  -H "Authorization: Bearer $TOKEN" | grep -o '"clientId":"spring-boot-app"')

if [ -z "$CLIENT_CHECK" ]; then
  echo "❌ Client 'spring-boot-app' not found"
else
  echo "✅ Client exists"
  
  # Get client details
  CLIENT_ID=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/clients" \
    -H "Authorization: Bearer $TOKEN" | sed -n 's/.*"id":"\([^"]*\)".*"clientId":"spring-boot-app".*/\1/p')
  
  echo "Client UUID: $CLIENT_ID"
  
  # Update client secret
  echo -e "\n3. Updating client secret..."
  curl -s -X POST "http://localhost:8180/admin/realms/demo/clients/$CLIENT_ID/client-secret" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "type": "secret",
      "value": "crWUk56hbcZSHE4NPzi0ywFxUcAYHrEE"
    }'
  
  # Get new secret
  SECRET_RESPONSE=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/clients/$CLIENT_ID/client-secret" \
    -H "Authorization: Bearer $TOKEN")
  
  echo "Client secret response: $SECRET_RESPONSE"
fi

# Check users
echo -e "\n4. Checking users..."
USERS=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/users" \
  -H "Authorization: Bearer $TOKEN")

echo "Current users:"
echo $USERS | grep -o '"username":"[^"]*"' | sed 's/"username":"//g' | sed 's/"//g'

# Create test user if not exists
if [[ ! $USERS == *"testuser"* ]]; then
  echo -e "\n5. Creating testuser..."
  curl -s -X POST "http://localhost:8180/admin/realms/demo/users" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "username": "testuser",
      "enabled": true,
      "emailVerified": true,
      "email": "testuser@example.com",
      "firstName": "Test",
      "lastName": "User",
      "credentials": [{
        "type": "password",
        "value": "password123",
        "temporary": false
      }]
    }'
  
  # Get user ID
  USER_ID=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/users?username=testuser" \
    -H "Authorization: Bearer $TOKEN" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')
  
  # Assign USER role
  echo "Assigning USER role..."
  # First get role ID
  ROLE_ID=$(curl -s -X GET "http://localhost:8180/admin/realms/demo/roles" \
    -H "Authorization: Bearer $TOKEN" | sed -n 's/.*"id":"\([^"]*\)".*"name":"USER".*/\1/p')
  
  if [ ! -z "$ROLE_ID" ]; then
    curl -s -X POST "http://localhost:8180/admin/realms/demo/users/$USER_ID/role-mappings/realm" \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" \
      -d "[{\"id\":\"$ROLE_ID\",\"name\":\"USER\"}]"
  fi
fi

# Test login
echo -e "\n6. Testing login..."
LOGIN_TEST=$(curl -s -X POST "http://localhost:8180/realms/demo/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=spring-boot-app" \
  -d "client_secret=crWUk56hbcZSHE4NPzi0ywFxUcAYHrEE" \
  -d "username=testuser" \
  -d "password=password123")

if [[ $LOGIN_TEST == *"access_token"* ]]; then
  echo "✅ Login successful!"
else
  echo "❌ Login failed:"
  echo $LOGIN_TEST
fi

echo -e "\n=== Setup Complete ===">