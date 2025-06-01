package com.example.keycloak.service;

import com.example.keycloak.dto.PreferencesDto;
import com.example.keycloak.dto.UserDataDto;
import com.example.keycloak.dto.UserProfileDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

public interface UserService {
    UserProfileDto getUserProfile(Jwt jwt);
    UserDataDto getUserData(Jwt jwt);
    PreferencesDto updateUserPreferences(Jwt jwt, Map<String, Object> preferences);
}