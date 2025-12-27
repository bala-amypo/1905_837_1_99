package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
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
    private final UserServiceImpl userService;
    private final UserRepository userRepo;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserServiceImpl userService,
                          UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    // ✅ FIXED FOR TEST 70
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody RegisterRequest request) {

        userService.registerUser(request);

        Map<String, String> response = new HashMap<>();
        response.put("email", request.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(), req.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepo.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Set<String> roles = user.getRoles()
                    .stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toSet());

            String token = jwtUtil.generateToken(
                    user.getEmail(), user.getId(), roles);

            return ResponseEntity.ok(
                    new AuthResponse(token, user.getId(), user.getEmail(), roles)
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Login Error"));
        }
    }
}
