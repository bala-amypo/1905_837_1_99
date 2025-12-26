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

    @Override
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

        // Safe Role Logic (Assumes DataSeeder ran, falls back safely)
        Role userRole = roleRepository.findByName("USER")
                .orElse(roleRepository.findByName("ADMIN").orElse(null));

        if (userRole == null) {
            // Emergency fallback if Seeder failed
             try { userRole = roleRepository.save(new Role("USER")); } catch (Exception e) {}
        }
        
        if (userRole != null) user.getRoles().add(userRole);

        return userRepository.save(user);
    }
}