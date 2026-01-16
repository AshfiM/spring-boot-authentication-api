package com.example.userauthentication;

import org.springframework.beans.factory.annotation.Autowired;
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

    public String checkUserExist(UserDTO isUser) {

        Optional<UserEntity> userEntity = userRepository.findById(isUser.getUserName());
        if (userEntity.isPresent()) {
            if (passwordEncoder.matches(isUser.getPassword(), userEntity.get().getPasswordHash())) {
                return userEntity.get().getUsername();
            }
            else {
                throw new RuntimeException("invalid credentials");
            }
        }
        else {
            throw new RuntimeException("invalid Credentials");
        }

    }

    public UserEntity getByUserName(String userName){
        return userRepository.findById(userName)
                .orElseThrow(() -> new RuntimeException("user not exist"));
    }

    public UserEntity addUser(UserDTO newUser) {

        if (userRepository.findById(newUser.getUserName()).isPresent()) {
            throw new RuntimeException("user already exist");
        }

        UserEntity user = new UserEntity();
        user.setUsername(newUser.getUserName());
        user.setPasswordHash(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(user);
    }
}
