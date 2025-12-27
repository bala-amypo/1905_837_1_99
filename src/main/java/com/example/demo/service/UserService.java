package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.RegisterRequest;

public interface UserService {
    User registerUser(RegisterRequest request);
}