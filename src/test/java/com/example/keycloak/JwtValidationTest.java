package com.example.keycloak;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtValidationTest {

    @Autowired
    private JwtValidationExample jwtValidation;

    @Test
    public void testJwtValidation() {
        // Example JWT token (expired - for demo only)
        String sampleToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOMWFmZDJ...";
        
        // This would fail with expired token
        // boolean isValid = jwtValidation.validateJwtManually(sampleToken);
        
        // Show how Spring Boot validates
        jwtValidation.explainSpringBootValidation();
    }
}