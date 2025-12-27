package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest req) {
        // The test expects a map containing the email to verify registration
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(req.getPassword());
        u.setName(req.getEmail()); // Fallback name
        
        userService.registerUser(u);
        
        // Return simple map as expected by test70_registerEndpoint
        return Map.of(
            "email", u.getEmail(),
            "id", u.getId(),
            "message", "User registered successfully"
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        User u = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        Set<String> roles = new HashSet<>();
        u.getRoles().forEach(r -> roles.add(r.getName()));
        
        String token = jwtUtil.generateToken(u.getEmail(), u.getId(), roles);
        return new AuthResponse(token, u.getEmail(), u.getId());
    }
}