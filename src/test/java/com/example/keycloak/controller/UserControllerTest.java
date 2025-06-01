package com.example.keycloak.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.keycloak.config.SecurityConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    private static final String USER_JWT_CLAIMS = """
            {
                "preferred_username": "testuser",
                "email": "testuser@example.com",
                "given_name": "Test",
                "family_name": "User",
                "realm_access": {
                    "roles": ["USER"]
                }
            }
            """;

    @Test
    void getUserProfile_withValidToken_shouldReturnProfile() throws Exception {
        mockMvc.perform(get("/api/user/profile")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("email", "testuser@example.com")
                                .claim("given_name", "Test")
                                .claim("family_name", "User")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void getUserProfile_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserData_withValidToken_shouldReturnData() throws Exception {
        mockMvc.perform(get("/api/user/data")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.data.orders").value(5));
    }

    @Test
    void updatePreferences_withValidData_shouldReturnSuccess() throws Exception {
        String preferencesJson = """
                {
                    "theme": "dark",
                    "language": "en",
                    "notifications": true
                }
                """;

        mockMvc.perform(post("/api/user/preferences")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testuser")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                        ))
                        .contentType("application/json")
                        .content(preferencesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.updatedPreferences.theme").value("dark"));
    }

    @Test
    void userEndpoint_withAdminRole_shouldAlsoHaveAccess() throws Exception {
        mockMvc.perform(get("/api/user/profile")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("preferred_username", "testadmin")
                                .claim("email", "testadmin@example.com")
                                .claim("given_name", "Test")
                                .claim("family_name", "Admin")
                                .claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN", "USER")))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testadmin"));
    }
}