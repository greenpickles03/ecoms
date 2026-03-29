package com.black.ecoms.controller;

import com.black.ecoms.dto.LoginRequest;
import com.black.ecoms.dto.UserRegistrationRequest;
import com.black.ecoms.service.UserDetailsServiceImpl;
import com.black.ecoms.service.UsersService;
import com.black.ecoms.utility.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public UsersController(UsersService usersService,
                           AuthenticationManager authenticationManager,
                           UserDetailsServiceImpl userDetailsService,
                           JwtUtil jwtUtil) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUsers(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(usersService.createUsers(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", userDetails.getUsername(),
                "roles", userDetails.getAuthorities()
        ));
    }
}
