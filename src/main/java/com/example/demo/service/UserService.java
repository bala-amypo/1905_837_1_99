package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.RegisterRequest; 
import java.util.Map;

public interface UserService {
    
    void registerUser(RegisterRequest request);
}

