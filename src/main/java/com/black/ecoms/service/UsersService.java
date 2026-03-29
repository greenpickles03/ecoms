package com.black.ecoms.service;

import com.black.ecoms.dto.UserRegistrationRequest;
import com.black.ecoms.model.Users;
import com.black.ecoms.repository.UserRepository;
import com.black.ecoms.service.impl.UsersImp;
import com.black.ecoms.utility.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService implements UsersImp {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                passwordEncoder.encode(request.getPassword()),
                new HashSet<>(Set.of(Role.ROLE_USER))
        );
        userRepository.save(user);

        return Map.of(
                "message", "User created successfully",
                "details", user,
                "status", 201
        );
    }
}
