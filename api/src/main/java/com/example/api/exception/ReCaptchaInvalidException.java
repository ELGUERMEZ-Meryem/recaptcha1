package com.example.api.exception;

/**
 * ReCaptcha invalid exception.
 */
public class ReCaptchaInvalidException extends Exception {
    public ReCaptchaInvalidException(String message) {
        super(message);
    }
}
