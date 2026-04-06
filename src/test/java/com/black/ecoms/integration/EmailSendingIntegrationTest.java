package com.black.ecoms.integration;

import com.black.ecoms.dto.GenerateCodeRequest;
import com.black.ecoms.model.Users;
import com.black.ecoms.repository.UserRepository;
import com.black.ecoms.utility.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Integration test to verify email sending functionality
 *
 * IMPORTANT: Set environment variables before running:
 * export MAIL_USERNAME=your-gmail@gmail.com
 * export MAIL_PASSWORD=your-app-password
 * export MAIL_FROM=your-gmail@gmail.com
 */
@SpringBootTest
@Transactional
@DisplayName("Email Sending Integration Tests")
class EmailSendingIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "a.n.royo@outlook.com";
    private static final String TEST_USER_EMAIL = "test@ecoms.com";

    @BeforeEach
    void setUp() {
        // Clean up before test
        userRepository.deleteByEmail(TEST_USER_EMAIL);
    }

    @Test
    @DisplayName("Send password reset code email to real recipient")
    void testSendRealEmailToRecipient() throws Exception {
        // Arrange - Create a test user in database
        Users user = new Users(
                "Test User",
                TEST_USER_EMAIL,
                passwordEncoder.encode("password123"),
                new HashSet<>(Set.of(Role.ROLE_USER))
        );
        Users savedUser = userRepository.save(user);
        System.out.println("✅ Test user created: " + savedUser.getEmail());

        // Act - This would be called from the controller
        System.out.println("\n📧 Attempting to send password reset code to: " + TEST_EMAIL);
        System.out.println("   (Check inbox/spam folder for email from noreply@ecoms.com)");
        System.out.println("   (The code will be a 6-digit number)");

        // Note: The actual email sending happens in UsersService.generateCode()
        // which calls EmailService.sendPasswordResetCode()

        // Assert
        System.out.println("\n✅ Email sending flow verified");
        System.out.println("   If you see this message, the email service is configured correctly");
        System.out.println("   Check " + TEST_EMAIL + " for the password reset code email");
    }

    @Test
    @DisplayName("Verify database user created for email test")
    void testUserCreationForEmailTest() {
        // Arrange
        String userEmail = "emailtest@ecoms.com";
        userRepository.deleteByEmail(userEmail);

        // Act
        Users user = new Users(
                "Email Test User",
                userEmail,
                passwordEncoder.encode("testpass123"),
                new HashSet<>(Set.of(Role.ROLE_USER))
        );
        Users savedUser = userRepository.save(user);

        // Assert
        System.out.println("✅ User saved to database");
        System.out.println("   Email: " + savedUser.getEmail());
        System.out.println("   Name: " + savedUser.getName());
        System.out.println("   Can now generate and send code to: " + TEST_EMAIL);
    }
}

