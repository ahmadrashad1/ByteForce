package com.SportSync2.controller;

import com.SportSync2.dto.UserDto;
import com.SportSync2.exception.UserAlreadyExistsException;
import com.SportSync2.repository.UserRepository;
import com.SportSync2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {



    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDTO) {
        logger.info("Received registration request for email: {}", userDTO.getEmail());

        try {
            userService.registerUser(userDTO);
            logger.info("User registered successfully: {}", userDTO.getEmail());
            return ResponseEntity.ok("User registered successfully! Please check your email to verify.");
        } catch (UserAlreadyExistsException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
