package com.SportSync2.service;

import com.SportSync2.dto.PasswordResetResponseDto;
import com.SportSync2.dto.RegisterResponseDto;
import com.SportSync2.entity.User;
import com.SportSync2.entity.VerificationToken;
import com.SportSync2.repository.UserRepository;
import com.SportSync2.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailCaptor;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(emailService);
        authService = spy(authService);  // enable spying to mock internal calls like sendVerificationEmail
        authService.userRepository = userRepository;
        authService.tokenRepository = tokenRepository;
        authService.mailSender = mailSender;
    }

    @Test
    void registerUser_SuccessfulRegistration() {
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(null);

        doNothing().when(authService).sendVerificationEmail(eq(email), anyString());

        RegisterResponseDto response = authService.registerUser(email, password);

        assertEquals("Registration successful. Please verify your email.", response.getMessage());
        assertNotNull(response.getToken());
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(VerificationToken.class));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        String email = "test@example.com";
        String password = "password123";
        when(userRepository.findByEmail(email)).thenReturn(new User());

        RegisterResponseDto response = authService.registerUser(email, password);

        assertEquals("Email already in use", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void verifyUser_ValidToken() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setUsed(false);

        User user = new User();
        user.setEmail(email);
        user.setVerified(false);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));
        when(userRepository.findByEmail(email)).thenReturn(user);

        String result = authService.verifyUser(token);

        assertEquals("Account verified successfully.", result);
        assertTrue(user.isVerified());
        verify(userRepository).save(user);
        assertTrue(verificationToken.isUsed());
    }

    @Test
    void verifyUser_InvalidToken() {
        String result = authService.verifyUser("invalid-token");
        assertEquals("Invalid or expired verification link.", result);
    }

    @Test
    void loginUser_SuccessfulLogin() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setVerified(true);

        String result = authService.loginUser(email, rawPassword, user);
        assertEquals("Login successful", result);
    }

    @Test
    void loginUser_NotVerified() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setVerified(false);

        String result = authService.loginUser(email, rawPassword, user);
        assertEquals("Please verify your email before logging in.", result);
    }

    @Test
    void loginUser_InvalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("correctPassword"));
        user.setVerified(true);

        String result = authService.loginUser("test@example.com", "wrongPassword", user);
        assertEquals("Invalid email or password.", result);
    }

    @Test
    void requestPasswordReset_ValidEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        PasswordResetResponseDto response = authService.requestPasswordReset(email);

        assertEquals("Password reset email sent.", response.getMessage());
        assertNotNull(response.getToken());
        verify(tokenRepository).save(any(VerificationToken.class));
    }

    @Test
    void requestPasswordReset_InvalidEmail() {
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        PasswordResetResponseDto response = authService.requestPasswordReset(email);

        assertEquals("User not found.", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void resetPassword_ValidToken() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setUsed(false);

        User user = new User();
        user.setEmail(email);
        user.setPassword("oldPassword");

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));
        when(userRepository.findByEmail(email)).thenReturn(user);

        String result = authService.resetPassword(token, newPassword);

        assertEquals("Password reset successfully.", result);
        assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
        assertTrue(verificationToken.isUsed());
    }

    @Test
    void resetPassword_InvalidToken() {
        String result = authService.resetPassword("invalid-token", "newPassword");
        assertEquals("Invalid or expired token.", result);
    }
}
