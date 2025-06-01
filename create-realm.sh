#!/bin/bash

echo "Creating demo-realm in Keycloak..."

# Get admin token
TOKEN=$(curl -s -X POST "http://localhost:8180/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | grep -o '"access_token":"[^"]*' | sed 's/"access_token":"//')

if [ -z "$TOKEN" ]; then
  echo "Failed to get admin token. Make sure Keycloak is running and admin credentials are correct."
  exit 1
fi

# Import realm from JSON file
curl -s -X POST "http://localhost:8180/admin/realms" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d @realm-export.json

echo "Realm import completed!"

# Verify realm was created
REALM_CHECK=$(curl -s http://localhost:8180/realms/demo-realm)
if [[ $REALM_CHECK == *"realm"* ]]; then
  echo "✓ demo-realm successfully created!"
else
  echo "✗ Failed to create demo-realm"
  exit 1
fi