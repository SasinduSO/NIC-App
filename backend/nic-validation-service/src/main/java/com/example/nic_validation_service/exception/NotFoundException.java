package com.example.nic_validation_service.exception;

public class NotFoundException extends RuntimeException{
    
    public NotFoundException(String message){
        super(message); //calls cnstructor of supper class
    }
    
}
