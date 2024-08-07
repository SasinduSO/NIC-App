package com.example.nic_validation_service.exception;

public class InvalidNicException extends RuntimeException{
    public InvalidNicException(String message){
        super(message); //alling super constructir
    }
    
}
