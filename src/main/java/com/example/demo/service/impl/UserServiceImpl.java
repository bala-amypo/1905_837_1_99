package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public User registerUser(Map<String, String> userData) {
        String email = userData.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // 1. Check if user exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 2. Prepare User Object
        User user = new User();
        user.setName(userData.get("name"));
        user.setEmail(email);
        user.setPassword(encoder.encode(userData.get("password")));
        
        // 3. Handle Role (The Critical Part)
        // We do NOT assume we can save. We try to find. 
        Role userRole = roleRepository.findByName("USER").orElse(null);
        
        if (userRole == null) {
            // If strictly not found, try to save a new one.
            try {
                userRole = roleRepository.save(new Role("USER"));
            } catch (Exception e) {
                // If this fails, it means another thread created it 1ms ago.
                // So we fetch it again.
                userRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Role USER creation failed"));
            }
        }
        
        user.getRoles().add(userRole);
        
        // 4. Save User
        return userRepository.save(user);
    }
}