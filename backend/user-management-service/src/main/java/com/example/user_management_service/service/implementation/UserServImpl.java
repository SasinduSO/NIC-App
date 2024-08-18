package com.example.user_management_service.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_management_service.models.User;
import com.example.user_management_service.repository.userRep;
import com.example.user_management_service.service.UserService;

@Service
public class UserServImpl implements UserService {

    @Autowired
    private userRep userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void saveResetToken(String email, String token) {
        User user = findByEmail(email);
        if (user != null) {
            user.setResetToken(token);
            saveUser(user);
        }
    }

    @Override
    public User findByResetToken(String token) {
        Optional<User> user = userRepository.findByResetToken(token);
        return user.orElse(null);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword); // Set the encoded password
            saveUser(user);
        }
    }
}
