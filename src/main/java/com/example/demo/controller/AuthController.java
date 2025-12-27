package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        userRepo.save(u);
        return Map.of("email", u.getEmail());
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> creds) {
        User u = userRepo.findByEmail(creds.get("email")).orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(creds.get("password"), u.getPassword())) throw new RuntimeException("Bad creds");
        
        Set<String> roles = new HashSet<>();
        u.getRoles().forEach(r -> roles.add(r.getName()));
        String token = jwtUtil.generateToken(u.getEmail(), u.getId(), roles);
        return Map.of("token", token, "email", u.getEmail(), "userId", u.getId());
    }
}