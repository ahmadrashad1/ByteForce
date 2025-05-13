package com.SportSync2.service;

import com.SportSync2.dto.PasswordResetResponseDto;
import com.SportSync2.dto.RegisterResponseDto;
import com.SportSync2.entity.User;
import com.SportSync2.entity.VerificationToken;
import com.SportSync2.repository.UserRepository;
import com.SportSync2.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static com.SportSync2.controller.UserController.logger;

@Service
public class AuthService {
    @Autowired
    private EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(EmailService emailService) {
        this.emailService = emailService;
    }

    public RegisterResponseDto registerUser(String email, String password) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return new RegisterResponseDto("Email already in use", null);
        }

        // Save user with 'unverified' status
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(true);
        userRepository.save(user);


        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        tokenRepository.save(verificationToken);

        // Send verification email
        sendVerificationEmail(email, token);

        return new RegisterResponseDto("Registration successful. Please verify your email.", token);
    }

    public void sendVerificationEmail(String to, String token) {
        String subject = "Email Verification";
        String verificationLink = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Click the link to verify your email: " + verificationLink);

        try {
            mailSender.send(message);
            System.out.println("Verification email sent successfully to: " + to);
        } catch (MailException e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public String verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);

        if (verificationToken == null || verificationToken.isUsed()) {
            return "Invalid or expired verification link.";
        }

        // Activate user account
        User user = userRepository.findByEmail(verificationToken.getEmail());
        if (user == null) {
            return "User not found.";
        }
        user.setVerified(true);
        userRepository.save(user);

        // Mark token as used
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        return "Account verified successfully.";
    }

    public String loginUser(String email, String password, User user) {

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            if (!user.isVerified()) {
                return "Please verify your email before logging in.";
            }
            return "Login successful";
        }
        return "Invalid email or password.";
    }

    public PasswordResetResponseDto requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new PasswordResetResponseDto("User not found.", null);
        }

        String token = UUID.randomUUID().toString();
        VerificationToken resetToken = new VerificationToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        sendResetPasswordEmail(email, token);

        return new PasswordResetResponseDto("Password reset email sent.", token);
    }


    private void sendResetPasswordEmail(String email, String token) {
        String url = "http://localhost:8080/auth/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("Click the link to reset your password: " + url);

        try {
            mailSender.send(message);
            System.out.println("Reset password email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send reset password email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Reset password
    public String resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);

        if (verificationToken == null || verificationToken.isUsed()) {
            return "Invalid or expired token.";
        }

        // Get user and update password
        User user = userRepository.findByEmail(verificationToken.getEmail());
        if (user == null) {
            return "User not found.";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        return "Password reset successfully.";
    }
}
