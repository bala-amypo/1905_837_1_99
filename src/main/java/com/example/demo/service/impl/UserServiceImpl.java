package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;

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

    @Override
    @Transactional
    public User registerUser(Map<String, String> userData) {
        String email = userData.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(userData.get("name"));
        user.setEmail(email);
        user.setPassword(encoder.encode(userData.get("password")));
        
        // --- ROBUST ROLE HANDLING ---
        // 1. Try to find the role
        Optional<Role> roleOpt = roleRepository.findByName("USER");
        Role userRole;
        
        if (roleOpt.isPresent()) {
            userRole = roleOpt.get();
        } else {
            // 2. If not found, try to save. If save fails (race condition), try find again.
            try {
                userRole = roleRepository.saveAndFlush(new Role("USER"));
            } catch (Exception e) {
                // If it crashed, it means the role was created ms ago by another thread/test
                userRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Role USER could not be created or found"));
            }
        }
        
        user.getRoles().add(userRole);
        return userRepository.saveAndFlush(user);
    }
}