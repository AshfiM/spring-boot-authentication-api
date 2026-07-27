package com.example.userauthentication.service;

import com.example.userauthentication.entity.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Optional<UserEntity> user = userRepository.findById(userName);
        if (user.isEmpty()) throw new BadCredentialsException("invalid username or password");
        return user.get();
    }
}
