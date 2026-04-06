package com.black.ecoms.service;

import com.black.ecoms.dto.ChangePasswordRequest;
import com.black.ecoms.dto.UserRegistrationRequest;
import com.black.ecoms.model.Users;
import com.black.ecoms.repository.UserRepository;
import com.black.ecoms.service.impl.UsersImp;
import com.black.ecoms.utility.Role;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService implements UsersImp {

    private final UserRepository userRepository;
    private final EmailService emailService;
    public String code;

    public UsersService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public Map<String, Object> createUsers(UserRegistrationRequest request) {
        Optional<Users> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            return Map.of(
                    "message", "Email already exists",
                    "details", existing.get(),
                    "status", 400
            );
        }

        Users user = new Users(
                request.getName(),
                request.getEmail(),
                hashPassword(request.getPassword()),
                new HashSet<>(Set.of(Role.ROLE_USER))
        );
        userRepository.save(user);

        return Map.of(
                "message", "User created successfully",
                "details", user,
                "status", 201
        );
    }

    public Map<String, Object> generateCode(String email) {
        // Check if user exists
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return Map.of("message", "User not found", "status", 404);
        }

        // Generate a random 6-digit code
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        code = String.valueOf(otp);

        // Send code via email
        boolean emailSent = emailService.sendPasswordResetCode(email, code);

        if (emailSent) {
            return Map.of(
                    "message", "Code generated and sent to your email successfully",
                    "status", 200
            );
        } else {
            return Map.of(
                    "message", "Code generated but failed to send email",
                    "status", 500
            );
        }
    }

    @Override
    public Map<String, Object> changePassword(ChangePasswordRequest request) {

        Optional<Users> usersList = userRepository.findByEmail(request.getEmail());
        if(usersList.isPresent()){
            if (usersList.get().getEmail().equals(request.getEmail()) && request.getRequestCode().equals(code)){
                Users user = usersList.get();
                user.setPassword(hashPassword(request.getNewPassword()));
                userRepository.save(user);
                return Map.of(
                        "message", "Password changed successfully",
                        "details", user,
                        "status", 200
                );
            } else {
                return Map.of(
                        "message", "Invalid code or email",
                        "status", 400
                );
            }
        }

        return Map.of(
                "message", "Account not exists",
                "details", request,
                "status", 400
        );
    }

    @Override
    public Map<String, Object> fetchAllUsers() {
        return Map.of(
                "message", "Users fetched successfully",
                "details", userRepository.findAll(),
                "status", 200
        );
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Password hashing algorithm unavailable", e);
        }
    }
}
