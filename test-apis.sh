#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "=== Testing Keycloak Spring Boot APIs ==="

# 1. Test public endpoints
echo -e "\n${GREEN}1. Testing Public Endpoints (No Auth)${NC}"
echo "GET /api/public/health:"
curl -s http://localhost:8080/api/public/health | jq . || echo "Response: $(curl -s http://localhost:8080/api/public/health)"

echo -e "\nGET /api/public/info:"
curl -s http://localhost:8080/api/public/info | jq . || echo "Response: $(curl -s http://localhost:8080/api/public/info)"

# 2. Get access token for testuser
echo -e "\n${GREEN}2. Getting Access Token for testuser${NC}"
TOKEN_RESPONSE=$(curl -s -X POST "http://localhost:8180/realms/demo/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=spring-boot-app" \
  -d "client_secret=crWUk56hbcZSHE4NPzi0ywFxUcAYHrEE" \
  -d "username=testuser" \
  -d "password=password123" \
  -d "scope=openid profile email roles")

USER_TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"access_token":"[^"]*' | sed 's/"access_token":"//')

if [ -z "$USER_TOKEN" ]; then
  echo -e "${RED}Failed to get user token${NC}"
  echo "Response: $TOKEN_RESPONSE"
else
  echo -e "${GREEN}Got user token successfully${NC}"
fi

# 3. Test user endpoints
echo -e "\n${GREEN}3. Testing User Endpoints${NC}"
echo "GET /api/user/profile:"
curl -s -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/user/profile | jq . || echo "Response: $(curl -s -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/user/profile)"

echo -e "\nGET /api/user/data:"
curl -s -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/user/data | jq . || echo "Response: $(curl -s -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/user/data)"

# 4. Get access token for admin
echo -e "\n${GREEN}4. Getting Access Token for testadmin${NC}"
ADMIN_RESPONSE=$(curl -s -X POST "http://localhost:8180/realms/demo/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=spring-boot-app" \
  -d "client_secret=crWUk56hbcZSHE4NPzi0ywFxUcAYHrEE" \
  -d "username=testadmin" \
  -d "password=password123" \
  -d "scope=openid profile email roles")

ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | grep -o '"access_token":"[^"]*' | sed 's/"access_token":"//')

if [ -z "$ADMIN_TOKEN" ]; then
  echo -e "${RED}Failed to get admin token${NC}"
  echo "Response: $ADMIN_RESPONSE"
else
  echo -e "${GREEN}Got admin token successfully${NC}"
fi

# 5. Test admin endpoints
echo -e "\n${GREEN}5. Testing Admin Endpoints${NC}"
echo "GET /api/admin/dashboard:"
curl -s -H "Authorization: Bearer $ADMIN_TOKEN" http://localhost:8080/api/admin/dashboard | jq . || echo "Response: $(curl -s -H "Authorization: Bearer $ADMIN_TOKEN" http://localhost:8080/api/admin/dashboard)"

echo -e "\nGET /api/admin/users:"
curl -s -H "Authorization: Bearer $ADMIN_TOKEN" "http://localhost:8080/api/admin/users?page=0&size=5" | jq . || echo "Response: $(curl -s -H "Authorization: Bearer $ADMIN_TOKEN" "http://localhost:8080/api/admin/users?page=0&size=5")"

# 6. Test unauthorized access
echo -e "\n${GREEN}6. Testing Unauthorized Access${NC}"
echo "User trying to access admin endpoint (should fail):"
curl -s -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/admin/dashboard -w "\nHTTP Status: %{http_code}\n"

echo -e "\n=== Test Complete ==="/antml:parameter>
</invoke>