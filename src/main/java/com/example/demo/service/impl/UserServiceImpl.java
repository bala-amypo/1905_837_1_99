package com.example.demo.service.impl;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    // REMOVE @Override if interface signature doesn't match, or update Interface.
    public User registerUser(RegisterRequest req) {
        if (req.getEmail() == null || req.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));

        // --- DYNAMIC ROLE LOGIC ---
        // 1. Get role name from request, default to "USER" if empty
        String roleName = (req.getRole() != null && !req.getRole().isEmpty()) 
                          ? req.getRole().toUpperCase() 
                          : "USER";

        // 2. Find or Create Role
        Role userRole = roleRepository.findByName(roleName).orElse(null);
        if (userRole == null) {
            try {
                userRole = roleRepository.save(new Role(roleName));
            } catch (Exception e) {
                // Handle race condition
                userRole = roleRepository.findByName(roleName).orElseThrow();
            }
        }
        
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }
}