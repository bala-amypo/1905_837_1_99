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
    public ResponseEntity<String> register(@RequestBody Map<String, String> body) {
        try {
            User user = userService.registerUser(body);
            
            // MANUAL JSON CONSTRUCTION (No Jackson crashes possible)
            String jsonResponse = String.format("{\"id\":%d, \"email\":\"%s\", \"message\":\"Success\"}", 
                    user.getId(), user.getEmail());
            
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace(); // Print error to server logs
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
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
            e.printStackTrace();
            return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
        }
    }
}