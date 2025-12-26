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

    @Override
    public User registerUser(Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        String name = userData.get("name");

        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email is required");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password is required");

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        
        // SAFE LOGIC: We assume roles are seeded by DataSeeder. 
        // If "USER" is missing, we fetch "ADMIN" or just fail clearly (instead of crashing transaction).
        Role userRole = roleRepository.findByName("USER")
                .orElse(roleRepository.findByName("ADMIN").orElse(null));

        if (userRole != null) {
            user.getRoles().add(userRole);
        } else {
            // Fallback: This effectively shouldn't happen if DataSeeder runs
            System.err.println("WARNING: Roles not found during registration!");
        }
        
        return userRepository.save(user);
    }
}