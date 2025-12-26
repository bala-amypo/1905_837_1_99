package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.RegisterRequest; // Import the new DTO
import java.util.Map;

public interface UserService {
    // Change parameter from Map to RegisterRequest
    User registerUser(RegisterRequest request); 
}