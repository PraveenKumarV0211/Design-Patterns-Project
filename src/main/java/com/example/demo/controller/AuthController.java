package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = authService.register(dto);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        User user = authService.login(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }
}
