package com.SportSync2.service;

import com.SportSync2.entity.User;
import com.SportSync2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Email already in use";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(false);
        userRepository.save(user);
        return "Registration successful. Please verify your email.";
    }

    public String loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            if (!user.get().isVerified()) {
                return "Please verify your email before logging in.";
            }
            return "Login successful!";
        }
        return "Invalid email or password.";
    }
}
