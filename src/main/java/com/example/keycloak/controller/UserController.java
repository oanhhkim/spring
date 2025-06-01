package com.example.keycloak.controller;

import com.example.keycloak.dto.PreferencesDto;
import com.example.keycloak.dto.UserDataDto;
import com.example.keycloak.dto.UserProfileDto;
import com.example.keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.getUserProfile(jwt));
    }

    @GetMapping("/data")
    public ResponseEntity<UserDataDto> getUserData(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.getUserData(jwt));
    }

    @PostMapping("/preferences")
    public ResponseEntity<PreferencesDto> updatePreferences(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, Object> preferences) {
        return ResponseEntity.ok(userService.updateUserPreferences(jwt, preferences));
    }
}