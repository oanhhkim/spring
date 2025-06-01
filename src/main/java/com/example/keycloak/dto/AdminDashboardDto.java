package com.example.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    private String adminUser;
    private Map<String, Object> statistics;
    private Map<String, String> systemStatus;
    private LocalDateTime timestamp;
}