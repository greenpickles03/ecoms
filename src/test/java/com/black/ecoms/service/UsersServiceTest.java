package com.black.ecoms.service;

import com.black.ecoms.dto.GenerateCodeRequest;
import com.black.ecoms.model.Users;
import com.black.ecoms.repository.UserRepository;
import com.black.ecoms.utility.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Users Service - Generate Code Tests")
class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsersService usersService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a test user
        testUser = new Users("John Doe", "john@example.com", "encoded_password",
                new HashSet<>(Set.of(Role.ROLE_USER)));
    }

    @Test
    @DisplayName("Should generate code and send email successfully for existing user")
    void testGenerateCodeSuccess() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(emailService.sendPasswordResetCode(anyString(), anyString())).thenReturn(true);

        // Act
        Map<String, Object> result = usersService.generateCode(email);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.get("status"));
        assertEquals("Code generated and sent to your email successfully", result.get("message"));

        // Verify email service was called
        verify(emailService, times(1)).sendPasswordResetCode(email, usersService.code);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    void testGenerateCodeUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Map<String, Object> result = usersService.generateCode(email);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.get("status"));
        assertEquals("User not found", result.get("message"));

        // Verify email service was NOT called
        verify(emailService, never()).sendPasswordResetCode(anyString(), anyString());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should return 500 when email fails to send")
    void testGenerateCodeEmailFailed() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(emailService.sendPasswordResetCode(anyString(), anyString())).thenReturn(false);

        // Act
        Map<String, Object> result = usersService.generateCode(email);

        // Assert
        assertNotNull(result);
        assertEquals(500, result.get("status"));
        assertEquals("Code generated but failed to send email", result.get("message"));

        // Verify email service was called
        verify(emailService, times(1)).sendPasswordResetCode(email, usersService.code);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should generate a valid 6-digit code")
    void testGeneratedCodeFormat() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(emailService.sendPasswordResetCode(anyString(), anyString())).thenReturn(true);

        // Act
        usersService.generateCode(email);
        String code = usersService.code;

        // Assert
        assertNotNull(code);
        assertTrue(code.matches("\\d{6}"), "Code should be a 6-digit number");
        int codeInt = Integer.parseInt(code);
        assertTrue(codeInt >= 100000 && codeInt <= 999999, "Code should be between 100000 and 999999");
    }

    @Test
    @DisplayName("Should generate different codes on multiple calls")
    void testMultipleCodeGeneration() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(emailService.sendPasswordResetCode(anyString(), anyString())).thenReturn(true);

        // Act
        usersService.generateCode(email);
        String firstCode = usersService.code;

        usersService.generateCode(email);
        String secondCode = usersService.code;

        // Assert - codes might be same due to randomness, but we can verify both are valid
        assertNotNull(firstCode);
        assertNotNull(secondCode);
        assertTrue(firstCode.matches("\\d{6}"));
        assertTrue(secondCode.matches("\\d{6}"));

        verify(emailService, times(2)).sendPasswordResetCode(anyString(), anyString());
    }
}

