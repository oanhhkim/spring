package com.example.keycloak.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        log.error("Access denied: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = Map.of(
            "error", "Access Denied",
            "message", "You don't have permission to access this resource",
            "status", HttpStatus.FORBIDDEN.value(),
            "timestamp", LocalDateTime.now(),
            "path", request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        
        log.error("Authentication failed: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = Map.of(
            "error", "Authentication Failed",
            "message", "Invalid or expired token",
            "status", HttpStatus.UNAUTHORIZED.value(),
            "timestamp", LocalDateTime.now(),
            "path", request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = Map.of(
            "error", "Resource Not Found",
            "message", ex.getMessage(),
            "status", HttpStatus.NOT_FOUND.value(),
            "timestamp", LocalDateTime.now(),
            "path", request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error occurred", ex);
        
        Map<String, Object> errorResponse = Map.of(
            "error", "Internal Server Error",
            "message", "An unexpected error occurred",
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "timestamp", LocalDateTime.now(),
            "path", request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}