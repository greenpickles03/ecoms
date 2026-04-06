package com.black.ecoms.controller;

import com.black.ecoms.dto.AuthResponse;
import com.black.ecoms.dto.LoginRequest;
import com.black.ecoms.dto.LoginResponse;
import com.black.ecoms.dto.RegisterRequest;
import com.black.ecoms.model.Users;
import com.black.ecoms.repository.UserRepository;
import com.black.ecoms.utility.JwtUtil;
import com.black.ecoms.utility.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already registered"));
        }

        Users user = new Users(
                request.getFirstName() + " " + request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                new HashSet<>(Set.of(Role.ROLE_USER))
        );

        userRepository.save(user);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getEmail(), roleNames);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getEmail(), String.join(",", roleNames)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Optional<Users> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Users user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getEmail(), roleNames);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        if (jwtUtil.isTokenValid(token)) {
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "email", jwtUtil.extractUsername(token)
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", "Token is invalid or expired"));
    }
}
