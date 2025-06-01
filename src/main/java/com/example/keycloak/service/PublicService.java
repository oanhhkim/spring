package com.example.keycloak.service;

import java.util.Map;

public interface PublicService {
    Map<String, Object> getHealthStatus();
    Map<String, Object> getApplicationInfo();
}