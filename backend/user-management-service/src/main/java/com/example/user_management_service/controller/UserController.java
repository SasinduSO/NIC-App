package com.example.user_management_service.controller;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_management_service.exception.UserNotFoundException;
import com.example.user_management_service.models.User;
import com.example.user_management_service.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }
            System.out.println("Login email : "+ loginRequest.getPassword());
            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            if (!matches) {
                return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("null")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            if (!isValidEmail(user.getEmail())) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            String resetToken = UUID.randomUUID().toString();
            userService.saveResetToken(email, resetToken);

            String resetLink = "http://localhost:8082/api/users/reset-password?token=" + resetToken;
            sendResetEmail(email, resetLink);

            return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in forgotPassword method", e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sasindu22so@outlook.com");
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please click the following link: " + resetLink);
        emailSender.send(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody User UpdatUser) {
        try {
            User user = userService.findByResetToken(token);
            if (user == null) {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
            }
            String newPassword= UpdatUser.getPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            userService.updatePassword(user.getEmail(), encodedPassword);

            boolean matches = passwordEncoder.matches(newPassword, encodedPassword);
            System.out.println("Password matches: " + matches);
            System.out.println("new assword:" + newPassword);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
