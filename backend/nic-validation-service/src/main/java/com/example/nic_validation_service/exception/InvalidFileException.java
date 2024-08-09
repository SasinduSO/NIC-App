package com.example.nic_validation_service.exception;

public class InvalidFileException extends RuntimeException{
    public InvalidFileException(String message){
        super(message); //alling super constructir
    }
}
