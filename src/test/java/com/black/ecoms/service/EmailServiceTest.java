package com.black.ecoms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Email Service Tests")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set the email properties using reflection
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@ecoms.com");
        ReflectionTestUtils.setField(emailService, "subjectCode", "Your E-Commerce Password Reset Code");
    }

    @Test
    @DisplayName("Should send password reset code email successfully")
    void testSendPasswordResetCodeSuccess() {
        // Arrange
        String toEmail = "user@example.com";
        String code = "123456";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendPasswordResetCode(toEmail, code);

        // Assert
        assertTrue(result, "Email should be sent successfully");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should return false when email sending fails")
    void testSendPasswordResetCodeFailure() {
        // Arrange
        String toEmail = "user@example.com";
        String code = "123456";

        doThrow(new RuntimeException("SMTP connection failed")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendPasswordResetCode(toEmail, code);

        // Assert
        assertFalse(result, "Email sending should fail");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send generic email successfully")
    void testSendEmailSuccess() {
        // Arrange
        String toEmail = "user@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendEmail(toEmail, subject, body);

        // Assert
        assertTrue(result, "Email should be sent successfully");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should return false when generic email fails")
    void testSendEmailFailure() {
        // Arrange
        String toEmail = "user@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        doThrow(new RuntimeException("Network error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendEmail(toEmail, subject, body);

        // Assert
        assertFalse(result, "Email should fail");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should include correct email details in password reset message")
    void testPasswordResetEmailContent() {
        // Arrange
        String toEmail = "user@example.com";
        String code = "123456";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendPasswordResetCode(toEmail, code);

        // Assert
        verify(mailSender, times(1)).send(argThat((SimpleMailMessage message) ->
                message.getTo()[0].equals(toEmail) &&
                message.getSubject().equals("Your E-Commerce Password Reset Code") &&
                message.getText().contains(code) &&
                message.getFrom().equals("noreply@ecoms.com")
        ));
    }

    @Test
    @DisplayName("Should handle null email address gracefully")
    void testSendPasswordResetCodeNullEmail() {
        // Arrange
        String code = "123456";
        doThrow(new NullPointerException()).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendPasswordResetCode(null, code);

        // Assert
        assertFalse(result, "Should return false for null email");
    }

    @Test
    @DisplayName("Should handle null code gracefully")
    void testSendPasswordResetCodeNullCode() {
        // Arrange
        String toEmail = "user@example.com";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        boolean result = emailService.sendPasswordResetCode(toEmail, null);

        // Assert
        assertTrue(result, "Should still send email with null code");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

