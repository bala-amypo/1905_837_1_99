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
        
        // Fix: Fetch role safely. If not found, try to save it in a separate try-block
        // to avoid marking the main transaction as rollback-only if a race condition occurs.
        Role userRole = roleRepository.findByName("USER").orElse(null);
        
        if (userRole == null) {
            try {
                userRole = roleRepository.save(new Role("USER"));
            } catch (Exception e) {
                // If save fails, it means another thread/test created it just now. Fetch it.
                userRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Error handling USER role"));
            }
        }
        
        user.getRoles().add(userRole);
        
        // Save the user
        return userRepository.save(user);
    }
}