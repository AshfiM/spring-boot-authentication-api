package com.example.userauthentication.exception;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> userAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> log = Map.of(
                "msg", ex.getMessage(),
                "timestamp", Instant.now().toString()

        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(log);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> badCredentialsException(Exception ex) {
        Map<String, String> log = Map.of(
                "msg", "invalid username or password",
                "timestamp", Instant.now().toString()

        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(log);
    }

}
