package com.example.userauthentication.service;

import com.example.userauthentication.entity.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void setRole(String username, String role) {
        Optional<UserEntity> user = userRepository.findById(username);
        if (user.isEmpty()) throw new BadCredentialsException("Invalid username");
        user.get().setRole(role);
        userRepository.save(user.get());
    }
}
