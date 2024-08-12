package com.example.user_management_service.service.implementation;


import com.example.user_management_service.models.User;
import com.example.user_management_service.repository.userRep;
import com.example.user_management_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServImpl implements UserService {

    @Autowired
    private userRep userRepository;

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
}
