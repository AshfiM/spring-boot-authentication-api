package com.example.userauthentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtUtility jwtUtility, AuthenticationManager authenticationManager) {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
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
   public ResponseEntity<?> login(@RequestBody UserDTO user, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
        );
        String token = jwtUtility.generateToken(user.getUserName());
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        return ResponseEntity.ok("login successful");
    }

}
