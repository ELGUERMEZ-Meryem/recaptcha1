package com.example.api.service;

import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;

public interface IRecaptchaV3 {
    void processResponse(String response, String action) throws ReCaptchaInvalidException, InvalidReCaptchaException;
}
