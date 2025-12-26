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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("status", "Success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        System.out.println("LOGIN ATTEMPT: " + req.getEmail()); // Debug Log
        try {
            // 1. Check Credentials
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Fetch User & Roles
            User user = userRepo.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("User found in Auth but not in Repo?"));
            
            Set<String> roles = user.getRoles().stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toSet());

            // 3. Generate Token
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roles);
            
            System.out.println("LOGIN SUCCESS: " + user.getEmail()); // Debug Log
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), roles));

        } catch (BadCredentialsException e) {
            System.err.println("LOGIN FAILED: Bad Credentials for " + req.getEmail());
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Incorrect email or password"));
        } catch (Exception e) {
            // This is the important part - if it crashes, you will see WHY now.
            System.err.println("LOGIN CRASHED: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}