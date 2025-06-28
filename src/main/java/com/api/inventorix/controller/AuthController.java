package com.api.inventorix.controller;

import com.api.inventorix.model.Admin;
import com.api.inventorix.service.AuthService;
import com.api.inventorix.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> req) {
        try {
            String email = req.get("email");
            String username = req.get("username");
            String password = req.get("password");

            Admin admin = authService.signup(email, username, password);

            return ResponseEntity.ok(Map.of("message", "Signup successful", "adminId", admin.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        return authService.login(email, password)
                .map(admin -> {
                    String token = jwtUtil.generateToken(admin.getEmail());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid email or password")));
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                return ResponseEntity.ok(Map.of("status", "valid", "email", email));
            }
        }
        return ResponseEntity.status(401).body(Map.of("status", "invalid or expired"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Untuk JWT, logout dilakukan client-side (hapus token).
        return ResponseEntity.ok(Map.of("message", "Logout successful — silakan hapus token di client"));
    }

}