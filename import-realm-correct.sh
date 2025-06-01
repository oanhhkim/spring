#!/bin/bash

echo "=== Importing Keycloak Realm ==="

# Step 1: Get admin token
echo "1. Getting admin access token..."
TOKEN_RESPONSE=$(curl -s -X POST "http://localhost:8180/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli")

TOKEN=$(echo $TOKEN_RESPONSE | sed 's/.*"access_token":"\([^"]*\)".*/\1/')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "$TOKEN_RESPONSE" ]; then
  echo "❌ Failed to get admin token"
  echo "Response: $TOKEN_RESPONSE"
  exit 1
fi

echo "✅ Got access token"

# Step 2: Import realm
echo "2. Importing realm from realm-export.json..."
IMPORT_RESPONSE=$(curl -s -X POST "http://localhost:8180/admin/realms" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d @realm-export.json)

# Check if import was successful
if [ -z "$IMPORT_RESPONSE" ]; then
  echo "✅ Realm imported successfully!"
else
  echo "❌ Import response: $IMPORT_RESPONSE"
  echo "If realm already exists, you may need to delete it first."
fi

# Step 3: Verify realm exists
echo "3. Verifying realm..."
REALM_CHECK=$(curl -s http://localhost:8180/realms/demo)

if [[ $REALM_CHECK == *"\"realm\":\"demo\""* ]]; then
  echo "✅ Realm 'demo' is accessible!"
  echo ""
  echo "=== Import completed successfully! ==="
  echo ""
  echo "Test users created:"
  echo "- testuser/password123 (role: USER)"
  echo "- testadmin/password123 (roles: ADMIN, USER)"
  echo "- superadmin/password123 (role: SUPER_ADMIN)"
else
  echo "❌ Failed to verify realm"
  echo "Response: $REALM_CHECK"
  exit 1
fi