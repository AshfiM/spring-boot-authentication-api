package com.example.userauthentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login",
            consumes = "application/json")
    public ResponseEntity<String> getUser(@RequestBody UserDTO userDTO){
        try {
            String user = userService.checkUserExist(userDTO);
             return ResponseEntity.ok(user);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }


    @PostMapping(value = "/signup",
        consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> signUp(@RequestBody UserDTO newUser) {
        try {
            UserEntity userEntity = userService.addUser(newUser);
            UserDTO userDTO = new UserDTO();
            userDTO.setUserName(userEntity.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

}
