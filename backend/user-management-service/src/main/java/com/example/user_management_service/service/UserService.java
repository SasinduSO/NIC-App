package com.example.user_management_service.service;

import com.example.user_management_service.models.User;

public interface  UserService {

    User findByEmail(String email);
    User saveUser(User user);
    void saveResetToken(String email, String token); 
    User findByResetToken(String token); 
    void updatePassword(String email, String newPassword);
    
}
