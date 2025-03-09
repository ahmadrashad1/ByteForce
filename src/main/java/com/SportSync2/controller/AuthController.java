package com.SportSync2.controller;

import com.SportSync2.dto.LoginDto;
import com.SportSync2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginDto loginDto) {
        String result = authService.registerUser(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String result = authService.loginUser(loginDto.getEmail(), loginDto.getPassword());

        if (result.startsWith("Invalid") || result.startsWith("Please verify")) {
            return ResponseEntity.status(401).body("{\"error\": \"" + result + "\"}");
        }

        return ResponseEntity.ok("{\"message\": \"Login successful\"}");
    }
}
