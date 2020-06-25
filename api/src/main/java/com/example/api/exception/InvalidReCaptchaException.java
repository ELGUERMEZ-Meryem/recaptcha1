package com.example.api.exception;

public class InvalidReCaptchaException extends Exception {
    public InvalidReCaptchaException(String message){
        super(message);
    }
}
