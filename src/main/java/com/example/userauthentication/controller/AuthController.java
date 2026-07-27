package com.example.userauthentication.controller;


import com.example.userauthentication.service.UserService;
import com.example.userauthentication.utility.JwtUtility;
import com.example.userauthentication.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController(JwtUtility jwtUtility, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> signup(@RequestBody UserDTO newUser) {
        userService.addUser(newUser);
        Map<String, String> info = Map.of(
                "msg", "user created",
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(info);
    }
//  if token in header
//    @PostMapping(value = "/login",
//    consumes = "application/json")
//    public ResponseEntity<?> login(@RequestBody UserDTO user) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//
//        String token = jwtUtility.generateToken(user.getUserName());
//        return ResponseEntity.ok(token);
//
//    }

    @PostMapping(value = "/login",
    consumes = "application/json")
   public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
        String token = jwtUtility.generateToken(user.getUserName(), role);
        ResponseCookie responseCookie = ResponseCookie.from("JWT",token)
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .build();
        Map<String, String> info = Map.of(
                "msg", "login successful",
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(info);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok("logout successful");
    }
}
