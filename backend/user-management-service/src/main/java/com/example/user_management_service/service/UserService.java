package com.example.user_management_service.service;

import com.example.user_management_service.models.User;

public interface  UserService {

    User findByUsername(String username);
    User saveUser(User user);
    
}
