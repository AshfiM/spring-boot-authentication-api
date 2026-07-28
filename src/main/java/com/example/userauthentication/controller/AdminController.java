package com.example.userauthentication.controller;

import com.example.userauthentication.dto.SetRole;
import com.example.userauthentication.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/report")
    public ResponseEntity<String> showReport(){
        return ResponseEntity.ok("admin report");
    }

    @PostMapping("/set-role")
    public ResponseEntity<String> setRole(@RequestBody SetRole roll) {
        adminService.setRole(roll.getUsername(), roll.getRole());
        return ResponseEntity.ok("roll set");
    }
}
