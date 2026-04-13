package com.black.ecoms.controller;

import com.black.ecoms.dto.ChangePasswordRequest;
import com.black.ecoms.dto.GenerateCodeRequest;
import com.black.ecoms.dto.UserRegistrationRequest;
import com.black.ecoms.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUsers(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(usersService.createUsers(request));
    }

    @PostMapping("/generate-code")
    public ResponseEntity<Map<String, Object>> generateCode(@RequestBody GenerateCodeRequest request){
        System.out.println("Received email for code generation: " + request.getEmail());
        return ResponseEntity.ok(usersService.generateCode(request.getEmail()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody ChangePasswordRequest request){
        return ResponseEntity.ok(usersService.changePassword(request));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> fetchAllUsers(){
        return ResponseEntity.ok(usersService.fetchAllUsers());
    }
}
