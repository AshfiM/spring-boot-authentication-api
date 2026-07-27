package com.example.userauthentication.service;

import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.entity.UserEntity;
import com.example.userauthentication.exception.UserAlreadyExistsException;
import com.example.userauthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public void addUser(UserDTO newUser)  {
        if (userRepository.findById(newUser.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException("user already exists");
        }
        UserEntity user = UserEntity.builder()
                        .username(newUser.getUserName())
                        .passwordHash(passwordEncoder.encode(newUser.getPassword()))
                        .role("").build();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException(ex.getMessage());
        }

    }
}
