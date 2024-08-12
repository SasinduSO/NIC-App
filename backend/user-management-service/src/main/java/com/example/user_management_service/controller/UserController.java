package com.example.user_management_service.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/status")
    public String getStatusFromManagementService() {
        return "API is working";
    }
}