package com.example.keycloak.service.impl;

import com.example.keycloak.dto.AdminDashboardDto;
import com.example.keycloak.dto.UserDto;
import com.example.keycloak.exception.ResourceNotFoundException;
import com.example.keycloak.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    // In-memory storage for demo purposes
    private final Map<String, UserDto> users = new ConcurrentHashMap<>();

    public AdminServiceImpl() {
        initializeDemoUsers();
    }

    @Override
    public AdminDashboardDto getAdminDashboard(Jwt jwt) {
        String adminUsername = jwt.getClaimAsString("preferred_username");
        log.info("Admin dashboard accessed by: {}", adminUsername);

        Map<String, Object> statistics = Map.of(
                "totalUsers", users.size(),
                "activeUsers", countActiveUsers(),
                "newUsersToday", 5,
                "totalRevenue", "$12,345"
        );

        Map<String, String> systemStatus = Map.of(
                "api", "operational",
                "database", "operational",
                "cache", "operational"
        );

        return AdminDashboardDto.builder()
                .adminUser(adminUsername)
                .statistics(statistics)
                .systemStatus(systemStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Fetching all users - page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());

        List<UserDto> userList = new ArrayList<>(users.values());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userList.size());
        
        List<UserDto> pageContent = userList.subList(start, end);
        
        return new PageImpl<>(pageContent, pageable, userList.size());
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Fetching user with ID: {}", userId);
        
        UserDto user = users.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        
        return user;
    }

    @Override
    public UserDto updateUserStatus(String userId, String status, Jwt jwt) {
        String adminUsername = jwt.getClaimAsString("preferred_username");
        log.info("Admin {} updating user {} status to {}", adminUsername, userId, status);

        UserDto user = users.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        user.setStatus(status);
        user.setLastModifiedBy(adminUsername);
        user.setLastModifiedAt(LocalDateTime.now());

        return user;
    }

    @Override
    public Map<String, Object> deleteUser(String userId, Jwt jwt) {
        String adminUsername = jwt.getClaimAsString("preferred_username");
        log.warn("Admin {} deleting user {}", adminUsername, userId);

        if (!users.containsKey(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        users.remove(userId);

        return Map.of(
                "message", "User deleted successfully",
                "deletedBy", adminUsername,
                "deletedAt", LocalDateTime.now()
        );
    }

    @Override
    public List<Map<String, Object>> getSystemLogs() {
        log.info("Fetching system logs - Super Admin access");
        
        return List.of(
                Map.of(
                        "id", UUID.randomUUID().toString(),
                        "level", "INFO",
                        "message", "System started successfully",
                        "timestamp", LocalDateTime.now().minusHours(2)
                ),
                Map.of(
                        "id", UUID.randomUUID().toString(),
                        "level", "WARN",
                        "message", "High memory usage detected",
                        "timestamp", LocalDateTime.now().minusHours(1)
                ),
                Map.of(
                        "id", UUID.randomUUID().toString(),
                        "level", "INFO",
                        "message", "Database backup completed",
                        "timestamp", LocalDateTime.now().minusMinutes(30)
                )
        );
    }

    private void initializeDemoUsers() {
        users.put("user1", UserDto.builder()
                .id("user1")
                .username("john.doe")
                .email("john@example.com")
                .status("active")
                .createdAt(LocalDateTime.now().minusDays(30))
                .build());

        users.put("user2", UserDto.builder()
                .id("user2")
                .username("jane.smith")
                .email("jane@example.com")
                .status("active")
                .createdAt(LocalDateTime.now().minusDays(15))
                .build());
    }

    private long countActiveUsers() {
        return users.values().stream()
                .filter(user -> "active".equals(user.getStatus()))
                .count();
    }
}