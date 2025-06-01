package com.example.keycloak.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpoint_shouldBeAccessible_withoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void protectedEndpoint_shouldReturn401_withoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void userEndpoint_shouldBeAccessible_withUserRole() throws Exception {
        mockMvc.perform(get("/api/user/profile")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("email", "testuser@example.com")
                                .claim("given_name", "Test")
                                .claim("family_name", "User")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void adminEndpoint_shouldReturn403_withUserRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_shouldBeAccessible_withAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testadmin")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adminUser").value("testadmin"));
    }

    @Test
    void superAdminEndpoint_shouldReturn403_withAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/logs")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testadmin")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN")))
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    void superAdminEndpoint_shouldBeAccessible_withSuperAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/logs")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "superadmin")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("SUPER_ADMIN", "ADMIN", "USER")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void postEndpoint_shouldWorkWithCorrectRole() throws Exception {
        mockMvc.perform(post("/api/user/preferences")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        ))
                        .contentType("application/json")
                        .content("{\"theme\":\"light\",\"language\":\"es\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void deleteEndpoint_shouldWorkForAdmin() throws Exception {
        mockMvc.perform(delete("/api/admin/users/user1")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testadmin")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
}