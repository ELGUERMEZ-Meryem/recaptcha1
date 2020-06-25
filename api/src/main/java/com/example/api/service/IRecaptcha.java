package com.example.api.service;

import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;

public interface IRecaptcha {
    void processResponse(String response) throws ReCaptchaInvalidException, InvalidReCaptchaException;
}
