package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepo;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserService userService, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            // 1. Create User
            User user = userService.registerUser(body);

            // 2. Map to DTO (Prevents StackOverflow / JSON Errors)
            Set<String> roleNames = user.getRoles().stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toSet());

            UserResponseDto response = new UserResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    roleNames
            );
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepo.findByEmail(req.getEmail()).orElseThrow();
            Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roles);
            
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), roles));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }
}