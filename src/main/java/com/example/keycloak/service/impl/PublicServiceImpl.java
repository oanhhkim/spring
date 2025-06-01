package com.example.keycloak.service.impl;

import com.example.keycloak.service.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

    @Override
    public Map<String, Object> getHealthStatus() {
        log.debug("Health check requested");
        
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "Keycloak Demo API"
        );
    }

    @Override
    public Map<String, Object> getApplicationInfo() {
        log.debug("Application info requested");
        
        return Map.of(
                "application", "Spring Boot Keycloak Demo",
                "version", "1.0.0",
                "description", "Public endpoint accessible without authentication"
        );
    }
}