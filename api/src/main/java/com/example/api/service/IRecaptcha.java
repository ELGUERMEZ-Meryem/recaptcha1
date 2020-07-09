package com.example.api.service;

import com.example.api.exception.InvalidReCaptchaException;
import com.example.api.exception.ReCaptchaInvalidException;

public interface IRecaptcha {
    /**
     * Validate captcha response.
     *
     * @param response captcha response
     * @throws ReCaptchaInvalidException when reCaptcha was not successfully validated
     * @throws InvalidReCaptchaException when captcha response contains invalid characters
     */
    void processResponse(String response) throws ReCaptchaInvalidException, InvalidReCaptchaException;
}
