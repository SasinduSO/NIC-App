package com.example.user_management_service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$CFHZCjf7XhGxY.gBpuiHY.fnQcd7cBE1jbFxCFe3gq1TI7wPyEQNG";

        // Check if the raw password matches the encoded password
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("Password matches: " + matches);

        // Encode the raw password and print the result
        String newEncodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + newEncodedPassword);
    }
}


