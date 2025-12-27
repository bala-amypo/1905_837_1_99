package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.RegisterRequest; // Import the new DTO
import java.util.Map;

public interface UserService {
    
    void registerUser(RegisterRequest request);
}

