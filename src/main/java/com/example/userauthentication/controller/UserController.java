package com.example.userauthentication.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {


    @GetMapping("/show")
    public ResponseEntity<String> showUsers() {
        return ResponseEntity.ok("automatically authenticated");
    }


}
