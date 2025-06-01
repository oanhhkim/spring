package com.example.keycloak.controller;

import com.example.keycloak.dto.AdminDashboardDto;
import com.example.keycloak.dto.UserDto;
import com.example.keycloak.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(adminService.getAdminDashboard(jwt));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllUsers(PageRequest.of(page, size)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PostMapping("/users/{userId}/status")
    public ResponseEntity<UserDto> updateUserStatus(
            @PathVariable String userId,
            @RequestBody Map<String, String> statusUpdate,
            @AuthenticationPrincipal Jwt jwt) {
        String newStatus = statusUpdate.get("status");
        return ResponseEntity.ok(adminService.updateUserStatus(userId, newStatus, jwt));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable String userId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(adminService.deleteUser(userId, jwt));
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSystemLogs() {
        return ResponseEntity.ok(adminService.getSystemLogs());
    }
}