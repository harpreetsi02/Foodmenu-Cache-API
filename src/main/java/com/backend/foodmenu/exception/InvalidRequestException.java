package com.backend.foodmenu.exception;

public class InvalidRequestException extends RuntimeException{

    public InvalidRequestException(String message){
        super(message);
    }
}
