package com.example.keycloak.service;

import com.example.keycloak.dto.AdminDashboardDto;
import com.example.keycloak.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public interface AdminService {
    AdminDashboardDto getAdminDashboard(Jwt jwt);
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(String userId);
    UserDto updateUserStatus(String userId, String status, Jwt jwt);
    Map<String, Object> deleteUser(String userId, Jwt jwt);
    List<Map<String, Object>> getSystemLogs();
}