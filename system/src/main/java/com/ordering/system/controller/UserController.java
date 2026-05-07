package com.ordering.system.controller;

import com.ordering.system.dto.RegisterRequest;
import com.ordering.system.entity.User;
import com.ordering.system.repository.UserRepository;
import com.ordering.system.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public UserController(AuthService authService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    // Thymeleaf page
    @GetMapping("/user-management")
    public String userManagementPage() {
        return "user-management";
    }

    // REST - Get all users
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // REST - Register new user
    @PostMapping("/api/users/register")
    @ResponseBody
    public ResponseEntity<Map<String, String>> register(
            @RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            authService.register(request);
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // REST - Delete user
    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // REST - Update user role
    @PutMapping("/api/users/{id}/role")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(body.get("role"));
            userRepository.save(user);
            response.put("message", "Role updated successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // REST - Change password
    @PutMapping("/api/users/{id}/password")
    @ResponseBody
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
                encoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            user.setPassword(encoder.encode(body.get("password")));
            userRepository.save(user);
            response.put("message", "Password changed successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}