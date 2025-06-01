package com.example.keycloak.service.impl;

import com.example.keycloak.dto.PreferencesDto;
import com.example.keycloak.dto.UserDataDto;
import com.example.keycloak.dto.UserProfileDto;
import com.example.keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public UserProfileDto getUserProfile(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        log.info("Getting profile for user: {}", username);
        
        return UserProfileDto.builder()
                .username(username)
                .email(jwt.getClaimAsString("email"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .roles(extractRoles(jwt))
                .tokenIssuedAt(jwt.getIssuedAt())
                .tokenExpiresAt(jwt.getExpiresAt())
                .build();
    }

    @Override
    public UserDataDto getUserData(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        log.info("Getting data for user: {}", username);
        
        Map<String, Object> userData = Map.of(
                "orders", 5,
                "lastLogin", "2024-01-20",
                "preferences", Map.of(
                        "theme", "dark",
                        "language", "en"
                )
        );
        
        return UserDataDto.builder()
                .message("User specific data")
                .username(username)
                .data(userData)
                .build();
    }

    @Override
    public PreferencesDto updateUserPreferences(Jwt jwt, Map<String, Object> preferences) {
        String username = jwt.getClaimAsString("preferred_username");
        log.info("Updating preferences for user: {}", username);
        
        // In a real application, this would save to database
        // For demo purposes, we're just returning the updated preferences
        
        return PreferencesDto.builder()
                .status("success")
                .message("Preferences updated successfully")
                .username(username)
                .updatedPreferences(preferences)
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return List.of();
    }
}