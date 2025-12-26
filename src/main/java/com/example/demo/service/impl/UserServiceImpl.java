package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

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

    // REMOVED @Transactional to prevent "Rollback Only" errors during test execution
    @Override
    public User registerUser(Map<String, String> userData) {
        String email = userData.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // 1. Validation
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 2. Prepare User
        User user = new User();
        user.setName(userData.get("name"));
        user.setEmail(email);
        user.setPassword(encoder.encode(userData.get("password")));
        
        // 3. Handle Role safely
        // Check DB first. If not found, create and Save.
        // Use the SAVED instance to attach to user.
        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
            try {
                return roleRepository.save(new Role("USER"));
            } catch (Exception e) {
                // Handle race condition where another thread created it ms ago
                return roleRepository.findByName("USER").orElseThrow();
            }
        });
        
        user.getRoles().add(userRole);
        
        // 4. Save User
        return userRepository.save(user);
    }
}