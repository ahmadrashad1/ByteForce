package com.SportSync2.controller;

import com.SportSync2.dto.LoginDto;
import com.SportSync2.dto.PasswordResetResponseDto;
import com.SportSync2.dto.RegisterResponseDto;
import com.SportSync2.dto.ResetPasswordRequest;
import com.SportSync2.entity.User;
import com.SportSync2.entity.VerificationToken;
import com.SportSync2.repository.UserRepository;
import com.SportSync2.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.SportSync2.repository.UserRepository;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid LoginDto loginDto) {
        RegisterResponseDto result = authService.registerUser(loginDto.getEmail(), loginDto.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", result.getMessage());
        if (result.getToken() != null) {
            response.put("token", result.getToken());
        }

        return ResponseEntity.ok(response);
    }



    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        String result = authService.verifyUser(token);

        Map<String, Object> response = new HashMap<>();
        response.put("message", result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());
        String result = authService.loginUser(loginDto.getEmail(), loginDto.getPassword(), user);

        Map<String, Object> response = new HashMap<>();
        if (result.equals("Invalid email or password.") || result.equals("Please verify your email before logging in.")) {
            response.put("error", result);
            return ResponseEntity.status(401).body(response);
        }

        response.put("message", result);
        response.put("userId", user.getId());


        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody @Valid Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Email is required");
            return ResponseEntity.badRequest().body(error);
        }

        PasswordResetResponseDto responseDto = authService.requestPasswordReset(email);
        Map<String, Object> response = new HashMap<>();
        response.put("message", responseDto.getMessage());

        if (responseDto.getToken() != null) {
            response.put("token", responseDto.getToken());  // helpful for testing
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String result = authService.resetPassword(request.getToken(), request.getNewPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", result);

        return ResponseEntity.ok(response);
    }



}


