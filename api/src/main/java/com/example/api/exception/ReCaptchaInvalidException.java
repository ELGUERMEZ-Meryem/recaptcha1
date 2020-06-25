package com.example.api.exception;

public class ReCaptchaInvalidException extends Exception {
    public ReCaptchaInvalidException(String message) {
        super(message);
    }
}
