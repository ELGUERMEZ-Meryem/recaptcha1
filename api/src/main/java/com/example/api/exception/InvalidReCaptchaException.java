package com.example.api.exception;

/**
 * Invalid reCaptcha exception.
 */
public class InvalidReCaptchaException extends Exception {
    public InvalidReCaptchaException(String message) {
        super(message);
    }
}
