package com.example.demo.controller; 

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService; // Ensure correct import
import com.example.demo.service.impl.UserServiceImpl; // Or Use Impl directly
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

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserServiceImpl userService, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
    try {
        User user = userService.registerUser(body);
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Collections.singletonMap("error", e.getMessage()));
    }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            // 1. Authenticate (Triggers User.toString() internally in logs)
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Fetch User Details safely
            User user = userRepo.findByEmail(req.getEmail()).orElseThrow();
            Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
            
            // 3. Generate Token
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), roles);
            
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), roles));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception e) {
            // If it crashes, print WHY.
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Login Error: " + e.getMessage()));
        }
    }
}